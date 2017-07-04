package com.workmanagement.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.ReportIndex;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysUserLog;

/**
 * 手工修改Dao
 * @author tianhao
 *
 */
public interface ManualEntryDao {
	
	/**
	 * 查询指标大类
	 * @return
	 */
	List<IndexTb> queryIndexTbAll(Map<String, Object> map);
	
	/**
	 * 根据机构码查询
	 */
	DefaultIndexItem queryCodeOrg(@Param("codeOrg")String codeOrg,@Param("areaId")Integer areaId);
	/**
	 * 根据信用码查询
	 */
	DefaultIndexItem queryCodeCredit(@Param("codeCredit")String codeCredit,@Param("areaId")Integer areaId);
	
	/**
	 * 通过id查询指标大类
	 * @param id
	 * @return
	 */
	IndexTb queryIndexTbById(@Param("id")Integer id);
	
	/**
	 * 查询临时表数据
	 */
	List<Map<String,Object>> temporaryTableList(Map<String,Object> map);
	
	/**
	 * 查询列表页面
	 * @param param
	 * @return
	 */
	List<SysUserLog> querySysUserLogList(Map<String, Object> param);
	
	/**
	 * 通过ID查询
	 * @param id
	 * @return
	 */
	SysUserLog getSysUserLogById(@Param("id")Integer id);
	
	/**
	 * 通过ID查询
	 * @param id
	 * @return
	 */
	DefaultIndexItem getDefaultIndexItemById(@Param("id")Integer id);
	
	/**
	 * 通過code得到对象
	 * @param code
	 * @return
	 */
	IndexItemTb getIndexItemTbByCode(@Param("code")String code);
	
	/**
	 * 通過name获取指标大类
	 * @param name
	 * @return
	 */
	IndexTb queryIndexTbByIndexName(@Param("name")String name);
	
	/**
	 * 更改上报数据状态
	 * @param sql
	 */
	void updataStatus(@Param("updataStatus")String sql);
	
	/**
	 * 查询机构及下级机构
	 * @param map
	 * @return
	 */
	List<SysOrg> querySysOrgAll(Map<String, Object> map);
	
	/**
	 * 保存修改
	 * @param reportIndex
	 */
	void insertReportIndex(ReportIndex reportIndex);
	
	/**
	 * 修改动态表数据
	 * @param map
	 */
	void updateIndexTbSql(Map<String, Object> map);
	
	/**
	 * 查询手工修改历史
	 * @return
	 */
	List<SysUserLog> querySysUserLog(@Param("indexId")Integer indexId);
	
	/**
	 * 查询数据字典详情
	 * @param dicContent
	 * @return
	 */
	DicContent queryDicContentByIdAndCode(DicContent dicContent);
	
	/**
	 * 通过id修改企业二码
	 * @param map
	 */
	void updateDefaultIndexitemCodeCreditById(Map<String, Object> map);
	
	/**
	 * 通过id修改企业二码
	 * @param map
	 */
	void updateDefaultIndexitemCodeOrgById(Map<String, Object> map);
	
	/**
	 * 查询是否有重复
	 * @param map
	 * @return
	 */
	Integer queryDefaultIndexItemCountByCodeCredit(Map<String, Object> map);
	
	/**
	 * 查询是否有重复
	 * @param map
	 * @return
	 */
	Integer queryDefaultIndexItemCountByCodeOrg(Map<String, Object> map);

}
