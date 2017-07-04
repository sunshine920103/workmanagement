package com.workmanagement.model;

/**
 * 
 * @author wqs
 *别名表对应的实体类
 */
public class IndexItemAlias {
	
	private Integer indexItemAliasId;  
	private String indexItemAliasName;  //别名名称
	private Integer indexItemId;  //外键，指标项id
	private Integer sysAreaId;		//外键，所属区域id
	private Integer indexId;		//指标大类id，这个字段在做excel报送时使用	
	
	
	
	public Integer getIndexId() {
		return indexId;
	}
	public void setIndexId(Integer indexId) {
		this.indexId = indexId;
	}
	public Integer getIndexItemAliasId() {
		return indexItemAliasId;
	}
	public void setIndexItemAliasId(Integer indexItemAliasId) {
		this.indexItemAliasId = indexItemAliasId;
	}
	public String getIndexItemAliasName() {
		return indexItemAliasName;
	}
	public void setIndexItemAliasName(String indexItemAliasName) {
		this.indexItemAliasName = indexItemAliasName;
	}
	public Integer getIndexItemId() {
		return indexItemId;
	}
	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}
	public Integer getSysAreaId() {
		return sysAreaId;
	}
	public void setSysAreaId(Integer sysAreaId) {
		this.sysAreaId = sysAreaId;
	}

	
	
}
