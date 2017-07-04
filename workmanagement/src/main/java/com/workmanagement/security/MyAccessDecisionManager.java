package com.workmanagement.security;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysRole;
import com.workmanagement.model.SysUser;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysRoleService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;

/**
 * 在这个类中，最重要的是decide方法，如果不存在对该资源的定义，直接放行；否则，如果找到正确的角色，即认为拥有权限，并放行，否则throw new
 * AccessDeniedException("no right");这样，就会进入上面提到的403.jsp页面。
 * 
 * @author shilong.zhang
 * 
 */
public class MyAccessDecisionManager implements AccessDecisionManager {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysOrgService sysOrgService;

	// In this method, need to compare authentication with configAttributes.
	// 1, A object is a URL, a filter was find permission configuration by this
	// URL, and pass to here.
	// 2, Check authentication has attribute in permission configuration
	// (configAttributes)
	// 3, If not match corresponding authentication, throw a
	// AccessDeniedException.
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		if (configAttributes == null) {
			return;
		}
		
			// 判断用户的状态/角色/机构是否改变
			
		
//		catch (Exception e) {// session过期时；MyUserDetails ud = (MyUserDetails)
//								// authentication.getPrincipal(); 这句代码会报错
//			throw new UsernameNotFoundException("用户信息过期，请重新登录");
////			throw new UsernameNotFoundException("0");
//		}

		Object use = authentication.getPrincipal();
		System.out.println(use.toString());
if(use instanceof MyUserDetails){
	try{	
		MyUserDetails ud = (MyUserDetails) use;
		SysUser su = sysUserService.querySystemUserById(ud.getSys_user_id());
			
			String roleKey = RedisKeys.SYS_ROLE + su.getSys_user_id();
			String orgKey = RedisKeys.SYS_USER_ORG + su.getSys_user_id();
			if (!su.isEnabled()) {
				throw new UsernameNotFoundException("用户已被禁用，请联系管理员");
			}
			
			if (su.getSys_org_id().intValue() != ud.getSys_org_id().intValue()) {
				RedisUtil.delBatchData(orgKey);
				throw new UsernameNotFoundException("所在机构已被变更，请重新登陆");
			}
			if (su.getSys_role_id().intValue() != ud.getRoleIds().get(0).intValue()) {
				RedisUtil.delBatchData(roleKey);
				throw new UsernameNotFoundException("用户角色已被变更，请重新登陆");
			}
		
			// 判断用户的角色权限是否被改变
			SysRole suSr = sysRoleService.querySystemRoleById(su.getSys_role_id());
			SysRole udSr = ud.getSysRole();
			if (suSr == null || udSr == null || StringUtils.isBlank(suSr.getSys_role_duties())
					|| StringUtils.isBlank(udSr.getSys_role_duties())) {
				throw new UsernameNotFoundException("用户角色异常，请联系管理员");
			}
			if (!udSr.getSys_role_duties().equals(suSr.getSys_role_duties())) {
				RedisUtil.delBatchData(roleKey);
				throw new UsernameNotFoundException("用户角色权限已被变更，请重新登陆");
			}
		
			// 判断用户所在机构的职责区域、所在区域、上级机构、机构类别、机构名称是否被改变
			SysOrg suSo = sysOrgService.queryInstitutionsById(su.getSys_org_id());
			SysOrg udSo = ud.getSysOrg();
			if (suSo == null || udSo == null) {
				throw new UsernameNotFoundException("用户机构异常，请联系管理员");
			}
		
			// 机构名称
			if (!suSo.getSys_org_name().equals(udSo.getSys_org_name())) {
				RedisUtil.delBatchData(orgKey);
				throw new UsernameNotFoundException("用户机构信息已被变更，请重新登陆");
			}
		
			
		
			// 上级机构
			if (suSo.getSys_org_upid() != null && udSo.getSys_org_upid() != null) {
				if (suSo.getSys_org_upid().intValue() != udSo.getSys_org_upid().intValue()) {
					RedisUtil.delBatchData(orgKey);
					throw new UsernameNotFoundException("用户机构信息已被变更，请重新登陆");
				}
			} else {
				if (suSo.getSys_org_upid() != udSo.getSys_org_upid()) {
					RedisUtil.delBatchData(orgKey);
					throw new UsernameNotFoundException("用户机构信息已被变更，请重新登陆");
				}
			}
		
			// 所在区域、机构类别
			if (suSo.getSys_area_id().intValue() != udSo.getSys_area_id().intValue()
					|| suSo.getSys_org_type_id().intValue() != udSo.getSys_org_type_id().intValue()) {
				RedisUtil.delBatchData(orgKey);
				throw new UsernameNotFoundException("用户机构信息已被变更，请重新登陆");
			}
		
		} catch (UsernameNotFoundException unfe){
			throw unfe;
		}
		}else{
			throw new UsernameNotFoundException("用户信息过期，请重新登录");
			
		}
			
}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
