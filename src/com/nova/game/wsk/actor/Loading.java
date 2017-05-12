package com.nova.game.wsk.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Loading extends Actor {
    private TextureRegion mRegion;
    
    public Loading() {
        Texture texture = new Texture(Gdx.files.internal("drawable/loading.png"));
        mRegion = new TextureRegion(texture);
        
        setSize(mRegion.getRegionWidth(), mRegion.getRegionHeight());
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        
        batch.draw(mRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
