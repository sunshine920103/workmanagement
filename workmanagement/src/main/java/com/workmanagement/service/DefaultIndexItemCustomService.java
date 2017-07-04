package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.DefaultIndexItemCustom;
import com.workmanagement.model.Dic;
import com.workmanagement.util.PageSupport;
@Service
public interface DefaultIndexItemCustomService {

	/**
	 * 查询列表
	 * @param param
	 * @param ps
	 * @return
	 */
	List<DefaultIndexItemCustom> queryDefaultIndexItemCustoms(Map<String, Object> param, PageSupport ps);
}
