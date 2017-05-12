package com.nova.game.wsk.actor;

import java.util.ArrayList;

import nova.common.game.wsk.data.CardData;
import nova.common.game.wsk.util.CardConstant;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.screen.AssetsManager;

public class CheckButton extends Group {

	private ImageButton mBackLastCardButton;
	private boolean mLastCardButtonSelected = false;
	private int mWidth = Constant.DEFAULT_WIDTH;
	
	public CheckButton() {
        TextureRegion[][] backLastCardRegion = TextureRegion.split(AssetsManager.getInstance().mLastCardBackTexture, 133, 60);
        mBackLastCardButton = new ImageButton(new TextureRegionDrawable(backLastCardRegion[0][0]), new TextureRegionDrawable(backLastCardRegion[0][1]));
        mBackLastCardButton.setPosition(mWidth - 140, 10);
        mBackLastCardButton.addListener(new ClickListener() {
        	@Override
        	public boolean touchDown(InputEvent event, float x, float y,
        			int pointer, int button) {
        		mLastCardButtonSelected = true;
        		return super.touchDown(event, x, y, pointer, button);
        	}
        	
        	@Override
        	public void touchUp(InputEvent event, float x, float y,
        			int pointer, int button) {
        		mLastCardButtonSelected = false;
        		super.touchUp(event, x, y, pointer, button);
        	}
        });
        
        addActor(mBackLastCardButton);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		drawLastCard(batch);
	}
	
	private void drawLastCard(Batch batch) {
		GameController controller = GameController.getInstance();
		ArrayList<CardData> lastCardDatas = controller.getOutLastCardDatas();
		if (lastCardDatas == null || lastCardDatas.size() <= 0) {
			return;
		}
		
		if (!mLastCardButtonSelected) {
			return;
		}
		
		float startX = mWidth - 150 - (CardConstant.CARD_WIDTH + 30 * controller.getOutLastCardDatas().size());
		for (int i = 0; i < controller.getOutLastCardDatas().size(); i++) {
			CardActor card = new CardActor(controller.getOutLastCardDatas().get(i));
			card.setPosition(startX + 30 * i, 10);
			card.setSize(CardConstant.CARD_WIDTH * 0.5f, CardConstant.CARD_HEIGHT * 0.5f);
			card.draw(batch, 1f);
		}
	}
}
