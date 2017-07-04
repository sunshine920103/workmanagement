package com.workmanagement.service;

import com.workmanagement.model.SysOrg;
import com.workmanagement.util.PageSupport;

import java.util.List;
import java.util.Map;

public interface SysOrgService {


    List<SysOrg> getOrgList(PageSupport ps, String page, Map<String, Object> param) throws Exception;

    /**
     * 通过多个地区id查询这些地区下是否有金融机构存在
     */
    List<SysOrg> querySysOrgByAreaIds(String[] ids);

    List<Integer> getAllIds();

    /**
     * 查询机构列表
     *
     * @param map
     * @return
     */
    List<SysOrg> queryInstitution(Map<String, Object> map);

    /**
     * 通过ID查询机构
     *
     * @param id
     * @return
     */
    SysOrg queryInstitutionsById(Integer id);

    /**
     * 保存机构
     *
     * @param i
     */
    void saveInstitutions(SysOrg i);

    /**
     * 删除机构
     *
     * @param id
     * @return
     */
    int delInstitutionsById(Integer id);

    /**
     * 通过名称和机构编码查询机构
     *
     * @param code
     * @param name
     * @return
     */
    List<SysOrg> queryInstitutionsByCodeAndName(String code, String name);

    /**
     * 查询父机构
     *
     * @param parent
     * @return
     */
    SysOrg queryParentInstitutionsById(Integer parent);

    /**
     * 更新机构
     *
     * @param so
     */
    void updateSysOrg(SysOrg so);

    /**
     * 通过机构类别查询机构
     *
     * @param tid
     * @return
     */
    List<SysOrg> querySysOrgByTypeId(int tid);

    /**
     * 通过所在地区ID查询机构
     *
     * @param sys_org_address_area_id
     * @return
     */
    List<SysOrg> querySysOrgByAddressAreaId(Integer sys_area_id);

    /**
     * 获得当前机构及其下属机构
     */
    public String getNowOrgNames();

    /**
     * 查询所有机构
     *
     * @return
     */
    List<SysOrg> queryAll();

    /**
     * 通过ID查询机构 没有删除状态
     *
     * @param id
     * @return
     */
    SysOrg queryInstitutionsByIdWithNoStatus(int parseInt);

    /**
     * 通过金融机构编码查询机构
     *
     * @param financialCode
     * @return
     */
    SysOrg querySysorgByFinancialCode(String financialCode);

    List<String> querySysorgInancialCode();

    //查询机构类别上级ID
    Integer queryInstitutionTypeUpidById(Integer sys_org_type_id);

    //通过机构名称查询机构id
    Integer queryInstitutionIdByName(String upname);

    //模糊查询 通过机构名称查询机构
    List<SysOrg> queryInstitutionByName(String orgName, Integer id);

    //查询全部机构 （需要上级机构）
    List<SysOrg> queryOrgAll(String orgName, Integer type);

    //根据机构id查询子机构
    List<SysOrg> querySubInstitutionById(Integer id);

    /**
     * 同过机构id数组查询机构
     */
    List<SysOrg> querySysOrgByOrgIds(String[] orgids);

    /**
     * 根据多个地区ID查询机构的Ids
     *
     * @param sysAreaIs
     * @return
     * @throws Exception
     */
    List<Integer> getSysOrgIdsByAreaIds(List<Integer> sysAreaIs) throws Exception;

    /**
     * 根据机构名字模糊查询数据
     *
     * @param sysName
     * @return
     */
    List<SysOrg> queryInstitutionmohu(String sysName);

    /**
     * 精确查询，通过名字
     *
     * @param orgName
     * @return
     * @throws Exception
     */
    SysOrg getSysOrgByName(String orgName) throws Exception;


    SysOrg getByIdNotHaveSub(Integer sysOrgId) throws Exception;

    List<SysOrg> queryOrgByAreaIdHaveSub(Integer aid);

    //通过id查询机构和下级机构
    List<SysOrg> queryOrgOrSubOrgById(Map<String, Object> map);

    List<Integer> queryInstitutionId(Map<String, Object> param);

    //查询机构 不带子机构
    List<SysOrg> querySysOrg(Map<String, Object> param);

    List<SysOrg> selectSysOrg(Map<String, Object> param);

    List<SysOrg> selectOneSysOrgByAreaAndType(Map<String, Object> param);

    SysOrg getOrgHaveTwoSub(Integer orgId);
}
