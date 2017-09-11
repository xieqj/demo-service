package com.sinotn.demo.itextpdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.sinotn.poi.CellValueExtractor;

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
public class ExamAccountPdf {

	@Test
	public void test(){
		String text=UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println(text);
	}

	@Test
	public void pdfStdAccount() throws Exception{
		File file=new File("D:\\sinotn\\实施\\黄金12月考试\\考点账号\\考点正式账号.xls");
		FileInputStream fis=new FileInputStream(file);
		Workbook wb=new HSSFWorkbook(fis);
		Sheet sheet=wb.getSheetAt(0);
		Row row;
		String examNodeName,account,password,studentSize;
		CellValueExtractor extractor=CellValueExtractor.getCellValueExtractor();
		int lrn=sheet.getLastRowNum();
		for(int i=lrn;i>0;i--){
			row=sheet.getRow(i);
			examNodeName=extractor.getStringValue(row.getCell(0));
			account=extractor.getStringValue(row.getCell(1));
			password=extractor.getStringValue(row.getCell(2));
			studentSize=extractor.getStringValue(row.getCell(3));
			this.writePdf(examNodeName, account, password, studentSize);
		}
		fis.close();
	}

	public void writePdf(String examNodeName,String account,String password,String studentSize) throws Exception{
		String file="D:\\sinotn\\实施\\黄金12月考试\\考点账号\\pdf\\"+examNodeName+".pdf";
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
			Font fontChinese = new Font(bfChinese, 10, Font.BOLD);
			Paragraph pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.setAlignment(Paragraph.ALIGN_CENTER);
			pragraph.add("全国黄金交易从业水平考试(12月11日)\r\n 时间： 9 : 30 - 11 : 00");
			doc.add(pragraph);

			fontChinese = new Font(bfChinese, 10, Font.NORMAL);
			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.setFirstLineIndent(20);
			pragraph.add("本文档包含有2016年12月11日全国黄金交易从业水平考试正式考试数据的服务器信息、账号密码信息，全国黄金交易从业水平考试是全国性的从业水平考试，考试是严肃的，数据是保密的，请各考点老师在接收后注意本文档的安全性和数据的安全性。\r\n");
			doc.add(pragraph);

			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.add("考点名称 ："+examNodeName);
			doc.add(pragraph);

			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.add("服务器IP ： 115.182.41.113");
			doc.add(pragraph);

			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.add("服务器端口：8080");
			doc.add(pragraph);

			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.add("用户账号 ：");
			Chunk chunk=new Chunk(account);
			chunk.setFont(FontFactory.getFont(FontFactory.HELVETICA, 12));
			pragraph.add(chunk);
			doc.add(pragraph);

			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.add("用户密码 ：");
			chunk=new Chunk(password);
			chunk.setFont(FontFactory.getFont(FontFactory.HELVETICA, 12));
			pragraph.add(chunk);
			doc.add(pragraph);

			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.add("总考生数 ："+studentSize);
			doc.add(pragraph);

			fontChinese = new Font(bfChinese, 10, Font.BOLD);
			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.add("注意事项");
			doc.add(pragraph);

			fontChinese = new Font(bfChinese, 10, Font.NORMAL);
			fontChinese.setColor(BaseColor.RED);

			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.setFirstLineIndent(20);
			pragraph.add("1、请做好用户账号和用户密码的保密工作。");
			doc.add(pragraph);

			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.setFirstLineIndent(20);
			pragraph.add("2、在下载正式数据后检查下载考生数量是否与本文档中考生数量是否一致，检查无误请进行封场，务必不能再进行测试工作。");
			doc.add(pragraph);

			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.setFirstLineIndent(20);
			pragraph.add("3、下载正式数据前请检查是否所有测试数据都已经上传。");
			doc.add(pragraph);

			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.setFirstLineIndent(20);
			pragraph.add("4、考试服务器还原功能是否已经停掉。");
			doc.add(pragraph);

			pragraph=new Paragraph();
			pragraph.setFont(fontChinese);
			pragraph.setFirstLineIndent(20);
			pragraph.add("5、下载考试数只需一台考试服务器（不是管理机）下载一次。");
			doc.add(pragraph);

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
