/**
 * 
 */
package com.workmanagement.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.workmanagement.dao.SysOrgDao;
import com.workmanagement.dao.SysRoleDao;
import com.workmanagement.dao.SystemUserDao;
import com.workmanagement.dao.SystemUserRightDao;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysUser;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.NetworkUtil;

/**
 * @author Administrator
 * 
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler, InitializingBean {
	@Autowired
	private SystemUserRightDao systemUserRightDao;
	@Autowired
	private SystemUserDao systemUserDao;
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysOrgDao sysOrgDao;

	protected Log logger = LogFactory.getLog(getClass());

	private String defaultTargetUrl;
	private boolean forwardToDestination = false;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		if (StringUtils.isBlank(defaultTargetUrl))
			throw new BeanInitializationException("You must configure defaultTargetUrl");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 * AuthenticationSuccessHandler
	 * #onAuthenticationSuccess(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		
		MyUserDetails ud = (MyUserDetails) authentication.getPrincipal();
		
		/**
		 * 设置登录用户角色和所在机构
		 */
		ud.setSysRole(sysRoleDao.querySystemRoleById(ud.getRoleIds().get(0)));
		ud.setSysOrg(sysOrgDao.queryInstitutionsById(ud.getSys_org_id()));

		/**
		 * 更新用户的登陆时间和IP
		 */
		SysUser su = new SysUser();
		su.setSys_user_error(0);
		su.setSys_user_id(ud.getSys_user_id());
		su.setEnabled(ud.isEnabled());
		su.setSys_user_last_login_ip(ud.getSys_user_login_ip());
		su.setSys_user_last_login_time(ud.getSys_user_login_time());
		su.setSys_user_login_ip(NetworkUtil.getIpAddress(request));
		su.setSys_user_login_time(new Date());
		systemUserDao.updateSystemUser(su);
		
		List<SysMenu> menus = systemUserRightDao.queryUserMenusByRoleIds(ud.getRoleIds());
		
		/**
		 * 定义一棵菜单树
		 */
		Map<Integer, SysMenu> map = new LinkedHashMap<Integer, SysMenu>();

		/**
		 * 用来保存二级菜单与一级菜单的对应关系，便于通过三级菜单快速查找
		 */
		Map<Integer, Integer> menu12Relation = new HashMap<Integer, Integer>();
		/**
		 * 存储三级菜单列表
		 */
		Map<Integer,SysMenu> menu3 = new HashMap<Integer,SysMenu>();

		for (SysMenu sm : menus) {
				if(sm.getSys_menu_parent_id()==0)
				map.put(sm.getSys_menu_id(), sm);// 一级菜单
		}
		for (SysMenu sysMenu : menus) {
			if (map.containsKey(sysMenu.getSys_menu_parent_id())) {// 为真表示该菜单为二级菜单
				map.get(sysMenu.getSys_menu_parent_id()).getSubMenus().add(sysMenu);
				menu12Relation.put(sysMenu.getSys_menu_id(), sysMenu.getSys_menu_parent_id());
				if(StringUtils.isEmpty(sysMenu.getSys_menu_path())){

					menu3.put(sysMenu.getSys_menu_id(),sysMenu);
					
				}
			}
		}
		
		for (SysMenu Menu : menus) {
			if(menu3.containsKey(Menu.getSys_menu_parent_id())){
				menu3.get(Menu.getSys_menu_parent_id()).getSubMenus().add(Menu);
			}
		}
		// 将菜单树转换为列表，便于前台显示
		List<SysMenu> newMenus = new ArrayList<SysMenu>();
		Iterator<SysMenu> is = map.values().iterator();
		while (is.hasNext()) {
			newMenus.add(is.next());
		}
		Iterator<SysMenu> s = menu3.values().iterator();
		while (s.hasNext()) {
			newMenus.add(s.next());
		}
		
		ud.setMenus(newMenus);
		// systemUserDao.updateLastLoginTimeById(new Date(),
		// ud.getSys_user_id());
		if (this.forwardToDestination) {
			logger.info("Login success,Forwarding to " + this.defaultTargetUrl);
			// 清空错误消息
			request.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			request.removeAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
			// 跳转
			request.getRequestDispatcher(this.defaultTargetUrl).forward(request, response);
		} else {
			logger.info("Login success,Redirecting to " + this.defaultTargetUrl);
			// 清空SESSION中的错误信息
			request.getSession()
					.removeAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
			request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			// 跳转
			this.redirectStrategy.sendRedirect(request, response, this.defaultTargetUrl);
		}
	}

	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}

	public void setForwardToDestination(boolean forwardToDestination) {
		this.forwardToDestination = forwardToDestination;
	}

}
