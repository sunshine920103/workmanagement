package com.workmanagement.model;

import java.io.Serializable;

/**
 * 
 * @author lzl
 * 企业标识
 */

public class ComPanyShow implements Serializable {
	
	private static final long serialVersionUID = 2360135839921423583L;
	
	
	
	
	private Integer sysComPanyShowId;
	private Integer defaultItemId;
	private String dicId;
	private String username;
	private String time;
	
	private String qymc;   //企业名称

	private String codeCredit;
	private String codeOrg;
	private Integer sysOrgId;
	private  Integer sysAreaId;
	private String recodeDate;
	private String index_jbxx_qyzs;
	private String index_ggxx_xm;
	private String index_jbxx_lxdh;
	
	
	private String biaoshi;

	public String getBiaoshi() {
		return biaoshi;
	}
	public void setBiaoshi(String biaoshi) {
		this.biaoshi = biaoshi;
	}
	public String getIndex_jbxx_qyzs() {
		return index_jbxx_qyzs;
	}
	public void setIndex_jbxx_qyzs(String index_jbxx_qyzs) {
		this.index_jbxx_qyzs = index_jbxx_qyzs;
	}
	public String getIndex_ggxx_xm() {
		return index_ggxx_xm;
	}
	public void setIndex_ggxx_xm(String index_ggxx_xm) {
		this.index_ggxx_xm = index_ggxx_xm;
	}
	public String getIndex_jbxx_lxdh() {
		return index_jbxx_lxdh;
	}
	public void setIndex_jbxx_lxdh(String index_jbxx_lxdh) {
		this.index_jbxx_lxdh = index_jbxx_lxdh;
	}
	public Integer getSysAreaId() {
		return sysAreaId;
	}
	public void setSysAreaId(Integer sysAreaId) {
		this.sysAreaId = sysAreaId;
	}
	public String getRecodeDate() {
		return recodeDate;
	}
	public void setRecodeDate(String recodeDate) {
		this.recodeDate = recodeDate;
	}
	public Integer getSysOrgId() {
		return sysOrgId;
	}
	public void setSysOrgId(Integer sysOrgId) {
		this.sysOrgId = sysOrgId;
	}
	private Integer sys_area_id; 
	
	public String getQymc() {
		return qymc;
	}
	public void setQymc(String qymc) {
		this.qymc = qymc;
	}
	public String getCodeCredit() {
		return codeCredit;
	}
	public void setCodeCredit(String codeCredit) {
		this.codeCredit = codeCredit;
	}
	public String getCodeOrg() {
		return codeOrg;
	}
	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}
	public Integer getSys_area_id() {
		return sys_area_id;
	}
	public void setSys_area_id(Integer sys_area_id) {
		this.sys_area_id = sys_area_id;
	}
	public Integer getSysComPanyShowId() {
		return sysComPanyShowId;
	}
	public void setSysComPanyShowId(Integer sysComPanyShowId) {
		this.sysComPanyShowId = sysComPanyShowId;
	}
	public Integer getDefaultItemId() {
		return defaultItemId;
	}
	public void setDefaultItemId(Integer defaultItemId) {
		this.defaultItemId = defaultItemId;
	}
	public String getDicId() {
		return dicId;
	}
	public void setDicId(String dicId) {
		this.dicId = dicId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
