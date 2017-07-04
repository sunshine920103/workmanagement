package com.workmanagement.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.workmanagement.service.IndexItemTbService;

/**
 * excel模板
 * @author tianhao
 *
 */
public class ReportExcelTemplate implements Serializable{
	private static final long serialVersionUID = 2364082042457842164L;
	
	@Autowired
	private IndexItemTbService indexItemTbService;
	private Integer reportExcelTemplateId;
	private String reportExcelTemplateName;//模板名称
	private Integer sysAreaId;//（外键）覆盖区域id
	private String reportExcelTemplateAreaName;//覆盖区域名称
	private Integer reportExcelTemplateStatus;//状态，0正常 1禁用
	private String reportExcelTemplatePath;//excel文件目录
	private String reportExcelTemplateIndexItemSet;//指标项集合
	private Integer sysOrgId;//所属机构Id
	private Integer indexId;//指标大类Id
	private Integer createUserId;
	private Date createTime;
	private String indexName;
	//指标项集合_集合形式
	private List<IndexItemTb> indexItemTbList;
	public Integer getReportExcelTemplateId() {
		return reportExcelTemplateId;
	}
	public void setReportExcelTemplateId(Integer reportExcelTemplateId) {
		this.reportExcelTemplateId = reportExcelTemplateId;
	}
	public String getReportExcelTemplateName() {
		return reportExcelTemplateName;
	}
	public void setReportExcelTemplateName(String reportExcelTemplateName) {
		this.reportExcelTemplateName = reportExcelTemplateName;
	}
	public Integer getSysAreaId() {
		return sysAreaId;
	}
	public void setSysAreaId(Integer sysAreaId) {
		this.sysAreaId = sysAreaId;
	}
	public String getReportExcelTemplateAreaName() {
		return reportExcelTemplateAreaName;
	}
	public void setReportExcelTemplateAreaName(String reportExcelTemplateAreaName) {
		this.reportExcelTemplateAreaName = reportExcelTemplateAreaName;
	}
	public Integer getReportExcelTemplateStatus() {
		return reportExcelTemplateStatus;
	}
	public void setReportExcelTemplateStatus(Integer reportExcelTemplateStatus) {
		this.reportExcelTemplateStatus = reportExcelTemplateStatus;
	}
	public String getReportExcelTemplatePath() {
		return reportExcelTemplatePath;
	}
	public void setReportExcelTemplatePath(String reportExcelTemplatePath) {
		this.reportExcelTemplatePath = reportExcelTemplatePath;
	}
	public String getReportExcelTemplateIndexItemSet() {
		return reportExcelTemplateIndexItemSet;
	}
	public void setReportExcelTemplateIndexItemSet(String reportExcelTemplateIndexItemSet) {
		this.reportExcelTemplateIndexItemSet = reportExcelTemplateIndexItemSet;
	}
	public List<IndexItemTb> getIndexItemTbList() {
		return indexItemTbList;
	}
	public void setIndexItemTbList(List<IndexItemTb> indexItemTbList) {
		this.indexItemTbList = indexItemTbList;
	}
	public Integer getSysOrgId() {
		return sysOrgId;
	}
	public void setSysOrgId(Integer sysOrgId) {
		this.sysOrgId = sysOrgId;
	}
	public Integer getIndexId() {
		return indexId;
	}
	public void setIndexId(Integer indexId) {
		this.indexId = indexId;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public IndexItemTbService getIndexItemTbService() {
		return indexItemTbService;
	}
	public void setIndexItemTbService(IndexItemTbService indexItemTbService) {
		this.indexItemTbService = indexItemTbService;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	@Override
	public String toString() {
		return "ReportExcelTemplate [reportExcelTemplateId=" + reportExcelTemplateId + ", reportExcelTemplateName="
				+ reportExcelTemplateName + ", sysAreaId=" + sysAreaId + ", reportExcelTemplateAreaName="
				+ reportExcelTemplateAreaName + ", reportExcelTemplateStatus=" + reportExcelTemplateStatus
				+ ", reportExcelTemplatePath=" + reportExcelTemplatePath + ", reportExcelTemplateIndexItemSet="
				+ reportExcelTemplateIndexItemSet + ", sysOrgId=" + sysOrgId + ", indexId=" + indexId
				+ ", indexItemTbList=" + indexItemTbList + "]";
	}
	
}