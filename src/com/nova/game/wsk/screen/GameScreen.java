package com.nova.game.wsk.screen;

import nova.common.game.wsk.data.PlayerInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nova.game.dialog.ChatDialog;
import com.nova.game.dialog.GameExitDialog;
import com.nova.game.dialog.PlayerInfoDialog;
import com.nova.game.dialog.GameExitDialog.onExitListener;
import com.nova.game.wsk.BaseScreen;
import com.nova.game.wsk.BaseStage;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.actor.BackGround;
import com.nova.game.wsk.actor.CheckButton;
import com.nova.game.wsk.actor.ControlButton;
import com.nova.game.wsk.actor.CountDown;
import com.nova.game.wsk.actor.GamePlayer;
import com.nova.game.wsk.actor.LastCards;
import com.nova.game.wsk.actor.MyCards;
import com.nova.game.wsk.actor.OutCards;
import com.nova.game.wsk.actor.OutLastCards;
import com.nova.game.wsk.actor.Result;
import com.nova.game.wsk.actor.Score;
import com.nova.game.wsk.actor.Trump;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.model.RoomInfoController;

public class GameScreen extends BaseScreen {

	protected SpriteBatch mBatch;
	private BaseStage mStage;
	
	private float mWidth = Constant.DEFAULT_WIDTH;
	private float mHeight = Constant.DEFAULT_HEIGHT;
	
	private GameController mGameManager;
	private GameExitDialog mExitDialog;
	private ChatDialog mChatDialog;
	
	private onExitListener mExitListener = new onExitListener() {
		@Override
		public void exit() {
			mGameManager.stopGame();
			mGame.goBack();
		}
	};
	
	public GameScreen(MainGame game) {
	    super(game);
		mGameManager = GameController.create(getControllerType());
		mGameManager.startGame();
	}
	
	protected String getControllerType() {
		return "join";
	}
	
	@Override
	public void dispose() {
		mBatch.dispose();
		mStage.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	    mStage.act();
	    mStage.draw();
	}

	@Override
	public void show() {
		mBatch = new SpriteBatch();
		
		mStage = new BaseStage(this);
		Gdx.input.setInputProcessor(mStage);
		Gdx.input.setCatchBackKey(true);
		
		BackGround bg = new BackGround(AssetsManager.getInstance().mBackgroundTexture);
		mStage.addActor(bg);
		
		Trump trump = new Trump();
		trump.setPosition(100, mHeight - 180);
		mStage.addActor(trump);
		
		Score score = new Score();
		score.setPosition(mWidth - 178, mHeight - 170);
		mStage.addActor(score);
		
		TextureRegion[][] smTR = TextureRegion.split(AssetsManager.getInstance().mSendMessageTexture, 100, 100);
		ImageButton smBtn = new ImageButton(new TextureRegionDrawable(smTR[0][0]), new TextureRegionDrawable(smTR[0][1]));
		smBtn.setPosition(50, 350);
		smBtn.addListener(mSendMessageBtnListener);
		mStage.addActor(smBtn);
		
		ControlButton cb = new ControlButton();
		mStage.addActor(cb);
		
		CheckButton chb = new CheckButton();
		mStage.addActor(chb);
		
		MyCards myCards = new MyCards();
		myCards.setPosition(0, 80);
		mStage.addActor(myCards);
		
		LastCards lastCards = new LastCards();
		mStage.addActor(lastCards);
		
		OutCards outCards = new OutCards();
		mStage.addActor(outCards);
		
		OutLastCards outLastCards = new OutLastCards();
		mStage.addActor(outLastCards);
		
		setPlayer();
		
		CountDown countDown = new CountDown();
		mStage.addActor(countDown);
		
		Result result = new Result();
		mStage.addActor(result);
		
		AssetsManager.getInstance().mBackgroundMusic.play();
		AssetsManager.getInstance().mBackgroundMusic.setVolume(0.1f);
		AssetsManager.getInstance().mBackgroundMusic.setLooping(true);
		
		mChatDialog = new ChatDialog();
		mChatDialog.setVisible(false);
		mStage.addActor(mChatDialog);
		
		mExitDialog = new GameExitDialog();
		mExitDialog.setExitListener(mExitListener);
		mExitDialog.setVisible(false);
		mStage.addActor(mExitDialog);
	}
	
	@Override
	public void doBackKeyAction() {
		if (mChatDialog.isVisible()) {
			mChatDialog.setVisible(false);
			return;
		}
		
		if (mExitDialog.isVisible()) {
			mExitDialog.setVisible(false);
		} else {
			mExitDialog.setVisible(true);
		}
	}
	
    private void setPlayer() {
        for (int i = 0; i < 4; i++) {
        	PlayerInfo info = null;//mGameManager.getPlayerInfos().get(getPlayerId(i));
        	if (info == null) {
        		info = new PlayerInfo();
        	}
            final GamePlayer player = new GamePlayer();
            
            float headX = 0;
            float headY = 0;

            if (i == 0) {
                headX = 30;
                headY = 30;
            } else if (i == 1) {
                headX = mWidth - 230;
                headY = mHeight - 500;
            } else if (i == 2) {
                headX = mWidth / 2 - 104;
                headY = mHeight - 210;
            } else if (i == 3) {
                headX = 30;
                headY = mHeight - 500;
            }
            
            player.setPosition(headX, headY);
            player.setIndex(i);
            player.addListener(new ClickListener() {
            	@Override
            	public void clicked(InputEvent event, float x, float y) {
            		PlayerInfo info = RoomInfoController.getInstance().getPlayerInfos().get(player.getPlayerId());
            		if (info == null) {
            			return;
            		}
            		mStage.addActor(new PlayerInfoDialog(player.getX() + 100 , player.getY(), info));
            	}
            });
            mStage.addActor(player);
        }
    }
    
    private ClickListener mSendMessageBtnListener = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			mChatDialog.setVisible(true);
		};
	};
}
