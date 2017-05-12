package com.nova.game.dialog;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.nova.game.wsk.BaseDialog;
import com.nova.game.wsk.UIUtil;
import com.nova.game.wsk.actor.Loading;

public class LoadingDialog extends BaseDialog {
    
    public LoadingDialog() {
        super(UIUtil.file2Drawable("drawable/hint_dialog_bg.png"));
        setBounds(686, 386, 547, 308);
        
        Loading loading = new Loading();
        loading.setPosition((getWidth() - loading.getWidth()) / 2, (getHeight() - loading.getHeight()) / 2);
        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
        RotateByAction ra = Actions.rotateBy(360, 1.0f);
        RepeatAction repeatAction = Actions.repeat(RepeatAction.FOREVER, ra);
        loading.addAction(repeatAction);
        addActor(loading);
    }
    
}
