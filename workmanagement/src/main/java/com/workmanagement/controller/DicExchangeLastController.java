 package com.workmanagement.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.DicExchangeLast;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.ReportIndexError;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysClassFyModel;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.service.DicContentService;
import com.workmanagement.service.DicExchangeLastService;
import com.workmanagement.service.DicService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.ExcelExportOrg;
import com.workmanagement.util.ExcelReaderTwoDic;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;
import com.workmanagement.util.UpLoadFile;




/**
 * 汇率维护
 * 
 * @author xiehao
 */
@Controller
@RequestMapping("/admin/dicExchangeLast")
public class DicExchangeLastController {

	@Autowired
	private DicExchangeLastService dicExchangeLastService;
	@Autowired
	private DicContentService dicContentService;
	@Autowired
	private DicService dicService;
	@Autowired
	private SysManageLogService sysManageLogService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private SysAreaService sysAreaService;
	/**
	 * 添加管理日志
	 * @param logCount  操作影响行数
	 * @param operateType 操作类型（增删改）
	 * @param logResult  操作结果 0 失败 1 成功
	 * @param sysManageLogNewValue   修改后的值
	 * @param sysManageLogOldValue   修改前的值
	 * @param sysManageLogQuerySql   搜索sql
	 * @param sysManageLogUrl   跳转url
	 */
	public void manageLog(HttpServletRequest request,Integer logCount,Integer operateType,boolean logResult,String sysManageLogNewValue,String sysManageLogOldValue,String sysManageLogFile,String sysManageLogQuerySql){
		SysManageLog sysManageLog=new SysManageLog();
		sysManageLog.setSysManageLogMenuName("汇率维护");
		sysManageLog.setSysManageLogCount(logCount);
		sysManageLog.setSysManageLogOperateType(operateType);
		sysManageLog.setSysManageLogResult(logResult);
		sysManageLog.setSysManageLogNewValue(sysManageLogNewValue);
		sysManageLog.setSysManageLogOldValue(sysManageLogOldValue);
		sysManageLog.setSysManageLogFile(sysManageLogFile);
		sysManageLog.setSysManageLogQuerySql(sysManageLogQuerySql);
		sysManageLogService.insertSysManageLogTb(sysManageLog,request);
	}
		@RequestMapping("/batchexport")
		public String batchexport(HttpServletRequest request, Model model,@RequestParam(required = false) String startTime,@RequestParam(required = false)String endTime,@RequestParam(required = false)Integer sysAreaId,@RequestParam(required = false)Integer qb){
				// 登录用户session
				MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
				//通过用户登录信息查询地区id
				SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
				
				//查询所有地区
				if(so!=null){
					//如果区域为空，就根据机构所在地址查询
					int areaid = so.getSys_area_id();
					String key = RedisKeys.SYS_AREA + areaid;
					SysArea area = RedisUtil.getObjData(key, SysArea.class);
					if(area==null){
						area = sysAreaService.queryAreaById(areaid);
						RedisUtil.setData(key, area);
					}
					model.addAttribute("areaList", area);
					model.addAttribute("qb", 1);
				}
		
		return "dicExchangeLast/batchexport";
	}

	/**
	 * 列表页面
	 * 
	 * @param reqeust
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model, @RequestParam(required = false) String currency,@RequestParam(required = false) String startTime,@RequestParam(required = false)String endTime,@RequestParam(required = false)Integer sysAreaId,@RequestParam(required = false)Integer qb) {
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();
		//通过用户登录信息查询地区id
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		
		//查询所有地区
		if(so!=null){
			//如果区域为空，就根据机构所在地址查询
			int areaid = so.getSys_area_id();
			String key = RedisKeys.SYS_AREA + areaid;
			SysArea area = RedisUtil.getObjData(key, SysArea.class);
			if(area==null){
				area = sysAreaService.queryAreaById(areaid);
				RedisUtil.setData(key, area);
			}
			model.addAttribute("areaList", area);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		if (!StringUtils.isBlank(currency)) {
			param.put("currency", currency);
		}
		
//		param.put("SYS_AREA_ID", so.getSys_area_id());
		List<DicExchangeLast> dicExchangeLastList=new ArrayList<>();
		//根据地区，时间周期查询汇率
		if(sysAreaId!=null ||startTime!=null||endTime!=null){
			if(sysAreaId!=null){
				if(sysAreaId==1){
					dicExchangeLastList=dicExchangeLastService.selectAllByAreaAndTime(null, startTime,endTime,ps);
				}else{
//					SysArea area=sysAreaService.getUpOrThisSysArea(sysAreaId);
					dicExchangeLastList=dicExchangeLastService.selectAllByAreaAndTime(sysAreaId, startTime,endTime,ps);
				}
				
			}else{
				if(so.getSys_area_id()==1){
					dicExchangeLastList=dicExchangeLastService.selectAllByAreaAndTime(null, startTime,endTime,ps);
				}else{
					dicExchangeLastList=dicExchangeLastService.selectAllByAreaAndTime(so.getSys_area_id(), startTime,endTime,ps);
				}
			}
			if(sysAreaId!=null ){
				SysArea sysArea=sysAreaService.queryAreaById(sysAreaId);
				model.addAttribute("sysAreaName",sysArea.getSysAreaName() );
			}
			if(dicExchangeLastList!=null && dicExchangeLastList.size()>0){
				for (DicExchangeLast dicExchangeLast : dicExchangeLastList) {
					SysArea area=sysAreaService.getUpOrThisSysArea(dicExchangeLast.getDicAreaId());
					dicExchangeLast.setSysAreaName(area.getSysAreaName());
				}
			}
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("sysAreaId", sysAreaId);
			model.addAttribute("qb", 1);
			model.addAttribute("dicExchangeLastList", dicExchangeLastList);
			return "dicExchangeLast/batchexport";
		}else{
			if(so.getSys_area_id()==1){
				dicExchangeLastList= DataUtil
						.isEmpty(dicExchangeLastService.queryAllDicExchange(ps,null));
			}else{
				dicExchangeLastList= DataUtil
						.isEmpty(dicExchangeLastService.queryDicExchangeLasts(param, ps,so.getSys_area_id()));
			}
			if(dicExchangeLastList!=null && dicExchangeLastList.size()>0){
				for (DicExchangeLast dicExchangeLast : dicExchangeLastList) {
					SysArea area=sysAreaService.getUpOrThisSysArea(dicExchangeLast.getDicAreaId());
					dicExchangeLast.setSysAreaName(area.getSysAreaName());
				}
			}
			model.addAttribute("dicExchangeLastList", dicExchangeLastList);
			return "dicExchangeLast/list";
		}
	}
	//通过名字查询所有币种（历史记录表）
	@RequestMapping("/dicExchangeHistoryList")
	private String queryAll(HttpServletRequest request,HttpServletResponse response,Model model, DicExchangeLast dicExchangeLast,String name,String time,Integer dicAreaId) throws IOException{
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();
		//通过用户登录信息查询地区id
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		PageSupport ps = PageSupport.initPageSupport(request);
		//通过汇率id查询汇率
		DicExchangeLast dic=dicExchangeLastService.getDicExchangeLastById(dicExchangeLast.getDicExchangeId());
		List<DicExchangeLast> dicExchangeHistoryList=new ArrayList<>();
		if(name==null){
			name=dic.getDicExchangeName();
			//如果是四川省的可以查询所有历史汇率，其他地区的只能查询本地区的信息
//			if(so.getSys_area_id()==1){
//				dicExchangeHistoryList=dicExchangeLastService.getDicExchangeLastByDicExchangeName(ps,name,null);
//			}else{
//				dicExchangeHistoryList=dicExchangeLastService.getDicExchangeLastByDicExchangeName(ps,name,so.getSys_area_id());
//			}
			dicExchangeHistoryList=dicExchangeLastService.getDicExchangeLastByDicExchangeName(ps,name,dicAreaId);
		}
		if(name!=null&&time!=null){
			dicExchangeHistoryList=dicExchangeLastService.getDicExchangeLastByHistory(ps,name,time,dicAreaId);
//			dicExchangeHistoryList=dicExchangeLastService.getDicExchangeLastByHistory(ps,name,time,so.getSys_area_id());
		}
		if(dicExchangeHistoryList!=null&&dicExchangeHistoryList.size()>0){
			model.addAttribute("dicExchangeHistoryList", dicExchangeHistoryList);
			model.addAttribute("dicAreaId", dicAreaId);
		}
		return "dicExchangeLast/dicExchangeHistoryList";
	}
	//通过时间搜索历史记录表（历史记录表）
	@RequestMapping("/dicExchangeHistoryListsosuo")
//	@ResponseBody
	private void query(HttpServletRequest request,HttpServletResponse response,Model model,String name,String time,Integer url,Integer dicAreaId) throws IOException{
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();
		//通过用户登录信息查询地区id
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		PageSupport ps = PageSupport.initPageSupport(request);
		List<DicExchangeLast> dicExchangeHistoryList=new ArrayList<>();
		List<DicExchangeLast> dicExchangeHistoryList1=new ArrayList<>();
		if(time!=null && !StringUtils.isBlank(name)){
//			if(so.getSys_area_id()==1){
//				dicExchangeHistoryList=dicExchangeLastService.getDicExchangeLastByHistory(ps,name,time,null);
//				dicExchangeHistoryList1=dicExchangeLastService.getDicExchangeLastByHistory(null,name,time,null);
//			}else{
//				dicExchangeHistoryList=dicExchangeLastService.getDicExchangeLastByHistory(ps,name,time,so.getSys_area_id());
//				dicExchangeHistoryList1=dicExchangeLastService.getDicExchangeLastByHistory(null,name,time,so.getSys_area_id());
				dicExchangeHistoryList=dicExchangeLastService.getDicExchangeLastByHistory(ps,name,time,dicAreaId);
				dicExchangeHistoryList1=dicExchangeLastService.getDicExchangeLastByHistory(null,name,time,dicAreaId);
//			}
			if(dicExchangeHistoryList!=null){
				if(url==null){
					String sql1="SELECT * FROM DIC_EXCHANGE_TB WHERE DIC_EXCHANGE_NAME = '"+ name + "' AND subStr(DIC_EXCHANGE_TIME,1,10)= '" +time+" '" ;
					String sql = StringUtils.replace(sql1, " ", "|");
					SysManageLog sysManageLog=new SysManageLog();
					sysManageLog.setSysManageLogMenuName("汇率维护");
					sysManageLog.setSysManageLogCount(dicExchangeHistoryList1.size());
					sysManageLog.setSysManageLogOperateType(4);
					sysManageLog.setSysManageLogResult(true);
					sysManageLog.setSysManageLogQueryUserCondition("通过币种:"+name+" 和时间: "+ time +"进行查询");
					sysManageLog.setSysManageLogUrl("/admin/dicExchangeLast/dicExchangeHistoryList.jhtml?name="+name+"&time="+time+"&url=1");
					sysManageLog.setSysManageLogQuerySql(sql);
					sysManageLogService.insertSysManageLogTb(sysManageLog,request);
				}
				Gson gson=new Gson();
				response.getWriter().write(gson.toJson(dicExchangeHistoryList));
			}
		}
	}
	
	
	/**
	 * 
	 * @param response
	 * @param request
	 * @param startTime   开始时间
	 * @param endTime     结束时间
	 * @param areaId	   地区id
	 * @throws UnsupportedEncodingException
	 * @throws ParseException 
	 */
	@RequestMapping("/exportAll")
	@ResponseBody
	public void downFile(HttpServletResponse response, HttpServletRequest request,@RequestParam(required = false) String startTime,@RequestParam(required = false)String endTime,@RequestParam(required = false)Integer sysAreaId,@RequestParam(required = false)Integer qb) throws UnsupportedEncodingException, ParseException {
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();
		//通过用户登录信息查询地区id
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		PageSupport ps = PageSupport.initPageSupport(request);
		String[] rowNames = {  "币种名称","代码","汇率","归档时间","所属区域" };
//		String[] propertyNames = {"dicExchangeName","dicExchangeCode","dicExchangeValue","dicExchangeTimeDate" ,"sysAreaName"};
		String[] propertyNames = {"dicExchangeName","dicExchangeCode","dicExchangeValue","dicExchangeTime" ,"sysAreaName"};
		List<DicExchangeLast> list=new ArrayList<>();
		String qbName=null;
		if(qb!=null){
			qbName=qb.toString();
		}
		//根据地区，时间周期查询汇率
		if("1".equals(qbName)){
			if(sysAreaId!=null){
				if(sysAreaId==1){
					list=dicExchangeLastService.selectAllByAreaAndTime(null, startTime,endTime,null);
				}else{
//					SysArea area=sysAreaService.getUpOrThisSysArea(sysAreaId);
					list=dicExchangeLastService.selectAllByAreaAndTime(sysAreaId, startTime,endTime,null);
				}
				if(list!=null && list.size()>0){
					for (DicExchangeLast dicExchangeLast : list) {
						SysArea area=sysAreaService.getUpOrThisSysArea(dicExchangeLast.getDicAreaId());
						dicExchangeLast.setSysAreaName(area.getSysAreaName());
						dicExchangeLast.setDicExchangeTime(dicExchangeLast.getDicExchangeTime().substring(0,10));
//						String str=dicExchangeLast.getDicExchangeTime().substring(0,4)+dicExchangeLast.getDicExchangeTime().substring(5,7)+dicExchangeLast.getDicExchangeTime().substring(8,10);
//						DateFormat fmt =new SimpleDateFormat("yyyyMMdd");
//						Date date = fmt.parse(str);
//						dicExchangeLast.setDicExchangeTime(dicExchangeLast.getDicExchangeTime().substring(0,4)+"/"+dicExchangeLast.getDicExchangeTime().substring(5,7)+"/"+dicExchangeLast.getDicExchangeTime().substring(8,10));
//						DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
//						Date date = fmt.parse(dicExchangeLast.getDicExchangeTime());
//						String str=date.getYear()+"/"+date.getMonth()+"/"+date.getDay();
//						dicExchangeLast.setDicExchangeTimeDate(date);
					}
				}
			}else{
				if(so.getSys_area_id()==1){
					list=dicExchangeLastService.selectAllByAreaAndTime(null, startTime,endTime,null);
				}else{
					list=dicExchangeLastService.selectAllByAreaAndTime(so.getSys_area_id(), startTime,endTime,null);
				}
				if(list!=null && list.size()>0){
					for (DicExchangeLast dicExchangeLast : list) {
						SysArea area=sysAreaService.getUpOrThisSysArea(dicExchangeLast.getDicAreaId());
						dicExchangeLast.setSysAreaName(area.getSysAreaName());
//						dicExchangeLast.setDicExchangeTime(dicExchangeLast.getDicExchangeTime().substring(0,4)+"/"+dicExchangeLast.getDicExchangeTime().substring(5,7)+"/"+dicExchangeLast.getDicExchangeTime().substring(8,10));
//						
						dicExchangeLast.setDicExchangeTime(dicExchangeLast.getDicExchangeTime().substring(0,10));
//						String str=dicExchangeLast.getDicExchangeTime().substring(0,4)+dicExchangeLast.getDicExchangeTime().substring(5,7)+dicExchangeLast.getDicExchangeTime().substring(8,10);
//						DateFormat fmt =new SimpleDateFormat("yyyyMMdd");
//						Date date = fmt.parse(str);
//						dicExchangeLast.setDicExchangeTime(dicExchangeLast.getDicExchangeTime().substring(0,4)+"/"+dicExchangeLast.getDicExchangeTime().substring(5,7)+"/"+dicExchangeLast.getDicExchangeTime().substring(8,10));
//						DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
//						Date date = fmt.parse(dicExchangeLast.getDicExchangeTime());
//						String str=date.getYear()+"/"+date.getMonth()+"/"+date.getDay();
//						dicExchangeLast.setDicExchangeTimeDate(date);
					}
				}
			}
			
			if(list!=null){
				// 生成excel
				ExcelExport<DicExchangeLast> excelExport = new ExcelExport<DicExchangeLast>();
				excelExport.setTitle("汇率维护");
				excelExport.setRowNames(rowNames);
				excelExport.setPropertyNames(propertyNames);
				excelExport.setList(list);
				String url=excelExport.exportExcel(request,response);
				//添加管理日志
				manageLog(request,list.size(),6,true,null,null,url,null);
			}
		}else{
			if(so.getSys_area_id()==1){
				list= dicExchangeLastService.getDicExchangeLastByDicExchangeName(null,null,null);
			}else{
				list= dicExchangeLastService.getDicExchangeLastByDicExchangeName(null,null,so.getSys_area_id());
			}
			if(list!=null){
				// 生成excel
				ExcelExport<DicExchangeLast> excelExport = new ExcelExport<DicExchangeLast>();
				excelExport.setTitle("汇率维护");
				excelExport.setRowNames(rowNames);
				excelExport.setPropertyNames(propertyNames);
				excelExport.setList(list);
				String url=excelExport.exportExcel(request,response);
				//添加管理日志
				manageLog(request,list.size(),6,true,null,null,url,null);
			}
		}
	}
	

	
	/**
	 * 添加、修改页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(Model model, @RequestParam(required = false) Integer id) {
		if (id != null) {
			DicExchangeLast dicExchangeLast = dicExchangeLastService.queryDicExchangeLastById(id);
			model.addAttribute("dicExchangeLast", dicExchangeLast);
		}
		return "dicExchangeLast/edit";
	}
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public JsonResWrapper submit(Model model, DicExchangeLast dicExchangeLast, HttpServletRequest request,Integer id,Double val){
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		JsonResWrapper jrw = new JsonResWrapper();
		//通过id查询历史汇率
		DicExchangeLast dic=dicExchangeLastService.getDicExchangeLastById(id);
		dicExchangeLast.setDicExchangeId(id);
		dicExchangeLast.setDicExchangeValue(val);
		dicExchangeLastService.updateHistoryExchange(dicExchangeLast);
		jrw.setMessage("操作成功");
		//添加管理日志
		manageLog(request,1,3,true,val.toString(),dic.getDicExchangeValue().toString(),null,null);
		return jrw;
	}

	/**
	 * 保存添加、修改
	 * @param model
	 * @param dicExchangeLast
	 * @param request
	 * @param time  添加时间
	 * @param name  币种
	 * @param code	币种编码	
	 * @param id	汇率id
	 * @param val	汇率
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editSubmit", method = RequestMethod.POST)
	@ResponseBody
	public JsonResWrapper editSubmit(Model model, DicExchangeLast dicExchangeLast, HttpServletRequest request,Date time,String name,String code,Integer id,String val) throws Exception {
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();
		//通过用户登录信息查询地区id
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		JsonResWrapper jrw = new JsonResWrapper();
		if (dicExchangeLast.getDicExchangeCode() == null || dicExchangeLast.getDicExchangeValue() == null
				|| dicExchangeLast.getDicExchangeName() == null) {
			jrw.setFlag(false);
			//添加管理日志
			manageLog(request,1,1,false,null,null,null,null);
			jrw.setMessage("操作失败，请填写完整");
			return jrw;
		} else {
			// 向数据字典添加值,币种为34
			List<DicContent> dicContentList = dicContentService.getDicContentsByDicId(34);
			String str = dicExchangeLast.getDicExchangeCode();
			for (int i = 0; i < str.length(); i++) {
				if ((str.charAt(i) <= 'Z' && str.charAt(i) >= 'A') || (str.charAt(i) <= 'z' && str.charAt(i) >= 'a')) {
					// 是字母
				} else {
					jrw.setFlag(false);
					//添加管理日志
					manageLog(request,1,1,false,null,null,null,null);
					jrw.setMessage("代码不是字母");
					return jrw;
				}
			}
			dicExchangeLast.setDicExchangeCode(str.toUpperCase());// 转为大写

			String myDicExchangeCode = null;
			String myDicExchangeName = null;
			if (dicExchangeLast.getDicExchangeId() != null) {
				DicExchangeLast myDicExchangeLast = dicExchangeLastService
						.queryDicExchangeLastById(dicExchangeLast.getDicExchangeId());
				myDicExchangeCode = myDicExchangeLast.getDicExchangeCode();
				myDicExchangeName = myDicExchangeLast.getDicExchangeName();
			}
			for (DicContent dicContentTwo : dicContentList) {
				if (dicExchangeLast.getDicExchangeId() != null) {// 如果为修改
																		// 则判断代码不包括自身

				}
			}
			Integer dicExchangeLastId = dicExchangeLast.getDicExchangeId();
			DicContent dicContent = null;
			if (dicContent == null) {
				dicContent = new DicContent();
			}
			dicContent.setDicId(34);
			dicContent.setDicContentCode(dicExchangeLast.getDicExchangeCode());
			dicContent.setDicContentValue(dicExchangeLast.getDicExchangeName());
			//查询数据字典，如果数据字典中有数据则不提交，没有则添加
			boolean i=dicContentService.isContentValueHaved(dicExchangeLast.getDicExchangeName());
			if(i==true){
				DicContent dicValue=dicContentService.getDicIdByDicContentValueAndDicId(dicExchangeLast.getDicExchangeName(),34);
				if(!dicValue.getDicContentCode().equals(dicExchangeLast.getDicExchangeCode())){
					jrw.setFlag(false);
					//添加管理日志
					manageLog(request,1,1,false,null,null,null,null);
					jrw.setMessage("添加的币种名称和代码不匹配，请确认后添加");
					return jrw;
				}
			}
			DicContent dic=dicContentService.getDicContentByDicIDAndCode(dicExchangeLast.getDicExchangeCode(),34);
			if(dic!=null){
				if(!dic.getDicContentValue().equals(dicExchangeLast.getDicExchangeName())){
					jrw.setFlag(false);
					//添加管理日志
					manageLog(request,1,1,false,null,null,null,null);
					jrw.setMessage("添加的币种名称和代码不匹配，请确认后添加");
					return jrw;
				}
			}
			if(i==false  ||dic==null){
				dicContentService.insertOrUpdate(dicContent);
			}
			dicExchangeLast.setDicId(34);
			dicExchangeLast.setDicAreaId(so.getSys_area_id());
			SimpleDateFormat sd = new SimpleDateFormat(dicExchangeLast.getDicExchangeTime()+"-HH.mm.ss");
	        Date date = new Date();
	        String tp = sd.format(date);
			dicExchangeLast.setDicExchangeTime(tp);
			dicExchangeLastService.insertExchenge(dicExchangeLast);
			//添加管理日志
			manageLog(request,1,1,true,dicExchangeLast.getDicExchangeName(),null,null,null);
			jrw.setMessage("操作成功");
		}
		model.addAttribute("dicExchangeLast", dicExchangeLast);
		return jrw;
	}

	// 下载excel模板
	@RequestMapping(value = "downLoad")
	public void downLoadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 生成
		ExcelExportOrg<DicExchangeLast> excelExport = new ExcelExportOrg<>();
		excelExport.setTitle("汇率导入模板");
		String[] rowNames = { "币种名称","代码","汇率","归档时间","所属区域" };
		List<DicContent>  listContent=dicContentService.getDicContentsByDicId(34);
		StringBuffer s=new StringBuffer();
		for (DicContent dicContent : listContent) {
			s.append(dicContent.getDicContentValue()+"    "+dicContent.getDicContentCode()+"\n");
		}
		String[] prompt=new String[4];
//		prompt[0]="";
		prompt[1]=s.toString();
//		prompt[2]="";
//		prompt[3]="";
		excelExport.setRowNames(rowNames);
		excelExport.setPrompt(prompt);
		excelExport.setRow(listContent.size());
		String url=excelExport.exportExcel(request, response);
		//添加管理日志
		manageLog(request,1,6,true,null,null,url,null);
	}
	/**
	 * 
	 * @param file
	 * @param request
	 * @param model
	 * @return  hlName  汇率名称
	 * @throws Exception
	 */
	// 导入excel
	@RequestMapping(value = "upLoad")
	public String upLoadFile(MultipartFile file, HttpServletRequest request,Model model) throws Exception {
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();
		//通过用户登录信息查询地区id
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		ExcelReaderTwoDic<DicExchangeLast> excelReader = new ExcelReaderTwoDic<>();
		excelReader.setRowNames(new String[] { "币种名称","代码","汇率","归档时间" ,"所属区域"});
		excelReader.setPropertyNames(new String[] { "dicExchangeName","dicExchangeCode","dicExchangeValue","dicExchangeTimeDate" ,"sysAreaName"});
		excelReader.setT(new DicExchangeLast());
		String fileName=UpLoadFile.upLoadFile(file);
		List<DicExchangeLast> list = excelReader.excelReader(request,fileName);
		//存所有错误信息的list
 		List<String> msgString=new ArrayList<String>();
		List<DicContent> dicExchangeLastlist =dicContentService.queryAllContent();
		
//		String originalFilename = file.getOriginalFilename();
//		String savePath = SettingUtils.getCommonSetting("upload.file.temp.path");
//		File myfile = new File(savePath);
//		if (!myfile.exists()) {
//			myfile.mkdirs();
//		}
//		// 新的名称
//		String newFileName = "/" + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
		if (list != null && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				DicExchangeLast upLoadDicExchangeLast=list.get(i);
				Integer countTodayDicExchangeName = dicExchangeLastService.countTodayDicExchangeName(upLoadDicExchangeLast.getDicExchangeName());
				boolean isEquals=false;
				
				String code1=upLoadDicExchangeLast.getDicExchangeCode();
				//判断汇率code是否为空
				if(code1!=null){
//					isEquals=true;
					//判断汇率code是否为字母
//					isEquals=false;
					for (int m = 0; m < code1.length(); m++) {
						if ((code1.charAt(m) <= 'Z' && code1.charAt(m) >= 'A') || (code1.charAt(m) <= 'z' && code1.charAt(m) >= 'a')) {
							isEquals=true;
						}
					}
					
					if (isEquals==false) {
						msgString.add("第"+ (i + 2) +"行2列，代码应为字母，请确认后提交");
					}
			
				}
				isEquals=false;
				if(code1!=null){
					//创建dicExchange对象
					DicExchangeLast dic=new DicExchangeLast();
					dic.setDicExchangeCode(code1.toUpperCase());// 转为大写
						//通过获取传入数据币种代码，对比查询代码是否正确
						for (DicContent dicExchangeLast : dicExchangeLastlist) {
							if(code1.toUpperCase().equals(dicExchangeLast.getDicContentCode())){
								isEquals=true;
							}
						}
					
						if (isEquals==false) {
							msgString.add("第"+ (i + 2) +"行2列，该币种代码错误，无法导入 请修正或增加币种后操作");
						}
				}
				if(isEquals==false){
					msgString.add("第"+(i+2)+"行汇率编码数据格式错误，不能导入，请确认后提交");
				}
				//汇率名称不能为空
				isEquals=false;
				String hlName=upLoadDicExchangeLast.getDicExchangeName();
				if(!StringUtils.isBlank(hlName)){
					isEquals=true;
				}
				if(isEquals==false){
					msgString.add("第"+(i+2)+"行汇率名称为空或名称格式错误");
				}
				//行政区划是否匹配
				isEquals=false;
				//通过地区id地区查询地区名称
				SysArea area=sysAreaService.queryAreaById(so.getSys_area_id());
				if(upLoadDicExchangeLast!=null){
					if(area.getSysAreaName().equals(upLoadDicExchangeLast.getSysAreaName())){
						isEquals=true;
					}
				}
				if(isEquals==false){
					msgString.add("第"+(i+2)+"行所属区域应该为"+area.getSysAreaName()+"，请修改后在提交");
				}
				//汇率只能为数字
				isEquals=false;
				Double value=upLoadDicExchangeLast.getDicExchangeValue();
				if(value!=null){
					isEquals=true;
				}
				if(isEquals==false){
					msgString.add("第"+(i+2)+"行汇率应该为数字");
				}
				//判断传入的时间是否大于当前时间，大于当前时间进行错误返回
				isEquals=false;
				Date time=list.get(i).getDicExchangeTimeDate();
				if(time!=null){
					SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
					Date newDate=new Date();
					if(time.getTime()<newDate.getTime()){
						isEquals=true;
					}
					if (isEquals==false) {
						msgString.add("第"+ (i + 2) +"行4列，导入时间不能超过当前时间，请确认后提交");
					}
				}
				if (isEquals==false) {
					msgString.add("第"+ (i + 2) +"行4列，导入时间格式错误，请确认后提交");
				}
				//通过获取传入的数据名称去content字典中去查找，如果有，则添加，不存在，则返回错误
				for (DicContent dicExchangeLast : dicExchangeLastlist) {
					if(upLoadDicExchangeLast.getDicExchangeName()!=null){
						if (upLoadDicExchangeLast.getDicExchangeName().equals(dicExchangeLast.getDicContentValue())) {
							isEquals=true;
						}
					}
				}
				if (isEquals==false) {
					msgString.add("第"+ (i + 2) +"行1列，该币种不存在，无法导入 请修正或增加币种后操作");
				}
				//判断传入的币种名称是否是汉字
				Pattern pattern = Pattern.compile(".*[\\u4e00-\\u9faf].*");
		        Matcher isNum = pattern.matcher(upLoadDicExchangeLast.getDicExchangeName());
		        if(!isNum.matches()){
		        	msgString.add("第"+ (i + 2) +"行，币种名称应该为汉字");
		        }
			}
			if(msgString.size()>0){
				model.addAttribute("msgString",msgString);
				//添加管理日志
				manageLog(request,msgString.size(),5,false,null,null,null,null);
				return "forward:list.jhtml";
			}
			
			// 导入数据库
			for (DicExchangeLast dicExchangeLast : list) {
				String name=dicExchangeLast.getDicExchangeName();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = sdf.format(dicExchangeLast.getDicExchangeTimeDate());
				
				SimpleDateFormat sd = new SimpleDateFormat(startTime+"-HH.mm.ss");
				 Date date = new Date();
			    String tp = sd.format(date);
//				Date time=dicExchangeLast.getDicExchangeTime();
//				String ti=sm.format(time);
				DicContent dicContent=dicContentService.getDicIdByDicContentValue(name);
				DicExchangeLast myDicExchangeLast=new DicExchangeLast();
				myDicExchangeLast.setDicId(34);
				myDicExchangeLast.setDicExchangeTime(tp);
				myDicExchangeLast.setDicAreaId(so.getSys_area_id());
				myDicExchangeLast.setDicExchangeCode(dicExchangeLast.getDicExchangeCode().toUpperCase());
				myDicExchangeLast.setDicExchangeName(name);
				myDicExchangeLast.setDicExchangeValue(dicExchangeLast.getDicExchangeValue());
				dicExchangeLastService.insertExchenge(myDicExchangeLast);
				request.setAttribute("msg", "操作成功");
			}
			//添加管理日志
			manageLog(request,list.size(),5,true,null,null,fileName,null);
		}else{
			request.setAttribute("msg", "数据格式有误，请确认后提交");
			if(list!=null){
				//添加管理日志
				manageLog(request,list.size(),5,false,null,null,fileName,null);
			}else{
				//添加管理日志
				manageLog(request,0,5,false,null,null,fileName,null);
			}
		}
		return "forward:list.jhtml";
	}
}
