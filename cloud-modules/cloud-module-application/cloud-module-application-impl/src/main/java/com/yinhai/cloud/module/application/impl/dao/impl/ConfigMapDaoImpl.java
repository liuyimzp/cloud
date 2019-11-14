package com.yinhai.cloud.module.application.impl.dao.impl;

import com.yinhai.cloud.module.application.impl.dao.ConfigMapDao;
import com.yinhai.cloud.module.application.impl.po.ConfigMapPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * @author: tianhy
 * @create: 2018-02-20
 */
public class ConfigMapDaoImpl extends HibernateDAO<ConfigMapPo> implements ConfigMapDao {

    @Override
    public ConfigMapPo select(Long id) {
        return super.get(id);
    }

    @Override
    public ConfigMapPo insert(ConfigMapPo po) {
        super.save(po);
        return po;
    }

    @Override
    public void delete(ConfigMapPo o) {
        super.delete(o);
    }

    @Override
    public void update(ConfigMapPo o) {
        super.update(o);
    }

    @Override
    public List<ConfigMapPo> getByAppId(Long appId) {
        return super.find(" from " + getEntityClassName(ConfigMapPo.class.getName()) + " where appId = ?", appId);
    }

    @Override
    public void deleteByAppId(Long appId) {
        super.delete(" delete from " + getEntityClassName(ConfigMapPo.class.getName()) + " where appId = ?", appId);
    }
}
