package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.util.PageSupport;

public interface SysManageLogService {

	List<SysManageLog> queryAdminLogTbs(Map<String, Object> param, PageSupport ps);
		void insertSysManageLogTb(SysManageLog sys,HttpServletRequest request);

		List<SysMenu> queryMenu();

		List<SysManageLog> querySql(Map<String, Object> map, PageSupport ps);
}
