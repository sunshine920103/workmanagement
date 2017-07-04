package com.workmanagement.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.GoverType;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysGover;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.service.GoverTypeService;
import com.workmanagement.service.SysGoverService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.ExcelReaderOrg;
import com.workmanagement.util.LoggerUtil;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.UpLoadFile;

/**
 * 政府部门管理
 * @author tianhao
 *
 */
@Controller
@RequestMapping(value="/admin/sysGover")
public class SysGoverController {
	
	@Autowired
	private SysGoverService sysGoverService;
	
	@Autowired
	private SysOrgService sysOrgService;
	
	@Autowired
	private GoverTypeService goverTypeService;
	
	@Autowired
	private SysManageLogService sysManageLogService;
	
	/**
	 * 政府部门列表页面
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(HttpServletRequest request, Model model,
			@RequestParam(required=false) String sysGovName,@RequestParam(required=false) Integer url) throws Exception{
		PageSupport ps = PageSupport.initPageSupport(request);
		SysGover sg=new SysGover();
		sg.setSysGovName(sysGovName);
		List<SysGover> its = DataUtil.isEmpty(sysGoverService.querySysGoverList(ps,sg));
		if(sysGovName!=null && !"".equals(sysGovName)){
			if(url==null){
				List<SysGover> sysGovList = DataUtil.isEmpty(sysGoverService.querySysGoverList(null,sg));
				String sql="SELECT * FROM SYS_GOV_TB WHERE 1=1 AND SYS_GOV_NAME LIKE '%"+sysGovName+"%' ORDER BY SYS_GOV_FINANCIAL_CODE";
				SysManageLog sm=new SysManageLog();
				sm.setSysManageLogMenuName("政府部门管理");
				sm.setSysManageLogResult(true);
				sm.setSysManageLogCount(sysGovList.size());
				sm.setSysManageLogOperateType(SysManageLog.SELECT_SYSMANAGElOG);
				sm.setSysManageLogQueryUserCondition(sysGovName+"(通过名称模糊查询)");
				sm.setSysManageLogQuerySql(sql);
				sm.setSysManageLogUrl("/admin/sysGover/list.jhtml?sysGovName="+sysGovName+"&url=1");
				sysManageLogService.insertSysManageLogTb(sm, request);
			}
		}
		model.addAttribute("its", its);
		model.addAttribute("sysGovName", sysGovName);
		return "sysGover/list";
	}
	
	/**
	 * 增加、修改政府部门
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(HttpServletRequest request,Model model,
			@RequestParam(required=false) Integer id) throws Exception{
		if(id!=null){
			SysGover it=sysGoverService.querySysGoverById(id);
			model.addAttribute("it", it);
		}
		SysGover sg=new SysGover();
		List<SysGover> its = sysGoverService.querySysGoverByNameList(sg);
		model.addAttribute("its", its);
		GoverType gt=new GoverType();
		List<GoverType> gts = goverTypeService.queryGoverTypeByNameList(gt);
		model.addAttribute("gts", gts);
		return "sysGover/add";
	}
	
	/**
	 * 增加、修改政府部门
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public JsonResWrapper save(Model model,HttpServletRequest request,
			@RequestParam(required=false) Integer sysGovId,
			@RequestParam(required=false) Integer sysGovUpid,
			@RequestParam(required=false) Integer sysGovTypeId,
			@RequestParam(required=false) String sysGovTypeCode,
			@RequestParam(required=false) String sysGovUpname,
			@RequestParam(required=false) String sysGovUpcode,
			@RequestParam(required=false) String sysGovName,
			@RequestParam(required=false) String sysGovFinancialCode,
			@RequestParam(required=false) String sysOrgNotes) throws Exception{
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		int sysAreaId=so.getSys_area_id();
		JsonResWrapper jrw = new JsonResWrapper();
		SysGover sg=new SysGover();
		sg.setSysGovId(sysGovId);
		sg.setSysGovUpid(sysGovUpid);
		sg.setSysGovTypeId(sysGovTypeId);
		sg.setSysGovUpname(sysGovUpname);
		sg.setSysGovName(sysGovName);
		sg.setSysGovFinancialCode(sysGovFinancialCode);
		sg.setSysOrgNotes(sysOrgNotes);
		sg.setSysAreaId(sysAreaId);
		sg.setSysDicId(42);
		//判断政府部门名称和代码是否为空
		if(StringUtils.isBlank(sysGovName) || StringUtils.isBlank(sysGovFinancialCode)){
			jrw.setFlag(false);
			jrw.setMessage("保存失败，参数缺失");
			return jrw;
		}
		//判断是否选择自己为自己的上级
		if(sysGovUpid!=null && sysGovId!=null){
			SysGover sum=sysGoverService.querySysGoverById(sysGovId);
			SysGover num=sysGoverService.querySysGoverById(sysGovUpid);
			if(sum.getSysGovUpid()==sysGovUpid){
				if(sum.getSysGovUpid()==num.getSysGovUpid()){
					jrw.setFlag(false);
					jrw.setMessage("保存失败，不能选择自己为自己的上级");
					return jrw;
				}
			}
		}
		//判断政府部门名称是否重复
		int nameCount=sysGoverService.querySysGoverByNameCount(sg);
		if(nameCount>0){
			jrw.setFlag(false);
			jrw.setMessage("保存失败，政府部门名称重复");
			return jrw;
		}
		//判断政府部门编码是否重复
		int codeCount=sysGoverService.querySysGoverByCodeCount(sg);
		if(codeCount>0){
			jrw.setFlag(false);
			jrw.setMessage("保存失败，政府部门编码重复");
			return jrw;
		}
		//判断与政府类型编码是否匹配
		if(sysGovTypeId!=null && !"".equals(sysGovTypeId)){
			if(sysGovTypeCode!=null){
				if(sysGovFinancialCode.length()>=4){
					String govCode=sysGovFinancialCode.substring(0, 4);
					if(!govCode.equals(sysGovTypeCode)){
						jrw.setFlag(false);
						jrw.setMessage("保存失败，与政府类型编码不匹配");
						return jrw;
					}
				}else{
					jrw.setFlag(false);
					jrw.setMessage("保存失败，部门编码不符合要求");
					return jrw;
				}
			}
		}
		//判断与上级部门编码是否匹配
		if(sysGovUpid!=null && !"".equals(sysGovUpid)){
			if(sysGovUpcode!=""){
				if(sysGovFinancialCode.length()>=8){
					String govCode=sysGovFinancialCode.substring(0, 8);
					String govUpcode=sysGovUpcode.substring(0, 8);
					if(!govCode.equals(govUpcode)){
						jrw.setFlag(false);
						jrw.setMessage("保存失败，与上级部门编码不匹配");
						return jrw;
					}
				}else{
					jrw.setFlag(false);
					jrw.setMessage("保存失败，部门编码不符合要求");
					return jrw;
				}
			}
		}
		SysManageLog sm=new SysManageLog();
		//判断是新增还是修改
		if(sysGovId==null){
			if(sg.getSysGovUpid()==null){
				sg.setSysGovUpid(0);
			}
			sysGoverService.saveSysGover(sg);
			sm.setSysManageLogOperateType(SysManageLog.INSERT_SYSMANAGElOG);
		}else{
			sysGoverService.updateSysGover(sg);
			sm.setSysManageLogOperateType(SysManageLog.UPDATE_SYSMANAGElOG);
		}
		sm.setSysManageLogMenuName("政府部门管理");
		sm.setSysManageLogResult(true);
		sm.setSysManageLogCount(1);
		sysManageLogService.insertSysManageLogTb(sm, request);
		jrw.setMessage("保存成功");
		return jrw;
	}
	
	/**
	 * 通过政府部门名称查找政府代码
	 * @param name
	 * @return
	 */
	@RequestMapping("/getcode")
	@ResponseBody
	public void getcode(@RequestParam(required=false) String name,
			@RequestParam(required=false) String govType,HttpServletResponse response) throws Exception{
		String code;
		if("0".equals(govType)){
			//查找政府部门代码
			code=sysGoverService.querySysGoverCodeByName(name);
		}else{
			//查找政府类型代码
			code=goverTypeService.queryGoverTypeCodeByName(name);
		}
		Gson gson=new Gson();
		response.getWriter().write(gson.toJson(code));
	}
	
	/**
	 * 通过ID删除政府部门及下级部门
	 * @param id 
	 * @return
	 */
	@RequestMapping("/del")
	@ResponseBody
	public JsonResWrapper del(HttpServletRequest request,@RequestParam(required=false) Integer id) throws Exception{
		JsonResWrapper jrw = new JsonResWrapper();
		SysGover it=sysGoverService.querySysGoverById(id);
		if(id==null || it==null){
			jrw.setFlag(false);
			jrw.setMessage("删除失败，参数缺失");
			return jrw;
		}
		int count=sysGoverService.querySysGoverUpidByCount(id);
		if(count>0){
			jrw.setFlag(false);
			jrw.setMessage("存在下级部门，不允许删除");
			return jrw;
		}
//		sysGoverService.delSysGoverByUpid(id);
		sysGoverService.delSysGoverById(id);
		SysManageLog sm=new SysManageLog();
		sm.setSysManageLogMenuName("政府部门管理");
		sm.setSysManageLogOperateType(SysManageLog.DELECT_SYSMANAGElOG);
		sm.setSysManageLogResult(true);
		sm.setSysManageLogCount(1);
		sysManageLogService.insertSysManageLogTb(sm, request);
		jrw.setMessage("删除成功");
		return jrw;
	}
	
	/**
	 * 导出全部数据EXCEL
	 * @param model
	 * @param 
	 * @return
	 */
	@RequestMapping("/exportAll")
	@ResponseBody
	public void downFile(HttpServletResponse response, HttpServletRequest request,
			@RequestParam(required=false) String name) {
		try{
			
			List<SysGover> list;
			if(name!=null || !"".equals(name)){
				list = sysGoverService.queryTypeAll(name);
			}else{
				list = sysGoverService.queryTypeAll(null);
			}
			String[] rowNames = { "政府部门名称", "政府部门编码", "上级部门名称", "政府类型名称", "备注"};
			String[] propertyNames = { "sysGovName", "sysGovFinancialCode", "sysGovUpname", "sysGovTypeName", "sysOrgNotes"};
			
			if(list!=null){
				// 生成excel
				ExcelExport<SysGover> excelExport = new ExcelExport<>();
				excelExport.setTitle("政府部门管理");
				excelExport.setRowNames(rowNames);
				excelExport.setPropertyNames(propertyNames);
				excelExport.setList(list);;
				String url=excelExport.exportExcel(request,response);
				
				SysManageLog sm=new SysManageLog();
				sm.setSysManageLogMenuName("政府部门管理");
				sm.setSysManageLogOperateType(SysManageLog.EXPORT_SYSMANAGElOG);
				sm.setSysManageLogResult(true);
				sm.setSysManageLogCount(list.size());
				sm.setSysManageLogFile(url);
				sysManageLogService.insertSysManageLogTb(sm, request);
			}
		}catch (Exception e){
			LoggerUtil.error(e);
		}
	}
	
	/**
	 * 导出EXCEL
	 * @param model
	 * @param id 机构ID
	 * @return
	 */
	@RequestMapping("/export")
	@ResponseBody
	public void downLoadFile(HttpServletResponse response, HttpServletRequest request,
							 @RequestParam(required=false) Integer id) {
		try{
			
			SysGover sg = sysGoverService.queryInstitutionsById(id);
			List<SysGover> sysGover=new ArrayList<>();
			sysGover.add(sg);
			int count=sysGoverService.querySysGoverUpidByCount(id);
			if(count>0){
				sysGover.addAll(sysGoverService.querySysGoverByUpid(id));
			}
			String[] rowNames = { "政府部门名称", "政府部门编码", "上级部门名称", "政府类型名称", "备注"};
			String[] propertyNames = { "sysGovName", "sysGovFinancialCode", "sysGovUpname", "sysGovTypeName", "sysOrgNotes"};
			if(sysGover!=null){
				// 生成excel
				ExcelExport<SysGover> excelExport = new ExcelExport<>();
				excelExport.setTitle("政府部门管理");
				excelExport.setRowNames(rowNames);
				excelExport.setPropertyNames(propertyNames);
				excelExport.setList(sysGover);
				String url=excelExport.exportExcel(request,response);
				
				SysManageLog sm=new SysManageLog();
				sm.setSysManageLogMenuName("政府部门管理");
				sm.setSysManageLogOperateType(SysManageLog.EXPORT_SYSMANAGElOG);
				sm.setSysManageLogResult(true);
				sm.setSysManageLogCount(1);
				sm.setSysManageLogFile(url);
				sysManageLogService.insertSysManageLogTb(sm, request);
			}
		}catch (Exception e){
			LoggerUtil.error(e);
		}
	}
	
	/**
	 * 导出EXCEL模板
	 * @param model
	 * @param id 机构ID
	 * @return
	 */
	@RequestMapping("/exportModel")
	@ResponseBody
	public void downLoadFileModel(HttpServletResponse response, HttpServletRequest request) {
			SysGover sg = null;
			String[] rowNames = { "政府部门名称", "政府部门编码", "上级部门名称", "政府类型名称", "备注"};
			String[] propertyNames = { "sysGovName", "sysGovFinancialCode", "sysGovUpname", "sysGovTypeName", "sysOrgNotes"};
				// 生成excel
				ExcelExport<SysGover> excelExport = new ExcelExport<>();
				excelExport.setTitle("政府部门模板");
				excelExport.setRowNames(rowNames);
				excelExport.setPropertyNames(propertyNames);
				excelExport.setObj(sg);
				excelExport.exportObjctExcel(response);
				
				SysManageLog sm=new SysManageLog();
				sm.setSysManageLogMenuName("政府部门管理");
				sm.setSysManageLogOperateType(SysManageLog.EXPORT_SYSMANAGElOG);
				sm.setSysManageLogResult(true);
				sm.setSysManageLogCount(1);
				sysManageLogService.insertSysManageLogTb(sm, request);
	}
	
	/**
	 * 导入数据
	 * @param model
	 * @param 
	 * @return
	 */
	@RequestMapping("/excelReader")
	public String upload(MultipartFile file, HttpServletRequest request, Model model) throws Exception{
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		SysOrg ss = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		int sysAreaId=ss.getSys_area_id();
		ExcelReaderOrg<SysGover> excelReader = new ExcelReaderOrg<>();
		excelReader.setT(new SysGover());
		String[] rowNames = { "政府部门名称", "政府部门编码", "上级部门名称", "政府类型名称", "备注"};
		String[] propertyNames = { "sysGovName", "sysGovFinancialCode", "sysGovUpname", "sysGovTypeName", "sysOrgNotes"};
		excelReader.setRowNames(rowNames);
		excelReader.setPropertyNames(propertyNames);
		String fileName=UpLoadFile.upLoadFile(file);
		List<SysGover> list=excelReader.excelReader(request,fileName);
		List<String> msgString=new ArrayList<String>();
		List<String> msgStr=new ArrayList<String>();
		if(list!=null){
			Integer count=0;
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getSysGovName()==null){
					msgString.add("第"+ (i + 2) +"行1列，政府部门名称不能为空，请确认后添加");
					count=count+1;
				}
				if(list.get(i).getSysGovFinancialCode()==null){
					msgString.add("第"+ (i + 2) +"行2列，政府部门编码不能为空，请确认后添加");
					count=count+1;
				}
				if(list.get(i).getSysGovTypeName()==null){
					msgString.add("第"+ (i + 2) +"行4列，政府类型名称不能为空，请确认后添加");
					count=count+1;
				}
			}
			if(count>0){
				model.addAttribute("msgString",msgString);
				return "forward:list.jhtml";
			}
			Integer num=0;
			for (int i = 0; i < list.size(); i++) {
				GoverType gt=goverTypeService.queryGoverTypeIdByName(list.get(i).getSysGovTypeName());
				if(gt==null){
					msgStr.add("第"+ (i + 2) +"行4列，无此政府类型，请确认后添加");
					num=num+1;
				}
				if(list.get(i).getSysGovFinancialCode().length()==14){
					String govCode=list.get(i).getSysGovFinancialCode().substring(0, 4);
					if(!govCode.equals(gt.getSysGovTypeCode())){
						msgStr.add("第"+ (i + 2) +"行2列，政府部门编码与政府类型不匹配，请确认后添加");
						num=num+1;
					}
				}else{
					msgStr.add("第"+ (i + 2) +"行2列，政府部门编码为14位，请确认后添加");
					num=num+1;
				}
				SysGover s;
				if(!"".equals(list.get(i).getSysGovUpname())){
					String name=list.get(i).getSysGovUpname();
					s=sysGoverService.querySysGoverIdByName(name);
					if(s!=null){
						if(list.get(i).getSysGovFinancialCode().length()==14){
							String upCode=s.getSysGovFinancialCode().substring(0, 8);
							String code=list.get(i).getSysGovFinancialCode().substring(0, 8);
							if(!upCode.equals(code)){
								msgStr.add("第"+ (i + 2) +"行2列，政府部门编码与上级编码不匹配，请确认后添加");
								num=num+1;
							}
						}else{
							msgStr.add("第"+ (i + 2) +"行2列，政府部门编码与上级编码不匹配，请确认后添加");
							num=num+1;
						}
					}else{
						msgStr.add("第"+ (i + 2) +"行3列，无此上级政府部门，请确认后添加");
						num=num+1;
					}
				}
				List<SysGover> so=sysGoverService.querySysGoverByCodeAndName(list.get(i).getSysGovName(), null);
				if(!so.isEmpty()&&so.size()>0){
					msgStr.add("第"+ (i + 2) +"行1列，已有此政府部门名称，请确认后添加");
					num=num+1;
				}
				List<SysGover> se=sysGoverService.querySysGoverByCodeAndName(null, list.get(i).getSysGovFinancialCode());
				if(!se.isEmpty()&&se.size()>0){
					msgStr.add("第"+ (i + 2) +"行2列，已有此政府部门编码，请确认后添加");
					num=num+1;			
				}
			}
			if(num>0){
				model.addAttribute("msgStr",msgStr);
				return "forward:list.jhtml";
			}
			for (int i = 0; i < list.size(); i++) {
				SysGover sg=new SysGover();
				GoverType gt=goverTypeService.queryGoverTypeIdByName(list.get(i).getSysGovTypeName());
				SysGover sys;
				if(!"".equals(list.get(i).getSysGovUpname())){
					String name=list.get(i).getSysGovUpname();
					sys=sysGoverService.querySysGoverIdByName(name);
					sg.setSysGovUpid(sys.getSysGovId());
				}
				sg.setSysGovName(list.get(i).getSysGovName());
				sg.setSysGovFinancialCode(list.get(i).getSysGovFinancialCode());
				if(sg.getSysGovUpid()==null){
					sg.setSysGovUpid(0);
				}
				sg.setSysGovTypeId(gt.getSysGovTypeId());
				sg.setSysDicId(42);
				sg.setSysAreaId(sysAreaId);
				sg.setSysOrgNotes(list.get(i).getSysOrgNotes());
				sysGoverService.saveSysGover(sg);
			}
			SysManageLog sm=new SysManageLog();
			sm.setSysManageLogMenuName("政府部门管理");
			sm.setSysManageLogOperateType(SysManageLog.IMPORT_SYSMANAGElOG);
			sm.setSysManageLogResult(true);
			sm.setSysManageLogCount(list.size());
			sysManageLogService.insertSysManageLogTb(sm, request);
			request.setAttribute("msg", "导入成功");
		}
		return "forward:list.jhtml";
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
	public List<SysGover> sysGoverByName(HttpServletRequest request,@RequestParam(required=false) String name) throws Exception{
		SysGover sg=new SysGover();
		sg.setSysGovName(name);
		List<SysGover> sos = sysGoverService.querySysGoverByNameList(sg);
		return sos;
	}

}
