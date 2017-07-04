package com.workmanagement.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 关联信息查询
 * Created by lzm on 2017/3/24.
 */
public interface RelateInfoDao {

    Map<String, Object> queryData(@Param("querySql") String querySql);

    List<Map<String, Object>> queryMoreData(@Param("querySql") String querySql);

    Integer insertOne(@Param("insertSql") String insertSql);
}
