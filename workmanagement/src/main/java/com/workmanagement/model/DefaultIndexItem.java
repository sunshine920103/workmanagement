package com.workmanagement.model;

import java.io.Serializable;

/**
 * 二码对应表
 * @author lzl
 *
 */
public class DefaultIndexItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4715205856109507172L;
	
	
	private Integer defaultIndexItemId;
	private String codeCredit;
	private String codeOrg;
	
	private Integer defaultIndexItemOldId;
	private String defaultIndexItemTime;
	private Integer sys_area_id; 
	private Integer combine_status;//合并标识 
	private String qymc;   //企业名称 
	private Integer orgId;   //企业名称
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getSys_area_id() {
		return sys_area_id;
	}
	public void setSys_area_id(Integer sys_area_id) {
		this.sys_area_id = sys_area_id;
	}
	public Integer getCombine_status() {
		return combine_status;
	}
	public void setCombine_status(Integer combine_status) {
		this.combine_status = combine_status;
	}

	
	public String getQymc() {
		return qymc;
	}
	public void setQymc(String qymc) {
		this.qymc = qymc;
	}
	public Integer getDefaultIndexItemId() {
		return defaultIndexItemId;
	}
	public void setDefaultIndexItemId(Integer defaultIndexItemId) {
		this.defaultIndexItemId = defaultIndexItemId;
	}
	public String getCodeCredit() {
		return codeCredit;
	}
	public void setCodeCredit(String codeCredit) {
		this.codeCredit = codeCredit;
	}
	@Override
	public String toString() {
		return "企业二码 [统一社会码:" + codeCredit
				+ ", 组织机构代码:" + codeOrg+ "]";
	}
	public String getCodeOrg() {
		return codeOrg;
	}
	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}
	public Integer getDefaultIndexItemOldId() {
		return defaultIndexItemOldId;
	}
	public void setDefaultIndexItemOldId(Integer defaultIndexItemOldId) {
		this.defaultIndexItemOldId = defaultIndexItemOldId;
	}
	public String getDefaultIndexItemTime() {
		return defaultIndexItemTime;
	}
	public void setDefaultIndexItemTime(String string) {
		this.defaultIndexItemTime = string;
	}
	
	public DefaultIndexItem(String codeCredit, String codeOrg) {
		super();
		this.codeCredit = codeCredit;
		this.codeOrg = codeOrg;
	}
	
	public DefaultIndexItem(){}
}
