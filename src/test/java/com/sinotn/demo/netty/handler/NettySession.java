package com.sinotn.demo.netty.handler;

public class NettySession {
	private String channelId;
	private String ipAddr;

	public NettySession(String channelId,String ipAddr){
		this.channelId=channelId;
		this.ipAddr=ipAddr;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void close(){
		System.out.println("session is closed");
	}
}
