package com.workmanagement.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SysRole implements Serializable {
	
	private static final long serialVersionUID = -6797643685935538458L;
	
	/**
	 * 人行管理员
	 */
	public static final Integer PEPOLE_ADMIN = 1;
	/**
	 * 人行查询员
	 */
	public static final Integer PEPOLE_QUERY = 2;
	/**
	 * 人行保送员
	 */
	public static final Integer PEPOLE_REPORT = 3;
	/**
	 * 商行管理员
	 */
	public static final Integer MER_ADMIN = 4;
	/**
	 * 商行查询员
	 */
	public static final Integer MER_QUERY = 5;
	/**
	 * 商行报送员
	 */
	public static final Integer MER_REPORT = 6;
	/**
	 * 人行异议处理员
	 */
	public static final Integer PEPLE_OBJ=7;
	/**
	 * 金融机构异议处理员
	 */
	public static final Integer MER_OBJ=8;
	/**
	 * 其他机构查询员
	 */
	public static final Integer MANAGE_QUERY=9;
	/**
	 * 其他机构报送员
	 */
	public static final Integer MANAGE_REPORT=10;
	
	private Integer sys_role_id;
	private String sys_role_name;
	private String sys_role_duties;
	private String sys_role_notes;
	private List<Integer> menuIds;
	private Integer sys_role_type;
	private Integer areaId;
	
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getSys_role_type() {
		return sys_role_type;
	}
	public void setSys_role_type(Integer sys_role_type) {
		this.sys_role_type = sys_role_type;
	}
	public Integer getSys_role_id() {
		return sys_role_id;
	}
	public void setSys_role_id(Integer sys_role_id) {
		this.sys_role_id = sys_role_id;
	}
	public String getSys_role_name() {
		return sys_role_name;
	}
	public void setSys_role_name(String sys_role_name) {
		this.sys_role_name = sys_role_name;
	}
	public String getSys_role_duties() {
		return sys_role_duties;
	}
	public void setSys_role_duties(String sys_role_duties) {
		this.sys_role_duties = sys_role_duties;
	}
	public String getSys_role_notes() {
		return sys_role_notes;
	}
	
	public List<Integer> getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(List<Integer> menuIds) {
		this.menuIds = menuIds;
	}
	public void setSys_role_notes(String sys_role_notes) {
		this.sys_role_notes = sys_role_notes;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static Integer getPepoleAdmin() {
		return PEPOLE_ADMIN;
	}
	public static Integer getPepoleQuery() {
		return PEPOLE_QUERY;
	}
	public static Integer getPepoleReport() {
		return PEPOLE_REPORT;
	}
	public static Integer getMerAdmin() {
		return MER_ADMIN;
	}
	public static Integer getMerQuery() {
		return MER_QUERY;
	}
	public static Integer getMerReport() {
		return MER_REPORT;
	}
	public static Integer getPepleObj() {
		return PEPLE_OBJ;
	}
	public static Integer getMerObj() {
		return MER_OBJ;
	}
	
	
	
}
