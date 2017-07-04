package com.workmanagement.controller;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workmanagement.model.ComPanyShow;
import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.IdentiFication;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.service.ComPanyShowService;
import com.workmanagement.service.DefaultIndexItemService;
import com.workmanagement.service.DicContentService;
import com.workmanagement.service.DicService;
import com.workmanagement.service.IdentiFicationService;
import com.workmanagement.service.IndexItemTbService;
import com.workmanagement.service.IndexTbService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysClassFyService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOtherManageService;
import com.workmanagement.util.SettingUtils;

@Controller
@RequestMapping("/admin/companyInfoQuery")
public class CompanyInfoQueryController {

	@Autowired
	private IndexItemTbService indexItemTbService;
	@Autowired
	IndexTbService indexTbService;
	@Autowired
	SysAreaService sysAreaService;
	@Autowired
	SysOrgService sysOrgService;
	@Autowired
	DefaultIndexItemService defaultIndexItemService;
	@Autowired
	SysOtherManageService sysOtherManageService;
	@Autowired
	ComPanyShowService comPanyShowService;
	@Autowired
	DicContentService dicContentService;
	@Autowired
	DicService dicService;
	@Autowired
	SysClassFyService sysClassFyService;
	@Autowired
	IdentiFicationService identiFicationService;

	@RequestMapping("/list")
	public String list(Model model, String indexItemCode[], String beginTime, String endTime) throws Exception {
		MyUserDetails su = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SysOrg so = sysOrgService.getByIdNotHaveSub(su.getSys_org_id());
		
		if (indexItemCode != null & !StringUtils.isBlank(beginTime) & !StringUtils.isBlank(endTime)) {

			SysArea sy = sysAreaService
					.queryAreaById(so.getSys_org_affiliation_area_id());
			
			Map<String, Object> pa = new HashMap<>();
			if (indexItemCode != null) {
				if (!StringUtils.isBlank(indexItemCode[0]))
					pa.put("codeCredit", indexItemCode[0]);
				if (!StringUtils.isBlank(indexItemCode[1]))
					pa.put("codeOrg", indexItemCode[1]);
				if(su.getSys_user_id()!=1)
				pa.put("area", sy.getSysAreaId());
				List<DefaultIndexItem> deff = defaultIndexItemService.queryAllByName(pa);
				if (!CollectionUtils.isEmpty(deff)) {
					
						String did = "";
						for (DefaultIndexItem defaultIndexItem : deff) {
							did+=(defaultIndexItem.getDefaultIndexItemId()+",");
						}
						did = did.substring(0,did.length()-1);
					
					Map<String, Object> par = new HashMap<>();
					if(su.getSys_user_id()!=1)
					par.put("area", sy.getSysAreaId());
					par.put("defaultIds", did.split(","));
					par.put("begin", beginTime);
					par.put("end", endTime);
					List<Map<String, Object>> com = comPanyShowService.queryAll(par);
					if (com.size() > 0) {
						model.addAttribute("ComPanyShow", com);
					}
					List<IndexTb> indexTb = null;
					if(su.getSys_user_id()!=1)
					indexTb = indexTbService.queryAll2(null, sy.getSysAreaId());
					else
						indexTb = indexTbService.queryAll(null);
					model.addAttribute("indexTb", indexTb);
					if (indexTb.size() == 0)
						model.addAttribute("err", "暂无信息");
					Map<String, List<Map<String, Object>>> map = new HashMap<>();
					Map<String, Object> map1 = new HashMap<>();
					int t1 = 0, t2 = 0, t3 = 0;
					try {
						t1 = dicService.getDicByDicName("金融机构或者政府部门",sysAreaService.queryAreaByCode("510000").getSysAreaId()).getDicId();
						t2 = dicService.getDicByDicName("经济行业",sysAreaService.queryAreaByCode("510000").getSysAreaId()).getDicId();
						t3 = dicService.getDicByDicName("地区",sysAreaService.queryAreaByCode("510000").getSysAreaId()).getDicId();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (IndexTb index : indexTb) {
						
						StringBuffer sb = new StringBuffer("select *  from " + index.getIndexCode()
								+ "_tb where DEFAULT_INDEX_ITEM_ID in(" + did
								+ ") and RECORD_DATE between '" + beginTime + "' and '" + endTime + "'");
						Map<String, Object> sql = new HashMap<>();
						sql.put("sql", sb.toString());
						List<Map<String, Object>> li = indexTbService.queryIndexTbByCode(sql);

						List<Integer> arsb=sysAreaService.getAllUpAreaIds(so.getSys_area_id());
						List<IndexItemTb> indexItemTbs = indexItemTbService.queryItemsByAreaIds(index.getIndexId(), arsb);
						
						for (IndexItemTb item : indexItemTbs) {
							if (item.getIndexItemType() == 3) {
								
									for (Map<String, Object> map2 : li) {
										for (String map3 : map2.keySet()) {
											if (item.getIndexItemCode().toUpperCase().equals(map3)) {

												if(map2.get(map3)!=null&&!map2.get(map3).toString().equals("null")){
												if(item.getDicId() == t2){
													String jjhy = sysClassFyService.queryModelByCode(map2.get(map3).toString()).getSysIndustryName();
													map2.put(map3,jjhy);
												}else if(item.getDicId() == t3){
													String areaName = sysAreaService.queryAreaByCode(map2.get(map3).toString()).getSysAreaName();
													map2.put(map3,areaName);
													
												}else if(item.getDicId() == t1){
													
												}else{
													System.out.println(item.getIndexItemName()+"==========="+map3);
													map2.put(map3,
															dicContentService
																	.getDicContentByDicIDAndCode(map2.get(map3).toString(),
																			item.getDicId())
																	.getDicContentValue());
													
												
												}
											}
										}
									}
								}
											
							
						}
						map1.put(index.getIndexCode(), indexItemTbs);
						map.put(index.getIndexCode(), li);
					}
					model.addAttribute("indexItem", map);
					model.addAttribute("indexItems", map1);
					
					try {
						Map<String, Object> ident = new HashMap<>();
						ident.put("orgIds", su.getSys_org_id());
						List<IdentiFication> dic = identiFicationService.queryIdentiFicationByAll(ident,null);
						model.addAttribute("dic", dic);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					model.addAttribute("indexItemCode", indexItemCode);
					model.addAttribute("beginTime", beginTime);
					model.addAttribute("endTime", endTime);
				
					}
					} else {
					model.addAttribute("err", "企业不存在");
				}
					
			}
			

		}
		SysOtherManage s;
		try {
			s = sysOtherManageService.querySysOtherManage(su.getSys_user_id());
			model.addAttribute("pass", s.getSysSetOrgSwitch());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "companyInfoQuery/list";

	}
	@RequestMapping("/getUrl")
	@ResponseBody
	public String getUrl(HttpServletRequest request){
  
		return  "http://"+request.getServerName()+SettingUtils.getCommonSetting("out.http.url");
	}
}
