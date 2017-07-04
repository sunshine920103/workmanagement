package com.workmanagement.model;

import java.io.Serializable;

/**
 * 字典详情表
 * <p>
 * Created by lzm on 2017/3/6.
 */
public class DicContent implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4702194739035614675L;

    private Integer dicContentId;  //主键
    private Integer dicId;  //（外键）数据字典主键
    private String dicContentCode;  //字典代码,与行业分类和汇率关联，唯一，无法删除使用的字典代码
    private String dicContentValue;  //指标值,与行业分类和汇率关联，唯一，无法删除使用的指标值

    public DicContent() {
    }

    public DicContent(Integer dicId, String dicContentCode, String dicContentValue) {
        this.dicId = dicId;
        this.dicContentCode = dicContentCode;
        this.dicContentValue = dicContentValue;
    }

    public DicContent(Integer dicContentId, Integer dicId, String dicContentCode, String dicContentValue) {
        this.dicContentId = dicContentId;
        this.dicId = dicId;
        this.dicContentCode = dicContentCode;
        this.dicContentValue = dicContentValue;
    }

    public Integer getDicContentId() {
        return dicContentId;
    }

    public void setDicContentId(Integer dicContentId) {
        this.dicContentId = dicContentId;
    }

    public Integer getDicId() {
        return dicId;
    }

    public void setDicId(Integer dicId) {
        this.dicId = dicId;
    }

    public String getDicContentCode() {
        return dicContentCode;
    }

    public void setDicContentCode(String dicContentCode) {
        this.dicContentCode = dicContentCode;
    }

    public String getDicContentValue() {
        return dicContentValue;
    }

    public void setDicContentValue(String dicContentValue) {
        this.dicContentValue = dicContentValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dicContentId == null ? "" : ("字典详情id：" + dicContentId + ","))
                .append(dicId == null ? "" : ("数据字典id：" + dicId + ","))
                .append(dicContentCode == null ? "" : ("字典代码：" + dicContentCode + ","))
                .append(dicContentValue == null ? "" : ("指标值：" + dicContentValue + ","));
        return sb.substring(0, sb.length() - 1);
    }
}
