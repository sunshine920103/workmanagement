package com.workmanagement.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

/**
 * 最核心的代码就是invoke方法中的InterceptorStatusToken token =
 * super.beforeInvocation(fi);这一句
 * ，即在执行doFilter之前，进行权限的检查，而具体的实现已经交给accessDecisionManager了，下文中会讲述。
 * 
 * @author 
 * 
 */
@Service("myFilterSecurityInterceptor")
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

	private FilterInvocationSecurityMetadataSource securityMetadataSource;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);

		HttpServletRequest req = (HttpServletRequest) request;
		req.getSession().removeAttribute("errNum");
		try {
			invoke(fi);
		} catch (DataAccessException e){
			req.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", "连接超时，请重新登录");
			invoke(fi);
		}catch (Exception e) {
			req.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", e.getMessage());
			invoke(fi);
		}
	}

	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public Class<? extends Object> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	public void invoke(FilterInvocation fi) throws IOException, ServletException {
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource newSource) {
		this.securityMetadataSource = newSource;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
