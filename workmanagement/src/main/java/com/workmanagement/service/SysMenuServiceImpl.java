package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.SysMenuDao;
import com.workmanagement.model.SysMenu;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	SysMenuDao sysMenuDao;
	@Override
	public List<SysMenu> queryMenu(Map<String, Object> map, PageSupport ps) {
		if(ps!=null){
	        PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
	        return PageHelperSupport.queryCount(sysMenuDao.queryMenu(map), ps);
		}else{
	        return sysMenuDao.queryMenu(map);
		}
	}
	@Override
	public void updateMenu(SysMenu menu) {
		sysMenuDao.updateMenu(menu);
	}
	@Override
	public void insertMenu(SysMenu menu) {
		// TODO Auto-generated method stub
		sysMenuDao.insertMenu(menu);
	}
	@Override
	public void deleteMenu(Integer menuId) {
		// TODO Auto-generated method stub
		sysMenuDao.deleteMenu(menuId);
	}
	@Override
	public SysMenu queryMenuName(String name) {
		// TODO Auto-generated method stub
		return sysMenuDao.queryMenuName(name);
	}
	@Override
	public void insertRoleMenu(Integer rid, Integer mid) {
		// TODO Auto-generated method stub
		sysMenuDao.insertRoleMenu(rid, mid);
	}

}
