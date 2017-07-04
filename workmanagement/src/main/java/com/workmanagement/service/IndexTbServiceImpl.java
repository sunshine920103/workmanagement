package com.workmanagement.service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.IndexTbDao;
import com.workmanagement.model.IndexTb;
import com.workmanagement.util.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/** 
  *
  * @author  作者 wqs 
  * @date 创建时间：2017年3月14日 上午9:13:35 
  * 
  */
@Service
public class IndexTbServiceImpl extends BaseDaoSupport implements IndexTbService {
	
	@Autowired
	private IndexTbDao indexTbDao;
	
		
	@Override
	public void save(IndexTb indexTb) {
		//缓存的key
		String indexKey = RedisKeys.INDEX_LIST;
		//修改指标大类
		indexTbDao.insertIndex(indexTb);
		RedisUtil.delBatchData(indexKey+"*");
		
	}
	
	@Override
	public List<IndexTb> queryAll(PageSupport ps) {
        if ( ps == null){//表示不分页
        	return indexTbDao.queryAll();
        }else {
            PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());          
            return PageHelperSupport.queryCount( indexTbDao.queryAll(),ps);
        }	
	}
	
	@Override
	public List<IndexTb> queryAll2(PageSupport ps,Integer id) {
        if ( ps == null){//表示不分页
        	return indexTbDao.queryAll2(id);
        }else {
            PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());          
            return PageHelperSupport.queryCount( indexTbDao.queryAll2(id),ps);
        }	
	}
	
	@Override
	public List<IndexTb> mohuQueryAll(PageSupport ps, String words,Integer aId) {
        if ( ps == null){//表示不分页
        	return indexTbDao.mohuQueryAll(words,aId);
        }else {
            PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());          
            return PageHelperSupport.queryCount( indexTbDao.mohuQueryAll(words,aId),ps);
        }
	}

	@Override
	public List<IndexTb> mohuQueryAll2(PageSupport ps, @Param("areaIds")List<Integer> areaIds, String words) {
        if ( ps == null){//表示不分页
        	return indexTbDao.mohuQueryAll2(areaIds,words);
        }else {
            PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());          
            return PageHelperSupport.queryCount( indexTbDao.mohuQueryAll2(areaIds,words),ps);
        }
	}
	

	@Override
	public IndexTb queryById(Integer id) {
		return indexTbDao.queryById(id);
	}
	
	@Override
	public List<IndexTb> queryIndexBySysAreaId(Integer sysAreaId) {
		return indexTbDao.queryIndexBySysAreaId(sysAreaId);
	}
	
	@Override
	public void updateIndex(IndexTb indexTb) {
		indexTbDao.updateIndex(indexTb);
	}

	@Override
	public void change(Integer indexId ,Integer status) {
		indexTbDao.change(indexId ,status);
	}

	@Override
	public void createTable(String tbName,String tbId) {
		indexTbDao.createTable( tbName,tbId);	
	}

	@Override
	public List<IndexTb> selectAll() {
		return  indexTbDao.selectAll();
	}

	@Override
	public IndexTb queryIdByName(String indexName) {
		 return indexTbDao.queryIdByName(indexName);
	}
	@Override
	public IndexTb queryByNameAndAreaId(String indexName,Integer areaId) {
		return indexTbDao.queryByNameAndAreaId(indexName,areaId);
	}
	
	@Override
	public String selectIndexCodeByindexItemCode(@Param("indexItemCode")String indexItemCode) {
		return indexTbDao.selectIndexCodeByindexItemCode(indexItemCode);
	}
	
	@Override
	public List<String> selectAllIndexName() {
		return indexTbDao.selectAllIndexName();
	}

	@Override
	public List<String> selectAllIndexCode() {
		return indexTbDao.selectAllIndexCode();
	}

	@Override
	public IndexTb getIndexTbbyIndexCode(String indexCode) {
		return indexTbDao.getIndexTbbyIndexCode(indexCode);
	}
	

	/**
	 * 通过多个地区id查询指标大类
	 *
	 * @param ps
	 * @param sysAreaIds
	 * @return
	 */
	@Override
	public List<IndexTb> queryIndexBySysAreaIds(PageSupport ps, List<Integer> sysAreaIds) {
		if (ps == null){//表示不分页
			return indexTbDao.queryIndexBySysAreaIds(sysAreaIds);
		}else {
			PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			return PageHelperSupport.queryCount( indexTbDao.queryIndexBySysAreaIds(sysAreaIds),ps);
		}
	}
	@Override
	public List<IndexTb> queryIndex(Map<String, Object> map){
		return indexTbDao.queryIndex(map);
	}

	@Override
	public IndexTb queryIdByCode(String code) {
		return indexTbDao.queryIdByCode(code);
	}
	
	@Override
	public IndexTb queryByCodeAndAreaId(String code,Integer areaId) {
		return indexTbDao.queryByCodeAndAreaId(code,areaId);
	}

	@Override
	public List<Map<String, Object>> queryIndexIndexYhdk(Map<String, Object> param,PageSupport ps) {
		if (ps == null){//表示不分页
			return indexTbDao.queryIndexIndexYhdk(param);
		}else {
			PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			return PageHelperSupport.queryCount( indexTbDao.queryIndexIndexYhdk(param),ps);
		}
	}
	@Override
	public List<Map<String, Object>> queryIndexIndexSfxx(Map<String, Object> param,PageSupport ps) {
		if (ps == null){//表示不分页
			return indexTbDao.queryIndexIndexSfxx(param);
		}else {
			PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			return PageHelperSupport.queryCount( indexTbDao.queryIndexIndexSfxx(param),ps);
		}
	}
	@Override
	public List<Map<String, Object>> queryIndexIndexXzcfxx(Map<String, Object> param,PageSupport ps) {
		
		if (ps == null){//表示不分页
			return indexTbDao.queryIndexIndexXzcfxx(param);
		}else {
			PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			return PageHelperSupport.queryCount( indexTbDao.queryIndexIndexXzcfxx(param),ps);
		}
	}
	@Override
	public Integer countAll() {
		return indexTbDao.countAll();
	}

	@Override
	public Integer countAll2(Integer id) {
		return indexTbDao.countAll2(id);
	}

	@Override
	public List<Map<String, Object>> queryIndexTbByCode(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return indexTbDao.queryIndexTbByCode(map);
	}

    @Override
    public List<IndexTb> getAllUsedIndexTb() {
        return indexTbDao.getAllUsedIndexTb();
    }

	@Override
	public void del(Integer indexId) {
		indexTbDao.del(indexId);
	}

}
