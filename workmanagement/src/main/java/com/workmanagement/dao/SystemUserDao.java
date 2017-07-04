/**
 * 
 */
package com.workmanagement.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.SysUser;

/**
 * @author lzl
 *
 */

public interface SystemUserDao {
	
	//---------------------------------------------------------------------------------
	//---------------------------------------以前的代码---------------------------------
	//---------------------------------------------------------------------------------
	
	/**
	 * 更新当前登录时间
	 * @param lastLoginTime
	 * @param id
	 */
	public void updateLastLoginTimeById(@Param(value="lastLoginTime") Date lastLoginTime, @Param(value="id") Integer id);
	
	public List<SysUser> queryAll();
	public void insert(SysUser systemUser);
	
	public void insertUsername(SysUser systemUser);
	
	public void update(SysUser systemUser);
	
	public void updateRegInfo(SysUser systemUser);
	
	public void updateEnabled(@Param(value="userId") Integer userId,@Param(value="enabled") Boolean freeze);
	
	public SysUser queryUser(@Param(value="userId") Integer userId);
	
	public SysUser queryUserDetail(@Param(value="userId") Integer userId);
	
	public void updateInvitationCode(@Param(value="code") String code, @Param(value="id") Integer id);
	
	public Integer existInvitationCode(@Param(value="code") String code);
	
	public List<SysUser> queryUsersByPosition(@Param(value="parish") Integer parish, @Param(value="landmark") Integer landmark,@Param(value="street") Integer street);
	
	public SysUser queryUserByUsername(@Param(value="username") String username, @Param(value="invitation_code") String invitation_code);
	
	public void deleteUserById(@Param(value="id") Integer id);
	
	
	
	//---------------------------------------------------------------------------------
	//---------------------------------------现在的代码---------------------------------
	//---------------------------------------------------------------------------------

	public SysUser querySystemUserById(@Param("id") Integer id);

	public void delSystemUserRole(@Param("uid") Integer id);

	public void updateSystemUser(SysUser su);

	public void insertSystemUser(SysUser su);
	List<SysUser> querySystemUsers(Map<String, Object> map);
	public void insertSystemRole(@Param("uid") Integer id, @Param("rid") Integer role);

	public void delUserById(@Param("uid") Integer id);

	public SysUser querySystemUserByCodeAndName(@Param("code") String code, @Param("name") String name);
	
	
	List<SysUser> querySql(Map<String, Object> map);
	List<SysUser> queryCard(String card);
}
