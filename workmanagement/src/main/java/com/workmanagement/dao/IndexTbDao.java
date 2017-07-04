package com.workmanagement.dao;

import com.workmanagement.model.IndexTb;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 作者 wqs
 * @date 创建时间：2017年3月13日 下午6:50:41
 * 对指标大类操作的Dao接口
 */
public interface IndexTbDao {

    /**
     * 新增
     */
    void insertIndex(IndexTb indexTb);

    /**
     * 创建指标大类对应的数据库表
     */
    void createTable(@Param("tbName") String tbName, @Param("tbId") String tbId);

    /**
     * 根据指标大类名称对应的数据库表名查询该指标
     */
    public IndexTb queryIdByCode(@Param("indexCode") String code);
    
    /**
     * 根据指标大类名称对应的数据库表名查询该指标加地区
     */
    public IndexTb queryByCodeAndAreaId(@Param("indexCode") String code,@Param("areaId") Integer areaId);

    /**
     * 根据指标id查询只带启用的指标项的指标
     */
    IndexTb queryIndexWithUsedItemsById(@Param("indexId") Integer indexId);

    /**
     * 报送数据
     */
    void reportInfo(List<Map<String, Object>> excelList, List<String> indexNames, String tbName);


    /**
     * 获取所有name
     */
    List<IndexTb> selectAll();

    /**
     * 通过指标大类名称获取
     */
    IndexTb queryIdByName(@Param("indexName") String indexName);
    
	/**
	 * 通过指标名称和地区id
	 */
	IndexTb queryByNameAndAreaId(@Param("indexName") String indexName,@Param("areaId") Integer areaId);

    /**
     * 根据indexItemCode获取indexCode
     */
    String selectIndexCodeByindexItemCode(String indexItemCode);

    /**
     * 更新
     */
    Integer updateIndex(IndexTb indexTb);

    /**
     * 更新指标大类对应的数据库表
     */
    void updateTable(IndexTb indexTb);

    /**
     * 登录地区为四川省：查询所有指标大类
     */
    List<IndexTb> queryAll();

    /**
     * 登录地区为市：查询所有指标大类
     */
    List<IndexTb> queryAll2(Integer id);

    /**
     * 登录地区为四川省:根据指标大类名称模糊查询
     *
     * @param words
     * @return
     */
    List<IndexTb> mohuQueryAll(@Param("words") String words, @Param("aId") Integer aId);

    /**
     * 登录地区为市:根据指标大类名称模糊查询
     *
     * @param words
     * @return
     */
    List<IndexTb> mohuQueryAll2(@Param("areaIds")List<Integer> areaIds, String words);

    /**
     * 根据id查询
     */
    IndexTb queryById(Integer id);

    /**
     * 通过地区id查询指标大类
     *
     * @param sysAreaId
     * @return
     */
    List<IndexTb> queryIndexBySysAreaId(Integer sysAreaId);

    /**
     * 根据指标大类名称模糊查询
     */
    List<IndexTb> mohuQuery(String indexName);

    /**
     * 将指标大类置为无效
     */
    void change(Integer indexId, Integer status);

    /**
     * 通过多个地区id查询指标大类
     *
     * @param sysAreaIds
     * @return
     */
    List<IndexTb> queryIndexBySysAreaIds(List<Integer> sysAreaIds);

    IndexTb getIndexTbbyIndexCode(String indexCode);

    /**
     * 获取所有name
     */
    List<String> selectAllIndexName();

    /**
     * 获取所有code
     */
    List<String> selectAllIndexCode();

    List<IndexTb> queryIndex(@Param("map") Map<String, Object> map);


    /**
     * 获取所有的银行贷款信息
     */
    List<Map<String, Object>> queryIndexIndexYhdk(Map<String, Object> param);


    /**
     * 四川省能查看全部的指标个数
     */
    public Integer countAll();


    /**
     * 司法信息
     */
    List<Map<String, Object>> queryIndexIndexSfxx(Map<String, Object> param);

    /**
     * 行政处罚信息
     */
    List<Map<String, Object>> queryIndexIndexXzcfxx(Map<String, Object> param);

    /**
     * 登录市所建和四川省建的指标个数
     */
    Integer countAll2(Integer id);

    List<Map<String, Object>> queryIndexTbByCode(Map<String, Object> map);

    List<IndexTb> getAllUsedIndexTb();
    
    /**
	 * 删除指标大类
	 */
	void del(@Param("indexId") Integer indexId);
}
