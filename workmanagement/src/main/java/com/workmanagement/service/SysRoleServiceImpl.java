package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.SysRoleDao;
import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysRole;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDao systemRoleDao;

	@Override
	public int delRoleById(Integer id) {
		return systemRoleDao.delRoleById(id);
	}
	
	@Override
	public List<SysRole> querySystemRoles(Map<String, Object> param) {
			return systemRoleDao.querySystemRoles(param);
	}

	@Override
	public SysRole querySystemRoleById(Integer id) {
		return systemRoleDao.querySystemRoleById(id);
	}
	
	@Override
	public void saveSystemRole(SysRole sr) {
		//查询职责名称集合
		if(sr.getMenuIds()!=null){
			String duties = systemRoleDao.queryRoleDuties(sr.getMenuIds());
			sr.setSys_role_duties(duties);
		}
		
		if(sr.getSys_role_id()!=null){
			systemRoleDao.delMenuOfSystemRole(sr.getSys_role_id());
			systemRoleDao.updateSystemRole(sr);
		}else{
			systemRoleDao.insertSystemRole(sr);
		}
		
		if(sr.getMenuIds()!=null){
			systemRoleDao.insertMenuOfSystemRole(sr.getSys_role_id(), sr.getMenuIds());
		}
	}

	@Override
	public List<SysMenu> querySubSystemMenuByRoleId(Integer pid, Integer rid) {
		return systemRoleDao.querySubSystemMenuByRoleId(pid,rid);
	}

	@Override
	public List<SysMenu> querySystemMenus(Integer rid) {
		return systemRoleDao.querySystemMenus(rid);
	}

	@Override
	public SysRole querySystemRoleByName(String name) {
		return systemRoleDao.querySystemRoleByName(name);
	}

	@Override
	public void updateSysRole(SysRole sr) {
		systemRoleDao.updateSystemRole(sr);
	}

	@Override
	public String querySysUserId(Integer uid) {
		// TODO Auto-generated method stub
		 return systemRoleDao.querySysUserId(uid);

	}

	@Override
	public List<SysMenu> querySystemMenusName(String name) {
		// TODO Auto-generated method stub
		return systemRoleDao.querySystemMenusName(name);
	}

	@Override
	public List<SysMenu> queryAllMenu(Integer id) {
		// TODO Auto-generated method stub
		return systemRoleDao.queryAllMenu(id);
	}

	@Override
	public List<SysMenu> queryParentAllMenu(String name) {
		// TODO Auto-generated method stub
		return systemRoleDao.queryParentAllMenu(name);
	}

	@Override
	public String getRoleIdByType(Integer id) {
		// TODO Auto-generated method stub
		return systemRoleDao.getRoleIdByType(id);
	}

	@Override
	public int querySystemRoleByName1(Integer id) {
		// TODO Auto-generated method stub
		return systemRoleDao.querySystemRoleByName1(id);
	}

	@Override
	public void delMenuOfSystemRole(Integer id) {
		// TODO Auto-generated method stub
		systemRoleDao.delMenuOfSystemRole(id);
	}

	@Override
	public SysMenu queryMenuByMenuId(Integer integer) {
		// TODO Auto-generated method stub
		return systemRoleDao.queryMenuByMenuId(integer);
	}

	@Override
	public List<SysMenu> queryMenuNameByRoleId(Integer sys_role_id) {
		// TODO Auto-generated method stub
		return systemRoleDao.queryMenuNameByRoleId(sys_role_id);
	}

	@Override
	public List<SysMenu> queryMenuByMenuIdByParentId(Integer rid,Integer menuId) {
		// TODO Auto-generated method stub
		return systemRoleDao.queryMenuByMenuIdByParentId(rid,menuId);
	}

	@Override
	public List<SysMenu> querySystemMenus1(Integer id) {
		// TODO Auto-generated method stub
		return systemRoleDao.querySystemMenus1(id);
	}

	@Override
	public List<SysRole> queryAllByArea(PageSupport ps,Map<String,Object> param) {
		// TODO Auto-generated method stub
		if(ps!=null){
			PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			return PageHelperSupport.queryCount(systemRoleDao.queryAllByArea(param),ps);
		}else{
			return systemRoleDao.queryAllByArea(param);
		}
	}

	
}
