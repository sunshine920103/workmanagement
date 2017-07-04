package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import com.workmanagement.model.IdentiFication;

public interface IdentiFicationDao {
	List<IdentiFication> queryIdentiFicationByAll(Map<String, Object> map);
	void updateIdentiFication(IdentiFication iden);
	void insertIdentiFication(IdentiFication iden);
	void deleteIdentiFication(Integer id);
}
