package com.sinotn.demo.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Sharable
public class SessionHandler extends ChannelInboundHandlerAdapter {
	public static final AttributeKey<NettySession> SESSION_KEY=AttributeKey.valueOf("sinotn.session");

	public SessionHandler(){
		System.out.println("SessionHandler is created");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel=ctx.channel();
		System.out.println(channel);
		String ipAddr=null;
		SocketAddress socketAddr=channel.remoteAddress();
		if((null!=socketAddr)&&(socketAddr instanceof InetSocketAddress)){
			InetSocketAddress inetSocket=(InetSocketAddress)socketAddr;
			InetAddress inetAddr=inetSocket.getAddress();
			if(null!=inetAddr){
				ipAddr=inetAddr.getHostAddress();
			}
		}
		String channelId=channel.id().asLongText();
		NettySession session=new NettySession(channelId,ipAddr);
		ctx.channel().attr(SESSION_KEY).set(session);
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		NettySession session=ctx.channel().attr(SESSION_KEY).get();
		if(null!=session){
			session.close();
		}
		ctx.fireChannelInactive();
	}

}
