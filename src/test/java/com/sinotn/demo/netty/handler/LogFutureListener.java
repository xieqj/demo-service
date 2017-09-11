package com.sinotn.demo.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.GenericFutureListener;

public class LogFutureListener implements GenericFutureListener<ChannelPromise> {
	private ByteBuf byteBuf;

	public LogFutureListener(ByteBuf byteBuf){
		this.byteBuf=byteBuf;
	}

	@Override
	public void operationComplete(ChannelPromise future) throws Exception {
		System.out.println("refCnt->"+this.byteBuf.refCnt());
		System.out.println(future.isSuccess());
	}

}
