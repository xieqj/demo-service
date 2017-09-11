package com.sinotn.demo.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import com.sinotn.demo.netty.NettyLogger;

/**
 * 从流中解析读取文件
 * @Title FileExtractor.java
 * @Package com.sinotn.demo.netty.handler
 * @Description
 * Copyright: Copyright (c) 2015
 * Company:北京信诺软通
 *
 * @author <a href="mailto:xieqj@sinotn.com">谢启进</a>
 * @date 2016年7月21日 下午6:01:45
 * @version V1.0
 */
public class FileExtractor {
	//文件长度(4字节长度无符号int)
	private long fileLength;
	//文件名长度(2字节长度无符号short)
	private int nameLength;
	private String filename;
	private File file;
	private File directory;
	private FileChannel fileChannel;
	private RandomAccessFile raf;

	public FileExtractor(){
		//TODO 临时指定目录
		this.directory=new File("d:/netty");
	}

	public void release(){
		this.fileLength=0;
		this.nameLength=0;
		this.filename=null;
		if(null!=this.fileChannel){
			try{
				this.fileChannel.close();
				this.fileChannel=null;
			}catch(Throwable e){
				StringBuilder sb=new StringBuilder();
				sb.append("关闭文件通道发生系统错误");
				if(this.file!=null){
					sb.append(this.file.getAbsolutePath());
				}
				NettyLogger.DEBUG.error(sb.toString(),e);
			}
		}
		if(this.raf!=null){
			try{
				this.raf.close();
				this.raf=null;
			}catch(Throwable e){
				StringBuilder sb=new StringBuilder();
				sb.append("关闭文件流发生系统错误");
				if(this.file!=null){
					sb.append(this.file.getAbsolutePath());
				}
				NettyLogger.DEBUG.error(sb.toString(),e);
			}
		}

		this.file=null;
	}

	public File extract(ByteBuf in,Charset charset) {
		try{
			if(this.filename==null){
				if(this.nameLength==0){
					if(in.readableBytes()<2) return null;
					this.nameLength=in.readUnsignedShort();
					if(this.nameLength<=0){
						throw new DecoderException("解析文件名时，读取到非法文件名长度数值:"+this.nameLength);
					}
				}
				if(in.readableBytes()<this.nameLength) return null;
				//解析文件名称
				ByteBuf slice=in.slice(in.readerIndex(), this.nameLength);
				this.filename=slice.toString(charset);
				in.readerIndex(in.readerIndex()+this.nameLength);
				//初始化文件数据
				this.file=new File(this.directory,this.filename);
				File parent=this.file.getParentFile();
				if(parent.exists()==false){
					if(false==parent.mkdirs()){
						throw new DecoderException("无法创建文件夹"+parent.getAbsolutePath());
					}
				}
				this.raf=new RandomAccessFile(this.file, "rw");
				this.fileChannel=this.raf.getChannel();
			}

			if(null!=this.file){
				if(this.fileLength==0){
					//解析文件长度
					if(in.readableBytes()<4) return null;
					this.fileLength=in.readUnsignedInt();
					if(this.fileLength<=0){
						throw new DecoderException("解析文件数据时，读取到非法文件长度数值:"+this.fileLength);
					}
				}
				//解析文件内容
				if(in.isReadable()==false) return null;
				int readSize=in.readableBytes();
				if(readSize>this.fileLength){
					readSize=(int)this.fileLength;
				}
				int readerIndex=in.readerIndex();
				ByteBuffer byteBuffer=in.internalNioBuffer(readerIndex, readSize);
				this.fileChannel.write(byteBuffer);
				in.readerIndex(readerIndex+readSize);
				this.fileLength=this.fileLength-readSize;

				if(this.fileLength==0){
					//文件内容读取完成
					File iRet=this.file;
					this.release();
					return iRet;
				}else if(this.fileLength<0){
					throw new DecoderException("文件解码长度错误"+this.fileLength);
				}
			}

			return null;
		}catch(DecoderException e){
			this.release();
			throw e;
		}catch(Throwable t){
			this.release();
			throw new DecoderException(t);
		}
	}
}
