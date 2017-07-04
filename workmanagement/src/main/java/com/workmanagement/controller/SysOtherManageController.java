package com.workmanagement.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOtherManageService;

/**
 * 其他管理
 * @author tianhao
 *
 */
@Controller
@RequestMapping(value="/admin/sysOtherManage")
public class SysOtherManageController {
	
	@Autowired
	private SysOtherManageService sysOtherManageService;

	@Autowired
	private SysOrgService sysOrgService;
	
	@Autowired
	private SysManageLogService sysManageLogService;
	
	/**
	 * 其他管理页面
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/index")
	public String list(HttpServletRequest request, Model model) throws Exception{
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		SysOtherManage stm=sysOtherManageService.querySysOtherManage(userDetails.getSys_user_id());
		if(stm!=null){
			String ss=stm.getSysSetStime();
			String se=stm.getSysSetEtime();
			ss=ss.substring(0, 5);
			se=se.substring(0, 5);
			stm.setSysSetStime(ss);
			stm.setSysSetEtime(se);
		}
		model.addAttribute("stm", stm);
		return "sysOtherManage/list";
	}
	
	/**
	 * 修改页面
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/update")
	public String update(HttpServletRequest request, Model model,
			@RequestParam(required=false) Integer id) throws Exception{
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		SysOtherManage stm=sysOtherManageService.querySysOtherManage(userDetails.getSys_user_id());
		if(stm!=null){
			String ss=stm.getSysSetStime();
			String se=stm.getSysSetEtime();
			ss=ss.substring(0, 5);
			se=se.substring(0, 5);
			stm.setSysSetStime(ss);
			stm.setSysSetEtime(se);
		}
		model.addAttribute("roleId", userDetails.getSys_role_id());
		model.addAttribute("stm", stm);
		return "sysOtherManage/add";
	}
	
	/**
	 * 修改
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/updateSys")
	@ResponseBody
	public Map<String,Object> updateSys(HttpServletRequest request,Model model,
			@RequestParam(required=false) Integer sysSetId,
			@RequestParam(required=false) String sysSetStime,
			@RequestParam(required=false) String sysSetEtime,
			@RequestParam(required=false) Integer sysSetLoginOverdue,
//			@RequestParam(required=false) Integer sysSetQueryLimitSwitch,
			@RequestParam(required=false) Integer sysSetLoginNum,
			@RequestParam(required=false) Integer sysSetQwdRule,
			@RequestParam(required=false) Integer sysSetOrgSwitch,
			@RequestParam(required=false) Integer authFileSwitch,
			@RequestParam(required=false) Integer operateAuthFileSwitch,
			@RequestParam(required=false) Integer creditReportAuthFileSwitch,
			@RequestParam(required=false) Integer monthLimit,
			@RequestParam(required=false) String multipleLimit) throws Exception{
		Map<String, Object> map = new HashMap<>();
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		int sysAreaId=so.getSys_area_id();
		String ss=sysSetStime+":00";
		String se=sysSetEtime+":00";
		SysOtherManage stm=new SysOtherManage();
		stm.setSysAreaId(sysAreaId);
		stm.setSysSetId(sysSetId);
		stm.setSysSetStime(ss);
		stm.setSysSetEtime(se);
		stm.setSysSetLoginOverdue(sysSetLoginOverdue);
//		stm.setSysSetQueryLimitSwitch(sysSetQueryLimitSwitch);
		stm.setSysSetLoginNum(sysSetLoginNum);
		stm.setSysSetQwdRule(sysSetQwdRule);
		stm.setSysSetOrgSwitch(sysSetOrgSwitch);
		stm.setAuthFileSwitch(authFileSwitch);
		stm.setOperateAuthFileSwitch(operateAuthFileSwitch);
		stm.setCreditReportAuthFileSwitch(creditReportAuthFileSwitch);
		stm.setMonthLimit(monthLimit);
		double multiple=Double.parseDouble(multipleLimit);
		stm.setMultipleLimit(multiple);
		SysOtherManage other=sysOtherManageService.querySysOtherManageById(1);
		if(sysAreaId!=1){
			if(other!=null){
				String stime=other.getSysSetStime().substring(0, 5);
				String etime=other.getSysSetEtime().substring(0, 5);
				SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
				Date stimes=sdf.parse(stime);
				Date etimes=sdf.parse(etime);
				Date sysSetStimes=sdf.parse(sysSetStime);
				Date sysSetEtimes=sdf.parse(sysSetEtime);
				if (sysSetStimes.getTime() > stimes.getTime()) {
					map.put("msg", "开始时间不能大于四川省的开始时间");
					return map;
	            }
				if(sysSetEtimes.getTime() < etimes.getTime()){
					map.put("msg", "结束时间不能小于四川省的结束时间");
					return map;
				}
				if(sysSetLoginOverdue.intValue()>other.getSysSetLoginOverdue().intValue()){
					map.put("msg", "超期登录限制不能大于四川省的超期登录限制");
					return map;
				}
				if(sysSetLoginNum.intValue()>other.getSysSetLoginNum().intValue()){
					map.put("msg", "登录错误次数限制不能大于四川省的登录错误次数限制");
					return map;
				}
				if(other.getSysSetQwdRule().intValue()==2){
					if(sysSetQwdRule.intValue()!=2 && sysSetQwdRule.intValue()!=3){
						map.put("msg", "请选择字母和数字或者字母、数字和特殊字符");
						return map;
					}
				}
				if(other.getSysSetQwdRule().intValue()==3){
					if(sysSetQwdRule.intValue()!=3){
						map.put("msg", "请选择字母、数字和特殊字符");
						return map;
					}
				}
				if(other.getAuthFileSwitch().intValue()==1){
					if(authFileSwitch.intValue()!=1){
						map.put("msg", "手工修改-授权文件不能关闭");
						return map;
					}
				}
				if(other.getOperateAuthFileSwitch().intValue()==1){
					if(operateAuthFileSwitch.intValue()!=1){
						map.put("msg", "异议处理-授权文件不能关闭");
						return map;
					}
				}
				if(other.getCreditReportAuthFileSwitch().intValue()==1){
					if(creditReportAuthFileSwitch.intValue()!=1){
						map.put("msg", "信用报告-授权文件不能关闭");
						return map;
					}
				}
				if(multiple>other.getMultipleLimit()){
					map.put("msg", "倍数不能大于四川省的倍数");
					return map;
				}
			}
		}
		SysOtherManage som=sysOtherManageService.querySysOtherManageById(sysAreaId);
		if(som!=null){
			sysOtherManageService.updateSysOtherManage(stm);
			if(sysSetId.intValue()==1){
				sysOtherManageService.updateMonthLimit(monthLimit);
			}
			SysManageLog sm=new SysManageLog();
			sm.setSysManageLogMenuName("其他管理");
			sm.setSysManageLogOperateType(SysManageLog.UPDATE_SYSMANAGElOG);
			sm.setSysManageLogResult(true);
			sm.setSysManageLogCount(1);
			sysManageLogService.insertSysManageLogTb(sm, request);
		}else{
			stm.setMonthLimit(other.getMonthLimit());
			sysOtherManageService.insertSysOtherManage(stm);
			SysManageLog sm=new SysManageLog();
			sm.setSysManageLogMenuName("其他管理");
			sm.setSysManageLogOperateType(SysManageLog.INSERT_SYSMANAGElOG);
			sm.setSysManageLogResult(true);
			sm.setSysManageLogCount(1);
			sysManageLogService.insertSysManageLogTb(sm, request);
		}
		map.put("msg", "操作成功");
		return map;
	}
}
