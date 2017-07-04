package com.workmanagement.model;

import java.io.File;
import java.util.Date;
import java.util.List;

public class AdminObjModel {
	//主键
	private Integer sysOperateId;
	//操作人id
	private Integer sysUserId;
	//操作机构id
	private Integer sysOrgId;
	//操作时间
	private Date sysOperateTime;
	//状态
	private Integer sysOperateStatus;
	//授权文件
	private String authFile;
	//被修改指标大类id
	private Integer indexItemId;
	//归档时间
	private Date recordDate;
	//二码id
	private Integer defaultIndexItemId;
	//异议列表
	private List<SysOperateListModel> sysOperateListModel;
	//指标大类名称
	private String indexName;
	//推送机构
	private String sysOrgName;
	//报送机构id
	private Integer reportOrgId;
	//企业名称
	private String jbxxQimc;
	//信用码
	private String codeCredit;
	//机构码
	private String codeOrg;
	//动态表id
	private Integer dataId;
	//录入机构
	private String sysReportOrgName;
	
	public Integer getDataId() {
		return dataId;
	}
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	public Integer getReportOrgId() {
		return reportOrgId;
	}
	public void setReportOrgId(Integer reportOrgId) {
		this.reportOrgId = reportOrgId;
	}
	public List<SysOperateListModel> getSysOperateListModel() {
		return sysOperateListModel;
	}
	public void setSysOperateListModel(List<SysOperateListModel> sysOperateListModel) {
		this.sysOperateListModel = sysOperateListModel;
	}
	public Integer getSysOperateId() {
		return sysOperateId;
	}
	public void setSysOperateId(Integer sysOperateId) {
		this.sysOperateId = sysOperateId;
	}
	public Integer getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}
	public Integer getSysOrgId() {
		return sysOrgId;
	}
	public void setSysOrgId(Integer sysOrgId) {
		this.sysOrgId = sysOrgId;
	}
	public Date getSysOperateTime() {
		return sysOperateTime;
	}
	public void setSysOperateTime(Date sysOperateTime) {
		this.sysOperateTime = sysOperateTime;
	}
	public Integer getSysOperateStatus() {
		return sysOperateStatus;
	}
	public void setSysOperateStatus(Integer sysOperateStatus) {
		this.sysOperateStatus = sysOperateStatus;
	}
	
	public String getAuthFile() {
		return authFile;
	}
	public void setAuthFile(String authFile) {
		this.authFile = authFile;
	}
	public Integer getIndexItemId() {
		return indexItemId;
	}
	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public Integer getDefaultIndexItemId() {
		return defaultIndexItemId;
	}
	public void setDefaultIndexItemId(Integer defaultIndexItemId) {
		this.defaultIndexItemId = defaultIndexItemId;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getSysOrgName() {
		return sysOrgName;
	}
	public void setSysOrgName(String sysOrgName) {
		this.sysOrgName = sysOrgName;
	}
	public String getJbxxQimc() {
		return jbxxQimc;
	}
	public void setJbxxQimc(String jbxxQimc) {
		this.jbxxQimc = jbxxQimc;
	}
	public String getCodeCredit() {
		return codeCredit;
	}
	public void setCodeCredit(String codeCredit) {
		this.codeCredit = codeCredit;
	}
	public String getCodeOrg() {
		return codeOrg;
	}
	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}
	public String getSysReportOrgName() {
		return sysReportOrgName;
	}
	public void setSysReportOrgName(String sysReportOrgName) {
		this.sysReportOrgName = sysReportOrgName;
	}
	@Override
	public String toString() {
		return "AdminObjModel [sysOperateId=" + sysOperateId + ", sysUserId="
				+ sysUserId + ", sysOrgId=" + sysOrgId + ", sysOperateTime="
				+ sysOperateTime + ", sysOperateStatus=" + sysOperateStatus
				+ ", authFile=" + authFile + ", indexItemId=" + indexItemId
				+ ", recordDate=" + recordDate + ", defaultIndexItemId="
				+ defaultIndexItemId + "]";
	}
	
	
}
