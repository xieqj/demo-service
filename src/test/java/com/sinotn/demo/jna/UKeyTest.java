package com.sinotn.demo.jna;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.sinotn.util.StreamUtil;

public class UKeyTest {

	@Test
	public void testGetDevState(){
		System.out.println(UKey.INSTANCE.GetDevState());
	}

	@Test
	public void testConnectDev(){
		int connId=UKey.INSTANCE.ConnectDev();
		System.out.println(connId);
		int iRet=UKey.INSTANCE.DisconnectDev(connId);
		System.out.println(iRet);
	}

	@Test
	public void testGetTimepiece(){
		int connId=UKey.INSTANCE.ConnectDev();
		System.out.println(connId);
		long timestamp=UKey.INSTANCE.GetTimepiece(connId+1);
		System.out.println(timestamp);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(new Date(timestamp)));
	}

	@Test
	public void testSymEcb1() throws Exception {
		InputStream is=null;
		OutputStream os=null;
		File source=new File("D:\\demo\\data.rar");
		File encrypt=new File("D:\\demo\\encrypt.rar");
		//File decrypt=new File("D:\\demo\\decrypt.rar");
		int connId=UKey.INSTANCE.ConnectDev();
		int ktype=3;
		int kmode=1;
		int inputLen=2048;
		byte[] inputs=new byte[inputLen];
		byte[] output=new byte[inputLen];
		int len;
		//加密
		is=new FileInputStream(source);
		os=new FileOutputStream(encrypt);
		System.out.println(source.length()+"->"+is.available());
		StreamUtil.writeLong(os, source.length());
		long t=System.currentTimeMillis();
		for(len=is.read(inputs);len!=-1;len=is.read(inputs)){
			System.out.println(len);
			UKey.INSTANCE.SymEcb(connId, ktype, kmode, inputs, inputLen, output);
			os.write(output);
		}
		System.out.println(System.currentTimeMillis()-t);
		os.close();
		is.close();
	}

	@Test
	public void testStrEncrypt() throws Exception {
		String text="谢启进";
		byte[] datas=text.getBytes("UTF-8");
		System.out.println(datas.length);
		int connId=UKey.INSTANCE.ConnectDev();
		int ktype=3;
		int kmode=1;
		int dataLen=datas.length;

		int inputLen=((dataLen-1)/16)*16+16;
		byte[] inputs=datas;
		if(dataLen<inputLen){
			inputs=new byte[inputLen];
			System.arraycopy(datas, 0, inputs, 0, dataLen);
		}
		byte[] output=new byte[inputLen];
		int iRet=UKey.INSTANCE.SymEcb(connId, ktype, kmode, inputs, inputLen, output);
		System.out.println(iRet);
		String base64=Base64.encodeBase64String(output);
		System.out.println(base64);
		inputs=Base64.decodeBase64(base64);
		output=new byte[inputs.length];
		kmode=2;
		UKey.INSTANCE.SymEcb(connId, ktype, kmode, inputs, inputs.length, output);
		System.out.println("["+new String(output, "UTF-8").trim()+"]");
		/*String base64=Base64.encodeBase64String(output);
		System.out.println(base64);*/


	}

	@Test
	public void testLength(){
		int outputLen;
		for(int i=0;i<36;i++){
			outputLen=((i-1)/16)*16+16;
			System.out.println(i+"->"+outputLen);
		}
	}

	@Test
	public void testSymEcb2() throws Exception {
		InputStream is=null;
		OutputStream os=null;
		//File source=new File("D:\\demo\\data.rar");
		File encrypt=new File("D:\\demo\\encrypt.rar");
		File decrypt=new File("D:\\demo\\decrypt.rar");
		int connId=UKey.INSTANCE.ConnectDev();
		int ktype=3;
		int kmode=2;
		int inputLen=2048;
		byte[] inputs=new byte[inputLen];
		byte[] output=new byte[inputLen];
		int len;
		//解密
		is=new FileInputStream(encrypt);
		long filesize=StreamUtil.readLong(is);
		os=new FileOutputStream(decrypt);
		System.out.println(encrypt.length()+"->"+is.available());
		StreamUtil.writeLong(os, encrypt.length());
		long t=System.currentTimeMillis();
		for(len=is.read(inputs);len!=-1;len=is.read(inputs)){
			System.out.println(len);
			os.write(output);
			UKey.INSTANCE.SymEcb(connId, ktype, kmode, inputs, inputLen, output);
			if(filesize<inputLen){
				os.write(output, 0, (int)filesize);
				break;
			}else{
				filesize-=inputLen;
				os.write(output);
			}
		}
		System.out.println(System.currentTimeMillis()-t);
		os.close();
		is.close();


		//解密
		//int kmodeDecrypt=2;
		//UKey.INSTANCE.SymEcb(connId, ktype, kmodeEncrypt, inputs, inputLen, output);
	}
}
