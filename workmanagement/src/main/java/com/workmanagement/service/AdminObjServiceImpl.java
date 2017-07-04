package com.workmanagement.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.AdminObjModelDao;
import com.workmanagement.model.AdminObjModel;
import com.workmanagement.model.SysOperateListModel;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;

@Service
public class AdminObjServiceImpl implements AdminObjService {
	@Autowired
	private AdminObjModelDao adminObjDao;
	/**
	 * 添加异议处理数据
	 * @param adminObj
	 */
	@Override
	public Integer insertOrUpdate(AdminObjModel adminObj) {
		Integer id=0;
		//查询异议处理列表是否存在数据
		AdminObjModel adminObjModel=adminObjDao.selectOperateByItemId(adminObj.getSysOperateId());
		if(adminObjModel!=null){
			if(adminObjModel!=null){
				Map<String, Object> maps = new HashMap<>();
				maps.put("sysOperateId", adminObj.getSysOperateId());
				maps.put("sysOperateStatus", adminObj.getSysOperateStatus());
				maps.put("sys_operate_time", adminObj.getSysOperateTime());
				
				adminObjDao.update(maps);
				AdminObjModel admin=adminObjDao.selectOperateId(maps);
				if(admin!=null){
					id=admin.getSysOperateId();
				}
			}
		}else{
			adminObjDao.insert(adminObj);
			id=adminObj.getSysOperateId();
		}
  		return id;
	}
	/**
	 * 查询异议处理列表
	 */
	@Override
	public List<AdminObjModel> queryAll(PageSupport ps) {
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(adminObjDao.queryAll(),ps);
	}
	/**
	 * 终止更新状态
	 */
	@Override
	public void updateStatus(AdminObjModel adminObj) {
		adminObjDao.updateStatus(adminObj);
	}
	/**
	 *添加修改指标项数据到list
	 * @param sys
	 */
	@Override
	public void insertOrUpdateItem(SysOperateListModel sys) {
		List<SysOperateListModel> sysList=adminObjDao.selectOperateListByItemId(sys.getIndexItemId(),sys.getSysOperateId());
		if(sysList.size()>0){
			adminObjDao.updateItem(sys);
		}else{
			adminObjDao.insertItem(sys);
		}
		
	}
	/**
	 * 根据指标大类id和企业二码id和机构id和时间查询异议处理表中的数据
	 * @param maps
	 * @return
	 */
	@Override
	public List<SysOperateListModel> selectOperateByQueryAll(Map<String, Object> maps) {
		return adminObjDao.selectOperateByQueryAll(maps);
	}
	/**
	 * 查询异议处理详情表
	 * @param indexId
	 * @return
	 */
	@Override
	public List<SysOperateListModel> selectOperateList(Integer indexId) {
		return adminObjDao.selectOperateList(indexId);
	}
	@Override
	public Integer insertOrUpdateOperate(AdminObjModel adminObj) {
		Integer id=0;
//		Map<String, Object> map = new HashMap<>();
//		map.put("RECORD_DATE", adminObj.getRecordDate());
//		map.put("DEFAULT_INDEX_ITEM_ID", adminObj.getDefaultIndexItemId());
//		map.put("INDEX_ITEM_ID", adminObj.getIndexItemId());
//		map.put("SYS_ORG_ID", adminObj.getSysOrgId());
//		AdminObjModel ad=adminObjDao.selectOperateId(map);
//		if(ad!=null){
//			adminObjDao.updateOperate(adminObj);
//			if(ad!=null){
//				id=ad.getSysOperateId();
//			}
//		}else{
			adminObjDao.insertOperate(adminObj);
			id=adminObj.getSysOperateId();
//		}
		return id;
	}
	/**
	 * 通过指标大类查询是否存在数据
	 * @param indexItemId
	 * @return 
	 */
	@Override
	public AdminObjModel selectOperateByItemId(Integer ing) {
		return adminObjDao.selectOperateByItemId(ing);
	}
	/**
	 * 通过指标大类id，企业id，机构id，归档时间查询异议处理表是否存在该条数据
	 * @param maps
	 * @return
	 */
	@Override
	public AdminObjModel selectOperateId(Map<String, Object> map) {
		return adminObjDao.selectOperateId(map);
	}
	@Override
	public List<SysOperateListModel> queryMarkByOperateId(Integer ing) {
		// TODO Auto-generated method stub
		return adminObjDao.queryMarkByOperateId(ing);
	}
	@Override
	public List<SysOperateListModel> selectOperateByIdAll(Integer sysOperateId) {
		// TODO Auto-generated method stub
		return adminObjDao.selectOperateByIdAll(sysOperateId);
	}
	/**
	 * 通过异议处理表id删除数据
	 */
	@Transactional
	@Override
	public void delOperateByDataId(Integer sysOperateId) {
		// TODO Auto-generated method stub
		adminObjDao.delOperateByDataId(sysOperateId);
		adminObjDao.delOperateListByOperateId(sysOperateId);
	}
	/**
	 * 通过动态表id和指标大类id查询异议处理表信息
	 */
	@Override
	public List<AdminObjModel> selectSysOperateIdByDataIdAndIndexItemId(
			Integer dataId, Integer indexItemId) {
		// TODO Auto-generated method stub
		return adminObjDao.selectSysOperateIdByDataIdAndIndexItemId(dataId,indexItemId);
	}

	


}
