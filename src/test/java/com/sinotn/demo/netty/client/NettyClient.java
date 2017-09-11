package com.sinotn.demo.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import com.sinotn.demo.netty.handler.ExamHandler;
import com.sinotn.demo.netty.handler.Message;

public class NettyClient {
	private static final int PORT=8090;
	private static final String ipAddr="127.0.0.1";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_SNDBUF, 1)
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				//p.addLast(new LoggingHandler(LogLevel.DEBUG));
				//TODO 编码解码器
				p.addLast(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						System.out.println("[客户端事件 initChannel]");
						ChannelPipeline p = ch.pipeline();
						//p.addLast(new LoggingHandler(LogLevel.DEBUG));
						//p.addLast("UnsharableHandler",new UnsharableHandler());
						//p.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
						//p.addLast(new LineBasedFrameDecoder(8192));
						//p.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
						//p.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
						p.addLast(new ExamHandler());
						p.addLast(new EchoHandler());
						//p.addLast("encoder", new ExamHandler());
					}
				});
			}
		});

		// Start the client.
		Channel channel=b.connect(ipAddr, PORT).sync().channel();

		Scanner scanner=new Scanner(System.in);
		while(true){
			String line=scanner.nextLine();
			System.out.println("接收到输入字符串:"+line);
			if("exit".equals(line)){
				System.out.println("正在退出客户端！！@"+System.currentTimeMillis());
				Future<?> f=group.shutdownGracefully();
				f.addListener(new GenericFutureListener(){
					@Override
					public void operationComplete(Future future) throws Exception {
						System.out.println("成功退出客户端！！@"+System.currentTimeMillis());
						System.exit(0);
					}});
			}else if(line.startsWith("msg://")){
				Message msg=buildMessage(line.substring(6));
				channel.writeAndFlush(msg);
			}else if("enter".equals(line)){
				channel.writeAndFlush("[enter]\r\n");
			}else if("flush".equals(line)){
				channel.flush();
			}else if(line.startsWith("file://")){
				File file=new File(line.substring(7));
				DefaultFileRegion region=new DefaultFileRegion(file, 0, file.length());
				channel.write(region);
			}else{
				channel.write(line);
			}
		}
	}
	/*
	 msg://Hello Netty#1#1#E:/DelphiWorkspace.zip;d:/db.sql
	 msg://file://d:/demo2.txt#1#1#E:/php.zip;E:/by.jpg

	 */
	private static Message buildMessage(String line){
		Message iRet=new Message((short)1,1L);
		String[] params=line.split("#");
		if(params.length>0){
			String param=params[0];
			if(param.startsWith("file://")){
				iRet.setText(getFileText(param.substring(7)));
			}else{
				iRet.setText(param);
			}
			System.out.println("发送字符串："+iRet.getText().length());
		}
		if(params.length>1){
			iRet.setType(Short.valueOf(params[1]));
		}
		if(params.length>2){
			iRet.setToken(Long.valueOf(params[2]));
		}
		if(params.length>3){
			params=params[3].split(";");
			File[] fs=new File[params.length];
			for(int i=0;i<params.length;i++){
				fs[i]=new File(params[i]);
			}
			iRet.setFiles(fs);
		}
		return iRet;
	}

	private static String getFileText(String path){
		StringBuilder sb=new StringBuilder();
		BufferedReader reader=null;
		try{
			reader=new BufferedReader(new FileReader(new File(path)));
			for(String line=reader.readLine();line!=null;line=reader.readLine()){
				sb.append(line);
			}
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			if(null!=reader){
				try{reader.close();}catch(Throwable e){}
			}
		}
		return sb.toString();
	}
}
