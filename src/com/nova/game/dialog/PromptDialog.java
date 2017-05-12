package com.nova.game.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nova.game.wsk.BaseDialog;
import com.nova.game.wsk.UIUtil;

public class PromptDialog extends BaseDialog {
	private TextButtonStyle mTextButtonStyle;
	private TextButton mPromptBtn;

	private ClickListener mButtonClickListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			setVisible(false);
		};
	};

	public PromptDialog(String title) {
	    super(title);
	    
	    setBounds(800, 400, 500, 200);
	    
		mTextButtonStyle = UIUtil.getTextButtonStyle();
		mPromptBtn = new TextButton("确定", mTextButtonStyle);
		mPromptBtn.setBounds(50, 2, 350, 100);
		mPromptBtn.addListener(mButtonClickListener);
		addActor(mPromptBtn);
	}
}
