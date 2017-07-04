package com.workmanagement.service;


import com.workmanagement.model.ReportIndex;
import com.workmanagement.util.PageSupport;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReportIndexService {
    /**
     * 查询所有所有列表
     */
    List<ReportIndex> queryReportIndexs(PageSupport ps);

    /**
     * 查询：同一个模板，同一个机构，同一天是否上报有成功记录
     */
    List<ReportIndex> queryReporByTempOrgDate(@Param("time") String time,
                                              @Param("temptId") Integer temptId, @Param("orgId") Integer orgId);

    /**
     * 根据指定的报送方法查找列表
     *
     * @param ps
     * @return
     * @throws Exception
     */
    List<ReportIndex> getReportsByMethod(PageSupport ps, Integer reportIndexMethod) throws Exception;

    /**
     * 更新数据
     */
    Integer updateBySql(@Param("sql") String sql) throws Exception;
    
    /**
     * 执行DDL
     */
    void insertIndex(@Param("sql") String sql) throws Exception;

    /**
     * 报送数据
     */
    void reportInfo(@Param("sqlMap") Map<String, String> sqlMap);

    ReportIndex queryReportIndexsById(Integer id);

    /**
     * 添加数据到报送记录表
     *
     * @param reportIndex
     */
    void insert(ReportIndex reportIndex);

    /**
     * 通过名称查询
     *
     * @param reportIndex
     */
    List<ReportIndex> queryReportIndexsByName(String indexName);


    void updata(ReportIndex index);

    /**
     * 增加一条数据
     *
     * @param reportIndex
     */
    Integer insertOne(ReportIndex reportIndex) throws Exception;


    /**
     * 根据类别以及机构ids查看信息
     *
     * @return
     * @throws Exception
     */
    List<ReportIndex> getReportsByMethodAndOrgIds(PageSupport ps, Integer reportIndexMethod, List<Integer> sysOrgIds) throws Exception;

    Integer countBySql(String sql);

    /**
     * 删除动态表
     */
    void deleteTempTb(String tempTbName);

    /**
     * 查询动态表数据状态
     */
    Integer queryStatusNum(String tempTbName, Integer status);

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
    List<ReportIndex> getDataBySome(PageSupport ps, List<Integer> sysOrgIds, Integer reportIndexMethod,
                                    String reportIndexTemplate, Integer reportIndexStatus,
                                    String recordDate, String reportSubmitTime) throws Exception;
}

