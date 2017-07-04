package com.workmanagement.service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.SysUserLogDao;
import com.workmanagement.model.*;
import com.workmanagement.util.NetworkUtil;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户行为审计以及记录用户操作记录的service
 * <p>
 * Created by lzm on 2017/3/13.
 */
@Service(value = "sysUserLogService")
public class SysUserLogServiceImpl implements SysUserLogService {

    @Autowired
    private SysUserLogDao sysUserLogDao;
    @Autowired
    private DefaultIndexItemService defaultIndexItemService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 添加一条数据
     *
     * @param sysUserLog
     * @return
     * @throws Exception
     */
    public boolean insertOneLog(SysUserLog sysUserLog, HttpServletRequest request) {
        try {
            sysUserLog.setSysUserLogIp(NetworkUtil.getIpAddress(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(sysUserLog.getSysUserLogUserName())) {
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Integer sysOrgId = userDetails.getSysOrg().getSys_org_id();//机构ID
            String sysUserLogOrgName = userDetails.getSysOrg().getSys_org_name();//机构名称
            String sysUserLogUserName = userDetails.getUsername();//用户名
            String sysUserLogRoleName = userDetails.getSysRole().getSys_role_name();//角色名称
            sysUserLog.setSysOrgId(sysOrgId);
            sysUserLog.setSysUserLogOrgName(sysUserLogOrgName);
            sysUserLog.setSysUserLogUserName(sysUserLogUserName);
            sysUserLog.setSysUserLogRoleName(sysUserLogRoleName);
            if (sysUserLog.getSysUserLogTime() == null) {
                sysUserLog.setSysUserLogTime(new Date());
            }
        } else {
            SysUser userDetails = sysUserService.querySystemUserById(sysUserService.querySystemUserByCodeAndName(null, sysUserLog.getSysUserLogUserName()).getSys_user_id());
            Integer sysOrgId = userDetails.getSys_org_id();//机构ID
            String sysUserLogOrgName = userDetails.getSys_user_org_name();//机构名称
            String sysUserLogUserName = userDetails.getUsername();//用户名
            String sysUserLogRoleName = userDetails.getSys_user_role_name();//角色名称
            sysUserLog.setSysOrgId(sysOrgId);
            sysUserLog.setSysUserLogOrgName(sysUserLogOrgName);
            sysUserLog.setSysUserLogUserName(sysUserLogUserName);
            sysUserLog.setSysUserLogRoleName(sysUserLogRoleName);
            if (sysUserLog.getSysUserLogTime() == null) {
                sysUserLog.setSysUserLogTime(new Date());
            }
        }
        return sysUserLogDao.insertOneLog(sysUserLog) > 0;
    }

    /**
     * 根据条件查询数据
     *
     * @param ps
     * @param map @return
     * @throws Exception
     */
    @Override
    public List<SysUserLog> selectBySome(PageSupport ps, Map<String, Object> map) {
        if (ps == null) {
            return sysUserLogDao.selectBySome(map);
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        List<SysUserLog> list1 = PageHelperSupport.queryCount(sysUserLogDao.selectBySome(map), ps);
        this.reSetNames(list1);
        return list1;
    }

    /**
     * 第一次进页面
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<SysUserLog> selectAll(PageSupport ps, Integer areaId, List<SysOrg> SysOrgList) {
        if (ps == null) {
            return sysUserLogDao.selectAll(areaId, SysOrgList);
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        List<SysUserLog> list = PageHelperSupport.queryCount(sysUserLogDao.selectAll(areaId, SysOrgList), ps);
        this.reSetNames(list);
        return list;
    }

    /**
     * 根据主键查询一条数据
     *
     * @param sysUserLogId
     * @return
     * @throws Exception
     */
    @Override
    public SysUserLog selectOne(Integer sysUserLogId) {
        SysUserLog sysUserLog = sysUserLogDao.selectOne(sysUserLogId);
        this.reSetName(sysUserLog);
        return sysUserLog;
    }

    /**
     * 获取用户才能操作的菜单
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<SysMenu> getMenus() {
        return sysUserLogDao.getMenus();
    }

    private void reSetName(SysUserLog sysUserLog) {
        String code = sysUserLog.getSysUserLogEnterpriseCode();
        if (StringUtils.isNotBlank(code)) {
            DefaultIndexItem indexItem = defaultIndexItemService.queryById(Integer.valueOf(code));
            if (indexItem != null) {
                code = (StringUtils.isNotBlank(indexItem.getCodeCredit()) ? indexItem.getCodeCredit() + "," : "") +
                        StringUtils.trimToEmpty(indexItem.getCodeOrg());
                sysUserLog.setSysUserLogEnterpriseCode(code);
            }
        }
    }

    private void reSetNames(List<SysUserLog> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (SysUserLog s : list) {
                this.reSetName(s);
            }
        }
    }

    /**
     * 管理员进来看机构
     *
     * @param sysOrgId
     * @return
     * @throws Exception
     */
    @Override
    public List<SysUserLog> getByOrgId(PageSupport ps, Integer sysOrgId) {
        if (ps == null) {
            return sysUserLogDao.getByOrgId(sysOrgId);
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(sysUserLogDao.getByOrgId(sysOrgId), ps);
    }

    /**
     * 其他人进来看自己
     *
     * @param userName
     * @return
     * @throws Exception
     */
    @Override
    public List<SysUserLog> getByUserName(PageSupport ps, Integer sysOrgId, String userName) {
        if (ps == null) {
            return sysUserLogDao.getByUserName(sysOrgId, userName);
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(sysUserLogDao.getByUserName(sysOrgId, userName), ps);
    }

    /**
     * 管理员或其他人进来看机构的查询次数
     *
     * @param logTime
     * @param orgId
     * @param userName
     * @return
     */
    @Override
    public Integer getCountOfThisOrgQueryNum(String logTime, Integer orgId, String userName) {
        return sysUserLogDao.getCountOfThisOrgQueryNum(logTime, orgId, userName);
    }

    /**
     * 管理员或其他人进来看机构的查询结果
     *
     * @param logTime
     * @param orgId
     * @param userName
     * @return
     */
    @Override
    public List<SysUserLog> getThisOrgQueryData(PageSupport ps, String logTime, Integer orgId, String userName) {
        if (ps == null) {
            return sysUserLogDao.getThisOrgQueryData(logTime, orgId, userName);
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(sysUserLogDao.getThisOrgQueryData(logTime, orgId, userName), ps);
    }
}
