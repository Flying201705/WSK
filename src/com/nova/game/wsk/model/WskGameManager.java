package com.nova.game.wsk.model;


import java.util.ArrayList;

import nova.common.game.wsk.data.CardData;
import nova.common.game.wsk.util.GameTimer;
import nova.common.game.wsk.util.TimerCallback;

import com.nova.game.wsk.handler.GameRequestDispatcher;


public class WskGameManager {

	// 倒计时
	private int mDuration = 9;
	private int mSecDuration = 9;
	private GameTimer mGameTimer;
	private GameTimer mSecGameTimer;
	
	
	public WskGameManager() {

	}
	
	public void startGame() {
		mGameTimer = new GameTimer(mCallback);
		mSecGameTimer = new GameTimer(mSecCallback);
	}
	
	public void stopGame() {
		if (mGameTimer.isRunning()) {
			mGameTimer.stop();
		}
		
		if (mSecGameTimer.isRunning()) {
			mSecGameTimer.stop();
		}
	}
	
	private TimerCallback mCallback = new TimerCallback() {
		
		@Override
		public void handleMessage() {
			if (mDuration > 0) {
				mDuration--;
				return;
			}
		}
	};
	
	private TimerCallback mSecCallback = new TimerCallback() {
		
		@Override
		public void handleMessage() {
			if (mSecDuration > 0) {
				mSecDuration--;
				return;
			}
		}
	};
	
	public void startCountDown() {
		mDuration = 9;
		if (mGameTimer.isRunning()) {
			mGameTimer.stop();
		}
		mGameTimer = new GameTimer(mCallback);
		mGameTimer.start();
	}
	
	public void startSecCountDown() {
		mSecDuration = 9;
		if (mSecGameTimer.isRunning()) {
			mSecGameTimer.stop();
		}
		
		mSecGameTimer = new GameTimer(mSecCallback);
		mSecGameTimer.start();
	}
	
	public int getCountDown() {
		return mDuration;
	}
	
	public int getSecCountDown() {
		return mSecDuration;
	}
	
	// 不出牌
	public void handleNotOutCard(int roomId, int playerId) {

	}
	
	public void handleOutCard(int roomId, int playerId, ArrayList<CardData> datas) {

	}
	
	public void handleOutLastCard(int roomId, int playerId, ArrayList<CardData> datas) {
		
	}
}
