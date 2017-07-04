package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.DefaultIndexItemCustom;
import com.workmanagement.util.PageSupport;

public interface DefaultIndexItemCustomDao {
	List<DefaultIndexItemCustom> queryDefaultIndexItemCustoms(Map<String, Object> param);
}
