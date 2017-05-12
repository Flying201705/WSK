package com.nova.game.wsk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class BaseDialog extends Dialog{
    final static int TITLE_HEIGHT = 80;
    
	private TextButton mButton;
	private TextButton mPrimaryButton;
	private TextButton mSecondaryButton;
	private boolean mCanceledOnTouchOutside;
	
	private ClickListener mDialogClickListener = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			if (!mCanceledOnTouchOutside) {
				return;
			}
			
			if (x < getX() || x > (getX() + getWidth())
					|| y < getY() || y > (getY() + getHeight())) {
				setVisible(false);
			}
		};
	};
	
    public BaseDialog() {
        this("");
    }

    public BaseDialog(String title) {
        super(title, UIUtil.getDefaultWindowStyle());
        this.setBounds(0, 0, 1000, 400);
        this.setPosition((Constant.DEFAULT_WIDTH - getWidth()) / 2, (Constant.DEFAULT_HEIGHT - getHeight()) / 2);
        addListener(mDialogClickListener);
    }
    
    public BaseDialog(Drawable background) {
        super("", UIUtil.getDefaultWindowStyle(background));
        this.setBounds(0, 0, 1000, 400);
        this.setPosition((Constant.DEFAULT_WIDTH - getWidth()) / 2, (Constant.DEFAULT_HEIGHT - getHeight()) / 2);
        addListener(mDialogClickListener);
    }
	
    public BaseDialog(String title, boolean isTranslucent) {
    	super(title, UIUtil.getDefaultWindowStyle(isTranslucent));
    	this.setBounds(0, 0, 1000, 400);
        this.setPosition((Constant.DEFAULT_WIDTH - getWidth()) / 2, (Constant.DEFAULT_HEIGHT - getHeight()) / 2);
        ImageButton closeButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("drawable/icon_close.png")))));
        closeButton.setPosition(getWidth() - closeButton.getWidth() - 25, getHeight() - closeButton.getHeight() - 25);
        closeButton.addListener(new ClickListener() {
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		super.clicked(event, x, y);
        		setVisible(false);
        	}
        });
        addActor(closeButton);
        addListener(mDialogClickListener);
    }
//    @Override
//    public void setTitle(String title) {
//        super.setTitle(title);
//        if (title != null && !title.isEmpty()) {
//            padTop(TITLE_HEIGHT);
//        } else {
//            padTop(0);
//        }
//    }
	
	public void setCanceledOnTouchOutside(boolean cancel) {
    	mCanceledOnTouchOutside = true;
    }
    
    public void setPrimaryButton(String button, ClickListener listener) {
    	mPrimaryButton = new TextButton(button, UIUtil.getTextButtonStyle());
    	mPrimaryButton.addListener(listener);
    	mPrimaryButton.setBounds(80, 25, 350, 100);
    	addActor(mPrimaryButton);
    }
    
    public void setSecondaryButton(String button, ClickListener listener) {
    	mSecondaryButton = new TextButton(button, UIUtil.getTextButtonStyle());
    	mSecondaryButton.addListener(listener);
    	mSecondaryButton.setBounds(550, 25, 350, 100);
    	addActor(mSecondaryButton);
    }
    
    public void setButton(String button, ClickListener listener) {
    	mButton = new TextButton(button, UIUtil.getTextButtonStyle());
    	mButton.addListener(listener);
    	mButton.setBounds((getWidth() - 350) / 2, 25, 350, 100);
    	addActor(mButton);
    }
}
