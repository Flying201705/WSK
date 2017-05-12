package com.nova.game.wsk.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.screen.AssetsManager;

public class Result extends Actor {

    private GameController mGameManager;

    public Result() {
        mGameManager = GameController.getInstance();
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (mGameManager.isGameResultShow()) {
            TextureRegion[][] result = TextureRegion.split(AssetsManager.getInstance().mFlagResultTexture, 86, 80);
            int mWidth = Constant.DEFAULT_WIDTH;
            int mHeight = Constant.DEFAULT_HEIGHT;
            
            Sprite sprite = new Sprite(mGameManager.isGameWin() ? result[0][0] : result[0][1]);
            sprite.setBounds(mWidth/2 - 80, mHeight/2 - 80, 160, 160);
            sprite.draw(batch);
        }
    }
}
