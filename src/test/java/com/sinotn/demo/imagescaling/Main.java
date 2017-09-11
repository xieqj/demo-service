package com.sinotn.demo.imagescaling;

import java.io.File;

import org.junit.Test;

import com.sinotn.util.PhotoUtil;

public class Main {

	@Test
	public void scale(){
		File source=new File("D:\\demo\\photo\\3308231977050100541_1.jpg");
		File output=new File("D:\\demo\\photo\\3308231977050100541_out.jpg");
		PhotoUtil.scale2Jpg(source, output, 500);
		//ResampleOp  resampleOp = new ResampleOp (100,200);
		//BufferedImage rescaledTomato = resampleOp.filter(tomato, null);
	}
}
