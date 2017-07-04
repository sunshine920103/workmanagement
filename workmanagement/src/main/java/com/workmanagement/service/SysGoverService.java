package com.workmanagement.service;

import java.util.List;
import com.workmanagement.model.Dic;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysGover;
import com.workmanagement.util.PageSupport;

/**
 * 政府部门管理Service接口
 * @author tianhao
 *
 */
public interface SysGoverService {

	/**
	 * 查询政府部门列表
	 * 
	 * @return
	 */
	List<SysGover> querySysGoverList(PageSupport ps,SysGover sg) throws Exception;
	
	/**
	 * 通過ID查詢政府部门
	 * @param id
	 * @return
	 */
	SysGover querySysGoverById(Integer id) throws Exception;
	
	/**
	 * 保存政府部门
	 * @param sg
	 * @throws Exception
	 */
	void saveSysGover(SysGover sg) throws Exception;
	
	/**
	 * 通过Name查询名称是否重复
	 * @param sysGovName
	 * @return
	 * @throws Exception
	 */
	int querySysGoverByNameCount(SysGover sg) throws Exception;
	
	/**
	 * 通过Code查询编码是否重复
	 * @param sg
	 * @return
	 * @throws Exception
	 */
	int querySysGoverByCodeCount(SysGover sg) throws Exception;
	
	/**
	 * 通过ID删除政府部门
	 * @param id
	 */
	void delSysGoverById(Integer id) throws Exception;
	
	/**
	 * 修改政府部门
	 * @param sg
	 * @throws Exception
	 */
	void updateSysGover(SysGover sg) throws Exception;
	
	/**
	 * 查询政府部门所有数据
	 * @return
	 * @throws Exception
	 */
	List<SysGover> queryTypeAll(String name) throws Exception;
	
	/**
	 * 根据ID查询政府部门
	 * @param id
	 * @return
	 * @throws Exception
	 */
	SysGover queryInstitutionsById(Integer id) throws Exception;
	
	/**
	 * 通过名称和编码查询政府部门
	 * @param name
	 * @param code
	 * @return
	 * @throws Exception
	 */
	List<SysGover> querySysGoverByCodeAndName(String name,String code) throws Exception;
	
	/**
	 * 根据政府部门名称查询id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	SysGover querySysGoverIdByName(String name) throws Exception;
	
	/**
	 * 根据字典名称名称查询id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	Dic queryDicIdByName(String name) throws Exception;
	
	/**
	 * 根据地区名称名称查询id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	SysArea querySysAreaIdByName(String name) throws Exception;
	
	/**
	 * 通过政府部门名称查询编码
	 * @param name
	 * @return
	 * @throws Exception
	 */
	String querySysGoverCodeByName(String name) throws Exception;
	
	/**
	 * 查询政府部门列表
	 * 
	 * @return
	 */
	List<SysGover> querySysGoverByNameList(SysGover sg) throws Exception;
	
	/**
	 * 通过ID删除下级政府部门
	 * @param id
	 */
	void delSysGoverByUpid(Integer id) throws Exception;
	
	/**
	 * 通过id查询是否有下级政府部门
	 * @param id
	 * @return
	 */
	int querySysGoverUpidByCount(Integer id);
	
	/**
	 * 通过id查询下级政府部门
	 * @param id
	 * @return
	 */
	List<SysGover> querySysGoverByUpid(Integer id);
}
