package com.sinotn.demo.netty.handler;

import java.io.File;

public class Message {
	/**
	 * 消息类型枚举定义
	 * @Title Message.java
	 * @Package com.sinotn.demo.netty.handler
	 * @Description
	 * Copyright: Copyright (c) 2015
	 * Company:北京信诺软通
	 *
	 * @author <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月21日 上午9:06:18
	 * @version V1.0
	 */
	public enum Type{
		REQUEST((short)1,"请求")
		,RESPONSE((short)2,"响应");
		private short value;
		private String text;

		private Type(short value,String text){
			this.value=value;
			this.text=text;
		}

		public short getValue() {
			return value;
		}

		public String getText() {
			return text;
		}
	}
	/**
	 * 消息类型(消息头:1字节无符号byte)
	 */
	private short type=Type.RESPONSE.value;
	/**
	 * 消息令牌(消息头:4字节无符号int)
	 */
	private long token;
	/**
	 * 字符消息体
	 */
	private String text;
	/**
	 * 文件消息列表
	 */
	private File[] files;

	public Message(short type,long token){
		this.type=type;
		this.token=token;
	}

	/**
	 * 消息类型
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月19日 下午2:19:31
	 */
	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public long getToken() {
		return token;
	}

	public void setToken(long token) {
		this.token = token;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] file) {
		this.files = file;
	}
	/**
	 * 获取消息包含文件数
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月21日 上午9:13:49
	 */
	public int getFileSize() {
		if(this.files==null) return 0;
		return this.files.length;
	}
}
