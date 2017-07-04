package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import com.workmanagement.model.AdminObjModel;
import com.workmanagement.model.SysOperateListModel;
import com.workmanagement.util.PageSupport;

public interface AdminObjService {
	/**
	 * 添加异议处理数据
	 * @param adminObj
	 */
	Integer insertOrUpdate(AdminObjModel adminObj);
	/**
	 * 通过异议处理表删除异议处理变信息
	 * @param dataId
	 * @param indexItemId
	 */
	void delOperateByDataId(Integer sysOperateId);
	
	/**
	 * 通过动态表id和指标大类id查询异议处理表信息
	 * @param dataId
	 * @param indexItemId
	 */
	List<AdminObjModel> selectSysOperateIdByDataIdAndIndexItemId(Integer dataId,Integer indexItemId);

	List<AdminObjModel> queryAll(PageSupport ps);

	void updateStatus(AdminObjModel adminObj);
	/**
	 *添加修改指标项数据到list
	 * @param sys
	 */

	void insertOrUpdateItem(SysOperateListModel sys);
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

	Integer insertOrUpdateOperate(AdminObjModel adminObj);
	/**
	 * 通过指标大类查询是否存在数据
	 * @param indexItemId
	 * @return 
	 */

	AdminObjModel selectOperateByItemId(Integer ing);
	/**
	 * 通过指标大类id，企业id，机构id，归档时间查询异议处理表是否存在该条数据
	 * @param maps
	 * @return
	 */
	AdminObjModel selectOperateId(Map<String, Object> map);
	/**
	 * 通过异议处理表查询该数据是否被选中
	 * @param ing
	 * @return
	 */
	List<SysOperateListModel> queryMarkByOperateId(Integer ing);

	List<SysOperateListModel> selectOperateByIdAll(Integer sysOperateId);

}
