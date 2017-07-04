/**
 * 
 */
package com.workmanagement.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author lzl
 * 
 */
public class MyUserDetails extends SysUser implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4478522253428798545L;
	/**
	 * Spring框架所需的认证参数
	 */
	private Collection<GrantedAuthority> authorities;

	/**
	 * 用来存储登录用户的角色ID列表
	 */
	private List<Integer> roleIds;

	/**
	 * 用来存储登录用户登录后所要显示的所有菜单
	 */
	private List<SysMenu> menus;

	private Map<Integer, Integer> rightMap;

	public MyUserDetails() {
		super();
	}

	public MyUserDetails(String username, String password, Collection<GrantedAuthority> authorities) {
		super();
		this.setUsername(username);
		this.setPassword(password);
		this.authorities = authorities;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public Map<Integer, Integer> getRightMap() {
		return rightMap;
	}

	public void setRightMap(Map<Integer, Integer> rightMap) {
		this.rightMap = rightMap;
	}

	public List<SysMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<SysMenu> menus) {
		this.menus = menus;
	}

	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getAuthorities
	 * ()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
