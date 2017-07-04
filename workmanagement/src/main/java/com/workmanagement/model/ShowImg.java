package com.workmanagement.model;

import java.io.Serializable;

/**
 * Created by lzm on 2017/3/25.
 */
public class ShowImg implements Serializable {
    private static final long serialVersionUID = 4630357329118780886L;

    private String qymc;
    private String guanxi;
    private String zbd;
    private String areaname;

    public ShowImg() {
    }

    public ShowImg(String qymc, String guanxi, String zbd) {
        this.qymc = qymc;
        this.guanxi = guanxi;
        this.zbd = zbd;
    }

    public ShowImg(String qymc, String guanxi, String zbd, String areaname) {
        this.qymc = qymc;
        this.guanxi = guanxi;
        this.zbd = zbd;
        this.areaname = areaname;
    }

    public String getQymc() {
        return qymc;
    }

    public void setQymc(String qymc) {
        this.qymc = qymc;
    }

    public String getGuanxi() {
        return guanxi;
    }

    public void setGuanxi(String guanxi) {
        this.guanxi = guanxi;
    }

    public String getZbd() {
        return zbd;
    }

    public void setZbd(String zbd) {
        this.zbd = zbd;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }
}
