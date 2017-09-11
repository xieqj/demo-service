package com.sinotn.demo.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import com.sinotn.demo.netty.NettyLogger;

/**
 * Cumulate {@link ByteBuf}s by merge them into one {@link ByteBuf}'s, using memory copies.
 * @Title MergeCumulator.java
 * @Package com.sinotn.demo.netty.handler
 * @Description
 * Copyright: Copyright (c) 2015
 * Company:北京信诺软通
 *
 * @author <a href="mailto:xieqj@sinotn.com">谢启进</a>
 * @date 2016年7月22日 下午8:46:34
 * @version V1.0
 */
public class MergeCumulator implements Cumulator {
	/**
	 * 最大64K缓存容量
	 */
	private static final int MAX_CAPACITY=65536;
	private static final MergeCumulator inst=new MergeCumulator();
	private boolean debug=NettyLogger.DEBUG.isDebugEnabled();

	private MergeCumulator(){}

	public static Cumulator get(){
		return inst;
	}

	@Override
	public ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in) {
		ByteBuf buffer=null;
		int writerIndex=cumulation.writerIndex();
		int maxCap=cumulation.maxCapacity();
		if(maxCap>MAX_CAPACITY){
			maxCap=cumulation.capacity();
			if(maxCap<=MAX_CAPACITY){
				maxCap=MAX_CAPACITY;
			}else if(this.debug){
				this.byteBufInfo(cumulation);
			}
		}
		//in要写入cumulation，需要的writerIndex;
		int needWriterIndex=maxCap - in.readableBytes();
		if (writerIndex > needWriterIndex) {
			//当前cumulation容量不够
			if(cumulation.refCnt()==1){
				int needDiscardSize=writerIndex-needWriterIndex;
				int readerIndex=cumulation.readerIndex();
				if(readerIndex>needDiscardSize){
					if(this.debug){
						this.byteBufInfo(cumulation);
					}
					cumulation.discardSomeReadBytes();
					if(this.debug){
						this.byteBufInfo(cumulation);
					}
					writerIndex=cumulation.writerIndex();
					needWriterIndex=maxCap-in.readableBytes();
					if (writerIndex <= needWriterIndex){
						buffer=cumulation;
					}
				}
			}
			// Expand cumulation (by replace it) when either there is not more room in the buffer
			// or if the refCnt is greater then 1 which may happen when the user use slice().retain() or
			// duplicate().retain().
			//
			// See:
			// - https://github.com/netty/netty/issues/2327
			// - https://github.com/netty/netty/issues/1764
			if(buffer==null){
				buffer = expandCumulation(alloc, cumulation, in.readableBytes());
			}
		} else {
			buffer = cumulation;
		}

		buffer.writeBytes(in);
		in.release();
		return buffer;
	}

	private void byteBufInfo(ByteBuf cumulation){
		StringBuilder sb=new StringBuilder();
		sb.append("字节缓存空间回收(");
		sb.append("rid:").append(cumulation.readerIndex());
		sb.append(", rsz:").append(cumulation.readableBytes());
		sb.append(", wid:").append(cumulation.writerIndex());
		sb.append(", wsz:").append(cumulation.writableBytes());
		sb.append(", cap:").append(cumulation.capacity());
		sb.append(")");
		NettyLogger.DEBUG.debug(sb.toString());
	}

	private ByteBuf expandCumulation(ByteBufAllocator alloc, ByteBuf cumulation, int readable) {
		ByteBuf oldCumulation = cumulation;
		cumulation = alloc.buffer(oldCumulation.readableBytes() + readable);
		cumulation.writeBytes(oldCumulation);
		oldCumulation.release();
		return cumulation;
	}
}
