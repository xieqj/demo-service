package com.sinotn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class NodeAppEncrypt {
	private static final int DEFAULT_SALT=110;

	@Test
	public void testGson() throws Exception {
		Calendar cal = Calendar.getInstance();
		TimeZone timeZone = cal.getTimeZone();
		System.out.println(timeZone.getID());
		System.out.println(timeZone.getDisplayName());
		System.out.println(timeZone.getRawOffset());
		System.out.println(8*60*60*1000);
	}

	@Test
	public void test(){
		int size=20;
		int[] indexs=new int[size];
		int index;
		for(int i=0;i<size;i++){
			if(i==0){
				indexs[i]=i;
			}else{
				index=RandomUtils.nextInt(i+1);
				if(index==i){
					indexs[index]=i;
				}else{
					indexs[i]=indexs[index];
					indexs[index]=i;
				}
			}
		}

		for(int i=0;i<size;i++){
			System.out.println(indexs[i]);
		}
	}

	@Test
	public void testBitOperate(){
		int v1=10;
		int mark=8;
		int iRet=(v1&(~mark));
		System.out.println(iRet);
	}

	@Test
	public void fileMove() throws Exception{
		File folder=new File("D:\\sinotn\\home\\temp\\client");
		File store=new File("D:\\sinotn\\home\\NodeApps\\201702\\dest");
		if(store.exists()){
			FileUtils.deleteDirectory(store);
		}
		FileUtils.moveDirectory(folder, store);
	}

	@Test
	public void testGetFileText() throws Exception{
		File file=new File("D:\\sinotn\\SgeHome2\\readme.txt");
		StringBuilder sb=new StringBuilder();
		BufferedReader br=null;
		try{
			br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			for(String line=br.readLine();line!=null;line=br.readLine()){
				sb.append(line);
			}
		}finally{
			if(br!=null){
				br.close();
			}
		}
		System.out.println(sb.toString());
	}

	@Test
	public void encrypt(){
		File file=new File("D:\\sinotn\\SgeHome2\\NodeAppV1.2.3890.zip");
		System.out.println("正在加密");
		File endryptFile=this.encrypt(file);
		System.out.println("完成加密");
		System.out.println("正在签名");
		String sign=this.getSign(endryptFile);
		System.out.println("完成签名");
		String filename=endryptFile.getName();
		filename=filename.substring(0, filename.lastIndexOf("."))+"_"+sign+".sinotn";
		endryptFile.renameTo(new File(endryptFile.getParentFile(),filename));
	}

	@Test
	public void testSign() throws Exception{
		/*File file=new File("D:\\sinotn\\SgeHome2\\dt0.dt");
		String sign=this.getSign(file);
		System.out.println(sign);*/
		String text=URLDecoder.decode("%E4%B8%BB%E8%A6%81_26036023071.dt","utf-8");
		System.out.println(text);
	}

	public String getSign(File file){
		CRC32 crc32 = new CRC32();
		FileInputStream fis=null;
		CheckedInputStream cis=null;
		try {
			fis=new FileInputStream(file);
			cis=new CheckedInputStream(fis, crc32);
			byte[] buf=new byte[512];
			while (cis.read(buf) != -1) {}
			return String.valueOf(crc32.getValue());
		} catch (Throwable e) {
			SinotnLogger.DEBUG.error("计算考点程序文件校验码发生系统错误",e);;
		}  finally {
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
		return null;
	}

	public File encrypt(File file){
		InputStream is=null;
		OutputStream os=null;
		try{
			String filename=file.getName();
			File iRet=new File(file.getParentFile(),filename.substring(0, filename.lastIndexOf("."))+".sinotn");
			os=new FileOutputStream(iRet);
			is=new FileInputStream(file);
			byte[] buf=new byte[128];
			int j;
			for(int len=is.read(buf);len!=-1;len=is.read(buf)){
				for(j=0;j<len;j++){
					buf[j]=(byte)(buf[j]^DEFAULT_SALT);
				}
				os.write(buf,0,len);
			}
			os.close();os=null;
			is.close();is=null;
			return iRet;
		}catch(Throwable e){
			SinotnLogger.DEBUG.error("考点程序解码发生系统错误",e);
		}finally{
			if(is!=null){
				try{is.close();}catch(Exception e){}
			}
			if(os!=null){
				try{os.close();}catch(Exception e){}
			}
		}
		return null;
	}
}
