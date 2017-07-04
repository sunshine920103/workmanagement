package com.workmanagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.GoverTypeDao;
import com.workmanagement.model.GoverType;
import com.workmanagement.util.BaseDaoSupport;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
/**
 * 政府部门类型Service实现
 * @author tianhao
 *
 */
@Service
public class GoverTypeServiceImpl extends BaseDaoSupport implements GoverTypeService {

	@Autowired
	private GoverTypeDao goverTypeDao;
	
	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#queryAll()
	 */
	@Override
	public List<GoverType> queryGoverTypeList(PageSupport ps,GoverType gt) throws Exception{
		if(ps==null){
			return goverTypeDao.queryGoverTypeList(gt);
		}
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(goverTypeDao.queryGoverTypeList(gt),ps);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#saveGoverType()
	 */
	@Override
	public void saveGoverType(GoverType gt) throws Exception{
		
		goverTypeDao.saveGoverType(gt);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#selectGoverTypeById(java.lang.Integer)
	 */
	@Override
	public GoverType queryGoverTypeById(Integer id) throws Exception{
		
		return goverTypeDao.queryGoverTypeById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#delGoverTypeById(java.lang.Integer)
	 */
	@Override
	public void delGoverTypeById(Integer id) throws Exception{
		
		goverTypeDao.delGoverTypeById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#queryGoverTypeByIdCount(java.lang.Integer)
	 */
	@Override
	public int queryGoverTypeByIdCount(Integer id) throws Exception{
		
		return goverTypeDao.queryGoverTypeByIdCount(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#queryGoverTypeByNameCount(java.lang.String)
	 */
	@Override
	public int queryGoverTypeByNameCount(GoverType gt) throws Exception{
		
		return goverTypeDao.queryGoverTypeByNameCount(gt);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#updateGoverType(com.workmanagement.model.GoverType)
	 */
	@Override
	public void updateGoverType(GoverType gt) throws Exception{
		
		goverTypeDao.updateGoverType(gt);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#queryGoverTypeCodeByName(java.lang.String)
	 */
	@Override
	public String queryGoverTypeCodeByName(String name) {
		
		return goverTypeDao.queryGoverTypeCodeByName(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#queryTypeAll()
	 */
	@Override
	public List<GoverType> queryTypeAll(String name) throws Exception {
		
		return goverTypeDao.queryTypeAll(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#queryGoverTypeIdByName(java.lang.String)
	 */
	@Override
	public GoverType queryGoverTypeIdByName(String name) throws Exception {
		
		return goverTypeDao.queryGoverTypeIdByName(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#queryGoverTypeByCodeCount(com.workmanagement.model.GoverType)
	 */
	@Override
	public int queryGoverTypeByCodeCount(GoverType gt) throws Exception {
		
		return goverTypeDao.queryGoverTypeByCodeCount(gt);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.GoverTypeService#queryGoverTypeByNameList(com.workmanagement.model.GoverType)
	 */
	@Override
	public List<GoverType> queryGoverTypeByNameList(GoverType gt) throws Exception {
		
		return goverTypeDao.queryGoverTypeList(gt);
	}

}
