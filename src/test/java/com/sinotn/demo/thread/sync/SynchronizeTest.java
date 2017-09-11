package com.sinotn.demo.thread.sync;

public class SynchronizeTest {

	public static void main(String[] args) {
		Bank bank=new Bank();
		Thread t1=new Thread(new BankRunner(bank, "1"));
		t1.start();

		Thread t2=new Thread(new BankRunner(bank, "2"));
		t2.start();
	}

}
