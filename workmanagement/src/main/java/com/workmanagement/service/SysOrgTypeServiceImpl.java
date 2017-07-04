package com.workmanagement.service;

import com.github.pagehelper.PageHelper;
import com.google.gson.reflect.TypeToken;
import com.workmanagement.dao.SysOrgTypeDao;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOrgType;
import com.workmanagement.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysOrgTypeServiceImpl implements SysOrgTypeService {

    @Autowired
    private SysOrgTypeDao sysOrgTypeDao;
    @Autowired
    private SysAreaService sysAreaService;
    @Resource(name = "subDataGet")
    private SubDataGet subDataGet;

    @Override
    public List<SysOrgType> getTypesByOrgIds(List<SysOrg> orgs) {
        List<Integer> orgIds = sysOrgTypeDao.getTypesByOrgIds(orgs);
        List<Integer> copy = new ArrayList<>();
        copy.addAll(orgIds);
        for (Integer typeId : orgIds) {
            getUpTypes(copy, typeId);
        }
        return sysOrgTypeDao.getTpsByTpIds(copy);
    }

    @Override
    public Map<String, Object> getTypesByAreaIds(Integer areaId) {
        Map<String, Object> map = new HashMap<>();
        List<Integer> allSubAreaIds = sysAreaService.getAllSubAreaIds(areaId);
        List<Integer> quer = sysOrgTypeDao.getTypesByAreaIds(allSubAreaIds);
        List<Integer> copys = new ArrayList<>();
        copys.addAll(quer);
        for (Integer typeId : quer) {
            getUpTypes(copys, typeId);
        }
        List<SysOrgType> sysOrgTypes = sysOrgTypeDao.getUpsByTypeIds(copys);
        for (SysOrgType sysOrgType : sysOrgTypes) {
            setSubType(sysOrgType, copys);
        }
        map.put("needTypes", sysOrgTypes);
        map.put("thisIds", copys);
        return map;
    }

    private void setSubType(SysOrgType subType, List<Integer> orgTypes) {
        Integer sys_org_type_id = subType.getSys_org_type_id();
        List<SysOrgType> subTypeByIds = sysOrgTypeDao.getSubTypeByIds(orgTypes, sys_org_type_id);
        if (CollectionUtils.isNotEmpty(subTypeByIds)) {
            for (SysOrgType sysOrgType : subTypeByIds) {
                setSubType(sysOrgType, orgTypes);
            }
        }
    }

    /**
     * @param sub 的 upid 是一样的
     * @return
     */
    @SuppressWarnings("unchecked")
    private void getUpTypes(List<Integer> sub, Integer typeId) {
        Integer typeUpid = getTypeByIdNotSub(typeId).getSys_org_type_upid();
        if (typeUpid != null) {
            if (!sub.contains(typeUpid)) {
                sub.add(typeUpid);
                getUpTypes(sub, typeUpid);
            }
        }
    }

    @Override
    public SysOrgType getTypeByIdNotSub(Integer typeId) {
        return sysOrgTypeDao.getTypeByIdNotSub(typeId);
    }

    @Override
    public List<SysOrgType> getOrgTypeList(PageSupport ps, String page, String orgTypeName) throws Exception {
        if (page == null && ps == null) {//表示不分页
            return sysOrgTypeDao.queryAll();
        } else {
            if (orgTypeName == null) {
                PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
                return PageHelperSupport.queryCount(sysOrgTypeDao.queryAll(), ps);
            } else {
                PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
                return PageHelperSupport.queryCount(sysOrgTypeDao.queryInstitutionTypeByName(orgTypeName, null), ps);
            }

        }
    }

    @Override
    public List<SysOrgType> queryInstitutionTypeByName(String orgTypeName, Integer type) {
        return sysOrgTypeDao.queryInstitutionTypeByName(orgTypeName, type);
    }

    @Override
    public Integer queryInstitutionTypeIdByName(String typename) {
        return sysOrgTypeDao.queryInstitutionTypeIdByName(typename);

    }

    @Override
    public SysOrgType queryTypeAll(String code) {
        return sysOrgTypeDao.queryTypeAll(code);
    }

    @Override
    public String queryOrgTypeCodeByName(String name) {
        return sysOrgTypeDao.queryOrgTypeCodeByName(name);

    }

    @Override
    public SysOrgType queryInstitutionsTypeById(Integer id) {
        return sysOrgTypeDao.queryInstitutionsTypeById(id);
    }

    @Override
    public int delInstitutionsTypeById(Integer id) {
        clear();
        return sysOrgTypeDao.delInstitutionsTypeById(id);
    }

    @Override
    public List<SysOrgType> queryInstitutionType(Integer type, Integer used) {
        return sysOrgTypeDao.queryInstitutionType(type, used);
    }

    @Override
    public void saveInstitutionsType(SysOrgType i) {
        if (i.getSys_org_type_id() != null) {
            sysOrgTypeDao.updateInstitutionsType(i);
        } else {
            sysOrgTypeDao.saveInstitutionsType(i);
        }
        clear();
    }

    @Override
    public List<SysOrgType> queryUniqueInstiType(String name, String code) {
        return sysOrgTypeDao.queryUniqueInstiType(name, code);
    }

    @Override
    public List<SysOrgType> queryParentInstitutionsTypeById(Integer parent) {
        return sysOrgTypeDao.queryParentInstitutionsTypeById(parent);
    }

    @Override
    public void updateInstitutionsType(SysOrgType it) {
        sysOrgTypeDao.updateInstitutionsType(it);
        clear();
    }

    @Override
    public List<SysOrgType> queryAll() {
        return sysOrgTypeDao.queryAll();
    }

    @Override
    public List<SysOrgType> queryInstitutionTypeByTid(Integer id) {
        return sysOrgTypeDao.queryInstitutionTypeByTid(id);
    }

    /**
     * 清空缓存
     */
    private void clear() {
        RedisUtil.delBatchData("*");
    }

    @Override
    public SysOrgType queryInstitutionsTypeByCode(String orgTypeCode) {
        return sysOrgTypeDao.queryInstitutionsTypeByCode(orgTypeCode);
    }

    @Override
    public List<SysOrgType> queryOrgTypeAll() {
        return sysOrgTypeDao.queryOrgTypeAll();
    }

    @Override
    public List<SysOrgType> queryGovTypeAll() {
        return sysOrgTypeDao.queryGovTypeAll();
    }

    @Override
    public List<SysOrgType> queryTypeList(Map<String, Object> param) {
        return sysOrgTypeDao.queryTypeList(param);
    }

    @Override
    public List<SysOrgType> getTopOrgTypes(PageSupport ps) {
        if (ps == null) {
            List<SysOrgType> sysOrgTypes;
            String key = RedisKeys.SYS_ORG_TYPE_LIST + "getTopOrgTypes";
            if (RedisUtil.isEmpty(key)) {
                //如果没有缓存
                sysOrgTypes = sysOrgTypeDao.getTopOrgTypes();
                RedisUtil.setData(key, sysOrgTypes);
            } else {
                Type type = new TypeToken<List<SysOrgType>>() {
                }.getType();
                sysOrgTypes = RedisUtil.getListData(key, type);
            }
            return sysOrgTypes;
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(sysOrgTypeDao.getTopOrgTypes(), ps);
    }

    @Override
    public List<SysOrgType> getSubOrgTypesById(Integer typeId) {
        return sysOrgTypeDao.getSubOrgTypesById(typeId);
    }

    @Override
    public List<Integer> getAllUpOrgTypeIds(Integer typeId) {
        String key = RedisKeys.SYS_ORG_TYPE + "getAllUpOrgTypeIds";
        List<Integer> list;
        if (RedisUtil.isEmpty(key)) {
            SysOrgType sysOrgType = sysOrgTypeDao.getUpOrgTypesById(typeId);
            list = new ArrayList<>();
            subDataGet.getAllOrgTypeIds(sysOrgType, list);
            RedisUtil.setData(key, list);
        } else {
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            list = RedisUtil.getListData(key, type);
        }
        return list;
    }

    @Override
    public SysOrgType queryInstitutionsTypeByUpid(Integer sys_org_type_upid) {
        return sysOrgTypeDao.queryInstitutionsTypeByUpid(sys_org_type_upid);
    }
}
