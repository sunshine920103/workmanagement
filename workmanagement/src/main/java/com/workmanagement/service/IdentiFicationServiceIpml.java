package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.IdentiFicationDao;
import com.workmanagement.model.IdentiFication;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

@Service
public class IdentiFicationServiceIpml implements IdentiFicationService {

	@Autowired
	private IdentiFicationDao identiFicationDao;
	@Override
	public List<IdentiFication> queryIdentiFicationByAll(Map<String, Object> map, PageSupport ps) {
		if(ps!=null){
	        PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
	        return PageHelperSupport.queryCount(identiFicationDao.queryIdentiFicationByAll(map), ps);
		}else{

	        return identiFicationDao.queryIdentiFicationByAll(map);
		}
	}
	@Override
	public void updateIdentiFication(IdentiFication iden) {
		// TODO Auto-generated method stub
		identiFicationDao.updateIdentiFication(iden);
	}
	@Override
	public void insertIdentiFication(IdentiFication iden) {
		// TODO Auto-generated method stub
		identiFicationDao.insertIdentiFication(iden);
	}
	@Override
	public void deleteIdentiFication(Integer id) {
		// TODO Auto-generated method stub
		identiFicationDao.deleteIdentiFication(id);
	}

}
