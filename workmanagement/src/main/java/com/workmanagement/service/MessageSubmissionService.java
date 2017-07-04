package com.workmanagement.service;

import com.workmanagement.dao.DefaultIndexItemDao;
import com.workmanagement.dao.RelateInfoDao;
import com.workmanagement.dao.ReportIndexDao;
import com.workmanagement.dao.SysCheckDao;
import com.workmanagement.enums.BothEnum;
import com.workmanagement.model.*;
import com.workmanagement.util.DicExcelOut;
import com.workmanagement.util.LoggerUtil;
import com.workmanagement.util.UpLoadFile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 报文报送的service
 * Created by lzm on 2017/3/20.
 */
@Service(value = "messageSubmissionService")
public class MessageSubmissionService {
    private final String[] times = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMdd"};//要转换的时间类型
    private Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
    @Autowired
    private DicContentService dicContentService;
    @Autowired
    private DicService dicService;
    @Autowired
    private ReportIndexDao reportIndexDao;
    @Autowired
    private DefaultIndexItemDao defaultIndexItemDao;
    @Autowired
    private SysCheckService sysCheckService;
    @Autowired
    private SysCheckDao sysCheckDao;
    @Autowired
    private RelateInfoDao relateInfoDao;
    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private SysUserLogService sysUserLogService;
    @Autowired
    private SysClassFyService sysClassFyService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private IndexItemTbService indexItemTbService;
    @Autowired
    private DefaultIndexItemService defaultIndexItemService;

    /**
     * 分解txt文档存数据
     *
     * @param file
     * @param indexTb
     * @throws Exception
     */
    public Map<String, Object> insertData(final MultipartFile file, final IndexTb indexTb,
                                          final MyUserDetails userDetails, final Date reportTime,
                                          boolean canUsedCode, HttpServletRequest request) throws Exception {
        Integer sysAreaId = userDetails.getSysOrg().getSys_area_id();//这是登陆用户的所属区域ID
        String sysOrgName = userDetails.getSysOrg().getSys_org_name();//这是登陆用户的所属机构名字
        Integer sysUserId = userDetails.getSys_user_id();//这是登陆用户的ID
        List<Integer> allUpAreaIds = sysAreaService.getAllUpAreaIds(sysAreaId);

        Date submitTime = new Date();

        ExecutorService pool = Executors.newCachedThreadPool();
        List<String> errorList = new ArrayList<>();
        List<DefaultIndexItem> changeIndexItem = new ArrayList<>();
        Map<String, Object> returnMap = new HashMap<>();

        List<Map<String, Object>> sqlData = new ArrayList<>();
        Integer sysOrgId = userDetails.getSys_org_id();//这是登陆用户的所属机构ID
        Integer rowNum = 1;
        List<IndexItemTb> indexItemTb = indexItemTbService.getIndexIntemsIsUsedByIdAndAreaIds(indexTb.getIndexId(), allUpAreaIds);
        Integer valueSize = indexItemTb.size();    //条数
        Integer cellNum = null;    //模板列数
        if (canUsedCode) {
            cellNum = valueSize + 2;
        } else {
            cellNum = valueSize + 1;
        }
        boolean isYewu = true;
        if (indexTb.getIndexType() == 0) {//业务信息
            isYewu = false;
        }
        /**
         * 第一步，先把文件存起来
         */
        String fileName = UpLoadFile.upLoadFile(file);//文件存储的路径
        /**
         * 第二步，进行txt文档的解析
         */
        InputStream files = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            files = file.getInputStream();
            inputStreamReader = new InputStreamReader(files, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String moLine = StringUtils.replace(bufferedReader.readLine(), "\t", "");      //第一行是标题
//            if (StringUtils.isBlank(moLine)) {
//                errorList.add("文件内容不能为空，请检查后上传");
//            }

            //获取到上级的区域 id 并获取到这个 id 的所有下级地区 id
            Integer sysUpAreaId = sysAreaService.getUpOrThisSysArea(sysAreaId).getSysAreaId();

            StringBuilder areaIds = new StringBuilder();
            for (Integer i : allUpAreaIds) {
                areaIds.append(i).append(",");
            }
            areaIds.deleteCharAt(areaIds.lastIndexOf(","));
            SysCheckCache sysCheckCache = new SysCheckCache(sysCheckDao, sysAreaService, indexItemTb, sysAreaId, indexTb.getIndexId(), reportTime);
            /**
             *
             */
            while (StringUtils.isNotBlank(moLine)) {     //循环读取数据
                if (rowNum != 1) {
                    pool.execute(new CheckRowData(errorList, rowNum, indexTb, indexItemTb, sysOrgId, sysAreaId, reportTime, submitTime,
                            moLine, cellNum, sqlData, canUsedCode, sysUpAreaId, areaIds.toString(), changeIndexItem, sysCheckCache));
                }
                rowNum++;
                moLine = StringUtils.replace(bufferedReader.readLine(), "\t", "");    //读取下一行的数据
            }
            pool.shutdown();//停止接收新线程
            while (true) {
                if (pool.isTerminated()) {//线程全部结束
                    break;
                }
            }
            if (rowNum < 3) {
                errorList.add("上报内容不能为空，请检查后上传");
            }
        } catch (Exception e) {
            LoggerUtil.error(e);
            errorList.add("未知错误，请联系管理员");
        } finally {
            IOUtils.closeQuietly(bufferedReader);
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(files);
        }

        /**
         * 校验基本信息
         */
        if (StringUtils.equals(indexTb.getIndexCode(), "index_jbxx")) {
            this.equalsJbxx(errorList, sqlData);
        }

        List<Integer> valueOnlyTiles = null;
        if (CollectionUtils.isEmpty(errorList)) {
            /**
             * 遍历数据，进行入库规则
             */
            valueOnlyTiles = new ArrayList<>();//装唯一值字段下标的list
            for (int i = 0; i < indexItemTb.size(); i++) {
                if (indexItemTb.get(i).getIndexItemImportUnique() == 1) {//唯一值
                    valueOnlyTiles.add(i);//添加字段名
                }
            }

            /**
             * 对排好序的集合进行去重校验
             *      如果二码主键+唯一值都一样就是重复数据
             *      先把二码主键相等的找出来
             *      titles.add("sys_org_id");//机构id                   0
             *      titles.add("sys_area_id");//地区id                  1
             *      titles.add("record_date");//归档时间                2
             *      titles.add("submit_time");//上报时间                3
             *      titles.add("default_index_item_id");//二码主键      4
             */
            if (CollectionUtils.isNotEmpty(valueOnlyTiles)) {//如果有唯一值
                List<Map<String, Object>> copyList = new ArrayList<>();
                copyList.addAll(sqlData);//拷贝的一份list
                for (int y = 0; y < sqlData.size(); y++) {
                    Map<String, Object> map = sqlData.get(y);
                    if (MapUtils.isNotEmpty(map)) {
                        List<Object> values = (List<Object>) map.get(ReportIndexDao.VALUES);//值
                        Integer defaultItemId = Integer.valueOf(String.valueOf(values.get(4)));
                        /**
                         * 遍历copy的行的list
                         */
                        int size = y + 1;
                        if (size < copyList.size()) {
                            for (int x = size; x < copyList.size(); x++) {
                                Map<String, Object> copyMap = copyList.get(x);
                                List<Object> copyValue = (List<Object>) copyMap.get(ReportIndexDao.VALUES);//值
                                Integer copyDefaultItemId = Integer.valueOf(String.valueOf(copyValue.get(4)));
                                if (defaultItemId.intValue() == copyDefaultItemId.intValue()) {//如果遇到defaultItemId相等，就看第二束
                                    for (Integer i : valueOnlyTiles) {
                                        Object o = values.get(i + 5);
                                        Object o1 = copyValue.get(i + 5);
                                        if (o != null && o1 != null) {
                                            //如果两个唯一值不是空的才比较
                                            String firstData = String.valueOf(o);
                                            String copyFirstData = String.valueOf(o1);
                                            if (StringUtils.equalsIgnoreCase(firstData, copyFirstData)) {//表示唯一值重复
                                                errorList.add("第" + map.get("rowNum") + "行与第" + copyMap.get("rowNum") + "行的唯一值重复，请检查后上传");
                                                copyList.remove(x);
                                                x--;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        /**
         * 进行授信和贷款的校验
         */
        if (CollectionUtils.isEmpty(errorList)) {
            if (StringUtils.equals(indexTb.getIndexCode(), "index_yxdk")
                    || StringUtils.equals(indexTb.getIndexCode(), "index_yxsx")) {
                sysCheckService.checkDkAndSx(sqlData, null, reportTime, errorList, indexTb);
            }
        }

        if (CollectionUtils.isEmpty(errorList)) {//如果没有错误
            List<Map<String, Object>> insertList = new ArrayList<>();//新增的map的list集合
            List<Map<String, Object>> updateList = new ArrayList<>();//更新的map的list集合


            //获取到上级的区域 id 并获取到这个 id 的所有下级地区 id
//            List<Integer> allSubAreaIds = sysAreaService.getAllSubAreaIds(sysAreaService.getUpOrThisSysArea(sysAreaId).getSysAreaId());
//            StringBuilder areaIds = new StringBuilder();
//            for (Integer i : allSubAreaIds) {
//                areaIds.append(i).append(",");
//            }
//            areaIds.deleteCharAt(areaIds.lastIndexOf(","));

            pool = Executors.newCachedThreadPool();
            for (Map<String, Object> map : sqlData) {//遍历sqlData
                pool.execute(new Classify(insertList, updateList, isYewu, map, indexTb, sysOrgId, sysAreaId, reportTime, valueOnlyTiles, indexItemTb));
            }
            pool.shutdown();//停止接收新线程
            while (true) {
                if (pool.isTerminated()) {//线程全部结束
                    break;
                }
            }
            boolean insertHaveData = CollectionUtils.isNotEmpty(insertList);
            boolean updateHaveData = CollectionUtils.isNotEmpty(updateList);
            /**
             * 给insert集合重新排序
             */
            List<Map<String, Object>> newInsert = null;
            boolean isNewInsertHaveData = false;
            if (insertHaveData) {
                isNewInsertHaveData = true;
                newInsert = new ArrayList<>();
                Integer[] row = new Integer[insertList.size()];
                for (int i = 0; i < insertList.size(); i++) {//获取行数重新排序
                    row[i] = Integer.valueOf(String.valueOf(insertList.get(i).get("rowNum")));
                    newInsert.add(null);
                }
                Arrays.sort(row);//重新排序
                for (Map<String, Object> map : insertList) {
                    Integer rownum = Integer.valueOf(String.valueOf(map.get("rowNum")));
                    int xiabiao = Arrays.binarySearch(row, rownum);
                    newInsert.set(xiabiao, map);
                }
            }

            if (CollectionUtils.isEmpty(errorList)) {
                /**
                 * 专门做update的线程
                 */
                if (updateHaveData) {
                    pool = Executors.newCachedThreadPool();
                    pool.execute(new UpdateData(updateList, indexTb, indexItemTb, valueOnlyTiles, isYewu, sysAreaId));
                    pool.shutdown();//停止接收新线程
                }

                /**
                 * 专门做insert操作
                 */
                if (isNewInsertHaveData) {
//                    Classify c = new Classify(valueOnlyTiles, indexItemTb);
                    for (Map<String, Object> map : newInsert) {
//                        List<Object> value = (List<Object>) map.get(ReportIndexDao.VALUES);//值
//                        List<Object> title = (List<Object>) map.get(ReportIndexDao.TITLES);//字段
//                        /**
//                         * 上方入库，下方检查数据可能insert变update
//                         *      业务信息:   上报数据两码+标识码+所属区域+所属机构相同+上报时间
//                         *      基本信息:   上报数据两码+标识码+所在区域相同+上报时间
//                         */
//                        StringBuilder sbf = new StringBuilder();
//                        sbf.append("select count(0) from ").append(indexTb.getIndexCode()).append("_tb").append(" where sys_area_id=")
//                                .append(sysAreaId).append(" and record_date='").append(DateFormatUtils.format(reportTime, times[0]))
//                                .append("'").append(" and default_index_item_id=").append(value.get(4));
//                        if (isYewu) {
//                            /**
//                             * 业务信息
//                             *              同一时间上报数据两码+标识码+所属区域+所属机构相同
//                             */
//                            sbf.append(" and sys_org_id=").append(sysOrgId);
//                        }
//                        c.setSql(sbf, value);
//                        Integer count = reportIndexDao.countBySql(sbf.toString());
//                        if (count > 0) {//表示有数据是更新操作
//                            sbf = new StringBuilder();
//                            sbf.append("update ").append(indexTb.getIndexCode()).append("_tb").append(" set ");
//                            for (int i = 0; i < title.size(); i++) {
//                                sbf.append(title.get(i)).append("='").append(StringUtils.trimToEmpty(String.valueOf(value.get(i)))).append("',");
//                            }
//                            sbf.deleteCharAt(sbf.length() - 1);
//                            sbf.append(" where sys_area_id=").append(value.get(1)).append(" and record_date='").append(DateFormatUtils.format((Date) value.get(2), times[0]))
//                                    .append("'").append(" and default_index_item_id=").append(value.get(4));
//                            if (isYewu) {
//                                sbf.append(" and sys_org_id=").append(value.get(0));
//                            }
//                            c.setSql(sbf, value);
//                            reportIndexDao.updateBySql(sbf.toString());
//                        } else {//表示是新增
                        try {
                            reportIndexDao.addSomeDataToIndexTb(map);
                        } catch (Exception e) {
                            throw new Exception();
                        }
//                        }
                    }
                }
                /**
                 * 记录成功信息
                 */
                String path = null;
                if (CollectionUtils.isNotEmpty(errorList)) {
                    DicExcelOut<String> excelOut = new DicExcelOut<>();
                    excelOut.setRowNames(new String[]{"错误信息"});
                    excelOut.setList(errorList);
                    path = excelOut.outStringNotRespones();
                }
                reportIndexDao.insertOne(new ReportIndex(ReportIndexDao.TXT_SUBMIT, indexTb.getIndexName(), (rowNum - 2), reportTime,
                        ReportIndexDao.STSTUS_SUCCES, sysOrgId, sysOrgName, submitTime, sysUserId, fileName, path));
                sysUserLogService.insertOneLog(new SysUserLog(("报文报送"), null, indexTb.getIndexId(),
                        null, null, null, submitTime, SysUserLogService.IMPORT, (rowNum - 2),
                        null, null, null, fileName, null, true), request);
                returnMap.put("errorList", errorList);
                returnMap.put("changeIndexItem", changeIndexItem);
                return returnMap;
            } else {
                /**
                 * 记录失败信息
                 */
                String path = null;
                if (CollectionUtils.isNotEmpty(errorList)) {
                    DicExcelOut<String> excelOut = new DicExcelOut<>();
                    excelOut.setRowNames(new String[]{"错误信息"});
                    excelOut.setList(errorList);
                    path = excelOut.outStringNotRespones();
                }
                reportIndexDao.insertOne(new ReportIndex(ReportIndexDao.TXT_SUBMIT, indexTb.getIndexName(), (rowNum - 2), reportTime,
                        ReportIndexDao.STSTUS_ERROR, sysOrgId, sysOrgName, submitTime, sysUserId, fileName, path));
                sysUserLogService.insertOneLog(new SysUserLog(("报文报送"), null, indexTb.getIndexId(),
                        null, null, null, submitTime, SysUserLogService.IMPORT, (rowNum - 2),
                        null, null, null, fileName, null, false), request);
                returnMap.put("errorList", errorList);
                returnMap.put("changeIndexItem", changeIndexItem);
                return returnMap;
            }
        } else {
            /**
             * 记录失败信息
             */
            String path = null;
            if (CollectionUtils.isNotEmpty(errorList)) {
                DicExcelOut<String> excelOut = new DicExcelOut<>();
                excelOut.setRowNames(new String[]{"错误信息"});
                excelOut.setList(errorList);
                path = excelOut.outStringNotRespones();
            }
            reportIndexDao.insertOne(new ReportIndex(ReportIndexDao.TXT_SUBMIT, indexTb.getIndexName(), (rowNum - 2), reportTime,
                    ReportIndexDao.STSTUS_ERROR, sysOrgId, sysOrgName, submitTime, sysUserId, fileName, path));
            sysUserLogService.insertOneLog(new SysUserLog(("报文报送"), null, indexTb.getIndexId(),
                    null, null, null, submitTime, SysUserLogService.IMPORT, (rowNum - 2),
                    null, null, null, fileName, null, false), request);
            returnMap.put("errorList", errorList);
            returnMap.put("changeIndexItem", changeIndexItem);
            return returnMap;
        }
    }

    /**
     * 校验基本信息
     *
     * @param errorList
     * @param sqlData
     */
    private void equalsJbxx(List<String> errorList, List<Map<String, Object>> sqlData) {
        List<Map<String, Object>> copyList = new ArrayList<>();
        copyList.addAll(sqlData);
        for (int y = 0; y < sqlData.size(); y++) {
            //遍历新增操作里唯一值是否唯一
            Map<String, Object> map = sqlData.get(y);
            List<Object> value = (List<Object>) map.get(ReportIndexDao.VALUES);//值
            Integer defaulItemId = Integer.valueOf(String.valueOf(value.get(4)));
            /**
             * 遍历copy的行的list
             */
            int size = y + 1;
            for (int x = size; x < copyList.size(); x++) {
                Map<String, Object> copyMap = copyList.get(x);
                List<Object> copyValue = (List<Object>) copyMap.get(ReportIndexDao.VALUES);//值
                Integer copyDefaulItemId = Integer.valueOf(String.valueOf(copyValue.get(4)));
                if (defaulItemId.intValue() == copyDefaulItemId.intValue()) {
                    String qymc = String.valueOf(value.get(5));
                    String copyQymc = String.valueOf(copyValue.get(5));
                    if (!StringUtils.equals(qymc, copyQymc)) {
                        errorList.add("第" + map.get("rowNum") + "行与第" + copyMap.get("rowNum") + "行二码一致，企业名称不同，有误，请检查后上传");
                        copyList.remove(copyMap);
                        x--;
                    }
                }
            }
        }
    }

    /**
     * 更新一批数据的线程
     */
    private final class UpdateData implements Runnable {
        private List<Map<String, Object>> updateList;
        private IndexTb indexTb;
        private List<IndexItemTb> indexItemTbs;
        private List<Integer> valueOnlyTiles;//装唯一值字段下标的list
        private boolean isYewu;
        private Integer sysAreaId;

        private UpdateData(List<Map<String, Object>> updateList, IndexTb indexTb, List<IndexItemTb> indexItemTbs,
                           List<Integer> valueOnlyTiles, boolean isYewu, Integer sysAreaId) {
            this.updateList = updateList;
            this.indexTb = indexTb;
            this.indexItemTbs = indexItemTbs;
            this.valueOnlyTiles = valueOnlyTiles;
            this.isYewu = isYewu;
            this.sysAreaId = sysAreaId;
        }

        @Override
        public void run() {
            List<Map<String, Object>> newUpdate = new ArrayList<>();
            Integer[] row = new Integer[updateList.size()];
            for (int i = 0; i < updateList.size(); i++) {//获取行数重新排序
                row[i] = Integer.valueOf(String.valueOf(updateList.get(i).get("rowNum")));
                newUpdate.add(null);
            }
            Arrays.sort(row);//重新排序
            for (Map<String, Object> map : updateList) {
                Integer rownum = Integer.valueOf(String.valueOf(map.get("rowNum")));
                int xiabiao = Arrays.binarySearch(row, rownum);
                newUpdate.set(xiabiao, map);
            }

            /**
             * 遍历重新排序好的集合入库
             */
            Classify c = new Classify(valueOnlyTiles, indexItemTbs);
            for (Map<String, Object> map : newUpdate) {
                List<String> title = (List<String>) map.get(ReportIndexDao.TITLES);//值
                List<Object> value = (List<Object>) map.get(ReportIndexDao.VALUES);//值
                StringBuilder sbf = new StringBuilder();
                sbf.append("update ").append(indexTb.getIndexCode()).append("_tb").append(" set ");
                for (int i = 0; i < title.size(); i++) {
                    Object obj = value.get(i);
                    sbf.append(title.get(i)).append("=");
                    if (i > 4) {
                        int x = indexItemTbs.get(i - 5).getIndexItemType();
                        if (x == 2) {//数字类型直接进
                            sbf.append(obj);
                        } else {
                            if (obj instanceof Date) {//如果是时间类型
                                Date times = (Date) obj;
                                obj = DateFormatUtils.format(times, "yyyy-MM-dd");
                                sbf.append("'").append(obj).append("'");
                            } else {//是String类型
                                if (obj == null) {
                                    sbf.append("").append("null").append("");
                                } else {
                                    sbf.append("'").append(obj).append("'");
                                }
                            }
                        }
                    } else {
                        if (obj instanceof Date) {//如果是时间类型
                            Date times = (Date) obj;
                            String data;
                            if (StringUtils.equals(title.get(i), "submit_time")) {
                                data = StringUtils.trimToNull(DateFormatUtils.format(times, "yyyy-MM-dd HH:mm:ss"));
                            } else {
                                data = StringUtils.trimToNull(DateFormatUtils.format(times, "yyyy-MM-dd"));
                            }
                            sbf.append("'").append(data).append("'");
                        } else {
                            sbf.append("'").append(obj).append("'");
                        }

                    }
                    sbf.append(",");
                }
                sbf.deleteCharAt(sbf.length() - 1);
                sbf.append(" where record_date='").append(DateFormatUtils.format((Date) value.get(2), times[0]))
                        .append("'");
                if (!isYewu) {
                    /**
                     * 基本信息
                     */
                    sbf.append(" and sys_area_id =").append(sysAreaId).append(" and default_index_item_id=").append(value.get(4));
//                    sbf.append(" and sys_org_id=").append(value.get(0));
                } else {
                    String otherAreaDefaultIds = defaultIndexItemService.getOtherAreaDefaultIds(Integer.valueOf(String.valueOf((value).get(4))));
                    sbf.append(" and default_index_item_id in").append(otherAreaDefaultIds);
                }
                c.setSql(sbf, value);
                try {
                    reportIndexDao.updateBySql(sbf.toString());
                } catch (Exception e) {
                    LoggerUtil.error(e);
                }
            }
        }
    }

    /**
     * 把数据分组的线程
     */
    private final class Classify implements Runnable {
        private List<Map<String, Object>> insertList;//新增的map的list集合
        private List<Map<String, Object>> updateList;//更新的map的list集合
        private boolean isYewu;
        private Map<String, Object> map;
        private IndexTb indexTb;
        private Integer sysOrgId;
        private Integer sysAreaId;
        private Date reportTime;
        private List<Integer> valueOnlyTiles;//装唯一值字段下标的list
        private List<IndexItemTb> indexItemTb;

        private Classify(List<Integer> valueOnlyTiles, List<IndexItemTb> indexItemTb) {
            this.valueOnlyTiles = valueOnlyTiles;
            this.indexItemTb = indexItemTb;
        }

        private Classify(List<Map<String, Object>> insertList, List<Map<String, Object>> updateList, boolean isYewu,
                         Map<String, Object> map, IndexTb indexTb, Integer sysOrgId, Integer sysAreaId, Date reportTime,
                         List<Integer> valueOnlyTiles, List<IndexItemTb> indexItemTb) {
            this.insertList = insertList;
            this.updateList = updateList;
            this.isYewu = isYewu;
            this.map = map;
            this.indexTb = indexTb;
            this.sysOrgId = sysOrgId;
            this.sysAreaId = sysAreaId;
            this.reportTime = reportTime;
            this.valueOnlyTiles = valueOnlyTiles;
            this.indexItemTb = indexItemTb;
        }

        @Override
        public void run() {
            List<Object> value = (List<Object>) map.get(ReportIndexDao.VALUES);//值
            StringBuilder sbf = new StringBuilder();
            sbf.append("select count(0) from ").append(indexTb.getIndexCode()).append("_tb")
                    .append(" where record_date='").append(DateFormatUtils.format(reportTime, times[0])).append("'");
            if (!isYewu) {
                /**
                 * 不是业务信息
                 *              同一时间上报数据两码+标识码+所属区域相同
                 */
//                sbf.append(" and sys_org_id=").append(sysOrgId);
                sbf.append(" and sys_area_id =").append(sysAreaId)
                        .append(" and default_index_item_id=").append(Integer.valueOf(String.valueOf((value).get(4))));
            } else {
                String otherAreaDefaultIds = defaultIndexItemService.getOtherAreaDefaultIds(Integer.valueOf(String.valueOf((value).get(4))));
                sbf.append(" and default_index_item_id in").append(otherAreaDefaultIds);
            }
            this.setSql(sbf, value);
            this.shunt(sbf.toString());
        }

        private void shunt(String sbf) {
            try {
                Integer count = reportIndexDao.countBySql(sbf);
                if (count > 0) {//表示有数据是更新操作
                    updateList.add(map);
                } else {//表示是新增,还要检查输入的值是否重复
                    insertList.add(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setSql(StringBuilder sbf, List<Object> value) {
            if (CollectionUtils.isNotEmpty(valueOnlyTiles)) {
                for (Integer i : valueOnlyTiles) {
                    IndexItemTb iit = indexItemTb.get(i);
                    sbf.append(" and ").append(iit.getIndexItemCode()).append("=");
                    sbf.append("'").append(value.get(i + 5)).append("'");
                }
            }
        }
    }

    /**
     * 专门进行数据校验
     */
    private final class CheckRowData implements Runnable {
        private List<String> errorList;
        private List<IndexItemTb> indexItemTbs;
        private Integer rowNum;
        private IndexTb indexTbs;
        private Integer sysOrgId;
        private Integer sysAreaId;
        private Date reportTimes;
        private Date submitTime;
        private String moLines;
        private Integer cellNum;
        private List<Map<String, Object>> sqlData;
        private boolean canUsedCode;
        private List<String> isTrue;
        private List<String> titles;//装字段名的List
        private List<Object> values;//装数据
        private String orgName = null;
        private String stringSysAreaIds;
        private Integer sysUpAreaId;
        private List<DefaultIndexItem> changeIndexItem;
        private SysCheckCache sysCheckCache;

        private CheckRowData(List<String> errorList, Integer rowNum, IndexTb indexTb, List<IndexItemTb> indexItemTbs,
                             Integer sysOrgId, Integer sysAreaId, Date reportTime, Date submitTime, String moLine, Integer cellNum,
                             List<Map<String, Object>> sqlData, boolean canUsedCode, Integer sysUpAreaId, String stringSysAreaIds,
                             List<DefaultIndexItem> changeIndexItem, SysCheckCache sysCheckCache) {
            this.errorList = errorList;
            this.rowNum = rowNum;
            this.indexTbs = indexTb;
            this.indexItemTbs = indexItemTbs;
            this.sysOrgId = sysOrgId;
            this.sysAreaId = sysAreaId;
            this.reportTimes = reportTime;
            this.submitTime = submitTime;
            this.moLines = moLine;
            this.cellNum = cellNum;
            this.sqlData = sqlData;
            this.canUsedCode = canUsedCode;
            this.isTrue = new ArrayList<>();
            this.values = new ArrayList<>();
            this.titles = new ArrayList<>();
            this.sysUpAreaId = sysUpAreaId;
            this.stringSysAreaIds = stringSysAreaIds;
            this.changeIndexItem = changeIndexItem;
            this.sysCheckCache = sysCheckCache;
        }

        @Override
        public void run() {
            /**
             * 第一点五步，创建sql语句需要的字段
             */
            Map<String, Object> insertOrUpdateMap = new HashMap<>();
            /**
             * 装表名
             */
            String tableName = indexTbs.getIndexCode();//数据库表名字
            insertOrUpdateMap.put(ReportIndexDao.TABLE_NAME, tableName + "_tb");
            /**
             * 装字段名
             */
            titles.add("sys_org_id");//机构id
            titles.add("sys_area_id");//地区id
            titles.add("record_date");//归档时间
            titles.add("submit_time");//上报时间
            titles.add("default_index_item_id");//二码主键
            values.add(sysOrgId);
            values.add(sysAreaId);
            values.add(reportTimes);
            values.add(submitTime);
            for (IndexItemTb i : indexItemTbs) {
                String title = i.getIndexItemCode();//数据库字段名
                titles.add(title);
            }
            insertOrUpdateMap.put("rowNum", rowNum);
            insertOrUpdateMap.put(ReportIndexDao.TITLES, titles);//数据库的字段名
            /**
             * 装字段对应下的数据
             */
            String[] split = StringUtils.split(moLines, "|");
            int lenth = split.length;
            if (lenth != cellNum) {
                errorList.add("第" + rowNum + "行列数错误，不符合指标大类<" + indexTbs.getIndexName() + ">要求，请检查后上传，实为：" + split.length + ",应为:" + cellNum);
            } else {//列数一致才比较
                int xx = 0;
                if (canUsedCode) {
                    xx = 1;
                }
                if (rowNum > 1) {//表示正在读取正文的数据
                    for (int i = 0; i < lenth; i++) {//一列列读取数据
                        String thisCellData = StringUtils.trimToNull(split[i]);//第一列的数据
                        /**
                         * 读取前俩列的数据是否正确
                         */
                        if (i == 0) {
                            String nextData = null;
                            if (canUsedCode) {
                                nextData = StringUtils.trimToNull(split[1]);//第二列的数。组织机构码
                            }
                            this.OneAndTwoCell(thisCellData, nextData);//验证二码
                        }
                        /**
                         * 读取后面的正文数据然后存入数据库
                         */
                        if (i > xx) {//表示读取的是正文的数据
                            int lie = i + 1;
                            IndexItemTb thisCellIntemData = indexItemTbs.get(i - (xx + 1));
                            int x = thisCellIntemData.getIndexItemType();//此列的数据类型
                            boolean isNotNull = thisCellIntemData.getIndexItemEmpty() == 0;//不能为空
                            if (isNotNull) {//表示不能为空i
                                if (StringUtils.isBlank(thisCellData)) {     //如果为空
                                    this.addError("第" + rowNum + "行第" + lie + "列数据的值不能为空，请检查后上传");
                                } else {
                                    this.OldDataToNew(x, lie, thisCellData, thisCellIntemData);
                                }
                            } else {//不唯一也可以为空
                                this.OldDataToNew(x, lie, thisCellData, thisCellIntemData);
                            }
                        }
                    }

                    if (CollectionUtils.isEmpty(isTrue)) {//如果这条数据正确再进行校验
                        List<Object> list = new ArrayList<>();
                        list.addAll(values);
                        list.remove(0);
                        list.remove(0);
                        list.remove(0);
                        list.remove(0);
                        list.remove(0);
                        boolean a = false;
                        try {
                            a = sysCheckCache.getCheckData(list);
                        } catch (Exception e) {
                            this.addError("校验时候发生异常,请联系管理员");
                            e.printStackTrace();
                        }
                        if (!a) {//如果没通过校验
                            this.addError("第" + rowNum + "行的数据未通过校验，请检查后上传");
                        }
                        /**
                         * 往sql的map里装数据
                         */
                        insertOrUpdateMap.put(ReportIndexDao.VALUES, values);
                        sqlData.add(insertOrUpdateMap);
                    }
                }
            }
        }

        /**
         * 头两列的统一社会码和组织机构代码
         *
         * @param codeCredit
         * @param codeOrg
         */
        private void OneAndTwoCell(String codeCredit, String codeOrg) {
            boolean isJbxx = false;
            if (StringUtils.equals(indexTbs.getIndexCode(), "index_jbxx")) {//如果是基本信息需要往二码表里添加数据
                isJbxx = true;
            }
            if (canUsedCode) {//如果可以用组织机构代码
                if (codeCredit != null && codeOrg != null) {//两码都不为空
//                    if (!codeCredit.matches("[a-zA-Z0-9]{18}") || !codeOrg.matches("[a-zA-Z0-9]{8}[-][0-9]")) {
                    if (!sysCheckService.checkCodeCredit(codeCredit)) {
                        this.addMsgToIsTrue(0);
                    }
                    if (!sysCheckService.checkCodeOrg(codeOrg)) {
                        this.addMsgToIsTrue(1);
                    }
                    this.addError("第" + rowNum + "行\"统一社会信用代码\"不能与\"组织机构代码\"在上报时候同时使用，请检查后上传");
//                    } else {
//                        DefaultIndexItem d = defaultIndexItemDao.getByCredit(codeCredit, sysUpAreaId);
//                        if (d == null) {//没有查到数据
//                            //查组织码
//                            List<DefaultIndexItem> indexItemList = defaultIndexItemDao.getByCodeOrg(codeOrg, sysUpAreaId);
//                            if (CollectionUtils.isNotEmpty(indexItemList)) {
//                                d = indexItemList.get(0);
//                                if (StringUtils.isNotBlank(d.getCodeCredit())) {//表示查出的数据的统一码与输入的统一码不一致
//                                    this.addError("第" + rowNum + "行\"统一社会信用代码\"在基本信息中查询不到对应的信息，请检查后上传");
//                                } else {//如果查出来没有统一码
//                                    //数据也可以上报
//                                    values.add(d.getDefaultIndexItemId());
//                                    if (isJbxx) {
//                                        orgName = d.getQymc();
//                                        d.setCodeCredit(codeCredit);
//                                        defaultIndexItemDao.updateDefaultIndexItem(d);
//                                    } else {
//                                        this.queryYw(d);
//                                    }
//                                }
//                            } else {//表示也没有查到
//                                if (isJbxx) {//如果是基本信息需要往二码表里添加数据
//                                    this.rightToData(codeCredit, codeOrg);
//                                } else {
//                                    this.addError("第" + rowNum + "行\"统一社会信用代码\"或\"组织机构代码\"查询不到对应的信息，请检查后上传");
//                                }
//                            }
//                        } else {
//                            if (d.getCodeOrg() != null) {//如果能查出来组织码
//                                if (StringUtils.equals(codeOrg, d.getCodeOrg())) {//如果相等
//                                    /**
//                                     * 正确数据
//                                     */
//                                    values.add(d.getDefaultIndexItemId());
//                                    if (isJbxx) {
//                                        orgName = d.getQymc();
//                                    } else {
//                                        this.queryYw(d);
//                                    }
//                                } else {//如果不相等
//                                    this.addError("第" + rowNum + "行\"组织机构代码\"与用当前行的\"统一社会信用代码\"查出的\"组织机构代码\"不匹配，请检查后上传");
//                                }
//                            } else {//查不出来组织码
//                                if (isJbxx) {//基本信息的话更新二码表
//                                    List<DefaultIndexItem> byCodeOrg = defaultIndexItemDao.getByCodeOrg(codeOrg, sysUpAreaId);
//                                    if (CollectionUtils.isNotEmpty(byCodeOrg)) {//如果根据组织码也能查到数据
//                                        DefaultIndexItem indexItem = byCodeOrg.get(0);
//                                        if (indexItem.getCodeCredit() == null) {//如果查出的数据没有统一码,表示是一条数据
//                                            d.setCodeOrg(codeOrg);
//                                            d.setDefaultIndexItemOldId(indexItem.getDefaultIndexItemId());
//                                            d.setDefaultIndexItemTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//                                            defaultIndexItemDao.updateDefaultIndexItem(d);
//                                            indexItem.setCombine_status(0);
//                                            defaultIndexItemDao.updateDefaultIndexItem(indexItem);
//                                        } else {//错误数据
//                                            this.addError("第" + rowNum + "行\"统一社会信用代码\"与\"组织机构代码\"查出多个企业，请检查后上传");
//                                        }
//                                    } else {
//                                        d.setCodeOrg(codeOrg);
//                                        defaultIndexItemDao.updateDefaultIndexItem(d);
//                                    }
//                                }
//                            }
//                        }
//                    }
                } else if (codeCredit != null) {
                    if (!sysCheckService.checkCodeCredit(codeCredit)) {
                        this.addMsgToIsTrue(0);
                    } else {
                        List<DefaultIndexItem> dlist = defaultIndexItemDao.getByCredit(codeCredit, sysUpAreaId);
                        DefaultIndexItem d = null;
                        if (CollectionUtils.isNotEmpty(dlist)) {
                            d = dlist.get(0);
                        }
                        this.duplicationCodeTwo(d, isJbxx, codeCredit);
                    }
                } else if (codeOrg != null) {
                    if (!sysCheckService.checkCodeOrg(codeOrg)) {
                        this.addMsgToIsTrue(1);
                    } else {
                        List<DefaultIndexItem> d = defaultIndexItemDao.getByCodeOrg(codeOrg, sysUpAreaId);
                        if (CollectionUtils.isEmpty(d)) {
                            if (isJbxx) {//如果是基本信息需要往二码表里添加数据
                                this.rightToData(null, codeOrg);
                            } else {//不是基本信息
                                this.addError("第" + rowNum + "行\"组织机构代码\"查询不到对应的信息，请检查后上传");
                            }
                        } else {//查到数据
                            DefaultIndexItem indexItem = d.get(0);
                            if (indexItem.getCodeCredit() != null) {
                                this.addError("第" + rowNum + "行\"组织机构代码\"查询不到对应的信息，请检查后上传");
                            } else {
                                values.add(indexItem.getDefaultIndexItemId());
                                if (isJbxx) {
                                    orgName = indexItem.getQymc();
                                } else {
                                    this.queryYw(indexItem);
                                }
                            }
                            orgName = indexItem.getQymc();
                        }
                    }
                } else {
                    this.addError("第" + rowNum + "行\"统一社会信用代码\"和\"组织机构代码\"不能为空，请检查后上传");
                }
            } else {//不可以用组织机构代码
                if (codeCredit != null) {//统一码不为空
                    if (!sysCheckService.checkCodeCredit(codeCredit)) {
                        this.addMsgToIsTrue(0);
                    } else {
                        List<DefaultIndexItem> dlist = defaultIndexItemDao.getByCredit(codeCredit, sysUpAreaId);
                        DefaultIndexItem d = null;
                        if (CollectionUtils.isNotEmpty(dlist)) {
                            d = dlist.get(0);
                        }
                        this.duplicationCodeTwo(d, isJbxx, codeCredit);
                    }
                } else {//统一码为空
                    this.addError("第" + rowNum + "行\"统一社会信用代码\"不能为空，请检查后上传");
                }
            }
        }

        private void addError(String msg) {
            isTrue.add("cuowu");
            errorList.add(msg);
        }

        /**
         * @param i 0是统一码。1是社会码
         */
        private void addMsgToIsTrue(int i) {
            if (i == 0) {
                this.addError("第" + rowNum + "行\"统一社会信用代码\"输入错误，请检查后上传");
            } else {
                this.addError("第" + rowNum + "行\"组织机构代码\"输入错误，请检查后上传");
            }
        }

        private void queryYw(DefaultIndexItem d) {
            StringBuilder sql = new StringBuilder("select * from index_jbxx_tb where default_index_item_id=").append(d.getDefaultIndexItemId());
            sql.append(" and sys_area_id =").append(sysUpAreaId).append(" and record_date <= '")
                    .append(DateFormatUtils.format(reportTimes, "yyyy-MM-dd")).append("'");
            List<Map<String, Object>> list = relateInfoDao.queryMoreData(sql.toString());
            if (CollectionUtils.isEmpty(list)) {//如果查不到数据
                this.addError("第" + rowNum + "行在基本信息表没有对应的企业数据，请先报送基本信息表");
            }
        }

        private void duplicationCodeTwo(DefaultIndexItem d, boolean isJbxx, String codeCredit) {
            if (d == null) {
                this.duplicationCode(isJbxx, codeCredit);
            } else {
                values.add(d.getDefaultIndexItemId());
                if (isJbxx) {
                    orgName = "表示是基本信息";
                } else {
                    this.queryYw(d);
                }
            }
        }

        private void duplicationCode(boolean isJbxx, String codeCredit) {
            if (isJbxx) {//如果是基本信息需要往二码表里添加数据
                this.rightToData(codeCredit, null);
            } else {//不是基本信息
                this.addError("第" + rowNum + "行\"统一社会信用代码\"查询不到对应的信息，请检查后上传");
            }
        }

        private void rightToData(String codeCredit, String codeOrg) {
            DefaultIndexItem indexItem = new DefaultIndexItem(codeCredit, codeOrg);
            indexItem.setSys_area_id(sysUpAreaId);
            defaultIndexItemDao.dinsert(indexItem);
            Integer indexItemId = indexItem.getDefaultIndexItemId();
            values.add(indexItemId);//往里添加主键
        }

        private void OldDataToNew(int x, int lie, String thisCellData, IndexItemTb thisCellIntemData) {
            switch (x) {//字符类型
                case 0: {
                    int qymcCellNum = canUsedCode ? 3 : 2;
                    //orgName是原来的名字
                    Integer varLength = thisCellIntemData.getVarLength();
                    int length = StringUtils.length(thisCellData);
                    if ((length * 2) > varLength) {
                        //如果这个长度大于了设置的长度
                        this.addError("第" + rowNum + "行第" + (lie) + "列输入的长度超出了设置的长度，请检查后上传");
                    }
                    if (orgName != null && lie == qymcCellNum) {//表示是基本信息表
                        String sql = "select * from default_index_item_tb where default_index_item_id=" + values.get(4);
                        Map<String, Object> map = relateInfoDao.queryData(sql);
                        if (MapUtils.isNotEmpty(map)) {//如果能搜到数据
                            String qymc = MapUtils.getString(map, "QYMC");//数据库的老的名字
                            if (StringUtils.isNotBlank(qymc)) {//如果企业名称不为空
                                if (!StringUtils.equals(thisCellData, qymc)) {//企业名称不相等
                                    //默认不修改企业名称
//                                    DefaultIndexItem d = new DefaultIndexItem();
//                                    d.setDefaultIndexItemId(Integer.valueOf(String.valueOf(values.get(4))));
//                                    d.setQymc(orgName);
                                    DefaultIndexItem d2 = new DefaultIndexItem();
                                    d2.setDefaultIndexItemId(Integer.valueOf(String.valueOf(values.get(4))));
                                    d2.setQymc(qymc);//老的名字
                                    String codeCredit = MapUtils.getString(map, "CODE_CREDIT");
                                    String codeOrg = MapUtils.getString(map, "CODE_ORG");
                                    d2.setCodeCredit(codeCredit);
                                    d2.setCodeOrg(codeOrg);
                                    d2.setDefaultIndexItemTime(thisCellData);//上报的名字
                                    changeIndexItem.add(d2);
//                                    defaultIndexItemDao.updateDefaultIndexItem(d);
                                    values.add(qymc);
                                } else {
                                    values.add(thisCellData);
                                }
                            } else {//企业名称为空
                                DefaultIndexItem indexItem = new DefaultIndexItem();
                                indexItem.setDefaultIndexItemId(Integer.valueOf(String.valueOf(values.get(4))));
                                indexItem.setQymc(thisCellData);
                                defaultIndexItemDao.updateDefaultIndexItem(indexItem);
                                values.add(thisCellData);
                            }
                        }
                    } else {//表示不是基本信息表
                        values.add(thisCellData);
                    }
                    break;
                }
                case 1: {//时间类型
                    if (thisCellData == null) {
                        values.add(null);
                    } else {
                        try {
                            values.add(DateUtils.parseDate(thisCellData, times));
                        } catch (Exception e) {
                            this.addError("第" + rowNum + "行第" + (lie) + "列应该是时间类型格式，请检查后上传，正确格式如：\"2016-03-03\"或者\"2016/03/03\"或者\"20160303\"");
                        }
                    }
                    break;
                }
                case 2: {//数值类型
                    if (thisCellData == null) {
                        values.add(null);
                    } else {
                        Matcher matcher = pattern.matcher(StringUtils.trimToNull(thisCellData));
                        if (!matcher.matches()) {//如果不是数值类型
                            this.addError("第" + rowNum + "行第" + (lie) + "列应该是数值类型格式，请检查后上传。");
                        } else {
                            values.add(thisCellData);
                        }
                    }
                    break;
                }
                case 3: {//数据字典
                    if (StringUtils.isBlank(thisCellData)) {
                        values.add(null);
                    } else {
                        Integer dicId = thisCellIntemData.getDicId();//数据字典的id
                        String dicName = null;
                        try {
                            dicName = dicService.getDicByDicId(dicId).getDicName();
                        } catch (Exception e) {
                            //ignore
                        }
                        if (StringUtils.equalsIgnoreCase(dicName, BothEnum.dicOrgAndGov.getDicName())) {//金融机构或者政府部门
                            try {
                                SysOrg sysOrg = sysOrgService.querySysorgByFinancialCode(thisCellData);
                                if (sysOrg == null) {
                                    this.addError("第" + rowNum + "行第" + (lie) + "列输入的金融机构或者政府部门编号查询不到对应的信息，请检查后上传");
                                } else {
                                    values.add(thisCellData);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (StringUtils.equalsIgnoreCase(dicName, BothEnum.dicSysArea.getDicName())) {//地区
                            try {
                                SysArea sysArea = sysAreaService.queryAreaByCode(thisCellData);
                                if (sysArea == null) {
                                    this.addError("第" + rowNum + "行第" + (lie) + "列输入的行政代码查询不到对应的地区，请检查后上传");
                                } else {
                                    values.add(thisCellData);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (StringUtils.equalsIgnoreCase(dicName, BothEnum.dicClassFy.getDicName())) {//行业
                            List<SysClassFyModel> sysClassFyModels = sysClassFyService.queryListByName(thisCellData);
                            if (CollectionUtils.isEmpty(sysClassFyModels)) {
                                SysClassFyModel sysClassFyModel = sysClassFyService.queryModelByCode(thisCellData);
                                if (null == sysClassFyModel) {
                                    this.addError("第" + rowNum + "行第" + (lie) + "列输入的经济行业名称(区分圆角半角符号)或代码查询不到对应的信息，请检查后上传");
                                } else {
                                    values.add(sysClassFyModel.getSysIndustryCode());
                                }
                            } else {
                                if (sysClassFyModels.size() == 1) {
                                    values.add(sysClassFyModels.get(0).getSysIndustryCode());
                                } else {
                                    int index = 0;
                                    int lenth = 0;
                                    for (int i = 0; i < sysClassFyModels.size(); i++) {
                                        if (StringUtils.length(sysClassFyModels.get(i).getSysIndustryNotes()) > lenth) {
                                            lenth = StringUtils.length(sysClassFyModels.get(i).getSysIndustryNotes());
                                            index = i;
                                        }
                                    }
                                    values.add(sysClassFyModels.get(index).getSysIndustryCode());
                                }
                            }
                        } else {
                            try {
                                DicContent data = dicContentService.queryContentByDicIdAndValues(dicId, thisCellData);
                                if (data == null) {
                                    this.addError("第" + rowNum + "行第" + (lie) + "列请输入正确的字典代码或指标值，此列属于数据字典：<"
                                            + dicService.getDicByDicId(thisCellIntemData.getDicId()).getDicName() + ">，请检查后上传");
                                } else {
                                    values.add(data.getDicContentCode());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}
