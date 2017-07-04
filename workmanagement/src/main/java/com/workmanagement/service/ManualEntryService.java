package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpRequest;

import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.util.PageSupport;

/**
 * 手工修改Service接口
 * @author tianhao
 *
 */
public interface ManualEntryService {
	
	/**
	 * 查询指标大类
	 * @return
	 */
	List<IndexTb> queryIndexTbAll(Map<String, Object> map);
	
	/**
	 * 根据传入的信用码/机构码得到对象
	 * @throws Exception 
	 */
	DefaultIndexItem getDefaultIndexItemByCode(String defaultIndexItemByCode,Integer areaId) throws Exception;
	
	/**
	 * 通过id查询指标大类
	 * @param id
	 * @return
	 */
	IndexTb queryIndexTbById(Integer id);
	
	/**
	 * 查询临时表数据
	 */
	List<Map<String,Object>> temporaryTableList(PageSupport ps,Map<String,Object> map);
	
	/**
	 * 查询列表页面
	 * @param param
	 * @return
	 */
	List<SysUserLog> querySysUserLogList(Map<String, Object> param,PageSupport ps);
	
	/**
	 * 通过ID查询
	 * @param id
	 * @return
	 */
	SysUserLog getSysUserLogById(Integer id);
	
	/**
	 * 通过ID查询
	 * @param id
	 * @return
	 */
	DefaultIndexItem getDefaultIndexItemById(Integer id);
	
	/**
	 * 通過code得到对象
	 * @param code
	 * @return
	 */
	IndexItemTb getIndexItemTbByCode(String code);
	
	/**
	 * 通過name获取指标大类
	 * @param name
	 * @return
	 */
	IndexTb queryIndexTbByIndexName(String name);
	
	/**
	 * 更改上报数据状态
	 * @param sql
	 */
	void updataStatus(String sql);
	
	/**
	 * 查询机构及下级机构
	 * @param map
	 * @return
	 */
	List<SysOrg> querySysOrgAll(Map<String, Object> map);

	/**
	 * 修改动态表数据
	 * @param map
	 * @throws Exception 
	 */
	void updateIndexTbSql(String[] indexItemCode,String[] indexItemValue,Integer majorId,IndexTb indexTb,String path,Integer defaultId,String codeCredit,String codeOrg,HttpServletRequest request) throws Exception;
	
	/**
	 * 查询手工修改历史
	 * @return
	 */
	List<SysUserLog> querySysUserLog(Integer indexId,PageSupport ps);
	
	/**
	 * 查询数据字典详情
	 * @param dicContent
	 * @return
	 */
	DicContent queryDicContentByIdAndCode(DicContent dicContent);
	
	/**
	 * 查询是否有重复
	 * @param map
	 * @return
	 */
	Integer queryDefaultIndexItemCountByCodeCredit(Map<String, Object> map);
	
	/**
	 * 查询是否有重复
	 * @param map
	 * @return
	 */
	Integer queryDefaultIndexItemCountByCodeOrg(Map<String, Object> map);
}
