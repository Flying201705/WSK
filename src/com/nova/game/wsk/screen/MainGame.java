package com.nova.game.wsk.screen;

import com.nova.game.wsk.BaseGame;

import nova.common.game.wsk.data.PlayerInfo;

public class MainGame extends BaseGame {
    
    private PlayerInfo myInfo;

    @Override
    public void create() {
        AssetsManager.getInstance().loadAssets();
        setScreen(new LoginScreen(this));
    }
    
    public void setMyInfo(PlayerInfo info) {
        myInfo = info;
    }
    
    public PlayerInfo getMyInfo() {
        return myInfo;
    }
}
