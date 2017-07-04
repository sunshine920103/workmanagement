package com.workmanagement.model;

import java.io.Serializable;
import java.util.List;

/**
 * 字典表
 * <p>
 * Created by lzm on 2017/3/6.
 */
public class Dic implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2815252884902604402L;

    private Integer dicId;  //主键
    private Integer sysAreaId;  //外键：所属地区id（区域表的主键）
    private String dicName;  //字典名称必须是唯一，无法删除使用的字典
    private String dicNotes; //备注
    private String isThisArea; //默认不是本地区的字典
    private List<DicContent> dicContentList;  //字典详情表子集

    public Dic() {
    }

    public Dic(Integer dicId, Integer sysAreaId) {
        this.dicId = dicId;
        this.sysAreaId = sysAreaId;
    }

    public Dic(Integer sysAreaId, String dicName) {
        this.sysAreaId = sysAreaId;
        this.dicName = dicName;
    }

    public Dic(Integer sysAreaId, String dicName, List<DicContent> dicContentList) {
        this.sysAreaId = sysAreaId;
        this.dicName = dicName;
        this.dicContentList = dicContentList;
    }

    public Dic(Integer dicId, Integer sysAreaId, String dicName) {
        this.dicId = dicId;
        this.sysAreaId = sysAreaId;
        this.dicName = dicName;
    }

    public Dic(Integer dicId, Integer sysAreaId, String dicName, List<DicContent> dicContentList) {
        this.dicId = dicId;
        this.sysAreaId = sysAreaId;
        this.dicName = dicName;
        this.dicContentList = dicContentList;
    }

    public Dic(Integer dicId, Integer sysAreaId, String dicName, String dicNotes, List<DicContent> dicContentList) {
        this.dicId = dicId;
        this.sysAreaId = sysAreaId;
        this.dicName = dicName;
        this.dicNotes = dicNotes;
        this.dicContentList = dicContentList;
    }

    public Integer getDicId() {
        return dicId;
    }

    public void setDicId(Integer dicId) {
        this.dicId = dicId;
    }

    public Integer getSysAreaId() {
        return sysAreaId;
    }

    public void setSysAreaId(Integer sysAreaId) {
        this.sysAreaId = sysAreaId;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public List<DicContent> getDicContentList() {
        return dicContentList;
    }

    public void setDicContentList(List<DicContent> dicContentList) {
        this.dicContentList = dicContentList;
    }

    public String getDicNotes() {
        return dicNotes;
    }

    public void setDicNotes(String dicNotes) {
        this.dicNotes = dicNotes;
    }

    public String getIsThisArea() {
        return isThisArea;
    }

    public void setIsThisArea(String isThisArea) {
        this.isThisArea = isThisArea;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dicId == null ? "" : ("数据字典id：" + dicId + ","))
                .append(sysAreaId == null ? "" : ("地区id：" + sysAreaId + ","))
                .append(dicName == null ? "" : ("字典名称：" + dicName + ","))
                .append(dicNotes == null ? "" : ("字典备注：" + dicNotes + ","));
        return sb.substring(0, sb.length() - 1);
    }
}
