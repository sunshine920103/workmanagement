package com.workmanagement.model;

import org.apache.commons.lang.time.DateFormatUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户行为审计的model
 * <p>
 * Created by lzm on 2017/3/13.
 */
public class SysUserLog implements Serializable {

    private static final long serialVersionUID = 3263895178586206266L;

    private Integer sysUserLogId;              //主键
    private Integer sysOrgId;                  //（外键）机构ID
    private String sysUserLogOrgName;          //机构名称
    private String sysUserLogUserName;         //用户名称
    private String sysUserLogRoleName;         //角色名称
    private String sysUserLogMenuName;         //菜单名称
    private String sysUserLogEnterpriseCode;   //企业编码
    private Integer indexId;                   //(外键)指标大类id
    private Integer indexItemId;               //(外键)指标项id
    private String sysUserLogOldValue;         //原值
    private String sysUserLogNewValue;         //修改值
    private Date sysUserLogTime;               //操作时间（到秒）  1
    private Integer sysUserLogOperateType;     //操作类型（增删改，导入导出）
    private Integer sysUserLogCount;            //数据的数量
    private String sysUserLogQueryUserCondition;   //搜索的条件（用户看）
    private String sysUserLogQuerySql;         //搜索sql
    private String sysUserLogUrl;              //查看详情（url跳转）
    private String sysUserLogFile;             //导入导出的文件url
    private String sysUserLogAuthFile;         //授权文件
    private boolean sysUserLogResult;          //操作结果 0 失败 1 成功
    private String sysUserLogIp;  //操作ip

    public String getSysUserLogIp() {
		return sysUserLogIp;
	}

	public void setSysUserLogIp(String sysUserLogIp) {
		this.sysUserLogIp = sysUserLogIp;
	}

	//这俩是mapper左连接查出来的
    private String indexName;                   //指标大类名称
    private String indexItemName;               //指标项名称

    public SysUserLog() {
    }

    /**
     * 不带操作时间的构造器
     *
     * @param sysUserLogMenuName           菜单名称
     * @param sysUserLogEnterpriseCode     企业编码
     * @param indexId                      指标大类id
     * @param indexItemId                  指标项id
     * @param sysUserLogOldValue           原值
     * @param sysUserLogNewValue           修改值
     * @param sysUserLogOperateType        操作类型
     * @param sysUserLogCount              数据的数量
     * @param sysUserLogQueryUserCondition 搜索的条件
     * @param sysUserLogQuerySql           搜索sql
     * @param sysUserLogUrl                查看详情
     * @param sysUserLogFile               导入导出的文件url
     * @param sysUserLogAuthFile           授权文件
     * @param sysUserLogResult             操作结果
     */
    public SysUserLog(String sysUserLogMenuName, String sysUserLogEnterpriseCode, Integer indexId,
                      Integer indexItemId, String sysUserLogOldValue, String sysUserLogNewValue,
                      Integer sysUserLogOperateType, Integer sysUserLogCount, String sysUserLogQueryUserCondition,
                      String sysUserLogQuerySql, String sysUserLogUrl, String sysUserLogFile,
                      String sysUserLogAuthFile, boolean sysUserLogResult) {
        this.sysUserLogMenuName = sysUserLogMenuName;
        this.sysUserLogEnterpriseCode = sysUserLogEnterpriseCode;
        this.indexId = indexId;
        this.indexItemId = indexItemId;
        this.sysUserLogOldValue = sysUserLogOldValue;
        this.sysUserLogNewValue = sysUserLogNewValue;
        this.sysUserLogOperateType = sysUserLogOperateType;
        this.sysUserLogCount = sysUserLogCount;
        this.sysUserLogQueryUserCondition = sysUserLogQueryUserCondition;
        this.sysUserLogQuerySql = sysUserLogQuerySql;
        this.sysUserLogUrl = sysUserLogUrl;
        this.sysUserLogFile = sysUserLogFile;
        this.sysUserLogAuthFile = sysUserLogAuthFile;
        this.sysUserLogResult = sysUserLogResult;
    }

    /**
     * 带时间的构造器
     *
     * @param sysUserLogMenuName           菜单名称
     * @param sysUserLogEnterpriseCode     企业编码
     * @param indexId                      指标大类id
     * @param indexItemId                  指标项id
     * @param sysUserLogOldValue           原值
     * @param sysUserLogNewValue           修改值
     * @param sysUserLogTime               操作时间
     * @param sysUserLogOperateType        操作类型
     * @param sysUserLogCount              数据的数量
     * @param sysUserLogQueryUserCondition 搜索的条件
     * @param sysUserLogQuerySql           搜索sql
     * @param sysUserLogUrl                查看详情
     * @param sysUserLogFile               导入导出的文件url
     * @param sysUserLogAuthFile           授权文件
     * @param sysUserLogResult             操作结果
     */
    public SysUserLog(String sysUserLogMenuName, String sysUserLogEnterpriseCode, Integer indexId,
                      Integer indexItemId, String sysUserLogOldValue, String sysUserLogNewValue,
                      Date sysUserLogTime, Integer sysUserLogOperateType, Integer sysUserLogCount,
                      String sysUserLogQueryUserCondition, String sysUserLogQuerySql, String sysUserLogUrl,
                      String sysUserLogFile, String sysUserLogAuthFile, boolean sysUserLogResult) {
        this.sysUserLogMenuName = sysUserLogMenuName;
        this.sysUserLogEnterpriseCode = sysUserLogEnterpriseCode;
        this.indexId = indexId;
        this.indexItemId = indexItemId;
        this.sysUserLogOldValue = sysUserLogOldValue;
        this.sysUserLogNewValue = sysUserLogNewValue;
        this.sysUserLogTime = sysUserLogTime;
        this.sysUserLogOperateType = sysUserLogOperateType;
        this.sysUserLogCount = sysUserLogCount;
        this.sysUserLogQueryUserCondition = sysUserLogQueryUserCondition;
        this.sysUserLogQuerySql = sysUserLogQuerySql;
        this.sysUserLogUrl = sysUserLogUrl;
        this.sysUserLogFile = sysUserLogFile;
        this.sysUserLogAuthFile = sysUserLogAuthFile;
        this.sysUserLogResult = sysUserLogResult;
    }
    
    public SysUserLog(Integer sysOrgId, String sysUserlogOrgName, String sysUserlogUserName,
			String sysUserlogRoleName, String sysUserlogMenuName,
			Date sysUserLogTime, Integer sysUserlogOperateType,
			boolean sysUserlogResult,  String sysUserlogQueryUserCondition
			) {
		super();
		this.sysOrgId = sysOrgId;
		this.sysUserLogOrgName = sysUserlogOrgName;
		this.sysUserLogUserName = sysUserlogUserName;
		this.sysUserLogRoleName = sysUserlogRoleName;
		this.sysUserLogMenuName = sysUserlogMenuName;
		this.sysUserLogTime = sysUserLogTime;
		this.sysUserLogOperateType = sysUserlogOperateType;
		this.sysUserLogResult = sysUserlogResult;
		this.sysUserLogQueryUserCondition = sysUserlogQueryUserCondition;
	}

    public Integer getSysUserLogId() {
        return sysUserLogId;
    }

    public void setSysUserLogId(Integer sysUserLogId) {
        this.sysUserLogId = sysUserLogId;
    }

    public Integer getSysOrgId() {
        return sysOrgId;
    }

    public void setSysOrgId(Integer sysOrgId) {
        this.sysOrgId = sysOrgId;
    }

    public String getSysUserLogOrgName() {
        return sysUserLogOrgName;
    }

    public void setSysUserLogOrgName(String sysUserLogOrgName) {
        this.sysUserLogOrgName = sysUserLogOrgName;
    }

    public String getSysUserLogUserName() {
        return sysUserLogUserName;
    }

    public void setSysUserLogUserName(String sysUserLogUserName) {
        this.sysUserLogUserName = sysUserLogUserName;
    }

    public String getSysUserLogRoleName() {
        return sysUserLogRoleName;
    }

    public void setSysUserLogRoleName(String sysUserLogRoleName) {
        this.sysUserLogRoleName = sysUserLogRoleName;
    }

    public String getSysUserLogMenuName() {
        return sysUserLogMenuName;
    }

    public void setSysUserLogMenuName(String sysUserLogMenuName) {
        this.sysUserLogMenuName = sysUserLogMenuName;
    }

    public String getSysUserLogEnterpriseCode() {
        return sysUserLogEnterpriseCode;
    }

    public void setSysUserLogEnterpriseCode(String sysUserLogEnterpriseCode) {
        this.sysUserLogEnterpriseCode = sysUserLogEnterpriseCode;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public Integer getIndexItemId() {
        return indexItemId;
    }

    public void setIndexItemId(Integer indexItemId) {
        this.indexItemId = indexItemId;
    }

    public String getSysUserLogOldValue() {
        return sysUserLogOldValue;
    }

    public void setSysUserLogOldValue(String sysUserLogOldValue) {
        this.sysUserLogOldValue = sysUserLogOldValue;
    }

    public String getSysUserLogNewValue() {
        return sysUserLogNewValue;
    }

    public void setSysUserLogNewValue(String sysUserLogNewValue) {
        this.sysUserLogNewValue = sysUserLogNewValue;
    }

    public Date getSysUserLogTime() {
        return sysUserLogTime;
    }

    public void setSysUserLogTime(Date sysUserLogTime) {
        this.sysUserLogTime = sysUserLogTime;
    }

    public Integer getSysUserLogOperateType() {
        return sysUserLogOperateType;
    }

    public void setSysUserLogOperateType(Integer sysUserLogOperateType) {
        this.sysUserLogOperateType = sysUserLogOperateType;
    }

    public Integer getSysUserLogCount() {
        return sysUserLogCount;
    }

    public void setSysUserLogCount(Integer sysUserLogCount) {
        this.sysUserLogCount = sysUserLogCount;
    }

    public String getSysUserLogQueryUserCondition() {
        return sysUserLogQueryUserCondition;
    }

    public void setSysUserLogQueryUserCondition(String sysUserLogQueryUserCondition) {
        this.sysUserLogQueryUserCondition = sysUserLogQueryUserCondition;
    }

    public String getSysUserLogQuerySql() {
        return sysUserLogQuerySql;
    }

    public void setSysUserLogQuerySql(String sysUserLogQuerySql) {
        this.sysUserLogQuerySql = sysUserLogQuerySql;
    }

    public String getSysUserLogUrl() {
        return sysUserLogUrl;
    }

    public void setSysUserLogUrl(String sysUserLogUrl) {
        this.sysUserLogUrl = sysUserLogUrl;
    }

    public String getSysUserLogFile() {
        return sysUserLogFile;
    }

    public void setSysUserLogFile(String sysUserLogFile) {
        this.sysUserLogFile = sysUserLogFile;
    }

    public String getSysUserLogAuthFile() {
        return sysUserLogAuthFile;
    }

    public void setSysUserLogAuthFile(String sysUserLogAuthFile) {
        this.sysUserLogAuthFile = sysUserLogAuthFile;
    }

    public boolean getSysUserLogResult() {
        return sysUserLogResult;
    }

    public void setSysUserLogResult(boolean sysUserLogResult) {
        this.sysUserLogResult = sysUserLogResult;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexItemName() {
        return indexItemName;
    }

    public void setIndexItemName(String indexItemName) {
        this.indexItemName = indexItemName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(sysUserLogId == null ? "" : ("用户行为审计id：" + sysUserLogId + ","))
                .append(sysOrgId == null ? "" : ("机构id：" + sysOrgId + ","))
                .append(sysUserLogOrgName == null ? "" : ("机构名称：" + sysUserLogOrgName + ","))
                .append(sysUserLogUserName == null ? "" : ("用户姓名：" + sysUserLogUserName + ","))
                .append(sysUserLogRoleName == null ? "" : ("角色名称：" + sysUserLogRoleName + ","))
                .append(sysUserLogMenuName == null ? "" : ("菜单名称：" + sysUserLogMenuName + ","))
                .append(sysUserLogEnterpriseCode == null ? "" : ("企业编码：" + sysUserLogEnterpriseCode + ","))
                .append(indexId == null ? "" : ("指标大类id：" + indexId + ","))
                .append(indexItemId == null ? "" : ("指标项id：" + indexItemId + ","))
                .append(sysUserLogOldValue == null ? "" : ("原值：" + sysUserLogOldValue + ","))
                .append(sysUserLogNewValue == null ? "" : ("现值：" + sysUserLogNewValue + ","))
                .append(sysUserLogTime == null ? "" : ("操作时间：" + DateFormatUtils.format(sysUserLogTime, "yyyy:MM:dd HH:mm:ss") + ","))
                .append(sysUserLogOperateType == null ? "" : ("操作类型：" + sysUserLogOperateType + ","))
                .append(sysUserLogCount == null ? "" : ("操作的数据条数：" + sysUserLogCount + ","))
                .append(sysUserLogQueryUserCondition == null ? "" : ("查询条件：" + sysUserLogQueryUserCondition + ","))
                .append("操作结果：").append(sysUserLogResult).append(",");
        return sb.substring(0, sb.length() - 1);
    }
}
