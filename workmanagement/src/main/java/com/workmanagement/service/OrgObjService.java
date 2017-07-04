package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.workmanagement.model.AdminObjModel;
import com.workmanagement.model.SysOperateListModel;
import com.workmanagement.util.PageSupport;

/**
 * 金融机构异议处理Service接口
 * @author tianhao
 *
 */
public interface OrgObjService {

	/**
	 * 查询异议列表
	 * @param map
	 * @return
	 */
	List<AdminObjModel> querySysOperateList(PageSupport ps,Map<String, Object> map);
	
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
	 * 修改动态表数据
	 * @param indexId
	 * @param majorId
	 * @param indexItemCode
	 * @param indexItemValue
	 * @param operateInformation
	 * @param operateOrgdesc
	 * @param operateService
	 * @param sysOperateList
	 * @param sysOperateId
	 * @param file
	 * @throws Exception 
	 */
	void updateIndexTbSql(Integer indexId,Integer majorId,String[] indexItemCode,String[] indexItemValue,String[] operateInformation,String[] operateOrgdesc,String[] operateService,String sysOperateList,Integer sysOperateId,String file,Integer defaultId,HttpServletRequest request) throws Exception;
	
	/**
	 * 查询异议处理详情列表
	 * @param operateId
	 * @return
	 */
	List<SysOperateListModel> querySysOperateListByOperateId(Integer operateId);
	
	/**
	 * 查询异议处理
	 * @param operateId
	 * @return
	 */
	AdminObjModel querySysOperateById(Integer operateId);
	
	/**
	 * 查询是否存在
	 * @param map
	 * @return
	 */
	Integer querySysOperateCountByStatus(Map<String, Object> map);

	List<AdminObjModel> querySysOperateList1(PageSupport ps,Map<String, Object> map);
	List<AdminObjModel> queryAllSysOperateList1(PageSupport ps,Map<String, Object> map);
	List<AdminObjModel> queryAllSysOperateList(PageSupport ps);
	
	
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
