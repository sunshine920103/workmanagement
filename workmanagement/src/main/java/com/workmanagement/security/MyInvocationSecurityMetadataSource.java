package com.workmanagement.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.workmanagement.dao.SystemUserRightDao;
import com.workmanagement.model.SysPathRole;

/**
 * 此类静态加载所有的url与角色
 * 
 * @author shilong.zhang
 * 
 */
@Service("myInvocationSecurityMetadataSource")
public class MyInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private SystemUserRightDao systemUserRightDao;

	private static Map<String, Collection<ConfigAttribute>> rightMap = new HashMap<String, Collection<ConfigAttribute>>();

	// According to a URL, Find out permission configuration of this URL.
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		loadResourceDefine();
		if (rightMap != null && rightMap.size() > 0) {
			String path = ((FilterInvocation) object).getRequest().getServletPath();
			return rightMap.get(path);
		}
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	// 加载所有资源与权限的关系
	public void loadResourceDefine() {
		rightMap.clear();
		List<SysPathRole> resources = systemUserRightDao.queryAllPathRole();
		if (!CollectionUtils.isEmpty(resources)) {
			for (int i = 0; i < 1; i++) {
				Collection<ConfigAttribute> configAttributes;
				if (rightMap.containsKey(resources.get(0).getSys_menu_path())) {
					configAttributes = rightMap.get(resources.get(0).getSys_menu_path());
				} else {
					configAttributes = new ArrayList<ConfigAttribute>();
				}

				ConfigAttribute configAttribute = new SecurityConfig("ROLE_" + resources.get(0).getSys_role_id());
				configAttributes.add(configAttribute);

				rightMap.put(resources.get(0).getSys_menu_path(), configAttributes);
			}
			
		}

	}
}
