package com.workmanagement.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.workmanagement.model.CreditReportQuery;

public interface CompanyListFilterService {
	/**
	 * 查询信用报告的
	 */
	List<Map<String, Object>> queryType();
	
/**
 * 查询信用评分
 */
	List<Map<String, Object>> queryPing();
	
	
	void insert(CreditReportQuery crq);
	

	void updateCreditReportQuery(String key,String time);
	

	Integer queryNum(Map<String, Object> map);
}
