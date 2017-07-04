package com.workmanagement.dao;

import com.workmanagement.model.SysCheck;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 校验规则表
 * Created by lzm on 2017/3/16.
 */
public interface SysCheckDao {

    /**
     * 超级管理员获取所有
     *
     * @return
     */
    List<SysCheck> getAll();

    /**
     * 添加一条数据
     *
     * @param sysCheck
     * @return
     * @throws Exception
     */
    Integer insertOne(SysCheck sysCheck);

    /**
     * 根据主键获取一条数据
     *
     * @param sysCheckId
     * @return
     * @throws Exception
     */
    SysCheck getOneById(@Param("sysCheckId") Integer sysCheckId);

    /**
     * 根据地区 ids 获取多个校验规则
     *
     * @param sysAreaIds
     * @return
     * @throws Exception
     */
    List<SysCheck> getSomeByAreaIds(@Param("sysAreaIds") List<Integer> sysAreaIds);

    /**
     * 根据主键删除一条数据
     *
     * @param sysCheckId
     * @return
     * @throws Exception
     */
    Integer delOneById(@Param("sysCheckId") Integer sysCheckId);

    /**
     * 根据校验的指标项以及机构ids获取数据
     *
     * @param indexId
     * @param sysAreaIds
     * @param thisDate
     * @return
     */
    List<SysCheck> getSysCheckByIndexIdAndAreaIdsAndTime(@Param("indexId") Integer indexId,
                                                         @Param("sysAreaIds") List<Integer> sysAreaIds,
                                                         @Param("thisDate") Date thisDate);

    /**
     * 更新一个校验规则，只能更新时间
     *
     * @param sysCheck
     * @return
     * @throws Exception
     */
    Integer updateOne(SysCheck sysCheck);


    /**
     * 查询某个企业在某个月的银行授信
     *
     * @param recordTime
     * @param indexItemId
     * @return
     */
    Double getYxsxSxed(@Param("recordTime") String recordTime, @Param("indexItemId") int indexItemId,
                       @Param("yxnbpjjg") String yxnbpjjg);

    /**
     * 查询某个企业在某个月的银行授信除了一些在上报的数据里是update操作的数据
     *
     * @param recordTime
     * @param indexItemId
     * @return
     */
    Double getNotUpdYxsxSxed(@Param("recordTime") String recordTime, @Param("indexItemId") int indexItemId,
                             @Param("yxnbpjjg") String yxnbpjjg, @Param("indexYxsxIds") List<Integer> indexYxdkIds);

    /**
     * 查询某个企业在某个月的银行授信
     *
     * @param recordTime
     * @param indexItemId
     * @return
     */
    Double getYxdkHtje(@Param("recordTime") String recordTime, @Param("indexItemId") int indexItemId,
                       @Param("dkywfsdjrjg") String dkywfsdjrjg);

    /**
     * 查询某个企业在某个月的银行授信除了一些在上报的数据里是update操作的数据
     *
     * @param recordTime
     * @param indexItemId
     * @return
     */
    Double getNotUpdYxdkHtje(@Param("recordTime") String recordTime, @Param("indexItemId") int indexItemId,
                             @Param("dkywfsdjrjg") String dkywfsdjrjg, @Param("indexYxdkIds") List<Integer> indexYxdkIds);

    /**
     * 根据字段名和条件查sum
     *
     * @param map
     * @return
     */
    Map<String, Object> getMapByOnly(Map<String, Object> map);

    Integer insertCode(String type);


    Integer delCode(String type);


    Integer selCode(String type);
}
