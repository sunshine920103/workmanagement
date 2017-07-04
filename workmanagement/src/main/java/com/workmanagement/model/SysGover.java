package com.workmanagement.model;

import java.io.Serializable;
/**
 * 政府部门管理
 * @author tianhao
 *
 */
public class SysGover implements Serializable{

	private static final long serialVersionUID = 7360023737214169768L;
	
	private Integer sysGovId;
	private String sysGovFinancialCode;//政府部门编码，不为空
	private String sysGovName;//政府部门名称，不为空
	private Integer sysGovUpid;//上级政府部门ID
	private Integer sysGovTypeId;//政府类型ID
	private Integer sysDicId;//（外键）数据字典id
	private String sysOrgNotes;//备注
	private String sysGovUpname;//上级政府部门名称
	private String sysGovUpcode;//上级政府部门编码
	private Integer sysAreaId;//（外键）所属区域id
	private String sysGovTypeName;//政府类型名称
	private String sysGovTypeCode;//政府类型编码
	private String sysDicName;//数据字典名称
	private String sysAreaName;//所属区域名称
	public Integer getSysGovId() {
		return sysGovId;
	}
	public void setSysGovId(Integer sysGovId) {
		this.sysGovId = sysGovId;
	}
	public String getSysGovFinancialCode() {
		return sysGovFinancialCode;
	}
	public void setSysGovFinancialCode(String sysGovFinancialCode) {
		this.sysGovFinancialCode = sysGovFinancialCode;
	}
	public String getSysGovName() {
		return sysGovName;
	}
	public void setSysGovName(String sysGovName) {
		this.sysGovName = sysGovName;
	}
	public Integer getSysGovUpid() {
		return sysGovUpid;
	}
	public void setSysGovUpid(Integer sysGovUpid) {
		this.sysGovUpid = sysGovUpid;
	}
	public Integer getSysGovTypeId() {
		return sysGovTypeId;
	}
	public void setSysGovTypeId(Integer sysGovTypeId) {
		this.sysGovTypeId = sysGovTypeId;
	}
	public Integer getSysDicId() {
		return sysDicId;
	}
	public void setSysDicId(Integer sysDicId) {
		this.sysDicId = sysDicId;
	}
	public String getSysOrgNotes() {
		return sysOrgNotes;
	}
	public void setSysOrgNotes(String sysOrgNotes) {
		this.sysOrgNotes = sysOrgNotes;
	}
	public String getSysGovUpname() {
		return sysGovUpname;
	}
	public void setSysGovUpname(String sysGovUpname) {
		this.sysGovUpname = sysGovUpname;
	}
	public Integer getSysAreaId() {
		return sysAreaId;
	}
	public void setSysAreaId(Integer sysAreaId) {
		this.sysAreaId = sysAreaId;
	}
	public String getSysGovTypeName() {
		return sysGovTypeName;
	}
	public void setSysGovTypeName(String sysGovTypeName) {
		this.sysGovTypeName = sysGovTypeName;
	}
	public String getSysDicName() {
		return sysDicName;
	}
	public void setSysDicName(String sysDicName) {
		this.sysDicName = sysDicName;
	}
	public String getSysAreaName() {
		return sysAreaName;
	}
	public void setSysAreaName(String sysAreaName) {
		this.sysAreaName = sysAreaName;
	}
	public String getSysGovTypeCode() {
		return sysGovTypeCode;
	}
	public void setSysGovTypeCode(String sysGovTypeCode) {
		this.sysGovTypeCode = sysGovTypeCode;
	}
	public String getSysGovUpcode() {
		return sysGovUpcode;
	}
	public void setSysGovUpcode(String sysGovUpcode) {
		this.sysGovUpcode = sysGovUpcode;
	}
	
}
