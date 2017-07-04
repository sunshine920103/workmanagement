package com.workmanagement.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.workmanagement.model.MyUserDetails;

public class HandleLog {
//	@Autowired
//	static SysUserTimeDao sysUserTimeDao;
//	public static void addHandleLog(String name){
//		
//		MyUserDetails ud = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication()
//				.getPrincipal();
//		
//		//插入时间
//		SysUserTime sy = new SysUserTime();
//		sy.setSYS_USER_ID(ud.getSys_user_id());
//		sy.setSYS_USER_TIME_BEGIN(new Date());
//		sy.setSYS_USER_TIME_IP(ud.getSys_user_login_ip());
//		sy.setSYS_USER_TIME_MENU(name);
//		sysUserTimeDao.insertSysUserTime(sy);
//	}
}
