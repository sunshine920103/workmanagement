/**
 * 
 */
package com.workmanagement.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.workmanagement.model.SysOtherManage;
import com.workmanagement.model.SysUser;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOtherManageService;
import com.workmanagement.service.SysUserLogService;
import com.workmanagement.service.SysUserService;

/**
 * @author Administrator
 * 
 */
public class LoginFailureHandler implements AuthenticationFailureHandler, InitializingBean {
	protected Log logger = LogFactory.getLog(getClass());

	private String defaultTargetUrl;
	private boolean forwardToDestination = false;
	private boolean allowSessionCreation = true;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Autowired
	private SysUserLogService sysUserLogService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysOtherManageService sysOtherManageService;
	@Autowired
	SysAreaService sysAreaService;
	@Autowired
	SysOrgService sysOrgService;

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

	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}

	public void setForwardToDestination(boolean forwardToDestination) {
		this.forwardToDestination = forwardToDestination;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		saveException(request, exception);
		if (this.forwardToDestination) {
			logger.info("Login failure,Forwarding to " + this.defaultTargetUrl);
			request.getRequestDispatcher(this.defaultTargetUrl).forward(request, response);
		} else {
			logger.info("Login failure,Redirecting to " + this.defaultTargetUrl);
			this.redirectStrategy.sendRedirect(request, response, this.defaultTargetUrl);
		}
	}

	protected final void saveException(HttpServletRequest request, AuthenticationException exception)
			throws IOException {
		String j_username = request
				.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);

		SysUser user = (SysUser) sysUserService.querySystemUserByCodeAndName(j_username, j_username);
		if (forwardToDestination) {
			user.setSys_user_error(0);
			sysUserService.updateSystemUser(user);
		
			request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception.getMessage());
			request.setAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, j_username);
		} else {
			if (user != null) {
				try {
					SysUserLog log = new SysUserLog();
					log.setSysUserLogUserName(user.getUsername());
					log.setSysUserLogOperateType(SysUserLogService.LOGIN);
					log.setSysUserLogMenuName("系统登录");
					log.setSysUserLogResult(false);
					sysUserLogService.insertOneLog(log,request);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					try {
						SysOtherManage s = sysOtherManageService.querySysOtherManage(user.getSys_user_id());
						if(user.getSys_user_error() < s.getSysSetLoginNum()){
							user.setSys_user_error(user.getSys_user_error()+1);
							request.getSession().setAttribute("errNum", "错误次数:"+user.getSys_user_error()+"次,还剩:"+(s.getSysSetLoginNum()-user.getSys_user_error())+"次");
						}else
							user.setEnabled(false);
						sysUserService.updateSystemUser(user);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			
			HttpSession session = request.getSession(false);

			if (session != null || allowSessionCreation) {
				request.getSession().setAttribute(
						UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, j_username);
				request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception.getMessage());
			}
		}
	}

	protected boolean isAllowSessionCreation() {
		return allowSessionCreation;
	}

	public void setAllowSessionCreation(boolean allowSessionCreation) {
		this.allowSessionCreation = allowSessionCreation;
	}
}
