package com.sinotn.demo.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.EncoderException;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public class StringEncode {
	/**
	 * 文本消息最大片段编码长度
	 */
	public static final int TEXT_SEGMENT_SIZE=0Xffff;
	private ByteBufAllocator alloc;
	private boolean enforceHeap;
	private Charset charset;
	private String text;
	//每次编码最大字符数
	private int maxCharaterPerEncode=1024*2;
	private int index=0;

	public StringEncode(String text,Charset charset){
		if(text==null||text.length()==0) throw new CodecException("不能对空字符或NULL进行编码");
		this.text=text;
		this.charset = charset;
	}

	public void setMaxCharaterPerEncode(int maxCharaterPerEncode) {
		this.maxCharaterPerEncode = maxCharaterPerEncode;
		CharsetEncoder encoder=CharsetUtil.encoder(charset);
		float maxBytesPerChar=encoder.maxBytesPerChar();
		int i=(int)(this.maxCharaterPerEncode*maxBytesPerChar);
		if(i>TEXT_SEGMENT_SIZE){
			i=(int)(TEXT_SEGMENT_SIZE/maxBytesPerChar);
			throw new EncoderException("最大片段编码长度"+TEXT_SEGMENT_SIZE+"一次最多支持对"+i+"字符进行编码"+maxCharaterPerEncode);
		}
	}

	public String getText() {
		return text;
	}

	public void setAlloc(ByteBufAllocator alloc) {
		this.alloc = alloc;
	}

	private ByteBuf newByteBuf(int length){
		if(this.alloc==null){
			this.alloc=ByteBufAllocator.DEFAULT;
		}
		if (this.enforceHeap) {
			return alloc.heapBuffer(length);
		} else {
			return alloc.buffer(length);
		}
	}

	public void setEnforceHeap(boolean enforceHeap) {
		this.enforceHeap = enforceHeap;
	}

	public void release(){
		this.alloc=null;
		this.charset=null;
		this.text=null;
	}
	/**
	 * 是否还有未编码数据
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月21日 上午11:32:15
	 */
	public boolean hasNext(){
		return (this.index<this.text.length());
	}

	public ByteBuf next(){
		if(this.index<this.text.length()){
			ByteBuf byteBuf=null;
			try{
				CharBuffer charBuffer;
				int endIndex=this.index+this.maxCharaterPerEncode;
				if(endIndex>this.text.length()){
					endIndex=this.text.length();
				}
				charBuffer=CharBuffer.wrap(this.text, this.index, endIndex);
				byteBuf=this.encodeString(charBuffer);
				this.index=endIndex;
				return byteBuf;
			}catch(EncoderException e){
				if(null!=byteBuf){
					byteBuf.readableBytes();
				}
				throw e;
			}catch(Throwable t){
				if(null!=byteBuf){
					byteBuf.readableBytes();
				}
				throw new EncoderException(t);
			}
		}else{
			return null;
		}
	}

	private ByteBuf encodeString(CharBuffer charBuffer) {
		CharsetEncoder encoder=CharsetUtil.encoder(charset);
		int length = (int) ((double) charBuffer.remaining() * encoder.maxBytesPerChar())+2;//增加两字节空间存储字符编码长度信息
		boolean release = true;
		final ByteBuf dst=this.newByteBuf(length);
		try {
			final ByteBuffer dstBuf = dst.internalNioBuffer(0, length);
			final int pos = dstBuf.position();
			//编码信息前两字节保留存储编码后长度信息
			dstBuf.position(2);
			CoderResult cr = encoder.encode(charBuffer, dstBuf, true);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}
			cr = encoder.flush(dstBuf);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}
			dst.writerIndex(dst.writerIndex() + dstBuf.position() - pos);
			dst.setShort(0, dst.readableBytes()-2);
			release = false;
			return dst;
		} catch (CharacterCodingException x) {
			throw new IllegalStateException(x);
		} finally {
			if (release) {
				dst.release();
			}
		}
	}
}
