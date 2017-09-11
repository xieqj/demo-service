package com.sinotn.demo.java;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import org.junit.Test;

import com.sinotn.SinotnLogger;

public class FileTest {

	@Test
	public void list(){
		File folder=new File("D:\\demo\\photo\\o1blsbdj873UG7yfdHk0SbIG4KFDNrGI");
		File[] photos=folder.listFiles();
		if(photos!=null && photos.length>0){
			int index;
			for(File photo:photos){
				/*if(photo.isDirectory()){
					continue;
				}*/
				index=photo.getName().indexOf(".");
				if(index==-1){
					System.out.println(photo.getAbsolutePath());
				}
			}
		}
	}

	@Test
	public void copy(){
		File source=new File("D:\\demo\\menu_room_src.json");
		File dest=new File("D:\\demo\\menu_room_dest.json");
		this.transfer(source, dest);
	}

	@Test
	public void startWith(){
		String name="{0000-}socket1.png";
		boolean iRet=name.startsWith("{0000-}");
		System.out.println(iRet);
	}

	private void transfer(File source,File dest){
		RandomAccessFile rafSource=null;
		RandomAccessFile rafDest=null;
		try{
			rafSource=new RandomAccessFile(source, "r");
			rafDest=new RandomAccessFile(dest, "rw");
			FileChannel channel=rafSource.getChannel();
			channel.transferTo(0, rafSource.length(), rafDest.getChannel());
			rafSource.close();
			rafSource=null;
			rafDest.close();
			rafDest=null;
		}catch(Throwable e){
			StringBuilder sb=new StringBuilder();
			sb.append("拷贝文件");
			sb.append(source.getAbsolutePath());
			sb.append("到文件").append(dest.getAbsolutePath());
			sb.append("发生系统错误");
			SinotnLogger.DEBUG.error(sb.toString(),e);
		}finally{
			if(rafSource!=null){
				try{rafSource.close();}catch(Throwable e){}
			}
			if(rafDest!=null){
				try{rafDest.close();}catch(Throwable e){}
			}
		}
	}
}
