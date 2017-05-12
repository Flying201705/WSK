package com.nova.game.wsk.actor;


import java.util.ArrayList;

import nova.common.game.wsk.data.CardData;
import nova.common.game.wsk.util.CardConstant;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.model.GameController;

public class OutCards extends Actor {
    
    private GameController mGameManager;

    public OutCards() {
        mGameManager = GameController.getInstance();
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for (int i = 0; i < 4; i++) {
            ArrayList<CardData> outcardData = mGameManager.getOutCardDatas().get(i);
            if (outcardData == null || outcardData.size() <= 0) {
                continue;
            }
            
            for (int j = 0; j < outcardData.size(); j++) {
                CardActor card = new CardActor(outcardData.get(j));
                int mWidth = Constant.DEFAULT_WIDTH;
                int mHeight = Constant.DEFAULT_HEIGHT;
                int position = getPlayerPosition(i);
                if (position == 0) {
                    float startX = mWidth / 2 - (CardConstant.CARD_WIDTH + 30 * outcardData.size()) / 2;
                    card.setPosition(startX + 30 * j, 350);
                } else if (position == 1) {
                    float startX = mWidth - outcardData.size() * 30 - CardConstant.CARD_WIDTH - 300;
                    card.setPosition(startX + 30 * j, mHeight / 2 - CardConstant.CARD_HEIGHT / 2 + 50);
                } else if (position == 2) {
                    float startX = mWidth / 2 - (CardConstant.CARD_WIDTH + 30 * outcardData.size()) / 2;
                    card.setPosition(startX + 30 * j, mHeight - 400);
                } else if (position == 3) {
                    card.setPosition(300 + 30 * j, mHeight / 2 - CardConstant.CARD_HEIGHT / 2 + 50);
                }
                card.draw(batch, 1f);
            }
        }
    }
    
    private int getPlayerPosition(int playerId) {
    	int position = playerId - mGameManager.getOwnerPlayerIndex();
    	if (position < 0) {
    		position = position + 4;
    	}
    	return position;
    }
}
