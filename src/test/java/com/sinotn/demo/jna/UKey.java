package com.sinotn.demo.jna;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface UKey extends StdCallLibrary {
	public static final UKey INSTANCE=(UKey)Native.loadLibrary("jnihaikey", UKey.class);
	/**
	 * function GetDevState(): Integer; stdcall; external 'jnihaikey.dll';
	 * 获取设备状态 返回0没有设备 返回1有设备
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2017年5月20日 上午9:50:04
	 */
	int GetDevState();
	/**
	 * function ConnectDev(): Integer; stdcall; external 'jnihaikey.dll';
	 * 连接设备 返回非-1表示连接成功,注意返回值后续函数要使用
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2017年5月20日 上午9:50:38
	 */
	int ConnectDev();
	/**
	 * function DisconnectDev(h: Integer): Integer; stdcall; external 'jnihaikey.dll';
	 * 断开设备连接 参数h是ConnectDev的返回值
	 * @param connId
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2017年5月20日 上午9:51:13
	 */
	int DisconnectDev(int connId);
	/**
	 * function SymEcb(h: Integer; ktype: integer; kmode: integer; inbuf: pchar; inlen: integer; outbuf: pchar): Integer; stdcall; external 'jnihaikey.dll';
	加密解密
	h:ConnectDev的返回值
	ktype:3表示使用SSF33算法,4表示使用SCB2算法
	kmode:1表示加密,2表示解密
	inbuf:输入数组
	inlen:输入数据长度(要是16的倍数)
	outbuf:输出数组
	返回值0成功其他表示失败
	 * @param connId ConnectDev的返回值
	 * @param ktype 3表示使用SSF33算法,4表示使用SCB2算法
	 * @param kmode 1表示加密,2表示解密
	 * @param inputs 输入数组
	 * @param inputLen 输入数据长度(要是16的倍数)
	 * @param output 输出数组
	 * @return
	 * 返回值0成功其他表示失败
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2017年5月20日 上午9:54:12
	 */
	int SymEcb(int connId,int ktype, int kmode, byte[] inputs, int inputLen, byte[] output);

	/**
	 * function GetTimepiece(h: Integer): int64; stdcall; external 'jnihaikey.dll';
	 * 参数h是ConnectDev的返回值
	 * 返回值是时间戳,如果返回0表示时间无效
	 * @param connId
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2017年5月20日 上午10:00:41
	 */
	long GetTimepiece(int connId);
}
