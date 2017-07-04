package com.workmanagement.service;

import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.DefaultIndexItemCombine;
import com.workmanagement.util.PageSupport;

import java.util.List;
import java.util.Map;

public interface DefaultIndexItemService {

    List<DefaultIndexItem> queryBycode(DefaultIndexItem defaultIndexItem);

    List<DefaultIndexItemCombine> queryByTime(Map<String, Object> map, PageSupport ps);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    DefaultIndexItem queryById(Integer id);
    
    /**
     * 通过id查询,简单映射
     * @param id
     * @return
     */
    DefaultIndexItem getById(Integer id);
    
    /**
     * 根据社会信用码查询
     * @param codeCredit
     * @return
     */
    List<DefaultIndexItem> queryByCridet(String codeCredit );
    
    /**
     * 根据组织机构码查询
     * @param codeCredit
     * @return
     */
    List<DefaultIndexItem> queryByOrg(String codeOrg );
    
    void updateDefaultIndexItem(DefaultIndexItem defaultIndexItem);


    void delectDefaultIndexItem(Integer id);

    /**
     * 根据俩码查找唯一的数据
     *
     * @param codeCredit
     * @param codeOrg
     * @return
     */
    DefaultIndexItem queryByCodeAndCredit(String codeCredit, String codeOrg);

    /**
     * 根据	组织机构代码	查询
     *
     * @param codeOrg
     * @return
     */
    List<DefaultIndexItem> getByCodeOrg(String codeOrg, Integer areaId);

    /**
     * 根据	统一社会信用代码	查询
     *
     * @param codeCredit
     * @return
     */
    List<DefaultIndexItem> getByCredit(String codeCredit, Integer areaId);

    /**
     * 根据	统一社会信用代码	或	组织机构代码	查询
     *
     * @param values
     * @return
     */
    List<DefaultIndexItem> getByCreditOrCode(String values, Integer areaId);


    /**
     * 保存
     */
    void dinsert(DefaultIndexItem defaultIndexItem);

    /**
     * 通过sql查询数据
     *
     * @param querySql
     * @return
     */
    List<DefaultIndexItem> queryDataBySql(String querySql);


    DefaultIndexItem queryByAll(Map<String, Object> map);

    DefaultIndexItem queryByComPanyShow(Map<String, Object> map);
    
    

    void insertDefaultIndexItemCombine(DefaultIndexItemCombine de);

    List<DefaultIndexItemCombine> queryDefaultIndexItemCombine(Integer orgId,PageSupport ps);

    DefaultIndexItemCombine queryDefaultIndexItemCombineTop();
    

    void updateDefaultIndexItemCombine(DefaultIndexItemCombine de);


    String getOtherAreaDefaultIds(Integer defaultId);
    

    List<DefaultIndexItem> queryAllByName(Map<String, Object> map);
}
