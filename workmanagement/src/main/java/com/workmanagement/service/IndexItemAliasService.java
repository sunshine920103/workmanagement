package com.workmanagement.service;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.IndexItemAlias;

/**
 * 对别名操作的service
 * @author wqs
 *
 */
public interface IndexItemAliasService {
	/**
	 * 保存一个别名
	 */
	public void save(IndexItemAlias alias);
	
	/**
	 * 根据指标项id查询
	 */
	 public IndexItemAlias selectByIndexItemId(Integer id);
	 
	/**
	 * 根据地区id和别名名称查询别名表
	 */
	IndexItemAlias selectByAliasNameAndAreaId(@Param("indexId")Integer indexId,@Param("name")String name,@Param("areaId")Integer areaId);

	 
	/**
	 * 通过指标项id和区域id查询别名
	 */
	 public IndexItemAlias selectByIndexItemIdAndAreaId(Integer indexItemId,Integer aId);
	 /**
	  * 更新
	  */
	 public void update(Integer indexItemAliasId,String indexItemAliasName);
	 
	/**
	 * 根据名字查询
	 */
	public IndexItemAlias getByName( String aliasName);
	
	/**
	 * 根据id删除一个别名
	 */
	public void deleteById( Integer id);
}
