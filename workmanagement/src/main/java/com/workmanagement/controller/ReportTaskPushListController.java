package com.workmanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.util.StringUtil;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.ReportTaskPushList;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.service.ReportTaskPushListService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;

/**
 * 任务推送
 * 
 * @author renyang
 */
@Controller
@RequestMapping("/admin/resportTaskFinished")
public class ReportTaskPushListController {

	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private ReportTaskPushListService reportTaskPushListService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysManageLogService sysManageLogService;

	/**
	 * 列表页面
	 * 
	 * @param reqeust task_begin task_end task_name taskType
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model,@RequestParam(required = false) String  task_begin,
			@RequestParam(required = false) String  task_end,@RequestParam(required = false) String  task_name,
			@RequestParam(required = false) Integer  taskType,@RequestParam(required = false) Integer  url
			) throws Exception {
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Map<String, Object> param = new HashMap<String, Object>();
		//缓存单个机构
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
			if(so==null){
				so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
				RedisUtil.setData(orgKey, so);
			}
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
		}
		if(taskType!=null||StringUtil.isNotEmpty(task_begin)||StringUtil.isNotEmpty(task_end)){
			String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
			StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
			sb.deleteCharAt(sb.length()-1);
			String sql1="";
			if(!"".equals(task_name)){
				sql1=" AND settb.report_task_push_set_name like '%"+task_name+"%'";
			}
			if(!"".equals(task_name)){
				param.put("task_name", task_name);
			}else{
				param.put("task_name", null);
			}
			if(taskType!=3){
				param.put("taskType", taskType);
			}
			param.put("task_begin", task_begin);
			param.put("task_end", task_end);
			PageSupport ps = PageSupport.initPageSupport(request);
			List<ReportTaskPushList> reportTaskPushSetList=reportTaskPushListService.queryAll(param,ps);
			model.addAttribute("reportTaskPushSetList", reportTaskPushSetList);
			model.addAttribute("task_name", task_name);
			model.addAttribute("taskType", taskType);
			model.addAttribute("task_begin", task_begin);
			model.addAttribute("task_end", task_end);
			if(url==null){
				SysManageLog sysManageLog=new SysManageLog();
				sysManageLog.setSysManageLogMenuName("任务完成情况统计");
				if(reportTaskPushSetList!=null){
					sysManageLog.setSysManageLogCount(reportTaskPushSetList.size());
				}else{
					sysManageLog.setSysManageLogCount(0);
				}
				sysManageLog.setSysManageLogOperateType(4);
				sysManageLog.setSysManageLogResult(true);
				String sql=" SELECT listtb.report_task_push_status,settb.report_task_push_set_name,listtb.report_task_push_list_endtime,settb.report_task_push_set_type,listtb.sys_org_name"+
						   " FROM (SELECT ltb.*,tb.SYS_ORG_NAME,tb.SYS_AREA_ID FROM report_task_push_list_tb ltb LEFT JOIN sys_org_tb tb ON ltb.SYS_ORG_ID=tb.SYS_ORG_ID) AS listtb"+
						   " LEFT JOIN (SELECT * FROM report_task_push_set_tb stb LEFT JOIN sys_org_tb tb ON stb.SYS_ORG_CREATE_ID=tb.SYS_ORG_ID) AS settb"+
						   " ON listtb.report_task_push_set_id =settb.report_task_push_set_id "+
						   " WHERE listtb.report_task_push_status ="+taskType+
						   " AND listtb.report_task_push_list_endtime Between "+task_begin+" And "+task_end+sql1+
						   " AND (listtb.SYS_AREA_ID IN ("+sb+") OR settb.SYS_AREA_ID IN("+sb+"))"+
						   " ORDER BY listtb.report_task_push_list_endtime DESC";
				sysManageLog.setSysManageLogQuerySql(sql);
				sysManageLog.setSysManageLogUrl("/admin/resportTaskFinished/list.jhtml?taskType="+taskType+"&task_begin="+task_begin+"&task_end="+task_end+"&task_name="+task_name+"&url=1");
				sysManageLogService.insertSysManageLogTb(sysManageLog,request);
				model.addAttribute("url", 1);
			}else{
				model.addAttribute("url", 1);
			}
			return "resportTaskFinished/list";
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<ReportTaskPushList> reportTaskPushSetList = DataUtil
				.isEmpty(reportTaskPushListService.queryAll(param,ps));
		model.addAttribute("reportTaskPushSetList", reportTaskPushSetList);
		return "resportTaskFinished/list";
	}
}
