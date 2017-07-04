package com.workmanagement.service;

import com.workmanagement.dao.SysCheckDao;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.SysCheck;
import com.workmanagement.util.Calculator;
import com.workmanagement.util.SubDataGet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzm on 2017/6/10.
 */
public final class SysCheckCache {
    private SysAreaService sysAreaService;
    private List<SysCheck> list;
    private List<IndexItemTb> items;

    public SysCheckCache(SysCheckDao sysCheckDao, SysAreaService sysAreaService, List<IndexItemTb> items,
                         Integer sysAreaId, Integer indexId, Date thisDate) {
        this.sysAreaService = sysAreaService;
        this.items = items;
        List<Integer> allUpAreaIds = sysAreaService.getAllUpAreaIds(sysAreaId);
        list = sysCheckDao.getSysCheckByIndexIdAndAreaIdsAndTime(indexId, allUpAreaIds, thisDate);
    }

    public boolean getCheckData(List<Object> cellData) throws Exception {
        if (items.size() != cellData.size()) {//表示前面的校验出错了
            return false;
        }
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
        return doubles;
    }
}
