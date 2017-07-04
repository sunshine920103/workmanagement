package com.workmanagement.service;

import java.util.List;

import com.workmanagement.model.SysClassFyModel;
import com.workmanagement.util.PageSupport;

public interface SysClassFyService {
	/*  查询所有数据
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysClassFyService#queryAllSysClassFy()
	 */
	List<SysClassFyModel> queryAllSysClassFy(PageSupport ps);
	/**
	 * 通过id查询类型码
	 */
	SysClassFyModel queryCodeById(Integer hyid);
	/**
	 * 添加数据
	 */
	void insertExchenge(SysClassFyModel sysClassFyModel);

	/**
	 * 根据行业名字查询
	 */
	SysClassFyModel queryByName(String sysIndustryName);
	/**
	 * 根据行业名字查询
	 */
	List<SysClassFyModel> queryListByName(String sysIndustryName);
	
	/**
	 * 通过种类查询所有数据
	 */
	SysClassFyModel isSysIndNameByAll(String name);
	/**
	 * 通过行业编码查询查询有没有关联数据
	 * @param sysIndustryCode
	 * @return
	 */
	List<SysClassFyModel> queryClassFyModelByCode(String sysIndustryCode,Integer hyid);
	/**
	 * 通过id删除数据
	 * @param hyid
	 */
	void delClassFyById(Integer hyid);
	/**
	 * 通过id查询行业数据
	 * @param id
	 * @return
	 */
	SysClassFyModel queryAllCodeById(Integer id);
	/*
	 * 根据行业编码查询父行业
	 */
	SysClassFyModel queryModelByCode(String newCode);
	List<SysClassFyModel> queryAllSysClassFy1();
	/**
	 * 修改数据
	 * @param sys
	 */
	void updateExchenge(SysClassFyModel sys);
	
	List<SysClassFyModel> isSysIndNameByAll1(String sysclassName,PageSupport ps);
	Integer isSysIndustryName(String sysIndustryName);
	/**
	 * 通过编码模糊查询数据
	 * @param name
	 * @param ps
	 * @return
	 */
	List<SysClassFyModel> queryModelByCode1(String name, PageSupport ps);

}
