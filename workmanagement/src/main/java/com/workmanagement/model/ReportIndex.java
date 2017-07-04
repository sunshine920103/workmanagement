package com.workmanagement.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 报送记录表
 * Created by lzm on 2017/3/20.
 */
public class ReportIndex implements Serializable {

    private static final long serialVersionUID = -1829235249165664743L;

    private Integer reportIndexId;          //主键
    private Integer reportIndexMethod;      //提交方式，0.报文报送 1.excel报送 2.手工录入 3.月季报送
    private String reportIndexTemplate;     //报送模板名称
    private Integer reportIndexNumbers;     //数量 条
    private Date reportIndexTime;           //报送时间
    private Integer reportIndexStatus;      //状态，0上报成功，1上报失败
    private Integer sysOrgId;               //（外键）机构id
    private String reportIndexOrgName;      //机构名称
    private Date reportIndexSubmitTime;     //提交时间
    private Integer sysUserId;              //（外键）上报用户id
    private String reportIndexPath;         //报送文件目录
    private String errorExcelPath;          //报送错误信息文件目录

    private String reportIndexMethodName;
    private String reportIndexStatusName;
    private String sysUserName;

    public ReportIndex() {
    }

    public ReportIndex(Integer reportIndexMethod, String reportIndexTemplate,
                       Integer reportIndexNumbers, Date reportIndexTime, Integer reportIndexStatus,
                       Integer sysOrgId, String reportIndexOrgName, Date reportIndexSubmitTime,
                       Integer sysUserId, String reportIndexPath, String errorExcelPath) {
        this.reportIndexMethod = reportIndexMethod;
        this.reportIndexTemplate = reportIndexTemplate;
        this.reportIndexNumbers = reportIndexNumbers;
        this.reportIndexTime = reportIndexTime;
        this.reportIndexStatus = reportIndexStatus;
        this.sysOrgId = sysOrgId;
        this.reportIndexOrgName = reportIndexOrgName;
        this.reportIndexSubmitTime = reportIndexSubmitTime;
        this.sysUserId = sysUserId;
        this.reportIndexPath = reportIndexPath;
        this.errorExcelPath = errorExcelPath;
    }

    public Integer getReportIndexId() {
        return reportIndexId;
    }

    public void setReportIndexId(Integer reportIndexId) {
        this.reportIndexId = reportIndexId;
    }

    public Integer getReportIndexMethod() {
        return reportIndexMethod;
    }

    public void setReportIndexMethod(Integer reportIndexMethod) {
        this.reportIndexMethod = reportIndexMethod;
    }

    public String getReportIndexTemplate() {
        return reportIndexTemplate;
    }

    public void setReportIndexTemplate(String reportIndexTemplate) {
        this.reportIndexTemplate = reportIndexTemplate;
    }

    public Integer getReportIndexNumbers() {
        return reportIndexNumbers;
    }

    public void setReportIndexNumbers(Integer reportIndexNumbers) {
        this.reportIndexNumbers = reportIndexNumbers;
    }

    public Date getReportIndexTime() {
        return reportIndexTime;
    }

    public void setReportIndexTime(Date reportIndexTime) {
        this.reportIndexTime = reportIndexTime;
    }

    public Integer getReportIndexStatus() {
        return reportIndexStatus;
    }

    public void setReportIndexStatus(Integer reportIndexStatus) {
        this.reportIndexStatus = reportIndexStatus;
    }

    public Integer getSysOrgId() {
        return sysOrgId;
    }

    public void setSysOrgId(Integer sysOrgId) {
        this.sysOrgId = sysOrgId;
    }

    public String getReportIndexOrgName() {
        return reportIndexOrgName;
    }

    public void setReportIndexOrgName(String reportIndexOrgName) {
        this.reportIndexOrgName = reportIndexOrgName;
    }

    public Date getReportIndexSubmitTime() {
        return reportIndexSubmitTime;
    }

    public void setReportIndexSubmitTime(Date reportIndexSubmitTime) {
        this.reportIndexSubmitTime = reportIndexSubmitTime;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getReportIndexPath() {
        return reportIndexPath;
    }

    public void setReportIndexPath(String reportIndexPath) {
        this.reportIndexPath = reportIndexPath;
    }

    public String getReportIndexMethodName() {
        return reportIndexMethodName;
    }

    public void setReportIndexMethodName(String reportIndexMethodName) {
        this.reportIndexMethodName = reportIndexMethodName;
    }

    public String getReportIndexStatusName() {
        return reportIndexStatusName;
    }

    public void setReportIndexStatusName(String reportIndexStatusName) {
        this.reportIndexStatusName = reportIndexStatusName;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getErrorExcelPath() {
        return errorExcelPath;
    }

    public void setErrorExcelPath(String errorExcelPath) {
        this.errorExcelPath = errorExcelPath;
    }
}
