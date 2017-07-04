package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.DicExchangeLast;

public interface DicExchangeLastDao {
	/**
	 * 根据id得到单个对象
	 * @param id
	 * @return
	 */
	public DicExchangeLast getDicExchangeLastById(Integer id);
	/**
	 * 根据币种查找
	 * @param dicExchangeName
	 * @return
	 */
	List<DicExchangeLast> getDicExchangeLastByDicExchangeName(@Param("dicExchangeName")String dicExchangeName,@Param("sysAreaId")Integer id);
	void delDicExchangeLast(@Param("dicExchangeLastCode")String dicExchangeLastCode);
	/**
	 * 查询该币种今天是否有上报过
	 * @param today
	 * @return
	 */
	Integer countTodayDicExchangeName(@Param("dicExchangeName")String dicExchangeName,@Param("today")String today);
	public void insertExchenge(DicExchangeLast dicExchangeLast);
	public void updateExchange(DicExchangeLast dicExchangeLast);
	public DicExchangeLast getDicExchangeLastByDicExchangeName1(
			String dicExchangeName);
	public List<DicExchangeLast> queryDicExchangeLasts(Map<String, Object> param,@Param("sysAreaId")Integer id);
	/**
	 * 修改历史值
	 * @param dicExchangeLast
	 */
	public void updateHistoryExchange(DicExchangeLast dicExchangeLast);
	
	public List<DicExchangeLast> getDicExchangeLastByHistory(@Param("dicExchangeName")String name, @Param("dicExchangeTime")String time,@Param("sysAreaId")Integer id);
	/**
	 * 查询所有汇率
	 * @return
	 */
	public List<DicExchangeLast> queryAllDicExchange(@Param("dicExchangeName")String name);
	/**
	 * 根据地区id和时间周期查询数据
	 * 
	 */
	public List<DicExchangeLast> selectAllByAreaAndTime(@Param("sysAreaId")Integer sysAreaId,
			@Param("startTime")String startTime, @Param("endTime")String endTime);
}
