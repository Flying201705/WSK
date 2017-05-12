package com.nova.game.dialog;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nova.game.wsk.BaseDialog;
import com.nova.game.wsk.screen.AssetsManager;

public class GameExitDialog extends BaseDialog {
	
	public interface onExitListener {
		public void exit();
	}
	
	private onExitListener mExitListener;
	
	public GameExitDialog() {
		setPrimaryButton("取消", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				GameExitDialog.this.setVisible(false);
			}
		});
		
		setSecondaryButton("退出", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				GameExitDialog.this.setVisible(false);
				if (mExitListener != null) {
					mExitListener.exit();
				}
			}
		});
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
	    // TODO Auto-generated method stub
	    super.draw(batch, parentAlpha);
	    AssetsManager.getInstance().mFont.draw(batch, "您要退出游戏吗?", getX() + 100, getTop() - 100);
	}

	public void setExitListener(onExitListener listener) {
		mExitListener = listener;
	}
}
