package com.sinotn.demo.commons;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class FileUtil {

	@Test
	public void testMoveDirectory()throws Exception{
		File srcDir=new File("D:\\sinotn\\MojHome");
		File destDir=new File("E:\\MojHome2");
		FileUtils.moveDirectory(srcDir, destDir);
	}

	@Test
	public void signFile() throws Exception{

	}
}
