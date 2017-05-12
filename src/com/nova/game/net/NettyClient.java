package com.nova.game.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;

import com.nova.game.net.handler.ClientInboundHandler;
import com.nova.game.net.util.ERequestType;

public class NettyClient {

	private String mHost;
	private int mPort;
	
	// test begin
	public Channel channel;
	
	public NettyClient(String host, int port) {
		mHost = host;
		mPort = port;
	}
	
	public void start() {
		new Thread(new Runnable() {

			@Override
			public void run() {
		        try {
					connect(mHost, mPort, ERequestType.SOCKET);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void connect(String host, int port, final ERequestType requestType) throws Exception {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		String msg = "1234";
		if (ERequestType.SOCKET.equals(requestType)) {
			try {
				Bootstrap b = new Bootstrap();
				b.group(workerGroup).channel(NioSocketChannel.class);
				b.handler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast("frameDecoder",
								new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
						pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
						pipeline.addLast("handler", new ClientInboundHandler());
					}
				});
				b.option(ChannelOption.SO_KEEPALIVE, true);

				ChannelFuture f = b.connect(host, port).sync();
				ChannelManager.getInstance().initChannel(f.channel());
				f.channel().closeFuture().sync();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (ERequestType.HTTP.equals(requestType)) {

			Bootstrap b = new Bootstrap();

			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {

					// 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
					ch.pipeline().addLast(new HttpResponseDecoder());
					// 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
					ch.pipeline().addLast(new HttpRequestEncoder());
					ch.pipeline().addLast(new ClientInboundHandler());
				}
			});
			ChannelFuture f = b.connect(host, port).sync();
			b.option(ChannelOption.TCP_NODELAY, true);
			b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

			URI uri = new URI("http://" + host + ":" + port);

			DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
					uri.toASCIIString(), Unpooled.wrappedBuffer(
							new StringBuffer().append("999").append(",").append(msg).toString().getBytes("UTF-8")));

			// 构建http请求
			request.headers().set(HttpHeaders.Names.HOST, host);
			request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
			// 发送http请求
			f.channel().write(request);
			f.channel().flush();
			f.channel().closeFuture().sync();
		}

		try {
		} finally {
			workerGroup.shutdownGracefully();
		}

	}
}
