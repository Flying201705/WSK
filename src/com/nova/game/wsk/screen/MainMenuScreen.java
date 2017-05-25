package com.nova.game.wsk.screen;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nova.game.dialog.JoinRoomDialog;
import com.nova.game.dialog.PromptDialog;
import com.nova.game.wsk.BaseScreen;
import com.nova.game.wsk.BaseStage;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.WXInfo;
import com.nova.game.wsk.handler.GameRequestDispatcher;
import com.nova.game.wsk.model.RoomInfoController;

public class MainMenuScreen extends BaseScreen {
	
	private MainGame mGame;
	private SpriteBatch mBatch;
	private BaseStage mStage;
	private OrthographicCamera mCamera;
	private Sprite mBackground;
	private ImageButton mLocalGameButton;
	private ImageButton mTestGameButton;
	private ImageButton mJoinGameButton;
	
	private JoinRoomDialog mJoinRoomDialog;
	private PromptDialog mPromptDialog;
	private Label mLabel;
	
	private WXInfo mWxInfo;
	private BitmapFont mFont;
	
	private boolean mIsUpdateScreen = false;

	public MainMenuScreen(MainGame game) {
	    super(game);
		mGame = game;
	}
	
	@Override
	public void dispose() {
		mBatch.dispose();
		mStage.dispose();
		if (mFont != null) {
		    mFont.dispose();
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (mIsUpdateScreen) {
		    updateScreen();
		}
		
		mBatch.setProjectionMatrix(mCamera.combined);
		mBatch.begin();
		mBackground.draw(mBatch);
		// AssetsManager.getInstance().mFont.draw(mBatch, "测试一下哈", 500, 500);
		mBatch.draw(new Texture(Gdx.files.internal("drawable/wsk.png")), 805, Constant.DEFAULT_HEIGHT - 400);
		mBatch.end();
		
		mStage.act(delta);
		mStage.draw();
	}

	@Override
	public void show() {
	    float width = Constant.DEFAULT_WIDTH;
	    float height = Constant.DEFAULT_HEIGHT;
	    
		mStage = new BaseStage(this);
		Gdx.input.setInputProcessor(mStage);
		Gdx.input.setCatchBackKey(true);

		mCamera = new OrthographicCamera(width, height);
		mCamera.setToOrtho(false);
		mBatch = new SpriteBatch();
		
		mBackground = new Sprite(AssetsManager.getInstance().mMenuBackgroundTexture);
		mBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		mJoinGameButton = createButton(AssetsManager.getInstance().mJoinGameTexture);
		mJoinGameButton.setPosition(1380, height - 440);
		mJoinGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				requestFastGame();
			}
		});
		
		mLocalGameButton = createButton(AssetsManager.getInstance().mLocalGameTexture);
		mLocalGameButton.setPosition(1080, height - 660);
		mLocalGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				mGame.setScreen(new LocalGameScreen(mGame));
			}
		});
		
		ImageButton mCreateRoomButton = createButton(AssetsManager.getInstance().mRoomCreateTexture);
		mCreateRoomButton.setPosition(140, height - 440);
		mCreateRoomButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				requestCreateRoom();
			}
		});

		ImageButton mAddRoomButton = createButton(AssetsManager.getInstance().mRoomJoinTexture);
        mAddRoomButton.setPosition(450, height - 660);
        mAddRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showJoinRoomDialog();
            }
        });
        
		mTestGameButton = createButton(AssetsManager.getInstance().mTestGameTexture);
		mTestGameButton.setPosition(755, height - 845);
		mTestGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				mGame.setScreen(new TestGameScreen(mGame));
			}
		});
		
		mStage.addActor(mLocalGameButton);
		mStage.addActor(mCreateRoomButton);
		mStage.addActor(mAddRoomButton);
		mStage.addActor(mTestGameButton);
		mStage.addActor(mJoinGameButton);
		
		mWxInfo = WXInfo.getInstance();
		Image head = new Image(byte2Texture(mWxInfo.getHead()));
		mFont = AssetsManager.getInstance().mFontGenerator.generateFont(36, mWxInfo.getNickName(), false);
		LabelStyle labelStyle = new LabelStyle(mFont, Color.BLACK);
		Label name = new Label( mWxInfo.getNickName(), labelStyle);
		name.setPosition(150, 80);
		mStage.addActor(head);
		mStage.addActor(name);
	}
	
	private ImageButton createButton(Texture texture) {
		TextureRegion[][] textureRegion = TextureRegion.split(texture, 400, 105);
		return new ImageButton(new TextureRegionDrawable(textureRegion[0][0]), new TextureRegionDrawable(textureRegion[0][1]));
	}

	@Override
	public void doBackKeyAction() {
	    if (mPromptDialog != null && mPromptDialog.isVisible()) {
	        mPromptDialog.setVisible(false);
	    } else if (mJoinRoomDialog != null && mJoinRoomDialog.hasParent()) {
//	        mJoinRoomDialog.setVisible(false);
	        mJoinRoomDialog.hide();
	    } else {
	        super.doBackKeyAction();
	    }
	}
	
	private void updateScreen() {
		if (RoomInfoController.getInstance().isRoomStart()) {
		    mIsUpdateScreen = false;
			mGame.setScreen(new GameScreen(mGame));
		} else if (RoomInfoController.getInstance().isRoomCreated()) {
		    mIsUpdateScreen = false;
			mGame.setScreen(new RoomScreen(mGame, true));
		} else if (RoomInfoController.getInstance().isRoomJoin()) {
		    mIsUpdateScreen = false;
			if (RoomInfoController.getInstance().getRoomId() >= 0) {
				mGame.setScreen(new RoomScreen(mGame));
			} else {
				showPromptDialog(-1);
			}
		}
	}
	
    private void showJoinRoomDialog() {
        if (mJoinRoomDialog == null) {
            mJoinRoomDialog = new JoinRoomDialog("Room Id");
        }
    	mJoinRoomDialog.setJoinClickListener(new ClickListener() {
    		@Override
    		public void clicked(InputEvent event, float x, float y) {
    			int input = mJoinRoomDialog.getInput();
    			if (input < 0) {
    				return;
    			}
    			
    			requestJoinRoom(input);
    		}
    	});
    	mJoinRoomDialog.show(mStage);
    }
    
    private void showPromptDialog(int errorCode) {
    	String title = "房间为空";
    	if (errorCode == -2) {
    		title = "房间已满";
    	} else if (errorCode == -3) {
    		title = "游戏已开始";
    	}
    	
        if (mLabel == null) {
            mLabel = new Label(title, new LabelStyle(AssetsManager.getInstance().mFont, Color.BLACK));
        }
    	if (mPromptDialog == null) {
    	    mPromptDialog = new PromptDialog("");
    	    mPromptDialog.text(mLabel);
    	}
    	mLabel.setText(title);
    	mStage.addActor(mPromptDialog);
    	mPromptDialog.setVisible(true);
    }
    
    private void requestFastGame() {
    	GameRequestDispatcher request = new GameRequestDispatcher();
		request.initGameInfo();
		mIsUpdateScreen = true;
    }
    
    private void requestCreateRoom() {
    	GameRequestDispatcher request = new GameRequestDispatcher();
		request.createRoom();
		mIsUpdateScreen = true;
    }
    
    private void requestJoinRoom(int room) {
        GameRequestDispatcher request = new GameRequestDispatcher();
		request.joinRoom(room);
		mIsUpdateScreen = true;
    }
    
    public Texture byte2Texture(final byte[] bytes) {
        Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
        // int width = pixmap.getWidth();
        // int height = pixmap.getHeight();
        // int preferWidth = MathUtils.nextPowerOfTwo(width);
        // int preferHeight = MathUtils.nextPowerOfTwo(height);
        Texture texture = new Texture(pixmap);
        // texture.draw(pixmap, 0, 0);
        pixmap.dispose();

        return texture;
    }
}
