package com.sinotn.demo.encode;

import org.junit.Test;

public class Main {

	@Test
	public void testTimestampEncode(){
		long timestamp=System.currentTimeMillis();
		int shift=5;
		String str;
		for(int i=0;i<10;i++){
			str=BaseConvert.compressNumber2(timestamp, shift);
			System.out.println(str+"->"+BaseConvert.compressNumber(timestamp, shift));
			timestamp++;
		}

	}

}
