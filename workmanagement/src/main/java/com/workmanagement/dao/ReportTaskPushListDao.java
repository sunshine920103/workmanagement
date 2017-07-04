package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.ReportTaskPushList;

public interface ReportTaskPushListDao {
	public ReportTaskPushList getReportTaskPushListById(@Param("reportTaskPushListId")Integer reportTaskPushListId);
	public void insert(ReportTaskPushList reportTaskPushList);
	public void update(ReportTaskPushList reportTaskPushList);
	void delete(@Param("reportTaskPushListId")Integer reportTaskPushListId);
	
	/**
	 * 最早一条未完成的相关任务
	 * 报送方式 机构id 模板 
	 * @param reportTaskPushSetMethod
	 * @param reportTaskPushOrgId
	 * @param reportTaskPushSetTemplate
	 * @return
	 */
	ReportTaskPushList unfinishedReportTaskPushList(@Param("reportTaskPushSetType")Integer reportTaskPushSetType,@Param("sysOrgId")Integer sysOrgId,@Param("reportTaskPushSetTempLateId")Integer reportTaskPushSetTempLateId);
	public List<ReportTaskPushList> queryAll(@Param("param") Map<String, Object> param);
	public List<ReportTaskPushList> queryReportTaskPushListByStatus(@Param("i") int i);
	public List<ReportTaskPushList> queryList();
}
