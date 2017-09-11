package com.sinotn.demo.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Scanner;

import com.sinotn.demo.netty.handler.MessageHandler;
import com.sinotn.demo.netty.handler.SessionHandler;

public class NettyServer {
	private static final int PORT=9000;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws Exception{
		final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		final EventLoopGroup workerGroup = new NioEventLoopGroup();

		final SessionHandler sessionHandler=new SessionHandler();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup)
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 100)//设置连接请求缓存队列
		.childOption(ChannelOption.SO_KEEPALIVE, true)
		//.handler(new LoggingHandler(LogLevel.DEBUG))//handler在初始化时就会执行，而childHandler会在客户端成功connect后才执行
		.childHandler(new ChannelInitializer<SocketChannel>() {//每一个客户端成功连接都会执行一次
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				System.out.println("[服务端事件 initChannel]");
				ChannelPipeline p = ch.pipeline();
				//ByteBuf delimite=Unpooled.copiedBuffer("#".getBytes());
				//ByteBuf delimite=Unpooled.wrappedBuffer(Delimiters.lineDelimiter());
				//p.addLast("UnsharableHandler",new UnsharableHandler());
				p.addLast("SessionHandler",sessionHandler);
				//p.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
				//p.addLast("framer", new FixedLengthFrameDecoder(3));
				//p.addLast("line", new LineBasedFrameDecoder(1024));
				//p.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
				//p.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
				//p.addLast(new EchoHandler());
				p.addLast(new MessageHandler());
				//TODO 编码解码器

			}
		});

		// Start the server.
		//b.bind(PORT).sync();
		b.bind("127.0.0.1",PORT).sync();
		Scanner scanner=new Scanner(System.in);
		while(true){
			String line=scanner.nextLine();
			System.out.println("接收到输入字符串:"+line);
			if("exit".equals(line)){
				System.out.println("正在退出服务端！！@"+System.currentTimeMillis());
				Future<?> f1=bossGroup.shutdownGracefully();
				f1.addListener(new GenericFutureListener(){
					@Override
					public void operationComplete(Future future) throws Exception {
						System.out.println("成功退出主线程组！！@"+System.currentTimeMillis());
						Future<?> f2=workerGroup.shutdownGracefully();
						f2.addListener(new GenericFutureListener(){
							@Override
							public void operationComplete(Future future) throws Exception {
								System.out.println("成功退出服务端！！@"+System.currentTimeMillis());
								System.exit(0);
							}});
					}});
			}else{
				System.out.println(line);
			}
		}
	}

}
