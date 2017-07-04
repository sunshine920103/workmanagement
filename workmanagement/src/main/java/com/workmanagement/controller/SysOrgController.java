package com.workmanagement.controller;

import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.*;
import com.workmanagement.service.*;
import com.workmanagement.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 机构管理
 *
 * @author renyang
 */
@Controller
@RequestMapping("/admin/sysOrg")
public class SysOrgController {
    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private SysOrgTypeService sysOrgTypeService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private SysManageLogService sysManageLogService;
    @Autowired
    private DefaultIndexItemService defaultIndexItemService;

    /**
     * 导出EXCEL模板
     *
     * @param model
     * @param id    机构ID
     * @return
     */
    @RequestMapping("/exportModel")
    @ResponseBody
    public void downLoadFileModel(HttpServletResponse response, HttpServletRequest request) {
        SysOrg so = null;
        String[] rowNames = {"机构名称", "上级机构名称", "机构编码", "机构类别名称", "机构所在地", "电话", "地址", "法定代表人/负责人", "机构经办人", "经办人联系电话", "组织机构代码", "统一社会信用代码", "许可证", "注册资本", "实际开立日期", "代码证编号", "登记号", "备注", "服务中心电话", "服务中心名称"};
        String[] propertyNames = {"sys_org_name", "sys_org_upname", "sys_org_financial_code", "sys_org_type_name", "sys_org_address_area_name", "sys_org_phone", "sys_org_address", "sys_org_representative", "sys_org_finance_operator", "sys_org_finance_operator_phone", "sys_org_code", "sys_org_credit_code", "sys_org_service_center_call", "sys_org_service_center_name"};
        // 生成excel
        ExcelExport<SysOrg> excelExport = new ExcelExport<>();
        excelExport.setTitle("机构管理模板");
        excelExport.setRowNames(rowNames);
        excelExport.setPropertyNames(propertyNames);
        excelExport.setObj(so);
        //excelExport.exportObjctExcel(response);
        String url = excelExport.exportExcel(request, response);
        SysManageLog sysManageLog = new SysManageLog();
        sysManageLog.setSysManageLogMenuName("机构管理");
        sysManageLog.setSysManageLogFile(url);
        sysManageLog.setSysManageLogOperateType(6);
        sysManageLog.setSysManageLogResult(true);
        sysManageLogService.insertSysManageLogTb(sysManageLog, request);
    }

    /**
     * 导入数据
     *
     * @param model
     * @param
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/excelReader")
    public String upload(MultipartFile file, Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<SysOrg> sList = new ArrayList<SysOrg>();
        ExcelReaderOrg<SysOrg> excelReader = new ExcelReaderOrg<SysOrg>();
        excelReader.setT(new SysOrg());
        String[] rowNames = {"机构名称", "上级机构名称", "机构编码", "机构类别名称", "机构所在地", "电话", "地址", "法定代表人/负责人", "机构经办人", "经办人联系电话", "组织机构代码", "统一社会信用代码", "许可证", "注册资本", "实际开立日期", "代码证编号", "登记号", "备注", "机构类型", "服务中心电话", "服务中心名称"};
        String[] propertyNames = {"sys_org_name", "sys_org_upname", "sys_org_financial_code", "sys_org_type_name", "sys_org_address_area_name", "sys_org_phone", "sys_org_address", "sys_org_representative", "sys_org_finance_operator", "sys_org_finance_operator_phone", "sys_org_code", "sys_org_credit_code", "sys_org_licence", "sys_org_reg_capital", "sys_org_issuance_day", "sys_org_code_number", "sys_org_reg_number", "sys_org_notes", "sys_org_typeName", "sys_org_service_center_call", "sys_org_service_center_name"};
        excelReader.setRowNames(rowNames);
        excelReader.setPropertyNames(propertyNames);
        String fileName = UpLoadFile.upLoadFile(file);
        List<SysOrg> list = excelReader.excelReader(request, fileName);
        List<String> msgString = new ArrayList<String>();
        //如果list有值 便利其中的值 并判断
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                SysOrg sysorg = new SysOrg();
                String name = list.get(i).getSys_org_name();
                String code = list.get(i).getSys_org_financial_code();
                String upname = list.get(i).getSys_org_upname();
                String typename = list.get(i).getSys_org_type_name();
                String areaname = list.get(i).getSys_org_address_area_name();
                String phone = list.get(i).getSys_org_phone();
                String address = list.get(i).getSys_org_address();
                String representative = list.get(i).getSys_org_representative();
                String operator = list.get(i).getSys_org_finance_operator();
                String operatorPhone = list.get(i).getSys_org_finance_operator_phone();
                String orgCode = list.get(i).getSys_org_code();
                String creditCode = list.get(i).getSys_org_credit_code();
                String licence = list.get(i).getSys_org_licence();
                String capital = list.get(i).getSys_org_reg_capital();
                Date issuanceDay = list.get(i).getSys_org_issuance_day();
                String codeNumber = list.get(i).getSys_org_code_number();
                String number = list.get(i).getSys_org_reg_number();
                String notes = list.get(i).getSys_org_notes();
                String typeName = list.get(i).getSys_org_typeName();
                String call = list.get(i).getSys_org_service_center_call();
                String centerName = list.get(i).getSys_org_service_center_name();
                if (name != null && code != null) {//机构名称和代码不能为空
                    List<SysOrg> so = sysOrgService.queryInstitutionsByCodeAndName(code, name);//添加时code和name唯一
                    if (so.isEmpty() && so.size() == 0) {//为空时，才添加
                        sysorg.setSys_org_name(name);
                        sysorg.setSys_org_financial_code(code.toUpperCase());
                        sysorg.setSys_org_phone(phone);
                        sysorg.setSys_org_address(address);
                        sysorg.setSys_org_representative(representative);
                        sysorg.setSys_org_finance_operator(operator);
                        sysorg.setSys_org_finance_operator_phone(operatorPhone);
                        sysorg.setSys_org_code(orgCode);
                        sysorg.setSys_org_credit_code(creditCode);
                        sysorg.setSys_org_licence(licence);
                        sysorg.setSys_org_reg_capital(capital);
                        sysorg.setSys_org_issuance_day(issuanceDay);
                        sysorg.setSys_org_code_number(codeNumber);
                        sysorg.setSys_org_reg_number(number);
                        sysorg.setSys_org_notes(notes);
                        sysorg.setSys_org_service_center_call(call);
                        sysorg.setSys_org_service_center_name(centerName);
                        sysorg.setSys_org_current_query_times(0);
                        sysorg.setSys_org_current_limit_query_times(0);
                        sysorg.setSys_org_status(1);
                        Date date = new Date();
                        date.setDate(1);
                        sysorg.setSys_org_time(date);
                        if ("".equals(areaname)) {//地区名称不能为空，为必填项
                            msgString.add("第" + (i + 2) + "行地区不能为空 ");
                        }
                        List<SysArea> sys = sysAreaService.queryAreaByNameAndCode(areaname, null);
                        if (sys == null || sys.size() <= 0) {
                            msgString.add("第" + (i + 2) + "行地区不存在");
                        } else {
                            for (int j = 0; j < sys.size(); j++) {
                                sysorg.setSys_area_id((sys.get(j).getSysAreaId()));
                                SysArea area = sysAreaService.getUpOrThisSysArea((sys.get(j).getSysAreaId()));
                                if ("0".equals(area.getSysAreaType())) {
                                    sysorg.setSys_org_affiliation_area_id(null);
                                } else {
                                    sysorg.setSys_org_affiliation_area_id(area.getSysAreaId());
                                }

                            }
                        }
                        if ("".equals(typename)) {//机构类别必填项 不能为空
                            msgString.add("第" + (i + 2) + "行机构类别名称不能为空");
                        }
                        Integer typeid = sysOrgTypeService.queryInstitutionTypeIdByName(typename);
                        if (typeid == null) {
                            msgString.add("第" + (i + 2) + "行机构类别名称不存在");
                        }
                        if (typeid != null) {
                            SysOrgType sysOrgType = sysOrgTypeService.queryInstitutionsTypeById(typeid);
                            if (sysOrgType.getSys_org_type_type() == 0) {
                                sysorg.setSys_org_type(0);
                                sysorg.setSys_dic_id(83);
                            }
                            if (sysOrgType.getSys_org_type_type() == 1) {
                                sysorg.setSys_org_type(1);
                                sysorg.setSys_dic_id(83);
                            }
                            if (!"".equals(upname)) {
                                Integer upid = sysOrgService.queryInstitutionIdByName(upname);
                                if (upid == null) {
                                    msgString.add("第" + (i + 2) + "行上级机构不存在");
                                } else {
                                    SysOrg parentOrg = sysOrgService.queryInstitutionsById(upid);//通过上级id得到机构
                                    if (!parentOrg.getSys_org_type().equals(sysOrgType.getSys_org_type_type())) {
                                        msgString.add("第" + (i + 2) + "行机构和上级机构类型不一致");
                                    }
                                    String parentcode = parentOrg.getSys_org_financial_code().substring(0, 6);
                                    if (code.length() != 14) {
                                        msgString.add("第" + (i + 2) + "行机构编码必须为14位");
                                    }
                                    if (code.length() == 14) {
                                        String subcode = code.substring(0, 6).toUpperCase();
                                        if (!subcode.equals(parentcode)) {
                                            msgString.add("第" + (i + 2) + "行机构编码和上级机构编码前6位必须相同");
                                        }
                                    }
                                    SysOrg sysOrg = sysOrgService.queryInstitutionsById(upid);
                                    Integer typeId = sysOrg.getSys_org_type_id();
                                    if (!(typeid.toString()).equals(typeId.toString())) {
                                        msgString.add("第" + (i + 2) + "行机构类别名称和上级机构类别名称不一致");
                                    }
                                    sysorg.setSys_org_upid(upid);
                                    sysorg.setSys_org_type_id(typeid);
                                }
                            }
                            if ("".equals(upname)) {
                                sysorg.setSys_org_upid(null);
                                sysorg.setSys_org_type_id(typeid);
                                    /*String financialcode=code.toUpperCase();
                                    SysOrgType sysOrgType=sysOrgTypeService.queryInstitutionsTypeById(typeid);
									String typecode=sysOrgType.getSys_org_type_code();
									if(financialcode.length()<typecode.length()){
										msgString.add("第"+(i+2)+"行机构编码位数少于机构类别编码");
									}
									if(financialcode.length()>=typecode.length()){
										if(!typecode.equals(financialcode.substring(0,typecode.length()))){
											msgString.add("第"+(i+2)+"行机构编码和机构类别编码前"+typecode.length()+"位必须一致");
										}
									}*/

                            }
                        }

                    } else {
                        msgString.add("第" + (i + 2) + "行机构名称或机构编码已存在");
                    }
                } else {
                    msgString.add("第" + (i + 2) + "行机构名称或机构编码为空");
                }
                sList.add(sysorg);//将能够存储的对象装到sList中
            }
            //查询表中的重复数据，如果存在不能导入数据库
            for (int j = 0; j < list.size(); j++) {
                for (int j2 = j + 1; j2 < list.size(); j2++) {
                    if (list.get(j).getSys_org_name().equals(list.get(j2).getSys_org_name()) || list.get(j).getSys_org_financial_code().toUpperCase().equals(list.get(j2).getSys_org_financial_code().toUpperCase())) {
                        msgString.add("第" + (j + 2) + "行数据和第" + (j2 + 2) + "行数据重复，请确认后添加");
                    }
                }
            }
            if (msgString.size() > 0) {
                model.addAttribute("msgString", msgString);
                return "forward:list.jhtml";
            }
            //遍历sList容器 同意存入数据库中
            Set<Integer> set = new HashSet<Integer>();
            for (int j = 0; j < sList.size(); j++) {
                sysOrgService.saveInstitutions(sList.get(j));
                set.add(sList.get(j).getSys_org_type_id());
            }
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("typeIds", set);
            List<SysOrgType> typeList = sysOrgTypeService.queryTypeList(param);
            for (int j = 0; j < typeList.size(); j++) {
                SysOrgType orgType = typeList.get(j);
                orgType.setSys_org_type_used(true);
                sysOrgTypeService.saveInstitutionsType(orgType);
            }
            model.addAttribute("msg", "导入成功");
//					String originalFilename = file.getOriginalFilename();
//					String savePath = SettingUtils.getCommonSetting("upload.file.temp.path");
//					File myfile = new File(savePath);
//					if (!myfile.exists()) {
//						myfile.mkdirs();
//					}
//					// 新的名称
//					String newFileName = "/" + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            SysManageLog sysManageLog = new SysManageLog();
            sysManageLog.setSysManageLogMenuName("机构管理");
            sysManageLog.setSysManageLogCount(sList.size());
            sysManageLog.setSysManageLogFile(fileName);
            sysManageLog.setSysManageLogOperateType(5);
            sysManageLog.setSysManageLogResult(true);
            sysManageLogService.insertSysManageLogTb(sysManageLog, request);
        }
        model.addAttribute("msgString", msgString);
        return "forward:list.jhtml";
    }

    @RequestMapping("/getType")
    @ResponseBody
    public void getType(@RequestParam(required = false) String name, HttpServletResponse response) throws IOException {
        Integer typeId = sysOrgTypeService.queryInstitutionTypeIdByName(name);
        Integer type = sysOrgTypeService.queryInstitutionsTypeById(typeId).getSys_org_type_type();

        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(type));
    }

    /**
     * 通过机构名称查找机构代码
     *
     * @param name
     * @return
     */
    @RequestMapping("/getcode")
    @ResponseBody
    public void getcode(@RequestParam(required = false) String name, HttpServletResponse response) throws IOException {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> param = new HashMap<String, Object>();
        //缓存单个机构
        String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
        SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
        if (so == null) {
            so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
            RedisUtil.setData(orgKey, so);
        }
        if (so.getSys_area_id() != null) {
            //获取地区缓存
            StringBuffer sb = new StringBuffer();
            SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
            DataUtil.getChildAreaIds(sysArea, sb);
            param.put("area_id", sb.toString().split(","));//区域id
            param.put("upid", true);
        }
        Integer typeId = sysOrgTypeService.queryInstitutionTypeIdByName(name);
        param.put("typeId", typeId);
        List<SysOrg> sysOrgList = sysOrgService.queryInstitution(param);
        for (int i = 0; i < sysOrgList.size(); i++) {
            for (int j = i + 1; j < sysOrgList.size(); j++) {
                StringBuffer sbff = new StringBuffer();
                StringBuffer sbff2 = new StringBuffer();
                List<Integer> ids = new ArrayList<Integer>();
                List<Integer> ids2 = new ArrayList<Integer>();
                DataUtil.getChildOrgIds(sysOrgList.get(j), sbff);
                DataUtil.getChildOrgIds(sysOrgList.get(i), sbff2);
                String[] s = sbff.toString().split(",");
                String[] s2 = sbff2.toString().split(",");
                for (int k = 0; k < s.length; k++) {
                    ids.add(Integer.parseInt(s[k]));
                }
                if (ids.contains(sysOrgList.get(i).getSys_org_id())) {
                    sysOrgList.remove(i);
                    i--;
                    break;
                }
                for (int l = 0; l < s2.length; l++) {
                    ids2.add(Integer.parseInt(s2[l]));
                }
                if (ids2.contains(sysOrgList.get(j).getSys_org_id())) {
                    sysOrgList.remove(j);
                    j--;
                }

            }
        }
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(sysOrgList));
    }

    /**
     * 导出全部数据EXCEL
     *
     * @param model
     * @param
     * @return
     * @throws ParseException
     */
    @RequestMapping("/exportAll")
    @ResponseBody
    public void downFile(HttpServletResponse response, HttpServletRequest request,
                         @RequestParam(required = false) String orgName,
                         @RequestParam(required = false) String[] num1,
                         @RequestParam(required = false) String[] num2
    ) throws ParseException {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
        SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
        if (so == null) {
            so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
            RedisUtil.setData(orgKey, so);
        }
        List<SysOrg> list = new ArrayList<SysOrg>();
        List<SysOrg> lists = new ArrayList<SysOrg>();
        Map<String, Object> param = new HashMap<String, Object>();
        if (!"".equals(orgName) && orgName != null) {
            if (num1.length == 0 && num2.length == 0) {
                Integer affiliationAreaId = so.getSys_org_affiliation_area_id();
                Integer areaId = so.getSys_area_id();
                if (affiliationAreaId == null) {
                    param.put("orgName", orgName);
                } else if (!areaId.equals(affiliationAreaId)) {
                    //获取地区缓存
                    String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
                    StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
                    if (sb == null) {
                        sb = new StringBuffer();
                        SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
                        DataUtil.getChildAreaIds(sysArea, sb);
                        //设置地区ID集合缓存
                        RedisUtil.setData(areaSbKey, sb);
                    }
                    param.put("area_id", sb.toString().split(","));
                    param.put("orgName", orgName);
                } else {
                    param.put("affiliationArea", affiliationAreaId);
                    param.put("orgName", orgName);
                }
                list = sysOrgService.querySysOrg(param);
            }
            if (num1.length > 0 && num2.length == 0) {
                Set<String> set = new HashSet<String>();
                for (int i = 0; i < num1.length; i++) {
                    Integer orgId = Integer.parseInt(num1[i]);
                    StringBuffer sb = new StringBuffer();
                    SysOrg sos = sysOrgService.queryInstitutionsById(orgId);
                    DataUtil.getChildOrgIds(sos, sb);
                    sb.deleteCharAt(sb.length() - 1);
                    String[] arr = sb.toString().split(",");
                    for (int j = 0; j < arr.length; j++) {
                        set.add(arr[j]);
                    }
                }
                String[] arr = set.toArray(new String[set.size()]);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orgName", orgName);
                map.put("orgIds", arr);
                list = sysOrgService.querySysOrg(map);
            }
            if (num2.length > 0 && num1.length == 0) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orgIds", num2);
                map.put("orgName", orgName);
                list = sysOrgService.querySysOrg(map);
            }
            if (num1.length > 0 && num2.length > 0) {
                Set<String> set = new HashSet<String>();
                for (int i = 0; i < num1.length; i++) {
                    Integer orgId = Integer.parseInt(num1[i]);
                    StringBuffer sb = new StringBuffer();
                    SysOrg sos = sysOrgService.queryInstitutionsById(orgId);
                    DataUtil.getChildOrgIds(sos, sb);
                    sb.deleteCharAt(sb.length() - 1);
                    String[] arr = sb.toString().split(",");
                    for (int j = 0; j < arr.length; j++) {
                        set.add(arr[j]);
                    }
                }
                for (int i = 0; i < num2.length; i++) {
                    set.add(num2[i]);
                }
                String[] arr = set.toArray(new String[set.size()]);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orgName", orgName);
                map.put("orgIds", arr);
                list = sysOrgService.querySysOrg(map);
            }
        } else {
            if (num1.length == 0 && num2.length == 0) {
                Integer affiliationAreaId = so.getSys_org_affiliation_area_id();
                Integer areaId = so.getSys_area_id();
                if (affiliationAreaId == null) {
                    param = new HashMap<String, Object>();
                } else if (!areaId.equals(affiliationAreaId)) {
                    //获取地区缓存
                    String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
                    StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
                    if (sb == null) {
                        sb = new StringBuffer();
                        SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
                        DataUtil.getChildAreaIds(sysArea, sb);
                        //设置地区ID集合缓存
                        RedisUtil.setData(areaSbKey, sb);
                    }
                    param.put("area_id", sb.toString().split(","));
                } else {
                    param.put("affiliationArea", affiliationAreaId);
                }
                list = sysOrgService.querySysOrg(param);
            }
            if (num1.length > 0 && num2.length == 0) {
                Set<String> set = new HashSet<String>();
                for (int i = 0; i < num1.length; i++) {
                    Integer orgId = Integer.parseInt(num1[i]);
                    StringBuffer sb = new StringBuffer();
                    SysOrg sos = sysOrgService.queryInstitutionsById(orgId);
                    DataUtil.getChildOrgIds(sos, sb);
                    sb.deleteCharAt(sb.length() - 1);
                    String[] arr = sb.toString().split(",");
                    for (int j = 0; j < arr.length; j++) {
                        set.add(arr[j]);
                    }
                }
                String[] arr = set.toArray(new String[set.size()]);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orgIds", arr);
                list = sysOrgService.querySysOrg(map);
            }
            if (num2.length > 0 && num1.length == 0) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orgIds", num2);
                list = sysOrgService.querySysOrg(map);
            }
            if (num1.length > 0 && num2.length > 0) {
                Set<String> set = new HashSet<String>();
                for (int i = 0; i < num1.length; i++) {
                    Integer orgId = Integer.parseInt(num1[i]);
                    StringBuffer sb = new StringBuffer();
                    SysOrg sos = sysOrgService.queryInstitutionsById(orgId);
                    DataUtil.getChildOrgIds(sos, sb);
                    sb.deleteCharAt(sb.length() - 1);
                    String[] arr = sb.toString().split(",");
                    for (int j = 0; j < arr.length; j++) {
                        set.add(arr[j]);
                    }
                }
                for (int i = 0; i < num2.length; i++) {
                    set.add(num2[i]);
                }
                String[] arr = set.toArray(new String[set.size()]);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orgIds", arr);
                list = sysOrgService.querySysOrg(map);
            }

        }
        for (int i = 0; i < list.size(); i++) {
            SysOrg sysOrg = list.get(i);
            Date date = sysOrg.getSys_org_issuance_day();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            if (date != null) {
                String dateString = formatter.format(date);
                sysOrg.setDate(dateString);
            }
            if (sysOrg.getSys_org_used()) {
                sysOrg.setUsed("是");
            } else {
                sysOrg.setUsed("否");
            }
            if (sysOrg.getSys_org_type() == 0) {
                sysOrg.setSys_org_typeName("银行机构");
            } else {
                sysOrg.setSys_org_typeName("其他机构");
            }
            lists.add(sysOrg);
        }
        String[] rowNames = {"机构名称", "上级机构名称", "机构编码", "机构类别名称", "机构所在地", "电话", "地址", "法定代表人/负责人", "机构经办人", "经办人联系电话", "组织机构代码", "统一社会信用代码", "许可证", "注册资本", "实际开立日期", "代码证编号", "登记号", "备注", "机构类型"};
        String[] propertyNames = {"sys_org_name", "sys_org_upname", "sys_org_financial_code", "sys_org_type_name", "sys_org_address_area_name", "sys_org_phone", "sys_org_address", "sys_org_representative", "sys_org_finance_operator", "sys_org_finance_operator_phone", "sys_org_code", "sys_org_credit_code", "sys_org_licence", "sys_org_reg_capital", "date", "sys_org_code_number", "sys_org_reg_number", "sys_org_notes", "sys_org_typeName"};
        if (lists != null) {
            // 生成excel
            ExcelExport<SysOrg> excelExport = new ExcelExport<SysOrg>();
            excelExport.setTitle("机构管理");
            excelExport.setRowNames(rowNames);
            excelExport.setPropertyNames(propertyNames);
            excelExport.setList(lists);
            String url = excelExport.exportExcel(request, response);
            SysManageLog sysManageLog = new SysManageLog();
            sysManageLog.setSysManageLogMenuName("机构管理");
            sysManageLog.setSysManageLogCount(lists.size());
            sysManageLog.setSysManageLogFile(url);
            sysManageLog.setSysManageLogOperateType(6);
            sysManageLog.setSysManageLogResult(true);
            sysManageLogService.insertSysManageLogTb(sysManageLog, request);
        }
    }

    /**
     * 导出EXCEL
     *
     * @param model
     * @param id    机构ID
     * @return
     */
    @RequestMapping("/export")
    @ResponseBody
    public void downLoadFile(HttpServletResponse response, HttpServletRequest request,
                             @RequestParam(required = false) Integer id) {
        try {
            String key = RedisKeys.SYS_ORG + id;
            SysOrg so = RedisUtil.getObjData(key, SysOrg.class);
            if (so == null) {
                so = sysOrgService.queryInstitutionsById(id);
                RedisUtil.setData(key, so);
            }
            if (so.getSys_org_type() == 0) {
                so.setSys_org_typeName("银行机构");
            } else {
                so.setSys_org_typeName("其他机构");
            }
            String[] rowNames = {"机构名称", "上级机构名称", "机构编码", "机构类别名称", "机构所在地", "电话", "地址", "法定代表人/负责人", "机构经办人", "经办人联系电话", "组织机构代码", "统一社会信用代码", "许可证", "注册资本", "实际开立日期", "代码证编号", "登记号", "备注", "机构类型"};
            String[] propertyNames = {"sys_org_name", "sys_org_upname", "sys_org_financial_code", "sys_org_type_name", "sys_org_address_area_name", "sys_org_phone", "sys_org_address", "sys_org_representative", "sys_org_finance_operator", "sys_org_finance_operator_phone", "sys_org_code", "sys_org_credit_code", "sys_org_licence", "sys_org_reg_capital", "sys_org_issuance_day", "sys_org_code_number", "sys_org_reg_number", "sys_org_notes", "sys_org_type", "subSysOrg"};
            if (so != null) {
                // 生成excel
                ExcelExportOrg<SysOrg> excelExport = new ExcelExportOrg<SysOrg>();
                excelExport.setTitle("机构管理");
                excelExport.setRowNames(rowNames);
                excelExport.setPropertyNames(propertyNames);
                excelExport.setObj(so);
                String url = excelExport.exportObjctExcel(response);
                SysManageLog sysManageLog = new SysManageLog();
                sysManageLog.setSysManageLogMenuName("机构管理");
                if (so.getSubSysOrg() != null && so.getSubSysOrg().size() > 0) {
                    sysManageLog.setSysManageLogCount(so.getSubSysOrg().size() + 1);
                } else {
                    sysManageLog.setSysManageLogCount(1);
                }
                sysManageLog.setSysManageLogFile(url);
                sysManageLog.setSysManageLogOperateType(6);
                sysManageLog.setSysManageLogResult(true);
                sysManageLogService.insertSysManageLogTb(sysManageLog, request);
            }
        } catch (Exception e) {
            LoggerUtil.error(e);
        }
    }

    /**
     * 通过名称获取机构
     *
     * @param model
     * @param name  机构名称
     * @param id    机构ID
     * @return
     */
    @RequestMapping("/getOrgByName")
    @ResponseBody
    public List<SysOrg> getOrgByName(@RequestParam(required = false) String name, @RequestParam(required = false) Integer typeId) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> param = new HashMap<String, Object>();
        //缓存单个机构
        String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
        SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
        if (so == null) {
            so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
            RedisUtil.setData(orgKey, so);
        }
        if (so.getSys_area_id() != null) {
            //获取地区缓存
            String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
            StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
            if (sb == null) {
                sb = new StringBuffer();
                SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
                DataUtil.getChildAreaIds(sysArea, sb);
                //设置地区ID集合缓存
                RedisUtil.setData(areaSbKey, sb);
            }
            param.put("area_id", sb.toString().split(","));//区域id
            param.put("orgName", name);
            param.put("upid", true);
            if (typeId != null) {
                param.put("type", typeId);
            }

        }
        List<SysOrg> sysOrgList = sysOrgService.queryInstitution(param);
        return sysOrgList;
    }

    /**
     * 通过名称获取机构
     *
     * @param model
     * @param name  机构名称
     * @param id    机构ID
     * @return
     */
    @RequestMapping("/getOrgTypeByName")
    @ResponseBody
    public List<SysOrgType> getOrgTypeByName(@RequestParam(required = false) String name, @RequestParam(required = false) Integer type, HttpServletRequest request) {
        List<SysOrgType> its = sysOrgTypeService.queryInstitutionTypeByName(name, type);
        return its;
    }

    /**
     * 通过ID获取机构
     *
     * @param id 机构ID
     * @return
     */
    @RequestMapping("/getInstitutions")
    @ResponseBody
    public SysOrg getInstitutions(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return null;
        }
        //缓存单个机构
        SysOrg so;
        String orgKey = RedisKeys.SYS_ORG + "admin_sysOrg_getInstitutions" + id;
        if (RedisUtil.isEmpty(orgKey)) {
            so = sysOrgService.getOrgHaveTwoSub(id);
            RedisUtil.setData(orgKey, so);
        } else {
            so = RedisUtil.getObjData(orgKey, SysOrg.class);
        }
        return so;
    }

    @RequestMapping("/getInstitutionType")
    @ResponseBody
    public SysOrgType getInstitutionType(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return null;
        }
        return sysOrgTypeService.queryInstitutionsTypeById(id);
    }

    /**
     * 通过ID删除机构
     *
     * @param model
     * @param id    机构ID
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public JsonResWrapper del(HttpServletRequest request, @RequestParam(required = false) Integer id) {
        JsonResWrapper jrw = new JsonResWrapper();
        try {
            //缓存单个机构
            String orgKey = RedisKeys.SYS_ORG + id;
            SysOrg instin = RedisUtil.getObjData(orgKey, SysOrg.class);
            if (instin == null) {
                instin = sysOrgService.queryInstitutionsById(id);
                RedisUtil.setData(orgKey, instin);
            }
            if (id == null || instin == null) {
                jrw.setFlag(false);
                jrw.setMessage("删除失败，参数缺失");
                SysManageLog sysManageLog = new SysManageLog();
                sysManageLog.setSysManageLogMenuName("机构管理");
                sysManageLog.setSysManageLogCount(1);
                sysManageLog.setSysManageLogOperateType(2);
                sysManageLog.setSysManageLogResult(false);
                sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                return jrw;
            }
            StringBuffer sb = new StringBuffer();
            DataUtil.getChildOrgIds(instin, sb);
            String[] orgid = sb.toString().split(",");
            if (orgid.length > 1) {
                jrw.setFlag(false);
                jrw.setMessage("删除失败，该机构下有子机构");
                SysManageLog sysManageLog = new SysManageLog();
                sysManageLog.setSysManageLogMenuName("机构管理");
                sysManageLog.setSysManageLogCount(1);
                sysManageLog.setSysManageLogOperateType(2);
                sysManageLog.setSysManageLogResult(false);
                sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                return jrw;
            }
            if (instin.getSys_org_used()) {
                jrw.setFlag(false);
                jrw.setMessage("删除失败，该机构已被使用");
                SysManageLog sysManageLog = new SysManageLog();
                sysManageLog.setSysManageLogMenuName("机构管理");
                sysManageLog.setSysManageLogCount(1);
                sysManageLog.setSysManageLogOperateType(2);
                sysManageLog.setSysManageLogResult(false);
                sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                return jrw;
            }
            //判断机构或子机构是否被使用
            /*if(getInstinAndSubInstinNum(instin)>0){
                jrw.setFlag(false);
				jrw.setMessage("删除失败，该机构或该机构的子机构已被使用");
				return jrw;
			}*/
            //删除机构及子机构
            //delInsti(instin);
            //删除机构
            sysOrgService.delInstitutionsById(id);
            //设置该机构及子机构的 机构类型 / 区域 为未使用
            List<SysOrg> sos = sysOrgService.querySysOrgByTypeId(instin.getSys_org_type_id());
            List<SysOrgType> type = sysOrgTypeService.queryInstitutionTypeByTid(instin.getSys_org_type_id());
            //如果未查询到机构则把该机构类型设为未使用
            if (CollectionUtils.isEmpty(sos) && type.size() <= 1) {
                SysOrgType sot = sysOrgTypeService.queryInstitutionsTypeById(instin.getSys_org_type_id());
                sot.setSys_org_type_used(false);
                sysOrgTypeService.updateInstitutionsType(sot);
            }
            sos = sysOrgService.querySysOrgByAddressAreaId(instin.getSys_area_id());
            //如果未查询到机构则把该机地址设为未使用
            if (CollectionUtils.isEmpty(sos)) {
                SysArea sa = new SysArea();
                sa.setSysAreaId(instin.getSys_area_id());
                sa.setSysAreaUsed(false);
                sysAreaService.updateSysArea(sa);
            }
            jrw.setMessage("删除成功");
            SysManageLog sysManageLog = new SysManageLog();
            sysManageLog.setSysManageLogMenuName("机构管理");
            sysManageLog.setSysManageLogCount(1);
            sysManageLog.setSysManageLogOperateType(2);
            sysManageLog.setSysManageLogResult(true);
            sysManageLogService.insertSysManageLogTb(sysManageLog, request);
        } catch (Exception e) {
            LoggerUtil.error(e);
        }

        return jrw;
    }

    /**
     * 设置该机构及机构的 机构类型 / 区域 为未使用
     *
     * @param instin
     */
    private void setUsed(SysOrg instin) {

        //递归设置机构及机构的 机构类型 / 区域 为未使用
        if (!CollectionUtils.isEmpty(instin.getSubSysOrg())) {
            for (SysOrg i : instin.getSubSysOrg()) {
                setUsed(i);
            }
        }

        List<SysOrg> sos = sysOrgService.querySysOrgByTypeId(instin.getSys_org_type_id());
        List<SysOrgType> typeList = sysOrgTypeService.queryInstitutionTypeByTid(instin.getSys_org_type_id());
        //如果未查询到机构则把该机构类型设为未使用
        if (CollectionUtils.isEmpty(sos) && typeList.size() <= 1) {
            SysOrgType sot = sysOrgTypeService.queryInstitutionsTypeById(instin.getSys_org_type_id());
            sot.setSys_org_type_used(false);
            sysOrgTypeService.updateInstitutionsType(sot);
        }


        sos = sysOrgService.querySysOrgByAddressAreaId(instin.getSys_area_id());
        //如果未查询到机构则把该机构类型设为未使用
        if (CollectionUtils.isEmpty(sos)) {
            SysArea sa = new SysArea();
            sa.setSysAreaId(instin.getSys_area_id());
            sa.setSysAreaUsed(false);
            sysAreaService.updateSysArea(sa);
        }
    }

    /**
     * 删除机构及子机构
     *
     * @param instin
     * @return 删除的机构数量
     */
    private int delInsti(SysOrg instin) {
        int total = 0;
        if (CollectionUtils.isEmpty(instin.getSubSysOrg())) {
            return sysOrgService.delInstitutionsById(instin.getSys_org_id());
        }
        for (SysOrg i : instin.getSubSysOrg()) {
            total += (sysOrgService.delInstitutionsById(instin.getSys_org_id()) + delInsti(i));
        }
        return total;
    }


    /**
     * 添加、修改页面
     *
     * @param model
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public String add(HttpServletRequest request, Model model, @RequestParam(required = false) Integer id,
                      @RequestParam(required = false) Integer flag) throws Exception {
        if (id != null) {
            String key = RedisKeys.SYS_ORG + id;
            SysOrg i = RedisUtil.getObjData(key, SysOrg.class);
            if (i == null) {
                i = sysOrgService.queryInstitutionsById(id);
                RedisUtil.setData(key, i);
            }
            //将机构所在地区的名称加上父地区
            String fullAddress = getParentArea(i.getSys_area_id());
            i.setSys_org_address_area_name(fullAddress);
            model.addAttribute("i", i);
        }
        if (flag != null) {
            if (flag == 1) {
                model.addAttribute("msg", "保存失败，参数缺失");
            } else if (flag == 2) {
                model.addAttribute("msg", "服务器异常");
            } else if (flag == 3) {
                model.addAttribute("msg", "选择的机构类别不存在");
            } else if (flag == 4) {
                model.addAttribute("msg", "选择的机构地址不存在");
            } else {
                model.addAttribute("msg", "保存成功");
            }
        }
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer roleType = userDetails.getSysRole().getSys_role_type();
        Integer roleId = userDetails.getSysRole().getSys_role_id();
        SysOrg so = sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
        Map<String, Object> param = new HashMap<String, Object>();
        if (so != null) {
            //缓存集合机构
            /*List<SysOrg> is = new ArrayList<SysOrg>();
            if(CollectionUtils.isEmpty(is)){
				if(so.getSys_area_id()!=null){
					//获取地区缓存
					String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
					StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
					if(sb==null){
						sb = new StringBuffer();
						SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
						DataUtil.getChildAreaIds(sysArea, sb);
						//设置地区ID集合缓存
						RedisUtil.setData(areaSbKey, sb);
					}
					//param.put("area_id", sb.toString().split(","));//区域id
					//param.put("insti_id", userDetails.getSys_org_id());
					//param.put("upid", true);
				}
				//is = sysOrgService.queryInstitution(param);
			}*/
            //model.addAttribute("is", is);
            //获取登录用户的区域
            Integer dutyArea = so.getSys_area_id();
            String dutyAreaKey = RedisKeys.SYS_AREA + dutyArea;
            SysArea area = RedisUtil.getObjData(dutyAreaKey, SysArea.class);
            if (area == null) {
                area = sysAreaService.queryAreaById(dutyArea);
                RedisUtil.setData(dutyAreaKey, area);
            }
            model.addAttribute("area", area);
            //获取机构类型列表
            String orgTypeKey = RedisKeys.SYS_ORG_TYPE;
            Type orgTypeType = new TypeToken<List<SysOrgType>>() {
            }.getType();
            List<SysOrgType> sot = RedisUtil.getListData(orgTypeKey, orgTypeType);
            if (CollectionUtils.isEmpty(sot)) {
                sot = sysOrgTypeService.queryInstitutionType(null, null);
                RedisUtil.setData(orgTypeKey, sot);
            }
            model.addAttribute("its", sot);
            model.addAttribute("roleType", roleType);
            model.addAttribute("roleId", roleId);
        }
        return "sysOrg/add";
    }

    /**
     * 获取父地区名称串
     *
     * @param pid
     * @return
     */
    private String getParentArea(Integer pid) {
        SysArea a = sysAreaService.queryAreaById(pid);
        if (a != null && a.getSysAreaUpid() != 0) {
            return getParentArea(a.getSysAreaUpid()) + "-" + a.getSysAreaName();
        }
        return a.getSysAreaName();

    }

    /**
     * 保存添加、修改
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public JsonResWrapper save(Model model, HttpServletRequest request,
                               @RequestParam(required = false) Integer sys_org_id,
                               @RequestParam(required = false) Integer sys_org_upid,
                               @RequestParam(required = false) String sys_org_upname,
                               @RequestParam(required = false) String sys_org_financial_code,
                               @RequestParam(required = false) String sys_org_name,
                               @RequestParam(required = false) Integer sys_org_type_id,
                               @RequestParam(required = false) String typeName,
                               @RequestParam(required = false) Integer sys_area_id,
                               @RequestParam(required = false) String sys_org_phone,
                               @RequestParam(required = false) String sys_org_address,
                               @RequestParam(required = false) String sys_org_representative,
                               @RequestParam(required = false) String sys_org_finance_operator,
                               @RequestParam(required = false) String sys_org_finance_operator_phone,
                               @RequestParam(required = false) String sys_org_code,
                               @RequestParam(required = false) String sys_org_credit_code,
                               @RequestParam(required = false) String sys_org_licence,
                               @RequestParam(required = false) String sys_org_reg_capital,
                               @RequestParam(required = false) String sys_org_issuance_day,
                               @RequestParam(required = false) String sys_org_code_number,
                               @RequestParam(required = false) String sys_org_reg_number,
                               @RequestParam(required = false) String sys_org_notes,
                               @RequestParam(required = false) Integer type,
                               @RequestParam(required = false) String sys_org_service_center_call,
                               @RequestParam(required = false) String sys_org_service_center_name) {
        JsonResWrapper jrw = new JsonResWrapper();
        try {
            SysOrg i = new SysOrg();
            //机构类别
            SysOrgType it = sysOrgTypeService.queryInstitutionsTypeById(sys_org_type_id);
            //机构地址
            SysArea posi = sysAreaService.queryAreaById(sys_area_id);

            try {
                if (posi == null) {
                    SysManageLog sysManageLog = new SysManageLog();
                    sysManageLog.setSysManageLogMenuName("机构管理");
                    sysManageLog.setSysManageLogCount(1);
                    if (sys_org_id != null) {
                        sysManageLog.setSysManageLogOperateType(3);
                    } else {
                        sysManageLog.setSysManageLogOperateType(1);
                    }
                    DefaultIndexItem defaultIndexItem = defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
                    if (defaultIndexItem != null) {
                        sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
                    }
                    sysManageLog.setSysManageLogResult(false);
                    sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                    jrw.setFlag(false);
                    jrw.setMessage("选择的机构地址不存在");
                    return jrw;
                }
                if (it == null) {
                    SysManageLog sysManageLog = new SysManageLog();
                    sysManageLog.setSysManageLogMenuName("机构管理");
                    sysManageLog.setSysManageLogCount(1);
                    if (sys_org_id != null) {
                        sysManageLog.setSysManageLogOperateType(3);
                    } else {
                        sysManageLog.setSysManageLogOperateType(1);
                    }
                    DefaultIndexItem defaultIndexItem = defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
                    if (defaultIndexItem != null) {
                        sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
                    }
                    sysManageLog.setSysManageLogResult(false);
                    sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                    jrw.setFlag(false);
                    jrw.setMessage("选择的机构类别不存在");
                    return jrw;
                }
                i.setSys_org_id(sys_org_id);
                i.setSys_org_upid(sys_org_upid);
                i.setSys_org_upname(sys_org_upname);
                i.setSys_org_financial_code(sys_org_financial_code.toUpperCase());
                i.setSys_org_name(sys_org_name);
                i.setSys_org_type_id(sys_org_type_id);
                i.setSys_org_type_name(it.getSys_org_type_name());
                i.setSys_area_id(sys_area_id);
                i.setSys_org_address_area_name(posi.getSysAreaName());
                i.setSys_org_phone(sys_org_phone);
                i.setSys_org_address(sys_org_address);
                i.setSys_org_representative(sys_org_representative);
                i.setSys_org_finance_operator(sys_org_finance_operator);
                i.setSys_org_finance_operator_phone(sys_org_finance_operator_phone);
                i.setSys_org_code(sys_org_code);
                i.setSys_org_credit_code(sys_org_credit_code);
                i.setSys_org_licence(sys_org_licence);
                i.setSys_org_reg_capital(sys_org_reg_capital);
                i.setSys_org_service_center_call(sys_org_service_center_call);
                i.setSys_org_service_center_name(sys_org_service_center_name);
                if (sys_org_id == null) {
                    i.setSys_org_current_query_times(0);
                    i.setSys_org_current_limit_query_times(0);
                    i.setSys_org_status(1);
                    Date date = new Date();
                    date.setDate(1);
                    i.setSys_org_time(date);
                }
                SysArea area = sysAreaService.getUpOrThisSysArea(sys_area_id);
                if ("0".equals(area.getSysAreaType())) {
                    i.setSys_org_affiliation_area_id(null);
                } else {
                    i.setSys_org_affiliation_area_id(area.getSysAreaId());
                }
                /*if(sys_org_id!=null){
                    SysOrg org=sysOrgService.queryInstitutionsById(sys_org_id);
					String logo=org.getSys_org_logo();
					String report=org.getSys_org_credit_report();
					if(logo!=null){
						if(file1.getSize()>0){
							String fileName1=UpLoadFile.upLoadFile(file1);
							i.setSys_org_logo(fileName1);
						}else{
							i.setSys_org_logo(logo);
						}
					}else{
						if(file1.getSize()>0){
							String fileName1=UpLoadFile.upLoadFile(file1);
							i.setSys_org_logo(fileName1);
						}else{
							i.setSys_org_logo(null);
						}
					}
					if(report!=null){
						if(file.getSize()>0){
							String fileName=UpLoadFile.upLoadFile(file);
							i.setSys_org_credit_report(fileName);
						}else{
							i.setSys_org_credit_report(report);
						}
					}else{
						if(file.getSize()>0){
							String fileName=UpLoadFile.upLoadFile(file);
							i.setSys_org_credit_report(fileName);
						}else{
							i.setSys_org_credit_report(null);
						}
					}
				}else{
					if(file.getSize()>0){
						String fileName=UpLoadFile.upLoadFile(file);
						i.setSys_org_credit_report(fileName);
					}else{
						i.setSys_org_credit_report(null);
					}
					if(file1.getSize()>0){
						String fileName1=UpLoadFile.upLoadFile(file1);
						i.setSys_org_logo(fileName1);
					}else{
						i.setSys_org_logo(null);
					}
				}*/


                if (!StringUtils.isBlank(sys_org_issuance_day)) {
                    i.setSys_org_issuance_day(DateFormatter.formatDate(sys_org_issuance_day));
                }
                i.setSys_org_code_number(sys_org_code_number);
                i.setSys_org_reg_number(sys_org_reg_number);
                i.setSys_org_notes(sys_org_notes);
                i.setSys_org_type(type);
                i.setSys_dic_id(83);
                if (StringUtils.isBlank(sys_org_financial_code) || StringUtils.isBlank(sys_org_name) || sys_org_type_id == null) {
                    SysManageLog sysManageLog = new SysManageLog();
                    sysManageLog.setSysManageLogMenuName("机构管理");
                    sysManageLog.setSysManageLogCount(1);
                    if (sys_org_id != null) {
                        sysManageLog.setSysManageLogOperateType(3);
                    } else {
                        sysManageLog.setSysManageLogOperateType(1);
                    }
                    DefaultIndexItem defaultIndexItem = defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
                    if (defaultIndexItem != null) {
                        sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
                    }
                    sysManageLog.setSysManageLogResult(false);
                    sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                    jrw.setFlag(false);
                    jrw.setMessage("保存失败，参数缺失");
                    return jrw;
                }

                //如果有上级机构，则判断机构类别是否和上级机构的机构类别相同
                if (sys_org_upid != null) {
                    SysOrg parentOrg = sysOrgService.getByIdNotHaveSub(sys_org_upid);
                    if (!sys_org_type_id.toString().equals(parentOrg.getSys_org_type_id().toString())) {
                        SysManageLog sysManageLog = new SysManageLog();
                        sysManageLog.setSysManageLogMenuName("机构管理");
                        sysManageLog.setSysManageLogCount(1);
                        if (sys_org_id != null) {
                            sysManageLog.setSysManageLogOperateType(3);
                        } else {
                            sysManageLog.setSysManageLogOperateType(1);
                        }
                        DefaultIndexItem defaultIndexItem = defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
                        if (defaultIndexItem != null) {
                            sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
                        }
                        sysManageLog.setSysManageLogResult(false);
                        sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                        jrw.setFlag(false);
                        jrw.setMessage("保存失败，机构类别和上级机构的机构类别不一致");
                        return jrw;
                    }
                    if (!sysOrgTypeService.queryInstitutionsTypeById(sys_org_type_id).getSys_org_type_name().equals("四川省农信社及农商行")) {
                        String parentcode = parentOrg.getSys_org_financial_code().substring(0, 6);
                        String subcode = sys_org_financial_code.toUpperCase().substring(0, 6);
                        if (!subcode.equals(parentcode)) {
                            model.addAttribute("returnMsg", "保存失败，机构编码前6位和上级机构编码前6位不一致");
                            SysManageLog sysManageLog = new SysManageLog();
                            sysManageLog.setSysManageLogMenuName("机构管理");
                            sysManageLog.setSysManageLogCount(1);
                            if (sys_org_id != null) {
                                sysManageLog.setSysManageLogOperateType(3);
                            } else {
                                sysManageLog.setSysManageLogOperateType(1);
                            }
                            DefaultIndexItem defaultIndexItem = defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
                            if (defaultIndexItem != null) {
                                sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
                            }
                            sysManageLog.setSysManageLogResult(false);
                            sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                            jrw.setFlag(false);
                            jrw.setMessage("保存失败，机构编码前6位和上级机构编码前6位不一致");
                            return jrw;
                        }
                    }
                }
                /*if(sys_org_upid==null){
                    String code=sys_org_financial_code.toUpperCase();
					SysOrgType sysOrgType=sysOrgTypeService.queryInstitutionsTypeById(sys_org_type_id);
					String typecode=sysOrgType.getSys_org_type_code();
					if(code.length()<sysOrgType.getSys_org_type_code().length()){
						model.addAttribute("returnMsg", "保存失败，机构编码位数少于机构类型编码位数");
						return "forward:add.jhtml?id="+sys_org_id;
					}
					if(!typecode.equals(code.substring(0, typecode.length()))){
						model.addAttribute("returnMsg", "保存失败，机构编码和机构类型编码前"+typecode.length()+"位必须一致");
						SysManageLog sysManageLog=new SysManageLog();
						sysManageLog.setSysManageLogMenuName("机构管理");
						sysManageLog.setSysManageLogCount(1);
						if(sys_org_id!=null){
							sysManageLog.setSysManageLogOperateType(3);
						}else{
							sysManageLog.setSysManageLogOperateType(1);
						}
						DefaultIndexItem defaultIndexItem= defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
						if(defaultIndexItem!=null){
							sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
						}
						sysManageLog.setSysManageLogResult(false);
						sysManageLogService.insertSysManageLogTb(sysManageLog);
						if(sys_org_id!=null){
							return "forward:add.jhtml?id="+sys_org_id;
						}else{
							return "forward:add.jhtml";
						}
					}
				}*/
                //判断修改时是否修改机构类别，如果是一级机构，则不允许修改机构列表
                /*if(sys_org_id!=null){
                    SysOrg org = sysOrgService.queryInstitutionsById(sys_org_id);
					if(!sys_org_type_id.toString().equals(org.getSys_org_type_id().toString())){
						jrw.setFlag(false);
						jrw.setMessage("保存失败，一级机构的机构类别不允许修改");
						SysManageLog sysManageLog=new SysManageLog();
						sysManageLog.setSysManageLogMenuName("机构管理");
						sysManageLog.setSysManageLogCount(1);
						sysManageLog.setSysManageLogOperateType(3);
						DefaultIndexItem defaultIndexItem= defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
						if(defaultIndexItem!=null){
							sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
						}
						sysManageLog.setSysManageLogResult(false);
						sysManageLogService.insertSysManageLogTb(sysManageLog);
						return jrw;
					}
				}*/

                //判断编码和名称是否相同
                List<SysOrg> instis = sysOrgService.queryInstitutionsByCodeAndName(sys_org_financial_code.toUpperCase(), sys_org_name);
                if (!CollectionUtils.isEmpty(instis)) {
                    if (sys_org_id != null) {//更新
                        for (SysOrg insti : instis) {
                            if (insti.getSys_org_id().intValue() != sys_org_id.intValue()) {
                                SysManageLog sysManageLog = new SysManageLog();
                                sysManageLog.setSysManageLogMenuName("机构管理");
                                sysManageLog.setSysManageLogCount(1);
                                sysManageLog.setSysManageLogOperateType(3);
                                DefaultIndexItem defaultIndexItem = defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
                                if (defaultIndexItem != null) {
                                    sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
                                }
                                sysManageLog.setSysManageLogResult(false);
                                sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                                jrw.setFlag(false);
                                jrw.setMessage("保存失败，金融机构编码或机构名称已存在");
                                return jrw;
                            }
                        }
                    } else {//保存
                        SysManageLog sysManageLog = new SysManageLog();
                        sysManageLog.setSysManageLogMenuName("机构管理");
                        sysManageLog.setSysManageLogCount(1);
                        sysManageLog.setSysManageLogOperateType(1);
                        DefaultIndexItem defaultIndexItem = defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
                        if (defaultIndexItem != null) {
                            sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
                        }
                        sysManageLog.setSysManageLogResult(false);
                        sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                        jrw.setFlag(false);
                        jrw.setMessage("保存失败，金融机构编码或机构名称已存在");
                        return jrw;
                    }
                }

                //判断父机构是否是自己或自己的子机构
                if (sys_org_id != null && sys_org_upid != null) {
                    if (sys_org_id.intValue() == sys_org_upid.intValue()) { //判断是否选择的上级地区是否是自己
                        SysManageLog sysManageLog = new SysManageLog();
                        sysManageLog.setSysManageLogMenuName("机构管理");
                        sysManageLog.setSysManageLogCount(1);
                        sysManageLog.setSysManageLogOperateType(3);
                        DefaultIndexItem defaultIndexItem = defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
                        if (defaultIndexItem != null) {
                            sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
                        }
                        sysManageLog.setSysManageLogResult(false);
                        sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                        jrw.setFlag(false);
                        jrw.setMessage("保存失败，上级机构不能选择自己");
                        return jrw;
                    }
                    SysOrg ins = sysOrgService.queryParentInstitutionsById(sys_org_upid);
                    if (checkParent(ins.getSubSysOrg(), sys_org_id) > 0) {//判断是否在自己的子地区添加
                        SysManageLog sysManageLog = new SysManageLog();
                        sysManageLog.setSysManageLogMenuName("机构管理");
                        sysManageLog.setSysManageLogCount(1);
                        sysManageLog.setSysManageLogOperateType(3);
                        DefaultIndexItem defaultIndexItem = defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
                        if (defaultIndexItem != null) {
                            sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
                        }
                        sysManageLog.setSysManageLogResult(false);
                        sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                        jrw.setFlag(false);
                        jrw.setMessage("保存失败，上级机构不能添加到自己的子机构");
                        return jrw;
                    }
                }
            } catch (Exception e) {
                LoggerUtil.error(e);
                jrw.setFlag(false);
                jrw.setMessage("服务器异常");
                return jrw;

            }
            SysOrg sysOrg = null;
            if (sys_org_id != null) {
                sysOrg = sysOrgService.getByIdNotHaveSub(sys_org_id);
            }
            SysManageLog sysManageLog = new SysManageLog();
            sysManageLog.setSysManageLogMenuName("机构管理");
            sysManageLog.setSysManageLogCount(1);
            if (sys_org_id != null) {
                SysOrg org = sysOrgService.queryInstitutionsById(sys_org_id);
                SysOrg upOrg = new SysOrg();
                if (org.getSys_org_upid() != null) {
                    upOrg = sysOrgService.queryParentInstitutionsById(org.getSys_org_upid());
                }
                String areaName = sysAreaService.queryAreaById(org.getSys_area_id()).getSysAreaName();
                sysManageLog.setSysManageLogOperateType(3);
                String name = sysOrgTypeService.queryInstitutionsTypeById(org.getSys_org_type_id()).getSys_org_type_name();
                String oldValue = "机构类别名称:" + name + ",金融机构编码:" + org.getSys_org_financial_code() + ",机构名称:" + org.getSys_org_name() +
                        ((org.getSys_org_upid() == null ? "" : ",上级机构:" + upOrg.getSys_org_name()) + ",机构所在地:" + areaName +
                                ("".equals(org.getSys_org_phone()) ? "" : ",电话:" + org.getSys_org_phone()) +
                                ("".equals(org.getSys_org_address()) ? "" : ",地址:" + org.getSys_org_address()) +
                                ("".equals(org.getSys_org_representative()) ? "" : ",法定代表人/负责人:" + org.getSys_org_representative()) +
                                ("".equals(org.getSys_org_finance_operator()) ? "" : ",金融机构经办人:" + org.getSys_org_finance_operator()) +
                                ("".equals(org.getSys_org_finance_operator_phone()) ? "" : ",经办人联系电话:" + org.getSys_org_finance_operator_phone()) +
                                ("".equals(org.getSys_org_code()) ? "" : ",金融机构代码:" + org.getSys_org_code()) +
                                ("".equals(org.getSys_org_credit_code()) ? "" : ",统一社会信用代码:" + org.getSys_org_credit_code()) +
                                ("".equals(org.getSys_org_licence()) ? "" : ",许可证:" + org.getSys_org_licence()) +
                                ("".equals(org.getSys_org_reg_capital()) ? "" : ",注册资本:" + org.getSys_org_reg_capital()) +
                                ("".equals(org.getSys_org_issuance_day()) ? "" : ",实际开立日期:" + org.getSys_org_issuance_day()) +
                                ("".equals(org.getSys_org_code_number()) ? "" : ",代码证编号:" + org.getSys_org_code_number()) +
                                ("".equals(org.getSys_org_reg_number()) ? "" : ",登记号:" + org.getSys_org_reg_number()) +
                                ("".equals(org.getSys_org_notes()) ? "" : ",备注:" + org.getSys_org_notes()));
                String newAreaName = sysAreaService.queryAreaById(sys_area_id).getSysAreaName();
                String newValue = "机构类别名称:" + it.getSys_org_type_name() + ",金融机构编码:" + sys_org_financial_code + ",机构名称:" + sys_org_name +
                        (sys_org_upid == null ? "" : ",上级机构:" + sys_org_upname) + ",机构所在地:" + newAreaName + ("".equals(sys_org_phone) ? "" : ",电话:" + sys_org_phone) +
                        ("".equals(sys_org_address) ? "" : ",地址:" + sys_org_address) + ("".equals(sys_org_representative) ? "" : ",法定代表人/负责人:" + sys_org_representative) +
                        ("".equals(sys_org_finance_operator) ? "" : ",金融机构经办人:" + sys_org_finance_operator) +
                        ("".equals(sys_org_finance_operator_phone) ? "" : ",经办人联系电话:" + sys_org_finance_operator_phone) +
                        ("".equals(sys_org_code) ? "" : ",金融机构代码:" + sys_org_code) +
                        ("".equals(sys_org_credit_code) ? "" : ",统一社会信用代码:" + sys_org_credit_code) +
                        ("".equals(sys_org_licence) ? "" : ",许可证:" + sys_org_licence) +
                        ("".equals(sys_org_reg_capital) ? "" : ",注册资本:" + sys_org_reg_capital) +
                        ("".equals(sys_org_issuance_day) ? "" : ",实际开立日期:" + sys_org_issuance_day) +
                        ("".equals(sys_org_code_number) ? "" : ",代码证编号:" + sys_org_code_number) +
                        ("".equals(sys_org_reg_number) ? "" : ",登记号:" + sys_org_reg_number) +
                        ("".equals(sys_org_notes.trim()) ? "" : ",备注:" + sys_org_notes);
                sysManageLog.setSysManageLogOldValue(oldValue);
                sysManageLog.setSysManageLogNewValue(newValue);
            } else {
                sysManageLog.setSysManageLogOperateType(1);
            }
            DefaultIndexItem defaultIndexItem = defaultIndexItemService.queryByCodeAndCredit(sys_org_credit_code, sys_org_code);
            if (defaultIndexItem != null) {
                sysManageLog.setSysManageLogEnterpriseCode(defaultIndexItem.getDefaultIndexItemId().toString());
            }
            sysManageLog.setSysManageLogResult(true);
            sysManageLogService.insertSysManageLogTb(sysManageLog, request);
            //保存机构时如果是修改,同时修改子机构对应的值
            if (sys_org_id != null) {
                SysOrg sOrg = sysOrgService.queryInstitutionsById(sys_org_id);
                Integer typeId = sOrg.getSys_org_type_id();
                String code = sOrg.getSys_org_financial_code();
                String oldCode = code.substring(0, 6);
                String newCode = sys_org_financial_code.substring(0, 6);
                List<SysOrg> orgList = null;
                if (typeId != sys_org_type_id || !oldCode.equals(newCode)) {
                    StringBuffer sb = new StringBuffer();
                    DataUtil.getChildOrgIds(sOrg, sb);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("orgIds", sb.toString().split(","));
                    map.put("upid", true);
                    orgList = sysOrgService.queryInstitution(map);
                }
                if (!oldCode.equals(newCode)) {
                    if (orgList != null && orgList.size() > 0) {
                        for (int j = 0; j < orgList.size(); j++) {
                            if (orgList.get(j).getSys_org_id() == sys_org_id) {
                                continue;
                            }
                            String subOldCode = orgList.get(j).getSys_org_financial_code();
                            String subNewCode = newCode + subOldCode.substring(6);
                            List<SysOrg> list = sysOrgService.queryInstitutionsByCodeAndName(subNewCode, null);
                            if (list != null && list.size() > 0) {
                                jrw.setFlag(false);
                                jrw.setMessage("保存失败,该机构下存在子结构的编码已存在");
                                return jrw;
                            }
                        }
                    }
                }
                if (!oldCode.equals(newCode)) {
                    if (orgList != null && orgList.size() > 0) {
                        for (int j = 0; j < orgList.size(); j++) {
                            if (orgList.get(j).getSys_org_id() == sys_org_id) {
                                continue;
                            }
                            String subOldCode = orgList.get(j).getSys_org_financial_code();
                            String subNewCode = newCode + subOldCode.substring(6);
                            orgList.get(j).setSys_org_financial_code(subNewCode);
                            sysOrgService.saveInstitutions(orgList.get(j));
                        }
                    }
                }
                if (typeId != sys_org_type_id) {
                    if (orgList != null && orgList.size() > 0) {
                        for (int j = 0; j < orgList.size(); j++) {
                            if (orgList.get(j).getSys_org_id() == sys_org_id) {
                                continue;
                            }
                            orgList.get(j).setSys_org_type_id(sys_org_type_id);
                            orgList.get(j).setSys_org_type(type);
                            if (type == 0) {
                                orgList.get(j).setSys_dic_id(83);
                            }
                            if (type == 1) {
                                orgList.get(j).setSys_dic_id(42);
                            }
                            sysOrgService.saveInstitutions(orgList.get(j));
                        }
                    }
                }


            }
            sysOrgService.saveInstitutions(i);
            if (sysOrg != null) {
                setUsed(sysOrg);
            }
            //更新机构类别为已使用
            it.setSys_org_type_used(true);
            sysOrgTypeService.updateInstitutionsType(it);
            //更新所在区域为已使用
            SysArea useArea = new SysArea();
            useArea.setSysAreaId(sys_area_id);
            useArea.setSysAreaUsed(true);
            sysAreaService.updateSysArea(useArea);
            jrw.setMessage("保存成功");

        } catch (Exception e) {
            LoggerUtil.error(e);
        }
        return jrw;
    }

    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    @ResponseBody
    public JsonResWrapper unlock(@RequestParam(required = false) Integer id) {
        JsonResWrapper jrw = new JsonResWrapper();
        try {
            if (id == null) {
                jrw.setFlag(false);
                jrw.setMessage("解锁失败,参数缺失");
            }
            SysOrg so = sysOrgService.getByIdNotHaveSub(id);
            so.setSys_org_status(1);
            Date date = new Date();
            date.setDate(1);
            so.setSys_org_time(date);
            sysOrgService.saveInstitutions(so);
            jrw.setMessage("解锁成功");
            return jrw;
        } catch (Exception e) {
            jrw.setFlag(false);
            jrw.setMessage("服务器异常");
            return jrw;
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public JsonResWrapper reset(@RequestParam(required = false) Integer id) {
        JsonResWrapper jrw = new JsonResWrapper();
        try {
            if (id == null) {
                jrw.setFlag(false);
                jrw.setMessage("清空失败,参数缺失");
            }
            SysOrg so = sysOrgService.getByIdNotHaveSub(id);
            so.setSys_org_current_query_times(0);
            sysOrgService.saveInstitutions(so);
            jrw.setMessage("清空成功");
            return jrw;
        } catch (Exception e) {
            jrw.setFlag(false);
            jrw.setMessage("服务器异常");
            return jrw;
        }
    }

    /**
     * 判断是否是自己的子机构
     *
     * @param areas 父元素集合  只有一个对象
     * @param pid
     * @return 大于0 是自己的子机构
     */
    private int checkParent(List<SysOrg> ins, Integer pid) {
        if (!CollectionUtils.isEmpty(ins)) {
            for (SysOrg i : ins) {
                List<SysOrg> p = i.getSubSysOrg();
                if (CollectionUtils.isEmpty(p)) {
                    return i.getSys_org_id().intValue() == pid.intValue() ? 1 : 0;
                }
                return ((i.getSys_org_id().intValue() == pid.intValue() ? 1 : 0) + checkParent(p, pid));
            }
        }
        return 0;
    }

    /**
     * 列表页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model, @RequestParam(required = false) String orgName,
                       @RequestParam(required = false) Integer url, HttpSession session) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer role = userDetails.getSysRole().getSys_role_type();
        //缓存单个机构
        String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
        SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
        if (so == null) {
            so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
            RedisUtil.setData(orgKey, so);
        }
        //缓存集合机构
        /*String orgListKey = RedisKeys.SYS_ORG_LIST_USER + userDetails.getSys_user_id();
        Type type = new TypeToken<List<SysOrg>>(){}.getType();
		List<SysOrg> issro = RedisUtil.getListData(orgListKey, type);*/
        List<SysOrg> issro;
        List<SysOrgType> typeList = new ArrayList<>();
        if (orgName == null || "".equals(orgName)) {
            Map<String, Object> map = sysOrgTypeService.getTypesByAreaIds(userDetails.getSysOrg().getSys_area_id());
            typeList = (List<SysOrgType>) map.get("needTypes");
            List<Integer> integerList = (List<Integer>) map.get("thisIds");
            session.setAttribute("orgTypesThisIds", integerList);
        }
        if (orgName != null && !"".equals(orgName)) {
            //获取地区缓存
            List<Integer> allSubAreaIds = sysAreaService.getAllSubAreaIds(so.getSys_area_id());
            StringBuilder sb = new StringBuilder();
            for (Integer in : allSubAreaIds) {
                sb.append(in).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            Map<String, Object> map = new HashMap<>();
            map.put("upid", true);
            map.put("area_id", allSubAreaIds);//区域id
            map.put("orgName", orgName.trim());
            PageSupport ps = PageSupport.initPageSupport(request);
            issro = sysOrgService.getOrgList(ps, "page", map);
            List<SysOrg> issro2 = sysOrgService.getOrgList(null, null, map);
            model.addAttribute("query", 1);
            model.addAttribute("is", issro);
            if (url == null) {
                SysManageLog sysManageLog = new SysManageLog();
                sysManageLog.setSysManageLogMenuName("机构管理");
                if (issro2 != null && issro2.size() > 0) {
                    sysManageLog.setSysManageLogCount(issro2.size());
                } else {
                    sysManageLog.setSysManageLogCount(0);
                }
                sysManageLog.setSysManageLogOperateType(4);
                sysManageLog.setSysManageLogQueryUserCondition("机构名称:" + orgName);
                sysManageLog.setSysManageLogResult(true);
                String sql = "SELECT s.*, t.sys_org_type_name AS sys_org_type_name" +
                        " FROM sys_org_tb s" +
                        " LEFT JOIN sys_org_type_tb t ON t.sys_org_type_id = s.sys_org_type_id" +
                        " WHERE s.sys_org_name like '%" + orgName + "%'" +
                        " AND s.sys_area_id IN(" + sb.toString() + ")";
                sysManageLog.setSysManageLogQuerySql(sql);
                sysManageLog.setSysManageLogUrl("/admin/sysOrg/list.jhtml?orgName=" + orgName + "&url=1");
                sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                model.addAttribute("url", 1);
            } else {
                if (url == 1) {
                    model.addAttribute("url", 1);
                }
            }

        }
        model.addAttribute("orgName", orgName);
        model.addAttribute("type", typeList);
        model.addAttribute("role", role);
        return "sysOrg/list";
    }

    @RequestMapping("/orgInfoList")
    public String orgInfoList(HttpServletRequest request, Model model, @RequestParam(required = false) String orgNameOrcode) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> param = new HashMap<String, Object>();
        //缓存单个机构
        String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
        SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
        if (so == null) {
            so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
            RedisUtil.setData(orgKey, so);
        }
        if (orgNameOrcode != null && !"".equals(orgNameOrcode)) {
            List<SysOrg> issro = new ArrayList<SysOrg>();
            if (CollectionUtils.isEmpty(issro)) {
                if (so.getSys_area_id() != null) {
                    //获取地区缓存
                    String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
                    StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
                    if (sb == null) {
                        sb = new StringBuffer();
                        SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
                        DataUtil.getChildAreaIds(sysArea, sb);
                        //设置地区ID集合缓存
                        RedisUtil.setData(areaSbKey, sb);
                    }
                    param.put("area_id", sb.toString().split(","));//区域id
                    //获取登录用所在机构ID
                    //如果有上级ID，这需要添加此参数. 用于判断查询的机构是否必须是一级机构（upid为null），不是则不传
                    param.put("upid", true);
                    param.put("orgNameOrcode", orgNameOrcode.trim());
                }
            }
            PageSupport ps = PageSupport.initPageSupport(request);
            issro = sysOrgService.getOrgList(ps, "page", param);
            model.addAttribute("orgList", issro);
            List<SysOrg> item = sysOrgService.getOrgList(null, null, param);
            String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
            StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
            SysManageLog sysManageLog = new SysManageLog();
            sysManageLog.setSysManageLogMenuName("机构信息查询");
            if (item != null && item.size() > 0) {
                sysManageLog.setSysManageLogCount(item.size());
            } else {
                sysManageLog.setSysManageLogCount(0);
            }
            sysManageLog.setSysManageLogOperateType(4);
            sysManageLog.setSysManageLogQueryUserCondition("机构名称或机构编码:" + orgNameOrcode);
            sysManageLog.setSysManageLogResult(true);
            String sql = "SELECT s.*, t.sys_org_type_name AS sys_org_type_name" +
                    " FROM sys_org_tb s" +
                    " LEFT JOIN sys_org_type_tb t ON t.sys_org_type_id = s.sys_org_type_id" +
                    " WHERE (s.sys_org_name like  '%" + orgNameOrcode + "%' OR s.sys_org_financial_code like  '%" + orgNameOrcode + "%')" +
                    " AND s.sys_area_id IN(" + sb.deleteCharAt(sb.length() - 1) + ")";
            sysManageLog.setSysManageLogQuerySql(sql);
            sysManageLog.setSysManageLogUrl("/admin/sysOrg/orgInfoList.jhtml?orgNameOrcode=" + orgNameOrcode);
            sysManageLogService.insertSysManageLogTb(sysManageLog, request);
        }
        model.addAttribute("orgNameOrcode", orgNameOrcode);
        return "dataTypeQuery/orgInfo";
    }

    @RequestMapping("/downAll")
    public void downAll(HttpServletResponse response, HttpServletRequest request,
                        @RequestParam(required = false) String orgNameOrcode) throws Exception {
        if (StringUtil.isNotEmpty(orgNameOrcode)) {
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Map<String, Object> param = new HashMap<String, Object>();
            //缓存单个机构
            String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
            SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
            if (so == null) {
                so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
                RedisUtil.setData(orgKey, so);
            }
            if (so.getSys_area_id() != null) {
                //获取地区缓存
                String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
                StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
                if (sb == null) {
                    sb = new StringBuffer();
                    SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
                    DataUtil.getChildAreaIds(sysArea, sb);
                    //设置地区ID集合缓存
                    RedisUtil.setData(areaSbKey, sb);
                }
                param.put("area_id", sb.toString().split(","));//区域id
                //获取登录用所在机构ID
                //如果有上级ID，这需要添加此参数. 用于判断查询的机构是否必须是一级机构（upid为null），不是则不传
                param.put("upid", true);
                param.put("orgNameOrcode", orgNameOrcode.trim());
            }
            List<SysOrg> item = sysOrgService.getOrgList(null, null, param);
            String[] rowNames = {"机构名称", "机构编码"};
            String[] propertyNames = {"sys_org_name", "sys_org_financial_code"};

            if (item != null) {
                // 生成excel
                ExcelExport<SysOrg> excelExport = new ExcelExport<SysOrg>();
                excelExport.setTitle("机构信息");
                excelExport.setRowNames(rowNames);
                excelExport.setPropertyNames(propertyNames);
                excelExport.setList(item);
                String url = excelExport.exportExcel(request, response);
                SysManageLog sysManageLog = new SysManageLog();
                sysManageLog.setSysManageLogMenuName("机构信息查询");
                sysManageLog.setSysManageLogCount(item.size());
                sysManageLog.setSysManageLogFile(url);
                sysManageLog.setSysManageLogOperateType(6);
                sysManageLog.setSysManageLogResult(true);
                sysManageLogService.insertSysManageLogTb(sysManageLog, request);
            }
        }

    }

    /**
     * 去掉重复数据
     *
     * @param is
     */
    private void delRepeat(List<SysOrg> subIs, List<SysOrg> is, List<SysOrg> indexs) {
        for (SysOrg subSo : subIs) {
            if (!CollectionUtils.isEmpty(subSo.getSubSysOrg())) {
                delRepeat(subSo.getSubSysOrg(), is, indexs);
            }
            for (int i = 0; i < is.size(); i++) {
                SysOrg so = is.get(i);
                if (so.getSys_org_id().intValue() == subSo.getSys_org_id().intValue()) {
                    indexs.add(so);
                }
            }
        }
    }

    @RequestMapping("/getOrgList")
    @ResponseBody
    public void getOrgByName(HttpServletResponse response, @RequestParam(required = false) String name) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //缓存单个机构
        String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
        SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
        if (so == null) {
            so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
            RedisUtil.setData(orgKey, so);
        }
        Integer typeId = null;
        if (name != null) {
            typeId = sysOrgTypeService.queryInstitutionTypeIdByName(name);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Integer affiliationAreaId = so.getSys_org_affiliation_area_id();
        Integer areaId = so.getSys_area_id();
        if (affiliationAreaId == null) {
            map.put("typeId", typeId);
        } else if (!areaId.equals(affiliationAreaId)) {
            //获取地区缓存
            String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
            StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
            if (sb == null) {
                sb = new StringBuffer();
                SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
                DataUtil.getChildAreaIds(sysArea, sb);
                //设置地区ID集合缓存
                RedisUtil.setData(areaSbKey, sb);
            }
            map.put("area_id", sb.toString().split(","));
            map.put("typeId", typeId);
        } else {
            map.put("affiliationArea", affiliationAreaId);
            map.put("typeId", typeId);
        }
        /*if (so != null) {
            if (so.getSys_area_id() != null) { // 如果机构的区域不为null，
														// 则根据机构的区域地址查询机构及子机构
				//获取地区缓存
				String areaSbKey = RedisKeys.SYS_AREA_SB_CHILD + so.getSys_area_id();
				StringBuffer sb = RedisUtil.getObjData(areaSbKey, StringBuffer.class);
				if(sb==null){
					sb=new StringBuffer();
					SysArea sysArea = sysAreaService.queryAreaById(so.getSys_area_id());
					DataUtil.getChildAreaIds(sysArea, sb);
					//设置地区ID集合缓存
					RedisUtil.setData(areaSbKey, sb);
				}
				map.put("area_id", sb.toString().split(","));//区域id
				map.put("typeId", typeId);
			}
		}*/
        SysOrgType orgType = sysOrgTypeService.queryInstitutionsTypeById(typeId);
        List<SysOrg> sysOrgList = sysOrgService.querySysOrg(map);
        List<SysOrgType> sysOrgType = new ArrayList<>();
        sysOrgType.add(orgType);
        List<ZtreeVo> ztreeVo = DataUtil.getZtree(sysOrgList, sysOrgType);
        //要分配的机构
        Gson gson = new Gson();
        try {
            response.getWriter().write(gson.toJson(ztreeVo));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
