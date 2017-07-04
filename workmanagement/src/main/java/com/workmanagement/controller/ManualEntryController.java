package com.workmanagement.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.workmanagement.model.*;
import com.workmanagement.service.*;
import com.workmanagement.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 手工修改
 * @author tianhao
 *
 */
@Controller
@RequestMapping(value="/admin/manualEntry")
public class ManualEntryController {

    /**
     * 常量：行业
     */
    public static final String INDUSTRY = "行业";
    /**
     * 常量：机构
     */
    public static final String ORGANIZATION = "机构";
    /**
     * 常量：政府
     */
    public static final String GOVERNMENT = "政府";
    /**
     * 常量：地区
     */
    public static final String Area = "地区";
    @Autowired
	private ManualEntryService manualEntryService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private DicService dicService;
	@Autowired
	private SysOtherManageService sysOtherManageService;
	@Autowired
	private SysClassFyService sysClassFyService;
	@Autowired
	private SysGoverService sysGoverService;
	@Autowired
	private DicContentService dicContentService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private IndexItemTbService indexItemTbService;
	@Autowired
	private SysOrgTypeService sysOrgTypeService;

    /**
     * 判断s1是否包含s2
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean isContain(String s1, String s2) {
        return s1.contains(s2);
    }
	
	/**
	 * 手工修改记录列表页面
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(HttpServletRequest request, Model model) throws Exception{
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
		StringBuffer orgSb = new StringBuffer();
		DataUtil.getChildOrgIds(so, orgSb);
		map.put("orgIds", orgSb.toString().split(","));
		PageSupport ps = PageSupport.initPageSupport(request);
		List<SysUserLog> sysUserLog = manualEntryService.querySysUserLogList(map, ps);
		model.addAttribute("sysUserLog", sysUserLog);
		return "manualEntry/list";
	}
	
	/**
	 * 手工修改检索、列表页面
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/add")
	public String reportList(HttpServletRequest request, Model model,
			@RequestParam(required=false) String begin,@RequestParam(required=false) String end,
			@RequestParam(required=false) Integer indexId,@RequestParam(required=false) String code,
			@RequestParam(required=false) Integer sysOrgId,
			@RequestParam(required=false) Integer addOrlist) throws Exception{

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
		map.put("areaId", areaIds);
		StringBuffer orgSb = new StringBuffer();
		DataUtil.getChildAreaIds(sa, orgSb);
		String[] orgIds=orgSb.toString().split(",");
		StringBuffer orgSbs = new StringBuffer();
		DataUtil.getChildOrgIds(so, orgSbs);
		String[] sysOrgIds=orgSbs.toString().split(",");
		String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
		if("1".equals(role)){
			map.put("orgType", 3);
		}else if("8".equals(role)){
			map.put("orgType", 2);
			map.put("orgIds", so.getSys_org_id());
		}else if("4".equals(role) || "5".equals(role) ||"6".equals(role) || "9".equals(role) || "10".equals(role)){
			map.put("orgType", 4);
			map.put("sysOrgIds", sysOrgIds);
		}else{
			map.put("orgType", 1);
			map.put("orgIds", orgIds);
		}
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
		if(addOrlist!=null && addOrlist==1){
			Pattern pattern = Pattern.compile(".*[\\u4e00-\\u9faf].*");
	        Matcher isNum = pattern.matcher(code);
	        if(isNum.matches()){
	        	request.setAttribute("msg", "统一社会信用码/组织机构代码不能为汉字");
	        	model.addAttribute("indexTbs", indexTbs);
//	    		model.addAttribute("sysOrgs", sysOrgs);
				return "manualEntry/add";
	        }
	        if(code.length()>18){
	        	request.setAttribute("msg", "统一社会信用码/组织机构代码长度不能大于18位");
	        	model.addAttribute("indexTbs", indexTbs);
//	    		model.addAttribute("sysOrgs", sysOrgs);
				return "manualEntry/add";
	        }
	        SysOrg sysorg=sysOrgService.queryInstitutionsByIdWithNoStatus(sysOrgId);
	        SysArea area=sysAreaService.getUpOrThisSysArea(sysorg.getSys_area_id());
			DefaultIndexItem defaultIndexItem = manualEntryService.getDefaultIndexItemByCode(code,area.getSysAreaId());
			if (defaultIndexItem == null) {
				defaultIndexItem = manualEntryService.getDefaultIndexItemByCode(code,sysorg.getSys_area_id());
				if(defaultIndexItem == null){
					request.setAttribute("msg", "该信用码/机构码不存在,请重新输入");
					model.addAttribute("indexTbs", indexTbs);
//					model.addAttribute("sysOrgs", sysOrgs);
					return "manualEntry/add";
				}
			}
			IndexTb indexTb=manualEntryService.queryIndexTbById(indexId);
			String sql = "select a.*,b.sys_org_name from " + indexTb.getIndexCode()
                    + "_tb a,sys_org_tb b where a.sys_org_id=b.sys_org_id and a.record_date >='" + begin
                    + "' and a.record_date <='" + end
					+ "' and a.default_index_item_id=" + defaultIndexItem.getDefaultIndexItemId()
					+ " and a.sys_org_id="+sysOrgId;
//			String sql="select * from " + indexTb.getIndexCode() + "_tb";
			Map<String, Object> queryMajorIdmap = new HashMap<>();
			queryMajorIdmap.put("queryTemporarySql", sql);
			PageSupport ps = PageSupport.initPageSupport(request);
			List<Map<String,Object>> queryMajorIddataValues=manualEntryService.temporaryTableList(ps,queryMajorIdmap);
			if (queryMajorIddataValues == null || queryMajorIddataValues.size()==0) {
				request.setAttribute("msg",
						"未搜索到填充数据，在'" + indexTb.getIndexName() + "'指标大类中(归档时间大于等于"+begin+"并且"+"小于等于" + end + ")尚无与该信用码/机构码相关的数据");
				model.addAttribute("indexTbs", indexTbs);
//				model.addAttribute("sysOrgs", sysOrgs);
				return "manualEntry/add";
			}
			model.addAttribute("queryMajorIddataValues", queryMajorIddataValues);
			model.addAttribute("indexTb", indexTb);
			model.addAttribute("code", indexTb.getIndexCode().toUpperCase()+"_ID");
			model.addAttribute("defaultIndexItemId",defaultIndexItem.getDefaultIndexItemId());
		}
		model.addAttribute("indexTbs", indexTbs);
//		model.addAttribute("sysOrgs", sysOrgs);
		return "manualEntry/add";
	}
	
	/**
	 * 查询机构列表
     * @param model
     * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/getOrgList")
	@ResponseBody
	public void getOrgByName(HttpServletResponse response){
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
		String areaKey=RedisKeys.SYS_AREA+so.getSys_area_id();
		SysArea sa = RedisUtil.getObjData(areaKey, SysArea.class);
		if(sa==null){
			sa = sysAreaService.queryAreaById(so.getSys_area_id());
			RedisUtil.setData(areaKey, sa);
		}
		// 查询管辖地区及子地区的ID
		List<Integer> idsub=sysAreaService.getAllSubAreaIds(sa.getSysAreaId());
		map.put("areaId", idsub);
//		StringBuffer orgSb = new StringBuffer();
//		DataUtil.getChildAreaIds(sa, orgSb);
//		String[] orgIds=orgSb.toString().split(",");
		StringBuffer orgSbs = new StringBuffer();
		DataUtil.getChildOrgIds(so, orgSbs);
		String[] sysOrgIds=orgSbs.toString().split(",");
		String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
		if("1".equals(role)){
			if(userDetails.getSys_role_id()==1){
				map = new HashMap<String, Object>();
			}else{
				map.put("orgType", 3);
			}
		}else if("8".equals(role)){
			map.put("orgType", 2);
			map.put("orgIds", so.getSys_org_id());
		}else if("4".equals(role) || "5".equals(role) ||"6".equals(role) || "9".equals(role) || "10".equals(role)){
			map.put("orgType", 4);
			map.put("sysOrgIds", sysOrgIds);
		}else{
			map.put("orgType", 1);
			map.put("orgIds", idsub);
		}

		String orgListKey = RedisKeys.SYS_ORG_LIST_USER + userDetails.getSys_user_id();
		Type type = new TypeToken<List<SysOrg>>(){}.getType();
		List<SysOrg> sysOrgs = RedisUtil.getListData(orgListKey, type);

		Set<Integer> set=new HashSet<>();
		for (int i = 0; i < sysOrgs.size(); i++) {
			set.add(sysOrgs.get(i).getSys_org_type_id());
		}
		Map<String, Object> orgTypeMap = new HashMap<String, Object>();
		orgTypeMap.put("typeIds", set);
		List<SysOrgType> sysOrgType=sysOrgTypeService.queryTypeList(orgTypeMap);
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < sysOrgType.size(); i++) {
			DataUtil.getParentOrgTypeIds(sysOrgType.get(i), sb, sysOrgTypeService);
		}
		orgTypeMap.put("typeIds", sb.toString().split(","));
		sysOrgType=sysOrgTypeService.queryTypeList(orgTypeMap);
        List<ZtreeVo> ztreeVo = DataUtil.getZtree(sysOrgs, sysOrgType);
        Gson gson = new Gson();
        try {
			response.getWriter().write(gson.toJson(ztreeVo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询机构列表
     * @param model
     * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/getOrgLists")
    @ResponseBody
    public void getOrgByNames(HttpServletResponse response) {
        // 登录用户session
//        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
//                .getPrincipal();
		List<SysOrg> sysOrgs=sysOrgService.queryOrgAll(null, 0);
		Set<Integer> set=new HashSet<>();
		for (int i = 0; i < sysOrgs.size(); i++) {
			set.add(sysOrgs.get(i).getSys_org_type_id());
		}
		Map<String, Object> orgTypeMap = new HashMap<String, Object>();
		orgTypeMap.put("typeIds", set);
		List<SysOrgType> sysOrgType=sysOrgTypeService.queryTypeList(orgTypeMap);
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < sysOrgType.size(); i++) {
			DataUtil.getParentOrgTypeIds(sysOrgType.get(i), sb, sysOrgTypeService);
		}
		orgTypeMap.put("typeIds", sb.toString().split(","));
		sysOrgType=sysOrgTypeService.queryTypeList(orgTypeMap);
        List<ZtreeVo> ztreeVo = DataUtil.getZtree(sysOrgs, sysOrgType);
        Gson gson = new Gson();
        try {
			response.getWriter().write(gson.toJson(ztreeVo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 手工修改历史列表页面
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/changeHistory")
	public String changeHistory(HttpServletRequest request, Model model,
			@RequestParam(required=false) Integer indexId) throws Exception{
		PageSupport ps = PageSupport.initPageSupport(request);
		List<SysUserLog> querySysUserLog=manualEntryService.querySysUserLog(indexId,ps);
		model.addAttribute("querySysUserLog", querySysUserLog);
		return "manualEntry/changeHistory";
	}
	
	/**
	 * 查看手工修改
	 * @param model
	 * @param reportIndexId
	 * @return
	 */
	@RequestMapping("/show")
	public String show(Model model, Integer sysUserLogId) {
		SysUserLog sysUserLog = manualEntryService.getSysUserLogById(sysUserLogId);
		IndexTb indexTb = manualEntryService.queryIndexTbById(sysUserLog.getIndexId());
		model.addAttribute("sysUserLog", sysUserLog);
		model.addAttribute("indexTb", indexTb);
		return "manualEntry/show";
	}
	
	/**
	 * 企业 在 该指标大类下 该批次 数据的有效数据
	 * @param request
	 * @param defaultIndexItemId
	 * @param indexId
	 * @param reportId
	 * @return
	 */
	@RequestMapping("/defaultData")
	public String defaultData(HttpServletRequest request,Model model, @RequestParam(required=false) Integer id,
			@RequestParam(required=false) Integer indexId) {
		IndexTb indexTb = manualEntryService.queryIndexTbById(indexId);
		Map<String, Object> map = new HashMap<>();
		String queryTemporarySql = "select a.*,b.sys_area_name,c.code_credit,c.code_org from " + indexTb.getIndexCode() + "_tb a,sys_area_tb b,default_index_item_tb c where a.sys_area_id=b.sys_area_id and a.default_index_item_id=c.default_index_item_id and a."+ indexTb.getIndexCode() + "_id="+id;
		map.put("queryTemporarySql", queryTemporarySql);
		// 获取表数据集
		PageSupport ps = PageSupport.initPageSupport(request);
		List<Map<String, Object>> dataValues = manualEntryService.temporaryTableList(ps,map);

		model.addAttribute("dataValues", dataValues);
		model.addAttribute("indexTb", indexTb);
		model.addAttribute("code", indexTb.getIndexCode().toUpperCase()+"_ID");
		return "manualEntry/defaultData";
	}

	/**
     * 模糊查询
     * @param model
     * @param
     * @return
     * @throws IOException
	 */
	@RequestMapping("/indexTbByName")
	@ResponseBody
	public List<IndexTb> indexTbByName(HttpServletRequest request,@RequestParam(required=false) String name) throws Exception{
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取管辖区域
		String areaKey=RedisKeys.SYS_AREA+so.getSys_area_id();
		SysArea sa = RedisUtil.getObjData(areaKey, SysArea.class);
		// 查询管辖地区及子地区的ID
		List<Integer> areaIds = new ArrayList<>();
		List<Integer> idup=sysAreaService.getAllUpAreaIds(sa.getSysAreaId());
		List<Integer> idsub=sysAreaService.getAllSubAreaIds(sa.getSysAreaId());
		areaIds.addAll(idup);
		areaIds.addAll(idsub);
		Set<Integer> setAreaIds=new HashSet<>();
		setAreaIds.addAll(areaIds);
		map.put("areaId", setAreaIds);
		if("".equals(name)){
			map.put("name", null);
		}else{
			map.put("name", name);
		}
		List<IndexTb> indexTbs=manualEntryService.queryIndexTbAll(map);
		return indexTbs;
	}

	/**
     * 模糊查询
     * @param model
     * @param
     * @return
     * @throws IOException
	 */
	@RequestMapping("/sysOrgByName")
	@ResponseBody
	public List<SysOrg> sysOrgByName(HttpServletRequest request,@RequestParam(required=false) String name) throws Exception{
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取管辖区域
		String areaKey=RedisKeys.SYS_AREA+so.getSys_area_id();
		SysArea sa = RedisUtil.getObjData(areaKey, SysArea.class);
		// 查询管辖地区及子地区的ID
		StringBuffer sb = new StringBuffer();
		DataUtil.getParentAreaIds(sa, sb, sysAreaService);// 获取改地区及父地区的ID
		DataUtil.getChildAreaIds(sa, sb);// 获取改地区及子地区的ID
		String[] ids = sb.toString().split(",");
		map.put("areaId", ids);
		// 查询管辖地区及子地区的ID
		StringBuffer orgSb = new StringBuffer();
		DataUtil.getChildAreaIds(sa, orgSb);
		String[] orgIds=orgSb.toString().split(",");
		map.put("name", name);
		String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
		if("1".equals(role)){
			map.put("orgType", 3);
		}else if("8".equals(role)){
			map.put("orgType", 2);
			map.put("orgIds", so.getSys_org_id());
		}else{
			map.put("orgType", 1);
			map.put("orgIds", orgIds);
		}
		List<SysOrg> sysOrgs=manualEntryService.querySysOrgAll(map);
		return sysOrgs;
	}
	
	/**
	 * 修改
	 * @param model
	 * @param request
	 * @param indexId
	 * @param majorId
     * @return
     * @throws Exception
	 */
	@RequestMapping("/update")
    public String update(Model model, HttpServletRequest request, @RequestParam(required = false) Integer indexId,
                         @RequestParam(required = false) Integer majorId) throws Exception {
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
		List<DicContent> dicContentList=dicContentService.queryAllContent();
		List<SysClassFyModel> industryList=sysClassFyService.queryAllSysClassFy1();
		List<SysOrg> sysGoverList=sysOrgService.queryOrgAll(null, 1);
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
					if(isContain(dic.getDicName(),INDUSTRY)){
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
					}else if(isContain(dic.getDicName(),ORGANIZATION) || isContain(dic.getDicName(),GOVERNMENT)){
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
					}else if(isContain(dic.getDicName(),Area)){
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
		SysOtherManage stm=sysOtherManageService.querySysOtherManage(userDetails.getSys_user_id());
		model.addAttribute("majorId", majorId);
		model.addAttribute("indexId", indexId);
		model.addAttribute("indexName", indexTb.getIndexName());
		model.addAttribute("mapSon", mapSon);
		model.addAttribute("myValueList", valueList);
		model.addAttribute("myIndexItemTbList", indexItemTbList);
		model.addAttribute("authFileSwitch", stm.getAuthFileSwitch());
		model.addAttribute("industryList", industryList);
		model.addAttribute("sysGoverList", sysGoverList);
		model.addAttribute("dicContentList", dicContentList);
		return "manualEntry/update";
	}
	
	/**
	 * 下载导入文件
	 * @param request
	 * @param response
	 * @param fileName
	 * @param name
	 */
	@RequestMapping(value = "/downLoadFile")
	public void downLoadFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=false) String fileName,
			@RequestParam(required=false) String name) {

		DownLoadFile.downLoadFile(fileName, name, request, response);
	}
	
	/**
	 * 保存修改
	 * @param file
	 * @param indexId
	 * @param indexItemCode
	 * @param majorId
	 * @param request
     * @return
     * @throws Exception
	 */
	@RequestMapping("/save")
	public String save(MultipartFile file, @RequestParam Integer indexId,
			@RequestParam(required = false) String[] indexItemCode,
			@RequestParam(required = false) String[] indexItemValue,
			@RequestParam(required = false) Integer majorId,
			@RequestParam(required = false) Integer YesNo,
			@RequestParam(required = false) Integer defaultId,
			@RequestParam(required = false) String codeCredit,
			@RequestParam(required = false) String codeOrg,
			HttpServletRequest request) throws Exception {
		if(YesNo==0){
			return "forward:defaultData.jhtml?id="+majorId+"&indexId="+indexId;
		}
		IndexTb indexTb = manualEntryService.queryIndexTbById(indexId);
		String indexCode = indexTb.getIndexCode();
		String sqllist = "select * from " + indexCode + "_tb where " + indexCode + "_id=" + majorId;
		Map<String, Object> maps = new HashMap<>();
		maps.put("queryTemporarySql", sqllist);
		// 获取表数据集
		PageSupport ps = PageSupport.initPageSupport(request);
		List<Map<String, Object>> dataValues = manualEntryService.temporaryTableList(ps,maps);
		Map<String, Object> mapSon = dataValues.get(0);
		Integer areaId=Integer.parseInt(mapSon.get("SYS_AREA_ID").toString());
		Map<String, Object> map = new HashMap<>();
		map.put("defaultId", defaultId);
		map.put("codeCredit", codeCredit);
		map.put("codeOrg", codeOrg);
		map.put("areaId", areaId);
		if(!"".equals(codeCredit)){
			Integer count=manualEntryService.queryDefaultIndexItemCountByCodeCredit(map);
			if(count>0){
				request.setAttribute("msg", "社会统一信用代码重复:"+codeCredit);
				return "forward:update.jhtml?majorId="+majorId+"&indexId="+indexId;
			}
		}
		if(!"".equals(codeOrg)){
			Integer count=manualEntryService.queryDefaultIndexItemCountByCodeOrg(map);
			if(count>0){
				request.setAttribute("msg", "组织机构代码重复:"+codeOrg);
				return "forward:update.jhtml?majorId="+majorId+"&indexId="+indexId;
			}
		}


		String path=null;
		if(file!=null){
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
//	        file.transferTo(newFile);
//			path=savePath + newName;
			path=UpLoadFile.upLoadFile(file);
		}

		List<IndexItemTb> indexItemTbList = indexItemTbService.queryIndexItemTbsByIndexId(indexId);
		//判断指标项数据类型
		for (int j = 0; j < indexItemTbList.size(); j++) {
			for (int i = 0; i < indexItemCode.length; i++) {
				if (indexItemTbList.get(j).getIndexItemCode().equals(indexItemCode[i])) {
					if(indexItemTbList.get(j).getIndexItemEmpty()==0){
						if("".equals(indexItemValue[i])){
							request.setAttribute("msg", indexItemTbList.get(j).getIndexItemName()+"不能为空");
							return "forward:update.jhtml?majorId="+majorId+"&indexId="+indexId;
						}
					}
					Pattern pattern = Pattern.compile("([-\\+]?[1-9]([0-9]*)(\\.[0-9]+)?)|(^0$)");
			        Matcher isNum = pattern.matcher(indexItemValue[i]);
					if(indexItemTbList.get(j).getIndexItemType()==2){
						if(!"".equals(indexItemValue[i]) && indexItemValue[i]!=null){
							if(!isNum.matches()){
								request.setAttribute("msg", "请输入数字:"+indexItemTbList.get(j).getIndexItemName());
								return "forward:update.jhtml?majorId="+majorId+"&indexId="+indexId;
							}else{
								if(!"".equals(indexItemValue[i])){
									Double num=Double.parseDouble(indexItemValue[i]);
									indexItemValue[i]=num.toString();
								}
							}
						}
					}else if(indexItemTbList.get(j).getIndexItemType()==3){
						Integer dicId=indexItemTbList.get(i).getDicId();
						Dic dic=dicService.getDicByDicId(dicId);
						if(dic!=null){
							if(isContain(dic.getDicName(),INDUSTRY)){
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
												request.setAttribute("msg", "无此数据字典类型:"+indexItemValue[i]);
												return "forward:update.jhtml?majorId="+majorId+"&indexId="+indexId;
											}
										}
									}
								}
							}else if(isContain(dic.getDicName(),ORGANIZATION) || isContain(dic.getDicName(),GOVERNMENT)){
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
												request.setAttribute("msg", "无此数据字典类型:"+indexItemValue[i]);
												return "forward:update.jhtml?majorId="+majorId+"&indexId="+indexId;
											}
										}
									}
								}
							}else if(isContain(dic.getDicName(),Area)){
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
												request.setAttribute("msg", "无此数据字典类型:"+indexItemValue[i]);
												return "forward:update.jhtml?majorId="+majorId+"&indexId="+indexId;
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
										request.setAttribute("msg", "无此数据字典类型:"+indexItemValue[i]);
										return "forward:update.jhtml?majorId="+majorId+"&indexId="+indexId;
									}
								}
							}
						}else{
							request.setAttribute("msg", "无此数据字典:"+indexItemTbList.get(j).getIndexItemName());
							return "forward:update.jhtml?majorId="+majorId+"&indexId="+indexId;
                        }
					}
				}
			}
		}
		String recordDate=mapSon.get("RECORD_DATE").toString();
		for (int k = 0; k < indexItemTbList.size(); k++) {
			if(indexItemTbList.get(k).getIndexItemImportUnique()==1){
				String sqlCount = "select * from " + indexCode + "_tb where DEFAULT_INDEX_ITEM_ID=" + defaultId+" and RECORD_DATE='"+recordDate+"' and "+indexItemCode[k]+"='"+indexItemValue[k]+"' and "+indexCode + "_id!=" + majorId;
				Map<String, Object> mapCount = new HashMap<>();
				mapCount.put("queryTemporarySql", sqlCount);
				// 获取表数据集
				List<Map<String, Object>> dataValuesCount = manualEntryService.temporaryTableList(null,mapCount);
				if(dataValuesCount.size()>0){
					request.setAttribute("msg", "已存在不能修改:"+indexItemTbList.get(k).getIndexItemName());
					return "forward:update.jhtml?majorId="+majorId+"&indexId="+indexId;
				}
			}
		}
		manualEntryService.updateIndexTbSql(indexItemCode, indexItemValue, majorId, indexTb, path, defaultId, codeCredit, codeOrg, request);
		request.setAttribute("msg", "操作成功");
		return "forward:defaultData.jhtml?id="+majorId+"&indexId="+indexId;
	}
	
	/**
	 * 模糊查询
	 * @param model
	 * @param 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/sysIndustrByName")
	@ResponseBody
	public List<SysClassFyModel> sysIndustrByName(HttpServletRequest request,@RequestParam(required=false) String name) throws Exception{
		
		List<SysClassFyModel> sysIndustrys=sysClassFyService.isSysIndNameByAll1(name, null);
		return sysIndustrys;
	}
	
	/**
	 * 模糊查询
	 * @param model
	 * @param 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/sysGoverByName")
	@ResponseBody
	public List<SysOrg> sysGoverByName(HttpServletRequest request,@RequestParam(required=false) String name) throws Exception{
		List<SysOrg> sysGover=sysOrgService.queryOrgAll(name, 1);
		return sysGover;
	}

}
