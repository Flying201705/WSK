package com.nova.game.net.handler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ClientReader extends ChannelInboundHandlerAdapter
{
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
	{
		Channel channel = ctx.channel();
		if (evt instanceof IdleStateEvent)
		{
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE)
			{
				channel.close();
			}
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
		android.util.Log.e("zhangxx", "carnnel Read 11");
		byte[] array = (byte[]) msg;
		try
		{
			FileOutputStream fos = new FileOutputStream("/sdcard/cardsh.jpg");
			fos.write(array);
			fos.close();
			ctx.close();
		}
		catch (FileNotFoundException e)
		{
			android.util.Log.e("zhangxx", "carnnel " + e.toString());
			e.printStackTrace();
		}
		catch (IOException e)
		{
			android.util.Log.e("zhangxx", "carnnel " + e.toString());
			e.printStackTrace();
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		ctx.close();
		cause.printStackTrace();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx)
	{

		String flage = "Handshake";
		ByteBuf buf = ctx.alloc().buffer(flage.getBytes().length);
		buf.writeBytes(flage.getBytes());
		ctx.writeAndFlush(buf);
	}
}