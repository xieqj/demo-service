package com.sinotn.demo.poi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.StringReader;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class TestReader {

	@Test
	public void testWordExtractor()throws Exception{
		ClassPathResource cpr=new ClassPathResource("/com/sinotn/demo/poi/word3.doc");
		InputStream is=cpr.getInputStream();
		WordExtractor extractor = new WordExtractor(is);
		String text=extractor.getText();
		StringReader reader=new StringReader(text);
		BufferedReader br=new BufferedReader(reader);
		for(String line=br.readLine();line!=null;line=br.readLine()){
			System.out.println(line);
		}
		is.close();
		br.close();
	}

	@Test
	public void testHWPFDocument() throws Exception{
		ClassPathResource cpr=new ClassPathResource("/com/sinotn/demo/poi/word2.doc");
		InputStream is=cpr.getInputStream();
		HWPFDocument doc = new HWPFDocument(is);
		String text=doc.getDocumentText();
		StringReader reader=new StringReader(text);
		BufferedReader br=new BufferedReader(reader);
		for(String line=br.readLine();line!=null;line=br.readLine()){
			System.out.println(line);
		}

		is.close();
		br.close();
	}
}
