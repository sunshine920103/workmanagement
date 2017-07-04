package com.workmanagement.service;

import com.github.pagehelper.PageHelper;
import com.workmanagement.dao.DefaultIndexItemDao;
import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.DefaultIndexItemCombine;
import com.workmanagement.util.PageHelperSupport;
import com.workmanagement.util.PageSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DefaultIndexItemServiceImpl implements DefaultIndexItemService {
    @Autowired
    DefaultIndexItemDao defaultIndexItemDao;

    @Override
    public List<DefaultIndexItem> queryBycode(DefaultIndexItem defaultIndexItem) {
        // TODO Auto-generated method stub
        return defaultIndexItemDao.queryBycode(defaultIndexItem);
    }

    @Override
    public DefaultIndexItem queryById(Integer id) {
        return defaultIndexItemDao.queryById(id);
    }
    
    @Override
    public DefaultIndexItem getById(Integer id) {
        return defaultIndexItemDao.getById(id);
    }

    @Override
    public void updateDefaultIndexItem(DefaultIndexItem defaultIndexItem) {
        defaultIndexItemDao.updateDefaultIndexItem(defaultIndexItem);
    }

    @Override
    public void delectDefaultIndexItem(Integer id) {
        defaultIndexItemDao.delectDefaultIndexItem(id);
    }

    @Override
    public List<DefaultIndexItemCombine> queryByTime(Map<String, Object> map, PageSupport ps) {
        if (ps != null) {
            PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
            return PageHelperSupport.queryCount(defaultIndexItemDao.queryByTime(map), ps);
        } else {

            return defaultIndexItemDao.queryByTime(map);
        }
    }

    @Override
    public DefaultIndexItem queryByCodeAndCredit(String codeCredit, String codeOrg) {
        return defaultIndexItemDao.queryByCodeAndCredit(codeCredit, codeOrg);
    }

    @Override
    public List<DefaultIndexItem> getByCodeOrg(String codeOrg, Integer areaId) {
        return defaultIndexItemDao.getByCodeOrg(codeOrg, areaId);
    }

    @Override
    public List<DefaultIndexItem> getByCredit(String codeCredit, Integer areaId) {
        return defaultIndexItemDao.getByCredit(codeCredit, areaId);
    }

    /**
     * 根据	统一社会信用代码	或	组织机构代码	查询
     *
     * @param values
     * @return
     */
    @Override
    public List<DefaultIndexItem> getByCreditOrCode(String values, Integer areaId) {
        return defaultIndexItemDao.getByCreditOrCode(values, areaId);
    }

    @Override
    public void dinsert(DefaultIndexItem defaultIndexItem) {
        defaultIndexItemDao.dinsert(defaultIndexItem);
    }

    /**
     * 通过sql查询数据
     *
     * @param querySql
     * @return
     */
    @Override
    public List<DefaultIndexItem> queryDataBySql(String querySql) {
        return defaultIndexItemDao.queryDataBySql(querySql);
    }

    @Override
    public DefaultIndexItem queryByAll(Map<String, Object> map) {
        return defaultIndexItemDao.queryByAll(map);
    }

    @Override
    public DefaultIndexItem queryByComPanyShow(Map<String, Object> map) {
        return defaultIndexItemDao.queryByComPanyShow(map);
    }

    @Override
    public void insertDefaultIndexItemCombine(DefaultIndexItemCombine de) {
        defaultIndexItemDao.insertDefaultIndexItemCombine(de);
    }

    @Override
    public List<DefaultIndexItemCombine> queryDefaultIndexItemCombine(Integer orgId, PageSupport ps) {
        if (ps != null) {
            PageHelper.startPage(ps.getPageOffset() + 1, ps.getPageSize());
            return PageHelperSupport.queryCount(defaultIndexItemDao.queryDefaultIndexItemCombine(orgId), ps);
        } else {
            return defaultIndexItemDao.queryDefaultIndexItemCombine(orgId);
        }
    }

    @Override
    public DefaultIndexItemCombine queryDefaultIndexItemCombineTop() {
        return defaultIndexItemDao.queryDefaultIndexItemCombineTop();
    }

    @Override
    public void updateDefaultIndexItemCombine(DefaultIndexItemCombine de) {
        defaultIndexItemDao.updateDefaultIndexItemCombine(de);
    }

    @Override
    public List<DefaultIndexItem> queryByCridet(String codeCredit) {
        return defaultIndexItemDao.queryByCridet(codeCredit);
    }

    @Override
    public List<DefaultIndexItem> queryByOrg(String codeOrg) {
        return defaultIndexItemDao.queryByOrg(codeOrg);
    }

    @Override
    public String getOtherAreaDefaultIds(Integer defaultId) {
        //一个企业在不同地区相关联的二码id
        List<Integer> dids = new ArrayList<>();
        DefaultIndexItem defaultIndexItem = getById(defaultId);
        if (StringUtils.isNotBlank(defaultIndexItem.getCodeCredit())) {
            List<DefaultIndexItem> dList = queryByCridet(defaultIndexItem.getCodeCredit());
            if (CollectionUtils.isNotEmpty(dList)) {
                for (DefaultIndexItem de : dList) {
                    dids.add(de.getDefaultIndexItemId());
                }
            }
        }
        if (StringUtils.isNotBlank(defaultIndexItem.getCodeOrg())) {
            List<DefaultIndexItem> dList2 = queryByOrg(getById(defaultId).getCodeOrg());
            if (CollectionUtils.isNotEmpty(dList2)) {
                for (DefaultIndexItem de : dList2) {
                    dids.add(de.getDefaultIndexItemId());
                }
            }
        }
        StringBuilder defids = new StringBuilder("(");
        for (Integer did : dids) {
            defids.append(did).append(",");
        }
        defids.deleteCharAt(defids.lastIndexOf(",")).append(")");
        return defids.toString();
    }

	@Override
	public List<DefaultIndexItem> queryAllByName(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return defaultIndexItemDao.queryAllByName(map);
	}
}
