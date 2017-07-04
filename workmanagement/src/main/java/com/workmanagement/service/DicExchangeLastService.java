package com.workmanagement.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.DicExchangeLast;
import com.workmanagement.util.PageSupport;

public interface DicExchangeLastService {

	/**
	 * 通过ID查询
	 * @param id
	 * @return
	 */
	DicExchangeLast queryDicExchangeLastById(Integer id);
	/**
	 * 查询列表
	 * @param param
	 * @param ps
	 * @param integer 
	 * @return
	 */
	List<DicExchangeLast> queryDicExchangeLasts(Map<String, Object> param, PageSupport ps, Integer integer);

	/**
	 * 更新或保存
	 * 
	 * 添加或修改汇率的同时，保存在汇率历史表中
	 *
	 */
//	void insertExchenge(DicExchangeLast dicExchangeLast);
	/**
	 * 根据币种查找
	 * @param dicExchangeName
	 * @return
	 */
	List<DicExchangeLast> getDicExchangeLastByDicExchangeName(PageSupport ps,String dicExchangeName,Integer id);
	void delDicExchangeLast(String dicExchangeLastCode);
	/**
	 * 查询该币种今天是否有上报过
	 * @param today
	 * @return
	 */
	Integer countTodayDicExchangeName(@Param("dicExchangeName")String dicExchangeName);
	void insertExchenge(DicExchangeLast dicExchangeLast);
	DicExchangeLast getDicExchangeLastById(Integer id);
	DicExchangeLast getDicExchangeLastByDicExchangeName1(String dicExchangeName);
	/**
	 * 修改历史数据
	 * @param dicExchangeLast
	 */
	void updateHistoryExchange(DicExchangeLast dicExchangeLast);
	List<DicExchangeLast> getDicExchangeLastByHistory(PageSupport ps,String name, String time, Integer integer);
	/**
	 * 查询所有汇率
	 * @return
	 */
	List<DicExchangeLast> queryAllDicExchange(PageSupport ps,String name);
	/**
	 * 根据地区，时间周期查询数据
	 * @param sysAreaId
	 * @param startTime
	 * @param endTime
	 * @param ps
	 * @return
	 */
	List<DicExchangeLast> selectAllByAreaAndTime(Integer sysAreaId,
			String startTime, String endTime, PageSupport ps);
}
