package com.workmanagement.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.workmanagement.model.ComPanyShow;
import com.workmanagement.model.DefaultIndexItemCustom;
import com.workmanagement.model.IdentiFication;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.ReportIndexError;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.model.SysRole;
import com.workmanagement.model.SysUser;
import com.workmanagement.model.SysUserLog;
import com.workmanagement.service.ComPanyShowService;
import com.workmanagement.service.DefaultIndexItemCustomService;
import com.workmanagement.service.DefaultIndexItemService;
import com.workmanagement.service.DicService;
import com.workmanagement.service.IdentiFicationService;
import com.workmanagement.service.IndexItemTbService;
import com.workmanagement.service.IndexTbService;
import com.workmanagement.service.ReportIndexService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOtherManageService;
import com.workmanagement.service.SysRoleService;
import com.workmanagement.service.SysUserLogService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.PageSupport;

/**
 * 企业名单筛选
 * 
 * @author lzl
 *
 */
@Controller
@Scope("session")
@RequestMapping("/admin/companyListFilter")
public class CompanyListFilterController {
	@Autowired
	private IndexTbService indexTbService;
	@Autowired
	private IndexItemTbService indexItemTbService;
	@Autowired
	private DefaultIndexItemService defaultIndexItemService;
	@Autowired
	private ReportIndexService reportIndexService;
	@Autowired
	private DefaultIndexItemCustomService defaultIndexItemCustomService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserLogService sysUserLogService;

	@Autowired
	private SysOtherManageService sysOtherManageService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private DicService dicService;
	private static final String SYS_MENU = "企业名单筛选";
	// 查询结果集
	private List<DefaultIndexItemCustom> defaultIndexItemCustomList;
	// 筛选条件集
	private List<ReportIndexError> conditionList;
	String querySql = "";
	String querySqlCount = "";
	String newQuerySql = null;

	@RequestMapping("/list")
	public String index(Model model) {
		MyUserDetails us = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// 所有指标大类name,code
		List<String> allIndexName = indexTbService.selectAllIndexName();
		List<String> allIndexCode = indexTbService.selectAllIndexCode();
		model.addAttribute("allIndexCode", allIndexCode);
		model.addAttribute("allIndexName", allIndexName);
		SysOtherManage s;
		try {
			s = sysOtherManageService.querySysOtherManage(us.getSys_user_id());
			model.addAttribute("pass", s.getSysSetOrgSwitch());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "companyListFilter/list";
	}

	@RequestMapping("/query")
	public String query(HttpServletRequest request, String[] tables, String[] indexItemCode) {

		String userKeyCredit = indexItemCode[0];
		String userKeyOrg = indexItemCode[1];
		// 筛选条件集
		conditionList = new ArrayList<>();
		// 筛选本机构及下级或者有职责区域的机构企业
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SysOrg so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
		StringBuffer sb = new StringBuffer();
		StringBuffer areaSb = null;
		DataUtil.getChildOrgIds(so, sb);
		if (so.getSys_area_id() != null) {
			SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
			areaSb = new StringBuffer();
			DataUtil.getChildAreaIds(sysArea, areaSb);
		}
		String[] split = sb.toString().split(",");
		String orgids = Arrays.toString(split);
		orgids = orgids.replace("]", "").replace("[", "");// orgId集
		if (areaSb != null) {
			split = areaSb.toString().split(",");
			String orgides = Arrays.toString(split);
			orgides = orgides.replace("]", "").replace("[", "");
			orgids += "," + orgides;
		}
		// 记录表单值下标,每使用一次加一
		int num = 0;
		querySql = "SELECT d.* FROM default_index_item_tb d " + "WHERE 1=1 AND ";
		// querySql = "SELECT DISTINCT d.code_credit,d.code_org,index_jbxx_qymc
		// FROM default_index_item_tb d,index_jbxx_tb j WHERE
		// j.default_index_item_id=d.default_index_item_id AND ";
		querySqlCount = "SELECT DISTINCT COUNT(0) FROM default_index_item_tb d WHERE 1=1 AND ";
		String strCode = null;
		String strName = null;
		boolean isNull = true;
		for (String string : indexItemCode) {
			if (!string.trim().equals("")) {
				isNull = false;
				break;
			}
		}
		if (isNull == true) {
			request.setAttribute("msg", "查询内容不能为空");
			return "forward:index.jhtml";
		}
		for (int i = 0; i < indexItemCode.length; i++) {
			if (i == 2) {
				break;
			}
			if (i == 0) {
				strCode = "code_credit";
				strName = "统一社会信用码";
			} else if (i == 1) {
				strCode = "code_org";
				strName = "组织机构代码";
			}
			if (!indexItemCode[num].equals("")) {
				// 是否为模糊查询
				if (indexItemCode[num].substring(0, 1).equals("*") || indexItemCode[num]
						.substring(indexItemCode[num].length() - 1, indexItemCode[num].length()).equals("*")) {
					indexItemCode[num] = indexItemCode[num].trim();
					// 前后模糊
					if (indexItemCode[num].substring(0, 1).equals("*") && indexItemCode[num]
							.substring(indexItemCode[num].length() - 1, indexItemCode[num].length()).equals("*")) {
						indexItemCode[num] = indexItemCode[num].replace("*", "");
						querySql += " " + strCode + " LIKE '%" + indexItemCode[num].toUpperCase() + "%' AND ";
						querySqlCount += " " + strCode + " LIKE '%" + indexItemCode[num].toUpperCase() + "%' AND ";
						ReportIndexError reportIndexError = new ReportIndexError();
						reportIndexError.setReportIndexErrorName(strName);
						reportIndexError.setReportIndexErrorNotes("*" + indexItemCode[num] + "*");
						conditionList.add(reportIndexError);
					}
					// 开头模糊
					else if (indexItemCode[num].substring(0, 1).equals("*")) {
						indexItemCode[num] = indexItemCode[num].replace("*", "");
						querySql += " " + strCode + " LIKE '%" + indexItemCode[num].toUpperCase() + "' AND ";
						querySqlCount += " " + strCode + " LIKE '%" + indexItemCode[num].toUpperCase() + "' AND ";
						ReportIndexError reportIndexError = new ReportIndexError();
						reportIndexError.setReportIndexErrorName(strName);
						reportIndexError.setReportIndexErrorNotes("*" + indexItemCode[num]);
						conditionList.add(reportIndexError);
					}
					// 结尾模糊
					else {
						indexItemCode[num] = indexItemCode[num].replace("*", "");
						querySql += " " + strCode + " LIKE '" + indexItemCode[num].toUpperCase() + "%' AND ";
						querySqlCount += " " + strCode + " LIKE '" + indexItemCode[num].toUpperCase() + "%' AND ";
						ReportIndexError reportIndexError = new ReportIndexError();
						reportIndexError.setReportIndexErrorName(strName);
						reportIndexError.setReportIndexErrorNotes(indexItemCode[num] + "*");
						conditionList.add(reportIndexError);
					}
				} else {
					querySql += " " + strCode + " ='" + indexItemCode[num].toUpperCase() + "' AND ";
					querySqlCount += " " + strCode + " ='" + indexItemCode[num].toUpperCase() + "' AND ";
					ReportIndexError reportIndexError = new ReportIndexError();
					reportIndexError.setReportIndexErrorName(strName);
					reportIndexError.setReportIndexErrorNotes(indexItemCode[num]);
					conditionList.add(reportIndexError);
				}
			}
			num++;
		}
		if (tables == null || tables.length < 1) {// 如果未选择指标大类
			if (querySql.lastIndexOf("AND ") != -1) {// 若机构码或信用码不为空
				querySql = querySql.substring(0, querySql.lastIndexOf("AND "));
				querySqlCount = querySqlCount.substring(0, querySqlCount.lastIndexOf("AND "));
			} else {
				querySql = "SELECT d.* FROM default_index_item_tb d " + "WHERE 1=1  AND ";
				// querySql = " SELECT distinct
				// d.code_credit,d.code_org,index_jbxx_qymc FROM
				// default_index_item_tb d,index_jbxx_tb j WHERE
				// j.default_index_item_id=d.default_index_item_id";
				querySqlCount = "SELECT distinct COUNT(0) FROM default_index_item_tb";
			}
		} else {
			querySql += " d.default_index_item_id in(select distinct default_index_item_id FROM ";
			querySqlCount += " d.default_index_item_id in(select distinct default_index_item_id FROM ";

			// 结尾括号数量
			String endStr = "";
			num = 2;
			// 指标大类集合
			for (int i = 0; i < tables.length; i++) {
				endStr += ")";
				// 当前指标大类表单对象
				IndexTb indexTb = indexTbService.getIndexTbbyIndexCode(tables[i]);
				List<IndexItemTb> indexItemTbs = new ArrayList<>();
				if (indexTb != null) {
					List<Integer> arsb = sysAreaService.getAllUpAreaIds(so.getSys_area_id());
					indexItemTbs = indexItemTbService.queryItemsByAreaIds(indexTb.getIndexId(), arsb);
				}
				querySql += tables[i] + "_tb where " + "SYS_ORG_ID in(" + orgids + ")and ";
				querySqlCount += tables[i] + "_tb where " + "SYS_ORG_ID in(" + orgids + ")and ";
				for (int j = 0; j < indexItemTbs.size(); j++) {
					// 是否为时间类型值
					if (indexItemTbs.get(j).getIndexItemType() == 1) {
						// 起始时间是否为空
						if (indexItemCode[num] != null && !indexItemCode[num].equals("")) {
							ReportIndexError reportIndexError = new ReportIndexError();
							reportIndexError.setReportIndexErrorName(indexItemTbs.get(j).getIndexItemName());
							indexItemCode[num] = indexItemCode[num].trim();
							querySql += indexItemTbs.get(j).getIndexItemCode() + " >='" + indexItemCode[num] + "' AND ";
							querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " >='" + indexItemCode[num]
									+ "' AND ";
							reportIndexError.setReportIndexErrorNotes(indexItemCode[num] + "~");
							// 结束时间是否为空
							if (indexItemCode[num + 1] != null && !indexItemCode[num + 1].equals("")) {
								indexItemCode[num + 1] = indexItemCode[num + 1].trim();
								querySql += " " + indexItemTbs.get(j).getIndexItemCode() + "<='"
										+ indexItemCode[num + 1] + "' AND ";
								querySqlCount += " " + indexItemTbs.get(j).getIndexItemCode() + "<='"
										+ indexItemCode[num + 1] + "' AND ";
								reportIndexError.setReportIndexErrorNotes(
										reportIndexError.getReportIndexErrorNotes() + indexItemCode[num + 1]);
							}
							conditionList.add(reportIndexError);
						}
						// 起始为空时，结束时间是否为空
						else if (indexItemCode[num + 1] != null && !indexItemCode[num + 1].equals("")) {
							ReportIndexError reportIndexError = new ReportIndexError();
							reportIndexError.setReportIndexErrorName(indexItemTbs.get(j).getIndexItemName());
							indexItemCode[num + 1] = indexItemCode[num + 1].trim();
							querySql += indexItemTbs.get(j).getIndexItemCode() + " <='" + indexItemCode[num + 1]
									+ "' AND ";
							querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " <='" + indexItemCode[num + 1]
									+ "' AND ";
							reportIndexError.setReportIndexErrorNotes("~" + indexItemCode[num + 1]);
							conditionList.add(reportIndexError);
						}
						if (j == indexItemTbs.size() - 1 && i == tables.length - 1) {// 所有表单最后一条
							querySql += " 1=1 " + endStr;
							querySqlCount += " 1=1 " + endStr;
						} else if (j == indexItemTbs.size() - 1) {// 当前表单最后一条
							querySql += " default_index_item_id in(select distinct default_index_item_id FROM ";
							querySqlCount += " default_index_item_id in(select distinct default_index_item_id FROM ";
						}
						num++;
					} else {
						// 表单值不为空
						if (indexItemCode[num] != null && !indexItemCode[num].equals("")) {
							indexItemCode[num] = indexItemCode[num].trim();
							if (j == indexItemTbs.size() - 1 && i == tables.length - 1) {// 所有表单最后一条
								String sqlValue = getSql(indexItemTbs.get(j).getIndexItemType(), indexItemCode[num]);
								// 记录筛选条件
								getReportIndexError(indexItemTbs.get(j).getIndexItemName(), indexItemCode[num]);

								querySql += indexItemTbs.get(j).getIndexItemCode() + sqlValue + " AND ";
								querySql += " 1=1 " + endStr;
								querySqlCount += indexItemTbs.get(j).getIndexItemCode() + sqlValue + " AND ";
								querySqlCount += " 1=1 " + endStr;
							} else if (j == indexItemTbs.size() - 1) {// 当前表单最后一条
								String sqlValue = getSql(indexItemTbs.get(j).getIndexItemType(), indexItemCode[num]);
								// 记录筛选条件
								getReportIndexError(indexItemTbs.get(j).getIndexItemName(), indexItemCode[num]);

								querySql += indexItemTbs.get(j).getIndexItemCode() + sqlValue + " AND "
										+ " default_index_item_id in(select distinct default_index_item_id FROM ";
								querySqlCount += indexItemTbs.get(j).getIndexItemCode() + sqlValue + " AND "
										+ " default_index_item_id in(select distinct default_index_item_id FROM ";
							} else {
								// 字符
								if (indexItemTbs.get(j).getIndexItemType() == 0) {
									// 是否为模糊查询
									if (indexItemCode[num].substring(0, 1).equals("*") || indexItemCode[num]
											.substring(indexItemCode[num].length() - 1, indexItemCode[num].length())
											.equals("*")) {
										// 前后模糊
										if (indexItemCode[num].substring(0, 1).equals("*") && indexItemCode[num]
												.substring(indexItemCode[num].length() - 1, indexItemCode[num].length())
												.equals("*")) {
											indexItemCode[num] = indexItemCode[num].replace("*", "");
											querySql += indexItemTbs.get(j).getIndexItemCode() + " LIKE '%"
													+ indexItemCode[num] + "%' AND ";
											querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " LIKE '%"
													+ indexItemCode[num] + "%' AND ";
											ReportIndexError reportIndexError = new ReportIndexError();
											reportIndexError
													.setReportIndexErrorName(indexItemTbs.get(j).getIndexItemName());
											reportIndexError.setReportIndexErrorNotes("*" + indexItemCode[num] + "*");
											conditionList.add(reportIndexError);
										}
										// 开头模糊
										else if (indexItemCode[num].substring(0, 1).equals("*")) {
											indexItemCode[num] = indexItemCode[num].replace("*", "");
											querySql += indexItemTbs.get(j).getIndexItemCode() + " LIKE '%"
													+ indexItemCode[num] + "' AND ";
											querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " LIKE '%"
													+ indexItemCode[num] + "' AND ";
											ReportIndexError reportIndexError = new ReportIndexError();
											reportIndexError
													.setReportIndexErrorName(indexItemTbs.get(j).getIndexItemName());
											reportIndexError.setReportIndexErrorNotes("*" + indexItemCode[num]);
											conditionList.add(reportIndexError);
										}
										// 结尾模糊
										else {
											indexItemCode[num] = indexItemCode[num].replace("*", "");
											querySql += indexItemTbs.get(j).getIndexItemCode() + " LIKE '"
													+ indexItemCode[num] + "%' AND ";
											querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " LIKE '"
													+ indexItemCode[num] + "%' AND ";
											ReportIndexError reportIndexError = new ReportIndexError();
											reportIndexError
													.setReportIndexErrorName(indexItemTbs.get(j).getIndexItemName());
											reportIndexError.setReportIndexErrorNotes(indexItemCode[num] + "*");
											conditionList.add(reportIndexError);
										}
									} else {
										if(indexItemCode[num].toLowerCase().equals("null")){
											querySql += indexItemTbs.get(j).getIndexItemCode() + " is null AND ";
											querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " is null AND ";
										}else if(indexItemCode[num].toLowerCase().equals("notnull")){

											querySql += indexItemTbs.get(j).getIndexItemCode() + " is not null AND ";
											querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " is not null AND ";
										}else{
											querySql += indexItemTbs.get(j).getIndexItemCode() + " ='" + indexItemCode[num]
													+ "' AND ";
											querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " ='"
													+ indexItemCode[num] + "' AND ";
										}
										ReportIndexError reportIndexError = new ReportIndexError();
										reportIndexError
												.setReportIndexErrorName(indexItemTbs.get(j).getIndexItemName());
										reportIndexError.setReportIndexErrorNotes(indexItemCode[num]);
										conditionList.add(reportIndexError);
									}
								}
								// 数值
								else if (indexItemTbs.get(j).getIndexItemType() == 2) {
									// 是否为大小于查询
									if (indexItemCode[num].indexOf("-") != -1) {
										// 区间
										if (!indexItemCode[num].substring(0, 1).equals("-")
												&& !indexItemCode[num].substring(indexItemCode[num].length() - 1,
														indexItemCode[num].length()).equals("-")
												&& indexItemCode[num].indexOf("-") != -1) {
											String numOne = indexItemCode[num].substring(0,
													indexItemCode[num].indexOf("-"));
											try {
												isDouble(numOne);
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
												request.setAttribute("msg", "输入了错误的数据:" + numOne + " 请输入数值类型");
												return "forward:index.jhtml";
											}
											String numTwo = indexItemCode[num]
													.substring(indexItemCode[num].indexOf("-") + 1);
											try {
												isDouble(numTwo);
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
												request.setAttribute("msg", "输入了错误的数据:" + numTwo + " 请输入数值类型");
												return "redirect:forward.jhtml";
											}
											querySql += indexItemTbs.get(j).getIndexItemCode() + " >" + numOne
													+ " AND ";
											querySql += indexItemTbs.get(j).getIndexItemCode() + " <" + numTwo
													+ " AND ";
											querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " >" + numOne
													+ " AND ";
											querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " <" + numTwo
													+ " AND ";
											ReportIndexError reportIndexError = new ReportIndexError();
											reportIndexError
													.setReportIndexErrorName(indexItemTbs.get(j).getIndexItemName());
											reportIndexError.setReportIndexErrorNotes("大于" + numOne + ",小于" + numTwo);
											conditionList.add(reportIndexError);
										}
										// 小于
										else if (indexItemCode[num].substring(0, 1).equals("-")) {
											String numTwo = indexItemCode[num]
													.substring(indexItemCode[num].indexOf("-") + 1);
											try {
												isDouble(numTwo);
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
												request.setAttribute("msg", "输入了错误的数据:" + numTwo + " 请输入数值类型");
												return "forward:index.jhtml";
											}
											querySql += indexItemTbs.get(j).getIndexItemCode() + " <" + numTwo
													+ " AND ";
											querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " <" + numTwo
													+ " AND ";
											ReportIndexError reportIndexError = new ReportIndexError();
											reportIndexError
													.setReportIndexErrorName(indexItemTbs.get(j).getIndexItemName());
											reportIndexError.setReportIndexErrorNotes("小于" + numTwo);
											conditionList.add(reportIndexError);
										}
										// 大于
										else {
											String numOne = indexItemCode[num].substring(0,
													indexItemCode[num].indexOf("-"));
											try {
												isDouble(numOne);
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
												request.setAttribute("msg", "输入了错误的数据:" + numOne + " 请输入数值类型");
												return "forward:index.jhtml";
											}
											querySql += indexItemTbs.get(j).getIndexItemCode() + " >" + numOne
													+ " AND ";
											querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " >" + numOne
													+ " AND ";
											ReportIndexError reportIndexError = new ReportIndexError();
											reportIndexError
													.setReportIndexErrorName(indexItemTbs.get(j).getIndexItemName());
											reportIndexError.setReportIndexErrorNotes("大于" + numOne);
											conditionList.add(reportIndexError);
										}
									} else {
										try {
											isDouble(indexItemCode[num]);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											request.setAttribute("msg", "输入了错误的数据:" + indexItemCode[num] + " 请输入数值类型");
											return "forward:index.jhtml";
										}
										querySql += indexItemTbs.get(j).getIndexItemCode() + " =" + indexItemCode[num]
												+ " AND ";
										querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " ="
												+ indexItemCode[num] + " AND ";
										ReportIndexError reportIndexError = new ReportIndexError();
										reportIndexError
												.setReportIndexErrorName(indexItemTbs.get(j).getIndexItemName());
										reportIndexError.setReportIndexErrorNotes(indexItemCode[num]);
										conditionList.add(reportIndexError);
									}
								} else {
									querySql += indexItemTbs.get(j).getIndexItemCode() + " ='" + indexItemCode[num]
											+ "' AND ";
									querySqlCount += indexItemTbs.get(j).getIndexItemCode() + " ='" + indexItemCode[num]
											+ "' AND ";
									ReportIndexError reportIndexError = new ReportIndexError();
									reportIndexError.setReportIndexErrorName(indexItemTbs.get(j).getIndexItemName());
									reportIndexError.setReportIndexErrorNotes(indexItemCode[num]);
									conditionList.add(reportIndexError);
								}
							}
						} else {
							if (j == indexItemTbs.size() - 1 && i == tables.length - 1) {// 所有表单最后一条
								querySql += " 1=1 " + endStr;
								querySqlCount += " 1=1 " + endStr;
							} else if (j == indexItemTbs.size() - 1) {// 当前表单最后一条
								querySql += " default_index_item_id in(select distinct default_index_item_id FROM ";
								querySqlCount += " default_index_item_id in(select distinct default_index_item_id FROM ";
							}
						}
					}
					num++;
				}
			}
		}
		// 统一社会信用码 标记
		request.setAttribute("userKeyCredit", userKeyCredit);
		// 组织机构代码 标记
		request.setAttribute("userKeyOrg", userKeyOrg);

		// 操作对象

		String log_object = "筛选条件(";
		for (int k = 0; k < conditionList.size(); k++) {
			if (k == conditionList.size() - 1) {
				log_object += conditionList.get(k).getReportIndexErrorName() + ":"
						+ conditionList.get(k).getReportIndexErrorNotes() + ")";
			}
			log_object += conditionList.get(k).getReportIndexErrorName() + ":"
					+ conditionList.get(k).getReportIndexErrorNotes() + ",";
		}

		return "forward:result.jhtml";
	}

	private void isDouble(String num) throws Exception {
		try {
			Double.parseDouble(num);
		} catch (Exception e) {
			throw new Exception("输入数值类型时含非法字符，数值只能为数值或者数值与中划线的组合且中划线只能有一个");
		}
	}

	@RequestMapping("/copyresult")
	public String copyresult(Model model, HttpServletRequest request, HttpSession session) {

		PageSupport ps = PageSupport.initPageSupport(request);
		Object objSql = request.getAttribute("querySql");
		String querySql = null;
		if (objSql == null) {
			querySql = String.valueOf(session.getAttribute("sysUserBehaviorAudit_sql"));
		} else {
			querySql = String.valueOf(objSql);
			session.setAttribute("sysUserBehaviorAudit_sql", querySql);
		}
		String sql = StringUtils.replace(querySql, "|", " ");
		Map<String, Object> map = new HashMap<>();
		map.put("sql", sql);
		defaultIndexItemCustomList = defaultIndexItemCustomService.queryDefaultIndexItemCustoms(map, ps);

		request.setAttribute("defaultIndexItemCustomList", defaultIndexItemCustomList);
		request.setAttribute("conditionList", conditionList);

		return "companyListFilter/copyresult";
	}

	@Autowired
	private SysRoleService sysRoleService;

	@RequestMapping("/result")
	public String result(HttpServletRequest request) throws Exception {
		MyUserDetails us = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		SysUser su = sysUserService.querySystemUserById(us.getSys_user_id());
		SysOrg so = sysOrgService.getByIdNotHaveSub(su.getSys_org_id());
		try {
			// 查询所有匹配数据
			Map<String, Object> map = new HashMap<>();
			String sqlOne = "SELECT * FROM  (" + querySql.substring(0, querySql.indexOf("FROM d") - 1);
			sqlOne += ", ROW_NUMBER() OVER(ORDER BY d.default_index_item_id DESC ) AS cpbid ";

			String sqlTwo = "";

			SysRole sr = sysRoleService.querySystemRoleById(us.getRoleIds().get(0));
			boolean isPepole = (sr.getSys_role_type() == SysRole.PEPOLE_ADMIN
					|| sr.getSys_role_type() == SysRole.PEPOLE_QUERY || sr.getSys_role_type() == SysRole.PEPOLE_REPORT);

			if (isPepole) {
			
				List<Integer> arers = sysAreaService.getAllSubAreaIds(so.getSys_area_id());
				String sb = "";
				for (Integer integer : arers) {
					sb += (integer + ",");
				}
				sb = sb.substring(0, sb.length() - 1);
				sqlTwo = querySql.substring(querySql.indexOf("FROM d") - 1) + " and d.sys_area_id in(" + sb
						+ ")) AS a ";
			} else {
				sqlTwo = querySql.substring(querySql.indexOf("FROM d") - 1) + " and d.sys_area_id =" + so.getSys_org_affiliation_area_id()
						+ ") AS a ";
			}

			newQuerySql = sqlOne + sqlTwo;
			map.put("queryTemporarySql", newQuerySql);
			// 结果数据集
			PageSupport ps = PageSupport.initPageSupport(request);

			defaultIndexItemCustomList = defaultIndexItemCustomService.queryDefaultIndexItemCustoms(map, ps);

			for (DefaultIndexItemCustom comPanyShow : defaultIndexItemCustomList) {
				Map<String, Object> qyzs = new HashMap<String, Object>();
				qyzs = comPanyShowService.querySql("SELECT index_jbxx_qyzs FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_JBXX_QYZS FROM INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
				if(qyzs!=null)
					comPanyShow.setQyzs(qyzs.get("INDEX_JBXX_QYZS").toString());
				Map<String, Object> ggxm =new HashMap<String, Object>();
				ggxm =comPanyShowService.querySql("SELECT INDEX_GGXX_XM FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_GGXX_XM FROM INDEX_GGXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
				if(ggxm!=null)
					comPanyShow.setFddbr(ggxm.get("INDEX_GGXX_XM").toString());
				Map<String, Object> lxdh =new HashMap<String, Object>();
				lxdh =comPanyShowService.querySql("SELECT INDEX_JBXX_LXDH FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_JBXX_LXDH FROM INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
				if(lxdh!=null)
					comPanyShow.setLxdh(lxdh.get("INDEX_JBXX_LXDH").toString());
			}
			request.setAttribute("defaultIndexItemCustomList", defaultIndexItemCustomList);
			request.setAttribute("conditionList", conditionList);
			StringBuffer sb = new StringBuffer();
			for (ReportIndexError reportIndexError : conditionList) {
				sb.append(reportIndexError.getReportIndexErrorName() + ":" + reportIndexError.getReportIndexErrorNotes()
						+ ",");
			}

			sysUserLogService
					.insertOneLog(new SysUserLog(SYS_MENU, null, null, null, null, null, SysUserLogService.SELECT,
							defaultIndexItemCustomList == null ? 0 : defaultIndexItemCustomList.size(),
							sb.toString().substring(0, sb.length() - 1), newQuerySql,
							"/admin/companyListFilter/copyresult.jhtml", null, null, true),request);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return "companyListFilter/result";
	}

	/**
	 * 导出筛选结果
	 * 
	 * @param reportIndexId
	 */
	@RequestMapping(value = "/downLoadConditionList")
	public void downLoadConditionList(HttpServletRequest request, HttpServletResponse response) {
		// 生成
		ExcelExport<DefaultIndexItemCustom> excelExport = new ExcelExport<>();
		excelExport.setTitle("企业名单筛选结果");
		excelExport.setRowNames(new String[] { "统一社会信用代码", "组织机构代码", "企业名称", "联系电话", "法定代表人" });
		excelExport.setPropertyNames(new String[] { "codeCredit", "codeOrg", "qymc", "lxdh", "fddbr" });
		Map<String, Object> map = new HashMap<>();
		map.put("queryTemporarySql", newQuerySql);
		map.put("queryTemporarySql_count", querySqlCount);
		// 结果数据集(所有结果 不分页)
		List<DefaultIndexItemCustom> myDefaultIndexItemCustomList = defaultIndexItemCustomService
				.queryDefaultIndexItemCustoms(map, null);
		for (DefaultIndexItemCustom comPanyShow : myDefaultIndexItemCustomList) {
			Map<String, Object> qyzs = new HashMap<String, Object>();
			qyzs = comPanyShowService.querySql("SELECT index_jbxx_qyzs FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_JBXX_QYZS FROM INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
			if(qyzs!=null)
				comPanyShow.setQyzs(qyzs.get("INDEX_JBXX_QYZS").toString());
			Map<String, Object> ggxm =new HashMap<String, Object>();
			ggxm =comPanyShowService.querySql("SELECT INDEX_GGXX_XM FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_GGXX_XM FROM INDEX_GGXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
			if(ggxm!=null)
				comPanyShow.setFddbr(ggxm.get("INDEX_GGXX_XM").toString());
			Map<String, Object> lxdh =new HashMap<String, Object>();
			lxdh =comPanyShowService.querySql("SELECT INDEX_JBXX_LXDH FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_JBXX_LXDH FROM INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
			if(lxdh!=null)
				comPanyShow.setLxdh(lxdh.get("INDEX_JBXX_LXDH").toString());
		}
		excelExport.setList(myDefaultIndexItemCustomList);
		String url = excelExport.exportExcel(request, response);
		try {
			sysUserLogService.insertOneLog(new SysUserLog(SYS_MENU, null, null, null, null, null,
					SysUserLogService.EXPORT, myDefaultIndexItemCustomList.size(), null, null, null, url, null, true),request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<DefaultIndexItemCustom> getDefaultIndexItemCustomList() {
		return defaultIndexItemCustomList;
	}

	public void setDefaultIndexItemCustomList(List<DefaultIndexItemCustom> defaultIndexItemCustomList) {
		this.defaultIndexItemCustomList = defaultIndexItemCustomList;
	}

	/**
	 * 组成筛选结果对象
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	private ReportIndexError getReportIndexError(String name, String value) {
		String notes = "";
		// 是否为模糊查询
		if (value.substring(0, 1).equals("*") || value.substring(value.length() - 1, value.length()).equals("*")) {
			// 前后模糊
			if (value.substring(0, 1).equals("*") && value.substring(value.length() - 1, value.length()).equals("*")) {
				notes = value + "(前后模糊查询)";
			}
			// 开头模糊
			else if (value.substring(0, 1).equals("*")) {
				notes = value + "(前模糊查询)";
			}
			// 结尾模糊
			else {
				notes = value + "(后模糊查询)";
			}
		}
		// 是否为大小于查询
		else if (value.indexOf("-") != -1) {
			// 区间
			if (value.substring(0, 1).equals("-") && value.substring(value.length() - 1, value.length()).equals("-")) {
				String numOne = value.substring(0, value.indexOf("-"));
				String numTwo = value.substring(value.indexOf("-") + 1);
				notes = "大于" + numOne + ",小于" + numTwo;
			}
			// 小于
			else if (value.substring(0, 1).equals("-")) {
				String numTwo = value.substring(value.indexOf("-") + 1);
				notes = "小于" + numTwo;
			}
			// 大于
			else {
				String numOne = value.substring(0, value.indexOf("-"));
				notes = "大于" + numOne;
			}
		} else {
			notes = value;
		}
		ReportIndexError reportIndexError = new ReportIndexError();
		reportIndexError.setReportIndexErrorName(name);
		reportIndexError.setReportIndexErrorNotes(notes);
		conditionList.add(reportIndexError);
		return reportIndexError;
	}

	/**
	 * 得到经过重新组合的sql
	 * 
	 * @param type
	 *            类型 0字符 2数值
	 * @param value
	 *            值
	 * @return
	 */
	private String getSql(int type, String value) {
		String sqlValue = "";
		// 字符
		if (type == 0) {
			// 是否为模糊查询
			if (value.substring(0, 1).equals("*") || value.substring(value.length() - 1, value.length()).equals("*")) {
				// 前后模糊
				if (value.substring(0, 1).equals("*")
						&& value.substring(value.length() - 1, value.length()).equals("*")) {
					value = value.replace("*", "");
					sqlValue = " LIKE '%" + value + "%' ";
					sqlValue = " LIKE '%" + value + "%' ";
				}
				// 开头模糊
				else if (value.substring(0, 1).equals("*")) {
					value = value.replace("*", "");
					sqlValue = " LIKE '%" + value + "' ";
					sqlValue = " LIKE '%" + value + "' ";
				}
				// 结尾模糊
				else {
					value = value.replace("*", "");
					sqlValue = " LIKE '" + value + "%' ";
					sqlValue = " LIKE '" + value + "%' ";
				}
			} else {
				sqlValue = " ='" + value + "' ";
				sqlValue = " ='" + value + "' ";
			}
		}
		// 数值
		else if (type == 2) {
			// 是否为大小于查询
			if (value.indexOf("-") != -1) {
				// 区间
				if (value.substring(0, 1).equals("-")
						&& value.substring(value.length() - 1, value.length()).equals("-")) {
					String numOne = value.substring(0, value.indexOf("-"));
					String numTwo = value.substring(value.indexOf("-") + 1);
					sqlValue = " >=" + numOne + " ";
					sqlValue = " <=" + numTwo + " ";
					sqlValue = " >=" + numOne + " ";
					sqlValue = " <=" + numTwo + " ";
				}
				// 小于
				else if (value.substring(0, 1).equals("-")) {
					String numTwo = value.substring(value.indexOf("-") + 1);
					sqlValue = " <" + numTwo + " ";
					sqlValue = " <" + numTwo + " ";
				}
				// 大于
				else {
					String numOne = value.substring(0, value.indexOf("-"));
					sqlValue = " >" + numOne + " ";
					sqlValue = " >" + numOne + " ";
				}
			} else {
				sqlValue = " =" + value + " ";
				sqlValue = " =" + value + " ";
			}
		} else {
			sqlValue = " =" + value + " ";
			sqlValue = " =" + value + " ";
		}
		return sqlValue;
	}
	
	@Autowired
	IdentiFicationService identiFicationService;
@Autowired
ComPanyShowService comPanyShowService;
	@RequestMapping("/getQyxx")
	public String getQyxx(String check, HttpServletRequest request, Model model,Integer id) throws Exception {
		if(!StringUtils.isBlank(check))
			check = check.substring(0, check.length()-1);
		Map<String, Object> map = new HashMap<>();
		if(id==null){
		if (!StringUtils.isBlank(check) &&id==null) {
			String a = "SELECT  d.* FROM default_index_item_tb d WHERE d.DEFAULT_INDEX_ITEM_ID in("+check+")";
			map.put("queryTemporarySql", a);
			defaultIndexItemCustomList = defaultIndexItemCustomService.queryDefaultIndexItemCustoms(map, null);
		}
		}else if(id==1){
			if(!StringUtils.isBlank(check)){
				map.put("queryTemporarySql", newQuerySql+" where DEFAULT_INDEX_ITEM_ID not in("+check+")");
				defaultIndexItemCustomList  = defaultIndexItemCustomService.queryDefaultIndexItemCustoms(map, null);
			}else{
				map.put("queryTemporarySql", newQuerySql);
				defaultIndexItemCustomList  = defaultIndexItemCustomService.queryDefaultIndexItemCustoms(map, null);
			
			}
		}

		for (DefaultIndexItemCustom comPanyShow : defaultIndexItemCustomList) {
			Map<String, Object> qyzs = new HashMap<String, Object>();
			qyzs = comPanyShowService.querySql("SELECT index_jbxx_qyzs FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_JBXX_QYZS FROM INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
			if(qyzs!=null)
				comPanyShow.setQyzs(qyzs.get("INDEX_JBXX_QYZS").toString());
			Map<String, Object> ggxm =new HashMap<String, Object>();
			ggxm =comPanyShowService.querySql("SELECT INDEX_GGXX_XM FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_GGXX_XM FROM INDEX_GGXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
			if(ggxm!=null)
				comPanyShow.setFddbr(ggxm.get("INDEX_GGXX_XM").toString());
			Map<String, Object> lxdh =new HashMap<String, Object>();
			lxdh =comPanyShowService.querySql("SELECT INDEX_JBXX_LXDH FROM (SELECT ROW_NUMBER()OVER(ORDER BY RECORD_DATE desc) AS c,INDEX_JBXX_LXDH FROM INDEX_JBXX_TB WHERE DEFAULT_INDEX_ITEM_ID ="+comPanyShow.getDefaultItemId()+") AS a WHERE a.c=1");
			if(lxdh!=null)
				comPanyShow.setLxdh(lxdh.get("INDEX_JBXX_LXDH").toString());
		}

		model.addAttribute("defaultIndexItemCustomList", defaultIndexItemCustomList);

			// 当前登录用户的session
			MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			SysOrg so = sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
			SysArea sy = null;
			if(so.getSys_org_affiliation_area_id()!=null)
			sy = sysAreaService
					.queryAreaById(so.getSys_org_affiliation_area_id());
			else
				sy = sysAreaService
				.queryAreaById(so.getSys_area_id());
		
			
			model.addAttribute("area",sy);
			map.put("orgId", userDetails.getSys_org_id().toString());
			List<IdentiFication> iden = identiFicationService.queryIdentiFicationByAll(map, null);
			model.addAttribute("iden",iden);
		
		return "companyListFilter/resultbs";
	}
	
}
