package com.workmanagement.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysImportentEnterpriseGroup;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysRole;
import com.workmanagement.model.SysUser;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.service.DefaultIndexItemService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysImportentEnterpriseGroupService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysRoleService;
import com.workmanagement.service.SysUserLogService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.ExcelReader;
import com.workmanagement.util.LoggerUtil;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;
import com.workmanagement.util.UpLoadFile;

/**
 * 重点企业管理
 * 
 * @author lzl
 */

@Controller
@RequestMapping("/admin/NewlyIncreased")
public class SysImportentEnterpriseGroupController {
	@Autowired
	private SysImportentEnterpriseGroupService sysImportentEnterpriseGroupService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private DefaultIndexItemService defaultIndexItemService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysUserLogService sysUserLogService;

	private static final String SYS_MENU = "重点企业群";

	/**
	 * 主页面
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
	public String list(Model model, HttpServletRequest request, @RequestParam(required = false) String orgName) throws Exception {
		// 当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Map<String, Object> map = new HashMap<>();
		PageSupport ps = PageSupport.initPageSupport(request);
		// 查询角色列表
		SysRole sr = sysRoleService.querySystemRoleById(userDetails.getRoleIds().get(0));
		boolean isPepole = sr.getSys_role_type() == SysRole.PEPOLE_ADMIN;
		if (isPepole) {
			if(userDetails.getSys_user_id().intValue() != 1){
				SysOrg so = sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
				SysArea sysArea1 = sysAreaService.queryAreaById(so.getSys_area_id());
				if(!sysArea1.getSysAreaType().equals("1")){
					// 获取地区缓存
					String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
					StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
					if (sb == null) {
						sb = new StringBuffer();
						SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
						DataUtil.getChildAreaIds(sysArea, sb);
						// 设置地区ID集合缓存
						RedisUtil.setData(areaSbKey, sb);
					}
					String[] s = sb.toString().split(",");// 区域id
					map.put("area_id", s);
					map.put("upid", s);
					List<SysOrg> is = sysOrgService.queryInstitution(map);
					StringBuffer sb1 = new StringBuffer();
					for (SysOrg sysOrg : is) {
						sb1.append(sysOrg.getSys_org_id() + ",");
					}
					map.put("orgIds", sb1.toString().split(","));
					
				}else{
					map.put("orgIn", so.getSys_org_affiliation_area_id());
				}
			}
		
		} else {
			map.put("orgId", userDetails.getSys_org_id());
		}
		if (!StringUtils.isEmpty(orgName)) {

			map.put("name", orgName);
			model.addAttribute("name", orgName);
		}

		List<SysImportentEnterpriseGroup> list = sysImportentEnterpriseGroupService.querySieg(map, ps);
		model.addAttribute("list", list);
		return "NewlyIncreased/list";
	}

	
	/*
	 * 下载模版
	 */
	@RequestMapping("/downLoad")
	@ResponseBody
	public void downLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			String[] rowNames = { "统一社会信用代码", "组织机构代码" };
			String[] propertyNames = { "codeCredit", "codeOrg" };

			// 生成excel
			ExcelExport<SysUser> excelExport = new ExcelExport<>();
			excelExport.setTitle("重点企业群Excle模版");
			excelExport.setRowNames(rowNames);
			excelExport.setPropertyNames(propertyNames);
			String url = excelExport.exportExcel(request, response);
			sysUserLogService.insertOneLog(new SysUserLog(SYS_MENU, null, null, null, null, null,
					SysManageLog.EXPORT_SYSMANAGElOG, null, "导出模版", null, null, url, null, true),request);

		} catch (Exception e) {
			LoggerUtil.error(e);
			sysUserLogService.insertOneLog(new SysUserLog(SYS_MENU, null, null, null, null, null,
					SysManageLog.EXPORT_SYSMANAGElOG, null, "导出模版", null, null, null, null, false),request);

		}
	}
	
	/**
	 * 新增页面
	 */
	@RequestMapping("/add")
	public String add(){
		return "/NewlyIncreased/add";
	}
	
	/**
	 * 上传
	 * @param name
	 * @param file
	 * @param request
	 */
	@RequestMapping("/importExcle")
	public String importExcle(Model model,String name,MultipartFile file, HttpServletRequest request){
		// 当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer areaId = userDetails.getSysOrg().getSys_area_id();
		SysImportentEnterpriseGroup s0 = sysImportentEnterpriseGroupService.qureyByName(name);
		List<String> errorList = new ArrayList<>();
		if(s0 !=null){
			model.addAttribute("msg", "该重点企业群名称已存在，请换一个名称");	
			return "/NewlyIncreased/add";
		}else{
			String url = UpLoadFile.upLoadFile(file);
			String[] rowNames = { "统一社会信用代码", "组织机构代码" };
			String[] propertyNames = { "codeCredit", "codeOrg" };
			
			ExcelReader<DefaultIndexItem> excelReader = new ExcelReader<>();
			excelReader.setRowNames(rowNames);
			excelReader.setPropertyNames(propertyNames);
			excelReader.setT(new DefaultIndexItem());
			try {
				List<DefaultIndexItem> list = excelReader.excelNoValidateReader(new File(url), request,0);
				//校验
				for (int i=0;i<list.size();i++) {
					DefaultIndexItem defaultIndexItem = list.get(i);
//					if(StringUtils.isNotBlank(defaultIndexItem.getCodeCredit())
//							&& StringUtils.isNotBlank(defaultIndexItem.getCodeOrg())){
//						errorList.add("第"+(i+1)+"行数不能二码都填写");
//					} 
					//先根据信用码查询二码表
					List<DefaultIndexItem> dlist =defaultIndexItemService.getByCredit(defaultIndexItem.getCodeCredit(),areaId);
					DefaultIndexItem  d= null;
					if(CollectionUtils.isNotEmpty(dlist)){
						d = dlist.get(0);
					}
					if(d!=null){//在二码表中有企业					
						//判断二码表中的这条数据是否有组织码
						if(!StringUtils.isBlank(d.getCodeOrg())){//有组织码
							//二码表中的组织码与上报的组织码不同
							if(!d.getCodeOrg().equals(defaultIndexItem.getCodeOrg())){
								errorList.add("第"+(i+1)+"行数据的组织码有误，请检查");
							}
						}
					}else{//二码表中没有对应的企业信息
						List<DefaultIndexItem> dl =defaultIndexItemService.getByCodeOrg(defaultIndexItem.getCodeOrg(),areaId);
						if(CollectionUtils.isEmpty(dl)){
							errorList.add("第"+(i+1)+"行数据没有对应的企业");
						}else{
						if(dl.size()==1){
							d = dl.get(0);
						}else{
							if(StringUtils.isBlank(dl.get(0).getCodeCredit())){
								d = dl.get(1);
							}else{								
								d = dl.get(0);
							}
						}
						}
					}
				}
				
				if(CollectionUtils.isEmpty(errorList)){//没有错误消息，可以上报数据，存进数据库
					SysImportentEnterpriseGroup s = new SysImportentEnterpriseGroup();
					s.setSys_importent_enterprise_group_name(name);
					s.setSys_user_id(userDetails.getSys_user_id());
					s.setSys_org_id(userDetails.getSys_org_id());
					sysImportentEnterpriseGroupService.insertGroup(s);
					//再将新增的这个重点企业群查询出，得到id
					SysImportentEnterpriseGroup s1 = sysImportentEnterpriseGroupService.qureyByName(name);
					Integer id = s1.getSys_importent_enterprise_group_id();
					for (int i=0;i<list.size();i++) {
						Integer did = null;
						if(StringUtils.isNotBlank(list.get(i).getCodeCredit())){
							List<DefaultIndexItem> dlist =defaultIndexItemService.getByCredit(list.get(i).getCodeCredit(),areaId);
							DefaultIndexItem  d= null;
							if(CollectionUtils.isNotEmpty(dlist)){
								d = dlist.get(0);
							}
							did = d.getDefaultIndexItemId();
						}else{
							List<DefaultIndexItem> dl =defaultIndexItemService.getByCodeOrg(list.get(i).getCodeOrg(),areaId);
							if(dl.size()==1){
								did = dl.get(0).getDefaultIndexItemId();
							}else{
								if(StringUtils.isBlank(dl.get(0).getCodeCredit())){
									did = dl.get(1).getDefaultIndexItemId();
								}else{								
									did = dl.get(0).getDefaultIndexItemId();
								}
							}
						}
						sysImportentEnterpriseGroupService.insertOneOfGroup(id, did);
					}					
					model.addAttribute("msg", "操作成功");
					return "/NewlyIncreased/add";
				}else{
					model.addAttribute("msg", "操作失败");					
					model.addAttribute("errorList", errorList);	
					return "/NewlyIncreased/add";
				}
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("err", e.getMessage());
				//model.addAttribute("msg", "出现异常");
				model.addAttribute("errorList", errorList);
				return "/NewlyIncreased/add";
			}
		}
	}
	
	/**
	 * 导出
	 */
	@RequestMapping("/exportExcel")
	@ResponseBody
	public void exportExcel(Model model,HttpServletResponse response,HttpServletRequest request,Integer id){
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);			
			//根据企业群的id查询旗下的企业
			SysImportentEnterpriseGroup s= sysImportentEnterpriseGroupService.querySieg(map, null).get(0);
			List<DefaultIndexItem> list=s.getDefault_index_item_id();
			String[] rowNames = { "统一社会信用代码", "组织机构代码","企业名称"};
			String[] propertyNames = { "codeCredit", "codeOrg","qymc" };			
			// 生成excel
			ExcelExport<DefaultIndexItem> excelExport = new ExcelExport<>();
			excelExport.setTitle(s.getSys_importent_enterprise_group_name());
			excelExport.setRowNames(rowNames);
			excelExport.setPropertyNames(propertyNames);
			excelExport.setList(list);
			String url = excelExport.exportExcel(request, response);
			sysUserLogService.insertOneLog(new SysUserLog(SYS_MENU, null, null, null, null, null,
					SysManageLog.EXPORT_SYSMANAGElOG, null, "导出", null, null, url, null, true),request);

		} catch (Exception e) {
			LoggerUtil.error(e);
		}
	} 
	
	
	@RequestMapping("/getGroup")
	@ResponseBody
	public List<SysImportentEnterpriseGroup>  getGroup(String name,HttpServletRequest request) throws Exception{
		// 当前登录用户的session
				MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				Map<String, Object> map = new HashMap<>();
				PageSupport ps = PageSupport.initPageSupport(request);
				// 查询角色列表
				SysRole sr = sysRoleService.querySystemRoleById(userDetails.getRoleIds().get(0));
				boolean isPepole = sr.getSys_role_type() == SysRole.PEPOLE_ADMIN;
				if (isPepole) {
					if(userDetails.getSys_user_id().intValue() != 1){
						SysOrg so = sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
						SysArea sysArea1 = sysAreaService.queryAreaById(so.getSys_area_id());
						if(!sysArea1.getSysAreaType().equals("1")){
							// 获取地区缓存
							String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
							StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
							if (sb == null) {
								sb = new StringBuffer();
								SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
								DataUtil.getChildAreaIds(sysArea, sb);
								// 设置地区ID集合缓存
								RedisUtil.setData(areaSbKey, sb);
							}
							String[] s = sb.toString().split(",");// 区域id
							map.put("area_id", s);
							map.put("upid", s);
							List<SysOrg> is = sysOrgService.queryInstitution(map);
							StringBuffer sb1 = new StringBuffer();
							for (SysOrg sysOrg : is) {
								sb1.append(sysOrg.getSys_org_id() + ",");
							}
							map.put("orgIds", sb1.toString().split(","));
							
						}else{
							map.put("orgIn", so.getSys_org_affiliation_area_id());
						}
					}
				
				} else {
					map.put("orgId", userDetails.getSys_org_id());
				}
				if (!StringUtils.isEmpty(name)) {
					map.put("name", name);
				}

				List<SysImportentEnterpriseGroup> list = sysImportentEnterpriseGroupService.querySieg(map, ps);
		return list;
	}
	
	@RequestMapping("/getGroupId")
	@ResponseBody
	public List<SysImportentEnterpriseGroup>  getGroup(Integer id) throws Exception{
		// 当前登录用户的session
					Map<String, Object> map = new HashMap<>();
					map.put("id", id);
				
				List<SysImportentEnterpriseGroup> list = sysImportentEnterpriseGroupService.querySieg(map, null);
		return list;
	}
}
