package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.ComPanyShow;

public interface ComPanyShowDao {
	List<ComPanyShow> queryComPanyShow(Map<String, Object> map);
	
	void insertComPanyShow(ComPanyShow com);
	void updateComPanyShow(ComPanyShow com);
	List<Map<String, Object>> queryAll(Map<String, Object> map);
	Map<String, Object> querySql(@Param("sql")String sql);
}
