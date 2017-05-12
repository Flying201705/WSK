package com.nova.game.wsk.actor;

import nova.common.game.wsk.data.CardData;
import nova.common.game.wsk.util.CardConstant;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nova.game.wsk.screen.AssetsManager;

public class CardActor extends Actor {

	private int mIndex;
	private CardData mCard;
	private int mType = 0;
	private Sprite mCardSprite;

	public CardActor(int index, CardData data) {
		mIndex = index;
		mCard = data;
		mType = 0;
		mCardSprite = new Sprite(AssetsManager.getInstance().mCardTextures[mCard.getIndex()]);
		setSize(CardConstant.CARD_WIDTH, CardConstant.CARD_HEIGHT);
	}
	
	public CardActor(CardData data) {
		this(-1, data);
	}
	
	public int getIndex() {
		return mIndex;
	}
	
	public CardData getCard() {
		return mCard;
	}
	
	public void setCardType(int type) {
		mType = type;
	}
	
	public int getCardType() {
		return mType;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		mCardSprite.setPosition(getX(), getY());
		mCardSprite.setRotation(getRotation());
		mCardSprite.setSize(getWidth(), getHeight());
		mCardSprite.draw(batch);
		if (mCard.getLevel() > 0) {
			batch.draw(AssetsManager.getInstance().mFlagTrumpTexture, getX() + 20, getY() + 30);
		}
		super.draw(batch, parentAlpha);
	}
	
	@Override
	public void setPosition(float x, float y) {
		mCardSprite.setPosition(x, y);
		super.setPosition(x, y);
	}
	
	@Override
	public void setBounds(float x, float y, float width, float height) {
		mCardSprite.setBounds(x, y, width, height);
		super.setBounds(x, y, width, height);
	}

	@Override
	public void setSize(float width, float height) {
		mCardSprite.setSize(width, height);
		super.setSize(width, height);
	}
	
	@Override
	public void setColor(float r, float g, float b, float a) {
		mCardSprite.setColor(r, g, b, a);
		super.setColor(r, g, b, a);
	}
	
	public Rectangle getBounds(){
		return mCardSprite.getBoundingRectangle();
	}
}
