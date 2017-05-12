package com.nova.game.wsk.screen;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.nova.game.wsk.actor.CardActor;
import com.nova.game.wsk.model.GameController;

import nova.common.game.wsk.data.CardData;
import nova.common.game.wsk.data.PlayerData;
import nova.common.game.wsk.util.CardConstant;

public class TestGameScreen extends LocalGameScreen {

	public TestGameScreen(MainGame game) {
        super(game);
    }
	
	@Override
	public void render(float delta) {
		super.render(delta);
		mBatch.begin();
		drawCustomCard();
		mBatch.end();
	}

    private void drawCustomCard() {
		HashMap<Integer, PlayerData> datas = GameController.getInstance().getPlayerDatas();
		if (datas == null) {
			return;
		}
		
		for (int i = 1; i < 4; i++) {
			if (datas.get(i) == null || datas.get(i).getCardList() == null) {
				continue;
			}

			ArrayList<CardData> cardDatas = datas.get(i).getCardList();
			for (int j = 0; j < cardDatas.size(); j++) {
				CardActor card = new CardActor(cardDatas.get(j));
				card.setPosition(300 + 30 * j, Gdx.graphics.getHeight() - CardConstant.CARD_HEIGHT - 70 * (i - 1));
				card.draw(mBatch, 1f);
			}
		}
	}
}
