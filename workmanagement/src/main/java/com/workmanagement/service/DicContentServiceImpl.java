package com.workmanagement.service;

import com.google.gson.reflect.TypeToken;
import com.workmanagement.dao.DicContentDao;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.IndexTb;
import com.workmanagement.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzm on 2017/3/9.
 */
@Service(value = "dicContentService")
public class DicContentServiceImpl implements DicContentService {

    @Autowired
    private DicContentDao dicContentDao;

    /**
     * 根据主键获取数据字典详细项
     *
     * @param dicContentId 字典详细页的ID（主键）
     * @return 单条字典详细页
     */
    @Override
    public DicContent getDicContentById(Integer dicContentId) {
        String key = DicService.SYS_DIC + "getDicContentById" + dicContentId;
        DicContent dic;
        if (RedisUtil.isEmpty(key)) {
            dic = dicContentDao.getDicContentById(dicContentId);
            RedisUtil.setData(key, dic);
        } else {
            dic = RedisUtil.getObjData(key, DicContent.class);
        }
        return dic;
    }

    /**
     * 根据主键获取数据字典详细项
     *
     * @param dicId 此表的外键（数据字典表的主键）
     * @return 多条字典详细页
     */
    @Override
    public List<DicContent> getDicContentsByDicId(Integer dicId) {
        String key = DicService.SYS_DIC + "getDicContentsByDicId" + dicId;
        List<DicContent> dicContentList;
        if (RedisUtil.isEmpty(key)) {
            dicContentList = dicContentDao.getDicContentsByDicId(dicId);
            RedisUtil.setData(key, dicContentList);
        } else {
            Type type = new TypeToken<List<DicContent>>() {
            }.getType();
            dicContentList = RedisUtil.getListData(key, type);
        }
        return dicContentList;
    }

    /**
     * 根据主键删除一条数据字典详细项
     *
     * @param dicContentId 字典详细页的ID（主键）
     * @return 操作成功条数
     */
    @Override
    public boolean delOneDicContentById(Integer dicContentId) {
        boolean b = dicContentDao.delOneDicContentById(dicContentId) > 0;
        clear();
        return b;
    }

    /**
     * 根据dic_id删除多条数据字典详细项
     *
     * @param dicId 数据字典表的ID（外键）
     * @return 操作成功条数
     */
    @Override
    public boolean delDicContentsByDicId(Integer dicId) {
        boolean b = dicContentDao.delDicContentsByDicId(dicId) > 0;
        clear();
        return b;
    }

    /**
     * 添加一条数据字典详细项
     *
     * @param dicContent 一条字典详细页的数据
     * @return 操作成功条数
     */
    @Override
    public boolean insertOneContent(DicContent dicContent) {
        boolean b = dicContentDao.insertOneContent(dicContent) > 0;
        clear();
        return b;
    }

    /**
     * 查询输入的字典代码在表内是否被使用
     * 用于判断用户新增数据是否重复
     *
     * @param dicId          数据字典的ID（此表外键）
     * @param dicContentCode 字典代码
     * @return 是否被使用了
     */
    @Override
    public boolean isDicContentCodeBeUsed(Integer dicId, String dicContentCode) {
        return dicContentDao.isDicContentCodeBeUsed(dicId, dicContentCode) > 0;
    }

    /**
     * 查询输入的指标值在表内是否被使用
     * 用于判断用户新增数据是否重复
     *
     * @param dicId           数据字典的ID（此表外键）
     * @param dicContentValue 指标值
     * @return 是否被使用了
     */
    @Override
    public boolean isDicContentValueBeUsed(Integer dicId, String dicContentValue) {
        return dicContentDao.isDicContentValueBeUsed(dicId, dicContentValue) > 0;
    }

    /**
     * 判断这个字典详情是否被使用
     *
     * @param dicContentId 字典详细页的ID（主键）
     * @return 是否被使用
     */
    @Override
    public boolean isThisBeUsed(Integer dicContentId) {
        int i = dicContentDao.isThisBeUsed(dicContentId);
        if (i > 0) return true;
        List<IndexTb> indexTbs = dicContentDao.getIndexCodeByDicCBeUsed(dicContentId);
        String dicContentCode = dicContentDao.getDicContentById(dicContentId).getDicContentCode();
        if (CollectionUtils.isNotEmpty(indexTbs)) {//如果有引用了这个的指标大类
            for (IndexTb it : indexTbs) {
                List<String> itemCode = dicContentDao.getIndexItemCode(dicContentId, it.getIndexId());
                if (CollectionUtils.isNotEmpty(itemCode)) {
                    StringBuilder sb = new StringBuilder("select count(0) from ");
                    sb.append(it.getIndexCode()).append("_tb").append(" where 1=1");
                    for (String s : itemCode) {
                        sb.append(" and ").append(s).append("='").append(dicContentCode).append("'");
                    }
                    Integer count = dicContentDao.getCount(sb.toString());
                    if (count != null && count > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 根据数据字典id和数据字典目录code查询
     *
     * @param dicId
     * @return
     */

    @Override
    public DicContent getDicContentByDicIDAndCode(String dicContentCode, Integer dicId) {
        String key = DicService.SYS_DIC + "getDicContentByDicIDAndCode" + dicContentCode + dicId;
        DicContent dic;
        if (RedisUtil.isEmpty(key)) {
            dic = dicContentDao.getDicContentByDicIDAndCode(dicContentCode, dicId);
            RedisUtil.setData(key, dic);
        } else {
            dic = RedisUtil.getObjData(key, DicContent.class);
        }
        return dic;
    }

    @Override
    public void insertOrUpdate(DicContent dicContent) {
        if (dicContent.getDicContentId() == null) {
            dicContentDao.insert(dicContent);
            clear();
        } else {
            dicContentDao.update(dicContent);
            clear();
        }
    }

    @Override
    public List<DicContent> getDicByDicIdAndContent(int i) {
        return dicContentDao.getDicByDicIdAndContent(i);
    }

    /**
     * 查询字典指标值是否存在
     *
     * @param dicContentValue
     * @return
     * @throws Exception
     */
    @Override
    public boolean isContentValueHaved(String dicContentValue) {
        return dicContentDao.isContentValueHaved(dicContentValue) > 0;
    }

    /**
     * 根据指标值获取数据字典ID
     *
     * @param dicContentValue
     * @return
     * @throws Exception
     */
    @Override
    public DicContent getDicIdByDicContentValue(String dicContentValue) {
        String key = DicService.SYS_DIC + "getDicIdByDicContentValue" + dicContentValue;
        DicContent dic;
        if (RedisUtil.isEmpty(key)) {
            dic = dicContentDao.getDicIdByDicContentValue(dicContentValue);
            RedisUtil.setData(key, dic);
        } else {
            dic = RedisUtil.getObjData(key, DicContent.class);
        }
        return dic;
    }

    @Override
    public List<DicContent> queryAllContent() {
        return dicContentDao.queryAllContent();
    }

    @Override
    public DicContent getDicIdByDicContentValueAndDicId(String dicContentValue, Integer dicId) {
        String key = DicService.SYS_DIC + "getDicIdByDicContentValueAndDicId" + dicContentValue + dicId;
        DicContent dic;
        if (RedisUtil.isEmpty(key)) {
            dic = dicContentDao.getDicIdByDicContentValueAndDicId(dicContentValue, dicId);
            RedisUtil.setData(key, dic);
        } else {
            dic = RedisUtil.getObjData(key, DicContent.class);
        }
        return dic;
    }

    @Override
    public DicContent queryContentByDicIdAndValues(Integer dicId, String values) {
        String key = DicService.SYS_DIC + "queryContentByDicIdAndValues" + values + dicId;
        DicContent dic;
        if (RedisUtil.isEmpty(key)) {
            Map<String, Object> map = new HashMap<>();
            map.put("dicId", dicId);
            map.put("values", values);
            dic = dicContentDao.queryContentByDicIdAndValues(map);
            RedisUtil.setData(key, dic);
        } else {
            dic = RedisUtil.getObjData(key, DicContent.class);
        }
        return dic;
    }

    @Override
    public boolean updDicContent(Integer dicId, String values) {
        DicContent dicContent = new DicContent();
        dicContent.setDicContentValue(values);
        dicContent.setDicContentId(dicId);
        boolean b = dicContentDao.updateOneContent(dicContent) > 0;
        clear();
        return b;
    }

    private void clear() {
        RedisUtil.delBatchData(DicService.SYS_DIC);
    }
}
