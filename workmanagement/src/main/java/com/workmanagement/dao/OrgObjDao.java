package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.AdminObjModel;
import com.workmanagement.model.SysOperateListModel;

/**
 * 金融机构异议处理Dao
 * @author tianhao
 *
 */
public interface OrgObjDao {
	
	/**
	 * 查询异议列表
	 * @param map
	 * @return
	 */
	List<AdminObjModel> querySysOperateList(Map<String, Object> map);
	
	/**
	 * 查询是否有数据
	 * @param map
	 * @return
	 */
	Integer querySysOperateCount(Map<String, Object> map);
	
	/**
	 * 增加异议处理
	 * @param adminObjModel
	 */
	void insertSysOperate(AdminObjModel adminObjModel);
	
	/**
	 * 修改异议处理
	 * @param adminObjModel
	 */
	void updateSysOperate(AdminObjModel adminObjModel);
	
	/**
	 * 查询是否有数据
	 * @param map
	 * @return
	 */
	Integer querySysOperateListCount(Map<String, Object> map);
	
	/**
	 * 增加异议处理列表
	 * @param sysOperateListModel
	 */
	void insertSysOperateList(SysOperateListModel sysOperateListModel);
	
	/**
	 * 修改异议处理列表
	 * @param sysOperateListModel
	 */
	void updateSysOperateList(SysOperateListModel sysOperateListModel);
	
	/**
	 * 查询异议处理详情列表
	 * @param operateId
	 * @return
	 */
	List<SysOperateListModel> querySysOperateListByOperateId(@Param("operateId")Integer operateId);
	
	/**
	 * 查询异议处理
	 * @param operateId
	 * @return
	 */
	AdminObjModel querySysOperateById(@Param("operateId")Integer operateId);
	
	/**
	 * 修改异议处理状态
	 * @param operateId
	 */
	void updateSysOperateStatus(@Param("operateId")Integer operateId);
	
	/**
	 * 查询是否存在
	 * @param map
	 * @return
	 */
	Integer querySysOperateCountByStatus(Map<String, Object> map);

	List<AdminObjModel> querySysOperateList1(Map<String, Object> map);
	List<AdminObjModel> queryAllSysOperateList1(Map<String, Object> map);
	List<AdminObjModel> queryAllSysOperateList();

	
	/**
	 * 查询二码ID
	 * @param map
	 * @return
	 */
	List<Integer> queryDefaultId(Map<String, Object> map);
	
	/**
	 * 修改标识
	 * @param sl
	 */
	void updateSysOperateListMark(SysOperateListModel sl);

}
