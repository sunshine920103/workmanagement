package com.workmanagement.controller;

import com.workmanagement.model.*;
import com.workmanagement.service.DefaultIndexItemService;
import com.workmanagement.service.RelateInfoService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysUserLogService;
import com.workmanagement.util.DicExcelOut;
import com.workmanagement.util.PageSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 关联信息查询
 * Created by lzm on 2017/3/23.
 */
@Controller
@Scope("session")
@RequestMapping("/admin/relatedInfo")
public class RelatedInfoController {

    @Autowired
    private DefaultIndexItemService defaultIndexItemService;
    @Autowired
    private SysUserLogService sysUserLogService;
    @Autowired
    private RelateInfoService relateInfoService;
    @Autowired
    private SysAreaService sysAreaService;

    // 上报时间
    private String indexDwdbxxRecordTime;
    //查询结果
    private DefaultIndexItemCustom defaultIndexItemCustom;
    //显示图片
    private List<ShowImg> showImgs;
    //查询结果
    private List<DefaultIndexItemCustom> defaultIndexItemCustomList;
    private List<DefaultIndexItemCustom> queryDefaultIndexItemCustomList;
    private String sysAreaIdsString;
    private Integer upAreaId;
    private boolean isAdmin = false;
    private String sysAllSubArea;


    /**
     * 跳转首页，啥也不干
     *
     * @return
     */
    @RequestMapping("/list")
    public String list() {
        return "relatedInfo/list";
    }

    /**
     * 查询结果集
     *
     * @param model
     * @param enterpriseCode
     * @return
     */
    @RequestMapping("/query")
    public String query(Model model, String enterpriseCode, HttpSession session,
                        HttpServletRequest request) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        Integer sysAreaId = userDetails.getSysOrg().getSys_area_id();
        upAreaId = sysAreaService.getUpOrThisSysArea(sysAreaId).getSysAreaId();
        String query = "企业二码：" + enterpriseCode;
        List<Integer> allSubAreaIds = sysAreaService.getAllSubAreaIds(upAreaId);
        StringBuilder sb = new StringBuilder();
        for (Integer i : allSubAreaIds) {
            sb.append(i).append(",");
        }
        sysAllSubArea = sb.deleteCharAt(sb.lastIndexOf(",")).toString();
//        DefaultIndexItem defaultIndexItem = null;
        if (StringUtils.isBlank(enterpriseCode)) {
            model.addAttribute("msg", "请输入正确的 统一社会信用代码/组织机构代码");
            return "relatedInfo/list";
        }
        if (userDetails.getSys_role_id() == 1) {
            isAdmin = true;
        }
        StringBuilder querySql = new StringBuilder("select * from default_index_item_tb");
        querySql.append(" where default_index_item_id in(select DISTINCT default_index_item_id from default_index_item_tb")
                .append(" where (code_credit = '").append(enterpriseCode).append("' or code_org = '")
                .append(enterpriseCode).append("') and combine_status = 1) and sys_area_id in(")
                .append(sysAllSubArea).append(")");
//        String querySql = "select * from default_index_item_tb where code_credit='" +
//                enterpriseCode + "' and sys_area_id in(" + sysAreaIdsString + ") and combine_status = 1 or code_org='" +
//                enterpriseCode + "' and sys_area_id in(" + sysAreaIdsString + ") and combine_status = 1";
        PageSupport ps = PageSupport.initPageSupport(request);
        List<Map<String, Object>> maps = relateInfoService.queryMoreData(ps, querySql.toString());
        String stringQuerySql = StringUtils.replace(querySql.toString(), " ", "|");
        if (CollectionUtils.isNotEmpty(maps)) {
            this.setData(maps);
        } else {
            this.log(SysUserLogService.SELECT, query, stringQuerySql, 0, false, null, request);
            model.addAttribute("msg", "未查询到企业信息");
            return "relatedInfo/list";
        }
//        DefaultIndexItem byCredit = defaultIndexItemService.getByCredit(enterpriseCode, sysAreaId);
//        if (byCredit == null) {
//            List<DefaultIndexItem> list = defaultIndexItemService.getByCodeOrg(enterpriseCode, sysAreaId);
//            if (CollectionUtils.isEmpty(list)) {
//                this.log(SysUserLogService.SELECT, query, querySql, 0, false, null);
//                model.addAttribute("msg", "未查询到企业信息");
//                return "relatedInfo/list";
//            } else {
//                defaultIndexItem = list.get(0);
//            }
//        } else {
//            defaultIndexItem = byCredit;
//        }
//        defaultIndexItemCustom = new DefaultIndexItemCustom();
//        defaultIndexItemCustom.setCodeCredit(StringUtils.trimToEmpty(defaultIndexItem.getCodeCredit()));//统一社会信用代码
//        defaultIndexItemCustom.setCodeOrg(StringUtils.trimToEmpty(defaultIndexItem.getCodeOrg()));//组织机构代码
//        defaultIndexItemCustom.setDefaultIndexItemId(defaultIndexItem.getDefaultIndexItemId());//二码表主键
//        defaultIndexItemCustom.setQymc(this.getQymc(defaultIndexItem.getDefaultIndexItemId()));//企业名称
//        defaultIndexItemCustom.setFddbr(this.getFrdb(defaultIndexItem.getDefaultIndexItemId()));//法人名字
        model.addAttribute("queryDefaultIndexItemCustomList", queryDefaultIndexItemCustomList);
        this.log(SysUserLogService.SELECT, query, stringQuerySql, maps.size(), true, null, request);
        return "relatedInfo/list";
    }

    /**
     * 管理员查看的查询结果集
     *
     * @param model
     * @return
     */
    @RequestMapping("/sysQuery")
    public String sysQuery(Model model, HttpServletRequest request, HttpSession session) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        if (userDetails.getSys_role_id() == 1) {
            isAdmin = true;
        }
        PageSupport ps = PageSupport.initPageSupport(request);
        String querySql = (String) request.getAttribute("querySql");
        String sql = StringUtils.replace(querySql, "|", " ");
        List<Map<String, Object>> maps = relateInfoService.queryMoreData(ps, sql);
        if (CollectionUtils.isNotEmpty(maps)) {
            this.setData(maps);
        }
        model.addAttribute("queryDefaultIndexItemCustomList", queryDefaultIndexItemCustomList);
        return "relatedInfo/copyList";
    }

    private void setData(List<Map<String, Object>> maps) {
        queryDefaultIndexItemCustomList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            DefaultIndexItemCustom defaultIndexItemCustom = new DefaultIndexItemCustom();
            String areaName = sysAreaService.queryAreaById(MapUtils.getInteger(map, "SYS_AREA_ID")).getSysAreaName();
            defaultIndexItemCustom.setIsSon(areaName);//设置归属地区
            defaultIndexItemCustom.setCodeCredit(MapUtils.getString(map, "CODE_CREDIT"));
            defaultIndexItemCustom.setCodeOrg(MapUtils.getString(map, "CODE_ORG"));
            Integer defaultIndexItemId = MapUtils.getInteger(map, "DEFAULT_INDEX_ITEM_ID");
            defaultIndexItemCustom.setDefaultIndexItemId(defaultIndexItemId);
            defaultIndexItemCustom.setQymc(getQymc(defaultIndexItemId));
            defaultIndexItemCustom.setFddbr(getFrdb(defaultIndexItemId));
            defaultIndexItemCustom.setSys_area_id(MapUtils.getInteger(map, "SYS_AREA_ID"));
            queryDefaultIndexItemCustomList.add(defaultIndexItemCustom);
        }
    }

    /**
     * 点击详情，展示数据，用户选择要展示的信息。
     *
     * @param model
     * @return
     */
    @RequestMapping("/chooseType")
    public String chooseType(Model model, @RequestParam Integer defaultIndexItemId) {
        DefaultIndexItem dii = defaultIndexItemService.queryById(defaultIndexItemId);
        defaultIndexItemCustom = new DefaultIndexItemCustom();
        defaultIndexItemCustom.setCodeCredit(dii.getCodeCredit());
        defaultIndexItemCustom.setCodeOrg(dii.getCodeOrg());
        defaultIndexItemCustom.setDefaultIndexItemId(defaultIndexItemId);
        defaultIndexItemCustom.setQymc(getQymc(defaultIndexItemId));
        defaultIndexItemCustom.setFddbr(getFrdb(defaultIndexItemId));
        defaultIndexItemCustom.setSys_area_id(dii.getSys_area_id());
        defaultIndexItemCustom.setIsSon(sysAreaService.getAreaNotSub(dii.getSys_area_id()).getSysAreaName());
        model.addAttribute("defaultIndexItemCustom", defaultIndexItemCustom);

        setSysAreaIdsString(defaultIndexItemCustom);

        return "relatedInfo/chooseType";
    }

    private void setSysAreaIdsString(DefaultIndexItemCustom dii) {
        List<Integer> sysAreaIds = sysAreaService.getAllSubAreaIds(dii.getSys_area_id());
        StringBuilder sb = new StringBuilder();
        for (Integer i : sysAreaIds) {
            sb.append(i).append(",");
        }
        sysAreaIdsString = sb.deleteCharAt(sb.lastIndexOf(",")).toString();
        upAreaId = sysAreaService.getUpOrThisSysArea(dii.getSys_area_id()).getSysAreaId();
    }

    /**
     * 导出excel
     *
     * @return
     */
    @RequestMapping("/excelOut")
    public void excelOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] rowNames = {"统一社会信用代码", "组织机构代码", "企业名称", "关联关系", "地区"};
        String[] propertyNames = {"codeCredit", "codeOrg", "qymc", "relatedType", "isSon"};
        defaultIndexItemCustom.setRelatedType("当前企业");
        defaultIndexItemCustomList.add(0, defaultIndexItemCustom);
        DicExcelOut<DefaultIndexItemCustom> export = new DicExcelOut<>();
        export.setTitle("关联信息");
        export.setRowNames(rowNames);
        export.setPropertyNames(propertyNames);
        export.setList(defaultIndexItemCustomList);
        String fileName = export.outInfo(request, response);
        this.log(SysUserLogService.EXPORT, null, null, defaultIndexItemCustomList.size(), true, fileName, request);
    }

    /**
     * 点击查询关联结果，获取到结果
     *
     * @param model
     * @param indexDwdbxxRecordTime 归档时间
     * @param relatedTypes
     * @return
     */
    @RequestMapping("/result")
    public String result(Model model, String indexDwdbxxRecordTime, String[] relatedTypes) {
        this.indexDwdbxxRecordTime = indexDwdbxxRecordTime;
        // 操作对象
        defaultIndexItemCustomList = null;
        defaultIndexItemCustomList = this.getDefaultIndexItemCustomList(relatedTypes);
        String screen = Arrays.toString(relatedTypes).replace("[", "").replace("]", "");
        model.addAttribute("defaultIndexItemCustom", defaultIndexItemCustom);
        model.addAttribute("defaultIndexItemCustomList", defaultIndexItemCustomList);
        model.addAttribute("indexDwdbxxRecordTime", indexDwdbxxRecordTime);
        model.addAttribute("screen", screen);
        return "relatedInfo/result";
    }

    /**
     * 展示关系图谱
     *
     * @return
     */
    @RequestMapping("/relatedImg")
    public String relatedImg() {
        return "relatedInfo/relatedImg";
    }

    /**
     * 展示关系图谱
     *
     * @return
     */
    @RequestMapping("/relatedImgJson")
    @ResponseBody
    public List<ShowImg> relatedImgJson() {
        ShowImg si = new ShowImg();
        si.setQymc(defaultIndexItemCustom.getQymc());
        si.setGuanxi("本企业");
        si.setZbd("无");
        showImgs.add(si);
        return this.showImgs;
    }

    /**
     * 根据defaultIndexItemCustom得到企业关联信息集合 indexDwdbxxRecordTime上报截止时间可为空
     * relatedTypes 关联信息筛选
     *
     * @return
     */
    private List<DefaultIndexItemCustom> getDefaultIndexItemCustomList(String[] relatedTypes) {
        List<DefaultIndexItemCustom> customList = new ArrayList<>();
        showImgs = new ArrayList<>();
        for (String relatedType : relatedTypes) {
            String telatedType = StringUtils.trimToNull(relatedType);
            if (StringUtils.equals(telatedType, "投资关联")) {
                //对外投资,获取投资的企业    统一社会信用代码或者获取到个人的身份证号码
                List<String> dwtz;
                if (isAdmin) {
                    dwtz = new ArrayList<>();
                    for (DefaultIndexItemCustom d : queryDefaultIndexItemCustomList) {
                        List<String> dwtz1 = this.getDwtz(d.getDefaultIndexItemId());
                        this.setSysAreaIdsString(d);
                        if (CollectionUtils.isNotEmpty(dwtz1)) {
                            dwtz.addAll(dwtz1);
                        }
                        this.getBeiZbgc(d.getDefaultIndexItemId());//获取投资自己的出资人的名字到图谱上面
                        List<Integer> beiDwtz = this.getBeiDwtz();//被外部企业投资了,获取投资方的       二码主键
                        this.setData("投资关联(主动)", dwtz1, null, customList);
                        this.setData("投资关联(被动)", null, beiDwtz, customList);
                    }
                } else {
                    dwtz = this.getDwtz(defaultIndexItemCustom.getDefaultIndexItemId());
                    this.getBeiZbgc(defaultIndexItemCustom.getDefaultIndexItemId());//获取投资自己的出资人的名字到图谱上面
                    List<Integer> beiDwtz = this.getBeiDwtz();//被外部企业投资了,获取投资方的       二码主键
                    this.setData("投资关联(主动)", dwtz, null, customList);
                    this.setData("投资关联(被动)", null, beiDwtz, customList);
                }
            } else if (StringUtils.equals(telatedType, "担保关联")) {
                List<String> dwdb;
                if (isAdmin) {
                    dwdb = new ArrayList<>();
                    for (DefaultIndexItemCustom d : queryDefaultIndexItemCustomList) {
                        List<String> dwdb1 = this.getDwdb(d.getDefaultIndexItemId());
                        this.setSysAreaIdsString(d);
                        if (CollectionUtils.isNotEmpty(dwdb1)) {
                            dwdb.addAll(dwdb1);
                            List<Integer> beiDwdb = this.getBeiDwdb();
                            this.setData("担保关联(主动)", dwdb1, null, customList);
                            this.setData("担保关联(被动)", null, beiDwdb, customList);
                        }
                    }
                } else {
                    dwdb = this.getDwdb(defaultIndexItemCustom.getDefaultIndexItemId());
                    List<Integer> beiDwdb = this.getBeiDwdb();
                    this.setData("担保关联(主动)", dwdb, null, customList);
                    this.setData("担保关联(被动)", null, beiDwdb, customList);
                }
            } else if (StringUtils.equals(telatedType, "高管关联")) {// 是否为其他企业的高管
                List<Integer> ggxx;
                if (isAdmin) {
                    ggxx = new ArrayList<>();
                    for (DefaultIndexItemCustom d : queryDefaultIndexItemCustomList) {
                        List<Integer> ggxx1 = this.getGgxx(d.getDefaultIndexItemId());
                        this.setSysAreaIdsString(d);
                        if (CollectionUtils.isNotEmpty(ggxx1)) {
                            ggxx.addAll(ggxx1);
                        }
                        this.setData("高管关联", null, ggxx1, customList);
                    }
                } else {
                    ggxx = this.getGgxx(defaultIndexItemCustom.getDefaultIndexItemId());
                    this.setData("高管关联", null, ggxx, customList);
                }
            }
        }
        return customList;
    }

    /**
     * 获取企业名称（基本信息）
     *
     * @param indexItemId
     * @return
     */
    private String getQymc(Integer indexItemId) {
        String sql = "SELECT index_jbxx_qymc FROM index_jbxx_tb WHERE index_jbxx_qymc IS NOT NULL " +
                "AND default_index_item_id=" + indexItemId +
                " ORDER BY index_jbxx_id DESC fetch first 1 rows only";
        Map<String, Object> map = relateInfoService.queryData(sql);
        if (MapUtils.isEmpty(map)) {//如果没查出来
            return "(企业名称暂缺)";
        } else {
            return MapUtils.getString(map, "INDEX_JBXX_QYMC");
        }
    }

    /**
     * 获取法定代表人
     *
     * @param indexItemId
     * @return
     */
    private String getFrdb(Integer indexItemId) {// 高管类型1 为法人代表
        String sql = "SELECT index_ggxx_xm from index_ggxx_tb where index_ggxx_gglx=" +
                "(select dic_content_code from dic_content_tb where dic_id =" +
                "(select dic_id from dic_tb where dic_name='高管类型') and dic_content_value='法人代表')" +
                " AND default_index_item_id=" + indexItemId +
                " ORDER BY index_ggxx_id DESC fetch first 1 rows only";
        Map<String, Object> map = relateInfoService.queryData(sql);
        if (MapUtils.isEmpty(map)) {//如果没查出来
            return "(法定代表人暂缺)";
        } else {
            return MapUtils.getString(map, "INDEX_GGXX_XM");
        }
    }

    /*******************************************************投资关联**********************************************************************/

    /**
     * 对外投资,获取投资的企业     统一社会信用代码或者获取到个人的身份证号码
     *
     * @return
     */
    private List<String> getDwtz(Integer indexItemId) {
        String sql = "select distinct index_dwtzxx_dwtzqyzzjgdm from index_dwtzxx_tb where default_index_item_id=" +
                indexItemId + " AND RECORD_DATE <= '" + indexDwdbxxRecordTime + "'" +
                " and sys_area_id in (" + sysAreaIdsString + ")";
        return this.getStringData(sql, "INDEX_DWTZXX_DWTZQYZZJGDM");
    }

    /**
     * 被外部企业投资了,获取投资方的       二码主键
     *
     * @return
     */
    private List<Integer> getBeiDwtz() {
        String sql = "select distinct default_index_item_id from index_dwtzxx_tb where index_dwtzxx_dwtzqyzzjgdm in (" +
                "'" + getIn() + "')" + " AND record_date <= '" + indexDwdbxxRecordTime + "' " +
                "and sys_area_id in(" + sysAreaIdsString + ")";
        return this.getIntegerData(sql, "DEFAULT_INDEX_ITEM_ID");
    }

    /**
     * 资本构成（获取出资人的信息）,只可能是人
     *
     * @return
     */
    private void getBeiZbgc(Integer indexItemId) {
        String sql = "select distinct index_zbgc_czrmc from index_zbgc_tb where default_index_item_id=" +
                indexItemId + " AND record_date <= '" + indexDwdbxxRecordTime + "'" +
                " and sys_area_id in (" + sysAreaIdsString + ")";
        List<String> list = this.getStringData(sql, "INDEX_ZBGC_CZRMC");
        if (CollectionUtils.isNotEmpty(list)) {
            for (String s : list) {
                showImgs.add(new ShowImg(s, "资本构成", "被动"));
            }
        }
    }

    /*******************************************************担保关联**********************************************************************/

    /**
     * 对外担保（获取被担保方的证件号码）
     * 企业就是二码
     * 个人就是非企业二码
     *
     * @return
     */
    private List<String> getDwdb(Integer indexItemId) {
        String sql = "select distinct index_dwdbxx_bdbrzjhm from index_dwdbxx_tb where default_index_item_id="
                + indexItemId + " AND record_date <= '" + indexDwdbxxRecordTime + "'" +
                " and sys_area_id in (" + sysAreaIdsString + ")";
        return this.getStringData(sql, "INDEX_DWDBXX_BDBRZJHM");
    }

    /**
     * 被对外担保（获取担保方的证件号码）
     * 企业就是二码
     *
     * @return
     */
    private List<Integer> getBeiDwdb() {
        String sql = "select distinct default_index_item_id from index_dwdbxx_tb where index_dwdbxx_bdbrzjhm in ('"
                + getIn() + "')" + " and record_date <= '" + indexDwdbxxRecordTime + "' " +
                "and sys_area_id in(" + sysAreaIdsString + ")";
        return this.getIntegerData(sql, "DEFAULT_INDEX_ITEM_ID");
    }


    /*******************************************************高管关联**********************************************************************/

    /**
     * 高管关联（获取高管有的其他公司的二码主键）
     *
     * @return
     */
    private List<Integer> getGgxx(Integer indexItemId) {
        String sql = "select distinct default_index_item_id from index_ggxx_tb where index_ggxx_zjhm in (" +
                "select distinct index_ggxx_zjhm from index_ggxx_tb where index_ggxx_zjlx = '0' " +
                " and default_index_item_id = " + indexItemId +
                " and record_date <= '" + indexDwdbxxRecordTime + "')" +
                " and default_index_item_id <> " + indexItemId +
                " and sys_area_id in (" + sysAreaIdsString + ")";
        return this.getIntegerData(sql, "DEFAULT_INDEX_ITEM_ID");
    }

    /*******************************************************工具函数**********************************************************************/

    private String getIn() {
        String in;
        if (defaultIndexItemCustom.getCodeOrg() != null && defaultIndexItemCustom.getCodeCredit() != null) {
            in = defaultIndexItemCustom.getCodeOrg() + "','" + defaultIndexItemCustom.getCodeCredit();
        } else if (defaultIndexItemCustom.getCodeOrg() != null) {
            in = defaultIndexItemCustom.getCodeOrg();
        } else {
            in = defaultIndexItemCustom.getCodeCredit();
        }
        return in;
    }

    /**
     * 获取多个字符串的list
     *
     * @param sql
     * @param column
     * @return
     */
    private List<Integer> getIntegerData(String sql, String column) {
        List<Map<String, Object>> list = relateInfoService.queryMoreData(null, sql);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Integer> data = new ArrayList<>();
            for (Map<String, Object> m : list) {
                data.add(MapUtils.getInteger(m, column));
            }
            return data;
        }
        return null;
    }

    /**
     * 获取多个字符串的list
     *
     * @param sql
     * @param column
     * @return
     */
    private List<String> getStringData(String sql, String column) {
        List<Map<String, Object>> list = relateInfoService.queryMoreData(null, sql);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> data = new ArrayList<>();
            for (Map<String, Object> m : list) {
                data.add(MapUtils.getString(m, column));
            }
            return data;
        }
        return null;
    }

    /**
     * 给变量赋值
     *
     * @param relatedType
     * @param stringList
     * @param integerList
     * @param customList
     */
    private void setData(String relatedType, List<String> stringList, List<Integer> integerList,
                         List<DefaultIndexItemCustom> customList) {
        SysArea areaNotSub = sysAreaService.getAreaNotSub(upAreaId);
        if (CollectionUtils.isNotEmpty(stringList)) {//获取到  统一社会信用代码或组织机构代码 或者身份证号码
            for (String s : stringList) {
                /**
                 *👇的是企业,先查二码表里看看查得到数据不,如果能查到数据表示是一个企业,否则是个人
                 */
                List<DefaultIndexItem> defaultIndexItems = defaultIndexItemService.getByCreditOrCode(s, upAreaId);
                if (CollectionUtils.isNotEmpty(defaultIndexItems)) {
                    DefaultIndexItemCustom d = new DefaultIndexItemCustom();
                    DefaultIndexItem indexItem = defaultIndexItems.get(0);
                    String qymc = this.getQymc(indexItem.getDefaultIndexItemId());

                    d.setQymc(qymc);//企业名称
                    d.setCodeCredit(indexItem.getCodeCredit());
                    d.setCodeOrg(indexItem.getCodeOrg());
                    d.setRelatedType(relatedType);
                    d.setIsSon(areaNotSub.getSysAreaName());
                    d.setQyzs(areaNotSub.getSysAreaCode());
                    customList.add(d);

                    ShowImg si = new ShowImg();
                    if (relatedType.length() > 4) {//表示有主被动关系
                        si.setGuanxi(StringUtils.left(relatedType, 4));//截取前4位
                        si.setZbd(StringUtils.substring(relatedType, 5, 7));
                    } else {//表示是高管关联
                        si.setGuanxi(relatedType);
                    }
                    si.setQymc(areaNotSub.getSysAreaName() + ":" + qymc);
                    showImgs.add(si);
                } else {
                    /**
                     * 👇下面的是个人
                     */
                    if (StringUtils.left(relatedType, 4).equals("投资关联")) {
                        String sql = "select distinct index_dwtzxx_dwtzqymc from index_dwtzxx_tb where index_dwtzxx_dwtzqyzzjgdm='"
                                + s + "' and sys_area_id in (" + sysAreaIdsString + ")";
                        List<Map<String, Object>> map = relateInfoService.queryMoreData(null, sql);
                        if (CollectionUtils.isNotEmpty(map)) {
                            for (Map<String, Object> maps : map) {
                                String qymc1 = MapUtils.getString(maps, "INDEX_DWTZXX_DWTZQYMC");
                                if (StringUtils.isNotBlank(qymc1)) {
                                    ShowImg si = new ShowImg((areaNotSub.getSysAreaName() + ":" + qymc1), "投资关联", "主动");
                                    showImgs.add(si);
                                }
                            }
                        }
                    } else {//如果是担保关联
                        String sql = "select distinct index_dwdbxx_bdbrmc from index_dwdbxx_tb where index_dwdbxx_bdbrzjhm='"
                                + s + "' and index_dwdbxx_bdbrzjlx <> 'P' and sys_area_id in (" + sysAreaIdsString + ")";
                        List<Map<String, Object>> map = relateInfoService.queryMoreData(null, sql);
                        if (CollectionUtils.isNotEmpty(map)) {
                            for (Map<String, Object> maps : map) {
                                String qymc1 = MapUtils.getString(maps, "INDEX_DWDBXX_BDBRMC");
                                if (StringUtils.isNotBlank(qymc1)) {
                                    ShowImg si = new ShowImg((areaNotSub.getSysAreaName() + ":" + qymc1), "担保关联", "主动");
                                    showImgs.add(si);
                                }
                            }
                        }
                    }
                }
            }
        } else if (CollectionUtils.isNotEmpty(integerList)) {//获取到  二码主键
            for (Integer i : integerList) {
                DefaultIndexItemCustom d = new DefaultIndexItemCustom();
                DefaultIndexItem indexItem = defaultIndexItemService.queryById(i);
                if (indexItem != null) {
                    String qymc = this.getQymc(i);
                    d.setQymc(qymc);//企业名称
                    d.setCodeCredit(indexItem.getCodeCredit());
                    d.setCodeOrg(indexItem.getCodeOrg());
                    d.setRelatedType(relatedType);
                    d.setIsSon(areaNotSub.getSysAreaName());
                    d.setQyzs(areaNotSub.getSysAreaCode());
                    customList.add(d);

                    ShowImg si = new ShowImg();
                    if (relatedType.length() > 4) {//表示有主被动关系
                        si.setGuanxi(StringUtils.left(relatedType, 4));//截取前4位
                        si.setZbd(StringUtils.substring(relatedType, 5, 7));
                    } else {//表示是高管关联
                        si.setGuanxi(relatedType);
                    }
                    si.setQymc(areaNotSub.getSysAreaName() + ":" + qymc);
                    showImgs.add(si);
                }
            }
        }
    }

    private void log(Integer type, String queryCondition, String querySql,
                     Integer logCount, boolean result, String logFile, HttpServletRequest request) throws Exception {
        String logUrl = "/admin/relatedInfo/sysQuery.jhtml";
        sysUserLogService.insertOneLog(new SysUserLog("关联信息查询", null, null,
                null, null, null, type,
                logCount, queryCondition, querySql, logUrl, logFile,
                null, result), request);
    }
}
