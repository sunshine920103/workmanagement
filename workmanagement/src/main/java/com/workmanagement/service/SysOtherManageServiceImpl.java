package com.workmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.workmanagement.dao.SysOtherManageDao;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.model.SysUser;
import com.workmanagement.util.BaseDaoSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;

/**
 * 其他管理Service实现
 * 
 * @author tianhao
 *
 */
@Service
public class SysOtherManageServiceImpl extends BaseDaoSupport implements SysOtherManageService {

	@Autowired
	private SysOtherManageDao sysOtherManageDao;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysOrgService sysOrgService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.workmanagement.service.SysOtherManageService#querySysOtherManage()
	 */
	@Override
	public SysOtherManage querySysOtherManage(Integer aid) throws Exception {
		// 获取缓存
//				String setbKey = RedisKeys.SYS_SET + aid;
				SysOtherManage s;
//				if (RedisUtil.isEmpty(setbKey)) {
					s = this.query(aid);
//					RedisUtil.setData(RedisKeys.SYS_SET + aid, s);
//				} else {
//					s = RedisUtil.getObjData(setbKey, SysOtherManage.class);
//				}
				return s;
	}

	public SysOtherManage query(Integer aid) throws Exception {
		SysUser su = sysUserService.querySystemUserById(aid);
		SysArea sy = sysAreaService
				.queryAreaById(sysOrgService.queryInstitutionsById(su.getSys_org_id()).getSys_area_id());
		while (!sy.getSysAreaType().equals("0") && !sy.getSysAreaType().equals("1")) {
			sy = sysAreaService.queryAreaById(sy.getSysAreaUpid());
		}
		SysOtherManage s = sysOtherManageDao.querySysOtherManage(sy.getSysAreaId());
		if (s == null) {
			s = sysOtherManageDao.querySysOtherManage(1);
		}
		return s;
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.workmanagement.service.SysOtherManageService#updateSysOtherManage(com
	 * .workmanagement.model.SysOtherManage)
	 */
	@Override
	public void updateSysOtherManage(SysOtherManage stm) throws Exception {
		
		sysOtherManageDao.updateSysOtherManage(stm);
		RedisUtil.delBatchData(RedisKeys.SYS_SET);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.workmanagement.service.SysOtherManageService#insertSysOtherManage(com
	 * .workmanagement.model.SysOtherManage)
	 */
	@Override
	public void insertSysOtherManage(SysOtherManage stm) throws Exception {
		
		sysOtherManageDao.insertSysOtherManage(stm);
		RedisUtil.delBatchData(RedisKeys.SYS_SET);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.workmanagement.service.SysOtherManageService#querySysOtherManageById(
	 * java.lang.Integer)
	 */
	@Override
	public SysOtherManage querySysOtherManageById(Integer aid) throws Exception {

		return sysOtherManageDao.querySysOtherManage(aid);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.SysOtherManageService#updateMonthLimit(java.lang.Integer)
	 */
	@Override
	public void updateMonthLimit(Integer monthLimit) throws Exception {
		
		sysOtherManageDao.updateMonthLimit(monthLimit);
	}

	@Override
	public SysOtherManage querySysOtherManageAreaId(Integer aid) throws Exception {
		// TODO Auto-generated method stub
		SysOtherManage s = sysOtherManageDao.querySysOtherManage(aid);
		if (s == null) {
			s = sysOtherManageDao.querySysOtherManage(1);
		}
		return s;
	}

}
