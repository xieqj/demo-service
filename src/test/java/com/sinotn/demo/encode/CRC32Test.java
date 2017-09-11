package com.sinotn.demo.encode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.junit.Test;

public class CRC32Test {

	@Test
	public void testGetCRC32(){
		//File copy=new File("E:\\slice\\copy.zip");
		File output=new File("D:\\temp\\考试服务器V2.0.7876.zip");
		System.out.println("output:"+this.getCRC32(output));
		/*File source=new File("E:\\slice\\c50151ed-5165-4fdb-b3ca-9ea0507fb2cb.zip");
		System.out.println("source:"+this.getCRC32(source));*/

		//System.out.println("copy:"+this.getCRC32(copy));
	}

	private String getCRC32(File file){
		CRC32 crc32 = new CRC32();
		FileInputStream fileinputstream = null;
		CheckedInputStream checkedinputstream = null;
		//long l=System.currentTimeMillis();//3213654631
		try {
			fileinputstream = new FileInputStream(file);
			checkedinputstream = new CheckedInputStream(fileinputstream, crc32);
			byte[] buf=new byte[1024];
			while (checkedinputstream.read(buf) != -1) {}
			//System.out.println(System.currentTimeMillis()-l);
			long  longVal=crc32.getValue();
			//System.out.println(3213654631L==longVal);
			return String.valueOf(longVal);
			//System.out.println(longVal);
			//String hexStr = Long.toHexString(longVal).toUpperCase();
			//System.out.println(hexStr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileinputstream != null) {
				try {
					fileinputstream.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			if (checkedinputstream != null) {
				try {
					checkedinputstream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
}
