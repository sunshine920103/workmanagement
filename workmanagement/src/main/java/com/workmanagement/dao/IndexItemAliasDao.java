package com.workmanagement.dao;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.IndexItemAlias;

/**
 * 操作别名表的dao接口
 * @author wqs
 *
 */
public interface IndexItemAliasDao {
	
	/**
	 * 保存一个别名
	 */
	public void save(IndexItemAlias alias);
	/**
	 * 根据地区id和别名名称查询别名表
	 */
	IndexItemAlias selectByAliasNameAndAreaId(@Param("indexId")Integer indexId,@Param("name")String name,@Param("areaId")Integer areaId);
	
	/**
	 * 根据指标项id查询
	 */
	public IndexItemAlias selectByIndexItemId(Integer IndexItemId);
	
	/**
	 * 通过指标项id和区域id查询别名
	 */
	 public IndexItemAlias selectByIndexItemIdAndAreaId(@Param("indexItemId")Integer id,@Param("sysAreaId")Integer aId);
	
	/**
	 * 根据名字查询
	 */
	public IndexItemAlias getByName(@Param("indexItemAliasName") String aliasName);
	
	/**
	 * 根据id删除一个别名
	 */
	public void deleteById(@Param("indexItemAliasId") Integer id);
	
	 /**
	  * 更新
	  */
	 public void update(@Param("indexItemAliasId")Integer indexItemAliasId,@Param("indexItemAliasName")String indexItemAliasName);
}
