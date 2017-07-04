package com.workmanagement.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.reflect.TypeToken;
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
import com.workmanagement.service.DefaultIndexItemService;
import com.workmanagement.service.DicContentService;
import com.workmanagement.service.DicService;
import com.workmanagement.service.IndexItemTbService;
import com.workmanagement.service.IndexTbService;
import com.workmanagement.service.ManualEntryService;
import com.workmanagement.service.OrgObjService;
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
import com.workmanagement.util.UpLoadFile;

/**
 * 金融机构异议处理
 * @author tianhao
 *
 */
@Controller
@RequestMapping("/admin/orgObj")
public class OrgObjController {
	
	@Autowired
	private OrgObjService orgObjService;
	
	@Autowired
	private SysOrgService sysOrgService;
	
	@Autowired
	private ManualEntryService manualEntryService;
	
	@Autowired
	private SysAreaService sysAreaService;
	
	@Autowired
	private DefaultIndexItemService defaultIndexItemService;
	
	@Autowired
	private IndexTbService indexTbService;
	
	@Autowired
	private DicService dicService;
	
	@Autowired
	private SysClassFyService sysClassFyService;
	
	@Autowired
	private SysGoverService sysGoverService;
	
	@Autowired
	private DicContentService dicContentService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysOtherManageService sysOtherManageService;
	
	@Autowired
	private IndexItemTbService indexItemTbService;
	
	@Autowired
    private SysUserLogService sysUserLogService;

	/**
	 * 金融机构异议处理列表页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(HttpServletRequest request,Model model,@RequestParam(required=false) String jbxxQymc,
			@RequestParam(required=false) String defaultIndexItemCode,@RequestParam(required=false) Integer indexId,
			@RequestParam(required=false) String recordDate,@RequestParam(required=false) Integer selectId,
			@RequestParam(required=false) Integer status,@RequestParam(required=false) String msgs){
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> map = new HashMap<String, Object>();
		//根据机构ID获取机构缓存
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
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
		List<Integer> areaIds = new ArrayList<>();
		List<Integer> idup=sysAreaService.getAllUpAreaIds(sa.getSysAreaId());
		List<Integer> idsub=sysAreaService.getAllSubAreaIds(sa.getSysAreaId());
		areaIds.addAll(idup);
		areaIds.addAll(idsub);
		Set<Integer> setAreaIds=new HashSet<>();
		setAreaIds.addAll(areaIds);
		map.put("areaId", setAreaIds);
		List<IndexTb> indexTbs=manualEntryService.queryIndexTbAll(map);
		if(indexTbs.size()==0){
			Map<String, Object> indexMap = new HashMap<String, Object>();
			indexTbs=manualEntryService.queryIndexTbAll(indexMap);
		}
		for (int i = 0; i < indexTbs.size(); i++) {
			if(indexTbs.get(i).getSysAreaId()!=1){
				SysArea area=sysAreaService.queryAreaById(indexTbs.get(i).getSysAreaId());
				String name=area.getSysAreaName()+"-"+indexTbs.get(i).getIndexName();
				indexTbs.get(i).setIndexName(name);
			}
		}
		if(selectId!=null){
			String sql="SELECT 	(SELECT INDEXTB.INDEX_NAME FROM INDEX_TB INDEXTB WHERE INDEXTB.INDEX_ID=OPERATE.INDEX_ITEM_ID) AS indexName,"
				+"(SELECT ORG.SYS_ORG_NAME FROM SYS_ORG_TB ORG WHERE ORG.SYS_ORG_ID=OPERATE.SYS_ORG_ID) AS sysOrgName,"
				+"OPERATE.SYS_OPERATE_TIME AS sysOperateTime,"
				+"OPERATE.RECORD_DATE AS recordDate,"
				+"OPERATE.SYS_OPERATE_STATUS AS sysOperateStatus,"
				+"OPERATE.DEFAULT_INDEX_ITEM_ID AS defaultIndexItemId,"
				+"OPERATE.INDEX_ITEM_ID AS indexItemId,"
				+"OPERATE.SYS_ORG_ID AS sysOrgId,"
				+"OPERATE.SYS_OPERATE_ID AS sysOperateId,"
				+"OPERATE.REPORT_ORG_ID AS reportOrgId,"
				+"OPERATE.DATA_ID AS dataId,"
				+"(SELECT JBXX.INDEX_JBXX_QYMC FROM INDEX_JBXX_TB JBXX WHERE JBXX.DEFAULT_INDEX_ITEM_ID=OPERATE.DEFAULT_INDEX_ITEM_ID FETCH FIRST 1 ROWS ONLY) AS jbxxQimc,"
				+"(SELECT DEFAULTINDEX.CODE_CREDIT FROM DEFAULT_INDEX_ITEM_TB DEFAULTINDEX WHERE DEFAULTINDEX.DEFAULT_INDEX_ITEM_ID=OPERATE.DEFAULT_INDEX_ITEM_ID) AS codeCredit,"
				+"(SELECT DEFAULTINDEX.CODE_ORG FROM DEFAULT_INDEX_ITEM_TB DEFAULTINDEX WHERE DEFAULTINDEX.DEFAULT_INDEX_ITEM_ID=OPERATE.DEFAULT_INDEX_ITEM_ID) AS codeOrg,"
				+"(SELECT ORG.SYS_ORG_NAME FROM SYS_ORG_TB ORG WHERE ORG.SYS_ORG_ID=OPERATE.REPORT_ORG_ID) AS sysReportOrgName"
				+" FROM SYS_OPERATE_TB OPERATE"
				+" WHERE (OPERATE.SYS_ORG_ID="+userDetails.getSys_org_id()+" OR (OPERATE.REPORT_ORG_ID="+userDetails.getSys_org_id()+" AND OPERATE.SYS_OPERATE_STATUS!=0))";
			String condition="";
			String url="/admin/orgObj/list.jhtml?";
			if(!"".equals(defaultIndexItemCode) || !"".equals(jbxxQymc)){
				Map<String, Object> maps = new HashMap<String, Object>();
				if("".equals(defaultIndexItemCode)){
					maps.put("defaultIndexItemCode", null);
				}else{
					maps.put("defaultIndexItemCode", defaultIndexItemCode);
					condition+="统一社会信用代码/组织机构代码["+defaultIndexItemCode+"] ";
					url+="defaultIndexItemCode="+defaultIndexItemCode+"&";
				}
				if("".equals(jbxxQymc)){
					maps.put("jbxxQymc", null);
				}else{
					maps.put("jbxxQymc", jbxxQymc);
					condition+="企业名称["+jbxxQymc+"] ";
					url+="jbxxQymc="+jbxxQymc+"&";
				}
				String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
				if(!"1".equals(role) && !"7".equals(role)){
					SysArea area=sysAreaService.getUpOrThisSysArea(so.getSys_area_id());
					maps.put("areaId", area.getSysAreaId());
				}else{
					maps.put("areaId", null);
				}
				List<Integer> queryDefaultId=orgObjService.queryDefaultId(maps);
				Integer[] defaultId=new Integer[queryDefaultId.size()];
				for (int i = 0; i < queryDefaultId.size(); i++) {
					defaultId[i]=queryDefaultId.get(i);
				}
				if(defaultId.length>0){
					map.put("defaultId", defaultId);
					String defaultIds="";
					for (int i = 0; i < defaultId.length; i++) {
						defaultIds+=defaultId[i]+",";
					}
					defaultIds=defaultIds.substring(0, defaultIds.length()-1);
					sql+=" AND OPERATE.DEFAULT_INDEX_ITEM_ID IN ("+defaultIds+")";
				}else{
					if(msgs!=null){
						model.addAttribute("msgs", msgs);
					}
					model.addAttribute("msg", "企业二码和企业名称不匹配");
					model.addAttribute("indexTbs", indexTbs);
					map.put("sysOrgId", so.getSys_org_id());
					map.put("reporateType", 2);
					model.addAttribute("orgId", so.getSys_org_id());
					PageSupport ps = PageSupport.initPageSupport(request);
					List<AdminObjModel> sysOperateList=orgObjService.querySysOperateList(ps, map);
					model.addAttribute("sysOperateList", sysOperateList);
					return "orgObj/list";
				}
			}else{
				map.put("defaultId", null);
			}
			if(indexId==null || "".equals(indexId)){
				map.put("indexItemId", null);
			}else{
				map.put("indexItemId", indexId);
				sql+=" AND OPERATE.INDEX_ITEM_ID="+indexId;
				IndexTb indexTb = manualEntryService.queryIndexTbById(indexId);
				condition+="指标大类["+indexTb.getIndexName()+"] ";
				url+="indexId="+indexId+"&";
			}
			if(recordDate==null || "".equals(recordDate)){
				map.put("recordDate", null);
			}else{
				map.put("recordDate", recordDate);
				sql+=" AND subStr(OPERATE.SYS_OPERATE_TIME,1,10)='"+recordDate+"'";
				condition+="推送时间["+recordDate+"] ";
				url+="recordDate="+recordDate+"&";
			}
			if(status==null || "".equals(status)){
				map.put("status", null);
			}else{
				map.put("status", status);
				sql+=" AND OPERATE.SYS_OPERATE_STATUS="+status;
				String status1=null;
				if(status==0){
					status1="待处理";
				}else if(status==1){
					status1="处理中";
				}else{
					status1="已处理";
				}
				condition+="处理状态["+status1+"]";
				url+="status="+status;
			}
			sql+=" ORDER BY OPERATE.SYS_OPERATE_TIME DESC";
			SysUserLog sul=new SysUserLog();
			sul.setSysUserLogMenuName("机构异议处理");
			sul.setIndexId(indexId);
			sul.setSysUserLogOperateType(4);
			String sql1 = StringUtils.replace(sql, " ", "|");
			sul.setSysUserLogQuerySql(sql1);
			sul.setSysUserLogResult(true);
			sul.setSysUserLogQueryUserCondition(condition);
			map.put("sysOrgId", so.getSys_org_id());
			map.put("reporateType", 2);
			List<AdminObjModel> operateList=orgObjService.querySysOperateList(null, map);
			sul.setSysUserLogCount(operateList.size());
			sul.setSysUserLogUrl(url);
			sysUserLogService.insertOneLog(sul,request);
			
			model.addAttribute("jbxxQymc", jbxxQymc);
			model.addAttribute("defaultIndexItemCode", defaultIndexItemCode);
			model.addAttribute("indexId", indexId);
			if(indexId!=null && !"".equals(indexId)){
				IndexTb indexTb=indexTbService.queryById(indexId);
				if(indexTb!=null){
					model.addAttribute("indexName", indexTb.getIndexName());
				}
			}
			model.addAttribute("recordDate", recordDate);
			model.addAttribute("selectId", selectId);
			model.addAttribute("status", status);
		}else{
			map.put("defaultId", null);
			map.put("indexItemId", null);
			map.put("recordDate", null);
		}
		if(msgs!=null){
			model.addAttribute("msgs", msgs);
		}
		model.addAttribute("indexTbs", indexTbs);
		map.put("sysOrgId", so.getSys_org_id());
		map.put("reporateType", 2);
		model.addAttribute("orgId", so.getSys_org_id());
		PageSupport ps = PageSupport.initPageSupport(request);
		List<AdminObjModel> sysOperateList=orgObjService.querySysOperateList(ps, map);
		model.addAttribute("sysOperateList", sysOperateList);
		return "orgObj/list";
	}
	
	/**
	 * 金融机构异议处理检索、列表页面
	 * @param request
	 * @param model
	 * @param file
	 * @param timeReport
	 * @param companyName
	 * @param indexId
	 * @param defaultIndexItemCode
	 * @param sysOrgId
	 * @param addOrlist
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public String edit(HttpServletRequest request,Model model,@RequestParam(required=false) MultipartFile file,
			@RequestParam(required=false) String timeReport,@RequestParam(required=false) String companyName,
			@RequestParam(required=false) Integer indexId,@RequestParam(required=false) String defaultIndexItemCode,
			@RequestParam(required=false) Integer sysOrgId,@RequestParam(required=false) Integer addOrlist,
			@RequestParam(required=false) Integer operateAuthFile) throws Exception{
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取管辖区域
		String areaKey=RedisKeys.SYS_AREA+so.getSys_area_id();
		SysArea sa = RedisUtil.getObjData(areaKey, SysArea.class);
		if(sa==null){
			sa = sysAreaService.queryAreaById(so.getSys_area_id());
			RedisUtil.setData(areaKey, sa);
		}
		// 查询管辖地区及子地区的ID
		List<Integer> areaIds = new ArrayList<>();
		List<Integer> idup=sysAreaService.getAllUpAreaIds(sa.getSysAreaId());
		List<Integer> idsub=sysAreaService.getAllSubAreaIds(sa.getSysAreaId());
		areaIds.addAll(idup);
		areaIds.addAll(idsub);
		Set<Integer> setAreaIds=new HashSet<>();
		setAreaIds.addAll(areaIds);
		map.put("areaId", setAreaIds);
		String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
		if("1".equals(role)){
			map.put("orgType", 3);
		}else{
			map.put("orgType", 2);
		}
		map.put("orgIds", so.getSys_org_id());
		List<IndexTb> indexTbs=manualEntryService.queryIndexTbAll(map);
		if(indexTbs.size()==0){
			Map<String, Object> indexMap = new HashMap<String, Object>();
			indexTbs=manualEntryService.queryIndexTbAll(indexMap);
		}
		for (int i = 0; i < indexTbs.size(); i++) {
			if(indexTbs.get(i).getSysAreaId()!=1){
				SysArea area=sysAreaService.queryAreaById(indexTbs.get(i).getSysAreaId());
				String name=area.getSysAreaName()+"-"+indexTbs.get(i).getIndexName();
				indexTbs.get(i).setIndexName(name);
			}
		}
//		List<SysOrg> sysOrgs=manualEntryService.querySysOrgAll(map);
		SysOtherManage stm=sysOtherManageService.querySysOtherManage(userDetails.getSys_user_id());
		if(addOrlist!=null && addOrlist==1){
			Pattern pattern = Pattern.compile(".*[\\u4e00-\\u9faf].*");
	        Matcher isNum = pattern.matcher(defaultIndexItemCode);
	        if(isNum.matches()){
	        	request.setAttribute("msg", "统一社会信用码/组织机构代码不能为汉字");
	        	model.addAttribute("indexTbs", indexTbs);
//				model.addAttribute("sysOrgs", sysOrgs);
				model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
				return "orgObj/edit";
	        }
	        if(defaultIndexItemCode.length()>18){
	        	request.setAttribute("msg", "统一社会信用码/组织机构代码长度不能大于18位");
	        	model.addAttribute("indexTbs", indexTbs);
//				model.addAttribute("sysOrgs", sysOrgs);
				model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
				return "orgObj/edit";
	        }
	        Integer areaId;
	        if(sysOrgId!=null && !"".equals(sysOrgId)){
	        	 SysOrg sysorg=sysOrgService.queryInstitutionsByIdWithNoStatus(sysOrgId);
	        	 SysArea area=sysAreaService.getUpOrThisSysArea(sysorg.getSys_area_id());
	        	 areaId=area.getSysAreaId();
	        }else{
	        	 SysArea area=sysAreaService.getUpOrThisSysArea(so.getSys_area_id());
	        	 areaId=area.getSysAreaId();
	        	 sysOrgId=so.getSys_org_id();
	        }
			DefaultIndexItem defaultIndexItem = manualEntryService.getDefaultIndexItemByCode(defaultIndexItemCode,areaId);
			if (defaultIndexItem == null) {
				if(sysOrgId!=null && !"".equals(sysOrgId)){
					SysOrg sysorg=sysOrgService.queryInstitutionsByIdWithNoStatus(sysOrgId);
					defaultIndexItem = manualEntryService.getDefaultIndexItemByCode(defaultIndexItemCode,sysorg.getSys_area_id());
				}else{
					defaultIndexItem = manualEntryService.getDefaultIndexItemByCode(defaultIndexItemCode,so.getSys_area_id());
				}
				if(defaultIndexItem == null){
					request.setAttribute("msg", "该信用码/机构码不存在,请重新输入");
					model.addAttribute("indexTbs", indexTbs);
//					model.addAttribute("sysOrgs", sysOrgs);
					model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
					return "orgObj/edit";
				}
			}
			Integer defaultAreaId;
			if(sysOrgId!=null && !"".equals(sysOrgId)){
				SysOrg sysorg=sysOrgService.queryInstitutionsByIdWithNoStatus(sysOrgId);
				defaultAreaId=sysorg.getSys_area_id();
			}else{
				defaultAreaId=so.getSys_area_id();
			}
			/**
			 * 通过传入的二码和企业名称去进行判断输入的信息是否匹配，如果匹配，则进行异议处理操作，如果不匹配，则返回异常
			 */
			//通过企业二码查询对应的id
			List<DefaultIndexItem> dlist =defaultIndexItemService.getByCredit(defaultIndexItemCode, areaId);
			DefaultIndexItem defaultIndexItemCredit = null;
			if(CollectionUtils.isNotEmpty(dlist)){
				defaultIndexItemCredit = dlist.get(0);
			}
			String qymc="";
			Integer id=0;
			if(defaultIndexItemCredit!=null){
				id=defaultIndexItemCredit.getDefaultIndexItemId();
			}else{
				dlist=defaultIndexItemService.getByCredit(defaultIndexItemCode, defaultAreaId);
				if(CollectionUtils.isNotEmpty(dlist)){
					defaultIndexItemCredit = dlist.get(0);
				}
				if(defaultIndexItemCredit!=null){
					id=defaultIndexItemCredit.getDefaultIndexItemId();
				}else{
					List<DefaultIndexItem> defaultIndexItemOrg=defaultIndexItemService.getByCodeOrg(defaultIndexItemCode, areaId);
					if(defaultIndexItemOrg.size()>0){
						for (int i = 0; i < defaultIndexItemOrg.size(); i++) {
							id=defaultIndexItemOrg.get(0).getDefaultIndexItemId();
						}
					}else{
						defaultIndexItemOrg=defaultIndexItemService.getByCodeOrg(defaultIndexItemCode, defaultAreaId);
						for (int i = 0; i < defaultIndexItemOrg.size(); i++) {
							id=defaultIndexItemOrg.get(0).getDefaultIndexItemId();
						}
					}
				}
			}
			
			//通过id去基本信息里面查询对应的企业名称
			String sql="SELECT INDEX_JBXX_QYMC FROM  INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID="+id+" AND SYS_AREA_ID="+areaId;
			Map<String, Object> queryMajorIdmap = new HashMap<>();
			queryMajorIdmap.put("queryTemporarySql", sql);
			PageSupport ps = PageSupport.initPageSupport(request);
			//查询符合条件的所有信息
			List<Map<String,Object>> queryMajorIddataValues=manualEntryService.temporaryTableList(ps,queryMajorIdmap);
			if(queryMajorIddataValues==null){
				//通过id去基本信息里面查询对应的企业名称
				String sql1="SELECT INDEX_JBXX_QYMC FROM  INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID="+id+" AND SYS_AREA_ID="+defaultAreaId;
				Map<String, Object> queryMajorIdmaps = new HashMap<>();
				queryMajorIdmaps.put("queryTemporarySql", sql1);
				queryMajorIddataValues=manualEntryService.temporaryTableList(ps,queryMajorIdmaps);
			}
			for (int i = 0; i < queryMajorIddataValues.size(); i++) {
				qymc=(String)queryMajorIddataValues.get(0).get("INDEX_JBXX_QYMC");
			}
			if(!qymc.equals(companyName)){
				request.setAttribute("msg", "无此企业名称，请重新输入");
				model.addAttribute("indexTbs", indexTbs);
//				model.addAttribute("sysOrgs", sysOrgs);
				model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
				return "orgObj/edit";
			}
			//通过指标大类indexName查询indexCode
			IndexTb indexTb=indexTbService.queryById(indexId);
			//查询表数据
			sql="select * from "+ indexTb.getIndexCode()+"_tb i,sys_org_tb s  where i.sys_org_id=s.sys_org_id and i.DEFAULT_INDEX_ITEM_ID ="+ id+"  and i.sys_org_id= "+ sysOrgId +" and i.RECORD_DATE = '"+ timeReport+" '";
			queryMajorIdmap.put("queryTemporarySql", sql);
			//查询符合条件的所有信息
			queryMajorIddataValues=manualEntryService.temporaryTableList(ps,queryMajorIdmap);
			if (queryMajorIddataValues == null || queryMajorIddataValues.size()==0) {
				request.setAttribute("msg",
						"未搜索到填充数据，在'" + indexTb.getIndexName() + "'指标大类中(归档时间等于"+timeReport+")尚无与该信用码/机构码相关的数据");
				model.addAttribute("indexTbs", indexTbs);
//				model.addAttribute("sysOrgs", sysOrgs);
				model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
				return "orgObj/edit";
			}
//			//判断是否已经处理此数据
//			Map<String, Object> mapOperate = new HashMap<String, Object>();
//			mapOperate.put("sysOrgid", sysOrgId);
//			mapOperate.put("indexId", indexId);
//			mapOperate.put("defaultId", id);
//			mapOperate.put("recoedDate", timeReport);
//			Integer count=orgObjService.querySysOperateCountByStatus(mapOperate);
//			if(count>0){
//				request.setAttribute("msg", "该异议已提交处理");
//				model.addAttribute("indexTbs", indexTbs);
//				model.addAttribute("sysOrgs", sysOrgs);
//				return "orgObj/edit";
//			}
			
			/**
			 * 对上传图片进行处理，将图片路径添加到数据库
			 */
//			// 文件名
//	        String originalFilename = file.getOriginalFilename();
//	        //新的名称加上文件的扩展名
//	        String newName = "/" + UUID.randomUUID() + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
//	        // 存储的物理路径
//	        String subDir = "/userUploadTextFile";
//	        String savePath = SettingUtils.getCommonSetting("upload.file.path") + subDir;
//	        File newFile = new File(savePath + newName);
//	        if (!newFile.exists()) {
//	            newFile.mkdirs();
//	        }
			String path=null;
			if(operateAuthFile.intValue()==1){
				path=UpLoadFile.upLoadFile(file);
			}
	        model.addAttribute("code", indexTb.getIndexCode().toUpperCase()+"_ID");
	        model.addAttribute("indexTableName", indexTb.getIndexCode().toUpperCase()+"_TB");
	        model.addAttribute("file", path);
	        model.addAttribute("indexId", indexTb.getIndexId());
	        model.addAttribute("indexName", indexTb.getIndexName());
	        model.addAttribute("defaultIndexItemCode", defaultIndexItemCode);
	        model.addAttribute("queryMajorIddataValues", queryMajorIddataValues);
		}
		model.addAttribute("indexTbs", indexTbs);
//		model.addAttribute("sysOrgs", sysOrgs);
		model.addAttribute("operateAuthFileSwitch", stm.getOperateAuthFileSwitch());
		return "orgObj/edit";
	}
	
	/**
	 * 金融机构异议处理详情页面
	 * @param model
	 * @param request
	 * @param indexId
	 * @param majorId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/show")
	public String update(Model model,HttpServletRequest request,@RequestParam(required=false) Integer indexId, 
			@RequestParam(required=false) Integer majorId,@RequestParam(required=false) String indexTableName,
			@RequestParam(required=false) Integer defaultId,@RequestParam(required=false) String file) throws Exception {
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//根据机构ID获取机构缓存
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg sorg = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(sorg==null){
			sorg = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, sorg);
		}
//		if(sorg!=null){
//			String key = RedisKeys.SYS_AREA_ALL ;
//			 Type type = new TypeToken<List<SysArea>>() {
//	            }.getType();
//			List<SysArea> aList=RedisUtil.getListData(key, type);
//			if(CollectionUtils.isEmpty(aList)){
//				aList=sysAreaService.queryAll2();
//				RedisUtil.setData(key, aList);
//			}
//			model.addAttribute("areaList", aList);
//		}
		if(sorg!=null){
			//查询四川省地区
			String key = RedisKeys.SYS_AREA + 1;
			SysArea area = RedisUtil.getObjData(key, SysArea.class);
			if(area==null){
				area = sysAreaService.queryAreaById(1);
				RedisUtil.setData(key, area);
			}
			model.addAttribute("areaList", area);
		}
		List<SysClassFyModel> industryList=sysClassFyService.queryAllSysClassFy1();
		List<SysOrg> sysGoverList=sysOrgService.queryOrgAll(null, 1);
		List<DicContent> dicContentList=dicContentService.queryAllContent();
		IndexTb indexTb = manualEntryService.queryIndexTbById(indexId);
		String indexCode = indexTb.getIndexCode();
		List<IndexItemTb> indexItemTbList = indexItemTbService.queryIndexItemTbsByIndexId(indexId);
		String sql = "select a.*,c.code_credit,c.code_org from " + indexCode + "_tb a,default_index_item_tb c where a.default_index_item_id=c.default_index_item_id and a." + indexCode + "_id=" + majorId;
		Map<String, Object> map = new HashMap<>();
		map.put("queryTemporarySql", sql);
		// 获取表数据集
		PageSupport ps = PageSupport.initPageSupport(request);
		List<Map<String, Object>> dataValues = manualEntryService.temporaryTableList(ps,map);
		Map<String, Object> mapSon = dataValues.get(0);
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
		for (int i = 0; i < indexItemTbList.size(); i++) {
			if(indexItemTbList.get(i).getIndexItemType()==3){
				Integer dicId=indexItemTbList.get(i).getDicId();
				Dic dic=dicService.getDicByDicId(dicId);
				if(dic!=null){
					if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
						SysClassFyModel industry;
						if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
							industry=sysClassFyService.queryModelByCode(valueList.get(i).toString());
						}else{
							industry=sysClassFyService.queryModelByCode(null);
						}
						if(industry!=null){
							valueList.set(i, industry.getSysIndustryName());
							indexItemTbList.get(i).setDicType(1);
						}else{
							if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}else{
								indexItemTbList.get(i).setDicType(4);
							}
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.ORGANIZATION) || ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
						SysOrg sysOrg;
						if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
							sysOrg=sysOrgService.querySysorgByFinancialCode(valueList.get(i).toString());
						}else{
							sysOrg=sysOrgService.querySysorgByFinancialCode(null);
						}
						if(sysOrg!=null){
							valueList.set(i, sysOrg.getSys_org_name());
							if(sysOrg.getSys_org_type()==0){
								indexItemTbList.get(i).setDicType(2);
							}else{
								indexItemTbList.get(i).setDicType(3);
							}
						}else{
							if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}else{
								indexItemTbList.get(i).setDicType(4);
							}
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.Area)){
						SysArea sysArea;
						if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
							sysArea=sysAreaService.queryAreaByCode(valueList.get(i).toString());
						}else{
							sysArea=sysAreaService.queryAreaByCode(null);
						}
						if(sysArea!=null ){
							valueList.set(i, sysArea.getSysAreaName());
							indexItemTbList.get(i).setDicType(5);
						}else{
							if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}else{
								indexItemTbList.get(i).setDicType(4);
							}
						}
					}else{
						if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
							DicContent dicContent=new DicContent();
							dicContent.setDicId(dic.getDicId());
							dicContent.setDicContentCode(valueList.get(i).toString());
							DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
							if(dics!=null){
								valueList.set(i, dics.getDicContentValue());
								indexItemTbList.get(i).setDicType(4);
							}
						}else{
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
		Date recordDate=(Date)mapSon.get("RECORD_DATE");
		Integer reportOrgId=Integer.parseInt(mapSon.get("SYS_ORG_ID").toString());
		AdminObjModel ao=new AdminObjModel();
		ao.setSysUserId(userDetails.getSys_user_id());
		ao.setSysOperateTime(new Date());
		ao.setSysOperateStatus(0);
		ao.setAuthFile(file);
		ao.setIndexItemId(indexId);
		ao.setRecordDate(recordDate);
		ao.setDefaultIndexItemId(defaultId);
		ao.setReportOrgId(reportOrgId);
		ao.setDataId(majorId);
		orgObjService.insertSysOperate(ao);
		Integer sysOperateId=0;
		sysOperateId=ao.getSysOperateId();
		model.addAttribute("sysOperateId", sysOperateId);
		model.addAttribute("majorId", majorId);
		model.addAttribute("indexId", indexId);
		model.addAttribute("defaultId", defaultId);
		model.addAttribute("file", file);
		model.addAttribute("indexName", indexTb.getIndexName());
		model.addAttribute("mapSon", mapSon);
		model.addAttribute("myValueList", valueList);
		model.addAttribute("myIndexItemTbList", indexItemTbList);
		model.addAttribute("dicContentList", dicContentList);
		model.addAttribute("industryList", industryList);
		model.addAttribute("sysGoverList", sysGoverList);
		return "orgObj/show";
	}
	
	/**
	 * 
	 * @param request
	 * @param model
	 * @param indexId  指标大类id
	 * @param check    选中指标项id
	 * @throws IOException 
	 */
	@RequestMapping(value="operate")
	@ResponseBody
	public Map<String,Object> operate(HttpServletRequest request,HttpServletResponse resp,Model model,@RequestParam(required = false)Integer indexId,
			@RequestParam(required = false)String check,@RequestParam(required = false)Integer defaultId,
			@RequestParam(required = false)String file,@RequestParam(required = false)Integer majorId,
			@RequestParam(required = false)Integer reportOrgId,@RequestParam(required = false)Integer sysOperateId) throws IOException{
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		IndexTb indexTb = manualEntryService.queryIndexTbById(indexId);
		List<IndexItemTb> indexItemTbList = indexItemTbService.queryIndexItemTbsByIndexId(indexTb.getIndexId());
		Map<String, Object> maps = new HashMap<>();
		if(!"".equals(check) && check!=null){
			check=check.substring(0, check.length()-1);
			String[] array=check.split(",");
			Map<String, Object> map = new HashMap<>();
			AdminObjModel ao=new AdminObjModel();
			ao.setSysUserId(userDetails.getSys_user_id());
			ao.setSysOrgId(userDetails.getSys_org_id());
			ao.setSysOperateTime(new Date());
			ao.setSysOperateStatus(0);
			ao.setSysOperateId(sysOperateId);
			orgObjService.updateSysOperate(ao);
			map.put("sysOperateId", sysOperateId);
			Integer[] sysOperateListId=new Integer[array.length];
			for (int j = 0; j < indexItemTbList.size(); j++) {
				for (int i = 0; i < array.length; i++) {
					if(array[i].equals(indexItemTbList.get(j).getIndexItemId().toString())){
						map.put("indexItemId", array[i]);
						Integer listId=orgObjService.querySysOperateListCount(map);
						if(listId==null){
							SysOperateListModel sl=new SysOperateListModel();
							sl.setSysOperateId(sysOperateId);
							sl.setSysOperateTime(new Date());
							sl.setIndexItemId(indexItemTbList.get(j).getIndexItemId());
							sl.setMark(1);
							orgObjService.insertSysOperateList(sl);
							sysOperateListId[i]=sl.getSysOperateListId();
						}else{
							SysOperateListModel sl=new SysOperateListModel();
							sl.setMark(1);
							sl.setSysOperateListId(listId);
							orgObjService.updateSysOperateListMark(sl);
							sysOperateListId[i]=listId;
						}
					}else{
						map.put("indexItemId", indexItemTbList.get(j).getIndexItemId());
						Integer listId=orgObjService.querySysOperateListCount(map);
						if(listId==null){
							SysOperateListModel sl=new SysOperateListModel();
							sl.setSysOperateId(sysOperateId);
							sl.setSysOperateTime(new Date());
							sl.setIndexItemId(indexItemTbList.get(j).getIndexItemId());
							sl.setMark(0);
							orgObjService.insertSysOperateList(sl);
						}
					}
				}
			}
//			for (int j = 0; j < indexItemTbList.size(); j++) {
//				map.put("indexItemId", indexItemTbList.get(j).getIndexItemId());
//				Integer listId=orgObjService.querySysOperateListCount(map);
//				if(listId==null){
//					SysOperateListModel sl=new SysOperateListModel();
//					sl.setSysOperateId(sysOperateId);
//					sl.setSysOperateTime(new Date());
//					sl.setIndexItemId(indexItemTbList.get(j).getIndexItemId());
//					sl.setMark(1);
//					orgObjService.insertSysOperateList(sl);
//				}
//			}
			String sysOperateList="";
			for (int i = 0; i < sysOperateListId.length; i++) {
				sysOperateList+=sysOperateListId[i]+",";
			}
			sysOperateList=sysOperateList.substring(0, sysOperateList.length()-1);
			maps.put("sysOperateList", sysOperateList);
			maps.put("msg", null);
			
			SysUserLog sysul=new SysUserLog();
			sysul.setSysUserLogMenuName("机构异议处理");
			sysul.setSysUserLogNewValue("待处理");
			sysul.setSysUserLogOperateType(3);
			sysul.setSysUserLogCount(1);
			sysul.setSysUserLogResult(true);
			sysul.setIndexId(indexTb.getIndexId());
			sysUserLogService.insertOneLog(sysul, request);
		}else{
			maps.put("msg", "msg");
		}
		return maps;
	}
	
	/**
	 * 金融机构异议处理详情页面
	 * @param model
	 * @param request
	 * @param indexId
	 * @param majorId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Map<String,Object> save(Model model,HttpServletRequest request,@RequestParam(required=false) Integer indexId, 
			@RequestParam(required=false) Integer majorId,@RequestParam(required=false) Integer defaultId,
			@RequestParam(required=false) String[] indexItemCode,@RequestParam(required=false) String[] indexItemValue,
			@RequestParam(required=false) String[] operateInformation,@RequestParam(required=false) String[] operateOrgdesc,
			@RequestParam(required=false) String[] operateService,@RequestParam(required=false) String sysOperateList,
			@RequestParam(required=false) Integer sysOperateId,@RequestParam(required=false) String file,
			@RequestParam(required=false) Integer shouAndrevise,@RequestParam(required=false) Integer sysOrgId,
			@RequestParam(required=false) Integer operateId,@RequestParam(required=false) String recordDate) throws Exception {
		Map<String, Object> map = new HashMap<>();
		IndexTb indexTb = manualEntryService.queryIndexTbById(indexId);
		List<IndexItemTb> indexItemTbList = indexItemTbService.queryIndexItemTbsByIndexId(indexTb.getIndexId());
		if(indexItemCode!=null){
			//判断指标项数据类型
			for (int j = 0; j < indexItemTbList.size(); j++) {
				for (int i = 0; i < indexItemCode.length; i++) {
					if (indexItemTbList.get(j).getIndexItemCode().equals(indexItemCode[i])) {
						if(indexItemTbList.get(j).getIndexItemEmpty()==0){
							if("".equals(indexItemValue[i])){
//								request.setAttribute("msg", indexItemTbList.get(j).getIndexItemName()+"不能为空");
//								if(shouAndrevise==1){
//									return "forward:show.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId;
//								}else{
//									return "forward:revise.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId+"&sysOrgId="+sysOrgId+"&operateId="+operateId+"&recordDate="+recordDate;
//								}
								map.put("msg", indexItemTbList.get(j).getIndexItemName()+"不能为空");
								return map;
							}
						}
						Pattern pattern = Pattern.compile("([-\\+]?[1-9]([0-9]*)(\\.[0-9]+)?)|(^0$)");
				        Matcher isNum = pattern.matcher(indexItemValue[i]);
						if(indexItemTbList.get(j).getIndexItemType()==2){
							if(!"".equals(indexItemValue[i]) && indexItemValue[i]!=null){
								if(!isNum.matches()){
//									request.setAttribute("msg", "请输入数字:"+indexItemTbList.get(j).getIndexItemName());
//									if(shouAndrevise==1){
//										return "forward:show.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId;
//									}else{
//										return "forward:revise.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId+"&sysOrgId="+sysOrgId+"&operateId="+operateId+"&recordDate="+recordDate;
//									}
									map.put("msg", "请输入数字:"+indexItemTbList.get(j).getIndexItemName());
									return map;
								}else{
									if(!"".equals(indexItemValue[i])){
										Double num=Double.parseDouble(indexItemValue[i]);
										indexItemValue[i]=num.toString();
									}
								}
							}
						}else if(indexItemTbList.get(j).getIndexItemType()==3){
							Integer dicId=indexItemTbList.get(j).getDicId();
							Dic dic=dicService.getDicByDicId(dicId);
							if(dic!=null){
								if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
									SysClassFyModel industry=sysClassFyService.queryByName(indexItemValue[i]);
									if(industry!=null){
										indexItemValue[i]=industry.getSysIndustryCode();
									}else{
										industry=sysClassFyService.queryModelByCode(indexItemValue[i]);
										if(industry!=null){
											indexItemValue[i]=industry.getSysIndustryCode();
										}else{
											DicContent dicContent=new DicContent();
											dicContent.setDicId(dic.getDicId());
											dicContent.setDicContentValue(indexItemValue[i]);
											DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
											if(dics!=null){
												indexItemValue[i]=dics.getDicContentCode();
											}else{
												DicContent dicContents=new DicContent();
												dicContents.setDicId(dic.getDicId());
												dicContents.setDicContentCode(indexItemValue[i]);
												dics=manualEntryService.queryDicContentByIdAndCode(dicContents);
												if(dics!=null){
													indexItemValue[i]=dics.getDicContentCode();
												}else{
//													request.setAttribute("msg", "无此数据字典类型:"+indexItemValue[i]);
//													if(shouAndrevise==1){
//														return "forward:show.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId;
//													}else{
//														return "forward:revise.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId+"&sysOrgId="+sysOrgId+"&operateId="+operateId+"&recordDate="+recordDate;
//													}
													map.put("msg", "无此数据字典类型:"+indexItemValue[i]);
													return map;
												}
											}
										}
									}
								}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.ORGANIZATION) || ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
									SysOrg sysOrg=sysOrgService.getSysOrgByName(indexItemValue[i]);
									if(sysOrg!=null){
										indexItemValue[i]=sysOrg.getSys_org_financial_code();
									}else{
										sysOrg=sysOrgService.querySysorgByFinancialCode(indexItemValue[i]);
										if(sysOrg!=null){
											indexItemValue[i]=sysOrg.getSys_org_financial_code();
										}else{
											DicContent dicContent=new DicContent();
											dicContent.setDicId(dic.getDicId());
											dicContent.setDicContentValue(indexItemValue[i]);
											DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
											if(dics!=null){
												indexItemValue[i]=dics.getDicContentCode();
											}else{
												DicContent dicContents=new DicContent();
												dicContents.setDicId(dic.getDicId());
												dicContents.setDicContentCode(indexItemValue[i]);
												dics=manualEntryService.queryDicContentByIdAndCode(dicContents);
												if(dics!=null){
													indexItemValue[i]=dics.getDicContentCode();
												}else{
//													request.setAttribute("msg", "无此数据字典类型:"+indexItemValue[i]);
//													if(shouAndrevise==1){
//														return "forward:show.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId;
//													}else{
//														return "forward:revise.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId+"&sysOrgId="+sysOrgId+"&operateId="+operateId+"&recordDate="+recordDate;
//													}
													map.put("msg", "无此数据字典类型:"+indexItemValue[i]);
													return map;
												}
											}
										}
									}
								}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.Area)){
									List<SysArea> sysArea=sysAreaService.queryAreaByNameAndCode(indexItemValue[i], null);
									if(!sysArea.isEmpty()&&sysArea.size()>0){
										indexItemValue[i]=sysArea.get(0).getSysAreaCode();
									}else{
										SysArea sysArea1=sysAreaService.queryAreaByCode(indexItemValue[i]);
										if(sysArea1!=null){
											indexItemValue[i]=sysArea1.getSysAreaCode();
										}else{
											DicContent dicContent=new DicContent();
											dicContent.setDicId(dic.getDicId());
											dicContent.setDicContentValue(indexItemValue[i]);
											DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
											if(dics!=null){
												indexItemValue[i]=dics.getDicContentCode();
											}else{
												DicContent dicContents=new DicContent();
												dicContents.setDicId(dic.getDicId());
												dicContents.setDicContentCode(indexItemValue[i]);
												dics=manualEntryService.queryDicContentByIdAndCode(dicContents);
												if(dics!=null){
													indexItemValue[i]=dics.getDicContentCode();
												}else{
//													request.setAttribute("msg", "无此数据字典类型:"+indexItemValue[i]);
//													if(shouAndrevise==1){
//														return "forward:show.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId;
//													}else{
//														return "forward:revise.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId+"&sysOrgId="+sysOrgId+"&operateId="+operateId+"&recordDate="+recordDate;
//													}
													map.put("msg", "无此数据字典类型:"+indexItemValue[i]);
													return map;
												}
											}
										}
									}
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentValue(indexItemValue[i]);
									DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										indexItemValue[i]=dics.getDicContentCode();
									}else{
										DicContent dicContents=new DicContent();
										dicContents.setDicId(dic.getDicId());
										dicContents.setDicContentCode(indexItemValue[i]);
										dics=manualEntryService.queryDicContentByIdAndCode(dicContents);
										if(dics!=null){
											indexItemValue[i]=dics.getDicContentCode();
										}else{
//											request.setAttribute("msg", "无此数据字典类型:"+indexItemValue[i]);
//											if(shouAndrevise==1){
//												return "forward:show.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId;
//											}else{
//												return "forward:revise.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId+"&sysOrgId="+sysOrgId+"&operateId="+operateId+"&recordDate="+recordDate;
//											}
											map.put("msg", "无此数据字典类型:"+indexItemValue[i]);
											return map;
										}
									}
								}
							}else{
//								request.setAttribute("msg", "无此数据字典:"+indexItemTbList.get(j).getIndexItemName());
//								if(shouAndrevise==1){
//									return "forward:show.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId;
//								}else{
//									return "forward:revise.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId+"&sysOrgId="+sysOrgId+"&operateId="+operateId+"&recordDate="+recordDate;
//								}
								map.put("msg", "无此数据字典:"+indexItemTbList.get(j).getIndexItemName());
								return map;
							}
						}
					}
				}
			}
			
			String indexCode = indexTb.getIndexCode();
			String sqllist = "select * from " + indexCode + "_tb where " + indexCode + "_id=" + majorId;
			Map<String, Object> maps = new HashMap<>();
			maps.put("queryTemporarySql", sqllist);
			// 获取表数据集
			PageSupport ps = PageSupport.initPageSupport(request);
			List<Map<String, Object>> dataValues = manualEntryService.temporaryTableList(ps,maps);
			Map<String, Object> mapSon = dataValues.get(0);
			String dates=mapSon.get("RECORD_DATE").toString();
			for (int k = 0; k < indexItemCode.length; k++) {
				if(indexItemTbList.get(k).getIndexItemImportUnique()==1){
					String sqlCount = "select * from " + indexCode + "_tb where DEFAULT_INDEX_ITEM_ID=" + defaultId+" and RECORD_DATE='"+dates+"' and "+indexItemCode[k]+"='"+indexItemValue[k]+"' and "+indexCode + "_id!=" + majorId;
					Map<String, Object> mapCount = new HashMap<>();
					mapCount.put("queryTemporarySql", sqlCount);
					// 获取表数据集
					List<Map<String, Object>> dataValuesCount = manualEntryService.temporaryTableList(null,mapCount);
					if(dataValuesCount.size()>0){
//						request.setAttribute("msg", "已存在不能修改:"+indexItemTbList.get(k).getIndexItemName());
//						if(shouAndrevise==1){
//							return "forward:show.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId;
//						}else{
//							return "forward:revise.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId+"&sysOrgId="+sysOrgId+"&operateId="+operateId+"&recordDate="+recordDate;
//						}
						map.put("msg", "已存在不能修改:"+indexItemTbList.get(k).getIndexItemName());
						return map;
					}
				}
			}
			
			orgObjService.updateIndexTbSql(indexId, majorId, indexItemCode, indexItemValue, operateInformation, operateOrgdesc, operateService, sysOperateList, sysOperateId ,file,defaultId, request);		
		}else{
//			request.setAttribute("msg", "请选择指标项");
//			if(shouAndrevise==1){
//				return "forward:show.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId;
//			}else{
//				return "forward:revise.jhtml?majorId="+majorId+"&indexId="+indexId+"&file="+file+"&defaultId="+defaultId+"&sysOrgId="+sysOrgId+"&operateId="+operateId+"&recordDate="+recordDate;
//			}
			map.put("msg", "请选择指标项");
			return map;
		}
//		request.setAttribute("msg", "操作成功");
//		return "forward:list.jhtml";
		map.put("msg", "操作成功");
		return map;
	}
	
	/**
	 * 通过列表页面点击进入异议处理详情
	 * @param model
	 * @param request
	 * @param indexId
	 * @param sysOrgId
	 * @param operateTime
	 * @param defaultId
	 * @param recordDate
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="revise")
	public String revise(Model model,HttpServletRequest request,@RequestParam(required=false)Integer indexId,
			@RequestParam(required=false)Integer sysOrgId,@RequestParam(required=false)Integer operateId,
			@RequestParam(required=false)Integer defaultId,@RequestParam(required=false)String recordDate,
			@RequestParam(required=false)Integer majorId) throws Exception{
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		//根据机构ID获取机构缓存
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg sorg = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(sorg==null){
			sorg = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, sorg);
		}
//		if(sorg!=null){
//			String key = RedisKeys.SYS_AREA_ALL ;
//			 Type type = new TypeToken<List<SysArea>>() {
//	            }.getType();
//			List<SysArea> aList=RedisUtil.getListData(key, type);
//			if(CollectionUtils.isEmpty(aList)){
//				aList=sysAreaService.queryAll2();
//				RedisUtil.setData(key, aList);
//			}
//			model.addAttribute("areaList", aList);
//		}
		if(sorg!=null){
			//查询四川省地区
			String key = RedisKeys.SYS_AREA + 1;
			SysArea area = RedisUtil.getObjData(key, SysArea.class);
			if(area==null){
				area = sysAreaService.queryAreaById(1);
				RedisUtil.setData(key, area);
			}
			model.addAttribute("areaList", area);
		}
		List<SysClassFyModel> industryList=sysClassFyService.queryAllSysClassFy1();
		List<SysOrg> sysGoverList=sysOrgService.queryOrgAll(null, 1);
		List<DicContent> dicContentList=dicContentService.queryAllContent();
		IndexTb indexTb = manualEntryService.queryIndexTbById(indexId);
		String indexCode = indexTb.getIndexCode();
		List<IndexItemTb> indexItemTbList = indexItemTbService.queryIndexItemTbsByIndexId(indexId);
		String sql = "select a.*,c.code_credit,c.code_org from " + indexCode 
				+ "_tb a,default_index_item_tb c where a.default_index_item_id=c.default_index_item_id and a."+indexCode+"_id="+majorId;
		Map<String, Object> map = new HashMap<>();
		map.put("queryTemporarySql", sql);
		// 获取表数据集
		PageSupport ps = PageSupport.initPageSupport(request);
		List<Map<String, Object>> dataValues = manualEntryService.temporaryTableList(ps,map);
		Map<String, Object> mapSon = dataValues.get(0);
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
		for (int i = 0; i < indexItemTbList.size(); i++) {
			if(indexItemTbList.get(i).getIndexItemType()==3){
				Integer dicId=indexItemTbList.get(i).getDicId();
				Dic dic=dicService.getDicByDicId(dicId);
				if(dic!=null){
					if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
						SysClassFyModel industry;
						if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
							industry=sysClassFyService.queryModelByCode(valueList.get(i).toString());
						}else{
							industry=sysClassFyService.queryModelByCode(null);
						}
						if(industry!=null){
							valueList.set(i, industry.getSysIndustryName());
							indexItemTbList.get(i).setDicType(1);
						}else{
							if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}else{
								indexItemTbList.get(i).setDicType(4);
							}
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.ORGANIZATION) || ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
						SysOrg sysOrg;
						if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
							sysOrg=sysOrgService.querySysorgByFinancialCode(valueList.get(i).toString());
						}else{
							sysOrg=sysOrgService.querySysorgByFinancialCode(null);
						}
						if(sysOrg!=null){
							valueList.set(i, sysOrg.getSys_org_name());
							if(sysOrg.getSys_org_type()==0){
								indexItemTbList.get(i).setDicType(2);
							}else{
								indexItemTbList.get(i).setDicType(3);
							}
						}else{
							if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}else{
								indexItemTbList.get(i).setDicType(4);
							}
						}
					}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.Area)){
						SysArea sysArea;
						if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
							sysArea=sysAreaService.queryAreaByCode(valueList.get(i).toString());
						}else{
							sysArea=sysAreaService.queryAreaByCode(null);
						}
						if(sysArea!=null ){
							valueList.set(i, sysArea.getSysAreaName());
							indexItemTbList.get(i).setDicType(5);
						}else{
							if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(i).toString());
								DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(i, dics.getDicContentValue());
									indexItemTbList.get(i).setDicType(4);
								}
							}else{
								indexItemTbList.get(i).setDicType(4);
							}
						}
					}else{
						if(valueList.get(i)!=null && !"".equals(valueList.get(i))){
							DicContent dicContent=new DicContent();
							dicContent.setDicId(dic.getDicId());
							dicContent.setDicContentCode(valueList.get(i).toString());
							DicContent dics=manualEntryService.queryDicContentByIdAndCode(dicContent);
							if(dics!=null){
								valueList.set(i, dics.getDicContentValue());
								indexItemTbList.get(i).setDicType(4);
							}
						}else{
							indexItemTbList.get(i).setDicType(4);
						}
					}
				}
			}
		}
		AdminObjModel aom=orgObjService.querySysOperateById(operateId);
		List<SysOperateListModel> operateList=orgObjService.querySysOperateListByOperateId(operateId);
		String sysOperateList="";
		if(operateList.size()>0){
			for (int i = 0; i < operateList.size(); i++) {
				if(operateList.get(i).getMark()==1){
					sysOperateList+=operateList.get(i).getSysOperateListId()+",";
				}
			}
			sysOperateList=sysOperateList.substring(0, sysOperateList.length()-1);
		}
		for (int j = 0; j < valueList.size(); j++) {
			if("null".equals(valueList.get(j))){
				valueList.set(j, null);
			}
		}
		model.addAttribute("majorId", majorId);
		model.addAttribute("indexId", indexId);
		model.addAttribute("sysOrgId", sysOrgId);
		model.addAttribute("operateId", operateId);
		model.addAttribute("recordDate", recordDate);
		model.addAttribute("file", aom.getAuthFile());
		model.addAttribute("indexName", indexTb.getIndexName());
		model.addAttribute("mapSon", mapSon);
		model.addAttribute("myValueList", valueList);
		model.addAttribute("myIndexItemTbList", indexItemTbList);
		model.addAttribute("myoperateList", operateList);
		model.addAttribute("sysOperateId", operateId);
		model.addAttribute("sysOperateList", sysOperateList);
		model.addAttribute("industryList", industryList);
		model.addAttribute("sysGoverList", sysGoverList);
		model.addAttribute("dicContentList", dicContentList);
		model.addAttribute("defaultId", defaultId);
		return "orgObj/report";
	}
}
