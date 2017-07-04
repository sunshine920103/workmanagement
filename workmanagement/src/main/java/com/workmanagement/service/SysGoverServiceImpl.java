package com.workmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.SysGoverDao;
import com.workmanagement.model.Dic;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysGover;
import com.workmanagement.util.BaseDaoSupport;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
/**
 * 政府部门管理Service实现
 * @author tianhao
 *
 */
@Service
public class SysGoverServiceImpl extends BaseDaoSupport implements SysGoverService {

	@Autowired
	private SysGoverDao sysGoverDao;

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#querySysGoverList(com.workmanagement.model.SysGover)
	 */
	@Override
	public List<SysGover> querySysGoverList(PageSupport ps,SysGover sg) throws Exception {
		if(ps==null){
			return sysGoverDao.querySysGoverList(sg);
		}
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(sysGoverDao.querySysGoverList(sg),ps);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#querySysGoverById(java.lang.Integer)
	 */
	@Override
	public SysGover querySysGoverById(Integer id) throws Exception {
		
		return sysGoverDao.querySysGoverById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#saveSysGover(com.workmanagement.model.SysGover)
	 */
	@Override
	public void saveSysGover(SysGover sg) throws Exception {
		
		sysGoverDao.saveSysGover(sg);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#querySysGoverByNameCount(java.lang.String)
	 */
	@Override
	public int querySysGoverByNameCount(SysGover sg) throws Exception {
		
		return sysGoverDao.querySysGoverByNameCount(sg);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#delSysGoverById(java.lang.Integer)
	 */
	@Override
	public void delSysGoverById(Integer id) throws Exception{
		
		sysGoverDao.delSysGoverById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#updateSysGover(com.workmanagement.model.SysGover)
	 */
	@Override
	public void updateSysGover(SysGover sg) throws Exception {
		
		sysGoverDao.updateSysGover(sg);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#queryTypeAll()
	 */
	@Override
	public List<SysGover> queryTypeAll(String name) throws Exception {
		
		return sysGoverDao.queryTypeAll(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#queryInstitutionsById(java.lang.Integer)
	 */
	@Override
	public SysGover queryInstitutionsById(Integer id) throws Exception {
		
		return sysGoverDao.queryInstitutionsById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#querySysGoverByCodeAndName(java.lang.String, java.lang.String)
	 */
	@Override
	public List<SysGover> querySysGoverByCodeAndName(String name, String code) throws Exception {
		
		return sysGoverDao.querySysGoverByCodeAndName(name, code);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#querySysGoverIdByName(java.lang.String)
	 */
	@Override
	public SysGover querySysGoverIdByName(String name) throws Exception {
		
		return sysGoverDao.querySysGoverIdByName(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#queryDicIdByName(java.lang.String)
	 */
	@Override
	public Dic queryDicIdByName(String name) throws Exception {
		
		return sysGoverDao.queryDicIdByName(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#querySysAreaIdByName(java.lang.String)
	 */
	@Override
	public SysArea querySysAreaIdByName(String name) throws Exception {
		
		return sysGoverDao.querySysAreaIdByName(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#querySysGoverByCodeCount(com.workmanagement.model.SysGover)
	 */
	@Override
	public int querySysGoverByCodeCount(SysGover sg) throws Exception {
		
		return sysGoverDao.querySysGoverByCodeCount(sg);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#querySysGoverCodeByName(java.lang.String)
	 */
	@Override
	public String querySysGoverCodeByName(String name) throws Exception {
		
		return sysGoverDao.querySysGoverCodeByName(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#querySysGoverByNameList(com.workmanagement.model.SysGover)
	 */
	@Override
	public List<SysGover> querySysGoverByNameList(SysGover sg) throws Exception {
		
		return sysGoverDao.querySysGoverList(sg);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#delSysGoverByUpid(java.lang.Integer)
	 */
	@Override
	public void delSysGoverByUpid(Integer id) throws Exception {
		
		sysGoverDao.delSysGoverByUpid(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#querySysGoverUpidByCount(java.lang.Integer)
	 */
	@Override
	public int querySysGoverUpidByCount(Integer id) {
		
		return sysGoverDao.querySysGoverUpidByCount(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysGoverService#querySysGoverByUpid(java.lang.Integer)
	 */
	@Override
	public List<SysGover> querySysGoverByUpid(Integer id) {
		
		return sysGoverDao.querySysGoverByUpid(id);
	}
	
}
