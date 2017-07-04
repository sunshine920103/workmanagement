package com.workmanagement.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 任务列表
 * @author renyang
 *
 */
public class ReportTaskPushList implements Serializable{
	private static final long serialVersionUID = 2810344220855674389L;
	
	private Integer reportTaskPushListId;
	private Integer reportTaskPushSetId;//任务记录管理表id
	private Integer sysOrgId;//执行机构id
	private Integer reportTaskPushStatus;//完成状态 0 未完成  1已完成  2逾期
	private Date reportTaskPushListEndTime;//结束时间
	
	private String reportTaskPushSetName;//任务名称
	private Integer reportTaskPushSetType;//报送类型
	private String sysOrgName;//机构名称
	public ReportTaskPushList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getReportTaskPushListId() {
		return reportTaskPushListId;
	}
	public void setReportTaskPushListId(Integer reportTaskPushListId) {
		this.reportTaskPushListId = reportTaskPushListId;
	}
	public Integer getReportTaskPushSetId() {
		return reportTaskPushSetId;
	}
	public void setReportTaskPushSetId(Integer reportTaskPushSetId) {
		this.reportTaskPushSetId = reportTaskPushSetId;
	}
	public Integer getSysOrgId() {
		return sysOrgId;
	}
	public void setSysOrgId(Integer sysOrgId) {
		this.sysOrgId = sysOrgId;
	}
	public Integer getReportTaskPushStatus() {
		return reportTaskPushStatus;
	}
	public void setReportTaskPushStatus(Integer reportTaskPushStatus) {
		this.reportTaskPushStatus = reportTaskPushStatus;
	}
	public String getReportTaskPushSetName() {
		return reportTaskPushSetName;
	}
	public void setReportTaskPushSetName(String reportTaskPushSetName) {
		this.reportTaskPushSetName = reportTaskPushSetName;
	}
	
	public Integer getReportTaskPushSetType() {
		return reportTaskPushSetType;
	}
	public void setReportTaskPushSetType(Integer reportTaskPushSetType) {
		this.reportTaskPushSetType = reportTaskPushSetType;
	}
	public String getSysOrgName() {
		return sysOrgName;
	}
	public void setSysOrgName(String sysOrgName) {
		this.sysOrgName = sysOrgName;
	}
	public Date getReportTaskPushListEndTime() {
		return reportTaskPushListEndTime;
	}
	public void setReportTaskPushListEndTime(Date reportTaskPushListEndTime) {
		this.reportTaskPushListEndTime = reportTaskPushListEndTime;
	}
	
}
