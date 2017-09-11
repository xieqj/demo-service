package com.sinotn.demo.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * Cumulate {@link ByteBuf}s.
 * @Title Cumulator.java
 * @Package com.sinotn.demo.netty.handler
 * @Description
 * Copyright: Copyright (c) 2015
 * Company:北京信诺软通
 *
 * @author <a href="mailto:xieqj@sinotn.com">谢启进</a>
 * @date 2016年7月22日 下午8:42:44
 * @version V1.0
 */
public interface Cumulator {
	/**
	 * Cumulate the given {@link ByteBuf}s and return the {@link ByteBuf} that holds the cumulated bytes.
	 * The implementation is responsible to correctly handle the life-cycle of the given {@link ByteBuf}s and so
	 * call {@link ByteBuf#release()} if a {@link ByteBuf} is fully consumed.
	 * @param alloc
	 * @param cumulation
	 * @param in
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月22日 下午8:43:14
	 */
	ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in);
}
