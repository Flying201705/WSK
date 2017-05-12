package com.nova.game.wsk.actor;

import java.util.Random;

import nova.common.game.wsk.data.PlayerInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.model.RoomInfoController;
import com.nova.game.wsk.screen.AssetsManager;


public class Player extends Actor {
    private final static int NAME_BACKGROUND_HEIGHT = 50;
    
    private PlayerInfo mPlayerInfo;
    private String mName;
    private Texture mAvatar;
    private Texture mHeaderBackground;
    private Texture mNameBackground;
    private int mSex; // 0 man; 1 woman
    private int mPlayerIndex;
    
    public Player () {
        this(null, null, 0);
    }
    
    public Player(PlayerInfo playerInfo) {
        this(playerInfo.getName(), null, playerInfo.getSex());
        mPlayerInfo = playerInfo;
    }
    
    public Player (String name, Texture avatar, int sex) {
        mName = name;
        
        if (avatar != null) {
            mAvatar = avatar;
        } else {
            Random rd = new Random();
            int index = rd.nextInt(16);
            mAvatar = AssetsManager.getInstance().mHeadTextures[index];
        }
        
        if (sex > 1 || sex < 0) {
            mSex = 0;
        } else {
            mSex = sex;
        }
        
        mHeaderBackground = AssetsManager.getInstance().mHeaderPrimaryBackgroundTexture;
        mNameBackground = AssetsManager.getInstance().mNamePrimaryBackgroundTexture;
        setSize(getHeaderBackgroundSize(), getHeaderBackgroundSize());
    }
    
    public void setIndex(int index) {
    	mPlayerIndex = index;
    	mHeaderBackground = getHeaderBackgroundTexture(mPlayerIndex);
    	mNameBackground = getPlayerNameBackgroundTexture(mPlayerIndex);
    	setSize(getHeaderBackgroundSize(), getHeaderBackgroundSize());
    	refreshPlayerInfo();
    }
    
    public int getIndex() {
    	return mPlayerIndex;
    }
    
    public int getPlayerId() {
    	return getPlayerId(mPlayerIndex);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if ( isVisible()) {
            refreshPlayerInfo();
            
        	// 头像背景
            batch.draw(mHeaderBackground, getX(), getY(), getWidth(), getHeight());
            
            // 头像
            if (mAvatar != null) {
                float avatarW = mAvatar.getWidth() * getHeaderZoom();
                float avatarH = mAvatar.getHeight() * getHeaderZoom();
                batch.draw(mAvatar, getX() + (getWidth() - avatarW) / 2, getY() + (getHeight() - avatarH) / 2, avatarW, avatarH);
            }

            // 人名
            if (mNameBackground != null) {
                batch.draw(mNameBackground, getPlayerNamePositionX(), getPlayerNamePositionY(), getWidth(), NAME_BACKGROUND_HEIGHT);
            }
            if (mName != null) {
                AssetsManager.getInstance().mFont.setColor(getPlayerNameColor(mPlayerIndex));
                AssetsManager.getInstance().mFont.draw(batch, mName, getPlayerNamePositionX() + 5,
                        getPlayerNamePositionY() + NAME_BACKGROUND_HEIGHT - (NAME_BACKGROUND_HEIGHT - AssetsManager.getInstance().mFont.getXHeight()) / 2);
            }
        }
        super.draw(batch, parentAlpha);
    }
    
    protected Color getPlayerNameColor(int position) {
    	return Color.WHITE;
    }
    
    protected float getPlayerNamePositionX() {
    	return getX();
    }
    
    protected float getPlayerNamePositionY() {
    	return getY() - NAME_BACKGROUND_HEIGHT;
    }
    
    protected int getPlayerId(int position) {
    	return position;
    }
    
    protected float getHeaderBackgroundSize() {
    	return mHeaderBackground.getWidth();
    }

    protected float getHeaderZoom() {
    	return 2.5f;
    }
    
    protected Texture getHeaderBackgroundTexture(int position) {
    	if (position % 2 == 0) {
    		return AssetsManager.getInstance().mHeaderPrimaryBackgroundTexture;
    	} else {
    		return AssetsManager.getInstance().mHeaderSecondaryBackgroundTexture;
    	}
    }
    
    protected Texture getPlayerNameBackgroundTexture(int position) {
    	if (position % 2 == 0) {
    		return AssetsManager.getInstance().mNamePrimaryBackgroundTexture;
    	} else {
    		return AssetsManager.getInstance().mNameSecondaryBackgroundTexture;
    	}
    }
    
    public void setName(String name) {
        mName = name;
    }
    
    public String getName () {
        return mName;
    }
    
    public void setAvatar(Texture avatar) {
        mAvatar = avatar;
    }
    
    public Texture getAvatar() {
        return mAvatar;
    }
    
    public void setSex(int sex) {
        mSex = sex;
    }
    
    public int getSex () {
        return mSex;
    }
    
    private Texture getHeadTexture(String head) {
    	int index = 0;
    	try {
    		index = Integer.valueOf(head);
		} catch (Exception e) {
		}
    	if (index <= 0 || index > 16) {
    		return AssetsManager.getInstance().mHeadTextures[0];
    	} else {
    		return AssetsManager.getInstance().mHeadTextures[index];
    	}
    }
    
    private void refreshPlayerInfo() {
        if (mPlayerInfo == null) {
            mPlayerInfo = RoomInfoController.getInstance().getPlayerInfos().get(getPlayerId(mPlayerIndex));
            if (mPlayerInfo != null) {
                mAvatar = getHeadTexture(mPlayerInfo.getHead());
                mName = mPlayerInfo.getName();
            }
        }
    }
}
