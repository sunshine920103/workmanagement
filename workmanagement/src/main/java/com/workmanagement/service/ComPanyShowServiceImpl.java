package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.ComPanyShowDao;
import com.workmanagement.model.ComPanyShow;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

@Service
public class ComPanyShowServiceImpl implements ComPanyShowService {

	@Autowired
	ComPanyShowDao comPanyShowDao;
	@Override
	public List<ComPanyShow> queryComPanyShow(Map<String, Object> map, PageSupport ps) {
		// TODO Auto-generated method stub
		if(ps!=null){
	        PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
	        return PageHelperSupport.queryCount(comPanyShowDao.queryComPanyShow(map),ps);
		}else{

	        return comPanyShowDao.queryComPanyShow(map);
		}
	}

	@Override
	public void insertComPanyShow(ComPanyShow com) {
		// TODO Auto-generated method stub
		comPanyShowDao.insertComPanyShow(com);
	}

	@Override
	public void updateComPanyShow(ComPanyShow com) {
		// TODO Auto-generated method stub
		comPanyShowDao.updateComPanyShow(com);
	}

	@Override
	public List<Map<String, Object>> queryAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return comPanyShowDao.queryAll(map);
	}

	@Override
	public Map<String, Object> querySql(String sql) {
		// TODO Auto-generated method stub
		return comPanyShowDao.querySql(sql);
	}

}
