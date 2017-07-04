package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.ReportTaskPushList;
import com.workmanagement.util.PageSupport;

public interface ReportTaskPushListService {
	ReportTaskPushList queryReportTaskPushListById(Integer reportTaskPushListById);
	List<ReportTaskPushList> queryReportTaskPushLists(Map<String, Object> param, PageSupport ps);
	void updateOrSave(ReportTaskPushList reportTaskPushList);
	void delete(Integer reportTaskPushListId);
	/**
	 * 最早一条未完成的相关任务
	 * 报送方式 机构id 模板 
	 * @param reportTaskPushSetMethod
	 * @param reportTaskPushOrgId
	 * @param reportTaskPushSetTemplate
	 * @return
	 */
	ReportTaskPushList unfinishedReportTaskPushList(Integer reportTaskPushSetType,Integer sysOrgId,Integer reportTaskPushSetTempLateId);
	List<ReportTaskPushList> queryAll(Map<String, Object> param,PageSupport ps);
	List<ReportTaskPushList> queryReportTaskPushListByStatus(int i);
	List<ReportTaskPushList> queryList();
}
