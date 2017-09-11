package com.sinotn.demo.jna;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface Sqlite extends StdCallLibrary {
	public static final Sqlite INSTANCE=(Sqlite)Native.loadLibrary("sqlitease", Sqlite.class);

	public int SQLiteEncrypt(String path, String passwd, int passwdByteLen);

	public int SQLiteDecode(String path, String passwd, int passwdByteLen);
}
