package com.workmanagement.model;

import java.util.Date;

public class SysOperateListModel {
	//主键
	private Integer sysOperateListId;
	//外键
	private Integer sysOperateId;
	//修改时间
	private Date sysOperateTime;
	//被修改的指标项id
	private Integer indexItemId;
	//报数机构说明
	private String orgExplain;
	//服务中心说明
	private String serverExplain;
	//信息主体说明
	private String maininfoExplain;
	//是否选中
	private Integer mark;
	
	//对应指标项的结果集
	private String indexItemValue;
	public String getIndexItemValue() {
		return indexItemValue;
	}
	public void setIndexItemValue(String indexItemValue) {
		this.indexItemValue = indexItemValue;
	}
	public Integer getMark() {
		return mark;
	}
	public void setMark(Integer mark) {
		this.mark = mark;
	}
	public Integer getSysOperateListId() {
		return sysOperateListId;
	}
	public void setSysOperateListId(Integer sysOperateListId) {
		this.sysOperateListId = sysOperateListId;
	}
	public Integer getSysOperateId() {
		return sysOperateId;
	}
	public void setSysOperateId(Integer sysOperateId) {
		this.sysOperateId = sysOperateId;
	}
	public Date getSysOperateTime() {
		return sysOperateTime;
	}
	public void setSysOperateTime(Date sysOperateTime) {
		this.sysOperateTime = sysOperateTime;
	}
	public Integer getIndexItemId() {
		return indexItemId;
	}
	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}
	public String getOrgExplain() {
		return orgExplain;
	}
	public void setOrgExplain(String orgExplain) {
		this.orgExplain = orgExplain;
	}
	public String getServerExplain() {
		return serverExplain;
	}
	public void setServerExplain(String serverExplain) {
		this.serverExplain = serverExplain;
	}
	public String getMaininfoExplain() {
		return maininfoExplain;
	}
	public void setMaininfoExplain(String maininfoExplain) {
		this.maininfoExplain = maininfoExplain;
	}
	@Override
	public String toString() {
		return "SysOperateListModel [sysOperateListId=" + sysOperateListId
				+ ", sysOperateId=" + sysOperateId + ", sysOperateTime="
				+ sysOperateTime + ", indexItemId=" + indexItemId
				+ ", orgExplain=" + orgExplain + ", serverExplain="
				+ serverExplain + ", maininfoExplain=" + maininfoExplain + "]";
	}

	
}
