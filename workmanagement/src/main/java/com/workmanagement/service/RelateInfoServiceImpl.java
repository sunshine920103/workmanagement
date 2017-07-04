package com.workmanagement.service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.RelateInfoDao;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 关联信息查询
 * Created by lzm on 2017/3/24.
 */
@Service("relateInfoService")
public class RelateInfoServiceImpl implements RelateInfoService {

    @Autowired
    private RelateInfoDao relateInfoDao;

    @Override
    public Map<String, Object> queryData(String querySql) {
        return relateInfoDao.queryData(querySql);
    }

    @Override
    public List<Map<String, Object>> queryMoreData(PageSupport ps, String querySql) {
        if (ps == null) {
            return relateInfoDao.queryMoreData(querySql);
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(relateInfoDao.queryMoreData(querySql), ps);
    }
}
