package com.workmanagement.service;

import com.workmanagement.model.SysMenu;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.util.PageSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户行为审计以及记录用户操作记录的service
 * <p>
 * Created by lzm on 2017/3/13.
 */
public interface SysUserLogService {

    //查询条件的map的key
    public static final String ORG_NAME = "sysUserLogOrgName";
    public static final String MENU_NAME = "sysUserLogMenuName";
    public static final String USER_NAME_OR_ENTERPRISE_CODE = "sysUserLogUserNameOrEnterpriseCode";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String OPERATE_TYPE = "sysUserLogOperateType";
    public static final String AREA_ID = "areaId";
    public static final String SYS_ORG_LIST = "SysOrgList";

    //存数据用的字符
    /**
     * 增加
     */
    public static final Integer INSERT = 1;
    /**
     * 删除
     */
    public static final Integer DELETE = 2;
    /**
     * 修改
     */
    public static final Integer UPDATE = 3;
    /**
     * 查询
     */
    public static final Integer SELECT = 4;
    /**
     * 导入
     */
    public static final Integer IMPORT = 5;
    /**
     * 导出
     */
    public static final Integer EXPORT = 6;
    /**
     * 打印
     */
    public static final Integer PRINT = 7;

    /**
     * 登录
     */
    public static final Integer LOGIN = 8;

    /**
     * 添加一条数据
     *
     * @param sysUserLog
     * @return
     * @throws Exception
     */
    boolean insertOneLog(SysUserLog sysUserLog, HttpServletRequest request);

    /**
     * 根据条件查询数据
     *
     * @param map
     * @return
     * @throws Exception
     */
    List<SysUserLog> selectBySome(PageSupport ps, Map<String, Object> map);

    /**
     * 第一次进页面获取到所有数据
     *
     * @return
     * @throws Exception
     */
    List<SysUserLog> selectAll(PageSupport ps, Integer areaId, List<SysOrg> SysOrgList);


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
    List<SysUserLog> getByOrgId(PageSupport ps, Integer sysOrgId);

    /**
     * 其他人进来看自己
     *
     * @param userName
     * @return
     * @throws Exception
     */
    List<SysUserLog> getByUserName(PageSupport ps, Integer sysOrgId, String userName);

    /**
     * 管理员或其他人进来看机构的查询次数
     *
     * @param logTime
     * @param orgId
     * @return
     */
    Integer getCountOfThisOrgQueryNum(String logTime, Integer orgId, String userName);

    /**
     * 管理员或其他人进来看机构的查询结果
     *
     * @param logTime
     * @param orgId
     * @return
     */
    List<SysUserLog> getThisOrgQueryData(PageSupport ps, String logTime, Integer orgId, String userName);
}
