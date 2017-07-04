package com.workmanagement.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 汇率维护
 * @author xiehao
 *
 */
public class DicExchangeLast implements Serializable{
	private static final long serialVersionUID = -69077863004241353L;
	private Integer dicExchangeId;
	//数据字典详情id，外键
	private Integer dicId;
	//币种
	private String dicExchangeName;
	//代码
	private String dicExchangeCode;
	//汇率
	private Double dicExchangeValue;
	//最后更新时间
	private String dicExchangeTime;
	//最后更新时间 date类型
	private Date dicExchangeTimeDate;
	//所属区域
	private Integer dicAreaId;
	//所属区域名称
	private String sysAreaName;
	public String getSysAreaName() {
		return sysAreaName;
	}
	public void setSysAreaName(String sysAreaName) {
		this.sysAreaName = sysAreaName;
	}
	public Date getDicExchangeTimeDate() {
		return dicExchangeTimeDate;
	}
	public void setDicExchangeTimeDate(Date dicExchangeTimeDate) {
		this.dicExchangeTimeDate = dicExchangeTimeDate;
	}
	public Integer getDicExchangeId() {
		return dicExchangeId;
	}
	public void setDicExchangeId(Integer dicExchangeId) {
		this.dicExchangeId = dicExchangeId;
	}
	public Integer getDicId() {
		return dicId;
	}
	public void setDicId(Integer dicId) {
		this.dicId = dicId;
	}
	public String getDicExchangeName() {
		return dicExchangeName;
	}
	public void setDicExchangeName(String dicExchangeName) {
		this.dicExchangeName = dicExchangeName;
	}
	public String getDicExchangeCode() {
		return dicExchangeCode;
	}
	public void setDicExchangeCode(String dicExchangeCode) {
		this.dicExchangeCode = dicExchangeCode;
	}
	public Double getDicExchangeValue() {
		return dicExchangeValue;
	}
	public void setDicExchangeValue(Double dicExchangeValue) {
		this.dicExchangeValue = dicExchangeValue;
	}
	public Integer getDicAreaId() {
		return dicAreaId;
	}
	public void setDicAreaId(Integer dicAreaId) {
		this.dicAreaId = dicAreaId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDicExchangeTime() {
		return dicExchangeTime;
	}
	public void setDicExchangeTime(String dicExchangeTime) {
		this.dicExchangeTime = dicExchangeTime;
	}
	@Override
	public String toString() {
		return "DicExchangeLast [dicExchangeId=" + dicExchangeId + ", dicId="
				+ dicId + ", dicExchangeName=" + dicExchangeName
				+ ", dicExchangeCode=" + dicExchangeCode
				+ ", dicExchangeValue=" + dicExchangeValue + ", dicAreaId="
				+ dicAreaId + "]";
	}

	
	
}
