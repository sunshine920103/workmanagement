package com.workmanagement.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.reflect.TypeToken;
import com.workmanagement.dao.SysManageLogDao;
import com.workmanagement.dao.SystemUserRightDao;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysRole;
import com.workmanagement.model.SysUser;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysManageLogServiceImpl;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysRoleService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.DataValidation;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.LoggerUtil;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;
import com.workmanagement.util.SortArea;
import com.workmanagement.util.SubDataGet;

/**
 * 用户管理
 * 
 * @author lzl
 */
@Controller
@RequestMapping("/admin/sysUser")
public class SysUserController {

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SystemUserRightDao systemUserRightDao;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysManageLogService sysManageLogService;

	private static final String SYS_MENU = "用户管理";

	/**
	 * 首页
	 * 
	 * @param reqeust
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request, Model model) {
		// 当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String orgKey = RedisKeys.SYS_USER_ORG + userDetails.getSys_org_id();

		// 查询角色列表
		SysRole sr = sysRoleService.querySystemRoleById(userDetails.getRoleIds().get(0));
		boolean isPepole = (sr.getSys_role_type() == SysRole.PEPOLE_ADMIN
				|| sr.getSys_role_type() == SysRole.PEPOLE_QUERY || sr.getSys_role_type() == SysRole.PEPOLE_REPORT);
		Map<String, Object> map = new HashMap<String, Object>();

		if (isPepole)
			map.put("pepole", null);
		else
			map.put("pepole", "58161");
		List<SysRole> srs = sysRoleService.querySystemRoles(map);
		model.addAttribute("srs", srs);
		

		return "sysUser/index";
	}

	/**
	 * 列表页面
	 * 
	 * @param reqeust
	 * @param model
	 * @param key
	 *            输入框查询条件
	 * @return
	 */
	@RequestMapping("/list")
	public String add(HttpServletRequest request, Model model, @RequestParam(required = false) String key,
			@RequestParam(required = false) Integer orgName, @RequestParam(required = false) String roleName) {
		// 当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SysUser su = sysUserService.querySystemUserById(userDetails.getSys_user_id());

		StringBuffer stringBuffer = new StringBuffer(
				"SELECT su.*, sr.sys_role_name AS sys_user_role_name, sr.sys_role_id AS sys_role_id,i.sys_org_name AS sys_user_org_name FROM sys_user_tb su LEFT JOIN sys_role_user_tb sru ON su.sys_user_id = sru.sys_user_id LEFT JOIN sys_role_tb sr ON sr.sys_role_id = sru.sys_role_id LEFT JOIN sys_org_tb i ON su.sys_org_id = i.sys_org_id LEFT JOIN sys_area_tb a ON i.sys_area_id = a.sys_area_id WHERE 1=1  AND su.sys_user_id != "
						+ userDetails.getSys_user_id());

		List<SysUser> sus = new ArrayList<>();

		try {

			// 查询用户列表
			PageSupport ps = PageSupport.initPageSupport(request);
			Map<String, Object> param = new HashMap<String, Object>();
			// 查询的时候排除当前用户
			param.put("self", userDetails.getSys_user_id());

			if (orgName!=null) {
				SysOrg so = sysOrgService.queryInstitutionsById(orgName);
				if (so != null) {
					StringBuffer sb = new StringBuffer();
					DataUtil.getChildOrgIds(so, sb);
					param.put("orgArea", sb.toString().split(","));
					stringBuffer.append(" and i.sys_org_id IN(");
					String[] s = sb.toString().split(",");
					for (int i = 0; i < sb.toString().split(",").length; i++) {
						stringBuffer.append(s[i] + ",");
					}
					stringBuffer = new StringBuffer(stringBuffer.substring(0, stringBuffer.length() - 1));
					stringBuffer.append(") ");
				}
				model.addAttribute("orgName", so);
			} else {

				SysRole sr = sysRoleService.querySystemRoleById(userDetails.getRoleIds().get(0));
				boolean isPepole = (sr.getSys_role_type() == SysRole.PEPOLE_ADMIN);
				Map<String, Object> map = new HashMap<String, Object>();

				if (isPepole)
					map.put("pepole", null);
				else
					map.put("pepole", "58161");
				List<SysRole> srs = sysRoleService.querySystemRoles(map);
				model.addAttribute("srs", srs);
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
							param.put("orgArea", sb1.toString().split(","));
							stringBuffer.append(" and i.sys_org_id IN(");
							String[] s1 = sb1.toString().split(",");
							for (int i = 0; i < sb1.toString().split(",").length; i++) { 
								stringBuffer.append(s1[i] + ",");
							}
							stringBuffer = new StringBuffer(stringBuffer.substring(0, stringBuffer.length() - 1));
							stringBuffer.append(") ");
						}else{
							param.put("orgIn", sysArea1.getSysAreaId());
							stringBuffer.append("and i.sys_org_id in (select sys_org_id from sys_org_tb where SYS_ORG_AFFILIATION_AREA_ID = "+sysArea1.getSysAreaId()+")");
						}
					}
				} else {

					SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());

					StringBuffer sb = new StringBuffer();
					DataUtil.getChildOrgIds(so, sb);
					map.put("orgIds", sb.toString().split(","));

					map.put("upid", "222");
					List<SysOrg> is = sysOrgService.queryInstitution(map);

					StringBuffer sb1 = new StringBuffer();
					for (SysOrg sysOrg : is) {
						sb1.append(sysOrg.getSys_org_id() + ",");
					}
					param.put("orgArea", sb.toString().split(","));

					stringBuffer.append(" and i.sys_org_id IN(");
					String[] s1 = sb1.toString().split(",");
					for (int i = 0; i < sb1.toString().split(",").length; i++) {
						stringBuffer.append(s1[i] + ",");
					}
					stringBuffer = new StringBuffer(stringBuffer.substring(0, stringBuffer.length() - 1));
					stringBuffer.append(") ");

				}
			}

			if (!StringUtils.isBlank(key)) {
				model.addAttribute("key", key);
				param.put("key", key);
				stringBuffer.append(" and (su.sys_user_name like '%" + key + "%' OR su.username like '%" + key + "%')");
			}
			if (!StringUtils.isBlank(roleName)) {
				model.addAttribute("roleName", roleName);
				param.put("role_id", sysRoleService.querySystemRoleByName(roleName).getSys_role_id());

				stringBuffer.append(
						" and sr.sys_role_id = " + sysRoleService.querySystemRoleByName(roleName).getSys_role_id());
			}
			sus = DataUtil.isEmpty(sysUserService.querySystemUsers(param, ps));
			model.addAttribute("sus", sus);
			if (!StringUtils.isBlank(key) || !StringUtils.isBlank(roleName) || orgName!=null)
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null, null, null,
						new Date(), SysManageLog.SELECT_SYSMANAGElOG, sus == null ? 0 : sus.size(),
						(StringUtils.isBlank(key) ? "" : "用户代码或用户名称:" + key + ",")
								+ (orgName!=null? "" : "机构名称:" + orgName + ",")
								+ (StringUtils.isBlank(roleName) ? "" : "角色名称:" + roleName),
						stringBuffer.toString(), "/admin/sysUser/sysQuery.jhtml", null, null, true),request);

		} catch (Exception e) {
			// TODO: handle exception
			if (key != null || roleName != null || orgName != null)
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null, null, null,
						new Date(), SysManageLog.SELECT_SYSMANAGElOG, sus == null ? 0 : sus.size(),
						(StringUtils.isBlank(key) ? "" : "用户代码或用户名称:" + key + ",")
								+ (orgName!=null ? "" : "机构名称:" + orgName + ",")
								+ (StringUtils.isBlank(roleName) ? "" : "角色名称:" + roleName),
						stringBuffer.toString(), "/admin/sysUser/sysQuery.jhtml", null, null, false),request);

		} 
			return "sysUser/list";
		
	}

	/**
	 * 管理员查看的查询结果集
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/sysQuery")
	public String sysQuery(Model model, HttpServletRequest request, HttpSession session) throws Exception {
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
		PageSupport ps = PageSupport.initPageSupport(request);

		List<SysUser> sus = DataUtil.isEmpty(sysUserService.querySql(map, ps));
		model.addAttribute("sus", sus);
		return "sysUser/list";
	}

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

	/**
	 * 重置密码
	 * 
	 * @param id
	 *            用户ID
	 * @return
	 */
	@RequestMapping("/resetPwd")
	@ResponseBody
	public JsonResWrapper modify_pwd(HttpServletRequest request, @RequestParam(required = false) Integer id) {
		JsonResWrapper jrw = new JsonResWrapper();
		PasswordEncoder spe = new BCryptPasswordEncoder();
		SysUser su = sysUserService.querySystemUserById(id);
	

		try {
			if (su != null) {
				su.setPassword(spe.encode(SysUser.DEFUALT_PWD));
				su.setSys_user_error(SysUser.DEFUALT_ERR);
				try {
					// sysManageLogService.insertSysManage("用户管理",
					// su.getUsername(), null, null, "", "",
					// SysManageLog.UPDATE_SYSMANAGElOG, "", true);
					sysManageLogService.insertSysManageLogTb(
							new SysManageLog(SYS_MENU, su.getUsername(), null, null, null, null, new Date(),
									SysManageLog.UPDATE_SYSMANAGElOG, null, "重置密码", null, null, null, null, true),request);
					sysUserService.updateSystemUser(su);
					jrw.setMessage("重置密码成功");

				} catch (Exception e) {
					// sysManageLogService.insertSysManage("用户管理",
					// su.getUsername(), null, null, "", "",
					// SysManageLog.UPDATE_SYSMANAGElOG, "", false);
					sysManageLogService.insertSysManageLogTb(
							new SysManageLog(SYS_MENU, su.getUsername(), null, null, null, null, new Date(),
									SysManageLog.UPDATE_SYSMANAGElOG, null, "重置密码", null, null, null, null, false),request);

				}
			} else {
				jrw.setFlag(false);
				jrw.setMessage("重置密码失败");
				sysManageLogService.insertSysManageLogTb(
						new SysManageLog(SYS_MENU, su.getUsername(), null, null, null, null, new Date(),
								SysManageLog.UPDATE_SYSMANAGElOG, null, "重置密码", null, null, null, null, false),request);

			}
		} catch (Exception e) {
			jrw.setFlag(false);
			jrw.setMessage("系统异常");
			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, su.getUsername(), null, null, null,
					null, new Date(), SysManageLog.UPDATE_SYSMANAGElOG, null, "重置密码", null, null, null, null, false),request);

			LoggerUtil.error(e);

		}
		return jrw;
	}

	/**
	 * 锁定/重置用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/lock")
	@ResponseBody
	public JsonResWrapper lock(HttpServletRequest request, @RequestParam(required = false) Integer id,
			@RequestParam(required = false) Boolean status) {
		JsonResWrapper jrw = new JsonResWrapper();
		SysUser su = sysUserService.querySystemUserById(id);
		

		try {
			String msg = null;

			if (id == null) {

				msg = "用户" + (status ? "解锁" : "锁定") + "失败，参数缺失";
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, su.getUsername(), null, null,
						!status ? "解锁" : "锁定", status ? "解锁" : "锁定", new Date(), SysManageLog.UPDATE_SYSMANAGElOG, null,
						"锁定/解锁用户", null, null, null, null, false),request);

				jrw.setFlag(false);
				jrw.setMessage(msg);
				return jrw;
			}

			su.setSys_user_id(id);
			su.setEnabled(status);

			// 启用的时候需要把密码这位默认密码
			if (status) {
				PasswordEncoder spe = new BCryptPasswordEncoder();
				su.setPassword(spe.encode(SysUser.DEFUALT_PWD));
				su.setSys_user_error(SysUser.DEFUALT_ERR);
			}

			msg = "用户" + (status ? "解锁" : "锁定") + "成功";

			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, su.getUsername(), null, null,
					!status ? "解锁" : "锁定", status ? "解锁" : "锁定", new Date(), SysManageLog.UPDATE_SYSMANAGElOG, null,
					"锁定/解锁用户", null, null, null, null, true),request);
			sysUserService.updateSystemUser(su);
			jrw.setMessage(msg);

		} catch (Exception e) {
			LoggerUtil.error(e);

			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, su.getUsername(), null, null,
					!status ? "解锁" : "锁定", status ? "解锁" : "锁定", new Date(), SysManageLog.UPDATE_SYSMANAGElOG, null,
					"锁定/解锁用户", null, null, null, null, false),request);
		}

		return jrw;
	}

	/**
	 * 重置用户查询次数
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/limit")
	@ResponseBody
	public JsonResWrapper limit(HttpServletRequest request, @RequestParam(required = false) Integer id) {
		JsonResWrapper jrw = new JsonResWrapper();
		SysUser su = sysUserService.querySystemUserById(id);
	
		try {
			if (su != null) {
				su.setSys_user_query_times(0);
				sysUserService.updateSystemUser(su);
				sysManageLogService.insertSysManageLogTb(
						new SysManageLog(SYS_MENU, su.getUsername(), null, null, null, null, new Date(),
								SysManageLog.UPDATE_SYSMANAGElOG, null, "重置查询次数", null, null, null, null, true),request);

				jrw.setMessage("重置查询次数成功");
				return jrw;
			} else {
				sysManageLogService.insertSysManageLogTb(
						new SysManageLog(SYS_MENU, su.getUsername(), null, null, null, null, new Date(),
								SysManageLog.UPDATE_SYSMANAGElOG, null, "重置查询次数", null, null, null, null, false),request);
				jrw.setFlag(false);
				jrw.setMessage("重置查询次数失败");
				return jrw;
			}
		} catch (Exception e) {

			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, su.getUsername(), null, null, null,
					null, new Date(), SysManageLog.UPDATE_SYSMANAGElOG, null, "重置查询次数", null, null, null, null, false),request);
			jrw.setFlag(false);
			jrw.setMessage("系统异常");
			LoggerUtil.error(e);

		}
		return jrw;
	}

	/**
	 * 添加、修改页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/add")
	public String add(Model model, @RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer posi) {// 标记进入的位置 true
															// 用户管理页面， flase
															// 修改页面
		model.addAttribute("posi", posi);
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (id != null) {
			SysUser su = sysUserService.querySystemUserById(id);
			model.addAttribute("su", su);
		}

		SysRole sr = sysRoleService.querySystemRoleById(userDetails.getRoleIds().get(0));
		boolean isPepole = (sr.getSys_role_type() == SysRole.PEPOLE_ADMIN
				|| sr.getSys_role_type() == SysRole.PEPOLE_QUERY || sr.getSys_role_type() == SysRole.PEPOLE_REPORT);
		Map<String, Object> map = new HashMap<String, Object>();

		if (isPepole)
			map.put("pepoles", "222");
		else
			map.put("pepole", "58161");
		List<SysRole> srs = sysRoleService.querySystemRoles(map);
		model.addAttribute("srs", srs);
		if (isPepole) {
			SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			model.addAttribute("org", so);
			Map<String, Object> m = new HashMap<>();
			m.put("id", so.getSys_org_id());
			// List<Integer> i =new ArrayList<>();
			// SysArea ar = sysAreaService.queryAreaById(so.getSys_area_id());
			// SubDataGet su = new SubDataGet();
			// su.getAllAreaIds(ar,i);

			m.put("area", so.getSys_area_id());
			List<SysOrg> is = sysOrgService.queryOrgOrSubOrgById(m);
			model.addAttribute("is", is);
			Map<String, Object> maps = new HashMap<>();
			maps.put("id", so.getSys_org_id());
			List<SysOrg> chi = sysOrgService.queryOrgOrSubOrgById(maps);
			model.addAttribute("orgKey", chi);
		} else {
			SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			model.addAttribute("org", so);
			Map<String, Object> m = new HashMap<>();
			m.put("id", so.getSys_org_id());
			// List<Integer> i =new ArrayList<>();
			// SysArea ar = sysAreaService.queryAreaById(so.getSys_area_id());
			// SubDataGet su = new SubDataGet();
			// su.getAllAreaIds(ar,i);
			List<SysOrg> is = sysOrgService.queryOrgOrSubOrgById(m);
			model.addAttribute("is", is);

			Map<String, Object> maps = new HashMap<>();
			maps.put("id", so.getSys_org_id());
			List<SysOrg> chi = sysOrgService.queryOrgOrSubOrgById(maps);
			model.addAttribute("orgKey", chi);
		}
		return "sysUser/add";
	}

	/**
	 * 保存添加、修改
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonResWrapper save(Model model, HttpServletRequest request, @RequestParam(required = false) Integer posi, // 标记进入的位置
																														// true
																														// 用户管理页面，
																														// flase
																														// 修改页面
			@RequestParam(required = false) Integer sys_user_id, @RequestParam(required = false) String username,
			@RequestParam(required = false) String sys_user_name, @RequestParam(required = false) String sys_user_card,
			@RequestParam(required = false) String sys_user_contacts,
			@RequestParam(required = false) String sys_user_phone,
			@RequestParam(required = false) Integer sys_user_org_id,
			@RequestParam(required = false) String sys_user_org_name,
			@RequestParam(required = false) Integer sys_user_role_id,
			@RequestParam(required = false) String sys_user_role_name,
			@RequestParam(required = false) String sys_user_notes) {
		model.addAttribute("posi", posi);
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		JsonResWrapper jrw = new JsonResWrapper();
		
		try {
			SysUser su = new SysUser();
			su.setSys_user_id(sys_user_id);
			su.setUsername(username);
			su.setSys_user_name(sys_user_name);
			su.setSys_user_card(sys_user_card);
			su.setSys_create_user_id(userDetails.getSys_user_id());
			su.setSys_user_contacts(sys_user_contacts);
			su.setSys_user_phone(sys_user_phone);
			su.setSys_user_notes(sys_user_notes);
			su.setSys_org_id(sys_user_org_id);
			su.setSys_role_id(sys_user_role_id);
			su.setSys_user_error(0);

			if (StringUtils.isBlank(username) || StringUtils.isBlank(sys_user_name)
					|| StringUtils.isBlank(sys_user_card) || StringUtils.isBlank(sys_user_contacts)
					|| sys_user_org_id == null || sys_user_role_id == null) {
				jrw.setFlag(false);
				jrw.setMessage("保存失败，参数缺失");
				return jrw;
			}

			if (!DataValidation.isMobileOrPhone(sys_user_contacts)) {
				jrw.setFlag(false);
				jrw.setMessage("联系方式格式错误");
				return jrw;
			}
			if (!DataValidation.isIdcard(sys_user_card)) {
				jrw.setFlag(false);
				jrw.setMessage("身份证格式错误");
				return jrw;
			}

			
			SysUser sysUser = systemUserRightDao.queryUserDetailsByUsername(username);
			if (sys_user_id != null && sysUser != null
					&& sysUser.getSys_user_id().intValue() != sys_user_id.intValue()) {
				jrw.setFlag(false);
				jrw.setMessage("登录名已存在");
				return jrw;
			} else if (sys_user_id == null && sysUser != null) {
				jrw.setFlag(false);
				jrw.setMessage("登录名已存在");
				return jrw;
			}
			List<SysUser> sy = sysUserService.queryCard(su.getSys_user_card());
			if(sys_user_id!=null && !CollectionUtils.isEmpty(sy)&&sysUser!=null){
				if(!sysUser.getSys_user_card().equals(sy.get(0).getSys_user_card())){
					jrw.setFlag(false);
					jrw.setMessage("身份证号码已存在");
					return jrw;
				}
			}
			if (sys_user_id == null) {
				if(!CollectionUtils.isEmpty(sy)){
					jrw.setFlag(false);
					jrw.setMessage("身份证号码已存在");
					return jrw;
				}
				SysRole sr = sysRoleService.querySystemRoleById(sys_user_role_id);
				if (sr.getSys_role_type() == 1 || sr.getSys_role_type() == 4) {
					Map<String, Object> m = new HashMap<>();
					m.put("sql",
							"SELECT * FROM SYS_USER_TB,SYS_ROLE_TB WHERE sys_user_tb.SYS_ROLE_ID = SYS_ROLE_tb.SYS_ROLE_ID AND SYS_ROLE_TYPE = "
									+ sr.getSys_role_type() + "  AND SYS_ORG_ID = " + sys_user_org_id);
					List<SysUser> u = sysUserService.querySql(m, null);
					if (!CollectionUtils.isEmpty(u)) {
						jrw.setFlag(false);
						jrw.setMessage("此机构已存在管理员");
						return jrw;
					}
				}
			}
			sysUserService.saveSystemUser(su, sys_user_role_id,request);
			jrw.setMessage("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.error(e);

		}
		return jrw;
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/del")
	@ResponseBody
	public JsonResWrapper del(HttpServletRequest request, @RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer statu) {
		JsonResWrapper jrw = new JsonResWrapper();

		try {
			if (id == null) {
				jrw.setFlag(false);
				jrw.setMessage("停用失败，参数缺失");
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null, null, null,
						new Date(), SysManageLog.DELECT_SYSMANAGElOG, null, "删除用户", null, null, null, null, false),request);

				return jrw;
			}

			SysUser su = sysUserService.querySystemUserById(id);

			if (su == null) {
				jrw.setFlag(false);
				jrw.setMessage("停用失败，停用的用户不存在");
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null, null, null,
						new Date(), SysManageLog.DELECT_SYSMANAGElOG, null, "停用用户", null, null, null, null, false),request);

				return jrw;
			}
		
			if (statu == 1) {
				// 删除用户
				su.setSys_delete(0);
				sysUserService.updateSystemUser(su);
				sysManageLogService.insertSysManageLogTb(
						new SysManageLog(SYS_MENU, null, null, null, su.toString(), null, new Date(),
								SysManageLog.UPDATE_SYSMANAGElOG, null, "停用用户", null, null, null, null, true),request);

				jrw.setMessage("停用成功");
			} else {
				SysRole sr = sysRoleService.querySystemRoleById(su.getSys_role_id());
				if(sr.getSys_role_type().intValue()==1 || sr.getSys_role_type().intValue()==4){
				Map<String, Object> m = new HashMap<>();
				m.put("sql",
						"SELECT * FROM SYS_USER_TB,SYS_ROLE_TB WHERE sys_user_tb.SYS_ROLE_ID = SYS_ROLE_tb.SYS_ROLE_ID AND SYS_ROLE_TYPE = "
								+ sr.getSys_role_type() + "  AND SYS_ORG_ID = " + su.getSys_org_id()
								+ " and sys_delete = 1");
				List<SysUser> u = sysUserService.querySql(m, null);
				
				if (!CollectionUtils.isEmpty(u)) {
					jrw.setFlag(false);
					jrw.setMessage("此机构已存在管理员,无法启用");
					return jrw;
				} 
				
				}

				List<SysUser> sy = sysUserService.queryCard(su.getSys_user_card());
				if(!CollectionUtils.isEmpty(sy)){
					jrw.setFlag(false);
					jrw.setMessage("身份证号码已被使用，无法启用");
					return jrw;
				}
					su.setSys_delete(1);
					sysUserService.updateSystemUser(su);
					sysManageLogService.insertSysManageLogTb(
							new SysManageLog(SYS_MENU, null, null, null, su.toString(), null, new Date(),
									SysManageLog.UPDATE_SYSMANAGElOG, null, "停用用户", null, null, null, null, true),request);

					jrw.setMessage("启用成功");
				
			}
			//
			// Map<String, Object> param = new HashMap<String, Object>();
			// param.put("insti_id", su.getSys_org_id());
			// List<SysUser> sus = sysUserService.querySystemUsers(param, null);
			//
			// // 判断被删除的用户所在机构下是否还有用户，如果没有则把机构的使用状态改为未使用
			// if (CollectionUtils.isEmpty(sus)) {
			// SysOrg so =
			// sysOrgService.queryInstitutionsById(su.getSys_org_id());
			// so.setSys_org_used(false);
			// sysOrgService.updateSysOrg(so);
			// }
			//
			// param = new HashMap<String, Object>();
			// param.put("method", 2);
			// param.put("role_id", su.getSys_role_id());
			// sus = sysUserService.querySystemUsers(param, null);
			// // 判断被删除的用户角色是否还有用户使用， 如果没有则把角色的使用状态改为未使用
			// if (CollectionUtils.isEmpty(sus)) {
			// SysRole sr = new SysRole();
			// sr.setSys_role_id(su.getSys_role_id());
			// sysRoleService.updateSysRole(sr);
			// }
			//

		} catch (Exception e) {
			LoggerUtil.error(e);

		}

		return jrw;
	}

	/**
	 * 检查用户机构
	 * 
	 * @param id
	 *            机构ID
	 * @return
	 */
	@RequestMapping("/check")
	@ResponseBody
	public JsonResWrapper check(HttpServletRequest request, @RequestParam(required = false) Integer id,
			@RequestParam(required = false) Boolean status) {
		JsonResWrapper jrw = new JsonResWrapper();

		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SysRole sr = sysRoleService.querySystemRoleById(userDetails.getRoleIds().get(0));
		boolean isPepole = (sr.getSys_role_type() == SysRole.PEPOLE_ADMIN
				|| sr.getSys_role_type() == SysRole.PEPOLE_QUERY || sr.getSys_role_type() == SysRole.PEPOLE_REPORT);

		// 查询角色列表
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pepole", isPepole);
		List<SysRole> srs = sysRoleService.querySystemRoles(map);
		Iterator<SysRole> iterator = srs.iterator();

		// 获取选中的机构
		SysOrg so = sysOrgService.queryInstitutionsById(id);
		// 获取机构的本级及下级ID
		StringBuffer sb = new StringBuffer();
		DataUtil.getChildOrgIds(userDetails.getSysOrg(), sb);

		// 判断登录用户的角色是否是人行
		if (isPepole) {
			// 判断是否是在本机构下新增用户，是，去掉人、商管理员
			if (so.getSys_org_id().intValue() == userDetails.getSys_org_id().intValue()) {
				while (iterator.hasNext()) {
					SysRole role = iterator.next();
					if (role.getSys_role_type().intValue() == SysRole.PEPOLE_ADMIN.intValue()
							|| role.getSys_role_type().intValue() == SysRole.MER_ADMIN.intValue()) {
						iterator.remove();
					}
				}
				// 判断是否是在本机构的下级机构新增用户,是去掉人、商查询员和报送员
			} else if (sb.toString().indexOf(so.getSys_org_id().toString()) != -1) {
				while (iterator.hasNext()) {
					SysRole role = iterator.next();
					if (role.getSys_role_type().intValue() != SysRole.PEPOLE_ADMIN.intValue()
							&& role.getSys_role_type().intValue() != SysRole.MER_ADMIN.intValue()) {
						iterator.remove();
					}
				}
				// 在其他机构新增用户，只能新增人、商管理员
			} else {
				while (iterator.hasNext()) {
					SysRole role = iterator.next();
					if (role.getSys_role_type().intValue() != SysRole.PEPOLE_ADMIN.intValue()
							&& role.getSys_role_type().intValue() != SysRole.MER_ADMIN.intValue()) {
						iterator.remove();
					}
				}
			}

		} else {

			// 判断是否是在本机构下新增用户，是，去掉管理员
			if (so.getSys_org_id().intValue() == userDetails.getSys_org_id().intValue()) {
				while (iterator.hasNext()) {
					SysRole role = iterator.next();
					if (role.getSys_role_type().intValue() == SysRole.MER_ADMIN.intValue()) {
						iterator.remove();
					}
				}
				// 判断是否是在本机构的下级机构新增用户,是去掉查询员和报送员
			} else if (sb.toString().indexOf(so.getSys_org_id().toString()) != -1) {
				while (iterator.hasNext()) {
					SysRole role = iterator.next();
					if (role.getSys_role_type().intValue() != SysRole.MER_ADMIN.intValue()) {
						iterator.remove();
					}
				}
			}
		}

		jrw.setData(srs);
		return jrw;
	}

	/**
	 * 导出EXCEL
	 * 
	 * @param model
	 * @param id
	 *            机构ID
	 * @return
	 */
	@RequestMapping("/export")
	@ResponseBody
	public void downLoadFile(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(required = false) String key, @RequestParam(required = false) Integer orgName,
			@RequestParam(required = false) String roleName) {
		// 当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SysUser su = sysUserService.querySystemUserById(userDetails.getSys_user_id());
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			// 查询的时候排除当前用户
			param.put("self", userDetails.getSys_user_id());

			if (orgName!=null) {
				SysOrg so = sysOrgService.queryInstitutionsById(orgName);
				if (so != null) {
					StringBuffer sb = new StringBuffer();
					DataUtil.getChildOrgIds(so, sb);
					param.put("orgArea", sb.toString().split(","));
					}
				model.addAttribute("orgName", so);
			} else {

				SysRole sr = sysRoleService.querySystemRoleById(userDetails.getRoleIds().get(0));
				boolean isPepole = (sr.getSys_role_type() == SysRole.PEPOLE_ADMIN);
				Map<String, Object> map = new HashMap<String, Object>();

				if (isPepole)
					map.put("pepole", null);
				else
					map.put("pepole", "58161");
				List<SysRole> srs = sysRoleService.querySystemRoles(map);
				model.addAttribute("srs", srs);
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
							param.put("orgArea", sb1.toString().split(","));
							}else{
							param.put("orgIn", sysArea1.getSysAreaId());
						}
					}
				} else {

					SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());

					StringBuffer sb = new StringBuffer();
					DataUtil.getChildOrgIds(so, sb);
					map.put("orgIds", sb.toString().split(","));

					map.put("upid", "222");
					List<SysOrg> is = sysOrgService.queryInstitution(map);

					StringBuffer sb1 = new StringBuffer();
					for (SysOrg sysOrg : is) {
						sb1.append(sysOrg.getSys_org_id() + ",");
					}
					param.put("orgArea", sb.toString().split(","));

					
			}

			if (!StringUtils.isBlank(key)) {
				param.put("key", key);
			}
			if (!StringUtils.isBlank(roleName)) {
				param.put("role_id", sysRoleService.querySystemRoleByName(roleName).getSys_role_id());

			}
			List<SysUser> sus = DataUtil.isEmpty(sysUserService.querySystemUsers(param, null));
			for (SysUser sysUser : sus) {
				SysUser s = sysUserService.querySystemUserById(sysUser.getSys_create_user_id());
				if(s!=null)
					sysUser.setAdd(s.getUsername());
			}
			String[] rowNames = { "登录名", "姓名", "证件号", "联系方式", "角色名称", "机构名称","创建人","备注", "状态", "上次登录ip", "上次登录时间",
					"最后登录ip", "最后登录时间", "是否停用"};
			String[] propertyNames = { "username", "sys_user_name", "sys_user_card", "sys_user_contacts",
					"sys_user_role_name", "sys_user_org_name","add", "sys_user_notes", "abl",
					"sys_user_login_ip", "beginTime", "sys_user_last_login_ip", "endTime","delete" };

			// 生成excel
			ExcelExport<SysUser> excelExport = new ExcelExport<>();
			excelExport.setTitle("用户管理");
			excelExport.setRowNames(rowNames);
			excelExport.setPropertyNames(propertyNames);
			excelExport.setList(sus);
			String file = excelExport.exportExcel(request, response);
			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null, null, null,
					new Date(), SysManageLog.EXPORT_SYSMANAGElOG, sus.size(), null, null, null, file, null, true),request);

		
			}
			} catch (Exception e) {
			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null, null, null,
					new Date(), SysManageLog.EXPORT_SYSMANAGElOG, 0, null, null, null, null, null, false),request);

			LoggerUtil.error(e);

		}
	}

	@RequestMapping("/getRoleId")
	@ResponseBody
	public String getRoleId(Integer id) {
		return sysRoleService.getRoleIdByType(id);

	}
	
	@RequestMapping("/getUserOrg")
	@ResponseBody
	public Integer getUserOrg(Integer id) {
		return sysUserService.querySystemUserById(id).getSys_org_id();

	}
	
	@RequestMapping("/edit")
	public String edit(Integer id,Model model) throws Exception{
		SysUser su = sysUserService.querySystemUserById(id);
		model.addAttribute("su", su);
		SysArea sa = sysAreaService.getAreaNotSub(
		sysOrgService.getByIdNotHaveSub(su.getSys_org_id()).getSys_org_affiliation_area_id());
		model.addAttribute("sa", sa);
		SysRole sr = sysRoleService.querySystemRoleById(su.getSys_role_id());
		model.addAttribute("sr", sr);
		return "sysUser/edit";
		
	}
}
