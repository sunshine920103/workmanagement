package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import com.workmanagement.model.ComPanyShow;
import com.workmanagement.util.PageSupport;

public interface ComPanyShowService {
	public	List<ComPanyShow> queryComPanyShow(Map<String, Object> map,PageSupport ps);
	
	public void insertComPanyShow(ComPanyShow com);
	
	public void updateComPanyShow(ComPanyShow com);

	List<Map<String, Object>> queryAll(Map<String, Object> map);
	

	Map<String, Object> querySql(String sql);
}
