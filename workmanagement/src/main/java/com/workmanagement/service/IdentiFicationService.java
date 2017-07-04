package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import com.workmanagement.model.IdentiFication;
import com.workmanagement.util.PageSupport;

public interface IdentiFicationService {

	List<IdentiFication> queryIdentiFicationByAll(Map<String, Object> map,PageSupport ps);

	void updateIdentiFication(IdentiFication iden);
	void insertIdentiFication(IdentiFication iden);

	void deleteIdentiFication(Integer id);
}
