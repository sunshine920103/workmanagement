package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.ReportExcelTemplateDao;
import com.workmanagement.model.IndexItemAlias;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.ReportExcelTemplate;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.util.BaseDaoSupport;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

/**
 * 
 * @author tianhao
 *
 */
@Service
public class ReportExcelTemplateServiceImpl extends BaseDaoSupport implements ReportExcelTemplateService {

	@Autowired
	private ReportExcelTemplateDao reportExcelTemplateDao;
	
	@Autowired
	private SysManageLogService sysManageLogService;
	
	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#queryReportExcelTemplateList()
	 */
	@Override
	public List<ReportExcelTemplate> queryReportExcelTemplateList(Map<String, Object> map,PageSupport ps) {
		if(ps==null){
			return reportExcelTemplateDao.queryReportExcelTemplateList(map);
		}
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(reportExcelTemplateDao.queryReportExcelTemplateList(map),ps);
	}

	@Override
	public List<ReportExcelTemplate> queryReportExcelTemplate(Map<String, Object> map,String name) {
		return reportExcelTemplateDao.queryReportExcelTemplate(map,name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#queryAreaAll()
	 */
	@Override
	public List<SysArea> queryAreaAll() {
		
		return reportExcelTemplateDao.queryAreaAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#selectAllIndexName()
	 */
	@Override
	public List<String> selectAllIndexName(Map<String, Object> map) {
		
		return reportExcelTemplateDao.selectAllIndexName(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#selectAllIndexCode()
	 */
	@Override
	public List<String> selectAllIndexCode(Map<String, Object> map) {
		
		return reportExcelTemplateDao.selectAllIndexCode(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#selectAllIndexId()
	 */
	@Override
	public List<Integer> selectAllIndexId(Map<String, Object> map) {
		
		return reportExcelTemplateDao.selectAllIndexId(map);
	}

	@Override
	public List<IndexItemTb> queryIndexItemsByIndex(Map<String, Object> map) {
		
		return reportExcelTemplateDao.queryIndexItemsByIndex(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#queryIndexTbById(java.lang.Integer)
	 */
	@Override
	public IndexTb queryIndexTbById(Integer id) {
		
		return reportExcelTemplateDao.queryIndexTbById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#allReportExcelTemplateName()
	 */
	@Override
	public List<String> allReportExcelTemplateName(Map<String, Object> map) {
		
		return reportExcelTemplateDao.allReportExcelTemplateName(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#insertReportExcelTemplate(com.workmanagement.model.ReportExcelTemplate)
	 */
	@Override
	@Transactional
	public void insertReportExcelTemplate(ReportExcelTemplate reportExcelTemplate,HttpServletRequest request) {
		
		reportExcelTemplateDao.insertReportExcelTemplate(reportExcelTemplate);
		SysManageLog sm=new SysManageLog();
		sm.setSysManageLogMenuName("EXCEL模板设置");
		sm.setIndexId(reportExcelTemplate.getIndexId());
		sm.setSysManageLogOperateType(SysManageLog.INSERT_SYSMANAGElOG);
		sm.setSysManageLogResult(true);
		sm.setSysManageLogCount(1);
		sysManageLogService.insertSysManageLogTb(sm, request);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#getReportExcelTemplateById(java.lang.String)
	 */
	@Override
	public ReportExcelTemplate queryReportExcelTemplateById(Integer reportExcelTemplateId) {
		
		return reportExcelTemplateDao.queryReportExcelTemplateById(reportExcelTemplateId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#queryReportExcelTemplateByName(java.lang.String)
	 */
	@Override
	public ReportExcelTemplate queryReportExcelTemplateByName(String reportExcelTemplateName) {
		
		return reportExcelTemplateDao.queryReportExcelTemplateByName(reportExcelTemplateName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#updateReportExcelTemplateStatus(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void delReportExcelTemplate(Integer id) {
		
		reportExcelTemplateDao.delReportExcelTemplate(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#queryIndexItemAlias(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<IndexItemAlias> queryIndexItemAlias(Integer aid) {
		
		return reportExcelTemplateDao.queryIndexItemAlias(aid);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#queryIndexItemTbByCode(java.lang.String)
	 */
	@Override
	public IndexItemTb queryIndexItemTbByCode(String code) {
		
		return reportExcelTemplateDao.queryIndexItemTbByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ReportExcelTemplateService#queryIndexIdByCount(java.lang.Integer)
	 */
	@Override
	public Integer queryIndexIdByCount(Integer indexId) {
		
		return reportExcelTemplateDao.queryIndexIdByCount(indexId);
	}
}
