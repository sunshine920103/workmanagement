package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysUser;
import com.workmanagement.util.PageSupport;

public interface SysManageLogDao {
	
	List<SysManageLog> queryAdminLogTbs(Map<String, Object> param);

	void insertSysManageLogTb(SysManageLog sys);
	List<SysMenu> queryMenu();

	List<SysManageLog> querySql(Map<String, Object> map);
}
