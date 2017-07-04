package com.workmanagement.dao;

import com.workmanagement.model.Dic;
import com.workmanagement.model.DicContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据字典的dao接口
 * <p>
 * Created by lzm on 2017/3/8.
 */
public interface DicDao {
    /**
     * 根据主键获取一个数据字典
     *
     * @param dicId 数据字典ID（主键）
     * @return 单条数据字典
     * @throws Exception
     */
    Dic getDicByDicId(@Param("dicId") Integer dicId);

    /**
     * 获取所有数据字典
     *
     * @throws Exception
     */
    List<Dic> getAllDic();

    /**
     * 查询区域下所有的数据字典
     * 根据一个或者多个所属区域ID（地区表主键）
     *
     * @param sysAreaIds 地区ID（主键）
     * @return 地区下所有数据字典的集合
     * @throws Exception
     */
    List<Dic> getDicsBySysAreaId(@Param("sysAreaIds") List<Integer> sysAreaIds);


    List<Dic> getDicsBySysAreaIdHaveThree(@Param("sysAreaIds") List<Integer> sysAreaIds);

    /**
     * 根据名字查一个数据字典
     *
     * @param dicName 数据字典的名字
     * @return 单条数据字典
     * @throws Exception
     */
    Dic getDicByDicName(@Param("dicName") String dicName, @Param("areaId") Integer areaId);

    /**
     * 根据名字判断是否输入有重名
     *
     * @return 判断表里此数据字典名字有无被使用有没有这个数据
     * @throws Exception
     */
    Integer isDicNameBeUsedInDic(@Param("dicName") String dicName, @Param("areaId") Integer areaId);

    /**
     * 根据主键删除一个数据字典
     *
     * @param dicId 数据字典表ID（主键）
     * @return 操作的成功条数
     * @throws Exception
     */
    Integer delByDicId(@Param("dicId") Integer dicId);

    /**
     * 新增一个数据字典
     *
     * @param dic 一条数据字典的数据
     * @return 操作成功的条数
     * @throws Exception
     */
    Integer insertOneDic(Dic dic);

    /**
     * 更新一个数据字典
     *
     * @param dic 一条数据字典的数据
     * @return 操作成功的条数
     * @throws Exception
     */
    Integer updateOneDic(Dic dic);

    /**
     * 查询是否有被使用
     * index_item_tb
     *
     * @return 此数据字典被指标项表使用的条数 大于等于1 表示被使用了
     * @throws Exception
     */
    Integer usedInIndexItem(@Param("dicId") Integer dicId);

    /**
     * 查询是否有被使用
     * sys_check_tb
     *
     * @return 此数据字典被指标项表使用的条数 大于等于1 表示被使用了
     * @throws Exception
     */
    Integer usedInSysIndustry(@Param("dicId") Integer dicId);

    /**
     * 查询是否有被使用
     * sys_gov_tb
     *
     * @return 此数据字典被指标项表使用的条数 大于等于1 表示被使用了
     * @throws Exception
     */
    Integer usedInSysGov(@Param("dicId") Integer dicId);

    Integer selectDicContentNames(DicContent dic);

    Dic getDicByDicIdAndContent(@Param("dicId") Integer i);

    Dic getDicById(Integer id);

    List<Dic> getDicByLikeName(@Param("dicName") String dicName, @Param("sysAreaIds") List<Integer> sysAreaIds);

    List<Dic> getAllDicNotTree();
}
