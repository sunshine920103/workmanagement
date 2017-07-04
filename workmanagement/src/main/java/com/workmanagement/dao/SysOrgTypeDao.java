package com.workmanagement.dao;

import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOrgType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysOrgTypeDao {

    List<Integer> getTypesByOrgIds(@Param("orgs") List<SysOrg> orgs);

    List<SysOrgType> getUpsByTypeIds(@Param("typeIds") List<Integer> typeIds);

    List<SysOrgType> getTpsByTpIds(@Param("typeIds") List<Integer> typeIds);

    List<SysOrgType> getSubTypeByIds(@Param("typeIds") List<Integer> typeIds, @Param("upId") Integer upId);

    SysOrgType getTypeByIdNotSub(@Param("typeId") Integer typeId);

    List<Integer> getTypesByAreaIds(@Param("areaIds") List<Integer> areaIds);

    List<SysOrgType> queryInstitutionType(@Param("type") Integer type, @Param("used") Integer used);

    SysOrgType queryInstitutionsTypeById(@Param("id") Integer id);

    int delInstitutionsTypeById(@Param("id") Integer id);

    void saveInstitutionsType(SysOrgType i);

    void updateInstitutionsType(SysOrgType i);

    List<SysOrgType> queryUniqueInstiType(@Param("name") String name, @Param("code") String code);

    List<SysOrgType> queryParentInstitutionsTypeById(@Param("id") Integer parent);

    List<SysOrgType> queryAll();

    SysOrgType queryInstitutionsTypeByCode(@Param("orgTypeCode") String orgTypeCode);

    String queryOrgTypeCodeByName(@Param("name") String name);

    SysOrgType queryTypeAll(@Param("code") String code);

    Integer queryInstitutionTypeIdByName(@Param("typename") String typename);

    List<SysOrgType> queryInstitutionTypeByName(@Param("orgTypeName") String orgTypeName, @Param("type") Integer type);

    List<SysOrgType> queryInstitutionTypeByTid(@Param("sys_org_type_id") Integer id);

    List<SysOrgType> queryOrgTypeAll();

    List<SysOrgType> queryGovTypeAll();

    List<SysOrgType> queryTypeList(@Param("param") Map<String, Object> param);

    List<SysOrgType> getTopOrgTypes();

    List<SysOrgType> getSubOrgTypesById(@Param("typeId") Integer typeId);

    SysOrgType getUpOrgTypesById(@Param("typeId") Integer typeId);

    SysOrgType queryInstitutionsTypeByUpid(@Param("upid") Integer sys_org_type_upid);
}