package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.SysMenu;

public interface SysMenuDao {
	List<SysMenu> queryMenu(Map<String, Object> map);
	void updateMenu(SysMenu menu);
	void insertMenu(SysMenu menu);
	void deleteMenu(@Param("menuId")Integer menuId);
	SysMenu queryMenuName(String name);
	void insertRoleMenu(@Param("rid")Integer rid,@Param("mid")Integer mid);
	
}
