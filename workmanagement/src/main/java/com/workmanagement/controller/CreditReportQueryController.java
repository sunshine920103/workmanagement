package com.workmanagement.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.workmanagement.dao.CompanyListFilterDao;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.CreditReportQuery;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.service.CompanyListFilterService;
import com.workmanagement.service.DicContentService;
import com.workmanagement.service.DicService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOtherManageService;
import com.workmanagement.util.UpLoadFile;
/**
 *信用报告
 * @author lzl
 *
 */
@Controller
@RequestMapping("/admin/creditReportQuery")
public class CreditReportQueryController {
	
	@Autowired
	CompanyListFilterService companyListFilterService;
	@Autowired
	private SysOtherManageService sysOtherManageService;
	@Autowired
	private DicContentService dicContentService;
	@Autowired
	private DicService dicService;

	
	
	@RequestMapping("/list")
	public String index(Model model,String cre,String org,String qymc,String code) throws Exception{
		MyUserDetails us = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		model.addAttribute("cre", cre);
		model.addAttribute("org", org);
		model.addAttribute("qymc", qymc);
		SysOtherManage s;
		try {
			s = sysOtherManageService.querySysOtherManage(us.getSys_user_id());
			model.addAttribute("pass", s.getSysSetOrgSwitch());
			model.addAttribute("switch", s.getAuthFileSwitch());
			model.addAttribute("cred", s.getCreditReportAuthFileSwitch());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(code)){
			SysArea sy = sysAreaService
					.queryAreaById(sysOrgService.getByIdNotHaveSub(us.getSys_org_id()).getSys_area_id());
			model.addAttribute("code", sy.getSysAreaCode());
		
		}else
			model.addAttribute("code", code);
		List<Map<String, Object>> list = companyListFilterService.queryType();
		model.addAttribute("type", list);
		List<DicContent> dic =dicContentService.getDicContentsByDicId(dicService.getDicByDicName("查询原因",sysAreaService.queryAreaByCode("510000").getSysAreaId()).getDicId());
		model.addAttribute("dic", dic);
		return "creditReportQuery/list";
	}
	
	@RequestMapping("/search")
	public String search(Model model,
			@RequestParam(required=false) String creditCode,
			@RequestParam(required=false) String orgCode,
			@RequestParam(required=false) String industryName,
			@RequestParam(required=false) String time,
			@RequestParam(required=false) String t,
			@RequestParam(required=false) String code,
			@RequestParam(required=false) Integer reason,
			@RequestParam(required=false) MultipartFile file,
			HttpServletRequest request 
			) throws Exception{
		MyUserDetails us = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CreditReportQuery  crq =new CreditReportQuery();
		crq.setCreditCode(creditCode);
		crq.setOrgCode(orgCode);
		crq.setEnterpriseName(industryName);
		Date date =new Date();
//		 String c = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date);
		 crq.setOperateTime(date);
		crq.setDate(time);
		
		crq.setCreditReportType(t);
		crq.setQueryReason(reason);
		crq.setOrgId(us.getSys_org_id());
		if(file != null){

			if(!file.isEmpty()){			
				String filePath =UpLoadFile.upLoadFile(file);
				crq.setAuthFile(filePath);
			}
		}
		crq.setOperator(us.getUsername());
		
		companyListFilterService.insert(crq);
		
		
        long ts = date.getTime();
        String res = String.valueOf(ts);

		model.addAttribute("querytime", res);
		model.addAttribute("creditCode", creditCode);
		model.addAttribute("orgCode", orgCode);
		model.addAttribute("industryName", industryName);
		model.addAttribute("time", time);
		model.addAttribute("t", t);


		SysOrg so = sysOrgService.getByIdNotHaveSub(us.getSys_org_id());
		model.addAttribute("code", code);
		
		if(so.getSys_org_upid()!=null){
			so.setSys_org_current_query_times(so.getSys_org_current_query_times()+1);
			sysOrgService.saveInstitutions(so);
		}
		
		return "creditReportQuery/list";
	}

	@RequestMapping("/getLimit")
	@ResponseBody
	public String getLimit(Integer id) throws Exception{
		MyUserDetails us = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		SysOtherManage s = sysOtherManageService.querySysOtherManage(us.getSys_user_id());
		Integer num = s.getMonthLimit();
			SysOrg so = sysOrgService.getByIdNotHaveSub(id);
				if(so.getSys_org_upid()!=null){
				if(so.getSys_org_status().intValue() == 1){
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(so.getSys_org_time());
					calendar.add(calendar.MONDAY, +num);
					java.util.Date resultDate = calendar.getTime(); // 结果
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String oldDate = sdf.format(resultDate);
					Date date = new Date();
					String newDate = sdf.format(date);
					if(StringUtils.isNumeric(oldDate) && StringUtils.isNumeric(newDate)){
						if(Integer.parseInt(newDate) >Integer.parseInt(oldDate)){
							if(so.getSys_org_current_query_times().intValue() >= so.getSys_org_current_limit_query_times().intValue())
								return "本月信用报告查询次数已超过限制次数，请联系管理员";
						}
					}
				}else{
				
					return "信用报告查询功能已被锁定，请联系管理员";
				}
				
			}
		return "";
	}
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysOrgService sysOrgService;
	
}
