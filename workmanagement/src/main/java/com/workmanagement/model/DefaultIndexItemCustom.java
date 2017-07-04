package com.workmanagement.model;

import java.io.Serializable;

/**
 * 默认指标项 扩展类
 * 
 * @author lzl
 *
 */

public class DefaultIndexItemCustom extends DefaultIndexItem implements Serializable {
	private static final long serialVersionUID = -6446114763999370775L;
	// 企业名称
	private String qymc;
	// 法定代表人
	private String fddbr;
	// 关联类型 1投资关联 2担保关联 3高管关联
	private String relatedType;
	// 是否有子集
	private String isSon;
	// 联系电话
	private String lxdh;
	// 被担保人证件类型
	private String bdbrzjlx;
	// 被担保人证件号码
	private String bdbrzjhm;
	// 被担保人证件号码
	private String initiativeOrPassivity;
	private String qyzs;

	private Integer defaultItemId;
	public Integer getDefaultItemId() {
		return defaultItemId;
	}

	public void setDefaultItemId(Integer defaultItemId) {
		this.defaultItemId = defaultItemId;
	}

	public String getQyzs() {
		return qyzs;
	}

	public void setQyzs(String qyzs) {
		this.qyzs = qyzs;
	}

	public DefaultIndexItemCustom() {
	}

	public String getInitiativeOrPassivity() {
		return initiativeOrPassivity;
	}

	public void setInitiativeOrPassivity(String initiativeOrPassivity) {
		this.initiativeOrPassivity = initiativeOrPassivity;
	}

	public String getBdbrzjhm() {
		return bdbrzjhm;
	}

	public void setBdbrzjhm(String bdbrzjhm) {
		this.bdbrzjhm = bdbrzjhm;
	}

	public String getBdbrzjlx() {
		return bdbrzjlx;
	}

	public void setBdbrzjlx(String bdbrzjlx) {
		this.bdbrzjlx = bdbrzjlx;
	}

	public String getIsSon() {
		return isSon;
	}

	public void setIsSon(String isSon) {
		this.isSon = isSon;
	}

	public String getQymc() {
		if (this.qymc == null) {
			return "(暂缺)";
		}
		return qymc;
	}

	public void setQymc(String qymc) {
		this.qymc = qymc;
	}

	public String getFddbr() {
		if (this.fddbr == null) {
			return "(暂缺)";
		}
		return fddbr;
	}

	public void setFddbr(String fddbr) {
		this.fddbr = fddbr;
	}

	public String getRelatedType() {
		return relatedType;
	}

	public void setRelatedType(String relatedType) {
		this.relatedType = relatedType;
	}

	public String getLxdh() {
		if (this.lxdh == null) {
			return "(暂缺)";
		}
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

}
