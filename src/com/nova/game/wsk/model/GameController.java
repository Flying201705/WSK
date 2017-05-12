package com.nova.game.wsk.model;


import java.util.ArrayList;
import java.util.HashMap;

import com.nova.game.wsk.actor.CardActor;
import com.nova.game.wsk.screen.AssetsManager;

import nova.common.game.wsk.GameStage;
import nova.common.game.wsk.data.CardData;
import nova.common.game.wsk.data.GameInfo;
import nova.common.game.wsk.data.GameRoundInfo;
import nova.common.game.wsk.data.OutCardData;
import nova.common.game.wsk.data.PlayerData;
import nova.common.game.wsk.data.PlayerInfo;
import nova.common.game.wsk.data.TrumpData;
import nova.common.game.wsk.util.CardConstant;
import nova.common.game.wsk.util.GameTimer;
import nova.common.game.wsk.util.TimerCallback;

public class GameController {
	
	private static final Object mLock = new Object();
	private int mPlayerIndex = 0;
	private static GameController mController;
	private WskGameManager mManager;
	private HashMap<Integer, PlayerData> mPlayerDatas;
	private ArrayList<CardData> mLastCardDatas = new ArrayList<CardData>();
	private ArrayList<CardData> mOutLastCardDatas = new ArrayList<CardData>();
	private ArrayList<CardActor> mOwnerCardActors = new ArrayList<CardActor>();
	private HashMap<Integer, ArrayList<CardData>> mOutCardDatas = new HashMap<Integer, ArrayList<CardData>>();
	private RoomInfoController mRoomController = RoomInfoController.getInstance();
	private GameInfo mGameInfo;
	private GameRoundInfo mGameRoundInfo;
	private boolean mIsGameResultShow;
	
	private GameTimer mResultTimer = new GameTimer(new TimerCallback() {
		private int mCount = 0;
		@Override
		public void handleMessage() {
			mCount++;
			if (mCount >= 3) {
				mIsGameResultShow = false;
				mCount = 0;
				mResultTimer.stop();
			}
		}
	});
	
	private GameController(String type) {
		mGameInfo = new GameInfo();
		mGameRoundInfo = new GameRoundInfo();
		if (type.equals("local")) {
			mManager = new LocalGameManager();
		} else if (type.equals("join")) {
			mManager = new JoinGameManager();
		} else {
			mManager = new WskGameManager();
		}
	}
	
	public static GameController create(String type) {
		synchronized (mLock) {
			if (mController == null) {
				mController = new GameController(type);
			}
			return mController;
		}
	}
	
	public static GameController getInstance() {
		synchronized (mLock) {
			if (mController == null) {
				throw new RuntimeException("WskGameController not yet created!?!");
			}
			return mController;
		}
	}
	
	public void startGame() {
		mManager.startGame();
	}
	
	public void stopGame() {
		mManager.stopGame();
	}
	
	public void setOwnerPlayerIndex(int index) {
		mPlayerIndex = index;
	}
	
	public int getOwnerPlayerIndex() {
		return mPlayerIndex;
	}
	
	public void setGameInfo(GameInfo info) {
		mGameInfo = info;
	}
	
	public void setGameRoundInfo(GameRoundInfo info) {
		mGameRoundInfo = info;
	}
	
	public int getBanker() {
		return mGameInfo.getBankerId();
	}
	
	public int getTrumpColor() {
		return mGameInfo.getTrumpColor();
	}
	
	public void setTrumpColor(int color) {
		mGameInfo.setTrumpColor(color);
	}
	
	public int getTrumpFace() {
		return mGameInfo.getTrumpFace();
	}
	
	public int getScore() {
		return mGameInfo.getScore();
	}
	
	public void setScore(int score) {
		mGameInfo.setScore(score);
	}
	
	public int getCurrentPlayer() {
		return mGameRoundInfo.getCurrent();
	}
	
	public void setCurrentPlayer(int playerId) {
		mGameRoundInfo.setCurrent(playerId);
	}
	
	public int getCurrentSecPlayer() {
		return mGameRoundInfo.getSecurrent();
	}
	
	public void setCurrentSecPlayer(int playerId) {
		mGameRoundInfo.setSecurrent(playerId);
	}
	
	public int getFirstPlayer() {
		return mGameRoundInfo.getFirst();
	}
	
	public void setFirstPlayer(int playerId) {
		mGameRoundInfo.setFirst(playerId);
	}
	
	public void setLargePlayer(int playerId) {
		mGameRoundInfo.setLarger(playerId);
	}
	
	public int getCountDown() {
		return mManager.getCountDown();
	}
	
	public int getSecCountDown() {
		return mManager.getSecCountDown();
	}
	
	public boolean isGameResultShow() {
		return mIsGameResultShow;
	}
	
	public boolean isGameWin() {
		return mGameInfo.isGameWin(mPlayerIndex);
	}
	
	public void setStage(int stage) {
		mGameRoundInfo.setStage(stage);
		
		if (!mIsGameResultShow && stage == GameStage.GAME_END) {
			mIsGameResultShow = true;
			if (isGameWin()) {
				AssetsManager.getInstance().mWinSound.play(1.0f);
			} else {
				AssetsManager.getInstance().mLoseSound.play(1.0f);
			}
			mResultTimer.start();
		}
		
		if (isOperateStage()) {
			mManager.startCountDown();
		}
	}
	
	public void setSecStage(int secStage) {
		mGameRoundInfo.setSecStage(secStage);
		
		if (isOperateStage()) {
			mManager.startSecCountDown();
		}
	}
	
	public boolean isOperateStage() {
		if ( mGameRoundInfo.getStage() == GameStage.CALL_TRUMP_WAIT
				|| mGameRoundInfo.getStage() == GameStage.OUT_CARD_WAIT
				|| mGameRoundInfo.getStage() == GameStage.OUT_LAST_CARD_WAIT
				|| mGameRoundInfo.getStage() == GameStage.BACK_TRUMP_WAIT
				|| mGameRoundInfo.getStage() == GameStage.PAY_TRIBUTE_WAIT
				|| mGameRoundInfo.getSecStage() == GameStage.PAY_TRIBUTE_SEC_WAIT
				|| mGameRoundInfo.getStage() == GameStage.BACK_TRIBUTE_WAIT
				|| mGameRoundInfo.getSecStage() == GameStage.BACK_TRIBUTE_SEC_WAIT) {
			return true;
		}
		
		return false;
	}
	
	public boolean isOwnerPlayerOperate() {
		return mGameRoundInfo.getCurrent() == mPlayerIndex || mGameRoundInfo.getSecurrent() == mPlayerIndex;
	}
	
	public boolean isOutCardStage() {
		return mGameRoundInfo.getStage() == GameStage.OUT_CARD_WAIT;
	}
	
	public boolean isOutLastCardStage() {
		return mGameRoundInfo.getStage() == GameStage.OUT_LAST_CARD_WAIT;
	}
	
	public boolean isSendLastCardStage() {
		return mGameRoundInfo.getStage() == GameStage.SEND_LAST_CARD_WAIT;
	}
	
	public boolean isCallTrumpStage() {
		return mGameRoundInfo.getStage() == GameStage.CALL_TRUMP_WAIT;
	}
	
	public boolean isBackTrumpStage() {
		return mGameRoundInfo.getStage() == GameStage.BACK_TRUMP_WAIT;
	}
	
	public boolean isPayTributeStage() {
		return mGameRoundInfo.getStage() == GameStage.PAY_TRIBUTE_WAIT;
	}
	
	public boolean isBackTributeStage() {
		return mGameRoundInfo.getStage() == GameStage.BACK_TRIBUTE_WAIT;
	}
	
	public boolean isPaySecTributeStage() {
		return mGameRoundInfo.getSecStage() == GameStage.PAY_TRIBUTE_SEC_WAIT;
	}
	
	public boolean isBackSecTributeStage() {
		return mGameRoundInfo.getSecStage() == GameStage.BACK_TRIBUTE_SEC_WAIT;
	}
	
	public void handleOwnerNotOutCard() {
		mManager.handleNotOutCard(mRoomController.getRoomId(), mPlayerIndex);
	}
	
	public int handleOwnerOutLastCard() {
		ArrayList<CardData> datas = getOwnerSelectCardDatas();
		if (datas.size() <= 0) {
			return -1;
		}
		
		if (datas.size() != 8) {
			return -2;
		}
		
		mOutLastCardDatas.removeAll(mOutLastCardDatas);
		mOutLastCardDatas.addAll(datas);
		getOwnerDatas().removeOutCardFromTotalCard(datas);
		initOwnerCardList(getOwnerDatas().getCardList());
		
		mManager.handleOutLastCard(mRoomController.getRoomId(), mPlayerIndex, datas);
		
		return 0;
	}
	
	public int handleOwnerOutCard() {
		ArrayList<CardData> datas = getOwnerSelectCardDatas();
		if (datas.size() <= 0) {
			return -1;
		}
		
		if (!isOwnerCardDatasRight(datas)) {
			return -2;
		}
		
		mOutCardDatas.put(mPlayerIndex, datas);
		getOwnerDatas().removeOutCardFromTotalCard(datas);
		initOwnerCardList(getOwnerDatas().getCardList());
		mManager.handleOutCard(mRoomController.getRoomId(), mPlayerIndex, datas);
		return 0;
	}
	
	public void setLastCardDatas(ArrayList<CardData> datas) {
		if (mLastCardDatas.equals(datas)) {
			return;
		}
		mLastCardDatas.removeAll(mLastCardDatas);
		mLastCardDatas.addAll(datas);
	}
	
	public ArrayList<CardData> getLastCardDatas() {
		if (mGameRoundInfo.getStage() == GameStage.OUT_LAST_CARD_END
				|| mGameRoundInfo.getStage() == GameStage.SEND_LAST_CARD_WAIT
				|| mGameRoundInfo.getStage() == GameStage.SEND_LAST_CARD_END) {
			return mLastCardDatas;
		}
		
		return null;
	}
	
	public ArrayList<CardData> getOutLastCardDatas() {
		return mOutLastCardDatas;
	}
	
	public void setOutLastCardDatas(ArrayList<CardData> datas) {
		if (mOutLastCardDatas.equals(datas)) {
			return;
		}
		mOutLastCardDatas.removeAll(mOutLastCardDatas);
		mOutLastCardDatas.addAll(datas);
	}
	
	public HashMap<Integer, ArrayList<CardData>> getOutCardDatas() {
		return mOutCardDatas;
	}
	
	public void setOutCardDatas(HashMap<Integer, ArrayList<CardData>> datas) {
		mOutCardDatas.clear();
		mOutCardDatas.putAll(datas);
	}
	
	public void clearOutCardDatas() {
		mOutCardDatas.clear();
	}
	
	public PlayerData getOwnerDatas() {
		return mPlayerDatas.get(mPlayerIndex);
	}
	
	public HashMap<Integer, PlayerData> getPlayerDatas() {
		return mPlayerDatas;
	}
	
	public void setPlayerDatas(HashMap<Integer, PlayerData> datas) {
		mPlayerDatas = datas;
		if (mPlayerDatas != null && mPlayerDatas.size() > 0) {
			updateOwnerCardList(getOwnerDatas().getCardList());
		}
	}
	
	private boolean isNeedUpdateOwnerCard(ArrayList<CardData> datas) {
		if (mOwnerCardActors.size() != datas.size()) {
			return true;
		}
		
		ArrayList<CardData> cardDatas = new ArrayList<CardData>();
		for (CardActor card : mOwnerCardActors) {
			cardDatas.add(card.getCard());
		}
		
		if (isSameCardDatas(datas, cardDatas)) {
			return false;
		}
		
		return true;
	}
	
	private boolean isSameCardDatas(ArrayList<CardData> datas1, ArrayList<CardData> datas2) {
		if (datas1.size() != datas2.size()) {
			return false;
		}
		
		for (int i = 0; i < datas1.size(); i++) {
			if (datas1.get(i).getIndex() != datas2.get(i).getIndex()) {
				return false;
			}
		}
		
		return true;
	}
	
	private void updateOwnerCardList(ArrayList<CardData> datas) {
		if (!isNeedUpdateOwnerCard(datas)) {
			return;
		}
       initOwnerCardList(datas);
	}
	
	private void initOwnerCardList(ArrayList<CardData> datas) {
        mOwnerCardActors.removeAll(mOwnerCardActors);
        for (int i = 0; i < datas.size(); i++) {
            CardActor card = new CardActor(i, datas.get(i));
            mOwnerCardActors.add(card);
        }
	}
	
	public ArrayList<CardData> getOwnerPromptData() {
		ArrayList<CardData> datas = new ArrayList<CardData>();
		if (mGameRoundInfo.getStage() == GameStage.CALL_TRUMP_WAIT) {
			TrumpData data = new TrumpData(mGameInfo.getTrumpFace());
			if (mOutCardDatas.get(mGameRoundInfo.getLarger()) != null) {
				data.updateData(mOutCardDatas.get(mGameRoundInfo.getLarger()));
			}
			datas = getOwnerDatas().getTrumpCards(data, false);
		} else if (mGameRoundInfo.getStage() == GameStage.PAY_TRIBUTE_WAIT || mGameRoundInfo.getSecStage() == GameStage.PAY_TRIBUTE_SEC_WAIT) {
			datas = getOwnerDatas().getPayTributeCards(1, false);
		} else if (mGameRoundInfo.getStage() == GameStage.BACK_TRIBUTE_WAIT || mGameRoundInfo.getSecStage() == GameStage.BACK_TRIBUTE_SEC_WAIT) {
			datas = getOwnerDatas().getBackTributeCards(1, false);
		} else if (mGameRoundInfo.getStage() == GameStage.OUT_LAST_CARD_WAIT) {
			datas = getOwnerDatas().getOutLastCards(false);
		} else {
			if (mPlayerIndex == getFirstPlayer()) {
				datas = getOwnerDatas().getFirstOutCardData(false);
			} else {
				OutCardData outCard = new OutCardData(mOutCardDatas.get(mGameRoundInfo.getFirst()).get(0), mOutCardDatas.get(mGameRoundInfo.getFirst()).size());
				outCard.setLargeCard(mOutCardDatas.get(mGameRoundInfo.getLarger()).get(0));
				datas = getOwnerDatas().getOutCardDatas(outCard, mGameRoundInfo.getLarger() % 2 == 0, false);
			}
		}
		return datas;
	}

	public void clearOwnerCardType() {
		for (CardActor card : mOwnerCardActors) {
			if (card.getCardType() != 0) {
				card.setCardType(0);
			}
		}
	}
	
    public ArrayList<CardActor> getOwnerCardList() {
        return mOwnerCardActors;
    }
    
    private ArrayList<CardData> getOwnerSelectCardDatas() {
        ArrayList<CardData> datas = new ArrayList<CardData>();
        for (CardActor card : mOwnerCardActors) {
            if (card != null && card.getCardType() == 1) {
                datas.add(getOwnerDatas().getCardList().get(card.getIndex()));
            }
        }
        return datas;
    }
    
    private boolean isOwnerCardDatasRight(ArrayList<CardData> datas) {
		if (isCallTrumpStage()) {
			return isTrumpRight(datas);
		} else if (mGameRoundInfo.getStage() == GameStage.PAY_TRIBUTE_WAIT 
				|| mGameRoundInfo.getSecStage() == GameStage.PAY_TRIBUTE_SEC_WAIT 
				|| mGameRoundInfo.getStage() == GameStage.BACK_TRIBUTE_WAIT
				|| mGameRoundInfo.getSecStage() == GameStage.BACK_TRIBUTE_SEC_WAIT) {
			return datas.size() == 1;
		} else {
			return isOutCardRight(mPlayerIndex, datas);
		}
    }
    
	private boolean isTrumpRight(ArrayList<CardData> datas) {
		TrumpData largeTrumpData = new TrumpData(mGameInfo.getTrumpFace());
		if (mOutCardDatas.get(mGameRoundInfo.getLarger()) != null) {
			largeTrumpData.updateData(mOutCardDatas.get(mGameRoundInfo.getLarger()));
		}
		
		if (datas.size() <= largeTrumpData.getCount()) {
			return false;
		}
		
		HashMap<Integer, Integer> trumpInfo = new HashMap<Integer, Integer>();
		for (CardData data : datas) {
			int index = data.getIndex();
			int size = 0;
			if (trumpInfo.get(index) != null) {
				size = trumpInfo.get(index);
			}
			size++;
			trumpInfo.put(index, size);
		}
		
		int trumpFace = mGameInfo.getTrumpFace();
		if (trumpInfo.size() == 3 && trumpInfo.get(CardConstant.CARD_KING_BEGIN) != null) {
			if (trumpInfo.get(CardConstant.CARD_FANG_J) != null && 
					trumpInfo.get(CardConstant.CARD_FANG_A + trumpFace - 1) != null &&
					trumpInfo.get(CardConstant.CARD_FANG_A + trumpFace - 1) > 1) {
				return true;
			}
			
			if (trumpInfo.get(CardConstant.CARD_MEI_J) != null && 
					trumpInfo.get(CardConstant.CARD_MEI_A + trumpFace - 1) != null &&
					trumpInfo.get(CardConstant.CARD_MEI_A + trumpFace - 1) > 1) {
				return true;
			}
			
			if (trumpInfo.get(CardConstant.CARD_TAO_J) != null && 
					trumpInfo.get(CardConstant.CARD_TAO_A + trumpFace - 1) != null &&
					trumpInfo.get(CardConstant.CARD_TAO_A + trumpFace - 1) > 1) {
				return true;
			}
			
			if (trumpInfo.get(CardConstant.CARD_XIN_J) != null && 
					trumpInfo.get(CardConstant.CARD_XIN_A + trumpFace - 1) != null &&
					trumpInfo.get(CardConstant.CARD_XIN_A + trumpFace - 1) > 1) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isOutCardRight(int playerId, ArrayList<CardData> datas) {
		if (playerId == mGameRoundInfo.getFirst()) {
			boolean isSameCard = false;
			int index = -1;
			for (int i = 0; i < datas.size(); i++) {
				CardData card =datas.get(i);
				if (index == -1) {
					index = card.getIndex();
				}
				
				if (index != card.getIndex()) {
					isSameCard = false;
					break;
				}
				
				isSameCard = true;
			}
			
			return isSameCard;
		} else {
			OutCardData largeOutCard = new OutCardData(mOutCardDatas.get(mGameRoundInfo.getFirst()).get(0), mOutCardDatas.get(mGameRoundInfo.getFirst()).size());
			largeOutCard.setLargeCard(mOutCardDatas.get(mGameRoundInfo.getLarger()).get(0));
			if (datas.size() != largeOutCard.getCount()) {
				return false;
			}
			int color = largeOutCard.getFirstCard().getColor();
			int level = largeOutCard.getFirstCard().getLevel();
			int count = 0;
			for (int i = 0; i < mPlayerDatas.get(playerId).getCardList().size(); i++) {
				if (level > 0) {
					if (mPlayerDatas.get(playerId).getCardList().get(i).getLevel() > 0) {
						count++;
					}
				} else if (mPlayerDatas.get(playerId).getCardList().get(i).getColor() == color && mPlayerDatas.get(playerId).getCardList().get(i).getLevel() == 0) {
					count++;
				}
			}

			if (count > datas.size()) {
				count = datas.size();
			}

			int count2 = 0;
			for (int i = 0; i < datas.size(); i++) {
				if (level > 0) {
					if (datas.get(i).getLevel() > 0) {
						count2++;
					}
				} else if (datas.get(i).getColor() == color && datas.get(i).getLevel() == 0) {
					count2++;
				}
			}

			if (count2 < count) {
				return false;
			}
			
			return true;
		}
	}
}
