package com.sinotn.demo.thread.sync;

public class Bank {
	private Object obj1=new Object();
	private Object obj2=new Object();

	public void transfer1(String id){
		synchronized (this.obj1) {
			System.out.println("Bank1");
			System.out.println(id);
		}		
	}
	
	public void transfer2(String id){
		synchronized (this.obj2) {
			System.out.println("Bank2");
			System.out.println(id);
		}
	}
	
	public synchronized void transfer3(String id){
		System.out.println("Bank3");
		System.out.println(id);	
	}
	
	public synchronized void transfer4(String id){
		System.out.println("Bank4");
		System.out.println(id);
	}
}
