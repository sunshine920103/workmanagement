package com.workmanagement.enums;

/**
 * 各种默认的枚举
 * Created by lzm on 2017/4/6.
 */
public enum BothEnum {
    /**
     * 金融机构或者政府部门
     */
    dicOrgAndGov("金融机构或者政府部门"),
    /**
     * 行业
     */
    dicClassFy("经济行业"),
    /**
     * 地区
     */
    dicSysArea("地区");


    private String dicName;

    BothEnum(String dicName) {
        this.dicName = dicName;
    }

    public String getDicName() {
        return dicName;
    }
}
