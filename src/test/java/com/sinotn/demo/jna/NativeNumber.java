package com.sinotn.demo.jna;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface NativeNumber extends StdCallLibrary{
	public static final NativeNumber INSTANCE=(NativeNumber)Native.loadLibrary("number64", NativeNumber.class);

	int Encrypt(byte[] source, byte[] output);
}
