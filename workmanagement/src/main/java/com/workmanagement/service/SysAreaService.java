package com.workmanagement.service;

import com.workmanagement.model.SysArea;
import com.workmanagement.util.PageSupport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 地区管理的Service接口
 *
 * @author Wqs
 */
public interface SysAreaService {

    /**
     * 通过ID查询地区
     *
     * @param id
     * @return
     */
    SysArea queryAreaById(Integer id);
    
    /**
     * 通过ID查询地区，只有一级下级地区
     *
     * @param id
     * @return
     */
    SysArea queryAreaById2(Integer id);
    
    /**
     * 通过ID查询地区,无上下级关系
     *
     * @param id
     * @return
     */
    SysArea queryAreaById3(Integer id);

    /**
     * 通过行政代码查询地区
     *
     */
    SysArea queryAreaByCode(@Param("code") String code);
    
    /**
     * 通过名字查询地区
     *
     */
    List<SysArea> queryAreaByName(String name);

    /**
     * 模糊查询
     *
     * @return
     */
    List<SysArea> search(@Param("condition") String condition, PageSupport ps);
    /**
     * 模糊查询
     *
     * @return
     */
    List<SysArea> search2(@Param("condition") String condition, PageSupport ps);

    /**
     * 保存地区
     *
     * @param a
     */
    void saveArea(SysArea a);

    /**
     * 通过名称和代码查询地区
     *
     * @param name
     * @param code
     * @return
     */
    List<SysArea> queryAreaByNameAndCode(String name, String code);

    /**
     * 查询父地区
     *
     * @param parent
     * @return
     */
    SysArea queryParentAreasById(Integer parent);

    /**
     * 查询子地区
     *
     * @param id
     * @return
     */
    List<SysArea> querySubAreasById(Integer id);

    /**
     * 删除地区
     *
     * @param id
     */
    int delAreaByIds(String[] ids);

    /**
     * 查询没有被使用的地区数量
     *
     * @param ids
     * @return
     */
    int queryUnusedAreaTotalByIds(String[] ids);

    /**
     * 通过地区名称获取 地区区域码
     *
     * @param address
     * @return
     */
    String queryCodeByName(String address);

    /**
     * 更新地区
     *
     * @param useArea
     */
    void updateSysArea(SysArea area);

    /**
     * 查询全部地区
     *
     * @return
     */
    List<SysArea> queryAll();
    List<SysArea> queryAllAreaWithSub();
    /**
     * 查询全部地区
     *
     * @return
     */
    List<SysArea> queryAll2();


    /**
     * 查询所有地区的ids
     *
     * @return
     */
    List<Integer> queryAllIds();

    /**
     * 通过登录用户的id查询所属地区
     */

    SysArea getUpOrThisSysArea(Integer areaId);

    List<Integer> getAllSubAreaIds(Integer areaId);

    List<Integer> getAllUpAreaIds(Integer areaId);

    SysArea getAreaNotSub(Integer sysAreaId);
}
