package com.workmanagement.service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.SysOrgDao;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysOrg;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysOrgServiceImpl implements SysOrgService {

    @Autowired
    private SysOrgDao institutionsDao;

    @Override
    public List<Integer> getAllIds() {
        return institutionsDao.getAllIds();
    }

    @Override
    public List<SysOrg> getOrgList(PageSupport ps, String page, Map<String, Object> param) throws Exception {
        if (page == null && ps == null) {//表示不分页
            return institutionsDao.querySysOrg(param);
        } else {
            PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
            return PageHelperSupport.queryCount(institutionsDao.querySysOrg(param), ps);
        }
    }

    @Override
    public List<SysOrg> querySubInstitutionById(Integer id) {
        return institutionsDao.querySubInstitutionById(id);
    }

    @Override
    public List<SysOrg> queryOrgAll(String orgName, Integer type) {
        return institutionsDao.queryOrgAll(orgName, type);
    }

    @Override
    public List<SysOrg> queryInstitutionByName(String orgName, Integer id) {
        return institutionsDao.queryInstitutionByName(orgName, id);
    }

    @Override
    public Integer queryInstitutionIdByName(String upname) {
        return institutionsDao.queryInstitutionIdByName(upname);
    }

    @Override
    public List<SysOrg> querySysOrgByAreaIds(String[] ids) {
        return institutionsDao.querySysOrgByAreaIds(ids);
    }

    @Override
    public Integer queryInstitutionTypeUpidById(Integer sys_org_type_id) {
        return institutionsDao.queryInstitutionTypeUpidById(sys_org_type_id);
    }

    @Override
    public List<SysOrg> queryInstitution(Map<String, Object> map) {
        return institutionsDao.queryInstitution(map);
    }

    @Override
    public SysOrg queryInstitutionsById(Integer id) {
        String key = RedisKeys.SYS_ORG + "queryInstitutionsById" + id;
        SysOrg sysOrg;
        if (RedisUtil.isEmpty(key)) {
            sysOrg = institutionsDao.queryInstitutionsById(id);
            RedisUtil.setData(key, sysOrg);
        } else {
            sysOrg = RedisUtil.getObjData(key, SysOrg.class);
        }
        return sysOrg;
    }

    @Override
    public void saveInstitutions(SysOrg i) {
        if (i.getSys_org_id() != null) {
            institutionsDao.updateInstitutions(i);
        } else {
            institutionsDao.insertInstitutions(i);
        }
        clear();
    }

    @Override
    public int delInstitutionsById(Integer id) {
        clear();
        return institutionsDao.delInstitutionsById(id);
    }

    @Override
    public List<SysOrg> queryInstitutionsByCodeAndName(String code, String name) {
        return institutionsDao.queryInstitutionsByCodeAndName(code, name);
    }

    @Override
    public SysOrg queryParentInstitutionsById(Integer parent) {
        return institutionsDao.queryParentInstitutionsById(parent);
    }

    @Override
    public void updateSysOrg(SysOrg so) {
        institutionsDao.updateInstitutions(so);
        clear();
    }

    @Override
    public List<SysOrg> querySysOrgByTypeId(int tid) {
        return institutionsDao.querySysOrgByTypeId(tid);
    }

    @Override
    public List<SysOrg> querySysOrgByAddressAreaId(Integer sys_area_id) {
        return institutionsDao.querySysOrgByAddressAreaId(sys_area_id);
    }


    /**
     * 获得当前机构及其下属机构
     */
    public String getNowOrgNames() {
        // 登录用户session
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        SysOrg so = queryInstitutionsById(userDetails.getSys_org_id());
        StringBuilder sb = new StringBuilder();
        if (so != null) {
            // 查询管辖地区及子地区的orgName
            getOrgNames(so, sb);
        }
        String orgNames = sb.toString();
        if (orgNames.substring(orgNames.length() - 1, orgNames.length()).equals(",")) {
            orgNames = orgNames.substring(0, orgNames.length() - 1);
        }
        return orgNames;
    }

    /**
     * 获取指定机构及子机构的ID
     *
     * @param
     */
    private void getOrgNames(SysOrg so, StringBuilder sb) {
        if (CollectionUtils.isEmpty(so.getSubSysOrg())) {
            sb.append("'").append(so.getSys_org_name()).append("'");
        } else {
            for (SysOrg sysOrg : so.getSubSysOrg()) {
                getOrgNames(sysOrg, sb);
            }
            sb.append("'").append(so.getSys_org_name()).append("'");
        }
        sb.append(",");
    }

    @Override
    public List<SysOrg> queryAll() {
        return institutionsDao.queryAll();
    }

    /**
     * 清空机构缓存
     */
    private void clear() {
        RedisUtil.delBatchData("*");
    }

    @Override
    public SysOrg queryInstitutionsByIdWithNoStatus(int parseInt) {
        return institutionsDao.queryInstitutionsByIdWithNoStatus(parseInt);
    }

    @Override
    public SysOrg querySysorgByFinancialCode(String financialCode) {
        return institutionsDao.querySysorgByFinancialCode(financialCode);
    }

    @Override
    public List<String> querySysorgInancialCode() {
        return institutionsDao.querySysorgInancialCode();
    }

    @Override
    public List<SysOrg> querySysOrgByOrgIds(String[] orgids) {
        return institutionsDao.querySysOrgByOrgIds(orgids);
    }

    /**
     * 根据多个地区ID查询机构的Ids
     *
     * @param sysAreaIs
     * @return
     * @throws Exception
     */
    @Override
    public List<Integer> getSysOrgIdsByAreaIds(List<Integer> sysAreaIs) throws Exception {
        return institutionsDao.getSysOrgIdsByOrgIds(sysAreaIs);
    }

    @Override
    public List<SysOrg> queryInstitutionmohu(String sysName) {
        return institutionsDao.queryInstitutionmohu(sysName);
    }

    /**
     * 精确查询，通过名字
     *
     * @param orgName
     * @return
     * @throws Exception
     */
    @Override
    public SysOrg getSysOrgByName(String orgName) throws Exception {
        return institutionsDao.getSysOrgByName(orgName);
    }

    @Override
    public SysOrg getByIdNotHaveSub(Integer sysOrgId) throws Exception {
        return institutionsDao.getByIdNotHaveSub(sysOrgId);
    }

    @Override
    public List<SysOrg> queryOrgByAreaIdHaveSub(Integer aid) {
        return institutionsDao.queryOrgByAreaIdHaveSub(aid);
    }

    @Override
    public List<SysOrg> queryOrgOrSubOrgById(Map<String, Object> map) {
        return institutionsDao.queryOrgOrSubOrgById(map);
    }

    @Override
    public List<Integer> queryInstitutionId(Map<String, Object> param) {
        return institutionsDao.queryInstitutionId(param);
    }

    @Override
    public List<SysOrg> querySysOrg(Map<String, Object> param) {
        return institutionsDao.querySysOrg(param);
    }

    @Override
    public List<SysOrg> selectSysOrg(Map<String, Object> param) {
        return institutionsDao.selectSysOrg(param);
    }

    @Override
    public List<SysOrg> selectOneSysOrgByAreaAndType(Map<String, Object> param) {
        return institutionsDao.selectOneSysOrgByAreaAndType(param);
    }

    @Override
    public SysOrg getOrgHaveTwoSub(Integer orgId) {
        SysOrg orgHaveTwoSub = institutionsDao.getOrgHaveOneSub(orgId);
        List<SysOrg> subSysOrg = orgHaveTwoSub.getSubSysOrg();
        if (CollectionUtils.isNotEmpty(subSysOrg)) {
            for (SysOrg org : subSysOrg) {
                SysOrg orgHaveTwoSub1 = institutionsDao.getOrgHaveOneSub(org.getSys_org_id());
                List<SysOrg> subSysOrg1 = orgHaveTwoSub1.getSubSysOrg();
                if (CollectionUtils.isNotEmpty(subSysOrg1)) {
                    org.setSubSysOrg(subSysOrg1);
                }
            }
        }
        return orgHaveTwoSub;
    }
}
