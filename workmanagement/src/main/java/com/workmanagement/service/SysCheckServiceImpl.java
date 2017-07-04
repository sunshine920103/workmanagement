package com.workmanagement.service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.DefaultIndexItemDao;
import com.workmanagement.dao.ReportIndexDao;
import com.workmanagement.dao.SysCheckDao;
import com.workmanagement.model.*;
import com.workmanagement.util.Calculator;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.SubDataGet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * 校验规则service
 * Created by lzm on 2017/3/16.
 */
@Service(value = "sysCheckService")
public class SysCheckServiceImpl implements SysCheckService {

    private final String[] timeType = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMdd",
            "yyyy-MM-dd HH:mm:ss.s", "yyyy/MM/dd HH:mm:ss.s", "yyyyMMdd HH:mm:ss.s"};
    @Autowired
    private SysCheckDao sysCheckDao;
    @Autowired
    private DefaultIndexItemDao defaultIndexItemDao;
    @Autowired
    private IndexItemTbService indexItemTbService;
    @Autowired
    private SysAreaService sysAreaService;

    @Override
    public List<SysCheck> getAll(PageSupport ps) {
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(sysCheckDao.getAll(), ps);
    }

    /**
     * 添加校验规则
     *
     * @param checkItems      一个或多个指标项id
     * @param sysAreaId       所属机构id
     * @param checkSDate      开始日期
     * @param checkEDate      截止日期
     * @param indexId         关联的指标大类的id
     * @param orgCreatorId    创建的机构的id
     * @param sysCheckFormula 校验公式
     * @return
     * @throws Exception
     */
    @Override
    public Integer insertOne(String checkItems, Integer sysAreaId, String checkSDate,
                             String checkEDate, Integer indexId, Integer orgCreatorId,
                             String sysCheckFormula, String sysCheckExplain) throws Exception {
        SysCheck sysCheck = new SysCheck(checkItems, sysCheckFormula, new Date(), indexId, orgCreatorId);
        this.setDate(checkSDate, checkEDate, sysCheck);
        sysCheck.setSysAreaId(sysAreaId);
        sysCheck.setSysCheckExplain(sysCheckExplain);
        if (sysCheckDao.insertOne(sysCheck) <= 0) {
            throw new Exception();
        }
        return sysCheck.getSysCheckId();
    }

    /**
     * 根据主键获取一条数据
     *
     * @param sysCheckId
     * @return
     * @throws Exception
     */
    @Override
    public SysCheck getOneById(Integer sysCheckId) throws Exception {
        return sysCheckDao.getOneById(sysCheckId);
    }

    /**
     * 根据单个机构id获取一堆数据
     *
     * @param sysAreaIds
     * @return
     * @throws Exception
     */
    @Override
    public List<SysCheck> getSomeByAreaIds(PageSupport ps, List<Integer> sysAreaIds) throws Exception {
        if (ps == null) {
            return sysCheckDao.getSomeByAreaIds(sysAreaIds);
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(sysCheckDao.getSomeByAreaIds(sysAreaIds), ps);
    }

    /**
     * 根据主键删除一条数据
     *
     * @param sysCheckId
     * @return
     * @throws Exception
     */
    @Override
    public boolean delOneById(Integer sysCheckId) throws Exception {
        boolean a = sysCheckDao.delOneById(sysCheckId) > 0;
        if (!a) {
            throw new Exception();
        }
        return true;
    }

    /**
     * 专门校验 银行贷款和银行授信的方法。属于内置校验规则
     *
     * @param txt
     * @param excel
     * @param thisDate
     * @param errorList
     * @param indexTb
     */
    public void checkDkAndSx(List<Map<String, Object>> txt, List<Map<String, Object>> excel, Date thisDate, List<String> errorList, IndexTb indexTb) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer sysOrgId = userDetails.getSys_org_id();//机构id
        Integer sysAreaId = userDetails.getSysOrg().getSys_area_id();//区域id
        String recordTime = DateFormatUtils.format(thisDate, "yyyy-MM");
        List<Integer> allUpAreaIds = sysAreaService.getAllUpAreaIds(sysAreaId);
        /**
         * map的key是defaultId+发生地金融机构的,假设是报银行授信
         */
        boolean isYxsx = false;
        String table = "index_yxdk_tb";
        String title = "index_yxdk_htje";
        if (StringUtils.equalsIgnoreCase(indexTb.getIndexCode(), "index_yxsx")) {
            isYxsx = true;
            table = "index_yxsx_tb";
            title = "index_yxsx_sxed";
        }
        List<IndexItemTb> indexItemTbs = indexItemTbService.getIndexIntemsIsUsedByIdAndAreaIds(indexTb.getIndexId(), allUpAreaIds);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("title", title);
        queryMap.put("table", table);
        queryMap.put("sysOrgId", sysOrgId.toString());
        queryMap.put("sysAreaId", sysAreaId.toString());
        queryMap.put("recordTime", thisDate);
        List<String> thisError = new ArrayList<>();
        Map<String, Double> map = new HashMap<>();
        List<Integer> updIndexItemId = new ArrayList<>();

        List<String> onlyValue = new ArrayList<>();//存字段
        for (IndexItemTb iit : indexItemTbs) {
            if (iit.getIndexItemImportUnique() == 1) {//找出是识别码的列
                onlyValue.add(iit.getIndexItemCode());
            }
        }
        if (txt != null) {//如果是txt上报
            List<Map<String, Object>> biJiaoMap = this.checkTxt(txt, isYxsx, onlyValue);
            try {
                this.setValueToMap(biJiaoMap, map, thisError, thisDate, isYxsx, title, onlyValue, true, queryMap, updIndexItemId);
            } catch (ParseException e) {
                errorList.add("时间格式有误");
            }
        } else {
            try {
                this.setValueToMap(excel, map, thisError, thisDate, isYxsx, title, onlyValue, false, queryMap, updIndexItemId);
            } catch (ParseException e) {
                errorList.add("时间格式有误");
            }
        }
        if (CollectionUtils.isEmpty(thisError)) {
            if (CollectionUtils.isEmpty(updIndexItemId)) {
                updIndexItemId = null;
            }
            //再遍历map
            for (String s : map.keySet()) {
                Double count = MapUtils.getDouble(map, s, 0D);
                /**
                 * 再从数据库里把符合条件的金额查出来
                 */
                String[] keyAndCode = StringUtils.split(s, "|");
                if (isYxsx) {
                    Double yxdkHtje = sysCheckDao.getYxdkHtje(recordTime, Integer.parseInt(keyAndCode[0]), keyAndCode[1]);
                    Double notUpdYxsxSxed = sysCheckDao.getNotUpdYxsxSxed(recordTime, Integer.parseInt(keyAndCode[0]), keyAndCode[1], updIndexItemId);
                    if (notUpdYxsxSxed != null && notUpdYxsxSxed != 0D) {
                        count += notUpdYxsxSxed;
                    }
                    if (yxdkHtje != null && yxdkHtje != 0D) {
                        if (count < yxdkHtje) {
                            DefaultIndexItem indexItem = defaultIndexItemDao.queryById(Integer.valueOf(keyAndCode[0]));
                            String codeCredit = indexItem.getCodeCredit();
                            if (codeCredit != null) {
                                errorList.add("统一社会信用码为\"" + codeCredit + "\"的数据未通过系统内置校验(归档日期当月的：“银行授信—授信额度”≧“银行贷款—合同金额”之和");
                            } else {
                                errorList.add("组织机构代码为\"" + indexItem.getCodeOrg() + "\"的数据未通过系统内置校验(归档日期当月的：“银行授信—授信额度”≧“银行贷款—合同金额”之和");
                            }
                        }
                    }
                } else {
                    Double yxsxSxed = sysCheckDao.getYxsxSxed(recordTime, Integer.parseInt(keyAndCode[0]), keyAndCode[1]);
                    Double notUpdYxdkHtje = sysCheckDao.getNotUpdYxdkHtje(recordTime, Integer.parseInt(keyAndCode[0]), keyAndCode[1], updIndexItemId);
                    if (notUpdYxdkHtje != null && notUpdYxdkHtje != 0D) {
                        count += notUpdYxdkHtje;
                    }
                    if (yxsxSxed != null && yxsxSxed != 0D) {
                        if (count > yxsxSxed) {
                            //表示有问题
                            DefaultIndexItem indexItem = defaultIndexItemDao.queryById(Integer.valueOf(keyAndCode[0]));
                            String codeCredit = indexItem.getCodeCredit();
                            if (codeCredit != null) {
                                errorList.add("统一社会信用码为\"" + codeCredit + "\"的数据未通过系统内置校验(归档日期当月的：“银行授信—授信额度”≧“银行贷款—合同金额”之和");
                            } else {
                                errorList.add("组织机构代码为\"" + indexItem.getCodeOrg() + "\"的数据未通过系统内置校验(归档日期当月的：“银行授信—授信额度”≧“银行贷款—合同金额”之和");
                            }
                        }
                    }
                }

            }
        } else {
            errorList.addAll(thisError);
        }
    }

    private List<Map<String, Object>> checkTxt(List<Map<String, Object>> txt, boolean isYxsx, List<String> onlyValue) {
        List<Map<String, Object>> biJiaoMap = new ArrayList<>();
        Map<Integer, String> txtMap = new HashMap<>();
        List<String> titles = (List<String>) txt.get(0).get(ReportIndexDao.TITLES);//数据库的字段名
        if (isYxsx) {
            this.swichYhsx(titles, txtMap, onlyValue);
        } else {
            this.swichYhdk(titles, txtMap, onlyValue);
        }
        for (Map<String, Object> aTxt : txt) {
            Map<String, Object> objectMap = new HashMap<>();
            List<Object> values = (List<Object>) aTxt.get(ReportIndexDao.VALUES);//要存入数据库的值
            for (Integer x : txtMap.keySet()) {
                objectMap.put(txtMap.get(x), values.get(x));
            }
            objectMap.put("rowNum", aTxt.get("rowNum"));
            biJiaoMap.add(objectMap);
        }
        return biJiaoMap;
    }

    private void setValueToMap(List<Map<String, Object>> data, Map<String, Double> map,
                               List<String> thisError, Date thisDate, boolean isYxsx,
                               String title, List<String> onlyValue, boolean isTxt,
                               Map<String, Object> queryMap, List<Integer> updIndexItemId) throws ParseException {
        String sTime = isYxsx ? "index_yxsx_sxqsrq" : "index_yxdk_dkrq";
        String eTime = isYxsx ? "index_yxsx_sxzzrq" : "index_yxdk_dqr";
        String yxjg = isYxsx ? "index_yxsx_sxywfsdjrjg" : "index_yxdk_dkywfsdjrjg";
        String ids = isYxsx ? "INDEX_YXSX_ID" : "INDEX_YXDK_ID";
        if (isTxt) {
            for (Map<String, Object> dataMap : data) {
                Date sxqsrq = (Date) dataMap.get(sTime);//开始时间
                Date sxzzrq = (Date) dataMap.get(eTime);//结束时间
                if (sxqsrq.getTime() > thisDate.getTime() || sxzzrq.getTime() < thisDate.getTime()) {
                    //如果归档时间不属于开始和结束时间之间
                    thisError.add("第" + dataMap.get("rowNum") + "行数据归档时间必须属于开始和结束时间之间");
                }
                Double thisMoney = MapUtils.getDouble(dataMap, title, 0D);
                String thisJg = String.valueOf(dataMap.get(yxjg));//被授信的机构码
                String key = String.valueOf(dataMap.get("default_index_item_id") + "|" + thisJg);
                Double count = MapUtils.getDouble(map, key, 0D);//先从map里面把上一个的值取出来
                StringBuilder sb = new StringBuilder();
                if (CollectionUtils.isNotEmpty(onlyValue)) {
                    for (String s : onlyValue) {
                        sb.append(" and ").append(s).append("='").append(dataMap.get(s)).append("'");
                    }
                }
                queryMap.put("indexItemId", dataMap.get("default_index_item_id"));
                queryMap.put("value", sb.toString());
                Map<String, Object> mapByOnly = sysCheckDao.getMapByOnly(queryMap);
                if (mapByOnly != null) {
//                    Double title1 = MapUtils.getDouble(mapByOnly, queryMap.get("title"), 0D);
//                    thisMoney += title1;
                    updIndexItemId.add(Integer.valueOf(String.valueOf(mapByOnly.get(ids))));
                }
                map.put(key, (thisMoney + count));
            }
        } else {
            for (Map<String, Object> mapExcel : data) {
                String indexYxsxSxqsrq = String.valueOf(mapExcel.get(sTime.toUpperCase()));//开始时间
                Date sxqsrq = DateUtils.parseDate(indexYxsxSxqsrq, timeType);
                String indexYxsxSxzzrq = String.valueOf(mapExcel.get(eTime.toUpperCase()));//结束时间
                Date sxzzrq = DateUtils.parseDate(indexYxsxSxzzrq, timeType);
                if (sxqsrq.getTime() > thisDate.getTime() || sxzzrq.getTime() < thisDate.getTime()) {
                    //如果归档时间不属于开始和结束时间之间
                    thisError.add("第" + mapExcel.get("NUM") + "行数据归档时间必须属于开始和结束时间之间");
                }
                Double thisMoney = MapUtils.getDouble(mapExcel, title.toUpperCase(), 0D);
                String thisJg = String.valueOf(mapExcel.get(yxjg.toUpperCase()));//被授信的机构码
                String key = String.valueOf(mapExcel.get("DEFAULT_INDEX_ITEM_ID") + "|" + thisJg);
                Double count = MapUtils.getDouble(map, key, 0D);//先从map里面把上一个的值取出来
                StringBuilder sb = new StringBuilder();
                if (CollectionUtils.isNotEmpty(onlyValue)) {
                    for (String s : onlyValue) {
                        sb.append(" and ").append(s).append("='").append(mapExcel.get(s.toUpperCase())).append("'");
                    }
                }
                queryMap.put("indexItemId", mapExcel.get("DEFAULT_INDEX_ITEM_ID"));
                queryMap.put("value", sb.toString());
                Map<String, Object> mapByOnly = null;
                try {
                    mapByOnly = sysCheckDao.getMapByOnly(queryMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mapByOnly != null) {
//                    Double title1 = MapUtils.getDouble(mapByOnly, queryMap.get("title"), 0D);
//                    thisMoney += title1;
                    updIndexItemId.add(Integer.valueOf(String.valueOf(mapByOnly.get(ids))));
                }
                map.put(key, (thisMoney + count));
            }
        }
    }

    /**
     * 进行校验
     *
     * @param items     需要校验的指标项集合
     * @param cellData  这一行的数据,开始
     * @param sysAreaId 需要验证的机构的地区id
     * @param indexId   指标大类id
     * @param thisDate  归档时间
     * @return
     * @throws Exception
     */
    public boolean getCheckData(List<IndexItemTb> items, List<Object> cellData, Integer sysAreaId,
                                Integer indexId, Date thisDate) throws Exception {
        if (items.size() != cellData.size()) {//表示前面的校验出错了
            return false;
        }
        List<Integer> allUpAreaIds = sysAreaService.getAllUpAreaIds(sysAreaId);
        List<SysCheck> list = sysCheckDao.getSysCheckByIndexIdAndAreaIdsAndTime(indexId, allUpAreaIds, thisDate);
        if (CollectionUtils.isNotEmpty(list)) {//有校验规则
            Map<Integer, Integer> integerMap = new HashMap<>();
            for (int xx = 0; xx < items.size(); xx++) {
                //integerMap 里 key 为item 的值, value 为这个值对应的下标
                integerMap.put(items.get(xx).getIndexItemId(), xx);
            }
            for (SysCheck s : list) {//遍历校验规则
                String formula = s.getSysCheckFormula();
                if (StringUtils.contains(formula, ",or,")) {
                    //如果校验公式里包含 or 表示是个 "||" 比较的条件
                    boolean isRight = false;
                    String[] ss = SubDataGet.split(formula, ",or,");
                    //分解出来的公式是多个公式的组合
                    for (String sss : ss) {
                        if (isRight) {
                            //第二次循环进来,如果有一个正确就直接返回正确
                            return true;
                        }
                        if (StringUtils.contains(sss, ",and,")) {
                            //如果这个公式里面包含 and, 就分解这个 and
                            String[] and = SubDataGet.split(sss, ",and,");
                            boolean ab = true;
                            for (String bb : and) {
                                if (!this.checkThisFormula(bb, integerMap, cellData)) {
                                    //如果校验通过了
                                    ab = false;
                                }
                            }
                            if (ab) {
                                isRight = true;
                            }
                        } else {
                            //这个公式里面没有包含 and 表示是一个公式
                            if (this.checkThisFormula(sss, integerMap, cellData)) {
                                //如果这个公式验证通过了
                                isRight = true;
                            }
                        }
                    }
                    return isRight;
                } else if (StringUtils.contains(formula, ",and,")) {
                    //表示校验公式里没有 or 但是有 and
                    String[] ss = SubDataGet.split(formula, ",and,");
                    for (String bb : ss) {
                        if (!this.checkThisFormula(bb, integerMap, cellData)) {
                            //如果有一个没有通过校验直接校验不通过
                            return false;
                        }
                    }
                } else {
                    //表示这是一个公式没有 or 也没有 and
                    if (!this.checkThisFormula(formula, integerMap, cellData)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkThisFormula(String formula, Map<Integer, Integer> integerMap, List<Object> cellData) {
//        String[] formulas = StringUtils.split(formula, ",");//分解出了公式数组，再拼接成真正的公式
        if (!StringUtils.containsAny(formula, "<>!=+-*/")) {
            //如果是非空校验
            if (StringUtils.isNumeric(formula)) {
                //表示如果是数字
                //先获取到在集合里面的下标
                Integer xiaobao = integerMap.get(Integer.valueOf(formula));
                //通过下标从集合里面的值是否不为空
                return cellData.get(xiaobao) != null;
            } else {
                if (formula.startsWith("$")) {
                    String substring = StringUtils.substring(formula, 1);
                    Integer xiaobao = integerMap.get(Integer.valueOf(substring));
                    Object o = cellData.get(xiaobao);
                    if (o != null) {
                        String idCard = String.valueOf(o);
                        String substring1 = StringUtils.substring(idCard, 0, 6);
                        return sysAreaService.queryAreaByCode(substring1) != null && SubDataGet.checkIdCard(idCard);
                    }
                }
            }
        } else {
//            if (StringUtils.startsWith(formulas[formulas.length - 1], "#")) {
//                //如果最后一位是以#开头的表示是个校验值
//                counts = Double.valueOf(StringUtils.removeStart(formulas[formulas.length - 1], "#"));
//                //把符号也赋值了
//                fuhao = formulas[formulas.length - 2];
//                //把这个校验值和符号从数组里删除
//            }
//            if (counts == null) {
//                //如果没有需要校验的值，表示是指标项之间比较
//                if (formulas.length > 3) {
//                    //表示是带计算的
//                    int x = 0;
//                    for (int i = 0; i < formulas.length; i++) {
//                        if (SubDataGet.isMath(formulas[i])) {
//                            x = i;
//                            break;
//                        }
//                    }
//                    //分解成不带比较符号的两组东西
//                    String[] left = Arrays.copyOfRange(formulas, 0, x - 1);
//                    String[] right = Arrays.copyOfRange(formulas, x + 1, formulas.length);
//
//                    StringBuilder sb = new StringBuilder();
//                    for (int s1 = 0; s1 < formulas.length - 2; s1++) {
//                        this.setCont(formulas, s1, integerMap, cellData, sb);
//                    }
//                    cont = Calculator.conversion(sb.toString());
//                    Integer xiabiao = integerMap.get(Integer.valueOf(formulas[formulas.length - 1]));
//                    //获取到这个值
//                    counts = Double.valueOf((String.valueOf(cellData.get(xiabiao))));
//                    fuhao = formulas[formulas.length - 2];
//                } else {
//                    Queue<Double> queue = new LinkedBlockingQueue<>();
//                    for (String ss : formulas) {
//                        if (StringUtils.isNumeric(ss)) {
//                            //表示如果是数字
//                            //先获取到在集合里面的下标
//                            Integer xiaobao = integerMap.get(Integer.valueOf(ss));
//                            //通过下标从集合里面取值
//                            Double aDouble = Double.valueOf(String.valueOf(cellData.get(xiaobao)));
//                            //把值加入到列队里面去
//                            queue.offer(aDouble);
//                        } else {
//                            fuhao = ss;
//                        }
//                    }
//                    cont = queue.poll();
//                    counts = queue.poll();
//                }
//            } else {
//                StringBuilder test = new StringBuilder();
//                //如果有校验值表示需要和校验值比较
//                for (int s1 = 0; s1 < formulas.length - 2; s1++) {
//                    this.setCont(formulas, s1, integerMap, cellData, test);
//                }
//                if (formulas.length == 3) {
//                    //表示没有加减只有比较
//                    cont = Double.valueOf(test.toString());
//                } else {
//                    cont = Calculator.conversion(test.toString());
//                }
//            }
            Double cont = null;//这是前面的值
            Double counts = null;//这是需要校验的值
            String fuhao = null;
//          //分解成不带比较符号的两组东西
            List<String> list = SubDataGet.replaceToList(formula);
            fuhao = list.get(2);//把符号获取到
            try {
                cont = getFormulaDouble(list.get(0), integerMap, cellData);
            } catch (RuntimeException e) {
                return false;
            }
            try {
                counts = getFormulaDouble(list.get(1), integerMap, cellData);
            } catch (RuntimeException e) {
                return false;
            }
//            //把
//            StringBuilder sb = new StringBuilder();
//            for (Integer key : integerMap.keySet()) {
//                if (StringUtils.contains(formula, String.valueOf(key))) {
//                    int i1 = StringUtils.indexOf(formula, String.valueOf(key));
//                    String left = StringUtils.left(formula, i1);
//                    if (!StringUtils.endsWith(left, "#")) {
//                        //如果里面包含这个 id
//                        sb.append(String.valueOf(cellData.get(integerMap.get(key))));
//                    }
//                }
//            }
//            formula = StringUtils.remove(sb.toString(), "#");
            switch (fuhao) {
                case ">": {
                    if (!(cont > counts)) {
                        return false;
                    }
                    break;
                }
                case "<": {
                    if (!(cont < counts)) {
                        return false;
                    }
                    break;
                }
                case "=": {
                    if (!(cont.doubleValue() == counts.doubleValue())) {
                        return false;
                    }
                    break;
                }
                case ">=": {
                    if (!(cont >= counts)) {
                        return false;
                    }
                    break;
                }
                case "<=": {
                    if (!(cont <= counts)) {
                        return false;
                    }
                    break;
                }
                case "!=": {
                    if (cont.doubleValue() == counts.doubleValue()) {
                        return false;
                    }
                    break;
                }
                default: {
                    return false;
                }
            }
        }
        return true;
    }

    private Double getFormulaDouble(String formula, Map<Integer, Integer> integerMap, List<Object> cellData) {
        Double doubles = 0D;
        if (formula.startsWith(",")) {
            //如果是逗号开头就删除逗号
            formula = formula.substring(1);
        }
        if (formula.endsWith(",")) {
            //删除最后的逗号
            formula = StringUtils.removeEnd(formula, ",");
        }
        //去除前后的括号,如果有的话
        if (formula.startsWith("(") && formula.endsWith(")")) {
            formula = formula.substring(1, formula.length() - 1);//取出中间的数
        }
        if (formula.startsWith(",")) {
            //如果是逗号开头就删除逗号
            formula = formula.substring(1);
        }
        if (formula.endsWith(",")) {
            //删除最后的逗号
            formula = StringUtils.removeEnd(formula, ",");
        }
        String[] a = StringUtils.split(formula, ",");
        StringBuilder sb = new StringBuilder();
        for (String b : a) {
            sb.append(b);
        }
        formula = sb.toString();//重新赋值
        if (StringUtils.isNumeric(formula)) {
            //如果是单个指标项
            Integer integer = integerMap.get(Integer.parseInt(formula));//获取下标
            Object o = cellData.get(integer);
            if (o == null) {
                throw new RuntimeException();
            }
            doubles = Double.valueOf(String.valueOf(o));
        } else {
            if (StringUtils.containsAny(formula, "+-*/")) {
                //1.带符号多个指标项
                for (Integer key : integerMap.keySet()) {
                    if (StringUtils.contains(formula, key.toString())) {
                        Object o = cellData.get(integerMap.get(key));
                        if (o == null) {
                            throw new RuntimeException();
                        }
                        formula = StringUtils.replace(formula, key.toString(), String.valueOf(o));
                    }
                }
                doubles = Calculator.conversion(formula);
            } else {
                //2.没有符号可能是数字
                if (formula.startsWith("#")) {
                    doubles = Double.valueOf(StringUtils.substring(formula, 1));
                }
            }
        }
//        if (StringUtils.isNumeric(formula)) {
//            //如果是一个数字表示是指标项
//            Integer integer = integerMap.get(Integer.parseInt(formula));//获取下标
//            Object o = cellData.get(integer);
//            if (o == null) {
//                throw new RuntimeException();
//            }
//            doubles = Double.valueOf(String.valueOf(o));
//        } else {
//            //表示不是指标项
//            if (StringUtils.containsAny(formula, "+-*/")) {
//                //1.带符号多个指标项
//                for (Integer key : integerMap.keySet()) {
//                    if (StringUtils.contains(formula, key.toString())) {
//                        Object o = cellData.get(integerMap.get(key));
//                        if (o == null) {
//                            throw new RuntimeException();
//                        }
//                        formula = StringUtils.replace(formula, key.toString(), String.valueOf(o));
//                    }
//                }
//                doubles = Calculator.conversion(formula);
//            } else {
//                //2.没有符号可能是数字
//                if (formula.startsWith("#")) {
//                    doubles = Double.valueOf(StringUtils.substring(formula, 1));
//                }
//            }
//        }
        return doubles;
    }

    private void setCont(String[] yy, int s1, Map<Integer, Integer> integerMap, List<Object> cellData, StringBuilder test) {
        if (StringUtils.isNumeric(yy[s1])) {
            //如果是数字，表示是指标项id
            //获取到这个指标项在传递过来的数据里的下标
            Integer xiabiao = integerMap.get(Integer.valueOf(yy[s1]));
            //获取到这个值
            String c = (String.valueOf(cellData.get(xiabiao)));
            //拼接成新的公式
            test.append(c);
        } else {
            //如果这个不是数字表示是符号
            test.append(yy[s1]);
        }
    }

    @Override
    public boolean checkCodeCredit(String codeCredit) {
        if (selCode(true)) {
            //如果有开启
            //长度验证
            if (StringUtils.startsWith(codeCredit, "A")) {
                return true;
            }
            if (StringUtils.length(codeCredit) != 18) {
                return false;
            }
            String mustNotIn = "IOSVZ";
            if (StringUtils.containsAny(codeCredit, mustNotIn)) {
                return false;
            }
            String oneString = "159Y";//登记管理部门代码1位
            String one = String.valueOf(codeCredit.charAt(0));
            if (!StringUtils.contains(oneString, one)) {
                //如果没有包含了这个
                return false;
            }
            List<String> twoList = new ArrayList<>();
            twoList.add("1239");//机构编制
            twoList.add("1239");//民政
            twoList.add("123");//工商
            twoList.add("1");//其他
            int x = StringUtils.indexOf(oneString, one);
            String two = String.valueOf(codeCredit.charAt(1));
            if (!StringUtils.contains(twoList.get(x), two)) {
                //没有对应上第一位的
                return false;
            }
            String three = StringUtils.substring(codeCredit, 2, 8);
            if (sysAreaService.queryAreaByCode(three) == null) {
                //查不到行政区划
                return false;
            }
            String four = StringUtils.substring(codeCredit, 8, 17);//组织机构代码
            if (!checkCodeOrgInCredit(four)) {
                return false;
            }
            String duiBi = "0123456789ABCDEFGHJKLMNPQRTUWXY";
            Integer CixWi = 0;
            for (int i = 0; i < 17; i++) {
                Integer Ci = StringUtils.indexOf(duiBi, codeCredit.charAt(i));
                Integer Wi = (int) (Math.pow(3, i) % 31);
                CixWi += (Ci * Wi);
            }
            int i = 31 - (CixWi % 31);
            String last;
            if (i == 31) {
                last = "0";
            } else if (i == 30) {
                last = "Y";
            } else {
                last = String.valueOf(duiBi.charAt(i));
            }
            return StringUtils.endsWith(codeCredit, last);
        } else {
            boolean matches = codeCredit.matches("[A-Z0-9]{18}");
            if (!matches) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCodeOrgInCredit(String codeOrg) {
        boolean matches = codeOrg.matches("[A-Z0-9]{8}[A-Z0-9]");
        return matches && checkContinue(codeOrg);
    }

    private boolean checkContinue(String codeOrg) {
        String duiBi = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Integer[] jiaQuan = {3, 7, 9, 10, 5, 8, 4, 2};
        Integer xx = 0;
        for (int i = 0; i < 8; i++) {
            String s = String.valueOf(codeOrg.charAt(i));
            int i2 = StringUtils.indexOf(duiBi, s);//下标
            xx += (i2 * jiaQuan[i]);
        }
        int i = 11 - (xx % 11);
        String lastCodeOrg = getLastCodeOrg(i);
        return StringUtils.endsWith(codeOrg, lastCodeOrg);
    }

    @Override
    public boolean checkCodeOrg(String codeOrg) {
        if (StringUtils.length(codeOrg) != 10) {
            return false;
        }
        boolean matches = codeOrg.matches("[A-Z0-9]{8}[-—][A-Z0-9]");
        if (!matches) {
            return false;
        }
        if (selCode(false)) {
            //如果有规则
            codeOrg = codeOrg.substring(0, 8) + codeOrg.substring(9);
            return checkContinue(codeOrg);
        }
        return true;
    }

    private String getLastCodeOrg(Integer index) {
        switch (index) {
            case 0: {
                return "0";
            }
            case 1: {
                return "1";
            }
            case 2: {
                return "2";
            }
            case 3: {
                return "3";
            }
            case 4: {
                return "4";
            }
            case 5: {
                return "5";
            }
            case 6: {
                return "6";
            }
            case 7: {
                return "7";
            }
            case 8: {
                return "8";
            }
            case 9: {
                return "9";
            }
            case 10: {
                return "X";
            }
            case 11: {
                return "0";
            }
        }
        return null;
    }

    /**
     * 更新一个校验规则
     *
     * @param sysCheckId
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateOne(Integer sysCheckId, String checkItems, Integer sysAreaId, String checkSDate,
                             String checkEDate, Integer indexId, Integer orgCreatorId, String sysCheckFormula,
                             String sysCheckExplain) throws Exception {
        SysCheck sysCheck = new SysCheck(checkItems, sysCheckFormula, null, indexId, orgCreatorId);
        sysCheck.setSysCheckId(sysCheckId);
        sysCheck.setSysAreaId(sysAreaId);
        sysCheck.setSysCheckExplain(sysCheckExplain);
        this.setDate(checkSDate, checkEDate, sysCheck);
        if (sysCheckDao.updateOne(sysCheck) <= 0) {
            throw new Exception();
        }
        return true;
    }

    private void setDate(String checkSDate, String checkEDate, SysCheck sysCheck) throws ParseException {
        checkSDate = StringUtils.trimToNull(checkSDate);
        checkEDate = StringUtils.trimToNull(checkEDate);
        if (StringUtils.isNotBlank(checkSDate)) {
            Date sDate = DateUtils.parseDate(checkSDate, timeType);
            sysCheck.setSysCheckSDate(sDate);//开始时间
            if (StringUtils.isNotBlank(checkEDate)) {
                Date eDate = DateUtils.parseDate(checkEDate, timeType);
                sysCheck.setSysCheckEDate(eDate);//结束时间
            }
        }
    }

    private void swichYhsx(List<String> titles, Map<Integer, String> txtMap, List<String> onlyValue) {
        for (int i = 0; i < titles.size(); i++) {
            String value = titles.get(i);
            switch (value) {
                case "default_index_item_id": {
                    txtMap.put(i, value);
                    break;
                }
                case "index_yxsx_sxed": {
                    txtMap.put(i, value);
                    break;
                }
                case "index_yxsx_sxqsrq": {
                    txtMap.put(i, value);
                    break;
                }
                case "index_yxsx_sxzzrq": {
                    txtMap.put(i, value);
                    break;
                }
                case "index_yxsx_sxywfsdjrjg": {
                    txtMap.put(i, value);
                    break;
                }
            }
            if (CollectionUtils.isNotEmpty(onlyValue)) {
                for (String s : onlyValue) {
                    if (StringUtils.equals(s, value)) {
                        txtMap.put(i, s);
                    }
                }
            }
        }
    }

    private void swichYhdk(List<String> titles, Map<Integer, String> txtMap, List<String> onlyValue) {
        for (int i = 0; i < titles.size(); i++) {
            String value = titles.get(i);
            switch (value) {
                case "default_index_item_id": {
                    txtMap.put(i, value);
                    break;
                }
                case "index_yxdk_htje": {
                    txtMap.put(i, value);
                    break;
                }
                case "index_yxdk_dkrq": {
                    txtMap.put(i, value);
                    break;
                }
                case "index_yxdk_dqr": {
                    txtMap.put(i, value);
                    break;
                }
                case "index_yxdk_dkywfsdjrjg": {
                    txtMap.put(i, value);
                    break;
                }
            }
            if (CollectionUtils.isNotEmpty(onlyValue)) {
                for (String s : onlyValue) {
                    if (StringUtils.equals(s, value)) {
                        txtMap.put(i, s);
                    }
                }
            }
        }
    }

    private boolean insertCode(boolean isCodeCredit) {
        if (isCodeCredit) {
            return sysCheckDao.insertCode("统一社会信用代码") > 0;
        }
        return sysCheckDao.insertCode("组织机构代码") > 0;
    }

    private boolean delCode(boolean isCodeCredit) {
        if (isCodeCredit) {
            return sysCheckDao.delCode("统一社会信用代码") > 0;
        }
        return sysCheckDao.delCode("组织机构代码") > 0;
    }

    @Override
    public boolean selCode(boolean isCodeCredit) {
        if (isCodeCredit) {
            return sysCheckDao.selCode("统一社会信用代码") > 0;
        }
        return sysCheckDao.selCode("组织机构代码") > 0;
    }

    @Override
    public boolean changeCode(boolean isCodeCredit) {
        boolean heiRenYaGao;
        //统一码
        if (selCode(isCodeCredit)) {
            heiRenYaGao = delCode(isCodeCredit);
        } else {
            heiRenYaGao = insertCode(isCodeCredit);
        }
        return heiRenYaGao;
    }
}
