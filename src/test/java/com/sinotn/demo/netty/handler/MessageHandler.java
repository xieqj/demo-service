package com.sinotn.demo.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class MessageHandler extends ExamHandler {

	public MessageHandler(){}

	@Override
	public void messageRead(ChannelHandlerContext ctx, Message msg) throws Exception {
		Channel channel=ctx.channel();
		NettySession session=channel.attr(SessionHandler.SESSION_KEY).get();
		System.out.println("通讯会话:"+session);
		String text=msg.getText();
		//"A中";[65, -28, -72, -83]
		if(text==null){
			System.out.println("接收到字符数：0");
		}else{
			if(text.length()>100){
				System.out.println("接收到字符数："+text.length());
			}else{
				System.out.println("接收到字符："+text);
			}
		}

		if("echo".equals(text)){
			msg.setType(Message.Type.RESPONSE.getValue());
			ctx.channel().writeAndFlush(msg);
		}
		if(text==null){
			ctx.close();
		}
	}

}
