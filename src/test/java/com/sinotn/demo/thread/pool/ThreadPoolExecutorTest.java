package com.sinotn.demo.thread.pool;

import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class ThreadPoolExecutorTest {
	private ThreadPoolExecutor executor;

	@Before
	public void befor(){
		int coreTaskThreadSize=4;
		int maxTaskThreadSize=4;
		int threadIdleTime=1;
		int taskQueueSize=100;
		executor=new ThreadPoolExecutor(
				coreTaskThreadSize,
				maxTaskThreadSize,
				threadIdleTime,
				TimeUnit.MINUTES,
				new LinkedBlockingQueue<Runnable>(taskQueueSize),
				Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.CallerRunsPolicy());//异步阻塞立即执行策略
	}

	@Test
	public void concurrent(){
		Scanner scanner=new Scanner(System.in);
		while(true){
			String line=scanner.nextLine();
			System.out.println("接收到输入字符串:"+line);
			if("exit".equals(line)){
				this.executor.shutdown();
			}else{
				int size=Integer.valueOf(line);
				ConcurrentHashMap<String, String> maps=new ConcurrentHashMap<String, String>();
				for(int i=0;i<size;i++){
					this.executor.execute(new ConcurrentMapTask(maps,i));
				}
			}
		}
	}

	@Test
	public void console(){
		Scanner scanner=new Scanner(System.in);
		while(true){
			String line=scanner.nextLine();
			System.out.println("接收到输入字符串:"+line);
			if("exit".equals(line)){
				this.executor.shutdown();
			}else{
				try{
					String[] params=line.split(",");
					int size=Integer.valueOf(params[0]);
					int repeat=1;
					if(params.length>1){
						repeat=Integer.valueOf(params[1]);
					}
					for(int i=0;i<size;i++){
						this.executor.execute(new Task(String.valueOf(i),repeat));
					}

				}catch(Throwable e){
					e.printStackTrace();
				}
			}
		}
	}

	private class Task implements Runnable{
		private String prefix;
		private int size;

		Task(String prefix,int size){
			this.prefix=prefix;
			this.size=size;
		}

		@Override
		public void run() {
			for(int i=0;i<size;i++){
				System.out.println(Thread.currentThread().getName()+"-"+this.prefix+"-"+i);
			}
		}
	}
}
