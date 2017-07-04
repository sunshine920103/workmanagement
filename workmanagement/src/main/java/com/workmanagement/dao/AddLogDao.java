package com.workmanagement.dao;

import java.util.Map;

import com.workmanagement.model.AddLog;

public interface AddLogDao {
	AddLog queryValue(Map<String, Object> map);

}
