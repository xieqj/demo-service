package com.sinotn.demo.digest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.digester.Digester;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class DigestUtilsTest {
	private String ref;
	private String title;
	private ArrayList<String> option=new ArrayList<String>();

	@Test
	public void parseToMap()throws Exception{
		ClassPathResource cpr=new ClassPathResource("com/sinotn/demo/digest/map.xml");
		InputStream is=cpr.getInputStream();
		Digester dgt=new Digester();
		dgt.setValidating(false);
		HashMap<String, Object> map=new HashMap<String, Object>();
		dgt.push(map);

		dgt.addRule("ext/organ", new TagMapRule());

		dgt.parse(is);
		System.out.println(map);
		is.close();
	}

	@Test
	public void testRead()throws Exception{
		String path="d:/question.txt";
		String path1="d:/question.xml";
		PrintWriter pw=new PrintWriter(new File(path1));
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		//Pattern p=Pattern.compile("^\\d{2}\\..+");
		Pattern p=Pattern.compile("^\\d{1}.+");
		for(String line=br.readLine();line!=null;line=br.readLine()){
			line=line.trim();
			if(line.length()==0) continue;
			if(p.matcher(line).matches()){
				if(this.ref!=null){
					this.writeXml(pw);
				}
				this.ref="81-"+line.substring(0, 1);
				this.title=line.substring(2);
			}else{
				this.option.add(line.substring(2));
			}
		}
		br.close();
		if(this.ref!=null){
			this.writeXml(pw);
		}
		pw.flush();
		pw.close();
		System.out.println("处理完成");
	}

	private void writeXml(PrintWriter pw){
		pw.println("<question>");
		pw.println("<type>a42</type>");
		pw.println("<difficulty>2</difficulty>");
		pw.print("<refNo>");
		pw.print(this.ref);
		this.ref=null;
		pw.println("</refNo>");
		pw.println("<subject><![CDATA[金融英语]]></subject>");
		pw.println("<section><![CDATA[1.1.1]]></section>");
		pw.print("<title><![CDATA[");
		pw.print(this.title);
		this.title=null;
		pw.println("]]></title>");
		int size=this.option.size();
		pw.println("<options>");
		for(int i=0;i<size;i++){
			pw.println("<option>");
			pw.print("<content><![CDATA[");
			pw.print(this.option.get(i));
			pw.println("]]></content>");
			if(i==0){
				pw.println("<isAnswer>false</isAnswer>");
			}else{
				pw.println("<isAnswer>false</isAnswer>");
			}

			pw.println("</option>");
		}
		pw.println("</options>");
		pw.println("</question>");
		this.option.clear();
	}

	@Test
	public void testFileDigest() throws Exception{
		String path="D:\\sinotn\\文档\\金融英语考试\\示例试卷\\听力音频文件";
		String pathOut=path+"/out";
		File folder=new File(path);
		File folderOut=new File(pathOut);
		File[] fs=folder.listFiles();
		File file;
		for(int i=0;i<fs.length;i++){
			file=fs[i];
			if(file.isDirectory()) continue;
			this.process(file, new File(folderOut,file.getName()));
		}

		fs=folderOut.listFiles();
		folder=new File(path+"/md5");
		for(int i=0;i<fs.length;i++){
			file=fs[i];
			if(file.isDirectory()) continue;
			this.md5(file, folder);
		}
	}

	private void md5(File src,File folder) throws Exception{
		InputStream is=null;
		try{
			is=new FileInputStream(src);
			String md5Str=DigestUtils.md5Hex(is);
			is.close();
			is=null;
			FileUtils.copyFile(src, new File(folder,md5Str+"_"+src.getName()));
		}finally{
			IOUtils.closeQuietly(is);
		}
	}

	private void process(File src,File dest) throws Exception{
		InputStream is=null;
		OutputStream os=null;
		try{
			is=new FileInputStream(src);
			os=new FileOutputStream(dest);
			byte[] buf=new byte[128];
			int j;
			for(int len=is.read(buf);len!=-1;len=is.read(buf)){
				for(j=0;j<len;j++){
					buf[j]=(byte)(buf[j]^110);
				}
				os.write(buf,0,len);
			}
			IOUtils.closeQuietly(os);
			os=null;
			IOUtils.closeQuietly(is);
			is=null;
			//DigestUtils.md
		}catch(Throwable e){
			e.printStackTrace();
		}
		finally{
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
		}
	}
}
