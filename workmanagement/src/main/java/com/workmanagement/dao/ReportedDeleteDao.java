package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.IndexTb;

/** 
  *
  * @author  作者 renyang
  * @date 
  * 
  */
public interface ReportedDeleteDao {

	List<IndexTb> queryIndexTb(@Param("map") Map<String, Object> map);

	List<Map<String, Object>> queryResult(Map<String, Object> sqlMap);

	Integer deleteData(Map<String, Object> delMap);
	
}
