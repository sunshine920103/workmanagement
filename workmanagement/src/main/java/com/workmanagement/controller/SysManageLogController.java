package com.workmanagement.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.reflect.TypeToken;
import com.ibm.db2.jcc.t4.sb;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysRole;
import com.workmanagement.model.SysUser;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysRoleService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.DateFormatter;
import com.workmanagement.util.DownLoadFile;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;

/**
 * 日志管理
 * 
 * @author lzl
 */
@Controller
@RequestMapping("/admin/sysManageLog")
public class SysManageLogController {

	@Autowired
	private SysManageLogService sysManageLogService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	SysUserService sysUserService;
	@Autowired
	SysRoleService sysRoleService;

	private static final String SYS_MENU = "管理日志";

	/**
	 * 查看详情
	 * 
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(Model model, @RequestParam(required = false) Integer id, HttpServletRequest request,
			HttpSession session) {

		if (id != null) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("id", id);
			model.addAttribute("sysUserLog", sysManageLogService.queryAdminLogTbs(param, null));
			String sql = "SELECT * FROM sys_manage_log_tb where  sys_manage_log_id =" + id;
			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null, null, null,
					new Date(), SysManageLog.SELECT_SYSMANAGElOG, 1, "", sql, "/admin/sysManageLog/detail.jhtml", null,
					null, true), request);

		} else {
			Object objSql = request.getAttribute("querySql");
			String querySql = null;
			if (objSql == null) {
				querySql = String.valueOf(session.getAttribute("sysUserBehaviorAudit_sql"));
			} else {
				querySql = String.valueOf(objSql);
				session.setAttribute("sysUserBehaviorAudit_sql", querySql);
			}
			String sql = StringUtils.replace(querySql, "|", " ");
			Map<String, Object> map = new HashMap<>();
			map.put("sql", sql);
			model.addAttribute("sysUserLog", sysManageLogService.querySql(map, null));

		}
		return "sysManageLog/detail";
	}

	@RequestMapping(value = "/forward")
	public String forward(HttpServletRequest request, Integer sysId) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("id", sysId);
		List<SysManageLog> s = sysManageLogService.queryAdminLogTbs(map, null);
		request.setAttribute("querySql", s.get(0).getSysManageLogQuerySql());
		return "forward:" + s.get(0).getSysManageLogUrl();
	}

	/**
	 * 返回ID集合
	 * 
	 * @param i
	 * @return
	 */
	private String getInstiIds(SysOrg i) {
		String ids = "";
		List<SysOrg> subs = i.getSubSysOrg();
		if (CollectionUtils.isEmpty(subs)) {
			return i.getSys_org_id().toString();
		}
		for (SysOrg sub : subs) {
			ids += getInstiIds(sub) + ",";
		}
		ids += i.getSys_org_id().toString();
		return ids;
	}

	@RequestMapping("/addTest")
	public String addTest(Model model, HttpServletRequest request, HttpSession session) {
		PageSupport ps = PageSupport.initPageSupport(request);
		Object objSql = request.getAttribute("querySql");
		String querySql = null;
		if (objSql == null) {
			querySql = String.valueOf(session.getAttribute("sysUserBehaviorAudit_sql"));
		} else {
			querySql = String.valueOf(objSql);
			session.setAttribute("sysUserBehaviorAudit_sql", querySql);
		}
		String sql = StringUtils.replace(querySql, "|", " ");
		Map<String, Object> map = new HashMap<>();
		map.put("sql", sql);
		model.addAttribute("als", sysManageLogService.querySql(map, ps));
		return "sysManageLog/list";
	}

	/**
	 * 日志列表页面
	 * 
	 * @param key
	 *            输入的关键字
	 * @param begin
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/list")
	public String add(HttpServletRequest request, Model model, @RequestParam(required = false) Integer methodTextVal,
			@RequestParam(required = false) String key, @RequestParam(required = false) String begin,
			@RequestParam(required = false) String end, @RequestParam(required = false) String menu,
			@RequestParam(required = false) Integer types) {
		StringBuffer stringBu = new StringBuffer("	SELECT * FROM sys_manage_log_tb S where 1=1 ");

		// 当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SysUser su = sysUserService.querySystemUserById(userDetails.getSys_user_id());
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		SysOrg i = null;
		if (methodTextVal != null) {
			param.put("insti", methodTextVal);
			i = sysOrgService.queryInstitutionsById(methodTextVal);

			String s = getInstiIds(i);
			if (i != null) {
				param.put("instiAll", s.split(","));
			}
			stringBu.append(" and S.sys_org_id IN(" + s.toString() + ")");
			model.addAttribute("methodTextVal", i);

		} else {
			SysRole sr = sysRoleService.querySystemRoleById(userDetails.getRoleIds().get(0));
			boolean isPepole = (sr.getSys_role_type() == SysRole.PEPOLE_ADMIN
					|| sr.getSys_role_type() == SysRole.PEPOLE_QUERY || sr.getSys_role_type() == SysRole.PEPOLE_REPORT);
			i = sysOrgService.queryInstitutionsById(su.getSys_org_id());

			if (isPepole) {

				if (userDetails.getSys_user_id().intValue() != 1) {
					SysArea sysArea1 = sysAreaService.queryAreaById(i.getSys_area_id());
					if (!sysArea1.getSysAreaType().equals("1")) {
						// 默认是登录用户所在机构及子机构
						if (i != null) {
							String o = getInstiIds(i);
							param.put("instiAll", o.split(","));
							stringBu.append(" and S.sys_org_id IN(" + o + ")");
						}
					} else {
						param.put("orgIn", sysArea1.getSysAreaId());
						stringBu.append(
								"and S.sys_org_id in (select sys_org_id from sys_org_tb where SYS_ORG_AFFILIATION_AREA_ID = "
										+ sysArea1.getSysAreaId() + ")");

					}
				}
			} else {

			
				StringBuffer sb = new StringBuffer();
				DataUtil.getChildOrgIds(i, sb);

				param.put("instiAll", sb.toString().split(","));
				stringBu.append(" and i.sys_org_id IN(" + sb);

				stringBu = new StringBuffer(stringBu.substring(0, stringBu.length() - 1));
				stringBu.append(") ");

			}

			model.addAttribute("methodTextVal", i);
		}

		if (!StringUtils.isBlank(key)) {
			param.put("key", key);
			stringBu.append(" and S.sys_manage_log_user_name LIKE '%" + key + "%' OR S.sys_manage_log_menu_name LIKE'%"
					+ key + "%'");
			model.addAttribute("key", key);
		}
		if (!StringUtils.isBlank(menu)) {
			param.put("menu", menu);
			stringBu.append(" AND S.sys_manage_log_menu_name = '" + menu + "'");
			model.addAttribute("menu", menu);
		}
		if (types != null) {
			if (types != 0) {
				param.put("types", types);
				stringBu.append(" AND S.sys_manage_log_operate_type = " + types);
			}
			model.addAttribute("types", types);
		}
		try {
			if (!StringUtils.isBlank(begin)) {
				begin += " 00:00:00";
				param.put("begin", DateFormatter.formatDateTime(begin));
				model.addAttribute("begin", begin.substring(0, 10));
				stringBu.append(" AND S.sys_manage_log_time BETWEEN '" + begin);
			}
			if (!StringUtils.isBlank(end)) {
				end += " 23:59:59";
				param.put("end", DateFormatter.formatDateTime(end));
				model.addAttribute("end", end.substring(0, 10));
				stringBu.append("'  AND '" + end + "'");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		stringBu.append(" order by sys_manage_log_id desc");
		List<SysManageLog> als = sysManageLogService.queryAdminLogTbs(param, ps);
		if (!StringUtils.isBlank(key) || methodTextVal != null || !StringUtils.isBlank(begin)
				|| !StringUtils.isBlank(end) || (types != null && types != 0) || !StringUtils.isBlank(menu)) {

			sysManageLogService
					.insertSysManageLogTb(
							new SysManageLog(SYS_MENU, null, null, null, null, null, new Date(),
									SysManageLog.SELECT_SYSMANAGElOG, als == null ? 0 : als.size(),
									(methodTextVal != null ? "" : "机构名称:" + i.getSys_org_name() + ",")
											+ (StringUtils.isBlank(key) ? "" : "用户名或企业代码:" + key + ",")
											+ (StringUtils.isBlank(begin) ? "" : "开始时间:" + begin)
											+ (StringUtils.isBlank(end) ? "" : "结束时间:" + end)
											+ (StringUtils.isBlank(menu) ? "" : "菜单:" + menu)
											+ (types == null ? ""
													: types == 0 ? ""
															: "类型:" + (types == 1 ? "增加"
																	: types == 2 ? "删除"
																			: types == 3 ? "修改"
																					: types == 4 ? "查询"
																							: types == 5 ? "导入"
																									: types == 6 ? "导出"
																											: "")),
									stringBu.toString(), "/admin/sysManageLog/addTest.jhtml", null, null, true),
							request);
		}
		model.addAttribute("als", als);
		// 菜单列表
		List<SysMenu> me = sysManageLogService.queryMenu();

		model.addAttribute("me", me);
		return "sysManageLog/list";
	}

	@Autowired
	SysAreaService sysAreaService;

	public String[] sm(HttpServletRequest request) {
		String[] s = null;
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Map<String, Object> param = new HashMap<String, Object>();
		// 缓存单个机构
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if (so == null) {
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		// 缓存集合机构
		String orgListKey = RedisKeys.SYS_ORG_LIST_USER + userDetails.getSys_user_id();
		Type type = new TypeToken<List<SysOrg>>() {
		}.getType();
		List<SysOrg> issro = RedisUtil.getListData(orgListKey, type);
		if (CollectionUtils.isEmpty(issro)) {
			if (so.getSys_area_id() != null) {
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
				s = sb.toString().split(",");// 区域id

			}
		}
		return s;
	}

	@RequestMapping(value = "/excl")
	@ResponseBody
	public JsonResWrapper excl(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) Integer methodTextVal, @RequestParam(required = false) String key,
			@RequestParam(required = false) String begin, @RequestParam(required = false) String end,
			@RequestParam(required = false) String menu, @RequestParam(required = false) Integer types)
			throws Exception {
		JsonResWrapper jrw = new JsonResWrapper();
		// 当前登录用户的session

		// 当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SysUser su = sysUserService.querySystemUserById(userDetails.getSys_user_id());
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		SysOrg i = null;
		if (methodTextVal != null) {
			param.put("insti", methodTextVal);
			i = sysOrgService.queryInstitutionsById(methodTextVal);

			String s = getInstiIds(i);
			if (i != null) {
				param.put("instiAll", s.split(","));
			}

		} else {
			SysRole sr = sysRoleService.querySystemRoleById(userDetails.getRoleIds().get(0));
			boolean isPepole = (sr.getSys_role_type() == SysRole.PEPOLE_ADMIN
					|| sr.getSys_role_type() == SysRole.PEPOLE_QUERY || sr.getSys_role_type() == SysRole.PEPOLE_REPORT);
			i = sysOrgService.queryInstitutionsById(su.getSys_org_id());

			if (isPepole) {

				if (userDetails.getSys_user_id().intValue() != 1) {
					SysArea sysArea1 = sysAreaService.queryAreaById(i.getSys_area_id());
					if (!sysArea1.getSysAreaType().equals("1")) {
						// 默认是登录用户所在机构及子机构
						if (i != null) {
							String o = getInstiIds(i);
							param.put("instiAll", o.split(","));
						}
					} else {
						param.put("orgIn", sysArea1.getSysAreaId());
						
					}
				}
			} else {

			
				StringBuffer sb = new StringBuffer();
				DataUtil.getChildOrgIds(i, sb);

				param.put("instiAll", sb.toString().split(","));

			}

		}

		if (!StringUtils.isBlank(key)) {
			param.put("key", key);
		}
		if (!StringUtils.isBlank(menu)) {
			param.put("menu", menu);
		}
		if (types != null) {
			if (types != 0) {
				param.put("types", types);
			}
		}
		try {
			if (!StringUtils.isBlank(begin)) {
				begin += " 00:00:00";
				param.put("begin", DateFormatter.formatDateTime(begin));
			}
			if (!StringUtils.isBlank(end)) {
				end += " 23:59:59";
				param.put("end", DateFormatter.formatDateTime(end));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		List<SysManageLog> als = sysManageLogService.queryAdminLogTbs(param, null);
		if (als != null) {
			if (als.size() > 5000) {
				jrw.setMessage("下载数据量过大，请细化筛选条件!");
				return jrw;
			}
		}
		String[] rowNames = { "日志id", "机构名称", "用户名称", "角色名称", "菜单名称", "操作对象", "原值", "现值", "操作时间", "操作类型", "授权文件" };
		String[] propertyNames = { "sysManageLogId", "sysManageLogOrgName", "sysManageLogUserName",
				"sysManageLogRoleName", "sysManageLogMenuName", "sysManageLogEnterpriseCode", "sysManageLogOldValue",
				"sysManageLogNewValue", "time", "type", "sysManageLogAuthFile" };

		try {
			// 生成excel
			ExcelExport<SysManageLog> excelExport = new ExcelExport<>();
			excelExport.setTitle("管理日志");
			excelExport.setRowNames(rowNames);
			excelExport.setPropertyNames(propertyNames);
			excelExport.setList(als);
			String url = excelExport.exportExcel(request, response);
			sysManageLogService
					.insertSysManageLogTb(
							new SysManageLog(SYS_MENU, null, null, null, null, null, new Date(),
									SysManageLog.EXPORT_SYSMANAGElOG, als.size(), null, null, null, url, null, true),
							request);

		} catch (Exception e) {
			// TODO: handle exception
			sysManageLogService.insertSysManageLogTb(
					new SysManageLog(SYS_MENU, null, null, null, null, null, new Date(),
							SysManageLog.EXPORT_SYSMANAGElOG, als.size(), null, null, null, null, null, false),
					request);

		}
		return jrw;
	}

	/**
	 * 下载导入导出的文件
	 *
	 * @return
	 */
	@RequestMapping("/downFile")
	public void downFile(@RequestParam String url, @RequestParam Integer type, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (type == 1) {// 导入导出文件
			DownLoadFile.downLoadFile(url, ("导入导出文件"), request, response);
		} else {// 授权文件
			DownLoadFile.downLoadFile(url, ("授权文件"), request, response);
		}
	}

}
