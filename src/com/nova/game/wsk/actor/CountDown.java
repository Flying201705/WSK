package com.nova.game.wsk.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.screen.AssetsManager;

public class CountDown extends Actor {

    private GameController mGameManager;
    private TextureRegion[] mTimeNumberRegions;

    public CountDown() {
        mGameManager = GameController.getInstance();
        mTimeNumberRegions = new TextureRegion[10];
        for (int i = 0; i < mTimeNumberRegions.length; i++) {
            mTimeNumberRegions[i] = new TextureRegion(AssetsManager.getInstance().mTimeNumberTexture, i * 12, 0, 12, 17);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!mGameManager.isOperateStage()) {
            return;
        }
        
        drawCountDown(batch, getPlayerPosition(mGameManager.getCurrentPlayer()), false);
        if (mGameManager.isPaySecTributeStage() || mGameManager.isBackSecTributeStage()) {
            drawCountDown(batch, getPlayerPosition(mGameManager.getCurrentSecPlayer()), true);
        }
    }

    private void drawCountDown(Batch batch, int position, boolean isSecPlayer) {
        float countDownX = 0;
        float countDownY = 0;
        float countDownWidth = mTimeNumberRegions[0].getRegionWidth() * 2;
        float countDownHeight = mTimeNumberRegions[0].getRegionHeight() * 2;
        
        int mWidth = Constant.DEFAULT_WIDTH;
        int mHeight = Constant.DEFAULT_HEIGHT;
        
        if (position == 0) {
            countDownX = mWidth / 2 - countDownWidth / 2;
            countDownY = 380;
        } else if (position == 1) {
            countDownX = mWidth - 300;
            countDownY = mHeight / 2 - countDownHeight / 2 + 80;
        } else if (position == 2) {
            countDownX = mWidth / 2 - countDownWidth / 2;
            countDownY = mHeight - 350;
        } else if (position == 3) {
            countDownX = 300;
            countDownY = mHeight / 2 - countDownHeight / 2 + 80;
        }
        
        if (position >= 0 && position <= 3) {
            Sprite bg = new Sprite(AssetsManager.getInstance().mTimeBackgroundTexture);
            bg.setBounds(countDownX - (126 - countDownWidth) / 2, countDownY - (104 - countDownHeight) /2, 126, 104);
            bg.draw(batch);
//            batch.draw(AssetsManager.getInstance().mTimeBackgroundTexture, countDownX - (126 - countDownWidth) / 2, countDownY - (104 - countDownHeight) /2, 126, 104);
            int countDown = isSecPlayer ? mGameManager.getSecCountDown() : mGameManager.getCountDown();
            Sprite number = new Sprite(mTimeNumberRegions[countDown % 10]);
            number.setBounds(countDownX, countDownY, countDownWidth, countDownHeight);
            number.draw(batch);
//            batch.draw(mTimeNumberRegions[countDown % 10], countDownX, countDownY, countDownWidth, countDownHeight);
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
