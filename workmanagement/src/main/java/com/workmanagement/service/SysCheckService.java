package com.workmanagement.service;

import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.SysCheck;
import com.workmanagement.util.PageSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lzm on 2017/3/16.
 */
public interface SysCheckService {

    List<SysCheck> getAll(PageSupport ps);

    /**
     * 添加一条数据
     *
     * @return
     * @throws Exception
     */
    Integer insertOne(String checkItems, Integer sysAreaId, String checkSDate,
                      String checkEDate, Integer indexId, Integer orgCreatorId,
                      String sysCheckFormula, String sysCheckExplain) throws Exception;

    /**
     * 根据主键获取一条数据
     *
     * @param sysCheckId
     * @return
     * @throws Exception
     */
    SysCheck getOneById(Integer sysCheckId) throws Exception;

    /**
     * 根据单个机构id获取一堆数据
     *
     * @param sysAreaIds
     * @return
     * @throws Exception
     */
    List<SysCheck> getSomeByAreaIds(PageSupport ps, List<Integer> sysAreaIds) throws Exception;

    /**
     * 根据主键删除一条数据
     *
     * @param sysCheckId
     * @return
     * @throws Exception
     */
    boolean delOneById(Integer sysCheckId) throws Exception;

    /**
     * 专门校验 银行贷款和银行授信的方法。属于内置校验规则
     *
     * @param txt
     * @param excel
     * @param thisDate
     * @param errorList
     * @param indexTb
     */
    void checkDkAndSx(List<Map<String, Object>> txt, List<Map<String, Object>> excel, Date thisDate, List<String> errorList, IndexTb indexTb) throws Exception;

    /**
     * 进行校验
     *
     * @param items    需要校验的指标项集合
     * @param cellData 这一行的数据,下标从2开始
     * @param sysOrgId 需要验证的机构的id
     * @param thisDate 归档时间
     * @return
     * @throws Exception
     */
    boolean getCheckData(List<IndexItemTb> items, List<Object> cellData, Integer sysOrgId, Integer indexId, Date thisDate) throws Exception;

    /**
     * 更新一个校验规则
     *
     * @return
     * @throws Exception
     */
    boolean updateOne(Integer sysCheckId, String checkItems, Integer areaId, String checkSDate,
                      String checkEDate, Integer indexId, Integer orgCreatorId, String sysCheckFormula,
                      String sysCheckExplain) throws Exception;


    boolean checkCodeCredit(String codeCredit);


    boolean checkCodeOrg(String codeOrg);

    boolean selCode(boolean isCodeCredit);

    boolean changeCode(boolean isCodeCredit);
}
