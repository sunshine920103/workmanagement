package com.workmanagement.service;

import com.workmanagement.model.IndexTb;
import com.workmanagement.util.PageSupport;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/** 
  *
  * @author  作者 wqs 
  * @date 创建时间：2017年3月14日 上午9:06:46 
  * 指标大类的Service接口
  */

public interface IndexTbService {
	
	/**
	 * 保存指标大类
	 */
	public void save(IndexTb indexTb);
	
	/**
	 * 根据指标大类名称对应的数据库表名查询该指标
	 */
	public IndexTb queryIdByCode(String code);
	
	/**
	 * 根据指标大类名称对应的数据库表名查询该指标和地区id
	 */
	public IndexTb queryByCodeAndAreaId(String code,Integer areaId);
	
	/**
	 * 四川省能查看全部的指标个数
	 */
	public Integer countAll();
	
	/**
	 * 登录市所建和四川省建的指标个数
	 */
	public Integer countAll2(Integer id);
	
	/**
	 * 创建指标大类对应的数据库表
	 */
	public void createTable(String tbName,String  tbId);
	
	/**
	 * 通过指标名称
	 */
	IndexTb queryIdByName(String indexName);
	
	/**
	 * 通过指标名称和地区id
	 */
	IndexTb queryByNameAndAreaId(String indexName,Integer areaId);
	
	/**
	 * 获取所有name
	 */
	List<IndexTb> selectAll();
	/**
	 * 登录地区为四川省：查询所有指标大类
	 */
	public List<IndexTb> queryAll(PageSupport ps);
	
	/**
	 * 登录地区为市：查询所有指标大类
	 */
	public List<IndexTb> queryAll2(PageSupport ps,Integer id);
	
	/**
	 * 登录地区为四川省:根据指标大类名称模糊查询
	 */
	public List<IndexTb> mohuQueryAll(PageSupport ps,String words,Integer aId);
	
	/**
	 * 登录地区为市:根据指标大类名称模糊查询
	 */
	public List<IndexTb> mohuQueryAll2(PageSupport ps,@Param("areaIds")List<Integer> areaIds,String words);
	
	/**
	 * 通过id查询
	 */
	public IndexTb queryById(Integer id);
	
	/**
	 * 通过地区id查询指标大类
	 * @param sysAreaId
	 */
	public List<IndexTb> queryIndexBySysAreaId(Integer sysAreaId);
	
	/**
	 * 更新指标大类
	 */
	public void updateIndex(IndexTb indexTb);
	
	/**
	 * 将指标大类置为无效
	 */
	public void change(Integer indexId ,Integer status);
	
	/**
	 * 通过多个地区id查询指标大类
	 * @param sysAreaIds
	 * @param ps
	 * @return
	 */
	List<IndexTb> queryIndexBySysAreaIds(PageSupport ps,List<Integer> sysAreaIds);
	
	
	/**
	 * 根据indexItemCode获取indexCode
	 */
	String selectIndexCodeByindexItemCode(String indexItemCode);

	/**
	 * 
	 * @param indexCode
	 * @return
	 */
	IndexTb getIndexTbbyIndexCode(String indexCode);
	
	/**
	 * 删除指标大类
	 */
	void del(@Param("indexId") Integer indexId);
	
	/**
	 * 获取所有name
	 */
	List<String> selectAllIndexName();
	
	/**
	 * 获取所有code
	 */
	List<String> selectAllIndexCode();

	public List<IndexTb> queryIndex(Map<String, Object> map);
	

	/**
	 * 获取所有的银行贷款信息
	 */
	List<Map<String, Object>> queryIndexIndexYhdk(Map<String, Object> param, PageSupport ps);
	/**
	 * 司法信息
	 */
	List<Map<String, Object>> queryIndexIndexSfxx(Map<String, Object> param, PageSupport ps);
	/**
	 * 行政处罚信息
	 */
	List<Map<String, Object>> queryIndexIndexXzcfxx(Map<String, Object> param, PageSupport ps);
	

	public List<Map<String, Object>> queryIndexTbByCode(Map<String, Object> map);

    List<IndexTb> getAllUsedIndexTb();
}
