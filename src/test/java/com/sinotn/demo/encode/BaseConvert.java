package com.sinotn.demo.encode;

public class BaseConvert {

	/**
	 * 进制编码表, 使用base64编码表作为基础, 使用'-'替换'+', 使用'.'替换'/',
	 * 目的是UUIDUtil使用生成的id可以在url不编码就可以传递
	 */
	final static char[] digits = {
		'0' , '1' , '2' , '3' , '4' , '5' ,
		'6' , '7' , '8' , '9' , 'a' , 'b' ,
		'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
		'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
		'o' , 'p' , 'q' , 'r' , 's' , 't' ,
		'u' , 'v' , 'w' , 'x' , 'y' , 'z' ,
		'A' , 'B' , 'C' , 'D' , 'E' , 'F' ,
		'G' , 'H' , 'I' , 'J' , 'K' , 'L' ,
		'M' , 'N' , 'O' , 'P' , 'Q' , 'R' ,
		'S' , 'T' , 'U' , 'V' , 'W' , 'X' ,
		'Y' , 'Z' , '-' , '.' ,
	};

	/**
	 * 把10进制的数字转换成2^shift进制 字符串
	 * @param number 10进制数字
	 * @param shift 5表示32进制，6表示64进制，原理 2^shift
	 * @return 2^shift进制字符串
	 */
	public static String compressNumber(long number, int shift) {

		char[] buf = new char[64];
		int charPos = 64;
		int radix = 1 << shift;
		long mask = radix - 1;
		do {
			buf[--charPos] = digits[(int)(number & mask)];
			number >>>= shift;
		} while (number != 0);
		return new String(buf, charPos, (64 - charPos));
	}

	public static String compressNumber2(long number, int shift) {

		char[] buf = new char[64];
		int charPos = 64;
		int radix = 1 << shift;
		long mask = radix - 1;
		do {
			buf[--charPos] = digits[(int)(mask-(number & mask))];
			number >>>= shift;
		} while (number != 0);
		return new String(buf, charPos, (64 - charPos));
	}

	/**
	 * 把2^shift进制的字符串转换成10进制
	 * @param decompStr 2^shift进制的字符串
	 * @param shift 5表示32进制，6表示64进制，原理 2^shift
	 * @return 10进制数字
	 */
	public static long decompressNumber(String decompStr, int shift) {

		long result = 0;
		for (int i = decompStr.length() - 1; i >= 0; i--) {
			if (i == decompStr.length() - 1) {
				result += getCharIndexNum(decompStr.charAt(i));
				continue;
			}
			for (int j = 0; j < digits.length; j++) {
				if (decompStr.charAt(i) == digits[j]) {
					result += ((long)j) << shift * (decompStr.length() - 1 - i);
				}
			}
		}
		return result;
	}

	/**
	 * 将字符转成数值
	 * @param ch -- 字符
	 * @return -- 对应数值
	 */
	private static long getCharIndexNum(char ch) {

		int num = (ch);
		if ( num >= 48 && num <= 57) {
			return num - 48;
		} else if (num >= 97 && num <= 122) {
			return num - 87;
		} else if (num >= 65 && num <= 90) {
			return num - 29;
		} else if (num == 43)  {
			return 62;
		} else if (num == 47)  {
			return 63;
		}
		return 0;
	}
}
