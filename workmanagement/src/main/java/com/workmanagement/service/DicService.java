package com.workmanagement.service;

import com.workmanagement.model.Dic;
import com.workmanagement.model.DicContent;
import com.workmanagement.util.PageSupport;

import java.util.List;

/**
 * 数据字典的service
 * ！业务逻辑都在这里处理
 * <p>
 * Created by lzm on 2017/3/8.
 */
public interface DicService {
    String SYS_DIC = "Dic_And_AicContent";

    /**
     * 根据主键获取一个数据字典
     *
     * @param dicId 某个数据字典的id（主键）
     * @return 查询出来的单个数据字典
     * @throws Exception
     */
    Dic getDicByDicId(Integer dicId);

    /**
     * 获取所有数据字典
     *
     * @throws Exception
     */
    List<Dic> getAllDic(PageSupport ps);

    /**
     * 查询区域下所有的数据字典
     * 根据一个或者多个所属区域ID（地区表主键）
     *
     * @param sysAreaId 区域表的主键，可能有一个或者多个
     * @param ps        分页的model
     * @return 查询出来的数据字典集合
     * @throws Exception
     */
    List<Dic> getDicsBySysAreaId(PageSupport ps, List<Integer> sysAreaId);


    List<Dic> getDicsBySysAreaIdHaveThree(PageSupport ps, List<Integer> sysAreaId);

    /**
     * 根据名字查一个数据字典
     *
     * @param dicName 数据字典的名字
     * @return 单条数据字典
     * @throws Exception
     */
    Dic getDicByDicName(String dicName, Integer areaId);

    /**
     * 根据名字判断是否输入有重名
     *
     * @return 判断表里此数据字典名字有无被使用有没有这个数据
     * @throws Exception
     */
    boolean isDicNameBeUsedInDic(String dicName, Integer areaId);

    /**
     * 根据主键删除一个数据字典
     *
     * @param dicId 某个数据字典的id（主键）
     * @return 返回操作成功否，如果不成功返回错误信息，成功则为null
     * @throws Exception
     */
    boolean deleteByDicId(Integer dicId);

    /**
     * 新增一个数据字典
     *
     * @param dic 需要新增的数据字典model
     * @return 返回操作成功否
     * @throws Exception
     */
    boolean insertOneDic(Dic dic);

    /**
     * 修改一个数据字典
     *
     * @param dic 一条数据字典的数据
     * @return 返回操作成功否，如果不成功返回错误信息，成功则为null
     * @throws Exception
     */
    boolean updateOneDic(Dic dic);

    /**
     * 查询是否有被使用
     *
     * @return 此数据字典被指标项表使用的条数 大于等于1 表示被使用了
     * @throws Exception
     */
    boolean isThisBeUsed(Integer dicId);

    /**
     * 获取字典下，名称的个数
     *
     * @return
     */
    Integer selectDicContentNames(DicContent dic);

    /**
     * 通过字典id查询本身及关联数据
     *
     * @return
     */
    Dic getDicByDicIdAndContent(int i);

    Dic getDicById(Integer id);

    List<Dic> getDicByLikeName(PageSupport ps, String dicName, List<Integer> sysAreaIds);

    List<Dic> getAllDicNotTree(PageSupport ps);

    /**
     * 重新给名字设置一下带上地区名字,除了四川省的
     *
     * @param dicList
     */
    void reSetDIcName(List<Dic> dicList);
}
