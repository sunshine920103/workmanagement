package com.workmanagement.service;

import com.github.pagehelper.PageHelper;
import com.google.gson.reflect.TypeToken;
import com.workmanagement.dao.SysAreaDao;
import com.workmanagement.model.SysArea;
import com.workmanagement.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysAreaServiceImpl extends BaseDaoSupport implements SysAreaService {

    @Autowired
    private SysAreaDao sysAreaDao;

    @Resource(name = "subDataGet")
    private SubDataGet subDataGet;


    @Override
    public SysArea queryAreaById(Integer id) {
        SysArea sysArea;
        String key = RedisKeys.SYS_AREA + "queryAreaById" + id;
        if (RedisUtil.isEmpty(key)) {
            sysArea = sysAreaDao.queryAreaById(id);
            RedisUtil.setData(key, sysArea);
        } else {
            sysArea = RedisUtil.getObjData(key, SysArea.class);
        }
        return sysArea;
    }
    
    @Override
    public SysArea queryAreaById2(Integer id) {
        SysArea sysArea = sysAreaDao.queryAreaById2(id);
        return sysArea;
    }
    
    @Override
    public SysArea queryAreaById3(Integer id) {
        SysArea sysArea = sysAreaDao.queryAreaById3(id);
        return sysArea;
    }


    @Override
    public void saveArea(SysArea a) {
        if (a.getSysAreaId() != null) {
            sysAreaDao.updateArea(a);
        } else {
            sysAreaDao.insertArea(a);
        }
        clearRedis();
    }

    @Override
    public List<SysArea> queryAreaByNameAndCode(String name, String code) {
        return sysAreaDao.queryAreaByNameAndCode(name, code);
    }


    @Override
    public int delAreaByIds(String[] ids) {
        int i = sysAreaDao.delAreaByIds(ids);
        clearRedis();
        return i;
    }

    @Override
    public int queryUnusedAreaTotalByIds(String[] ids) {
        return sysAreaDao.queryUnusedAreaTotalByIds(ids);
    }

    @Override
    public String queryCodeByName(String address) {
        return sysAreaDao.queryCodeByName(address);
    }

    @Override
    public void updateSysArea(SysArea area) {
        sysAreaDao.updateArea(area);
        clearRedis();
    }

    @Override
    public List<SysArea> queryAll() {
        return sysAreaDao.queryAll();
    }
    @Override
    public List<SysArea> queryAllAreaWithSub() {
    	return sysAreaDao.queryAllAreaWithSub();
    }
    
    @Override
    public List<SysArea> queryAll2() {
        return sysAreaDao.queryAll2();
    }

    @Override
    public SysArea queryParentAreasById(Integer parent) {
        return sysAreaDao.queryParentAreasById(parent);
    }

    @Override
    public List<SysArea> querySubAreasById(Integer id) {
        return sysAreaDao.querySubAreasById(id);
    }

    @Override
    public SysArea queryAreaByCode(String code) {
        return sysAreaDao.queryAreaByCode(code);
    }
    
    @Override
    public List<SysArea> queryAreaByName(String name) {
        return sysAreaDao.queryAreaByName(name);
    }

    /**
     * 清空缓存
     */
    private void clearRedis() {
        RedisUtil.delBatchData("*");
    }


    /**
     * 查询所有地区的ids
     *
     * @return
     */
    public List<Integer> queryAllIds() {
        return sysAreaDao.queryAllIds();
    }


    @Override
    public List<SysArea> search(String condition, PageSupport ps) {
        if (ps != null) {
            PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
            return PageHelperSupport.queryCount(sysAreaDao.search(condition), ps);
        } else {
            return sysAreaDao.search(condition);
        }
    }

    @Override
    public List<SysArea> search2(String condition, PageSupport ps) {
        if (ps != null) {
            PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
            return PageHelperSupport.queryCount(sysAreaDao.search2(condition), ps);
        } else {
            return sysAreaDao.search2(condition);
        }
    }
    /**
     * 获取到上级地区到市级别,如果是省级直接返回
     *
     * @param areaId
     * @return
     */
    @Override
    public SysArea getUpOrThisSysArea(Integer areaId) {
        SysArea sy = queryParentAreasById(areaId);
        String type = sy.getSysAreaType();
        while (!StringUtils.equals(type, "0") && !StringUtils.equals(type, "1")) {
            sy = sy.getSubArea().get(0);
            type = sy.getSysAreaType();
        }
        return sy;
    }

    public List<Integer> getAllSubAreaIds(Integer areaId) {
        List<Integer> areaIds = null;
        String key = RedisKeys.SYS_AREA + "mustGetSubIds" + areaId;
        if (RedisUtil.isEmpty(key)) {
            SysArea sy = queryAreaById(areaId);
            areaIds = new ArrayList<>();
            subDataGet.getAllAreaIds(sy, areaIds);
            RedisUtil.setData(key, areaIds);
        } else {
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            areaIds = RedisUtil.getListData(key, type);
        }
        return areaIds;
    }

    @Override
    public List<Integer> getAllUpAreaIds(Integer areaId) {
        List<Integer> areaIds = null;
        String key = RedisKeys.SYS_AREA + "mustGetUpIds" + areaId;
        if (RedisUtil.isEmpty(key)) {
            SysArea sy = queryParentAreasById(areaId);
            areaIds = new ArrayList<>();
            subDataGet.getAllAreaIds(sy, areaIds);
            RedisUtil.setData(key, areaIds);
        } else {
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            areaIds = RedisUtil.getListData(key, type);
        }
        return areaIds;
    }

    @Override
    public SysArea getAreaNotSub(Integer sysAreaId) {
        return sysAreaDao.getAreaNotSub(sysAreaId);
    }
}
