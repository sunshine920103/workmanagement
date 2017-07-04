package com.workmanagement.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmanagement.dao.CompanyListFilterDao;
import com.workmanagement.model.CreditReportQuery;

@Service
public class CompanyListFilterServiceImpl implements CompanyListFilterService {

	
	@Autowired
	CompanyListFilterDao CompanyListFilterDao;
	@Autowired
	private CompanyListFilterDao companyListFilterDao;
	@Override
	public List<Map<String, Object>> queryType() {
		// TODO Auto-generated method stub
		return CompanyListFilterDao.queryType();
	}
	@Override
	public void insert(CreditReportQuery crq) {
		// TODO Auto-generated method stub
		companyListFilterDao.insert(crq);
	}
	@Override
	public void updateCreditReportQuery(String key, String time) {
		// TODO Auto-generated method stub
		companyListFilterDao.updateCreditReportQuery(key, time);
	}
	@Override
	public List<Map<String, Object>> queryPing() {
		// TODO Auto-generated method stub
		return companyListFilterDao.queryPing();
	}
	@Override
	public Integer queryNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return companyListFilterDao.queryNum(map);
	}
	
	
}
