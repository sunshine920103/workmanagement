package com.workmanagement.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 
 * @author wqs
 *		指标：对应数据库表
 */
public class IndexTb {
	private Integer indexId;
	private String indexName; //指标大类名称
	private String indexCode;  //数据库表名
	private Integer sysAreaId;  //（外键）地区id
	private Integer indexNumber;  //序号
	private String indexNotes;  //描述
	private Integer indexUsed;  //是否启用，0否1（默认）是
	private String indexCreateTime;  //创建时间
	private Integer sys_user_id;  //（外键）创建者id
	private Integer sys_org_id;  //（外键）创建机构id
	private Integer indexType; //指标类型 0：基本信息 1：业务信息
	private String  sysAreaName; //地区名字
	
//	private  List<IndexItemTb> indexItemTbs;  //指标项集合
//	
//	public List<IndexItemTb> getIndexItemTbs() {
//		return indexItemTbs;
//	}
//	public void setIndexItemTbs(List<IndexItemTb> indexItemsList) {
//		this.indexItemTbs = indexItemsList;
//	}
	public Integer getIndexId() {
		return indexId;
	}
	public void setIndexId(Integer indexId) {
		this.indexId = indexId;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getIndexCode() {
		return indexCode;
	}
	public void setIndexCode(String indexCode) {
		this.indexCode = indexCode;
	}
	public Integer getSysAreaId() {
		return sysAreaId;
	}
	public void setSysAreaId(Integer sysAreaId) {
		this.sysAreaId = sysAreaId;
	}
	public Integer getIndexNumber() {
		return indexNumber;
	}
	public void setIndexNumber(Integer indexNumber) {
		this.indexNumber = indexNumber;
	}
	public String getIndexNotes() {
		return indexNotes;
	}
	public void setIndexNotes(String indexNotes) {
		this.indexNotes = indexNotes;
	}
	public Integer getIndexUsed() {
		return indexUsed;
	}
	public void setIndexUsed(Integer indexUsed) {
		this.indexUsed = indexUsed;
	}
	public String getIndexCreateTime() {
		return indexCreateTime;
	}
	public void setIndexCreateTime(String indexCreateTime) {
		this.indexCreateTime = indexCreateTime;
	}
	public Integer getSys_user_id() {
		return sys_user_id;
	}
	public void setSys_user_id(Integer sys_user_id) {
		this.sys_user_id = sys_user_id;
	}
	public Integer getSys_org_id() {
		return sys_org_id;
	}
	public void setSys_org_id(Integer sys_org_id) {
		this.sys_org_id = sys_org_id;
	}
	public Integer getIndexType() {
		return indexType;
	}
	public void setIndexType(Integer indexType) {
		this.indexType = indexType;
	}
	public String getSysAreaName() {
		return sysAreaName;
	}
	public void setSysAreaName(String sysAreaName) {
		this.sysAreaName = sysAreaName;
	}
	@Override
	public String toString() {
		String type ="基本信息";
		if(indexType !=0){
			type ="业务信息";
		}
		String use = "";
		if(indexUsed ==0){
			use ="禁用";
		}else{
			use ="启用";
		}
		return "指标 [指标名：" + indexName + ", 指标代号：" + indexCode + ", 指标序号：" + indexNumber + ", 备注：" + indexNotes + ", 是否启用：" + use
				+ ", 创建时间：" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ", 指标类型：" + type+ " ]";
	}
	
	
	
}
