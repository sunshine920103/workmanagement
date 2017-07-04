package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.ReportedDeleteDao;
import com.workmanagement.model.IndexTb;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

/** 
*
* @author  作者renyang
* @date 
* 
*/
@Service
public class ReportedDeleteServiceImp  implements ReportedDeleteService {
	
	@Autowired
	private ReportedDeleteDao reportedDeleteDao;
	@Override	
	public List<IndexTb> queryIndexTb(Map<String, Object> map){
		return reportedDeleteDao.queryIndexTb(map);
		
	}
	@Override
	public List<Map<String, Object>> queryResult(Map<String, Object> sqlMap){
		return reportedDeleteDao.queryResult(sqlMap);
	}
	@Override
	public List<Map<String, Object>> getPage(PageSupport ps, String page, Map<String, Object> sqlMap){
		if (page == null && ps == null){//表示不分页
            return reportedDeleteDao.queryResult(sqlMap);
        }else {
            PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
            return PageHelperSupport.queryCount(reportedDeleteDao.queryResult(sqlMap),ps);
        }
	}
	@Override
	public Integer deleteData(Map<String, Object> delMap){
		return reportedDeleteDao.deleteData(delMap);
	}
}
