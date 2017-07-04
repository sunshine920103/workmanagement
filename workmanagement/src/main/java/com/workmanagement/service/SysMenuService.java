package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.SysMenu;
import com.workmanagement.util.PageSupport;

public interface SysMenuService {

	List<SysMenu> queryMenu(Map<String, Object> map,PageSupport ps);

	void updateMenu(SysMenu menu);
	void insertMenu(SysMenu menu);

	void deleteMenu(Integer menuId);
	

	SysMenu queryMenuName(String name);
	

	void insertRoleMenu(Integer rid,Integer mid);
}
