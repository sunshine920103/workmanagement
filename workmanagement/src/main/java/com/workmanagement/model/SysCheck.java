package com.workmanagement.model;

import org.apache.commons.lang.time.DateFormatUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 校验规则表
 * Created by lzm on 2017/3/16.
 */
public class SysCheck implements Serializable {

    private static final long serialVersionUID = -4121034200307916431L;

    private Integer sysCheckId;         //主键
    private String sysCheckItems;       //他们爸爸的名字
    private String sysCheckFormula;     //校验公式  必填
    private Date sysCheckCTime;         //创建时间  必填
    private Date sysCheckSDate;         //开始日期
    private Date sysCheckEDate;         //截止日期
    private Integer indexId;            //关联的指标大类的id
    private Integer orgCreatorId;       //创建的机构的id
    private String sysCheckExplain;     //说明
    private Integer sysAreaId;          //作用地区 ID

    public SysCheck() {
    }

    public SysCheck(String sysCheckItems, String sysCheckFormula, Date sysCheckCTime, Integer indexId, Integer orgCreatorId) {
        this.sysCheckItems = sysCheckItems;
        this.sysCheckFormula = sysCheckFormula;
        this.sysCheckCTime = sysCheckCTime;
        this.indexId = indexId;
        this.orgCreatorId = orgCreatorId;
    }

    public SysCheck(String sysCheckItems, String sysCheckFormula, Date sysCheckCTime, Date sysCheckSDate, Date sysCheckEDate, Integer indexId, Integer orgCreatorId, String sysCheckExplain) {
        this.sysCheckItems = sysCheckItems;
        this.sysCheckFormula = sysCheckFormula;
        this.sysCheckCTime = sysCheckCTime;
        this.sysCheckSDate = sysCheckSDate;
        this.sysCheckEDate = sysCheckEDate;
        this.indexId = indexId;
        this.orgCreatorId = orgCreatorId;
        this.sysCheckExplain = sysCheckExplain;
    }

    public SysCheck(Integer sysCheckId, String sysCheckItems, String sysCheckFormula, Date sysCheckCTime, Date sysCheckSDate, Date sysCheckEDate, Integer indexId, Integer orgCreatorId, String sysCheckExplain) {
        this.sysCheckId = sysCheckId;
        this.sysCheckItems = sysCheckItems;
        this.sysCheckFormula = sysCheckFormula;
        this.sysCheckCTime = sysCheckCTime;
        this.sysCheckSDate = sysCheckSDate;
        this.sysCheckEDate = sysCheckEDate;
        this.indexId = indexId;
        this.orgCreatorId = orgCreatorId;
        this.sysCheckExplain = sysCheckExplain;
    }

    public Integer getSysCheckId() {
        return sysCheckId;
    }

    public void setSysCheckId(Integer sysCheckId) {
        this.sysCheckId = sysCheckId;
    }

    public String getSysCheckFormula() {
        return sysCheckFormula;
    }

    public void setSysCheckFormula(String sysCheckFormula) {
        this.sysCheckFormula = sysCheckFormula;
    }

    public Date getSysCheckCTime() {
        return sysCheckCTime;
    }

    public void setSysCheckCTime(Date sysCheckCTime) {
        this.sysCheckCTime = sysCheckCTime;
    }

    public Date getSysCheckSDate() {
        return sysCheckSDate;
    }

    public void setSysCheckSDate(Date sysCheckSDate) {
        this.sysCheckSDate = sysCheckSDate;
    }

    public Date getSysCheckEDate() {
        return sysCheckEDate;
    }

    public void setSysCheckEDate(Date sysCheckEDate) {
        this.sysCheckEDate = sysCheckEDate;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public Integer getOrgCreatorId() {
        return orgCreatorId;
    }

    public void setOrgCreatorId(Integer orgCreatorId) {
        this.orgCreatorId = orgCreatorId;
    }

    public String getSysCheckItems() {
        return sysCheckItems;
    }

    public void setSysCheckItems(String sysCheckItems) {
        this.sysCheckItems = sysCheckItems;
    }

    public String getSysCheckExplain() {
        return sysCheckExplain;
    }

    public void setSysCheckExplain(String sysCheckExplain) {
        this.sysCheckExplain = sysCheckExplain;
    }

    public Integer getSysAreaId() {
        return sysAreaId;
    }

    public void setSysAreaId(Integer sysAreaId) {
        this.sysAreaId = sysAreaId;
    }

    @Override
    public String toString() {
        StringBuilder sbf = new StringBuilder();
        sbf.append(sysCheckId == null ? "" : ("校验规则id：" + sysCheckId + ","))
                .append(sysCheckItems == null ? "" : ("指标大类：" + sysCheckItems + ","))
                .append(sysCheckFormula == null ? "" : ("校验公式：" + sysCheckFormula + ","))
                .append(sysCheckCTime == null ? "" : ("创建时间：" + DateFormatUtils.format(sysCheckCTime, "yyyy:MM:dd HH:mm:ss") + ","))
                .append(sysCheckSDate == null ? "" : ("开始时间：" + DateFormatUtils.format(sysCheckSDate, "yyyy:MM:dd") + ","))
                .append(sysCheckEDate == null ? "" : ("截止日期：" + DateFormatUtils.format(sysCheckEDate, "yyyy:MM:dd") + ","))
                .append(indexId == null ? "" : ("指标大类id：" + indexId + ","))
                .append(orgCreatorId == null ? "" : ("创建机构id：" + orgCreatorId + ","))
                .append(sysCheckExplain == null ? "" : ("校验说明：" + sysCheckExplain + ","));
        return sbf.substring(0, sbf.length() - 1);
    }
}
