package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.AdminObjModel;
import com.workmanagement.model.SysOperateListModel;

public interface AdminObjModelDao {
	/**
	 * 添加异议处理数据
	 * @param adminObj
	 */
	void insert(AdminObjModel adminObj);
	/**
	 * 根据异议处理表id删除异议处理详情表信息
	 * @param sysOperateId
	 */
	void delOperateListByOperateId(@Param("sysOperateId")Integer sysOperateId);
	
	/**
	 * 通过动态表id和指标大类id删除异议处理变信息
	 * @param dataId
	 * @param indexItemId
	 */
	void delOperateByDataId(@Param("sysOperateId")Integer sysOperateId);
	/**
	 * 通过指标大类查询是否存在数据
	 * @param indexItemId
	 * @return 
	 */
	AdminObjModel selectOperateByItemId(@Param("sysOperateId")Integer sysOperateId);
	/**
	 * 更新异议处理数据
	 * @param adminObj
	 */
	void update(Map<String, Object> maps);
	/**
	 * 查询所有异议处理信息
	 * @return
	 */
	List<AdminObjModel> queryAll();
	/**
	 * 终止更改状态
	 * @param adminObj
	 */
	void updateStatus(AdminObjModel adminObj);
	/**
	 *添加指标项数据到list
	 * @param sys
	 */
	void insertItem(SysOperateListModel sys);
	/**
	 * 通过指标项查询数据是否存在
	 * @param indexItemId
	 * @return
	 */
	List<SysOperateListModel> selectOperateListByItemId(@Param("indexItemId")Integer indexItemId,@Param("sysOperateId")Integer sysOperateId);
	/**
	 * 跟新异议处理list
	 * @param sys
	 */
	void updateItem(SysOperateListModel sys);
	/**
	 * 根据指标大类id和企业二码id和机构id和时间查询异议处理表中的数据
	 * @param maps
	 * @return
	 */
	List<SysOperateListModel> selectOperateByQueryAll(Map<String, Object> maps);
	/**
	 * 查询异议处理详情表
	 * @param indexId
	 * @return
	 */
	List<SysOperateListModel> selectOperateList(Integer indexId);
	/**
	 * 通过指标大类id，企业id，机构id，归档时间查询异议处理表是否存在该条数据
	 * @param maps
	 * @return
	 */
	AdminObjModel selectOperateId(Map<String, Object> maps);
	/**
	 * 更新异议处理表信息
	 * @param adminObj
	 */
	void updateOperate(AdminObjModel adminObj);
	/**
	 * 添加异议处理表信息
	 * @param adminObj
	 */
	void insertOperate(AdminObjModel adminObj);
	List<SysOperateListModel> queryMarkByOperateId(@Param("sysOperateId")Integer ing);
	List<SysOperateListModel> selectOperateByIdAll(@Param("sysOperateId")Integer sysOperateId);
	/**
	 * 通过动态表id和指标大类id查询异议处理表信息
	 */
	List<AdminObjModel> selectSysOperateIdByDataIdAndIndexItemId(
			@Param("dataId")Integer dataId,@Param("indexItemId") Integer indexItemId);

}
