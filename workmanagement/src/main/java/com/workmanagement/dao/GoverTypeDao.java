package com.workmanagement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.GoverType;

/**
 * 政府部门类型Dao
 * @author tianhao
 *
 */
public interface GoverTypeDao {
	
	/**
	 * 查询政府部门类型列表
	 * 
	 * @return
	 */
	List<GoverType> queryGoverTypeList(GoverType gt) throws Exception;
	
	/**
	 * 保存政府部门类型
	 * @return
	 */
	void saveGoverType(GoverType gt) throws Exception;
	
	/**
	 * 通過ID查詢政府部门类型
	 * @param id
	 * @return
	 */
	GoverType queryGoverTypeById(@Param("id") Integer id) throws Exception;
	
	/**
	 * 通过ID删除政府部门类型
	 * @param id
	 */
	void delGoverTypeById(@Param("id") Integer id) throws Exception;
	
	/**
	 * 通过ID查询政府类型使用数量
	 * @param id
	 * @return
	 */
	int queryGoverTypeByIdCount(@Param("id") Integer id) throws Exception;
	
	/**
	 * 通過Name查詢名称是否重复
	 * @param id
	 * @return
	 */
	int queryGoverTypeByNameCount(GoverType gt) throws Exception;
	
	/**
	 * 通過Code查詢名称是否重复
	 * @param id
	 * @return
	 */
	int queryGoverTypeByCodeCount(GoverType gt) throws Exception;
	
	/**
	 * 通过ID修改政府部门类型
	 * @param gt
	 */
	void updateGoverType(GoverType gt) throws Exception;
	
	/**
	 * 通过政府类别名称查找政府代码
	 * @param name
	 * @return
	 */
	String queryGoverTypeCodeByName(String name);
	
	/**
	 * 查询政府类型所有数据
	 * @return
	 * @throws Exception
	 */
	List<GoverType> queryTypeAll(@Param("name") String name) throws Exception;
	
	/**
	 * 根据政府类型名称查询id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	GoverType queryGoverTypeIdByName(@Param("name") String name) throws Exception;

}
