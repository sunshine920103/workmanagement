package com.workmanagement.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 汇率历史
 * @author danny
 *
 */
public class DicExchangeHistory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6692927091672111089L;
	private Integer dicExchangeId;
	//汇率外键
	private Integer dicExchangeLastId;
	//币种
	private String dicExchangeName;
	//代码
	private String dicExchangeCode;
	//汇率
	private Double dicExchangeValue;
	//更新时间
	private Date dicExchangeTime;
	public DicExchangeHistory() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DicExchangeHistory(Integer dicExchangeId, Integer dicExchangeLastId, String dicExchangeName,
			String dicExchangeCode, Double dicExchangeValue, Date dicExchangeTime) {
		super();
		this.dicExchangeId = dicExchangeId;
		this.dicExchangeLastId = dicExchangeLastId;
		this.dicExchangeName = dicExchangeName;
		this.dicExchangeCode = dicExchangeCode;
		this.dicExchangeValue = dicExchangeValue;
		this.dicExchangeTime = dicExchangeTime;
	}
	public Integer getDicExchangeId() {
		return dicExchangeId;
	}
	public void setDicExchangeId(Integer dicExchangeId) {
		this.dicExchangeId = dicExchangeId;
	}
	public Integer getDicExchangeLastId() {
		return dicExchangeLastId;
	}
	public void setDicExchangeLastId(Integer dicExchangeLastId) {
		this.dicExchangeLastId = dicExchangeLastId;
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
	public Date getDicExchangeTime() {
		return dicExchangeTime;
	}
	public void setDicExchangeTime(Date dicExchangeTime) {
		this.dicExchangeTime = dicExchangeTime;
	}
}
