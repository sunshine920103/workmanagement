package com.workmanagement.dao;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.SysOtherManage;

/**
 * 其他管理Dao
 * @author tianhao
 *
 */
public interface SysOtherManageDao {

	/**
	 * 查询其他管理
	 * @return
	 * @throws Exception
	 */
	SysOtherManage querySysOtherManage(@Param("aid") Integer aid) throws Exception;
	
	/**
	 * 修改其他管理
	 * @param stm
	 * @throws Exception
	 */
	void updateSysOtherManage(SysOtherManage stm) throws Exception;
	
	/**
	 * 添加其他管理
	 * @param stm
	 * @throws Exception
	 */
	void insertSysOtherManage(SysOtherManage stm) throws Exception;
	
	/**
	 * 修改次数限制-月数
	 * @param monthLimit
	 */
	void updateMonthLimit(@Param("monthLimit") Integer monthLimit) throws Exception;
}
