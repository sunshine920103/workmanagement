package com.workmanagement.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmanagement.service.SysOrgService;

/**
 * 任务推送
 * @author renyang 
 *
 */
@Service
public class ReportTaskPushSet implements Serializable{
	private static final long serialVersionUID = -7006113435242703628L;
	@Autowired
	SysOrgService sysOrgService;
	
	private Integer reportTaskPushSetId;
	//任务名称
	private String reportTaskPushSetName;
	//任务周期
	private String reportTaskPushSetCycle;
	//创建时间
	private Date reportTaskPushSetTime;
	//报送类型0报文 1 excel模版
	private Integer reportTaskPushSetType;
	//结束时间
	private Date reportTaskPushSetEndTime;
	//模板id
	private Integer reportTaskPushSetTempLateId;
	//状态，0有效，1失效
	private Integer reportTaskPushSetStatus;
	//创建者机构id
	private Integer sysOrgCreateId;
	//执行机构ids
	private String sysOrgExcuteIds;
	
	//模板名称
	private String reportTaskPushSetTemplate;
	//创建机构名称
	private String reportTaskPushSetOrgName;
	//任务负责机构列表_集合形式
	private List<SysOrg> sysOrgList;
	public ReportTaskPushSet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<SysOrg> getSysOrg() {
		sysOrgList=new ArrayList<>();
		if (this.sysOrgExcuteIds!=null) {
			String[] array=this.sysOrgExcuteIds.split(",");
			for (String id : array) {
				SysOrg sysOrg = sysOrgService.queryInstitutionsById(Integer.parseInt(id.trim()));
				sysOrgList.add(sysOrg);
			}
		}
		return sysOrgList;
	}
	
	public Integer getReportTaskPushSetStatus() {
		return reportTaskPushSetStatus;
	}
	public void setReportTaskPushSetStatus(Integer reportTaskPushSetStatus) {
		this.reportTaskPushSetStatus = reportTaskPushSetStatus;
	}
	public Integer getReportTaskPushSetId() {
		return reportTaskPushSetId;
	}
	public void setReportTaskPushSetId(Integer reportTaskPushSetId) {
		this.reportTaskPushSetId = reportTaskPushSetId;
	}
	public String getReportTaskPushSetName() {
		return reportTaskPushSetName;
	}
	public void setReportTaskPushSetName(String reportTaskPushSetName) {
		this.reportTaskPushSetName = reportTaskPushSetName;
	}
	public String getReportTaskPushSetCycle() {
		return reportTaskPushSetCycle;
	}
	public void setReportTaskPushSetCycle(String reportTaskPushSetCycle) {
		this.reportTaskPushSetCycle = reportTaskPushSetCycle;
	}
	public Date getReportTaskPushSetTime() {
		return reportTaskPushSetTime;
	}
	public void setReportTaskPushSetTime(Date reportTaskPushSetTime) {
		this.reportTaskPushSetTime = reportTaskPushSetTime;
	}
	public Integer getReportTaskPushSetType() {
		return reportTaskPushSetType;
	}
	public void setReportTaskPushSetType(Integer reportTaskPushSetType) {
		this.reportTaskPushSetType = reportTaskPushSetType;
	}
	
	public Date getReportTaskPushSetEndTime() {
		return reportTaskPushSetEndTime;
	}
	public void setReportTaskPushSetEndTime(Date reportTaskPushSetEndTime) {
		this.reportTaskPushSetEndTime = reportTaskPushSetEndTime;
	}
	public Integer getReportTaskPushSetTempLateId() {
		return reportTaskPushSetTempLateId;
	}
	public void setReportTaskPushSetTempLateId(Integer reportTaskPushSetTempLateId) {
		this.reportTaskPushSetTempLateId = reportTaskPushSetTempLateId;
	}
	public Integer getSysOrgCreateId() {
		return sysOrgCreateId;
	}
	public void setSysOrgCreateId(Integer sysOrgCreateId) {
		this.sysOrgCreateId = sysOrgCreateId;
	}
	public String getSysOrgExcuteIds() {
		return sysOrgExcuteIds;
	}
	public void setSysOrgExcuteIds(String sysOrgExcuteIds) {
		this.sysOrgExcuteIds = sysOrgExcuteIds;
	}
	public List<SysOrg> getSysOrgList() {
		return sysOrgList;
	}
	public void setSysOrgList(List<SysOrg> sysOrgList) {
		this.sysOrgList = sysOrgList;
	}
	public SysOrgService getSysOrgService() {
		return sysOrgService;
	}
	public void setSysOrgService(SysOrgService sysOrgService) {
		this.sysOrgService = sysOrgService;
	}
	public String getReportTaskPushSetTemplate() {
		return reportTaskPushSetTemplate;
	}
	public void setReportTaskPushSetTemplate(String reportTaskPushSetTemplate) {
		this.reportTaskPushSetTemplate = reportTaskPushSetTemplate;
	}
	public String getReportTaskPushSetOrgName() {
		return reportTaskPushSetOrgName;
	}
	public void setReportTaskPushSetOrgName(String reportTaskPushSetOrgName) {
		this.reportTaskPushSetOrgName = reportTaskPushSetOrgName;
	}
	
}
