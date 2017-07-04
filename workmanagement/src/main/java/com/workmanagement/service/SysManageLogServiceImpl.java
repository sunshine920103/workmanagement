package com.workmanagement.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.SysManageLogDao;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysUser;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.util.NetworkUtil;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
@Service
public class SysManageLogServiceImpl implements SysManageLogService {
	@Autowired
	SysManageLogDao sysManageLogDao;
	@Autowired
	SysUserService sysUserService;

	@Override
	public List<SysManageLog> queryAdminLogTbs(Map<String, Object> param, PageSupport ps) {
		
		if(ps!=null){
	        PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
	        return PageHelperSupport.queryCount(sysManageLogDao.queryAdminLogTbs(param),ps);
		}else{

	        return sysManageLogDao.queryAdminLogTbs(param);
		}
	}
	@Override
	public void insertSysManageLogTb(SysManageLog sys,HttpServletRequest request) {
		try {
			sys.setSysManageLogIp(NetworkUtil.getIpAddress(request));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isBlank(sys.getSysManageLogUserName())){
			// TODO Auto-generated method stub
			MyUserDetails us = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			SysUser user =sysUserService.querySystemUserById(us.getSys_user_id());
			sys.setSysOrgId(user.getSys_org_id());
			sys.setSysManageLogOrgName(user.getSys_user_org_name());
			sys.setSysManageLogRoleName(user.getSys_user_role_name());
			if(sys.getSysManageLogTime()==null){
				sys.setSysManageLogTime(new Date());
			}
			sys.setSysManageLogUserName(user.getUsername());
		}
		sysManageLogDao.insertSysManageLogTb(sys);
	}

	@Override
	public List<SysMenu> queryMenu() {
		// TODO Auto-generated method stub
		return sysManageLogDao.queryMenu();
	}
	@Override
	public List<SysManageLog> querySql(Map<String, Object> map,PageSupport ps) {
		// TODO Auto-generated method stub
		if(ps!=null){
	        PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
	        return PageHelperSupport.queryCount(sysManageLogDao.querySql(map),ps);
		}else{

	        return sysManageLogDao.queryAdminLogTbs(map);
		}
	}
	
	
	
}
