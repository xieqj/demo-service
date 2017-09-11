package com.sinotn.demo.nio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

public class Buffer {
	private int fileCount=0;
	private int directoryCount=0;

	@Test
	public void testTransfer() throws Exception{
		File srcFolder=new File("D:\\sinotn\\SgeHome");
		File destFolder=new File("F:/");
		File[] fs=srcFolder.listFiles();
		long t=System.currentTimeMillis();
		for(int i=0;i<fs.length;i++){
			this.copy(fs[i], destFolder);
		}
		t=System.currentTimeMillis()-t;
		System.out.println(t+"ms fileCount="+this.fileCount+"; directoryCount="+this.directoryCount);
	}

	public void copy(File src,File dest) throws Exception{
		if(src.isDirectory()){
			directoryCount++;
			File target=new File(dest,src.getName());
			target.mkdir();
			File[] fs=src.listFiles();
			if(fs!=null&&fs.length>0){
				for(int i=0;i<fs.length;i++){
					this.copy(fs[i], target);
				}
			}
		}else{
			fileCount++;
			RandomAccessFile aFile = new RandomAccessFile(src,"r");
			RandomAccessFile aFile2 = new RandomAccessFile(new File(dest,src.getName()),"rw");
			aFile.getChannel().transferTo(0, aFile.length(), aFile2.getChannel());
			aFile.close();
			aFile2.close();
		}
	}

	@Test
	public void testRead() throws Exception{
		//String path="D:/demo.txt";
		String path="D:/db.sql";
		RandomAccessFile aFile = new RandomAccessFile(path,"rw");
		FileChannel inChannel = aFile.getChannel();
		//create buffer with capacity of 48 bytes
		ByteBuffer buf = ByteBuffer.allocate(48);
		int bytesRead = inChannel.read(buf); //read into buffer.
		int counter=0;
		while (bytesRead != -1) {
			buf.flip();  //make buffer ready for read
			//while(buf.hasRemaining()){
			//System.out.print((char) buf.get()); // read 1 byte at a time
			//}
			System.out.println(buf.limit());
			buf.clear(); //make buffer ready for writing
			bytesRead = inChannel.read(buf);
			counter++;
		}
		System.out.println(counter);
		aFile.close();
	}

	@Test
	public void testRead2() throws Exception{
		String path="D:/db.sql";
		BufferedReader reader=new BufferedReader(new FileReader(new File(path)));
		for(String line=reader.readLine();line!=null;line=reader.readLine()){
			System.out.println(line);
		}
		reader.close();
	}
}
