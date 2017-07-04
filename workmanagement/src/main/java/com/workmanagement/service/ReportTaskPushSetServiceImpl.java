package com.workmanagement.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.ReportTaskPushSetDao;
import com.workmanagement.model.ReportTaskPushSet;
import com.workmanagement.util.BaseDaoSupport;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

@Service
public class ReportTaskPushSetServiceImpl extends BaseDaoSupport implements ReportTaskPushSetService {

	@Autowired 
	private ReportTaskPushSetDao reportTaskPushSetDao;
	@Autowired 
//	private ReportTaskPushListDao reportTaskPushListDao;
	@Override
	public List<ReportTaskPushSet> queryReportTaskPushSetsAll(){
		return reportTaskPushSetDao.queryReportTaskPushSetsAll();
	}
	
	@Override
	public List<ReportTaskPushSet> queryReportTaskPushSets(Integer id,PageSupport ps) throws Exception {
		if(ps!=null){
			PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			return PageHelperSupport.queryCount(reportTaskPushSetDao.queryReportTaskPushSets(id),ps);
		}else{
			return reportTaskPushSetDao.queryReportTaskPushSets(id);
		}
	    
	}

	@Override
	public ReportTaskPushSet queryReportTaskPushSetById(Integer id) {
		// TODO Auto-generated method stub
		return reportTaskPushSetDao.getReportTaskPushSetById(id);
	}
	@Override
	public Integer deleteTaskById(Integer reportTaskPushSetId){
		return reportTaskPushSetDao.deleteTaskById(reportTaskPushSetId);
	}
	@Override
	public void updateOrSave(ReportTaskPushSet reportTaskPushSet) {
		// TODO Auto-generated method stub
		if (reportTaskPushSet.getReportTaskPushSetId()==null) {
			reportTaskPushSetDao.insert(reportTaskPushSet);
		}else{
			reportTaskPushSetDao.update(reportTaskPushSet);
		}
	}
	@Override
	public ReportTaskPushSet queryReportTaskPushSetByEndTime(Date parse){
		return reportTaskPushSetDao.queryReportTaskPushSetByEndTime(parse);
	}
}
