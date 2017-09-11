package com.sinotn.demo.administrative;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.sinotn.id.SeqUUIDGenerator;

public class Bootstrap {

	@Test
	public void testUUID(){
		for(int i=0;i<43;i++){
			String uuid=UUID.randomUUID().toString();
			//System.out.println(uuid);
			Pattern p=Pattern.compile("-");
			uuid=p.matcher(uuid).replaceAll("");
			System.out.println(uuid);
		}
	}

	@Test
	public void testTimestamp() throws Exception {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long l=sdf.parse("2017-01-20 12:00:00").getTime();
		System.out.println(l);

		//l=1485086902084;
		Date d=new Date(1485086902084l);
		System.out.println(sdf.format(d));

	}

	@Test
	public void logAnalyse(){
		String path="D:\\sinotn\\考试数据\\logs\\examinee_runtime.log";
		String output="D:\\sinotn\\考试数据\\logs\\output.txt";
		String examCertId="320982198708202287";
		BufferedReader br=null;
		PrintStream ps=null;
		try{
			br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
			ps=new PrintStream(new File(output));
			String line;
			while((line=br.readLine())!=null){
				line=line.trim();
				if(line.length()==0){
					continue;
				}
				if(line.indexOf(examCertId)!=-1){
					ps.println(line);
				}
			}
			ps.close();
			ps=null;
			br.close();
			br=null;
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			if(br!=null){
				try{br.close();}catch(Throwable e){}
			}
			if(ps!=null){
				try{ps.close();}catch(Throwable e){}
			}
		}
	}

	@Test
	public void testDetectDecode()throws Exception{
		//String path="D:\\sinotn\\文档\\哈尔滨法院业务素质考试\\客户测试数据\\刑事类/刑事类.txt";
		//String path="D:\\test.txt";
		//String encode=EncodingDetect.getJavaEncode(path);
		System.out.println(Integer.MAX_VALUE);

	}

	@Test
	public void patternMatch(){
		Pattern p=Pattern.compile("^\\d+[\u3001\\.].*$");
		String text="10、法律是治国之重器，法律是善治之前提。(× )";
		Matcher matcher=p.matcher(text);

		System.out.println(matcher.matches());
	}

	@Test
	public void test2(){
		String text="ddd&nbsp;aaa";
		System.out.println(text.replaceAll("&nbsp;", " "));
		text="ddd<br/>aaa";
		String[] params=text.split("<br[/]{0,1}>");
		System.out.println(params.length);
	}

	@Test
	public void specialCharacter(){
		String text="　　A.应当建议检察院改变起诉罪名，不能直接以抢劫罪定罪";
		text=text.replaceAll("\\u3000", " ").trim();
		System.out.println(text);
	}

	@Test
	public void whitespace(){
		String text="\u3000\u00a0";
		text=text.replaceAll("[\\u3000\\u00a0]", "1");
		System.out.println("["+text+"]"+text.length());
	}

	@Test
	public void patternFind(){
		Pattern p=Pattern.compile("[\uff08\\(]\\s*[ABCD\u00d7\u221a\u5bf9\u9519]+\\s*[\uff09\\)]");
		String text="《人民法院司法警察条例》从（  B  ）起施行。";
		//\u3000中文全角空格
		text=text.replaceAll("\\u3000", " ");
		//不换行空格
		text=text.replaceAll("\u00a0", " ");
		//中文全角数字
		text=text.replaceAll("\\uff10", "0");
		text=text.replaceAll("\\uff11", "1");
		text=text.replaceAll("\\uff12", "2");
		text=text.replaceAll("\\uff13", "3");
		text=text.replaceAll("\\uff14", "4");
		text=text.replaceAll("\\uff15", "5");
		text=text.replaceAll("\\uff16", "6");
		text=text.replaceAll("\\uff17", "7");
		text=text.replaceAll("\\uff18", "8");
		text=text.replaceAll("\\uff19", "9");
		text=text.replaceAll("\\uff21", "A");
		text=text.replaceAll("\\uff22", "B");
		text=text.replaceAll("\\uff23", "C");
		text=text.replaceAll("\\uff24", "D");
		text=text.trim();
		Matcher matcher=p.matcher(text);

		String title="";
		HashSet<String> answerLetter=new HashSet<String>();
		String letterStr;
		if(matcher.find()){
			int end=matcher.end();
			int len=text.length();
			if(end==len){
				title=text.substring(0, matcher.start());
			}else{
				title=text.substring(0,matcher.start());
				title+="()";
				title+=text.substring(matcher.end());
			}
			letterStr=matcher.group();
			letterStr=letterStr.substring(1,letterStr.length()-1).trim();
			if("√对".indexOf(letterStr)!=-1){
				answerLetter.add("A");
			}else if("×错".indexOf(letterStr)!=-1){
				answerLetter.add("B");
			}else{
				for(int i=letterStr.length()-1;i>=0;i--){
					answerLetter.add(letterStr.substring(i, i+1));
				}
			}
		}
		System.out.println(title);
		System.out.println(answerLetter);
	}

	@Test
	public void parseAnswer(){
		String text="9、被害人因受到犯罪侵犯，提起附带民事诉讼要求赔偿精神损失的，人民法院不予受理；但如果其另行提起民事诉讼，要求赔偿精神损失的，人民法院可以支持。×";
		Pattern p=Pattern.compile("[ABCD\\u00d7\\u221a\\u5bf9\\u9519][ABCD\\s]*$");
		Matcher matcher=p.matcher(text);
		if(matcher.find()){
			System.out.println(matcher.group());
		}

	}

	@Test
	public void parseOption(){
		String text="A诉讼参与人数量      B庭审秩序      C现有警力      D场地座位";
		int letterCode=text.charAt(0);
		letterCode++;
		String nextLetter=String.valueOf((char)(letterCode));
		ArrayList<Integer> indexs=new ArrayList<Integer>(4);
		int index;
		int fromIndex=0;
		do{
			index=text.indexOf(nextLetter,fromIndex);
			if(index==-1){
				break;
			}else{
				indexs.add(index);
				fromIndex=index;
				letterCode++;
				nextLetter=String.valueOf((char)(letterCode));
			}
		}while(true);
		System.out.println(indexs);
		if(indexs.size()==0){
			System.out.println(text);
		}else{
			System.out.println(text.substring(0, indexs.get(0)));
			int endIndex;
			for(int i=0;i<indexs.size();i++){
				endIndex=i+1;
				if(endIndex<indexs.size()){
					System.out.println(text.substring(indexs.get(i), indexs.get(endIndex)));
				}else{
					System.out.println(text.substring(indexs.get(i)));
				}
			}
		}
	}

	@Test
	public void parseToSql(){
		InputStream is=null;
		BufferedReader br=null;
		PrintWriter pw=null;
		try{
			ArrayList<AdministrativeVO> list=new ArrayList<AdministrativeVO>();
			HashMap<String, AdministrativeVO> maps=new HashMap<String, AdministrativeVO>();
			ClassPathResource cpr=new ClassPathResource("/com/sinotn/demo/administrative/source.txt");
			is=cpr.getInputStream();
			br=new BufferedReader(new InputStreamReader(is));

			AdministrativeVO iRet;
			AdministrativeVO p;
			for(String line=br.readLine();line!=null;line=br.readLine()){
				line=line.trim();
				if(line.length()==0) continue;
				iRet=this.parseLine(line);
				if(iRet.getParentCode()==null){
					list.add(iRet);
					iRet.setDisplayNo(list.size());
				}else{
					p=maps.get(iRet.getParentCode());
					p.addChild(iRet);
				}
				maps.put(iRet.getCode(), iRet);
			}

			int index=1;
			for(int i=0;i<list.size();i++){
				iRet=list.get(i);
				index=this.fixTreeIndex(iRet, index);
			}

			pw=new PrintWriter(new File("D:/administrative.sql"));
			for(int i=0;i<list.size();i++){
				iRet=list.get(i);
				iRet.writeSql(pw);
			}
			pw.flush();
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			if(null!=pw){
				try{pw.close();}catch(Throwable e){}
			}
			if(null!=is){
				try{is.close();}catch(Throwable e){}
			}
			if(null!=br){
				try{br.close();}catch(Throwable e){}
			}
		}
	}

	private int fixTreeIndex(AdministrativeVO vo,int index){
		vo.setLft(index);
		int idx=index+1;
		List<AdministrativeVO> childs=vo.getChilds();
		if(childs!=null&&childs.size()>0){
			AdministrativeVO child;
			for(int i=0;i<childs.size();i++){
				child=childs.get(i);
				idx=this.fixTreeIndex(child, idx);
			}
		}
		vo.setRgt(idx);
		return idx+1;
	}

	private AdministrativeVO parseLine(String text){
		AdministrativeVO iRet=new AdministrativeVO();
		iRet.setId(SeqUUIDGenerator.genSequenceUUID());
		String value=text.substring(0, 6);
		iRet.setCode(value);//行政区划代码
		String str;
		for(int i=4;i>=0;i-=2){
			str=text.substring(i, i+2);
			if(!"00".equals(str)){
				if(i>0){
					StringBuilder p=new StringBuilder(6);
					p.append(value.substring(0, i));
					for(int j=0;j<(6-i);j++){
						p.append("0");
					}
					iRet.setParentCode(p.toString());
				}
				break;
			}
		}
		value=text.substring(6).trim();
		//\u3000中文全角空格
		value=value.replaceAll("\\u3000", "");
		iRet.setName(value);
		return iRet;
	}


	@Test
	public void ztreeData(){
		InputStream is=null;
		BufferedReader br=null;
		PrintWriter pw=null;
		try{
			ClassPathResource cpr=new ClassPathResource("/com/sinotn/demo/administrative/source.txt");
			is=cpr.getInputStream();
			br=new BufferedReader(new InputStreamReader(is));
			pw=new PrintWriter(new File("D:/ztree.txt"));
			HashMap<String, String> iRet;
			StringBuilder sb;
			for(String line=br.readLine();line!=null;line=br.readLine()){
				line=line.trim();
				if(line.length()==0) continue;
				iRet=this.parse(line);
				sb=new StringBuilder();
				sb.append("{");
				sb.append("\"id\":\"").append(iRet.get("code")).append("\"");
				sb.append(",\"pId\":\"").append(iRet.get("parent")).append("\"");
				sb.append(",\"name\":\"").append(iRet.get("name")).append("\"");
				sb.append("},");
				pw.println(sb.toString());
			}
			pw.flush();
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			if(null!=pw){
				try{pw.close();}catch(Throwable e){}
			}
			if(null!=is){
				try{is.close();}catch(Throwable e){}
			}
			if(null!=br){
				try{br.close();}catch(Throwable e){}
			}
		}
	}

	private HashMap<String, String> parse(String text){
		HashMap<String, String> iRet=new HashMap<String, String>();
		String value=text.substring(0, 6);
		iRet.put("code", value);
		String str;
		for(int i=4;i>=0;i-=2){
			str=text.substring(i, i+2);
			if(!"00".equals(str)){
				if(i==0){
					iRet.put("parent", "0");
				}else{
					StringBuilder p=new StringBuilder(6);
					p.append(value.substring(0, i));
					for(int j=0;j<(6-i);j++){
						p.append("0");
					}
					iRet.put("parent", p.toString());
				}
				break;
			}
		}
		value=text.substring(6).trim();
		//\u3000中文全角空格
		value=value.replaceAll("\\u3000", "");
		iRet.put("name", value);
		return iRet;
	}
}
