package com.workmanagement.controller;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.service.CompanyListFilterService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOtherManageService;
import com.workmanagement.util.SettingUtils;

@Controller
@RequestMapping("/admin/creditScoreQuery")
public class creditScoreQueryController {
	

	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	CompanyListFilterService companyListFilterService;

	@Autowired
	private SysOtherManageService sysOtherManageService;
	
	@RequestMapping("/list")
	public String list(Model model,String cre,String org,String code, HttpServletRequest request) throws Exception{
		MyUserDetails us = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		model.addAttribute("cre", cre);
		model.addAttribute("org", org);
		SysOtherManage s;
		try {
			s = sysOtherManageService.querySysOtherManage(us.getSys_user_id());
			model.addAttribute("pass", s.getSysSetOrgSwitch());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(code)){
			SysArea sy = sysAreaService
					.queryAreaById(sysOrgService.getByIdNotHaveSub(us.getSys_org_id()).getSys_area_id());
			model.addAttribute("code", sy.getSysAreaCode());
		
		}else
			model.addAttribute("code", code);
		List<Map<String, Object>> list = companyListFilterService.queryPing();
		model.addAttribute("type", list);
	
		    model.addAttribute("url", "http://"+request.getServerName()+SettingUtils.getCommonSetting("out.http.url"));
		return "creditScoreQuery/list";
	}
}
