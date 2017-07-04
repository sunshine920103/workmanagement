package com.workmanagement.service;

import java.sql.Clob;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmanagement.dao.AddLogDao;
import com.workmanagement.model.AddLog;

@Service
public class AddLogServiceImpl implements AddLogService {
	@Autowired
	AddLogDao addLogDao;
	@Override
	public AddLog queryValue(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return addLogDao.queryValue(map);
	}

}
