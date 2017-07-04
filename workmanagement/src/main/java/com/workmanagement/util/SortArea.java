  package com.workmanagement.util;

import java.util.Comparator;

import com.workmanagement.model.SysArea;

/** @author  wqs 
	* @date 创建时间：2017年3月12日 上午11:55:15 
	* @version 1.0 
	* @parameter  
	* 将装有地区的list集合按照code排序
	* @return  */
public class SortArea  implements Comparator<Object>{

	@Override
	public int compare(Object obj1, Object obj2) {
		SysArea a1 = (SysArea)obj1;
		SysArea a2 = (SysArea)obj2;
		int flag= a1.getSysAreaCode().compareTo(a2.getSysAreaCode());
		return flag;
	}

}
