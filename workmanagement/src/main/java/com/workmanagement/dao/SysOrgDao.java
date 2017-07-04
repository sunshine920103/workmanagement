package com.workmanagement.dao;

/**
 * 机构管理Dao
 *
 * @author renyang
 */

import com.workmanagement.model.SysOrg;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysOrgDao {
    /**
     * 查詢一級机构列表
     *
     * @param map
     * @return
     */
    List<SysOrg> queryInstitution(@Param("param") Map<String, Object> map);

    List<Integer> getAllIds();

    SysOrg queryInstitutionsById(@Param("id") Integer id);

    void updateInstitutions(SysOrg i);

    void insertInstitutions(SysOrg i);

    int delInstitutionsById(@Param("id") Integer id);

    List<SysOrg> queryInstitutionsByCodeAndName(@Param("code") String code, @Param("name") String name);

    SysOrg queryParentInstitutionsById(@Param("id") Integer parent);

    List<SysOrg> querySysOrgByTypeId(@Param("tid") int tid);

    List<SysOrg> querySysOrgByAddressAreaId(@Param("aid") Integer sys_area_id);

    List<SysOrg> queryAll();

    SysOrg queryInstitutionsByIdWithNoStatus(@Param("parseInt") int parseInt);

    SysOrg querySysorgByFinancialCode(@Param("financialCode") String financialCode);

    List<String> querySysorgInancialCode();

    Integer queryInstitutionTypeUpidById(@Param("sys_org_type_id") Integer sys_org_type_id);

    List<SysOrg> querySysOrgByAreaIds(@Param("ids") String[] ids);

    Integer queryInstitutionIdByName(@Param("upname") String upname);

    List<SysOrg> queryInstitutionByName(@Param("orgName") String orgName, @Param("id") Integer id);

    List<SysOrg> queryOrgAll(@Param("orgName") String orgName, @Param("type") Integer type);

    List<SysOrg> querySubInstitutionById(Integer id);

    List<SysOrg> querySysOrgByOrgIds(@Param("orgids") String[] orgids);

    /**
     * 根据多个地区ID查询机构的Ids
     *
     * @param sysAreaIs
     * @return
     * @throws Exception
     */
    List<Integer> getSysOrgIdsByOrgIds(List<Integer> sysAreaIs) throws Exception;

    List<SysOrg> queryInstitutionmohu(@Param("orgName") String name);

    /**
     * 精确查询，通过名字
     *
     * @param orgName
     * @return
     * @throws Exception
     */
    SysOrg getSysOrgByName(@Param("orgName") String orgName) throws Exception;

    SysOrg getByIdNotHaveSub(@Param("sysOrgId") Integer sysOrgId);

    List<SysOrg> queryOrgByAreaIdHaveSub(@Param("aid") Integer aid);

    List<SysOrg> queryOrgOrSubOrgById(Map<String, Object> map);

    List<Integer> queryInstitutionId(@Param("param") Map<String, Object> param);

    List<SysOrg> querySysOrg(@Param("param") Map<String, Object> param);

    List<SysOrg> selectSysOrg(Map<String, Object> param);

    List<SysOrg> selectOneSysOrgByAreaAndType(@Param("param") Map<String, Object> param);

    SysOrg getOrgHaveOneSub(Integer orgId);
}


