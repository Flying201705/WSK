package com.nova.game.wsk.actor;


import java.util.ArrayList;

import nova.common.game.wsk.data.CardData;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.screen.AssetsManager;

public class ControlButton extends Group {
    private ImageButton mOutCardButton;
    private ImageButton mOutLastCardButton;
    private ImageButton mSetTrumpButton;
    private ImageButton mPassButton;
    private ImageButton mSkipButton;
    private ImageButton mPayTributeButton;
    private ImageButton mBackTributeButton;
    
    private int mWidth = Constant.DEFAULT_WIDTH;
    private GameController mGameManager;
    private int mResultCode = 0;
    
    
    public ControlButton() {
        mGameManager = GameController.getInstance();
        
        TextureRegion[][] outCardRegion = TextureRegion.split(AssetsManager.getInstance().mOutCardTexture, 232, 87);
        mOutCardButton = new ImageButton(new TextureRegionDrawable(outCardRegion[0][0]), new TextureRegionDrawable(outCardRegion[0][1]));
        mOutCardButton.setPosition(mWidth / 2 + 100, 350);
        mOutCardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	if (!mGameManager.isOwnerPlayerOperate()) {
            		return;
            	}
                handleOutCardList();
                AssetsManager.getInstance().mOutCardSound.play(1.0f);
                super.clicked(event, x, y);
            }
        });
        
        TextureRegion[][] payTributeRegion = TextureRegion.split(AssetsManager.getInstance().mPayTributeTexture, 232, 87);
        mPayTributeButton = new ImageButton(new TextureRegionDrawable(payTributeRegion[0][0]), new TextureRegionDrawable(payTributeRegion[0][1]));
        mPayTributeButton.setPosition(mWidth / 2 + 100, 350);
        mPayTributeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	if (!mGameManager.isOwnerPlayerOperate()) {
            		return;
            	}
                handleOutCardList();
                super.clicked(event, x, y);
            }
        });
        
        TextureRegion[][] backTributeRegion = TextureRegion.split(AssetsManager.getInstance().mBackTributeTexture, 232, 87);
        mBackTributeButton = new ImageButton(new TextureRegionDrawable(backTributeRegion[0][0]), new TextureRegionDrawable(backTributeRegion[0][1]));
        mBackTributeButton.setPosition(mWidth / 2 + 100, 350);
        mBackTributeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	if (!mGameManager.isOwnerPlayerOperate()) {
            		return;
            	}
                handleOutCardList();
                super.clicked(event, x, y);
            }
        });
        
        TextureRegion[][] outLastCardRegion = TextureRegion.split(AssetsManager.getInstance().mOutLastCardTexture, 232, 87);
        mOutLastCardButton = new ImageButton(new TextureRegionDrawable(outLastCardRegion[0][0]), new TextureRegionDrawable(outLastCardRegion[0][1]));
        mOutLastCardButton.setPosition(mWidth / 2 + 100, 350);
        mOutLastCardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	if (!mGameManager.isOwnerPlayerOperate()) {
            		return;
            	}
            	handleOutLastCardList();
                super.clicked(event, x, y);
            }
        });
        
        TextureRegion[][] setTrumpRegion = TextureRegion.split(AssetsManager.getInstance().mTrumpTexture, 232, 87);
        mSetTrumpButton = new ImageButton(new TextureRegionDrawable(setTrumpRegion[0][0]), new TextureRegionDrawable(setTrumpRegion[0][1]));
        mSetTrumpButton.setPosition(mWidth / 2 + 100, 350);
        mSetTrumpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	if (!mGameManager.isOwnerPlayerOperate()) {
            		return;
            	}
                handleOutCardList();
                super.clicked(event, x, y);
            } 
        });
        
        TextureRegion[][] passRegion = TextureRegion.split(AssetsManager.getInstance().mPassTexture, 232, 87);
        mPassButton = new ImageButton(new TextureRegionDrawable(passRegion[0][0]), new TextureRegionDrawable(passRegion[0][1]));
        mPassButton.setPosition(mWidth / 2 + 350, 350);
        mPassButton.addListener(new ClickListener() {
           @Override
            public void clicked(InputEvent event, float x, float y) {
           	if (!mGameManager.isOwnerPlayerOperate()) {
        		return;
        	}
               backAllCardList();
               mGameManager.handleOwnerNotOutCard();
               AssetsManager.getInstance().mPassSound.play(1.0f);
                super.clicked(event, x, y);
            }
        });
        
        TextureRegion[][] skipRegion = TextureRegion.split(AssetsManager.getInstance().mSkipTexture, 232, 87);
        mSkipButton = new ImageButton(new TextureRegionDrawable(skipRegion[0][0]), new TextureRegionDrawable(skipRegion[0][1]));
        mSkipButton.setPosition(mWidth / 2 - 350, 350);
        mSkipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	if (!mGameManager.isOwnerPlayerOperate()) {
            		return;
            	}
                backAllCardList();
                
                ArrayList<CardData> datas = mGameManager.getOwnerPromptData();
                if (datas != null && datas.size() > 0) {
                    for (CardData data : datas) {
                        for (int i = 0; i < mGameManager.getOwnerCardList().size(); i++) {
                            if (mGameManager.getOwnerCardList().get(i).getCard().getIndex() == data.getIndex() && mGameManager.getOwnerCardList().get(i).getCardType() == 0) {
                                mGameManager.getOwnerCardList().get(i).setCardType(1);
                                break;
                            }
                        }
                    }
                }
                super.clicked(event, x, y);
            }
        });
        
        addActor(mOutCardButton);
        addActor(mPayTributeButton);
        addActor(mBackTributeButton);
        addActor(mOutLastCardButton);
        addActor(mSetTrumpButton);
        addActor(mPassButton);
        addActor(mSkipButton);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
    	if (mResultCode < 0) {
    		float errorX = mWidth / 2 - AssetsManager.getInstance().mErrorOutCardTexture.getWidth() / 2;
    		float errorY = 300;
    		batch.draw(AssetsManager.getInstance().mErrorOutCardTexture, errorX, errorY);
    	}
    	
        if (!mGameManager.isOwnerPlayerOperate() || !mGameManager.isOperateStage()) {
            return;
        }
        
        if (mGameManager.isCallTrumpStage()) {
            mOutCardButton.setVisible(false);
            mPayTributeButton.setVisible(false);
            mBackTributeButton.setVisible(false);
            mOutLastCardButton.setVisible(false);
            mSetTrumpButton.setVisible(true);
            mPassButton.setVisible(true);
            mSkipButton.setVisible(true);
        } else if (mGameManager.isOutCardStage() || mGameManager.isBackTrumpStage()) {
            mOutCardButton.setVisible(true);
            mPayTributeButton.setVisible(false);
            mBackTributeButton.setVisible(false);
            mOutLastCardButton.setVisible(false);
            mSetTrumpButton.setVisible(false);
            mPassButton.setVisible(false);
            mSkipButton.setVisible(true);
        } else if (mGameManager.isOutLastCardStage()) {
            mOutCardButton.setVisible(false);
            mPayTributeButton.setVisible(false);
            mBackTributeButton.setVisible(false);
            mOutLastCardButton.setVisible(true);
            mSetTrumpButton.setVisible(false);
            mPassButton.setVisible(false);
            mSkipButton.setVisible(true);
        } else if (mGameManager.isPayTributeStage() || mGameManager.isPaySecTributeStage()) {
            mOutCardButton.setVisible(false);
            mPayTributeButton.setVisible(true);
            mBackTributeButton.setVisible(false);
            mOutLastCardButton.setVisible(false);
            mSetTrumpButton.setVisible(false);
            mPassButton.setVisible(false);
            mSkipButton.setVisible(true);
        } else if (mGameManager.isBackTributeStage() || mGameManager.isBackSecTributeStage()) {
            mOutCardButton.setVisible(false);
            mPayTributeButton.setVisible(false);
            mBackTributeButton.setVisible(true);
            mOutLastCardButton.setVisible(false);
            mSetTrumpButton.setVisible(false);
            mPassButton.setVisible(false);
            mSkipButton.setVisible(true);
        }
        
        super.draw(batch, parentAlpha);
    }
    
    private void handleOutLastCardList() {
    	mResultCode = mGameManager.handleOwnerOutLastCard();
    	backAllCardList();
    	clearResultCodeDelay();
    }
    
    private void handleOutCardList() {
    	mResultCode = mGameManager.handleOwnerOutCard();
    	backAllCardList();
    	clearResultCodeDelay();
    }
    
    private void backAllCardList() {
        mGameManager.clearOwnerCardType();
    }
    
    private void clearResultCodeDelay() {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mResultCode = 0;
			}
		}).start();
    }
}
