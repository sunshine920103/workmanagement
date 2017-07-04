package com.workmanagement.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * 
 * @author wqs renyang
 *
 *  指标项 ：对应数据库表的字段
 */
public class IndexItemTb {
	
	private Integer indexItemId;
	private Integer indexId;  //（外键）指标大类id
	private String indexItemName;  //指标项名称
	private String indexItemCode;  //数据库字段名
	private Integer indexItemNumber;  //序号
	private Integer sysAreaId;  //（外键）所属区域id
	private Integer indexItemUsed;  //是否启用，0否1（默认）是
	private Integer indexItemType;  //数据类型，0字符，1时间，2数值,3数据字典
	private Integer dicId;  //(外键)数据字典id
	private Integer indexItemImportUnique;  //是否识别码（唯一）0否（默认）1是
	private Integer indexItemEmpty;  //是否可以为空，0否，1 是（默认）
	private String indexItemNetId;  //网络标识
	private String indexItemNotes;  //备注
	private String indexItemCreateTime;  //创建时间
	private Integer sys_user_id;  //（外键）创建者id
	private Integer sys_org_id;  //（外键）创建机构id
	private String indexItemAliasName   ;//别名
	private Integer indexItemAccuracy;//类型为数值时，限定精度（小数点后几位）
	private String sys_org_name; //机构名称
	private Integer varLength;//字符长度
	
	/**
	 * 县的指标项，的上级市的名称
	 */
	private String shi;
	
	//自定义变量，异议处理做判断用
	private Integer dicType;
	
	//所属地区名称
	private String sysAreaName;
	public String getSysAreaName() {
		return sysAreaName;
	}

	public void setSysAreaName(String sysAreaName) {
		this.sysAreaName = sysAreaName;
	}

	@Override
	public String toString() {
		String use = "";
		if(indexItemUsed ==0){
			use ="禁用";
		}else{
			use ="启用";
		}
		String type = "";
		if(indexItemType ==0){
			type ="字符";
		}else if(indexItemType ==1){
			type ="时间";			
		}else if(indexItemType ==2){
			type ="数值";			
		}else {
			type ="字典";			
		}
		String unique = "";
		if(indexItemImportUnique ==0){
			use ="否";
		}else{
			use ="是";
		}
		String empty = "";
		if(indexItemEmpty ==0){
			use ="否";
		}else{
			use ="是";
		}
		
		return "指标项 [指标项名称：" + indexItemName+ ", 指标项代号：" + indexItemCode + ", 指标项序号：" + indexItemNumber 
				+ ", 是否启用：" +use+ ", 指标项类型：" +type 
				+ ", 是否识别码（唯一）：" + unique + ", 是否可以为空：" + empty
				+ ", 网络标识：" + indexItemNetId + ", 备注：" + indexItemNotes + ", 创建时间："
				+ DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")+"]";
	}
	
	public Integer getVarLength() {
		return varLength;
	}
	public void setVarLength(Integer varLength) {
		this.varLength = varLength;
	}
	public Integer getIndexItemAccuracy() {
		return indexItemAccuracy;
	}
	public void setIndexItemAccuracy(Integer indexItemAccuracy) {
		this.indexItemAccuracy = indexItemAccuracy;
	}
	public String getSys_org_name() {
		return sys_org_name;
	}
	public void setSys_org_name(String sys_org_name) {
		this.sys_org_name = sys_org_name;
	}
	public Integer getIndexItemId() {
		return indexItemId;
	}
	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}
	public Integer getIndexId() {
		return indexId;
	}
	public void setIndexId(Integer indexId) {
		this.indexId = indexId;
	}
	public String getIndexItemName() {
		return indexItemName;
	}
	public void setIndexItemName(String indexItemName) {
		this.indexItemName = indexItemName;
	}
	public String getIndexItemCode() {
		return indexItemCode;
	}
	public void setIndexItemCode(String indexItemCode) {
		this.indexItemCode = indexItemCode;
	}
	public Integer getIndexItemNumber() {
		return indexItemNumber;
	}
	public void setIndexItemNumber(Integer indexItemNumber) {
		this.indexItemNumber = indexItemNumber;
	}
	public Integer getSysAreaId() {
		return sysAreaId;
	}
	public void setSysAreaId(Integer sysAreaId) {
		this.sysAreaId = sysAreaId;
	}
	public Integer getIndexItemUsed() {
		return indexItemUsed;
	}
	public void setIndexItemUsed(Integer indexItemUsed) {
		this.indexItemUsed = indexItemUsed;
	}
	public Integer getIndexItemType() {
		return indexItemType;
	}
	public void setIndexItemType(Integer indexItemType) {
		this.indexItemType = indexItemType;
	}
	public Integer getDicId() {
		return dicId;
	}
	public void setDicId(Integer dicId) {
		this.dicId = dicId;
	}
	public Integer getIndexItemImportUnique() {
		return indexItemImportUnique;
	}
	public void setIndexItemImportUnique(Integer indexItemImportUnique) {
		this.indexItemImportUnique = indexItemImportUnique;
	}
	public Integer getIndexItemEmpty() {
		return indexItemEmpty;
	}
	public void setIndexItemEmpty(Integer indexItemEmpty) {
		this.indexItemEmpty = indexItemEmpty;
	}
	public String getIndexItemNetId() {
		return indexItemNetId;
	}
	public void setIndexItemNetId(String indexItemNetId) {
		this.indexItemNetId = indexItemNetId;
	}
	public String getIndexItemNotes() {
		return indexItemNotes;
	}
	public void setIndexItemNotes(String indexItemNotes) {
		this.indexItemNotes = indexItemNotes;
	}
	public String getIndexItemCreateTime() {
		return indexItemCreateTime;
	}
	public void setIndexItemCreateTime(String indexItemCreateTime) {
		this.indexItemCreateTime = indexItemCreateTime;
	}
	public Integer getSys_user_id() {
		return sys_user_id;
	}
	public void setSys_user_id(Integer sys_user_id) {
		this.sys_user_id = sys_user_id;
	}
	public Integer getSys_org_id() {
		return sys_org_id;
	}
	public void setSys_org_id(Integer sys_org_id) {
		this.sys_org_id = sys_org_id;
	}
	public String getIndexItemAliasName() {
		return indexItemAliasName;
	}
	public void setIndexItemAliasName(String indexItemAliasName) {
		this.indexItemAliasName = indexItemAliasName;
	}

	public Integer getDicType() {
		return dicType;
	}

	public void setDicType(Integer dicType) {
		this.dicType = dicType;
	}

	public String getShi() {
		return shi;
	}

	public void setShi(String shi) {
		this.shi = shi;
	}
	
}
