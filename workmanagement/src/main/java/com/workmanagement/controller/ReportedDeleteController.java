package com.workmanagement.controller;

import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.*;
import com.workmanagement.service.*;
import com.workmanagement.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 报送数据删除
 * 
 * @author renyang
 *
 */
@Controller
@RequestMapping("/admin/reportedDelete ")
public class ReportedDeleteController {
	@Autowired
    IndexItemTbService indexItemTbService;
    @Autowired
    private SysOrgService sysOrgService;
	@Autowired
	private ReportIndexService reportIndexService;
	@Autowired
	private SysOrgTypeService sysOrgTypeService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private ReportExcelTemplateService reportExcelTemplateService;
	@Autowired
	private IndexTbService indexTbService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private ReportedDeleteService reportedDeleteService;
	@Autowired
	private SysUserLogService sysUserLogService;
	@Autowired
	private AdminObjService adminObjService;

	@RequestMapping("/down")
	public void down(HttpServletResponse response, HttpServletRequest request,
			 @RequestParam(required=false) Integer reportIndexId,@RequestParam(required=false) String reportIndexName,
			 @RequestParam(required=false) String menuName
			) throws Exception{
		ReportIndex  reportIndex =reportIndexService.queryReportIndexsById(reportIndexId);
		String fileName=reportIndex.getReportIndexPath();
		if(fileName!=null){
			DownLoadFile.downLoadFile(fileName, reportIndexName, request, response);
			Integer indexId=null;
			if(reportIndex.getReportIndexMethod()==0){
				indexId=indexTbService.queryIdByName(reportIndex.getReportIndexTemplate()).getIndexId();
			}
			if(reportIndex.getReportIndexMethod()==1){
				indexId=reportExcelTemplateService.queryReportExcelTemplateByName(reportIndex.getReportIndexTemplate()).getIndexId();
			}
			SysUserLog sysUserLog=new SysUserLog();
			sysUserLog.setSysUserLogMenuName(menuName);
			sysUserLog.setIndexId(indexId);
			sysUserLog.setSysUserLogOperateType(6);
			sysUserLog.setSysUserLogResult(true);
			sysUserLog.setSysUserLogFile(fileName);
			sysUserLogService.insertOneLog(sysUserLog,request);
		}

	}

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
					DataUtil.getChildOrgIds(so, sbr);//得到机构的ID及子机构的ID
					//设置机构ID集合缓存
					RedisUtil.setData(orgSbKey, sbr);
				}
				param.put("orgIds", sbr.toString().split(","));
				param.put("orgName", name);
				sysOrgList=sysOrgService.querySysOrg(param);
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
				sysOrgList=sysOrgService.querySysOrg(param);
			}
			return sysOrgList;
	}

	@RequestMapping("/getIndexByName")
	@ResponseBody
	public List<IndexTb> getIndexByName(@RequestParam(required=false) String name){
		Map<String,Object> map= new HashMap<String,Object>();
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        StringBuffer areaId=new StringBuffer();
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		SysArea area = sysAreaService.queryAreaById(so.getSys_area_id());
		DataUtil.getChildAreaIds(area, areaId);
		//map.put("areaId", areaId.toString().split(","));
		map.put("indexName", name);
		List<IndexTb> indexList=indexTbService.queryIndex(map);
			return indexList;
	}

	@RequestMapping("/list")
    public String index(HttpServletRequest request, Model model, @RequestParam(required = false) Integer orgId,
                        @RequestParam(required = false) Integer indexId,
                        @RequestParam(required = false) String beginTime,
                        @RequestParam(required = false) String endTime,
                        @RequestParam(required = false) String orgCode,
                        @RequestParam(required = false) Integer url
    ) throws Exception {
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer roleId=userDetails.getSys_role_id();
		SysRole sysRole=sysRoleService.querySystemRoleById(roleId);
		Integer roleType=sysRole.getSys_role_type();
		String indexName=null;
		String orgName=null;

		//缓存单个机构
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
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
           // Map<String, Object> param = new HashMap<String, Object>();
			/*if (so != null) {
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
			List<SysOrg> is = sysOrgService.queryInstitution(param);*/
			String a ="";
			if(affiliationAreaId!=null){
				for (int i = 0; i < is.size(); i++) {
					a+=is.get(i)+",";
				}
				a =a.substring(0,a.length()-1);
			}
			String sql3="";
			if(StringUtil.isNotEmpty(beginTime)&&StringUtil.isNotEmpty(endTime)&&(StringUtil.isEmpty(orgCode)||"".equals(orgCode.trim()))){
				sql3="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'";
				if(indexId!=null&&orgId==null){
					IndexTb indexTb=indexTbService.queryById(indexId);
					String name=indexTb.getIndexName();
					indexName=name;
					if("".equals(a)){
						sql3="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+
								"AND ( rtb.report_index_template='"+name+"' OR ttb.index_id="+indexId+")";
					}else{
						sql3="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+
								"AND ( rtb.report_index_template='"+name+"' OR ttb.index_id="+indexId+")"+
								"AND rtb.sys_org_id IN("+a+")";
					}
					
				}
				/*if(orgId!=null){
					sql3="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+"AND rtb.sys_org_id="+orgId;
					orgName=sysOrgService.queryInstitutionsById(orgId).getSys_org_name();
				}else{
					sql3="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+"AND rtb.sys_org_id in("+a+")";
				}*/
				if(indexId!=null&&orgId!=null){
					IndexTb indexTb=indexTbService.queryById(indexId);
					String name=indexTb.getIndexName();
					indexName=name;
					sql3="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+
						"AND ( rtb.report_index_template='"+name+"' OR ttb.index_id="+indexId+")"+
						"AND rtb.sys_org_id="+orgId;
				}
				String sql="SELECT * FROM report_index_tb rtb "+
						" LEFT JOIN sys_org_tb otb ON rtb.sys_org_id=otb.sys_org_id"+
						" LEFT JOIN report_excel_template_tb ttb ON rtb.report_index_template=ttb.report_excel_template_name"+
						" WHERE (rtb.report_index_status=0 OR rtb.report_index_status=2)"+sql3+
						" ORDER BY rtb.REPORT_INDEX_TIME desc";
				Map<String, Object> sqlMap = new HashMap<String, Object>();
				sqlMap.put("sql", sql);
				PageSupport ps = PageSupport.initPageSupport(request);
				List<Map<String, Object>> reportIndex=reportedDeleteService.getPage(ps,"page",sqlMap);
				List<Map<String, Object>> report=reportedDeleteService.queryResult(sqlMap);
				model.addAttribute("reportIndexs", reportIndex);
				if(url==null){
					SysUserLog sysUserLog=new SysUserLog();
					sysUserLog.setSysUserLogMenuName("数据删除");
					sysUserLog.setIndexId(indexId);
					if(report!=null&&report.size()>0){
						sysUserLog.setSysUserLogCount(report.size());
					}else{
						sysUserLog.setSysUserLogCount(0);
					}
					sysUserLog.setSysUserLogQueryUserCondition("开始时间："+beginTime+",结束时间:"+endTime+(indexId==null?"":(",指标大类："+indexName))+(orgId==null?"":(",机构："+orgName)));
					sysUserLog.setSysUserLogOperateType(4);
					sysUserLog.setSysUserLogResult(true);
					sysUserLog.setSysUserLogQuerySql(sql);
					sysUserLog.setSysUserLogUrl("/admin/reportedDelete/list.jhtml?beginTime="+beginTime+"&endTime="+endTime+"&orgName="+orgName+"&indexName="+indexName+"&url=1");
					sysUserLogService.insertOneLog(sysUserLog,request);
					model.addAttribute("url", 1);
				}else{
					if(url==1){
						model.addAttribute("url", 1);
					}
				}
			}
			if(StringUtil.isNotEmpty(beginTime)&&StringUtil.isNotEmpty(endTime)&&StringUtil.isNotEmpty(orgCode)&&!"".equals(orgCode.trim())){
				if(indexId!=null){
					IndexTb indexTb=indexTbService.queryById(indexId);//根据指标大类id得到指标大类的对象
					String indexCode=indexTb.getIndexCode();//得到动态生成表的表名
					indexName=indexTb.getIndexName();
					String sql1="";
					if(orgId!=null){
						sql1="AND ctb.sys_org_id="+orgId+" AND (dtb.code_credit= '"+orgCode+"' or dtb.code_org = '"+orgCode+"')";
					}else{
						if("".equals(a)){
							sql1="AND (dtb.code_credit= '"+orgCode+"' or dtb.code_org = '"+orgCode+"')";
						}else{
							sql1="AND ctb.sys_org_id in("+a+") AND (dtb.code_credit= '"+orgCode+"' or dtb.code_org = '"+orgCode+"')";
						}
						
					}
					//根据地区 等查询数据删除列表
						String sql="select ctb."+indexCode+"_id AS cid,otb.sys_org_name AS REPORT_INDEX_ORG_NAME,ctb.record_date AS report_index_time,ctb.submit_time AS report_index_submit_time,ctb.sys_org_id AS sys_org_id from "+indexCode+"_tb ctb"+
								" left join sys_org_tb otb on ctb.sys_org_id=otb.sys_org_id"+
								" left join default_index_item_tb dtb on dtb.default_index_item_id=ctb.default_index_item_id"+
								" WHERE ctb.record_date BETWEEN '"+beginTime+"' and '"+endTime+"'"+sql1+
								" ORDER BY ctb.record_date desc";
						Map<String, Object> sqlMap = new HashMap<String, Object>();
						sqlMap.put("sql", sql);
					//分页查询
					PageSupport ps = PageSupport.initPageSupport(request);
					List<Map<String, Object>> reportIndex=reportedDeleteService.getPage(ps,"page",sqlMap);
					List<Map<String, Object>> report=reportedDeleteService.queryResult(sqlMap);
					model.addAttribute("reportIndexs", reportIndex);
					if(url==null){
						SysUserLog sysUserLog=new SysUserLog();
						sysUserLog.setSysUserLogMenuName("数据删除");
						sysUserLog.setIndexId(indexId);
						if(report!=null&&report.size()>0){
							sysUserLog.setSysUserLogCount(report.size());
						}else{
							sysUserLog.setSysUserLogCount(0);
						}
						sysUserLog.setSysUserLogQueryUserCondition("开始时间："+beginTime+",结束时间:"+endTime+",企业二码："+orgCode+",指标大类："+indexName+(orgId==null?"":(",机构："+orgName)));
						sysUserLog.setSysUserLogOperateType(4);
						sysUserLog.setSysUserLogResult(true);
						sysUserLog.setSysUserLogQuerySql(sql);
						sysUserLog.setSysUserLogUrl("/admin/reportedDelete/list.jhtml?beginTime="+beginTime+"&endTime="+endTime+"&orgName="+orgName+"&indexName="+indexName+"&orgCode="+orgCode+"&url=1");
						sysUserLogService.insertOneLog(sysUserLog,request);
						model.addAttribute("url", 1);
					}else{
						model.addAttribute("url", 1);
					}
				}
			}
			if(StringUtil.isEmpty(beginTime)&&StringUtil.isEmpty(endTime)){
				String sql="";
				if("".equals(a)){
					sql="SELECT * FROM report_index_tb rtb "+
							" LEFT JOIN sys_org_tb otb ON rtb.sys_org_id=otb.sys_org_id"+
							" WHERE (rtb.report_index_status=0 OR rtb.report_index_status=2)"+
							" ORDER BY rtb.REPORT_INDEX_TIME desc";
				}else{
					sql="SELECT * FROM report_index_tb rtb "+
							" LEFT JOIN sys_org_tb otb ON rtb.sys_org_id=otb.sys_org_id"+
							" WHERE rtb.sys_org_id IN("+a+") AND (rtb.report_index_status=0 OR rtb.report_index_status=2)"+
							" ORDER BY rtb.REPORT_INDEX_TIME desc";
				}
				Map<String, Object> sqlMap = new HashMap<String, Object>();
				sqlMap.put("sql", sql);
				PageSupport ps = PageSupport.initPageSupport(request);
				List<Map<String, Object>> reportIndex=reportedDeleteService.getPage(ps,"page",sqlMap);
				model.addAttribute("reportIndexs", reportIndex);
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
			//Map<String, Object> map = new HashMap<String, Object>();
			//map.put("orgIds", sbr.toString().split(","));
			//map.put("upid", true);
			sbr.deleteCharAt(sbr.length()-1);
			/*List<SysOrg> is = sysOrgService.queryInstitution(map);//通过id查询机构列表
			String a ="";
			for (SysOrg sysOrg : is) {
				a+=sysOrg.getSys_org_id()+",";
			}
			a =a.substring(0,a.length()-1);
			model.addAttribute("is", is);*/
			String sql3="";
			if(StringUtil.isNotEmpty(beginTime)&&StringUtil.isNotEmpty(endTime)&&(StringUtil.isEmpty(orgCode)||"".equals(orgCode.trim()))){
				if(indexId!=null&&orgId!=null){
					IndexTb indexTb=indexTbService.queryById(indexId);
					String name=indexTb.getIndexName();
					indexName=name;
					sql3="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+
						"AND ( rtb.report_index_template='"+name+"' OR ttb.index_id="+indexId+")"+
						"AND rtb.sys_org_id="+orgId;
				}else if(indexId!=null&&orgId==null){
					IndexTb indexTb=indexTbService.queryById(indexId);
					String name=indexTb.getIndexName();
					indexName=name;
					sql3="AND rtb.report_index_time BETWEEN '"+beginTime+"' and '"+endTime+"'"+
						"AND ( rtb.report_index_template='"+name+"' OR ttb.index_id="+indexId+")"+
						"AND rtb.sys_org_id in("+sbr+")";
				}
				String sql="SELECT * FROM report_index_tb rtb "+
						" LEFT JOIN sys_org_tb otb ON rtb.sys_org_id=otb.sys_org_id"+
						" LEFT JOIN report_excel_template_tb ttb ON rtb.report_index_template=ttb.report_excel_template_name"+
						" WHERE (rtb.report_index_status=0 OR rtb.report_index_status=2)"+sql3+
						" ORDER BY rtb.REPORT_INDEX_TIME desc";
				Map<String, Object> sqlMap = new HashMap<String, Object>();
				sqlMap.put("sql", sql);
				PageSupport ps = PageSupport.initPageSupport(request);
				List<Map<String, Object>> reportIndex=reportedDeleteService.getPage(ps,"page",sqlMap);
				List<Map<String, Object>> report=reportedDeleteService.queryResult(sqlMap);
				model.addAttribute("reportIndexs", reportIndex);
				if(url==null){
					SysUserLog sysUserLog=new SysUserLog();
					sysUserLog.setSysUserLogMenuName("数据删除");
					sysUserLog.setIndexId(indexId);
					if(report!=null&&report.size()>0){
						sysUserLog.setSysUserLogCount(report.size());
					}else{
						sysUserLog.setSysUserLogCount(0);
					}
					sysUserLog.setSysUserLogQueryUserCondition("开始时间："+beginTime+",结束时间:"+endTime+(indexId==null?"":(",指标大类："+indexName))+(orgId==null?"":(",机构："+orgName)));
					sysUserLog.setSysUserLogOperateType(4);
					sysUserLog.setSysUserLogResult(true);
					sysUserLog.setSysUserLogQuerySql(sql);
					sysUserLog.setSysUserLogUrl("/admin/reportedDelete/list.jhtml?beginTime="+beginTime+"&endTime="+endTime+"&orgName="+orgName+"&indexName="+indexName+"&url=1");
					sysUserLogService.insertOneLog(sysUserLog,request);
					model.addAttribute("url", 1);
				}else{
					model.addAttribute("url", 1);
				}
			}
			if(StringUtil.isNotEmpty(beginTime)&&StringUtil.isNotEmpty(endTime)&&StringUtil.isNotEmpty(orgCode)&&!"".equals(orgCode.trim())){
				if(indexId!=null){
					IndexTb indexTb=indexTbService.queryById(indexId);//根据指标大类id得到指标大类的对象
					String indexCode=indexTb.getIndexCode();//得到动态生成表的表名
					indexName=indexTb.getIndexName();
					String sql1="";
					if(orgId!=null){
						sql1="AND ctb.sys_org_id="+orgId+" AND (dtb.code_credit= '"+orgCode+"' or dtb.code_org = '"+orgCode+"')";
					}else{
						sql1="AND ctb.sys_org_id in("+sbr+") AND (dtb.code_credit= '"+orgCode+"' or dtb.code_org = '"+orgCode+"')";

					}
					//根据地区 等查询数据删除列表
					 String sql=" select ctb."+indexCode+"_id AS cid,otb.sys_org_name AS REPORT_INDEX_ORG_NAME,ctb.record_date AS report_index_time,ctb.submit_time AS report_index_submit_time,ctb.sys_org_id AS sys_org_id from "+indexCode+"_tb ctb"+
								" left join sys_org_tb otb on ctb.sys_org_id=otb.sys_org_id"+
								" left join default_index_item_tb dtb on dtb.default_index_item_id=ctb.default_index_item_id"+
								" WHERE ctb.record_date BETWEEN '"+beginTime+"' and '"+endTime+"'"+sql1+
								" ORDER BY ctb.record_date desc";
						Map<String, Object> sqlMap = new HashMap<String, Object>();
						sqlMap.put("sql", sql);
					//分页查询
					PageSupport ps = PageSupport.initPageSupport(request);
					List<Map<String, Object>> reportIndex=reportedDeleteService.getPage(ps,"page",sqlMap);
					List<Map<String, Object>> report=reportedDeleteService.queryResult(sqlMap);
					model.addAttribute("reportIndexs", reportIndex);
					if(url==null){
						SysUserLog sysUserLog=new SysUserLog();
						sysUserLog.setSysUserLogMenuName("数据删除");
						sysUserLog.setIndexId(indexId);
						if(report!=null&&report.size()>0){
							sysUserLog.setSysUserLogCount(report.size());
						}else{
							sysUserLog.setSysUserLogCount(0);
						}
						sysUserLog.setSysUserLogQueryUserCondition("开始时间："+beginTime+",结束时间:"+endTime+",企业二码："+orgCode+",指标大类："+indexName+(orgId==null?"":(",机构："+orgName)));
						sysUserLog.setSysUserLogOperateType(4);
						sysUserLog.setSysUserLogResult(true);
						sysUserLog.setSysUserLogQuerySql(sql);
						sysUserLog.setSysUserLogUrl("/admin/reportedDelete/list.jhtml?beginTime="+beginTime+"&endTime="+endTime+"&orgName="+orgName+"&indexName="+indexName+"&orgCode="+orgCode+"&url=1");
						sysUserLogService.insertOneLog(sysUserLog,request);
						model.addAttribute("url", 1);
					}else{
						model.addAttribute("url", 1);
					}
				}
			}
			if(StringUtil.isEmpty(beginTime)&&StringUtil.isEmpty(endTime)){
				String sql="SELECT * FROM report_index_tb rtb "+
						" LEFT JOIN sys_org_tb otb ON rtb.sys_org_id=otb.sys_org_id"+
						" WHERE otb.sys_org_id IN("+sbr+") AND (rtb.report_index_status=0 OR rtb.report_index_status=2) "+
						" ORDER BY rtb.REPORT_INDEX_TIME desc";
				Map<String, Object> sqlMap = new HashMap<String, Object>();
				sqlMap.put("sql", sql);
				PageSupport ps = PageSupport.initPageSupport(request);
				List<Map<String, Object>> reportIndex=reportedDeleteService.getPage(ps,"page",sqlMap);
				model.addAttribute("reportIndexs", reportIndex);
			}
		}
		//获取地区缓存
		/*String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
		StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
		if(sb==null){
			sb=new StringBuffer();
			SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
			DataUtil.getChildAreaIds(sysArea, sb);
			//设置地区ID集合缓存
			RedisUtil.setData(areaSbKey, sb);
		}
		List<Integer> areaIdList=new ArrayList<Integer>();
		String [] Aid=sb.toString().split(",");
		for (int i = 0; i < Aid.length; i++) {
			areaIdList.add(Integer.parseInt(Aid[i]));
		}*/
		List<IndexTb> indexTbList=null;
		if(so.getSys_org_upid()!=null){
			indexTbList=indexTbService.queryAll2(null, so.getSys_area_id());
		}else{
			indexTbList=indexTbService.queryAll(null);
		}
		//List<IndexTb> indexTbList=indexTbService.queryIndexBySysAreaIds(null, areaIdList);
		model.addAttribute("indexTbList", indexTbList);
		model.addAttribute("orgId",orgId);
		if(orgId!=null){
			orgName=sysOrgService.getByIdNotHaveSub(orgId).getSys_org_name();
		}
		model.addAttribute("orgName",orgName);
		model.addAttribute("indexId",indexId);
		model.addAttribute("indexName",indexName);
		model.addAttribute("beginTime",beginTime);
		model.addAttribute("endTime",endTime);
		model.addAttribute("orgCode",orgCode);
		model.addAttribute("roleType",roleType);
		return "ReportedDelete/list";
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
					" LEFT JOIN sys_org_tb otb ON rtb.sys_org_id=otb.sys_org_id"+
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
			sysUserLog.setSysUserLogMenuName("数据删除");
			sysUserLog.setIndexId(indexId);
			sysUserLog.setSysUserLogCount(1);
			sysUserLog.setSysUserLogOperateType(4);
			sysUserLog.setSysUserLogResult(true);
			sysUserLog.setSysUserLogQuerySql(sql);
			sysUserLog.setSysUserLogUrl("/admin/reportedDelete/show.jhtml?id="+id);
			sysUserLogService.insertOneLog(sysUserLog,req);
		}
		return "ReportedDelete/show";
	}

    /**
     *
     *批量删除
     *
     */
	@RequestMapping("/batchDele")
	@ResponseBody
	public JsonResWrapper batchDele(HttpServletRequest request,Model model,
								@RequestParam(required=false) String []  rids ,
								@RequestParam(required=false) String []  cids ,
								@RequestParam(required=false) String []  orgIds ,
								@RequestParam(required=false) String []  indexIds ,
								@RequestParam(required=false) String []  templates,
								@RequestParam(required=false) String []  methods) throws Exception{
		JsonResWrapper jrw = new JsonResWrapper();
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer roleType=userDetails.getSysRole().getSys_role_type();
		Integer UserOrgId=userDetails.getSys_org_id();
		if(rids.length==0&&cids.length==0){
			jrw.setFlag(false);
			jrw.setMessage("请选择删除项");
			return jrw;
		}
		//机构管理员只能删除当前负责机构的所有数据
		if(roleType==6){
			for (int i = 0; i < orgIds.length; i++) {
				if(UserOrgId!=Integer.parseInt(orgIds[i].trim())){
					jrw.setFlag(false);
					jrw.setMessage("删除失败，只能删除自己负责机构");
					SysUserLog sysUserLog=new SysUserLog();
					sysUserLog.setSysUserLogMenuName("数据删除");
					sysUserLog.setSysUserLogOperateType(2);
					sysUserLog.setSysUserLogResult(false);
					sysUserLogService.insertOneLog(sysUserLog,request);
					return jrw;
				}
			}
		}
		//判断是否能够删除
		for (int i = 0; i < rids.length; i++) {
			if(!rids[i].trim().equals("")){
				String zbdlname=null;
				Integer id=null;
				Integer method=Integer.parseInt(methods[i].trim());
				if(method==0){
					zbdlname=templates[i].trim();
					id=indexTbService.queryIdByName(zbdlname).getIndexId();
				}
				if(method==1){
					ReportExcelTemplate reportExcelTemplate=reportExcelTemplateService.queryReportExcelTemplateByName(templates[i].trim());
						IndexTb index=indexTbService.queryById(reportExcelTemplate.getIndexId());
						id=reportExcelTemplate.getIndexId();
						zbdlname=index.getIndexName();
				}
				if("基本信息".equals(zbdlname)){
					jrw.setFlag(false);
					jrw.setMessage("删除失败,基本信息不能删除");
					SysUserLog sysUserLog=new SysUserLog();
					sysUserLog.setSysUserLogMenuName("数据删除");
					sysUserLog.setIndexId(id);
					sysUserLog.setSysUserLogOperateType(2);
					sysUserLog.setSysUserLogResult(false);
					sysUserLogService.insertOneLog(sysUserLog,request);
					return jrw;
				}
			}
		}
		//删除数据
		for (int i = 0; i < rids.length; i++) {
			if(!rids[i].trim().equals("")){
				String zbdlname=null;
				Integer id=null;
				Integer method=Integer.parseInt(methods[i].trim());
				if(method==0){
					zbdlname=templates[i].trim();
					id=indexTbService.queryIdByName(zbdlname).getIndexId();
				}
				if(method==1){
					ReportExcelTemplate reportExcelTemplate=reportExcelTemplateService.queryReportExcelTemplateByName(templates[i].trim());
						IndexTb index=indexTbService.queryById(reportExcelTemplate.getIndexId());
						id=reportExcelTemplate.getIndexId();
						zbdlname=index.getIndexName();
				}
				Integer rid=Integer.parseInt(rids[i].trim());
				ReportIndex reportIndex=reportIndexService.queryReportIndexsById(rid);
			    String date=DateFormatter.formatDate(reportIndex.getReportIndexTime());
			    Integer oid=reportIndex.getSysOrgId();
				IndexTb indexTb=indexTbService.queryIdByName(zbdlname);
				String indexCode=indexTb.getIndexCode();//得到动态生成表的表名
				String sql="select * from "+indexCode+"_tb ctb where ctb.record_date='"+date+"' AND ctb.sys_org_id="+oid;
				Map<String, Object> sqlMap = new HashMap<>();
				sqlMap.put("sql", sql);
				List<Map<String, Object>> list=reportedDeleteService.queryResult(sqlMap);
				String delSql="delete "+indexCode+"_tb ctb where ctb.record_date='"+date+"' AND ctb.sys_org_id="+oid;
				Map<String, Object> delMap = new HashMap<>();
				delMap.put("delete", delSql);
				Integer num= reportedDeleteService.deleteData(delMap);//删除真实数据
				for (int j = 0; j < num; j++) {
					SysUserLog sysUserLog=new SysUserLog();
					sysUserLog.setSysUserLogMenuName("数据删除");
					sysUserLog.setIndexId(id);
					sysUserLog.setSysUserLogOperateType(2);
					sysUserLog.setSysUserLogResult(true);
					sysUserLogService.insertOneLog(sysUserLog,request);
				}
				for (int j = 0; j < list.size(); j++) {
					Integer did=(Integer) list.get(j).get(indexCode.toUpperCase()+"_ID");
					Integer indexId=indexTb.getIndexId();
					List<AdminObjModel> adminObjServiceList=adminObjService.selectSysOperateIdByDataIdAndIndexItemId(did, indexId);
					for (int k = 0; k < adminObjServiceList.size(); k++) {
						Integer sysOperateId=adminObjServiceList.get(k).getSysOperateId();
						adminObjService.delOperateByDataId(sysOperateId);
					}
				}
				//String delSql1="delete report_index_tb where report_index_id="+rid;
				//delMap.put("delete", delSql1);
				//reportedDeleteService.deleteData(delMap);//删除记录表数据
				reportIndex.setReportIndexStatus(2);
				reportIndexService.updata(reportIndex);
			}
		}
		for(int j = 0; j < cids.length; j++){
			if(!cids[j].trim().equals("")){
				Integer cid=Integer.parseInt(cids[j].trim());
				Integer indexId=Integer.parseInt(indexIds[j].trim());
				String zbdl=indexTbService.queryById(indexId).getIndexName();
				String indexCode=indexTbService.queryById(indexId).getIndexCode();
				if("基本信息".equals(zbdl)){
					String sql="select DEFAULT_INDEX_ITEM_ID from "+indexCode+"_tb where "+indexCode+"_id = "+cid;
					Map<String, Object> map = new HashMap<>();
					map.put("sql", sql);
					List<Map<String, Object>> list=reportedDeleteService.queryResult(map);
					Integer defaultId=(Integer) list.get(0).get("DEFAULT_INDEX_ITEM_ID");
					List<IndexTb> indexTbs=indexTbService.queryAll(null);
					Map<String, Object> sqlMap = new HashMap<String, Object>();
					for (IndexTb indexTb : indexTbs) {
						String indexCodes=indexTb.getIndexCode();
						if("index_jbxx".equals(indexCodes)){
							continue;
						}
		                String sbsql="select * from "+indexCodes+"_tb where default_index_item_id="+defaultId;
		                sqlMap.put("sql", sbsql);
		                List<Map<String, Object>> report=reportedDeleteService.queryResult(sqlMap);
		                if(report!=null&&report.size()>0){
		                	jrw.setFlag(false);
							jrw.setMessage("删除失败,基本信息不能删除");
							SysUserLog sysUserLog=new SysUserLog();
							sysUserLog.setSysUserLogMenuName("数据删除");
							sysUserLog.setIndexId(indexId);
							sysUserLog.setSysUserLogOperateType(2);
							sysUserLog.setSysUserLogResult(false);
							sysUserLogService.insertOneLog(sysUserLog,request);
							return jrw;
		                }
					}
				}
			}
		}
		StringBuffer sb=new StringBuffer();
		for (int j = 0; j < cids.length; j++) {
			if(!cids[j].trim().equals("")){
				Integer cid=Integer.parseInt(cids[j].trim());
				Integer indexId=Integer.parseInt(indexIds[j].trim());
				String zbdl=indexTbService.queryById(indexId).getIndexName();
				String indexCode=indexTbService.queryById(indexId).getIndexCode();
				if("基本信息".equals(zbdl)){
					String sql="select DEFAULT_INDEX_ITEM_ID from "+indexCode+"_tb where "+indexCode+"_id = "+cid;
					Map<String, Object> map = new HashMap<>();
					map.put("sql", sql);
					List<Map<String, Object>> list=reportedDeleteService.queryResult(map);
					Integer defaultId=(Integer) list.get(0).get("DEFAULT_INDEX_ITEM_ID");
					sb.append(defaultId+",");
				}
				String sql="select * from "+indexCode+"_tb  where "+indexCode+"_id="+cid;
				Map<String, Object> sqlMap = new HashMap<>();
				sqlMap.put("sql", sql);
				List<Map<String, Object>> list=reportedDeleteService.queryResult(sqlMap);
				String delSql="delete "+indexCode+"_tb where "+indexCode+"_id="+cid;
				Map<String, Object> delMap = new HashMap<>();
				delMap.put("delete", delSql);
				Integer num= reportedDeleteService.deleteData(delMap);//删除真实数据
				if(num<=0){
					jrw.setFlag(false);
					jrw.setMessage("删除失败");
					SysUserLog sysUserLog=new SysUserLog();
					sysUserLog.setSysUserLogMenuName("数据删除");
					sysUserLog.setIndexId(indexId);
					sysUserLog.setSysUserLogOperateType(2);
					sysUserLog.setSysUserLogResult(false);
					sysUserLogService.insertOneLog(sysUserLog,request);
					return jrw;
				}
				for (int k = 0; k < list.size(); j++) {
					Integer did=(Integer) list.get(k).get(indexCode.toUpperCase()+"_ID");
					List<AdminObjModel> adminObjList= adminObjService.selectSysOperateIdByDataIdAndIndexItemId(did, indexId);
					for (AdminObjModel adminObjModel : adminObjList) {
						Integer sysOperateId=adminObjModel.getSysOperateId();
						adminObjService.delOperateByDataId(sysOperateId);
					}
				}
				SysUserLog sysUserLog=new SysUserLog();
				sysUserLog.setSysUserLogMenuName("数据删除");
				sysUserLog.setIndexId(indexId);
				sysUserLog.setSysUserLogOperateType(2);
				sysUserLog.setSysUserLogResult(true);
				sysUserLogService.insertOneLog(sysUserLog,request);
			}
		}
		if(sb.length()!=0){
			sb.deleteCharAt(sb.length()-1);
			String[] ids=sb.toString().split(",");
			Map<String, Object> map = new HashMap<>();
			Map<String, Object> sqlMap = new HashMap<>();
			for (int i = 0; i < ids.length; i++) {
				String sql1="select * from index_jbxx_tb where  default_index_item_id="+ids[i];
				map.put("sql", sql1);
				List<Map<String, Object>> lists=reportedDeleteService.queryResult(map);
				if(lists==null||lists.size()==0){
					sqlMap.put("delete", "delete from default_index_item_tb where  default_index_item_id = "+ids[i]);
					reportedDeleteService.deleteData(sqlMap);
				}
			}
			
		}
		jrw.setMessage("删除成功");
		return jrw;
	}

    /**
	 * 删除
     *
     * @param rid
	 * @return
     * @throws Exception
     */
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResWrapper delete(HttpServletRequest request,Model model,
													@RequestParam(required=false) Integer cid,
													@RequestParam(required=false) String  name,
													@RequestParam(required=false) Integer orgId,
													@RequestParam(required=false) Integer method,
													@RequestParam(required=false) Integer rid,
													@RequestParam(required=false) Integer indexId) throws Exception {
		JsonResWrapper jrw = new JsonResWrapper();
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer roleId=userDetails.getSys_role_id();
		Integer UserOrgId=userDetails.getSys_org_id();
		SysRole sysRole=sysRoleService.querySystemRoleById(roleId);
		Integer roleType=sysRole.getSys_role_type();
		if(cid==null&&rid==null){
			jrw.setFlag(false);
			jrw.setMessage("删除失败，参数缺失");
			return jrw;
		}
		String zbdlname=null;
		Integer id=null;
		if(method!=null){
			if(method==0){
				zbdlname=name;
				id=indexTbService.queryIdByName(name).getIndexId();
			}
			if(method==1){
				ReportExcelTemplate reportExcelTemplate=reportExcelTemplateService.queryReportExcelTemplateByName(name);
					IndexTb index=indexTbService.queryById(reportExcelTemplate.getIndexId());
					id=reportExcelTemplate.getIndexId();
					zbdlname=index.getIndexName();
			}
		}
		//机构管理员只能删除当前负责机构的所有数据
		if(roleType==6){
			if(UserOrgId!=orgId){
				jrw.setFlag(false);
				jrw.setMessage("删除失败，只能删除自己负责机构");
				SysUserLog sysUserLog=new SysUserLog();
				sysUserLog.setSysUserLogMenuName("数据删除");
				sysUserLog.setIndexId(id);
				sysUserLog.setSysUserLogOperateType(2);
				sysUserLog.setSysUserLogResult(false);
				sysUserLogService.insertOneLog(sysUserLog,request);
				return jrw;
			}
		}

        if("基本信息".equals(zbdlname)){
			jrw.setFlag(false);
			jrw.setMessage("删除失败,基本信息不能删除");
			SysUserLog sysUserLog=new SysUserLog();
			sysUserLog.setSysUserLogMenuName("数据删除");
			sysUserLog.setIndexId(id);
			sysUserLog.setSysUserLogOperateType(2);
			sysUserLog.setSysUserLogResult(false);
			sysUserLogService.insertOneLog(sysUserLog,request);
			return jrw;
		}
		if(rid!=null){
			 ReportIndex reportIndex=reportIndexService.queryReportIndexsById(rid);
			    String date=DateFormatter.formatDate(reportIndex.getReportIndexTime());
			    Integer oid=reportIndex.getSysOrgId();
				IndexTb indexTb=indexTbService.queryIdByName(zbdlname);
				String indexCode=indexTb.getIndexCode();//得到动态生成表的表名
				String sql="select * from "+indexCode+"_tb ctb where ctb.record_date='"+date+"' AND ctb.sys_org_id="+oid;
				Map<String, Object> sqlMap = new HashMap<>();
				sqlMap.put("sql", sql);
				List<Map<String, Object>> list=reportedDeleteService.queryResult(sqlMap);
				String delSql="delete "+indexCode+"_tb ctb where ctb.record_date='"+date+"' AND ctb.sys_org_id="+oid;
				Map<String, Object> delMap = new HashMap<>();
				delMap.put("delete", delSql);
				Integer num= reportedDeleteService.deleteData(delMap);//删除真实数据
				/*if(num<=0){
					jrw.setFlag(false);
					jrw.setMessage("删除失败");
					SysUserLog sysUserLog=new SysUserLog();
					sysUserLog.setSysUserLogMenuName("数据删除");
					sysUserLog.setIndexId(id);
					sysUserLog.setSysUserLogOperateType(2);
					sysUserLog.setSysUserLogResult(false);
					sysUserLogService.insertOneLog(sysUserLog);
					return jrw;
				}*/
				for (int i = 0; i < num; i++) {
					SysUserLog sysUserLog=new SysUserLog();
					sysUserLog.setSysUserLogMenuName("数据删除");
					sysUserLog.setIndexId(id);
					sysUserLog.setSysUserLogOperateType(2);
					sysUserLog.setSysUserLogResult(true);
					sysUserLogService.insertOneLog(sysUserLog,request);
				}
				for (int j = 0; j < list.size(); j++) {
					Integer did=(Integer) list.get(j).get(indexCode.toUpperCase()+"_ID");
					Integer indexid=indexTb.getIndexId();
					List<AdminObjModel> adminObjList= adminObjService.selectSysOperateIdByDataIdAndIndexItemId(did, indexid);

                    for (AdminObjModel adminObjModel : adminObjList) {
						Integer sysOperateId= adminObjModel.getSysOperateId();
						adminObjService.delOperateByDataId(sysOperateId);
					}
				}
				//String delSql1="delete report_index_tb where report_index_id="+rid;
				//delMap.put("delete", delSql1);
				//reportedDeleteService.deleteData(delMap);//删除记录表数据
				reportIndex.setReportIndexStatus(2);
				reportIndexService.updata(reportIndex);
				jrw.setMessage("删除成功");
				return jrw;
		}
		if(cid!=null){
			if(indexId==null){
				jrw.setFlag(false);
				jrw.setMessage("删除失败");
				return jrw;
			}
			String zbdl=indexTbService.queryById(indexId).getIndexName();
			if("基本信息".equals(zbdl)){
				jrw.setFlag(false);
				jrw.setMessage("删除失败,基本信息不能删除");
				SysUserLog sysUserLog=new SysUserLog();
				sysUserLog.setSysUserLogMenuName("数据删除");
				sysUserLog.setIndexId(id);
				sysUserLog.setSysUserLogOperateType(2);
				sysUserLog.setSysUserLogResult(false);
				sysUserLogService.insertOneLog(sysUserLog,request);
				return jrw;
			}
			String indexCode=indexTbService.queryById(indexId).getIndexCode();
			String delSql="delete "+indexCode+"_tb where "+indexCode+"_id="+cid;
			Map<String, Object> delMap = new HashMap<>();
			delMap.put("delete", delSql);
			Integer num= reportedDeleteService.deleteData(delMap);//删除真实数据
			if(num<=0){
				jrw.setFlag(false);
				jrw.setMessage("删除失败");
				SysUserLog sysUserLog=new SysUserLog();
				sysUserLog.setSysUserLogMenuName("数据删除");
				sysUserLog.setIndexId(id);
				sysUserLog.setSysUserLogOperateType(2);
				sysUserLog.setSysUserLogResult(false);
				sysUserLogService.insertOneLog(sysUserLog,request);
				return jrw;
			}
			SysUserLog sysUserLog=new SysUserLog();
			sysUserLog.setSysUserLogMenuName("数据删除");
			sysUserLog.setIndexId(id);
			sysUserLog.setSysUserLogOperateType(2);
			sysUserLog.setSysUserLogResult(true);
			sysUserLogService.insertOneLog(sysUserLog,request);
			jrw.setMessage("删除成功");
			return jrw;
		}
		return jrw;


    }

    //根据地区查询全部删除列表
    public void queryAllTbByArea(HttpServletRequest request, Model model,String sql2,StringBuffer sb,
										String beginTime,String endTime,String orgCode,String orgName
				) throws Exception{
			List<IndexTb> indexTbs=indexTbService.queryAll(null);
			StringBuffer sbsql=new StringBuffer();
			for (IndexTb indexTb : indexTbs) {
				indexTb.getIndexName();
				String indexCode=indexTb.getIndexCode();
				sbsql.append(" select ctb."+indexCode+"_id AS cid,otb.sys_org_name AS REPORT_INDEX_ORG_NAME,ctb.record_date AS report_index_time,ctb.submit_time AS report_index_submit_time,ctb.sys_org_id AS sys_org_id from "+indexCode+"_tb ctb"+
						" left join sys_org_tb otb on ctb.sys_org_id=otb.sys_org_id"+
						" left join default_index_item_tb dtb on dtb.default_index_item_id=ctb.default_index_item_id"+
						" WHERE ctb.sys_area_id IN ("+sb+")"+sql2+
						" UNION"
						);
			}
			sbsql.delete(sbsql.length()-5, sbsql.length());
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("sql", sbsql);
			//分页查询
			PageSupport ps = PageSupport.initPageSupport(request);
			List<Map<String, Object>> reportIndex=reportedDeleteService.getPage(ps,"page",sqlMap);
			List<Map<String, Object>> report=reportedDeleteService.queryResult(sqlMap);
			model.addAttribute("reportIndexs", reportIndex);
		/*	SysUserLog sysUserLog=new SysUserLog();
			sysUserLog.setSysUserLogMenuName("数据删除");
			if(report!=null&&report.size()>0){
				sysUserLog.setSysUserLogCount(report.size());
			}else{
				sysUserLog.setSysUserLogCount(0);
			}
			sysUserLog.setSysUserLogQueryUserCondition("开始时间："+beginTime+",结束时间:"+endTime+(orgName==null?"":(",机构："+orgName))+",企业二码:"+orgCode);
			sysUserLog.setSysUserLogOperateType(4);
			sysUserLog.setSysUserLogResult(true);
			sysUserLog.setSysUserLogQuerySql(sbsql.toString());
			sysUserLog.setSysUserLogUrl("/admin/reportedDelete/list.jhtml?beginTime="+beginTime+"&endTime="+endTime+"&orgName="+orgName+"&orgCode="+orgCode);
			sysUserLogService.insertOneLog(sysUserLog);*/
		}

    //根据机构查询全部删除列表
    public void queryAllTbByOrg(HttpServletRequest request, Model model,String sql2,StringBuffer sbr,
									String beginTime,String endTime,String orgCode,String orgName
				) throws Exception{
			List<IndexTb> indexTbs=indexTbService.queryAll(null);
			StringBuffer sbsql=new StringBuffer();
			for (IndexTb indexTb : indexTbs) {
				indexTb.getIndexName();
				String indexCode=indexTb.getIndexCode();
                sbsql.append(" select ctb." + indexCode + "_id AS cid,otb.sys_org_name AS REPORT_INDEX_ORG_NAME,ctb.record_date AS report_index_time,ctb.submit_time AS report_index_submit_time,ctb.sys_org_id AS sys_org_id from " + indexCode + "_tb ctb" +
                                " left join sys_org_tb otb on ctb.sys_org_id=otb.sys_org_id"+
						" left join default_index_item_tb dtb on dtb.default_index_item_id=ctb.default_index_item_id"+
						" WHERE ctb.sys_org_id IN ("+sbr+")"+sql2+
						" UNION"
						);
			}
			sbsql.delete(sbsql.length()-5, sbsql.length());
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("sql", sbsql);
			//分页查询
			PageSupport ps = PageSupport.initPageSupport(request);
			List<Map<String, Object>> reportIndex=reportedDeleteService.getPage(ps,"page",sqlMap);
			List<Map<String, Object>> report=reportedDeleteService.queryResult(sqlMap);
			model.addAttribute("reportIndexs", reportIndex);
			/*SysUserLog sysUserLog=new SysUserLog();
			sysUserLog.setSysUserLogMenuName("数据删除");
			if(report!=null&&report.size()>0){
				sysUserLog.setSysUserLogCount(report.size());
			}else{
				sysUserLog.setSysUserLogCount(0);
			}
			sysUserLog.setSysUserLogQueryUserCondition("开始时间："+beginTime+",结束时间:"+endTime+(orgName==null?"":(",机构："+orgName))+",企业二码:"+orgCode);
			sysUserLog.setSysUserLogOperateType(4);
			sysUserLog.setSysUserLogResult(true);
			sysUserLog.setSysUserLogQuerySql(sbsql.toString());
			sysUserLog.setSysUserLogUrl("/admin/reportedDelete/list.jhtml?beginTime="+beginTime+"&endTime="+endTime+"&orgName="+orgName+"&orgCode="+orgCode);
			sysUserLogService.insertOneLog(sysUserLog);*/
		}
		
		@RequestMapping("/select")
		public String select(Model model,Integer indexId,Integer id,HttpServletRequest request) throws Exception{
			
			IndexTb in =indexTbService.queryById(indexId);
			List<IndexItemTb> indexItem = indexItemTbService.getIndexIntemsIsUsedByIdAndAreaIds(indexId,null);
			StringBuffer sb = new StringBuffer("select ");
			for (IndexItemTb indexItemTb : indexItem) {
				sb.append(indexItemTb.getIndexItemCode()+",");
			}
			sb =new StringBuffer(sb.substring(0, sb.length()-1));
			sb.append(" from "+in.getIndexCode()+"_tb where "+in.getIndexCode()+"_id ="+id );
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("sql", sb.toString());
			Map<String, Object> report=reportedDeleteService.queryResult(sqlMap).get(0);

			Map<String, Object> newMap=new HashMap<>();
			for (String string : report.keySet()) {
				Object obj = report.get(string);
				newMap.put(string.toLowerCase(), obj);
			}
			model.addAttribute("indexItem",indexItem);
			model.addAttribute("indexTb", newMap);
			model.addAttribute("indexId",indexId);
			model.addAttribute("id", id);
			SysUserLog sysUserLog=new SysUserLog();
			sysUserLog.setSysUserLogMenuName("数据删除");
			sysUserLog.setIndexId(indexId);
			sysUserLog.setSysUserLogCount(1);
			sysUserLog.setSysUserLogOperateType(4);
			sysUserLog.setSysUserLogResult(true);
			sysUserLog.setSysUserLogQuerySql(sb.toString());
			sysUserLog.setSysUserLogUrl("/admin/reportedDelete/select.jhtml?id="+id);
			sysUserLogService.insertOneLog(sysUserLog,request);
			return "ReportedDelete/type";
		}
		@RequestMapping("/deleteId")
		@ResponseBody
		public JsonResWrapper deleteId(Integer indexId,Integer id,HttpServletRequest request) throws Exception{
			JsonResWrapper jrw = new JsonResWrapper();
			IndexTb in =indexTbService.queryById(indexId);
			String indexName=in.getIndexName();
			Integer defaultId=null;
			if("基本信息".equals(indexName)){
				String sql="select DEFAULT_INDEX_ITEM_ID from "+in.getIndexCode()+"_tb where "+in.getIndexCode()+"_id = "+id;
				Map<String, Object> map = new HashMap<>();
				map.put("sql", sql);
				List<Map<String, Object>> list=reportedDeleteService.queryResult(map);
				defaultId=(Integer) list.get(0).get("DEFAULT_INDEX_ITEM_ID");
				List<IndexTb> indexTbs=indexTbService.queryAll(null);
				Map<String, Object> sqlMap = new HashMap<String, Object>();
				for (IndexTb indexTb : indexTbs) {
					String indexCode=indexTb.getIndexCode();
					if("index_jbxx".equals(indexCode)){
						continue;
					}
	                String sbsql="select * from "+indexCode+"_tb where default_index_item_id="+defaultId;
	                sqlMap.put("sql", sbsql);
	                List<Map<String, Object>> report=reportedDeleteService.queryResult(sqlMap);
	                if(report!=null&&report.size()>0){
	                	jrw.setFlag(false);
	    				jrw.setMessage("删除失败,该企业有业务信息不能删除");
	    				return jrw;
	                }
				}
				
			}
			String sql="select * from "+in.getIndexCode()+"_tb where "+in.getIndexCode()+"_id = "+id;
			Map<String, Object> map = new HashMap<>();
			map.put("sql", sql);
			List<Map<String, Object>> list=reportedDeleteService.queryResult(map);
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("delete", "delete from "+in.getIndexCode()+"_tb where "+in.getIndexCode()+"_id = "+id);
			Integer i=reportedDeleteService.deleteData(sqlMap);
			if(i==1){
				if("基本信息".equals(indexName)){
					String sql1="select * from "+in.getIndexCode()+"_tb where "+in.getIndexCode()+"_id != "+id+" AND default_index_item_id="+defaultId;
					map.put("sql", sql1);
					List<Map<String, Object>> lists=reportedDeleteService.queryResult(map);
					if(lists==null||lists.size()==0){
						sqlMap.put("delete", "delete from default_index_item_tb where  default_index_item_id = "+defaultId);
						reportedDeleteService.deleteData(sqlMap);
					}
				}
				for (int j = 0; j < list.size(); j++) {
					Integer did=(Integer) list.get(j).get(in.getIndexCode().toUpperCase()+"_ID");
					List<AdminObjModel> adminObjList=adminObjService.selectSysOperateIdByDataIdAndIndexItemId(did, indexId);
					for (AdminObjModel adminObjModel : adminObjList) {
						Integer sysOperateId=adminObjModel.getSysOperateId();
						adminObjService.delOperateByDataId(sysOperateId);
					}
				}
				jrw.setMessage("删除成功");
				SysUserLog sysUserLog=new SysUserLog();
				sysUserLog.setSysUserLogMenuName("数据删除");
				sysUserLog.setIndexId(indexId);
				sysUserLog.setSysUserLogOperateType(2);
				sysUserLog.setSysUserLogResult(true);
				sysUserLogService.insertOneLog(sysUserLog,request);
			}else{
				jrw.setFlag(false);
				jrw.setMessage("删除失败");
				SysUserLog sysUserLog=new SysUserLog();
				sysUserLog.setSysUserLogMenuName("数据删除");
				sysUserLog.setIndexId(indexId);
				sysUserLog.setSysUserLogOperateType(2);
				sysUserLog.setSysUserLogResult(false);
				sysUserLogService.insertOneLog(sysUserLog,request);
			}
			return jrw;
		}
		@RequestMapping("/getOrgList")
		@ResponseBody
		public void getOrgByName(HttpServletResponse response){
			// 机构列表
			MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Integer roleId=userDetails.getSys_role_id();
			SysRole sysRole=sysRoleService.querySystemRoleById(roleId);
			Integer roleType=sysRole.getSys_role_type();
			
			String orgListKey = RedisKeys.SYS_ORG_LIST_USER + userDetails.getSys_user_id();
			Type type = new TypeToken<List<SysOrg>>(){}.getType();
			List<SysOrg> sysOrgList = RedisUtil.getListData(orgListKey, type);
			if(CollectionUtils.isEmpty(sysOrgList)){
				//缓存单个机构
				String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
				SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
				if(so==null){
					so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
					RedisUtil.setData(orgKey, so);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				if(roleType==1||roleType==3){
					Integer affiliationAreaId=so.getSys_org_affiliation_area_id();
					Integer areaId=so.getSys_area_id();
					if(affiliationAreaId==null){
						map = new HashMap<String, Object>();
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
					}else{
						map.put("affiliationArea", affiliationAreaId);
					}
				}else{
					StringBuffer sbr=new StringBuffer();
					DataUtil.getChildOrgIds(so, sbr);
					map.put("orgIds", sbr.toString().split(","));
				}
				map.put("type", 0);
				
				sysOrgList=sysOrgService.querySysOrg(map);
				//设置机构缓存
				RedisUtil.setData(orgListKey, sysOrgList);
			}
			String key = RedisKeys.SYS_ORG_TYPE_LIST+ userDetails.getSys_user_id();
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
			SysOrgType type80 = sysOrgTypeService.getTypeByIdNotSub(80);
			sysOrgType.add(type80);
            List<ZtreeVo> ztreeVo = DataUtil.getZtree(sysOrgList, sysOrgType);
            //要分配的机构
			Gson gson=new Gson();
			try {
				response.getWriter().write(gson.toJson(ztreeVo));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
