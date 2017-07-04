package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.SysImportentEnterpriseGroup;

public interface SysImportentEnterpriseGroupDao {
	List<SysImportentEnterpriseGroup> querySieg(Map<String, Object> map);
	
	void insertGroup(SysImportentEnterpriseGroup s);
	
	void insertOneOfGroup(@Param("groupId") Integer groupId,@Param("defaultId")Integer defaultId);
	
	SysImportentEnterpriseGroup	qureyByName(@Param("name") String name);
}
