package com.workmanagement.model;

import java.io.Serializable;
/**
 * 政府部门类型
 * @author tianhao
 *
 */
public class GoverType implements Serializable{

	private static final long serialVersionUID = 6738223083733491034L;
	
	private Integer sysGovTypeId;
	private String sysGovTypeCode;//政府类型编码,不为空
	private String sysGovTypeName;//政府类型名称，不为空
	private String sysGovTypeNotes;//备注
	private Integer sysGovTypeUpid;//上级政府类型ID
	private String sysGovTypeUpname;//上级政府类型名称
	public Integer getSysGovTypeId() {
		return sysGovTypeId;
	}
	public void setSysGovTypeId(Integer sysGovTypeId) {
		this.sysGovTypeId = sysGovTypeId;
	}
	public String getSysGovTypeCode() {
		return sysGovTypeCode;
	}
	public void setSysGovTypeCode(String sysGovTypeCode) {
		this.sysGovTypeCode = sysGovTypeCode;
	}
	public String getSysGovTypeName() {
		return sysGovTypeName;
	}
	public void setSysGovTypeName(String sysGovTypeName) {
		this.sysGovTypeName = sysGovTypeName;
	}
	public String getSysGovTypeNotes() {
		return sysGovTypeNotes;
	}
	public void setSysGovTypeNotes(String sysGovTypeNotes) {
		this.sysGovTypeNotes = sysGovTypeNotes;
	}
	public Integer getSysGovTypeUpid() {
		return sysGovTypeUpid;
	}
	public void setSysGovTypeUpid(Integer sysGovTypeUpid) {
		this.sysGovTypeUpid = sysGovTypeUpid;
	}
	public String getSysGovTypeUpname() {
		return sysGovTypeUpname;
	}
	public void setSysGovTypeUpname(String sysGovTypeUpname) {
		this.sysGovTypeUpname = sysGovTypeUpname;
	}
	
}
