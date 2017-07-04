package com.workmanagement.controller;

import com.google.gson.reflect.TypeToken;
import com.workmanagement.dao.ReportIndexDao;
import com.workmanagement.model.*;
import com.workmanagement.service.*;
import com.workmanagement.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 报文报送的controller
 * Created by lzm on 2017/3/20.
 */
@Controller
@Scope("session")
@RequestMapping(value = "/admin/messageSubmission")
public class MessageSubmittedController {

    private final String[] timeType = {"yyyy-MM-dd"};
    @Autowired
    private IndexTbService indexTbService;
    @Autowired
    private IndexItemTbService indexItemTbService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private SysOrgTypeService sysOrgTypeService;
    @Autowired
    private ReportIndexService reportIndexService;
    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private MessageSubmissionService messageSubmissionService;
    @Autowired
    private SysOtherManageService sysOtherManageService;
    @Autowired
    private SysUserLogService sysUserLogService;
    @Autowired
    private RelateInfoService relateInfoService;
    @Autowired
    private DefaultIndexItemService defaultIndexItemService;

    @RequestMapping(value = "/list")
    public String list(Model model, HttpSession session, HttpServletRequest request) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        //用户能看到的指标大类
        List<IndexTb> indexTbList = this.getIndexTbList(userDetails);
        model.addAttribute("indexTbList", indexTbList);
        List<ReportIndex> reportIndexList = null;
        PageSupport ps = PageSupport.initPageSupport(request);
        if (userDetails.getSys_role_id() == 1) {//超级管理员
            reportIndexList = reportIndexService.getReportsByMethod(ps, ReportIndexDao.TXT_SUBMIT);
        } else {
            List<Integer> orgIds = this.getOrgIds(userDetails);
            reportIndexList = reportIndexService.getReportsByMethodAndOrgIds(ps, ReportIndexDao.TXT_SUBMIT, orgIds);
        }
        model.addAttribute("reportIndexList", reportIndexList);

        return "messageSubmission/list";
    }


    @RequestMapping("/getSysOrgs")
    @ResponseBody
    public List<ZtreeVo> getSysOrgs() throws Exception {
        MyUserDetails userDetails = DataUtil.getSessionUser();
        String key = RedisKeys.SYS_AREA + "messageSubmission_getSysOrgs" + userDetails.getSys_role_id()
                + userDetails.getSysRole().getAreaId() + userDetails.getSysRole().getSys_role_type();
        List<ZtreeVo> ztreeTwo;
        if (RedisUtil.isEmpty(key)) {
            List<SysOrg> sysOrgList = this.getSysOrg(userDetails);
//            List<SysOrgType> topOrgTypes = sysOrgTypeService.getTypesByOrgIds(sysOrgList);
//            topOrgTypes = topOrgTypes.stream().filter(n->n.getSys_org_type_id().equals("80")).collect(Collectors.toList());
            List<SysOrgType> topOrgTypes = new ArrayList<>();
            SysOrgType type = sysOrgTypeService.getTypeByIdNotSub(80);
            topOrgTypes.add(type);
            ztreeTwo = DataUtil.getZtreeTwo(sysOrgList, topOrgTypes);
            RedisUtil.setData(key, ztreeTwo);
        } else {
            Type type = new TypeToken<List<ZtreeVo>>() {
            }.getType();
            ztreeTwo = RedisUtil.getListData(key, type);
        }
        return ztreeTwo;
    }

    /**
     * 上传的报文报送txt文档
     *
     * @return
     */
    @RequestMapping(value = "/upLoad")
    public String upLoad(Model model, MultipartFile file, String time, Integer indexId,
                         HttpSession session, HttpServletRequest request) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        List<Integer> sysAreaIds = sysAreaService.getAllUpAreaIds(userDetails.getSysOrg().getSys_area_id());
        Date reportTime = DateUtils.parseDate(time, timeType);
        session.setAttribute("messageSubmission_upLoad_reportTime", time);
        IndexTb indexTb = indexTbService.queryById(indexId);
        SysOtherManage sysOtherManage = sysOtherManageService.querySysOtherManage(userDetails.getSys_user_id());

        boolean canUsedCode = true;//是否可以使用组织机构代码
        if (sysOtherManage.getAuthFileSwitch() == 0) {
            canUsedCode = false;
        }
        String[] del = new String[]{"\t", " "};
        String[] right = new String[]{"", ""};
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = file.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String moLine = StringUtils.replaceEach(bufferedReader.readLine(), del, right);//当前行的数据
            if (StringUtils.isBlank(moLine)) {
                model.addAttribute("msg", "请勿上传空文件");
                return "messageSubmission/add";
            }
            String[] data = StringUtils.split(moLine, "|");
            String[] newData = null;
            if (canUsedCode) {
                newData = Arrays.copyOfRange(data, 2, data.length);
            } else {
                newData = Arrays.copyOfRange(data, 1, data.length);
            }
            List<IndexItemTb> list = indexItemTbService.getIndexIntemsIsUsedByIdAndAreaIds(indexId, sysAreaIds);
            String[] momo = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                momo[i] = list.get(i).getIndexItemName();
            }
            if (!Arrays.equals(momo, newData)) {
                model.addAttribute("msg", "上传的文件和选择的指标大类不匹配");
                return "messageSubmission/add";
            }
            List<String> message;
            List<DefaultIndexItem> changeIndexItem;
            Map<String, Object> returnMap;
            try {
                returnMap = messageSubmissionService.insertData(file, indexTb, userDetails, reportTime, canUsedCode, request);
                message = (List<String>) returnMap.get("errorList");
                changeIndexItem = (List<DefaultIndexItem>) returnMap.get("changeIndexItem");
            } catch (Exception e) {
                model.addAttribute("msg", "未知错误导致入库异常！");
                return "messageSubmission/add";
            }
            if (CollectionUtils.isNotEmpty(message)) {
                model.addAttribute("messages", message);
                session.setAttribute("messageSubmission_message", message);
                return "messageSubmission/dataError";
            }
            if (CollectionUtils.isNotEmpty(changeIndexItem)) {
                model.addAttribute("changeIndexItem", changeIndexItem);
                return "messageSubmission/chooseQymc";
            }
        } catch (Exception e) {
            model.addAttribute("msg", "文件读取异常！");
            return "messageSubmission/add";
        } finally {
            IOUtils.closeQuietly(bufferedReader);
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(inputStream);
        }
        model.addAttribute("msg", "上报成功");
        return "messageSubmission/add";
    }

    @RequestMapping("/saveIndexName")
    @ResponseBody
    public String saveIndexName(@RequestParam String id, String newName, HttpSession session) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        Integer sysOrgId = userDetails.getSys_org_id();
        Integer areaId = userDetails.getSysOrg().getSys_area_id();
        //上报时间
        String reportTime = String.valueOf(session.getAttribute("messageSubmission_upLoad_reportTime"));
        id = StringUtils.removeEnd(id, ",");
        newName = StringUtils.removeEnd(newName, ",");
        String[] ids = StringUtils.split(id, ",");
        String[] newNames = StringUtils.split(newName, ",");
        for (int i = 0; i < ids.length; i++) {
            DefaultIndexItem d = new DefaultIndexItem();
            d.setDefaultIndexItemId(Integer.valueOf(ids[i]));
            d.setQymc(newNames[i]);
            defaultIndexItemService.updateDefaultIndexItem(d);
            //更新基本信息表
            StringBuilder sb = new StringBuilder("update index_jbxx_tb set index_jbxx_qymc='");
            sb.append(newNames[i]).append("' where default_index_item_id=").append(Integer.valueOf(ids[i]))
                    .append(" and sys_area_id=").append(areaId).append(" and sys_org_id=").append(sysOrgId)
                    .append("RECORD_DATE='").append(reportTime).append("'");
            reportIndexService.updateBySql(sb.toString());
        }
        return "操作成功";
    }

    /**
     * 导出错误信息成 excel 文件
     */
    @RequestMapping("/outMsg")
    public void outMsg(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws NoSuchMethodException {
        List<String> message = (List<String>) session.getAttribute("messageSubmission_message");
        if (CollectionUtils.isEmpty(message)) {
            message = new ArrayList<>();
            message.add("看到这句话表示你遇到 bug 了,请联系管理员.");
        }
        if (CollectionUtils.isNotEmpty(message)) {
            DicExcelOut<String> excelOut = new DicExcelOut<>();
            excelOut.setTitle("上报错误信息");
            excelOut.setRowNames(new String[]{"错误信息"});
            excelOut.setList(message);
            excelOut.outString(response, request);
        }
    }

    @RequestMapping("/downError")
    public void downError(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id) {
        String errorExcelPath = reportIndexService.queryReportIndexsById(id).getErrorExcelPath();
        DownLoadFile.downLoadFile(errorExcelPath, "报送错误记录", request, response);
    }

    /**
     * 下载某个指标大类的txt模板
     */
    @RequestMapping(value = "/downLoad")
    public void downLoad(Integer indexId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        IndexTb indexTb = indexTbService.queryById(indexId);
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        List<Integer> sysAreaIds = sysAreaService.getAllUpAreaIds(userDetails.getSysOrg().getSys_area_id());
        List<IndexItemTb> indexItemTbs = indexItemTbService.getIndexIntemsIsUsedByIdAndAreaIds(indexId, sysAreaIds);
        SysOtherManage sysOtherManage = sysOtherManageService.querySysOtherManage(userDetails.getSys_user_id());
        boolean canUsedCode = true;//是否可以使用组织机构代码
        if (sysOtherManage.getAuthFileSwitch() == 0) {//0关，1开，开表示不能用组织码
            canUsedCode = false;
        }
        CreateTxtFile.creatTxtFile(request, response, indexTb, indexItemTbs, canUsedCode);
    }

    /**
     * 下载上报的文件
     */
    @RequestMapping(value = "/downLoadFile")
    public void downLoadFile(Integer reportIndexId, HttpServletRequest request, HttpServletResponse response) {
        ReportIndex indexTb = reportIndexService.queryReportIndexsById(reportIndexId);
        String path = indexTb.getReportIndexPath();
        DownLoadFile.downLoadFile(path, indexTb.getReportIndexTemplate(), request, response);
    }

    /**
     * 展示某个数据详情
     */
    @RequestMapping(value = "/show")
    public String show(Integer reportIndexId, Model model) {
        ReportIndex reportIndex = reportIndexService.queryReportIndexsById(reportIndexId);
        model.addAttribute("reportIndex", reportIndex);
        return "messageSubmission/show";
    }

    /**
     * 跳转到新增报送的页面
     */
    @RequestMapping(value = "/add")
    public ModelAndView add(HttpSession session) throws Exception {
        ModelAndView view = new ModelAndView("messageSubmission/add");
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        List<IndexTb> indexTbList = this.getIndexTbList(userDetails);
        view.addObject("indexTbList", indexTbList);
        return view;
    }

    /**
     * 搜索上报的方法
     *
     * @param sysOrgId   机构id
     * @param indexName  指标大类id
     * @param status     上报状态
     * @param recordDate 归档时间
     * @return
     */
    @RequestMapping("/searchData")
    public ModelAndView searchData(Integer sysOrgId, String indexName, String recordDate,
                                   Integer status, String reportSubmitTime, HttpSession session,
                                   HttpServletRequest request) throws Exception {
        indexName = StringUtils.trimToNull(indexName);
        recordDate = StringUtils.trimToNull(recordDate);
        reportSubmitTime = StringUtils.trimToNull(reportSubmitTime);

        ModelAndView view = new ModelAndView("messageSubmission/list");
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        StringBuilder sql = new StringBuilder("select * from report_index_tb where report_index_method=").append(ReportIndexDao.TXT_SUBMIT).append(" and");
        StringBuilder showToUser = new StringBuilder();
        view.addObject("recordDate", recordDate);
        view.addObject("indexName", indexName);
        view.addObject("status", status);
        view.addObject("reportSubmitTime", reportSubmitTime);

        List<Integer> orgIds = null;
        if (sysOrgId != null) {
            orgIds = new ArrayList<>();
            SysOrg sysOrg = sysOrgService.getByIdNotHaveSub(sysOrgId);
            orgIds.add(sysOrg.getSys_org_id());
            sql.append(" sys_org_id=").append(sysOrgId).append(" and");
            showToUser.append("机构名称：").append(sysOrg.getSys_org_name()).append(",");
            view.addObject("sysOrg", sysOrg);
        } else {
            sql.append(" sys_org_id in(");
            orgIds = this.getOrgIds(userDetails);
            for (Integer i : orgIds) {
                sql.append(i).append(",");
            }
            sql.deleteCharAt(sql.lastIndexOf(",")).append(") and");
        }

        PageSupport ps = PageSupport.initPageSupport(request);
        List<ReportIndex> reportIndexList = reportIndexService.getDataBySome(ps, orgIds, ReportIndexDao.TXT_SUBMIT, indexName, status, recordDate, reportSubmitTime);
        view.addObject("reportIndexList", reportIndexList);
        long dataCount = ps.getTotalRecord();//数据条数

        if (indexName != null) {
            sql.append(" report_index_template='").append(indexName).append("' and");
            showToUser.append("模板名称：").append(indexName).append(",");
        }
        if (status != null) {
            sql.append(" report_index_status=").append(status).append(" and");
            showToUser.append("上报状态：").append(status == 0 ? "上报成功" : "上报失败").append(",");
        }
        if (recordDate != null) {
            sql.append(" to_char(report_index_time,'YYYY-MM-DD') = '").append(recordDate).append("'");
        }
        if (reportSubmitTime != null) {
            sql.append(" to_char(report_index_submit_time,'YYYY-MM-DD') = '").append(reportSubmitTime).append("'");
        }
        if (StringUtils.endsWith(sql.toString(), "and")) {
            sql.delete(sql.lastIndexOf("and"), sql.length());
            showToUser.append("归档时间：").append(recordDate);
        }
        sql.append(" order by report_index_id desc");
        String logSql = StringUtils.replace(sql.toString(), " ", "|");

        String sqls = recordDate + reportSubmitTime + sysOrgId + indexName + status;//放入session 的查询条件
        Object sessionSql = session.getAttribute("messageSubmission_someType");

        if (sessionSql == null) {
            session.setAttribute("messageSubmission_someType", DigestUtils.md5Hex(sqls));
            this.log(showToUser.toString(), logSql, dataCount, dataCount != 0, request);
        } else {
            String sqlSession = String.valueOf(sessionSql);
            if (!StringUtils.equals(sqlSession, DigestUtils.md5Hex(sqls))) {//如果两次的sql不相等
                session.setAttribute("messageSubmission_someType", DigestUtils.md5Hex(sqls));
                this.log(showToUser.toString(), logSql, dataCount, dataCount != 0, request);
            }
        }

        List<IndexTb> indexTbList = this.getIndexTbList(userDetails);
        view.addObject("indexTbList", indexTbList);
//        List<SysOrg> sysOrgList = this.getSysOrg(userDetails);
//        view.addObject("sysOrgList", sysOrgList);
        return view;
    }

    /**
     * 管理员查看的查询结果集
     *
     * @return
     */
    @RequestMapping("/sysQuery")
    public ModelAndView sysQuery(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView view = new ModelAndView("messageSubmission/copyList");
        Object objSql = request.getAttribute("querySql");
        String querySql = null;
        if (objSql == null) {
            querySql = String.valueOf(session.getAttribute("messageSubmission_sql"));
        } else {
            querySql = String.valueOf(objSql);
            session.setAttribute("messageSubmission_sql", querySql);
        }
        String sql = StringUtils.replace(querySql, "|", " ");
        PageSupport ps = PageSupport.initPageSupport(request);
        List<Map<String, Object>> list = relateInfoService.queryMoreData(ps, sql);
        view.addObject("reportIndexList", list);
        return view;
    }

    /**
     * 获取用户能看到的机构列表缓存
     *
     * @param userDetails
     * @return
     */
    private List<Integer> getOrgIds(MyUserDetails userDetails) throws Exception {
        List<Integer> sysOrgIds = null;
        Integer userType = userDetails.getSysRole().getSys_role_type(); //当前用户的用户类型
        Integer sysOrgId = userDetails.getSys_org_id();                 //当前机构的id
        String sysOrgIdKey = RedisKeys.SYS_ORG_LIST_USER + "messageSubmission" + userType + sysOrgId + userDetails.getSys_role_id();
        if (RedisUtil.isEmpty(sysOrgIdKey)) {
            sysOrgIds = new ArrayList<>();
            if (userType == 3) {//人行报送员
                Integer userAreaId = userDetails.getSysOrg().getSys_area_id();  //当前机构的地区id
                List<Integer> sysAreaIs = sysAreaService.getAllSubAreaIds(userAreaId);//存放当前及下属地区的id
                sysOrgIds = sysOrgService.getSysOrgIdsByAreaIds(sysAreaIs);
            } else if (userType == 10 || userType == 6) {//政府报送员,只能看到本机构报送的数据
                sysOrgIds.add(sysOrgId);
            } else if (userType == 1) {//超级管理员，人行管理员
                sysOrgIds = sysOrgService.getAllIds();
            }
            RedisUtil.setData(sysOrgIdKey, sysOrgIds);
        } else {
            Type sysOrgIdType = new TypeToken<List<Integer>>() {
            }.getType();
            sysOrgIds = RedisUtil.getListData(sysOrgIdKey, sysOrgIdType);
        }
        return sysOrgIds;
    }

    private List<SysOrg> getSysOrg(MyUserDetails userDetails) throws Exception {
        Integer userType = userDetails.getSysRole().getSys_role_type(); //当前用户的用户类型
        SysOrg sysOrg = userDetails.getSysOrg();
        List<SysOrg> sysOrgs = null;
        String key = RedisKeys.SYS_USER_LOG + userDetails.getSys_role_id() + userType + "messageSubmission" + sysOrg.getSys_org_id() + sysOrg.getSys_area_id();
        if (RedisUtil.isEmpty(key)) {
            List<Integer> orgIds = this.getOrgIds(userDetails);
            String[] a = new String[orgIds.size()];
            for (int x = 0; x < a.length; x++) {
                a[x] = String.valueOf(orgIds.get(x));
            }
            sysOrgs = sysOrgService.querySysOrgByOrgIds(a);
            RedisUtil.setData(key, sysOrgs);
        } else {
            Type type = new TypeToken<List<SysOrg>>() {
            }.getType();
            sysOrgs = RedisUtil.getListData(key, type);
        }
        return sysOrgs;
    }

    /**
     * 获取用户能看到的指标大类
     *
     * @param userDetails
     * @return
     */
    private List<IndexTb> getIndexTbList(MyUserDetails userDetails) throws Exception {
        Integer userAreaId = userDetails.getSysOrg().getSys_area_id();//当前机构的地区id
        if (userDetails.getSys_role_id() == 1) {//超级管理员
            return indexTbService.getAllUsedIndexTb();
        }
        //非超级管理员
        //存放当前及上级地区的id
        String indexTbKey = RedisKeys.INDEX_LIST_AREA + "messageSubmission" + userAreaId;
        List<IndexTb> indexTbList;//存放用户能看到的指标大类
        if (RedisUtil.isEmpty(indexTbKey)) {
            List<Integer> sysAreaIs = sysAreaService.getAllUpAreaIds(userAreaId);
            indexTbList = indexTbService.queryIndexBySysAreaIds(null, sysAreaIs);
            RedisUtil.setData(indexTbKey, indexTbList);
        } else {
            Type indexTbType = new TypeToken<List<IndexTb>>() {
            }.getType();
            indexTbList = RedisUtil.getListData(indexTbKey, indexTbType);
        }
        return indexTbList;
    }

    /**
     * 日志记录
     *
     * @param userCondition
     * @param logSql
     * @param dataCount
     * @throws Exception
     */
    private void log(String userCondition, String logSql, long dataCount, boolean isTrue, HttpServletRequest request) throws Exception {
        sysUserLogService.insertOneLog(new SysUserLog("报文报送", null, null,
                null, null, null, SysUserLogService.SELECT, Integer.valueOf(String.valueOf(dataCount)),
                userCondition, logSql, "/admin/messageSubmission/sysQuery.jhtml",
                null, null, isTrue), request);
    }
}
