package com.workmanagement.model;

import java.io.Serializable;
import java.util.Date;

public class CreditReportQuery implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7576346548363955351L;
	private Integer creditReprotQueryId;
	private String creditCode;
	private String orgCode;
	private String enterpriseName;
	private String serviceCenterName;
	private String serviceCenterPhnoe;
	private String date;
	private String creditReportType;
	private Integer queryReason;
	private String authFile;
	private Date operateTime;
	private String operator;
	private Integer orgId;
	
	
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getCreditReprotQueryId() {
		return creditReprotQueryId;
	}
	public void setCreditReprotQueryId(Integer creditReprotQueryId) {
		this.creditReprotQueryId = creditReprotQueryId;
	}
	public String getCreditCode() {
		return creditCode;
	}
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getServiceCenterName() {
		return serviceCenterName;
	}
	public void setServiceCenterName(String serviceCenterName) {
		this.serviceCenterName = serviceCenterName;
	}
	public String getServiceCenterPhnoe() {
		return serviceCenterPhnoe;
	}
	public void setServiceCenterPhnoe(String serviceCenterPhnoe) {
		this.serviceCenterPhnoe = serviceCenterPhnoe;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCreditReportType() {
		return creditReportType;
	}
	public void setCreditReportType(String creditReportType) {
		this.creditReportType = creditReportType;
	}
	public Integer getQueryReason() {
		return queryReason;
	}
	public void setQueryReason(Integer queryReason) {
		this.queryReason = queryReason;
	}
	public String getAuthFile() {
		return authFile;
	}
	public void setAuthFile(String authFile) {
		this.authFile = authFile;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
}
