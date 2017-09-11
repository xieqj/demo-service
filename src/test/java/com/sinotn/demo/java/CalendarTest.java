package com.sinotn.demo.java;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class CalendarTest {

	@Test
	public void testTimezone(){
		Calendar calendar=Calendar.getInstance();
		TimeZone timeZone=calendar.getTimeZone();
		/*String[] ids=TimeZone.getAvailableIDs();
		for(String str:ids){
			System.out.println(str);
		}*/
		int rawOffset=timeZone.getRawOffset()/60000;
		System.out.println(rawOffset);
		String[] ids=TimeZone.getAvailableIDs(rawOffset);
		for(int i=0;i<ids.length;i++){
			System.out.println(i+"、"+ids[i]);
		}
		//28800000 25200000
		System.out.println(timeZone.getDisplayName());
	}
}
