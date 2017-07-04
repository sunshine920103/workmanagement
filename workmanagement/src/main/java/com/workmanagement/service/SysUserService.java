package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.workmanagement.model.SysUser;
import com.workmanagement.util.PageSupport;

public interface SysUserService {

	
	/**
	 * 查询用户列表
	 * @param param
	 * @param ps
	 * @return
	 */
	List<SysUser> querySystemUsers(Map<String, Object> param, PageSupport ps);

	 List<SysUser> queryAll();
	/**
	 * 通过ID查询用户
	 * @param id
	 * @return
	 */
	SysUser querySystemUserById(Integer id);

	/**
	 * 保存用户
	 * @param su
	 * @param role
	 */
	void saveSystemUser(SysUser su, Integer role,HttpServletRequest re);

	/**
	 * 删除用户
	 * @param id
	 */
	void delUserById(Integer id);

	/**
	 * 更新用户
	 * @param su
	 */
	void updateSystemUser(SysUser su);

	/**
	 * 通过用户代码和名称查询用户
	 * @param code
	 * @param name
	 * @return
	 */
	 SysUser querySystemUserByCodeAndName(String code, String name);

	public void insertUsername(SysUser systemUser);
	
	
/**
 * 执行管理员查询的sql
 * @param map
 * @return
 */
	public List<SysUser> querySql(Map<String, Object> map,PageSupport ps);

	List<SysUser> queryCard(String card);

}
