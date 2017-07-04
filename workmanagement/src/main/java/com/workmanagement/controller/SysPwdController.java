package com.workmanagement.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.model.SysUser;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOtherManageService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.LoggerUtil;

@Controller
@RequestMapping("/admin/sysPwd")
public class SysPwdController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysOtherManageService sysOtherManageService;
	@Autowired
	SysOrgService sysOrgService;
	@Autowired
	SysManageLogService sysManageLogService;
	private static final String SYS_MENU = "密码管理";

	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping("/changePassword")
	public String index(Model model, HttpServletRequest request) {
		MyUserDetails us = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			SysOtherManage s = sysOtherManageService.querySysOtherManage(us.getSys_user_id());
				
			model.addAttribute("pass", s.getSysSetQwdRule());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "sysPwd/changePassword";

	}

	/**
	 * 修改密码
	 * 
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	@RequestMapping("/modify_pwd")
	@ResponseBody
	public JsonResWrapper modify_pwd(HttpServletRequest request, @RequestParam(required = false) String oldPwd,
			@RequestParam(required = false) String newPwd, @RequestParam(required = false) String rePwd) {
		JsonResWrapper jrw = new JsonResWrapper();
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		try {
			if (StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd)) {
				jrw.setFlag(false);
				jrw.setMessage("修改失败，参数缺失");
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null,null,null,new Date(),SysManageLog.UPDATE_SYSMANAGElOG,null,"修改密码",null,null,null,null,false),request);
				
				return jrw;
			}
			if (userDetails == null) {
				jrw.setFlag(false);
				jrw.setMessage("登录超时，请重新登录后修改");
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null,null,null,new Date(),SysManageLog.UPDATE_SYSMANAGElOG,null,"修改密码",null,null,null,null,false),request);
				
				return jrw;
			}
			if (!newPwd.equals(rePwd)) {
				jrw.setFlag(false);
				jrw.setMessage("修改失敗，确认密码和新密码不一致");
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null,null,null,new Date(),SysManageLog.UPDATE_SYSMANAGElOG,null,"修改密码",null,null,null,null,false),request);
				
				return jrw;
			}

			SysUser sysUser = sysUserService.querySystemUserById(userDetails.getSys_user_id());

			PasswordEncoder spe = new BCryptPasswordEncoder();
			if (spe.matches(oldPwd, sysUser.getPassword())) {
				SysUser su = new SysUser();
				su.setSys_user_id(sysUser.getSys_user_id());
				su.setPassword(spe.encode(newPwd));
				su.setEnabled(userDetails.isEnabled());
				sysUserService.updateSystemUser(su);
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null,null,null,new Date(),SysManageLog.UPDATE_SYSMANAGElOG,null,"修改密码",null,null,null,null,true),request);
				
			} else {

				jrw.setFlag(false);
				jrw.setMessage("修改失败，原密码错误");
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null,null,null,new Date(),SysManageLog.UPDATE_SYSMANAGElOG,null,"修改密码",null,null,null,null,false),request);
				
				return jrw;
			}

			jrw.setMessage("修改成功");
		} catch (Exception e) {
			LoggerUtil.error(e);

		}

		return jrw;
	}
}
