package com.nova.game;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nova.game.net.ChannelManager;
import com.nova.game.net.NettyClient;
import com.nova.game.net.util.NetWorkUtil;
import com.nova.game.wsk.screen.MainGame;

import android.os.Bundle;

public class MainActivity extends AndroidApplication {

	private NettyClient mClient;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		//   WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGLSurfaceView20API18 = true;
		initialize(new MainGame(), cfg);
		
		// 与netty服务器通信
		ChannelManager.getInstance().connect();
	}
	
	@Override
	public void onBackPressed() {
	    // super.onBackPressed();
	}
}