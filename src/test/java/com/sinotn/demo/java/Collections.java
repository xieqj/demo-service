package com.sinotn.demo.java;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class Collections {

	@Test
	public void testIterator(){
		ConcurrentHashMap<Integer, Integer> map=new ConcurrentHashMap<Integer, Integer>();
		for(int i=0;i<10;i++){
			map.put(i, i);
		}
		System.out.println(map);

		int val;
		for(Iterator<Integer> it=map.values().iterator();it.hasNext();){
			val=it.next();
			if(val>5){
				map.remove(val);
			}
		}
		System.out.println(map);
	}
}
