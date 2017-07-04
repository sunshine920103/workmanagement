package com.workmanagement.model;

import java.io.Serializable;
import java.util.List;

/**
 * 地区管理
 * 
 * @author wqs
 *
 */
public class SysArea implements Serializable {

	private static final long serialVersionUID = 4186860270818082034L;

	private Integer sysAreaId;
	private String sysAreaName;// 区域名称，不为空
	private String sysAreaCode;// 行政代码，不为空
	private String sysAreaType; // 区域类型，不为空： 0 省、直辖市 ， 1 地市 ， 2 区县 ， 3 乡镇
	private String sysAreaNotes;// 备注
	private Integer sysAreaUpid;// 上级区域ID，不填写则为一级区域
	private Boolean sysAreaUsed;// 是否使用 0 否 1 是（默认）
	private List<SysArea> subArea;
	private Integer pid; //上级区域的ID
	private String pcode; //上级区域的行政代码
	private String pname; //上级区域的名称
	private String ptype; //上级区域的区域类型
	private Integer dicId; //字典id
	
	
	
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public Integer getSysAreaId() {
		return sysAreaId;
	}
	public void setSysAreaId(Integer sysAreaId) {
		this.sysAreaId = sysAreaId;
	}
	public String getSysAreaName() {
		return sysAreaName;
	}
	public void setSysAreaName(String sysAreaName) {
		this.sysAreaName = sysAreaName;
	}
	public String getSysAreaCode() {
		return sysAreaCode;
	}
	public void setSysAreaCode(String sysAreaCode) {
		this.sysAreaCode = sysAreaCode;
	}
	public String getSysAreaType() {
		return sysAreaType;
	}
	public void setSysAreaType(String sysAreaType) {
		this.sysAreaType = sysAreaType;
	}
	public String getSysAreaNotes() {
		return sysAreaNotes;
	}
	public void setSysAreaNotes(String sysAreaNotes) {
		this.sysAreaNotes = sysAreaNotes;
	}
	public Integer getSysAreaUpid() {
		return sysAreaUpid;
	}
	public void setSysAreaUpid(Integer sysAreaUpid) {
		this.sysAreaUpid = sysAreaUpid;
	}
	public Boolean getSysAreaUsed() {
		return sysAreaUsed;
	}
	public void setSysAreaUsed(Boolean sysAreaUsed) {
		this.sysAreaUsed = sysAreaUsed;
	}
	public List<SysArea> getSubArea() {
		return subArea;
	}
	public void setSubArea(List<SysArea> subArea) {
		this.subArea = subArea;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	@Override
	public String toString() {
		String type =null; 
		if(sysAreaType.equals("0")){
			type="省、直辖市";
		}else if(this.sysAreaType.equals("1")){
			type="地市";
		}else if(this.sysAreaType.equals("2")){
			type="区县";
		}else {
			type="乡镇";
		}
		
		return "地区 [ 地区名字：" + sysAreaName + ", 地区代号：" + sysAreaCode
				+ ", 地区类型：" + type + ", 备注： " + sysAreaNotes + " ]";
	}
	public Integer getDicId() {
		return dicId;
	}
	public void setDicId(Integer dicId) {
		this.dicId = dicId;
	}
	
	
	
}
