package com.nova.game.wsk.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.model.RoomInfoController;
import com.nova.game.wsk.screen.AssetsManager;

public class GamePlayer extends Player {

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
        // 庄主标记
        if (GameController.getInstance().getBanker() == getIndex()) {
        	batch.draw(AssetsManager.getInstance().mFlagBankerTexture, getRight(), getTop());
        }
        
        // 消息
        String message = RoomInfoController.getInstance().getPlayerMessage(getPlayerId(getIndex()));
        if (message != null) {
        	Label.LabelStyle ls = new Label.LabelStyle();
        	ls.background = new NinePatchDrawable(new NinePatch(getPlayerMessageBackground(), 40, 40, 40, 40));
        	ls.font = AssetsManager.getInstance().mFont;
        	Label label = new Label(message, ls);
        	label.setWidth(330);
        	label.setWrap(true);
        	label.setPosition(getPlayerMessagePositionX(label.getWidth()), getPlayerMessagePositionY(label.getHeight()));
        	label.draw(batch, parentAlpha);
        }
	}
	
	private Texture getPlayerMessageBackground() {
		if (1 == getIndex()) {
			return new Texture(Gdx.files.internal("drawable/message_right_background.png"));
		}
		
	    return new Texture(Gdx.files.internal("drawable/message_left_background.png"));
	}
	
	private float getPlayerMessagePositionX(float messageWidth) {
		if (1 == getIndex()) {
			return getX() - messageWidth;
		}
		
	    return getX() + 120;
	}
	
	private float getPlayerMessagePositionY(float messageHeight) {
		if (0 == getIndex()) {
			return getY() + 380 - messageHeight / 2;
		}
		
		return getY() + 100 - messageHeight / 2;
	}
	
	@Override
	protected Color getPlayerNameColor(int position) {
		if (0 != getIndex()) {
			return Color.DARK_GRAY;
		}
		
		return super.getPlayerNameColor(position);
	}
	
	@Override
	protected float getPlayerNamePositionX() {
		if (0 == getIndex()) {
			return 200;
		}
		return super.getPlayerNamePositionX();
	}
	
	@Override
	protected float getPlayerNamePositionY() {
		if (0 == getIndex()) {
			return 15;
		}
		return super.getPlayerNamePositionY();
	}
	
	@Override
	protected int getPlayerId(int position) {
    	int playerId = position + GameController.getInstance().getOwnerPlayerIndex();
    	if (playerId >= 4) {
    		playerId = playerId - 4;
    	}
    	return playerId;
    }
	
	@Override
	protected float getHeaderBackgroundSize() {
		if (0 == getIndex()) {
			return 160;
		}
		
		return super.getHeaderBackgroundSize();
	}
	
	@Override
	protected float getHeaderZoom() {
		if (0 == getIndex()) {
			return 1.5f;
		}
		
		return super.getHeaderZoom();
	}
	
	@Override
	protected Texture getHeaderBackgroundTexture(int position) {
		return AssetsManager.getInstance().mHeaderBackgroundTexture;
	}
	
	@Override
	protected Texture getPlayerNameBackgroundTexture(int position) {
		if (position == 0) {
			return null;
		}
		
		return AssetsManager.getInstance().mNameBackgroundTexture;
	}
}
