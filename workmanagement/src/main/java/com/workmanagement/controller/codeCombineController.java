package com.workmanagement.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.db2.jcc.t4.ob;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.DefaultIndexItemCombine;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysUser;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.service.DefaultIndexItemService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysUserLogService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.DateFormatter;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.ExcelReader;
import com.workmanagement.util.LoggerUtil;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.UpLoadFile;

/**
 * 企业二码合并
 * 
 * @author lzl
 */
@Controller
@RequestMapping(value = "/admin/codeCombine")
public class codeCombineController {

	@Autowired
	DefaultIndexItemService defaultIndexItemService;
	@Autowired
	SysManageLogService sysManageLogService;

	@Autowired
	SysOrgService sysOrgService;

	private static final String SYS_MENU = "企业二码合并";

	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model) throws Exception {
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		param.put("menu", "企业二码合并");
		String[] s = { userDetails.getSys_org_id().toString() };
		param.put("instiAll", s);
		param.put("types", SysManageLog.UPDATE_SYSMANAGElOG);
		model.addAttribute("list", sysManageLogService.queryAdminLogTbs(param, ps));
		return "codeCombine/list";

	}

	@RequestMapping("/downLoad")
	@ResponseBody
	public void downLoad(HttpServletRequest request, HttpServletResponse response) {

		try {

			String[] rowNames = { "统一社会信用代码", "组织机构代码", "企业名称" };
			String[] propertyNames = { "codeCredit", "codeOrg", "qymc" };

			// 生成excel
			ExcelExport<DefaultIndexItem> excelExport = new ExcelExport<>();
			excelExport.setTitle("企业二码合并Excle模版");
			excelExport.setRowNames(rowNames);
			excelExport.setPropertyNames(propertyNames);
			String url = excelExport.exportExcel(request, response);
			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null, null, null,
					new Date(), SysManageLog.EXPORT_SYSMANAGElOG, null, "导出模版", null, null, url, null, true),request);

		} catch (Exception e) {
			LoggerUtil.error(e);

		}
	}

	/**
	 * 导入
	 * 
	 * @param file
	 *            文件名
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception 
	 */
	@RequestMapping("/importExcle")
	public String importExcle(MultipartFile file, HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SysOrg org = sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
		String url = UpLoadFile.upLoadFile(file);
		try {
			String[] rowNames = { "统一社会信用代码", "组织机构代码", "企业名称" };
			String[] propertyNames = { "codeCredit", "codeOrg", "qymc" };

			ExcelReader<DefaultIndexItem> excelReader = new ExcelReader<>();
			excelReader.setRowNames(rowNames);
			excelReader.setPropertyNames(propertyNames);
			excelReader.setT(new DefaultIndexItem());
			List<DefaultIndexItem> list = excelReader.excelNoValidateReader(new File(url), request, 1);
			List<Set<DefaultIndexItem>> newDe = new ArrayList<>();

			List<String> errList = new ArrayList<>();
			for (DefaultIndexItem defaultIndexItem2 : list) {
				defaultIndexItem2.setSys_area_id(org.getSys_area_id());
				String qymc = defaultIndexItem2.getQymc();
				defaultIndexItem2.setQymc(null);
				if (!StringUtils.isEmpty(defaultIndexItem2.getCodeCredit())
						&& !StringUtils.isEmpty(defaultIndexItem2.getCodeOrg())) {
					List<DefaultIndexItem> dit = defaultIndexItemService.queryBycode(defaultIndexItem2);
					if (dit.size() == 2) {

						Set<DefaultIndexItem> set = new HashSet<>();
						for (DefaultIndexItem defaultIndexItem : dit) {
							set.add(defaultIndexItem);
						}
						newDe.add(set);
					} else {
						errList.add("第" + (list.indexOf(defaultIndexItem2) + 2) + "中统一社会信用代码或组织机构码不存在，请检查！");
					}
				} else {
					errList.add("第" + (list.indexOf(defaultIndexItem2) + 2) + "数据格式有误！");
				}
			}
			model.addAttribute("list", newDe);
			if (errList.size() > 0)
				model.addAttribute("errList", errList);
			else
				model.addAttribute("errList", null);
			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null, null, null,
					new Date(), SysManageLog.IMPORT_SYSMANAGElOG, list.size(), null, null, null, url, null, true),request);

		} catch (Exception e) {
			model.addAttribute("err", "模版格式错误");
			return "codeCombine/list";
		}
		return "codeCombine/add";
	}

	@SuppressWarnings("deprecation")
	@RequestMapping("/hebin")
	public String heBin(@RequestParam(required = false) String[] chack, Model model,HttpServletRequest request) {
//		List<DefaultIndexItemCombine> list = new ArrayList<>();
		List<DefaultIndexItem> list = new ArrayList<>();
		
		String time = DateFormatter.formatDateTime(new Date());
		java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = formatter.parse(time);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (chack.length > 0) {
			for (int i = 0; i < chack.length; i++) {
				String[] id = chack[i].split("~");
				DefaultIndexItem d1 = defaultIndexItemService.queryById(Integer.parseInt(id[0]));
				DefaultIndexItem d2 = defaultIndexItemService.queryById(Integer.parseInt(id[1]));
				 DefaultIndexItem defaultId =
				 StringUtils.isBlank(d2.getCodeCredit()) ? d1 : d2;
				 try {
				
				 DefaultIndexItem de = new DefaultIndexItem();
				 de.setDefaultIndexItemId(defaultId.getDefaultIndexItemId());
				 de.setCodeCredit(defaultId.getDefaultIndexItemId() ==
				 d1.getDefaultIndexItemId()
				 ? d1.getCodeCredit() : d2.getCodeCredit());
				 de.setCodeOrg(defaultId.getDefaultIndexItemId() ==
				 d1.getDefaultIndexItemId() ? d2.getCodeOrg()
				 : d1.getCodeOrg());
				 de.setDefaultIndexItemOldId(defaultId.getDefaultIndexItemId()
				 == d1.getDefaultIndexItemId()
				 ? d2.getDefaultIndexItemId() : d1.getDefaultIndexItemId());
				 de.setDefaultIndexItemTime(time);
				 de.setQymc(d1.getQymc());
				 defaultIndexItemService.updateDefaultIndexItem(de);
				 DefaultIndexItem orgCode =
				 !StringUtils.isBlank(d2.getCodeCredit()) ? d1 : d2;
				 orgCode.setCombine_status(0);
				 defaultIndexItemService.updateDefaultIndexItem(orgCode);
				 list.add(de);
				
				 } catch (Exception e) {
				 // TODO: handle exception
				 model.addAttribute("err", e.getMessage());
				 }
				if (!StringUtils.isBlank(d1.getCodeCredit())) {
					DefaultIndexItemCombine defaultIndexItemCombine = new DefaultIndexItemCombine();
					defaultIndexItemCombine.setCode_credit_id(d1.getDefaultIndexItemId());
					defaultIndexItemCombine.setCode_credit_orgid(d1.getOrgId());
					defaultIndexItemCombine.setCode_org_id(d2.getDefaultIndexItemId());
					defaultIndexItemCombine.setCode_org_orgid(d2.getOrgId());
					defaultIndexItemService.insertDefaultIndexItemCombine(defaultIndexItemCombine);
				} else {

					DefaultIndexItemCombine defaultIndexItemCombine = new DefaultIndexItemCombine();
					defaultIndexItemCombine.setCode_credit_id(d2.getDefaultIndexItemId());
					defaultIndexItemCombine.setCode_credit_orgid(d2.getOrgId());
					defaultIndexItemCombine.setCode_org_id(d1.getDefaultIndexItemId());
					defaultIndexItemCombine.setCode_org_orgid(d1.getOrgId());

					defaultIndexItemCombine.setSub_time(date);
					defaultIndexItemService.insertDefaultIndexItemCombine(defaultIndexItemCombine);
				}
//				DefaultIndexItemCombine dce = defaultIndexItemService.queryDefaultIndexItemCombineTop();
//				list.add(dce);
			}
			sysManageLogService.insertSysManageLogTb(new SysManageLog(SYS_MENU, null, null, null, "0", list.size() + "",
					date, SysManageLog.UPDATE_SYSMANAGElOG, null, "批量合并二码", null, null, null, null, true),request);

		}

		model.addAttribute("list", list);
		return "codeCombine/deta";

	}

	@RequestMapping("/queryCode")
	public String queryCode(String time, HttpServletRequest request, Model model) {
		Map<String, Object> map = new HashMap<>();

		PageSupport ps = PageSupport.initPageSupport(request);
		   try {
				Date nowDate = DateFormatter.formatDateTime(time);  
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("begin", sdf.format(nowDate));
				model.addAttribute("list", defaultIndexItemService.queryByTime(map, ps));
				model.addAttribute("time", time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //得到当前时间

		return "codeCombine/deta";
	}

	@RequestMapping("/yes")
	@ResponseBody
	public JsonResWrapper yes(Integer id) {

		String time = DateFormatter.formatDateTime(new Date());
		java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = formatter.parse(time);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		JsonResWrapper jrw = new JsonResWrapper();
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			List<DefaultIndexItemCombine> dede = defaultIndexItemService.queryByTime(map, null);
			if(dede!=null){
				DefaultIndexItemCombine ded=dede.get(0);
			
			DefaultIndexItem d1 = defaultIndexItemService.queryById(ded.getCode_credit_id());
			DefaultIndexItem d2 = defaultIndexItemService.queryById(ded.getCode_org_id());
			DefaultIndexItem defaultId = StringUtils.isBlank(d2.getCodeCredit()) ? d1 : d2;
			try {

				DefaultIndexItem de = new DefaultIndexItem();
				de.setDefaultIndexItemId(defaultId.getDefaultIndexItemId());
				de.setCodeCredit(defaultId.getDefaultIndexItemId() == d1.getDefaultIndexItemId() ? d1.getCodeCredit()
						: d2.getCodeCredit());
				de.setCodeOrg(defaultId.getDefaultIndexItemId() == d1.getDefaultIndexItemId() ? d2.getCodeOrg()
						: d1.getCodeOrg());
				de.setDefaultIndexItemOldId(defaultId.getDefaultIndexItemId() == d1.getDefaultIndexItemId()
						? d2.getDefaultIndexItemId() : d1.getDefaultIndexItemId());
				de.setDefaultIndexItemTime(time);
				de.setQymc(d1.getQymc());
				defaultIndexItemService.updateDefaultIndexItem(de);
				DefaultIndexItem orgCode = !StringUtils.isBlank(d2.getCodeCredit()) ? d1 : d2;
				orgCode.setCombine_status(0);
				defaultIndexItemService.updateDefaultIndexItem(orgCode);
				ded.setStuta(2);
				defaultIndexItemService.updateDefaultIndexItemCombine(ded);
				jrw.setMessage("合并成功");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				// TODO: handle exception
				jrw.setFlag(false);
				jrw.setMessage("合并失败");
			}
			}else{

				jrw.setFlag(false);
				jrw.setMessage("合并失败");
			}
		return jrw;

	}
}
