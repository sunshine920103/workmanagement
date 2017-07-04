package com.workmanagement.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.service.RelateInfoService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.PageSupport;

@Controller
@RequestMapping("/admin/dataTypeQuery")
public class AreaInfoController {
	
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysManageLogService sysManageLogService;	
	@Autowired
	private RelateInfoService relateInfoService;
	/**
	 * 地区模糊查询
	 */
	@RequestMapping("/areaInfo")
	public String areaInfo(){
		return "dataTypeQuery/areaInfo";
	}
	@RequestMapping("/search")
	public String search(@RequestParam(required=false)String condition ,
			HttpServletRequest request,
			Model model){
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<SysArea> list =null;
		// 分页
		PageSupport ps = PageSupport.initPageSupport(request);
		StringBuffer sql2 = new StringBuffer("select * from sys_area_tb where sys_area_name like '%"+condition+
				"%' or sys_area_code like '%"+condition+"%'");
		StringBuffer sql = new StringBuffer("select * from ( select * from sys_area_tb where sys_area_name like '%"+condition+
				"%' or sys_area_code like '%"+condition+"%' ) s where s.sys_area_code like'51%'");		
		String sql0 = null;
		try{	
			if(userDetails.getSys_user_id()==1){//四川省管理员superadmin可查询全国
				sql0=StringUtils.replace(sql2.toString(), " ", "|");
				list = sysAreaService.search2(condition,ps);
			}else{//其他只能查询四川省		
				sql0=StringUtils.replace(sql.toString(), " ", "|");
				list = sysAreaService.search(condition,ps);
			}
			sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, 
					null, null, null, new Date(), 4, list!=null?list.size():0, "关键字："+(StringUtils.isBlank(condition)?" ":condition), 
					sql0,"/admin/dataTypeQuery/sysQuery.jhtml",null , null, true),request);
		}catch (Exception e) {
			sysManageLogService.insertSysManageLogTb(new SysManageLog("地区维护", null, null, 
					null, null, null, new Date(), 4, list!=null?list.size():0, "关键字："+(StringUtils.isBlank(condition)?" ":condition), 
					sql0,"/admin/dataTypeQuery/sysQuery.jhtml",null , null, false),request);
		}
		model.addAttribute("list", list);
		model.addAttribute("condition", condition);		
		return "dataTypeQuery/areaInfo";
	}
	
    /**
     * 管理员查看的查询结果集
     *
     * @return
     */
    @RequestMapping("/sysQuery")
    public ModelAndView sysQuery(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView view = new ModelAndView("dataTypeQuery/areaInfo");
        Object objSql = request.getAttribute("querySql");
        String querySql = null;
        if (objSql == null) {
            querySql = String.valueOf(session.getAttribute("query_sql"));
        } else {
            querySql = String.valueOf(objSql);
            session.setAttribute("query_sql", querySql);
        }
        String sql = StringUtils.replace(querySql, "|", " ");
        PageSupport ps = PageSupport.initPageSupport(request);
        List<Map<String, Object>> list = relateInfoService.queryMoreData(ps, sql);
        view.addObject("list", list);
        return view;
    }
    
    /**
     * 导出结果集
     */
    @RequestMapping("/exportTo")
    public void export(@RequestParam(required=false)String condition ,
			HttpServletRequest request,HttpServletResponse response){
    	List<SysArea> list = sysAreaService.search(condition,null);
    	String[] rowNames = { "编码","地区名称"};
		String[] propertyNames = { "sysAreaCode", "sysAreaName"};
		
		if(CollectionUtils.isNotEmpty(list)){
			// 生成excel
			ExcelExport<SysArea> excelExport = new ExcelExport<SysArea>();
			excelExport.setTitle("地区名称与代号对应信息");
			excelExport.setRowNames(rowNames);
			excelExport.setPropertyNames(propertyNames);
			excelExport.setList(list);
			String url=excelExport.exportExcel(request,response);
			SysManageLog sysManageLog=new SysManageLog();
			sysManageLog.setSysManageLogMenuName("地区信息查询");
			sysManageLog.setSysManageLogCount(list.size());
			sysManageLog.setSysManageLogFile(url);
			sysManageLog.setSysManageLogOperateType(6);
			sysManageLog.setSysManageLogResult(true);
			sysManageLogService.insertSysManageLogTb(sysManageLog,request);
		}
    }
}
