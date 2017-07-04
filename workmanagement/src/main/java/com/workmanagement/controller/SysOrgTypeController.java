package com.workmanagement.controller;

import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOrgType;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOrgTypeService;
import com.workmanagement.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机构类别管理
 *
 * @author renyang
 */
@Controller
@RequestMapping("/admin/sysOrgType")
public class SysOrgTypeController {

    @Autowired
    private SysOrgTypeService sysOrgTypeService;
    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private SysManageLogService sysManageLogService;
    @Autowired
    private SysAreaService sysAreaService;

    /**
     * 模糊查询
     *
     * @param
     * @return
     * @throws IOException
     */
    @RequestMapping("/getOrgTypeByName")
    @ResponseBody
    public List<SysOrgType> getOrgTypeByName(@RequestParam(required = false) String name) {
        List<SysOrgType> sos = sysOrgTypeService.queryInstitutionTypeByName(name, null);
        return sos;
    }


    /**
     * 导出全部数据EXCEL
     *
     * @param
     * @return
     */
    @RequestMapping("/exportAll")
    @ResponseBody
    public void downFile(HttpServletResponse response, HttpServletRequest request, String orgTypeName) {
        List<SysOrgType> list = new ArrayList<SysOrgType>();
        List<SysOrgType> lists = new ArrayList<SysOrgType>();
        if (!"".equals(orgTypeName) || orgTypeName != null) {
            list = sysOrgTypeService.queryInstitutionTypeByName(orgTypeName, null);
        } else {
            list = sysOrgTypeService.queryInstitutionTypeByName(null, null);
        }
        for (int i = 0; i < list.size(); i++) {
            SysOrgType sysOrgType = list.get(i);
            if (sysOrgType.getSys_org_type_used()) {
                sysOrgType.setUsed("是");
            } else {
                sysOrgType.setUsed("否");
            }
            lists.add(sysOrgType);
        }
        String[] rowNames = {"机构类型名称", "上级机构类型名称", "备注", "是否使用"};
        String[] propertyNames = {"sys_org_type_name", "sys_org_type_upname", "sys_org_type_notes", "used"};

        if (lists != null) {
            // 生成excel
            ExcelExport<SysOrgType> excelExport = new ExcelExport<>();
            excelExport.setTitle("机构类别");
            excelExport.setRowNames(rowNames);
            excelExport.setPropertyNames(propertyNames);
            excelExport.setList(lists);
            String url = excelExport.exportExcel(request, response);
            SysManageLog sysManageLog = new SysManageLog();
            sysManageLog.setSysManageLogMenuName("机构类别维护");
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
     * @param id 机构ID
     * @return
     */
    @RequestMapping("/export")
    @ResponseBody
    public void downLoadFile(HttpServletResponse response, HttpServletRequest request,
                             @RequestParam(required = false) Integer id) {
        try {
            String key = RedisKeys.SYS_ORG_TYPE + id;
            SysOrgType so = RedisUtil.getObjData(key, SysOrgType.class);
            if (so == null) {
                so = sysOrgTypeService.queryInstitutionsTypeById(id);
                RedisUtil.setData(key, so);
            }
            String[] rowNames = {"机构类型名称", "上级机构类型名称", "备注", "是否使用"};
            String[] propertyNames = {"sys_org_type_name", "sys_org_type_upname", "sys_org_type_notes", "sys_org_type_used", "subSysOrgType"};
            if (so != null) {
                // 生成excel
                ExcelExportOrg<SysOrgType> excelExport = new ExcelExportOrg<SysOrgType>();
                excelExport.setTitle("机构类别");
                excelExport.setRowNames(rowNames);
                excelExport.setPropertyNames(propertyNames);
                excelExport.setObj(so);
                String url = excelExport.exportObjctExcel(response);
                //String url=excelExport.exportExcel(request, response);
                SysManageLog sysManageLog = new SysManageLog();
                sysManageLog.setSysManageLogMenuName("机构类别维护");
                sysManageLog.setSysManageLogCount(1);
                sysManageLog.setSysManageLogFile(url);
                sysManageLog.setSysManageLogOperateType(6);
                sysManageLog.setSysManageLogResult(true);
                sysManageLogService.insertSysManageLogTb(sysManageLog, request);
            }
        } catch (Exception e) {
            LoggerUtil.error(e);
        }
    }

    @RequestMapping("/getSubSysOrgTypes")
    @ResponseBody
    public List<SysOrgType> getSubSysOrgTypes(@RequestParam Integer typeId) {
        return sysOrgTypeService.getSubOrgTypesById(typeId);
    }

    @RequestMapping("/getTopSysOrgTypes")
    @ResponseBody
    public List<SysOrgType> getTopSysOrgTypes() {
        return sysOrgTypeService.getTopOrgTypes(null);
    }

    /**
     * 通过ID获取机构类别
     *
     * @param id 机构类别ID
     * @return
     */
    @RequestMapping("/getType")
    @ResponseBody
    public SysOrgType getType(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return null;
        }
        SysOrgType sysOrgType = sysOrgTypeService.queryInstitutionsTypeById(id);
        return sysOrgType;
    }


    /**
     * 点击三角形加载机构和机构类别
     *
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("/getInstitutionsType")
    @ResponseBody
    public SysOrgType getInstitutionsType(@RequestParam Integer id, HttpSession session) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        SysOrgType sysOrgType = sysOrgTypeService.queryInstitutionsTypeById(id);
        Map<String, Object> param = new HashMap<>();
        SysOrg so = sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id());
        //缓存集合机构
        List<SysOrg> issro;
        Integer affiliationAreaId = so.getSys_org_affiliation_area_id();
        Integer areaId = so.getSys_area_id();
        if (affiliationAreaId == null) {
            param.put("upid", 3333);
            param.put("typeId", id);
            issro = sysOrgService.selectSysOrg(param);
        } else if (!areaId.equals(affiliationAreaId)) {
            List<Integer> allSubAreaIds = sysAreaService.getAllSubAreaIds(so.getSys_area_id());
            //区域id
            param.put("area_id", allSubAreaIds);
            //获取登录用所在机构ID
            param.put("typeId", id);
            issro = sysOrgService.selectOneSysOrgByAreaAndType(param);
        } else {
            param.put("affiliationArea", affiliationAreaId);
            param.put("typeId", id);
            issro = sysOrgService.selectOneSysOrgByAreaAndType(param);
        }
        sysOrgType.setSubSysOrg(issro);
        List<SysOrgType> list = sysOrgType.getSubSysOrgType();
        Object orgTypesThisIds = session.getAttribute("orgTypesThisIds");
        List<Integer> ids = null;
        if (orgTypesThisIds != null) {
            ids = (List<Integer>) orgTypesThisIds;
        }
        for (int i = 0; i < list.size(); i++) {
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(ids)) {
                Integer typeId = list.get(i).getSys_org_type_id();
                if (!ids.contains(typeId)) {
                    list.remove(i);
                    --i;
                }
            }
            SysOrgType sysOrgType1 = list.get(i);
            if (org.apache.commons.collections.CollectionUtils.isEmpty(sysOrgType1.getSubSysOrg())) {
                param.put("typeId", list.get(i).getSys_org_type_id());
                if (affiliationAreaId == null) {
                    issro = sysOrgService.selectSysOrg(param);
                } else {
                    issro = sysOrgService.selectOneSysOrgByAreaAndType(param);
                }
                sysOrgType1.setSubSysOrg(issro);
            }
        }
        return sysOrgType;
    }

    /**
     * 通过机构类别名称查找机构代码
     *
     * @param name
     * @return
     */
    @RequestMapping("/getcode")
    @ResponseBody
    public void getcode(@RequestParam(required = false) String name, HttpServletResponse response) throws IOException {
        //查找机构类别代码
        String code = sysOrgTypeService.queryOrgTypeCodeByName(name);
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(code));
    }

    /**
     * 通过ID删除机构类别
     *
     * @param id 机构类别ID
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public JsonResWrapper del(@RequestParam(required = false) Integer id, HttpServletRequest request) {
        JsonResWrapper jrw = new JsonResWrapper();
        SysOrgType it = sysOrgTypeService.queryInstitutionsTypeById(id);
        if (id == null || it == null) {
            jrw.setFlag(false);
            jrw.setMessage("删除失败，参数缺失");
            SysManageLog sysManageLog = new SysManageLog();
            sysManageLog.setSysManageLogMenuName("机构类别维护");
            sysManageLog.setSysManageLogCount(1);
            sysManageLog.setSysManageLogOperateType(2);
            sysManageLog.setSysManageLogResult(false);
            sysManageLogService.insertSysManageLogTb(sysManageLog, request);
            return jrw;
        }
        StringBuffer sb = new StringBuffer();
        DataUtil.getChildOrgTypeIds(it, sb);
        sb.deleteCharAt(sb.length() - 1);
        String[] arr = sb.toString().split(",");
        if (arr.length > 1) {
            jrw.setFlag(false);
            jrw.setMessage("删除失败，该机构类别下有子机构类别");
            SysManageLog sysManageLog = new SysManageLog();
            sysManageLog.setSysManageLogMenuName("机构类别维护");
            sysManageLog.setSysManageLogCount(1);
            sysManageLog.setSysManageLogOperateType(2);
            sysManageLog.setSysManageLogResult(false);
            sysManageLogService.insertSysManageLogTb(sysManageLog, request);
            return jrw;
        }
        //判断机构或子机构是否被使用
        List<SysOrg> orgList = sysOrgService.querySysOrgByTypeId(id);
        if (orgList.size() > 0) {
            jrw.setFlag(false);
            jrw.setMessage("删除失败，该机构类别已被使用");
            SysManageLog sysManageLog = new SysManageLog();
            sysManageLog.setSysManageLogMenuName("机构类别维护");
            sysManageLog.setSysManageLogCount(1);
            sysManageLog.setSysManageLogOperateType(2);
            sysManageLog.setSysManageLogResult(false);
            sysManageLogService.insertSysManageLogTb(sysManageLog, request);
            return jrw;
        }
        Integer upid = it.getSys_org_type_upid();
        sysOrgTypeService.delInstitutionsTypeById(id);
        if (upid != null) {
            SysOrgType upType = sysOrgTypeService.queryInstitutionsTypeById(upid);
            List<SysOrgType> typelist = sysOrgTypeService.queryInstitutionTypeByTid(upid);
            List<SysOrg> orglist = sysOrgService.querySysOrgByTypeId(upid);
            if (typelist.size() <= 1 && orglist.size() == 0) {
                upType.setSys_org_type_used(false);
                sysOrgTypeService.updateInstitutionsType(upType);
            }
        }
        jrw.setMessage("删除成功");
        SysManageLog sysManageLog = new SysManageLog();
        sysManageLog.setSysManageLogMenuName("机构类别维护");
        sysManageLog.setSysManageLogCount(1);
        sysManageLog.setSysManageLogOperateType(2);
        sysManageLog.setSysManageLogResult(true);
        sysManageLogService.insertSysManageLogTb(sysManageLog, request);
        return jrw;
    }

    /**
     * 删除机构类别及子类别
     * @param instin
     * @return 删除的机构类别数量
     *//*
    private int delInstiType(SysOrgType it){
		int total = 0;
		if(CollectionUtils.isEmpty(it.getSubSysOrgType())){
			return sysOrgTypeService.delInstitutionsTypeById(it.getSys_org_type_id());
		}
		for(SysOrgType i : it.getSubSysOrgType()){
			total += (sysOrgTypeService.delInstitutionsTypeById(it.getSys_org_type_id()) + delInstiType(i));
		}
		return total;
	}*/

    /**
     * 获取被使用机构类别及子类别的数量
     * @param instin
     * @return 被使用机构类别的数量
     */
    /*private int getInstinTypeAndSubInstinTypeNum(SysOrgType instin){
        int total = 0;
		if(CollectionUtils.isEmpty(instin.getSubSysOrgType())){
			return instin.getSys_org_type_used()?1:0;
		}
		for(SysOrgType i : instin.getSubSysOrgType()){
			total += (instin.getSys_org_type_used()?1:0 + getInstinTypeAndSubInstinTypeNum(i));
		}
		return total;
	}*/

    /**
     * 机构类别添加、修改页面
     *
     * @param model
     * @param id    机构类别ID
     * @return
     */
    @RequestMapping("/add")
    public String add(Model model, @RequestParam(required = false) Integer id) {
        if (id != null) {
            SysOrgType it = sysOrgTypeService.queryInstitutionsTypeById(id);
            model.addAttribute("it", it);
        }
        String key = RedisKeys.SYS_ORG_TYPE_LIST;
        Type type = new TypeToken<List<SysOrgType>>() {
        }.getType();
        List<SysOrgType> its = RedisUtil.getListData(key, type);
        if (CollectionUtils.isEmpty(its)) {
            its = sysOrgTypeService.queryInstitutionType(0, null);
            RedisUtil.setData(key, its);
        }
        List<SysOrgType> gov = sysOrgTypeService.queryInstitutionType(1, null);
        model.addAttribute("its", its);
        model.addAttribute("gov", gov);
        return "sysOrgType/add";
    }

    /**
     * 保存机构类别添加、修改
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public JsonResWrapper save(Model model, HttpServletRequest request,
                               @RequestParam(required = false) Integer sys_org_type_id,
                               @RequestParam(required = false) Integer sys_org_type_upid,
                               @RequestParam(required = false) String sys_org_type_upname,
                               @RequestParam(required = false) String sys_org_type_name,
                               @RequestParam(required = false) String sys_org_type_notes,
                               @RequestParam(required = false) Integer type) {

        JsonResWrapper jrw = new JsonResWrapper();

        List<SysOrgType> its = sysOrgTypeService.queryInstitutionType(null, null);
        model.addAttribute("its", its);

        SysOrgType i = new SysOrgType();
        i.setSys_org_type_id(sys_org_type_id);
        i.setSys_org_type_name(sys_org_type_name);
        i.setSys_org_type_notes(sys_org_type_notes);
        i.setSys_org_type_upid(sys_org_type_upid);
        i.setSys_org_type_upname(sys_org_type_upname);
        i.setSys_org_type_type(type);
        if (StringUtils.isBlank(sys_org_type_name)) {
            jrw.setFlag(false);
            jrw.setMessage("保存失败，参数缺失");
            SysManageLog sysManageLog = new SysManageLog();
            sysManageLog.setSysManageLogMenuName("机构类别维护");
            sysManageLog.setSysManageLogCount(1);
            if (sys_org_type_id != null) {
                sysManageLog.setSysManageLogOperateType(3);
            } else {
                sysManageLog.setSysManageLogOperateType(1);
            }
            sysManageLog.setSysManageLogResult(false);
            sysManageLogService.insertSysManageLogTb(sysManageLog, request);
            return jrw;
        }
        /*if(sys_org_type_id!=null){
            SysOrgType sysOrgType=sysOrgTypeService.queryInstitutionsTypeById(sys_org_type_id);
			if(sysOrgType.getSys_org_type_used()){
				if(!sysOrgType.getSys_org_type_code().equals(sys_org_type_code)){
					jrw.setFlag(false);
					jrw.setMessage("保存失败，机构类别已被占用");
					SysManageLog sysManageLog=new SysManageLog();
					sysManageLog.setSysManageLogMenuName("机构类别维护");
					sysManageLog.setSysManageLogCount(1);
					sysManageLog.setSysManageLogOperateType(3);
					sysManageLog.setSysManageLogResult(false);
					sysManageLogService.insertSysManageLogTb(sysManageLog);
					return jrw;
				}
			}
		}*/
        List<SysOrgType> instiTypes = sysOrgTypeService.queryUniqueInstiType(sys_org_type_name, null);
        if (!CollectionUtils.isEmpty(instiTypes)) {
            if (sys_org_type_id != null) {
                for (SysOrgType instiType : instiTypes) {
                    if (instiType != null && instiType.getSys_org_type_id().intValue() != sys_org_type_id.intValue()) {
                        jrw.setFlag(false);
                        jrw.setMessage("保存失败，机构类别名称或机构类别代码已存在");
                        SysManageLog sysManageLog = new SysManageLog();
                        sysManageLog.setSysManageLogMenuName("机构类别维护");
                        sysManageLog.setSysManageLogCount(1);
                        sysManageLog.setSysManageLogOperateType(3);
                        sysManageLog.setSysManageLogResult(false);
                        sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                        return jrw;
                    }
                }
            } else {
                jrw.setFlag(false);
                jrw.setMessage("保存失败，机构类别名称或机构类别代码已存在");
                SysManageLog sysManageLog = new SysManageLog();
                sysManageLog.setSysManageLogMenuName("机构类别维护");
                sysManageLog.setSysManageLogCount(1);
                sysManageLog.setSysManageLogOperateType(1);
                sysManageLog.setSysManageLogResult(false);
                sysManageLogService.insertSysManageLogTb(sysManageLog, request);
                return jrw;
            }
        }

        if (sys_org_type_id != null && sys_org_type_upid != null) {
            if (sys_org_type_id.intValue() == sys_org_type_upid.intValue()) { //判断是否选择的上级类别是否是自己
                jrw.setFlag(false);
                jrw.setMessage("保存失败，不能选择自己为自己的上级类别");
                return jrw;
            }
            List<SysOrgType> areas = sysOrgTypeService.queryParentInstitutionsTypeById(sys_org_type_upid);
            if (checkParent(areas, sys_org_type_id) > 0) {//判断是否在自己的子类别下添加
                jrw.setFlag(false);
                jrw.setMessage("保存失败，不能添加到自己的子类别下");
                return jrw;
            }
        }
        if (sys_org_type_upid != null) {
            SysOrgType upType = sysOrgTypeService.queryInstitutionsTypeById(sys_org_type_upid);
            upType.setSys_org_type_used(true);
            sysOrgTypeService.updateInstitutionsType(upType);
        }
        SysManageLog sysManageLog = new SysManageLog();
        sysManageLog.setSysManageLogMenuName("机构类别维护");
        sysManageLog.setSysManageLogCount(1);
        if (sys_org_type_id != null) {
            SysOrgType sysOrgType = sysOrgTypeService.queryInstitutionsTypeById(sys_org_type_id);
            sysManageLog.setSysManageLogOperateType(3);
            String oldValue = "机构类别名称:" + sysOrgType.getSys_org_type_name() + ("".equals(sysOrgType.getSys_org_type_notes()) ? "" : ",备注:" + sysOrgType.getSys_org_type_notes());
            String newValue = "机构类别名称:" + sys_org_type_name + ("".equals(sys_org_type_notes) ? "" : ",备注:" + sys_org_type_notes);
            sysManageLog.setSysManageLogOldValue(oldValue);
            sysManageLog.setSysManageLogNewValue(newValue);
        } else {
            sysManageLog.setSysManageLogOperateType(1);
        }
        sysManageLog.setSysManageLogResult(true);
        sysManageLogService.insertSysManageLogTb(sysManageLog, request);
        sysOrgTypeService.saveInstitutionsType(i);
        jrw.setMessage("保存成功");
        return jrw;
    }

    /**
     * 判断是否是自己的子地区
     *
     * @param pid
     * @return 大于0 是自己的子地区
     */
    private int checkParent(List<SysOrgType> parents, Integer pid) {
        if (!CollectionUtils.isEmpty(parents)) {
            for (SysOrgType it : parents) {
                List<SysOrgType> p = it.getSubSysOrgType();
                if (CollectionUtils.isEmpty(p)) {
                    return it.getSys_org_type_id().intValue() == pid.intValue() ? 1 : 0;
                }
                return ((it.getSys_org_type_id().intValue() == pid.intValue() ? 1 : 0) + checkParent(p, pid));
            }
        }
        return 0;
    }

    /**
     * 机构类别列表页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public String add(HttpServletRequest request, Model model, @RequestParam(required = false) String orgTypeName, @RequestParam(required = false) Integer url) throws Exception {
        String key = RedisKeys.SYS_ORG_TYPE_LIST;
        Type type = new TypeToken<List<SysOrgType>>() {
        }.getType();
        List<SysOrgType> is = new ArrayList<SysOrgType>();
        if (StringUtil.isNotEmpty(orgTypeName)) {
            is = sysOrgTypeService.queryInstitutionTypeByName(orgTypeName, null);
            List<SysOrgType> orgType = sysOrgTypeService.queryInstitutionTypeByName(orgTypeName, null);
            for (int i = 0; i < is.size(); i++) {
                for (int j = i + 1; j < is.size(); j++) {
                    StringBuffer sbff = new StringBuffer();
                    StringBuffer sbff2 = new StringBuffer();
                    List<Integer> ids = new ArrayList<Integer>();
                    List<Integer> ids2 = new ArrayList<Integer>();
                    DataUtil.getChildOrgTypeIds(is.get(j), sbff);
                    DataUtil.getChildOrgTypeIds(is.get(i), sbff2);
                    String[] s = sbff.toString().split(",");
                    String[] s2 = sbff2.toString().split(",");
                    for (int k = 0; k < s.length; k++) {
                        ids.add(Integer.parseInt(s[k]));
                    }
                    if (ids.contains(is.get(i).getSys_org_type_id())) {
                        is.remove(i);
                        i--;
                        break;
                    }
                    for (int l = 0; l < s2.length; l++) {
                        ids2.add(Integer.parseInt(s2[l]));
                    }
                    if (ids2.contains(is.get(j).getSys_org_type_id())) {
                        is.remove(j);
                        j--;
                    }

                }
            }
            model.addAttribute("its", is);
            if (url == null) {
                SysManageLog sysManageLog = new SysManageLog();
                sysManageLog.setSysManageLogMenuName("机构类别维护");
                if (orgType != null && orgType.size() > 0) {
                    sysManageLog.setSysManageLogCount(orgType.size());
                } else {
                    sysManageLog.setSysManageLogCount(0);
                }
                sysManageLog.setSysManageLogOperateType(4);
                sysManageLog.setSysManageLogQueryUserCondition("机构类别名称:" + orgTypeName);
                sysManageLog.setSysManageLogResult(true);
                String sql = "SELECT *  FROM sys_org_type_tb ty WHERE	ty.sys_org_type_name like  '%" + orgTypeName + "%'";
                sysManageLog.setSysManageLogQuerySql(sql);
                sysManageLog.setSysManageLogUrl("/admin/sysOrgType/list.jhtml?orgTypeName=" + orgTypeName + "&url=1");
                sysManageLogService.insertSysManageLogTb(sysManageLog, request);
            }
        }
        if (StringUtil.isEmpty(orgTypeName)) {
            is = sysOrgTypeService.queryInstitutionType(null, null);
            model.addAttribute("its", is);
        }
        model.addAttribute("orgTypeName", orgTypeName);
        return "sysOrgType/list";
    }
}
