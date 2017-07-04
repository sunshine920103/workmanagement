package com.workmanagement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.util.StringUtil;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.ReportIndex;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOrgType;
import com.workmanagement.model.SysRole;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.service.IndexTbService;
import com.workmanagement.service.ReportExcelTemplateService;
import com.workmanagement.service.ReportIndexService;
import com.workmanagement.service.ReportedDeleteService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOrgTypeService;
import com.workmanagement.service.SysRoleService;
import com.workmanagement.service.SysUserLogService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;

/**
 * 
 * @author renyang
 *	已报数据查询
 */
@Controller
@RequestMapping("/admin/reportedDataList")
public class ReportDateListController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private IndexTbService indexTbService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysOrgService  sysOrgService;
	@Autowired
	private SysAreaService  sysAreaService; 
	@Autowired
	private ReportedDeleteService reportedDeleteService;
	@Autowired
	private ReportIndexService reportIndexService;
	@Autowired
	private SysUserLogService sysUserLogService;
	@Autowired
	private ReportExcelTemplateService reportExcelTemplateService;
	@Autowired
	private SysOrgTypeService sysOrgTypeService;
	@RequestMapping("/getOrgByName")
	@ResponseBody
	public List<SysOrg> getOrgByName(@RequestParam(required=false) String name){
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		Integer roleId=userDetails.getSys_role_id();
		SysRole sysRole=sysRoleService.querySystemRoleById(roleId);
		Integer roleType=sysRole.getSys_role_type();
		Map<String, Object> param = new HashMap<String, Object>();
		List<SysOrg> sysOrgList=new ArrayList<SysOrg>();
		//缓存单个机构
			String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
			SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
			if(so==null){
				so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
				RedisUtil.setData(orgKey, so);
			}
			if(roleType==6){
				String orgSbKey = RedisKeys.SYS_ORG_SB+ so.getSys_org_id();
				StringBuffer sbr = RedisUtil.getObjData(orgSbKey, StringBuffer.class);
				if(sbr==null){
					sbr=new StringBuffer();
					DataUtil.getChildOrgIds(so, sbr);//得到机构的ID及子机构的ID
					//设置地区ID集合缓存
					RedisUtil.setData(orgSbKey, sbr);
				}
				param.put("orgIds", sbr.toString().split(","));
				param.put("orgName", name);
				param.put("upid", true);
				sysOrgList=sysOrgService.queryInstitution(param);
			}
			if(roleType==3||roleType==1){
				if(so.getSys_area_id()!=null){
					//获取地区缓存
					String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
					StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
					if(sb==null){
						sb = new StringBuffer();
						SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
						DataUtil.getChildAreaIds(sysArea, sb);
						//设置地区ID集合缓存
						RedisUtil.setData(areaSbKey, sb);
					}
					param.put("area_id", sb.toString().split(","));//区域id
					param.put("orgName", name);
					param.put("upid", true);
				}
				sysOrgList=sysOrgService.queryInstitution(param);
			}
			return sysOrgList;
	}
	/**
	 * 查询选中的已报数据的详细信息
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/show")
	public String show(Model model,@RequestParam(required=false) Integer id,HttpServletRequest req) throws Exception{
		if(id!=null){
			String sql="SELECT * FROM report_index_tb rtb "+
					" WHERE report_index_id="+id;
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("sql", sql);
			List<Map<String, Object>> reportIndex=reportedDeleteService.getPage(null,null,sqlMap);
			model.addAttribute("reportIndexs", reportIndex);
			ReportIndex  report=reportIndexService.queryReportIndexsById(id);
			Integer indexId=null;
			if(report.getReportIndexMethod()==0){
				indexId=indexTbService.queryIdByName(report.getReportIndexTemplate()).getIndexId();
			}
			if(report.getReportIndexMethod()==1){
				indexId=reportExcelTemplateService.queryReportExcelTemplateByName(report.getReportIndexTemplate()).getIndexId();
			}
			SysUserLog sysUserLog=new SysUserLog();
			sysUserLog.setSysUserLogMenuName("已报数据");
			sysUserLog.setIndexId(indexId);
			sysUserLog.setSysUserLogCount(1);
			sysUserLog.setSysUserLogOperateType(4);
			sysUserLog.setSysUserLogResult(true);
			sysUserLog.setSysUserLogQuerySql(sql);
			sysUserLog.setSysUserLogUrl("/admin/reportedDataList/show.jhtml?id="+id);
			sysUserLogService.insertOneLog(sysUserLog,req);
		}
		return "reportedDataList/show";
	}
	/**
	 * list页面，查询出已报送的所有数据
	 * @throws Exception 
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model,@RequestParam(required=false) Integer orgId, 
																@RequestParam(required=false) String indexName,
																@RequestParam(required=false) String beginTime,
																@RequestParam(required=false) String endTime,
																@RequestParam(required=false) Integer url
			) throws Exception {
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer roleId=userDetails.getSys_role_id();
		SysRole sysRole=sysRoleService.querySystemRoleById(roleId);
		Integer roleType=sysRole.getSys_role_type();
		String orgName=null;
		//缓存单个机构
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		String sql1="";
		if(roleType==3||roleType==1){//根据用户类型判断用户的权限
			Integer affiliationAreaId=so.getSys_org_affiliation_area_id();
			Integer areaId=so.getSys_area_id();
			Map<String, Object> param = new HashMap<String, Object>();
			List<Integer> is=new ArrayList<Integer>();
			if(affiliationAreaId==null){
				 is = sysOrgService.queryInstitutionId(param);
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
				param.put("area_id", sb.toString().split(","));
				is = sysOrgService.queryInstitutionId(param);
			}else{
				param.put("affiliationArea", affiliationAreaId);
				is = sysOrgService.queryInstitutionId(param);
			}
			/*Map<String, Object> param = new HashMap<String, Object>();
			if (so != null) {
				if (so.getSys_area_id() != null) { // 如果机构的区域不为null，
															// 则根据机构的区域地址查询机构及子机构
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
					param.put("area_id", sb.toString().split(","));// 区域id
					param.put("upid", true);
				}
			}
			String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
			StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
			sb.deleteCharAt(sb.length()-1);
			List<SysOrg> is = sysOrgService.queryInstitution(param);
			model.addAttribute("is", is);*/
			String a ="";
			if(affiliationAreaId!=null){
				for (int i = 0; i < is.size(); i++) {
					a+=is.get(i)+",";
				}
				a =a.substring(0,a.length()-1);
			}
			if(StringUtil.isNotEmpty(beginTime)&&StringUtil.isNotEmpty(endTime)){
				sql1="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'";
				if(orgId!=null){
					sql1="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+"AND rtb.sys_org_id="+orgId;
					orgName=sysOrgService.getByIdNotHaveSub(orgId).getSys_org_name();
					
				}
				if(!"请选择".equals(indexName)){
					sql1="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+"AND rtb.report_index_template='"+indexName+"'";
				}
				if(orgId!=null&&!"请选择".equals(indexName)){
					sql1="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+
						 "AND rtb.sys_org_id="+orgId+" AND rtb.report_index_template='"+indexName+"'";
				}
			}
			String sql="";
			if(affiliationAreaId==null){
				sql="SELECT * FROM report_index_tb rtb "+
						" LEFT JOIN sys_org_tb otb ON rtb.sys_org_id=otb.sys_org_id"+
						" WHERE (rtb.report_index_status=0 OR rtb.report_index_status=2)"+sql1+
						" ORDER BY rtb.REPORT_INDEX_TIME desc";
			}else{
				sql="SELECT * FROM report_index_tb rtb "+
						" LEFT JOIN sys_org_tb otb ON rtb.sys_org_id=otb.sys_org_id"+
						" WHERE rtb.sys_org_id IN("+a+") AND (rtb.report_index_status=0 OR rtb.report_index_status=2)"+sql1+
						" ORDER BY rtb.REPORT_INDEX_TIME desc";
			}
			
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("sql", sql);
			PageSupport ps = PageSupport.initPageSupport(request);
			List<Map<String, Object>> reportIndex=reportedDeleteService.getPage(ps,"page",sqlMap);
			List<Map<String, Object>> report=reportedDeleteService.getPage(null,null,sqlMap);
			model.addAttribute("reportIndexs", reportIndex);
			if(StringUtil.isNotEmpty(beginTime)&&StringUtil.isNotEmpty(endTime)){
				if(url==null){
					SysUserLog sysUserLog=new SysUserLog();
					sysUserLog.setSysUserLogMenuName("已报数据");
					if(report!=null&&report.size()>0){
						sysUserLog.setSysUserLogCount(report.size());
					}else{
						sysUserLog.setSysUserLogCount(0);
					}
					sysUserLog.setSysUserLogQueryUserCondition("开始时间："+beginTime+",结束时间:"+endTime+(indexName==null||"请选择".equals(indexName)?"":(",模板名称或指标大类："+indexName))+(orgId==null?"":(",机构："+orgName)));
					sysUserLog.setSysUserLogOperateType(4);
					sysUserLog.setSysUserLogResult(true);
					sysUserLog.setSysUserLogQuerySql(sql);
					sysUserLog.setSysUserLogUrl("/admin/reportedDataList/list.jhtml?beginTime="+beginTime+"&endTime="+endTime+"&orgName="+orgName+"&indexName="+indexName+"&url=1");
					sysUserLogService.insertOneLog(sysUserLog,request);
					model.addAttribute("url", 1);
				}else{
					model.addAttribute("url", 1);
				}
			}
		}
		if(roleType==6){
			String orgSbKey = RedisKeys.SYS_ORG_SB+ so.getSys_org_id();
			StringBuffer sbr = RedisUtil.getObjData(orgSbKey, StringBuffer.class);
			if(sbr==null){
				sbr=new StringBuffer();
				DataUtil.getChildOrgIds(so, sbr);//得到机构的ID及子机构的ID
				//设置地区ID集合缓存
				RedisUtil.setData(orgSbKey, sbr);
			}
			sbr.deleteCharAt(sbr.length()-1);
			if(StringUtil.isNotEmpty(beginTime)&&StringUtil.isNotEmpty(endTime)){
				sql1="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'";
				if(orgId!=null){
					sql1="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+"AND rtb.sys_org_id="+orgId;
					orgName=sysOrgService.getByIdNotHaveSub(orgId).getSys_org_name();
				}
				if(!"请选择".equals(indexName)){
					sql1="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+"AND rtb.report_index_template='"+indexName+"'";
				}
				if(orgId!=null&&!"请选择".equals(indexName)){
					sql1="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+
						 "AND rtb.sys_org_id="+orgId+" AND rtb.report_index_template='"+indexName+"'";
				}
			}
			String sql="SELECT * FROM report_index_tb rtb "+
					" LEFT JOIN sys_org_tb otb ON rtb.sys_org_id=otb.sys_org_id"+
					" WHERE otb.sys_org_id IN("+sbr+") AND (rtb.report_index_status=0 OR rtb.report_index_status=2) "+sql1+
					" ORDER BY rtb.REPORT_INDEX_TIME desc";
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("sql", sql);
			PageSupport ps = PageSupport.initPageSupport(request);
			List<Map<String, Object>> reportIndex=reportedDeleteService.getPage(ps,"page",sqlMap);
			List<Map<String, Object>> report=reportedDeleteService.getPage(null,null,sqlMap);
			model.addAttribute("reportIndexs", reportIndex);
			if(StringUtil.isNotEmpty(beginTime)&&StringUtil.isNotEmpty(endTime)){
				if(url==null){
					SysUserLog sysUserLog=new SysUserLog();
					sysUserLog.setSysUserLogMenuName("已报数据");
					if(report!=null&&report.size()>0){
						sysUserLog.setSysUserLogCount(report.size());
					}else{
						sysUserLog.setSysUserLogCount(0);
					}
					sysUserLog.setSysUserLogQueryUserCondition("开始时间："+beginTime+",结束时间:"+endTime+(indexName==null||"请选择".equals(indexName)?"":(",模板名称或指标大类："+indexName))+(orgId==null?"":(",机构："+orgName)));
					sysUserLog.setSysUserLogOperateType(4);
					sysUserLog.setSysUserLogResult(true);
					sysUserLog.setSysUserLogQuerySql(sql);
					sysUserLog.setSysUserLogUrl("/admin/reportedDataList/list.jhtml?beginTime="+beginTime+"&endTime="+endTime+"&orgName="+orgName+"&indexName="+indexName+"&url=1");
					sysUserLogService.insertOneLog(sysUserLog,request);
					model.addAttribute("url", 1);
				}else{
					model.addAttribute("url", 1);
				}
			}
		}
		String sql="SELECT  tb.report_excel_template_name AS REPORT_INDEX_TEMPLATE FROM report_excel_template_tb tb WHERE tb.report_excel_template_status=0"+
					" UNION SELECT  itb.INDEX_NAME AS REPORT_INDEX_TEMPLATE FROM INDEX_TB itb WHERE itb.INDEX_USED=1";
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("sql", sql);
		List<Map<String, Object>> template= reportedDeleteService.queryResult(sqlMap);
		model.addAttribute("template",template);
		model.addAttribute("orgId",orgId);
		model.addAttribute("orgName",orgName);
		model.addAttribute("indexName",indexName);
		model.addAttribute("beginTime",beginTime);
		model.addAttribute("endTime",endTime);
		model.addAttribute("roleType",roleType);
		return "ReportedDataList/list";
	}
	
		
}
