package com.sinotn.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class StreamUtil {
	@Test
	public void testSplit(){
		String text="name1|name2";
		String[] names=text.split("\\|");
		System.out.println(names.length);
	}

	@Test
	public void testWriteInt() throws Exception{
		int val=Integer.MAX_VALUE;
		long longVal=Long.MAX_VALUE;
		String text="北京信诺软通信息技术有限公司";
		File file=new File("D:/stream.dt");
		FileOutputStream fos=new FileOutputStream(file);
		this.writeInt(fos, val);
		this.writeLong(fos, longVal);
		this.writeText(fos, text);

		fos.close();
	}

	@Test
	public void testReadInt() throws Exception{
		File file=new File("D:/stream.dt");
		FileInputStream fis=new FileInputStream(file);
		System.out.println(this.readInt(fis)==Integer.MAX_VALUE);
		System.out.println(this.readLong(fis)==Long.MAX_VALUE);
		System.out.println(this.readText(fis));
		fis.close();
	}

	private void writeText(OutputStream os,String text) throws IOException{
		byte[] bytes=text.getBytes("utf-8");
		this.writeInt(os, bytes.length);
		os.write(bytes);
	}

	private String readText(InputStream is) throws IOException{
		if(is.available()<4){
			throw new IOException("流对象可读字节数不足4字节");
		}
		int len=this.readInt(is);
		if(len>0){
			byte[] bytes=new byte[len];
			is.read(bytes);
			return new String(bytes,"utf-8");
		}else{
			return null;
		}
	}

	private int readInt(InputStream is) throws IOException{
		if(is.available()<4){
			throw new IOException("流对象可读字节数不足4字节");
		}
		byte[] buf=new byte[4];
		is.read(buf);
		return  (buf[0]& 0xff) << 24 |
				(buf[1] & 0xff) << 16 |
				(buf[2] & 0xff) <<  8 |
				buf[3] & 0xff;
	}

	private void writeInt(OutputStream os,int intVal) throws IOException{
		byte[] buf=new byte[4];
		buf[0]=(byte) (intVal >>> 24);
		buf[1]=(byte) (intVal >>> 16);
		buf[2]=(byte) (intVal >>> 8);
		buf[3]=(byte) (intVal);
		os.write(buf);
	}

	private long readLong(InputStream is) throws IOException{
		if(is.available()<8){
			throw new IOException("流对象可读字节数不足8字节");
		}
		byte[] buf=new byte[8];
		is.read(buf);
		return  ((long) buf[0]     & 0xff) << 56 |
				((long) buf[1] & 0xff) << 48 |
				((long) buf[2] & 0xff) << 40 |
				((long) buf[3] & 0xff) << 32 |
				((long) buf[4] & 0xff) << 24 |
				((long) buf[5] & 0xff) << 16 |
				((long) buf[6] & 0xff) <<  8 |
				(long) buf[7] & 0xff;
	}

	private void writeLong(OutputStream os,long longVal) throws IOException{
		byte[] buf=new byte[8];
		buf[0]     = (byte) (longVal >>> 56);
		buf[1] = (byte) (longVal >>> 48);
		buf[2] = (byte) (longVal >>> 40);
		buf[3] = (byte) (longVal >>> 32);
		buf[4] = (byte) (longVal >>> 24);
		buf[5] = (byte) (longVal >>> 16);
		buf[6] = (byte) (longVal >>> 8);
		buf[7] = (byte) longVal;
		os.write(buf);
	}

	@Test
	public void testSlice() throws IOException{
		File source=new File("E:\\slice\\c50151ed-5165-4fdb-b3ca-9ea0507fb2cb.zip");
		File main=new File("E:\\slice\\main.zip");
		File slice=new File("E:\\slice\\slice.zip");

		this.slice(source, main, slice, 2*1000);
	}

	@Test
	public void testFileSize(){
		File slice=new File("E:\\slice\\slice.zip");
		long filesize=slice.length();
		System.out.println(filesize);
		System.out.println((int)Math.ceil(filesize/1024.0));
	}

	@Test
	public void testMerge() throws IOException{
		File output=new File("E:\\slice\\output.zip");
		File main=new File("E:\\slice\\main.zip");
		File slice=new File("E:\\slice\\slice.zip");

		this.merge(main, slice, output);
	}

	@Test
	public void testCopy() throws IOException{
		File output=new File("E:\\slice\\copy.zip");
		File source=new File("E:\\slice\\source.zip");
		FileUtils.copyFile(source, output);
	}

	private void merge(File mainFile, File sliceFile, File output) throws IOException{
		FileInputStream fis=null;
		FileOutputStream fos=null;
		try{
			byte[] buf=new byte[512];
			fos=new FileOutputStream(output);

			fis=new FileInputStream(sliceFile);
			for(int len=fis.read(buf);len!=-1;len=fis.read(buf)){
				fos.write(buf,0,len);
			}
			fis.close();

			fis=new FileInputStream(mainFile);
			for(int len=fis.read(buf);len!=-1;len=fis.read(buf)){
				fos.write(buf,0,len);
			}
			fis.close();

			fos.close();
		}finally{
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(fos);
		}
	}

	private void slice(File source, File mainFile, File sliceFile, int sliceSize) throws IOException{
		FileInputStream fis=null;
		FileOutputStream fos=null;
		try{
			int size=sliceSize;
			fis=new FileInputStream(source);
			fos=new FileOutputStream(sliceFile);
			byte[] buf=new byte[512];
			for(int len=fis.read(buf);len!=-1;len=fis.read(buf)){
				if(size>0){
					if(size>len){//还需继续切片
						fos.write(buf,0,len);
						size=size-len;
					}else{//最后切片输出，切换输出流到主干文件
						fos.write(buf,0,size);//完成切片输出。
						fos.flush();
						fos.close();
						fos=new FileOutputStream(mainFile);//切换到主干文件输出
						int remain=len-size;
						if(remain>0){
							int off=size;
							fos.write(buf, off, remain);
						}
						size=0;
					}
				}else{//直接输出到主干文件流。
					fos.write(buf,0,len);
				}
			}
		}finally{
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(fos);
		}
	}
}
