package com.workmanagement.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.ReportTaskPushSet;

public interface ReportTaskPushSetDao {
	public ReportTaskPushSet getReportTaskPushSetById(@Param("reportTaskPushSetId")Integer reportTaskPushSetId);
	public void insert(ReportTaskPushSet reportTaskPushSet);
	public void update(ReportTaskPushSet reportTaskPushSet);
	List<ReportTaskPushSet> queryReportTaskPushSets(@Param("id") Integer id);
	public Integer deleteTaskById(@Param("reportTaskPushSetId") Integer reportTaskPushSetId);
	public List<ReportTaskPushSet> queryReportTaskPushSetsAll();
	public ReportTaskPushSet queryReportTaskPushSetByEndTime(@Param("parse") Date parse);
}
