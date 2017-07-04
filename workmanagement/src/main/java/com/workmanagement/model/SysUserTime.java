package com.workmanagement.model;

import java.io.Serializable;
import java.util.Date;

public class SysUserTime implements Serializable{
	private static final long serialVersionUID = -4133513732800463761L;
	private Integer SYS_USER_TIME_ID;
	private Integer SYS_USER_ID;
	private Date SYS_USER_TIME_BEGIN;
	private String SYS_USER_TIME_IP;
	private String SYS_USER_TIME_MENU;
	public Integer getSYS_USER_TIME_ID() {
		return SYS_USER_TIME_ID;
	}
	public void setSYS_USER_TIME_ID(Integer sYS_USER_TIME_ID) {
		SYS_USER_TIME_ID = sYS_USER_TIME_ID;
	}
	public Integer getSYS_USER_ID() {
		return SYS_USER_ID;
	}
	public void setSYS_USER_ID(Integer sYS_USER_ID) {
		SYS_USER_ID = sYS_USER_ID;
	}
	public Date getSYS_USER_TIME_BEGIN() {
		return SYS_USER_TIME_BEGIN;
	}
	public void setSYS_USER_TIME_BEGIN(Date sYS_USER_TIME_BEGIN) {
		SYS_USER_TIME_BEGIN = sYS_USER_TIME_BEGIN;
	}
	public String getSYS_USER_TIME_IP() {
		return SYS_USER_TIME_IP;
	}
	public void setSYS_USER_TIME_IP(String sYS_USER_TIME_IP) {
		SYS_USER_TIME_IP = sYS_USER_TIME_IP;
	}
	public String getSYS_USER_TIME_MENU() {
		return SYS_USER_TIME_MENU;
	}
	public void setSYS_USER_TIME_MENU(String sYS_USER_TIME_MENU) {
		SYS_USER_TIME_MENU = sYS_USER_TIME_MENU;
	}
	}
