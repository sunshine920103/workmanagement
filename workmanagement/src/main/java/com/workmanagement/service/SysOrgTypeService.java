package com.workmanagement.service;

import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOrgType;
import com.workmanagement.util.PageSupport;

import java.util.List;
import java.util.Map;

public interface SysOrgTypeService {

    List<SysOrgType> getTypesByOrgIds(List<SysOrg> orgs);

    SysOrgType getTypeByIdNotSub(Integer typeId);

    Map<String, Object> getTypesByAreaIds(Integer areaId);

    List<SysOrgType> getOrgTypeList(PageSupport ps, String page, String orgTypeName) throws Exception;

    /**
     * 查询机构类别列表
     *
     * @return
     */
    List<SysOrgType> queryInstitutionType(Integer type, Integer used);

    /**
     * 保存机构类别
     *
     * @param i
     */
    void saveInstitutionsType(SysOrgType i);

    /**
     * 通过ID查询机构类别
     *
     * @param id
     * @return
     */
    SysOrgType queryInstitutionsTypeById(Integer id);

    /**
     * 通过ID删除机构类别
     *
     * @param id
     * @return
     */
    int delInstitutionsTypeById(Integer id);

    /**
     * 查询唯一的机构类别
     *
     * @param id
     * @param name
     * @param code
     * @return
     */
    List<SysOrgType> queryUniqueInstiType(String name, String code);

    /**
     * 查询父机构类别
     *
     * @param parent
     * @return
     */
    List<SysOrgType> queryParentInstitutionsTypeById(Integer parent);

    /**
     * 更新机构类别
     *
     * @param it
     */
    void updateInstitutionsType(SysOrgType it);

    /**
     * 查询所有机构类别
     *
     * @return
     */
    List<SysOrgType> queryAll();

    /**
     * 通过机构类别代码查询机构类别
     *
     * @param towOrgTypeCode
     * @return
     */
    SysOrgType queryInstitutionsTypeByCode(String orgTypeCode);

    /**
     * 通过机构类别名称查询机构代码
     *
     * @param
     * @return
     */
    String queryOrgTypeCodeByName(String name);

    SysOrgType queryTypeAll(String code);

    //通过机构类别名称查询机构类别id
    Integer queryInstitutionTypeIdByName(String typename);

    //模糊查询机构类别名称
    List<SysOrgType> queryInstitutionTypeByName(String orgTypeName, Integer type);

    /**
     * 通过机构类别id查询机构类别及子机构类别
     *
     * @param
     * @return
     */
    List<SysOrgType> queryInstitutionTypeByTid(Integer id);

    List<SysOrgType> queryOrgTypeAll();

    List<SysOrgType> queryGovTypeAll();

    List<SysOrgType> queryTypeList(Map<String, Object> param);

    List<SysOrgType> getTopOrgTypes(PageSupport ps);

    List<SysOrgType> getSubOrgTypesById(Integer typeId);

    List<Integer> getAllUpOrgTypeIds(Integer typeId);

    SysOrgType queryInstitutionsTypeByUpid(Integer sys_org_type_upid);
}
