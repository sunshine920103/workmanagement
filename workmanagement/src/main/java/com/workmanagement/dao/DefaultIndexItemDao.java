package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.DefaultIndexItemCombine;

import org.apache.ibatis.annotations.Param;

/**
 * 二码对应DAO
 *
 * @author lzl
 */
public interface DefaultIndexItemDao {
    List<DefaultIndexItem> queryBycode(DefaultIndexItem defaultIndexItem);

    List<DefaultIndexItemCombine> queryByTime(Map<String, Object> map);

    DefaultIndexItem queryById(@Param("id")Integer id);
    
    DefaultIndexItem getById(@Param("id")Integer id);

    void updateDefaultIndexItem(DefaultIndexItem defaultIndexItem);

    void delectDefaultIndexItem(Integer id);

    /**
     * 根据俩码查找唯一的数据
     *
     * @param codeCredit
     * @param codeOrg
     * @return
     */
    DefaultIndexItem queryByCodeAndCredit(@Param("codeCredit") String codeCredit, @Param("codeOrg") String codeOrg);

    /**
     * 根据	组织机构代码	查询
     *
     * @param codeOrg
     * @return
     */
    List<DefaultIndexItem> getByCodeOrg(@Param("codeOrg") String codeOrg, @Param("areaId") Integer areaId);

    /**
     * 根据	统一社会信用代码	查询
     *
     * @param codeCredit
     * @return
     */
    List<DefaultIndexItem> getByCredit(@Param("codeCredit") String codeCredit, @Param("areaId") Integer areaId);

    /**
     * 根据	统一社会信用代码	或	组织机构代码	查询
     *
     * @param values
     * @return
     */
    List<DefaultIndexItem> getByCreditOrCode(@Param("values") String values, @Param("areaId") Integer areaId);

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
    List<DefaultIndexItem> queryDataBySql(@Param("querySql") String querySql);

    /**
     * 更新二码表对象
     */
    void update(DefaultIndexItem defaultIndexItem);


    DefaultIndexItem queryByAll(Map<String, Object> map);
    /**
     * 企业标识查询
     * @param map
     * @return
     */
    DefaultIndexItem queryByComPanyShow(Map<String, Object> map);
    /**
     * 根据社会信用码查询
     * @param codeCredit
     * @return
     */
    List<DefaultIndexItem> queryByCridet(@Param("codeCredit")String codeCredit );
    
    /**
     * 根据社会信用码查询
     * @param codeCredit
     * @return
     */
    List<DefaultIndexItem> queryByOrg(@Param("codeOrg")String codeOrg );
   void insertDefaultIndexItemCombine(DefaultIndexItemCombine de);

   List<DefaultIndexItemCombine> queryDefaultIndexItemCombine(@Param("orgId")Integer orgId);

   DefaultIndexItemCombine queryDefaultIndexItemCombineTop();
  void updateDefaultIndexItemCombine(DefaultIndexItemCombine de);
  

  List<DefaultIndexItem> queryAllByName(Map<String, Object> map);
}
