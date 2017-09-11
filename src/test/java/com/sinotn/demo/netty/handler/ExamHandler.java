package com.sinotn.demo.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;

import java.io.File;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import com.sinotn.demo.netty.NettyLogger;

public class ExamHandler extends ChannelDuplexHandler{
	/**
	 * 消息头存储字节长度(消息类型1字节+消息令牌4字节+文件消息列表大小1字节)共6字节
	 */
	public static final int HEAD_BYTE_LENGTH=6;
	private Cumulator cumulator=MergeCumulator.get();
	private boolean debug=NettyLogger.DEBUG.isDebugEnabled();
	private Charset charset;
	private boolean enforceHeap;

	//消息结果
	private MessageHelper helper;
	//累计接收到的网络缓存数据
	private ByteBuf cumulation;

	public ExamHandler(){
		this("UTF-8",false);
	}

	public ExamHandler(String charset,boolean enforceHeap){
		this.charset=Charset.forName(charset);
		this.enforceHeap=enforceHeap;
	}
	/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 inbound方法
	 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.channelClosed(ctx);
		ctx.fireChannelInactive();
	}
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof ChannelInputShutdownEvent) {
			this.channelClosed(ctx);
		}
		ctx.fireUserEventTriggered(evt);
	}
	private void channelClosed(ChannelHandlerContext ctx) throws Exception {
		if(this.cumulation!=null&&this.cumulation.isReadable()){
			this.decode(ctx, this.cumulation);
		}
		if(null!=this.helper){
			if(this.helper.isFinish()){
				this.messageRead(ctx, this.helper.getMessage());
				ctx.fireChannelReadComplete();
			}
			this.helper.release();
			this.helper=null;
		}
		if(null!=this.cumulation){
			int refCnt=this.cumulation.refCnt();
			if(refCnt>0){
				this.cumulation.release(refCnt);
			}
			this.cumulation=null;
		}
	}

	@Override
	public final void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		this.channelClosed(ctx);
	}

	/**
	 * 考试机新消息可读
	 * 与channelReadComplete方法存在：1比1或1比n的匹配调用
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof ByteBuf){
			try{
				ByteBuf data = (ByteBuf) msg;
				if(debug){
					NettyLogger.DEBUG.debug("收到消息:size="+data.readableBytes());
				}
				//每次读取到的字节数据先汇总追加到一个ByteBuf
				if (this.cumulation == null) {
					this.cumulation = data;
				} else {
					this.cumulation = this.cumulator.cumulate(ctx.alloc(), this.cumulation, data);
				}
				//对汇总追加总ByteBuf进行解码
				this.decode(ctx,this.cumulation);
			}catch(DecoderException e){
				throw e;
			}catch (Throwable t) {
				throw new DecoderException(t);
			} finally{
				if(this.cumulation!=null&&!this.cumulation.isReadable()){
					this.cumulation.release();
					this.cumulation = null;
				}
			}
		}else{
			//传递给下一个处理器
			ctx.fireChannelRead(msg);
		}
	}

	/**
	 * 对字节数据进行解码，每解码一个Message实例发送给下一个Handler处理
	 * @param ctx
	 * @param in
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月22日 下午2:27:27
	 */
	protected void decode(ChannelHandlerContext ctx,ByteBuf in){
		try{
			while(in.isReadable()){
				int oldInputLength = in.readableBytes();
				//1、头信息读取
				if(this.helper==null){
					if(in.readableBytes()<HEAD_BYTE_LENGTH){//接收到的字节未满足消息头解码
						break;
					}
					//1、消息头信息解码
					//消息类型(1字节)
					short type=in.readUnsignedByte();
					//消息令牌(4字节)
					long token=in.readUnsignedInt();
					//文件数(2字节)
					short fileSize=in.readUnsignedByte();

					this.helper=MessageHelper.get(type, token,fileSize);
				}

				if(false==this.helper.isTextDecoded()){
					//2、文本消息解码
					int textSize=this.helper.getHeadTextSize();
					if(textSize==-1){
						//读取文本段头信息(文本长度)
						if(in.readableBytes()<2){
							break;
						}
						textSize=in.readUnsignedShort();
						this.helper.setHeadTextSize(textSize);
					}
					if(textSize>0){
						if(in.readableBytes()<textSize){
							break;
						}
						ByteBuf buf=in.slice(in.readerIndex(), textSize);
						this.helper.append(buf.toString(charset), textSize);
						in.readerIndex(in.readerIndex()+textSize);
					}
				}else if(this.helper.getHeadFileSize()>0){
					//3、文件消息解码
					File file=this.helper.getFileExtractor().extract(in,charset);
					if(null!=file){
						this.helper.appendFile(file);
					}
				}else{
					throw new DecoderException("非法请求编码数据");
				}
				//检查是否解析完成的消息，如果有则发送给下一个处理器进行下一步操作
				this.afterDecode(ctx);
				if (oldInputLength == in.readableBytes()) {//避免死循环
					break;
				}else if(ctx.isRemoved()){
					break;
				}
			}
			this.afterDecode(ctx);
		} catch (DecoderException e) {
			throw e;
		} catch (Throwable cause) {
			throw new DecoderException(cause);
		}
	}

	/**
	 * 每次编码后进行编码结果检查，如果当前消息对象Message解析完整，则调用Message相关方法消费
	 * @param ctx
	 * @throws Exception
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月22日 下午4:38:06
	 */
	private void afterDecode(ChannelHandlerContext ctx) throws Exception {
		if(null==this.helper) return;
		if(this.helper.isFinish()){
			this.messageRead(ctx, this.helper.getMessage());
			this.helper.release();
			this.helper=null;
		}
	}

	/**
	 * 对解码到消息对象进行业务处理
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月22日 下午4:36:48
	 */
	public void messageRead(ChannelHandlerContext ctx,Message msg) throws Exception{}

	protected final void release(){
		if(null!=this.helper){
			this.helper.release();
			this.helper=null;
		}
		if(null!=this.cumulation){
			if(this.cumulation.refCnt()>0){
				this.cumulation.release(this.cumulation.refCnt());
			}
			this.cumulation=null;
		}
	}

	/**
	 * 处理器内部发生异常
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(cause instanceof DecoderException){
			this.release();
			NettyLogger.DEBUG.error("数据解码发生系统错误",cause);
			//出现解码异常则关闭当前channel
			ctx.close();
		}else{
			ctx.fireExceptionCaught(cause);
		}
	}


	/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 outbound方法
	 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	/**
	 *
	 */
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if(null==msg) return;
		ByteBuf buf = null;
		StringEncode stringEncode=null;
		try{
			if(msg instanceof Message){
				Message iMessage=(Message)msg;
				ByteBufAllocator alloc=ctx.alloc();
				ChannelPromise voidPromise = ctx.voidPromise();

				//1、设置消息头ByteBuf
				buf = this.newByteBuf(alloc, HEAD_BYTE_LENGTH);
				//1.1、设置消息头－消息类型1字节
				buf.writeByte(iMessage.getType());
				//1.2、设置消息头－消息令牌4字节
				buf.writeInt((int)iMessage.getToken());
				//1.3、设置消息头－文件消息列表大小1字节
				final int fileSize=iMessage.getFileSize();//后结发送文件消息继续使用该变量，声明为final避免误设值
				buf.writeByte(fileSize);
				ctx.write(buf, voidPromise);
				buf = null;

				//2、发送文本信息
				String text=iMessage.getText();
				if(null!=text&&text.length()>0){
					stringEncode=new StringEncode(text,this.charset);
					stringEncode.setAlloc(alloc);
					stringEncode.setEnforceHeap(this.enforceHeap);
					for(buf=stringEncode.next();buf!=null;buf=stringEncode.next()){
						if(stringEncode.hasNext()){
							ctx.write(buf,voidPromise);
						}else{
							ChannelPromise p=voidPromise;
							if(fileSize==0){
								p=promise;
							}
							//当前是最后一个编码ByteBuf,发送两字节0表示文本信息结束
							if(buf.writableBytes()>=2){
								buf.writeShort(0);
								ctx.write(buf, p);
							}else{
								ctx.write(buf, voidPromise);
								//新建两字节发送0表示文本信息结束
								buf=this.newByteBuf(alloc, 2);
								buf.writeShort(0);
								ctx.write(buf, p);
							}
						}
					}
					buf = null;
				}else{
					//新建两字节发送0表示文本信息结束
					buf=this.newByteBuf(alloc, 2);
					buf.writeShort(0);
					ChannelPromise p=voidPromise;
					if(fileSize==0){
						p=promise;
					}
					ctx.write(buf, p);
					buf=null;
				}

				//3、发送文件消息
				if(fileSize>0){
					File[] fs=iMessage.getFiles();
					DefaultFileRegion region;
					File file;
					int lastIndex=fileSize-1;
					long fileLength;
					for(int i=0;i<fileSize;i++){
						file=fs[i];
						//跟文件名称编码
						buf=ByteBufUtil.encodeString(alloc, enforceHeap, CharBuffer.wrap(file.getName()), charset, 2,4);
						//文件名编码字节长度
						buf.setShort(0, (buf.readableBytes()-2));
						//文件大小
						fileLength=file.length();
						buf.writeInt((int)fileLength);
						ctx.write(buf, voidPromise);
						buf=null;
						//发送文件
						region=new DefaultFileRegion(file, 0, fileLength);
						ctx.write(region,(i==lastIndex)?promise:voidPromise);
					}
				}
				//4、flush发送消息数据
				ctx.flush();
			}else{
				//传递给下一个编码处理器
				ctx.write(msg, promise);
			}
		} catch (EncoderException e) {
			throw e;
		} catch (Throwable t) {
			throw new EncoderException(t);
		} finally {
			if(null!=buf){
				buf.release();
			}
			if(null!=stringEncode){
				stringEncode.release();
			}
		}
	}
	/**
	 * 创建一个新的ByteBuf
	 * @param alloc
	 * @param length
	 * @return
	 * @auth <a href="mailto:xieqj@sinotn.com">谢启进</a>
	 * @date 2016年7月21日 下午9:20:04
	 */
	private ByteBuf newByteBuf(ByteBufAllocator alloc,int length){
		if (this.enforceHeap) {
			return alloc.heapBuffer(HEAD_BYTE_LENGTH);
		} else {
			return alloc.buffer(HEAD_BYTE_LENGTH);
		}
	}
}
