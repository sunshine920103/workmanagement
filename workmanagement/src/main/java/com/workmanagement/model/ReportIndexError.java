package com.workmanagement.model;

import java.io.Serializable;

/**
 * 报送错误记录表
 * @author danny
 *
 */
public class ReportIndexError implements Serializable{
	private static final long serialVersionUID = 4592977760157305301L;
	private Integer reportIndexErrorId;			//主键
	private Integer reportIndexId;				//excel报送表id,外键
	private String reportIndexErrorItemCode;	//有误指标项code
	private String reportIndexErrorName;		//有误指标项名称
	private String reportIndexErrorValue;		//上报值
	private String reportIndexErrorNotes;		//说明

	public ReportIndexError() {
	}

	public ReportIndexError(Integer reportIndexErrorId, Integer reportIndexId, String reportIndexErrorItemCode,
			String reportIndexErrorName, String reportIndexErrorValue, String reportIndexErrorNotes) {
		super();
		this.reportIndexErrorId = reportIndexErrorId;
		this.reportIndexId = reportIndexId;
		this.reportIndexErrorItemCode = reportIndexErrorItemCode;
		this.reportIndexErrorName = reportIndexErrorName;
		this.reportIndexErrorValue = reportIndexErrorValue;
		this.reportIndexErrorNotes = reportIndexErrorNotes;
	}

	public Integer getReportIndexErrorId() {
		return reportIndexErrorId;
	}

	public void setReportIndexErrorId(Integer reportIndexErrorId) {
		this.reportIndexErrorId = reportIndexErrorId;
	}

	public Integer getReportIndexId() {
		return reportIndexId;
	}

	public void setReportIndexId(Integer reportIndexId) {
		this.reportIndexId = reportIndexId;
	}

	public String getReportIndexErrorItemCode() {
		return reportIndexErrorItemCode;
	}

	public void setReportIndexErrorItemCode(String reportIndexErrorItemCode) {
		this.reportIndexErrorItemCode = reportIndexErrorItemCode;
	}

	public String getReportIndexErrorName() {
		return reportIndexErrorName;
	}

	public void setReportIndexErrorName(String reportIndexErrorName) {
		this.reportIndexErrorName = reportIndexErrorName;
	}

	public String getReportIndexErrorValue() {
		return reportIndexErrorValue;
	}

	public void setReportIndexErrorValue(String reportIndexErrorValue) {
		this.reportIndexErrorValue = reportIndexErrorValue;
	}

	public String getReportIndexErrorNotes() {
		return reportIndexErrorNotes;
	}

	public void setReportIndexErrorNotes(String reportIndexErrorNotes) {
		this.reportIndexErrorNotes = reportIndexErrorNotes;
	}
}
