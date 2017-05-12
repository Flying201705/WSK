package com.nova.game.wsk.actor;


import java.util.ArrayList;

import nova.common.game.wsk.util.CardConstant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.model.GameController;

public class MyCards extends Actor {
    private static String TAG = "MyCards";
    
    private int mCardCount = 0;
    private int mFlag = 0;
    
    private ArrayList<CardActor> mDraggedCardActors = new ArrayList<CardActor>();
    
    private GameController mGameManager;
    
    public MyCards() {
        mGameManager = GameController.getInstance();
        setSize(Constant.DEFAULT_WIDTH, 160);
        
        addListener(new InputListener() {
            
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "InputListener touchDown x = " + x + "; y = " + y);
                return true;//super.touchDown(event, x, y, pointer, button);
            }
            
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            	y = y + getY();
                ArrayList<CardActor> cardList = mGameManager.getOwnerCardList();
                
                if (mDraggedCardActors.size() > 0) {
                    for (int i = cardList.size() - 1; i >= 0; i--) {
                        if (mDraggedCardActors.contains(cardList.get(i))) {
                            if (cardList.get(i).getCardType() == 1) {
                                cardList.get(i).setCardType(0);
                            } else {
                                cardList.get(i).setCardType(1);
                            }
                            continue;
                        }
                    }
                    mDraggedCardActors.removeAll(mDraggedCardActors);
                    return;
                }
                
                for (int i = cardList.size() - 1; i >= 0; i--) {
                    Rectangle rectangle = cardList.get(i).getBounds();
                    if (rectangle.contains(x, y)) {
                        if (cardList.get(i).getCardType() == 1) {
                            cardList.get(i).setCardType(0);
                        } else {
                            cardList.get(i).setCardType(1);
                        }
                        return;
                    }
                }
            
                super.touchUp(event, x, y, pointer, button);
            }
            
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Gdx.app.log(TAG, "InputListener touchDragged x = " + x + "; y = " + y);
                y = y + getY();
                ArrayList<CardActor> cardList = mGameManager.getOwnerCardList();
                for (int i = cardList.size() - 1; i >= 0; i--) {
                    Rectangle rectangle = cardList.get(i).getBounds();
                    Gdx.app.log(TAG, "touchDragged i = " + i + "; rectangle = " + rectangle);
                    if (rectangle.contains(x, y)) {
                        if (!mDraggedCardActors.contains(cardList.get(i))) {
                            mDraggedCardActors.add(cardList.get(i));
                        }
                        
                        break;
                    }
                }
                super.touchDragged(event, x, y, pointer);
            }
        });
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        
        if (mCardCount < mGameManager.getOwnerCardList().size()) {
            mFlag++;
            if (mFlag >= 5) {
                mCardCount++;
                mFlag = 0;
            }
        }
        
        for (int i = 0; i < mGameManager.getOwnerCardList().size(); i++) {
            if (i >= mCardCount) {
                break;
            }
            int cardTotal = mCardCount < mGameManager.getOwnerCardList().size() ? mCardCount : mGameManager.getOwnerCardList().size();
            float space = (Constant.DEFAULT_WIDTH - CardConstant.CARD_WIDTH * 1.5f) / (cardTotal - 1);
            if (space < 55) {
            	space = 55;
            } else if (space > 100) {
            	space = 100;
            }
            float startX = Constant.DEFAULT_WIDTH / 2 - (CardConstant.CARD_WIDTH * 1.5f + space * (cardTotal - 1)) / 2;
            CardActor card = mGameManager.getOwnerCardList().get(i);
            if (card != null) {
                card.setSize(CardConstant.CARD_WIDTH * 1.5f, CardConstant.CARD_HEIGHT * 1.5f);
                float disY = getY() + (card.getCardType() == 1 ? 20 : 0);
                card.setPosition(startX + space * i, disY);
                
                if (mDraggedCardActors.contains(card)) {
                    card.setColor(0.7f, 1, 1, 1);
                } else {
                    card.setColor(1, 1, 1, 1);
                }
                
                card.draw(batch, 1f);
            }
        }
    }
}
