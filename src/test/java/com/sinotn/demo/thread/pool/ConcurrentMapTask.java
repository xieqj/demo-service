package com.sinotn.demo.thread.pool;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapTask implements Runnable {
	private ConcurrentHashMap<String, String> maps;
	private int flag;

	public ConcurrentMapTask(ConcurrentHashMap<String, String> maps,int flag){
		this.maps=maps;
		this.flag=flag;
	}

	@Override
	public void run() {
		if(0==this.flag){
			Random random=new Random();
			while(true){
				if(this.maps.size()>10000) continue;
				String text=String.valueOf(random.nextInt());
				this.maps.put(text, text);
			}
		}else if(1==this.flag){
			while(true){
				if(this.maps.size()>0){
					this.maps.remove(this.maps.keySet().iterator().next());
				}
			}
		}else{
			while(true){
				if(this.maps.size()==0) continue;
				Set<Entry<String, String>> set=this.maps.entrySet();
				int i=0;
				for(Iterator<Entry<String, String>> it=set.iterator();it.hasNext();){
					it.next();
					i++;
				}
				System.out.println(i+"->"+this.maps.size());
			}

		}
	}

}
