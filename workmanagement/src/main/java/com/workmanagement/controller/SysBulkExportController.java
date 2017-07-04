package com.workmanagement.controller;

import com.google.gson.reflect.TypeToken;
import com.workmanagement.model.*;
import com.workmanagement.service.*;
import com.workmanagement.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 批量导出
 * Created by lzm on 2017/3/29.
 */
@Controller
@RequestMapping("/admin/sysBulkExport")
public class SysBulkExportController {
    @Autowired
    private IndexTbService indexTbService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private DicService dicService;
    @Autowired
    private IndexItemTbService indexItemTbService;
    @Autowired
    private SysOrgTypeService sysOrgTypeService;
    @Autowired
    private RelateInfoService relateInfoService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysArea sysAreaList = this.getSysAreaRedis(userDetails);
        model.addAttribute("sysAreaList", sysAreaList);
        return "sysBulkExport/list";
    }

    private SysArea getSysAreaRedis(MyUserDetails userDetails) {
        String key = "sysBulkExportController" + userDetails.getSysOrg().getSys_area_id();
        SysArea sysArea = null;
        if (RedisUtil.isEmpty(key)) {
            sysArea = sysAreaService.queryAreaById(userDetails.getSysOrg().getSys_area_id());
            RedisUtil.setData(key, sysArea);
        } else {
            sysArea = RedisUtil.getObjData(key, SysArea.class);
        }
        return sysArea;
    }

    @RequestMapping("/getSysOrgByArea")
    @ResponseBody
    public List<ZtreeVo> getSysOrgByArea(@RequestParam Integer areaId) {
        String key = RedisKeys.SYS_AREA + "sysBulkExport_getSysOrgByArea" + areaId;
        List<ZtreeVo> ztreeTwo;
        if (RedisUtil.isEmpty(key)) {
            List<Integer> areaIds = sysAreaService.getAllSubAreaIds(areaId);
            String[] strings = new String[areaIds.size()];
            for (int i = 0; i < areaIds.size(); i++) {
                strings[i] = String.valueOf(areaIds.get(i));
            }
            List<SysOrg> sysOrgs = sysOrgService.querySysOrgByAreaIds(strings);
            List<SysOrgType> sysOrgTypes = sysOrgTypeService.getTypesByOrgIds(sysOrgs);
            ztreeTwo = DataUtil.getZtreeTwo(sysOrgs, sysOrgTypes);
            RedisUtil.setData(key, ztreeTwo);
        } else {
            Type type = new TypeToken<List<ZtreeVo>>() {
            }.getType();
            ztreeTwo = RedisUtil.getListData(key, type);
        }
        return ztreeTwo;
    }

    @RequestMapping("/getIndexTbByArea")
    @ResponseBody
    public List<IndexTb> getIndexTbByArea(@RequestParam Integer areaId) {
        List<Integer> allUpAreaIds = sysAreaService.getAllUpAreaIds(areaId);
        return indexTbService.queryIndexBySysAreaIds(null, allUpAreaIds);
    }

    /**
     * 导出导出所有指标大类
     *
     * @throws Exception
     */
    @RequestMapping("/outAllData")
    public void outAllData(HttpServletResponse response, HttpServletRequest request) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer areaId = userDetails.getSysOrg().getSys_area_id();
        List<Integer> allUpAreaIds = sysAreaService.getAllUpAreaIds(areaId);
        String sourceDir = null;
        List<IndexTb> indexTbList = indexTbService.queryAll(null);
        if (CollectionUtils.isNotEmpty(indexTbList)) {
            /**
             * 导出指标大类
             */
            for (IndexTb indexTb : indexTbList) {
                List<IndexItemTb> indexItemTbs = indexItemTbService.queryItemsByAreaIds(indexTb.getIndexId(), allUpAreaIds);
                if (CollectionUtils.isNotEmpty(indexItemTbs)) {//如果该指标大类有指标项
                    List<String> rowNames = new ArrayList<>();
                    List<String> propertyNames = new ArrayList<>();
                    StringBuilder sb = new StringBuilder("select b.code_credit,b.code_org");
                    rowNames.add("统一社会信用码");
                    rowNames.add("组织机构代码");
                    propertyNames.add("CODE_CREDIT");
                    propertyNames.add("CODE_ORG");
                    boolean isJJHY = false;
                    String ssb = null;
                    for (IndexItemTb itemTb : indexItemTbs) {
                        if (itemTb.getIndexItemAliasName() != null) {
                            //使用别名
                            rowNames.add(itemTb.getIndexItemAliasName());
                        } else {
                            rowNames.add(itemTb.getIndexItemName());
                        }
                        propertyNames.add(itemTb.getIndexItemCode().toUpperCase());
                        if (itemTb.getDicId() != null) {
                            String dicName = dicService.getDicByDicId(itemTb.getDicId()).getDicName();
                            if (dicName.equals("经济行业")) {
                                isJJHY = true;
                                ssb = itemTb.getIndexItemCode();
                                sb.append(",c.sys_industry_name as ").append(itemTb.getIndexItemCode());
                            } else {
                                sb.append(",a.").append(itemTb.getIndexItemCode());
                            }
                        } else {
                            sb.append(",a.").append(itemTb.getIndexItemCode());
                        }
                    }
                    sb.append(" from ").append(indexTb.getIndexCode()).append("_tb as a,default_index_item_tb as b");
                    if (isJJHY) {
                        sb.append(",sys_industry_tb as c");
                    }
                    sb.append(" where b.default_index_item_id = a.default_index_item_id");
                    if (isJJHY) {
                        sb.append(" and c.sys_industry_code = a.").append(ssb);
                    }
                    sb.append(" order by ").append(indexTb.getIndexCode()).append("_id asc");
                    String ss = setDataToTemp(sb.toString(), rowNames, propertyNames, indexTb.getIndexName());
                    if (ss != null) {
                        sourceDir = ss;
                    }
                }
            }
        }
        if (sourceDir != null) {
            ZipUtil.zip(sourceDir, request, response);
        } else {
            DownLoadFile.downLoadFile("/opt/1.txt", "没有数据", request, response);
        }
    }

    /**
     * 导出需要的数据
     *
     * @param indexId 指标大类
     * @param netId   标识
     * @param areaId  地区
     * @param sTime   开始时间
     * @param eTime   结束时间
     */
    @RequestMapping("/outSomeData")
    public void outSomeData(Integer indexId, String netId, Integer areaId, Integer orgId, String sTime,
                            String eTime, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String endTime = "";//作用于数据表
        if (eTime != null) {
            endTime = " and a.record_date<='" + eTime + "'";
        }
        String area = "";//作用于数据表
        if (areaId != null) {
            List<Integer> allSubAreaIds = sysAreaService.getAllSubAreaIds(areaId);
            StringBuilder sysareaIds = new StringBuilder();
            for (Integer it : allSubAreaIds) {
                sysareaIds.append(it).append(",");
            }
            area = " and a.sys_area_id in (" + StringUtils.removeEnd(sysareaIds.toString(), ",") + ")";
        }
        String orgIds = "";
        if (orgId != null) {
            orgIds = " and a.sys_org_id=" + orgId;
        }
        if (indexId == null) {//如果没选指标大类，表示导出所有
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Integer areaIds = userDetails.getSysOrg().getSys_area_id();
            List<Integer> allUpAreaIds = sysAreaService.getAllUpAreaIds(areaIds);
            //只获取当前用户能看到的指标大类
            List<IndexTb> indexTbList = indexTbService.queryIndexBySysAreaIds(null, allUpAreaIds);
            String sourceDir = null;
            if (CollectionUtils.isNotEmpty(indexTbList)) {
                for (IndexTb ii : indexTbList) {
                    String ss = this.setDataTotemp(netId, ii, sTime, area, endTime, orgIds, areaId);
                    if (ss != null) {
                        sourceDir = ss;
                    }
                }
            }
            if (sourceDir != null) {//如果有文件就压缩出去
                ZipUtil.zip(sourceDir, request, response);
            } else {
                DownLoadFile.downLoadFile("/opt/1.txt", "没有数据", request, response);
            }
        } else {
            IndexTb indexTb = indexTbService.queryById(indexId);
            String ss = this.setDataTotemp(netId, indexTb, sTime, area, endTime, orgIds, areaId);
            if (ss != null) {//如果有文件就压缩出去
                ZipUtil.zip(ss, request, response);
            } else {
                DownLoadFile.downLoadFile("/opt/1.txt", "没有数据", request, response);
            }
        }
    }

    /**
     * 往bigmap里放入数据
     *
     * @param net
     * @param indexTb
     * @param sTime
     * @param area
     * @param endTime
     */
    private String setDataTotemp(String net, IndexTb indexTb, String sTime, String area,
                                 String endTime, String orgIds, Integer chooseAreaId) throws Exception {
        //规定必须选择地区
        List<Integer> allUpAreaIds = sysAreaService.getAllUpAreaIds(chooseAreaId);
        List<IndexItemTb> indexItemTbs;
        String sourceDir = null;
        if (net == null) {
            indexItemTbs = indexItemTbService.queryItemsByAreaIds(indexTb.getIndexId(), allUpAreaIds);
        } else {
            List<IndexItemTb> inList = indexItemTbService.queryItemsByAreaIds(indexTb.getIndexId(), allUpAreaIds);
            for (int i = 0; i < inList.size(); i++) {
                String netId = inList.get(i).getIndexItemNetId();
                boolean isNotIn = true;
                if (StringUtils.containsAny(netId, net)) {//如果属于里面
                    isNotIn = false;
                }
                if (isNotIn) {
                    inList.remove(i);
                    i--;
                }
            }
            indexItemTbs = inList;
        }
        if (CollectionUtils.isNotEmpty(indexItemTbs)) {//如果该指标大类有指标项
            List<String> rowNames = new ArrayList<>();
            List<String> propertyNames = new ArrayList<>();
            StringBuilder sb = new StringBuilder("select b.code_credit,b.code_org");
            rowNames.add("统一社会信用代码");
            rowNames.add("组织机构代码");
            propertyNames.add("CODE_CREDIT");
            propertyNames.add("CODE_ORG");
            boolean isJJHY = false;
            String ssb = null;
            for (IndexItemTb itemTb : indexItemTbs) {
                if (itemTb.getIndexItemAliasName() != null) {
                    //使用别名
                    rowNames.add(itemTb.getIndexItemAliasName());
                } else {
                    rowNames.add(itemTb.getIndexItemName());
                }
                propertyNames.add(itemTb.getIndexItemCode().toUpperCase());
                if (itemTb.getDicId() != null) {
                    String dicName = dicService.getDicByDicId(itemTb.getDicId()).getDicName();
                    if (dicName.equals("经济行业")) {
                        isJJHY = true;
                        ssb = itemTb.getIndexItemCode();
                        sb.append(",c.sys_industry_name as ").append(itemTb.getIndexItemCode());
                    } else {
                        sb.append(",a.").append(itemTb.getIndexItemCode());
                    }
                } else {
                    sb.append(",a.").append(itemTb.getIndexItemCode());
                }
            }
            sb.append(" from ").append(indexTb.getIndexCode()).append("_tb as a,default_index_item_tb as b");
            if (isJJHY) {
                sb.append(",sys_industry_tb as c");
            }
            sb.append(" where b.default_index_item_id = a.default_index_item_id");
            if (isJJHY) {
                sb.append(" and c.sys_industry_code = a.").append(ssb);
            }
            sb.append(" and a.record_date >= '").append(sTime).append("'").append(area)
                    .append(endTime).append(orgIds).append(" order by ")
                    .append(indexTb.getIndexCode()).append("_id asc");
            String ss = setDataToTemp(sb.toString(), rowNames, propertyNames, indexTb.getIndexName());
            if (ss != null) {
                sourceDir = ss;
            }
        }
        return sourceDir;
    }

    private String setDataToTemp(String sql, List<String> rowNames,
                                 List<String> propertyNames, String title) throws NoSuchMethodException {
        String sourceDir = null;
        List<Map<String, Object>> mapList = relateInfoService.queryMoreData(null, sql);
        if (CollectionUtils.isNotEmpty(mapList)) {
            DicExcelOut<Map<String, Object>> excelOut = new DicExcelOut<>();
            String[] rowName = rowNames.toArray(new String[rowNames.size()]);
            String[] propertyName = propertyNames.toArray(new String[propertyNames.size()]);
            excelOut.setTitle(title);
            excelOut.setRowNames(rowName);
            excelOut.setPropertyNames(propertyName);
            excelOut.setList(mapList);
            sourceDir = excelOut.exportMap();
        }
        return sourceDir;
    }
}
