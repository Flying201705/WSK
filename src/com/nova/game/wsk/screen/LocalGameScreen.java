package com.nova.game.wsk.screen;

public class LocalGameScreen extends GameScreen {

	public LocalGameScreen(MainGame game) {
        super(game);
    }

    @Override
	protected String getControllerType() {
		return "local";
	}
}
