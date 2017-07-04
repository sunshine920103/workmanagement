package com.workmanagement.model;

import java.io.Serializable;

/**
 * 其他管理
 * @author tianhao
 *
 */
public class SysOtherManage implements Serializable{

	private static final long serialVersionUID = -2328285995889800798L;
	
	private Integer sysSetId;
	private String sysSetStime;//工作日系统使用开始时间
	private String sysSetEtime;//工作日系统使用结束时间
	private Integer sysSetLoginOverdue;//超期登陆限制天数
	private Integer sysSetQueryLimitSwitch;//前5月信息查询平均次数限制开关：0关默认，1开
	private Integer sysSetLoginNum;//登陆错误次数限制上限
	private Integer sysSetQwdRule;//密码强度规则0数字，1字母，默认0
	private Integer sysSetOrgSwitch;//组织结构代码开关：0关，1开默认
	private Integer sysAreaId;//所属区域ID
	private Integer authFileSwitch;//手工修改授权文件开关：0关，1开默认
	private Integer operateAuthFileSwitch;//异议处理授权文件开关：0关，1开默认
	private Integer creditReportAuthFileSwitch;//信用报告授权文件开关：0关，1开默认
	private Integer monthLimit;//查询次数限制-月数
	private double multipleLimit;//查询次数限制-倍数
	public Integer getSysSetId() {
		return sysSetId;
	}
	public void setSysSetId(Integer sysSetId) {
		this.sysSetId = sysSetId;
	}
	public String getSysSetStime() {
		return sysSetStime;
	}
	public void setSysSetStime(String sysSetStime) {
		this.sysSetStime = sysSetStime;
	}
	public String getSysSetEtime() {
		return sysSetEtime;
	}
	public void setSysSetEtime(String sysSetEtime) {
		this.sysSetEtime = sysSetEtime;
	}
	public Integer getSysSetLoginOverdue() {
		return sysSetLoginOverdue;
	}
	public void setSysSetLoginOverdue(Integer sysSetLoginOverdue) {
		this.sysSetLoginOverdue = sysSetLoginOverdue;
	}
	public Integer getSysSetQueryLimitSwitch() {
		return sysSetQueryLimitSwitch;
	}
	public void setSysSetQueryLimitSwitch(Integer sysSetQueryLimitSwitch) {
		this.sysSetQueryLimitSwitch = sysSetQueryLimitSwitch;
	}
	public Integer getSysSetLoginNum() {
		return sysSetLoginNum;
	}
	public void setSysSetLoginNum(Integer sysSetLoginNum) {
		this.sysSetLoginNum = sysSetLoginNum;
	}
	public Integer getSysSetQwdRule() {
		return sysSetQwdRule;
	}
	public void setSysSetQwdRule(Integer sysSetQwdRule) {
		this.sysSetQwdRule = sysSetQwdRule;
	}
	public Integer getSysSetOrgSwitch() {
		return sysSetOrgSwitch;
	}
	public void setSysSetOrgSwitch(Integer sysSetOrgSwitch) {
		this.sysSetOrgSwitch = sysSetOrgSwitch;
	}
	public Integer getSysAreaId() {
		return sysAreaId;
	}
	public void setSysAreaId(Integer sysAreaId) {
		this.sysAreaId = sysAreaId;
	}
	public Integer getAuthFileSwitch() {
		return authFileSwitch;
	}
	public void setAuthFileSwitch(Integer authFileSwitch) {
		this.authFileSwitch = authFileSwitch;
	}
	public Integer getOperateAuthFileSwitch() {
		return operateAuthFileSwitch;
	}
	public void setOperateAuthFileSwitch(Integer operateAuthFileSwitch) {
		this.operateAuthFileSwitch = operateAuthFileSwitch;
	}
	public Integer getCreditReportAuthFileSwitch() {
		return creditReportAuthFileSwitch;
	}
	public void setCreditReportAuthFileSwitch(Integer creditReportAuthFileSwitch) {
		this.creditReportAuthFileSwitch = creditReportAuthFileSwitch;
	}
	public Integer getMonthLimit() {
		return monthLimit;
	}
	public void setMonthLimit(Integer monthLimit) {
		this.monthLimit = monthLimit;
	}
	public double getMultipleLimit() {
		return multipleLimit;
	}
	public void setMultipleLimit(double multipleLimit) {
		this.multipleLimit = multipleLimit;
	}
}
