package com.sinotn.demo.netty.base;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

import com.sinotn.demo.netty.handler.NettySession;
import com.sinotn.demo.netty.handler.SessionHandler;

/**

 */
public class UnsharableHandler implements ChannelInboundHandler,ChannelOutboundHandler {
	public UnsharableHandler(){
		System.out.println("[UnsharableHandler created]");
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
		System.out.println("[UnsharableHandler Inbound channelRead]"+ctx.channel()+this);
		ctx.fireChannelRead(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
		System.out.println("[UnsharableHandler Inbound channelReadComplete]");
		ctx.fireChannelReadComplete();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[UnsharableHandler Inbound channelRegistered]");
		ctx.fireChannelRegistered();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[UnsharableHandler Inbound channelUnregistered]");
		ctx.fireChannelUnregistered();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[UnsharableHandler Inbound channelActive]");
		Channel channel=ctx.channel();
		System.out.println(channel);
		NettySession session=ctx.channel().attr(SessionHandler.SESSION_KEY).get();
		System.out.println(session);
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[UnsharableHandler Inbound channelInactive]");
		ctx.fireChannelInactive();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("[UnsharableHandler Inbound userEventTriggered]");
		ctx.fireUserEventTriggered(evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[UnsharableHandler Inbound channelWritabilityChanged]");
		ctx.fireChannelWritabilityChanged();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.out.println("[UnsharableHandler Inbound exceptionCaught]");
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		//ctx.close();
		//ctx.fireExceptionCaught(cause);
	}
	//Outbound

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		System.out.println("[UnsharableHandler Outbound bind]");
		ctx.bind(localAddress, promise);
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		System.out.println("[UnsharableHandler Outbound connect]");
		ctx.connect(remoteAddress, localAddress, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("[UnsharableHandler Outbound disconnect]");
		ctx.disconnect(promise);
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("[UnsharableHandler Outbound close]");
		ctx.close(promise);
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("[UnsharableHandler Outbound deregister]");
		ctx.deregister(promise);
	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[UnsharableHandler Outbound read]");
		ctx.read();
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("[UnsharableHandler Outbound write]");
		ctx.write(msg, promise);
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[UnsharableHandler Outbound flush]");
		ctx.flush();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[UnsharableHandler handlerAdded]");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[UnsharableHandler handlerRemoved]");
	}

}
