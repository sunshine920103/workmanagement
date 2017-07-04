package com.workmanagement.service;

import com.workmanagement.dao.IndexItemTbDao;
import com.workmanagement.model.IndexItemTb;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexItemTbServiceImpl implements IndexItemTbService {
    @Autowired
    private IndexItemTbDao indexItemTbDao;

    @Override
    public IndexItemTb queryIndexItemTbById(@Param("indexItemId") Integer indexItemId) {
        return indexItemTbDao.queryIndexItemTbById(indexItemId);
    }

    @Override
    public void updateIndexItemTb(IndexItemTb indexItemTb) {
        indexItemTbDao.updateIndexItemTb(indexItemTb);
    }

    @Override
    public void insertIndexItemTb(IndexItemTb indexItemTb) {
        indexItemTbDao.insertIndexItemTb(indexItemTb);
    }

    @Override
    public List<IndexItemTb> queryIndexItemTbsByIndexId(@Param("indexId") Integer indexId) {
        return indexItemTbDao.queryIndexItemTbsByIndexId(indexId);
    }

    @Override
    public IndexItemTb getIndexItemTbByCode(String indexItemCode) {
        return indexItemTbDao.getIndexItemTbByCode(indexItemCode);
    }

    @Override
    public List<Integer> getDicIdsInIndexItem() {
        return indexItemTbDao.getDicIdsInIndexItem();

    }

    @Override
    public List<String> selectAllIndexItemNameByIndexId(Integer indexId) {
        return indexItemTbDao.selectAllIndexItemNameByIndexId(indexId);
    }

    @Override
    public List<String> selectAllIndexItemCodeByIndexId(Integer indexId) {
        return indexItemTbDao.selectAllIndexItemCodeByIndexId(indexId);
    }

    @Override
    public List<IndexItemTb> selectAllIndexItemByIndexItemType(Integer indexItemType) {
        return indexItemTbDao.selectAllIndexItemByIndexItemType(indexItemType);
    }

    @Override
    public List<String> getIndexIdsInIndexItem(Integer pdicId) {
        return indexItemTbDao.getIndexIdsInIndexItem(pdicId);
    }

    @Override
    public List<String> getUsedNames(Map<String, Object> sqlmap) {
        return indexItemTbDao.getUsedNames(sqlmap);
    }


	@Override
	public void deleteIndexItemTb(Integer indexId) {
		// TODO Auto-generated method stub
		indexItemTbDao.deleteIndexItemTb(indexId);
	}

    @Override
    public List<IndexItemTb> queryIndexItemTbsByIndexIdWithStatus(Integer IndexId) {
        return indexItemTbDao.queryIndexItemTbsByIndexIdWithStatus(IndexId);
    }

    @Override
    public void insertColumn(String indexNameCode, String indexItemCode, Integer indexItemType, Integer varLength, Integer indexItemEmpty) {
        // 在表中插入这个指标项对应的字段
        Map<Object, Object> map = new HashMap<>();
        map.put("tbName", indexNameCode + "_tb");// 表名
        map.put("columnName", indexItemCode);// 字段名
        if (indexItemType == 0) {
            map.put("type", "varchar(" + varLength + ")"); // 字符
            if (indexItemEmpty == 0) {// 不可为空
                //map.put("isEmpty", " not null with default 'null' ");
                map.put("isEmpty", " ");
            }
        } else if (indexItemType == 1) {
            map.put("type", "timestamp"); // 时间
            if (indexItemEmpty == 0) {// 不可为空
                //map.put("isEmpty", " not null with default '1700-01-01' ");
            	map.put("isEmpty", " ");
            }
        } else if (indexItemType == 2) {
            map.put("type", "double"); // 数值
            if (indexItemEmpty == 0) {// 不可为空
                //map.put("isEmpty", " not null with default 0 ");
            	map.put("isEmpty", " ");
            }
        } else {
            map.put("type", "varchar(100)"); // 字典
            if (indexItemEmpty == 0) {// 不可为空
                //map.put("isEmpty", " not null with default 'null' ");
            	map.put("isEmpty", " ");
            }
        }
        indexItemTbDao.insertColumn(map);
    }

    @Override
    public void addColumnBySql(Map<String, String> sqlmap) {
        indexItemTbDao.addColumnBySql(sqlmap);
    }

    /**
     * 根据指标大类id和指标项类型获取所有指标项
     *
     * @param indexId
     * @param indexItemType
     * @return
     */
    public List<IndexItemTb> getIndexItemsByIdAndType(Integer indexId, Integer indexItemType, List<Integer> sysAreaIds) {
        return indexItemTbDao.getIndexItemsByIdAndType(indexId, indexItemType, sysAreaIds);
    }

    @Override
    public IndexItemTb getIndexItemsByIndexIdAndName(Integer indexId, String indexItemName) {
        return indexItemTbDao.getIndexItemsByIndexIdAndName(indexId, indexItemName);
    }

    @Override
    public List<IndexItemTb> queryItemsByAreaIds(Integer indexId, List<Integer> aeraIds) {
        return indexItemTbDao.queryItemsByAreaIds(indexId, aeraIds);
    }

    /**
     * 获取指标下启用的指标项
     *
     * @param indexId
     * @param sysAreaIds
     */
    @Override
    public List<IndexItemTb> getIndexIntemsIsUsedByIdAndAreaIds(Integer indexId, List<Integer> sysAreaIds) {
        return indexItemTbDao.getIndexIntemsIsUsedByIdAndAreaIds(indexId, sysAreaIds);
    }

    @Override
    public List<IndexItemTb> getIndexIntemsIsUsedByIdAndAreaIdsAndCanNull(Integer indexId, List<Integer> sysAreaIds) {
        return indexItemTbDao.getIndexIntemsIsUsedByIdAndAreaIdsAndCanNull(indexId, sysAreaIds);
    }


}
