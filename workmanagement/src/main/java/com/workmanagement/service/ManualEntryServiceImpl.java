package com.workmanagement.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.workmanagement.controller.ManualEntryController;
import com.workmanagement.dao.ManualEntryDao;
import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.Dic;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysClassFyModel;
import com.workmanagement.model.SysGover;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.util.BaseDaoSupport;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
/**
 * 手工修改Service实现
 * @author tianhao
 *
 */
@Service
public class ManualEntryServiceImpl extends BaseDaoSupport implements ManualEntryService {

	@Autowired
	private ManualEntryDao manualEntryDao;
	
	@Autowired
    private SysUserLogService sysUserLogService;
	
	@Autowired
	private DicService dicService;
	
	@Autowired
	private SysOtherManageService sysOtherManageService;
	
	@Autowired
	private DefaultIndexItemService defaultIndexItemService;
	
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
	 * @see com.workmanagement.service.ManualEntryService#queryIndexTbAll()
	 */
	@Override
	public List<IndexTb> queryIndexTbAll(Map<String, Object> map) {
		
		return manualEntryDao.queryIndexTbAll(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#getDefaultIndexItemByCode(java.lang.String)
	 */
	@Override
	public DefaultIndexItem getDefaultIndexItemByCode(String defaultIndexItemByCode,Integer areaId) throws Exception {
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(defaultIndexItemByCode!=null){
			DefaultIndexItem defaultIndexItemCredit=manualEntryDao.queryCodeCredit(defaultIndexItemByCode,areaId);
			if(defaultIndexItemCredit!=null){
				return defaultIndexItemCredit;
			}else{
				SysOtherManage stm=sysOtherManageService.querySysOtherManage(userDetails.getSys_user_id());
				if(stm.getSysSetOrgSwitch()==1){
					DefaultIndexItem defaultIndexItemOrg=manualEntryDao.queryCodeOrg(defaultIndexItemByCode,areaId);
					if(defaultIndexItemOrg!=null){
						return defaultIndexItemOrg;
					}
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#queryIndexTbById(java.lang.Integer)
	 */
	@Override
	public IndexTb queryIndexTbById(Integer id) {
		
		return manualEntryDao.queryIndexTbById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#temporaryTableList(java.util.Map)
	 */
	@Override
	public List<Map<String,Object>> temporaryTableList(PageSupport ps,Map<String, Object> map) {
		if(ps==null){
			return manualEntryDao.temporaryTableList(map);
		}
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(manualEntryDao.temporaryTableList(map),ps);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#queryReportIndexs(java.util.Map)
	 */
	@Override
	public List<SysUserLog> querySysUserLogList(Map<String, Object> param,PageSupport ps) {
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(manualEntryDao.querySysUserLogList(param),ps);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#getReportIndexById(java.lang.Integer)
	 */
	@Override
	public SysUserLog getSysUserLogById(Integer id) {
		
		return manualEntryDao.getSysUserLogById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#getDefaultIndexItemById(java.lang.Integer)
	 */
	@Override
	public DefaultIndexItem getDefaultIndexItemById(Integer id) {
		
		return manualEntryDao.getDefaultIndexItemById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#getIndexItemTbByCode(java.lang.String)
	 */
	@Override
	public IndexItemTb getIndexItemTbByCode(String code) {
		
		return manualEntryDao.getIndexItemTbByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#queryIndexTbByIndexName(java.lang.String)
	 */
	@Override
	public IndexTb queryIndexTbByIndexName(String name) {
		
		return manualEntryDao.queryIndexTbByIndexName(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#updataStatus(java.lang.String)
	 */
	@Override
	public void updataStatus(String sql) {
		
		manualEntryDao.updataStatus(sql);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#querySysOrgAll(java.util.Map)
	 */
	@Override
	public List<SysOrg> querySysOrgAll(Map<String, Object> map) {
		
		return manualEntryDao.querySysOrgAll(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#updateIndexTbSql(java.util.Map)
	 */
	@Override
	@Transactional
	public void updateIndexTbSql(String[] indexItemCode,String[] indexItemValue,Integer majorId,IndexTb indexTb,String path,Integer defaultId,String codeCredit,String codeOrg,HttpServletRequest request) throws Exception{
		//查询修改前的数据
		String indexCode = indexTb.getIndexCode();
		List<IndexItemTb> indexItemTbList = indexItemTbService.queryIndexItemTbsByIndexId(indexTb.getIndexId());
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
		
		//修改企业二码
		DefaultIndexItem defaultIndexItem=defaultIndexItemService.queryById(defaultId);
		if(defaultIndexItem!=null){
			Map<String, Object> mapsdefault = new HashMap<>();
			mapsdefault.put("defaultId", defaultId);
			if("".equals(codeCredit)){
				codeCredit=null;
			}
			if("".equals(codeOrg)){
				codeOrg=null;
			}
			if(defaultIndexItem.getCodeCredit()!=null){
				if(!defaultIndexItem.getCodeCredit().equals(codeCredit)){
					mapsdefault.put("codeCredit", codeCredit);
					manualEntryDao.updateDefaultIndexitemCodeCreditById(mapsdefault);
					SysUserLog sul=new SysUserLog();
					sul.setSysUserLogMenuName("手工录入");
					sul.setIndexId(indexTb.getIndexId());
					sul.setSysUserLogEnterpriseCode(defaultId.toString());
					sul.setSysUserLogOldValue(defaultIndexItem.getCodeCredit());
					sul.setSysUserLogNewValue(codeCredit);
					sul.setSysUserLogOperateType(3);
					sul.setSysUserLogAuthFile(path);
					sul.setSysUserLogResult(true);
					sysUserLogService.insertOneLog(sul, request);
				}
			}else{
				if(codeCredit!=null){
					mapsdefault.put("codeCredit", codeCredit);
					manualEntryDao.updateDefaultIndexitemCodeCreditById(mapsdefault);
					SysUserLog sul=new SysUserLog();
					sul.setSysUserLogMenuName("手工录入");
					sul.setIndexId(indexTb.getIndexId());
					sul.setSysUserLogEnterpriseCode(defaultId.toString());
					sul.setSysUserLogOldValue(defaultIndexItem.getCodeCredit());
					sul.setSysUserLogNewValue(codeCredit);
					sul.setSysUserLogOperateType(3);
					sul.setSysUserLogAuthFile(path);
					sul.setSysUserLogResult(true);
					sysUserLogService.insertOneLog(sul, request);
				}
			}
			
			if(defaultIndexItem.getCodeOrg()!=null){
				if(!defaultIndexItem.getCodeOrg().equals(codeOrg)){
					mapsdefault.put("codeOrg", codeOrg);
					manualEntryDao.updateDefaultIndexitemCodeOrgById(mapsdefault);
					SysUserLog sul=new SysUserLog();
					sul.setSysUserLogMenuName("手工录入");
					sul.setIndexId(indexTb.getIndexId());
					sul.setSysUserLogEnterpriseCode(defaultId.toString());
					sul.setSysUserLogOldValue(defaultIndexItem.getCodeOrg());
					sul.setSysUserLogNewValue(codeOrg);
					sul.setSysUserLogOperateType(3);
					sul.setSysUserLogAuthFile(path);
					sul.setSysUserLogResult(true);
					sysUserLogService.insertOneLog(sul, request);
				}
			}else{
				if(codeOrg!=null){
					mapsdefault.put("codeOrg", codeOrg);
					manualEntryDao.updateDefaultIndexitemCodeOrgById(mapsdefault);
					SysUserLog sul=new SysUserLog();
					sul.setSysUserLogMenuName("手工录入");
					sul.setIndexId(indexTb.getIndexId());
					sul.setSysUserLogEnterpriseCode(defaultId.toString());
					sul.setSysUserLogOldValue(defaultIndexItem.getCodeOrg());
					sul.setSysUserLogNewValue(codeOrg);
					sul.setSysUserLogOperateType(3);
					sul.setSysUserLogAuthFile(path);
					sul.setSysUserLogResult(true);
					sysUserLogService.insertOneLog(sul, request);
				}
			}
		}
		//添加用户行为审计
		for (int j2 = 0; j2 < indexItemValue.length; j2++) {
			for (int k = j2; k < valueList.size(); ) {
				SysUserLog sul=new SysUserLog();
				sul.setSysUserLogMenuName("手工录入");
				sul.setIndexId(indexTb.getIndexId());
				sul.setIndexItemId(indexItemTbList.get(j2).getIndexItemId());
				sul.setSysUserLogEnterpriseCode(defaultId.toString());
				if("".equals(valueList.get(k)) || valueList.get(k)==null){
					sul.setSysUserLogOldValue(null);
				}else{
					if(indexItemTbList.get(k).getIndexItemType()==3){
						Integer dicId=indexItemTbList.get(k).getDicId();
						Dic dic=dicService.getDicByDicId(dicId);
						if(dic!=null){
							if(isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
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
							}else if(isContain(dic.getDicName(),ManualEntryController.ORGANIZATION)){
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
							}else if(isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
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
							}else if(isContain(dic.getDicName(),ManualEntryController.Area)){
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
				if("".equals(indexItemValue[j2])){
					sul.setSysUserLogNewValue(null);
				}else{
					if(indexItemTbList.get(k).getIndexItemType()==3){
						Integer dicId=indexItemTbList.get(k).getDicId();
						Dic dic=dicService.getDicByDicId(dicId);
						if(dic!=null){
							if(isContain(dic.getDicName(),ManualEntryController.INDUSTRY)){
								SysClassFyModel industry=sysClassFyService.queryModelByCode(indexItemValue[j2]);
								if(industry!=null){
									indexItemValue[j2]=industry.getSysIndustryName();
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(indexItemValue[j2]);
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										indexItemValue[j2]=dics.getDicContentValue();
									}
								}
							}else if(isContain(dic.getDicName(),ManualEntryController.ORGANIZATION)){
								SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(indexItemValue[j2]);
								if(sysOrg!=null){
									indexItemValue[j2]=sysOrg.getSys_org_name();
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(indexItemValue[j2]);
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										indexItemValue[j2]=dics.getDicContentValue();
									}
								}
							}else if(isContain(dic.getDicName(),ManualEntryController.GOVERNMENT)){
								SysOrg sysOrg=sysOrgService.querySysorgByFinancialCode(indexItemValue[j2]);
								if(sysOrg!=null){
									indexItemValue[j2]=sysOrg.getSys_org_name();
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(indexItemValue[j2]);
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										indexItemValue[j2]=dics.getDicContentValue();
									}
								}
							}else if(isContain(dic.getDicName(),ManualEntryController.Area)){
								SysArea sysArea=sysAreaService.queryAreaByCode(indexItemValue[j2]);
								if(sysArea!=null){
									indexItemValue[j2]=sysArea.getSysAreaName();
								}else{
									DicContent dicContent=new DicContent();
									dicContent.setDicId(dic.getDicId());
									dicContent.setDicContentCode(indexItemValue[j2]);
									DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
									if(dics!=null){
										indexItemValue[j2]=dics.getDicContentValue();
									}
								}
							}else{
								DicContent dicContent=new DicContent();
								dicContent.setDicId(dic.getDicId());
								dicContent.setDicContentCode(indexItemValue[j2]);
								DicContent dics=manualEntryDao.queryDicContentByIdAndCode(dicContent);
								if(dics!=null){
									indexItemValue[j2]=dics.getDicContentValue();
								}
							}
						}
					}
					sul.setSysUserLogNewValue(indexItemValue[j2]);
				}
				sul.setSysUserLogOperateType(3);
				sul.setSysUserLogAuthFile(path);
				sul.setSysUserLogResult(true);
				if(("".equals(valueList.get(k)) || valueList.get(k)==null) && !"".equals(indexItemValue[j2])){
					try {
						sysUserLogService.insertOneLog(sul, request);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if((!"".equals(valueList.get(k)) && valueList.get(k)!=null) && "".equals(indexItemValue[j2])){
					try {
						sysUserLogService.insertOneLog(sul, request);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if((!"".equals(valueList.get(k)) && valueList.get(k)!=null) && !"".equals(indexItemValue[j2])){
					if(!valueList.get(k).toString().equals(indexItemValue[j2])){
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
	 * @see com.workmanagement.service.ManualEntryService#querySysUserLog(com.workmanagement.util.PageSupport)
	 */
	@Override
	public List<SysUserLog> querySysUserLog(Integer indexId,PageSupport ps) {
		PageHelper.startPage(ps.getPageOffset()+1,ps.getPageSize());
		return PageHelperSupport.queryCount(manualEntryDao.querySysUserLog(indexId),ps);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#queryDicContentByIdAndCode(com.workmanagement.model.DicContent)
	 */
	@Override
	public DicContent queryDicContentByIdAndCode(DicContent dicContent) {
		
		return manualEntryDao.queryDicContentByIdAndCode(dicContent);
	}
	
	/**
	 * 判断s1是否包含s2
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean isContain(String s1,String s2) {
		return s1.contains(s2);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#queryDefaultIndexItemCountByCodeCredit(java.util.Map)
	 */
	@Override
	public Integer queryDefaultIndexItemCountByCodeCredit(Map<String, Object> map) {
		
		return manualEntryDao.queryDefaultIndexItemCountByCodeCredit(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.workmanagement.service.ManualEntryService#queryDefaultIndexItemCountByCodeOrg(java.util.Map)
	 */
	@Override
	public Integer queryDefaultIndexItemCountByCodeOrg(Map<String, Object> map) {
		
		return manualEntryDao.queryDefaultIndexItemCountByCodeOrg(map);
	}

}
