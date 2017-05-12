package com.nova.game.dialog;

import nova.common.game.wsk.data.PlayerInfo;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nova.game.wsk.BaseDialog;
import com.nova.game.wsk.screen.AssetsManager;

public class PlayerInfoDialog extends BaseDialog {

	private PlayerInfo mPlayer;
	
	public PlayerInfoDialog(float x, float y, PlayerInfo player) {
		super("", false);
        this.setBounds(x, y, 250, 400);
        setCanceledOnTouchOutside(true);
        mPlayer = player;
	}
	
    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO Auto-generated method stub
        super.draw(batch, parentAlpha);
		AssetsManager.getInstance().mFont.draw(batch, mPlayer.getName(), getX() + 10, getTop() - 30);
		AssetsManager.getInstance().mFont.draw(batch, mPlayer.getSex() == 0 ? "性别:男" : "性别:女", getX() + 30, getTop() - 120);
		AssetsManager.getInstance().mFont.draw(batch, "等级:进士", getX() + 30, getTop() - 180);
		AssetsManager.getInstance().mFont.draw(batch, "总局数:1060", getX() + 30, getTop() - 240);
		AssetsManager.getInstance().mFont.draw(batch, "胜率:43.02%", getX() + 30, getTop() - 300);
	}
}
