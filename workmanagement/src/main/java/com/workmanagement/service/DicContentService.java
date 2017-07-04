package com.workmanagement.service;

import com.workmanagement.model.DicContent;

import java.util.List;

/**
 * 数据字典详情表的service
 * ！业务逻辑都在这里处理
 * <p>
 * Created by lzm on 2017/3/8.
 */
public interface DicContentService {

    /**
     * 根据主键获取数据字典详细项
     *
     * @param dicContentId 字典详细页的ID（主键）
     * @return 单条字典详细页
     */
    DicContent getDicContentById(Integer dicContentId);

    /**
     * 根据主键获取数据字典详细项
     *
     * @param dicId 数据字典的ID（本表外键）
     * @return 多条字典详细页
     */
    List<DicContent> getDicContentsByDicId(Integer dicId);

    /**
     * 根据主键删除一条数据字典详细项
     *
     * @param dicContentId 字典详细页的ID（主键）
     * @return 操作成功条数
     */
    boolean delOneDicContentById(Integer dicContentId);

    /**
     * 根据dic_id删除多条数据字典详细项
     *
     * @param dicId 数据字典表的ID（外键）
     * @return 操作成功条数
     */
    boolean delDicContentsByDicId(Integer dicId);

    /**
     * 添加一条数据字典详细项
     *
     * @param dicContent 一条字典详细页的数据
     * @return 操作成功条数
     */
    boolean insertOneContent(DicContent dicContent);

    /**
     * 查询输入的字典代码在表内是否被使用
     * 用于判断用户新增数据是否重复
     *
     * @param dicId          数据字典的ID（此表外键）
     * @param dicContentCode 字典代码
     * @return 如果被使用则返回值 大于等于1
     * @throws Exception
     */
    boolean isDicContentCodeBeUsed(Integer dicId, String dicContentCode);

    /**
     * 查询输入的指标值在表内是否被使用
     * 用于判断用户新增数据是否重复
     *
     * @param dicId           数据字典的ID（此表外键）
     * @param dicContentValue 指标值
     * @return 如果被使用则返回值 大于等于1
     * @throws Exception
     */
    boolean isDicContentValueBeUsed(Integer dicId, String dicContentValue);

    /**
     * 判断这个字典详情是否被使用
     *
     * @param DicContentId 字典详细页的ID（主键）
     * @return 是否被使用
     * @throws Exception
     */
    boolean isThisBeUsed(Integer DicContentId);

    /**
     * 根据数据字典id和数据字典目录code查询
     *
     * @param dicId
     * @return xiehao
     */
    DicContent getDicContentByDicIDAndCode(String dicContentCode, Integer dicId);

    /**
     * xiehao
     */
    void insertOrUpdate(DicContent dicContent);

    List<DicContent> getDicByDicIdAndContent(int i);

    /**
     * 查询字典指标值是否存在
     *
     * @param dicContentValue
     * @return
     * @throws Exception
     */
    boolean isContentValueHaved(String dicContentValue);

    /**
     * 根据指标值获取数据字典ID
     *
     * @param dicContentValue
     * @return
     * @throws Exception
     */
    DicContent getDicIdByDicContentValue(String dicContentValue);

    /**
     * 根据数据字典id和指标的值，查询数据字典（唯一值）
     *
     * @return
     */
    DicContent getDicIdByDicContentValueAndDicId(String dicContentValue, Integer dicId);

    List<DicContent> queryAllContent();

    DicContent queryContentByDicIdAndValues(Integer dicId, String values);

    boolean updDicContent(Integer dicId, String values);
}
