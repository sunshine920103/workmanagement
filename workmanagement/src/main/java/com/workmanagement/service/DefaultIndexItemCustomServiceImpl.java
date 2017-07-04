package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.DefaultIndexItemCustomDao;
import com.workmanagement.model.DefaultIndexItemCustom;
import com.workmanagement.model.Dic;
import com.workmanagement.util.BaseDaoSupport;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

@Service
public class DefaultIndexItemCustomServiceImpl extends BaseDaoSupport implements DefaultIndexItemCustomService {

	@Autowired
	DefaultIndexItemCustomDao defaultIndexItemCustomDao;
	@Override
	public List<DefaultIndexItemCustom> queryDefaultIndexItemCustoms(Map<String, Object> param, PageSupport ps) {
		if(ps!=null){
		  PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
	        return PageHelperSupport.queryCount(defaultIndexItemCustomDao.queryDefaultIndexItemCustoms(param), ps);
		}else{
			 return defaultIndexItemCustomDao.queryDefaultIndexItemCustoms(param);
		}
	}
	

}
