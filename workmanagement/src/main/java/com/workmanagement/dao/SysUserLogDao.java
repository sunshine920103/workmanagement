package com.workmanagement.dao;

import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysUserLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 操作用户行为审计的dao
 * Created by lzm on 2017/3/13.
 */
public interface SysUserLogDao {

    /**
     * 添加一条数据
     *
     * @param sysUserLog
     * @return
     * @throws Exception
     */
    Integer insertOneLog(SysUserLog sysUserLog);

    /**
     * 根据条件查询数据
     *
     * @param map
     * @return
     * @throws Exception
     */
    List<SysUserLog> selectBySome(Map<String, Object> map);

    /**
     * 第一次进页面获取到所有数据
     *
     * @return
     * @throws Exception
     */
    List<SysUserLog> selectAll(@Param("areaId") Integer areaId, @Param("SysOrgList") List<SysOrg> SysOrgList);

    /**
     * 根据主键查询一条数据
     *
     * @return
     * @throws Exception
     */
    SysUserLog selectOne(Integer sysUserLogId);

    /**
     * 获取用户才能操作的菜单
     *
     * @return
     * @throws Exception
     */
    List<SysMenu> getMenus();

    /**
     * 管理员进来看机构
     *
     * @param sysOrgId
     * @return
     * @throws Exception
     */
    List<SysUserLog> getByOrgId(Integer sysOrgId);

    /**
     * 其他人进来看自己
     *
     * @param userName
     * @return
     * @throws Exception
     */
    List<SysUserLog> getByUserName(@Param("sysOrgId") Integer sysOrgId, @Param("userName") String userName);

    /**
     * 管理员或其他人进来看机构的查询次数
     *
     * @param logTime
     * @param orgId
     * @return
     */
    Integer getCountOfThisOrgQueryNum(@Param("logTime") String logTime,
                                      @Param("orgId") Integer orgId, @Param("userName") String userName);

    /**
     * 管理员或其他人进来看机构的查询结果
     *
     * @param logTime
     * @param orgId
     * @return
     */
    List<SysUserLog> getThisOrgQueryData(@Param("logTime") String logTime,
                                         @Param("orgId") Integer orgId, @Param("userName") String userName);
}
