package com.workmanagement.util;
import java.util.List;

/***
 * @作者:danny
 * @时间:2016-11-10
 * @功能:将有关分页信息封装到Page类中
 */
public class PageUtil <T>{
	/**
	 * 页总数
	 */
	public Integer pageCount;
	/**
	 * 显示记录数,null则查询所有
	 */
	public Integer pageSize=8;
	/**
	 * 记录总数
	 */
	public Integer totalCount;
	/**
	 * 当前页码,默认值
	 */
	public Integer pageNo=1;
	/**
	 * 每页显示的集合
	 */
	public List<T> lists;
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getPageSize() {
		//初始化值
		if (this.pageSize==0) {
			this.pageSize=1;
		}
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		//初始化值
		if (pageSize==0) {
			this.pageSize=1;
		}else{
			this.pageSize = pageSize;
		}
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
		//根据记录总数得到页数
		if(this.totalCount%this.pageSize==0){
			this.pageCount=this.totalCount/this.pageSize;
		}else{
			this.pageCount=this.totalCount/this.pageSize+1;
		}
	}
	public Integer getPageNo() {
		//初始化值
		if (this.pageNo==null||this.pageNo==0) {
			this.pageNo=1;
		}
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		//是否越过最小页或最大页
		if (pageNo==null||pageNo==0) {
			this.pageNo=1;
		}else if(pageNo>=this.pageCount){
			this.pageNo=this.pageCount;
		}
		else{
			this.pageNo=pageNo;
		}
	}
	public List<T> getLists() {
		return lists;
	}
	public void setLists(List<T> lists) {
		this.lists = lists;
	}
}
