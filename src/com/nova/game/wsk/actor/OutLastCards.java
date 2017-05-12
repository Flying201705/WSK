package com.nova.game.wsk.actor;

import nova.common.game.wsk.util.CardConstant;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.model.GameController;

public class OutLastCards extends Actor {

    private GameController mGameManager;
    private boolean mIsBackLastCard;

    public OutLastCards() {
        mGameManager = GameController.getInstance();
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if ((!mGameManager.isOutLastCardStage() && !mIsBackLastCard) || mGameManager.getOutLastCardDatas().size() <= 0) {
            return;
        }
        
        float startX = Constant.DEFAULT_WIDTH - 150 - (CardConstant.CARD_WIDTH + 30 * mGameManager.getOutLastCardDatas().size());
        for (int i = 0; i < mGameManager.getOutLastCardDatas().size(); i++) {
            CardActor card = new CardActor(mGameManager.getOutLastCardDatas().get(i));
            card.setPosition(startX + 30 * i, 10);
            card.setSize(CardConstant.CARD_WIDTH * 0.5f, CardConstant.CARD_HEIGHT * 0.5f);
            card.draw(batch, 1f);
        }
    }
}
