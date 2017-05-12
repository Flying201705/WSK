package com.nova.game.wsk.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.handler.GameRequestDispatcher;
import nova.common.game.wsk.data.PlayerInfo;
import nova.common.game.wsk.util.GameTimer;
import nova.common.game.wsk.util.TimerCallback;

public class RoomInfoController {

	private class ChatMessage {
		String message;
		long time;
		
		public ChatMessage(String message, long time) {
			this.message = message;
			this.time = time;
		}
		
		public String getMessage() {
			return this.message;
		}
		
		public long getTime() {
			return this.time;
		}
	}
	
	private static final Object mLock = new Object();
	private static RoomInfoController mController;
	
	private int mRoomId = -1;
	private HashMap<Integer, PlayerInfo> mPlayerInfos = new HashMap<Integer, PlayerInfo>();
	private HashMap<Integer, Sound> mPlayerSounds = new HashMap<Integer, Sound>();
	private HashMap<Integer, ChatMessage> mPlayerMessages = new HashMap<Integer, ChatMessage>();
	private boolean mRoomCreateComplete = false;
	private boolean mRoomJoinComplete = false;
	private boolean mRoomStartComplete = false;
	
	private GameTimer mGameTimer;
	
	private TimerCallback mCallback = new TimerCallback() {
		
		@Override
		public void handleMessage() {
			for (int i = 0; i < 4; i++) {
				if (mPlayerMessages.get(i) != null) {
					if (System.currentTimeMillis() - mPlayerMessages.get(i).getTime() >= 3000) {
						mPlayerMessages.remove(i);
					}
				}
			}
		}
	};
	
	private RoomInfoController() {
		mGameTimer = new GameTimer(mCallback);
		mGameTimer.start();
	}
	
	public static RoomInfoController getInstance() {
		synchronized (mLock) {
			if (mController == null) {
				mController = new RoomInfoController();
			}
			return mController;
		}
	}
	
	public void setRoomId(int roomId) {
		mRoomId = roomId;
	}
	
	public int getRoomId() {
		return mRoomId;
	}
	
	public void setPlayerInfos(HashMap<Integer, PlayerInfo> players) {
		mPlayerInfos.clear();
		mPlayerInfos.putAll(players);
	}
	
	public HashMap<Integer, PlayerInfo> getPlayerInfos() {
		return mPlayerInfos;
	}
	
	public void addPlayerSound(int playerId, Sound sound) {
		mPlayerSounds.put(playerId, sound);
	}
	
	public Sound getPlayerSound(int playerId) {
		return mPlayerSounds.get(playerId);
	}
	
	public void addPlayerMessage(int playerId, String message) {
		ChatMessage chat = new ChatMessage(message, System.currentTimeMillis());
		mPlayerMessages.put(playerId, chat);
	}
	
	public String getPlayerMessage(int playerId) {
		if (mPlayerMessages.get(playerId) != null) {
			return mPlayerMessages.get(playerId).getMessage();
		}
		
		return null;
	}
	
	public boolean isRoomCreated() {
		return mRoomCreateComplete;
	}
	
	public boolean isRoomJoin() {
		return mRoomJoinComplete;
	}
	
	public boolean isRoomStart() {
		return mRoomStartComplete;
	}
	
	public void onRoomGameStartComplete(int resultCode) {
		mRoomStartComplete = true;
		if (resultCode >= 0) {
			mRoomId = resultCode;
		}
	}
	
	public void onRoomCreateComplete(int resultCode) {
		mRoomCreateComplete = true;
		if (resultCode >= 0) {
			mRoomId = resultCode;
		}
	}
	
	public void onRoomJoinComplete(int resultCode) {
		mRoomJoinComplete = true;
		if (resultCode >= 0) {
			mRoomId = resultCode;
		}
	}
	
	public void replacePlayerPosition(int tarPosition) {
		String account = Gdx.app.getPreferences("LoginInfo").getString("account");
		int sourPosition = -1;
		Set set = mPlayerInfos.entrySet();
		Iterator it=set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			if (entry.getValue() == null) {
				continue;
			}
			if (((PlayerInfo)entry.getValue()).getId() == getPlayerId(account)) {
				sourPosition = (Integer)(entry.getKey());
				break;
			}
		}
		
		if (sourPosition == tarPosition || sourPosition <= 0) {
			return;
		}
		
		if (sourPosition > 0) {
			GameRequestDispatcher request = new GameRequestDispatcher();
			request.replacePlayerPosition(mRoomId, sourPosition, tarPosition);
		}
	}
	
	public int getPlayerId(String account) {
		if (Constant.PLAYERS.containsKey(account)) {
			return Constant.PLAYERS.get(account).getId();
		}
		
		return Constant.TEST_PLAYERID;
	}
}
