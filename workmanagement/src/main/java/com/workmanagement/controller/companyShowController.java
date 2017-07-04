package com.workmanagement.controller;

import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.ComPanyShow;
import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.IdentiFication;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysRole;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.service.ComPanyShowService;
import com.workmanagement.service.DefaultIndexItemService;
import com.workmanagement.service.DicContentService;
import com.workmanagement.service.DicService;
import com.workmanagement.service.IdentiFicationService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysRoleService;
import com.workmanagement.service.SysUserLogService;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.PageSupport;

@Controller
@RequestMapping("/admin/demand")
public class companyShowController {

	@Autowired
	DicContentService dicContentService;
	@Autowired
	DicService dicService;
	@Autowired
	SysAreaService sysAreaService;
	@Autowired
	SysOrgService sysOrgService;
	@Autowired
	ComPanyShowService comPanyShowService;
	@Autowired
	DefaultIndexItemService defaultIndexItemService;
	

	final String SYS_MENU="企业标识查询";

	@Autowired
	private SysRoleService sysRoleService;
	@RequestMapping("/list1")
	public String index(Model model,String code,String name,String typeId[],HttpServletRequest request) throws Exception{
		if(!StringUtils.isBlank(code)||!StringUtils.isBlank(name)||typeId!=null){
			MyUserDetails su = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			PageSupport ps = PageSupport.initPageSupport(request);
			
			// 查询角色列表
			SysRole sr = sysRoleService.querySystemRoleById(su.getRoleIds().get(0));
			boolean isPepole = (sr.getSys_role_type() == SysRole.PEPOLE_ADMIN);
			Map<String, Object> map = new HashMap<String, Object>();

			if (!isPepole) {
				map.put("orgid", su.getSys_org_id());
			}
				if(!StringUtils.isBlank(code)){
					map.put("code", code);
					model.addAttribute("code",code);
				}
				if(!StringUtils.isBlank(name)){
					map.put("name", name);
					model.addAttribute("name",name);
				}
				if(typeId!=null){
					map.put("type", typeId);
					model.addAttribute("typeId",typeId);
				}
				SysArea sy = sysAreaService
						.queryAreaById(sysOrgService.getByIdNotHaveSub(su.getSys_org_id()).getSys_org_affiliation_area_id());
				
				if(sy.getSysAreaId() !=1)
				map.put("area", sy.getSysAreaId());
				List<ComPanyShow> list = comPanyShowService.queryComPanyShow(map, ps);
				model.addAttribute("list",list);
		}
		try {
			List<DicContent> dic =dicContentService.getDicContentsByDicId(dicService.getDicByDicName("企业标识",sysAreaService.queryAreaByCode("510000").getSysAreaId()).getDicId());
			model.addAttribute("dic", dic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "companyShow/list";
	}
	
	@RequestMapping("/add")
	public String add(Integer id,Model model) throws Exception{
		if(id!=null){
			MyUserDetails su = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Map<String, Object> map = new HashMap<>();
			SysArea sy = sysAreaService
					.queryAreaById(sysOrgService.getByIdNotHaveSub(su.getSys_org_id()).getSys_org_affiliation_area_id());
			
			map.put("area", sy.getSysAreaId());
			map.put("id",id);
			model.addAttribute("list",comPanyShowService.queryComPanyShow(map, null).get(0));
		}
		
		try {
			List<DicContent> dic =dicContentService.getDicContentsByDicId(dicService.getDicByDicName("企业标识",sysAreaService.queryAreaByCode("510000").getSysAreaId()).getDicId());
			model.addAttribute("dic", dic);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "companyShow/show";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public JsonResWrapper save(Integer sysComPanyShowId,String codeCredit,String codeOrg,String qymc,String typeId[]) throws Exception{

		JsonResWrapper jw = new JsonResWrapper();
		if(sysComPanyShowId!=null){
			Map<String, Object> map = new HashMap<>();
			map.put("id",sysComPanyShowId);
			ComPanyShow com = comPanyShowService.queryComPanyShow(map, null).get(0);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < typeId.length; i++) {
				sb.append(typeId[i]+",");
			}
			com.setDicId(sb.substring(0, sb.length()-1).toString());
			comPanyShowService.updateComPanyShow(com);
			jw.setMessage("修改成功");
		}else{
			MyUserDetails su = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Map<String, Object> map = new HashMap<>();
			SysArea sy = sysAreaService
					.queryAreaById(sysOrgService.getByIdNotHaveSub(su.getSys_org_id()).getSys_org_affiliation_area_id());
			
			map.put("area", sy.getSysAreaId());
			if(!StringUtils.isBlank(codeCredit))
			map.put("codeCredit", codeCredit);
			if(!StringUtils.isBlank(codeOrg))
			map.put("codeOrg", codeOrg);
			map.put("qymc", qymc);
			map.put("SYS_ORG_ID", su.getSys_org_id());
			
			DefaultIndexItem de = defaultIndexItemService.queryByComPanyShow(map);
			DefaultIndexItem de1 = defaultIndexItemService.queryByAll(map);
			if(de1!=null){
				if(de==null){
				ComPanyShow com = new ComPanyShow();
				com.setDefaultItemId(de1.getDefaultIndexItemId());
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < typeId.length; i++) {
					sb.append(typeId[i]+",");
				}
				com.setDicId(sb.substring(0, sb.length()-1).toString());
				com.setUsername(su.getUsername());
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sd = sdf.format(new Date());
				com.setTime(sd);
				com.setSysOrgId(su.getSys_org_id());
				comPanyShowService.insertComPanyShow(com);
				jw.setMessage("添加成功");
				}else{

					jw.setMessage("此机构已存在该企业标识！");
				}
			}else{
				jw.setMessage("企业数据不存在！");
			}
		}
		return jw;
	}
	

	@Autowired
	private IdentiFicationService identiFicationService;
	
	@RequestMapping("/list")
	public String list(Model model,String recode,String recode1, String typeId,Integer orgIds,HttpServletRequest request) throws Exception{
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		PageSupport ps = PageSupport.initPageSupport(request);
		
		if(!StringUtils.isBlank(recode)||!StringUtils.isBlank(typeId)||orgIds!=null){
		
			Map<String,Object> par = new HashMap<>();
			if(!StringUtils.isBlank(recode)){
				par.put("recode", recode);
				model.addAttribute("recode",recode);
			}else{
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String sd = sdf.format(new Date());
				
				par.put("recode", sd);
			}
			if(!StringUtils.isBlank(recode1)){
				par.put("recode1", recode1);
				model.addAttribute("recode1",recode1);
			}else{
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String sd = sdf.format(new Date());
				
				par.put("recode1", sd);
			}
			if(!StringUtils.isBlank(typeId)){
				par.put("type", typeId.split(","));
				model.addAttribute("typeId",typeId);
			}
			if(orgIds!=null){
				par.put("orgid", orgIds);
				model.addAttribute("orgId",orgIds);
			}
			SysArea sy = sysAreaService
					.queryAreaById(sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id()).getSys_area_id());
			while (!sy.getSysAreaType().equals("0") && !sy.getSysAreaType().equals("1")) {
				sy = sysAreaService.queryAreaById(sy.getSysAreaUpid());
			}
			if(!sy.getSysAreaType().equals("0"))
			par.put("area", sy.getSysAreaId());
			List<ComPanyShow> comShow = comPanyShowService.queryComPanyShow(par, ps);
			if(!CollectionUtils.isEmpty(comShow)){
				for (ComPanyShow comPanyShow : comShow) {
					Map<String, Object> qyzs = new HashMap<String, Object>();
					qyzs = comPanyShowService.querySql("SELECT index_jbxx_qyzs FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_JBXX_QYZS FROM INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
					if(qyzs!=null)
						comPanyShow.setIndex_jbxx_qyzs(qyzs.get("INDEX_JBXX_QYZS").toString());
					else
						comPanyShow.setIndex_jbxx_qyzs("暂无信息");
					Map<String, Object> ggxm =new HashMap<String, Object>();
					ggxm =comPanyShowService.querySql("SELECT INDEX_GGXX_XM FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_GGXX_XM FROM INDEX_GGXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
					if(ggxm!=null)
						comPanyShow.setIndex_ggxx_xm(ggxm.get("INDEX_GGXX_XM").toString());
					else
						comPanyShow.setIndex_ggxx_xm("暂无信息");
					Map<String, Object> lxdh =new HashMap<String, Object>();
					lxdh =comPanyShowService.querySql("SELECT INDEX_JBXX_LXDH FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_JBXX_LXDH FROM INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
					if(lxdh!=null)
						comPanyShow.setIndex_jbxx_lxdh(lxdh.get("INDEX_JBXX_LXDH").toString());
					else
						comPanyShow.setIndex_jbxx_lxdh("暂无信息");
				}
			}
			model.addAttribute("comShow",comShow);
		}

		Map<String,Object> map = new HashMap<>();
		
		map.put("orgId", userDetails.getSys_org_id().toString());
		List<IdentiFication> iden = identiFicationService.queryIdentiFicationByAll(map, null);
		model.addAttribute("iden",iden);

			return "demand/list";
	}
	@Autowired
	SysUserLogService sysUserLogService;
	@RequestMapping(value = "/reportExcle")
	@ResponseBody
	public JsonResWrapper excl(HttpServletRequest request, HttpServletResponse response,
			String recode,String recode1, String typeId,Integer orgIds)
			throws Exception {
		JsonResWrapper jrw = new JsonResWrapper();
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
			
Map<String,Object> map = new HashMap<>();
		
		map.put("orgId", userDetails.getSys_org_id().toString());
		List<IdentiFication> iden = identiFicationService.queryIdentiFicationByAll(map, null);
			Map<String,Object> par = new HashMap<>();
			if(!StringUtils.isBlank(recode)){
				par.put("recode", recode);
			}else{
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String sd = sdf.format(new Date());
				
				par.put("recode", sd);
			}
			if(!StringUtils.isBlank(recode1)){
				par.put("recode1", recode1);
			}else{
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String sd = sdf.format(new Date());
				
				par.put("recode1", sd);
			}
			if(!StringUtils.isBlank(typeId)){
				par.put("type", typeId.split(","));
			}
			if(orgIds!=null){
				par.put("orgid", orgIds);
			}
			SysArea sy = sysAreaService
					.queryAreaById(sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id()).getSys_area_id());
			while (!sy.getSysAreaType().equals("0") && !sy.getSysAreaType().equals("1")) {
				sy = sysAreaService.queryAreaById(sy.getSysAreaUpid());
			}
			if(!sy.getSysAreaType().equals("0"))
			par.put("area", sy.getSysAreaId());
			List<ComPanyShow> comShow = comPanyShowService.queryComPanyShow(par, null);
			if(!CollectionUtils.isEmpty(comShow)){
				for (ComPanyShow comPanyShow : comShow) {
					String biaoshi ="";
					for (IdentiFication identiFication : iden) {
						String a[]=comPanyShow.getDicId().split(",");
						for (int i = 0; i < a.length; i++) {
							if(identiFication.getSys_identification_id().intValue()==Integer.parseInt(a[i])){
								biaoshi+=identiFication.getSys_identification_name()+" ";
							}
						}
					}
					comPanyShow.setBiaoshi(biaoshi);
					Map<String, Object> qyzs = new HashMap<String, Object>();
					qyzs = comPanyShowService.querySql("SELECT index_jbxx_qyzs FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_JBXX_QYZS FROM INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
					if(qyzs!=null)
						comPanyShow.setIndex_jbxx_qyzs(qyzs.get("INDEX_JBXX_QYZS").toString());
					else
						comPanyShow.setIndex_jbxx_qyzs("暂无信息");
					Map<String, Object> ggxm =new HashMap<String, Object>();
					ggxm =comPanyShowService.querySql("SELECT INDEX_GGXX_XM FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_GGXX_XM FROM INDEX_GGXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
					if(ggxm!=null)
						comPanyShow.setIndex_ggxx_xm(ggxm.get("INDEX_GGXX_XM").toString());
					else
						comPanyShow.setIndex_ggxx_xm("暂无信息");
					Map<String, Object> lxdh =new HashMap<String, Object>();
					lxdh =comPanyShowService.querySql("SELECT INDEX_JBXX_LXDH FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_JBXX_LXDH FROM INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
					if(lxdh!=null)
						comPanyShow.setIndex_jbxx_lxdh(lxdh.get("INDEX_JBXX_LXDH").toString());
					else
						comPanyShow.setIndex_jbxx_lxdh("暂无信息");
				}
			}
		


		
		String[] rowNames = { "统一社会信用代码", "组织机构码", "标识","企业名称","注册地","法定代表人","联系电话","创建时间"};
		String[] propertyNames = { "codeCredit", "codeOrg", "biaoshi","qymc","index_jbxx_qyzs","index_ggxx_xm","index_jbxx_lxdh","recodeDate"};

		try {
			// 生成excel
			ExcelExport<ComPanyShow> excelExport = new ExcelExport<>();
			excelExport.setTitle("企业标识");
			excelExport.setRowNames(rowNames);
			excelExport.setPropertyNames(propertyNames);
			excelExport.setList(comShow);
			String url = excelExport.exportExcel(request, response);
			sysUserLogService.insertOneLog(
					new SysUserLog(SYS_MENU, null, null, null, null, null, new Date(),
							SysManageLog.EXPORT_SYSMANAGElOG, comShow.size(), null, null, null, url, null, true),
					request);
		} catch (Exception e) {
			// TODO: handle exception
			sysUserLogService.insertOneLog(
					new SysUserLog(SYS_MENU, null, null, null, null, null, new Date(),
							SysManageLog.EXPORT_SYSMANAGElOG, comShow.size(), null, null, null, null, null, false),
					request);

		}
		return jrw;
	}
	
	@RequestMapping("/getIden")
	@ResponseBody
	public String getIden(Integer defaultId){
		String ident ="";
		Map<String,Object> map = new HashMap<>();
		map.put("defaultId", defaultId);
		List<Map<String, Object>> com = comPanyShowService.queryAll(map);
		if(!CollectionUtils.isEmpty(com)){
			String dic[] = com.get(0).get("DIC_ID").toString().split(",");
			for (int i = 0; i < dic.length; i++) {
				map.put("ifId", Integer.parseInt(dic[i]));
				ident+=identiFicationService.queryIdentiFicationByAll(map, null).get(0).getSys_identification_name()+",";
			}

			return ident.substring(0,ident.length()-1);
		}
		return ident;
	}
}
