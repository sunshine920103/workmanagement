package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.SysImportentEnterpriseGroupDao;
import com.workmanagement.model.SysImportentEnterpriseGroup;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

@Service
public class SysImportentEnterpriseGroupServiceImpl implements SysImportentEnterpriseGroupService {

	@Autowired
	private SysImportentEnterpriseGroupDao	sysImportentEnterpriseGroupDao;
	@Override
	public List<SysImportentEnterpriseGroup> querySieg(Map<String, Object> map, PageSupport ps) {
		
		if(ps!=null){
	        PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
	        return PageHelperSupport.queryCount(sysImportentEnterpriseGroupDao.querySieg(map),ps);
		}else{
	        return sysImportentEnterpriseGroupDao.querySieg(map);
		}
	}
	@Override
	public void insertGroup(SysImportentEnterpriseGroup s) {
		sysImportentEnterpriseGroupDao.insertGroup(s);
	}
	@Override
	public void insertOneOfGroup(Integer groupId, Integer defaultId) {
		sysImportentEnterpriseGroupDao.insertOneOfGroup(groupId, defaultId);
	}
	@Override
	public SysImportentEnterpriseGroup qureyByName(String name) {
		return sysImportentEnterpriseGroupDao.qureyByName(name);
	}
	
}
