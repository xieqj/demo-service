package com.sinotn.demo.jna;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.junit.Test;

public class SqliteTest {

	public SqliteTest(){}

	@Test
	public void testEncrypt() {
		try{
			String path="D:\\temp\\中 文\\9d9f3da8-2fde-45ea-8f0a-e227c3735974.dt";
			String passwd="9d9f3da8-2fde-45ea-8f0a-e227c3735974";
			int len=passwd.getBytes("UTF-8").length;

			int iRet=Sqlite.INSTANCE.SQLiteEncrypt(path, passwd, len);
			System.out.println(iRet);
		}catch(Throwable e){
			e.printStackTrace();
		}
	}

	@Test
	public void testXor(){
		int salt=255;
		System.out.println((byte)255);
		byte val=120;
		byte b=(byte)(val^salt);
		System.out.println(b);
		b=(byte)(b^salt);
		System.out.println(b);
	}

	@Test
	public void testDecode(){
		try{
			String path="D:\\temp\\9d9f3da8-2fde-45ea-8f0a-e227c3735974_2.dt";
			String passwd="9d9f3da8-2fde-45ea-8f0a-e227c3735974";
			int len=passwd.getBytes("UTF-8").length;

			int iRet=Sqlite.INSTANCE.SQLiteDecode(path, passwd, len);
			System.out.println(iRet);
			// TODO Auto-generated constructor stub
		}catch(Throwable e){
			e.printStackTrace();
		}
	}

	@Test
	public void testSign() throws Exception {
		File file1=new File("D:\\temp\\9d9f3da8-2fde-45ea-8f0a-e227c3735974_2.dt");
		File file2=new File("D:\\temp\\{sifaj1}9d9f3da8-2fde-45ea-8f0a-e227c3735974.dt");
		System.out.println(this.getSign(file2));
		System.out.println(this.getSign(file1));
	}

	private String getSign(File file) throws Exception{
		CRC32 crc32 = new CRC32();
		FileInputStream fis=null;
		CheckedInputStream cis=null;
		try {
			fis=new FileInputStream(file);
			cis=new CheckedInputStream(fis, crc32);
			byte[] buf=new byte[512];
			while (cis.read(buf) != -1) {}
			return String.valueOf(crc32.getValue());
		} finally {
			if (cis != null) {
				try {
					cis.close();
				} catch (IOException e) {}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e2) {}
			}
		}
	}
}
