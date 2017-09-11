package com.sinotn.demo.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.sinotn.demo.netty.handler.ByteBufUtil;

public class ByteBufTest {
	@Test
	public void test(){
		int intVal=4;
		int intResult=(intVal & -intVal);
		System.out.println(intResult);
		for(int i=0;i<10;i++){
			System.out.println(i&(intVal-1));
		}
		System.out.println("==========");
		AtomicInteger idx = new AtomicInteger();
		idx.set(Integer.MAX_VALUE);
		for(int i=0;i<20;i++){
			intResult=idx.incrementAndGet();
			System.out.println(intResult&(intVal-1));
		}

		/*String text="A中";
		byte[] buf=text.getBytes(CharsetUtil.UTF_8);
		System.out.println(buf.length);*/
		/*byte[] buf=new byte[]{65,0,66,67,68,69};
		ByteBuf byteBuf=Unpooled.wrappedBuffer(buf);
		System.out.println(byteBuf.maxCapacity()+"->"+byteBuf.capacity());*/
		/*int fromIndex=byteBuf.readerIndex();
		int toIndex=byteBuf.writerIndex();
		byte value=0;
		int index=byteBuf.indexOf(fromIndex, toIndex, value);
		ByteBuf slice=byteBuf.slice(fromIndex, index-fromIndex);
		System.out.println("["+slice.toString(CharsetUtil.UTF_8)+"]");
		byteBuf.readerIndex(index+1);
		System.out.println("["+byteBuf.toString(CharsetUtil.UTF_8)+"]");*/
	}

	@Test
	public void testFile() throws Exception{
		String filename="demo/test.txt";
		File directory=new File("d:/netty");
		File file=new File(directory,filename);
		File parent=file.getParentFile();
		if(parent.exists()==false){
			parent.mkdirs();
		}
		RandomAccessFile raf=new RandomAccessFile(file, "rw");
		FileChannel channel=raf.getChannel();
		byte[] buf=new byte[]{65,66,67,68,69};
		ByteBuf byteBuf=Unpooled.wrappedBuffer(buf);
		ByteBuffer byteBuffer=byteBuf.internalNioBuffer(1, 1);
		channel.write(byteBuffer);
		raf.close();
		System.out.println("["+byteBuf.toString(CharsetUtil.UTF_8)+"]");
	}

	@Test
	public void testByteBufUtil() throws Exception {
		Charset charset = Charset.forName("UTF-8");
		String text = "t中";
		byte[] bs = text.getBytes(charset);
		//bs=new byte[]{0,116, -28, -72, -83};
		System.out.println(bs.length);
		CharBuffer src = CharBuffer.wrap(text);
		ByteBuf buf = ByteBufUtil.encodeString(UnpooledByteBufAllocator.DEFAULT,false, src, charset, 2,0);
		System.out.println(buf.toString(charset));
	}


	@Test
	public void testSlice() throws Exception {
		Charset utf8 = Charset.forName("UTF-8");
		ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
		ByteBuf sliced = buf.slice(0, 14);
		System.out.println(sliced.toString(utf8));

		System.out.println("Integer Max Value:" + Integer.MAX_VALUE);
		for (int i = 0; i < 10; i++) {
			buf.setInt(0, Integer.MIN_VALUE + i);
			System.out.print(buf.getInt(0));
			System.out.println("; unsigned:" + buf.getUnsignedInt(0));
		}
		buf.setInt(0, -1);
		System.out.print(buf.getInt(0));
		System.out.println("; unsigned:" + buf.getUnsignedInt(0));
	}

	@Test
	public void testByteRead() throws Exception {
		byte[] b1 = { '\r', '\n' };
		for (int i = 0; i < b1.length; i++) {
			System.out.print("," + b1[i]);
		}
		System.out.println();
		String text = "Hello\nWorld";
		char[] cs = text.toCharArray();
		for (char c : cs) {
			System.out.println((byte) c);
		}
		byte[] b2 = text.getBytes();
		for (int i = 0; i < b2.length; i++) {
			System.out.print("," + b2[i]);
		}
		System.out.println();
	}
}
