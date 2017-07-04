package com.workmanagement.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.Dic;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.GoverType;
import com.workmanagement.model.IndexItemAlias;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.ReportExcelTemplate;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.service.DicContentService;
import com.workmanagement.service.DicService;
import com.workmanagement.service.IndexItemTbService;
import com.workmanagement.service.IndexTbService;
import com.workmanagement.service.ReportExcelTemplateService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOtherManageService;
import com.workmanagement.service.SysRoleService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.ExcelExportExcel;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;

/**
 * excel模板
 * 
 * @author tianhao
 */
@Controller
@RequestMapping("/admin/reportExcelTemplate")
public class ReportExcelTemplateController {
	
	@Autowired
	private ReportExcelTemplateService reportExcelTemplateService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysAreaService sysAreaService;
	
	@Autowired
	private SysOrgService sysOrgService;
	
	@Autowired
	private IndexItemTbService indexItemTbService;
	
	@Autowired
	private SysOtherManageService sysOtherManageService;
	
	@Autowired
	private SysManageLogService sysManageLogService;
	
	@Autowired
	private DicContentService dicContentService;
	
	@Autowired
	private DicService dicService;
	
	/**
	 * 列表页面
	 * 
	 * @param reqeust
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model,
			@RequestParam(required = false) String excelName,@RequestParam(required=false) Integer url) {
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取管辖区域
		String areaKey=RedisKeys.SYS_AREA_UP+so.getSys_area_id();
		SysArea sa = RedisUtil.getObjData(areaKey, SysArea.class);
		if(sa==null){
			sa = sysAreaService.queryParentAreasById(so.getSys_area_id());
			RedisUtil.setData(areaKey, sa);
		}
		request.setAttribute("sa", sa);
		// 查询管辖地区及子地区的ID
		List<Integer> areaIds = new ArrayList<>();
		List<Integer> idup=sysAreaService.getAllUpAreaIds(sa.getSysAreaId());
		List<Integer> idsub=sysAreaService.getAllSubAreaIds(sa.getSysAreaId());
		areaIds.addAll(idup);
		areaIds.addAll(idsub);
		Set<Integer> setAreaIds=new HashSet<>();
		setAreaIds.addAll(areaIds);
		map.put("areaId", setAreaIds);
		if(excelName!=null && !"".equals(excelName)){
			map.put("excelName", excelName);
		}else{
			map.put("excelName", null);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<ReportExcelTemplate> ret=DataUtil.isEmpty(reportExcelTemplateService.queryReportExcelTemplateList(map,ps));
		for (int i = 0; i < ret.size(); i++) {
			if(ret.get(i).getSysAreaId()!=1){
				SysArea area=sysAreaService.queryAreaById(ret.get(i).getSysAreaId());
				String name=area.getSysAreaName()+"-"+ret.get(i).getReportExcelTemplateName();
				ret.get(i).setReportExcelTemplateName(name);
			}
		}
		for (int i = 0; i < ret.size(); i++) {
			Integer indexId=ret.get(i).getIndexId();
			IndexTb indexTb=reportExcelTemplateService.queryIndexTbById(indexId);
			if(indexTb!=null){
				ret.get(i).setIndexName(indexTb.getIndexName());
			}
		}
		//添加管理日志
		if(excelName!=null && !"".equals(excelName)){
			if(url==null){
				List<ReportExcelTemplate> rets=DataUtil.isEmpty(reportExcelTemplateService.queryReportExcelTemplateList(map,null));
				List<Integer> setAreaId=new ArrayList<>();
				Iterator<Integer> setAreaId1=setAreaIds.iterator();
				while(setAreaId1.hasNext()){
					setAreaId.add(setAreaId1.next());
				}
				String areaId="";
				if(setAreaId.size()>0){
					for (int i = 0; i < setAreaId.size(); i++) {
						areaId+=setAreaId.get(i)+",";
					}
					areaId=areaId.substring(0, areaId.length()-1);
				}
				String sql1="SELECT * FROM REPORT_EXCEL_TEMPLATE_TB WHERE 1=1 AND SYS_AREA_ID IN ("+areaId+") AND REPORT_EXCEL_TEMPLATE_NAME LIKE '%"+excelName+"%' ORDER BY CREATE_TIME DESC";
				String sql = StringUtils.replace(sql1, " ", "|");
				SysManageLog sm=new SysManageLog();
				sm.setSysManageLogMenuName("EXCEL模板设置");
				sm.setSysManageLogResult(true);
				sm.setSysManageLogCount(rets.size());
				sm.setSysManageLogOperateType(SysManageLog.SELECT_SYSMANAGElOG);
				sm.setSysManageLogQueryUserCondition(excelName+"(通过名称模糊查询)");
				sm.setSysManageLogQuerySql(sql);
				sm.setSysManageLogUrl("/admin/reportExcelTemplate/list.jhtml?excelName="+excelName+"&url=1");
				sysManageLogService.insertSysManageLogTb(sm, request);
			}
		}
		model.addAttribute("ret", ret);
		model.addAttribute("excelName", excelName);
		return "reportExcelTemplate/list";
	}

	/**
	 * 添加页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edit")
	public String edit(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer reportExcelTemplateId) throws Exception {
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Integer roleId=userDetails.getSys_role_id();
//		String roleType=sysRoleService.getRoleIdByType(roleId);
//		if(!"1".equals(roleType)){
//			request.setAttribute("msg", "您没有权限增加Excel模板");
//			return "forward:list.jhtml";
//		}
		//根据机构ID获取机构缓存,用于查询机构所在地区
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		if(so!=null){
			//获取登录用的区域
			int areaId = so.getSys_area_id();
			String key = RedisKeys.SYS_AREA + areaId;
			SysArea a = RedisUtil.getObjData(key, SysArea.class);
			if(a==null){
				a = sysAreaService.queryAreaById(areaId);
				RedisUtil.setData(key, a);
			}
			model.addAttribute("a", a);
			model.addAttribute("poptype", "add");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String areaKey=RedisKeys.SYS_AREA+so.getSys_area_id();
		SysArea sa=RedisUtil.getObjData(areaKey, SysArea.class);
		if(sa==null){
			sa = sysAreaService.queryAreaById(so.getSys_area_id());
			RedisUtil.setData(areaKey, sa);
		}
		// 查询管辖地区及子地区的ID
		List<Integer> ids=sysAreaService.getAllUpAreaIds(sa.getSysAreaId());
		map.put("areaId", ids);
		// 所有指标大类
		List<String> allIndexCode = reportExcelTemplateService.selectAllIndexCode(map);
		List<Integer> allIndexId = reportExcelTemplateService.selectAllIndexId(map);
		List<String> allIndexName = reportExcelTemplateService.selectAllIndexName(map);
		if(allIndexId.size()>0){
			Integer indexId=allIndexId.get(0).intValue();
			map.put("indexId", indexId);
			List<IndexItemTb> indexItemTbList = reportExcelTemplateService.queryIndexItemsByIndex(map);
			List<IndexItemAlias> indexItemAlias=reportExcelTemplateService.queryIndexItemAlias(so.getSys_area_id());
			for (int j = 0; j < indexItemTbList.size(); j++) {
				for (int i = 0; i < indexItemAlias.size(); i++) {
					if(indexItemAlias.get(i).getIndexItemId().intValue()==indexItemTbList.get(j).getIndexItemId().intValue()){
						indexItemTbList.get(j).setIndexItemName(indexItemAlias.get(i).getIndexItemAliasName());;
					}
				}
			}
			model.addAttribute("indexItemTbList", indexItemTbList);
		}
		SysOtherManage stm=sysOtherManageService.querySysOtherManage(userDetails.getSys_user_id());
		model.addAttribute("allIndexCode", allIndexCode);
		model.addAttribute("allIndexId", allIndexId);
		model.addAttribute("allIndexName", allIndexName);
		model.addAttribute("orgSwitch", stm.getSysSetOrgSwitch());
		return "reportExcelTemplate/edit";
	}

	/**
	 * 根据指标大类id返回指标项集合(json)
	 * @param id
	 * @return
	 */
	@RequestMapping("/getIndexItemTbsJson")
	@ResponseBody
	public Map<String, Object> getIndexItemTbsJson(@RequestParam(required = false) Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//根据机构ID获取机构缓存,用于查询机构所在地区
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		IndexTb indexTb=reportExcelTemplateService.queryIndexTbById(id);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("indexId", indexTb.getIndexId());
		List<Integer> ids=sysAreaService.getAllUpAreaIds(so.getSys_area_id());
		map.put("areaId", ids);
		List<IndexItemTb> indexItemTbList = reportExcelTemplateService.queryIndexItemsByIndex(maps);
		List<IndexItemAlias> indexItemAlias=reportExcelTemplateService.queryIndexItemAlias(so.getSys_area_id());
		for (int j = 0; j < indexItemTbList.size(); j++) {
			for (int i = 0; i < indexItemAlias.size(); i++) {
				if(indexItemAlias.get(i).getIndexItemId().intValue()==indexItemTbList.get(j).getIndexItemId().intValue()){
					indexItemTbList.get(j).setIndexItemName(indexItemAlias.get(i).getIndexItemAliasName());;
				}
			}
		}
		map.put("indexItemTbList", indexItemTbList);
		
		return map;
	}
	
	/**
	 * 保存添加
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editSubmit", method = RequestMethod.POST)
	@ResponseBody
	public JsonResWrapper editSubmit(Model model, ReportExcelTemplate reportExcelTemplate, String[] indexItemCodes,
			Integer[] checkedInput,Integer orgSwitch,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResWrapper jrw = new JsonResWrapper();
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer roleId=userDetails.getSys_role_id();
		String roleType=sysRoleService.getRoleIdByType(roleId);
		if(!"1".equals(roleType)){
			jrw.setFlag(false);
			jrw.setMessage("你没有权限增加模板");
			return jrw;
		}
		if (reportExcelTemplate.getReportExcelTemplateName() == null
				|| reportExcelTemplate.getSysAreaId() == null
				|| reportExcelTemplate.getReportExcelTemplateAreaName().trim().equals("")) {
			jrw.setFlag(false);
			jrw.setMessage("名称或区域未填写");
			return jrw;
		} else if (indexItemCodes.length <= 2) {
			if(orgSwitch==1){
				jrw.setFlag(false);
				jrw.setMessage("至少选择一个指标项");
				return jrw;
			}else{
				if(indexItemCodes.length <= 1){
					jrw.setFlag(false);
					jrw.setMessage("至少选择一个指标项");
					return jrw;
				}
			}
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			if(reportExcelTemplate.getSysAreaId()==1){
				map.put("areaId", reportExcelTemplate.getSysAreaId());
			}else{
				SysArea area=sysAreaService.getUpOrThisSysArea(reportExcelTemplate.getSysAreaId());
				List<Integer> areaId=sysAreaService.getAllSubAreaIds(area.getSysAreaId());
				map.put("areaId", areaId);
			}
			map.put("areaType", reportExcelTemplate.getSysAreaId());
			reportExcelTemplate.setReportExcelTemplateName(reportExcelTemplate.getReportExcelTemplateName().trim());
			List<String> allReportExcelTemplateName = reportExcelTemplateService.allReportExcelTemplateName(map);
			for (String name : allReportExcelTemplateName) {
				if (reportExcelTemplate.getReportExcelTemplateName().equals(name)) {
					jrw.setFlag(false);
					jrw.setMessage("该模板名称已被占用");
					return jrw;
				}
			}
			for (int i = 0; i < checkedInput.length; i++) {
				for (int j = i+1; j < checkedInput.length; j++) {
					if(checkedInput[i]==checkedInput[j]){
						jrw.setFlag(false);
						jrw.setMessage("序号:第"+(i+1)+"行与第"+(j+1)+"行重复");
						return jrw;
					}
				}
			}
			for (int i = 0; i < checkedInput.length; i++) {
				for (int j = 0; j < checkedInput.length-i-1; j++) {
					if(checkedInput[j]>checkedInput[j+1]){
						int temp;
						temp=checkedInput[j];
						checkedInput[j]=checkedInput[j+1];
						checkedInput[j+1]=temp;
						String temp1;
						if(orgSwitch==1){
							temp1=indexItemCodes[j+2];
							indexItemCodes[j+2]=indexItemCodes[j+3];
							indexItemCodes[j+3]=temp1;
						}else{
							temp1=indexItemCodes[j+1];
							indexItemCodes[j+1]=indexItemCodes[j+2];
							indexItemCodes[j+2]=temp1;
						}
					}
				}
			}
			
			// 提交的指标项集合
			if (indexItemCodes != null && indexItemCodes.length > 0) {
				// 加上默认的社会码和信用码
				reportExcelTemplate.setReportExcelTemplateIndexItemSet(
						Arrays.toString(indexItemCodes).replace("[", "").replace("]", ""));
			}
			reportExcelTemplate.setSysOrgId(userDetails.getSys_org_id());
			reportExcelTemplate.setReportExcelTemplateStatus(0);
			reportExcelTemplate.setCreateUserId(userDetails.getSys_user_id());
			reportExcelTemplate.setCreateTime(new Date());
//			reportExcelTemplate.setIndexItemTbService(indexItemTbService);
//			List<IndexItemTb> indexItemTbList = reportExcelTemplate.getIndexItemTbList();
//			// 设置列名
//			String[] rowNames = new String[indexItemTbList.size()];
//			for (int i = 0; i < indexItemTbList.size(); i++) {
//				rowNames[i] = indexItemTbList.get(i).getIndexItemName();
//			}
//			//生成模板文件并保存
//			ExcelExport<ReportExcelTemplate> excelExport = new ExcelExport<>();
//			excelExport.setReportExcelTemplate(reportExcelTemplate);
//			excelExport.setIndexItemTbService(indexItemTbService);
//			excelExport.setTitle(reportExcelTemplate.getReportExcelTemplateName());
//			for (int i = 0; i < indexItemTbList.size(); i++) {
//				rowNames[i]=indexItemTbList.get(i).getIndexItemName();
//			}
//			excelExport.setRowNames(rowNames);
//			//执行
//			excelExport.exportExcel(request,response);
//			//赋值文件路径
//			reportExcelTemplate.setReportExcelTemplatePath(excelExport.getNewFileName());
			reportExcelTemplateService.insertReportExcelTemplate(reportExcelTemplate, request);
		}
		
		jrw.setMessage("操作成功");
		return jrw;
	}
	
	// 下载excel模板
	@RequestMapping(value = "/downLoad")
	public void downLoadFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) Integer id, @RequestParam(required = false) String name) throws Exception {
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//根据机构ID获取机构缓存,用于查询机构所在地区
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		ReportExcelTemplate reportExcelTemplate = null;
		if (id != null) {
			reportExcelTemplate = reportExcelTemplateService.queryReportExcelTemplateById(id);
		} else if (name != null) {
			reportExcelTemplate = reportExcelTemplateService.queryReportExcelTemplateByName(name);
		}
//		 //代码文件名
//		 String fileName = reportExcelTemplate.getReportExcelTemplatePath();
//		 //真实文件名
//		 String title = reportExcelTemplate.getReportExcelTemplateName();
//		 //下载
//		 DownLoadFile.downLoadFile(fileName, title, request, response);
//		 生成
		ExcelExportExcel<ReportExcelTemplate> excelExport = new ExcelExportExcel<>();
		excelExport.setTitle(reportExcelTemplate.getReportExcelTemplateName());
		List<IndexItemTb> indexItemTbList = new ArrayList<>();
		IndexItemTb itenCodeCredit=new IndexItemTb();
		itenCodeCredit.setIndexItemName("统一社会信用代码");
		itenCodeCredit.setIndexItemType(0);
		itenCodeCredit.setIndexItemCode("code_credit");
		itenCodeCredit.setVarLength(18);
		itenCodeCredit.setIndexItemEmpty(1);
		itenCodeCredit.setIndexId(reportExcelTemplate.getIndexId());
		indexItemTbList.add(itenCodeCredit);
		String indexItemSet=reportExcelTemplate.getReportExcelTemplateIndexItemSet();
		if(ManualEntryController.isContain(indexItemSet, "code_org")){
			IndexItemTb itenOrg=new IndexItemTb();
			itenOrg.setIndexItemName("组织机构代码");
			itenOrg.setIndexItemType(0);
			itenOrg.setIndexItemCode("code_org");
			itenOrg.setVarLength(10);
			itenOrg.setIndexItemEmpty(1);
			itenOrg.setIndexId(reportExcelTemplate.getIndexId());
			indexItemTbList.add(itenOrg);
		}
//		SysOtherManage stm=sysOtherManageService.querySysOtherManage(userDetails.getSys_user_id());
//		if(stm.getSysSetOrgSwitch()==1){
//			
//		}
		if (reportExcelTemplate.getReportExcelTemplateIndexItemSet()!=null) {
			String[] array=reportExcelTemplate.getReportExcelTemplateIndexItemSet().split(",");
//			for (String code : array) {
//				IndexItemTb indexItemTb = reportExcelTemplateService.queryIndexItemTbByCode(code.substring(1));
//				if(indexItemTb!=null){
//					indexItemTbList.add(indexItemTb);
//				}
//			}
			for (int i = 0; i < array.length; i++) {
				IndexItemTb indexItemTb;
				if(i==0){
					indexItemTb = reportExcelTemplateService.queryIndexItemTbByCode(array[i]);
				}else{
					indexItemTb = reportExcelTemplateService.queryIndexItemTbByCode(array[i].substring(1));
				}
				if(indexItemTb!=null){
					indexItemTbList.add(indexItemTb);
				}
			}
		}
		List<IndexItemAlias> indexItemAlias=reportExcelTemplateService.queryIndexItemAlias(so.getSys_area_id());
		String[] names = new String[indexItemTbList.size()];
		if(indexItemAlias.size()>0){
			for (int i = 0; i < indexItemTbList.size(); i++) {
				for (int j = 0; j < indexItemAlias.size(); j++) {
					if(indexItemAlias.get(j).getIndexItemId()!=null && indexItemTbList.get(i).getIndexItemId()!=null){
						if(indexItemAlias.get(j).getIndexItemId().intValue()==indexItemTbList.get(i).getIndexItemId().intValue()){
							indexItemTbList.get(i).setIndexItemName(indexItemAlias.get(j).getIndexItemAliasName());;
						}
					}
					names[i] = indexItemTbList.get(i).getIndexItemName();
				}
			}
		}else{
			for (int i = 0; i < indexItemTbList.size(); i++) {
				names[i] = indexItemTbList.get(i).getIndexItemName();
			}
		}
		Integer[] rows=new Integer[indexItemTbList.size()];
		String[] prompt = new String[indexItemTbList.size()];
		for (int i = 0; i < indexItemTbList.size(); i++) {
			if(indexItemTbList.get(i).getIndexItemType()==0){
				if(indexItemTbList.get(i).getIndexItemCode().equals("code_credit")){
					prompt[i]="类型:字符类型\n长度:"+indexItemTbList.get(i).getVarLength();
					rows[i]=3;
				}else if(indexItemTbList.get(i).getIndexItemCode().equals("code_org")){
					prompt[i]="类型:字符类型\n长度:"+indexItemTbList.get(i).getVarLength();
					rows[i]=3;
				}else{
					if(StringUtils.isBlank(indexItemTbList.get(i).getIndexItemNotes())){
						prompt[i]="类型:字符类型\n长度:"+indexItemTbList.get(i).getVarLength()+"字节(一个汉字占两个字节)";
						rows[i]=3;
					}else{
						prompt[i]=indexItemTbList.get(i).getIndexItemNotes();
						rows[i]=3;
					}
				}
			}else if(indexItemTbList.get(i).getIndexItemType()==1){
				if(StringUtils.isBlank(indexItemTbList.get(i).getIndexItemNotes())){
					prompt[i]="类型:时间类型";
					rows[i]=2;
				}else{
					prompt[i]=indexItemTbList.get(i).getIndexItemNotes();
					rows[i]=3;
				}
			}else if(indexItemTbList.get(i).getIndexItemType()==2){
				if(StringUtils.isBlank(indexItemTbList.get(i).getIndexItemNotes())){
					prompt[i]="类型:数值类型";
					rows[i]=2;
				}else{
					prompt[i]=indexItemTbList.get(i).getIndexItemNotes();
					rows[i]=2;
				}
			}else if(indexItemTbList.get(i).getIndexItemType()==3){
				if(StringUtils.isBlank(indexItemTbList.get(i).getIndexItemNotes())){
					List<DicContent> dicContentList=dicContentService.getDicContentsByDicId(indexItemTbList.get(i).getDicId());
					if(dicContentList.size()>0){
						StringBuffer sb=new StringBuffer();
						for (DicContent dicContent : dicContentList) {
							sb.append("代码:"+dicContent.getDicContentCode()+"  值:"+dicContent.getDicContentValue()+"\n");
						}
						if(dicContentList.size()>15){
							prompt[i]="类型:数据字典\n详情见数据字典";
							rows[i]=3;
						}else{
							prompt[i]="类型:数据字典\n"+sb;
							rows[i]=dicContentList.size()+2;
						}
					}else{
						Dic dic=dicService.getDicByDicId(indexItemTbList.get(i).getDicId());
						if(dic!=null){
							if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
								prompt[i]="类型:数据字典\n详情见行业表";
								rows[i]=3;
							}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.ORGANIZATION)){
								prompt[i]="类型:数据字典\n详情见机构表";
								rows[i]=3;
							}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.Area)){
								prompt[i]="类型:数据字典\n详情见地区表";
								rows[i]=3;
							}
						}else{
							prompt[i]="类型:数据字典\n详情见数据字典";
							rows[i]=3;
						}
					}
				}else{
					prompt[i]=indexItemTbList.get(i).getIndexItemNotes();
					rows[i]=3;
				}
			}
		}
		Integer[] indexId=new Integer[indexItemTbList.size()];
		Integer[] colors=new Integer[indexItemTbList.size()];
		for (int i = 0; i < indexItemTbList.size(); i++) {
			indexId[i] = indexItemTbList.get(i).getIndexId();
			colors[i]=indexItemTbList.get(i).getIndexItemEmpty();
		}
		IndexTb indexTb=reportExcelTemplateService.queryIndexTbById(indexId[0]);
		
		SysManageLog sm=new SysManageLog();
		sm.setSysManageLogMenuName("EXCEL模板设置");
		sm.setIndexId(reportExcelTemplate.getIndexId());
		sm.setSysManageLogOperateType(SysManageLog.EXPORT_SYSMANAGElOG);
		sm.setSysManageLogResult(true);
		sm.setSysManageLogCount(1);
		sysManageLogService.insertSysManageLogTb(sm, request);
		
		String[] rowNames = names;
		excelExport.setTableName(indexTb.getIndexName());
		excelExport.setRowNames(rowNames);
		excelExport.setPrompt(prompt);
		excelExport.setRows(rows);
		excelExport.setColors(colors);
		excelExport.setIndexItemTbService(indexItemTbService);// !
		excelExport.setReportExcelTemplate(reportExcelTemplate);
		excelExport.exportObjctExcel(response);
	}
	
	/**
	 * 删除模板
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping("/delExcel")
	@ResponseBody
	public JsonResWrapper updataStatus(HttpServletResponse response,HttpServletRequest request,@RequestParam(required = false) Integer id,
							@RequestParam(required = false) Integer sysOrgId,
							@RequestParam(required = false) Integer status) {
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		JsonResWrapper jrw = new JsonResWrapper();
		if(userDetails.getSys_org_id().intValue() != sysOrgId.intValue()){
			jrw.setFlag(false);
			jrw.setMessage("仅创建该模板的机构管理员才能删除");
			return jrw;
		}
		ReportExcelTemplate reportExcelTemplate = reportExcelTemplateService.queryReportExcelTemplateById(id);
		reportExcelTemplateService.delReportExcelTemplate(id);
		SysManageLog sm=new SysManageLog();
		sm.setSysManageLogMenuName("EXCEL模板设置");
		sm.setIndexId(reportExcelTemplate.getIndexId());
		sm.setSysManageLogOperateType(SysManageLog.DELECT_SYSMANAGElOG);
		sm.setSysManageLogResult(true);
		sm.setSysManageLogCount(1);
		sysManageLogService.insertSysManageLogTb(sm, request);
		jrw.setMessage("操作成功");
		return jrw;
	}

}
