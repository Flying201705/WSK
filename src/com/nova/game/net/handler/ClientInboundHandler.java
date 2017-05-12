package com.nova.game.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;

import com.nova.game.wsk.handler.FileResponseDispatcher;
import com.nova.game.wsk.handler.GameResponseDispatcher;

public class ClientInboundHandler extends ChannelInboundHandlerAdapter {
	
	GameResponseDispatcher mProcessor = new GameResponseDispatcher();
	FileResponseDispatcher mFileDispatcher = new FileResponseDispatcher();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof HttpResponse) {
			HttpResponse response = (HttpResponse) msg;
			System.out.println("CONTENT_TYPE:"
					+ response.headers().get(HttpHeaders.Names.CONTENT_TYPE));
		}
		if (msg instanceof HttpContent) {
			HttpContent content = (HttpContent) msg;
			ByteBuf buf = content.content();
			System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
			buf.release();
		}
		if (msg instanceof ByteBuf) {
			ByteBuf messageData = (ByteBuf) msg;
			int commandId = messageData.readInt();
			if (commandId >= 5000) {
				mFileDispatcher.processor(commandId, messageData);
			} else {
				int length = messageData.readInt();
				byte[] c = new byte[length];
				messageData.readBytes(c);
				android.util.Log.e("zhangxx", "length = " + length + ", c = " + new String(c));
				mProcessor.processor(commandId, new String(c));
			}
		}
	}
}
