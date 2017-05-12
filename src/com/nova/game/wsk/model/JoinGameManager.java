package com.nova.game.wsk.model;


import java.util.ArrayList;

import nova.common.game.wsk.data.CardData;

import com.nova.game.wsk.handler.GameRequestDispatcher;

public class JoinGameManager extends WskGameManager {
	
	private GameRequestDispatcher mRequest;
	
	public JoinGameManager() {
		mRequest = new GameRequestDispatcher();
	}
	
	@Override
	public void startGame() {
		super.startGame();
		// mRequest.startGame(RoomInfoController.getInstance().getRoomId());
	}
	
	@Override
	public void handleNotOutCard(int roomId, int playerId) {
		super.handleNotOutCard(roomId, playerId);
		mRequest.notOutCard(roomId, playerId);
	}
	
	@Override
	public void handleOutCard(int roomId, int playerId, ArrayList<CardData> datas) {
		super.handleOutCard(roomId, playerId, datas);
		mRequest.setOutCard(roomId, playerId, datas);
	}
	
	@Override
	public void handleOutLastCard(int roomId, int playerId, ArrayList<CardData> datas) {
		super.handleOutLastCard(roomId, playerId, datas);
		mRequest.setOutLastCard(roomId, playerId, datas);
	}
}
