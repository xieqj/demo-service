package com.sinotn.demo.itextpdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 使用itextpdf开源组件生成pdf文件demo示例
 * @Title ItextpdfDemo.java
 * @Package com.sinotn.demo.itextpdf
 * @Description 
 * Copyright: Copyright (c) 2015
 * Company:北京信诺软通
 * 
 * @author <a href="mailto:xieqj@sinotn.com">谢启进</a>
 * @date 2016年6月21日 上午12:19:42
 * @version V1.0
 */
public class ItextpdfDemo {

	@Test
	public void testWritePdf() throws Exception{
		String file="d:/demo.pdf";
		OutputStream os=null;
		/*
		 http://www.cnblogs.com/crazyjava/p/3199936.html
		 */
		try{
			Document doc=new Document();
			os=new FileOutputStream(new File(file));
			PdfWriter.getInstance(doc, os);
					
			doc.open();
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font fontChinese = new Font(bfChinese, 10, Font.NORMAL);
			Paragraph pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.setAlignment(Paragraph.ALIGN_CENTER);
			pragraph.add("黄金市场基础知识与交易实务(试卷号：3)\r\n总分：100.0分    时间：120分钟");
			doc.add(pragraph);
			
			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.add("准考证：XXXXXXXXXX     姓名：信诺软通     证件号：441781198611251619");
			pragraph.add("\r\n考场：学知楼二层201室     座位号：100     答卷时间：2016-06-21 9:30:00至2016-06-21 9:30:00     试卷得分：60");
			doc.add(pragraph);
			
			pragraph=new Paragraph();
			Font tx = new Font(bfChinese, 10, Font.BOLD);
			pragraph.setFont(tx);
			pragraph.add("一、单选题  共60题  60分  每题1分");
			doc.add(pragraph);
			
			for(int i=1;i<=60;i++){
				pragraph=new Paragraph();
				pragraph.setFont(fontChinese);
				pragraph.add(i+"、会员清算准备金余额低于交易所规定的最低余额，应该（ ），追加的资金必须 在（ ）补足会员清算准备金余额低于交易所规定的最低余额。");
				pragraph.add("\r\nA.追加资金");
				pragraph.add("\r\nB.下一个交易日开市前");
				pragraph.add("\r\nC.保证金");
				pragraph.add("\r\nD.第二日");
				pragraph.add("\r\n原题号：12");
				pragraph.add("\r\n参考答案：A");
				pragraph.add("\r\n考生答案：B");
				doc.add(pragraph);
			}
			
			
			pragraph=new Paragraph();
			pragraph.setFont(tx);
			pragraph.add("二、多选题  共20题  40分  每题2分");
			doc.add(pragraph);
			
			for(int i=1;i<=20;i++){
				pragraph=new Paragraph();
				pragraph.setFont(fontChinese);
				pragraph.add(i+"、会员清算准备金余额低于交易所规定的最低余额，应该（ ），追加的资金必须在（ ）补足会员清算准备金余额低于交易所规定的最低余额。");
				pragraph.add("\r\nA.追加资金");
				pragraph.add("\r\nB.下一个交易日开市前");
				pragraph.add("\r\nC.保证金");
				pragraph.add("\r\nD.第二日");
				pragraph.add("\r\n原题号：22");
				pragraph.add("\r\n参考答案：AB");
				pragraph.add("\r\n考生答案：BC");
				doc.add(pragraph);
			}
			doc.close();
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			if(null!=os){
				os.close();
			}
		}
		
	}
}
