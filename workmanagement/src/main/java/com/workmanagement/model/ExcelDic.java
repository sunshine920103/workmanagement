package com.workmanagement.model;

import java.io.Serializable;

/**
 * Created by lzm on 2017/3/11.
 */
public class ExcelDic implements Serializable{

    private static final long serialVersionUID = 3816819059915984887L;

    private String dicName;
    private String dicNotes;
    private String dicContentCode;
    private String dicContentValue;

    public ExcelDic() {
    }

    public ExcelDic(String dicName) {
        this.dicName = dicName;
    }

    public ExcelDic(String dicName, String dicNotes, String dicContentCode, String dicContentValue) {
        this.dicName = dicName;
        this.dicNotes = dicNotes;
        this.dicContentCode = dicContentCode;
        this.dicContentValue = dicContentValue;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
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

    public String getDicNotes() {
        return dicNotes;
    }

    public void setDicNotes(String dicNotes) {
        this.dicNotes = dicNotes;
    }

    @Override
    public String toString() {
        return "ExcelDic{" +
                "dicName='" + dicName + '\'' +
                ", dicContentCode='" + dicContentCode + '\'' +
                ", dicContentValue='" + dicContentValue + '\'' +
                '}';
    }
}
