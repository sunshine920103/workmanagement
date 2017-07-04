package com.workmanagement.controller;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.reflect.TypeToken;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysUser;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.ExcelReaderArea;
import com.workmanagement.util.LoggerUtil;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;
import com.workmanagement.util.SortArea;



/**
 * 地区管理
 * @author wqs
 */
@Controller
@RequestMapping("/admin/sysArea")
public class SysAreaController {

	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired 
	private SysManageLogService sysManageLogService;
	@Autowired 
	private SysUserService sysUserService;
	
	/**
	 * 通过ID获取地区
	 * @param model
	 * @param id 地区ID
	 * @return
	 */
	@RequestMapping("/getArea")
	@ResponseBody
	public SysArea getArae(@RequestParam(required=false) Integer id){
		if(id==null){
			return null;
		}
		//获取地区缓存
		String key = RedisKeys.SYS_AREA_DOWN + id;
		SysArea a = RedisUtil.getObjData(key, SysArea.class);
		if(a==null){
			a = sysAreaService.queryAreaById(id);
			RedisUtil.setData(key, a);
		}
		return a;
	}
	/**
	 * 通过名字查询地区
	 * 
	 */
	@RequestMapping("/findAreaByName")
	@ResponseBody
	public List<SysArea> findAreaByName(String areaName,String code){
		List<SysArea> list= sysAreaService.queryAreaByNameAndCode(areaName, code);
		return list;
	}
	
	/**
	 * 通过ID删除地区以及子地区,需判断机构表中是否有机构在地区以及子地区，若有则不能删除
	 * @param model
	 * @param id 地区ID
	 * @return
	 */
	@RequestMapping("/del")
	@ResponseBody
	public JsonResWrapper del(@RequestParam(required=true) Integer id,HttpServletRequest request){
		JsonResWrapper jrw = new JsonResWrapper();
			//获取地区缓存
			String key = RedisKeys.SYS_AREA + id;
			SysArea a = RedisUtil.getObjData(key, SysArea.class);
			if(a==null){
				a = sysAreaService.queryAreaById(id);
				RedisUtil.setData(key, a);
			}
			try {
			if(id==null || a==null){
				jrw.setFlag(false);
				jrw.setMessage("删除失败，参数缺失");
				return jrw;
			}
			
			//获取地区及子地区ID缓存
			String sbKey = RedisKeys.SYS_AREA_SB_CHILD + id;
			StringBuffer sb = RedisUtil.getObjData(sbKey, StringBuffer.class);
			if(sb==null){
				sb = new StringBuffer();
				DataUtil.getChildAreaIds(a, sb);
				RedisUtil.setData(sbKey, sb);
			}
			String[] ids = sb.toString().split(",");
			//查询该地区是否有子地区，若有则不允许删除
			if(a.getSubArea()!=null && a.getSubArea().size()>0){
				jrw.setFlag(false);
				jrw.setMessage("删除失败，在该地区有下级地区存在，不允许删除");
				return jrw;
			}
			
			//查询机构表中是否有机构在地区以及子地区，若有则不能删除
			List<SysOrg> orgList = sysOrgService.querySysOrgByAreaIds(ids);
			if(orgList!=null && orgList.size()>0){
				jrw.setFlag(false);
				jrw.setMessage("删除失败，在该地区下有机构存在，不允许删除");
				return jrw;
			}
			
			sysAreaService.delAreaByIds(ids);
			sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, null, a.toString(),null , 
					new Date(), 2, 1, null, null, null, null, null, true),request);
			jrw.setFlag(true);
			jrw.setMessage("删除成功");
		}catch (Exception e){
			sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, null, a.toString(),null , 
					new Date(), 2, 1, null, null, null, null, null, false),request);
		}
		return jrw;
	}
	
	/**
	 * 地区修改
	 * @param model
	 * @param code 地区代号
	 * @return
	 */
	@RequestMapping("/modify")
	public String modify(Model model, @RequestParam(required=false) String code){
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<SysArea> aList  = sysAreaService.queryAreaByNameAndCode(null, code);
		Integer id=aList.get(0).getSysAreaId();
		String upName= null;
		SysArea upa = sysAreaService.queryParentAreasById(id);
			if(!CollectionUtils.isEmpty(upa.getSubArea())){
				upName=upa.getSubArea().get(0).getSysAreaName();
			}else{
				upName="顶级区域";
			}
			SysArea a = sysAreaService.queryAreaById(id);
		a.setPname(upName);
		model.addAttribute("a", a);
		model.addAttribute("poptype", "modify");
		if(userDetails.getSys_user_id()==1){		
			return "sysArea/add2";
		}else{			
			return "sysArea/add";
		}
	}
	
	/**
	 * 地区添加
	 * @param model
	 * @param id 地区ID
	 * @return
	 */
	@RequestMapping("/add")
	public String add(Model model, @RequestParam(required=false) Integer id){
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		if(userDetails.getSys_user_id()==1){
			String key = RedisKeys.SYS_AREA_ALL ;
			 Type type = new TypeToken<List<SysArea>>() {
	            }.getType();
			List<SysArea> aList=RedisUtil.getListData(key, type);
			if(CollectionUtils.isEmpty(aList)){
				aList=sysAreaService.queryAll();
				RedisUtil.setData(key, aList);
			}
			model.addAttribute("aList", aList);
			model.addAttribute("poptype", "add");
			return "sysArea/add2";
		}
		if(id!=null){
			//根据地区ID获取缓存
			String key = RedisKeys.SYS_AREA + id;
			SysArea a = RedisUtil.getObjData(key, SysArea.class);
			if(a==null){
				a = sysAreaService.queryAreaById(id);
				RedisUtil.setData(key, a);
			}
			model.addAttribute("a", a);
			model.addAttribute("poptype", "add");
		}
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
		return "sysArea/add";
	}
	
	/**
	 * 保存地区添加
	 * @param model
	 * @param id 地区ID
	 * @return
	 */
	@RequestMapping(value = "/xgsave", method = RequestMethod.POST)
	@ResponseBody
	public JsonResWrapper xgsave(Model model, HttpServletRequest request,
					  @RequestParam(required=false) Integer sysAreaId,
					  @RequestParam(required=false) Integer sysAreaUpid,
					  @RequestParam(required=false) String pname,
					  @RequestParam(required=false) String sysAreaName,
					  @RequestParam(required=false) String sysAreaType,
					  @RequestParam(required=false) String sysAreaCode,
					  @RequestParam(required=false) String sysAreaNotes){
		JsonResWrapper jrw = new JsonResWrapper();
		try{
			SysArea a = new SysArea();
			a.setSysAreaId(sysAreaId);
			a.setSysAreaCode(sysAreaCode);
			a.setSysAreaType(sysAreaType);
			a.setSysAreaName(sysAreaName);
			a.setSysAreaNotes(sysAreaNotes);
			a.setSysAreaUpid(sysAreaUpid);
			a.setPname(pname);
			if(StringUtils.isBlank(sysAreaName) || StringUtils.isBlank(sysAreaType) || StringUtils.isBlank(sysAreaCode)){
				jrw.setFlag(false);
				jrw.setMessage("保存失败，参数缺失");
				return jrw;
			}
			//判断区域名称或行政代码是否已存在
			List<SysArea> sysAreaList = sysAreaService.queryAreaByNameAndCode(sysAreaName, sysAreaCode);
				if(sysAreaList!=null && sysAreaList.size()>0 ){ // 不为空，则说明该区域已存在
					if(sysAreaList.get(0).getSysAreaCode().equals(sysAreaCode) 
							&& sysAreaList.get(0).getSysAreaName().equals(sysAreaName)){//判断新保存的地区的名字和代号						
						jrw.setFlag(false);												//都一样则不能保存，否则可以更新
						jrw.setMessage("保存失败，区域名称或行政代码已存在");					//是否与数据库对应的都一致，如果
						return jrw;
					}
					sysAreaService.updateSysArea(a);
					jrw.setFlag(true);
					jrw.setMessage("更新成功");
					return jrw;
				}
			sysAreaService.saveArea(a);
			jrw.setFlag(true);
			jrw.setMessage("保存成功");
		}catch (Exception e){
			LoggerUtil.error(e);
			jrw.setFlag(false);
			jrw.setMessage("系统错误");
		}
		return jrw;
	}
	
	/**
	 * 保存地区添加
	 * @param model
	 * @param id 地区ID
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonResWrapper save(Model model, HttpServletRequest request,
						String poptype,
					  @RequestParam(required=false) Integer sysAreaId,
					  @RequestParam(required=false) Integer sysAreaUpid,
					  @RequestParam(required=false) String pname,
					  @RequestParam(required=false) String sysAreaName,
					  @RequestParam(required=false) String sysAreaType,
					  @RequestParam(required=false) String sysAreaCode,
					  @RequestParam(required=false) String sysAreaNotes){
		JsonResWrapper jrw = new JsonResWrapper();
		try{
			//当前登录用户的session
			MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
			SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
			if(so ==null){
				so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
				RedisUtil.setData(orgKey, so);
			}
			SysArea a = new SysArea();
			if("modify".equals(poptype)){
				a.setSysAreaId(sysAreaId);				
			}
			a.setSysAreaCode(sysAreaCode);
			a.setSysAreaType(sysAreaType);
			a.setSysAreaName(sysAreaName);
			a.setSysAreaNotes(sysAreaNotes);
			a.setSysAreaUpid(sysAreaUpid);
			a.setPname(pname);
			//判断本级code和上级code是否对应
			if(!"0".equals(sysAreaType)){				
				String code=sysAreaService.queryAreaById(sysAreaUpid).getSysAreaCode();//上级地区code
				if("1".equals(sysAreaType)){
					if(!code.substring(0, 2).equals(sysAreaCode.substring(0, 2))){
						jrw.setFlag(false);
						jrw.setMessage("保存失败，地市的区域代号前2位应与省相同");
						return jrw;
					}
				}else if("2".equals(sysAreaType)){
					if(!code.substring(0, 4).equals(sysAreaCode.substring(0, 4))){
						jrw.setFlag(false);
						jrw.setMessage("保存失败，区县的区域代号前4位应与地市相同");
						return jrw;
					}
				}else if("3".equals(sysAreaType)){
					if(!code.substring(0, 6).equals(sysAreaCode.substring(0, 6))){
						jrw.setFlag(false);
						jrw.setMessage("保存失败，乡镇的区域代号前6位应与区县相同");
						return jrw;
					}
				}
			}
			if(StringUtils.isBlank(sysAreaName) || StringUtils.isBlank(sysAreaType) || StringUtils.isBlank(sysAreaCode)){
				jrw.setFlag(false);
				jrw.setMessage("保存失败，参数缺失");
				return jrw;
			}
			//判断区域名称或行政代码是否已存在
			List<SysArea> sysAreaList = sysAreaService.queryAreaByNameAndCode(sysAreaName, sysAreaCode);
			if("modify".equals(poptype)){				
				if( !CollectionUtils.isEmpty(sysAreaList) ){ // 不为空，则说明该区域已存在
					if(sysAreaList.get(0).getSysAreaCode().equals(sysAreaCode) 
							&& sysAreaList.get(0).getSysAreaName().equals(sysAreaName) 
							&& (sysAreaNotes==null?"":sysAreaNotes).equals(sysAreaList.get(0).getSysAreaName())){//判断新保存的地区的名字和代号						
						jrw.setFlag(false);												//都一样则不能保存，否则可以更新
						jrw.setMessage("保存失败，区域名称或行政代码已存在");					//是否与数据库对应的都一致，如果
						return jrw;
					}
					sysAreaService.updateSysArea(a);
					sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, 
							null, sysAreaList.get(0).toString(), a.toString(), new Date(), 3, 1, 
							null, null, null, null, null, true),request);
					jrw.setFlag(true);
					jrw.setMessage("更新成功");
					return jrw;
				}
			}
			//新增前校验
			SysArea area ;
			try{				
				area = sysAreaService.queryAreaByCode(sysAreaCode);
			}catch(Exception e){
				e.printStackTrace();
				jrw.setFlag(false);
				jrw.setMessage("地区代号："+sysAreaCode+"，对应有多条地区数据");
				return jrw;
			}
			if(area !=null){
				jrw.setFlag(false);
				jrw.setMessage("保存失败，地区代号已存在");
				return jrw;
			}
			sysAreaService.saveArea(a);
			//保存管理日志
			if(!"modify".equals(poptype)){//新增
				sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, null,
						null, a.toString(), new Date(), 1, 1, null, null, null, null, null, true),request);
			}
			jrw.setFlag(true);
			jrw.setMessage("保存成功");
		}catch (Exception e){
			LoggerUtil.error(e);
			jrw.setFlag(false);
			jrw.setMessage("系统错误");
		}
		return jrw;
	}
	
	/**
	 * 从excel中导入地区
	 */
	@RequestMapping(value="/importArea")
	public String importArea(SysArea area,MultipartFile file,HttpServletRequest request,
			HttpServletResponse response,Model model) throws IOException{
		try{
		//读取文件
		if (file != null && file.getSize() > 0) {
			String[] rowNames = { "地区名称", "行政代号","上级地区行政代号","区域类型", "备注"};
			String[] propertyNames = { "sysAreaName", "sysAreaCode","pcode", "sysAreaType", "sysAreaNotes"};
			ExcelReaderArea<SysArea> excelReader = new ExcelReaderArea<>();
			excelReader.setRowNames(rowNames);
			excelReader.setPropertyNames(propertyNames);
			excelReader.setT(new SysArea());
			List<SysArea> list=new ArrayList<>();
			String filePath=null;
			list = excelReader.excelReader(file, request,filePath);//从excel中读取的数据集
			if(CollectionUtils.isEmpty(list)){
				model.addAttribute("msg", "请检查excel文件是否有误");
				return "forward:list.jhtml";
			}
			List<SysArea> Oklist = new ArrayList<>();//装正确结果的list
			List<String> errorList = new ArrayList<>();//装错误信息的list
			for(int i=0; i<list.size() ; i++){//校验当前excel文件中地区代号是否重复
				SysArea sysArea=list.get(i);
				for(int j=i+1; j<list.size() ; j++){
					SysArea sysArea2=list.get(j);
					if(sysArea.getSysAreaCode().equals(sysArea2.getSysAreaCode())){
						errorList.add("第"+(i+2)+"行数据的代号与第"+(j+2)+"行数据的代号重复了");
					}
				}
			}
			for (int i=0; i<list.size() ; i++) {
				SysArea sysArea=list.get(i);
				//先查询数据库中是否有该地区
				SysArea a = sysAreaService.queryAreaByCode(sysArea.getSysAreaCode());
				if( a != null){ //如果查询到该地区已经存在
					errorList.add("第"+(i+2)+"行地区数据的代号或名字已存在");
				}else{					
					//地区类型转成代号存入数据库，
					//校验每一条数据的区域代号sysAreaCode与上级地区的code是否符合条件
					if("省、直辖市".equals(sysArea.getSysAreaType()) || "0".equals(sysArea.getSysAreaType())){
						sysArea.setSysAreaType("0");
					}else if("地市".equals(sysArea.getSysAreaType()) || "1".equals(sysArea.getSysAreaType())){
						if(sysArea.getSysAreaCode().substring(0, 2).equals(sysArea.getPcode().substring(0, 2))){						
							sysArea.setSysAreaType("1");
						}else{
							errorList.add("第"+(i+2)+"行地区数据的代号与上级代号不匹配");
						}
					}else if("区县".equals(sysArea.getSysAreaType()) || "2".equals(sysArea.getSysAreaType())){
						if(sysArea.getSysAreaCode().substring(0, 4).equals(sysArea.getPcode().substring(0, 4))){						
							sysArea.setSysAreaType("2");
						}else{
							errorList.add("第"+(i+2)+"行地区数据的代号与上级代号不匹配");
						}
					}else if("乡镇".equals(sysArea.getSysAreaType()) || "3".equals(sysArea.getSysAreaType())){
						if(sysArea.getSysAreaCode().substring(0, 6).equals(sysArea.getPcode().substring(0, 6))){						
							sysArea.setSysAreaType("3");
						}else{
							errorList.add("第"+(i+2)+"行地区数据的代号与上级代号不匹配");
						}
					}else if(!"0".equals(sysArea.getSysAreaType()) && !"1".equals(sysArea.getSysAreaType()) && 
							!"2".equals(sysArea.getSysAreaType())  && !"3".equals(sysArea.getSysAreaType())){//地区类型填写错误
						errorList.add("第"+(i+2)+"行地区类型填写错误,请填写:省、直辖市(0)、地市(1)、区县(2)、乡镇(3),汉字或者代号");
					}				
					//前提：excel中的数据是分批次导入
					//通过上级区域行政代号sysAreaUpCode，查询上级区域			
					SysArea upArea= sysAreaService.queryAreaByCode(sysArea.getPcode());
					if(upArea==null && !"0".equals(sysArea.getSysAreaType())){//除开四川省以外，如果当前地区没有上级地区的话：
						errorList.add("第"+(i+2)+"行地区数据没有上级区域");
					}else{//当前地区有上级地区，或者没有上级地区（为四川省）
						if(upArea==null){//当前为四川省
							Oklist.add(sysArea);
						}else {	//当前不是四川省						
							Integer pid = upArea.getSysAreaId();//一个地区必须先存在上级区域
							sysArea.setSysAreaUpid(pid);
							Oklist.add(sysArea);						
						}
					} 
				}
			}
			List<String> wrongList  = new ArrayList<>();//导入异常消息集合  
			if(CollectionUtils.isEmpty(errorList)){//如果没有错误消息				
				for (int i=0;i< Oklist.size() ; i++) {
					try{
						sysAreaService.saveArea(Oklist.get(i));					
					}catch(Exception e){
						e.printStackTrace();
						wrongList.add("第"+(i+2)+"行数据导入异常");
					}
				}
			}
			model.addAttribute("errorList", errorList);
			if(CollectionUtils.isEmpty(wrongList) && CollectionUtils.isEmpty(errorList)){
				model.addAttribute("msg", "导入成功");
				sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, null, 
						null, null, new Date(), 5, list.size(), null, null, null,filePath , null, true),request);
			}else{
				model.addAttribute("msg", "导入失败");
				sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, null, 
						null, null, new Date(), 5, list.size(), null, null, null,filePath , null, false),request);
			}
			
		}else{
			model.addAttribute("msg", "无文件，请选择一个文件");
		}	
		}catch (Exception e){
			LoggerUtil.error(e);
			model.addAttribute("msg", "系统错误");
		}
		//return "sysArea/list";
		return "forward:list.jhtml";
	}
	/**
	 * 导出地区模版
	 */
	@RequestMapping("/exportAreaTemplate")
	public void exportAreaTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException{
		String[] rowNames = { "地区名称", "行政代号","上级地区行政代号","区域类型", "备注"};
		String[] propertyNames = { "sysAreaName", "sysAreaCode", "pcode","sysAreaType", "sysAreaNotes"};
		
		// 生成excel
		ExcelExport<SysArea> excelExport = new ExcelExport<>();
		excelExport.setTitle("地区列表模版");
		excelExport.setRowNames(rowNames);
		excelExport.setPropertyNames(propertyNames);
		String filePath=null;
		try{			
			filePath= excelExport.exportExcel(request, response);
			sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, null, null, null, 
					new Date(), 6, 0, null, null, null,filePath , null, true),request);
		}catch(Exception e){
			sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, null, null, null, 
					new Date(), 6, 0, null, null, null,filePath , null, false),request);			
		}
	}
	
	/**
	 * 导出地区到excel
	 * @throws IOException 
	 */
	@RequestMapping("/exportArea")
	public void exportArea(HttpServletResponse response, HttpServletRequest request,Integer id) 
			throws IOException{
		//查询登陆用户所在机构的地区以及子地区，
		SysArea area = sysAreaService.queryAreaById(id);
		List<SysArea> areaList = new ArrayList<>();
		List<SysArea> shiList = area.getSubArea();
		areaList.add(area);
		//遍历下级区域并装进areaList中
		if(shiList != null && shiList.size()>0){
			areaList.addAll(shiList);
			for (SysArea shiArea : shiList){
				List<SysArea> xianList= shiArea.getSubArea();
				if(xianList != null && xianList.size()>0){
					areaList.addAll(xianList);
					for (SysArea xianArea : xianList) {
						List<SysArea> zhengList= xianArea.getSubArea();
						areaList.addAll(zhengList);
					}
				}
			}
		}
		for (SysArea sysArea : areaList) {
			//通过上级地区id查询code
			SysArea upa=sysAreaService.queryAreaById(sysArea.getSysAreaUpid());
			if(upa!=null){				
				sysArea.setPcode(upa.getSysAreaCode());
			}
			if("0".equals(sysArea.getSysAreaType())){
				sysArea.setSysAreaType("省、直辖市");
			}else if("1".equals(sysArea.getSysAreaType())){
				sysArea.setSysAreaType("地市");
			}else if("2".equals(sysArea.getSysAreaType())){
				sysArea.setSysAreaType("区县");
			}else{
				sysArea.setSysAreaType("乡镇");
			}
		}
		//按地区代号排序
		SortArea sortArea = new SortArea();
		Collections.sort(areaList, sortArea);
		
		String[] rowNames = { "地区名称", "行政代号","上级地区行政代号","区域类型", "备注"};
		String[] propertyNames = { "sysAreaName", "sysAreaCode", "pcode","sysAreaType", "sysAreaNotes"};
		
		// 生成excel
		ExcelExport<SysArea> excelExport = new ExcelExport<>();
		excelExport.setTitle("地区列表");
		excelExport.setRowNames(rowNames);
		excelExport.setPropertyNames(propertyNames);
		excelExport.setList(areaList);
		String filePath=null;
		try{			
			filePath=excelExport.exportExcel(request, response);
			sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, null, null, null, 
					new Date(), 6, areaList.size(), null, null, null,filePath , null, true),request);
		}catch(Exception e){
			sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, null, null, null, 
					new Date(), 6, areaList.size(), null, null, null,filePath , null, false),request);			
		}	  
	}
	
	/**
	 * 导出地区到excel
	 * @throws IOException 
	 */
	@RequestMapping("/exportArea2")
	public void exportArea2(HttpServletResponse response, HttpServletRequest request) 
			throws IOException{
		//查询登陆用户所在机构的地区以及子地区，
		
		String key = RedisKeys.SYS_AREA_ALL_WITHSUB ;
		 Type type = new TypeToken<List<SysArea>>() {
           }.getType();
		List<SysArea> allList=RedisUtil.getListData(key, type);
		if(CollectionUtils.isEmpty(allList)){
			allList = sysAreaService.queryAllAreaWithSub();
			RedisUtil.setData(key, allList);
		}
		List<SysArea> areaList = new ArrayList<>();
		for (SysArea area : allList) {
			List<SysArea> shiList = area.getSubArea();			
			areaList.add(area);
			//遍历下级区域并装进areaList中
			if(shiList != null && shiList.size()>0){
				areaList.addAll(shiList);
				for (SysArea shiArea : shiList){
					List<SysArea> xianList= shiArea.getSubArea();
					if(xianList != null && xianList.size()>0){
						areaList.addAll(xianList);
						for (SysArea xianArea : xianList) {
							List<SysArea> zhengList= xianArea.getSubArea();
							areaList.addAll(zhengList);
						}
					}
				}
			}
		}
		for (SysArea sysArea : areaList) {
			//通过上级地区id查询code
			SysArea upa=sysAreaService.queryAreaById(sysArea.getSysAreaUpid());
			if(upa!=null){				
				sysArea.setPcode(upa.getSysAreaCode());
			}
			if("0".equals(sysArea.getSysAreaType())){
				sysArea.setSysAreaType("省、直辖市");
			}else if("1".equals(sysArea.getSysAreaType())){
				sysArea.setSysAreaType("地市");
			}else if("2".equals(sysArea.getSysAreaType())){
				sysArea.setSysAreaType("区县");
			}else{
				sysArea.setSysAreaType("乡镇");
			}
		}
		//按地区代号排序
		SortArea sortArea = new SortArea();
		Collections.sort(areaList, sortArea);
		
		String[] rowNames = { "地区名称", "行政代号","上级地区行政代号","区域类型", "备注"};
		String[] propertyNames = { "sysAreaName", "sysAreaCode", "pcode","sysAreaType", "sysAreaNotes"};
		
		// 生成excel
		ExcelExport<SysArea> excelExport = new ExcelExport<>();
		excelExport.setTitle("地区列表");
		excelExport.setRowNames(rowNames);
		excelExport.setPropertyNames(propertyNames);
		excelExport.setList(areaList);
		String filePath=null;
		try{			
			filePath=excelExport.exportExcel(request, response);
			sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, null, null, null, 
					new Date(), 6, areaList.size(), null, null, null,filePath , null, true),request);
		}catch(Exception e){
			sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, null, null, null, 
					new Date(), 6, areaList.size(), null, null, null,filePath , null, false),request);			
		}	  
	}

	
	/**
	 * 地区列表页面
	 * @param reqeust
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model){
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		if(userDetails.getSys_user_id()==1){
			String key = RedisKeys.SYS_AREA_ALL ;
			 Type type = new TypeToken<List<SysArea>>() {
	            }.getType();
			List<SysArea> aList=RedisUtil.getListData(key, type);
			if(CollectionUtils.isEmpty(aList)){
				//aList=sysAreaService.queryAll2();
				aList = sysAreaService.queryAreaByName("崇左市");
				RedisUtil.setData(key, aList);
			}
			model.addAttribute("aList", aList);
			return "sysArea/list2";
		}
		//根据机构ID获取机构缓存
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		if(so!=null){
			//如果区域为空，就根据机构所在地址查询
			int areaid = so.getSys_area_id();
			String key = RedisKeys.SYS_AREA + areaid;
			SysArea area = RedisUtil.getObjData(key, SysArea.class);
			if(area==null){
				area = sysAreaService.queryAreaById(areaid);
				RedisUtil.setData(key, area);
			}
			model.addAttribute("area", area);
		}
		return "sysArea/list";
	}
	
}
