package com.nova.game.wsk.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nova.game.wsk.model.GameController;

public class BackGround extends Actor {
    private Texture mBackground;
    private Sprite mSprite;

    public BackGround(Texture bg) {
        mBackground = bg;
        mBackground.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        mSprite = new Sprite(mBackground);
        setSize(mBackground.getWidth(), mBackground.getHeight());
        
        addListener(new ClickListener() {
        	@Override
        	public void touchUp(InputEvent event, float x, float y,
        			int pointer, int button) {
        		super.touchUp(event, x, y, pointer, button);
        		GameController.getInstance().clearOwnerCardType();
        	}
        });
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO Auto-generated method stub
        super.draw(batch, parentAlpha);
        
        if (mBackground != null && isVisible()) {
            mSprite.draw(batch);
        }
    }
}