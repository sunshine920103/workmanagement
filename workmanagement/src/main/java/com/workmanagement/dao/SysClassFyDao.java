package com.workmanagement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.SysClassFyModel;

public interface SysClassFyDao {
	/*  查询所有数据
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysClassFyService#queryAllSysClassFy()
	 */
	List<SysClassFyModel> queryAllSysClassFy();
	/**
	 * 通过行业id查询行业编码
	 * @param hyid
	 * @return
	 */
	SysClassFyModel queryCodeById(Integer hyid);
	void insertExchenge(SysClassFyModel sysClassFyModel);
	boolean isSysIndustryName();
	int isSysIndustryName(String name);
	SysClassFyModel isSysIndNameByAll(String name);
	/**
	 * 通过行业编码查询查询有没有关联数据
	 * @param sysIndustryCode
	 * @return
	 */
	List<SysClassFyModel> queryClassFyModelByCode(@Param("sysIndustryCode")String sysIndustryCode,@Param("sysIndustryId")Integer hyid);
	/**
	 * 通过id删除数据
	 * @param hyid
	 */
	void delClassFyById(Integer hyid);
	/**
	 * 根据行业名字查询
	 */
	SysClassFyModel queryByName(@Param("sysIndustryName") String sysIndustryName);
	/**
	 * 根据行业名字查询
	 */
	List<SysClassFyModel> queryListByName(@Param("sysIndustryName") String sysIndustryName);
	
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
	List<SysClassFyModel> isSysIndNameByAll1(@Param("sysIndustryName")String sysclassName);
	/**
	 * 通过编码模糊查询行业信息
	 * @param name
	 * @return
	 */
	List<SysClassFyModel> queryModelByCode1(@Param("sysIndustryCode")String name);

}
