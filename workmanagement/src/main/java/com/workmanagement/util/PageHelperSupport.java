package com.workmanagement.util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * pagehelper的一个工具，只是简化了方法中的代码
 * <p>
 * Created by lzm on 2017/3/9.
 */
public abstract class PageHelperSupport {

    public static <T> List<T> queryCount(List<T> list, PageSupport ps) {
        try {
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            PageInfo<T> page = new PageInfo<>(list);
            ps.setTotalRecord(page.getTotal());
            return page.getList();
        } finally {
            PageHelper.clearPage();
        }
    }
}
