package com.nova.game.wsk.model;

import java.util.ArrayList;

import nova.common.game.wsk.data.CardData;
import nova.common.game.wsk.data.PlayerInfo;
import nova.common.game.wsk.handler.GameDispatcher;
import nova.common.game.wsk.handler.GameLogger;
import nova.common.game.wsk.room.RoomController;
import nova.common.game.wsk.room.RoomManager;
import nova.common.game.wsk.util.GameCommand;

import com.nova.game.wsk.Constant;
import com.nova.game.wsk.Log;
import com.nova.game.wsk.handler.LocalGameHandler;


public class LocalGameManager extends WskGameManager {

	private GameDispatcher mDispatcher;
	
	@Override
	public void startGame() {
		super.startGame();
		PlayerInfo player = new PlayerInfo();
		player.setId(Constant.TEST_PLAYERID);
		player.setName("风口的飞猪");
		// 初始化LOG模块
		GameLogger.create(new Log());
		// FileLogRecorderManager.create();
		
		int roomId = RoomController.getInstance().joinRoom(player);
		RoomManager manager = RoomController.getInstance().getRoomManager(roomId);
		manager.setTestGameDelay(0);
		manager.setHandler(new LocalGameHandler());
		setDispatcher(manager.getGameManager());
		manager.startGame();
	}
	
	@Override
	public void stopGame() {
		super.stopGame();
	}
	
	@Override
	public void handleNotOutCard(int roomId, int playerId) {
		super.handleNotOutCard(roomId, playerId);
		mDispatcher.activeOutCard(playerId, new ArrayList<CardData>());
	}
	
	@Override
	public void handleOutCard(int roomId, int playerId, ArrayList<CardData> datas) {
		super.handleOutCard(roomId, playerId, datas);
		mDispatcher.activeOutCard(playerId, datas);
	}
	
	@Override
	public void handleOutLastCard(int roomId, int playerId, ArrayList<CardData> datas) {
		super.handleOutLastCard(roomId, playerId, datas);
		mDispatcher.activeOutLastCard(playerId, datas);
	}
	
	private void setDispatcher(GameDispatcher dispatcher) {
		mDispatcher = dispatcher;
	}
}
