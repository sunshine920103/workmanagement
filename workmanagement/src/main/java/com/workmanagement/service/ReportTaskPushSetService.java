package com.workmanagement.service;

import java.util.Date;
import java.util.List;

//import com.workmanagement.model.ReportTaskPushList;
import com.workmanagement.model.ReportTaskPushSet;
import com.workmanagement.util.PageSupport;

public interface ReportTaskPushSetService {
	ReportTaskPushSet queryReportTaskPushSetById(Integer reportTaskPushSetById);
	List<ReportTaskPushSet> queryReportTaskPushSets(Integer id, PageSupport ps) throws Exception;
	void updateOrSave(ReportTaskPushSet reportTaskPushSet);
	Integer deleteTaskById(Integer reportTaskPushSetId);
	//查询所有任务
	List<ReportTaskPushSet> queryReportTaskPushSetsAll();
	ReportTaskPushSet queryReportTaskPushSetByEndTime(Date parse);
}
