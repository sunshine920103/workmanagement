package com.workmanagement.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.CreditReportQuery;

public interface CompanyListFilterDao {
	List<Map<String, Object>> queryType();

	List<Map<String, Object>> queryPing();
	
	/**
	 * 查询信用报告的
	 */
	void insert(CreditReportQuery crq);
	
	
	void updateCreditReportQuery(@Param("key")String key,@Param("time")String time);
	
	
	Integer queryNum(Map<String, Object> map);
}
