package com.yinhai.cloud.module.application.impl.dao.impl;

import com.yinhai.cloud.module.application.impl.dao.AppEnvDao;
import com.yinhai.cloud.module.application.impl.po.AppEnvPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by tianhy on 2018/9/6.
 */
public class AppEnvDaoImpl extends HibernateDAO<AppEnvPo> implements AppEnvDao {
    @Override
    public AppEnvPo select(Long id) {
        return super.get(id);
    }

    @Override
    public AppEnvPo insert(AppEnvPo po) {
        super.save(po);
        return po;
    }


    @Override
    public void delete(AppEnvPo o) {
        super.delete(o);
    }

    @Override
    public void update(AppEnvPo o) {
        super.update(o);
    }

    @Override
    public List<AppEnvPo> getAppEnvsByAppId(Long appId) {
        return super.find(" from " + super.getEntityClassName(AppEnvPo.class.getName()) + " where appId = ?", appId);
    }

    @Override
    public List<AppEnvPo> getAppEnvsByAppIdAndEnvKey(Long appId, String envKey) {
        return super.find(" from " + super.getEntityClassName(AppEnvPo.class.getName()) + " where appId = ? and envKey = ?", appId, envKey);
    }
}
