package com.workmanagement.controller;

import com.google.gson.reflect.TypeToken;
import com.workmanagement.model.*;
import com.workmanagement.service.*;
import com.workmanagement.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验管理controller
 * 只有超级管理员和人行管理员能进来。表示
 * Created by lzm on 2017/3/16.
 */
@Controller
@RequestMapping(value = "/admin/reportVerifySet")
public class SysCheckController {

    @Autowired
    private SysOrgTypeService sysOrgTypeService;
    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private SysCheckService sysCheckService;
    @Autowired
    private IndexItemTbService indexItemTbService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private IndexTbService indexTbService;
    @Autowired
    private SysManageLogService manageLogService;
    @Resource(name = "subDataGet")
    private SubDataGet subDataGet;
    private ScriptEngine js = new ScriptEngineManager().getEngineByName("JavaScript");
    private Pattern checkItem = Pattern.compile("^[\\u4e00-\\u9fa5\\_\\（\\）]{0,}$");
    private Pattern checkDouble = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");

    @RequestMapping(value = "/index")
    public String index(Model model, HttpSession session, HttpServletRequest request) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        PageSupport ps = PageSupport.initPageSupport(request);
        List<SysCheck> list = null;
        if (userDetails.getSys_role_id() == 1) {
            //表示超级管理员
            list = sysCheckService.getAll(ps);
            model.addAttribute("isAdmin", "isAdmin");
            boolean b = sysCheckService.selCode(true);
            boolean b1 = sysCheckService.selCode(false);
            model.addAttribute("isOpenCodeCredit", String.valueOf(b));
            model.addAttribute("isOpenCodeOrg", String.valueOf(b1));
        } else {
            List<Integer> allUpAreaIds = sysAreaService.getAllUpAreaIds(userDetails.getSysOrg().getSys_area_id());
            list = sysCheckService.getSomeByAreaIds(ps, allUpAreaIds);//列表显示的数据
        }
        this.setNewName(list, userDetails.getSys_org_id());
        model.addAttribute("sysCheckList", list);
        return "reportVerifySet/index";
    }

    /**
     * 超级管理员设置是否校验组织机构代码以及统一社会信用码
     *
     * @return
     */
    @RequestMapping("/changeCheck")
    @ResponseBody
    public boolean changeCheck(Integer type) {
        boolean heiRenYaGao;
        if (type == 0) {
            //统一码
            heiRenYaGao = sysCheckService.changeCode(true);
        } else {
            //社会码
            heiRenYaGao = sysCheckService.changeCode(false);
        }
        return heiRenYaGao;
    }

    /**
     * 点击新增校验。
     * 传递机构列表
     * 传递指标大类列表
     *
     * @return
     */
    @RequestMapping(value = "/addOrUpdate")
    public String add(Model model, HttpSession session, Integer sysCheckId) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        //不管是谁都只能看到自己能看到的指标大类
        List<Integer> sysAreaIds = sysAreaService.getAllUpAreaIds(userDetails.getSysOrg().getSys_area_id());
        List<IndexTb> indexTbList = indexTbService.queryIndexBySysAreaIds(null, sysAreaIds);
        model.addAttribute("indexTbList", indexTbList);
        if (sysCheckId != null) {//修改操作
            Integer type = 1;
            SysCheck sysCheck1 = sysCheckService.getOneById(sysCheckId);
            if (!StringUtils.containsAny(sysCheck1.getSysCheckFormula(), "+=-/*><()")) {
                //如果不包含表示是非空校验
                type = 0;
            }
            model.addAttribute("type", type);
            this.setNewOneName(sysCheck1, userDetails.getSys_org_id());
//            model.addAttribute("sysCheck", sysCheck1);
//            List<Integer> orgIds = sysCheck1.getSysCheckLinked();
//            String[] strings = new String[orgIds.size()];
//            for (int i = 0; i < orgIds.size(); i++) {
//                strings[i] = String.valueOf(orgIds.get(i));
//            }
//            List<SysOrg> sysOrgs = sysOrgService.querySysOrgByOrgIds(strings);
//            model.addAttribute("sysOrgs", sysOrgs);
            model.addAttribute("indexTb", indexTbService.queryById(sysCheck1.getIndexId()));
            List<IndexItemTb> indexItemTbList;
            model.addAttribute("sysCheck", sysCheck1);
            if (StringUtils.containsAny(sysCheck1.getSysCheckFormula(), "+=-*/><")) {
                //如果包含表示是数值校验
                indexItemTbList = indexItemTbService.getIndexItemsByIdAndType(sysCheck1.getIndexId(), 2, sysAreaIds);
            } else {
                indexItemTbList = indexItemTbService.getIndexIntemsIsUsedByIdAndAreaIdsAndCanNull(sysCheck1.getIndexId(), sysAreaIds);
            }
            model.addAttribute("indexItemTbList", indexItemTbList);
        }
        return "reportVerifySet/add";
    }

    /**
     * 删除操作。获取这条数据的主键，再把数据里的机构id和上级机构id做比较。如果属于上级机构id，则不允许删除
     *
     * @param sysCheckId
     * @return
     */
    @RequestMapping(value = "/delOne")
    @ResponseBody
    public String delOne(@RequestParam Integer sysCheckId, HttpSession session, HttpServletRequest request) throws Exception {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        Integer creatAreaId = sysCheckService.getOneById(sysCheckId).getOrgCreatorId();//创建机构的id
        if (!(userDetails.getSys_org_id().intValue() == creatAreaId.intValue())) {
            this.log(null, null, null, SysManageLog.DELECT_SYSMANAGElOG, false, request);
            return "删除失败，不能删除其他机构创建的校验规则";
        } else {//当前机构和创建机构id是一样的，表示可以删除
            String oldValue = sysCheckService.getOneById(sysCheckId).toString();
            boolean isSuccess = sysCheckService.delOneById(sysCheckId);
            if (isSuccess) {
                this.log(null, oldValue, null, SysManageLog.DELECT_SYSMANAGElOG, true, request);
                return "操作成功";
            } else {
                this.log(null, null, null, SysManageLog.DELECT_SYSMANAGElOG, false, request);
                return "未知错误，删除失败";
            }
        }
    }

//    /**
//     * 点击  获取用户所能看到的机构列表
//     *
//     * @param session
//     * @return
//     */
//    @RequestMapping(value = "/getSysOrgs")
//    @ResponseBody
//    public List<SysOrg> getSysOrgs(HttpSession session) {
//        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
//        Integer roleId = userDetails.getSysRole().getSys_role_id();
//        Integer roleType = userDetails.getSysRole().getSys_role_type();
//        String key = RedisKeys.SYS_AREA + "sysCheck_getSysOrgs" + userDetails.getSys_org_id() + roleId + roleType;
//        List<SysOrg> list = null;
//        if (RedisUtil.isEmpty((key))) {
//            list = new ArrayList<>();
//            if (roleId == 1) {//表示是超级管理员
//                list = sysOrgService.queryAll();
//            } else {
//                if (roleType == 1) {//人行管理员
//                    Integer areaId = userDetails.getSysOrg().getSys_area_id();
//                    List<Integer> allUpAreaIds = sysAreaService.getAllSubAreaIds(areaId);
//                    String[] strings = new String[allUpAreaIds.size()];
//                    for (int i = 0; i < strings.length; i++) {
//                        strings[i] = String.valueOf(allUpAreaIds.get(i));
//                    }
//                    list = sysOrgService.querySysOrgByAreaIds(strings);
//                } else {
//                    SysOrg org = userDetails.getSysOrg();
//                    subDataGet.getAllOrgs(org, list);
//                }
//            }
//            RedisUtil.setData(key, list);
//        } else {
//            Type orgType = new TypeToken<List<SysOrg>>() {
//            }.getType();
//            list = RedisUtil.getListData(key, orgType);
//        }
//        return list;
//    }

    /**
     * 点击  获取用户所能看到的机构列表
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/getSysOrgs")
    @ResponseBody
    public List<ZtreeVo> getSysOrgs(HttpSession session) {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        List<SysOrg> list = this.getSysOrg(userDetails);
        List<SysOrgType> topOrgTypes = sysOrgTypeService.getTypesByOrgIds(list);
        return DataUtil.getZtreeTwo(list, topOrgTypes);
    }

    private List<SysOrg> getSysOrg(MyUserDetails userDetails) {
        Integer roleId = userDetails.getSysRole().getSys_role_id();
        Integer roleType = userDetails.getSysRole().getSys_role_type();
        String key = RedisKeys.SYS_ORG + "sysCheck_getSysOrgs" + userDetails.getSys_org_id() + roleId + roleType;
        List<SysOrg> list = null;
        if (RedisUtil.isEmpty((key))) {
            if (roleId == 1) {//表示是超级管理员
                list = sysOrgService.queryAll();
            } else {
                if (roleType == 1) {//人行管理员
                    Integer areaId = userDetails.getSysOrg().getSys_area_id();
                    List<Integer> allUpAreaIds = sysAreaService.getAllSubAreaIds(areaId);
                    String[] strings = new String[allUpAreaIds.size()];
                    for (int i = 0; i < strings.length; i++) {
                        strings[i] = String.valueOf(allUpAreaIds.get(i));
                    }
                    list = sysOrgService.querySysOrgByAreaIds(strings);
                } else {
                    list = new ArrayList<>();
                    SysOrg org = userDetails.getSysOrg();
                    subDataGet.getAllOrgs(org, list);
                }
            }
            RedisUtil.setData(key, list);
        } else {
            Type orgType = new TypeToken<List<SysOrg>>() {
            }.getType();
            list = RedisUtil.getListData(key, orgType);
        }
        return list;
    }

    private boolean checkKuoHao(String formula) {
        if (formula.contains(")") && !formula.contains("(")) {
            //如果有右括号却没有左括号
            return false;
        }
        if (formula.indexOf(")") < formula.indexOf("(")) {
            //如果右括号跑前面去了
            return false;
        }
        if (formula.contains("(")) {
            int i = formula.indexOf("(");
            while (i != -1) {
                formula = formula.substring(i + 1);
                if (!formula.contains(")")) {
                    return false;
                } else {
                    int i1 = formula.indexOf(")");
                    //如果
                    if (formula.contains("(")) {
                        int i2 = formula.indexOf("(");
                        if (i2 < i1) {
                            return false;
                        }
                    }
                    formula = formula.substring(i1 + 1);
                    i = formula.indexOf("(");
                }
            }
        }
        return true;
    }

    private boolean checkMath(String[] left, List<String> list) {
        Matcher matcher;
        Matcher matcher2;
        if (left.length == 1 || ((Arrays.binarySearch(left, ")") != -1
                && Arrays.binarySearch(left, "(") != -1)) && left.length == 3) {
            //表示不是多个指标项相加
            for (String s : left) {
                matcher = checkItem.matcher(s);
                matcher2 = checkDouble.matcher(s);
                if (matcher.matches()) {
                    //如果是中文
                    if (!list.contains(s)) {
                        //不包含在里面
                        return false;
                    }
                } else if (matcher2.matches()) {
                    //如果是数字
                } else {
                    //如果不是中文也不是数字
                    return false;
                }
            }
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : left) {
                matcher = checkItem.matcher(s);
                matcher2 = checkDouble.matcher(s);
                if (matcher.matches()) {
                    //如果是中文
                    if (!list.contains(s)) {
                        return false;
                    }
                    sb.append("10");
                } else {
                    //如果是符号或者是数字
                    if (matcher2.matches()) {
                        //如果是数字
                        sb.append(s);
                    } else {
                        //如果不是数字
                        if (!SubDataGet.isDontCare(s)) {
                            //不属于加减乘除和左右括号
                            return false;
                        }
                        sb.append(s);
                    }
                }
            }
            try {
                js.eval(sb.toString());
            } catch (ScriptException e) {
                return false;
            }
        }
        return true;
    }

    //重新设置公式,分解之后直接进去运算看有没有错
    private String reSetFormula(String formula, Map<String, Integer> map) {
        if (MapUtils.isNotEmpty(map)) {
            for (String key : map.keySet()) {
                if (StringUtils.contains(formula, key)) {
                    //如果包含这个 key
                    formula = StringUtils.replace(formula, key, "|");
                    formula = StringUtils.replace(formula, "|", String.valueOf(map.get(key)));
                }
            }
        }
        return formula;
    }

    /**
     * 验证用户输入的公式是否正确
     *
     * @param formula 公式
     * @return
     */
    @RequestMapping(value = "/checkValue")
    @ResponseBody
    public boolean checkValue(@RequestParam String formula, @RequestParam Integer indexId, Integer type, HttpSession session) {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        //不管是谁都只能看到自己能看到的指标大类
        List<Integer> sysAreaIds = sysAreaService.getAllUpAreaIds(userDetails.getSysOrg().getSys_area_id());
        //分解字符串看是否有输入错误的
        if (!SubDataGet.isHaveYiQi(formula)) {
            return false;
        }
        List<IndexItemTb> indexItemTbs;
        String[] strings = {"or", "and"};
        formula = StringUtils.replaceEach(formula, new String[]{" ", "\n", "\r"}, new String[]{"", "", ""});
        if (!checkKuoHao(formula)) {
            //括号不能黏在一起
            return false;
        }
        if (type != 1) {
            //表示是非空校验
            if (StringUtils.containsAny(formula, "<()!>+-*/=")
                    || StringUtils.startsWithAny(formula, strings)
                    || StringUtils.endsWithAny(formula, strings)) {
                //不能包含括号和符号,以及 or 和 and 开头和结尾
                return false;
            }
            if (StringUtils.contains(formula, "$")) {
                //如果包含 $ 符号表示是属于身份证校验
                int x = formula.indexOf("$");
                while (x != -1) {
                    if (formula.substring(x + 1).indexOf("$") == 0) {
                        //不能两个$符号连在一起
                        return false;
                    }
                    String left = StringUtils.left(formula, formula.indexOf("$"));
                    String right = StringUtils.right(formula, ((formula.length() - formula.lastIndexOf("$")) - 1));
                    formula = left + right;
                    x = formula.indexOf("$");
                }
            }
            indexItemTbs = indexItemTbService.getIndexIntemsIsUsedByIdAndAreaIdsAndCanNull(indexId, sysAreaIds);
            List<String> formulaList = this.getFormula(formula);
            List<String> isIn = new ArrayList<>();

            for (IndexItemTb iit : indexItemTbs) {
                isIn.add(iit.getIndexItemName());
            }
            for (String ss : formulaList) {
                if (!isIn.contains(ss)) {
                    return false;
                }
            }
        } else {
            if (StringUtils.startsWithAny(formula, strings) || StringUtils.endsWithAny(formula, strings)) {
                return false;
            }
            indexItemTbs = indexItemTbService.getIndexItemsByIdAndType(indexId, 2, sysAreaIds);
//            List<String> list = new ArrayList<>();
            //以名字为 key , id 为 value
            Map<String, Integer> map = new HashMap<>();
//            List<String> isHaveOne;//用于逻辑判断,比如自己不能大于自己
            for (IndexItemTb ii : indexItemTbs) {
                map.put(ii.getIndexItemName(), ii.getIndexItemId());
//                list.add(ii.getIndexItemName());
            }
            //获取公式
            List<String> formulaList = getFormula(formula);
            //分解公式
            for (String s : formulaList) {
//                isHaveOne = new ArrayList<>();
                s = reSetFormula(s, map);
                String[] value = SubDataGet.splitFormula(s);
                if (value.length < 3) {//最小的位数为3
                    return false;
                }
//                for (String b : value) {
//                    if (isHaveOne.contains(b) && !SubDataGet.isCode(b)) {
//                        //如果他已经在里面了就不通过
//                        return false;
//                    } else {//没有在里面
//                        isHaveOne.add(b);
//                    }
//                }
                boolean a = false;
                int x = 0;
                for (int i = 0; i < value.length; i++) {
                    if (SubDataGet.isMath(value[i])) {
                        //如果是比较符号
                        if (a) {
                            return false;
                        } else {
                            a = true;
                            x = i;
                        }
                    }
                }
                if (!a) {
                    return false;
                }
//                //分解成不带比较符号的两组东西
                String[] left = Arrays.copyOfRange(value, 0, x);
                String[] right = Arrays.copyOfRange(value, x + 1, value.length);
                if (StringUtils.equals(Arrays.toString(left), Arrays.toString(right))) {
                    //比较符号左右两边不能相等
                    return false;
                }
//                //校验两组是否是正确的校验值
                if (!chekNmb(left)) return false;
                if (!chekNmb(right)) return false;
//                if (!this.checkMath(left, list)) return false;
//                if (!this.checkMath(right, list)) return false;
//                //都通过了之后
            }
        }
        return true;
    }

    private boolean chekNmb(String[] strings) {
        if (strings != null) {
            StringBuilder sb = new StringBuilder();
            for (String s : strings) {
                sb.append(s);
            }
            try {
                js.eval(sb.toString());
            } catch (ScriptException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 提交新的校验规则录入数据库
     *
     * @param session
     * @param sTime
     * @param eTime
     * @param indexId
     * @param formula
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/submit")
    @ResponseBody
    public String submit(HttpSession session,
                         @RequestParam String sTime, String eTime,
                         @RequestParam Integer indexId, @RequestParam String formula,
                         Integer sysCheckId, String sysCheckExplain, HttpServletRequest request) throws Exception {
        formula = StringUtils.replaceEach(formula, new String[]{" ", "\n", "\r"}, new String[]{"", "", ""});
        sysCheckExplain = StringUtils.trimToNull(sysCheckExplain);
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        //不管是谁都只能看到自己能看到的指标大类
        Integer areaId = userDetails.getSysOrg().getSys_area_id();
        List<Integer> sysAreaIds = sysAreaService.getAllUpAreaIds(areaId);
        List<IndexItemTb> list = indexItemTbService.getIndexIntemsIsUsedByIdAndAreaIds(indexId, sysAreaIds);
        String indexName = indexTbService.queryById(indexId).getIndexName();
        Map<String, Integer> map = new HashMap<>();
        for (IndexItemTb ii : list) {//把该指标大类下的指标项装进map里，以名字为key，主键为value
            map.put(ii.getIndexItemName(), ii.getIndexItemId());
        }
        formula = this.replace(formula);
//        Matcher matcher;
//        String[] strings = SubDataGet.splitFormula(formula);
//        StringBuilder sb = new StringBuilder();
//        for (String s : strings) {
//            matcher = checkDouble.matcher(s);
//            if (matcher.matches()) {
//                //如果是数字
//                sb.append("#").append(s).append(",");
//            } else {
//                sb.append(s).append(",");
//            }
//        }
//        formula = sb.deleteCharAt(sb.lastIndexOf(",")).toString();
        for (String key : map.keySet()) {
            if (StringUtils.contains(formula, key)) {
                formula = StringUtils.replace(formula, key, map.get(key).toString() + ",");
            }
        }
        if (StringUtils.endsWith(formula, ",")) {
            formula = StringUtils.removeEnd(formula, ",");
        }
//        for (String s : map.keySet()) {
//            if (StringUtils.startsWith(s, "$")) {
//                s = StringUtils.remove(s, "$");
//                if (StringUtils.contains(formula, s)) {
//                    //如果是第一个位置的话,公式改变为替换
//                    String sbu = String.valueOf(MapUtils.getInteger(map, s));
//                    formula = StringUtils.replace(formula, s, ("$" + sbu + ","));
//                }
//            } else {
//                if (StringUtils.contains(formula, s)) {
//                    //如果是第一个位置的话,公式改变为替换
//                    String sbu = String.valueOf(MapUtils.getInteger(map, s));
//                    formula = StringUtils.replace(formula, s, (sbu + ","));
//                }
//            }
//        }
//        formula = StringUtils.removeEnd(formula, ",");
        if (StringUtils.isNotBlank(sysCheckExplain)) {
            SysArea upOrThisSysArea = sysAreaService.getUpOrThisSysArea(userDetails.getSysOrg().getSys_area_id());
            sysCheckExplain = sysCheckExplain +
                    "(" + sysAreaService.getAreaNotSub(upOrThisSysArea.getSysAreaId()).getSysAreaName() + ")";
        }
        Integer creatorOrgId = ((MyUserDetails) session.getAttribute("sessionUser")).getSys_org_id();
        if (sysCheckId != null) {//修改操作
            if (!(creatorOrgId.intValue() == sysCheckService.getOneById(sysCheckId).getOrgCreatorId().intValue())) {
                this.log(indexId, null, null, SysManageLog.UPDATE_SYSMANAGElOG, false, request);
                return "修改失败，不能修改其他机构创建的校验规则";
            } else {
                String oldValue = sysCheckService.getOneById(sysCheckId).toString();
                sysCheckService.updateOne(sysCheckId, indexName, areaId, sTime, eTime, indexId, creatorOrgId, formula, sysCheckExplain);
                this.log(indexId, oldValue, sysCheckService.getOneById(sysCheckId).toString(), SysManageLog.UPDATE_SYSMANAGElOG, true, request);
                return "修改成功";
            }
        } else {//新增操作
            Integer id = sysCheckService.insertOne(indexName, areaId, sTime, eTime, indexId, creatorOrgId, formula, sysCheckExplain);
            String newValue = sysCheckService.getOneById(id).toString();
            this.log(indexId, null, newValue, SysManageLog.INSERT_SYSMANAGElOG, true, request);
            return "新增成功";
        }
    }

    /**
     * 根据指标大类获取指标项
     *
     * @param indexId
     * @return
     */
    @RequestMapping(value = "/getItems")
    @ResponseBody
    public List<IndexItemTb> getItems(@RequestParam Integer indexId, @RequestParam Integer type, HttpSession session) {
        MyUserDetails userDetails = (MyUserDetails) session.getAttribute("sessionUser");
        //不管是谁都只能看到自己能看到的指标大类
        List<Integer> sysAreaIds = sysAreaService.getAllUpAreaIds(userDetails.getSysOrg().getSys_area_id());
        List<IndexItemTb> indexItemTbs;
        if (type == 1) {
            //如果是验证数字型
            indexItemTbs = indexItemTbService.getIndexItemsByIdAndType(indexId, 2, sysAreaIds);
        } else {
            indexItemTbs = indexItemTbService.getIndexIntemsIsUsedByIdAndAreaIdsAndCanNull(indexId, sysAreaIds);
        }
        return indexItemTbs;
    }

    private String replace(String formula) {
        String[] a = {"<", "+", "-", "*", "/", "!=", "(", ")", ">", "=", ">=", "<=", "and", "or"};
        String[] b = {"<,", "+,", "-,", "*,", "/,", "!=,", "(,", "),", ">,", "=,", ">=,", "<=,", "and,", "or,"};
        formula = StringUtils.replaceEach(formula, a, b);
        StringBuilder sb = new StringBuilder();
        boolean isStart = false;
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < formula.length(); i++) {
            if (StringUtils.isNumeric(String.valueOf(formula.charAt(i)))
                    || StringUtils.equals(String.valueOf(formula.charAt(i)), ".")) {
                //如果是属于数字的一部分
                sb.append(String.valueOf(formula.charAt(i)));
                isStart = true;
                if (i == formula.length() - 1) {
                    //如果是遍历的最后一次
                    sb2.append("#").append(sb).append(",");
                }
            } else {
                if (isStart) {
                    //如果是刚刚结束的
                    isStart = false;
                    sb2.append("#").append(sb).append(",");
                    sb = new StringBuilder();
                    sb2.append(String.valueOf(formula.charAt(i)));
                } else {
                    //如果不是属于数字就直接加进去
                    sb2.append(String.valueOf(formula.charAt(i)));
                }
            }
        }
        return sb2.toString();
    }

    /**
     * 分解公式只用于校验的时候
     *
     * @param formula
     * @return
     */
    private List<String> getFormula(String formula) {
        List<String> formulaList = new ArrayList<>();
        //如果包含"并且"或者"或"
        if (StringUtils.contains(formula, "and") || StringUtils.contains(formula, "or")) {
            formula = StringUtils.replaceEach(formula, new String[]{"and", "or"}, new String[]{"|", "|"});
            String[] split = StringUtils.split(formula, "|");
            formulaList.addAll(Arrays.asList(split));
        } else {
            formulaList.add(formula);
        }
        return formulaList;
    }

    /**
     * 给页面显示格式化后的数据
     *
     * @param list
     * @return
     */
    private void setNewName(List<SysCheck> list, Integer sysOrgId) throws Exception {
        if (CollectionUtils.isNotEmpty(list)) {
            for (SysCheck sk : list) {
                //把名字改一改
                this.setNewOneName(sk, sysOrgId);
            }
        }
    }

    /**
     * 给页面显示格式化后的数据
     *
     * @param sysCheck
     * @return
     */
    private void setNewOneName(SysCheck sysCheck, Integer sysOrgId) throws Exception {
        String formula = sysCheck.getSysCheckFormula();
//        String[] split = StringUtils.split(formula, ",");
//        StringBuilder sb = new StringBuilder();
//        for (String s : split) {
//            if (s.startsWith("$")) {
//                s = StringUtils.remove(s, "$");
//                String indexItemName = indexItemTbService.queryIndexItemTbById(Integer.valueOf(s)).getIndexItemName();
//                sb.append("$").append(indexItemName);
//            } else {
//                if (StringUtils.isNumeric(s)) {
//                    //如果是 number 数字
//                    String indexItemName = indexItemTbService.queryIndexItemTbById(Integer.valueOf(s)).getIndexItemName();
//                    sb.append(indexItemName);
//                } else {
//                    //如果不是 number 的表示是符号或者校验值
//                    if (SubDataGet.isCode(s)) {
//                        //如果是符号
//                        sb.append(s);
//                    } else {
//                        if (StringUtils.equals(s, "and") || StringUtils.equals(s, "or")) {
//                            sb.append(s);
//                        } else {
//                            //表示是校验值
//                            sb.append(StringUtils.right(s, s.length() - 1));
//                        }
//                    }
//                }
//            }
//        }
        Matcher matcher;
        StringBuilder sb = new StringBuilder();
//        String[] strings = SubDataGet.splitFormula(formula);
        String[] strings = StringUtils.split(formula, ",");
//        Integer areaId = sysOrgService.getByIdNotHaveSub(sysOrgId).getSys_area_id();
//        List<Integer> allUpAreaIds = sysAreaService.getAllUpAreaIds(areaId);
//        List<IndexItemTb> indexItemTbs;
//        if (StringUtils.containsAny(formula, "+-*/=!")) {
//            //表示是校验数值
//            indexItemTbs = indexItemTbService.getIndexItemsByIdAndType(sysCheck.getIndexId(), 2, allUpAreaIds);
//        } else {
//            //表示非空
//            indexItemTbs = indexItemTbService.getIndexIntemsIsUsedByIdAndAreaIdsAndCanNull(sysCheck.getIndexId(), allUpAreaIds);
//        }
//        Map<Integer, String> map = new HashMap<>();
//        for (IndexItemTb it : indexItemTbs) {
//            map.put(it.getIndexItemId(), it.getIndexItemName());
//        }
//        for (Integer ii : map.keySet()){
//            if (StringUtils.contains(formula,ii.toString())){
//                //如果包含
//                String sub = StringUtils.substring(formula,)
//            }
//        }
        for (String s : strings) {
            matcher = checkDouble.matcher(s);
            if (matcher.matches()) {
                //如果是数字
                String indexItemName = indexItemTbService.queryIndexItemTbById(Integer.valueOf(s)).getIndexItemName();
                sb.append(indexItemName);
            } else {
                if (s.startsWith("$")) {
                    s = s.substring(1);
                    String indexItemName = indexItemTbService.queryIndexItemTbById(Integer.valueOf(s)).getIndexItemName();
                    sb.append("$").append(indexItemName);
                } else if (s.startsWith("#")) {
                    s = s.substring(1);
                    sb.append(s);
                } else {
                    sb.append(s);
                }
            }
        }
        String sysAreaName = sysAreaService.getAreaNotSub(sysCheck.getSysAreaId()).getSysAreaName();
        sysCheck.setSysCheckItems(sysAreaName + ":" + sysCheck.getSysCheckItems());
        if (sysCheck.getOrgCreatorId().intValue() != sysOrgId.intValue()) {
            //如果不是本机构创建的校验规则,就没有创建时间
            sysCheck.setSysCheckCTime(null);
        }
        sysCheck.setSysCheckFormula(sb.toString());
    }

//    /**
//     * 获取有权限看的指标大类归属地区的ids
//     *
//     * @param userDetails
//     * @return
//     */
//    private List<Integer> getSysAreaIds(MyUserDetails userDetails) {
//        Integer sysAreaId = userDetails.getSysOrg().getSys_area_id();//这是登陆用户的所属区域ID
//        String key = RedisKeys.SYS_AREA + "sysCheck_sysAreaIds" + sysAreaId + userDetails.getSys_role_id();
//        List<Integer> list = null;
//        if (RedisUtil.isEmpty(key)) {
//            if (userDetails.getSys_role_id() == 1) {
//                list = sysAreaService.queryAllIds();
//            } else {
//                list = sysAreaService.getAllUpAreaIds(sysAreaId);
//            }
//            RedisUtil.setData(key, list);
//        } else {
//            Type orgType = new TypeToken<List<Integer>>() {
//            }.getType();
//            list = RedisUtil.getListData(key, orgType);
//        }
//        return list;
//    }

//    /**
//     * 获取当前机构和下属机构
//     * 当前机构下标为0
//     *
//     * @return
//     */
//    private List<SysOrg> getDownSysOrg(MyUserDetails userDetails) {
//        Integer sys_org_id = userDetails.getSys_org_id();
//        String key = RedisKeys.SYS_AREA + "sysCheck_sysOrgList" + sys_org_id;
//        List<SysOrg> list = null;
//        if (RedisUtil.isEmpty((key))) {
//            list = new ArrayList<>();
//            Integer sysOrgId = userDetails.getSys_org_id();//当前机构id
//            SysOrg sysOrg = sysOrgService.queryInstitutionsById(sysOrgId);
//            subDataGet.getAllOrgs(sysOrg, list);
//            RedisUtil.setData(key, list);
//        } else {
//            Type orgType = new TypeToken<List<SysOrg>>() {
//            }.getType();
//            list = RedisUtil.getListData(key, orgType);
//        }
//        return list;
//    }

    /**
     * 记录日志
     */
    private void log(Integer indexId, String oldValue, String newValue, Integer operateType, boolean result, HttpServletRequest request) {
        manageLogService.insertSysManageLogTb(new SysManageLog("校验管理", null, indexId,
                null, oldValue, newValue, null, operateType, 1, null,
                null, null, null, null, result), request);
    }
}
