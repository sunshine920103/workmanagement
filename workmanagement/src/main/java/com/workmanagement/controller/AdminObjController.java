package com.workmanagement.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.workmanagement.dao.ManualEntryDao;
import com.workmanagement.model.AdminObjModel;
import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.Dic;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysClassFyModel;
import com.workmanagement.model.SysGover;
import com.workmanagement.model.SysOperateListModel;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.service.AdminObjService;
import com.workmanagement.service.DefaultIndexItemService;
import com.workmanagement.service.DicContentService;
import com.workmanagement.service.DicService;
import com.workmanagement.service.IndexItemTbService;
import com.workmanagement.service.IndexTbService;
import com.workmanagement.service.ManualEntryService;
import com.workmanagement.service.OrgObjService;
import com.workmanagement.service.RelateInfoService;
import com.workmanagement.service.ReportIndexService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysClassFyService;
import com.workmanagement.service.SysGoverService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOtherManageService;
import com.workmanagement.service.SysRoleService;
import com.workmanagement.service.SysUserLogService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;
import com.workmanagement.util.SettingUtils;
import com.workmanagement.util.UpLoadFile;
/**   异议处理
 *   xiehao
 *   
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/admin/adminObj")
public class AdminObjController {
	@Autowired
	private SysOtherManageService sysOtherManageService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private DicContentService dicContentService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysClassFyService sysClassFyService;
	@Autowired
	private SysUserLogService sysUserLogService;
	@Autowired
	private DicService dicService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private ReportIndexService reportIndexService;
	@Autowired
	private IndexTbService indexTbService;
	@Autowired
	private ManualEntryService manualEntryService;
	@Autowired
	private DefaultIndexItemService defaultIndexItemService;
	@Autowired
	private IndexItemTbService indexItemTbService;
	@Autowired
	private AdminObjService adminObjService;
	@Autowired
	private ManualEntryDao manualEntryDao;
	@Autowired
	private OrgObjService orgObjService;
	@Autowired
	private SysGoverService sysGoverService;
	@Autowired
	private RelateInfoService relateInfoService;
	@RequestMapping(value="/newlist")
	public String newList(HttpSession session,HttpServletResponse response,HttpServletRequest request,Model model){
		PageSupport ps = PageSupport.initPageSupport(request);
		Object objSql = request.getAttribute("querySql");
		 if(objSql!=null){
			 String querySql = null;
			 if (objSql == null) {
				 querySql = String.valueOf(session.getAttribute("sysUserBehaviorAudit_sql"));
			 } else {
				 querySql = String.valueOf(objSql);
				 session.setAttribute("sysUserBehaviorAudit_sql", querySql);
			 }
			 String sqll = StringUtils.replace(querySql, "|", " ");
			 List<Map<String, Object>> list = relateInfoService.queryMoreData(ps, sqll);
			 model.addAttribute("sysOperateList", list);
			 return "adminObj/copylist";
		 }
		 return "adminObj/copylist";
	}
	@RequestMapping(value="/list")
	public String list(HttpSession session,HttpServletResponse response,HttpServletRequest request,Model model,@RequestParam(required = false)Integer indexId,@RequestParam(required = false)Integer defaultId,@RequestParam(required = false)Integer ing,@RequestParam(required = false)String time,@RequestParam(required = false)String defaultIndexItemCode,@RequestParam(required = false)String orgName,@RequestParam(required = false)String selectId,@RequestParam(required = false)Integer status,@RequestParam(required = false)String defaultIndexItemId,@RequestParam(required = false)String defaultIndexItemCreditId,@RequestParam(required = false)String valuePlay) throws IOException{
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PageSupport ps = PageSupport.initPageSupport(request);
		IndexTb indexTbModel=null;
		//未测试
		if(ing!=null){
			//点击继续按钮，去异议处理详情表中去查询是否存在标注的异议处理选项，如果不存在，则不允许其进行推送
			List<SysOperateListModel> markList=adminObjService.queryMarkByOperateId(ing);
			if(markList!=null &&markList.size()>0){
				AdminObjModel admin=new AdminObjModel();
				admin.setSysOperateStatus(1);
				admin.setSysOperateId(ing);
				admin.setSysOrgId(userDetails.getSys_org_id());
				adminObjService.updateStatus(admin);
				Gson gson=new Gson();
				response.getWriter().write(gson.toJson(markList));
				request.setAttribute("msg", "推送成功");
				//添加用户行为审计
				SysUserLog sul=new SysUserLog();
				sul.setSysUserLogMenuName("人行异议处理");
				sul.setIndexId(indexId);
				if(StringUtil.isNotEmpty(defaultIndexItemId)|| StringUtil.isNotEmpty(defaultIndexItemCreditId)){
					if(StringUtil.isNotEmpty(defaultIndexItemId)){
						sul.setSysUserLogEnterpriseCode(defaultIndexItemId.toString());
					}else{
						sul.setSysUserLogEnterpriseCode(defaultIndexItemCreditId.toString());
					}
				}
				sul.setSysUserLogOperateType(3);
				sul.setSysUserLogResult(true);
				if(valuePlay.length()>2){
					sul.setSysUserLogOldValue(valuePlay.substring(1, valuePlay.length()-1));
				}else{
					sul.setSysUserLogOldValue("");
				}
				sysUserLogService.insertOneLog(sul,request);
			}
		return null;
		}
		//查询所有指标大类
		List<IndexTb> indexTb=indexTbService.selectAll();
		for (int i = 0; i < indexTb.size(); i++) {
			if(indexTb.get(i).getSysAreaId()!=1){
				SysArea area=sysAreaService.queryAreaById(indexTb.get(i).getSysAreaId());
				String name=area.getSysAreaName()+"-"+indexTb.get(i).getIndexName();
				indexTb.get(i).setIndexName(name);
			}
		}
		//根据机构ID获取机构缓存
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		List<AdminObjModel> sysOperateList=new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer orgSb = new StringBuffer();
		DataUtil.getChildOrgIds(so, orgSb);
//		map.put("orgIds", orgSb.toString().split(","));
		String sql=" SELECT 	(SELECT INDEXTB.INDEX_NAME FROM INDEX_TB INDEXTB WHERE INDEXTB.INDEX_ID=OPERATE.INDEX_ITEM_ID) AS indexName,"+
				 " (SELECT ORG.SYS_ORG_NAME FROM SYS_ORG_TB ORG WHERE ORG.SYS_ORG_ID=OPERATE.SYS_ORG_ID) AS sysOrgName, "+
				"  OPERATE.SYS_OPERATE_TIME AS sysOperateTime,"+
				" OPERATE.RECORD_DATE AS recordDate,"+
				" OPERATE.SYS_OPERATE_STATUS AS sysOperateStatus, "+
				" OPERATE.DEFAULT_INDEX_ITEM_ID AS defaultIndexItemId, "+
				" OPERATE.INDEX_ITEM_ID AS indexItemId, "+
				" OPERATE.SYS_ORG_ID AS sysOrgId, "+
				" OPERATE.SYS_OPERATE_ID AS sysOperateId, "+
				" OPERATE.REPORT_ORG_ID AS reportOrgId, "+
				" OPERATE.DATA_ID AS dataId, "+
				"(SELECT JBXX.INDEX_JBXX_QYMC FROM INDEX_JBXX_TB JBXX WHERE JBXX.DEFAULT_INDEX_ITEM_ID=OPERATE.DEFAULT_INDEX_ITEM_ID FETCH FIRST 1 ROWS "+
				" ONLY) AS jbxxQimc,  (SELECT DEFAULTINDEX.CODE_CREDIT FROM DEFAULT_INDEX_ITEM_TB DEFAULTINDEX WHERE "+
				" DEFAULTINDEX.DEFAULT_INDEX_ITEM_ID=OPERATE.DEFAULT_INDEX_ITEM_ID) AS codeCredit, (SELECT DEFAULTINDEX.CODE_ORG FROM DEFAULT_INDEX_ITEM_TB DEFAULTINDEX WHERE "+
				" DEFAULTINDEX.DEFAULT_INDEX_ITEM_ID=OPERATE.DEFAULT_INDEX_ITEM_ID) AS codeOrg, (SELECT ORG.SYS_ORG_NAME FROM SYS_ORG_TB ORG WHERE ORG.SYS_ORG_ID=OPERATE.REPORT_ORG_ID) AS sysReportOrgName "+
				" FROM 	SYS_OPERATE_TB OPERATE  WHERE   1=1 "+
				" AND (OPERATE.SYS_ORG_ID ="+userDetails.getSys_org_id()+" OR (OPERATE.REPORT_ORG_ID="+userDetails.getSys_org_id()+" AND OPERATE.SYS_OPERATE_STATUS!=0)) ";
		String sql1="SELECT DEFAULT_INDEX_ITEM_ID FROM DEFAULT_INDEX_ITEM_TB  WHERE 1=1";
		String condition="根据";
		String url="";
		if(selectId!=null){
			if(!"".equals(defaultIndexItemCode)&&defaultIndexItemCode!=null ||orgName!=null &&!"".equals(orgName)){
				Map<String, Object> maps = new HashMap<String, Object>();
				if("".equals(defaultIndexItemCode)||defaultIndexItemCode==null){
					maps.put("defaultIndexItemCode", null);
					defaultIndexItemCode=null;
				}else{
					maps.put("defaultIndexItemCode", defaultIndexItemCode);
					sql1+="  AND (CODE_CREDIT="+defaultIndexItemCode+" OR CODE_ORG="+defaultIndexItemCode+")";
					condition+="统一社会信用代码/组织机构代码为："+defaultIndexItemCode;
					url+="defaultIndexItemCode="+defaultIndexItemCode+"&";
				}
				if("".equals(orgName)||orgName==null){
					maps.put("jbxxQymc", null);
					orgName=null;
				}else{
					maps.put("jbxxQymc", orgName);
					sql1+="AND QYMC="+orgName+"   ";
					condition+="； 企业名称为："+orgName;
					url+="orgName="+orgName+"&";
				}
				String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
				if(!"1".equals(role) && !"7".equals(role)){
					maps.put("areaId", so.getSys_area_id());
					sql1+="AND SYS_AREA_ID="+so.getSys_area_id();
				}else{
					maps.put("areaId", null);
				}
				List<Integer> queryDefaultId=orgObjService.queryDefaultId(maps);
				Integer[] defaultIds=new Integer[queryDefaultId.size()];
				for (int i = 0; i < queryDefaultId.size(); i++) {
					defaultIds[i]=queryDefaultId.get(i);
				}
				if(defaultIds.length>0){
					map.put("defaultId", defaultIds);
//					String defaultIds1="";
//					for (int i = 0; i < defaultIds.length; i++) {
//						defaultIds1+=defaultIds[i]+",";
//					}
//					defaultIds1=defaultIds1.substring(0, defaultIds1.length()-1);
//					sql1+=" AND OPERATE.DEFAULT_INDEX_ITEM_ID IN ("+defaultIds1+")";
				}else{
					model.addAttribute("msg", "企业二码和企业名称不匹配");
					model.addAttribute("indexTb", indexTb);
					map.put("orgIds", userDetails.getSys_org_id());
					map.put("reporateType", 1);
					model.addAttribute("orgId",userDetails.getSys_org_id());
					sysOperateList=orgObjService.querySysOperateList(ps, map);
					model.addAttribute("sysOperateList", sysOperateList);
					model.addAttribute("time",time);
					model.addAttribute("struts",status);
					model.addAttribute("orgName",orgName);
					model.addAttribute("indexId",indexId);
					model.addAttribute("selectId",selectId);
					model.addAttribute("defaultIndexItemCode",defaultIndexItemCode);
					model.addAttribute("indexName",null);
					
					//添加用户行为审计
					SysUserLog sul=new SysUserLog();
					sul.setSysUserLogMenuName("人行异议处理");
					sul.setIndexId(indexId);
					sul.setSysUserLogOperateType(4);
					String sql2 = StringUtils.replace(sql1, " ", "|");
					sul.setSysUserLogQuerySql(sql2);
					sul.setSysUserLogResult(false);
					sysUserLogService.insertOneLog(sul,request);
					return "adminObj/list";
				}
			}else{
				map.put("defaultId", null);
			}
			if(indexId==null || "".equals(indexId)){
				map.put("indexItemId", null);
			}else{
				indexTbModel=indexTbService.queryById(indexId);
				map.put("indexItemId", indexId);
				sql += "AND OPERATE.INDEX_ITEM_ID="+indexId+"   ";
				url+="indexId="+indexId+"&";
				
				IndexTb i=indexTbService.queryById(indexId);
				condition+="指标大类为："+i.getIndexName();
			}
			if(time==null || "".equals(time)){
				map.put("recordDate", null);
			}else{
				map.put("recordDate", time);
				sql+="AND subStr(OPERATE.SYS_OPERATE_TIME,1,10)='"+time+"'   ";
				condition+="时间为"+time;
				url+="time="+time+" &";
			}
			if(status==null || "".equals(status)){
				map.put("status", null);
			}else{
				map.put("status", status);
				sql+="AND OPERATE.SYS_OPERATE_STATUS="+status+"   ";
				url+="status="+status+"&";
				if(status==1){
					condition+="；状态为：待处理";
				}else if(status==2){
					condition+="；状态为：处理中";
				}else{
					condition+="；状态为：已处理";
				}
				
			}
			model.addAttribute("selectId",selectId);
			url+="selectId="+selectId;
		}else{
			map.put("defaultId", null);
			map.put("indexItemId", null);
			map.put("recordDate", null);
		}
//		if(userDetails.getSys_org_id()==5){
//			map.put("reporateType", 1);
//			sysOperateList=orgObjService.queryAllSysOperateList(ps);
//		}else{
			map.put("orgIds", userDetails.getSys_org_id());
			map.put("reporateType", 1);
//			map.put("rid",  userDetails.getSys_org_id());
			sysOperateList=orgObjService.querySysOperateList(ps,map);
			List<AdminObjModel> sysOperateList1=orgObjService.querySysOperateList(null,map);
			sql+="  ORDER BY OPERATE.SYS_OPERATE_TIME DESC";
			
			if(selectId!=null){
				//添加用户行为审计
				SysUserLog sul=new SysUserLog();
				sul.setSysUserLogMenuName("人行异议处理");
				sul.setIndexId(indexId);
				sul.setSysUserLogOperateType(4);
				sul.setSysUserLogQueryUserCondition(condition);
				if(sysOperateList1!=null && sysOperateList1.size()>0){
					sul.setSysUserLogCount(sysOperateList1.size());
				}else{
					sul.setSysUserLogCount(0);
				}
				if(StringUtil.isNotEmpty(defaultIndexItemId)|| StringUtil.isNotEmpty(defaultIndexItemCreditId)){
					if(StringUtil.isNotEmpty(defaultIndexItemId)){
						sul.setSysUserLogEnterpriseCode(defaultIndexItemId.toString());
					}else{
						sul.setSysUserLogEnterpriseCode(defaultIndexItemCreditId.toString());
					}
				}
				String sql3 = StringUtils.replace(sql, " ", "|");
				sul.setSysUserLogQuerySql(sql3);
				sul.setSysUserLogResult(true);
//				sul.setSysUserLogUrl("/admin/adminObj/newlist.jhtml?");
				sul.setSysUserLogUrl("/admin/adminObj/list.jhtml?"+url);
				sysUserLogService.insertOneLog(sul,request);
			}
//		}
		model.addAttribute("indexTb", indexTb);
		model.addAttribute("sysOperateList", sysOperateList);
		model.addAttribute("orgId",userDetails.getSys_org_id());
		model.addAttribute("LocalOrgId",userDetails.getSys_org_id());
		model.addAttribute("time",time);
		model.addAttribute("struts",status);
		model.addAttribute("orgName",orgName);
		model.addAttribute("indexId",indexId);
		
		model.addAttribute("defaultIndexItemCode",defaultIndexItemCode);
		if(indexTbModel!=null){
			model.addAttribute("indexName",indexTbModel.getIndexName());
		}else{
			model.addAttribute("indexName",null);
		}
		return "adminObj/list";
	}
	/**
	 * 模糊查询所有指标大类
	 * @param request
	 * @param indexName
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/editmohu")
	public void editmohu(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false)String indexName) throws IOException{
		indexName = URLDecoder.decode(indexName, "UTF-8"); 
		List<IndexTb> indexTb=new ArrayList<IndexTb>();
		if(indexName!=null){
			indexTb=indexTbService.mohuQueryAll(null,indexName,null);
		}
		Gson gson=new Gson();
		response.getWriter().write(gson.toJson(indexTb));
		return;
	}
	/**
	 * 模糊查询所有机构
	 * @param request
	 * @param sysName
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/sysOrgmohu")
	public void sysOrg(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false)String sysName) throws Exception{
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		List<SysOrg> sysOrg=new ArrayList<SysOrg>();
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
//			so=sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			so=sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		// 获取管辖区域
		String areaKey=RedisKeys.SYS_AREA+so.getSys_area_id();
		Map<String, Object> map = new HashMap<String, Object>();
		SysArea sa = RedisUtil.getObjData(areaKey, SysArea.class);
		// 查询管辖地区及子地区的ID
		StringBuffer sb = new StringBuffer();
		DataUtil.getParentAreaIds(sa, sb, sysAreaService);// 获取改地区及父地区的ID
		DataUtil.getChildAreaIds(sa, sb);// 获取改地区及子地区的ID
		String[] ids = sb.toString().split(",");
		if(sysName!=null){
			// 查询管辖地区及子地区的ID
			StringBuffer orgSb = new StringBuffer();
			DataUtil.getChildAreaIds(sa, orgSb);
			String[] orgIds=orgSb.toString().split(",");
			map.put("name", sysName);
			String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
			if("1".equals(role)){
				map.put("orgType", 3);
				map.put("areaId", ids);
			}else if("8".equals(role)){
				map.put("orgType", 2);
				map.put("orgIds", so.getSys_org_id());
			}else{
				map.put("orgType", 1);
				map.put("orgIds", orgIds);
			}
			sysOrg=manualEntryService.querySysOrgAll(map);
		}
		Gson gson=new Gson();
		response.getWriter().write(gson.toJson(sysOrg));
		return;
	}
	/**
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/edit")
	public String edit(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
		List<SysOrg> sysOrg=new ArrayList<>();
		List<IndexTb> indexTb=new ArrayList<IndexTb>();
		//查询所有指标大类
		indexTb=indexTbService.selectAll();
		SysOtherManage stm=sysOtherManageService.querySysOtherManage(userDetails.getSys_user_id());
		//查询报送机构
//		sysOrg=sysOrgService.queryAll();
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
//			so=sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			so=sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取管辖区域
		String areaKey=RedisKeys.SYS_AREA+so.getSys_area_id();
		SysArea sa = RedisUtil.getObjData(areaKey, SysArea.class);
		if(sa==null){
			sa = sysAreaService.queryAreaById(so.getSys_area_id());
			RedisUtil.setData(areaKey, sa);
		}
		// 查询管辖地区及子地区的ID
		StringBuffer sb = new StringBuffer();
		DataUtil.getParentAreaIds(sa, sb, sysAreaService);// 获取改地区及父地区的ID
		DataUtil.getChildAreaIds(sa, sb);// 获取改地区及子地区的ID
		String[] ids = sb.toString().split(",");
		map.put("areaId", ids);
		StringBuffer orgSb = new StringBuffer();
		DataUtil.getChildAreaIds(sa, orgSb);
		String[] orgIds=orgSb.toString().split(",");
		map.put("orgIds", orgIds);
		String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
		if("1".equals(role)){
			map.put("orgType", 3);
		}else{
			map.put("orgType", 1);
		}
		sysOrg=manualEntryService.querySysOrgAll(map);
		model.addAttribute("reportIndex", sysOrg);
		model.addAttribute("indexTb", indexTb);
		model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
		return "adminObj/edit";
	}
	/**
	 * 
	 * @param request
	 * @param model
	 * @param timeReport  截止归档时间
	 * @param indexId  指标大类id
	 * @param defaultIndexItemCode  二码
	 * @param sysOrgId  上报机构id
	 * @param orgName  企业名称
	 * @param file   上传的文件
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/reportList")
	public String reportList(HttpServletRequest request,Model model,@RequestParam(required = false)String timeReport,@RequestParam(required = false)Integer indexId,
								@RequestParam(required = false)String defaultIndexItemCode,@RequestParam(required = false)Integer sysOrgId,@RequestParam(required = false)String orgName,MultipartFile file,@RequestParam(required = false)Integer operateAuthFile) throws Exception{
		Integer id=0;
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
		//查询所有指标大类
		List<IndexTb> indexTb=indexTbService.selectAll();
		SysOtherManage stm=sysOtherManageService.querySysOtherManage(userDetails.getSys_user_id());
	
		//查询报送机构
//		List<SysOrg> sysOrg=sysOrgService.queryAll();
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		Map<String, Object> map = new HashMap<String, Object>();
		if(so==null){
//			so=sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			so=sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		// 获取管辖区域
		String areaKey=RedisKeys.SYS_AREA+so.getSys_area_id();
		SysArea sa = RedisUtil.getObjData(areaKey, SysArea.class);
		if(sa==null){
			sa = sysAreaService.queryAreaById(so.getSys_area_id());
			RedisUtil.setData(areaKey, sa);
		}
		// 查询管辖地区及子地区的ID
		StringBuffer sb = new StringBuffer();
		DataUtil.getParentAreaIds(sa, sb, sysAreaService);// 获取改地区及父地区的ID
		DataUtil.getChildAreaIds(sa, sb);// 获取改地区及子地区的ID
		String[] ids = sb.toString().split(",");
		map.put("areaId", ids);
		StringBuffer orgSb = new StringBuffer();
		DataUtil.getChildAreaIds(sa, orgSb);
		String[] orgIds=orgSb.toString().split(",");
		map.put("orgIds", orgIds);
		String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
		if("1".equals(role)){
			map.put("orgType", 3);
		}else{
			map.put("orgType", 1);
		}
		List<SysOrg> sysOrg=manualEntryService.querySysOrgAll(map);
		Integer sysOgAreaId=null;
		if(sysOrgId!=null){
			//通过上报机构查询对应的地区id
			SysOrg sysorg=sysOrgService.queryInstitutionsByIdWithNoStatus(sysOrgId);
//			sysOgAreaId=sysorg.getSys_area_id();
			SysArea area=sysAreaService.getUpOrThisSysArea(sysorg.getSys_area_id());
			sysOgAreaId=area.getSysAreaId();
		}else{
			SysArea area=sysAreaService.getUpOrThisSysArea(so.getSys_area_id());
			sysOgAreaId=area.getSysAreaId();
	//		sysOgAreaId=so.getSys_area_id();
		}
		DefaultIndexItem defaultIndexItem = manualEntryService.getDefaultIndexItemByCode(defaultIndexItemCode,sysOgAreaId);
		/**
		 * 通过传入的二码和企业名称去进行判断输入的信息是否匹配，如果匹配，则进行异议处理操作，如果不匹配，则返回异常
		 */
		
		if (defaultIndexItem == null) {
			request.setAttribute("msg", "该信用码/机构码不存在,请重新输入");
			model.addAttribute("reportIndex", sysOrg);
			model.addAttribute("indexTb", indexTb);
			model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
			return "adminObj/edit";
		}
		Pattern pattern = Pattern.compile(".*[\\u4e00-\\u9faf].*");
        Matcher isNum = pattern.matcher(defaultIndexItemCode);
        if(isNum.matches()){
        	request.setAttribute("msg", "统一社会信用码/组织机构代码不能为汉字");
        	model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
			return "adminObj/edit";
        }
        if(defaultIndexItemCode.length()>18){
        	request.setAttribute("msg", "统一社会信用码/组织机构代码长度不能大于18位");
        	model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
			return "adminObj/edit";
        }
    	//通过企业二码查询对应的id
		List<DefaultIndexItem> list=defaultIndexItemService.getByCredit(defaultIndexItemCode, sysOgAreaId);
		DefaultIndexItem defaultIndexItemCredit = null;
		if(CollectionUtils.isNotEmpty(list)){
			defaultIndexItemCredit = list.get(0);
		}
		String qymc="";
		if(defaultIndexItemCredit!=null){
			id=defaultIndexItemCredit.getDefaultIndexItemId();
			qymc=defaultIndexItemCredit.getQymc();
		}else{
			List<DefaultIndexItem> defaultIndexItemOrg=defaultIndexItemService.getByCodeOrg(defaultIndexItemCode, sysOgAreaId);
			for (int i = 0; i < defaultIndexItemOrg.size(); i++) {
				id=defaultIndexItemOrg.get(0).getDefaultIndexItemId();
				qymc=defaultIndexItemOrg.get(0).getQymc();
			}
		}
		if(!qymc.equals(orgName)){
			request.setAttribute("msg", "输入的企业名称不匹配，请重新输入");
			model.addAttribute("reportIndex", sysOrg);
			model.addAttribute("indexTb", indexTb);
			model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
			return "adminObj/edit";
		}
		//通过id去基本信息里面查询对应的企业名称
		String sql="SELECT INDEX_JBXX_QYMC FROM  INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID= "+id+" AND SYS_AREA_ID= "+sysOgAreaId;
		Map<String, Object> queryMajorIdmap = new HashMap<>();
		queryMajorIdmap.put("queryTemporarySql", sql);
		PageSupport ps = PageSupport.initPageSupport(request);
		//查询符合条件的所有信息
		List<Map<String,Object>> queryMajorIddataValues=manualEntryService.temporaryTableList(ps,queryMajorIdmap);
//		if(queryMajorIddataValues!=null &&  queryMajorIddataValues.size()>0){
//			for (int i = 0; i < queryMajorIddataValues.size(); i++) {
//				qymc=(String)queryMajorIddataValues.get(0).get("INDEX_JBXX_QYMC");
//			}
//			if(!qymc.equals(orgName)){
//				request.setAttribute("msg", "输入信息有误，请重新输入");
//				model.addAttribute("reportIndex", sysOrg);
//				model.addAttribute("indexTb", indexTb);
//				model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
//				return "adminObj/edit";
//			}
//		}else{
//			request.setAttribute("msg", "输入的企业代码不正确，请确认后输入");
//			model.addAttribute("reportIndex", sysOrg);
//			model.addAttribute("indexTb", indexTb);
//			model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
//			return "adminObj/edit";
//		}
		
		
		//通过指标大类indexName查询indexCode
		IndexTb indexTbModel=indexTbService.queryById(indexId);
		
		if(sysOrgId!=null){
			//查询表数据
			sql="select * from "+ indexTbModel.getIndexCode()+"_tb i,sys_org_tb s  where i.sys_org_id=s.sys_org_id and i.DEFAULT_INDEX_ITEM_ID ="+ id+"  and i.sys_org_id= "+ sysOrgId +" and i.RECORD_DATE = '"+ timeReport+" '";
		}else{
			sql="select * from "+ indexTbModel.getIndexCode()+"_tb i  ,sys_org_tb s  where i.sys_org_id=s.sys_org_id and  i.DEFAULT_INDEX_ITEM_ID ="+ id +" and i.RECORD_DATE = '"+ timeReport+" '";
		}
		queryMajorIdmap.put("queryTemporarySql", sql);
		//查询符合条件的所有信息
		queryMajorIddataValues=manualEntryService.temporaryTableList(ps,queryMajorIdmap);
		
		if (queryMajorIddataValues == null || queryMajorIddataValues.size()==0) {
			if(sysOrgId!=null){
				//通过上报机构查询对应的地区id
				SysOrg sysorg=sysOrgService.queryInstitutionsByIdWithNoStatus(sysOrgId);
				defaultIndexItem = manualEntryService.getDefaultIndexItemByCode(defaultIndexItemCode,sysorg.getSys_area_id());
				//查询表数据
				sql="select * from "+ indexTbModel.getIndexCode()+"_tb i,sys_org_tb s  where i.sys_org_id=s.sys_org_id and i.DEFAULT_INDEX_ITEM_ID ="+ defaultIndexItem.getDefaultIndexItemId()+"  and i.sys_org_id= "+ sysOrgId +" and i.RECORD_DATE = '"+ timeReport+" '";
			}else{
				defaultIndexItem = manualEntryService.getDefaultIndexItemByCode(defaultIndexItemCode,so.getSys_area_id());
				sql="select * from "+ indexTbModel.getIndexCode()+"_tb i  ,sys_org_tb s  where i.sys_org_id=s.sys_org_id and  i.DEFAULT_INDEX_ITEM_ID ="+ defaultIndexItem.getDefaultIndexItemId() +" and i.RECORD_DATE = '"+ timeReport+" '";
			}
			queryMajorIdmap.put("queryTemporarySql", sql);
			//查询符合条件的所有信息
			queryMajorIddataValues=manualEntryService.temporaryTableList(ps,queryMajorIdmap);
		}
		
		if (queryMajorIddataValues == null || queryMajorIddataValues.size()==0) {
			request.setAttribute("msg",
					"未搜索到填充数据，在'" + indexTbModel.getIndexName() + "'指标大类中(归档时间等于"+timeReport+")尚无与该信用码/机构码相关的数据");
			model.addAttribute("reportIndex", sysOrg);
			model.addAttribute("indexTb", indexTb);
			model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
			return "adminObj/edit";
			
		}
//		/**
//		 * 搜索异议处理信息时。如果之前已经存在异议处理信息，那么返回提示信息，不让其在进行操作，
//		 */
//		Map<String, Object> map = new HashMap<>();
//		map.put("RECORD_DATE",timeReport);
//		map.put("DEFAULT_INDEX_ITEM_ID", id);
//		map.put("INDEX_ITEM_ID", indexId);
//		map.put("SYS_ORG_ID", sysOrgId);
//		AdminObjModel adminObj=adminObjService.selectOperateId(map);
//		if(adminObj!=null){
//			request.setAttribute("msg", "该异议已提交，请勿重复提交");
//			model.addAttribute("reportIndex", sysOrg);
//			model.addAttribute("indexTb", indexTb);
//			return "adminObj/edit";
//		}
		/**
		 * 对上传图片进行处理，将图片路径添加到数据库
		 */
//		// 文件名
//        String originalFilename = file.getOriginalFilename();
//        //新的名称加上文件的扩展名
//        String newName = "/" + UUID.randomUUID() + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
//        // 存储的物理路径
//        String subDir = "/userUploadTextFile";
//        String savePath = SettingUtils.getCommonSetting("upload.file.path") + subDir;
//        File newFile = new File(savePath + newName);
//        if (!newFile.exists()) {
//            newFile.mkdirs();
//        }
		String fileName=null;
		if(operateAuthFile.intValue()==1){
			fileName=UpLoadFile.upLoadFile(file);
		}
//		String fileName=UpLoadFile.upLoadFile(file);
		model.addAttribute("code", indexTbModel.getIndexCode().toUpperCase()+"_ID");
		model.addAttribute("IndexTableName", indexTbModel.getIndexCode().toUpperCase()+"_TB");
		model.addAttribute("indexName", indexTbModel.getIndexName());
		model.addAttribute("reportIndex", sysOrg);
		model.addAttribute("file",fileName);
		model.addAttribute("indexTb", indexTb);
		model.addAttribute("reportOrgId", sysOrgId);
		model.addAttribute("orgCreditCode", defaultIndexItemCode);
		model.addAttribute("queryMajorIddataValues", queryMajorIddataValues);
		model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
		return "adminObj/edit";
	}
	/**
	 * 
	 * @param model
	 * @param id  需要进行异议处理数据的id
	 * @param IndexTableName 异议处理表的表名
	 * @param orgCreditCode 组织机构代码
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/show")
	public String show(Model model,@RequestParam(required = false)Integer id,String IndexTableName,HttpServletRequest request,@RequestParam(required = false)String orgCreditCode,String file,Integer reportOrgId,Integer ing,Integer ceshi,Integer itemId) throws Exception{
		Date recordDate=new Date();
		Integer qyId=0;
		Integer indexTbId=0;
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//获取登录用户id
		Integer userId=userDetails.getSys_user_id();
		Integer loginOrgId=userDetails.getSys_org_id();
		if(ceshi!=1){
			if(ing!=null){
				//点击继续按钮，去异议处理详情表中去查询是否存在标注的异议处理选项，如果不存在，则不允许其进行推送
				List<SysOperateListModel> markList=adminObjService.queryMarkByOperateId(ing);
				if(markList!=null &&markList.size()>0){
					AdminObjModel admin=new AdminObjModel();
					admin.setSysOperateStatus(1);
					admin.setSysOperateId(ing);
					adminObjService.updateStatus(admin);
				}
				
//				return "adminObj/edit";
//				return "forward:show.jhtml?id="+id+"&IndexTableName="+IndexTableName+"&orgCreditCode="+orgCreditCode+"&reportOrgId="+reportOrgId+"&ceshi="+1;
			}
		}
		/**
		 * 
		 * 查询异议处理页面的模板信息和对应的字段结果集用于在页面进行显示
		 * 
		 * 
		 */
		PageSupport ps = PageSupport.initPageSupport(request);
		String code=IndexTableName.substring(0, IndexTableName.length()-3);
		//通过获取的code去指标大类中查询对应的指标大类id
		IndexTb indexTbCode=indexTbService.getIndexTbbyIndexCode(code.toLowerCase());
		if(indexTbCode!=null){
			indexTbId=indexTbCode.getIndexId();
		}
		//通过指标大类回去对应的指标项
//		List<IndexItemTb> indexItemTbList = indexTbCode.getIndexItemTbs();
		//通过指标大类id查询所有指标项
		List<IndexItemTb> indexItemTbList = indexItemTbService.queryIndexItemTbsByIndexId(indexTbCode.getIndexId());
		//通过表名和id查询所有数据
		String sql="select * from " +IndexTableName+" where "+ code+"_ID = "+id;
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("queryTemporarySql", sql);
		//查询符合条件的所有信息
		List<Map<String,Object>> queryMajorIddataValues=manualEntryService.temporaryTableList(ps,sqlMap);
		//通过查询出符合条件的二码id和归档时间
		for (int i = 0; i < queryMajorIddataValues.size(); i++) {
			qyId=(Integer)queryMajorIddataValues.get(0).get("DEFAULT_INDEX_ITEM_ID");
			recordDate=(Date)queryMajorIddataValues.get(0).get("RECORD_DATE");
		}
			//点击异议处理，将异议处理信息添加到数据库
			AdminObjModel adminObj=new AdminObjModel();
			adminObj.setAuthFile(file);
			adminObj.setDataId(id);
			adminObj.setDefaultIndexItemId(qyId);
			adminObj.setIndexItemId(indexTbId);
			adminObj.setRecordDate(recordDate);
			adminObj.setSysOperateTime(new Date());
			adminObj.setSysUserId(userId);
//			adminObj.setSysOrgId(userDetails.getSys_org_id());
			adminObj.setReportOrgId(reportOrgId);
			Integer in=adminObjService.insertOrUpdateOperate(adminObj);
			
		
			
			
		
		/**
		 *查询对应的模板信息并进行比对
		 */
		Map<String, Object> mapSon = queryMajorIddataValues.get(0);
		Set<Entry<String, Object>> entries = mapSon.entrySet();
		// 比对列名 按顺序赋值
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		List<Object> valueList = new ArrayList<>();
		for (int j = 0; j < indexItemTbList.size(); j++) {
			for (Entry<String, Object> entry : entries) {
				if (indexItemTbList.get(j).getIndexItemCode().equals(entry.getKey().toLowerCase())) {
					if (entry.getValue() instanceof Date) {
						valueList.add(fm.format(entry.getValue()));
					} else {
						valueList.add(entry.getValue());
					}
					break;
				}
			}
		}
		/**
		 * 用于将存在在数据字典中的数据转化成为相对应的值
		 */
		List<DicContent> dicContentList=dicContentService.queryAllContent();
		List<DicContent> dicList=new ArrayList<>();
		List<List<DicContent>> list=new ArrayList<>();
		for (int i = 0; i < indexItemTbList.size(); i++) {
			if(indexItemTbList.get(i).getIndexItemType()==3){
				Integer dicId=indexItemTbList.get(i).getDicId();
				Dic dic=dicService.getDicByDicId(dicId);
				if(dic!=null){
					if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
						if(valueList.get(i)!=null){
							SysClassFyModel industry=sysClassFyService.queryModelByCode(valueList.get(i).toString());
							if(industry!=null){
								valueList.set(i, industry.getSysIndustryName());
								indexItemTbList.get(i).setDicType(1);
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
//								dicList=dicContentService.getDicContentsByDicId(dicId);
//								list.add(dicList);
									indexItemTbList.get(i).setDicType(4);
								}
							}
						}else{
							valueList.set(i, null);
							indexItemTbList.get(i).setDicType(1);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.ORGANIZATION)){
						if(valueList.get(i)!=null){
						SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(valueList.get(i).toString());
						if(sysOrg!=null){
							valueList.set(i, sysOrg.getSys_org_name());
							indexItemTbList.get(i).setDicType(2);
						}else{
							DicContent dicContent=new DicContent();
							dicContent.setDicId(dic.getDicId());
							dicContent.setDicContentCode(valueList.get(i).toString());
							DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
							if(dics!=null){
								valueList.set(i, dics.getDicContentValue());
//								dicList=dicContentService.getDicContentsByDicId(dicId);
//								list.add(dicList);
								indexItemTbList.get(i).setDicType(4);
							}
						}
						}else{
							valueList.set(i,null);
							indexItemTbList.get(i).setDicType(2);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.Area)){
						if(valueList.get(i)!=null){
							SysArea sysArea=sysAreaService.queryAreaByCode(valueList.get(i).toString());
							if(sysArea!=null ){
								valueList.set(i, sysArea.getSysAreaName());
								indexItemTbList.get(i).setDicType(5);
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}
						}else{
							indexItemTbList.get(i).setDicType(5);
							valueList.set(i,null);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
						if(valueList.get(i)!=null){
//							List<SysGover> so=sysGoverService.querySysGoverByCodeAndName(null, valueList.get(i).toString());
							SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(valueList.get(i).toString());
							if(sysOrg!=null){
								valueList.set(i, sysOrg.getSys_org_name());
								indexItemTbList.get(i).setDicType(3);
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
//								dicList=dicContentService.getDicContentsByDicId(dicId);
//								list.add(dicList);
									indexItemTbList.get(i).setDicType(4);
								}
							}
						}else{
							valueList.set(i,null);
							indexItemTbList.get(i).setDicType(3);
						}
					}else{
						if(valueList.get(i)!=null){
							DicContent dicContent=new DicContent();
							dicContent.setDicId(dic.getDicId());
							dicContent.setDicContentCode(valueList.get(i).toString());
							DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
							if(dics!=null){
								valueList.set(i, dics.getDicContentValue());
//							dicList=dicContentService.getDicContentsByDicId(dicId);
//							list.add(dicList);
								indexItemTbList.get(i).setDicType(4);
							}
						}else{
							valueList.set(i, null);
							indexItemTbList.get(i).setDicType(4);
						}
					}
				}
			}
		}
		
//		for (Object v: valueList) {
//			System.out.println(v);
//			if("null".equals(v)){
//				v=null;
//			}
//		}
		for (int j = 0; j < valueList.size(); j++) {
			if("null".equals(valueList.get(j))){
				valueList.set(j, null);
			}
		}
//		//通过企业二码查询对应的id
//		Integer itemId=0;
//		List<DefaultIndexItem> defaultIndexItemId=defaultIndexItemService.getByCreditOrCode(orgCreditCode);
//		for (int i = 0; i < defaultIndexItemId.size(); i++) {
//			itemId=defaultIndexItemId.get(0).getDefaultIndexItemId();
//		}
		//通过上报机构查询对应的地区id
		SysOrg sysorg=sysOrgService.queryInstitutionsByIdWithNoStatus(reportOrgId);
//		sysOgAreaId=sysorg.getSys_area_id();
		SysArea a=sysAreaService.getUpOrThisSysArea(sysorg.getSys_area_id());
		//通过企业二码查询对应的id
//		DefaultIndexItem defaultIndexItemCredit=defaultIndexItemService.getByCredit(orgCreditCode, a.getSysAreaId());
		String qymc="";
//		Integer itemId=0;
//		if(defaultIndexItemCredit!=null){
//			itemId=defaultIndexItemCredit.getDefaultIndexItemId();
//		}else{
//			List<DefaultIndexItem> defaultIndexItemOrg=defaultIndexItemService.getByCodeOrg(orgCreditCode, a.getSysAreaId());
//			for (int i = 0; i < defaultIndexItemOrg.size(); i++) {
//				itemId=defaultIndexItemOrg.get(0).getDefaultIndexItemId();
//			}
//		}
		//查询所有地区
//		List<SysArea> areaList=sysAreaService.queryAll();
//		model.addAttribute("areaList",areaList);
		//根据机构ID获取机构缓存
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
//			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			so = sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		if(so!=null){
			//如果区域为空，就根据机构所在地址查询
			int areaid = so.getSys_area_id();
			String key = RedisKeys.SYS_AREA + 1;
			SysArea area = RedisUtil.getObjData(key, SysArea.class);
			if(area==null){
				area = sysAreaService.queryAreaById(1);
				RedisUtil.setData(key, area);
			}
			model.addAttribute("areaList", area);
			
//			String key = RedisKeys.SYS_AREA_ALL ;
//		     Type type = new TypeToken<List<SysArea>>() {
//	            }.getType();
//			List<SysArea> aList=RedisUtil.getListData(key, type);
//			if(CollectionUtils.isEmpty(aList)){
//				aList=sysAreaService.queryAll2();
//				RedisUtil.setData(key, aList);
//			}
//			model.addAttribute("areaList", aList);
		}
		//查询所有的行业
		List<SysClassFyModel> sysClassFyModel=sysClassFyService.queryAllSysClassFy(null);
		model.addAttribute("sysClassFyModel", sysClassFyModel);
		//查询所有的机构
		List<SysOrg> sysOrgList=sysOrgService.queryAll();
		model.addAttribute("sysOrgList",sysOrgList);
		//查询所有的政府
//		List<SysGover> sysGoverList = DataUtil.isEmpty(sysGoverService.querySysGoverList(ps,null));
		List<SysOrg> sysGoverList=sysOrgService.queryOrgAll(null, 1);
		model.addAttribute("sysGoverList",sysGoverList);
		//通过企业二码id查询企业二码
	    DefaultIndexItem item=defaultIndexItemService.queryById(itemId);
	    model.addAttribute("IndexTableName", IndexTableName);
	    model.addAttribute("orgCreditCode", orgCreditCode);
	    model.addAttribute("reportOrgId", reportOrgId);
	    model.addAttribute("dtbid", id);
	    model.addAttribute("defaultId", qyId);
	    model.addAttribute("defaultIndexItem", item);
		model.addAttribute("myIndexItemTbList", indexItemTbList);
		model.addAttribute("myValueList", valueList); 
		model.addAttribute("indexTbId", indexTbId);
		model.addAttribute("ing", in);
		model.addAttribute("dicList",list );
		model.addAttribute("dicContentList",dicContentList );
		return "adminObj/show";
	}
	
	
	
	/**
	 * 
	 * @param response
	 * @param request
	 * @param model
	 * @param indexId
	 * @param check
	 * @param recordDate
	 * @param sysOrgId
	 * @param defaultId
	 * @throws IOException
	 */
	@RequestMapping(value="operateList")
	@ResponseBody
	public void operate(HttpServletResponse response,HttpServletRequest request,Model model,@RequestParam(required = false)Integer indexId,@RequestParam(required = false)String  check,
			String recordDate,Integer sysOrgId,Integer  defaultId) throws IOException{
		Integer indexItemId=0;
		if(check!=null && indexId!=null){
			//根据指标大类id和企业二码id和机构id和时间查询异议处理表中的数据
			Map<String, Object> maps = new HashMap<>();
			maps.put("SYS_ORG_ID", sysOrgId);
			maps.put("RECORD_DATE", recordDate);
			maps.put("DEFAULT_INDEX_ITEM_ID", defaultId);
			maps.put("INDEX_ITEM_ID", indexId);
//			AdminObjModel admin=adminObjService.selectOperateByQueryAll(maps);
			List<SysOperateListModel> operateList=adminObjService.selectOperateByQueryAll(maps);
			Gson gson=new Gson();
			response.getWriter().write(gson.toJson(operateList));
			
		}
		
	}
	/**
	 * indexItemCode_val   页面所有指标项的值
	 * indexItemCode_val   页面所有指标项的code值
	 * @param request
	 * @param model
	 * @param indexId  指标大类id
	 * @param check    选中指标项id
	 * @throws Exception 
	 */
	@RequestMapping(value="operate")
	@ResponseBody
	public void operate(HttpServletRequest request,Model model,@RequestParam(required = false)Integer indexId,@RequestParam(required = false)String  check,String org_val
							,String server_val,String maininfo_val,String indexItemCode_val,String itemCode_val,Integer dtbid,Integer ing,Integer yycl,@RequestParam(required = false)String defaultIndexItemid,@RequestParam(required = false)String defaultIndexItemCreditId,@RequestParam(required = false)String valuePlay,@RequestParam(required = false)Integer a) throws Exception{
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
		
//		/**
//		 * 将异议处理后选中的数据存入数据库,
//		 */
		if(yycl==1){
		Integer indexItemId=0;
		if(itemCode_val.length()>1 ){
		itemCode_val=itemCode_val.substring(1,itemCode_val.length()-1);
		String[] array=itemCode_val.split(",",-1);
		server_val=server_val.substring(1,server_val.length()-1);
		String[] server=server_val.split(",",-1);
		check=check.substring(1,check.length()-1);
		String[] checked=check.split(",",-1);
			for (int i = 0; i < array.length; i++) {
				indexItemId=Integer.parseInt(array[i].toString());
				SysOperateListModel sys=new SysOperateListModel();
				sys.setMark(0);
				if(check.length()>0){
					for (int j = 0; j < checked.length; j++) {
						Integer checkedId=Integer.parseInt(checked[j].toString());
						if(indexItemId.intValue()==checkedId.intValue()){
							sys.setMark(1);
						}
					}
				}
					//将异议处理信息指标项数据添加进数据库
					sys.setServerExplain(server[i]);
					sys.setSysOperateTime(new Date());
					sys.setSysOperateId(ing);
					sys.setIndexItemId(indexItemId);
					adminObjService.insertOrUpdateItem(sys);
					//改变异议处理状态
					AdminObjModel admin=new AdminObjModel();
					admin.setSysOperateId(ing);
					admin.setSysOrgId(userDetails.getSys_org_id());
					adminObjService.updateStatus(admin);
			}
			//添加用户行为审计
			SysUserLog sul=new SysUserLog();
			sul.setSysUserLogMenuName("人行异议处理");
			sul.setIndexId(indexId);
			if(StringUtil.isNotEmpty(defaultIndexItemid)|| StringUtil.isNotEmpty(defaultIndexItemCreditId)){
				if(StringUtil.isNotEmpty(defaultIndexItemid)){
					sul.setSysUserLogEnterpriseCode(defaultIndexItemid.toString());
				}else{
					sul.setSysUserLogEnterpriseCode(defaultIndexItemCreditId.toString());
				}
			}
			sul.setSysUserLogOperateType(1);
			sul.setSysUserLogResult(true);
			if(valuePlay.length()>2){
				sul.setSysUserLogOldValue(valuePlay.substring(1, valuePlay.length()-1));
			}else{
				sul.setSysUserLogOldValue("");
			}
			sysUserLogService.insertOneLog(sul,request);
		}
		return;
		}
		
	
		/**
		 * 将异议处理后选中的数据存入数据库,
		 */
		String code="";
		Integer indexItemId=0;
		if(check.length()>1 && indexId!=null){
		check=check.substring(1,check.length()-1);
		String[] array=check.split(",",-1);
			for (int i = 0; i < array.length; i++) {
				indexItemId=Integer.parseInt(array[i].toString());
					SysOperateListModel sys=new SysOperateListModel();
					if( server_val!=null ){
//						String org=org_val.substring(1, org_val.length()-1);
						String server=server_val.substring(1, server_val.length()-1);
						if(maininfo_val.length()>1){
							String maininfo=maininfo_val.substring(1,maininfo_val.length()-1);
							String[] maininfo_v=maininfo.split(",",-1);
							sys.setMaininfoExplain(maininfo_v[i]);
						}
						if(org_val.length()>1){
							String org=org_val.substring(1, org_val.length()-1);
							String[] org_v=org.split(",",-1);
							sys.setOrgExplain(org_v[i]);
						}
//						String[] org_v=org.split(",",-1);			
						String[] server_v=server.split(",",-1);			
						sys.setOrgExplain(null);
						sys.setServerExplain(server_v[i]);
					}
					//将异议处理信息指标项数据添加进数据库
					sys.setSysOperateTime(new Date());
					sys.setSysOperateId(ing);
					sys.setIndexItemId(indexItemId);
					adminObjService.insertOrUpdateItem(sys);
					//将异议处理状态变成异议处理
					AdminObjModel admin=new AdminObjModel();
					admin.setSysOperateStatus(0);
					admin.setSysOperateId(ing);
					adminObjService.updateStatus(admin);
					
				
			}
		}
		//如果为异议处理，则不在执行后面代码，如果为终止，继续执行后面代码
//			if(yycl==1){
//				return;
//			}
		
		
		
		if(a==9){
			//点击终止，将异议处理信息状态改为终止
			AdminObjModel adminObj=new AdminObjModel();
			adminObj.setSysOperateTime(new Date());
			adminObj.setSysOperateStatus(2);
			adminObj.setSysOperateId(ing);
			adminObjService.insertOrUpdate(adminObj);
			
			//添加用户行为审计
			SysUserLog sul=new SysUserLog();
			sul.setSysUserLogMenuName("人行异议处理");
			sul.setIndexId(indexId);
			if(StringUtil.isNotEmpty(defaultIndexItemid)|| StringUtil.isNotEmpty(defaultIndexItemCreditId)){
				if(StringUtil.isNotEmpty(defaultIndexItemid)){
					sul.setSysUserLogEnterpriseCode(defaultIndexItemid.toString());
				}else{
					sul.setSysUserLogEnterpriseCode(defaultIndexItemCreditId.toString());
				}
			}
			sul.setSysUserLogOperateType(3);
			sul.setSysUserLogResult(true);
			sysUserLogService.insertOneLog(sul,request);
			
			return;
			
		}
		
		//点击终止，将异议处理信息状态改为终止
		AdminObjModel adminObj=new AdminObjModel();
		adminObj.setSysOperateTime(new Date());
		adminObj.setSysOperateStatus(2);
		adminObj.setSysOperateId(ing);
		adminObjService.insertOrUpdate(adminObj);
		/**
		 * 将修改后的数据在动态表里面进行修改
		 */
		//通过异议处理表id查询二码id
		AdminObjModel adminObjModel=adminObjService.selectOperateByItemId(ing);
		Integer defaultIndexItemId=adminObjModel.getDefaultIndexItemId();
		//通过二码id查询组织机构代码
		DefaultIndexItem defaultIndexItem=defaultIndexItemService.queryById(defaultIndexItemId);
		//通过指标项id查询所有数据，带指标项
		IndexTb indexTb=indexTbService.queryById(indexId);
		if(indexTb!=null){
			//通过指标大类信息获取指标code
			code=indexTb.getIndexCode();
		}
		//获取所有指标项code
//		List<IndexItemTb> indexItemTb=indexTb.getIndexItemTbs();
		//通过指标大类id查询所有指标项
		List<IndexItemTb> indexItemTb = indexItemTbService.queryIndexItemTbsByIndexId(indexTb.getIndexId());
		for (IndexItemTb indexItemTb2 : indexItemTb) {
			indexItemTb2.getIndexItemCode();
		}
		/**
		 * 查询修改前数据
		 */
		String indexCode = indexTb.getIndexCode();
//		List<IndexItemTb> indexItemTbList = indexTb.getIndexItemTbs();
		//通过指标大类id查询所有指标项
		List<IndexItemTb> indexItemTbList = indexItemTbService.queryIndexItemTbsByIndexId(indexTb.getIndexId());
		String sqllist = "select * from " + code + "_tb where " + code + "_id=" + dtbid;
		Map<String, Object> maps = new HashMap<>();
		maps.put("queryTemporarySql", sqllist);
		// 获取动态表数据
		List<Map<String, Object>> dataValues = manualEntryDao.temporaryTableList(maps);
		Map<String, Object> mapSon = dataValues.get(0);
		Set<Entry<String, Object>> entries = mapSon.entrySet();
		// 比对列名 按顺序赋值
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		List<Object> newValue=new ArrayList<Object>();
		List<Object> valueList = new ArrayList<>();
		for (int j = 0; j < indexItemTbList.size(); j++) {
			for (Entry<String, Object> entry : entries) {
				if (indexItemTbList.get(j).getIndexItemCode().equals(entry.getKey().toLowerCase())) {
					if (entry.getValue() instanceof Date) {
						valueList.add(fm.format(entry.getValue()));
						newValue.add(fm.format(entry.getValue()));
					} else {
						valueList.add(entry.getValue());
						newValue.add(entry.getValue());
					}
					break;
				}
			}
		}
		
		for (int i = 0; i < indexItemTbList.size(); i++) {
			if(indexItemTbList.get(i).getIndexItemType()==3){
				Integer dicId=indexItemTbList.get(i).getDicId();
				Dic dic=dicService.getDicByDicId(dicId);
				if(dic!=null){
					if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
						if(valueList.get(i)!=null){
							SysClassFyModel industry=sysClassFyService.queryModelByCode(valueList.get(i).toString());
							if(industry!=null){
								valueList.set(i, industry.getSysIndustryName());
								newValue.set(i,industry.getSysIndustryCode());
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									newValue.set(i,industry.getSysIndustryCode());
								}
							}
						}else{
							valueList.set(i, null);
							newValue.set(i,null);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.Area)){
						if(valueList.get(i)!=null){
							SysArea sysArea=sysAreaService.queryAreaByCode(valueList.get(i).toString());
							if(sysArea!=null){
								valueList.set(i, sysArea.getSysAreaName());
								newValue.set(i,sysArea.getSysAreaCode());
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									newValue.set(i,sysArea.getSysAreaCode());
								}
							}
						}else{
							valueList.set(i,null);
							newValue.set(i,null);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.ORGANIZATION)||ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
						if(valueList.get(i)!=null){
							SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(valueList.get(i).toString());
							if(sysOrg!=null){
								valueList.set(i, sysOrg.getSys_org_name());
								newValue.set(i,sysOrg.getSys_org_financial_code());
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									newValue.set(i,dics.getDicContentCode());
								}
							}
						}else{
							valueList.set(i, null);
							newValue.set(i,null);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
						if(valueList.get(i)!=null){
//							List<SysGover> so=sysGoverService.querySysGoverByCodeAndName(null, valueList.get(i).toString());
//							if(!so.isEmpty()&&so.size()>0){
//								valueList.set(i, so.get(0).getSysGovName());
							SysOrg so=sysOrgService.querySysorgByFinancialCode(valueList.get(i).toString());
							if(so!=null){
								valueList.set(i, so.getSys_org_name());
								newValue.set(i,so.getSys_org_financial_code());
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									newValue.set(i,so.getSys_org_financial_code());
								}
							}
						}else{
							valueList.set(i,null);
							newValue.set(i,null);
						}
					}else{
						if(valueList.get(i)!=null){
							DicContent dicContent=new DicContent();
							dicContent.setDicId(dic.getDicId());
							dicContent.setDicContentCode(valueList.get(i).toString());
							DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
							if(dics!=null){
								valueList.set(i, dics.getDicContentValue());
								newValue.set(i, dics.getDicContentCode());
							}
						}else{
							valueList.set(i, null);
							newValue.set(i, null);
						}
					}
				}
			}
		}
		/**
		 * 用于将存在在数据字典中的数据转化成为相对应的值
		 * 
		 * 主要是通过对应指标项的dicId去查询对应的指标项，然后去数据字典中查看是否有对应字段，然后根据对应字段进行查询
		 * 
		 *直接循环数据
		 */
		String value="";
		List<Object> valueObj = new ArrayList<>();
		String indexItemCode=indexItemCode_val.substring(1, indexItemCode_val.length()-1);
		String[] indexItemCodeVal=indexItemCode.split(",",-1);
		
		
		
		
		itemCode_val=itemCode_val.substring(1,itemCode_val.length()-1);
		String[] array=itemCode_val.split(",",-1);
		if(itemCode_val.length()>1){
		for (int j = 0; j < indexItemTbList.size(); j++) {
			for (int i = 0; i < array.length; i++) {
				if (indexItemTbList.get(j).getIndexItemCode().equals(array[i])) {
						if(indexItemTbList.get(j).getIndexItemType()==3){
							Integer dicId=indexItemTbList.get(j).getDicId();
							Dic dic=dicService.getDicByDicId(dicId);
							if(dic!=null){
								if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
									SysClassFyModel industry=sysClassFyService.queryByName(indexItemCodeVal[i]);
									if(industry!=null){
										valueObj.add(industry.getSysIndustryCode());
									}else{
										industry=sysClassFyService.queryModelByCode(indexItemCodeVal[i]);
										if(industry!=null){
											valueObj.add(industry.getSysIndustryCode());
										}else{
											DicContent dicContent=new DicContent();
											dicContent.setDicId(dic.getDicId());
											dicContent.setDicContentValue(indexItemCodeVal[i]);
											DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
											if(dics!=null){
												valueObj.add(industry.getSysIndustryCode());
											}else{
												DicContent dicContents=new DicContent();
												dicContents.setDicId(dic.getDicId());
												dicContents.setDicContentCode(indexItemCodeVal[i]);
												dics=manualEntryService.queryDicContentByIdAndCode(dicContents);
													if(dics!=null){
														valueObj.add(industry.getSysIndustryCode());
													}
												}
											}
										}
									}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.ORGANIZATION)||ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
										SysOrg sysOrg=sysOrgService.getSysOrgByName(indexItemCodeVal[i]);
										if(sysOrg!=null){
											valueObj.add(sysOrg.getSys_org_financial_code());
										}else{
											sysOrg=sysOrgService.querySysorgByFinancialCode(indexItemCodeVal[i]);
											if(sysOrg!=null){
												valueObj.add(sysOrg.getSys_org_financial_code());
											}else{
												DicContent dicContent=new DicContent();
												dicContent.setDicId(dic.getDicId());
												dicContent.setDicContentValue(indexItemCodeVal[i]);
												DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
												if(dics!=null){
													valueObj.add(dics.getDicContentCode());
												}else{
													DicContent dicContents=new DicContent();
													dicContents.setDicId(dic.getDicId());
													dicContents.setDicContentCode(indexItemCodeVal[i]);
													dics=manualEntryService.queryDicContentByIdAndCode(dicContents);
													if(dics!=null){
														valueObj.add(dics.getDicContentCode());
													}
												}
											}
										}
									}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
//										List<SysGover> so=sysGoverService.querySysGoverByCodeAndName(indexItemCodeVal[i], null);
//										if(!so.isEmpty()&&so.size()>0){
//											valueObj.add(so.get(0).getSysGovFinancialCode());
										SysOrg so=sysOrgService.getSysOrgByName(indexItemCodeVal[i]);
										if(so!=null){
											valueObj.add(so.getSys_org_financial_code());
										}else{
//											so=sysGoverService.querySysGoverByCodeAndName(null, indexItemCodeVal[i]);
//											if(!so.isEmpty()&&so.size()>0){
//												valueObj.add(so.get(0).getSysGovFinancialCode());
											so=sysOrgService.querySysorgByFinancialCode(indexItemCodeVal[i]);
											if(so!=null){
												valueObj.add(so.getSys_org_financial_code());
											}else{
												DicContent dicContent=new DicContent();
												dicContent.setDicId(dic.getDicId());
												dicContent.setDicContentValue(indexItemCodeVal[i]);
												DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
												if(dics!=null){
													valueObj.add(dics.getDicContentCode());
												}else{
													DicContent dicContents=new DicContent();
													dicContents.setDicId(dic.getDicId());
													dicContents.setDicContentCode(indexItemCodeVal[i]);
													dics=manualEntryService.queryDicContentByIdAndCode(dicContents);
													if(dics!=null){
														valueObj.add(dics.getDicContentCode());
													}
												}
											}
										}
									}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.Area)){
										List<SysArea> sysArea=sysAreaService.queryAreaByNameAndCode(indexItemCodeVal[i], null);
										if(!sysArea.isEmpty()&&sysArea.size()>0){
											valueObj.add(sysArea.get(0).getSysAreaCode());
										}else{
											SysArea sysArea1=sysAreaService.queryAreaByCode(indexItemCodeVal[i]);
											if(sysArea1!=null){
												valueObj.add(sysArea1.getSysAreaCode());
											}else{
												DicContent dicContent=new DicContent();
												dicContent.setDicId(dic.getDicId());
												dicContent.setDicContentValue(indexItemCodeVal[i]);
												DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
												if(dics!=null){
													valueObj.add(dics.getDicContentCode());
												}else{
													DicContent dicContents=new DicContent();
													dicContents.setDicId(dic.getDicId());
													dicContents.setDicContentCode(indexItemCodeVal[i]);
													dics=manualEntryService.queryDicContentByIdAndCode(dicContents);
													if(dics!=null){
														valueObj.add(dics.getDicContentCode());
													}
												}
											}
										}
									}else{
										DicContent dicContent=new DicContent();
										dicContent.setDicId(dic.getDicId());
										dicContent.setDicContentValue(indexItemCodeVal[i]);
										DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
										if(dics!=null){
											valueObj.add(dics.getDicContentCode());
										}else{
											DicContent dicContents=new DicContent();
											dicContents.setDicId(dic.getDicId());
											dicContents.setDicContentCode(indexItemCodeVal[i]);
											dics=manualEntryService.queryDicContentByIdAndCode(dicContents);
											if(dics!=null){
												valueObj.add(dics.getDicContentCode());
											}
											}
										}
									}
						}else{
							valueObj.add(indexItemCodeVal[i]);
						}
				}
			}
		}
		}
		
//		for (int i = 0; i < indexItemCodeVal.length; i++) {
//			value=indexItemCodeVal[i];
//			//判断取出来的值是否是汉字，是汉字则去数据字典或者对应的数据库进行查询
//			Pattern pattern = Pattern.compile(".*[\\u4e00-\\u9faf].*");
//	        Matcher isNum = pattern.matcher(value);
//	        if(isNum.matches()){
//	        	//地区类
//	        	List<SysArea> areaName= sysAreaService.queryAreaByNameAndCode(value, null);
//	        	//行业类
//	        	SysClassFyModel industryName=industryName=sysClassFyService.queryByName(value);
//	        	//机构类
//	        	SysOrg sysOrgName=sysOrgService.getSysOrgByName(value);
//	        	//政府类
//	        	List<SysGover> soName=sysGoverService.querySysGoverByCodeAndName(value,null);
//	        	//数据字典类
//	        	DicContent dicName=dicContentService.getDicIdByDicContentValue(value);
//	        	if(industryName!=null){
//	        		valueObj.add(i, industryName.getSysIndustryCode());
//	        	}
//	        	else if(sysOrgName!=null){
//	        		valueObj.add(i, sysOrgName.getSys_org_financial_code());
//	        	}
//	        	else if(dicName!=null){
//	        		valueObj.add(i, dicName.getDicContentCode());
//	        	}
//	        	else if(soName!=null&&soName.size()>0){
//	        		valueObj.add(i, soName.get(0).getSysGovFinancialCode());
//	        	}else if(areaName!=null&&areaName.size()>0){
//	        		valueObj.add(i, areaName.get(0).getSysAreaCode());
//	        	}else{
//	        		valueObj.add(i, value);
//	        	}
//	        }else{
//	        	valueObj.add(i, value);
//	        }
//		}
//		for (int i = 0; i < indexItemTbList.size(); i++) {
		
		
		
		
		if(itemCode_val!=null ||indexItemCode_val!=null){
			List oldList=new ArrayList<>();
			List newList=new ArrayList<>();
			String itemCode=itemCode_val.substring(0, itemCode_val.length());
			String[] itemCodeVal=itemCode.split(",",-1);
		//执行修改方法,将修改后的动态表信息更新到动态表
		StringBuffer sbc=new StringBuffer();
			for (int i = 0; i < itemCodeVal.length; i++) {
				for (int j = 0; j < valueObj.size(); j++) {
					if(i==j){
//						if(valueObj.get(i).getClass()==Double.TYPE){
//							Integer val=(Integer)valueObj.get(i);
//							if(!val.equals(valueList.get(i))){
//								if(indexItemTbList.get(i).getIndexItemType()==2 || indexItemTbList.get(i).getIndexItemType()==1){
//									if("".equals(indexItemCodeVal[j])){
//										sbc.append(itemCodeVal[i]+"=null,");
//									}else{
//										sbc.append(itemCodeVal[i]+"='"+valueObj.get(j)+"',");
//									}
//								}else{
//									sbc.append(itemCodeVal[i]+"='"+valueObj.get(j)+"',");
//								}
//								oldList.add(valueList.get(i));
//								newList.add(valueObj.get(i));
//							}
//						}else{
						//如果页面的值和动态表的值不相等，就将他们装入各自的list相等则不进行赋值，用于添加用户行为审计
						if(!valueObj.get(i).equals(newValue.get(i))){
							if(indexItemTbList.get(i).getIndexItemType()==2 || indexItemTbList.get(i).getIndexItemType()==1){
								if("".equals(indexItemCodeVal[j])){
									sbc.append(itemCodeVal[i]+"=null,");
								}else{
									sbc.append(itemCodeVal[i]+"='"+valueObj.get(j)+"',");
								}
							}else{
								sbc.append(itemCodeVal[i]+"='"+valueObj.get(j)+"',");
							}
							oldList.add(valueList.get(i));
							newList.add(indexItemCodeVal[j]);
						}else{
							sbc.append(itemCodeVal[i]+"='"+valueObj.get(j)+"',");
						}
//						}
					}
				}
			}
		sbc.deleteCharAt(sbc.length()-1);
		String sql="update "+indexTb.getIndexCode()+"_tb set "+sbc+" where "+indexTb.getIndexCode()+"_id="+dtbid;
		Map<String, Object> map = new HashMap<>();
		map.put("updateIndexTbSql", sql);
		manualEntryDao.updateIndexTbSql(map);
	
		
		/**
		 * indexItemValue    修改了的动态表的字段值 
		 * path  授权文件
		 */
		//添加用户行为审计
		for (int j2 = 0; j2 < newList.size(); j2++) {
			for (int k = j2; k < oldList.size(); ) {
				SysUserLog sul=new SysUserLog();
				sul.setSysUserLogMenuName("人行异议处理");
				sul.setIndexId(indexId);
				sul.setIndexItemId(indexItemTbList.get(j2).getIndexItemId());
				if(oldList.get(k)==null){
					sul.setSysUserLogOldValue(null);
				}else{
					if(indexItemTbList.get(k).getIndexItemType()==3){
						Integer dicId=indexItemTbList.get(k).getDicId();
						//未
						Dic dic=dicService.getDicByDicId(dicId);
						if(dic!=null){
							DicContent dicContent=new DicContent();
							dicContent.setDicId(dic.getDicId());
							dicContent.setDicContentCode(valueList.get(k).toString());
							DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
							if(dics!=null){
								valueList.set(k, dics.getDicContentValue());
							}
						}
					}
					if(oldList.get(k)!=null){
						sul.setSysUserLogOldValue(oldList.get(k).toString());
					}else{
						sul.setSysUserLogOldValue(null);
					}
				}
				if("".equals(itemCodeVal[j2])){
					sul.setSysUserLogNewValue(null);
				}else{
					if(indexItemTbList.get(k).getIndexItemType()==3){
//						String dicName=indexItemTbList.get(k).getIndexItemName();
//						Dic dic=dicService.getDicByDicName(dicName,1);
						Integer dicId=indexItemTbList.get(k).getDicId();
						//未
						Dic dic=dicService.getDicByDicId(dicId);
						if(dic!=null){
							DicContent dicContent=new DicContent();
							dicContent.setDicId(dic.getDicId());
							dicContent.setDicContentCode(itemCodeVal[j2]);
							DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
							if(dics!=null){
								itemCodeVal[j2]=dics.getDicContentValue();
							}
						}
					}
//					sul.setSysUserLogNewValue(valueListNew.get(j2).toString());
					if(newList.get(j2)!=null){
						sul.setSysUserLogNewValue(newList.get(j2).toString());
					}else{
						sul.setSysUserLogNewValue(newList.get(j2).toString());
					}
				}
				//通过异议处理表id查询授权文件路径
				AdminObjModel admin=adminObjService.selectOperateByItemId(ing);
				//企业编码
				sul.setSysUserLogEnterpriseCode(defaultIndexItemId.toString());
				sul.setSysUserLogOperateType(3);
				sul.setSysUserLogAuthFile(admin.getAuthFile());
				sul.setSysUserLogResult(true);
				if(valueList.get(k)==null && !"".equals(itemCodeVal[j2])){
					try {
						sysUserLogService.insertOneLog(sul,request);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(valueList.get(k)!=null && "".equals(itemCodeVal[j2])){
					try {
						sysUserLogService.insertOneLog(sul,request);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(valueList.get(k)!=null && !"".equals(itemCodeVal[j2])){
					if(!valueList.get(k).toString().equals(itemCodeVal[j2])){
						try {
							sysUserLogService.insertOneLog(sul,request);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				break;
		}
	}
		}
		
	}
	/**
	 * 对只标记了异议处理的信息未进行推送的情况可以再次进行推送操作
	 * @param model
	 * @param request
	 * @param indexId
	 * @param sysOrgId
	 * @param operateTime
	 * @param defaultId
	 * @param recordDate
	 * @param reportOrgId
	 * @param dtbId
	 * @param sysOperateId
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value="showOperate")
	public String showOperate(Model model,HttpServletRequest request,Integer indexId,Integer sysOrgId,String operateTime,Integer defaultId,String recordDate,Integer reportOrgId,Integer dtbId,Integer sysOperateId) throws Exception{
		Integer qyId=0;
		PageSupport ps = PageSupport.initPageSupport(request);
		//通过指标大类id查询对应信息
		IndexTb indexTb=indexTbService.queryById(indexId);
		//获取指标大类code
		String indexCode=indexTb.getIndexCode();
		String sql="select * from " +indexCode+"_tb  where "+indexCode+"_ID =" +dtbId;
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("queryTemporarySql", sql);
		//查询符合条件的动态表的所有信息
		List<Map<String,Object>> queryMajorIddataValues=manualEntryService.temporaryTableList(ps,sqlMap);
		dtbId=0;
		if(queryMajorIddataValues!=null){
			for (int i = 0; i < queryMajorIddataValues.size(); i++) {
				String s=indexCode.toUpperCase()+"_ID";
				dtbId=(Integer)queryMajorIddataValues.get(0).get(s);
				qyId=(Integer)queryMajorIddataValues.get(0).get("DEFAULT_INDEX_ITEM_ID");
			}
		}
		//根据指标大类id和企业二码id和机构id和时间查询异议处理表中的数据
		Map<String, Object> maps = new HashMap<>();
		//获取指标项list
		List<SysOperateListModel> operateList=adminObjService.selectOperateByIdAll(sysOperateId);
		/**
		 *查询对应的模板信息并进行比对
		 */
		//通过获取的code去指标大类中查询对应的指标大类id
		IndexTb indexTbCode=indexTbService.getIndexTbbyIndexCode(indexCode.toLowerCase());
		//通过指标大类回去对应的指标项
//		List<IndexItemTb> indexItemTbList = indexTbCode.getIndexItemTbs();
		//通过指标大类id查询所有指标项
		List<IndexItemTb> indexItemTbList = indexItemTbService.queryIndexItemTbsByIndexId(indexTbCode.getIndexId());
		Map<String, Object> mapSon = queryMajorIddataValues.get(0);
		Set<Entry<String, Object>> entries = mapSon.entrySet();
		// 比对列名 按顺序赋值
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		List<Object> valueList = new ArrayList<>();
		for (int j = 0; j < indexItemTbList.size(); j++) {
			for (Entry<String, Object> entry : entries) {
				if (indexItemTbList.get(j).getIndexItemCode().equals(entry.getKey().toLowerCase())) {
					if (entry.getValue() instanceof Date) {
						valueList.add(fm.format(entry.getValue()));
					} else {
						valueList.add(entry.getValue());
					}
					break;
				}
			}
		}
		/**
		 * 用于将存在在数据字典中的数据转化成为相对应的值
		 */
		List<DicContent> dicList=new ArrayList<>();
		List<List<DicContent>> list=new ArrayList<>();
		for (int i = 0; i < indexItemTbList.size(); i++) {
			if(indexItemTbList.get(i).getIndexItemType()==3){
				Integer dicId=indexItemTbList.get(i).getDicId();
				Dic dic=dicService.getDicByDicId(dicId);
				if(dic!=null){
					if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
						if(valueList.get(i)!=null){
							SysClassFyModel industry=sysClassFyService.queryModelByCode(valueList.get(i).toString());
							if(industry!=null){
								valueList.set(i, industry.getSysIndustryName());
								indexItemTbList.get(i).setDicType(1);
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}
						}else{
							valueList.set(i, null);
							indexItemTbList.get(i).setDicType(1);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.Area)){
						if(valueList.get(i)!=null){
							SysArea sysArea=sysAreaService.queryAreaByCode(valueList.get(i).toString());
							if(sysArea!=null){
								valueList.set(i, sysArea.getSysAreaName());
								indexItemTbList.get(i).setDicType(5);
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}
						}else{
							valueList.set(i, null);
							indexItemTbList.get(i).setDicType(5);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.ORGANIZATION)){
						if(valueList.get(i)!=null){
							SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(valueList.get(i).toString());
							if(sysOrg!=null){
								valueList.set(i, sysOrg.getSys_org_name());
								indexItemTbList.get(i).setDicType(2);
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}
						}else{
							valueList.set(i, null);
							indexItemTbList.get(i).setDicType(2);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
						if(valueList.get(i)!=null){
//							List<SysGover> so=sysGoverService.querySysGoverByCodeAndName(null, valueList.get(i).toString());
//							if(!so.isEmpty()&&so.size()>0){
//								valueList.set(i, so.get(0).getSysGovName());
							SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(valueList.get(i).toString());
							if(sysOrg!=null){
								valueList.set(i, sysOrg.getSys_org_name());
								indexItemTbList.get(i).setDicType(3);
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}
						}else{
							valueList.set(i,null);
							indexItemTbList.get(i).setDicType(3);
						}
					}else{
						if(valueList.get(i)!=null){
							DicContent dicContent=new DicContent();
							dicContent.setDicId(dic.getDicId());
							dicContent.setDicContentCode(valueList.get(i).toString());
							DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
							if(dics!=null){
								valueList.set(i, dics.getDicContentValue());
								indexItemTbList.get(i).setDicType(4);
							}
						}else{
							valueList.set(i, null);
							indexItemTbList.get(i).setDicType(4);
						}
					}
				}
			}
		}
		for (int j = 0; j < valueList.size(); j++) {
			if("null".equals(valueList.get(j))){
				valueList.set(j, null);
			}
		}
		//查询所有地区
//		List<SysArea> areaList=sysAreaService.queryAll();
//		model.addAttribute("areaList", areaList);
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		//根据机构ID获取机构缓存
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
//			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			so = sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		if(so!=null){
			//如果区域为空，就根据机构所在地址查询
			int areaid = so.getSys_area_id();
			String key = RedisKeys.SYS_AREA + 1;
			SysArea area = RedisUtil.getObjData(key, SysArea.class);
			if(area==null){
				area = sysAreaService.queryAreaById(1);
				RedisUtil.setData(key, area);
			}
			model.addAttribute("areaList", area);
//			String key = RedisKeys.SYS_AREA_ALL ;
//		     Type type = new TypeToken<List<SysArea>>() {
//	            }.getType();
//			List<SysArea> aList=RedisUtil.getListData(key, type);
//			if(CollectionUtils.isEmpty(aList)){
//				aList=sysAreaService.queryAll2();
//				RedisUtil.setData(key, aList);
//			}
//			model.addAttribute("areaList", aList);
		}
		List<DicContent> dicContentList=dicContentService.queryAllContent();
		//查询所有的行业
		List<SysClassFyModel> sysClassFyModel=sysClassFyService.queryAllSysClassFy(null);
		model.addAttribute("sysClassFyModel", sysClassFyModel);
		//查询所有的机构
		List<SysOrg> sysOrgList=sysOrgService.queryAll();
		model.addAttribute("sysOrgList",sysOrgList);
		//查询所有的政府
//		List<SysGover> sysGoverList = DataUtil.isEmpty(sysGoverService.querySysGoverList(ps,null));
		List<SysOrg> sysGoverList=sysOrgService.queryOrgAll(null, 1);
		model.addAttribute("sysGoverList",sysGoverList);
		//通过企业二码id查询企业二码
	    DefaultIndexItem item=defaultIndexItemService.queryById(qyId);
		model.addAttribute("myIndexItemTbList", indexItemTbList);
		model.addAttribute("dtbid", dtbId);
		model.addAttribute("ing", sysOperateId);
		model.addAttribute("indexTbId", indexId);
		model.addAttribute("myValueList", valueList); 
		model.addAttribute("defaultIndexItem", item); 
		model.addAttribute("operateList", operateList);
//		model.addAttribute("dicList",list );
		model.addAttribute("dicContentList",dicContentList );
		return "adminObj/showOperate";
	}
	
	
	@RequestMapping(value="revise")
	public String revise(Model model,HttpServletRequest request,Integer indexId,Integer sysOrgId,String operateTime,Integer defaultId,String recordDate,Integer reportOrgId,Integer dtbId,Integer sysOperateId,Integer qb) throws Exception{
		Integer qyId=0;
		PageSupport ps = PageSupport.initPageSupport(request);
		//通过指标大类id查询对应信息
//		IndexTb indexTb=indexTbService.queryIdByName(indexName);
		IndexTb indexTb=indexTbService.queryById(indexId);
		//获取指标大类code
		String indexCode=indexTb.getIndexCode();
//		String sql="select * from " +indexCode+"_tb  where default_index_item_id= " + defaultId+" and sys_org_id= " +reportOrgId+" and record_date = '" +recordDate+" '";
//		String sql="select * from " +indexCode+"_tb  where default_index_item_id= " + defaultId+" and sys_org_id= " +reportOrgId;
		String sql="select * from " +indexCode+"_tb  where "+indexCode+"_ID =" +dtbId;
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("queryTemporarySql", sql);
		//查询符合条件的动态表的所有信息
		List<Map<String,Object>> queryMajorIddataValues=manualEntryService.temporaryTableList(ps,sqlMap);
		dtbId=0;
		if(queryMajorIddataValues!=null){
			for (int i = 0; i < queryMajorIddataValues.size(); i++) {
				String s=indexCode.toUpperCase()+"_ID";
				dtbId=(Integer)queryMajorIddataValues.get(0).get(s);
				qyId=(Integer)queryMajorIddataValues.get(0).get("DEFAULT_INDEX_ITEM_ID");
			}
		}
		//根据指标大类id和企业二码id和机构id和时间查询异议处理表中的数据
		Map<String, Object> maps = new HashMap<>();
//		maps.put("SYS_ORG_ID", sysOrgId);
//		maps.put("RECORD_DATE", recordDate);
//		maps.put("DEFAULT_INDEX_ITEM_ID", defaultId);
//		maps.put("INDEX_ITEM_ID", indexId);
//		AdminObjModel admin=adminObjService.selectOperateByQueryAll(maps);
		//获取指标项list
//		maps.put("sysOperateId", sysOperateId);
		List<SysOperateListModel> operateList=adminObjService.selectOperateByIdAll(sysOperateId);
//		if(operateList==null){
//			operateList=adminObjService.selectOperateId1(maps);
//		}
		
		/**
		 *查询对应的模板信息并进行比对
		 */
		//通过获取的code去指标大类中查询对应的指标大类id
		IndexTb indexTbCode=indexTbService.getIndexTbbyIndexCode(indexCode.toLowerCase());
		//通过指标大类回去对应的指标项
//		List<IndexItemTb> indexItemTbList = indexTbCode.getIndexItemTbs();
		//通过指标大类id查询所有指标项
		List<IndexItemTb> indexItemTbList = indexItemTbService.queryIndexItemTbsByIndexId(indexTbCode.getIndexId());
		Map<String, Object> mapSon = queryMajorIddataValues.get(0);
		Set<Entry<String, Object>> entries = mapSon.entrySet();
		// 比对列名 按顺序赋值
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		List<Object> valueList = new ArrayList<>();
		for (int j = 0; j < indexItemTbList.size(); j++) {
			for (Entry<String, Object> entry : entries) {
				if (indexItemTbList.get(j).getIndexItemCode().equals(entry.getKey().toLowerCase())) {
					if (entry.getValue() instanceof Date) {
						valueList.add(fm.format(entry.getValue()));
					} else {
						valueList.add(entry.getValue());
					}
					break;
				}
			}
		}
		/**
		 * 用于将存在在数据字典中的数据转化成为相对应的值
		 */
		List<DicContent> dicList=new ArrayList<>();
		List<List<DicContent>> list=new ArrayList<>();
		for (int i = 0; i < indexItemTbList.size(); i++) {
			if(indexItemTbList.get(i).getIndexItemType()==3){
				Integer dicId=indexItemTbList.get(i).getDicId();
				Dic dic=dicService.getDicByDicId(dicId);
				if(valueList.get(i)!=null){}
				if(dic!=null){
					if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
						if(valueList.get(i)!=null){
							SysClassFyModel industry=sysClassFyService.queryModelByCode(valueList.get(i).toString());
							if(industry!=null){
								valueList.set(i, industry.getSysIndustryName());
								indexItemTbList.get(i).setDicType(1);
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
//								dicList=dicContentService.getDicContentsByDicId(dicId);
//								list.add(dicList);
									indexItemTbList.get(i).setDicType(4);
								}
							}
						}else{
							valueList.set(i,null);
							indexItemTbList.get(i).setDicType(1);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.Area)){
						if(valueList.get(i)!=null){
							SysArea sysArea=sysAreaService.queryAreaByCode(valueList.get(i).toString());
							if(sysArea!=null){
								valueList.set(i, sysArea.getSysAreaName());
								indexItemTbList.get(i).setDicType(5);
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}
						}else{
							valueList.set(i, null);
							indexItemTbList.get(i).setDicType(5);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.ORGANIZATION)||ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
						if(valueList.get(i)!=null){
							SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(valueList.get(i).toString());
							if(sysOrg!=null){
								valueList.set(i, sysOrg.getSys_org_name());
								if(sysOrg.getSys_org_type()==0){
									indexItemTbList.get(i).setDicType(2);
								}else{
									indexItemTbList.get(i).setDicType(3);
									
								}
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
//								dicList=dicContentService.getDicContentsByDicId(dicId);
//								list.add(dicList);
									indexItemTbList.get(i).setDicType(4);
								}
							}
						}else{
							valueList.set(i,null);
							indexItemTbList.get(i).setDicType(2);
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
						if(valueList.get(i)!=null){
							SysOrg so=sysOrgService.querySysorgByFinancialCode(valueList.get(i).toString());
//							List<SysGover> so=sysGoverService.querySysGoverByCodeAndName(null, valueList.get(i).toString());
							if(so!=null){
								valueList.set(i, so.getSys_org_name());
								indexItemTbList.get(i).setDicType(3);
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
//								dicList=dicContentService.getDicContentsByDicId(dicId);
//								list.add(dicList);
									indexItemTbList.get(i).setDicType(4);
								}
							}
						}else{
							valueList.set(i, null);
							indexItemTbList.get(i).setDicType(3);
						}
					}else{
						if(valueList.get(i)!=null){
							DicContent dicContent=new DicContent();
							dicContent.setDicId(dic.getDicId());
							dicContent.setDicContentCode(valueList.get(i).toString());
							DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
							if(dics!=null){
								valueList.set(i, dics.getDicContentValue());
//							dicList=dicContentService.getDicContentsByDicId(dicId);
//							list.add(dicList);
								indexItemTbList.get(i).setDicType(4);
							}
						}else{
							valueList.set(i,null);
							indexItemTbList.get(i).setDicType(4);
						}
					}
				}
			}
		}
		for (int j = 0; j < valueList.size(); j++) {
			if("null".equals(valueList.get(j))){
				valueList.set(j, null);
			}
		}
		
		//查询所有地区
//		List<SysArea> areaList=sysAreaService.queryAll();
//		model.addAttribute("areaList", areaList);
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		//根据机构ID获取机构缓存
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
//			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			so = sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		if(so!=null){
//			如果区域为空，就根据机构所在地址查询
			int areaid = so.getSys_area_id();
			String key = RedisKeys.SYS_AREA + 1;
			SysArea area = RedisUtil.getObjData(key, SysArea.class);
			if(area==null){
				area = sysAreaService.queryAreaById(1);
				RedisUtil.setData(key, area);
			}
			model.addAttribute("areaList", area);
//			String key = RedisKeys.SYS_AREA_ALL ;
//		     Type type = new TypeToken<List<SysArea>>() {
//	            }.getType();
//			List<SysArea> aList=RedisUtil.getListData(key, type);
//			if(CollectionUtils.isEmpty(aList)){
//				aList=sysAreaService.queryAll2();
//				RedisUtil.setData(key, aList);
//			}
//			model.addAttribute("areaList", aList);
		}
		List<DicContent> dicContentList=dicContentService.queryAllContent();
		//查询所有的行业
		List<SysClassFyModel> sysClassFyModel=sysClassFyService.queryAllSysClassFy(null);
		model.addAttribute("sysClassFyModel", sysClassFyModel);
		//查询所有的机构
		List<SysOrg> sysOrgList=sysOrgService.queryAll();
		model.addAttribute("sysOrgList",sysOrgList);
		//查询所有的政府
//		List<SysGover> sysGoverList = DataUtil.isEmpty(sysGoverService.querySysGoverList(ps,null));
		List<SysOrg> sysGoverList=sysOrgService.queryOrgAll(null, 1);
		model.addAttribute("sysGoverList",sysGoverList);
		String type=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
		//通过企业二码id查询企业二码
	    DefaultIndexItem item=defaultIndexItemService.queryById(qyId);
		model.addAttribute("myIndexItemTbList", indexItemTbList);
		model.addAttribute("dtbId", dtbId);
		model.addAttribute("indexTbId", indexId);
		model.addAttribute("myValueList", valueList); 
		model.addAttribute("defaultIndexItem", item); 
		model.addAttribute("operateList", operateList);
		model.addAttribute("qb", qb);
		model.addAttribute("type", type);
//		model.addAttribute("dicList",list );
		model.addAttribute("dicContentList",dicContentList );
		return "adminObj/report";
	}
}
