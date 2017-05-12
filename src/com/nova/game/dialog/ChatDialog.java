package com.nova.game.dialog;

import android.os.Environment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nova.game.AudioManager;
import com.nova.game.net.ChannelManager;
import com.nova.game.wsk.BaseDialog;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.UIUtil;
import com.nova.game.wsk.model.GameController;
import com.nova.game.wsk.model.RoomInfoController;
import com.nova.game.wsk.screen.AssetsManager;

public class ChatDialog extends BaseDialog {
	// zhangxx add for audio begin
	private com.nova.game.AudioManager mAudioManager;
	// zhangxx add for audio end
	private TextField mTextField;
	private TextButton mVoiceButton;
	private TextButton mSendButton;
	private TextButton mSwitchButton;
	private boolean mIsVoiceMode;
	
	public ChatDialog() {
		super("聊天");
		setBounds(0, 0, 1200, 800);
		setPosition((Constant.DEFAULT_WIDTH - getWidth()) / 2, (Constant.DEFAULT_HEIGHT - getHeight()) / 2);
		
		mTextField = new TextField("", UIUtil.getTextFieldStyle());
		mTextField.setBounds(190, 25, 750, 100);
		mTextField.setMessageText("点击输入");
		mTextField.setTextFieldListener(new TextFieldListener() {
			
			@Override
			public void keyTyped(TextField arg0, char c) {
				android.util.Log.e("zhangxx", "c = " + c);
			}
		});
        addActor(mTextField);
        
        List.ListStyle style = new List.ListStyle();
        style.font = AssetsManager.getInstance().mFont;
        style.font.setColor(Color.DARK_GRAY);
        style.fontColorUnselected = Color.DARK_GRAY;
        style.selection = UIUtil.file2Drawable("drawable/edit_background.png", 4, 4, 4, 4);
        final List<String> list = new List<String>(style);
        list.setItems(Constant.COMMON_CHAT_MESSAGE);
        list.setColor(Color.DARK_GRAY);
        ScrollPane.ScrollPaneStyle sstyle = new ScrollPane.ScrollPaneStyle();
        sstyle.background = UIUtil.file2Drawable("drawable/edit_background.png", 4, 4, 4, 4);
        ScrollPane scrollPane = new ScrollPane(list, sstyle);
        scrollPane.setSize(1100, 500);
        scrollPane.setPosition(40, 160);
        scrollPane.addListener(new ClickListener() {
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		super.clicked(event, x, y);
        		android.util.Log.e("zhangxx", "select item : " + list.getSelectedIndex());
        		RoomInfoController.getInstance().addPlayerMessage(GameController.getInstance().getOwnerPlayerIndex(), list.getSelected());
        		Gdx.audio.newSound(Gdx.files.internal("sound/msg_w_" + list.getSelectedIndex() + ".ogg")).play();
        		ChannelManager.getInstance().sendTextAndVoice(RoomInfoController.getInstance().getRoomId(), 
            			RoomInfoController.getInstance().getPlayerInfos().get(GameController.getInstance().getOwnerPlayerIndex()).getId(), 
            			String.valueOf(list.getSelectedIndex()));
        		setVisible(false);
        	}
        });
        addActor(scrollPane);
		
        mSwitchButton = new TextButton("语音", UIUtil.getTextButtonStyle());
        mSwitchButton.setBounds(30, 25, 150, 100);
        mSwitchButton.addListener(mSwitchListener);
		mVoiceButton = new TextButton("长按 说话", UIUtil.getTextButtonStyle());
		mVoiceButton.setBounds(190, 25, 750, 100);
		mVoiceButton.setVisible(false);
		mVoiceButton.addListener(mVoiceListener);
        mSendButton = new TextButton("发送", UIUtil.getTextButtonStyle());
        mSendButton.setBounds(950, 25, 200, 100);
        mSendButton.addListener(mSendListener);
        
        addActor(mSwitchButton);
        addActor(mVoiceButton);
        addActor(mSendButton);
        
        // zhangxx add for audio begin
        String dir = Environment.getExternalStorageDirectory() + "/wsk/audios/";
		mAudioManager = AudioManager.getInstance(dir);
		// zhangxx add for audio end
	}
	
	private ClickListener mSwitchListener = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			mIsVoiceMode = !mIsVoiceMode;
			if (mIsVoiceMode) {
				mSwitchButton.setText("消息");
				mVoiceButton.setVisible(true);
				mTextField.setVisible(false);
			} else {
				mSwitchButton.setText("语音");
				mVoiceButton.setVisible(false);
				mTextField.setVisible(true);
			}
		};
	};
	
	private ClickListener mSendListener = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			String inputText = mTextField.getText();
			if (inputText != null && !inputText.isEmpty()) {
				RoomInfoController.getInstance().addPlayerMessage(GameController.getInstance().getOwnerPlayerIndex(), inputText);
				ChannelManager.getInstance().sendText(RoomInfoController.getInstance().getRoomId(), 
            			GameController.getInstance().getOwnerPlayerIndex(), inputText);
			}
			setVisible(false);
		};
	};
	
	private ClickListener mVoiceListener = new ClickListener() {
		public void touchUp(InputEvent arg0, float arg1, float arg2, int arg3, int arg4) {
			mVoiceButton.setText("长按 说话");
    		// zhangxx add for audio begin
    		mAudioManager.release();
    		ChannelManager.getInstance().sendVoice(RoomInfoController.getInstance().getRoomId(), 
    			GameController.getInstance().getOwnerPlayerIndex(), mAudioManager.getCurrentFilePath());
    		// zhangxx add for audio end
		}
		
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			mVoiceButton.setText("松开 结束");
    		// zhangxx add for audio begin
    		mAudioManager.prepareAudio();
    		// zhangxx add for audio end
			return super.touchDown(event, x, y, pointer, button);
		}
	};
}
