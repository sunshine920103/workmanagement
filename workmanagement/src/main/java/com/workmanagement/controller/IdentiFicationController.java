package com.workmanagement.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.ComPanyShow;
import com.workmanagement.model.IdentiFication;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysRole;
import com.workmanagement.model.SysUser;
import com.workmanagement.service.ComPanyShowService;
import com.workmanagement.service.IdentiFicationService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.DateFormatter;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.PageSupport;

@Controller
@RequestMapping("/admin/identification")
public class IdentiFicationController {

	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private IdentiFicationService identiFicationService;
	final String SYS_MENU="标识管理";
	
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request,String begin,String end){
		
		// 当前登录用户的session
				MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				
				SysArea sy = sysAreaService
						.queryAreaById(sysOrgService.queryInstitutionsById(userDetails.getSys_org_id()).getSys_area_id());
				while (!sy.getSysAreaType().equals("0") && !sy.getSysAreaType().equals("1")) {
					sy = sysAreaService.queryAreaById(sy.getSysAreaUpid());
				}
				Map<String, Object> map = new HashMap<>();
				if(!sy.getSysAreaName().equals("四川省")){
					List<Integer> list = sysAreaService.getAllSubAreaIds(sy.getSysAreaId());
					list.add(1);

					map.put("areaIds", list);
					map.put("orgId", userDetails.getSys_org_id());
				}
				if(!StringUtils.isBlank(begin))
				{
					model.addAttribute("begin", begin);
					map.put("begin", begin);
				}
				if(!StringUtils.isBlank(end)){
					map.put("end", end);
					model.addAttribute("end", end);
				}
				PageSupport ps = PageSupport.initPageSupport(request);
				
				List<IdentiFication> iden = identiFicationService.queryIdentiFicationByAll(map, ps);
				
				model.addAttribute("identiFication", iden);
		return "identification/list";
	}
	@Autowired
	SysManageLogService sysManageLogService;
	
	@RequestMapping(value = "/reportExcle")
	@ResponseBody
	public JsonResWrapper excl(HttpServletRequest request, HttpServletResponse response,
			String begin,String end)
			throws Exception {
		JsonResWrapper jrw = new JsonResWrapper();
		
		// 当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		
		SysArea sy = sysAreaService
				.queryAreaById(sysOrgService.queryInstitutionsById(userDetails.getSys_org_id()).getSys_area_id());
		while (!sy.getSysAreaType().equals("0") && !sy.getSysAreaType().equals("1")) {
			sy = sysAreaService.queryAreaById(sy.getSysAreaUpid());
		}
		Map<String, Object> map = new HashMap<>();
		if(!sy.getSysAreaName().equals("四川省")){
			List<Integer> list = sysAreaService.getAllSubAreaIds(sy.getSysAreaId());
			list.add(1);

			map.put("areaIds", list);
			map.put("orgId", userDetails.getSys_org_id());
		}
		if(!StringUtils.isBlank(begin))
		{
			map.put("begin", begin);
		}
		if(!StringUtils.isBlank(end)){
			map.put("end", end);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		
		List<IdentiFication> iden = identiFicationService.queryIdentiFicationByAll(map, null);
		
		String[] rowNames = { "表示名", "创建人", "创建时间"};
		String[] propertyNames = { "sys_identification_name", "sys_creat_username", "sys_creat_time"};

		try {
			// 生成excel
			ExcelExport<IdentiFication> excelExport = new ExcelExport<>();
			excelExport.setTitle("标识管理");
			excelExport.setRowNames(rowNames);
			excelExport.setPropertyNames(propertyNames);
			excelExport.setList(iden);
			String url = excelExport.exportExcel(request, response);
			sysManageLogService
					.insertSysManageLogTb(
							new SysManageLog(SYS_MENU, null, null, null, null, null, new Date(),
									SysManageLog.EXPORT_SYSMANAGElOG, iden.size(), null, null, null, url, null, true),
							request);

		} catch (Exception e) {
			// TODO: handle exception
			sysManageLogService.insertSysManageLogTb(
					new SysManageLog(SYS_MENU, null, null, null, null, null, new Date(),
							SysManageLog.EXPORT_SYSMANAGElOG, iden.size(), null, null, null, null, null, false),
					request);

		}
		return jrw;
	}
	
	@RequestMapping("/add")
	public String add(Model model,Integer id){
		if(id!=null){
		
			Map<String, Object> map = new HashMap<>();
			map.put("ifId", id);
			
			List<IdentiFication> iden = identiFicationService.queryIdentiFicationByAll(map, null);
			if(!CollectionUtils.isEmpty(iden)){
				for (IdentiFication identiFication : iden) {
					List<SysOrg> li = new ArrayList<>();
					String[] orgId = identiFication.getSys_org_id().split(",");
					for (int i = 0; i < orgId.length; i++) {
						if(StringUtils.isNumeric(orgId[i])){
							SysOrg sorg =  sysOrgService.queryInstitutionsById(Integer.parseInt(orgId[i]));
							li.add(sorg);
						}
					}
					identiFication.setSubOrgs(li);
				}
			}
			model.addAttribute("identiFication", iden.get(0));
		}
		return "identification/add";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public JsonResWrapper save(Model model,Integer idenId,String sys_org_name,String orgIds[]){
		JsonResWrapper jrw = new JsonResWrapper();
		String orgId = "";
		for (int i = 0; i < orgIds.length; i++) {
			orgId+=(orgIds[i]+",");
		}
		orgId = orgId.substring(0,orgId.length()-1);
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		
		if(idenId==null){
			
			SysArea sy = sysAreaService
					.queryAreaById(sysOrgService.queryInstitutionsById(userDetails.getSys_org_id()).getSys_area_id());
			while (!sy.getSysAreaType().equals("0") && !sy.getSysAreaType().equals("1")) {
				sy = sysAreaService.queryAreaById(sy.getSysAreaUpid());
			}
			IdentiFication iden = new IdentiFication();
			iden.setSys_identification_name(sys_org_name);
			iden.setSys_area_id(sy.getSysAreaId());
			iden.setSys_creat_org(userDetails.getSys_org_id());
			Date date =new Date();
			java.sql.Timestamp time = new Timestamp(date.getTime());
			iden.setSys_creat_time(time.toString());
			iden.setSys_org_id(orgId);
			iden.setSys_creat_username(userDetails.getUsername());
			try {
				identiFicationService.insertIdentiFication(iden);
				jrw.setMessage("保存成功");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jrw.setFlag(false);
				jrw.setMessage("保存失败");
			}
		}else{
			Map<String, Object> map = new HashMap<>();
			map.put("ifId", idenId);
			List<IdentiFication> iden = identiFicationService.queryIdentiFicationByAll(map, null);
			if(iden!=null){
				IdentiFication ide=iden.get(0);
				ide.setSys_identification_name(sys_org_name);
				ide.setSys_org_id(orgId);
				try {
					identiFicationService.updateIdentiFication(ide);
					jrw.setMessage("保存成功");
				} catch (Exception e) {
					// TODO: handle exception
					
					jrw.setFlag(false);
					jrw.setMessage("保存失败");
				}
			}else{
				jrw.setFlag(false);
				jrw.setMessage("参数缺失");
			}
		}
		return jrw;
	}
	
	@RequestMapping("/del")
	@ResponseBody
	public JsonResWrapper del(Integer id){
		JsonResWrapper jrw = new JsonResWrapper();
		try {
			identiFicationService.deleteIdentiFication(id);
			jrw.setMessage("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			jrw.setFlag(false);
			jrw.setMessage("删除失败");
		}
		return jrw;
		
	}
	
	@Autowired
	ComPanyShowService comPanyShowService;
	
	@RequestMapping("/addIden")
	@ResponseBody
	public JsonResWrapper addIden(String defId,String identId,String recode,Integer areaId){
		String[] dicId = defId.split(",");
		String[] def = identId.split(",");

		JsonResWrapper jw = new JsonResWrapper();
		for (int i = 0; i < def.length; i++) {
			
			MyUserDetails su = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Map<String, Object> map = new HashMap<>();
			//查询归档时间有无数据
			map.put("defaultId", Integer.parseInt(def[i]));
			map.put("area", areaId);
			map.put("recode", recode);
			List<ComPanyShow> comShow = comPanyShowService.queryComPanyShow(map, null);
			if(!CollectionUtils.isEmpty(comShow)){
				ComPanyShow com = comShow.get(0);
				
				com.setDicId(dicId[i].replace(";",",").substring(0, dicId[i].length()-1));
				com.setUsername(su.getUsername());
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sd = sdf.format(new Date());
				com.setTime(sd);
				com.setSysOrgId(su.getSys_org_id());
				comPanyShowService.updateComPanyShow(com);
				jw.setMessage("添加成功");
			}else{
				ComPanyShow com = new ComPanyShow();
				com.setDefaultItemId(Integer.parseInt(def[i]));
				
				com.setDicId(dicId[i].replace(";",",").substring(0, dicId[i].length()-1));
				com.setUsername(su.getUsername());
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sd = sdf.format(new Date());
				com.setTime(sd);
				com.setSysOrgId(su.getSys_org_id());
				com.setSysAreaId(areaId);
				com.setRecodeDate(recode);
				comPanyShowService.insertComPanyShow(com);
				jw.setMessage("添加成功");
			}
		}
		return jw;
	}
}
