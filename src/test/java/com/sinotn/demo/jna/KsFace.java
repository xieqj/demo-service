package com.sinotn.demo.jna;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface KsFace extends StdCallLibrary {
	public static final KsFace INSTANCE=(KsFace)Native.loadLibrary("ksface", KsFace.class);

	int faceconvert(String filepath,byte[] outbuffer);

	int facecompare(byte[] photoBytes1, byte[] photoBytes2);
}
