package com.workmanagement.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.DicExchangeLastDao;
import com.workmanagement.model.DicExchangeLast;
import com.workmanagement.util.BaseDaoSupport;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

@Service
public class DicExchangeLastServiceImpl extends BaseDaoSupport implements DicExchangeLastService {

	@Autowired 
	private DicExchangeLastDao dicExchangeLastDao;
	@Override
	public List<DicExchangeLast> queryDicExchangeLasts(Map<String, Object> param, PageSupport ps,Integer id) {
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(dicExchangeLastDao.queryDicExchangeLasts(param,id),ps);
	}

	@Override
	public DicExchangeLast queryDicExchangeLastById(Integer id) {
		// TODO Auto-generated method stub
		return dicExchangeLastDao.getDicExchangeLastById(id);
	}
	/**
	 * 添加或修改汇率的同时，保存在汇率历史表中
	 */

	@Override
	public List<DicExchangeLast> getDicExchangeLastByDicExchangeName(PageSupport ps,String dicExchangeName,Integer id) {
		if(ps!=null){
			PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			return PageHelperSupport.queryCount(dicExchangeLastDao.getDicExchangeLastByDicExchangeName(dicExchangeName,id),ps);
		}else{
			return dicExchangeLastDao.getDicExchangeLastByDicExchangeName(dicExchangeName,id);
		}
	}

	@Override
	public void delDicExchangeLast(String dicExchangeLastCode) {
		// TODO Auto-generated method stub
		dicExchangeLastDao.delDicExchangeLast(dicExchangeLastCode);
	}

	@Override
	public Integer countTodayDicExchangeName(String dicExchangeName) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String format = sdf.format(new Date());
		return dicExchangeLastDao.countTodayDicExchangeName(dicExchangeName, format);
	}

	@Override
	public void insertExchenge(DicExchangeLast dicExchangeLast) {
		String name=dicExchangeLast.getDicExchangeName();
		String time=dicExchangeLast.getDicExchangeTime();
		String newTime=time.substring(0,10);
		Integer id=dicExchangeLast.getDicAreaId();
		List<DicExchangeLast>  dic=dicExchangeLastDao.getDicExchangeLastByHistory(name,newTime,id);
		// TODO Auto-generated method stub
		if (dic!=null && dic.size()>0) {
			for(DicExchangeLast dicList:dic){
				dicExchangeLast.setDicExchangeId(dicList.getDicExchangeId());
				dicExchangeLastDao.updateExchange(dicExchangeLast);
			}
		}else{
			dicExchangeLastDao.insertExchenge(dicExchangeLast);
		}
		
	}

	@Override
	public DicExchangeLast getDicExchangeLastById(Integer id) {
		// TODO Auto-generated method stub
		return dicExchangeLastDao.getDicExchangeLastById(id);
	}

	@Override
	public DicExchangeLast getDicExchangeLastByDicExchangeName1(
			String dicExchangeName) {
		// TODO Auto-generated method stub
		return dicExchangeLastDao.getDicExchangeLastByDicExchangeName1(dicExchangeName);
	}

	@Override
	public void updateHistoryExchange(DicExchangeLast dicExchangeLast) {
		// TODO Auto-generated method stub
		dicExchangeLastDao.updateHistoryExchange(dicExchangeLast);
		
	}

	@Override
	public List<DicExchangeLast> getDicExchangeLastByHistory(PageSupport ps,String name, String time,Integer AreaId) {
		if(ps==null){
			return dicExchangeLastDao.getDicExchangeLastByHistory(name,time,AreaId);
		}
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		 return PageHelperSupport.queryCount(dicExchangeLastDao.getDicExchangeLastByHistory(name,time,AreaId),ps);
	}
	/**
	 * 查询所有汇率
	 */
	@Override
	public List<DicExchangeLast> queryAllDicExchange(PageSupport ps,String name) {
		 if(ps==null){
			 return dicExchangeLastDao.queryAllDicExchange(name);
		 }else{
			 PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			 return PageHelperSupport.queryCount(dicExchangeLastDao.queryAllDicExchange(name),ps);
		 }
	}
	/**
	 * 根据地区，时间周期查询数据
	 */
	@Override
	public List<DicExchangeLast> selectAllByAreaAndTime(Integer sysAreaId,
			String startTime, String endTime, PageSupport ps) {
		 if(ps==null){
			 return dicExchangeLastDao.selectAllByAreaAndTime(sysAreaId,startTime,endTime);
		 }else{
			 PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
			 return PageHelperSupport.queryCount(dicExchangeLastDao.selectAllByAreaAndTime(sysAreaId,startTime,endTime),ps);
		 }
	}
	
}
