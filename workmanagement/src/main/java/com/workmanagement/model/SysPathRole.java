package com.workmanagement.model;

import java.io.Serializable;

public class SysPathRole implements Serializable{
	private static final long serialVersionUID = 2901830634931306666L;
	private String sys_menu_path;
	private Integer sys_role_id;
	
	public String getSys_menu_path() {
		return sys_menu_path;
	}
	public void setSys_menu_path(String sys_menu_path) {
		this.sys_menu_path = sys_menu_path;
	}
	public Integer getSys_role_id() {
		return sys_role_id;
	}
	public void setSys_role_id(Integer sys_role_id) {
		this.sys_role_id = sys_role_id;
	}
}
