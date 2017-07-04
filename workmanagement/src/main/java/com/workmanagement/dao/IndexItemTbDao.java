package com.workmanagement.dao;

import com.workmanagement.model.IndexItemTb;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IndexItemTbDao {
    List<IndexItemTb> queryIndexItemTbsByIndexId(@Param("indexId") Integer indexId);

    IndexItemTb queryIndexItemTbById(@Param("indexItemId") Integer indexItemId);

    /**
     * 更新指标项
     *
     * @param indexItemTb
     */
    void updateIndexItemTb(IndexItemTb indexItemTb);

    /**
     * 根据指标大类和地区s查询指标项
     */
    List<IndexItemTb> queryItemsByAreaIds(@Param("indexId") Integer indexId, @Param("areaIds") List<Integer> aeraIds);

    void insertIndexItemTb(IndexItemTb indexItemTb);

//	void delIndexItemTbById(@Param("id") Integer id);

    /**
     * 在表中插入这个指标项对应的字段
     */
    void insertColumn(Map<Object, Object> map);

    /**
     * 根据sql,在表中插入这个指标项对应的字段
     */
    void addColumnBySql(Map<String, String> sqlmap);

    /**
     * 根据code得到对象
     *
     * @param code
     */
    IndexItemTb getIndexItemTbByCode(String indexItemCode);

    List<Integer> getDicIdsInIndexItem();

    /**
     * 根据indexId得到所有相关indexItemName
     */
    List<String> selectAllIndexItemNameByIndexId(@Param("indexId") Integer indexId);

    /**
     * 根据indexId得到所有相关indexItemCode
     */
    List<String> selectAllIndexItemCodeByIndexId(@Param("indexId") Integer indexId);

    /**
     * 根据指标项类型获取所有指标项
     *
     * @param indexItemType
     * @return
     */
    List<IndexItemTb> selectAllIndexItemByIndexItemType(@Param("indexItemType") Integer indexItemType);

    List<String> getIndexIdsInIndexItem(@Param("pdicId") Integer pdicId);

    List<String> getUsedNames(Map<String, Object> sqlmap);

    void deleteIndexItemTb(IndexItemTb indexItemTb);

    /**
     * 根据大指标id获取所有有效小指标
     *
     * @param id
     * @return
     */
    List<IndexItemTb> queryIndexItemTbsByIndexIdWithStatus(@Param("indexId") Integer indexId);


    /**
     * 根据指标大类id和指标项类型获取所有指标项
     *
     * @param indexId
     * @param indexItemType
     * @return
     */
    List<IndexItemTb> getIndexItemsByIdAndType(@Param("indexId") Integer indexId, @Param("indexItemType") Integer indexItemType,
                                               @Param("sysAreaIds") List<Integer> sysAreaIds);

    /**
     * 根据指标项name获取指标项
     */
    IndexItemTb getIndexItemsByIndexIdAndName(@Param("indexId") Integer indexId, @Param("indexItemName") String indexItemName);


    /**
     * 获取指标下启用的指标项
     */
    List<IndexItemTb> getIndexIntemsIsUsedByIdAndAreaIds(@Param("indexId") Integer indexId, @Param("sysAreaIds") List<Integer> sysAreaIds);
	/**
	 * 根据指标大类id删除指标项
	 * @param indexItemTb
	 */
	
	void deleteIndexItemTb(@Param("indexId")Integer indexId);

    List<IndexItemTb> getIndexIntemsIsUsedByIdAndAreaIdsAndCanNull(@Param("indexId") Integer indexId, @Param("sysAreaIds") List<Integer> sysAreaIds);
}
