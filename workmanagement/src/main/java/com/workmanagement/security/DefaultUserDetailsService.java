/**
 * 
 */
package com.workmanagement.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.workmanagement.dao.SystemUserRightDao;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysRole;

/**
 * @author lzl
 *
 */
public class DefaultUserDetailsService implements UserDetailsService {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private SystemUserRightDao systemUserRightDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MyUserDetails su = systemUserRightDao.queryUserDetailsByUsername(username);
		
		try {
			
			if (su != null) {
				
				List<Integer> roleIds = new ArrayList<Integer>();
				su.setAuthorities(getAuthorities(su.getSys_user_id(), roleIds));
				su.setRoleIds(roleIds);
			} else {
				throw new UsernameNotFoundException("用户名或密码错误");
			}
			
		} catch (Exception e) {
			logger.error("系统登录错误 # " + e.getMessage());
			throw new UsernameNotFoundException("系统错误，请联系管理员");
		}
		return su;
	}

	/**
	 * 获得访问角色权限,这是一个list集合,即一个admin可对应多个角色
	 * 
	 * @param id
	 * @return
	 */
	public Collection<GrantedAuthority> getAuthorities(Integer id, List<Integer> roleIds) {
		List<SysRole> roles = systemUserRightDao.queryUserRole(id);
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		if (roles != null && roles.size() != 0) {
			for (SysRole role : roles) {
				authList.add(new SimpleGrantedAuthority("ROLE_" + role.getSys_role_id()));
				roleIds.add(role.getSys_role_id());
			}
		}
		
		return authList;
	}
}
