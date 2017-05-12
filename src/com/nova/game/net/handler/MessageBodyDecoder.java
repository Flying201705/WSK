package com.nova.game.net.handler;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

class ByteHelp
{
	public static byte[] IntToBytes(int a)
	{
		byte[] bytes = new byte[4];
		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = (byte) ((a >> (i * 8)) & 0xff);
		}
		return bytes;
	}

	public static int BytesToInt(byte[] bbb)
	{
		int a = 0;
		for (int i = 0; i < bbb.length; i++)
		{
			int temp = bbb[i];
			a += (temp & 0xff) << (i * 8);
		}
		return a;
	}
}

public class MessageBodyDecoder extends ByteToMessageDecoder
{
	public int index = 0;
	public int length = 0;
	public byte[] bs;

	@Override
	public void decode(ChannelHandlerContext arg0, ByteBuf arg1, List<Object> arg2)
	{
		try
		{
			if (index == 0 && arg1.isReadable())
			{
				byte[] temp = new byte[4];
				arg1.readBytes(temp);
				length = ByteHelp.BytesToInt(temp);
				if (length > 0)
				{
					bs = new byte[length];
				}
			}
			while (arg1.isReadable() && length > 0)
			{
				bs[index] = arg1.readByte();
				index++;
				if ((index + 4) == length)
				{
					arg2.add(bs);
					ReadComplete();
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.err);
			ReadComplete();
			arg0.flush();
			arg0.close();
		}
	}

	public void ReadComplete()
	{
		index = 0;
		length = 0;
		bs = null;
	}
}