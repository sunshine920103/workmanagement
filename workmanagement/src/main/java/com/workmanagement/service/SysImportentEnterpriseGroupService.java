package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.SysImportentEnterpriseGroup;
import com.workmanagement.util.PageSupport;

public interface SysImportentEnterpriseGroupService {

	List<SysImportentEnterpriseGroup> querySieg(Map<String, Object> map,PageSupport ps);
	
	void insertGroup(SysImportentEnterpriseGroup s);
	
	void insertOneOfGroup(@Param("groupId") Integer groupId,@Param("defaultId")Integer defaultId);
	
	SysImportentEnterpriseGroup	qureyByName(@Param("name") String name);
}
