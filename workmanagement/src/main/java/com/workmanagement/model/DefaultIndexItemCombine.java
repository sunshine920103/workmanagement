package com.workmanagement.model;

import java.io.Serializable;
import java.util.Date;

public class DefaultIndexItemCombine implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8261482540733020852L;
	private Integer default_index_item_combine_id;
	private Integer code_credit_id;
	private Integer code_org_id;
	public Integer getDefault_index_item_combine_id() {
		return default_index_item_combine_id;
	}
	public void setDefault_index_item_combine_id(Integer default_index_item_combine_id) {
		this.default_index_item_combine_id = default_index_item_combine_id;
	}
	public Integer getCode_credit_id() {
		return code_credit_id;
	}
	public void setCode_credit_id(Integer code_credit_id) {
		this.code_credit_id = code_credit_id;
	}
	public Integer getCode_org_id() {
		return code_org_id;
	}
	public void setCode_org_id(Integer code_org_id) {
		this.code_org_id = code_org_id;
	}
	public Integer getCode_credit_orgid() {
		return code_credit_orgid;
	}
	public void setCode_credit_orgid(Integer code_credit_orgid) {
		this.code_credit_orgid = code_credit_orgid;
	}
	public Integer getCode_org_orgid() {
		return code_org_orgid;
	}
	public void setCode_org_orgid(Integer code_org_orgid) {
		this.code_org_orgid = code_org_orgid;
	}
	public Integer getStuta() {
		return stuta;
	}
	public void setStuta(Integer stuta) {
		this.stuta = stuta;
	}
	private Integer code_credit_orgid;
	private Integer code_org_orgid;
	private Integer stuta;
	private Date sub_time;
	private Integer org_stuta;

	public Integer getOrg_stuta() {
		return org_stuta;
	}
	public void setOrg_stuta(Integer org_stuta) {
		this.org_stuta = org_stuta;
	}
	public Date getSub_time() {
		return sub_time;
	}
	public void setSub_time(Date sub_time) {
		this.sub_time = sub_time;
	}
}
