package com.nova.game.wsk.handler;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nova.common.game.wsk.data.CardData;
import nova.common.game.wsk.util.GameCommand;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nova.game.net.ChannelManager;
import com.nova.game.wsk.Constant;

public class GameRequestDispatcher {
	
	private ChannelManager mChannel;
	
	public GameRequestDispatcher() {
		mChannel = ChannelManager.getInstance();
	}
	
	public void login() {
		
	}
	
	public void createRoom() {
		JsonObject json = new JsonObject();
		json.addProperty("com", GameCommand.COM_ROOM_CREATE);
		String account = Gdx.app.getPreferences("LoginInfo").getString("account");
		int playerId = getPlayerId(account);
		json.addProperty("player", playerId);
		mChannel.sendMessage(json.toString());
	}
	
	public void joinRoom(int room) {
		JsonObject json = new JsonObject();
		json.addProperty("com", GameCommand.COM_ROOM_JOIN);
		json.addProperty("room", String.valueOf(room));
		String account = Gdx.app.getPreferences("LoginInfo").getString("account");
		int playerId = getPlayerId(account);
		json.addProperty("player", playerId);
		mChannel.sendMessage(json.toString());
	}
	
	public void replacePlayerPosition(int room, int sourPosition, int tarPosition) {
		JsonObject json = new JsonObject();
		json.addProperty("com", GameCommand.COM_PLAYER_REPLACE);
		json.addProperty("room", String.valueOf(room));
		json.addProperty("sp", sourPosition);
		json.addProperty("tp", tarPosition);
		mChannel.sendMessage(json.toString());
	}

	public void getGameState(String account) {
		JSONObject json = new JSONObject();
		try {
			json.put("com", GameCommand.COM_GET_GAME_STATE);
			json.put("player", getPlayerId(account));
			mChannel.sendMessage(json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void initGameInfo() {
		JsonObject json = new JsonObject();
		json.addProperty("com", GameCommand.COM_GAME_INIT);
		String account = Gdx.app.getPreferences("LoginInfo").getString("account");
		int playerId = getPlayerId(account);
		json.addProperty("player", String.valueOf(playerId));
		mChannel.sendMessage(json.toString());
	}
	
	public void startGame(int room) {
		JsonObject json = new JsonObject();
		json.addProperty("com", GameCommand.COM_GAME_START);
		json.addProperty("room", String.valueOf(room));
		mChannel.sendMessage(json.toString());
	}
	
	public void notOutCard(int roomId, int playerId) {
		JSONObject json = new JSONObject();
		try {
			json.put("com", GameCommand.COM_OUT_PORKER);
			json.put("room", roomId);
			json.put("player", playerId);
			mChannel.sendMessage(json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void setOutCard(int roomId, int playerId, ArrayList<CardData> datas) {
		JSONObject json = new JSONObject();
		Gson gson = new Gson();
		String card = gson.toJson(datas);
		try {
			json.put("com", GameCommand.COM_OUT_PORKER);
			json.put("room", roomId);
			json.put("player", playerId);
			json.put("card", card);
			android.util.Log.e("zhangxx", "setOutCard : " + json.toString());
			mChannel.sendMessage(json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void setOutLastCard(int roomId, int playerId, ArrayList<CardData> datas) {
		JSONObject json = new JSONObject();
		Gson gson = new Gson();
		String card = gson.toJson(datas);
		try {
			json.put("com", GameCommand.COM_OUT_LAST_PORKER);
			json.put("room", roomId);
			json.put("player", playerId);
			json.put("card", card);
			android.util.Log.e("zhangxx", "setOutLastCard : " + json.toString());
			mChannel.sendMessage(json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public int getPlayerId(String account) {
		if (Constant.PLAYERS.containsKey(account)) {
			return Constant.PLAYERS.get(account).getId();
		}
		
		return Constant.TEST_PLAYERID;
	}
}
