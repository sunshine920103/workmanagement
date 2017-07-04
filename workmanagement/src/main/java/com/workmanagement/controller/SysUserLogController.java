package com.workmanagement.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.workmanagement.model.*;
import com.workmanagement.service.*;
import com.workmanagement.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;

/**
 * 用户行为审计的controller Created by lzm on 2017/3/14.
 */
@Controller
@RequestMapping(value = "/admin/sysUserBehaviorAudit")
public class SysUserLogController {

    private final int OVERFLOW_NUMBER = 5000;// 导出条数上限
    private final String MENU_NAME = "用户行为审计";// 菜单名称
    @Autowired
    private DicContentService dicContentService;
    @Autowired
    private DicService dicService;
    @Autowired
    private CompanyListFilterService companyListFilterService;
    @Autowired
    private SysUserLogService sysUserLogService;
    @Autowired
    private DefaultIndexItemService defaultIndexItemService;
    @Autowired
    private SysManageLogService sysManageLogService;
    @Autowired
    private SysOrgTypeService sysOrgTypeService;
    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private RelateInfoService relateInfoService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AddLogService addLogService;
    @Resource(name = "subDataGet")
    private SubDataGet subDataGet;

    /**
     * 首次进入
     *
     * @throws Exception
     */
    @RequestMapping(value = "/index")
    public String index(Model model, HttpServletRequest request, HttpSession session) throws Exception {
        session.removeAttribute("sys_user_log_excelOutMap");
        model.addAttribute("sysMenus", sysUserLogService.getMenus());// 获取到用户的所有菜单
        PageSupport ps = PageSupport.initPageSupport(request);
        List<SysUserLog> sysUserLogs = this.getLogList(ps, session);
        model.addAttribute("sysUserLogs", sysUserLogs);
        session.setAttribute("sys_user_log_size", ps.getTotalRecord());
        return "sysUserBehaviorAudit/list";
    }

    private List<SysUserLog> getLogList(PageSupport ps, HttpSession session) {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        Integer roleType = userDetails.getSysRole().getSys_role_type();
        List<SysUserLog> sysUserLogs;
        if (userDetails.getSys_role_id() == 1) {
            sysUserLogs = sysUserLogService.selectAll(ps, null, null);
        } else {
            if (roleType == 1) {//人行管理员
                sysUserLogs = sysUserLogService.selectAll(ps, userDetails.getSysOrg().getSys_area_id(), null);
            } else {
                sysUserLogs = sysUserLogService.selectAll(ps, null, getRedis(userDetails));
            }
        }
        return sysUserLogs;
    }

    @RequestMapping(value = "/show")
    public String index(Model model, @RequestParam(value = "id") Integer sysUserLogId) throws Exception {
        SysUserLog s = sysUserLogService.selectOne(sysUserLogId);
        model.addAttribute("sysUserLog", s);
        return "sysUserBehaviorAudit/show";
    }

    @RequestMapping(value = "/forward")
    public String forward(HttpServletRequest request, Integer sysId) throws Exception {
        SysUserLog s = sysUserLogService.selectOne(sysId);
        request.setAttribute("querySql", s.getSysUserLogQuerySql());
        return "forward:" + s.getSysUserLogUrl();
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        try {
            String[] rowNames = {"机构", "登录名", "用户角色", "操作对象", "菜单", "操作ip", "指标大类", "指标项",
                    "原值", "现值", "操作时间", "操作类型", "操作数据条数", "查询条件", "操作结果"};
            String[] propertyNames = {"sysUserLogOrgName", "sysUserLogUserName", "sysUserLogRoleName",
                    "sysUserLogEnterpriseCode", "sysUserLogMenuName", "sysUserLogIp", "indexName", "indexItemName",
                    "sysUserLogOldValue", "sysUserLogNewValue", "sysUserLogTime", "sysUserLogOperateType",
                    "sysUserLogCount", "sysUserLogQueryUserCondition", "sysUserLogResult"};
            // 生成excel
            DicExcelOut<SysUserLog> excelExport = new DicExcelOut<>();
            excelExport.setTitle("用户行为审计");
            excelExport.setRowNames(rowNames);
            excelExport.setPropertyNames(propertyNames);
            Map<String, Object> map = (Map<String, Object>) session.getAttribute("sys_user_log_excelOutMap");
            List<SysUserLog> list = null;
            if (MapUtils.isNotEmpty(map)) {
                list = sysUserLogService.selectBySome(null, map);
            } else {
                list = getLogList(null, session);
            }
            excelExport.setList(list);
            String url = excelExport.exportUserLog(request, response);
            // 记录日志
            this.log(SysManageLog.EXPORT_SYSMANAGElOG, list.size(), url, null, null, null, true, request);
        } catch (Exception e) {
            LoggerUtil.error(e);
        }
    }

    /**
     * 用来判断用户要导出的数据是否太多了,或者没进行查询进行提示
     *
     * @return 数据太多了返回true;
     */
    @RequestMapping(value = "/isOverflow")
    @ResponseBody
    public String isOverflow(HttpSession session, HttpServletRequest request) throws Exception {
        Integer listSize = Integer.valueOf(String.valueOf(session.getAttribute("sys_user_log_size")));
        if (listSize == 0) {
            this.log(SysManageLog.EXPORT_SYSMANAGElOG, 0, null, null, null, null, false, request);
            return "请先获取要导出的数据";
        } else if (listSize > OVERFLOW_NUMBER) {
            this.log(SysManageLog.EXPORT_SYSMANAGElOG, 0, null, null, null, null, false, request);
            return "导出失败，数据数量超过5000条";
        }
        return "操作成功";
    }

    /**
     * 查询用
     *
     * @param beginTime   开始时间
     * @param endTime     结束时间
     * @param operateType 操作类型
     * @param someSelect  搜索的名字或者企业编码
     * @param orgName     操作机构
     * @param menuName    操作菜单
     */
    @RequestMapping(value = "/selectLog")
    public String selectLog(Model model, @RequestParam String beginTime, @RequestParam String endTime,
                            Integer operateType, String someSelect, String orgName, String menuName, HttpServletRequest request,
                            HttpSession session) throws Exception {
        beginTime = StringUtils.trimToNull(beginTime);
        endTime = StringUtils.trimToNull(endTime);
        someSelect = StringUtils.trimToNull(someSelect);
        orgName = StringUtils.trimToNull(orgName);
        menuName = StringUtils.trimToNull(menuName);

        // 封装查询条件
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        boolean isSuper = userDetails.getSys_role_id() == 1;//超级管理员
        Integer type = userDetails.getSysRole().getSys_role_type();
        String endTimes = !StringUtils.equals(endTime,
                DateFormatUtils.format(Calendar.getInstance(TimeZone.getDefault()), "yyyy-MM-dd"))
                ? (endTime + " 23:59:59")
                : DateFormatUtils.format(Calendar.getInstance(TimeZone.getDefault()), "yyyy-MM-dd HH:mm:ss");
        Map<String, Object> query = new HashMap<>();
        query.put(SysUserLogService.START_TIME, beginTime);
        StringBuilder sb = new StringBuilder();
        StringBuilder showToUser = new StringBuilder();
        sb.append("select sult.*,it.index_name,iit.index_item_name from (sys_user_log_tb AS sult LEFT JOIN index_tb AS it ON "
                + "it.index_id=sult.index_id) LEFT JOIN index_item_tb AS iit ON iit.index_item_id=sult.index_item_id WHERE ");
        if (StringUtils.isNotBlank(orgName)) {
            query.put(SysUserLogService.ORG_NAME, orgName);
            sb.append("sult.sys_user_log_org_name='").append(orgName).append("' and ");
            showToUser.append("金融机构名称:").append(orgName).append(",");
        } else {
            if (!isSuper) {
                if (type == 1) {
                    query.put(SysUserLogService.AREA_ID, userDetails.getSysOrg().getSys_area_id());
                    Integer areaId = userDetails.getSysOrg().getSys_area_id();
                    sb.append("sult.sys_org_id in(select sot.sys_org_id from sys_org_tb as sot where SYS_ORG_AFFILIATION_AREA_ID =")
                            .append(areaId).append(")").append(" and ");
                } else {
                    List<SysOrg> sysOrgs = getRedis(userDetails);
                    query.put(SysUserLogService.SYS_ORG_LIST, sysOrgs);
                    sb.append("sult.sys_org_id in (");
                    for (SysOrg s : sysOrgs) {
                        sb.append(s.getSys_org_id()).append(",");
                    }
                    sb.deleteCharAt(sb.lastIndexOf(",")).append(") and ");
                }
            }
        }
        String menuCopy = menuName;
        if (StringUtils.isNotBlank(menuName)) {
            if (menuName.equals("信用报告查询")) {
                menuCopy = "信用报告";
            }
            if (menuName.equals("信用评分查询")) {
                menuCopy = "信用评分";
            }
            query.put(SysUserLogService.MENU_NAME, StringUtils.trimToNull(menuCopy));
            sb.append("sult.sys_user_log_menu_name like '%").append(menuCopy).append("%' and ");
            showToUser.append("操作菜单:").append(menuName).append(",");
        }
        if (operateType != null) {
            query.put(SysUserLogService.OPERATE_TYPE, operateType);
            sb.append("sult.sys_user_log_operate_type=").append(operateType).append(" and ");
            showToUser.append("操作类型:");
            switch (operateType) {
                case 1: {
                    showToUser.append("增加");
                    break;
                }
                case 2: {
                    showToUser.append("删除");
                    break;
                }
                case 3: {
                    showToUser.append("修改");
                    break;
                }
                case 4: {
                    showToUser.append("查询");
                    break;
                }
                case 5: {
                    showToUser.append("导入");
                    break;
                }
                case 6: {
                    showToUser.append("导出");
                    break;
                }
                case 7: {
                    showToUser.append("打印");
                    break;
                }
                case 8: {
                    showToUser.append("登陆");
                    break;
                }
            }
            showToUser.append(",");
        }
        if (StringUtils.isNotBlank(someSelect)) {
            query.put(SysUserLogService.USER_NAME_OR_ENTERPRISE_CODE, ("%" + StringUtils.trimToNull(someSelect) + "%"));
            sb.append("(sult.sys_user_log_user_name LIKE ").append("'%")
                    .append(someSelect).append("%' ").append("or sult.sys_user_log_enterprise_code like ")
                    .append("'%").append(someSelect).append("%') and ");
            showToUser.append("用户名或企业代码:").append(someSelect).append(",");
        }
        query.put(SysUserLogService.END_TIME, endTimes);
        sb.append("sult.sys_user_log_time between '").append(beginTime).append("' and '").append(endTimes)
                .append("' order by sys_user_log_id desc");
        showToUser.append("操作时间:").append(beginTime).append("~").append(endTime);
        String sql = StringUtils.replace(sb.toString(), " ", "|");

        PageSupport ps = PageSupport.initPageSupport(request);
        List<SysUserLog> list = DataUtil.isEmpty(sysUserLogService.selectBySome(ps, query));
        long dataCount = ps.getTotalRecord();// 数据条数

        String sqls = beginTime + endTime + operateType + someSelect + orgName + menuName;// 放入session
        // 的查询条件
        Object sessionSql = session.getAttribute("sysUserBehaviorAudit_someType");
        if (sessionSql == null) {
            session.setAttribute("sysUserBehaviorAudit_someType", DigestUtils.md5Hex(sqls));
            this.log(SysManageLog.SELECT_SYSMANAGElOG, Integer.valueOf(String.valueOf(dataCount)), null,
                    showToUser.toString(), "/admin/sysUserBehaviorAudit/sysQuery.jhtml", sql, dataCount != 0, request);
        } else {
            String sqlSession = String.valueOf(sessionSql);
            if (!StringUtils.equals(sqlSession, DigestUtils.md5Hex(sqls))) {// 如果两次的sql不相等
                session.setAttribute("sysUserBehaviorAudit_someType", DigestUtils.md5Hex(sqls));
                this.log(SysManageLog.SELECT_SYSMANAGElOG, Integer.valueOf(String.valueOf(dataCount)), null,
                        showToUser.toString(), "/admin/sysUserBehaviorAudit/sysQuery.jhtml", sql, dataCount != 0, request);
            }
        }

        model.addAttribute("sysUserLogs", list);
        model.addAttribute("sysMenus", sysUserLogService.getMenus());// 添加到model里
        model.addAttribute("beginTime", beginTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("operateType", operateType);
        model.addAttribute("someSelect", someSelect);
        model.addAttribute("orgName", orgName);
        model.addAttribute("menuName", menuName);

        session.setAttribute("sys_user_log_size", ps.getTotalRecord());
        session.setAttribute("sys_user_log_excelOutMap", query);

        return "sysUserBehaviorAudit/list";
    }

    /**
     * 管理员查看的查询结果集
     *
     * @param model
     * @return
     */
    @RequestMapping("/sysQuery")
    public String sysQuery(Model model, HttpServletRequest request, HttpSession session) throws Exception {
        Object objSql = request.getAttribute("querySql");
        String querySql = null;
        if (objSql == null) {
            querySql = String.valueOf(session.getAttribute("sysUserBehaviorAudit_sql"));
        } else {
            querySql = String.valueOf(objSql);
            session.setAttribute("sysUserBehaviorAudit_sql", querySql);
        }
        String sql = StringUtils.replace(querySql, "|", " ");
        PageSupport ps = PageSupport.initPageSupport(request);
        List<Map<String, Object>> list = relateInfoService.queryMoreData(ps, sql);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Map<String, Object> map : list) {
                String code = MapUtils.getString(map, "SYS_USER_LOG_ENTERPRISE_CODE");
                if (StringUtils.isNotBlank(code)) {
                    DefaultIndexItem indexItem = defaultIndexItemService.queryById(Integer.valueOf(code));
                    if (indexItem != null) {
                        code = (StringUtils.isNotBlank(indexItem.getCodeCredit()) ? indexItem.getCodeCredit() + ","
                                : "") + StringUtils.trimToEmpty(indexItem.getCodeOrg());
                        map.put("SYS_USER_LOG_ENTERPRISE_CODE", code);
                    }
                }
            }
        }
        model.addAttribute("sysUserLogs", list);
        return "sysUserBehaviorAudit/copyList";
    }

    /**
     * 下载导入导出的文件
     *
     * @return
     */
    @RequestMapping("/downFile")
    public void downFile(@RequestParam Integer sysId, @RequestParam Integer type, HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        SysUserLog sysUserLog = sysUserLogService.selectOne(sysId);
        if (type == 1) {// 导入导出文件
            DownLoadFile.downLoadFile(sysUserLog.getSysUserLogFile(), ("导入导出文件"), request, response);
        } else {// 授权文件
            DownLoadFile.downLoadFile(sysUserLog.getSysUserLogAuthFile(), ("授权文件"), request, response);
        }
    }

    @RequestMapping(value = "/getSysOrgs")
    @ResponseBody
    public List<ZtreeVo> getSysOrgs(HttpSession session) {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        String key = RedisKeys.SYS_AREA + "sysUserBehaviorAudit_getSysOrgs" + userDetails.getSys_role_id()
                + userDetails.getSysRole().getAreaId() + userDetails.getSysRole().getSys_role_type();
        List<ZtreeVo> ztreeTwo;
        if (RedisUtil.isEmpty(key)) {
            List<SysOrg> redis = this.getRedis(userDetails);
            List<SysOrgType> sysOrgTypes = sysOrgTypeService.getTypesByOrgIds(redis);
            ztreeTwo = DataUtil.getZtreeTwo(redis, sysOrgTypes);
            RedisUtil.setData(key, ztreeTwo);
        } else {
            Type type = new TypeToken<List<ZtreeVo>>() {
            }.getType();
            ztreeTwo = RedisUtil.getListData(key, type);
        }
        return ztreeTwo;
    }

    /**
     * 获取用户能看到的机构列表缓存
     *
     * @param userDetails
     * @return
     */
    private List<SysOrg> getRedis(MyUserDetails userDetails) {
        Integer userType = userDetails.getSysRole().getSys_role_type(); // 当前用户的用户类型
        List<SysOrg> sysOrgs = null;
        SysOrg sysOrg = userDetails.getSysOrg();
        String key = RedisKeys.SYS_USER_LOG + userType + "sysUserBehaviorAudit" + sysOrg.getSys_org_id()
                + sysOrg.getSys_area_id();
        if (RedisUtil.isEmpty(key)) {
            sysOrgs = new ArrayList<>();
            if (userType == 1) {
                List<Integer> list = sysAreaService.getAllSubAreaIds(userDetails.getSysOrg().getSys_area_id());
                if (CollectionUtils.isNotEmpty(list)) {
                    String[] a = new String[list.size()];
                    for (int x = 0; x < list.size(); x++) {
                        a[x] = String.valueOf(list.get(x));
                    }
                    List<SysOrg> sysOrgList = sysOrgService.querySysOrgByAreaIds(a);// 根据地区搜出所有的机构
                    sysOrgs.addAll(sysOrgList);
                }
            } else if (userType == 4) {// 机构管理员,只能看到本机构及下级机构
                subDataGet.getAllOrgs(sysOrg, sysOrgs);
            } else {
                sysOrg.setSubSysOrg(null);
                sysOrgs.add(sysOrg);
            }
            RedisUtil.setData(key, sysOrgs);
        } else {
            Type type = new TypeToken<List<SysOrg>>() {
            }.getType();
            sysOrgs = RedisUtil.getListData(key, type);
        }
        return sysOrgs;
    }

    /**
     * 日记记录
     *
     * @param operateType
     * @param count
     * @param filePath
     * @param isOk
     */
    private void log(Integer operateType, Integer count, String filePath, String userCondition, String logUrl,
                     String sql, boolean isOk, HttpServletRequest request) {
        sysManageLogService.insertSysManageLogTb(new SysManageLog(MENU_NAME, null, null, null, null, null, null,
                operateType, count, userCondition, sql, logUrl, filePath, null, isOk), request);
    }

    @RequestMapping("/addLog")
    @ResponseBody
    public String addLog(String id, String pw, Long logDate, String loginId, String oper, String resourceId,
                         HttpServletRequest request) throws Exception {
        if (!StringUtils.isBlank(id) && !StringUtils.isBlank(pw) && logDate != null && !StringUtils.isBlank(loginId)
                && !StringUtils.isBlank(oper) && !StringUtils.isBlank(resourceId)) {
            SysUser user = sysUserService.querySystemUserByCodeAndName(null, id);
            if (user != null && user.getPassword().equals(pw)) {
                java.sql.Timestamp ts = new Timestamp(logDate);

                // String c = new SimpleDateFormat("yyyy-MM-dd
                // HH:mm:ss:SSS").format(logDate);
                Map<String, Object> map = new HashMap<>();
                String sql = "select detail_ from ES10_LOG where LOGDATE_='" + ts + "' and LOGINID_ ='" + loginId
                        + "' and OPER_ = '" + oper + "' and RESOURCEID_ ='" + resourceId + "'";
                map.put("sql", sql);

                AddLog a = addLogService.queryValue(map);
                int i = 0;
                while (a == null && i < 10) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    a = addLogService.queryValue(map);
                    i++;
                }

                if (a != null) {
                    String s = a.getValue().substring(a.getValue().lastIndexOf("{"));
                    if (!StringUtils.isBlank(s)) {
                        JSONObject json = JSONObject.parseObject(s);
                        AddLog add = new AddLog();
                        if (json.get("QUERYCONDITION") != null) {
                            String can = json.get("QUERYCONDITION").toString();
                            List<DicContent> dic = dicContentService
                                    .getDicContentsByDicId(dicService.getDicByDicName("对应参数名称", sysAreaService.queryAreaByCode("510000").getSysAreaId()).getDicId());
                            for (DicContent dicContent : dic) {
                                if (can.indexOf(dicContent.getDicContentCode()) != -1) {
                                    can = can.replaceAll(dicContent.getDicContentCode(),
                                            dicContent.getDicContentValue());
                                }
                            }
                            add.setQuerycondition(can.replaceAll("@", ""));
                        }
                        if (json.get("OPERTYPEID") != null)
                            add.setOpertypeId(Integer.parseInt(json.get("OPERTYPEID").toString()));
                        if (json.get("ORGID") != null)
                            add.setOrgId(json.get("ORGID").toString());
                        if (json.get("OPERRESULT") != null)
                            add.setOperresult(json.get("OPERRESULT").toString());
                        if (json.get("TIME") != null)
                            add.setTime(json.get("TIME").toString());
                        if (json.get("USERID") != null)
                            add.setUserId(json.get("USERID").toString());
                        if (json.get("SYS_AREA_ID") != null)
                            add.setSys_area_id(Integer.parseInt(json.get("SYS_AREA_ID").toString()));
                        if (add.getOpertypeId() == 4) {
                            if (json.get("ISEXCEPTION") != null) {
                                String key = json.get("ISEXCEPTION").toString();
                                if (!StringUtils.isBlank(json.get("QUERYTIME").toString())) {
                                    java.sql.Timestamp time = new Timestamp(
                                            Long.parseLong(json.get("QUERYTIME").toString()));

                                    companyListFilterService.updateCreditReportQuery(key, time.toString());
                                }
                            }
                        }

                        SysOrg org = sysOrgService.querySysorgByFinancialCode(add.getOrgId());
                        if (org != null) {
                            SysUser userNew = sysUserService.querySystemUserByCodeAndName(null, add.getUserId());
                            switch (add.getOpertypeId()) {
                                case 1:
                                case 2:
                                case 3:
                                    try {
                                        sysManageLogService.insertSysManageLogTb(new SysManageLog(org.getSys_org_id(),
                                                org.getSys_org_name(), userNew.getUsername(),
                                                userNew.getSys_user_role_name(), add.getOperresult(), ts,
                                                add.getOpertypeId(), true, add.getQuerycondition()), request);
                                        return "true";
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return "false";
                                    }
                                case 4:
                                case 6:
                                case 7:
                                    try {
                                        sysUserLogService.insertOneLog(new SysUserLog(org.getSys_org_id(),
                                                org.getSys_org_name(), userNew.getUsername(),
                                                userNew.getSys_user_role_name(), add.getOperresult(), ts,
                                                add.getOpertypeId(), true, add.getQuerycondition()), request);
                                        return "true";
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return "false";
                                    }
                                default:
                                    return "false";
                            }
                        } else
                            return "ORGID 验证失败！";
                    } else
                        return "无法查询到该数据！";
                } else
                    return "JSON为空！";
            } else
                return "用户信息验证错误！";
        } else
            return "数据格式错误！";
    }
}
