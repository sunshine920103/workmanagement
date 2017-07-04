package com.workmanagement.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workmanagement.dao.SystemUserRightDao;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysPathRole;
import com.workmanagement.model.SysRole;
import com.workmanagement.model.SysUser;
import com.workmanagement.service.DefaultIndexItemService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysMenuService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysRoleService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.PageSupport;

@Controller

@RequestMapping("/admin/menuAdd")
public class MenuAddController {

	private static final String SYS_MENU = "菜单管理";
	@Autowired
	private SysManageLogService sysManageLogService;
	
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysOrgService sysOrgService;
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request){
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> map = new HashMap<>();
		map.put("type", 1);
		model.addAttribute("menu", sysMenuService.queryMenu(map, ps));
		return "MenuAdd/list";
	}
	
	@RequestMapping("/add")
	public String add(Model model,HttpServletRequest request,Integer id){

		Map<String, Object> ma = new HashMap<>();
		ma.put("path", "123");
		model.addAttribute("list", sysMenuService.queryMenu(ma, null));
		if(id!=null){
			Map<String, Object> map = new HashMap<>();
			map.put("menuId", id);
			model.addAttribute("menu", sysMenuService.queryMenu(map, null).get(0));
			map.put("menuId", sysMenuService.queryMenu(map, null).get(0).getSys_menu_parent_id());
			model.addAttribute("parent", sysMenuService.queryMenu(map, null).get(0));
			model.addAttribute("poptype",null);
		}else{
			model.addAttribute("poptype","add");
		}
		return "MenuAdd/add";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonResWrapper save(HttpServletRequest request, @RequestParam(required = false) String tp,
			@RequestParam(required = false) String menuname,
			@RequestParam(required = false) Integer shangmenu,
			@RequestParam(required = false) String menuurl,
			@RequestParam(required = false) Integer weight){

		JsonResWrapper jrw = new JsonResWrapper();
		if(!StringUtils.isEmpty(tp)){
			Map<String, Object> map = new HashMap<>();
			map.put("menuId", Integer.parseInt(tp));

			SysMenu menu1 = sysMenuService.queryMenu(map, null).get(0);
			SysMenu menu = sysMenuService.queryMenu(map, null).get(0);
			menu.setSys_menu_name(menuname);
			menu.setSys_menu_parent_id(shangmenu);
			menu.setSys_menu_path(menuurl);
			menu.setSys_menu_weight(weight);
			sysMenuService.updateMenu(menu);
			jrw.setMessage("保存成功");
			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU,null,null, null,menu1.toString(),menu.toString(),null,SysManageLog.UPDATE_SYSMANAGElOG,null,null,null,null,null,null,true),request);
			}else{
			SysMenu men =sysMenuService.queryMenuName(menuname);
			if(men==null){
				MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				SysMenu menu = new SysMenu();
				menu.setSys_menu_name(menuname);
				menu.setSys_menu_parent_id(shangmenu);
				menu.setSys_menu_path(menuurl);
				menu.setSys_menu_weight(weight);
				menu.setSys_menu_icon("");
				sysMenuService.insertMenu(menu);

				SysMenu m =sysMenuService.queryMenuName(menuname);
				SysRole role = sysRoleService.querySystemRoleById(userDetails.getSys_role_id());
				
				role.setSys_role_duties(role.getSys_role_duties()+","+menuname);
				sysRoleService.updateSysRole(role);
				
				sysMenuService.insertRoleMenu(userDetails.getSys_role_id(),m.getSys_menu_id());
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU,null,null, null,null,menu.toString(),null,SysManageLog.INSERT_SYSMANAGElOG,null,null,null,null,null,null,true),request);
				
				jrw.setMessage("保存成功");
			}else{
				jrw.setMessage("保存失败,菜单名已存在");
				sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU,null,null, null,null,null,null,SysManageLog.INSERT_SYSMANAGElOG,null,null,null,null,null,null,false),request);
				
			}
		}
		return jrw;
	}
	@RequestMapping(value = "/delete")
	@ResponseBody
	public JsonResWrapper delete(Integer id,HttpServletRequest request){
		JsonResWrapper jrw = new JsonResWrapper();
		Map<String, Object> map = new HashMap<>();
		map.put("menuId", id);

		SysMenu menu1 = sysMenuService.queryMenu(map, null).get(0);
		try {
			
			sysMenuService.deleteMenu(id);
			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU,menu1.getSys_menu_name(),null, null,null,null,null,SysManageLog.DELECT_SYSMANAGElOG,null,null,null,null,null,null,true),request);
			
			jrw.setMessage("删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			jrw.setMessage("删除失败");
			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU,menu1.getSys_menu_name(),null, null,null,null,null,SysManageLog.DELECT_SYSMANAGElOG,null,null,null,null,null,null,false),request);
			
		}
		return jrw;
	}
	
	@RequestMapping("/getOrgCode")
	@ResponseBody
	public String getOrgCode(Integer id) throws Exception{
		
		return sysOrgService.getByIdNotHaveSub(id).getSys_org_financial_code();
	}
	
	@RequestMapping("/getOrgName")
	@ResponseBody
	public String getOrgName(Integer id) throws Exception{
		
		return sysOrgService.getByIdNotHaveSub(id).getSys_org_name();
	}

	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysUserService sysUserService;
	
	@RequestMapping("/getcride")
	@ResponseBody
	public String getcride(Integer id){
		
		return defaultIndexItemService.queryById(id).getCodeCredit();
	}

	@Autowired
	DefaultIndexItemService defaultIndexItemService;
	@RequestMapping("/getcode")
	@ResponseBody
	public String getcode(Integer id){

		return defaultIndexItemService.queryById(id).getCodeOrg();
	}
}
