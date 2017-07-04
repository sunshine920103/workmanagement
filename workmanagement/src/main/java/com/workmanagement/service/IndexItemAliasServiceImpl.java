package com.workmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmanagement.dao.IndexItemAliasDao;
import com.workmanagement.model.IndexItemAlias;
import com.workmanagement.util.BaseDaoSupport;

/**
 * 对别名表操作的service接口的实现类
 * @author wqs
 *
 */
@Service
public class IndexItemAliasServiceImpl extends BaseDaoSupport implements IndexItemAliasService {
	
	@Autowired
	private IndexItemAliasDao indexItemAliasDao;
	
	@Override
	public void save(IndexItemAlias alias) {
		indexItemAliasDao.save(alias);
	}
	
	@Override
	public IndexItemAlias selectByIndexItemId(Integer IndexItemId) {
		return indexItemAliasDao.selectByIndexItemId(IndexItemId);
	}

	@Override
	public void update(Integer indexItemAliasId,String indexItemAliasName) {
		indexItemAliasDao.update(indexItemAliasId,indexItemAliasName);
	}

	@Override
	public IndexItemAlias getByName(String aliasName) {
		return indexItemAliasDao.getByName(aliasName);
	}

	@Override
	public void deleteById(Integer id) {
		indexItemAliasDao.deleteById(id);
	}

	@Override
	public IndexItemAlias selectByIndexItemIdAndAreaId(Integer indexItemId, Integer aId) {
		return indexItemAliasDao.selectByIndexItemIdAndAreaId(indexItemId,aId);
	}

	@Override
	public IndexItemAlias selectByAliasNameAndAreaId(Integer indexId, String name, Integer areaId) {
		// TODO Auto-generated method stub
		return indexItemAliasDao.selectByAliasNameAndAreaId(indexId, name, areaId);
	}

}
