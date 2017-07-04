/**
 * 
 */
package com.workmanagement.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lzl
 * 
 */
public class SysMenu implements Serializable{
	private static final long serialVersionUID = -4486950335511199243L;
	private Integer sys_menu_id;
	private String sys_menu_name;
	private String sys_menu_path;
	private Integer sys_menu_parent_id;
	private String sys_menu_icon;
	private Integer sys_menu_weight;
	private Integer sys_menu_type;
	public Integer getSys_menu_type() {
		return sys_menu_type;
	}
	public void setSys_menu_type(Integer sys_menu_type) {
		this.sys_menu_type = sys_menu_type;
	}
	private List<SysMenu> subMenus = new ArrayList<SysMenu>();
	private boolean checked = false;
	
	public Integer getSys_menu_id() {
		return sys_menu_id;
	}
	public void setSys_menu_id(Integer sys_menu_id) {
		this.sys_menu_id = sys_menu_id;
	}
	public String getSys_menu_name() {
		return sys_menu_name;
	}
	public void setSys_menu_name(String sys_menu_name) {
		this.sys_menu_name = sys_menu_name;
	}
	public String getSys_menu_path() {
		return sys_menu_path;
	}
	public void setSys_menu_path(String sys_menu_path) {
		this.sys_menu_path = sys_menu_path;
	}
	public Integer getSys_menu_parent_id() {
		return sys_menu_parent_id;
	}
	public void setSys_menu_parent_id(Integer sys_menu_parent_id) {
		this.sys_menu_parent_id = sys_menu_parent_id;
	}
	public String getSys_menu_icon() {
		return sys_menu_icon;
	}
	public void setSys_menu_icon(String sys_menu_icon) {
		this.sys_menu_icon = sys_menu_icon;
	}
	public Integer getSys_menu_weight() {
		return sys_menu_weight;
	}
	public void setSys_menu_weight(Integer sys_menu_weight) {
		this.sys_menu_weight = sys_menu_weight;
	}

	public List<SysMenu> getSubMenus() {
		return subMenus;
	}
	public void setSubMenus(List<SysMenu> subMenus) {
		this.subMenus = subMenus;
	}
	public boolean getChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	@Override
	public String toString() {
		return "SysMenu [sys_menu_id=" + sys_menu_id + ", sys_menu_name="
				+ sys_menu_name + ", sys_menu_path=" + sys_menu_path
				+ ", sys_menu_parent_id=" + sys_menu_parent_id
				+ ", sys_menu_icon=" + sys_menu_icon + ", sys_menu_weight="
				+ sys_menu_weight + ", subMenus=" + subMenus + ", checked="
				+ checked + "]";
	}

	
}
