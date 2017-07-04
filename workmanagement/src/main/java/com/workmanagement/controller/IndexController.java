/**
 * 
 */
package com.workmanagement.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.model.SysUser;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOtherManageService;
import com.workmanagement.service.SysUserLogService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.DateFormatter;

/**
 * @author lzl
 * 控制进入的页面
 */
@Controller
@RequestMapping(value = "/admin")
public class IndexController {
		
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private SysOtherManageService sysOtherManageService;
	@Autowired
	SysAreaService sysAreaService;
	@Autowired
	SysUserLogService sysUserLogService;
	
	@RequestMapping(value = "/index", method = { RequestMethod.POST, RequestMethod.GET })
	public String index(HttpServletRequest req) {
		try{
			MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			SysUser su = sysUserService.querySystemUserById(userDetails.getSys_user_id());
			
			//判断是否在允许登录的时间
			Date currDate = new Date();
			
			// 超管不用判断
			if (!su.getSys_user_role_name().equals("超级管理员")) {	
				SysOtherManage s = sysOtherManageService.querySysOtherManage(su.getSys_user_id());
				// 判断是否在允许登录的时间段
				if (!DateFormatter.isInDate(currDate, s.getSysSetStime(), s.getSysSetEtime())) {
					throw new UsernameNotFoundException("登录失败，当前时间段不允许登录");
				}
				if(su.getSys_delete().intValue()==0){
					throw new UsernameNotFoundException("用户已被停用，请联系管理员");
				}
				// 判断是否超期登录
				Date lastLoginTime = su.getSys_user_login_time();
				if (lastLoginTime != null) {
					Date overdue = DateFormatter.getLaterDay(lastLoginTime, (long) s.getSysSetLoginOverdue());
					if (DateFormatter.startThanEnd(currDate, overdue)) {
						// 设置用户的状态为禁用
						SysUser user = new SysUser();
						su.setSys_user_id(su.getSys_user_id());
						su.setEnabled(false);
						sysUserService.updateSystemUser(user);
						throw new UsernameNotFoundException("用户已被禁用，请联系管理员");
					}
				}
			}
			try {
				SysUserLog log = new SysUserLog();
				log.setSysUserLogUserName(su.getUsername());
				log.setSysUserLogOperateType(SysUserLogService.LOGIN);
				log.setSysUserLogMenuName("系统登录");
				log.setSysUserLogResult(true);
				sysUserLogService.insertOneLog(log,req);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//设置session
			req.getSession().setAttribute("sessionUser", userDetails);
		}catch(UsernameNotFoundException unfe){
			req.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", unfe.getMessage());
			return "redirect:/login.jhtml";
		}catch(Exception e){
			return "redirect: loginout.jhtml";
		}
		return "frame/main";
	}
	
	@RequestMapping(value = "/tab", method = { RequestMethod.GET })
	public String tab(HttpServletRequest req) {
		return "frame/tab";
	}
	
	@RequestMapping(value = "/top", method = { RequestMethod.GET })
	public String top(HttpServletRequest req, Model model) {
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		if(userDetails!=null){
			SysUser su = sysUserService.querySystemUserById(userDetails.getSys_user_id());
			SysOrg so = sysOrgService.queryInstitutionsById(su.getSys_org_id());
			model.addAttribute("org", so);
			model.addAttribute("user", su);
		}
		return "frame/top";
	}
	
	@RequestMapping(value = "/left", method = { RequestMethod.GET })
	public String left(HttpServletRequest req) {
		return "frame/left";
	}
	
	@RequestMapping(value = "/welcome", method = { RequestMethod.GET })
	public String welcome(HttpServletRequest req) {
		return "myPanel/myPanel";
	}
	
	@RequestMapping(value = "/loginout", method = { RequestMethod.GET })
	public String loginout(HttpServletRequest req) {
		req.getSession().invalidate();
		return "redirect:/login.jhtml";
	}
}
