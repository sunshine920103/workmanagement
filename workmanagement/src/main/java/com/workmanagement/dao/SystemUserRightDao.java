/**
 * 
 */
package com.workmanagement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysPathRole;
import com.workmanagement.model.SysRole;

/**
 * @author lzl
 * 
 */
public interface SystemUserRightDao {
	public MyUserDetails queryUserDetailsByUsername(@Param(value = "username") String username);

	public List<SysRole> queryUserRole(@Param(value = "user_id") Integer user_id);

	public List<SysPathRole> queryAllPathRole();

	public List<SysMenu> queryUserMenusByRoleIds(@Param(value = "roleIds") List<Integer> roleIds);
}
