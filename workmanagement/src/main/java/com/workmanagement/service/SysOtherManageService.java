package com.workmanagement.service;

import com.workmanagement.model.SysOtherManage;

/**
 * 其他管理Service接口
 * @author tianhao
 *
 */
public interface SysOtherManageService {

	/**
	 * 查询其他管理
	 * @return
	 * @throws Exception
	 */
	SysOtherManage querySysOtherManage(Integer aid) throws Exception;
	
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
	 * 查询其他管理
	 * @return
	 * @throws Exception
	 */
	SysOtherManage querySysOtherManageById(Integer aid) throws Exception;
	
	/**
	 * 修改次数限制-月数
	 * @param monthLimit
	 */
	void updateMonthLimit(Integer monthLimit) throws Exception;
	
	
	SysOtherManage querySysOtherManageAreaId(Integer aid) throws Exception;
}
