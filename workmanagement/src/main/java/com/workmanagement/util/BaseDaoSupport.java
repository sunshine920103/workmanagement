package com.workmanagement.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;


public class BaseDaoSupport extends SqlSessionDaoSupport {

	public void save(String key, Object object) {
		this.getSqlSession().insert(key, object);
	}

	/**
	 * Execute an update statement. The number of rows affected will be returned.
	 * 
	 * @param statement Unique identifier matching the statement to execute.
	 * @param parameter A parameter object to pass to the statement.

	 * @return int The number of rows affected by the update.
	 */
	public int update(String statement, Object parameter) {
		return getSqlSession().update(statement, parameter);
	}

	public void delete(String key, Object object) {
		this.getSqlSession().delete(key, object);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Object object) {
		return (T) this.getSqlSession().selectOne(key, object);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) this.getSqlSession().selectOne(key);
	}

	public <T> List<T> getList(String key) {
		return this.getSqlSession().selectList(key);
	}

	public <T> List<T> getList(String key, Object object) {
		return this.getSqlSession().selectList(key, object);
	}
	
	/**
	 * 采用数据库分页，性能高于getListPageSupport(String key, Object object, PageSupport pageSupport)。<br />
	 * 在Controller中需要PageSupport.initPageSupport(HttpServletRequest request, int totalRecord)方法支持, <br />
	 * 参数totalRecord需要手动查询
	 * 
	 * SQL语句中增加参数limit和offset, 如:<br />
	 * SELECT *<br />
	 * FROM  table<br />
	 * LIMIT #{limit} OFFSET #{offset}
	 * 
	 * @param key
	 * @param countKey 统计数量的key
	 * @param param
	 * @param pageSupport
	 * @return
	 * @throws IllegalArgumentException
	 */
	public <T> List<T> getListPageSupportByManualOperation(String key, String countKey, Map<String, Object> param, PageSupport pageSupport) throws IllegalArgumentException{
		Integer totalRecord = 0;
		if (!CollectionUtils.isEmpty(param))
			totalRecord = this.get(countKey, param);
		else
			totalRecord = this.get(countKey);
		if (totalRecord == null || totalRecord.intValue() <= 0)
			return null;
		
		if(pageSupport!=null){
			pageSupport.setTotalRecord(totalRecord);
			if (param == null){
				param = new HashMap<String, Object>();
			}
			//MYSQL
			/*param.put("limit", pageSupport.getPageSize());
			param.put("offset", pageSupport.getPageOffset());*/
			//DB2
			param.put("limit", (pageSupport.getPageSize() * pageSupport.getPageOffset()));
			param.put("offset", ((pageSupport.getPageOffset() + 1) * pageSupport.getPageSize()));
		}
		
		SqlSession sqlSession = this.getSqlSession();
		
		return sqlSession.selectList(key, param);
	}
	
	public <T> List<T> getListPageSupportByManualOperation(String key, Map<String, Object> param, PageSupport pageSupport) throws IllegalArgumentException{
		return getListPageSupportByManualOperation(key, key + "_count", param, pageSupport);
	}
}
