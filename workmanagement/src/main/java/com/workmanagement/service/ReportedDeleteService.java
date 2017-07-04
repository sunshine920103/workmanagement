package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import com.workmanagement.model.IndexTb;
import com.workmanagement.util.PageSupport;

/** 
  *
  * @author  作者renyang
  * @date 
  * 
  */

public interface ReportedDeleteService {

	List<IndexTb> queryIndexTb(Map<String, Object> map);

	List<Map<String, Object>> queryResult(Map<String, Object> sqlMap);

	List<Map<String, Object>> getPage(PageSupport ps, String string, Map<String, Object> sqlMap);

	Integer deleteData(Map<String, Object> delMap);

	
}
