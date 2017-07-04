package com.workmanagement.service;

import com.workmanagement.model.IndexItemTb;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
/**
 * 
 * @author wqs
 * 指标项接口
 *
 */
public interface IndexItemTbService {
	
	/**
	 * 根据指标大类和地区s查询指标项 
	 * 
	 */
	List<IndexItemTb> queryItemsByAreaIds(@Param("indexId") Integer indexId,@Param("areaIds") List<Integer> aeraIds);
	
	/**
	 * 根据小指标id获取指标项
	 * @param id
	 * @return
	 */
	IndexItemTb  queryIndexItemTbById(@Param("indexItemId") Integer indexItemId);
	
	/**
	 * 在表中插入这个指标项对应的字段
	 */
	void insertColumn(String indexNameCode,String indexItemCode,Integer indexItemType,Integer varLength,Integer indexItemEmpty );
	
	/**
	 * 根据sql,在表中插入这个指标项对应的字段
	 */
	void addColumnBySql(Map<String,String> sqlmap);
	
	/**
	 * 根据大指标id获取指标项
	 * @param id
	 * @return
	 */
	List<IndexItemTb>  queryIndexItemTbsByIndexId(@Param("indexId") Integer indexId);
	/**
	 * 更新、修改指标项
	 * @param indexItemTb
	 */
	void updateIndexItemTb(IndexItemTb indexItemTb);
	/**
	 * 新增指标项
	 * @param indexItemTb
	 */
	void insertIndexItemTb(IndexItemTb indexItemTb);
	/**
	 * 根据code得到对象
	 * @param code
	 */
	IndexItemTb getIndexItemTbByCode(String indexItemCode);
	/**
	 * 获取所有的数据字典id
	 * @param code
	 */
	List<Integer> getDicIdsInIndexItem();
	/**
	 * 根据indexId得到所有相关indexItemName
	 */
	List<String> selectAllIndexItemNameByIndexId(Integer indexId);
	/**
	 * 根据indexId得到所有相关indexItemCode
	 */
	List<String> selectAllIndexItemCodeByIndexId(Integer indexId);
	/**
	 * 根据指标项类型获取所有指标项
	 * @param indexItemType
	 * @return
	 */
	List<IndexItemTb> selectAllIndexItemByIndexItemType(Integer indexItemType);
	List<String> getIndexIdsInIndexItem(@Param("pdicId")Integer pdicId);
	List<String> getUsedNames(Map<String, Object> sqlmap);
	/**
	 * 根据指标大类id删除指标项
	 * @param indexItemTb
	 */
	
	void deleteIndexItemTb(@Param("indexId")Integer indexId);
	/**
	 * 根据大指标id获取所有有效指标项
	 * @param id
	 * @return
	 */
	List<IndexItemTb> queryIndexItemTbsByIndexIdWithStatus(@Param("IndexId") Integer IndexId);

	/**
	 * 根据指标大类id和指标项类型获取所有指标项
	 * @param indexId
	 * @param indexItemType
	 * @return
	 */
    List<IndexItemTb> getIndexItemsByIdAndType(Integer indexId, Integer indexItemType, List<Integer> sysAreaIds);


    /**
	 * 根据指标大类id和指标项name获取指标项
	 */
	IndexItemTb  getIndexItemsByIndexIdAndName(@Param("IndexId")Integer indexId,@Param("indexItemName") String indexItemName);

    /**
     * 获取指标下启用的指标项
     */
    List<IndexItemTb> getIndexIntemsIsUsedByIdAndAreaIds(@Param("indexId") Integer indexId, @Param("sysAreaIds") List<Integer> sysAreaIds);

	List<IndexItemTb> getIndexIntemsIsUsedByIdAndAreaIdsAndCanNull(@Param("indexId") Integer indexId, @Param("sysAreaIds") List<Integer> sysAreaIds);
}
