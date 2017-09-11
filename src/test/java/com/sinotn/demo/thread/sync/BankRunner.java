package com.sinotn.demo.thread.sync;

public class BankRunner implements Runnable {
	private String id;
	private Bank bank;
	
	public BankRunner(Bank bank,String id){
		this.bank=bank;
		this.id=id;
	}

	@Override
	public void run() {
		while(true){
			if("1".equals(this.id)){
				bank.transfer1(this.id);
			}else if("2".equals(this.id)){
				bank.transfer2(this.id);
			}else if("3".equals(this.id)){
				bank.transfer3(this.id);
			}else {
				bank.transfer4(this.id);
			}
		}
	}

}
