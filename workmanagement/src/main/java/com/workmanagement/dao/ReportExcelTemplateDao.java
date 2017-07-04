package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.IndexItemAlias;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.ReportExcelTemplate;
import com.workmanagement.model.SysArea;

/**
 * 
 * @author tianhao
 *
 */
public interface ReportExcelTemplateDao {
	
	/**
	 * 查询excel模板列表
	 * @return
	 */
	List<ReportExcelTemplate> queryReportExcelTemplateList(Map<String, Object> map);
	
	/**
	 * 查询指标大类启用时，对应的excel模板列表
	 * @param map
	 * @return
	 */
	List<ReportExcelTemplate> queryExcelTempsWitchIndexIsUsing(Map<String, Object> map);
	List<ReportExcelTemplate> queryReportExcelTemplate(@Param("map")Map<String, Object> map,@Param("name")String name);
	
	/**
	 * 查询所有地区
	 * @return
	 */
	List<SysArea> queryAreaAll();
	
	/**
	 * 获取所有name
	 */
	List<String> selectAllIndexName(Map<String, Object> map);
	/**
	 * 获取所有code
	 */
	List<String> selectAllIndexCode(Map<String, Object> map);
	/**
	 * 获取所有id
	 */
	List<Integer> selectAllIndexId(Map<String, Object> map);
	
	List<IndexItemTb> queryIndexItemsByIndex(Map<String, Object> map);
	
	IndexTb  queryIndexTbById(@Param("id") Integer id);
	
	/**
	 * 名称集合
	 */
	List<String> allReportExcelTemplateName(Map<String, Object> map);
	
	/**
	 * 保存
	 * @param reportExcelTemplate
	 */
	void insertReportExcelTemplate(ReportExcelTemplate reportExcelTemplate);
	
	/**
	 * 通过ID查询
	 * @param reportExcelTemplateId
	 * @return
	 */
	ReportExcelTemplate queryReportExcelTemplateById(@Param("reportExcelTemplateId") Integer reportExcelTemplateId);
	
	/**
	 * 通过Name查询
	 * @param reportExcelTemplateName
	 * @return
	 */
	ReportExcelTemplate queryReportExcelTemplateByName(@Param("reportExcelTemplateName") String reportExcelTemplateName);
	
	/**
	 * 删除模板
	 * @param status
	 * @param id
	 */
	void delReportExcelTemplate(@Param("id") Integer id);
	
	/**
	 * 通过ID和地区查询别名
	 * @param id
	 * @param aid
	 * @return
	 */
	List<IndexItemAlias> queryIndexItemAlias(@Param("aid") Integer aid);
	
	IndexItemTb queryIndexItemTbByCode(@Param("code") String code);

	Integer queryIndexIdByCount(@Param("indexId") Integer indexId);

}
