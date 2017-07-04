package com.workmanagement.model;

import java.io.Serializable;

/**
 * 
 * 行业维护
 * @author xiehao
 *
 */
public class SysClassFyModel implements Serializable {
	//主键
	private Integer sysIndustryId;
	//行业编码
	private String sysIndustryCode;
	//行业名称
	private String sysIndustryName;
	//备注
	private String sysIndustryNotes;
	//数据字典id（外键）
	private Integer sysDicId;
	//行业父类名字
	private String parentName;
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Integer getSysIndustryId() {
		return sysIndustryId;
	}
	public void setSysIndustryId(Integer sysIndustryId) {
		this.sysIndustryId = sysIndustryId;
	}
	public String getSysIndustryCode() {
		return sysIndustryCode;
	}
	public void setSysIndustryCode(String sysIndustryCode) {
		this.sysIndustryCode = sysIndustryCode;
	}
	public String getSysIndustryName() {
		return sysIndustryName;
	}
	public void setSysIndustryName(String sysIndustryName) {
		this.sysIndustryName = sysIndustryName;
	}
	public String getSysIndustryNotes() {
		return sysIndustryNotes;
	}
	public void setSysIndustryNotes(String sysIndustryNotes) {
		this.sysIndustryNotes = sysIndustryNotes;
	}
	public Integer getSysDicId() {
		return sysDicId;
	}
	public void setSysDicId(Integer sysDicId) {
		this.sysDicId = sysDicId;
	}
	@Override
	public String toString() {
		return "SysClassFyModel [sysIndustryId=" + sysIndustryId
				+ ", sysIndustryCode=" + sysIndustryCode + ", sysIndustryName="
				+ sysIndustryName + ", sysIndustryNotes=" + sysIndustryNotes
				+ ", sysDicId=" + sysDicId + "]";
	}
	
}
