package com.nova.game.wsk.handler;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import nova.common.game.wsk.data.CardData;
import nova.common.game.wsk.data.CardInfo;
import nova.common.game.wsk.data.GameInfo;
import nova.common.game.wsk.data.GameRoundInfo;
import nova.common.game.wsk.data.PlayerData;
import nova.common.game.wsk.data.PlayerInfo;
import nova.common.game.wsk.handler.FileLogRecorderManager;
import nova.common.game.wsk.handler.GameHandler;
import nova.common.game.wsk.util.CardUtil;

import com.nova.game.wsk.Constant;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.model.RoomInfoController;

public class LocalGameHandler implements GameHandler {
	private GameController mController;
	private String mStartGameTime;
	
	public LocalGameHandler() {
		mController = GameController.getInstance();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		mStartGameTime = df.format(new Date());// new Date()为获取当前系统时间
		FileLogRecorderManager.getInstance().startRecord();
		FileLogRecorderManager.getInstance().addMessage(0, mStartGameTime, "GAME START....");
	}
	
	@Override
	public void onRoomInfoChange(int roomId, HashMap<Integer, PlayerInfo> players) {
		int index = 0;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getId() == Constant.TEST_PLAYERID) {
				index = i;
			}
		}
		mController.setOwnerPlayerIndex(index);
		RoomInfoController.getInstance().setPlayerInfos(players);
	}
	

	@Override
	public void onGameInfoChange(int roomId, GameInfo gameInfo,
			GameRoundInfo roundInfo, HashMap<Integer, PlayerData> playerDatas,
			HashMap<Integer, ArrayList<CardData>> outcardDatas,
			ArrayList<CardData> lastDatas, ArrayList<CardData> outLastDatas) {
		mController.setGameInfo(gameInfo);
		updateControllerFromGameRoundInfo(roundInfo);
		// mController.setGameRoundInfo(roundInfo);
		mController.setPlayerDatas(playerDatas);
		mController.setOutCardDatas(outcardDatas);
		mController.setLastCardDatas(lastDatas);
		mController.setOutLastCardDatas(outLastDatas);
		
		SaveGameInfoToFile(roomId, gameInfo, roundInfo, playerDatas, outcardDatas, lastDatas, outLastDatas);
	}
	
	private void updateControllerFromGameRoundInfo (GameRoundInfo info) {
		mController.setCurrentPlayer(info.getCurrent());
		mController.setFirstPlayer(info.getFirst());
		mController.setLargePlayer(info.getLarger());
		mController.setSecStage(info.getSecStage());
		mController.setCurrentSecPlayer(info.getSecurrent());
		mController.setStage(info.getStage());
	}
	
	/*
	 * 保存游戏信息到文件
	 */
	private void SaveGameInfoToFile(int roomId, GameInfo gameInfo,
			GameRoundInfo roundInfo, HashMap<Integer, PlayerData> playerDatas,
			HashMap<Integer, ArrayList<CardData>> outcardDatas,
			ArrayList<CardData> lastDatas, ArrayList<CardData> outLastDatas) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String time = df.format(new Date());// new Date()为获取当前系统时间
		FileLogRecorderManager.getInstance().addMessage(roomId, mStartGameTime, time + " Game:" + gameInfo.toString() + "; Round:" + roundInfo.toString()
				+ "底牌:" + getMessageFromDatas(lastDatas)
				+ "扣底:" + getMessageFromDatas(outLastDatas));
		FileLogRecorderManager.getInstance().addMessage(roomId, mStartGameTime, time + " 玩家牌:" + getMessageFromPlayerDatas(playerDatas));
		android.util.Log.e("zhangxx", time + " Game:" + gameInfo.toString() + "; Round:" + roundInfo.toString()
				+ "Last:" + getMessageFromDatas(lastDatas)
				+ "OutLast:" + getMessageFromDatas(outLastDatas)
				+ "PlayerDatas:" + getMessageFromPlayerDatas(playerDatas));
	}
	
	private String getMessageFromDatas(ArrayList<CardData> datas) {
		String msg = "";
		for (CardData data : datas) {
			msg = msg + CardUtil.getCardName(data.getIndex()) + " ";
		}
		
		return msg;
	}
	
	private String getMessageFromPlayerDatas(HashMap<Integer, PlayerData> datas) {
		String msg = "";
		for (int i = 0; i < 4; i++) {
			msg = msg + i + "=";
			if (datas.get(i) == null) {
				continue;
			}
			
			for (CardData data : datas.get(i).getCardList()) {
				msg = msg + CardUtil.getCardName(data.getIndex()) + " ";
			}
		}
		return msg;
	}
}
