package com.workmanagement.model;

import java.io.Serializable;
import java.util.List;

/**
 * 机构类别管理
 * @author renyang
 *
 */
public class SysOrgType implements Serializable{
	private static final long serialVersionUID = -7636324966317101783L;
	private Integer sys_org_type_id;//机构类型id
	private Integer sys_org_type_upid;//机构类型上级id
	private String sys_org_type_name;//机构类型名称
	private String sys_org_type_code;//组织机构类型代码
	private String sys_org_type_notes;//备注
	private Boolean sys_org_type_used;//是否使用 0 否 1 是（默认）
	private String sys_org_type_upname; //上级机构的名称
	private Integer sys_org_type_type;//0为机构 1为政府
	private List<SysOrgType> subSysOrgType;
	private List<SysOrg> subSysOrg;
	private List<SysOrgType> subSysOrgTypes;
	private String used;
	
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}
	public String getSys_org_type_upname() {
		return sys_org_type_upname;
	}
	public void setSys_org_type_upname(String sys_org_type_upname) {
		this.sys_org_type_upname = sys_org_type_upname;
	}
	public Integer getSys_org_type_id() {
		return sys_org_type_id;
	}
	public void setSys_org_type_id(Integer sys_org_type_id) {
		this.sys_org_type_id = sys_org_type_id;
	}
	public Integer getSys_org_type_upid() {
		return sys_org_type_upid;
	}
	public void setSys_org_type_upid(Integer sys_org_type_upid) {
		this.sys_org_type_upid = sys_org_type_upid;
	}
	public String getSys_org_type_name() {
		return sys_org_type_name;
	}
	public void setSys_org_type_name(String sys_org_type_name) {
		this.sys_org_type_name = sys_org_type_name;
	}
	public String getSys_org_type_code() {
		return sys_org_type_code;
	}
	public void setSys_org_type_code(String sys_org_type_code) {
		this.sys_org_type_code = sys_org_type_code;
	}
	public String getSys_org_type_notes() {
		return sys_org_type_notes;
	}
	public void setSys_org_type_notes(String sys_org_type_notes) {
		this.sys_org_type_notes = sys_org_type_notes;
	}
	public Boolean getSys_org_type_used() {
		return sys_org_type_used;
	}
	public void setSys_org_type_used(Boolean sys_org_type_used) {
		this.sys_org_type_used = sys_org_type_used;
	}
	public List<SysOrgType> getSubSysOrgType() {
		return subSysOrgType;
	}
	public void setSubSysOrgType(List<SysOrgType> subSysOrgType) {
		this.subSysOrgType = subSysOrgType;
	}
	public Integer getSys_org_type_type() {
		return sys_org_type_type;
	}
	public void setSys_org_type_type(Integer sys_org_type_type) {
		this.sys_org_type_type = sys_org_type_type;
	}
	public List<SysOrg> getSubSysOrg() {
		return subSysOrg;
	}
	public void setSubSysOrg(List<SysOrg> subSysOrg) {
		this.subSysOrg = subSysOrg;
	}
	public List<SysOrgType> getSubSysOrgTypes() {
		return subSysOrgTypes;
	}
	public void setSubSysOrgTypes(List<SysOrgType> subSysOrgTypes) {
		this.subSysOrgTypes = subSysOrgTypes;
	}
	
}
