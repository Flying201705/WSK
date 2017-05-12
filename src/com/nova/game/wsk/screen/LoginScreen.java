package com.nova.game.wsk.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nova.game.dialog.ChatDialog;
import com.nova.game.dialog.LoadingDialog;
import com.nova.game.wsk.BaseScreen;
import com.nova.game.wsk.BaseStage;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.UIUtil;
import com.nova.game.wsk.actor.Loading;

import android.view.animation.RotateAnimation;
import nova.common.game.wsk.data.PlayerInfo;

public class LoginScreen extends BaseScreen {
    private MainGame mGame;
    private BaseStage mStage;
    private Preferences mPref;
    private SpriteBatch mBatch;
    private TextField mInputTextField;
    private Sprite mBackground;
    private LoadingDialog mLoadingDialog;
    
    public LoginScreen(MainGame game) {
        super(game);
        mGame = game;
    }
    
    @Override
    public void dispose() {
    	mBatch.dispose();
        mStage.dispose();        
    }

    @Override
    public void render(float paramFloat) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);        
        
		mBatch.begin();
		mBackground.draw(mBatch);
		mBatch.end();
        
        mStage.act();
        mStage.draw();
    }

    @Override
    public void show() {
        mStage = new BaseStage(this);
        Gdx.input.setInputProcessor(mStage);
        Gdx.input.setCatchBackKey(true);
        
        mPref = Gdx.app.getPreferences("LoginInfo");
        
        // BackGround bg = new BackGround(AssetsManager.getInstance().mBackgroundTexture);
        // mStage.addActor(bg);
        mBatch = new SpriteBatch();
        mBackground = new Sprite(AssetsManager.getInstance().mMenuBackgroundTexture);
		mBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
		mInputTextField = new TextField("", UIUtil.getTextFieldStyle());
		mInputTextField.setBounds(765, 750, 350, 100);
		mInputTextField.setText(mPref.getString("account"));
        mStage.addActor(mInputTextField);
        
        TextureRegion[][] textureRegion = TextureRegion.split(AssetsManager.getInstance().mLoginTexture, 400, 105);
        ImageButton loginBtn = new ImageButton(new TextureRegionDrawable(textureRegion[0][0]),
        		new TextureRegionDrawable(textureRegion[0][1]));
        loginBtn.setBounds(765, 400, 350, 100);
        loginBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // startLoginIn();
                String inputText = mInputTextField.getText();
                if (inputText != null && !inputText.isEmpty()) {
                    if (mLoadingDialog == null) {
                        mLoadingDialog = new LoadingDialog();
                    }
                    mStage.addActor(mLoadingDialog);
                    
                    PlayerInfo info = Constant.PLAYERS.get(inputText);
                    if (info != null) {
                        mPref.putString("account", inputText);
                        long time = System.currentTimeMillis();
                        mPref.putLong("time", time);
                        mPref.flush();
                        mGame.setScreen(new MainMenuScreen(mGame));
                    }
                }
            	// mStage.addActor(new ChatDialog());
            }
        });
        mStage.addActor(loginBtn);
    }

    @Override
    public void doBackKeyAction() {
        if (mLoadingDialog != null && mLoadingDialog.hasParent()) {
            verifyOk();
        } else {
            super.doBackKeyAction();
        }
    }
    
    private void startLoginIn() {
        String inputText = mInputTextField.getText();
        if (inputText != null && !inputText.isEmpty()) {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog();
            }
            mStage.addActor(mLoadingDialog);
            
            PlayerInfo info = Constant.PLAYERS.get(inputText);
            if (info != null) {
                mPref.putString("account", inputText);
                long time = System.currentTimeMillis();
                mPref.putLong("time", time);
                mPref.flush();
            }
        }
    }
    
    private void verifyOk() {
        if (mLoadingDialog != null) {
            mLoadingDialog.remove();
//          mGame.setMyInfo(info);
            mGame.setScreen(new MainMenuScreen(mGame));
        }

    }
}
