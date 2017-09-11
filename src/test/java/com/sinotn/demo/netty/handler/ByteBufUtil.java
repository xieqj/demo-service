package com.sinotn.demo.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public abstract class ByteBufUtil {

	/**
	 * 使用Charset编码对CharBuffer字符进行编码，返回使用通过ByteBufAllocator创建的新ByteBuf实例
	 * @param alloc ByteBuf分配器
	 * @param enforceHeap 是否强制使用堆内存空间
	 * @param src 需要编码的源字符串
	 * @param charset 字符编码字符集，比如：utf-8、GBK等
	 * @param offset 指定返回的新ByteBuf头部偏移量保有字节
	 * @param offset 指定返回的新ByteBuf尾部额外增加字节数
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月19日 下午3:37:44
	 */
	public static ByteBuf encodeString(ByteBufAllocator alloc, boolean enforceHeap, CharBuffer src, Charset charset, int offset,int extraCapacity) {
		final CharsetEncoder encoder = CharsetUtil.encoder(charset);
		int length = (int) ((double) src.remaining() * encoder.maxBytesPerChar()) + offset + extraCapacity;
		boolean release = true;
		final ByteBuf dst;
		if (enforceHeap) {
			dst = alloc.heapBuffer(length);
		} else {
			dst = alloc.buffer(length);
		}
		try {
			final ByteBuffer dstBuf = dst.internalNioBuffer(0, length);
			final int pos = dstBuf.position();
			if (offset > 0) {
				dstBuf.position(offset);
			}
			CoderResult cr = encoder.encode(src, dstBuf, true);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}
			cr = encoder.flush(dstBuf);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}
			dst.writerIndex(dst.writerIndex() + dstBuf.position() - pos);
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
