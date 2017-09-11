package com.sinotn.demo.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

/**
socket连接成功
	[客户端事件 handlerAdded]
	[客户端事件 Inbound channelRegistered]
	[客户端事件 Outbound connect]
	[客户端事件 Inbound channelActive]
	[客户端事件 Outbound read]

socket关闭事件
	[客户端事件 Inbound channelInactive]
	[客户端事件 Inbound channelUnregistered]
	[客户端事件 handlerRemoved]

服务端关闭连接
	[客户端事件 Inbound channelReadComplete]
	[客户端事件 Outbound read]
	[客户端事件 Inbound channelInactive]
	[客户端事件 Inbound channelUnregistered]
	[客户端事件 handlerRemoved]
 */
public class EchoHandler implements ChannelInboundHandler,ChannelOutboundHandler {
	public EchoHandler(){
		System.out.println("[客户端EchoHandler created]");
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
		System.out.println("[客户端事件 Inbound channelRead]");
		ctx.fireChannelRead(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
		System.out.println("[客户端事件 Inbound channelReadComplete]");
		ctx.fireChannelReadComplete();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[客户端事件 Inbound channelRegistered]");
		ctx.fireChannelRegistered();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[客户端事件 Inbound channelUnregistered]");
		ctx.fireChannelUnregistered();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[客户端事件 Inbound channelActive]");
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[客户端事件 Inbound channelInactive]");
		ctx.fireChannelInactive();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("[客户端事件 Inbound userEventTriggered]");
		ctx.fireUserEventTriggered(evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[客户端事件 Inbound channelWritabilityChanged]");
		ctx.fireChannelWritabilityChanged();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.out.println("[客户端事件 Inbound exceptionCaught]");
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		//ctx.close();
		//ctx.fireExceptionCaught(cause);
	}
	//Outbound

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		System.out.println("[客户端事件 Outbound bind]");
		ctx.bind(localAddress, promise);
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		System.out.println("[客户端事件 Outbound connect]");
		ctx.connect(remoteAddress, localAddress, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("[客户端事件 Outbound disconnect]");
		ctx.disconnect(promise);
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("[客户端事件 Outbound close]");
		ctx.close(promise);
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("[客户端事件 Outbound deregister]");
		ctx.deregister(promise);
	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[客户端事件 Outbound read]");
		ctx.read();
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("[客户端事件 Outbound write]");
		ctx.write(msg, promise);
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[客户端事件 Outbound flush]");
		ctx.flush();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[客户端事件 handlerAdded]");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("[客户端事件 handlerRemoved]");
	}

}
