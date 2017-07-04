package com.workmanagement.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.workmanagement.controller.ManualEntryController;
import com.workmanagement.dao.ManualEntryDao;
import com.workmanagement.dao.OrgObjDao;
import com.workmanagement.model.AdminObjModel;
import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.Dic;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysClassFyModel;
import com.workmanagement.model.SysGover;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOperateListModel;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.util.BaseDaoSupport;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;
/**
 * 金融机构异议处理Service实现
 * @author tianhao
 *
 */
@Service
public class OrgObjServiceImpl extends BaseDaoSupport implements OrgObjService {
	
	@Autowired
	private OrgObjDao orgObjDao;
	
	@Autowired
	private ManualEntryDao manualEntryDao;
	
	@Autowired
	private ManualEntryService manualEntryService;
	
	@Autowired
    private SysUserLogService sysUserLogService;
	
	@Autowired
	private DicService dicService;
	
	@Autowired
	private SysClassFyService sysClassFyService;
	
	@Autowired
	private SysGoverService sysGoverService;
	
	@Autowired
	private SysOrgService sysOrgService;
	
	@Autowired
	private SysAreaService sysAreaService;
	
	@Autowired
	private IndexItemTbService indexItemTbService;

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#querySysOperateList(java.util.Map)
	 */
	@Override
	public List<AdminObjModel> querySysOperateList(PageSupport ps,Map<String, Object> map) {
		if(ps==null){
			return orgObjDao.querySysOperateList(map);
		}
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(orgObjDao.querySysOperateList(map),ps);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#querySysOperateCount(java.util.Map)
	 */
	@Override
	public Integer querySysOperateCount(Map<String, Object> map) {
		
		return orgObjDao.querySysOperateCount(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#insertSysOperate(com.workmanagement.model.AdminObjModel)
	 */
	@Override
	public void insertSysOperate(AdminObjModel adminObjModel) {
		
		orgObjDao.insertSysOperate(adminObjModel);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#updateSysOperate(com.workmanagement.model.AdminObjModel)
	 */
	@Override
	public void updateSysOperate(AdminObjModel adminObjModel) {
		
		orgObjDao.updateSysOperate(adminObjModel);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#querySysOperateListCount(java.util.Map)
	 */
	@Override
	public Integer querySysOperateListCount(Map<String, Object> map) {
		
		return orgObjDao.querySysOperateListCount(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#insertSysOperateList(com.workmanagement.model.SysOperateListModel)
	 */
	@Override
	public void insertSysOperateList(SysOperateListModel sysOperateListModel) {
		
		orgObjDao.insertSysOperateList(sysOperateListModel);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#updateIndexTbSql(java.lang.Integer, java.lang.Integer, java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[], java.lang.Integer, java.lang.String)
	 */
	@Override
	@Transactional
	public void updateIndexTbSql(Integer indexId, Integer majorId, String[] indexItemCode, String[] indexItemValue,
			String[] operateInformation, String[] operateOrgdesc, String[] operateService, String sysOperateList,
			Integer sysOperateId,String file,Integer defaultId,HttpServletRequest request) throws Exception {
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg sorg = RedisUtil.getObjData(orgKey, SysOrg.class);
		//查询修改前的数据
		IndexTb indexTb = manualEntryDao.queryIndexTbById(indexId);
		String indexCode = indexTb.getIndexCode();
		List<IndexItemTb> indexItemTbList = indexItemTbService.queryIndexItemTbsByIndexId(indexId);
		String sqllist = "select * from " + indexCode + "_tb where " + indexCode + "_id=" + majorId;
		Map<String, Object> maps = new HashMap<>();
		maps.put("queryTemporarySql", sqllist);
		// 获取表数据集
		List<Map<String, Object>> dataValues = manualEntryDao.temporaryTableList(maps);
		Map<String, Object> mapSon = dataValues.get(0);
		Set<Entry<String, Object>> entries = mapSon.entrySet();
		// 比对列名 按顺序赋值
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		List<Object> valueList = new ArrayList<>();
		for (int j = 0; j < indexItemTbList.size(); j++) {
			for (Entry<String, Object> entry : entries) {
				if (indexItemTbList.get(j).getIndexItemCode().equals(entry.getKey().toLowerCase())) {
					if (entry.getValue() instanceof Date) {
						valueList.add(fm.format(entry.getValue()));
					} else {
						valueList.add(entry.getValue());
					}
					break;
				}
			}
		}
		//执行修改方法
		StringBuffer sbc=new StringBuffer();
		for (int i = 0; i < indexItemCode.length; i++) {
			for (int j = 0; j < indexItemValue.length; j++) {
				if(i==j){
					if(indexItemTbList.get(i).getIndexItemType()==2 || indexItemTbList.get(i).getIndexItemType()==1){
						if("".equals(indexItemValue[j])){
							sbc.append(indexItemCode[i]+"=null,");
						}else{
							sbc.append(indexItemCode[i]+"='"+indexItemValue[j]+"',");
						}
					}else{
						sbc.append(indexItemCode[i]+"='"+indexItemValue[j]+"',");
					}
				}
			}
		}
		sbc.deleteCharAt(sbc.length()-1);
		String sql="update "+indexTb.getIndexCode()+"_tb set "+sbc+" where "+indexTb.getIndexCode()+"_id="+majorId;
		Map<String, Object> map = new HashMap<>();
		map.put("updateIndexTbSql", sql);
		manualEntryDao.updateIndexTbSql(map);
		
		AdminObjModel sysOperate=orgObjDao.querySysOperateById(sysOperateId);
		//修改异议处理状态
		orgObjDao.updateSysOperateStatus(sysOperateId);
		
		SysUserLog sysul=new SysUserLog();
		sysul.setSysUserLogMenuName("机构异议处理");
		if(sysOperate!=null){
			if(sysOperate.getSysOperateStatus().intValue()==0){
				sysul.setSysUserLogOldValue("待处理");
			}else if(sysOperate.getSysOperateStatus().intValue()==1){
				sysul.setSysUserLogOldValue("处理中");
			}
		}
		sysul.setSysUserLogNewValue("已处理");
		sysul.setSysUserLogOperateType(3);
		sysul.setSysUserLogCount(1);
		sysul.setSysUserLogResult(true);
		sysul.setIndexId(indexTb.getIndexId());
		sysUserLogService.insertOneLog(sysul, request);
		
		//修改异议处理列表
		String[] array=sysOperateList.split(",");
		for (int i = 0; i < array.length; i++) {
			SysOperateListModel sol=new SysOperateListModel();
			sol.setOrgExplain(operateOrgdesc[i]);
			sol.setMaininfoExplain(operateInformation[i]);
			sol.setSysOperateTime(new Date());
			String id=array[i];
			sol.setSysOperateListId(Integer.parseInt(id));
			orgObjDao.updateSysOperateList(sol);
		}
		
		// 获取表数据集
		List<Map<String, Object>> dataValuesop = manualEntryDao.temporaryTableList(maps);
		Map<String, Object> mapSonop = dataValuesop.get(0);
		Set<Entry<String, Object>> entriesop = mapSonop.entrySet();
		// 比对列名 按顺序赋值
		SimpleDateFormat fms = new SimpleDateFormat("yyyy-MM-dd");
		List<Object> valueListop = new ArrayList<>();
		for (int j = 0; j < indexItemTbList.size(); j++) {
			for (Entry<String, Object> entry : entriesop) {
				if (indexItemTbList.get(j).getIndexItemCode().equals(entry.getKey().toLowerCase())) {
					if (entry.getValue() instanceof Date) {
						valueListop.add(fms.format(entry.getValue()));
					} else {
						valueListop.add(entry.getValue());
					}
					break;
				}
			}
		}
		//添加用户行为审计
		for (int j2 = 0; j2 < valueListop.size(); j2++) {
			for (int k = j2; k < valueList.size(); ) {
				SysUserLog sul=new SysUserLog();
				sul.setSysUserLogMenuName("金融机构异议处理");
				sul.setIndexId(indexTb.getIndexId());
				sul.setIndexItemId(indexItemTbList.get(j2).getIndexItemId());
				if(defaultId!=null && !"".equals(defaultId)){
					sul.setSysUserLogEnterpriseCode(defaultId.toString());
				}
				if(valueList.get(k)==null || "".equals(valueList.get(k))){
					sul.setSysUserLogOldValue(null);
				}else{
					if(indexItemTbList.get(k).getIndexItemType()==3){
						Integer dicId=indexItemTbList.get(k).getDicId();
						Dic dic=dicService.getDicByDicId(dicId);
						if(dic!=null){
							if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
								SysClassFyModel industry=sysClassFyService.queryModelByCode(valueList.get(k).toString());
								if(industry!=null){
									valueList.set(k, industry.getSysIndustryName());
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(valueList.get(k).toString());
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										valueList.set(k, dics.getDicContentValue());
									}
								}
							}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.ORGANIZATION)){
								SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(valueList.get(k).toString());
								if(sysOrg!=null){
									valueList.set(k, sysOrg.getSys_org_name());
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(valueList.get(k).toString());
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										valueList.set(k, dics.getDicContentValue());
									}
								}
							}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
								SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(valueList.get(k).toString());
								if(sysOrg!=null){
									valueList.set(k, sysOrg.getSys_org_name());
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(valueList.get(k).toString());
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										valueList.set(k, dics.getDicContentValue());
									}
								}
							}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.Area)){
								SysArea sysArea=sysAreaService.queryAreaByCode(valueList.get(k).toString());
								if(sysArea!=null){
									valueList.set(k, sysArea.getSysAreaName());
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(valueList.get(k).toString());
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										valueList.set(k, dics.getDicContentValue());
									}
								}
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueList.get(k).toString());
								DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueList.set(k, dics.getDicContentValue());
								}
							}
						}
					}
					sul.setSysUserLogOldValue(valueList.get(k).toString());
				}
				if(valueListop.get(k)==null || "".equals(valueListop.get(k))){
					sul.setSysUserLogNewValue(null);
				}else{
					if(indexItemTbList.get(k).getIndexItemType()==3){
						Integer dicId=indexItemTbList.get(k).getDicId();
						Dic dic=dicService.getDicByDicId(dicId);
						if(dic!=null){
							if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
								SysClassFyModel industry=sysClassFyService.queryModelByCode(valueListop.get(k).toString());
								if(industry!=null){
									valueListop.set(k, industry.getSysIndustryName());
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(valueListop.get(k).toString());
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										valueListop.set(k, dics.getDicContentValue());
									}
								}
							}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.ORGANIZATION)){
								SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(valueListop.get(k).toString());
								if(sysOrg!=null){
									valueListop.set(k, sysOrg.getSys_org_name());
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(valueListop.get(k).toString());
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										valueListop.set(k, dics.getDicContentValue());
									}
								}
							}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
								SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(valueListop.get(k).toString());
								if(sysOrg!=null){
									valueListop.set(k, sysOrg.getSys_org_name());
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(valueListop.get(k).toString());
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										valueListop.set(k, dics.getDicContentValue());
									}
								}
							}else if(ManualEntryController.isContain(dic.getDicName(),ManualEntryController.Area)){
								SysArea sysArea=sysAreaService.queryAreaByCode(valueListop.get(k).toString());
								if(sysArea!=null){
									valueListop.set(k, sysArea.getSysAreaName());
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(valueListop.get(k).toString());
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										valueListop.set(k, dics.getDicContentValue());
									}
								}
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(valueListop.get(k).toString());
								DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									valueListop.set(k, dics.getDicContentValue());
								}
							}
						}
					}
					sul.setSysUserLogNewValue(valueListop.get(k).toString());
				}
				sul.setSysUserLogOperateType(3);
				sul.setSysUserLogAuthFile(file);
				sul.setSysUserLogResult(true);
				if((valueList.get(k)==null || "".equals(valueList.get(k))) && (valueListop.get(k)!=null && !"".equals(valueListop.get(k)))){
					try {
						sysUserLogService.insertOneLog(sul, request);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if((valueList.get(k)!=null && !"".equals(valueList.get(k))) && (valueListop.get(k)==null || "".equals(valueListop.get(k)))){
					try {
						sysUserLogService.insertOneLog(sul, request);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if((valueList.get(k)!=null && !"".equals(valueList.get(k))) && (valueListop.get(k)!=null && !"".equals(valueListop.get(k)))){
					if(!valueList.get(k).toString().equals(valueListop.get(k).toString())){
						try {
							sysUserLogService.insertOneLog(sul, request);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#querySysOperateListByOperateId(java.lang.Integer)
	 */
	@Override
	public List<SysOperateListModel> querySysOperateListByOperateId(Integer operateId) {
		
		return orgObjDao.querySysOperateListByOperateId(operateId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#querySysOperateById(java.lang.Integer)
	 */
	@Override
	public AdminObjModel querySysOperateById(Integer operateId) {
		
		return orgObjDao.querySysOperateById(operateId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#querySysOperateCountByStatus(java.util.Map)
	 */
	@Override
	public Integer querySysOperateCountByStatus(Map<String, Object> map) {
		
		return orgObjDao.querySysOperateCountByStatus(map);
	}
	@Override
	public List<AdminObjModel> querySysOperateList1(PageSupport ps,Map<String, Object> map) {
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(orgObjDao.querySysOperateList1(map),ps);
	}
	@Override
	public List<AdminObjModel> queryAllSysOperateList1(PageSupport ps,Map<String, Object> map) {
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(orgObjDao.queryAllSysOperateList1(map),ps);
	}
	@Override
	public List<AdminObjModel> queryAllSysOperateList(PageSupport ps) {
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(orgObjDao.queryAllSysOperateList(),ps);
	}
	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#queryDefaultId(java.util.Map)
	 */
	@Override
	public List<Integer> queryDefaultId(Map<String, Object> map) {
		
		return orgObjDao.queryDefaultId(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.OrgObjService#updateSysOperateListMark(com.workmanagement.model.SysOperateListModel)
	 */
	@Override
	public void updateSysOperateListMark(SysOperateListModel sl) {
		
		orgObjDao.updateSysOperateListMark(sl);
	}

}
