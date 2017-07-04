package com.workmanagement.service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.ReportIndexDao;
import com.workmanagement.model.ReportIndex;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportIndexServiceImpl implements ReportIndexService {
    @Autowired
    private ReportIndexDao reportIndexDao;


    /**
     * 查询所有列表
     */
    @Override
    public List<ReportIndex> queryReportIndexs(PageSupport ps) {
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(reportIndexDao.queryReportIndexs(), ps);
    }

    /**
     * 通过id查询
     */
    @Override
    public ReportIndex queryReportIndexsById(Integer id) {
        return reportIndexDao.queryReportIndexsById(id);

    }

    /**
     * 根据指定的报送方法查找列表
     *
     * @param ps
     * @param reportIndexMethod
     * @return
     * @throws Exception
     */
    @Override
    public List<ReportIndex> getReportsByMethod(PageSupport ps, Integer reportIndexMethod) throws Exception {
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(reportIndexDao.getReportsByMethod(reportIndexMethod), ps);
    }

    @Override
    public void insert(ReportIndex reportIndex) {
        reportIndexDao.insert(reportIndex);
    }

    @Override
    public List<ReportIndex> queryReportIndexsByName(String indexName) {
        return reportIndexDao.queryReportIndexsByName(indexName);
    }

    @Override
    public void updata(ReportIndex index) {
        try {
            reportIndexDao.updateById(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer insertOne(ReportIndex reportIndex) throws Exception {
        return reportIndexDao.insertOne(reportIndex);
    }

    /**
     * 根据类别以及机构ids查看信息
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<ReportIndex> getReportsByMethodAndOrgIds(PageSupport ps, Integer reportIndexMethod, List<Integer> sysOrgIds) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("reportIndexMethod", reportIndexMethod);
        map.put("sysOrgIds", sysOrgIds);
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(reportIndexDao.getReportsByMethodAndOrgIds(map), ps);
    }

    @Override
    public Integer updateBySql(String sql) throws Exception {
        return reportIndexDao.updateBySql(sql);
    }
    
    @Override
    public void insertIndex(String sql) throws Exception {
    	reportIndexDao.insertIndex(sql);
    }

    @Override
    public void reportInfo(Map<String, String> sqlMap) {
        reportIndexDao.reportInfo(sqlMap);
    }

    /**
     * 根据条件查询数据
     *
     * @param ps
     * @param sysOrgIds
     * @param reportIndexMethod
     * @param reportIndexTemplate
     * @param reportIndexStatus
     * @param recordDate
     * @return
     * @throws Exception
     */
    @Override
    public List<ReportIndex> getDataBySome(PageSupport ps, List<Integer> sysOrgIds, Integer reportIndexMethod,
                                           String reportIndexTemplate, Integer reportIndexStatus,
                                           String recordDate, String reportSubmitTime) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("sysOrgIds", sysOrgIds);
        map.put("reportIndexMethod", reportIndexMethod);
        map.put("reportIndexTemplate", reportIndexTemplate);
        map.put("reportIndexStatus", reportIndexStatus);
        map.put("recordDate", recordDate);
        map.put("reportSubmitTime", reportSubmitTime);
        if (ps == null) {
            return reportIndexDao.getDataBySome(map);
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(reportIndexDao.getDataBySome(map), ps);
    }

    @Override
    public Integer countBySql(String sql) {
        return reportIndexDao.countBySql(sql);
    }

    @Override
    public List<ReportIndex> queryReporByTempOrgDate(String time, Integer temptId, Integer orgId) {
        return reportIndexDao.queryReporByTempOrgDate(time, temptId, orgId);
    }

    @Override
    public void deleteTempTb(String tempTbName) {
        reportIndexDao.deleteTempTb(tempTbName);
    }

    @Override
    public Integer queryStatusNum(String tempTbName, Integer status) {
        return reportIndexDao.queryStatusNum(tempTbName, status);
    }

}
