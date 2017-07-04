package com.workmanagement.controller;

import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.DicExchangeLast;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.ReportIndexError;
import com.workmanagement.model.SysClassFyModel;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.service.DicContentService;
import com.workmanagement.service.SysClassFyService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.ExcelExportOrg;
import com.workmanagement.util.ExcelReaderOrg;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.SettingUtils;
import com.workmanagement.util.UpLoadFile;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 行业维护
 * @author xiehao
 */
@Controller
@RequestMapping("/admin/sysClassfy")
public class SysClassFyController {
	
	@Autowired
	private SysClassFyService sysClassFyService;
	
	@Autowired
	private DicContentService dicContentService;
	@Autowired
	private SysManageLogService sysManageLogService;
	
	/**
	 * 添加管理日志
	 * @param logCount  操作影响行数
	 * @param operateType 操作类型（增删改）
	 * @param logResult  操作结果 0 失败 1 成功
	 * @param sysManageLogNewValue   修改后的值
	 * @param sysManageLogOldValue   修改前的值
	 */
	public void manageLog(HttpServletRequest request,Integer logCount,Integer operateType,boolean logResult,String sysManageLogNewValue,String sysManageLogOldValue,String sysManageLogFile){
		SysManageLog sysManageLog=new SysManageLog();
		sysManageLog.setSysManageLogMenuName("行业维护");
		sysManageLog.setSysManageLogCount(logCount);
		sysManageLog.setSysManageLogOperateType(operateType);
		sysManageLog.setSysManageLogResult(logResult);
		sysManageLog.setSysManageLogNewValue(sysManageLogNewValue);
		sysManageLog.setSysManageLogOldValue(sysManageLogOldValue);
		sysManageLog.setSysManageLogFile(sysManageLogFile);
		sysManageLogService.insertSysManageLogTb(sysManageLog,request);
	}
	/**
	 * 
	 * 查询所有行业
	 * @param request
	 * @param model
	 * @param currency
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model, @RequestParam(required = false) String currency, @RequestParam(required = false)String name,Integer url){
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		if (!StringUtils.isBlank(currency)) {
			param.put("currency", currency);
		}
//		if(name!=null &&!"".equals(name)){
//			List<SysClassFyModel> sysClassFyModel=sysClassFyService.isSysIndNameByAll1(name,ps); 
//			List<SysClassFyModel> sysClassFyModel1=sysClassFyService.isSysIndNameByAll1(name,null); 
//			if(url==null){
//				String sql1="SELECT * FROM SYS_INDUSTRY_TB  WHERE SYS_INDUSTRY_NAME LIKE '%"+name+"%'";
//				String sql = StringUtils.replace(sql1, " ", "|");
//				//添加管理日志
//				SysManageLog sysManageLog=new SysManageLog();
//				sysManageLog.setSysManageLogMenuName("行业维护");
//				sysManageLog.setSysManageLogCount(sysClassFyModel1.size());
//				sysManageLog.setSysManageLogOperateType(4);
//				sysManageLog.setSysManageLogResult(true);
//				sysManageLog.setSysManageLogQueryUserCondition("通过行业名:"+name+"进行查询");
//				sysManageLog.setSysManageLogUrl("/admin/sysClassfy/list.jhtml?name="+name+"&url=1");
//				sysManageLog.setSysManageLogQuerySql(sql);
//				sysManageLogService.insertSysManageLogTb(sysManageLog,request);
//				model.addAttribute("url", 1);
//			}else{
//				if(url==1){
//					model.addAttribute("url", 1);
//				}
//			}
//			model.addAttribute("sysClassFyModel", sysClassFyModel);
//			model.addAttribute("sysIndustryName", name);
//			return "sysClassfy/list" ;
//		}
		
		
		if(name!=null &&!"".equals(name)){
			//判断取出来的值是否是汉字，是汉字则通过名称去查询
			Pattern pattern = Pattern.compile(".*[\\u4e00-\\u9faf].*");
	        Matcher isNum = pattern.matcher(name);
	        if(isNum.matches()){
	        	List<SysClassFyModel> sysClassFyModel=sysClassFyService.isSysIndNameByAll1(name.trim(),ps); 
	        	List<SysClassFyModel> sysClassFyModel1=sysClassFyService.isSysIndNameByAll1(name.trim(),null); 
	        	model.addAttribute("sysClassFyModel", sysClassFyModel);
	        	model.addAttribute("sysIndustryName", name);
	        	if(url==null){
					String sql1="SELECT * FROM SYS_INDUSTRY_TB  WHERE SYS_INDUSTRY_NAME LIKE '%"+name+"%'";
					String sql = StringUtils.replace(sql1, " ", "|");
					//添加管理日志
					SysManageLog sysManageLog=new SysManageLog();
					sysManageLog.setSysManageLogMenuName("行业维护");
					sysManageLog.setSysManageLogCount(sysClassFyModel1.size());
					sysManageLog.setSysManageLogOperateType(4);
					sysManageLog.setSysManageLogResult(true);
					sysManageLog.setSysManageLogQueryUserCondition("通过行业名:"+name+"进行查询");
					sysManageLog.setSysManageLogUrl("/admin/sysClassfy/classifyInfo.jhtml?name="+name+"&url=1");
					sysManageLog.setSysManageLogQuerySql(sql);
					sysManageLogService.insertSysManageLogTb(sysManageLog,request);
					model.addAttribute("url", 1);
				}
	        	return "sysClassfy/list"  ;
	        }else{
	        	List<SysClassFyModel> sysClassFyModel=sysClassFyService.queryModelByCode1(name.toUpperCase().trim(),ps);
	        	List<SysClassFyModel> sysClassFyModel1=sysClassFyService.queryModelByCode1(name.toUpperCase().trim(),null);
	        	model.addAttribute("sysClassFyModel", sysClassFyModel);
	        	model.addAttribute("sysIndustryName", name);
	        	if(url==null){
					String sql1="SELECT * FROM SYS_INDUSTRY_TB  WHERE SYS_INDUSTRY_CODE LIKE '%"+name+"%'";
					String sql = StringUtils.replace(sql1, " ", "|");
					//添加管理日志
					SysManageLog sysManageLog=new SysManageLog();
					sysManageLog.setSysManageLogMenuName("行业维护");
					sysManageLog.setSysManageLogCount(sysClassFyModel1.size());
					sysManageLog.setSysManageLogOperateType(4);
					sysManageLog.setSysManageLogResult(true);
					sysManageLog.setSysManageLogQueryUserCondition("通过行业编码:"+name+"进行查询");
					sysManageLog.setSysManageLogUrl("/admin/sysClassfy/classifyInfo.jhtml?name="+name+"&url=1");
					sysManageLog.setSysManageLogQuerySql(sql);
					sysManageLogService.insertSysManageLogTb(sysManageLog,request);
					model.addAttribute("url", 1);
				}
	        	return "sysClassfy/list"  ;
	        }
		}
		List<SysClassFyModel> sysClassFyModel=sysClassFyService.queryAllSysClassFy(ps);
		model.addAttribute("sysClassFyModel", sysClassFyModel);
//		model.addAttribute("url", 1);
		return "sysClassfy/list" ;
	}
	
	
	/**
	 * 数据类型查询中的行业类型查询
	 * @param request
	 * @param model
	 * @param currency
	 * @param name
	 * @return
	 */
	@RequestMapping("/classifyInfo")
	public String listmohu(HttpServletRequest request, Model model, @RequestParam(required = false) String currency, @RequestParam(required = false)String name,Integer url, @RequestParam(required = false)String qb){
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		if (!StringUtils.isBlank(currency)) {
			param.put("currency", currency);
		}
		if(name!=null &&!"".equals(name)){
			//判断取出来的值是否是汉字，是汉字则通过名称去查询
			Pattern pattern = Pattern.compile(".*[\\u4e00-\\u9faf].*");
	        Matcher isNum = pattern.matcher(name);
	        if(isNum.matches()){
	        	List<SysClassFyModel> sysClassFyModel=sysClassFyService.isSysIndNameByAll1(name.trim(),ps); 
	        	List<SysClassFyModel> sysClassFyModel1=sysClassFyService.isSysIndNameByAll1(name.trim(),null); 
	        	model.addAttribute("sysClassFyModel", sysClassFyModel);
	        	model.addAttribute("sysIndustryName", name);
	        	model.addAttribute("qb", 2);
	        	if(url==null){
					String sql1="SELECT * FROM SYS_INDUSTRY_TB  WHERE SYS_INDUSTRY_NAME LIKE '%"+name+"%'";
					String sql = StringUtils.replace(sql1, " ", "|");
					//添加管理日志
					SysManageLog sysManageLog=new SysManageLog();
					sysManageLog.setSysManageLogMenuName("行业维护");
					sysManageLog.setSysManageLogCount(sysClassFyModel1.size());
					sysManageLog.setSysManageLogOperateType(4);
					sysManageLog.setSysManageLogResult(true);
					sysManageLog.setSysManageLogQueryUserCondition("通过行业名:"+name+"进行查询");
					sysManageLog.setSysManageLogUrl("/admin/sysClassfy/classifyInfo.jhtml?name="+name+"&url=1");
					sysManageLog.setSysManageLogQuerySql(sql);
					sysManageLogService.insertSysManageLogTb(sysManageLog,request);
					model.addAttribute("url", 1);
				}
	        	return "dataTypeQuery/classifyInfo" ;
	        }else{
	        	List<SysClassFyModel> sysClassFyModel=sysClassFyService.queryModelByCode1(name.toUpperCase().trim(),ps);
	        	List<SysClassFyModel> sysClassFyModel1=sysClassFyService.queryModelByCode1(name.toUpperCase().trim(),null);
	        	model.addAttribute("sysClassFyModel", sysClassFyModel);
	        	model.addAttribute("sysIndustryName", name);
	        	model.addAttribute("qb", 2);
	        	if(url==null){
					String sql1="SELECT * FROM SYS_INDUSTRY_TB  WHERE SYS_INDUSTRY_CODE LIKE '%"+name+"%'";
					String sql = StringUtils.replace(sql1, " ", "|");
					//添加管理日志
					SysManageLog sysManageLog=new SysManageLog();
					sysManageLog.setSysManageLogMenuName("行业维护");
					sysManageLog.setSysManageLogCount(sysClassFyModel1.size());
					sysManageLog.setSysManageLogOperateType(4);
					sysManageLog.setSysManageLogResult(true);
					sysManageLog.setSysManageLogQueryUserCondition("通过行业编码:"+name+"进行查询");
					sysManageLog.setSysManageLogUrl("/admin/sysClassfy/classifyInfo.jhtml?name="+name+"&url=1");
					sysManageLog.setSysManageLogQuerySql(sql);
					sysManageLogService.insertSysManageLogTb(sysManageLog,request);
					model.addAttribute("url", 1);
				}
	        	return "dataTypeQuery/classifyInfo" ;
	        }
		}
		if(StringUtil.isNotEmpty(qb)){
			List<SysClassFyModel> sysClassFyModel=sysClassFyService.queryAllSysClassFy(ps);
			model.addAttribute("sysClassFyModel", sysClassFyModel);
		}
		return "dataTypeQuery/classifyInfo" ;
	}
	
	/**
	 * 导出全部数据EXCEL
	 * @param model
	 * @param 
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/exportAll")
	@ResponseBody
	public void downFile(HttpServletResponse response, HttpServletRequest request,@RequestParam(required = false) String name,@RequestParam(required = false)Integer qb) throws UnsupportedEncodingException {
		int nameLength=name.length();
		PageSupport ps = PageSupport.initPageSupport(request);
		String[] rowNames = { "行业分类代码", "类别名称", "备注"};
		String[] propertyNames = {"sysIndustryCode","sysIndustryName","sysIndustryNotes"};
		List<SysClassFyModel> list=new ArrayList<>();
		if(nameLength>0){
			if(StringUtil.isNotEmpty(name)){
//				if(name!=null &&!"".equals(name)){
				//判断取出来的值是否是汉字，是汉字则通过名称去查询
				Pattern pattern = Pattern.compile(".*[\\u4e00-\\u9faf].*");
		        Matcher isNum = pattern.matcher(name);
		        if(isNum.matches()){
		        	list=sysClassFyService.isSysIndNameByAll1(name.trim(),null); 
		        }else{
		        	list=sysClassFyService.queryModelByCode1(name.toUpperCase().trim(),null);
		        }
			}
//			list=sysClassFyService.isSysIndNameByAll1(name,null);
			if(list!=null){
				// 生成excel
				ExcelExport<SysClassFyModel> excelExport = new ExcelExport<SysClassFyModel>();
				excelExport.setTitle("行业管理");
				excelExport.setRowNames(rowNames);
				excelExport.setPropertyNames(propertyNames);
				excelExport.setList(list);
				String url=excelExport.exportExcel(request,response);
				//添加管理日志
				manageLog(request,list.size(),6,true,null,null,url);
			}
		}else{
			if(!"1".equals(qb)){
				list= sysClassFyService.queryAllSysClassFy1();
				if(list!=null){
					// 生成excel
					ExcelExport<SysClassFyModel> excelExport = new ExcelExport<SysClassFyModel>();
					excelExport.setTitle("行业管理");
					excelExport.setRowNames(rowNames);
					excelExport.setPropertyNames(propertyNames);
					excelExport.setList(list);
					String url=excelExport.exportExcel(request,response);
					//添加管理日志
					manageLog(request,list.size(),6,true,null,null,url);
				}
			}
		}
	}
	
	
	
	@RequestMapping("/exportAll1")
	@ResponseBody
	public void downFile1(HttpServletResponse response, HttpServletRequest request,@RequestParam(required = false) String name,@RequestParam(required = false)Integer qb) throws UnsupportedEncodingException {
		int nameLength=name.length();
		PageSupport ps = PageSupport.initPageSupport(request);
		String[] rowNames = { "行业分类代码", "类别名称"};
		String[] propertyNames = {"sysIndustryCode","sysIndustryName"};
		List<SysClassFyModel> list=new ArrayList<>();
		if(nameLength!=1){
			if(StringUtil.isNotEmpty(name)){
//				if(name!=null &&!"".equals(name)){
				//判断取出来的值是否是汉字，是汉字则通过名称去查询
				Pattern pattern = Pattern.compile(".*[\\u4e00-\\u9faf].*");
		        Matcher isNum = pattern.matcher(name);
		        if(isNum.matches()){
		        	list=sysClassFyService.isSysIndNameByAll1(name.trim(),null); 
		        }else{
		        	list=sysClassFyService.queryModelByCode1(name.toUpperCase().trim(),null);
		        }
			}
//			list=sysClassFyService.isSysIndNameByAll1(name,null);
			if(list!=null){
				// 生成excel
				ExcelExport<SysClassFyModel> excelExport = new ExcelExport<SysClassFyModel>();
				excelExport.setTitle("行业管理");
				excelExport.setRowNames(rowNames);
				excelExport.setPropertyNames(propertyNames);
				excelExport.setList(list);
				String url=excelExport.exportExcel(request,response);
				//添加管理日志
				manageLog(request,list.size(),6,true,null,null,url);
			}
		}else{
			if(!"1".equals(qb)){
//				list= new ArrayList<>();
				list=sysClassFyService.queryAllSysClassFy1();
				if(list!=null){
					// 生成excel
					ExcelExport<SysClassFyModel> excelExport = new ExcelExport<SysClassFyModel>();
					excelExport.setTitle("行业管理");
					excelExport.setRowNames(rowNames);
					excelExport.setPropertyNames(propertyNames);
					excelExport.setList(list);
					String url=excelExport.exportExcel(request,response);
					//添加管理日志
					manageLog(request,list.size(),6,true,null,null,url);
				}
			}
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
		String[] rowNames = { "行业分类代码", "类别名称", "备注"};
		String[] propertyNames = {"sysIndustryCode","sysIndustryName","sysIndustryNotes"};
		if(id!=null){
			SysClassFyModel sysClassFyModel = sysClassFyService.queryAllCodeById(id);
			// 生成excel
//			ExcelExportOrg<SysClassFyModel> excelExport = new ExcelExportOrg<>();
//			ExcelExport<SysClassFyModel> excelExport = new ExcelExport<>();
//			excelExport.setTitle("行业管理");
//			excelExport.setRowNames(rowNames);
//			excelExport.setPropertyNames(propertyNames);
//			excelExport.setObj(sysClassFyModel);
//			excelExport.exportObjctExcel(response);
//			String url=excelExport.exportExcel(request,response);
			//添加管理日志
//			manageLog(1,6,true,null,null,url);
//			manageLog(1,6,true,null,null,null);
			List list=new ArrayList<>();
			list.add(sysClassFyModel);
			// 生成excel
			ExcelExport<SysClassFyModel> excelExport = new ExcelExport<SysClassFyModel>();
			excelExport.setTitle("行业管理");
			excelExport.setRowNames(rowNames);
			excelExport.setPropertyNames(propertyNames);
			excelExport.setList(list);
			String url=excelExport.exportExcel(request,response);
			manageLog(request,1,6,true,null,null,url);
//			System.out.println("======"+url);
		}
	}
	/**
	 * 添加行业数据
	 * @param model
	 * @param request
	 * @param sysClassFyModel
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(Model model,HttpServletRequest request,SysClassFyModel sysClassFyModel,Integer id){
		PageSupport ps = PageSupport.initPageSupport(request);
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		JsonResWrapper jrw = new JsonResWrapper();
		if(id!=null){
			SysClassFyModel sysClassFy =sysClassFyService.queryAllCodeById(id);
			//查询所选行业对应编码
			String code=sysClassFy.getSysIndustryCode();
			//将编码的后两位去掉，查询父行业
			if(code.length()>=3){
				String newCode=code.substring(0, code.length()-2);
				SysClassFyModel sysClassFyParent= sysClassFyService.queryModelByCode(newCode);
				//通过编码取出来的上级行业，如果存在上级行业，则将上级行业的信息进行返回，如果没有上级，则返回本身
				if(sysClassFyParent!=null){
					sysClassFy.setParentName(sysClassFyParent.getSysIndustryName());
				}else{
					sysClassFy.setParentName(sysClassFy.getSysIndustryName());
				}
			}else{
				SysClassFyModel sysClassFyParent= sysClassFyService.queryModelByCode(code);
				sysClassFy.setParentName(sysClassFyParent.getSysIndustryName());
			}
			model.addAttribute("sysClassFy", sysClassFy);
			return "sysClassfy/edit";
		}
		List<SysClassFyModel> sysClassFy=sysClassFyService.queryAllSysClassFy1();
		//点击增加按钮，把行业类别列表进行显示
		model.addAttribute("sysClassFy", sysClassFy);
		return "sysClassfy/add";
	}
	//添加行业信息
	@RequestMapping(value="/getcode")
	public void getcode(HttpServletResponse response, @RequestParam(required=false) Integer hyid) throws IOException{
		SysClassFyModel sysClassFy=sysClassFyService.queryCodeById(hyid);
		String hybm=sysClassFy.getSysIndustryCode();
		Gson gson=new Gson();
		response.getWriter().write(hybm);
	}
	
	/*
	 * 
	 * 添加行业信息
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonResWrapper save(Model model,SysClassFyModel sysClassFyModel, HttpServletRequest request) throws Exception {
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();
		JsonResWrapper jrw = new JsonResWrapper();
		if (sysClassFyModel.getSysIndustryCode() == null || sysClassFyModel.getSysIndustryName() == null) {
			jrw.setFlag(false);
			jrw.setMessage("操作失败，请填写完整");
			//添加管理日志
			manageLog(request,1,1,false,null,null,null);
			return jrw;
		}
		//获取页面传入后台的名字
		String name=sysClassFyModel.getSysIndustryName();
		//获取页面传入的行业代码
		String code=sysClassFyModel.getSysIndustryCode();
		//通过字符串截取需要的字符串
		String named = name.substring(name.indexOf(",") + 1);
		/**
		 * 修改
		 */
		//判断页面传入数据是否有id，存在id，则对数据修改，没有id则对数据进行新增
		if(sysClassFyModel.getSysIndustryId()!=null){
			SysClassFyModel s=sysClassFyService.queryAllCodeById(sysClassFyModel.getSysIndustryId());
			if(named.equals(s.getSysIndustryName())&&code.equals(s.getSysIndustryCode())){
				
			}else{
				if(!named.equals(s.getSysIndustryName())){
					SysClassFyModel sysClassFy=sysClassFyService.isSysIndNameByAll(named);
					if(sysClassFy!=null ){
						if(sysClassFy.getSysIndustryName()!=null){
							jrw.setFlag(false);
							jrw.setMessage("行业类已重复，请从新输入");
							//添加管理日志
							manageLog(request,1,1,false,null,null,null);
							return jrw;
						}
					}
				}else{
					SysClassFyModel sysClassFyCode=sysClassFyService.queryModelByCode(code);
					if(sysClassFyCode!=null ){
						if(sysClassFyCode.getSysIndustryCode()!=null){
							jrw.setFlag(false);
							jrw.setMessage("行业代码已重复，请从新输入");
							//添加管理日志
							manageLog(request,1,1,false,null,null,null);
							return jrw;
						}
					}
				}
			}
			SysClassFyModel sys=new SysClassFyModel();
			sys.setSysIndustryName(named);
			sys.setSysDicId(61);
			sys.setSysIndustryId(sysClassFyModel.getSysIndustryId());
			sys.setSysIndustryCode(sysClassFyModel.getSysIndustryCode());
			sys.setSysIndustryNotes(sysClassFyModel.getSysIndustryNotes());
			sysClassFyService.updateExchenge(sys);
			//将数据进行返回
			model.addAttribute("sysClassFyModel", sysClassFyModel);
			//添加管理日志
			manageLog(request,1,3,true,sysClassFyModel.getSysIndustryName()+","+sysClassFyModel.getSysIndustryCode(),s.getSysIndustryName()+","+s.getSysIndustryCode(),null);
			jrw.setMessage("操作成功");
			return jrw;
		}
		//通过添加名称去数据库查询有没有对应的数据，没有才进行添加，有就不添加
		SysClassFyModel sysClassFy=sysClassFyService.isSysIndNameByAll(named);
		SysClassFyModel sysClassFyCode=sysClassFyService.queryModelByCode(code);
		
		if(sysClassFy!=null ){
			if(sysClassFy.getSysIndustryName()!=null){
				jrw.setFlag(false);
				jrw.setMessage("行业类已重复，请从新输入");
				//添加管理日志
				manageLog(request,1,1,false,null,null,null);
				return jrw;
			}
		}
		if(sysClassFyCode!=null ){
			if(sysClassFyCode.getSysIndustryCode()!=null){
				jrw.setFlag(false);
				jrw.setMessage("行业代码已重复，请从新输入");
				//添加管理日志
				manageLog(request,1,1,false,null,null,null);
				return jrw;
			}
		}

		
	
		//将正确数据添加到数据库
		SysClassFyModel sys=new SysClassFyModel();
		sys.setSysIndustryName(named);
		sys.setSysDicId(61);
		sys.setSysIndustryCode(sysClassFyModel.getSysIndustryCode());
		sys.setSysIndustryNotes(sysClassFyModel.getSysIndustryNotes());
		sysClassFyService.insertExchenge(sys);
		//将数据进行返回
		model.addAttribute("sysClassFyModel", sysClassFyModel);
		//添加管理日志
		manageLog(request,1,1,true,sysClassFyModel.getSysIndustryName(),null,null);
		jrw.setMessage("操作成功");
		return jrw;
	}
	//删除行业信息
	@RequestMapping(value="/del")
	@ResponseBody
	public JsonResWrapper del(Model model,Integer id,String name, HttpServletRequest request){
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();
		JsonResWrapper jrw = new JsonResWrapper();
		/*
		 * 通过id查询行业编码，然后在根据编码去查询以他开头的数据，没有则表明没有下级关联，可以删除数据，如果有，则不对数据进行删除
		 * 
		 */
		//通过id去查询行业编码
		SysClassFyModel sysClassFy=sysClassFyService.queryCodeById(id);
		//通过id去查询行业信息
		SysClassFyModel sysClass=sysClassFyService.queryAllCodeById(id);
		//通过行业编码去查询是否有关联数据
		List<SysClassFyModel> sysClassFyModel=sysClassFyService.queryClassFyModelByCode(sysClassFy.getSysIndustryCode(),id);
		if(sysClassFyModel!=null && sysClassFyModel.size()>0){
			jrw.setFlag(false);
			jrw.setMessage("该行业被引用，不允许删除");
			//添加管理日志
			manageLog(request,1,2,false,null,null,null);
			return jrw;
		}
		sysClassFyService.delClassFyById(id);
		//添加管理日志
		manageLog(request,1,2,true,null,sysClass.getSysIndustryName(),null);
		jrw.setMessage("操作成功");
		return jrw;
	}
	//导出模板
	@RequestMapping(value = "downLoad")
	public void downLoadFile(HttpServletRequest request, HttpServletResponse response) {
		// 生成
		ExcelExport<DicExchangeLast> excelExport = new ExcelExport<>();
		excelExport.setTitle("行业导入模板");
		String[] rowNames = { "行业分类代码","类别名称","备注"};
		excelExport.setRowNames(rowNames);
		String url=excelExport.exportExcel(request, response);
		//添加管理日志
		manageLog(request,1,6,true,null,null,url);
	}
	/**
	 * 
	 * @param model
	 * @param file
	 * @param request
	 * @param response
	 * @return  hyName  行业名
	 * @throws Exception
	 */
	// 导入excel
	@RequestMapping(value = "/upLoad",method = RequestMethod.POST)
	public String upLoadFile(Model model,@RequestParam(required=false) MultipartFile file, HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();
		
		ExcelReaderOrg<SysClassFyModel> excelReader = new ExcelReaderOrg<>();
		excelReader.setRowNames(new String[] { "行业分类代码","类别名称","备注" });
		excelReader.setPropertyNames(new String[] { "sysIndustryCode","sysIndustryName","sysIndustryNotes"});
		excelReader.setT(new SysClassFyModel());
		String fileName=UpLoadFile.upLoadFile(file);
		List< SysClassFyModel> list = excelReader.excelReader(request,fileName);
//		String originalFilename = file.getOriginalFilename();
//		String savePath = SettingUtils.getCommonSetting("upload.file.temp.path");
//		File myfile = new File(savePath);
//		if (!myfile.exists()) {
//			myfile.mkdirs();
//		}
//		// 新的名称
//		String newFileName = "/" + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
		
		//存所有错误信息的list
		List<String> msgString=new ArrayList<String>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				SysClassFyModel sysClassFyModel=list.get(i);
				boolean isEquals=false;
				String str=sysClassFyModel.getSysIndustryCode().substring(0, 1);
				if((str.charAt(0) <= 'Z' && str.charAt(0) >= 'A')){
					isEquals=true;
				}
				if (isEquals==false) {
					msgString.add("第"+ (i + 2) +"行1列，行业开始字符应该为大写字母，请确认后添加");
				}
				//行业名不能为空
				isEquals=false;
				String hyName=sysClassFyModel.getSysIndustryName();
				if(!StringUtils.isBlank(hyName)){
					isEquals=true;
				}
				if (isEquals==false) {
					msgString.add("第"+ (i + 2) +"行2列，行业名不能为空");
				}
				
				isEquals=false;
				//通过行业编码去数据库查询是否重复
				SysClassFyModel sysClass=sysClassFyService.queryModelByCode(sysClassFyModel.getSysIndustryCode());
				//如果根据传入编码取出有数据，就不让他执行，如果没有，则让他执行后面的操作
				if(sysClass==null){
					isEquals=true;
				}
				if (isEquals==false) {
					msgString.add("第"+ (i + 2) +"行1列，该行业编码已经存在，请确认后添加");
				}
				isEquals=false;
				//通过传入行业名去数据库查询是否存在数据，如果存在，则不进行添加。不存在，在进行添加
				Integer sysClassfy=sysClassFyService.isSysIndustryName(sysClassFyModel.getSysIndustryName());
				if(sysClassfy==0){
					isEquals=true;
				}
				if (isEquals==false) {
					msgString.add("第"+ (i + 2) +"行1列，该行业名已经存在，请确认后添加");
				}
			}
			if(msgString.size()>0){
				model.addAttribute("msgString",msgString);
				//添加管理日志
				manageLog(request,msgString.size(),5,false,null,null,null);
				return "forward:list.jhtml";
			}
			//存所有错误信息的list
			List<String> msgStr=new ArrayList<String>();
			//查询表中的重复数据，如果存在不能导入数据库
			for (int j = 0; j < list.size(); j++) {
				for (int j2 = j+1; j2 < list.size(); j2++) {
					if(list.get(j).getSysIndustryName().equals(list.get(j2).getSysIndustryName())||list.get(j).getSysIndustryCode().equals(list.get(j2).getSysIndustryCode())){
						msgStr.add("第"+ (j + 2) +"行数据和第"+(j2+2)+"行数据重复，请确认后添加");
					}
				}
			}
			if(msgStr.size()>0){
				model.addAttribute("msgStr",msgStr);
				//添加管理日志
				manageLog(request,msgStr.size(),5,false,null,null,null);
				return "forward:list.jhtml";
			}
			
			
			// 导入数据库
			for (SysClassFyModel sysClassFyModel : list) {
				SysClassFyModel mysysClassFyModel=new SysClassFyModel();
				mysysClassFyModel.setSysDicId(61);
				String name=sysClassFyModel.getSysIndustryName();
				mysysClassFyModel.setSysIndustryName(name);
				mysysClassFyModel.setSysIndustryCode(sysClassFyModel.getSysIndustryCode());
				mysysClassFyModel.setSysIndustryNotes(sysClassFyModel.getSysIndustryNotes());
				sysClassFyService.insertExchenge(mysysClassFyModel);
				request.setAttribute("msg", "操作成功");
			}
			//添加管理日志
			manageLog(request,list.size(),5,true,null,null,fileName);
		}
		return "forward:list.jhtml";
	}
}
