package com.sinotn.demo.photo;

import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.junit.Test;

public class PhotoUtil {

	@Test
	public void testGetFormatName(){
		File file=new File("D:\\demo\\sac.png");
		String formatName=this.getFormatName(file);
		System.out.println("JPEG".equalsIgnoreCase(formatName));
	}

	public String getFormatName(File file){
		ImageInputStream iis=null;
		try{
			iis = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				return null;
			}
			ImageReader reader=iter.next();
			iis.close();
			iis=null;
			return reader.getFormatName();
		}catch(Throwable e){
			throw new RuntimeException("读取照片类型名称发生系统错误", e);
		}finally{
			if(iis!=null){
				try{
					iis.close();
				}catch(Throwable e){}
			}
		}
	}
}
