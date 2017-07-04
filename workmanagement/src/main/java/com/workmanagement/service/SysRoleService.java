package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysRole;
import com.workmanagement.util.PageSupport;

public interface SysRoleService {

	/**
	 * 查询角色列表
	 * @param param 
	 * @return
	 */
	List<SysRole> querySystemRoles(Map<String, Object> param);

	/**
	 * 通过ID查询角色
	 * @param id
	 * @return
	 */
	SysRole querySystemRoleById(Integer id);

	/**
	 * 保存角色
	 * @param sr
	 */
	void saveSystemRole(SysRole sr);

	/**
	 * 查询角色相对应的菜单
	 * @param pid
	 * @param rid 
	 * @return
	 */
	List<SysMenu> querySubSystemMenuByRoleId(Integer pid, Integer rid);

	/**
	 * 查询所有菜单
	 * @param rid 角色ID
	 * @return
	 */
	List<SysMenu> querySystemMenus(Integer rid);

	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	int delRoleById(Integer id);

	/**
	 * 通过名称查询角色
	 * @param name
	 * @return
	 */
	SysRole querySystemRoleByName(String name);

	/**
	 * 更新角色
	 * @param sr
	 */
	void updateSysRole(SysRole sr);
	

	String querySysUserId(@Param("uid") Integer uid);

	List<SysMenu> querySystemMenusName(String sys_menu_name);

	List<SysMenu> queryAllMenu(Integer integer);

	List<SysMenu> queryParentAllMenu(String name);

	String getRoleIdByType(Integer id);

	int querySystemRoleByName1(Integer id);

	void delMenuOfSystemRole(Integer id);
	/**
	 * 通过菜单对应id查询对应的菜单
	 * @param integer
	 * @return
	 */
	SysMenu queryMenuByMenuId(Integer integer);
	/**
	 * 通过角色id查询对应的菜单
	 * @param sys_role_id
	 * @return
	 */
	List<SysMenu> queryMenuNameByRoleId(Integer sys_role_id);
	/**
	 * 通过菜单id查询关联的菜单
	 * @param menuId
	 * @return
	 */
	List<SysMenu> queryMenuByMenuIdByParentId(Integer rid,Integer menuId);
	/**
	 * 查询所有菜单，没有条件限制
	 * @param id
	 * @return
	 */
	List<SysMenu> querySystemMenus1(Integer id);
/**
 * 通过地区筛选角色权限
 * @return
 */
	List<SysRole> queryAllByArea(PageSupport ps,Map<String,Object> param);

}
