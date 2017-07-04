package com.workmanagement.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.ReportExcelTemplate;
import com.workmanagement.model.ReportTaskPushList;
import com.workmanagement.model.ReportTaskPushSet;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOrgType;
import com.workmanagement.model.SysUser;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.model.ZtreeVo;
import com.workmanagement.service.IndexTbService;
import com.workmanagement.service.ReportExcelTemplateService;
import com.workmanagement.service.ReportTaskPushListService;
import com.workmanagement.service.ReportTaskPushSetService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOrgTypeService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;

/**
 * 任务推送
 * 
 * @author renyang
 */
@Controller
@RequestMapping("/admin/reportTaskPushSet")
public class ReportTaskPushSetController {

	@Autowired
	private ReportTaskPushSetService reportTaskPushSetService;
	@Autowired
	private IndexTbService indexTbService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private ReportExcelTemplateService reportExcelTemplateService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private ReportTaskPushListService reportTaskPushListService;
	@Autowired
	private SysManageLogService sysManageLogService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysOrgTypeService sysOrgTypeService;
	/**
	 * 通过名称获取机构
	 * @param name 机构名称
	 * @return
	 */
	@RequestMapping("/getOrgByName")
	@ResponseBody
	public List<SysOrg> getOrgByName(@RequestParam(required=false) String name){
		// 机构列表
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		StringBuffer sb = new StringBuffer();
		DataUtil.getChildOrgIds(so, sb);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgIds", sb.toString().split(","));
		map.put("orgName", name);
		map.put("upid", true);
		List<SysOrg> sysOrgList=sysOrgService.queryInstitution(map);
		return sysOrgList;
	}
	/**
	 * 通过机构列表
	 * @param
	 * @param 
	 * @return
	 */
	@RequestMapping("/getOrgList")
	@ResponseBody
	public void getOrgList(HttpServletResponse response){
		// 机构列表
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer orgId=userDetails.getSys_org_id();
		//缓存单个机构
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		
		Integer affiliationAreaId=so.getSys_org_affiliation_area_id();
		Integer areaId=so.getSys_area_id();
		String orgListKey = RedisKeys.SYS_ORG_LIST_USER + userDetails.getSys_user_id();
		Type type = new TypeToken<List<SysOrg>>(){}.getType();
		List<SysOrg> sysOrgList = RedisUtil.getListData(orgListKey, type);
		if(CollectionUtils.isEmpty(sysOrgList)){
			if(affiliationAreaId==null){
				sysOrgList = sysOrgService.querySysOrg(map);
			}else if(!areaId.equals(affiliationAreaId)){
				//获取地区缓存
				String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
				StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
				if(sb==null){
					sb=new StringBuffer();
					SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
					DataUtil.getChildAreaIds(sysArea, sb);
					//设置地区ID集合缓存
					RedisUtil.setData(areaSbKey, sb);
				}
				map.put("area_id", sb.toString().split(","));
				sysOrgList = sysOrgService.querySysOrg(map);
			}else{
				map.put("affiliationArea", affiliationAreaId);
				sysOrgList = sysOrgService.querySysOrg(map);
			}
			//设置机构缓存
			RedisUtil.setData(orgListKey, sysOrgList);
		}
		String key = RedisKeys.SYS_ORG_TYPE_LIST+ userDetails.getSys_user_id()+"task";
		Type orgtype = new TypeToken<List<SysOrgType>>(){}.getType();
		List<SysOrgType> sysOrgType = RedisUtil.getListData(key, orgtype);
		if(CollectionUtils.isEmpty(sysOrgType)){
			Set<Integer> set =new HashSet<Integer>();
			for (int i = 0; i < sysOrgList.size(); i++) {
				set.add(sysOrgList.get(i).getSys_org_type_id());
			}
			Map<String, Object> param=new HashMap<String, Object>();
			if(set.size()>0){
				param.put("typeIds", set);
			}
			StringBuffer sb=new StringBuffer();
			sysOrgType=sysOrgTypeService.queryTypeList(param);
			for (int i = 0; i < sysOrgType.size(); i++) {
				DataUtil.getParentOrgTypeIds(sysOrgType.get(i), sb, sysOrgTypeService);
			}
			param.put("typeIds", sb.toString().split(","));
			sysOrgType=sysOrgTypeService.queryTypeList(param);
			RedisUtil.setData(key, sysOrgType);
		}
		sysOrgType = new ArrayList<>();
		//要分配的机构
		SysOrgType type80 = sysOrgTypeService.getTypeByIdNotSub(80);
		sysOrgType.add(type80);
		List<ZtreeVo> ztreeVo=DataUtil.getZtree(sysOrgList, sysOrgType);
		Gson gson=new Gson();
		try {
			response.getWriter().write(gson.toJson(ztreeVo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 列表页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model) throws Exception {

		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		PageSupport ps = PageSupport.initPageSupport(request);
		List<ReportTaskPushSet> reportTaskPushSetList = DataUtil
				.isEmpty(reportTaskPushSetService.queryReportTaskPushSets(userDetails.getSys_org_id(), ps));
		model.addAttribute("reportTaskPushSetList", reportTaskPushSetList);
		return "reportTaskPushSet/list";
	}

	@RequestMapping("/show")
	public String show(Model model, Integer reportTaskPushSetId,HttpServletRequest req) throws Exception {
		ReportTaskPushSet reportTaskPushSet = reportTaskPushSetService.queryReportTaskPushSetById(reportTaskPushSetId);
		Integer setTempLateId = reportTaskPushSet.getReportTaskPushSetTempLateId();
		Integer createId = reportTaskPushSet.getSysOrgCreateId();
		Integer type=reportTaskPushSet.getReportTaskPushSetType();
		SysOrg sysOrg = sysOrgService.queryInstitutionsById(createId);
		reportTaskPushSet.setReportTaskPushSetOrgName(sysOrg.getSys_org_name());
		Integer indexId=null;
		//判断报送类型
		if(type==0){
			IndexTb indexTb = indexTbService.queryById(setTempLateId);
			indexId=setTempLateId;
			reportTaskPushSet.setReportTaskPushSetTemplate(indexTb.getIndexName());
		}
		if(type==1){
			ReportExcelTemplate template=reportExcelTemplateService.queryReportExcelTemplateById(setTempLateId);
			reportTaskPushSet.setReportTaskPushSetTemplate(template.getReportExcelTemplateName());
			indexId=template.getIndexId();
		}
		model.addAttribute("reportTaskPushSet", reportTaskPushSet);
		// 已选机构列表
		reportTaskPushSet.setSysOrgService(sysOrgService);
		List<SysOrg> sysOrgList = reportTaskPushSet.getSysOrg();
		model.addAttribute("sysOrgList", sysOrgList);
		SysManageLog sysManageLog=new SysManageLog();
		sysManageLog.setSysManageLogMenuName("任务管理");
		sysManageLog.setIndexId(indexId);
		sysManageLog.setSysManageLogCount(1);
		sysManageLog.setSysManageLogOperateType(4);
		sysManageLog.setSysManageLogResult(true);
		String sql=" SELECT pst.*,ot.sys_org_name AS reportTaskPushSetOrgName FROM report_task_push_set_tb  pst"+
				   " LEFT JOIN sys_org_tb ot ON pst.sys_org_create_id =ot.sys_org_id"+
				   " WHERE report_task_push_set_id ="+reportTaskPushSetId;
		sysManageLog.setSysManageLogQuerySql(sql);
		sysManageLog.setSysManageLogUrl("/admin/reportTaskPushSet/show.jhtml?reportTaskPushSetId="+reportTaskPushSetId);
		sysManageLogService.insertSysManageLogTb(sysManageLog,req);
		return "reportTaskPushSet/show";
	}
	//根据报送方式得到名称
	@RequestMapping("/getReportTaskPushSetMethodListByMethodIdJson")
	@ResponseBody
	public Map<String, Object> getReportTaskPushSetMethodListByMethodIdJson(
			@RequestParam(required = false) Integer reportTaskPushSetMethod) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> reportTaskPushSetTemplateList = new ArrayList<String>();
		if (reportTaskPushSetMethod == 0) {
			List<IndexTb> indexTbList = indexTbService.selectAll();
			for (int i = 0; i < indexTbList.size(); i++) {
				reportTaskPushSetTemplateList.add((indexTbList.get(i).getIndexName()));
			}
		} else if (reportTaskPushSetMethod == 1) {
			Map<String, Object> mapNull = new HashMap<String, Object>();
			List<ReportExcelTemplate> reportExcelTemplate=reportExcelTemplateService.queryReportExcelTemplateList(mapNull,null);
			for (ReportExcelTemplate reportExcelTemplate2 : reportExcelTemplate) {
				reportTaskPushSetTemplateList.add(reportExcelTemplate2.getReportExcelTemplateName());
			}
		}
		map.put("reportTaskPushSetTemplateList", reportTaskPushSetTemplateList);
		return map;
	}

	/**
	 * 删除任务
	 * 
	 * @param reportTaskPushSetId
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/del")
	@ResponseBody
	public JsonResWrapper del(@RequestParam(required = false) Integer reportTaskPushSetId,HttpServletRequest request) throws Exception {
		JsonResWrapper jrw = new JsonResWrapper();
		if (reportTaskPushSetId == null) {
			jrw.setFlag(false);
			jrw.setMessage("删除失败，参数缺失");
			return jrw;
		}
		ReportTaskPushSet reportTaskPushSet=reportTaskPushSetService.queryReportTaskPushSetById(reportTaskPushSetId);
		Integer type=reportTaskPushSet.getReportTaskPushSetType();
		Integer indexId=null;
		if(type==0){
			indexId=reportTaskPushSet.getReportTaskPushSetTempLateId();
		}
		if(type==1){
			ReportExcelTemplate reportExcelTemplate=reportExcelTemplateService.queryReportExcelTemplateById(reportTaskPushSet.getReportTaskPushSetTempLateId());
			indexId=reportExcelTemplate.getIndexId();
		}
		Integer delNum = reportTaskPushSetService.deleteTaskById(reportTaskPushSetId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reportTaskPushSetId", reportTaskPushSetId);
		List<ReportTaskPushList> queryReportTaskPushLists = reportTaskPushListService
				.queryReportTaskPushLists(map, null);
		if(queryReportTaskPushLists!=null){
			for (int i = 0; i < queryReportTaskPushLists.size(); i++) {
				reportTaskPushListService.delete(queryReportTaskPushLists.get(i).getReportTaskPushListId());
			}
		}
		if (delNum <= 0) {
			jrw.setFlag(false);
			jrw.setMessage("删除失败");
			SysManageLog sysManageLog=new SysManageLog();
			sysManageLog.setSysManageLogMenuName("任务管理");
			sysManageLog.setIndexId(indexId);
			sysManageLog.setSysManageLogCount(1);
			sysManageLog.setSysManageLogOperateType(2);
			sysManageLog.setSysManageLogResult(false);
			sysManageLogService.insertSysManageLogTb(sysManageLog,request);
			return jrw;
		}
		jrw.setMessage("删除成功");
		SysManageLog sysManageLog=new SysManageLog();
		sysManageLog.setSysManageLogMenuName("任务管理");
		sysManageLog.setIndexId(indexId);
		sysManageLog.setSysManageLogCount(1);
		sysManageLog.setSysManageLogOperateType(2);
		sysManageLog.setSysManageLogResult(true);
		sysManageLogService.insertSysManageLogTb(sysManageLog,request);
		return jrw;
	}
	/**
	 * 添加、修改页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(Model model, @RequestParam(required = false) Integer reportTaskPushSetId) {
		ReportTaskPushSet reportTaskPushSet = null;
		if (reportTaskPushSetId != null) {
			reportTaskPushSet = reportTaskPushSetService.queryReportTaskPushSetById(reportTaskPushSetId);
			MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			if (reportTaskPushSet.getSysOrgCreateId().intValue() != userDetails.getSys_org_id().intValue()) {
				model.addAttribute("msg", "仅创建该任务机构有权修改");
				return "forward:list.jhtml";
			}

			List<String> reportTaskPushSetTemplateList = new ArrayList<String>();// 模板集合
			Integer reportTaskPushSetType = reportTaskPushSet.getReportTaskPushSetType();
			if (reportTaskPushSetType == 0) {
				List<IndexTb> indexTbs = indexTbService.selectAll();
				for (IndexTb indexTb2 : indexTbs) {
					reportTaskPushSetTemplateList.add(indexTb2.getIndexName());
				}
				Integer tempLateId= reportTaskPushSet.getReportTaskPushSetTempLateId();
				IndexTb indexTb=indexTbService.queryById(tempLateId);
				reportTaskPushSet.setReportTaskPushSetTemplate(indexTb.getIndexName());
				model.addAttribute("reportTaskPushSetTemplateList", reportTaskPushSetTemplateList);
			} else if (reportTaskPushSetType == 1) {
				Map<String, Object> mapNull = new HashMap<String, Object>();
				List<ReportExcelTemplate> reportExcelTemplate=reportExcelTemplateService.queryReportExcelTemplateList(mapNull,null);
				for (ReportExcelTemplate reportExcelTemplate2 : reportExcelTemplate) {
					reportTaskPushSetTemplateList.add(reportExcelTemplate2.getReportExcelTemplateName());
				}
				ReportExcelTemplate	Template=reportExcelTemplateService.
						queryReportExcelTemplateById(reportTaskPushSet.getReportTaskPushSetTempLateId());
				reportTaskPushSet.setReportTaskPushSetTemplate(Template.getReportExcelTemplateName());
				model.addAttribute("reportTaskPushSetTemplateList", reportTaskPushSetTemplateList);
			}
			Date beginTime=reportTaskPushSet.getReportTaskPushSetTime();
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		    String str = sdf.format(beginTime);
			// 已选机构列表
			reportTaskPushSet.setSysOrgService(sysOrgService);
			List<SysOrg> sysOrgList = reportTaskPushSet.getSysOrg();
			model.addAttribute("sysOrgList", sysOrgList);
			model.addAttribute("beginTime", str);
		} else {
			// 用户
			MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			SysUser sysUser = sysUserService.querySystemUserById(userDetails.getSys_user_id());
			// 发布机构
			SysOrg institutions = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			reportTaskPushSet = new ReportTaskPushSet();
			reportTaskPushSet.setReportTaskPushSetOrgName(institutions.getSys_org_name());
			reportTaskPushSet.setSysOrgCreateId(sysUser.getSys_org_id());
			List<IndexTb> indexTb = indexTbService.selectAll();
			List<String> allIndexName=new ArrayList<String>();
			for (IndexTb indexTb2 : indexTb) {
				allIndexName.add(indexTb2.getIndexName());
			}
			model.addAttribute("allIndexName", allIndexName);
		}
		model.addAttribute("reportTaskPushSet", reportTaskPushSet);
		// 报送方式
		List<String> reportTaskPushSetMethodList = new ArrayList<>();// 报送方式集合
		reportTaskPushSetMethodList.add("WORD报送");
		reportTaskPushSetMethodList.add("EXCEL报送");
		List<Integer> reportTaskPushSetMethodIdList = new ArrayList<>();
		reportTaskPushSetMethodIdList.add(0);
		reportTaskPushSetMethodIdList.add(1);
		model.addAttribute("reportTaskPushSetMethodList", reportTaskPushSetMethodList);
		model.addAttribute("reportTaskPushSetMethodIdList", reportTaskPushSetMethodIdList);
		// 机构列表
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		StringBuffer sb = new StringBuffer();
		DataUtil.getChildOrgIds(so, sb);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgIds", sb.toString().split(","));
		List<SysOrg> is = sysOrgService.querySysOrg(map);
		model.addAttribute("is", is);
		return "reportTaskPushSet/edit";
	}

	/**
	 * 保存添加、修改
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editSubmit", method = RequestMethod.POST)
	@ResponseBody
	public JsonResWrapper editSubmit(Model model, @RequestParam(required = false) Integer reportTaskPushSetId,HttpServletRequest request,
			@RequestParam(required = false) String reportTaskPushSetName,
			@RequestParam(required = false) String reportTaskPushSetCycle,
			@RequestParam(required = false) Integer reportTaskPushSetStatus,
			@RequestParam(required = false) Integer reportTaskPushSetMethod,
			@RequestParam(required = false) String reportTaskPushSetTemplate,
			@RequestParam(required = false) Integer reportTaskPushSetOrgId,
			@RequestParam(required = false) String reportTaskPushSetOrgName,
			@RequestParam(required = false) String beginTimes,
			@RequestParam(required = false) String[] orgIds) throws Exception {
		JsonResWrapper jrw = new JsonResWrapper();
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (reportTaskPushSetName == null) {
			jrw.setFlag(false);
			jrw.setMessage("任务名称未填写");
			return jrw;
		} else if (orgIds == null || orgIds.length == 0) {
			jrw.setFlag(false);
			jrw.setMessage("机构列表未选择");
			return jrw;
		} else {
			ReportTaskPushSet reportTaskPushSet = new ReportTaskPushSet();
			String reportTaskPushSetOrgIdSet = Arrays.toString(orgIds).replace("[", "").replace("]", "").replace(" ", "");
			//往ReportTaskPushSet类里面存值
			reportTaskPushSet.setSysOrgExcuteIds(reportTaskPushSetOrgIdSet);
			/*Date now = new Date();
			LocalDate localDate=now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			Date newDate=java.sql.Date.valueOf(localDate);*/
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			beginTimes=beginTimes+"-"+01;
			Date newDate=sdf.parse(beginTimes);
			String [] cycle1=reportTaskPushSetCycle.split("-");
			String c=cycle1[1];
			Date date=getEndDate(newDate,reportTaskPushSetCycle);
			String time=sdf.format(date);
			String newtime="";
			Integer lastDay=getLastDay(date);
			if (Integer.parseInt(c.substring(0, c.indexOf("日"))) > lastDay) {// 如果大于当月最后一天 则使用最后一天
				newtime=time.substring(0, 8)+lastDay;
			}else{
				newtime=time.substring(0, 8)+c.substring(0, c.indexOf("日"));
			}
			Date endDate=sdf.parse(newtime);
			reportTaskPushSet.setReportTaskPushSetTime(date);
			reportTaskPushSet.setReportTaskPushSetEndTime(endDate);
			reportTaskPushSet.setReportTaskPushSetName(reportTaskPushSetName);
			reportTaskPushSet.setReportTaskPushSetCycle(reportTaskPushSetCycle);
			reportTaskPushSet.setReportTaskPushSetStatus(reportTaskPushSetStatus);
			reportTaskPushSet.setReportTaskPushSetType(reportTaskPushSetMethod);
			Integer indexId = null;
			if(reportTaskPushSetMethod==0){
				IndexTb idex=indexTbService.queryIdByName(reportTaskPushSetTemplate);
				indexId=idex.getIndexId();
				reportTaskPushSet.setReportTaskPushSetTempLateId(idex.getIndexId());
			}
			if(reportTaskPushSetMethod==1){
				ReportExcelTemplate reportExcelTemplate=reportExcelTemplateService.queryReportExcelTemplateByName(reportTaskPushSetTemplate);
				indexId=reportExcelTemplate.getIndexId();
				reportTaskPushSet.setReportTaskPushSetTempLateId(reportExcelTemplate.getReportExcelTemplateId());
			}
			reportTaskPushSet.setSysOrgCreateId(reportTaskPushSetOrgId);
			
			List<ReportTaskPushSet> queryReportTaskPushSets = reportTaskPushSetService.queryReportTaskPushSets(userDetails.getSys_org_id(), null);
			if (reportTaskPushSetId != null) {
				reportTaskPushSet.setReportTaskPushSetId(reportTaskPushSetId);
				ReportTaskPushSet queryReportTaskPushSetById = reportTaskPushSetService
						.queryReportTaskPushSetById(reportTaskPushSet.getReportTaskPushSetId());
				String oldReportTaskPushSetName = queryReportTaskPushSetById.getReportTaskPushSetName();// 原名
				for (ReportTaskPushSet nowReportTaskPushSet : queryReportTaskPushSets) {
					if (reportTaskPushSetName.equals(nowReportTaskPushSet.getReportTaskPushSetName())) {
						// 如果修改名称为原名 则跳过
						if (oldReportTaskPushSetName.equals(reportTaskPushSet.getReportTaskPushSetName())) {
							continue;
						}
						jrw.setFlag(false);
						jrw.setMessage("操作失败 任务名称已存在");
						SysManageLog sysManageLog=new SysManageLog();
						sysManageLog.setSysManageLogMenuName("任务管理");
						sysManageLog.setIndexId(indexId);
						sysManageLog.setSysManageLogCount(1);
						sysManageLog.setSysManageLogOperateType(3);
						sysManageLog.setSysManageLogResult(false);
						sysManageLogService.insertSysManageLogTb(sysManageLog,request);
						return jrw;
					}
				}
				ReportTaskPushSet rep=reportTaskPushSetService.queryReportTaskPushSetById(reportTaskPushSetId);
				String name=rep.getReportTaskPushSetName();
				String cycle=rep.getReportTaskPushSetCycle();
				Integer ststus=rep.getReportTaskPushSetStatus();
				Integer type=rep.getReportTaskPushSetType();
				Integer tempLateId=rep.getReportTaskPushSetTempLateId();
				String ids=rep.getSysOrgExcuteIds();
				String tempLateName=null;
				if(type==0){
					IndexTb idex=indexTbService.queryById(tempLateId);
					tempLateName=idex.getIndexName();
				}
				if(type==1){
					ReportExcelTemplate reportExcelTemplate=reportExcelTemplateService.queryReportExcelTemplateById(tempLateId);
					tempLateName=reportExcelTemplate.getReportExcelTemplateName();
				}
				String [] oldOrgExcuteIds=ids.split(",");
				StringBuffer sb=new StringBuffer();
				for (String oldOrgExcuteId : oldOrgExcuteIds) {
					sb.append(sysOrgService.queryInstitutionsById(Integer.parseInt(oldOrgExcuteId)).getSys_org_name()+",");
				}
				sb.deleteCharAt(sb.length()-1);
				String [] newOrgExcuteIds=orgIds;
				StringBuffer sbr=new StringBuffer();
				for (String newOrgExcuteId : newOrgExcuteIds) {
					sbr.append(sysOrgService.queryInstitutionsById(Integer.parseInt(newOrgExcuteId)).getSys_org_name()+",");
				}
				sbr.deleteCharAt(sbr.length()-1);
				String oldValue="任务名称:"+name+",任务周期:"+cycle+",任务状态:"+(ststus==0?"有效":"禁用")+",任务报送类型:"+(type==0?"报文报送":"excel报送")+",模板名称:"+tempLateName+",任务负责机构:"+sb;
				String newValue="任务名称:"+reportTaskPushSetName+",任务周期:"+reportTaskPushSetCycle+",任务状态:"+(reportTaskPushSetStatus==0?"有效":"禁用")+",任务报送类型:"+(reportTaskPushSetMethod==0?"报文报送":"excel报送")+",模板名称:"+reportTaskPushSetTemplate+",任务负责机构:"+sbr;
				SysManageLog sysManageLog=new SysManageLog();
				sysManageLog.setSysManageLogMenuName("任务管理");
				sysManageLog.setIndexId(indexId);
				sysManageLog.setSysManageLogOldValue(oldValue);
				sysManageLog.setSysManageLogNewValue(newValue);
				sysManageLog.setSysManageLogCount(1);
				sysManageLog.setSysManageLogOperateType(3);
				sysManageLog.setSysManageLogResult(true);
				sysManageLogService.insertSysManageLogTb(sysManageLog,request);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("reportTaskPushSetId", reportTaskPushSetId);
				List<ReportTaskPushList> queryReportTaskPushLists = reportTaskPushListService
						.queryReportTaskPushLists(map, null);
				if (queryReportTaskPushLists != null) {
					for (int i = 0; i < queryReportTaskPushLists.size(); i++) {
						Integer statu=queryReportTaskPushLists.get(i).getReportTaskPushStatus();
						if(statu==0){//当修改时，删除任务列表未完成的任务
							reportTaskPushListService.delete(queryReportTaskPushLists.get(i).getReportTaskPushListId());
						}
					}	
				}
			} else {
				for (ReportTaskPushSet nowReportTaskPushSet : queryReportTaskPushSets) {
					if ((nowReportTaskPushSet.getReportTaskPushSetName()).equals(reportTaskPushSet.getReportTaskPushSetName())) {
						jrw.setFlag(false);
						jrw.setMessage("操作失败 任务名称已存在");
						SysManageLog sysManageLog=new SysManageLog();
						sysManageLog.setSysManageLogMenuName("任务管理");
						sysManageLog.setIndexId(indexId);
						sysManageLog.setSysManageLogCount(1);
						sysManageLog.setSysManageLogOperateType(1);
						sysManageLog.setSysManageLogResult(false);
						sysManageLogService.insertSysManageLogTb(sysManageLog,request);
						return jrw;
					}
				}
				SysManageLog sysManageLog=new SysManageLog();
				sysManageLog.setSysManageLogMenuName("任务管理");
				sysManageLog.setIndexId(indexId);
				sysManageLog.setSysManageLogCount(1);
				sysManageLog.setSysManageLogOperateType(1);
				sysManageLog.setSysManageLogResult(true);
				sysManageLogService.insertSysManageLogTb(sysManageLog,request);
			}
			reportTaskPushSetService.updateOrSave(reportTaskPushSet);
			if (reportTaskPushSet.getReportTaskPushSetStatus() == 0) {// 有效时添加到任务列表
				try {
					addTashPushList(orgIds, reportTaskPushSet);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		jrw.setMessage("操作成功");
		return jrw;
	}

	private void addTashPushList(String[] orgIds, ReportTaskPushSet reportTaskPushSet) throws ParseException {
		for (int i = 0; i < orgIds.length; i++) {
			// 为任务列表添加值
			ReportTaskPushList reportTaskPushList = new ReportTaskPushList();
			reportTaskPushList.setReportTaskPushSetId(reportTaskPushSet.getReportTaskPushSetId());
			reportTaskPushList.setReportTaskPushListEndTime(reportTaskPushSet.getReportTaskPushSetEndTime());
			reportTaskPushList.setSysOrgId(Integer.parseInt(orgIds[i].trim()));
			reportTaskPushList.setReportTaskPushStatus(0);
			reportTaskPushListService.updateOrSave(reportTaskPushList);
		}
	}

	/*@RequestMapping("/getChildOrgJson")
	@ResponseBody
	public Map<String, Object> getChildOrgJson(Integer orgId) {

		Map<String, Object> map = new HashMap<>();
		SysOrg org = sysOrgService.queryInstitutionsById(orgId);
		List<SysOrg> subSysOrg = org.getSubSysOrg();
		if (CollectionUtils.isEmpty(subSysOrg) == false) {
			map.put("childOrgList", subSysOrg);
		}

		return map;
	}*/

	//得到结束时间
	public  Date getEndDate(Date beginTimes, String reportTaskPushSetCycle) throws ParseException{
		String cycle = reportTaskPushSetCycle.split("-")[0];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = null;
		if (cycle.equals("无")) {
			
		} else if (cycle.indexOf("季") != -1) {
			String season = cycle.substring(cycle.indexOf("季") + 1,cycle.indexOf("季") + 3 );// 几月
			String day = cycle.substring(cycle.indexOf("季") + 3, cycle.indexOf("日"));// 几日
			String format = sdf.format(beginTimes);
			String year = format.substring(0, format.indexOf("-"));
			String month=format.substring(format.indexOf("-")+1,format.lastIndexOf("-"));
			String date=year+"-"+month+"-"+day;
			String newDate=year+"-"+month+"-"+"01";
			parse=sdf.parse(newDate);
			Integer lastDay =null;
			if("季初".equals(season)){
				lastDay=getLastDay(parse);
			}else if("季中".equals(season)){
				lastDay=getLastDay(subMonth(parse, 1));
				parse=subMonth(parse, 1);
				
			}else if("季末".equals(season)){
				lastDay=getLastDay(subMonth(parse, 1));
				parse=subMonth(parse, 2);
			}
			
			date=sdf.format(parse);
			if (Integer.parseInt(day) > lastDay) {// 如果大于当月最后一天 则使用最后一天
				date=date.substring(0, date.lastIndexOf("-")+1)+lastDay;
			}else{
				date=date.substring(0, date.lastIndexOf("-")+1)+day;
			}
			parse=sdf.parse(date);
		}/* else if (cycle.indexOf("周") != -1) {
			cycle = cycle.substring(cycle.indexOf("周") + 1);// 周几
			int weekDay = getWeekDay(cycle);
			int weekOfDate = getWeekOfDate(beginTimes);
			int num = weekDay - weekOfDate;
			if (num<= -1) {
				num= num+7;
			}
			Date now = new Date();
			LocalDate localDate=now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			Date newDate=java.sql.Date.valueOf(localDate);
			parse= subDay(beginTimes, num);
		}*/ else if (cycle.indexOf("月") != -1) {
			cycle = cycle.substring(cycle.indexOf("月") + 1, cycle.indexOf("日"));// 每月几日
			int lastDay = getLastDay(beginTimes);
			String date =null;
			Calendar cal = Calendar.getInstance();
			cal.setTime(beginTimes);
			//下面的就是把当前日期加一个月
			//cal.add(Calendar.MONTH, 1);
			String format = sdf.format(cal.getTime());
			String year = format.substring(0, format.indexOf("-"));
			format = format.substring(format.indexOf("-") + 1);
			String month = format.substring(0, format.indexOf("-"));
			if (Integer.parseInt(cycle) > lastDay) {// 如果大于本月最后一天
													// 则使用最后一天
				date = year + "-" + month + "-" + lastDay;
			} else {
				date = year + "-" + month + "-" + cycle;
			}
			parse = sdf.parse(date);
		}else if(cycle.indexOf("单") != -1){
			cycle = cycle.substring(cycle.indexOf("次") + 1, cycle.indexOf("日"));// 每月几日
			int lastDay = getLastDay(beginTimes);
			String date =null;
			String format = sdf.format(beginTimes);
			String year = format.substring(0, format.indexOf("-"));
			format = format.substring(format.indexOf("-") + 1);
			String month = format.substring(0, format.indexOf("-"));
			if (Integer.parseInt(cycle) > lastDay) {// 如果大于本月最后一天
													// 则使用最后一天
				date = year + "-" + month + "-" + lastDay;
			} else {
				date = year + "-" + month + "-" + cycle;
			}
			parse = sdf.parse(date);
		}
		return parse;
	}
	/**
	 * 根据日期获得星期
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfDate(Date date) {
		int[] weekDaysCode = { 7, 1, 2, 3, 4, 5, 6 };// 0表示周日
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysCode[intWeek];
	}

	/**
	 * 某月最后一天日期
	 *
     */
	public int getLastDay(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String format = sdf.format(date);
		String year = format.substring(0, format.indexOf("-"));
		format = format.substring(format.indexOf("-") + 1);
		String month = format.substring(0, format.indexOf("-"));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);// 月份0开始算
		int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
		return dateOfMonth;
	}

	/**
	 * 传入具体日期 ，返回具体日期加x个月。
	 */
	public static Date subMonth(Date date, int month) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.MONTH, +month);
		Date dt1 = rightNow.getTime();
		return dt1;
	}

	/**
	 * 传入具体日期 ，返回具体日期加x日。
	 */
	public static Date subDay(Date date, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, day);// 把日期往后增加.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推的结果
		return date;
	}

	public static int getWeekDay(String weekDay) {
		int week = 0;
		if (weekDay.equals("日")) {
			week = 7;
		} else if (weekDay.equals("一")) {
			week = 1;
		} else if (weekDay.equals("二")) {
			week = 2;
		} else if (weekDay.equals("三")) {
			week = 3;
		} else if (weekDay.equals("四")) {
			week = 4;
		} else if (weekDay.equals("五")) {
			week = 5;
		} else if (weekDay.equals("六")) {
			week = 6;
		}
		return week;
	}

}
