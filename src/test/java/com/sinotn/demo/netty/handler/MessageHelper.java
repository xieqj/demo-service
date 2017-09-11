package com.sinotn.demo.netty.handler;

import io.netty.handler.codec.DecoderException;

import java.io.File;

/**
 * Message解码帮助类
 * @Title MessageHelper.java
 * @Package com.sinotn.demo.netty.handler
 * @Description
 * Copyright: Copyright (c) 2015
 * Company:北京信诺软通
 *
 * @author <a href="mailto:xieqj@sinotn.com">谢启进</a>
 * @date 2016年7月21日 下午4:46:09
 * @version V1.0
 */
public class MessageHelper {
	private Message message;
	private int headFileSize=0;
	private int headTextSize=-1;
	private StringBuilder sb;
	private FileExtractor fileExtractor;
	private File[] fs;

	private MessageHelper(){}

	public static MessageHelper get(short type,long token,int fileSize){
		MessageHelper iRet=new MessageHelper();
		iRet.message=new Message(type, token);
		iRet.headFileSize=fileSize;
		if(fileSize>0){
			iRet.fileExtractor=new FileExtractor();
			iRet.fs=new File[fileSize];
		}
		return iRet;
	}

	/**
	 * 获取文件解析器，如果初始化时fileSize不大于0，则该返回值为NULL
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月21日 下午5:39:20
	 */
	public FileExtractor getFileExtractor() {
		return fileExtractor;
	}

	/**
	 * 文本字节长度
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月21日 下午4:58:50
	 */
	public int getHeadTextSize() {
		return headTextSize;
	}
	/**
	 * 文本字节长度
	 * @param headTextSize
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月21日 下午4:59:22
	 */
	public void setHeadTextSize(int headTextSize) {
		this.headTextSize = headTextSize;
		if(this.sb==null&&this.headTextSize>0){
			this.sb=new StringBuilder(this.headTextSize);
		}
	}
	/**
	 * 添加解码到的字符串
	 * @param str
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月21日 下午5:06:33
	 */
	public void append(String str,int headTextSize){
		if(this.headTextSize==headTextSize){
			this.sb.append(str);
			this.headTextSize=-1;
		}else{
			throw new DecoderException("期望得到"+this.headTextSize+"编码长度的解码字符串，但得到的是"+headTextSize+"编码长度");
		}
	}

	/**
	 * 判断是否已经完成文本信息解码
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月21日 下午4:51:22
	 */
	public boolean isTextDecoded(){
		return (this.headTextSize==0);
	}

	/**
	 * 是否解码完成
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月21日 下午4:46:28
	 */
	public boolean isFinish(){
		boolean iRet=(this.headFileSize==0&&this.headTextSize==0);
		if(iRet){
			if(null!=this.sb){
				this.message.setText(sb.toString());
			}
			if(null!=this.fs){
				this.message.setFiles(this.fs);
			}
			this.release();
		}
		return iRet;
	}

	public Message getMessage() {
		return message;
	}

	public int getHeadFileSize() {
		return headFileSize;
	}

	public void appendFile(File file) {
		this.fs[this.fs.length-this.headFileSize]=file;
		this.headFileSize--;
	}

	public void release(){
		if(null!=this.fileExtractor){
			this.fileExtractor.release();
			this.fileExtractor=null;
		}
		this.sb=null;
		this.fs=null;
	}
}
