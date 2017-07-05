package com.workmanagement.controller;

import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.*;
import com.workmanagement.service.*;
import com.workmanagement.util.DicExcelOut;
import com.workmanagement.util.LoggerUtil;
import com.workmanagement.util.PageSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 数据字典的controller
 * Created by lzm on 2017/3/9.
 */
@Controller()
@RequestMapping(value = "/admin/dataCount")
public class DataCountController {

    @Autowired
    private SysClassFyService sysClassFyService;
    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private DicService dicService;
    @Autowired
    private DicContentService dicContentService;
    @Autowired
    private SysManageLogService sysManageLogService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private SysUserLogService sysUserLogService;
    @Autowired
    private RelateInfoService relateInfoService;

    /**
     * 根据地区显示不同的查询结果
     */
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, HttpSession session) throws Exception {
        return "dataCount/list";
    }
    @RequestMapping(value = "/searchcc")
    public String searchcc(HttpServletRequest request, HttpSession session) throws Exception {
        return "dataCount/search";
    }
    @RequestMapping(value = "/count")
    public String count(HttpServletRequest request, HttpSession session) throws Exception {
        return "dataCount/count";
    }
    @RequestMapping(value = "/show")
    public String show(HttpServletRequest request, HttpSession session) throws Exception {
        return "dataCount/show";
    }
    @RequestMapping(value = "/show2")
    public String show2(HttpServletRequest request, HttpSession session) throws Exception {
        return "dataCount/show2";
    }
    @RequestMapping(value = "/sjgl")
    public String sjgl(HttpServletRequest request, HttpSession session) throws Exception {
        return "dataCount/sjgl";
    }
    @RequestMapping(value = "/fjgbszljc")
    public String fjgbszljc(HttpServletRequest request, HttpSession session) throws Exception {
        return "dataCount/fjgbszljc";
    }@RequestMapping(value = "/fjgbsgmtj")
     public String fjgbsgmtj(HttpServletRequest request, HttpSession session) throws Exception {
        return "dataCount/fjgbsgmtj";
    }

    private void reSetName(List<Dic> dicList, Integer areaId) {
        if (CollectionUtils.isNotEmpty(dicList)) {
            for (Dic dic : dicList) {
                if (areaId != null) {
                    if (dic.getSysAreaId().intValue() == areaId.intValue()) {
                        dic.setIsThisArea("是的");
                    }
                }
                String areaName = sysAreaService.getAreaNotSub(dic.getSysAreaId()).getSysAreaName();
                if (!StringUtils.equals(areaName, "四川省")) {
                    dic.setDicName(areaName + "-" + dic.getDicName());
                }
            }
        }
    }

    /**
     * 直接跳转到编辑页面
     */
    @RequestMapping(value = "/add")
    public String add() {
        return "dic/edit";
    }

    /**
     * 导出时校验权限
     */
    @RequestMapping(value = "/isAdmin")
    @ResponseBody
    public String isAdmin() {
        return "操作成功";
    }

    /**
     * 数据字典详情是【查看】或者【修改】操作
     */
    @RequestMapping(value = "/edit")
    public String edit(Model model, @RequestParam Integer id, @RequestParam(required = false, value = "show") String show) throws Exception {
        //获取到页面点击后传递过来的单个数据字典的id（主键），作查询
        Dic dic = dicService.getDicByDicId(id);//根据主键获取单条数据
        List<DicContent> dicContentList = dicContentService.getDicContentsByDicId(dic.getDicId());
        model.addAttribute("dic", dic);
        model.addAttribute("dicContentList", dicContentList);//把数据传递出去
        if (show != null) {//点击的（查看按钮）
            return "dic/show";
        } else {//点击的（修改按钮）
            return "dic/edit";
        }
    }

    /**
     * 数据字典详情页目录验证
     */
    @RequestMapping(value = "/addVer")
    @ResponseBody
    public Map<String, Object> addVer(@RequestParam String code, @RequestParam Integer ddpid,
                                      @RequestParam String dicContentValue) throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 代码是否重复
        if (dicContentService.isDicContentValueBeUsed(ddpid, dicContentValue)) {//如果查到了数据，表示重名
            map.put("addVer", false);
            map.put("name", false);
        }
        if (dicContentService.isDicContentCodeBeUsed(ddpid, code)) {//如果查到了数据表示重名
            map.put("addVer", false);
            map.put("code", false);
        }
        return map;
    }

    /**
     * 更新数据字典详情
     *
     * @param dicId
     * @param contentId
     * @param dicContentValue
     * @return
     */
    @RequestMapping(value = "/updDicContent")
    @ResponseBody
    public String updDicContent(@RequestParam Integer dicId, @RequestParam Integer contentId,
                                @RequestParam String dicContentValue) {
        dicContentValue = StringUtils.trimToEmpty(dicContentValue);
        if (dicContentService.isDicContentCodeBeUsed(dicId, dicContentValue)) {
            return "该指标值已存在,请换个名称";
        }
        boolean b = dicContentService.updDicContent(contentId, dicContentValue);
        if (b) {
            return "操作成功";
        } else {
            return "操作失败";
        }
    }

    /**
     * 全部导出的功能
     */
    @RequestMapping(value = "/exportAll")
    public void exportAll(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        List<Dic> list;
        if (userDetails.getSys_role_id() == 1) {
            list = dicService.getAllDicNotTree(null);
        } else {
            List<Integer> areaIds = sysAreaService.getAllUpAreaIds(userDetails.getSysOrg().getSys_area_id());
            list = dicService.getDicsBySysAreaId(null, areaIds);
        }
        List<ExcelDic> excel = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {//如果有数据字典
            this.reSetName(list, null);
            this.reSetExcel(list, excel);
            this.excelOut("全部数据字典", request, response, excel);
        }
    }

    /**
     * 单个导出的功能
     */
    @RequestMapping(value = "/exportOne")
    public void exportOne(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer ddpid) throws Exception {
        List<ExcelDic> list = new ArrayList<>();
        List<DicContent> dicContentList = dicContentService.getDicContentsByDicId(ddpid);
        Dic dic = dicService.getDicByDicId(ddpid);
        List<Dic> dicList = new ArrayList<>();
        dicList.add(dic);
        this.reSetName(dicList, null);
        dic = dicList.get(0);
        if (CollectionUtils.isNotEmpty(dicContentList)) {
            //如果有子集再遍历
            for (DicContent d : dicContentList) {
                list.add(new ExcelDic(dic.getDicName(), dic.getDicNotes(), d.getDicContentCode(), d.getDicContentValue()));
            }
        } else {//没有子集就直接加名字
            list.add(new ExcelDic(dic.getDicName()));
        }
        this.excelOut(dic.getDicName(), request, response, list);
    }

    @RequestMapping(value = "/outSearch")
    public void outSearch(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Dic> list = (List<Dic>) session.getAttribute("dataTypeQueryDicInfoDicList");
        List<ExcelDic> excel = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {//如果有数据字典
            this.reSetExcel(list, excel);
            String[] rowNames = {"字典名称", "备注", "指标值", "字典代码"};
            String[] propertyNames = {"dicName", "dicNotes", "dicContentValue", "dicContentCode"};
            // 生成excel
            DicExcelOut<ExcelDic> excelExport = new DicExcelOut<>();
            excelExport.setTitle("查询出的数据字典");
            excelExport.setRowNames(rowNames);
            excelExport.setPropertyNames(propertyNames);
            excelExport.setList(excel);
            String fileName = excelExport.exportExcel(request, response);
            sysUserLogService.insertOneLog(new SysUserLog("数据字典查询", null,
                    null, null, null, null, SysUserLogService.EXPORT,
                    list.size(), null, null, null, fileName,
                    null, true), request);
        }
    }

    private void reSetExcel(List<Dic> list, List<ExcelDic> excel) throws Exception {
        for (Dic dic : list) {//遍历数据字典
            List<DicContent> dicContentList = dicContentService.getDicContentsByDicId(dic.getDicId());
            if (CollectionUtils.isNotEmpty(dicContentList)) {
                //如果有子集再遍历
                for (DicContent d : dicContentList) {
                    excel.add(new ExcelDic(dic.getDicName(), dic.getDicNotes(), d.getDicContentCode(), d.getDicContentValue()));
                }
            } else {
                //没有子集就直接加名字
                excel.add(new ExcelDic(dic.getDicName()));
            }
        }
    }

    /**
     * 增改提交,放在一起搞
     */
    @RequestMapping(value = "/editSubmit")
    @ResponseBody
    public JsonResWrapper editSubmit(Dic dic, Integer ddpid, String[] dicContentValue,
                                     String[] dicContentCode, HttpSession session,
                                     HttpServletRequest request) throws Exception {
        /**
         * 可能会有的操作分类及其要求：(前台页面已经验证好了规则，只管数据插入)
         *      1.这是一个新增数据字典的功能
         *          要求：1.只能一个一个新增，
         *               2.新增可能会包含一个或多个的字典详情页的数据
         *      2.修改一个数据字典的功能
         *          要求：1.名字可以改变，但不能和数据库查询的重名
         *               2.字典详情页只能删除不能修改
         *   判断条件：公共：dic只会有一个属性：dicName
         *      1.ddpid为空，俩string数组有可能会有数据，后面俩没数据
         *      2.ddpid为当前数据字典的id，数组里可能会有数据，后面俩没有数据
         */
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        List<Integer> areaIds = sysAreaService.getAllUpAreaIds(userDetails.getSysOrg().getSys_area_id());

        JsonResWrapper jrw = new JsonResWrapper();

        if (StringUtils.equals(dic.getDicName(), "地区") || StringUtils.equals(dic.getDicName(), "金融机构或者政府部门")
                || StringUtils.equals(dic.getDicName(), "经济行业")) {
            jrw.setFlag(false);
            jrw.setMessage("不能使用\"地区\",\"经济行业\",\"金融机构或者政府部门\"这三个名称作为数据字典名称");
            this.log(null, null, SysManageLog.INSERT_SYSMANAGElOG, false, 1, null, request);
        } else {
            if (dic.getDicId() == null && ddpid == null) {//说明这是一个新增操作
//            if (dicContentValue == null) {
//                jrw.setFlag(false);
//                jrw.setMessage("该数据字典下必须有值");
//            } else {
                if (!dicService.isDicNameBeUsedInDic(dic.getDicName(), userDetails.getSysOrg().getSys_area_id())) {//如果没有查到有重名
                    Dic dic1 = new Dic(areaIds.get(0), dic.getDicName());
                    dic1.setDicNotes(dic.getDicNotes());
                    boolean isTrue = dicService.insertOneDic(dic1);//先新增数据字典
                    this.log(null, dic1.toString(), SysManageLog.INSERT_SYSMANAGElOG, isTrue, 1, null, request);
                    if (ArrayUtils.isNotEmpty(dicContentValue)) {//表示有要新增的字典详情表数据
                        Integer dicId = dic1.getDicId();//获取到刚刚插入数据库的那条数据主键
                        this.insertData(dicId, dic.getDicName(), dicContentValue, dicContentCode, request);
                    }
                } else {//如果数据字典名字有重复
                    jrw.setFlag(false);
                    jrw.setMessage("字典名称已存在");
                    this.log(null, null, SysManageLog.INSERT_SYSMANAGElOG, false, 1, null, request);
                }
//            }
            } else {//说明这是一个修改操作
//            List<DicContent> dicContentsByDicId = dicContentService.getDicContentsByDicId(ddpid);
//            if (dicContentValue == null && dicContentsByDicId == null) {
//                jrw.setFlag(false);
//                jrw.setMessage("该数据字典下必须有值");
//            } else {
                Dic dic2 = dicService.getDicByDicId(ddpid);
                if (dic2.getSysAreaId().intValue() != userDetails.getSysOrg().getSys_area_id().intValue()) {
                    //表示没有修改权限
                    jrw.setFlag(false);
                    jrw.setMessage("操作失败,不能操作非本地区的数据字典");
                } else {
                    //先判断名字是否有修改，在判断有没有输入数据
                    Dic oldDic = dicService.getDicByDicId(ddpid);
                    if (StringUtils.equals(oldDic.getDicName(), dic.getDicName())) {
                        //名字没有修改时候进入,如果有输入数据
                        //如果备注有修改就改备注
                        if (!StringUtils.equals(oldDic.getDicNotes(), dic.getDicNotes())) {
                            String oldValue = oldDic.toString();
                            oldDic.setDicNotes(dic.getDicNotes());
                            dicService.updateOneDic(oldDic);
                            String newValue = oldDic.toString();
                            this.log(oldValue, newValue, SysManageLog.UPDATE_SYSMANAGElOG, true, 1, null, request);
                        }
                        if (dicContentValue != null) {
                            //进行数据新增
                            this.insertData(ddpid, dic.getDicName(), dicContentValue, dicContentCode, request);
                        }
                    } else {//表示改了名字
                        if (!dicService.isDicNameBeUsedInDic(dic.getDicName(), userDetails.getSysOrg().getSys_area_id())) {//如果没有查到有重名
                            Dic dic1 = dicService.getDicByDicId(ddpid);//老的
                            dic.setDicId(ddpid);//再设置她的主键
                            String oldValue = dic1.toString();
                            if (!StringUtils.equals(dic1.getDicNotes(), dic.getDicNotes())) {
                                //如果备注有修改就改备注
                                dic.setDicNotes(dic.getDicNotes());
                            }
                            dicService.updateOneDic(dic);//然后更新数据库
                            String newValue = dicService.getDicByDicId(ddpid).toString();
                            this.log(oldValue, newValue, SysManageLog.UPDATE_SYSMANAGElOG, true, 1, null, request);
                            if (dicContentValue != null) {//表示有要新增的字典详情表数据
                                //先加入字典详情表
                                this.insertData(ddpid, newValue, dicContentValue, dicContentCode, request);
                            }
                        } else {//查到有重名
                            jrw.setFlag(false);
                            jrw.setMessage("字典名称已存在");
                            this.log(null, null, SysManageLog.UPDATE_SYSMANAGElOG, false, 1, null, request);
                        }
                    }
                }
//            }
            }
        }

        return jrw;
    }

    /**
     * 删除数据字典详情表数据
     */
    @RequestMapping(value = "/isDelete")
    @ResponseBody
    public Map<String, Object> isDelete(@RequestParam(required = false) Integer dicContentId,
                                        @RequestParam(required = false) Integer dicId, HttpSession session,
                                        HttpServletRequest request) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        Integer areaIds = userDetails.getSysOrg().getSys_area_id();
        Map<String, Object> map = new HashMap<>();

        Dic dic = dicService.getDicByDicId(dicId);
        if (dic.getSysAreaId().intValue() != areaIds.intValue()) {
            map.put("isIng", false);
            map.put("msg", "删除失败,只能删除本地区的数据字典");
            this.log(null, null, SysManageLog.DELECT_SYSMANAGElOG, false, 1, null, request);
            return map;
        }
        boolean isDicContentBeUsed = dicContentService.isThisBeUsed(dicContentId);
        if (isDicContentBeUsed) {
            map.put("msg", "删除失败,该字典已被引用");
            this.log(null, null, SysManageLog.DELECT_SYSMANAGElOG, false, 1, null, request);
        } else {
            map.put("msg", "操作成功");
            DicContent content = dicContentService.getDicContentById(dicContentId);
            String oldValue = "指标值：" + content.getDicContentValue() + "," + "字典代码" + content.getDicContentCode();
            this.log(oldValue, null, SysManageLog.DELECT_SYSMANAGElOG, true, 1, null, request);
            dicContentService.delOneDicContentById(dicContentId);
        }
        map.put("isIng", !isDicContentBeUsed);
        return map;
    }

    /**
     * 删除数据字典这个大类
     *
     * @param model
     * @param ddpid 要被删除的主键
     * @return
     */
    @RequestMapping(value = "/delete")
    public String delete(Model model, @RequestParam(required = false) Integer ddpid,
                         HttpSession session, HttpServletRequest request) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        Integer areaIds = userDetails.getSysOrg().getSys_area_id();
        if (dicService.getDicByDicId(ddpid).getSysAreaId().intValue() != areaIds.intValue()) {
            model.addAttribute("msg", "删除失败,只能删除本地区的数据字典");
            this.log(null, null, SysManageLog.DELECT_SYSMANAGElOG, false, 1, null, request);
        } else {
            //先查有没有子集
            List<DicContent> list = dicContentService.getDicContentsByDicId(ddpid);
            if (CollectionUtils.isNotEmpty(list)) {
                model.addAttribute("msg", "删除失败,请先删除该字典下的值");
                this.log(null, null, SysManageLog.DELECT_SYSMANAGElOG, false, 1, null, request);
            } else {//再查自己如果被使用了
                if (dicService.isThisBeUsed(ddpid)) {
                    model.addAttribute("msg", "删除失败,该字典已被引用");
                    this.log(null, null, SysManageLog.DELECT_SYSMANAGElOG, false, 1, null, request);
                } else {
                    model.addAttribute("msg", "操作成功");
                    Dic dic = dicService.getDicByDicId(ddpid);
                    this.log(dic.toString(), null, SysManageLog.DELECT_SYSMANAGElOG, true, 1, null, request);
                    dicService.deleteByDicId(ddpid);
                }
            }
        }
        return "forward:list.jhtml";
    }

    //通过dicId得到所有字典目录
    @RequestMapping("/getDicContentsByDicIdJson")
    @ResponseBody
    public Map<String, Object> getDicContentsByDicIdJson(@RequestParam(required = false) Integer dicId) {
        Map<String, Object> map = new HashMap<>();
        Dic dic = dicService.getDicById(dicId);
        List<DicContent> dicContentList = dic.getDicContentList();
        map.put("dicContentList", dicContentList);
        return map;
    }

    /**
     * 跳转页面
     *
     * @return
     */
    @RequestMapping("/dicInfo")
    public String dicInfo(HttpSession session) {
        session.removeAttribute("dataTypeQuery_dicInfo_search");
        return "/dataTypeQuery/dicInfo";
    }

    @RequestMapping("/search")
    public ModelAndView search(String dicName, HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView view = new ModelAndView("/dataTypeQuery/dicInfo");
        StringBuilder sb = new StringBuilder("select * from dic_tb where 1=1 ");
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        List<Integer> areaIds;
        if (userDetails.getSys_role_id() == 1) {
            areaIds = sysAreaService.getAllSubAreaIds(userDetails.getSysOrg().getSys_area_id());
        } else {
            areaIds = sysAreaService.getAllUpAreaIds(userDetails.getSysOrg().getSys_area_id());
        }
        PageSupport ps = PageSupport.initPageSupport(request);
        List<Dic> dicList;
        if (StringUtils.isNotBlank(dicName)) {
            dicList = dicService.getDicByLikeName(ps, dicName, areaIds);
            sb.append(" and dic_name like '%").append(dicName).append("%' ");
        } else {
            dicList = dicService.getDicsBySysAreaId(ps, areaIds);
        }
        reSetName(dicList, null);
        view.addObject("dicList", dicList);

        sb.append(dicName).append(" and sys_area_id in (");
        for (Integer i : areaIds) {
            sb.append(i).append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(",")).append(") and dic_name not in('金融机构或者政府部门','地区','经济行业')");

        boolean isOk = true;
        if (CollectionUtils.isEmpty(dicList)) {
            isOk = false;
        }
        view.addObject("dicName", dicName);
        if (StringUtils.isBlank(dicName)) {
            dicName = "shishilani";
        }
        Object dataTypeQuery_dicInfo_search = session.getAttribute("dataTypeQuery_dicInfo_search");
        if (dataTypeQuery_dicInfo_search == null) {
            //第一次查询
            session.setAttribute("dataTypeQuery_dicInfo_search", dicName);
            sysUserLogService.insertOneLog(new SysUserLog("数据字典查询", null, null,
                    null, null, null, SysUserLogService.SELECT,
                    (int) (ps.getTotalRecord()), StringUtils.equals(dicName, "shishilani") ? "" : dicName, StringUtils.replace(sb.toString(), " ", "|"),
                    "/admin/dic/dicInfoCopy.jhtml",
                    null, null, isOk), request);
        } else {
            //第二次进来
            if (!StringUtils.equals(String.valueOf(dataTypeQuery_dicInfo_search), dicName)) {
                //如果条件不一样
                sysUserLogService.insertOneLog(new SysUserLog("数据字典查询", null, null,
                        null, null, null, SysUserLogService.SELECT,
                        (int) (ps.getTotalRecord()), StringUtils.equals(dicName, "shishilani") ? "" : dicName, StringUtils.replace(sb.toString(), " ", "|"),
                        "/admin/dic/dicInfoCopy.jhtml",
                        null, null, isOk), request);
            }
        }
        if (CollectionUtils.isNotEmpty(dicList)) {
            session.setAttribute("dataTypeQueryDicInfoDicList", dicList);
        }
        return view;
    }

    @RequestMapping("/dicInfoCopy")
    public ModelAndView dicInfoCopy(Model model, HttpServletRequest request, HttpSession session) {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        ModelAndView view = new ModelAndView("/dataTypeQuery/dicInfoCopy");
        PageSupport ps = PageSupport.initPageSupport(request);
        Object query = request.getAttribute("querySql");
        String querySql;
        if (query == null) {
            //表示是第二次进来
            querySql = String.valueOf(session.getAttribute("dic_dicInfoCopy_sql"));
        } else {
            querySql = String.valueOf(query);
            session.setAttribute("dic_dicInfoCopy_sql", querySql);
        }
        querySql = StringUtils.replace(querySql, "|", " ");
        List<Map<String, Object>> maps = relateInfoService.queryMoreData(ps, querySql);
        if (CollectionUtils.isNotEmpty(maps)) {
            this.reSetNameByMap(maps, userDetails.getSysOrg().getSys_area_id());
        }
        model.addAttribute("dicList", maps);
        return view;
    }

    private void reSetNameByMap(List<Map<String, Object>> maps, Integer areaId) {
        for (Map<String, Object> map : maps) {
            Integer sysAreaId = MapUtils.getInteger(map, "SYS_AREA_ID");
            String dicName = MapUtils.getString(map, "DIC_NAME");
            String areaName = sysAreaService.getAreaNotSub(sysAreaId).getSysAreaName();
            if (!StringUtils.equals(areaName, "四川省")) {
                dicName = (sysAreaService.getAreaNotSub(areaId).getSysAreaName() + "-" + dicName);
                map.put("DIC_NAME", dicName);
            }
        }
    }

    /**
     * 抽取出来的导出功能
     *
     * @param request
     * @param response
     * @param list
     * @return
     */
    private void excelOut(String fileNames, HttpServletRequest request, HttpServletResponse response, List<ExcelDic> list) {
        try {
            String[] rowNames = {"字典名称", "备注", "指标值", "字典代码"};
            String[] propertyNames = {"dicName", "dicNotes", "dicContentValue", "dicContentCode"};
            // 生成excel
            DicExcelOut<ExcelDic> excelExport = new DicExcelOut<>();
            excelExport.setTitle(fileNames);
            excelExport.setRowNames(rowNames);
            excelExport.setPropertyNames(propertyNames);
            excelExport.setList(list);
            String fileName = excelExport.exportExcel(request, response);
            this.log(null, null, SysManageLog.EXPORT_SYSMANAGElOG, true, list.size(), fileName, request);
        } catch (Exception e) {
            LoggerUtil.error(e);
        }
    }

    //批量新增数据字典详情表
    private void insertData(Integer ddpid, String dicName, String[] dicContentValue, String[] dicContentCode, HttpServletRequest request) throws Exception {
        for (int i = 0; i < dicContentCode.length; i++) {
            DicContent dicContent = new DicContent(ddpid, dicContentCode[i], dicContentValue[i]);
            boolean isTrue = dicContentService.insertOneContent(dicContent);
            this.log(null, ("字典名称:<" + dicName + ">下:" + dicContent.toString()), SysManageLog.INSERT_SYSMANAGElOG, isTrue, 1, null, request);
        }
    }

    //日志记录
    private void log(String oldValue, String newValue, Integer type, boolean isTrue, int logCount, String logFile, HttpServletRequest request) {
        sysManageLogService.insertSysManageLogTb(new SysManageLog("数据字典", null, null, null,
                oldValue, newValue, new Date(), type, logCount, null, null,
                null, logFile, null, isTrue), request);
    }

    @RequestMapping("/getDicType")
    @ResponseBody
    public Integer getDicType(Integer id) {
        Dic dic = dicService.getDicByDicId(id);
        if (dic != null) {
            if (dic.getDicName().equals("金融机构或者政府部门"))
                return 1;
            else if (dic.getDicName().equals("经济行业"))
                return 2;
            else if (dic.getDicName().equals("地区"))
                return 3;
            else
                return 4;
        }
        return 5;

    }

    @RequestMapping("/getDicClassFy")
    @ResponseBody
    public List<SysClassFyModel> getDicClassFy() {
        return sysClassFyService.queryAllSysClassFy1();
    }

    @RequestMapping("/getArea")
    @ResponseBody
    public SysArea getArea(HttpSession session) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");


        return sysAreaService.queryAreaById(sysOrgService.getByIdNotHaveSub(userDetails.getSys_org_id()).getSys_area_id());
    }


}
