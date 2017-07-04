package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysRole;

public interface SysRoleDao {
	
	List<SysRole> querySystemRoles(Map<String, Object> param);

	SysRole querySystemRoleById(@Param("id") Integer id);

	void saveSystemRole(SysRole sr);

	void updateSystemRole(SysRole sr);

	void insertSystemRole(SysRole sr);

	List<SysMenu> querySystemMenus(@Param("rid") Integer rid);

	List<SysMenu> querySubSystemMenuByRoleId(@Param("pid") Integer pid, @Param("rid") Integer rid);

	String queryRoleDuties(@Param("menuIds") List<Integer> list);

	void delMenuOfSystemRole(@Param("id") Integer id);

	void insertMenuOfSystemRole(@Param("id") Integer id, @Param("menuIds") List<Integer> menuIds);

	int delRoleById(@Param("id") Integer id);

	SysRole querySystemRoleByName(@Param("name") String name);
	
	String querySysUserId(@Param("uid") Integer uid);

	List<SysMenu> querySystemMenusName(@Param("name") String name);

	List<SysMenu> queryAllMenu(@Param("id") Integer id);

	List<SysMenu> queryParentAllMenu(@Param("name") String name);

	String getRoleIdByType(@Param("id") Integer id);

	int querySystemRoleByName1(Integer id);

	SysMenu queryMenuByMenuId(Integer integer);

	List<SysMenu> queryMenuNameByRoleId(Integer sys_role_id);

	List<SysMenu> queryMenuByMenuIdByParentId(@Param("rid")Integer rid,@Param("sysMenuId")Integer sysMenuId);

	List<SysMenu> querySystemMenus1(@Param("rid")Integer id);
	/**
	 * 通过地区区分角色
	 * @return
	 */
	List<SysRole> queryAllByArea(@Param("param") Map<String,Object> param);
}
