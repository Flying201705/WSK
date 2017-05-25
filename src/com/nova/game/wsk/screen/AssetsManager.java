package com.nova.game.wsk.screen;

import nova.common.game.wsk.util.CardConstant;
import nova.common.game.wsk.util.ChineseUtil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;

public class AssetsManager {

	private static AssetsManager mInstance;
	public Texture mBackgroundTexture;
	public Texture mMenuBackgroundTexture;
	public Texture mOutCardTexture;
	public Texture mOutLastCardTexture;
	public Texture mTrumpTexture;
	public Texture mPassTexture;
	public Texture mSkipTexture;
	public Texture[] mCardTextures;
	public Texture mCardBgTexture;
	public Texture mScoreNumberTexture;
	public Texture mTimeNumberTexture;
	public Texture mTimeBackgroundTexture;
	public Texture mLoginTexture;
	public Texture mJoinGameTexture;
	public Texture mLocalGameTexture;
	public Texture mTestGameTexture;
	public Texture mStartGameTexture;
	public Texture mRoomCreateTexture;
	public Texture mRoomJoinTexture;
	public Texture mRoomNumBackground;
	public Texture mCardValueTexture;
	public Texture mCardColorTexture;
	public Texture[] mHeadTextures;
	public Texture mHeaderBackgroundTexture;
	public Texture mNameBackgroundTexture;
	public Texture mHeaderPrimaryBackgroundTexture;
	public Texture mNamePrimaryBackgroundTexture;
	public Texture mHeaderSecondaryBackgroundTexture;
	public Texture mNameSecondaryBackgroundTexture;
	public Texture mFlagBankerTexture;
	public Texture mFlagResultTexture;
	public Texture mFlagTrumpTexture;
	public Texture mLastCardBackTexture;
	public Texture mPayTributeTexture;
	public Texture mBackTributeTexture;
	public Texture mSendMessageTexture;
	public Texture mErrorOutCardTexture;
	
	public Music mBackgroundMusic;
	public Sound mPassSound;
	public Sound mOutCardSound;
	public Sound mWinSound;
	public Sound mLoseSound;
	
	public FreeTypeFontGenerator mFontGenerator;
	public BitmapFont mFont;
	
	private AssetsManager() {
	}
	
	public static AssetsManager getInstance() {
		if (mInstance == null) {
			mInstance = new AssetsManager();
		}
		return mInstance;
	}
	
	public void loadAssets() {
		loadTexture();
		loadAudio();
		loadFont();
	}
	
	private void loadTexture() {
		mBackgroundTexture = new Texture(Gdx.files.internal("drawable/background.png"));
		mBackgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mMenuBackgroundTexture = new Texture(Gdx.files.internal("drawable/main_background.png"));
		mMenuBackgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		mOutCardTexture = new Texture(Gdx.files.internal("drawable/bt_outcard.png"));
		mOutLastCardTexture = new Texture(Gdx.files.internal("drawable/bt_out_last_card.png"));
		mTrumpTexture = new Texture(Gdx.files.internal("drawable/bt_call_trump.png"));
		mPassTexture = new Texture(Gdx.files.internal("drawable/bt_no_call.png"));
		mSkipTexture = new Texture(Gdx.files.internal("drawable/bt_skip.png"));
		
		mCardTextures = new Texture[CardConstant.CARD_COUNT];
		for (int i = 0; i < CardConstant.CARD_COUNT; i++) {
			if (CardConstant.CARD_DEFAULT == i) {
				mCardTextures[i] = new Texture(Gdx.files.internal("drawable/card_default.png"));
			} else if (i >= CardConstant.CARD_FANG_A && i < CardConstant.CARD_MEI_A) {
				mCardTextures[i] = new Texture(Gdx.files.internal("drawable/card_fang" + i + ".png"));
			} else if (i >= CardConstant.CARD_MEI_A && i < CardConstant.CARD_TAO_A) {
				mCardTextures[i] = new Texture(Gdx.files.internal("drawable/card_mei" + (i - CardConstant.CARD_MEI_A + 1) + ".png"));
			} else if (i >= CardConstant.CARD_TAO_A && i < CardConstant.CARD_XIN_A) {
				mCardTextures[i] = new Texture(Gdx.files.internal("drawable/card_tao" + (i - CardConstant.CARD_TAO_A + 1) + ".png"));
			} else if (i >= CardConstant.CARD_XIN_A && i < CardConstant.CARD_KING_BEGIN) {
				mCardTextures[i] = new Texture(Gdx.files.internal("drawable/card_xin" + (i - CardConstant.CARD_XIN_A + 1) + ".png"));
			} else {
				mCardTextures[i] = new Texture(Gdx.files.internal("drawable/card_gui" + (i - CardConstant.CARD_KING_BEGIN + 1) + ".png"));
			}
			mCardTextures[i].setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
		
		mScoreNumberTexture = new Texture(Gdx.files.internal("drawable/score_number.png"));
		mTimeNumberTexture = new Texture(Gdx.files.internal("drawable/time_number.png"));
		mTimeBackgroundTexture = new Texture(Gdx.files.internal("drawable/time_clock.png"));
		mLoginTexture = new Texture(Gdx.files.internal("drawable/btn_login.png"));
		mJoinGameTexture = new Texture(Gdx.files.internal("drawable/btn_game_fast.png"));
		mLocalGameTexture = new Texture(Gdx.files.internal("drawable/btn_game_local.png"));
		mTestGameTexture = new Texture(Gdx.files.internal("drawable/btn_game_test.png"));
		mStartGameTexture = new Texture(Gdx.files.internal("drawable/btn_game_start.png"));
		mRoomCreateTexture = new Texture(Gdx.files.internal("drawable/btn_room_create.png"));
		mRoomJoinTexture = new Texture(Gdx.files.internal("drawable/btn_room_join.png"));
		mRoomNumBackground = new Texture(Gdx.files.internal("drawable/num_room_background.png"));
		mCardValueTexture = new Texture(Gdx.files.internal("drawable/card_value.png"));
		mCardColorTexture = new Texture(Gdx.files.internal("drawable/card_color_s.png"));
		mCardBgTexture = new Texture(Gdx.files.internal("drawable/card_bg.png"));
		
		mHeadTextures = new Texture[17];
		for (int i = 0; i < mHeadTextures.length; i++) {
			mHeadTextures[i] = new Texture(Gdx.files.internal("drawable/face/head_" + i + ".png"));
		}
		mHeaderBackgroundTexture = new Texture(Gdx.files.internal("drawable/face/header_background.png"));
		mHeaderPrimaryBackgroundTexture = new Texture(Gdx.files.internal("drawable/face/header_primary_background.png"));
		mHeaderSecondaryBackgroundTexture = new Texture(Gdx.files.internal("drawable/face/header_secondary_background.png"));
		mNameBackgroundTexture = new Texture(Gdx.files.internal("drawable/face/name_background.png"));
		mNamePrimaryBackgroundTexture = new Texture(Gdx.files.internal("drawable/face/name_primary_background.png"));
		mNameSecondaryBackgroundTexture = new Texture(Gdx.files.internal("drawable/face/name_secondary_background.png"));
		mFlagBankerTexture = new Texture(Gdx.files.internal("drawable/flag_banker.png"));
		mFlagResultTexture = new Texture(Gdx.files.internal("drawable/flag_end.png"));
		mFlagTrumpTexture = new Texture(Gdx.files.internal("drawable/point.png"));
		mErrorOutCardTexture = new Texture(Gdx.files.internal("drawable/out_card_error.png"));
		mLastCardBackTexture = new Texture(Gdx.files.internal("drawable/bt_look_last_card.png"));
		mPayTributeTexture = new Texture(Gdx.files.internal("drawable/bt_pay_tribute.png"));
		mBackTributeTexture = new Texture(Gdx.files.internal("drawable/bt_back_tribute.png"));
		mSendMessageTexture = new Texture(Gdx.files.internal("drawable/btn_message.png"));
	}
	
	private void loadAudio() {
		mBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/background.mp3"));
		mPassSound = Gdx.audio.newSound(Gdx.files.internal("sound/game_m_pass0.ogg"));
		mOutCardSound = Gdx.audio.newSound(Gdx.files.internal("sound/outcard.ogg"));
		mWinSound = Gdx.audio.newSound(Gdx.files.internal("sound/win.ogg"));
		mLoseSound = Gdx.audio.newSound(Gdx.files.internal("sound/lose.ogg"));
	}
	
	private void loadFont() {
	    mFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/font.ttf"));
		mFont = mFontGenerator.generateFont(35, FreeTypeFontGenerator.DEFAULT_CHARS + 
				"聊级进士总局数胜率性别男女测试一下哈满的幸福风口飞猪三叉巨魔战将奔跑蜗牛倔强电脑房间确定为空加入退出取消您要游戏吗语音发送快点吧我等花儿都谢了大家好很高兴见到又断线网络怎么这差和你合作真是太愉们交个朋友能不告诉联系方式呀还吵有什专心玩走决天亮各意思离开会再想念息长按说话松结束击输", false);
//		mFont = new BitmapFont(fondData, fondData.getTextureRegion(), false);
	}
}
