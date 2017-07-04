package com.workmanagement.service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.DicDao;
import com.workmanagement.model.Dic;
import com.workmanagement.model.DicContent;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据字典的service
 * ！业务逻辑都在这里处理
 * <p>
 * Created by lzm on 2017/3/8.
 */
@Service("dicService")
public class DicServiceImpl implements DicService {

    @Autowired
    private DicDao dicDao;
    @Autowired
    private SysAreaService sysAreaService;

    /**
     * 根据主键获取一个数据字典
     *
     * @param dicId 某个数据字典的id（主键）
     * @return 查询出来的单个数据字典
     * @throws Exception
     */
    @Override
    public Dic getDicByDicId(Integer dicId) {
        String key = SYS_DIC + "getDicByDicId" + dicId;
        Dic dic;
        if (RedisUtil.isEmpty(key)) {
            dic = dicDao.getDicByDicId(dicId);
            RedisUtil.setData(key, dic);
        } else {
            dic = RedisUtil.getObjData(key, Dic.class);
        }
        return dic;
    }

    /**
     * 获取所有数据字典
     *
     * @throws Exception
     */
    @Override
    public List<Dic> getAllDic(PageSupport ps) {
        if (ps == null) {
            return dicDao.getAllDic();
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(dicDao.getAllDic(), ps);
    }

    /**
     * 查询区域下所有的数据字典
     * 根据一个或者多个所属区域ID（地区表主键）
     *
     * @param sysAreaId 区域表的主键，可能有一个或者多个
     * @return 查询出来的数据字典集合
     * @throws Exception
     */
    @Override
    public List<Dic> getDicsBySysAreaId(PageSupport ps, List<Integer> sysAreaId) {
        if (ps == null) {//表示不分页
            return dicDao.getDicsBySysAreaId(sysAreaId);
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(dicDao.getDicsBySysAreaId(sysAreaId), ps);
    }

    @Override
    public List<Dic> getDicsBySysAreaIdHaveThree(PageSupport ps, List<Integer> sysAreaId) {
        if (ps == null) {//表示不分页
            return dicDao.getDicsBySysAreaIdHaveThree(sysAreaId);
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(dicDao.getDicsBySysAreaIdHaveThree(sysAreaId), ps);
    }

    /**
     * 根据名字查一个数据字典
     *
     * @param dicName 数据字典的名字
     * @return 单条数据字典
     */
    @Override
    public Dic getDicByDicName(String dicName, Integer areaId) {
        String key = SYS_DIC + "getDicByDicName" + dicName + areaId;
        Dic dic;
        if (RedisUtil.isEmpty(key)) {
            dic = dicDao.getDicByDicName(dicName, areaId);
            RedisUtil.setData(key, dic);
        } else {
            dic = RedisUtil.getObjData(key, Dic.class);
        }
        return dic;
    }

    /**
     * 根据名字判断是否输入有重名
     *
     * @return 判断表里此数据字典名字有无被使用有没有这个数据, 被使用返回true
     */
    @Override
    public boolean isDicNameBeUsedInDic(String dicName, Integer areaId) {
        return dicDao.isDicNameBeUsedInDic(dicName, areaId) > 0;
    }

    /**
     * 根据主键删除一个数据字典
     * 因为和字典详情表相关联的，还要关联字典详情表，判断子集能否删除，
     * 子集能删除才允许删除
     *
     * @param dicId 某个数据字典的id（主键）
     * @return 返回操作成功否
     * @throws Exception
     */
    @Override
    public boolean deleteByDicId(Integer dicId) {
        boolean b = dicDao.delByDicId(dicId) > 0;
        clear();
        return b;
    }

    /**
     * 新增一个数据字典
     *
     * @param dic 需要新增的数据字典model
     * @return 返回操作成功否，如果不成功返回false，成功则为true
     * @throws Exception
     */
    @Override
    public boolean insertOneDic(Dic dic) {
        boolean b = dicDao.insertOneDic(dic) > 0;
        clear();
        return b;
    }

    /**
     * 修改一个数据字典
     *
     * @param dic 一条数据字典的数据
     * @return 返回操作成功否，如果不成功返回false，成功则为true
     * @throws Exception
     */
    @Override
    public boolean updateOneDic(Dic dic) {
        boolean b = dicDao.updateOneDic(dic) > 0;
        clear();
        return b;
    }

    /**
     * 查询是否有被使用
     *
     * @param dicId
     * @return 此数据字典被指标项表是否使用
     * @throws Exception
     */
    @Override
    public boolean isThisBeUsed(Integer dicId) {
        Integer a = dicDao.usedInIndexItem(dicId);
        Integer b = dicDao.usedInSysIndustry(dicId);
        Integer c = dicDao.usedInSysGov(dicId);
        return a > 0 || b > 0 || c > 0;
    }

    /**
     * 获取字典下，名称的个数
     *
     * @return xiehao
     */
    @Override
    public Integer selectDicContentNames(DicContent dict) {
        return dicDao.selectDicContentNames(dict);
    }

    /**
     * 通过字典id查询本身及关联数据
     *
     * @return xiehao
     */
    @Override
    public Dic getDicByDicIdAndContent(int i) {
        String key = SYS_DIC + "getDicByDicIdAndContent" + i;
        Dic dic;
        if (RedisUtil.isEmpty(key)) {
            dic = dicDao.getDicByDicIdAndContent(i);
            RedisUtil.setData(key, dic);
        } else {
            dic = RedisUtil.getObjData(key, Dic.class);
        }
        return dic;
    }

    @Override
    public Dic getDicById(Integer id) {
        String key = SYS_DIC + "getDicById" + id;
        Dic dic;
        if (RedisUtil.isEmpty(key)) {
            dic = dicDao.getDicById(id);
            RedisUtil.setData(key, dic);
        } else {
            dic = RedisUtil.getObjData(key, Dic.class);
        }
        return dic;
    }

    @Override
    public List<Dic> getDicByLikeName(PageSupport ps, String dicName, List<Integer> sysAreaIds) {
        if (ps == null) {
            return dicDao.getDicByLikeName(dicName, sysAreaIds);
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(dicDao.getDicByLikeName(dicName, sysAreaIds), ps);
    }

    @Override
    public List<Dic> getAllDicNotTree(PageSupport ps) {
        if (ps == null) {
            return dicDao.getAllDicNotTree();
        }
        PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
        return PageHelperSupport.queryCount(dicDao.getAllDicNotTree(), ps);
    }

    public void reSetDIcName(List<Dic> dicList) {
        if (CollectionUtils.isNotEmpty(dicList)) {
            for (Dic dic : dicList) {
                String areaName = sysAreaService.getAreaNotSub(dic.getSysAreaId()).getSysAreaName();
                if (!StringUtils.equals(areaName, "四川省")) {
                    dic.setDicName(areaName + "-" + dic.getDicName());
                }
            }
        }
    }

    private void clear() {
        RedisUtil.delBatchData(SYS_DIC);
    }
}
