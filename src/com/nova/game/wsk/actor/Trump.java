package com.nova.game.wsk.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.screen.AssetsManager;

public class Trump extends Actor{

    private Texture mCardBg;
    private TextureRegion[][] mCardValueRegions;
    private TextureRegion[][] mCardColorRegions;
    private GameController mGameManager;
    
    public Trump () {
        mCardBg = AssetsManager.getInstance().mCardBgTexture;
        mCardValueRegions = TextureRegion.split(AssetsManager.getInstance().mCardValueTexture, 41, 40);
        mCardColorRegions = TextureRegion.split(AssetsManager.getInstance().mCardColorTexture, 18, 21);
        
        mGameManager = GameController.getInstance();
        
        setSize(mCardBg.getWidth(), mCardBg.getHeight());
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        
        // batch.draw(mCardBg, getX(), getY(), getWidth(), getHeight());
        
        if (mGameManager.getTrumpColor() >= 0) {
            int value = mGameManager.getTrumpColor();
            if (value == 2) {
                value = 3;
            } else if (value == 3) {
                value = 2;
            }
            Sprite colorSprite = new Sprite(mCardColorRegions[0][value]);
            colorSprite.setPosition(getX(), getY());
            colorSprite.setSize(colorSprite.getWidth() * 2, colorSprite.getHeight() * 2);
            colorSprite.draw(batch);
        }
        
        if (mGameManager.getTrumpFace() > 0) {
            int value = mGameManager.getTrumpFace() == 14 ? 0 : mGameManager.getTrumpFace() - 1;
            int color = (mGameManager.getTrumpColor() == 0 || mGameManager.getTrumpColor() == 3) ? 0 : 1;
            Sprite valueSprite = new Sprite(mCardValueRegions[color][value]);
            valueSprite.setPosition(getX(), getY() + 50);
            valueSprite.draw(batch);
        }
    }
}
