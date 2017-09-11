package com.sinotn.demo.jna;

import org.junit.Test;

public class Main {

	@Test
	public void testKsFaceconvert() throws Exception{
		String folderPath="D:\\demo\\photo\\";
		String filename="ouka3kcko813GdR55RkFnbqzPLTVQIoh.jpg";
		String filepath=folderPath+filename;
		System.out.println(filename);
		byte[] outbuffer=new byte[5*1024];
		//long t=System.currentTimeMillis();
		int faceCount=KsFace.INSTANCE.faceconvert(filepath, outbuffer);
		//System.out.println((System.currentTimeMillis()-t)+"ms");
		System.out.println("faceCount="+faceCount);
		//byte byteVal;
		/*int count=0;
		for(int i=0;i<outbuffer.length;i++){
			byteVal=outbuffer[i];
			if(byteVal!=0){
				//System.out.println(byteVal);
				count++;
			}
		}
		System.out.println(count);*/

		byte[] outbuffer2;
		String[] files=new String[]{"1501046104000.jpg","1501046712546.jpg","1501046722437.jpg","1501047356328.jpg"};
		for(int i=0;i<files.length;i++){
			System.out.println(files[i]);
			String filepath2=folderPath+"ouka3kcko813GdR55RkFnbqzPLTVQIoh\\"+files[i];
			outbuffer2=new byte[5*1024];
			faceCount=KsFace.INSTANCE.faceconvert(filepath2, outbuffer2);
			System.out.println("faceCount="+faceCount);
			//t=System.currentTimeMillis();
			int similar=KsFace.INSTANCE.facecompare(outbuffer, outbuffer2);
			//System.out.println((System.currentTimeMillis()-t)+"ms");
			System.out.println("similar="+similar);
		}

	}

	@Test
	public void testBytearraytest(){
		byte[] inputs=new byte[]{1,2,3,0,5,7,0,0};
		int len=inputs.length;
		byte[] output=new byte[len];
		ByteArray.INSTANCE.bytearraytest(inputs, len, output);
		for(byte b:output){
			System.out.println(b);
		}
	}

	@Test
	public void testInt64test(){
		long l=9223372036854775807L;
		System.out.println(ByteArray.INSTANCE.int64test()==l);
	}

}
