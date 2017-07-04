package com.workmanagement.service;


import com.workmanagement.util.PageSupport;

import java.util.List;
import java.util.Map;

/**
 * 关联信息查询
 * Created by lzm on 2017/3/24.
 */
public interface RelateInfoService {

    Map<String, Object> queryData(String querySql);

    List<Map<String, Object>> queryMoreData(PageSupport ps, String querySql);
}
