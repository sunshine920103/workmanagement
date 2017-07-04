package com.workmanagement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.ReportIndexError;

public interface ReportIndexErrorDao {
	public void insert(ReportIndexError reportIndexError);
	public void update(ReportIndexError reportIndexError);
	void deleteByReportIndexId(@Param("reportIndexId")Integer reportIndexId);
}
