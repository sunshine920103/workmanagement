package com.workmanagement.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.SysOrgDao;
import com.workmanagement.dao.SysRoleDao;
import com.workmanagement.dao.SystemUserDao;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysRole;
import com.workmanagement.model.SysUser;
import com.workmanagement.util.BaseDaoSupport;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;

@Service
public class SysUserServiceImpl extends BaseDaoSupport implements SysUserService {

	@Autowired
	private SystemUserDao sysUserDao;
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysOrgDao instiutionsDao;
	@Autowired
	private SysOrgDao sysOrgDao;
	@Autowired
	private SysManageLogService sysManageLogService;
	

	@Override
	public List<SysUser> querySystemUsers(Map<String, Object> param, PageSupport ps) {

		if(ps!=null){
	        PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
	        return PageHelperSupport.queryCount(sysUserDao.querySystemUsers(param), ps);
		}else{

	        return sysUserDao.querySystemUsers(param);
		}
	}
	@Override
	public List<SysUser> queryAll() {
		// TODO Auto-generated method stub
		return sysUserDao.queryAll();
	}
	@Override
	public SysUser querySystemUserById(Integer id) {
		return sysUserDao.querySystemUserById(id);
	}

	@Override
	public void saveSystemUser(SysUser su, Integer role,HttpServletRequest request) {
		if(su.getSys_user_id()!=null){
			SysUser user = sysUserDao.querySystemUserById(su.getSys_user_id());
			
			//判断用户是否修改所在机构
			if(user.getSys_org_id().intValue()!=su.getSys_org_id().intValue()){
				
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("insti_id", user.getSys_org_id());
				List<SysUser> sus = querySystemUsers(param, null);
				
				//判断被修改的用户所在机构下是否还有用户，如果没有则把机构的使用状态改为未使用
				if(CollectionUtils.isEmpty(sus)){
					SysOrg so = sysOrgDao.queryInstitutionsById(su.getSys_org_id());
					so.setSys_org_used(false);
					instiutionsDao.updateInstitutions(so);
				}
			}
			
			//判断是否修改角色
			if(user.getSys_role_id().intValue()!=su.getSys_role_id().intValue()){
				
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("method", 2);
				param.put("role_id", user.getSys_role_id());
				List<SysUser> sus = querySystemUsers(param, null);
				//判断被删除的用户角色是否还有用户使用， 如果没有则把角色的使用状态改为未使用
				if(CollectionUtils.isEmpty(sus)){
					SysRole sr = new SysRole();
					sr.setSys_role_id(user.getSys_role_id());
					sysRoleDao.updateSystemRole(sr);
				}
			}
			
			
			//删除用户以前的角色
			sysUserDao.delSystemUserRole(su.getSys_user_id());
			//重新设置用户的禁用状态， 默认一直都是true
			su.setEnabled(true);
			//更新用户
			sysUserDao.updateSystemUser(su);

			sysManageLogService.insertSysManageLogTb(new SysManageLog("用户管理", su.getUsername(), null, null,user.toString(),su.toString(),new Date(),SysManageLog.UPDATE_SYSMANAGElOG,null,"修改用户信息",null,null,null,null,true),request);
			//清空改用户缓存
			clear(su.getSys_user_id());
		}else{
			PasswordEncoder spe = new BCryptPasswordEncoder();
			su.setUsername(su.getUsername());
			su.setPassword(spe.encode(SysUser.DEFUALT_PWD));
			//保存用户
			sysUserDao.insertSystemUser(su);
			sysManageLogService.insertSysManageLogTb(new SysManageLog("用户管理", su.getUsername(), null, null,null,su.toString(),new Date(),SysManageLog.INSERT_SYSMANAGElOG,null,"添加用户信息",null,null,null,null,true),request);			
		}
		
		//保存用户的角色
		sysUserDao.insertSystemRole(su.getSys_user_id(), role);
		
		
		//将机构更新为已使用状态
		SysOrg i = sysOrgDao.queryInstitutionsById(su.getSys_org_id());
		i.setSys_org_used(true);
		instiutionsDao.updateInstitutions(i);
	}

	@Override
	public void delUserById(Integer id) {
		sysUserDao.delUserById(id);
		clear(id);
	}

	@Override
	public void updateSystemUser(SysUser su) {
		sysUserDao.updateSystemUser(su);
		clear(su.getSys_user_id());
	}

	@Override
	public SysUser querySystemUserByCodeAndName(String code, String name) {
		return sysUserDao.querySystemUserByCodeAndName(code, name);
	}
	
	/**
	 * 清空缓存
	 */
	private void clear(Integer userId) {
		String pattern = RedisKeys.SYS_USER + "*" + userId;
		RedisUtil.delBatchData(pattern);
	}
	
	@Override
	public void insertUsername(SysUser systemUser) {
		// TODO Auto-generated method stub
		sysUserDao.insertSystemUser(systemUser);
	}
	@Override
	public List<SysUser> querySql(Map<String, Object> map, PageSupport ps) {
		// TODO Auto-generated method stub
		if(ps!=null){
	        PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
	        return PageHelperSupport.queryCount(sysUserDao.querySql(map), ps);
		}else{
	        return sysUserDao.querySql(map);
		}
	}
	@Override
	public List<SysUser> queryCard(String card) {
		// TODO Auto-generated method stub
		return sysUserDao.queryCard(card);
	}
}
