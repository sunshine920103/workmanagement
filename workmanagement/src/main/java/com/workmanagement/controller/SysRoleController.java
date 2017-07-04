package com.workmanagement.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysRole;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysRoleService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;

/**
 * 角色管理
 * @author xiehao
 */
@Controller
@RequestMapping("/admin/sysRole")
public class SysRoleController {
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysManageLogService sysManageLogService;
	@Autowired
	private SysOrgService sysOrgService;
	/**
	 * 添加管理日志
	 * @param logCount  操作影响行数
	 * @param operateType 操作类型（增删改）
	 * @param logResult  操作结果 0 失败 1 成功
	 * @param sysManageLogNewValue   修改后的值
	 * @param sysManageLogOldValue   修改前的值
	 */
	public void manageLog(HttpServletRequest request,Integer logCount,Integer operateType,boolean logResult,String sysManageLogNewValue,String sysManageLogOldValue){
		//添加管理日志
		SysManageLog sysManageLog=new SysManageLog();
		sysManageLog.setSysManageLogMenuName("权限管理");
		sysManageLog.setSysManageLogCount(logCount);
		sysManageLog.setSysManageLogOperateType(operateType);
		sysManageLog.setSysManageLogResult(logResult);
		sysManageLog.setSysManageLogNewValue(sysManageLogNewValue);
		sysManageLog.setSysManageLogOldValue(sysManageLogOldValue);
		sysManageLogService.insertSysManageLogTb(sysManageLog,request);
	}
	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	@RequestMapping("/del")
	@ResponseBody
	public JsonResWrapper del(@RequestParam(required=false) Integer id,HttpServletRequest request){
		JsonResWrapper jrw = new JsonResWrapper();
		if(id==null){
			jrw.setFlag(false);
			jrw.setMessage("删除失败，参数缺失");
			//添加管理日志
			manageLog(request,1,2,false,null,null);
			return jrw;
		}
		int delNum = sysRoleService.querySystemRoleByName1(id);
		if(delNum >0){
			jrw.setFlag(false);
			jrw.setMessage("删除失败，该角色已被使用");
			//添加管理日志
			manageLog(request,1,2,false,null,null);
			return jrw;
		}
		sysRoleService.delRoleById(id);
		sysRoleService.delMenuOfSystemRole(id);
		jrw.setMessage("删除成功");
		//添加管理日志
		manageLog(request,1,2,true,null,null);
		return jrw;
	}
	/**
	 * 修改角色页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/update")
	public String add(Model model, @RequestParam(required=false) Integer id){
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
//		if(id!=null){
//			SysRole sr = sysRoleService.querySystemRoleById(id);
//			model.addAttribute("sr", sr);
//		}
//		//根据id查询对应的类型
//		String type=sysRoleService.getRoleIdByType(id);
//		//查询所有菜单
//		List<SysMenu> sms = DataUtil.isEmpty(sysRoleService.querySystemMenus(id));
//		for(SysMenu sm : sms){
//		//查询和角色关联的子菜单
//		sm.setSubMenus(sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), id));
//		}
//		model.addAttribute("sms", sms);
//		model.addAttribute("lbid", type);
//		return "sysRole/update";
		Integer areaId=0;
		Integer roleId=0;
		if(id!=null){
			SysRole sr = sysRoleService.querySystemRoleById(id);
			areaId=sr.getAreaId();
			roleId=sr.getSys_role_id();
			model.addAttribute("sr", sr);
		}
		//查询所有菜单
		List<SysMenu> smsMenu = DataUtil.isEmpty(sysRoleService.querySystemMenus(id));
		
		
	
		List<SysMenu> smsMenuChild = new ArrayList<>();
		for(SysMenu sm : smsMenu){
			//查询和角色关联的子菜单
//			sm.setSubMenus(sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), id));
			smsMenuChild.addAll(sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), id));
			if(sm.getChecked()==true){
				smsMenuChild.add(sm);
			}
			
		}
		List list1=new ArrayList();
		for (SysMenu sysMenu : smsMenuChild) {
			if(sysMenu.getChecked()){
				list1.add(sysMenu);
			}
			Integer MenuId=sysMenu.getSys_menu_id();
			List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(id,MenuId);
			if(li!=null && li.size()>0){
				for (SysMenu sys : li) {
					if(sys.getChecked()){
						list1.add(sys);
					}
				}
			}
		}
//		model.addAttribute("smsMenu", smsMenu);
		model.addAttribute("smsMenuChild", list1);
		
		//通过type查询对应的权限
		//根据id查询对应的类型
		String type=sysRoleService.getRoleIdByType(id);
		Integer lbid=Integer.parseInt(type);
		List<SysMenu> sms =new ArrayList<>();
		//"1".equals(role)&& userDetails.getSys_role_id()==1 &&
//		roleId>141     内置角色不需要修改全部权限
		if(areaId==1){
			//查询所有菜单
			sms = DataUtil.isEmpty(sysRoleService.querySystemMenus(id));
			for(SysMenu sm : sms){
				//查询和角色关联的子菜单
				List<SysMenu> l=new ArrayList<>();
				List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), id);
				for (int j = 0; j < sys.size(); j++) {
					l.add(sys.get(j));
					Integer MenuId=sys.get(j).getSys_menu_id();
					List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
					if(li!=null && li.size()>0){
						for (SysMenu sysMenu : li) {
							l.add(sysMenu);
						}
					}
				}
//				sm.setSubMenus(sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), id));
				sm.setSubMenus(l);
			}
		}else{
			if(1==lbid){
				String name[]={"基础设置","安全管理","系统管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("重点企业群".equals(named) ||"标示管理".equals(named) ||"监管报表".equals(named) ||"其他管理".equals(named) ||"企业二码合并".equals(named) ||"任务完成情况统计".equals(named) ||"任务管理".equals(named) ||"权限管理".equals(named)  ||"行业分类管理".equals(named) ||"政府部门类型".equals(named) ||"政府部门管理".equals(named) ||"机构管理".equals(named) ||"地区维护".equals(named) ||"校验管理".equals(named) ||"EXCEL模板设置".equals(named) ||"汇率维护".equals(named) ||"数据字典".equals(named) ||"用户管理".equals(named) ||"管理日志".equals(named)||"用户行为审计".equals(named)||"指标设置".equals(named)){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(2==lbid){
				String name[]={"信息查询","安全管理","系统管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("监管报表".equals(named) ||"统计报表查询".equals(named) ||"关联信息查询".equals(named) ||"任务完成情况统计".equals(named)||"信用评分查询".equals(named) ||"重点企业监测".equals(named) ||"企业名单筛选".equals(named) ||"信用报告查询".equals(named) ||"管理日志".equals(named)||"企业标识查询".equals(named)||"企业信息查询".equals(named)){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(3==lbid){
				String name[]={"安全管理","基础设置","数据报送"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("标示管理".equals(named) ||"数据删除".equals(named) ||"已报数据".equals(named) ||"数据类型查询".equals(named)||"手工修改".equals(named) ||"EXCEL报送".equals(named) ||"报文报送".equals(named) ||"管理日志".equals(named) ||"用户行为审计".equals(named) ||"校验管理".equals(named) ||"EXCEL模板设置".equals(named) ||"指标设置".equals(named) ||"汇率维护".equals(named) ||"数据字典".equals(named) ){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(4==lbid){
				String name[]={"安全管理","系统管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("监管报表".equals(named) ||"用户管理".equals(named) ||"管理日志".equals(named)  ||"任务完成情况统计".equals(named) ){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(5==lbid){
				String name[]={"信息查询","安全管理","系统管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("监管报表".equals(named) ||"统计报表查询".equals(named) ||"关联信息查询".equals(named)||"任务完成情况统计".equals(named) ||"信用评分查询".equals(named) ||"重点企业监测".equals(named) ||"企业名单筛选".equals(named) ||"信用报告查询".equals(named) ||"管理日志".equals(named)||"企业标识查询".equals(named)||"企业信息查询".equals(named)){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(6==lbid){
				String name[]={"数据报送","安全管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("管理日志".equals(named)   ||"数据删除".equals(named)||"已报数据".equals(named) ||"数据类型查询".equals(named)||"手工修改".equals(named) ||"EXCEL报送".equals(named)){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(9==lbid){
				String name[]={"信息查询","安全管理","系统管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("监管报表".equals(named) ||"统计报表查询".equals(named) ||"关联信息查询".equals(named) ||"任务完成情况统计".equals(named) ||"信用评分查询".equals(named) ||"重点企业监测".equals(named) ||"企业名单筛选".equals(named) ||"信用报告查询".equals(named) ||"管理日志".equals(named)||"企业标识查询".equals(named)||"企业信息查询".equals(named)){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(10==lbid){
				String name[]={"数据报送","安全管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("管理日志".equals(named)  ||"数据删除".equals(named)||"已报数据".equals(named) ||"手工修改".equals(named) ||"报文报送".equals(named)||"数据类型查询".equals(named) ){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(7==lbid){
				String name[]={"异议处理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("数据删除".equals(named) ||"已报数据".equals(named) ||"手工修改".equals(named) ||"EXCEL报送".equals(named) ||"报文报送".equals(named) ||"管理日志".equals(named)  ||"人行异议处理".equals(named) ){
									l.add(sys.get(j));
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}	
			else{
				String name[]={"异议处理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("已报数据".equals(named) ||"手工修改".equals(named) ||"EXCEL报送".equals(named) ||"管理日志".equals(named) ||"机构异议处理".equals(named) ){
									l.add(sys.get(j));
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
		}
		
		
		model.addAttribute("sms", sms);
		
		return "sysRole/update";
	}
//	正确页面
//	/**
//	 * 添加、修改页面
//	 * @param model
//	 * @param id 
//	 * @return lbid   类别id
//	 */
	@RequestMapping("/add")
	public String add(Model model, @RequestParam(required=false) Integer id, @RequestParam(required=false) Integer lbid){
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//查询报送机构
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
			so=sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		}
		SysArea area=sysAreaService.getAreaNotSub(so.getSys_area_id());
		if(id!=null){
			//通过id查询所有角色信息
			SysRole sr = sysRoleService.querySystemRoleById(id);
			model.addAttribute("sr", sr);
		}
//		System.out.println("======="+ area.getSysAreaType());
		model.addAttribute("areaType", area.getSysAreaType());
		return "sysRole/add";
	}
	
	
//	/**   
//	 * 添加、修改页面
//	 * @param model
//	 * @param id 
//	 * @throws IOException 
//	 */
	@RequestMapping("/getcode")
	public void getcode(@RequestParam(required=false) Integer id,HttpServletResponse response,Model model, @RequestParam(required=false) Integer lbid) throws IOException{
		List<SysMenu> sms =new ArrayList<>();
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
		if("1".equals(role)&& userDetails.getSys_role_id()==1){
			//查询所有菜单
			sms = DataUtil.isEmpty(sysRoleService.querySystemMenus(id));
			for(SysMenu sm : sms){
				//查询和角色关联的子菜单
				List<SysMenu> l=new ArrayList<>();
				List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), id);
				for (int j = 0; j < sys.size(); j++) {
					l.add(sys.get(j));
					Integer MenuId=sys.get(j).getSys_menu_id();
					List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
					if(li!=null && li.size()>0){
						for (SysMenu sysMenu : li) {
							l.add(sysMenu);
						}
					}
				}
//				sm.setSubMenus(sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), id));
				sm.setSubMenus(l);
			}
		}else{
			if(1==lbid){
				String name[]={"基础设置","安全管理","系统管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("重点企业群".equals(named) ||"标示管理".equals(named) ||"监管报表".equals(named) ||"其他管理".equals(named) ||"企业二码合并".equals(named) ||"任务完成情况统计".equals(named) ||"任务管理".equals(named) ||"权限管理".equals(named)  ||"行业分类管理".equals(named) ||"政府部门类型".equals(named) ||"政府部门管理".equals(named) ||"机构管理".equals(named) ||"地区维护".equals(named) ||"校验管理".equals(named) ||"EXCEL模板设置".equals(named) ||"汇率维护".equals(named) ||"数据字典".equals(named) ||"用户管理".equals(named) ||"管理日志".equals(named)||"用户行为审计".equals(named)||"指标设置".equals(named)){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(2==lbid){
				String name[]={"信息查询","安全管理","系统管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("监管报表".equals(named) ||"统计报表查询".equals(named) ||"任务完成情况统计".equals(named) ||"关联信息查询".equals(named) ||"信用评分查询".equals(named) ||"重点企业监测".equals(named) ||"企业名单筛选".equals(named) ||"信用报告查询".equals(named) ||"管理日志".equals(named)||"企业标识查询".equals(named)||"企业信息查询".equals(named)){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(3==lbid){
				String name[]={"安全管理","基础设置","数据报送"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("标示管理".equals(named) ||"数据删除".equals(named) ||"已报数据".equals(named) ||"数据类型查询".equals(named)||"手工修改".equals(named) ||"EXCEL报送".equals(named) ||"报文报送".equals(named) ||"管理日志".equals(named) ||"用户行为审计".equals(named) ||"校验管理".equals(named) ||"EXCEL模板设置".equals(named) ||"指标设置".equals(named) ||"汇率维护".equals(named) ||"数据字典".equals(named) ){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(4==lbid){
				String name[]={"安全管理","系统管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("监管报表".equals(named) ||"用户管理".equals(named) ||"管理日志".equals(named) ||"任务完成情况统计".equals(named)  ){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(5==lbid){
				String name[]={"信息查询","安全管理","系统管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("监管报表".equals(named) ||"统计报表查询".equals(named) ||"关联信息查询".equals(named)||"任务完成情况统计".equals(named)  ||"信用评分查询".equals(named) ||"重点企业监测".equals(named) ||"企业名单筛选".equals(named) ||"信用报告查询".equals(named) ||"管理日志".equals(named)||"企业标识查询".equals(named)||"企业信息查询".equals(named)){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(6==lbid){
				String name[]={"数据报送","安全管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("管理日志".equals(named)   ||"数据删除".equals(named)||"已报数据".equals(named) ||"数据类型查询".equals(named)||"手工修改".equals(named) ||"EXCEL报送".equals(named) ){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(9==lbid){
				String name[]={"信息查询","安全管理","系统管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("监管报表".equals(named) ||"统计报表查询".equals(named) ||"关联信息查询".equals(named) ||"任务完成情况统计".equals(named) ||"信用评分查询".equals(named) ||"重点企业监测".equals(named) ||"企业名单筛选".equals(named) ||"信用报告查询".equals(named) ||"管理日志".equals(named)||"企业标识查询".equals(named)||"企业信息查询".equals(named)){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(10==lbid){
				String name[]={"数据报送","安全管理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("管理日志".equals(named)   ||"已报数据".equals(named) ||"数据类型查询".equals(named)||"数据删除".equals(named)||"手工修改".equals(named) ||"报文报送".equals(named) ){
									l.add(sys.get(j));
								}
								Integer MenuId=sys.get(j).getSys_menu_id();
								List<SysMenu> li=sysRoleService.queryMenuByMenuIdByParentId(0,MenuId);
								if(li!=null && li.size()>0){
									for (SysMenu sysMenu : li) {
										l.add(sysMenu);
									}
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
			else if(7==lbid){
				String name[]={"异议处理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("数据删除".equals(named) ||"已报数据".equals(named) ||"手工修改".equals(named) ||"EXCEL报送".equals(named) ||"报文报送".equals(named) ||"管理日志".equals(named) ||"人行异议处理".equals(named) ){
									l.add(sys.get(j));
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}	
			else{
				String name[]={"异议处理"};
				for (int m = 0; m< name.length; m++) {
					String name1=name[m];
					List list=sysRoleService.queryParentAllMenu(name1);
					for (int i = 0; i < list.size(); i++) {
						Integer in=(Integer)list.get(i);
						//查询所有菜单
						sms.addAll(DataUtil.isEmpty(sysRoleService.queryAllMenu(in)));
						for(SysMenu sm : sms){
							List<SysMenu> l=new ArrayList<>();
							List<SysMenu> sys=sysRoleService.querySubSystemMenuByRoleId(sm.getSys_menu_id(), in);
							for (int j = 0; j < sys.size(); j++) {
								String named=sys.get(j).getSys_menu_name();
								if("已报数据".equals(named) ||"手工修改".equals(named) ||"EXCEL报送".equals(named) ||"管理日志".equals(named) ||"机构异议处理".equals(named) ){
									l.add(sys.get(j));
								}
							}
							sm.setSubMenus(l);
						}
					}
				}
			}
		}
		
		Gson gson=new Gson();
		response.getWriter().write(gson.toJson(sms));
			
	}
	/**
	 * 保存添加、修改
	 * @param model
	 * @param id 
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonResWrapper save(HttpServletRequest request,Model model, 
					  @RequestParam(required=false) Integer sys_role_id,
					  @RequestParam(required=false) String sys_role_name,
					  @RequestParam(required=false) String sys_role_notes,
					  @RequestParam(required=false) Integer sys_role_type,
					  @RequestParam(required=false) List<Integer> duties){
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//查询报送机构
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
			so=sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		}
		SysArea area=sysAreaService.queryAreaById(so.getSys_area_id());
		Integer areaId=area.getSysAreaId();
		JsonResWrapper jrw = new JsonResWrapper();
		if(!duties.contains(64)){
			duties.add(64);
		}
		if(!duties.contains(84)){
			duties.add(84);
		}
		
		SysRole sr = new SysRole();
		sr.setSys_role_id(sys_role_id);
		sr.setSys_role_name(sys_role_name);
		sr.setSys_role_notes(sys_role_notes);
		sr.setSys_role_type(sys_role_type);
		sr.setAreaId(areaId);
		sr.setMenuIds(duties);

		if(StringUtils.isBlank(sys_role_name) ){
			jrw.setFlag(false);
			jrw.setMessage("保存失败，参数缺失");
			//添加管理日志
			manageLog(request,1,1,false,null,null);
			return jrw;
		}
		

		//判断名称是否唯一
		SysRole role = sysRoleService.querySystemRoleByName(sys_role_name);
		if(role!=null){
			if(sys_role_id!=null){
				if(role.getSys_role_id().intValue()!=sys_role_id.intValue()){
					jrw.setFlag(false);
					jrw.setMessage("保存失败，角色名称已存在");
					//添加管理日志
					manageLog(request,1,1,false,null,null);
					return jrw;	
				}
			}else{
				jrw.setFlag(false);
				jrw.setMessage("保存失败，角色名称已存在");
				//添加管理日志
				manageLog(request,1,1,false,null,null);
				return jrw;
			}
		}
		StringBuffer oldsb=new StringBuffer();
		List<SysMenu> oldMenu=sysRoleService.queryMenuNameByRoleId(sys_role_id);
		for (SysMenu sysMenu : oldMenu) {
			if(sysMenu!=null){
				oldsb.append(sysMenu.getSys_menu_name()+",");
			}
		}
		StringBuffer newsb=new StringBuffer();
		//获取修改后的数据，通过菜单id进行查询
		for (int i = 0; i < duties.size(); i++) {
			SysMenu menu=sysRoleService.queryMenuByMenuId(duties.get(i));
			if(menu!=null){
				newsb.append(menu.getSys_menu_name()+",");
			}
		}
		
		//保存角色
		sysRoleService.saveSystemRole(sr);
		//修改添加管理日志
		if(sys_role_id!=null){
			manageLog(request,1,3,true,newsb.toString(),oldsb.toString());
		}else{
			//添加管理日志
			manageLog(request,1,1,true,sr.getSys_role_name(),null);
		}
		model.addAttribute("areaType", area.getSysAreaType());
		jrw.setMessage("保存成功");
		return jrw;
	}
	
	/**
	 * 列表页面
	 * @param reqeust
	 * @param model   返回数据
	 * @return
	 */
	@RequestMapping("/list")
	public String add(HttpServletRequest request, Model model){
		//当前登录用户的session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		PageSupport ps = PageSupport.initPageSupport(request);
		//查询报送机构
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if(so==null){
			so=sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		}
		SysArea area=sysAreaService.queryAreaById(so.getSys_area_id());
		Integer areaId=area.getSysAreaId();
		String role=sysRoleService.getRoleIdByType(userDetails.getSys_role_id());
		List<SysRole> srs =new ArrayList<>();
		if(areaId==1){
			SysRole sr = sysRoleService.querySystemRoleById(userDetails.getRoleIds().get(0));
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("type", sr.getSys_role_type());
			srs= sysRoleService.querySystemRoles(param);
		}else{
			Map param=new HashMap<>();
//			List<SysArea> areaList=sysAreaService.querySubAreasById(areaId);
//			for (SysArea sysArea : areaList) {
//				param.put("areaId", sysArea.getSysAreaId());
//			}
//			param.put("areaId", areaId);
			StringBuffer sb = new StringBuffer();
			DataUtil.getChildAreaIds(area, sb);// 获取改地区及子地区的ID
			String[] ids = sb.toString().split(",");
			param.put("areaId", ids);
			srs= sysRoleService.queryAllByArea(null,param);
		}
			model.addAttribute("role",role);
			model.addAttribute("roleId",userDetails.getSys_role_id());
			model.addAttribute("srs", srs);
		return "sysRole/list";
	}
}
