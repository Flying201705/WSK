package com.nova.game.wsk.handler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import nova.common.game.wsk.data.CardData;
import nova.common.game.wsk.data.CardInfo;
import nova.common.game.wsk.data.GameInfo;
import nova.common.game.wsk.data.GameRoundInfo;
import nova.common.game.wsk.data.PlayerData;
import nova.common.game.wsk.data.PlayerInfo;
import nova.common.game.wsk.util.GameCommand;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.model.RoomInfoController;

public class GameResponseDispatcher {
	
	public void processor(int commandId, String message) {
		android.util.Log.e("zhangxx", "commandID : " + commandId + ", message : " + message);
		switch (commandId) {
		case GameCommand.COM_ROOM_INFO:
		// 获取房间信息
			processorRoomInfo(message);
			break;
		case GameCommand.COM_ROOM_CREATE:
		// 创建房间
			processorCreateRoom(message);
			break;
		case GameCommand.COM_ROOM_JOIN:
		// 加入房间
			processorJoinRoom(message);
			break;
		case GameCommand.COM_GAME_START:
		// 开始游戏
			processorStartGame(message);
			break;
		case GameCommand.COM_GET_GAME_INFO:
			processorGameState(message);
			break;
		default:
			break;
		}
	}
	
	private void processorRoomInfo(String message) {
		try {
			JSONObject json = new JSONObject(message);
			int roomId = json.getInt("room");
			JSONArray players = json.getJSONArray("players");
			Type type = new TypeToken<ArrayList<PlayerInfo>>() {}.getType();
			Gson gson = new Gson();
			ArrayList<PlayerInfo> temp_infos = gson.fromJson(players.toString(), type);
			HashMap<Integer, PlayerInfo> infos = new HashMap<Integer, PlayerInfo>();
			for (int i = 0; i < 4; i++) {
				infos.put(i, temp_infos.get(i));
			}
			
			RoomInfoController.getInstance().setPlayerInfos(infos);
			
			String account = Gdx.app.getPreferences("LoginInfo").getString("account");
			int playerId = getPlayerId(account);
			for (int i = 0; i < 4; i++) {
				if (infos.get(i).getId() == playerId) {
					GameController.getInstance().setOwnerPlayerIndex(i);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void processorCreateRoom(String message) {
		int result = Integer.valueOf(message);
		android.util.Log.e("zhangxx", "create room complete , result : " + result);
		RoomInfoController.getInstance().onRoomCreateComplete(result);
	}
	
	private void processorJoinRoom(String message) {
		int result = Integer.valueOf(message);
		if (result >= 0) {
			android.util.Log.e("zhangxx", "join room successful , room : " + result);
		} else if (result == -1) {
			android.util.Log.e("zhangxx", "join room error , 房间为空!");
		} else if (result == -2) {
			android.util.Log.e("zhangxx", "join room error , 该房间已满!");
		} else if (result == -3) {
			android.util.Log.e("zhangxx", "join room error , 游戏已开始!");
		}
		RoomInfoController.getInstance().onRoomJoinComplete(result);
	}
	
	private void processorStartGame(String message) {
		int result = Integer.valueOf(message);
		RoomInfoController.getInstance().onRoomGameStartComplete(result);
	}
	
	private void processorGameState(String message) {
		try {
			JSONObject json = new JSONObject(message);
			JSONObject gameJson = json.getJSONObject("game");
			JSONObject roundJson = json.getJSONObject("round");
			JSONArray lastJson = json.getJSONArray("last");
			JSONArray outLastJson = json.getJSONArray("outlast");
			JSONArray playerDatasJson = json.getJSONArray("datas");
			processorGameInfo(gameJson.toString());
			processorGameRoundInfo(roundJson.toString());
			processorLastCard(lastJson.toString());
			processorOutLastCard(outLastJson.toString());
			initCardData(processorCardDataFromMsg(playerDatasJson.toString()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void initCardData(HashMap<Integer, PlayerData> datas) {
		GameController.getInstance().setPlayerDatas(datas);
	}
	
	private HashMap<Integer, PlayerData> processorCardDataFromMsg(String message) {
		HashMap<Integer, PlayerData> datas = new HashMap<Integer, PlayerData>();
		Type type = new TypeToken<ArrayList<CardInfo>>() {}.getType();
		Gson gson = new Gson();
		ArrayList<CardInfo> infos = gson.fromJson(message, type);
		for (int i = 0; i < infos.size(); i++) {
			CardInfo info = infos.get(i);
			ArrayList<CardData> cards = new ArrayList<CardData>();
			for (int index : info.getData()) {
				cards.add(new CardData(index, GameController.getInstance().getTrumpFace(), GameController.getInstance().getTrumpColor()));
			}
			datas.put(info.getId(), new PlayerData("", cards));
		}
		return datas;
	}
	
	private void processorGameRoundInfo(String message) {
		Type type = new TypeToken<GameRoundInfo>() {}.getType();
		Gson gson = new Gson();
		GameRoundInfo round = gson.fromJson(message, type);
		updateControllerFromRound(round);
	}
	
	private void processorGameInfo(String message) {
		Type type = new TypeToken<GameInfo>() {}.getType();
		Gson gson = new Gson();
		GameInfo info = gson.fromJson(message, type);
		GameController.getInstance().clearOutCardDatas();
		GameController.getInstance().setGameInfo(info);
	}
	
	private void processorLastCard(String message) {
		Type type = new TypeToken<ArrayList<CardData>>() {}.getType();
		Gson gson = new Gson();
		ArrayList<CardData> datas = gson.fromJson(message, type);
		GameController.getInstance().setLastCardDatas(datas);
	}
	
	private void processorOutLastCard(String message) {
		Type type = new TypeToken<ArrayList<CardData>>() {}.getType();
		Gson gson = new Gson();
		ArrayList<CardData> datas = gson.fromJson(message, type);
		GameController.getInstance().setOutLastCardDatas(datas);
	}
	
	private void updateControllerFromRound(GameRoundInfo info) {
		GameController.getInstance().setCurrentPlayer(info.getCurrent());
		GameController.getInstance().setFirstPlayer(info.getFirst());
		GameController.getInstance().setLargePlayer(info.getLarger());
		GameController.getInstance().setSecStage(info.getSecStage());
		GameController.getInstance().setCurrentSecPlayer(info.getSecurrent());
		GameController.getInstance().setStage(info.getStage());
		HashMap<Integer, ArrayList<CardData>> datas = new HashMap<Integer, ArrayList<CardData>>();
		for (CardInfo cardInfo : info.getInfo()) {
			ArrayList<CardData> data = new ArrayList<CardData>();
			for (Integer index : cardInfo.getData()) {
				data.add(new CardData(index, GameController.getInstance().getTrumpFace(), GameController.getInstance().getTrumpColor()));
			}
			datas.put(cardInfo.getId(), data);
		}
		GameController.getInstance().setOutCardDatas(datas);
	}
	
	public int getPlayerId(String account) {
		if (Constant.PLAYERS.containsKey(account)) {
			return Constant.PLAYERS.get(account).getId();
		}
		
		return Constant.TEST_PLAYERID;
	}
}
