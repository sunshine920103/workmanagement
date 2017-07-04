package com.workmanagement.dao;


import com.workmanagement.model.DicContent;
import com.workmanagement.model.IndexTb;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据字典详情表的dao接口
 * <p>
 * Created by lzm on 2017/3/8.
 */
public interface DicContentDao {

    /**
     * 根据主键获取数据字典详细项
     *
     * @param dicContentId 字典详细页的ID（主键）
     * @return 单条字典详细页
     */
    DicContent getDicContentById(@Param("dicContentId") Integer dicContentId);

    /**
     * 根据数据字典id（外键）获取这个数据字典下的所有字典详情表
     *
     * @param dicId 此表的外键（数据字典表的主键）
     * @return 多条字典详细页
     */
    List<DicContent> getDicContentsByDicId(@Param("dicId") Integer dicId);

    /**
     * 根据主键删除一条数据字典详细项
     *
     * @param dicContentId 字典详细页的ID（主键）
     * @return 操作成功条数
     */
    Integer delOneDicContentById(@Param("dicContentId") Integer dicContentId);

    /**
     * 根据dic_id删除多条数据字典详细项
     *
     * @param dicId 数据字典表的ID（外键）
     * @return 操作成功条数
     */
    Integer delDicContentsByDicId(@Param("dicId") Integer dicId);

    /**
     * 添加一条数据字典详细项
     *
     * @param dicContent 一条字典详细页的数据
     * @return 操作成功条数
     */
    Integer insertOneContent(DicContent dicContent);

    /**
     * 更新一条数据字典详细项
     *
     * @param dicContent 一条字典详细页的数据
     * @return 操作成功条数
     */
    Integer updateOneContent(DicContent dicContent);

    /**
     * 查询输入的字典代码在表内是否被使用
     * 用于判断用户新增数据是否重复
     *
     * @param dicId          数据字典的ID（此表外键）
     * @param dicContentCode 字典代码
     * @return 如果被使用则返回值 大于等于1
     * @throws Exception
     */
    Integer isDicContentCodeBeUsed(@Param("dicId") Integer dicId, @Param("dicContentCode") String dicContentCode);

    /**
     * 查询输入的指标值在表内是否被使用
     * 用于判断用户新增数据是否重复
     *
     * @param dicId           数据字典的ID（此表外键）
     * @param dicContentValue 指标值
     * @return 如果被使用则返回值 大于等于1
     * @throws Exception
     */
    Integer isDicContentValueBeUsed(@Param("dicId") Integer dicId, @Param("dicContentValue") String dicContentValue);

    /**
     * 判断这个字典详情是否被使用
     *
     * @param dicContentId 字典详细页的ID（主键）
     * @return 如果被使用则返回值 大于等于1
     * @throws Exception
     */
    Integer isThisBeUsed(@Param("dicContentId") Integer dicContentId);

    List<IndexTb> getIndexCodeByDicCBeUsed(@Param("dicContentId") Integer dicContentId);

    List<String> getIndexItemCode(@Param("dicContentId") Integer dicContentId, @Param("indexId") Integer indexId);

    Integer getCount(@Param("querySql") String querySql);

    DicContent getDicContentByDicIDAndCode(@Param("dicContentCode") String dicContentCode, @Param("dicId") Integer dicId);

    void insert(DicContent dicContent);

    void update(DicContent dicContent);

    List<DicContent> getDicByDicIdAndContent(@Param("dicId") int i);

    /**
     * 查看指标值有没有
     *
     * @param dicContentValue
     * @return
     * @throws Exception
     */
    Integer isContentValueHaved(@Param("dicContentValue") String dicContentValue);

    /**
     * 根据指标值获取数据字典ID
     *
     * @param dicContentValue
     * @return
     * @throws Exception
     */
    DicContent getDicIdByDicContentValue(@Param("dicContentValue") String dicContentValue);

    /**
     * 根据数据字典id和指标的值，查询数据字典（唯一值）
     *
     * @return
     */
    DicContent getDicIdByDicContentValueAndDicId(@Param("dicContentValue") String dicContentValue,
                                                 @Param("dicId") Integer dicId);

    List<DicContent> queryAllContent();

    /**
     * 通过大表的id和小表的数据查询数据
     *
     * @param map
     * @return
     * @throws Exception
     */
    DicContent queryContentByDicIdAndValues(Map<String, Object> map);
}
