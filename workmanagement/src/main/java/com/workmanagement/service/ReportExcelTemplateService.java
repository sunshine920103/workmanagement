package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.IndexItemAlias;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.ReportExcelTemplate;
import com.workmanagement.model.SysArea;
import com.workmanagement.util.PageSupport;

/**
 * 
 * @author tianhao
 *
 */
public interface ReportExcelTemplateService {

	/**
	 * 查询excel模板列表
	 * @return
	 */
	List<ReportExcelTemplate> queryReportExcelTemplateList(Map<String, Object> map,PageSupport ps);
	List<ReportExcelTemplate> queryReportExcelTemplate(Map<String, Object> map,String name);
	
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
	
	/**
	 * 通过id获取指标大类
	 * @param id
	 * @return
	 */
	IndexTb  queryIndexTbById(Integer id);
	
	/**
	 * 名称集合
	 */
	List<String> allReportExcelTemplateName(Map<String, Object> map);
	
	/**
	 * 保存
	 * @param reportExcelTemplate
	 */
	void insertReportExcelTemplate(ReportExcelTemplate reportExcelTemplate,HttpServletRequest request);
	
	/**
	 * 通过ID查询
	 * @param reportExcelTemplateId
	 * @return
	 */
	ReportExcelTemplate queryReportExcelTemplateById(Integer reportExcelTemplateId);
	
	/**
	 * 通过Name查询
	 * @param reportExcelTemplateName
	 * @return
	 */
	ReportExcelTemplate queryReportExcelTemplateByName(String reportExcelTemplateName);
	
	/**
	 * 删除模板
	 * @param status
	 * @param id
	 */
	void delReportExcelTemplate(Integer id);
	
	/**
	 * 通过ID和地区查询别名
	 * @param id
	 * @param aid
	 * @return
	 */
	List<IndexItemAlias> queryIndexItemAlias(Integer aid);
	
	IndexItemTb queryIndexItemTbByCode(String code);
	
	Integer queryIndexIdByCount(Integer indexId);
}
