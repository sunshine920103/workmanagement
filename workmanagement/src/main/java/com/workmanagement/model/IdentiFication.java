package com.workmanagement.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author lzl
 *
 */
public class IdentiFication  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4250342741513118239L;
	
	
	private Integer sys_identification_id;  //id
	private String sys_identification_name;  //标识名
	private String sys_org_id;  //id
	private Integer sys_area_id;  //id
	private String sys_creat_username;  //id
	private Integer sys_creat_org;  //id
	private String sys_creat_time;  //id
	
	private List<SysOrg> subOrgs;
	private String sys_area_name;  //id
	public String getSys_area_name() {
		return sys_area_name;
	}
	public void setSys_area_name(String sys_area_name) {
		this.sys_area_name = sys_area_name;
	}
	public List<SysOrg> getSubOrgs() {
		return subOrgs;
	}
	public void setSubOrgs(List<SysOrg> subOrgs) {
		this.subOrgs = subOrgs;
	}
	public Integer getSys_identification_id() {
		return sys_identification_id;
	}
	public void setSys_identification_id(Integer sys_identification_id) {
		this.sys_identification_id = sys_identification_id;
	}
	public String getSys_identification_name() {
		return sys_identification_name;
	}
	public void setSys_identification_name(String sys_identification_name) {
		this.sys_identification_name = sys_identification_name;
	}
	public String getSys_org_id() {
		return sys_org_id;
	}
	public void setSys_org_id(String sys_org_id) {
		this.sys_org_id = sys_org_id;
	}
	public Integer getSys_area_id() {
		return sys_area_id;
	}
	public void setSys_area_id(Integer sys_area_id) {
		this.sys_area_id = sys_area_id;
	}
	public String getSys_creat_username() {
		return sys_creat_username;
	}
	public void setSys_creat_username(String sys_creat_username) {
		this.sys_creat_username = sys_creat_username;
	}
	public Integer getSys_creat_org() {
		return sys_creat_org;
	}
	public void setSys_creat_org(Integer sys_creat_org) {
		this.sys_creat_org = sys_creat_org;
	}
	public String getSys_creat_time() {
		return sys_creat_time;
	}
	public void setSys_creat_time(String sys_creat_time) {
		this.sys_creat_time = sys_creat_time;
	}
}
