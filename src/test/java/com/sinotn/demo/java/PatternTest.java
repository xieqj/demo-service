package com.sinotn.demo.java;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class PatternTest {

	@Test
	public void testKuoHao(){
		String text="1(    ）A()B（）C(    ）D(    ）s";
		Pattern p=Pattern.compile("[\\(\uff08][\\s\u3000\u00a0]*[\\)\uff09]");
		System.out.println(this.formatTitle(p, text));
	}

	private String formatTitle(Pattern p, String text){
		String kuohao="（    ）";
		int totalLength=text.length();
		StringBuilder sb=new StringBuilder(totalLength);
		Matcher matcher=p.matcher(text);
		int index=0;
		int start,end;
		while(matcher.find()){
			start=matcher.start();
			end=matcher.end();
			if(start>index){
				sb.append(text.substring(index, start));
			}
			sb.append(kuohao);
			index=end;
		}
		if(index<totalLength){
			sb.append(text.substring(index));
		}
		return sb.toString();
	}

	@Test
	public void testWordSup(){
		String text="质量合格的密目安全网每10cm×10cm=100cm<sup>2</sup>面积上有（    ）（    ）（      ）个以上的网目。";
		Pattern p=Pattern.compile("<sup>\\d*</sup>");
		int totalLength=text.length();
		StringBuilder sb=new StringBuilder();
		Matcher matcher=p.matcher(text);
		int index=0;
		int start,end;
		while(matcher.find()){
			start=matcher.start();
			end=matcher.end();
			if(start>index){
				this.format(sb, text.substring(index, start));
			}
			sb.append("<w:r w:rsidRPr=\"00DF1195\"><w:rPr><w:vertAlign w:val=\"superscript\"/></w:rPr><w:t>");
			sb.append(text.substring(start+5, end-5));
			sb.append("</w:t></w:r>\r\n");
			index=end;
		}
		if(index<totalLength){
			this.format(sb, text.substring(index));
		}
		System.out.println(sb.toString());
	}

	private void format(StringBuilder output, String text){
		output.append("<w:r>");// w:rsidRPr=\"00145379\"
		output.append("<w:t>");
		int len=text.length();
		char c;
		for(int i=0;i<len;i++){
			c=text.charAt(i);
			if(c=='<'){
				output.append("&lt;");
			}else if(c=='>'){
				output.append("&gt;");
			}else{
				output.append(c);
			}
		}
		output.append("</w:t>");
		output.append("</w:r>\r\n");
	}

	@Test
	public void testIpSplit(){
		String text="192.168.1.136";
		Pattern p=Pattern.compile("\\.");
		String[] params=p.split(text);
		System.out.println(params.length);
	}

	@Test
	public void testPathFormat(){

		String path="ks\\Images\\box_list02.png";
		Pattern pattern=Pattern.compile(File.separator);
		path=pattern.matcher(path).replaceAll("/");
		System.out.println(path);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testFind(){
		HashMap context=new HashMap();
		context.put("currentVersion", "1.0.235");
		context.put("needVersion", "1.2.235");
		String text="${currentVersion}现${currentVersion}时运行系统版本号：${currentVersion}。需要升级到版本：${needVersion}；";
		Pattern p=Pattern.compile("\\$\\{[\\w\\.]+\\}");
		Matcher matcher=p.matcher(text);
		StringBuilder sb=new StringBuilder();

		int index=0;
		int start,end;
		String prop;
		Object value;
		while(matcher.find()){
			start=matcher.start();
			end=matcher.end();
			if(start>index){
				sb.append(text.substring(index, start));
			}
			prop=text.substring(start+2, end-1);
			value=context.get(prop);
			if(value==null){
				sb.append("${").append(prop).append("}");
			}else{
				sb.append(value);
			}
			index=end;
		}
		if(sb.length()==0){
			sb.append(text);
		}

		System.out.println(sb.toString());
	}

	@Test
	public void testFind3(){
		String text="m2dd立方米m3dd";
		Pattern p=Pattern.compile("m[23]");
		Matcher matcher=p.matcher(text);
		if(matcher.find()){
			StringBuilder sb=new StringBuilder();

			int index=0;
			int start,end;
			do{
				start=matcher.start();
				end=matcher.end();
				if(start>index){
					sb.append(text.substring(index, start));
				}
				sb.append(text.substring(start, start+1));
				sb.append("<sup>");
				sb.append(text.substring(start+1, end));
				sb.append("</sup>");
				index=end;
			}while(matcher.find());

			if(index<text.length()){
				sb.append(text.substring(index));
			}

			System.out.println(sb.toString());

		}else{
			System.out.println(text);
		}
	}



	@Test
	public void testFind2(){
		String text="A．财产险     		B．人寿险    		C．强制险    		D．非强制险";
		Pattern p=Pattern.compile("[ABCDE][\\.\uff0e]");
		String[] options=p.split(text);
		for(String str:options){
			str=str.trim();
			if(str.length()==0){
				continue;
			}
			System.out.println("["+str+"]");
		}
		/*Matcher matcher=p.matcher(text);
		StringBuilder sb=new StringBuilder();

		text.split("");
		int index=0;
		int start,end;
		String prop;
		Object value;
		if(matcher.find()){
			matcher.
		}
		while(matcher.find()){
			start=matcher.start();
			end=matcher.end();
			if(start>index){
				sb.append(text.substring(index, start));
			}
			prop=text.substring(start+2, end-1);
			value=context.get(prop);
			if(value==null){
				sb.append("${").append(prop).append("}");
			}else{
				sb.append(value);
			}
			index=end;
		}*/
	}
}
