package com.nova.game.wsk.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.screen.AssetsManager;

public class Score extends Actor{
    
    private TextureRegion[] mScoreNumberRegions;
    
    public Score() {
        mScoreNumberRegions = new TextureRegion[10];
        for (int i = 0; i < mScoreNumberRegions.length; i++) {
            mScoreNumberRegions[i] = new TextureRegion(AssetsManager.getInstance().mScoreNumberTexture, i * 22, 0, 22, 34);
        }
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        
        int score = GameController.getInstance().getScore();
        int num1 = score % 10;
        int num10 = score / 10 % 10;
        int num100 = score / 100 % 10;
        
        Sprite sprite1 = new Sprite(mScoreNumberRegions[num1]);
        sprite1.setPosition(getX() + 44 * 2, getY());
        sprite1.setSize(44, 68);
        sprite1.draw(batch);
        if (num10 > 0 || num100 > 0) {
            Sprite sprite10 = new Sprite(mScoreNumberRegions[num10]);
            sprite10.setPosition(getX() + 44, getY());
            sprite10.setSize(44, 68);
            sprite10.draw(batch);
        }
        if (num100 > 0) {
            Sprite sprite100 = new Sprite(mScoreNumberRegions[num100]);
            sprite100.setPosition(getX(), getY());
            sprite100.setSize(44, 68);
            sprite100.draw(batch);
        }
    }
}
