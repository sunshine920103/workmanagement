package com.workmanagement.model;

import java.util.Date;
import java.util.List;

public class SysImportentEnterpriseGroup {
	private Integer sys_importent_enterprise_group_id; //id
	private String sys_importent_enterprise_group_name;//重点企业群名
	private Integer sys_user_id; //创建用户
	private Date submit_time; //创建时间
	private Integer sys_org_id;//所在机构 id、
	
	private String username;   //用户名称
	private String sys_org_name;//机构名称
	
	private List<DefaultIndexItem> default_index_item_id; 
	public List<DefaultIndexItem> getDefault_index_item_id() {
		return default_index_item_id;
	}
	public void setDefault_index_item_id(List<DefaultIndexItem> default_index_item_id) {
		this.default_index_item_id = default_index_item_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getSys_importent_enterprise_group_id() {
		return sys_importent_enterprise_group_id;
	}
	public void setSys_importent_enterprise_group_id(Integer sys_importent_enterprise_group_id) {
		this.sys_importent_enterprise_group_id = sys_importent_enterprise_group_id;
	}
	public String getSys_importent_enterprise_group_name() {
		return sys_importent_enterprise_group_name;
	}
	public void setSys_importent_enterprise_group_name(String sys_importent_enterprise_group_name) {
		this.sys_importent_enterprise_group_name = sys_importent_enterprise_group_name;
	}
	public Integer getSys_user_id() {
		return sys_user_id;
	}
	public void setSys_user_id(Integer sys_user_id) {
		this.sys_user_id = sys_user_id;
	}
	public Date getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(Date submit_time) {
		this.submit_time = submit_time;
	}
	public Integer getSys_org_id() {
		return sys_org_id;
	}
	public void setSys_org_id(Integer sys_org_id) {
		this.sys_org_id = sys_org_id;
	}
	public String getSys_org_name() {
		return sys_org_name;
	}
	public void setSys_org_name(String sys_org_name) {
		this.sys_org_name = sys_org_name;
	}
	
	
}
