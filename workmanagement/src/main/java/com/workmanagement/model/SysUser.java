/**
 * 
 */
package com.workmanagement.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.workmanagement.service.SysUserService;
import com.workmanagement.util.DateFormatter;

/**
 * @author lzl
 * 
 */
public class SysUser implements Serializable{
	private static final long serialVersionUID = 5030803093853449081L;

	/**
	 * 用户的默认登录密码
	 */
	public static final String DEFUALT_PWD= "66666666";
	/**
	 * 用户的默认登录密码
	 */
	public static final Integer DEFUALT_ERR= 0;
	
	public Integer getSys_user_id() {
		return sys_user_id;
	}
	public void setSys_user_id(Integer sys_user_id) {
		this.sys_user_id = sys_user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSys_user_name() {
		return sys_user_name;
	}
	public void setSys_user_name(String sys_user_name) {
		this.sys_user_name = sys_user_name;
	}
	public String getSys_user_card() {
		return sys_user_card;
	}
	public void setSys_user_card(String sys_user_card) {
		this.sys_user_card = sys_user_card;
	}
	public String getSys_user_phone() {
		return sys_user_phone;
	}
	public void setSys_user_phone(String sys_user_phone) {
		this.sys_user_phone = sys_user_phone;
	}
	public String getSys_user_contacts() {
		return sys_user_contacts;
	}
	public void setSys_user_contacts(String sys_user_contacts) {
		this.sys_user_contacts = sys_user_contacts;
	}
	public Integer getSys_role_id() {
		return sys_role_id;
	}
	public void setSys_role_id(Integer sys_role_id) {
		this.sys_role_id = sys_role_id;
	}
	public Integer getSys_org_id() {
		return sys_org_id;
	}
	public void setSys_org_id(Integer sys_org_id) {
		this.sys_org_id = sys_org_id;
	}
	public String getSys_user_notes() {
		return sys_user_notes;
	}
	public void setSys_user_notes(String sys_user_notes) {
		this.sys_user_notes = sys_user_notes;
	}
	public Integer getSys_user_error() {
		return sys_user_error;
	}
	public void setSys_user_error(Integer sys_user_error) {
		this.sys_user_error = sys_user_error;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getSys_user_login_ip() {
		return sys_user_login_ip;
	}
	public void setSys_user_login_ip(String sys_user_login_ip) {
		this.sys_user_login_ip = sys_user_login_ip;
	}
	public Date getSys_user_login_time() {
		return sys_user_login_time;
	}
	public void setSys_user_login_time(Date sys_user_login_time) {
		this.sys_user_login_time = sys_user_login_time;
	}
	public String getSys_user_last_login_ip() {
		return sys_user_last_login_ip;
	}
	public void setSys_user_last_login_ip(String sys_user_last_login_ip) {
		this.sys_user_last_login_ip = sys_user_last_login_ip;
	}
	public Date getSys_user_last_login_time() {
		return sys_user_last_login_time;
	}
	public void setSys_user_last_login_time(Date sys_user_last_login_time) {
		this.sys_user_last_login_time = sys_user_last_login_time;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static String getDefualtPwd() {
		return DEFUALT_PWD;
	}
	public static Integer getDefualtErr() {
		return DEFUALT_ERR;
	}
	
	private Integer sys_user_id;
	private String username;
	private String password;
	private String sys_user_name;
	private Integer sys_delete;
	public Integer getSys_delete() {
		return sys_delete;
	}
	public void setSys_delete(Integer sys_delete) {
		this.sys_delete = sys_delete;
	}
	@Override
	public String toString() {
		return "用户信息 [登录名:" + username 
				+ ", 姓名:" + sys_user_name + ", 证件号:" + sys_user_card + ", 电话:"
				+ sys_user_phone + ", 联系方式:" + sys_user_contacts
				+ ", 备注:" + sys_user_notes + ", 密码错误次数:"
				+ sys_user_error + ", 是否禁用:" + enabled 
				+  ", 机构名称:" + sys_user_org_name
				+ ", 机构地址:" + sys_user_org_address + ", 角色名称:" + sys_user_role_name
				+ ", 角色职责:" + sys_user_role_duties +"]";
	}

	private String sys_user_card;
	private String sys_user_phone;
	private String sys_user_contacts;

	private Integer sys_role_id;//角色ID
	private Integer sys_create_user_id;//角色ID
	private Integer sys_user_query_times;//角色ID
	public Integer getSys_create_user_id() {
		return sys_create_user_id;
	}
	public void setSys_create_user_id(Integer sys_create_user_id) {
		this.sys_create_user_id = sys_create_user_id;
	}
	public Integer getSys_user_query_times() {
		return sys_user_query_times;
	}
	public void setSys_user_query_times(Integer sys_user_query_times) {
		this.sys_user_query_times = sys_user_query_times;
	}

	private Integer sys_org_id;
	private String sys_user_notes;
	private Integer sys_user_error;
	private boolean enabled;
	private String sys_user_login_ip;
	private Date sys_user_login_time;
	private String sys_user_last_login_ip;
	private Date sys_user_last_login_time;
	
	

	private String sys_user_org_name;//机构名称
	private String sys_user_org_address;//机构地址
	private String sys_user_role_name;//角色名称
	private String sys_user_role_duties;//角色职责

	
	
	private String delete;
	private String abl;

	private String endTime;
	private String beginTime;

	private String add;
	
	
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}

	@Autowired
	SysUserService sysUserService;
	
	public String getDelete() {
		if(sys_delete==0)
			delete ="停用";
		if(sys_delete==1)
			delete ="可用";
		return delete;
	}
	public void setDelete() {
	}
	public String getAbl() {

		if(isEnabled())
			abl ="启用";
		else
			abl ="禁用";
		return abl;
	}
	public void setAbl() {
	}
	
	public void setAdd() {
	}
	public String getEndTime() {

		  endTime = DateFormatter.dateToString(sys_user_last_login_time,null);
		return endTime;
	}
	public void setEndTime() {
	}
	public String getBeginTime() {

		  beginTime = DateFormatter.dateToString(sys_user_login_time,null);
		return beginTime;
	}
	public void setBeginTime() {
	}

	
	public String getSys_user_org_name() {
		return sys_user_org_name;
	}
	public void setSys_user_org_name(String sys_user_org_name) {
		this.sys_user_org_name = sys_user_org_name;
	}
	public String getSys_user_org_address() {
		return sys_user_org_address;
	}
	public void setSys_user_org_address(String sys_user_org_address) {
		this.sys_user_org_address = sys_user_org_address;
	}
	public String getSys_user_role_name() {
		return sys_user_role_name;
	}
	public void setSys_user_role_name(String sys_user_role_name) {
		this.sys_user_role_name = sys_user_role_name;
	}
	
	public String getSys_user_role_duties() {
		return sys_user_role_duties;
	}
	public void setSys_user_role_duties(String sys_user_role_duties) {
		this.sys_user_role_duties = sys_user_role_duties;
	}
	private SysOrg sysOrg;
	private SysRole sysRole;

	public SysOrg getSysOrg() {
		return sysOrg;
	}
	public void setSysOrg(SysOrg sysOrg) {
		this.sysOrg = sysOrg;
	}
	public SysRole getSysRole() {
		return sysRole;
	}
	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}
	
	
}
