package com.nova.game.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nova.game.wsk.BaseDialog;
import com.nova.game.wsk.UIUtil;

public class JoinRoomDialog extends BaseDialog {
    
    private String mInput = "";
    private TextField mTextField;
    private ImageButton mCancelBtn;
    private ImageButton mOkBtn;
    private ImageButton mDeleteBtn;
    private ImageButton mOneBtn;
    private ImageButton mTwoBtn;
    private ImageButton mThreeBtn;
    private ImageButton mFourBtn;
    private ImageButton mFiveBtn;
    private ImageButton mSixBtn;
    private ImageButton mSevenBtn;
    private ImageButton mEightBtn;
    private ImageButton mNineBtn;
    private ImageButton mZeroBtn;
    
    private ClickListener mCancelClickListener = new ClickListener() {
    	public void clicked(InputEvent event, float x, float y) {
    		mInput = "";
            mTextField.setText(mInput);
            setVisible(false);
    	};
    };
    
    private ClickListener mDeleteClickListener = new ClickListener() {
        public void clicked(InputEvent event, float x, float y) {
            if (!mInput.isEmpty()) {
                mInput = mInput.substring(0, (mInput.length() - 1));
                mTextField.setText(mInput);
            }
        };
    };
    
    private ClickListener mNumberClickListener  = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            Actor actor = event.getListenerActor();
            if (actor instanceof ImageButton) {
                mInput = mInput + ((ImageButton) actor).getName();
                mTextField.setText(mInput);
            }
        };
    };
    
    public JoinRoomDialog(String title) {
        super(title);
        
        this.setBounds(420, 180, 1080, 720);
        
        mTextField = new TextField("", UIUtil.getTextFieldStyle());
        mTextField.setTouchable(Touchable.disabled);
        mTextField.setBounds(1, 515, 1078, 100);
        this.addActor(mTextField);
        
        mCancelBtn = new ImageButton(UIUtil.file2Drawable("drawable/icon_close.png"));
        mCancelBtn.setBounds(1020, 440, 39, 38);
        
        mOkBtn = UIUtil.generateImageButton("drawable/btn_ok.png", 218, 80);
        mOkBtn.setBounds(725, 2, 350, 100);
        
        mDeleteBtn = UIUtil.generateImageButton("drawable/btn_delete.png", 218, 80);
        mDeleteBtn.setBounds(5, 2, 350, 100);
        
        mOneBtn = UIUtil.generateImageButton("drawable/btn_num_1.png", 218, 80);
        mOneBtn.setBounds(5, 302, 350, 100);
        mOneBtn.setName("1");
        mTwoBtn = UIUtil.generateImageButton("drawable/btn_num_2.png", 218, 80);
        mTwoBtn.setBounds(365, 302, 350, 100);
        mTwoBtn.setName("2");
        mThreeBtn = UIUtil.generateImageButton("drawable/btn_num_3.png", 218, 80);
        mThreeBtn.setBounds(725, 302, 350, 100);
        mThreeBtn.setName("3");
        mFourBtn = UIUtil.generateImageButton("drawable/btn_num_4.png", 218, 80);
        mFourBtn.setBounds(5, 202, 350, 100);
        mFourBtn.setName("4");
        mFiveBtn = UIUtil.generateImageButton("drawable/btn_num_5.png", 218, 80);
        mFiveBtn.setBounds(365, 202, 350, 100);
        mFiveBtn.setName("5");
        mSixBtn = UIUtil.generateImageButton("drawable/btn_num_6.png", 218, 80);
        mSixBtn.setBounds(725, 202, 350, 100);
        mSixBtn.setName("6");
        mSevenBtn = UIUtil.generateImageButton("drawable/btn_num_7.png", 218, 80);
        mSevenBtn.setBounds(5, 102, 350, 100);
        mSevenBtn.setName("7");
        mEightBtn = UIUtil.generateImageButton("drawable/btn_num_8.png", 218, 80);
        mEightBtn.setBounds(365, 102, 350, 100);
        mEightBtn.setName("8");
        mNineBtn = UIUtil.generateImageButton("drawable/btn_num_9.png", 218, 80);
        mNineBtn.setBounds(725, 102, 350, 100);
        mNineBtn.setName("9");
        mZeroBtn = UIUtil.generateImageButton("drawable/btn_num_0.png", 218, 80);
        mZeroBtn.setBounds(365, 2, 350, 100);
        mZeroBtn.setName("0");
        
        mCancelBtn.addListener(mCancelClickListener);
        mDeleteBtn.addListener(mDeleteClickListener);
        mOneBtn.addListener(mNumberClickListener);
        mTwoBtn.addListener(mNumberClickListener);
        mThreeBtn.addListener(mNumberClickListener);
        mFourBtn.addListener(mNumberClickListener);
        mFiveBtn.addListener(mNumberClickListener);
        mSixBtn.addListener(mNumberClickListener);
        mSevenBtn.addListener(mNumberClickListener);
        mEightBtn.addListener(mNumberClickListener);
        mNineBtn.addListener(mNumberClickListener);
        mZeroBtn.addListener(mNumberClickListener);

        this.addActor(mCancelBtn);
        this.addActor(mOkBtn);
        this.addActor(mDeleteBtn);
        this.addActor(mOneBtn);
        this.addActor(mTwoBtn);
        this.addActor(mThreeBtn);
        this.addActor(mFourBtn);
        this.addActor(mFiveBtn);
        this.addActor(mSixBtn);
        this.addActor(mSevenBtn);
        this.addActor(mEightBtn);
        this.addActor(mNineBtn);
        this.addActor(mZeroBtn);
    }
    
    public int getInput() {
    	if (mInput == null || mInput.isEmpty()) {
    		return -1;
    	}
    	
    	return Integer.parseInt(mInput);
    }
    
    public void setJoinClickListener(ClickListener listener) {
    	mOkBtn.addListener(listener);
    }
    
}
