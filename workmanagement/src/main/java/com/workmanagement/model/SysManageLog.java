package com.workmanagement.model;

import java.io.Serializable;
import java.util.Date;

import com.workmanagement.util.DateFormatter;

/**
 * 管理日志
 * Created by lzl on 2017/3/13.
 */
public class SysManageLog implements Serializable {
	/**
	 * 增加
	 */
	public static final Integer INSERT_SYSMANAGElOG = 1;
	/**
	 * 删除
	 */
	public static final Integer DELECT_SYSMANAGElOG = 2;
	/**
	 * 修改
	 */
	public static final Integer UPDATE_SYSMANAGElOG = 3;
	/**
	 * 查询
	 */
	public static final Integer SELECT_SYSMANAGElOG = 4;
	/**
	 * 导入
	 */
	public static final Integer IMPORT_SYSMANAGElOG = 5;
	/**
	 * 导出
	 */
	public static final Integer EXPORT_SYSMANAGElOG = 6;
	
   
	private static final long serialVersionUID = -7950665315495674631L;
	
	

	private Integer sysManageLogId;    
    private Integer sysOrgId;              //（外键）机构ID
	private String sysManageLogOrgName;    //机构名称
    private String sysManageLogUserName;   //用户名称
    private String sysManageLogRoleName;    //角色名称
    private String sysManageLogMenuName;    //菜单名称
    private String sysManageLogEnterpriseCode;   //企业编码
    private Integer indexId;   					//(外键)指标大类id
    private Integer indexItemId;				 //(外键)指标项id
    private String sysManageLogOldValue;		//原值
    private String sysManageLogNewValue;		//修改值
    private Date sysManageLogTime;				//操作时间（到秒）  1
    private Integer sysManageLogOperateType;	//操作类型（增删改，导入导出）
    private String sysManageLogAuthFile;		//授权文件
    private boolean sysManageLogResult;			//操作结果 0 失败 1 成功
    private Integer sysManageLogCount;            //数据的数量
    private String sysManageLogQueryUserCondition;   //搜索的条件（用户看）
    private String sysManageLogQuerySql;         //搜索sql
    private String sysManageLogUrl;              //查看详情（url跳转）
    private String sysManageLogFile;             //导入导出的文件url
    private String indexName;				   //指标大类名称
    private String indexItemName;			   //指标项名称
    private String sysManageLogIp;  //操作ip
    
    
    private String type;
    private String time;
    
    
  public String getTime() {
	  time = DateFormatter.dateToString(sysManageLogTime,null);
		return time;
	}

	public void setTime() {
	}

public String getType() {

		if(sysManageLogOperateType==1)
		this.type = "增加";
		if(sysManageLogOperateType==2)
		this.type = "删除";
		if(sysManageLogOperateType==3)
		this.type = "修改";
		if(sysManageLogOperateType==4)
		this.type = "查询";
		if(sysManageLogOperateType==5)
		this.type = "导入";
		if(sysManageLogOperateType==6)
		this.type = "导出";
		return type;
	}

	public void setType() {

	}

public String getSysManageLogIp() {
		return sysManageLogIp;
	}

	public void setSysManageLogIp(String sysMagageLogIp) {
		this.sysManageLogIp = sysMagageLogIp;
	}

public Integer getSysOrgId() {
		return sysOrgId;
	}

	public void setSysOrgId(Integer sysOrgId) {
		this.sysOrgId = sysOrgId;
	}

    public Integer getSysManageLogCount() {
		return sysManageLogCount;
	}

	public void setSysManageLogCount(Integer sysManageLogCount) {
		this.sysManageLogCount = sysManageLogCount;
	}

	public String getSysManageLogQueryUserCondition() {
		return sysManageLogQueryUserCondition;
	}

	public void setSysManageLogQueryUserCondition(String sysManageLogQueryUserCondition) {
		this.sysManageLogQueryUserCondition = sysManageLogQueryUserCondition;
	}

	public String getSysManageLogQuerySql() {
		return sysManageLogQuerySql;
	}

	public void setSysManageLogQuerySql(String sysManageLogQuerySql) {
		this.sysManageLogQuerySql = sysManageLogQuerySql;
	}

	public String getSysManageLogUrl() {
		return sysManageLogUrl;
	}

	public void setSysManageLogUrl(String sysManageLogUrl) {
		this.sysManageLogUrl = sysManageLogUrl;
	}

	public String getSysManageLogFile() {
		return sysManageLogFile;
	}

	public void setSysManageLogFile(String sysManageLogFile) {
		this.sysManageLogFile = sysManageLogFile;
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

	public SysManageLog() {
    }

 
    
    /**
     * @param sysManageLogMenuName            菜单名称
     * @param sysManageLogEnterpriseCode      企业编码
     * @param indexId                       指标大类id
     * @param indexItemId                   指标项id
     * @param sysManageLogOldValue            原值
     * @param sysManageLogNewValue            修改值
     * @param sysManageLogTime                操作时间
     * @param sysManageLogOperateType         操作类型
     * @param sysManageLogCount               数据的数量
     * @param sysManageLogQueryUserCondition  搜索的条件
     * @param sysManageLogQuerySql            搜索sql
     * @param sysManageLogUrl                 查看详情
     * @param sysManageLogFile                导入导出的文件url
     * @param sysManageLogAuthFile            授权文件
     * @param sysManageLogResult              操作结果
     */
    public SysManageLog(String sysManageLogMenuName, String sysManageLogEnterpriseCode, Integer indexId,
                      Integer indexItemId, String sysManageLogOldValue, String sysManageLogNewValue,
                      Date sysManageLogTime, Integer sysManageLogOperateType, Integer sysManageLogCount,
                      String sysManageLogQueryUserCondition, String sysManageLogQuerySql, String sysManageLogUrl,
                      String sysManageLogFile, String sysManageLogAuthFile, boolean sysManageLogResult) {
        this.sysManageLogMenuName = sysManageLogMenuName;
        this.sysManageLogEnterpriseCode = sysManageLogEnterpriseCode;
        this.indexId = indexId;
        this.indexItemId = indexItemId;
        this.sysManageLogOldValue = sysManageLogOldValue;
        this.sysManageLogNewValue = sysManageLogNewValue;
        this.sysManageLogTime = sysManageLogTime;
        this.sysManageLogOperateType = sysManageLogOperateType;
        this.sysManageLogCount = sysManageLogCount;
        this.sysManageLogQueryUserCondition = sysManageLogQueryUserCondition;
        this.sysManageLogQuerySql = sysManageLogQuerySql;
        this.sysManageLogUrl = sysManageLogUrl;
        this.sysManageLogFile = sysManageLogFile;
        this.sysManageLogAuthFile = sysManageLogAuthFile;
        this.sysManageLogResult = sysManageLogResult;
    }
    public Integer getSysManageLogId() {
        return sysManageLogId;
    }

    public void setSysManageLogId(Integer sysManageLogId) {
        this.sysManageLogId = sysManageLogId;
    }

    public String getSysManageLogOrgName() {
        return sysManageLogOrgName;
    }

    public void setSysManageLogOrgName(String sysManageLogOrgName) {
        this.sysManageLogOrgName = sysManageLogOrgName;
    }

    public String getSysManageLogUserName() {
        return sysManageLogUserName;
    }

    public void setSysManageLogUserName(String sysManageLogUserName) {
        this.sysManageLogUserName = sysManageLogUserName;
    }

    public String getSysManageLogRoleName() {
        return sysManageLogRoleName;
    }

    public void setSysManageLogRoleName(String sysManageLogRoleName) {
        this.sysManageLogRoleName = sysManageLogRoleName;
    }

    public String getSysManageLogMenuName() {
        return sysManageLogMenuName;
    }

    public void setSysManageLogMenuName(String sysManageLogMenuName) {
        this.sysManageLogMenuName = sysManageLogMenuName;
    }

    public String getSysManageLogEnterpriseCode() {
        return sysManageLogEnterpriseCode;
    }

    public void setSysManageLogEnterpriseCode(String sysManageLogEnterpriseCode) {
        this.sysManageLogEnterpriseCode = sysManageLogEnterpriseCode;
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

    public String getSysManageLogOldValue() {
        return sysManageLogOldValue;
    }

    public void setSysManageLogOldValue(String sysManageLogOldValue) {
        this.sysManageLogOldValue = sysManageLogOldValue;
    }

    public String getSysManageLogNewValue() {
        return sysManageLogNewValue;
    }

    public void setSysManageLogNewValue(String sysManageLogNewValue) {
        this.sysManageLogNewValue = sysManageLogNewValue;
    }

    public Date getSysManageLogTime() {
        return sysManageLogTime;
    }

    public void setSysManageLogTime(Date sysManageLogTime) {
        this.sysManageLogTime = sysManageLogTime;
    }

   
    public Integer getSysManageLogOperateType() {
        return sysManageLogOperateType;
    }

    public void setSysManageLogOperateType(Integer sysManageLogOperateTye) {
        this.sysManageLogOperateType = sysManageLogOperateTye;
    }

    public String getSysManageLogAuthFile() {
        return sysManageLogAuthFile;
    }

    public void setSysManageLogAuthFile(String sysManageLogAuthFile) {
        this.sysManageLogAuthFile = sysManageLogAuthFile;
    }

    public boolean getSysManageLogResult() {
        return sysManageLogResult;
    }

    public void setSysManageLogResult(boolean sysManageLogResult) {
        this.sysManageLogResult = sysManageLogResult;
    }

    @Override
    public String toString() {
        return "SysManageLog{" +
                "sysManageLogId=" + sysManageLogId +
                ", sysManageLogOrgName='" + sysManageLogOrgName + '\'' +
                ", sysManageLogUserName='" + sysManageLogUserName + '\'' +
                ", sysManageLogRoleName='" + sysManageLogRoleName + '\'' +
                ", sysManageLogMenuName='" + sysManageLogMenuName + '\'' +
                ", sysManageLogEnterpriseCode=" + sysManageLogEnterpriseCode +
                ", indexId=" + indexId +
                ", indexItemId=" + indexItemId +
                ", sysManageLogOldValue='" + sysManageLogOldValue + '\'' +
                ", sysManageLogNewValue='" + sysManageLogNewValue + '\'' +
                ", sysManageLogTime=" + sysManageLogTime +
                ", sysManageLogDate=" + 
                ", sysManageLogObject='" + sysManageLogOperateType + '\'' +
                ", sysManageLogAuthFile='" + sysManageLogAuthFile + '\'' +
                ", sysManageLogResult=" + sysManageLogResult +
                '}';
    }

	public SysManageLog(Integer sysOrgId, String sysManageLogOrgName, String sysManageLogUserName,
			String sysManageLogRoleName, String sysManageLogMenuName,
			Date sysManageLogTime, Integer sysManageLogOperateType,
			boolean sysManageLogResult,  String sysManageLogQueryUserCondition
			) {
		super();
		this.sysOrgId = sysOrgId;
		this.sysManageLogOrgName = sysManageLogOrgName;
		this.sysManageLogUserName = sysManageLogUserName;
		this.sysManageLogRoleName = sysManageLogRoleName;
		this.sysManageLogMenuName = sysManageLogMenuName;
		this.sysManageLogTime = sysManageLogTime;
		this.sysManageLogOperateType = sysManageLogOperateType;
		this.sysManageLogResult = sysManageLogResult;
		this.sysManageLogQueryUserCondition = sysManageLogQueryUserCondition;
	}
    
    
}
