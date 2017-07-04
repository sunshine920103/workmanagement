package com.workmanagement.controller;

import com.workmanagement.model.*;
import com.workmanagement.service.*;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/myPanel")
public class MyPanelController {
    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private SysUserService sysUsreService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private IndexTbService indexTbService;
    @Autowired
    private OrgObjService orgObjService;
    @Autowired
    private SysUserLogService sysUserLogService;
    @Autowired
    private ReportTaskPushListService reportTaskPushListService;

	@Autowired
	DefaultIndexItemService defaultIndexItemService;
	@Autowired
	SysOtherManageService sysOtherManageService;

    /**
     * 我的面板首页
     *
     * @return
     * @throws Exception 
     */
    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request) throws Exception {

        // 当前登录用户的session
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        SysUser su = sysUsreService.querySystemUserById(userDetails.getSys_user_id());
        model.addAttribute("user", su);
        // 获取分页对象
        PageSupport ps = PageSupport.initPageSupport(request);
        Map<String, Object> param = new HashMap<>();


        Map<String, Object> map = new HashMap<>();
        map.put("reportTaskPushOrgId", userDetails.getSys_org_id());

        SysOrg i = sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
        SysUser sysUser = sysUsreService.querySystemUserById(userDetails.getSys_user_id());
        model.addAttribute("j", i.getSys_org_name());
        model.addAttribute("user", sysUser);

        model.addAttribute("menu", sysRoleService.querySysUserId(userDetails.getSys_user_id()));
        // 获取贷款逾期
        Map<String, Object> yhdk = new HashMap<>();
        yhdk.put("org", sysUser.getSys_org_id().toString());
        List<Map<String, Object>> indexYhdkList = indexTbService.queryIndexIndexYhdk(yhdk, null);

        model.addAttribute("indexYhdkList", indexYhdkList);
        model.addAttribute("yhdk", indexYhdkList != null ? indexYhdkList.size() : 0);


        // 获取所有集合
        List<Map<String, Object>> indexSfxxList = DataUtil.isEmpty(indexTbService.queryIndexIndexSfxx(yhdk, null));

        model.addAttribute("indexSfxxList", indexSfxxList);
        model.addAttribute("sfxx", indexSfxxList != null ? indexSfxxList.size() : 0);

        List<Map<String, Object>> indexXzcfxxList = DataUtil
                .isEmpty(indexTbService.queryIndexIndexXzcfxx(yhdk, null));
        model.addAttribute("indexXzcfxxList", indexXzcfxxList);
        model.addAttribute("xzcf", indexXzcfxxList != null ? indexXzcfxxList.size() : 0);

        //根据机构ID获取机构缓存
        String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
        SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
        if (so == null) {
            so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
            RedisUtil.setData(orgKey, so);
        }
        String type = sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
        List<AdminObjModel> sysOperateList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        StringBuffer orgSb = new StringBuffer();
        DataUtil.getChildOrgIds(so, orgSb);
        map1.put("orgIds", orgSb.toString().split(","));
        map1.put("defaultIndexItemId", null);
        map1.put("indexItemId", null);
        map1.put("recordDate", null);
//				if(userDetails.getSys_org_id()==5){
//					map1.put("reporateType", 1);
//					sysOperateList=orgObjService.queryAllSysOperateList(ps);
//				}else{
        map.put("orgIds", userDetails.getSys_org_id());
        map.put("reporateType", 3);
//					map.put("rid",  userDetails.getSys_org_id());
        sysOperateList = orgObjService.querySysOperateList(ps, map);
//					sysOperateList=orgObjService.queryAllSysOperateList1(ps,map1);
//				}
        model.addAttribute("sysOperateList", sysOperateList);
        model.addAttribute("OrgId", userDetails.getSys_org_id());
        model.addAttribute("type", type);
        model.addAttribute("yycl", sysOperateList != null ? sysOperateList.size() : 0);

        /**
         * 用户行为审计
         */
        PageSupport ps1 = PageSupport.initPageSupport(request);
        ps1.setPageSize(5);
        List<SysUserLog> logList = this.getSysUserLog(ps1);
        model.addAttribute("logList", logList);
//        getCountSysUserLog();
        //查询次数
        model.addAttribute("queryCount", i.getSys_org_current_query_times());
        

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
		String newDate = sdf.format(date);
		
		if(i.getSys_org_upid()!=null){
			SysOtherManage s = sysOtherManageService.querySysOtherManageAreaId(i.getSys_org_affiliation_area_id());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(i.getSys_org_time());
			calendar.add(calendar.MONDAY, +s.getMonthLimit());
			java.util.Date resultDate = calendar.getTime(); // 结果
			String oldDate = sdf.format(resultDate);
			if(StringUtils.isNumeric(oldDate) && StringUtils.isNumeric(newDate)){
				if(Integer.parseInt(newDate) >Integer.parseInt(oldDate)){
					 model.addAttribute("rest", i.getSys_org_current_limit_query_times()-i.getSys_org_current_query_times());
				       
				}
			}
			
		}
	
        


        map.put("reportTaskPushOrgId", userDetails.getSys_org_id());
        List<ReportTaskPushList> queryReportTaskPushLists = reportTaskPushListService.queryReportTaskPushLists(map,
                null);
        model.addAttribute("report", queryReportTaskPushLists);
        model.addAttribute("num", queryReportTaskPushLists != null ? queryReportTaskPushLists.size() : 0);

        
        //推送消息
       List<DefaultIndexItemCombine> dce = defaultIndexItemService.queryDefaultIndexItemCombine(userDetails.getSys_org_id(),null);

       Integer dceNum = 0;
		for (DefaultIndexItemCombine defaultIndexItemCombine : dce) {
			if((defaultIndexItemCombine.getCode_credit_orgid().intValue()==userDetails.getSys_org_id().intValue() && defaultIndexItemCombine.getOrg_stuta().intValue() == 1)||(defaultIndexItemCombine.getCode_org_orgid().intValue()==userDetails.getSys_org_id().intValue() && defaultIndexItemCombine.getStuta().intValue() == 1)){
				dceNum++;
			}
		}

	     model.addAttribute("dceNum", dceNum);
	     
	     
		
	     
        return "myPanel/myPanel";

    }
    @RequestMapping("/other")
    public String other(Model model, HttpServletRequest request) {
        PageSupport ps = PageSupport.initPageSupport(request);
        // 当前登录用户的session
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        SysUser sysUser = sysUsreService.querySystemUserById(userDetails.getSys_user_id());
        Map<String, Object> yhdk = new HashMap<>();
        yhdk.put("org", sysUser.getSys_org_id().toString());

        // 获取所有集合
        List<Map<String, Object>> indexXzcfxxList = DataUtil
                .isEmpty(indexTbService.queryIndexIndexXzcfxx(yhdk, null));
        model.addAttribute("indexXzcfxxList", indexXzcfxxList);
        return "myPanel/myPanelXZCF";
    }

    @RequestMapping("/other1")
    public String other1(Model model, HttpServletRequest request) {
        PageSupport ps = PageSupport.initPageSupport(request);
        // 当前登录用户的session
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        SysUser sysUser = sysUsreService.querySystemUserById(userDetails.getSys_user_id());
        Map<String, Object> yhdk = new HashMap<>();
        yhdk.put("org", sysUser.getSys_org_id().toString());

        // 获取所有集合
        List<Map<String, Object>> indexYhdkList = indexTbService.queryIndexIndexYhdk(yhdk, ps);
        model.addAttribute("indexYhdkList", indexYhdkList);
        return "myPanel/myPanelDai";
    }

    @RequestMapping("/other2")
    public String other2(Model model, HttpServletRequest request) {
        PageSupport ps = PageSupport.initPageSupport(request);
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        SysUser sysUser = sysUsreService.querySystemUserById(userDetails.getSys_user_id());
        Map<String, Object> yhdk = new HashMap<>();
        yhdk.put("org", sysUser.getSys_org_id().toString());
        // 司法信息
        List<Map<String, Object>> indexSfxxList = DataUtil.isEmpty(indexTbService.queryIndexIndexSfxx(yhdk, ps));
        model.addAttribute("indexSfxxList", indexSfxxList);
        return "myPanel/myPanelSi";
    }

    /**
     * 更多任务
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/other3")
    public String other3(Model model, HttpServletRequest request) {
        // 当前登录用户的session
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        PageSupport ps = PageSupport.initPageSupport(request);
        Map<String, Object> map = new HashMap<>();
        map.put("reportTaskPushOrgId", userDetails.getSys_org_id());
        List<ReportTaskPushList> queryReportTaskPushLists = reportTaskPushListService.queryReportTaskPushLists(map,
                ps);
        model.addAttribute("report", queryReportTaskPushLists);
        return "myPanel/myPanelPush";
    }

    /**
     * 异议处理信息
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/myPanel")
    public String myPanel(Model model, HttpServletRequest request) {
        //当前登录用户的session
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //根据机构ID获取机构缓存
        String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
        SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
        PageSupport ps = PageSupport.initPageSupport(request);
        if (so == null) {
            so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
            RedisUtil.setData(orgKey, so);
        }
        List<AdminObjModel> sysOperateList = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer orgSb = new StringBuffer();
        DataUtil.getChildOrgIds(so, orgSb);
        map.put("orgIds", orgSb.toString().split(","));
        map.put("defaultIndexItemId", null);
        map.put("indexItemId", null);
        map.put("recordDate", null);
        if (userDetails.getSys_org_id() == 5) {
            map.put("reporateType", 1);
            sysOperateList = orgObjService.querySysOperateList(ps, map);
        } else {
            map.put("id", userDetails.getSys_org_id());
            map.put("rid", userDetails.getSys_org_id());
            sysOperateList = orgObjService.queryAllSysOperateList1(ps, map);
        }
        model.addAttribute("sysOperateList", sysOperateList);
        model.addAttribute("OrgId", userDetails.getSys_org_id());
        model.addAttribute("yycl", sysOperateList != null ? sysOperateList.size() : 0);
        return "myPanel/myPanelyy";
    }

    @RequestMapping("/moreSysInfo")
    public ModelAndView moreSysInfo(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("myPanel/moreSysInfo");
        /**
         * 用户行为审计
         */
        PageSupport ps = PageSupport.initPageSupport(request);
        List<SysUserLog> logList = this.getSysUserLog(ps);
        view.addObject("logList", logList);
        return view;
    }

    @RequestMapping("/getQueryData")
    public ModelAndView getQueryData(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("myPanel/getQueryData");
        /**
         * 用户行为审计
         */
        PageSupport ps = PageSupport.initPageSupport(request);
        List<SysUserLog> logList = this.getQueryData(ps);
        view.addObject("logList", logList);
        return view;
    }

    private Integer getCountSysUserLog() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userType = userDetails.getSysRole().getSys_role_type();
        String logTime = DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM");
        if (userType == 1 || userType == 4) {
            //如果是管理员
            try {
                return sysUserLogService.getCountOfThisOrgQueryNum(logTime, userDetails.getSys_org_id(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                return sysUserLogService.getCountOfThisOrgQueryNum(logTime, userDetails.getSys_org_id(), userDetails.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<SysUserLog> getQueryData(PageSupport ps) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userType = userDetails.getSysRole().getSys_role_type();
        String logTime = DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM");
        List<SysUserLog> logList = null;
        if (userType == 1 || userType == 4) {
            //如果是管理员
            try {
                logList = sysUserLogService.getThisOrgQueryData(ps, logTime, userDetails.getSys_org_id(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                logList = sysUserLogService.getThisOrgQueryData(ps, logTime, userDetails.getSys_org_id(), userDetails.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logList;
    }

    private List<SysUserLog> getSysUserLog(PageSupport ps) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userType = userDetails.getSysRole().getSys_role_type();
        List<SysUserLog> logList = null;
        if (userType == 1 || userType == 4) {
            try {
                logList = sysUserLogService.getByOrgId(ps, userDetails.getSys_org_id());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                logList = sysUserLogService.getByUserName(ps, userDetails.getSys_org_id(), userDetails.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logList;
    }

    /**
     * 二码确认
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/comPanyShow")
    public String comPanyShow(Model model, HttpServletRequest request) {
        // 当前登录用户的session
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        PageSupport ps = PageSupport.initPageSupport(request);
        List<DefaultIndexItemCombine> dce = defaultIndexItemService.queryDefaultIndexItemCombine(userDetails.getSys_org_id(),ps);
       
        for (DefaultIndexItemCombine defaultIndexItemCombine : dce) {
			if(defaultIndexItemCombine.getCode_credit_orgid().intValue()==userDetails.getSys_org_id().intValue() && defaultIndexItemCombine.getOrg_stuta().intValue() == 1){
				defaultIndexItemCombine.setOrg_stuta(2);
				defaultIndexItemService.updateDefaultIndexItemCombine(defaultIndexItemCombine);
			}
			if(defaultIndexItemCombine.getCode_org_orgid().intValue()==userDetails.getSys_org_id().intValue() && defaultIndexItemCombine.getStuta().intValue() == 1){
				defaultIndexItemCombine.setStuta(2);
				defaultIndexItemService.updateDefaultIndexItemCombine(defaultIndexItemCombine);
			}
		}
        model.addAttribute("DefaultIndexItemCombine", dce);
     
        return "myPanel/comPanyShow";
    }
    
    
    @RequestMapping("/getPwd")
    @ResponseBody
    public boolean getPwd(){
    	 MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
                 .getPrincipal();

    	PasswordEncoder spe = new BCryptPasswordEncoder();
		boolean bo = spe.matches( "66666666",userDetails.getPassword());
		return bo;
    }
}
