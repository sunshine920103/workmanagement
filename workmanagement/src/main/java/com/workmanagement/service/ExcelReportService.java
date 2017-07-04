package com.workmanagement.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.springframework.web.multipart.MultipartFile;

import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.ReportExcelTemplate;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysOrg;

/**
 * 
 * @author wqs
 *
 */
public interface ExcelReportService {
	
	/**
	 * 报送基本信息保存二码
	 */
	public void saveDafults(int rowCount,String tempTbName,List<String> errorList,SysArea a,
			List<Map<String, Object>> qymcMsg);
		
	/**
	 * 存进临时表
	 */
	String  getExcelToTemp(ReportExcelTemplate rt,List<IndexItemTb> itemsList,SysArea a,int[] nums,Date submit,String tempTbName,Map<String,String> columnMap,List<Map<String, Object>> qymcMsg, MultipartFile file,
			List<String> indexNames,List<String> errorList,IndexTb it,Date reportDate,
			SysOrg so,List<String> uniques,List<Map<String, Object>> repeat,List<Integer> reNum,String[] isSuccess);
	
	public void tocheck(int rowCount,List<IndexItemTb> itemsList,SysArea a,StringBuffer areaIds,Date submit,String tempTbName,List<Map<String, Object>> qymcMsg,
			List<String> indexNames, List<String> errorList, IndexTb it, Date reportDate, SysOrg so,
			List<String> uniques, List<Map<String, Object>> repeat, List<Integer> reNum,String[] isSuccess);
		
	/**
	 * 校验excel文件中的统一社会信用代码或是组织机构代码是否有重复
	 */
	public void checkCreditUnique(String tempTbName, List<String> errorList);
	
	/**
	 * 校验excel文件中的统一社会信用代码或是组织机构代码是否有重复
	 */
	public void checkOrgUnique(String tempTbName, List<String> errorList);
	
	
	/**
	 * 组装sql
	 */
	void insertSqlAndReport(SysArea a,StringBuffer areaIds,Map<String,String> columnMap,Date submit,String tbName,String tempTbName,List<String>indexNames,SysOrg so,
			IndexTb it,String time,List<String> uniques,String[] isSuccess,List<String> updateList);
	
	/**
	 * 创建临时表
	 * @throws Exception 
	 */
	void insertTempTable(ReportExcelTemplate rt,IndexTb it,String tempTbName) throws Exception;
	
	/**
	 * 创建临时表前先判断是否存在
	 */
	Integer isTempTableExists(String tmepTbName);
	/**
	 * 获取临时表中的数据条数
	 */
	Integer getDataNum(String  tempTbName);
}
