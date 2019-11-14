package com.yinhai.cloud.module.application.impl.dao.impl;

import com.yinhai.cloud.module.application.impl.dao.AppConfigDao;
import com.yinhai.cloud.module.application.impl.po.AppConfigPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by tianhy on 2018/6/13.
 */
public class AppConfigDaoImpl extends HibernateDAO<AppConfigPo> implements AppConfigDao {
    @Override
    public AppConfigPo select(Long id) {
        return super.get(id);
    }

    @Override
    public AppConfigPo insert(AppConfigPo po) {
        super.save(po);
        return po;
    }

    @Override
    public void delete(AppConfigPo po) {
        super.delete(po);
    }

    @Override
    public void deleteByAppId(Long id) {
        super.delete("delete from " + super.getEntityClassName(AppConfigPo.class.getName()) + " where appId = ?", id);
    }

    @Override
    public List<AppConfigPo> getAppConfigsByImageId(Long appImageId) {
        return super.find(" from " + super.getEntityClassName(AppConfigPo.class.getName()) + " where appImageId = ?", appImageId);
    }

    @Override
    public void update(AppConfigPo po) {
        super.update(po);
    }
}
