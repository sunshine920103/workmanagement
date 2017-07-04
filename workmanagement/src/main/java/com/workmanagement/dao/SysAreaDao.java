package com.workmanagement.dao;

import com.workmanagement.model.SysArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 地区管理Dao
 * 
 * @author wqs
 *
 */
public interface SysAreaDao {
	/**
	 * 根据id查询地区
	 * 
	 * @param id
	 * @return SysArea
	 */
	SysArea queryAreaById(@Param("id") Integer id);
	
	SysArea queryAreaById2(@Param("id") Integer id);
	SysArea queryAreaById3(@Param("id") Integer id);

	/**
	 * 模糊查询
	 * @return
	 */
	List<SysArea> search(@Param("condition") String condition);
	
	/**
	 * 模糊查询
	 * @return
	 */
	List<SysArea> search2(@Param("condition") String condition);
	
	/**
	 * 通过行政代码查询地区
	 * 
	 * @param name
	 * @param code
	 * @return
	 */
	SysArea queryAreaByCode(@Param("code") String code);
	
    /**
     * 通过名字查询地区
     *
     */
    List<SysArea> queryAreaByName(@Param("name") String name);
	
	/**
	 * 更新地区
	 * 
	 * @param a
	 */
	void updateArea(SysArea a);

	/**
	 * 保存地区
	 * 
	 * @param a
	 */
	void insertArea(SysArea a);

	/**
	 * 根据id删除一个地区
	 * 
	 * @param a
	 */
	void delAreaById(@Param("id") Integer id);

	/**
	 * 查询父地区
	 */
	SysArea queryParentAreasById(@Param("id") Integer parent);
	
	/**
	 * 通过名称和行政代码查询地区
	 * 
	 * @param name
	 * @param code
	 * @return
	 */
	List<SysArea> queryAreaByNameAndCode(@Param("name") String name, @Param("code") String code);

	/**
	 * 删除地区
	 * 
	 * @param ids
	 * @return
	 */
	int delAreaByIds(@Param("ids") String[] ids);

	/**
	 * 查询没有被使用的地区的数量，用于在删除地区的时候做判断
	 * @param ids
	 * @return
	 */
	int queryUnusedAreaTotalByIds(@Param("ids") String[] ids);

	/**
	 * 通过地区名称获取 地区区域码
	 * 
	 * @param address
	 * @return
	 */
	String queryCodeByName(@Param("address") String address);

	/**
	 * 查询所有地区
	 * 
	 * @return
	 */
	List<SysArea> queryAll();
	List<SysArea> queryAllAreaWithSub();
	List<SysArea> queryAll2();

	/**
	 * 查询所有地区的ids
	 *
	 * @return
	 */
	List<Integer> queryAllIds();
	
	/**
	 * 查询子地区
	 */
	List<SysArea> querySubAreasById(Integer id);

    /**
     * 根据地区 id 获取地区,不包含子地区
     *
     * @param sysAreaId
     * @return
     */
    SysArea getAreaNotSub(@Param("sysAreaId") Integer sysAreaId);
}
