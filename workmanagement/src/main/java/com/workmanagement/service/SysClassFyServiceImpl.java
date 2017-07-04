package com.workmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.SysClassFyDao;
import com.workmanagement.model.SysClassFyModel;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

@Service
public class SysClassFyServiceImpl implements SysClassFyService {

	@Autowired
	private SysClassFyDao sysClassFyDao;

	/*  查询所有数据
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysClassFyService#queryAllSysClassFy()
	 */
	@Override
	public List<SysClassFyModel> queryAllSysClassFy(PageSupport ps) {
		if(ps!=null){
			PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			return PageHelperSupport.queryCount(sysClassFyDao.queryAllSysClassFy(),ps);
		}
		return sysClassFyDao.queryAllSysClassFy();
	}
/**
 * 通过id查询类型码
 */
	@Override
	public SysClassFyModel queryCodeById(Integer hyid) {
		return sysClassFyDao.queryCodeById(hyid);
	}
/**
 * 添加数据
 */
	@Override
	public void insertExchenge(SysClassFyModel sysClassFyModel) {
		sysClassFyDao.insertExchenge(sysClassFyModel);
	}
/**
 * 通过种类查询所有数据
 */
	@Override
	public SysClassFyModel isSysIndNameByAll(String name) {
		return sysClassFyDao.isSysIndNameByAll(name);
	}
	/**
	 * 通过行业编码查询查询有没有关联数据
	 * @param sysIndustryCode
	 * @return
	 */
	@Override
	public List<SysClassFyModel> queryClassFyModelByCode(String sysIndustryCode,Integer hyid) {
		// TODO Auto-generated method stub
		return sysClassFyDao.queryClassFyModelByCode(sysIndustryCode,hyid);
	}
	/**
	 * 通过id删除数据
	 * @param hyid
	 */
	@Override
	public void delClassFyById(Integer hyid) {
		// TODO Auto-generated method stub
		sysClassFyDao.delClassFyById(hyid);
	}
	/**
	 * 通过id查询行业数据
	 * @param id
	 * @return
	 */
	@Override
	public SysClassFyModel queryAllCodeById(Integer id) {
		// TODO Auto-generated method stub
		return sysClassFyDao.queryAllCodeById(id);
	}
	/*
	 * 根据行业编码查询父行业
	 */
	@Override
	public SysClassFyModel queryModelByCode(String newCode) {
		// TODO Auto-generated method stub
		return sysClassFyDao.queryModelByCode(newCode);
	}
	@Override
	public List<SysClassFyModel> queryAllSysClassFy1() {
		// TODO Auto-generated method stub
		return sysClassFyDao.queryAllSysClassFy1();
	}
	/**
	 * 修改数据
	 * @param sys
	 */
	@Override
	public void updateExchenge(SysClassFyModel sys) {
		sysClassFyDao.updateExchenge(sys);
	}
	@Override
	public List<SysClassFyModel> isSysIndNameByAll1(String sysclassName,
			PageSupport ps) {
		if(ps==null){
			return sysClassFyDao.isSysIndNameByAll1(sysclassName);
		}
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(sysClassFyDao.isSysIndNameByAll1(sysclassName),ps);
		
	}
	@Override
	public Integer isSysIndustryName(String sysIndustryName) {
		// TODO Auto-generated method stub
		return sysClassFyDao.isSysIndustryName(sysIndustryName);
	}
	@Override
	public SysClassFyModel queryByName(String sysIndustryName) {
		// TODO Auto-generated method stub
		return  sysClassFyDao.queryByName(sysIndustryName);
	}
	@Override
	public List<SysClassFyModel> queryListByName(String sysIndustryName) {
		// TODO Auto-generated method stub
		return  sysClassFyDao.queryListByName(sysIndustryName);
	}
	/**
	 * 通过编码模糊查询行业信息
	 * @param name
	 * @return
	 */
	@Override
	public List<SysClassFyModel> queryModelByCode1(String name, PageSupport ps) {
		// TODO Auto-generated method stub
		if(ps!=null){
			PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			return PageHelperSupport.queryCount(sysClassFyDao.queryModelByCode1(name),ps);
		}else{
			return sysClassFyDao.queryModelByCode1(name);
			
		}
	}

}
