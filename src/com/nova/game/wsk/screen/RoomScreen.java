package com.nova.game.wsk.screen;

import nova.common.game.wsk.data.PlayerInfo;
import nova.common.game.wsk.room.RoomController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nova.game.wsk.BaseScreen;
import com.nova.game.wsk.BaseStage;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.actor.BackGround;
import com.nova.game.wsk.actor.Player;
import com.nova.game.wsk.handler.GameRequestDispatcher;
import com.nova.game.wsk.model.RoomInfoController;
import com.nova.game.wsk.screen.AssetsManager;

public class RoomScreen extends BaseScreen {

    private MainGame mGame;
    private BaseStage mStage;
    private SpriteBatch mBatch;
    Sprite mBackground;
    private boolean mIsRoomOwner;
    
    public RoomScreen(MainGame game, boolean isOwner) {
        super(game);
        mGame = game;
        mIsRoomOwner = isOwner;
        
        String account = Gdx.app.getPreferences("LoginInfo").getString("account");
        PlayerInfo myInfo = Constant.PLAYERS.get(account);
        RoomInfoController.getInstance().getPlayerInfos().put(0, myInfo);
    }
    
    public RoomScreen(MainGame game) {
    	this(game, false);
    }
    
    @Override
    public void dispose() {
    	mBatch.dispose();
	    if (mStage != null) {
	        mStage.dispose();
	    }
		
	}

    @Override
    public void render(float arg0) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (RoomInfoController.getInstance().isRoomStart()) {
        	mGame.setScreen(new GameScreen(mGame));
        	return;
        }
        
        mBatch.begin();
        mBackground.draw(mBatch);
        mBatch.draw(AssetsManager.getInstance().mRoomNumBackground, Constant.DEFAULT_WIDTH - 350, Constant.DEFAULT_HEIGHT - 150);
        AssetsManager.getInstance().mFont.draw(mBatch, "房间 " + RoomInfoController.getInstance().getRoomId(), 
        		Constant.DEFAULT_WIDTH - 280, Constant.DEFAULT_HEIGHT - 100);
        mBatch.end();

        mStage.act();
        mStage.draw();
    }

    @Override
    public void show() {
    	mBatch = new SpriteBatch();
        
        mStage = new BaseStage(this);
        Gdx.input.setInputProcessor(mStage);
        Gdx.input.setCatchBackKey(true);
        
        // Texture bg = new Texture(Gdx.files.internal("drawable/background_menu.png"));
        // BackGround Background = new BackGround(bg);
        mBatch = new SpriteBatch();
        mBackground = new Sprite(AssetsManager.getInstance().mMenuBackgroundTexture);
		mBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        TextureRegion[][] start_button = TextureRegion.split(AssetsManager.getInstance().mStartGameTexture, 400, 105);
        ImageButton mStartGameButton = new ImageButton(new TextureRegionDrawable(start_button[0][0]), new TextureRegionDrawable(start_button[0][1]));
        mStartGameButton.setPosition(1500, 100);
        mStartGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // mGame.setScreen(new GameScreen());
            	requestStartGame();
            }
        });
        
        Player player_0 = new Player();
        player_0.setPosition(800, 200);
        player_0.setIndex(0);
        
        Player player_1 = new Player();
        player_1.setPosition(1100, 500);
        player_1.setIndex(1);
        player_1.addListener(new ClickListener() {
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		RoomInfoController.getInstance().replacePlayerPosition(1);
        	}
        });
        
        Player player_2 = new Player();
        player_2.setPosition(800, 800);
        player_2.setIndex(2);
        player_2.addListener(new ClickListener() {
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		RoomInfoController.getInstance().replacePlayerPosition(2);
        	}
        });
        
        Player player_3 = new Player();
        player_3.setPosition(500, 500);
        player_3.setIndex(3);
        player_3.addListener(new ClickListener() {
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		RoomInfoController.getInstance().replacePlayerPosition(3);
        	}
        });
        
        // mStage.addActor(Background);
        mStage.addActor(player_0);
        mStage.addActor(player_1);
        mStage.addActor(player_2);
        mStage.addActor(player_3);
        if (mIsRoomOwner) {
        	mStage.addActor(mStartGameButton);
        }
    }
    
    private void requestStartGame() {
    	GameRequestDispatcher request = new GameRequestDispatcher();
		request.startGame(RoomInfoController.getInstance().getRoomId());
    }
}
