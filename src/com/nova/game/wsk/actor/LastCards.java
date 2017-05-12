package com.nova.game.wsk.actor;


import java.util.ArrayList;

import nova.common.game.wsk.data.CardData;
import nova.common.game.wsk.util.CardConstant;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.screen.AssetsManager;

public class LastCards extends Actor {

    private GameController mGameManager;
    private int mLastCardCount;

    public LastCards() {
        mGameManager = GameController.getInstance();
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        ArrayList<CardData> lastCardDatas = mGameManager.getLastCardDatas();
        if (lastCardDatas == null || lastCardDatas.size() <= 0) {
            return;
        }
        if (mLastCardCount < lastCardDatas.size() * 3) {
            mLastCardCount++;
        }
        
        for (int i = 0; i < lastCardDatas.size(); i++) {
            float startX = Constant.DEFAULT_WIDTH / 2 - CardConstant.CARD_WIDTH * lastCardDatas.size() / 2 + CardConstant.CARD_WIDTH * i;
            float startY = 400 + CardConstant.CARD_HEIGHT;
            if (i * 3 > mLastCardCount) {
                batch.draw(AssetsManager.getInstance().mCardTextures[0], startX, startY, CardConstant.CARD_WIDTH, CardConstant.CARD_HEIGHT);
                continue;
            }
            CardActor card = new CardActor(lastCardDatas.get(i));
            card.setPosition(startX, startY);
            card.draw(batch, 1f);
        }
    }
}
