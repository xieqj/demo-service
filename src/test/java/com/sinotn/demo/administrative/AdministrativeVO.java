package com.sinotn.demo.administrative;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AdministrativeVO {
	private String id;
	private String code;
	private String parentCode;
	private String name;
	private String pid="";
	private int rgt;
	private int lft;
	private int displayNo;

	private List<AdministrativeVO> childs;

	public AdministrativeVO(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getRgt() {
		return rgt;
	}

	public void setRgt(int rgt) {
		this.rgt = rgt;
	}

	public int getLft() {
		return lft;
	}

	public void setLft(int lft) {
		this.lft = lft;
	}

	public int getDisplayNo() {
		return displayNo;
	}

	public void setDisplayNo(int displayNo) {
		this.displayNo = displayNo;
	}

	public List<AdministrativeVO> getChilds() {
		return childs;
	}

	public void addChild(AdministrativeVO child) {
		if(this.childs==null){
			this.childs=new ArrayList<AdministrativeVO>();
		}
		child.setPid(this.id);
		this.childs.add(child);
		child.setDisplayNo(this.childs.size());
	}

	public void writeSql(PrintWriter pw) {
		StringBuilder sb=new StringBuilder();
		sb.append("INSERT INTO mt_administrative (id,DISPLAY_NO,rgt,lft,`code`,`name`,PARENT_ID) VALUES (");
		sb.append("'").append(this.id).append("'");
		sb.append(",").append(this.displayNo);
		sb.append(",").append(this.rgt);
		sb.append(",").append(this.lft);
		sb.append(",'").append(this.code).append("'");
		sb.append(",'").append(this.name).append("'");
		sb.append(",'").append(this.pid).append("'");
		sb.append(");");
		pw.println(sb.toString());
		if(this.childs!=null&&this.childs.size()>0){
			for(int i=0;i<childs.size();i++){
				this.childs.get(i).writeSql(pw);
			}
		}
	}
}
