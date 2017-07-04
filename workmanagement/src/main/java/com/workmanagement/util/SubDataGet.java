package com.workmanagement.util;

import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOrgType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取机构和地区的工具类
 * Created by wonde on 2017/3/22.
 */
public final class SubDataGet {

    public static void main(String[] args) {
        String[] strings = splitFormula("(贴现率（%）+对外投资金额（元）)!=(5000.5454)");
        System.out.println(Arrays.toString(strings));

        System.out.println(checkIdCard("512403199305152614"));
    }

    /**
     * 分解公式，按照名字，符号，数字分
     *
     * @param formula
     * @return
     */
    public static String[] splitFormula(final String formula) {
        String a = "";
        List<String> list = new ArrayList<>();
        for (int x = 0; x < formula.length(); x++) {
            String c = String.valueOf(formula.charAt(x));
            if (StringUtils.equals(c, ")") || StringUtils.equals(c, "(")) {
                if (a.length() > 1) {
                    list.add(a);
                    a = "";
                }
                list.add(String.valueOf(c));
                continue;
            }
            a += c;
            if (isCode(String.valueOf(c))) {//为true表示是符号
                if (a.length() > 2) {
                    list.add(StringUtils.left(a, a.length() - 1));//先把算数符号前面的加进数组去
                }
                if (x != formula.length() - 1) {
                    if (isCode(c + String.valueOf(formula.charAt(x + 1)))) {//如果连接下一位后也是算法
                        list.add(String.valueOf(c + String.valueOf(formula.charAt(x + 1))));
                        ++x;
                    } else {
                        list.add(StringUtils.right(a, 1));//单独算数
                    }
                } else {
                    list.add(StringUtils.right(a, 1));//单独算数
                }
                a = "";
                continue;
            }
            if (x == formula.length() - 1 && StringUtils.isNotBlank(a)) {
                list.add(a);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] split(String formula, String search) {
        String replace = StringUtils.replace(formula, search, "|");
        return StringUtils.split(replace, "|");
    }

    public static boolean isHaveYiQi(final String formula) {
        String first = formula;
        int and = StringUtils.indexOf(first, "and");
        while (and != -1) {
            //如果公式里有 and
            first = StringUtils.substring(first, and + 3);
            and = first.indexOf("and");
            if (and == 0) {
                return false;
            }
        }
        first = formula;
        int or = StringUtils.indexOf(first, "or");
        while (or != -1) {
            //如果公式里有 or
            first = StringUtils.substring(first, or + 2);
            or = first.indexOf("or");
            if (or == 0) {
                return false;
            }
        }
        first = formula;
        int orand = StringUtils.indexOf(first, "or");
        while (orand != -1) {
            //如果公式里有 and
            first = StringUtils.substring(first, orand + 2);
            orand = first.indexOf("and");
            if (orand == 0) {
                return false;
            }
        }
        first = formula;
        int andor = StringUtils.indexOf(first, "and");
        while (andor != -1) {
            //如果公式里有 and
            first = StringUtils.substring(first, andor + 3);
            andor = first.indexOf("or");
            if (andor == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 属于表达式为真
     *
     * @param value
     * @return
     */
    public static boolean isMath(String value) {
        boolean a = false;
        switch (value) {
            case ">=": {
                a = true;
                break;
            }
            case "=": {
                a = true;
                break;
            }
            case "<=": {
                a = true;
                break;
            }
            case ">": {
                a = true;
                break;
            }
            case "<": {
                a = true;
                break;
            }
            case "!=": {
                a = true;
                break;
            }
            case "!": {
                a = true;
                break;
            }
        }
        return a;
    }

    /**
     * 属于表达式为真
     *
     * @param value
     * @return
     */
    public static boolean isCode(String value) {
        boolean a = false;
        switch (value) {
            case ">=": {
                a = true;
                break;
            }
            case "=": {
                a = true;
                break;
            }
            case "<=": {
                a = true;
                break;
            }
            case ">": {
                a = true;
                break;
            }
            case "<": {
                a = true;
                break;
            }
            case "!=": {
                a = true;
                break;
            }
            case "!": {
                a = true;
                break;
            }
            case "+": {
                a = true;
                break;
            }
            case "-": {
                a = true;
                break;
            }
            case "*": {
                a = true;
                break;
            }
            case "/": {
                a = true;
                break;
            }
        }
        return a;
    }

    /**
     * 只分解公式以比较符号作为中点,0-左边,1-右边,2-符号
     *
     * @param formula
     * @return
     */
    public static List<String> replaceToList(String formula) {
        List<String> list = new ArrayList<>();
//        int x = 0;
//        int len = 0;
//        for (int i = 0; i < formula.length(); i++) {
//            String s = String.valueOf(formula.charAt(i));
//            if (isMath(s)) {
//                x = i;
//                if (isMath(String.valueOf(formula.charAt(i + 1)))) {
//                    len = 2;
//                }
//            }
//        }
//        if (len != 0) {
//            list.add(StringUtils.left(formula, x - 1));//左边的
//            list.add(StringUtils.substring(formula, x + 1));//右边的
//            list.add(StringUtils.substring(formula, x - 1, x + 1));//符号
//        } else {
//            list.add(StringUtils.left(formula, x));//左边的
//            list.add(StringUtils.substring(formula, x + 1));//右边的
//            list.add(StringUtils.substring(formula, x, x + 1));//符号
//        }
        String[] split = StringUtils.split(formula, ",");
        for (int x = 0; x < split.length; x++) {
            if (isMath(split[x])) {
                //如果遇到了比较符号
                String[] left = Arrays.copyOfRange(split, 0, x);
                String lefts = Arrays.toString(left);
                String leftSub = StringUtils.substring(lefts, 1, lefts.length() - 1);
                list.add(StringUtils.replace(leftSub, " ", ""));//左边
                String[] right = Arrays.copyOfRange(split, x + 1, split.length);
                String rights = Arrays.toString(right);
                String rightSub = StringUtils.substring(rights, 1, rights.length() - 1);
                list.add(StringUtils.replace(rightSub, " ", ""));//右边
                list.add(split[x]);//符号
                break;
            }
        }
        return list;
    }

    /**
     * 属于表达式为真
     *
     * @param value
     * @return
     */
    public static boolean isCate(String value) {
        boolean a = false;
        switch (value) {
            case "+": {
                a = true;
                break;
            }
            case "-": {
                a = true;
                break;
            }
            case "*": {
                a = true;
                break;
            }
            case "/": {
                a = true;
                break;
            }
        }
        return a;
    }

    public static boolean isDontCare(String value) {
        boolean a = false;
        switch (value) {
            case "+": {
                a = true;
                break;
            }
            case "-": {
                a = true;
                break;
            }
            case "*": {
                a = true;
                break;
            }
            case "/": {
                a = true;
                break;
            }
            case "(": {
                a = true;
                break;
            }
            case ")": {
                a = true;
                break;
            }
        }
        return a;
    }

    public static boolean checkIdCard(String idCard) {
        String ai = "";
        if (idCard.length() != 18 && idCard.length() != 15) {
            return false;
        }
        if (idCard.length() == 18) {
            //前17位必须为数字
            ai = idCard.substring(0, 17);
        } else {
            //全部为数字
            ai = idCard;
        }
        if (!StringUtils.isNumeric(ai)) {
            //如果不是数字
            return false;
        }
        // 判断出生年月是否有效
        String strYear = ai.substring(6, 10);// 年份
        String strMonth = ai.substring(10, 12);// 月份
        String strDay = ai.substring(12, 14);// 日期
        if (!isDate(strYear + "-" + strMonth + "-" + strDay)) {
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                return false;
            }
        } catch (NumberFormatException | ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            return false;
        }
        if (idCard.length() == 18) {
            if (!isVarifyCode(ai, idCard)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 功能：判断字符串出生日期是否符合正则表达式：包括年月日，闰年、平年和每月31天、30天和闰月的28天或者29天
     *
     * @param strDate
     * @return
     */
    private static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?" +
                        "((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|" +
                        "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|" +
                        "(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|" +
                        "([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|" +
                        "([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|" +
                        "(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }

    /**
     * 判断第18位校验码是否正确
     * 第18位校验码的计算方式：
     * 　　1. 对前17位数字本体码加权求和
     * 　　公式为：S = Sum(Ai * Wi), i = 0, ... , 16
     * 　　其中Ai表示第i个位置上的身份证号码数字值，Wi表示第i位置上的加权因子，其各位对应的值依次为： 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 　　2. 用11对计算结果取模
     * 　　Y = mod(S, 11)
     * 　　3. 根据模的值得到对应的校验码
     * 　　对应关系为：
     * 　　 Y值：     0  1  2  3  4  5  6  7  8  9  10
     * 　　校验码： 1  0  X  9  8  7  6  5  4  3   2
     */
    private static boolean isVarifyCode(String ai, String idStr) {
        String[] VarifyCode = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = sum % 11;
        String strVerifyCode = VarifyCode[modValue];
        ai = ai + strVerifyCode;
        if (!StringUtils.equals(ai, idStr)) {
            return false;
        }
        return true;
    }

    /**
     * 输入本地区获取级联的地区的ids（包括自己）
     *
     * @param sysArea
     * @param areaIds
     */
    public synchronized void getAllAreaIds(final SysArea sysArea, final List<Integer> areaIds) {
        if (sysArea != null) {
            areaIds.add(sysArea.getSysAreaId());
            List<SysArea> areas = sysArea.getSubArea();
            if (CollectionUtils.isNotEmpty(areas)) {
                for (SysArea s : areas) {
                    getAllAreaIds(s, areaIds);
                }
            }
        }
    }

    /**
     * 输入本机构，获取级联的机构的ids（包括自己）
     *
     * @param sysOrg
     * @param sysOrgList
     */
    public synchronized void getAllOrgIds(final SysOrg sysOrg, final List<Integer> sysOrgList) {
        //先获取所有的机构
        if (sysOrg != null) {
            sysOrgList.add(sysOrg.getSys_org_id());
            List<SysOrg> list = sysOrg.getSubSysOrg();
            if (CollectionUtils.isNotEmpty(list)) {
                for (SysOrg s : list) {
                    getAllOrgIds(s, sysOrgList);
                }
            }
        }
    }

    /**
     * 输入本机构，获取级联的机构的ids（包括自己）
     *
     * @param sysOrgType
     * @param sysOrgList
     */
    public synchronized void getAllOrgTypeIds(final SysOrgType sysOrgType, final List<Integer> sysOrgList) {
        //先获取所有的机构
        if (sysOrgType != null) {
            sysOrgList.add(sysOrgType.getSys_org_type_id());
            List<SysOrgType> list = sysOrgType.getSubSysOrgType();
            if (CollectionUtils.isNotEmpty(list)) {
                for (SysOrgType s : list) {
                    getAllOrgTypeIds(s, sysOrgList);
                }
            }
        }
    }

    /**
     * 输入本机构，获取级联的机构（包括自己）
     *
     * @param sysOrg
     * @param sysOrgList
     */
    public synchronized void getAllOrgs(final SysOrg sysOrg, final List<SysOrg> sysOrgList) {
        //先获取所有的机构
        if (sysOrg != null) {
            sysOrgList.add(sysOrg);
            List<SysOrg> list = sysOrg.getSubSysOrg();
            if (CollectionUtils.isNotEmpty(list)) {
                for (SysOrg s : list) {
                    getAllOrgs(s, sysOrgList);
                }
            }
        }
    }

    /**
     * 输入本地区，获取所有地区ids，以string格式（包括自己）
     *
     * @param sysArea
     * @param sysAreas
     */
    public synchronized void getStringAllAreaIds(final SysArea sysArea, final List<String> sysAreas) {
        if (sysArea != null) {
            sysAreas.add(String.valueOf(sysArea.getSysAreaId()));
            List<SysArea> areas = sysArea.getSubArea();
            if (CollectionUtils.isNotEmpty(areas)) {
                for (SysArea s : areas) {
                    getStringAllAreaIds(s, sysAreas);
                }
            }
        }
    }

    /**
     * Double 转string 去除科学记数法显示
     *
     * @param d
     * @return
     */
    public String doubleToStr(Double d) {
        if (d == null) {
            return "";
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return (nf.format(d));
    }
}
