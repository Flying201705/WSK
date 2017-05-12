package com.nova.game.wsk;

import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public abstract class BaseGame extends Game {
    private Stack<Screen> mScreenStack = new Stack<Screen>();

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        Screen topScreen = mScreenStack.isEmpty() ? null : mScreenStack.peek();
        if (screen != null && screen != topScreen) {
            mScreenStack.push(screen);
        }
    }

    public void goBack() {
        Screen currScreen = getScreen();
        Screen topScreen = mScreenStack.peek();
        if (currScreen == topScreen) {
            mScreenStack.pop();
        }
        topScreen = mScreenStack.isEmpty() ? null : mScreenStack.peek();
        if (topScreen != null) {
            setScreen(topScreen);
            currScreen.dispose();
        } else {
            Gdx.app.exit();
        }
    }
}
