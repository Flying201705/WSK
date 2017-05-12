package com.nova.game.net;

import java.io.FileInputStream;

import nova.common.game.wsk.util.GameCommand;

import com.nova.game.net.util.NetWorkUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class ChannelManager {
	private static ChannelManager mInstance;
	private Channel mChannel;
	private int mNetType;
	
	private ChannelManager() {
		
	}
	
	public static ChannelManager getInstance() {
		if (mInstance == null) {
			mInstance = new ChannelManager();
		}
		return mInstance;
	}
	
	public void initChannel(Channel ch) {
		mChannel = ch;
	}
	
	public void updateNetType(int type) {
		mNetType = type;
		
		if (mNetType > 0) {
			isChannelActive();
		}
	}
	
	private boolean isChannelActive() {
		if (mChannel != null && mChannel.isActive()) {
			return true;
		}
		connect();
		return false;
	}
	
	public void connect() {
		// 与netty服务器通信
		NettyClient client = new NettyClient(NetWorkUtil.HOST, NetWorkUtil.PORT);
		client.start();
	}

	public void sendMessage(String message) {
		if (!isChannelActive()) {
			return;
		}
		
		ByteBuf messageData = Unpooled.buffer();
		messageData.writeInt(GameCommand.COM_GAME_MESSAGE);
		messageData.writeInt(message.length());
		messageData.writeBytes(message.getBytes());
		android.util.Log.e("zhangxx", "sendMessage : " + message);
		mChannel.writeAndFlush(messageData);
	}

	public void sendText(int room, int playerId, String text) {
		if (!isChannelActive()) {
			return;
		}
		
		ByteBuf bytebuf = Unpooled.buffer();
		bytebuf.writeInt(GameCommand.COM_OTHER_MESSAGE);
		// type: 0-文本 1-语音 2-文本&语音
		bytebuf.writeInt(0);
		bytebuf.writeInt(room);
		bytebuf.writeInt(playerId);
		android.util.Log.e("zhangxx", "room : " + room + ", text = " + text);
		bytebuf.writeInt(text.getBytes().length);
		bytebuf.writeBytes(text.getBytes());
		mChannel.writeAndFlush(bytebuf);
	}
	
	public void sendVoice(int room, int playerId, String file) {
		if (!isChannelActive()) {
			return;
		}
		
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] filebuf = new byte[fis.available()];
			fis.read(filebuf);
			fis.close();
			ByteBuf bytebuf = Unpooled.buffer();
			bytebuf.writeInt(GameCommand.COM_OTHER_MESSAGE);
			// type: 0-文本 1-语音 2-文本&语音
			bytebuf.writeInt(1);
			bytebuf.writeInt(room);
			bytebuf.writeInt(playerId);
			android.util.Log.e("zhangxx", "room : " + room + ", length = " + filebuf.length);
			bytebuf.writeInt(filebuf.length);
			bytebuf.writeBytes(filebuf);
			mChannel.writeAndFlush(bytebuf);
		} catch (Exception e) {
			
		}
	}
	
	public void sendTextAndVoice(int room, int playerId, String position) {
		if (!isChannelActive()) {
			return;
		}
		
		ByteBuf bytebuf = Unpooled.buffer();
		bytebuf.writeInt(GameCommand.COM_OTHER_MESSAGE);
		// type: 0-文本 1-语音 2-文本&语音
		bytebuf.writeInt(2);
		bytebuf.writeInt(room);
		bytebuf.writeInt(playerId);
		android.util.Log.e("zhangxx", "room : " + room + ", position = " + position);
		bytebuf.writeInt(position.getBytes().length);
		bytebuf.writeBytes(position.getBytes());
		mChannel.writeAndFlush(bytebuf);
	}
}
