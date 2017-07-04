package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.ReportTaskPushListDao;
import com.workmanagement.model.ReportTaskPushList;
import com.workmanagement.util.BaseDaoSupport;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

@Service
public class ReportTaskPushListServiceImpl extends BaseDaoSupport implements ReportTaskPushListService {

	@Autowired 
	private ReportTaskPushListDao reportTaskPushListDao;
	
	@Override
	public List<ReportTaskPushList> queryReportTaskPushLists(Map<String, Object> param, PageSupport ps) {
		return this.getListPageSupportByManualOperation("com.workmanagement.dao.ReportTaskPushListDao.queryReportTaskPushLists", param, ps);
	}

	@Override
	public ReportTaskPushList queryReportTaskPushListById(Integer id) {
		// TODO Auto-generated method stub
		return reportTaskPushListDao.getReportTaskPushListById(id);
	}

	@Override
	public void updateOrSave(ReportTaskPushList reportTaskPushList) {
		// TODO Auto-generated method stub
		if (reportTaskPushList.getReportTaskPushListId()==null) {
			reportTaskPushListDao.insert(reportTaskPushList);
		}else{
			reportTaskPushListDao.update(reportTaskPushList);
		}
	}

	@Override
	public void delete(Integer reportTaskPushListId) {
		// TODO Auto-generated method stub
		reportTaskPushListDao.delete(reportTaskPushListId);
	}

	@Override
	public ReportTaskPushList unfinishedReportTaskPushList(Integer reportTaskPushSetType, Integer sysOrgId,
			Integer reportTaskPushSetTempLateId) {
		// TODO Auto-generated method stub
		return reportTaskPushListDao.unfinishedReportTaskPushList(reportTaskPushSetType, sysOrgId, reportTaskPushSetTempLateId);
	}
	@Override
	public List<ReportTaskPushList> queryAll(Map<String, Object> param,PageSupport ps){
		if(ps!=null){
			PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			return PageHelperSupport.queryCount(reportTaskPushListDao.queryAll(param),ps);
		}else{
			return reportTaskPushListDao.queryAll(param);
		}
	}
	@Override
	public List<ReportTaskPushList> queryReportTaskPushListByStatus(int i){
		return reportTaskPushListDao.queryReportTaskPushListByStatus(i);
	}
	@Override
	public List<ReportTaskPushList> queryList(){
		return reportTaskPushListDao.queryList();
	}
}
