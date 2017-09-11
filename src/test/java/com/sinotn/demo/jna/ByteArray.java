package com.sinotn.demo.jna;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface ByteArray extends StdCallLibrary {
	public static final ByteArray INSTANCE=(ByteArray)Native.loadLibrary("test", ByteArray.class);

	public int bytearraytest(byte[] source, int len, byte[] output);

	public long int64test();
}
