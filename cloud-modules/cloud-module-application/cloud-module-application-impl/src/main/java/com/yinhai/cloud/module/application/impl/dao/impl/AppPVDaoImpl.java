package com.yinhai.cloud.module.application.impl.dao.impl;

import com.yinhai.cloud.module.application.impl.dao.AppPVDao;
import com.yinhai.cloud.module.application.impl.po.AppPVPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by tianhy on 2018/7/9.
 */
public class AppPVDaoImpl extends HibernateDAO<AppPVPo> implements AppPVDao {
    @Override
    public AppPVPo select(Long id) {
        return super.get(id);
    }

    @Override
    public AppPVPo insert(AppPVPo po) {
        super.save(po);
        return po;
    }

    @Override
    public void update(AppPVPo po) {
        super.update(po);
    }

    @Override
    public void delete(AppPVPo po) {
        super.delete(po);
    }

    @Override
    public List<AppPVPo> getPVsByAppId(Long appId) {
        return super.find(" from " + getEntityClassName(AppPVPo.class.getName()) + " where appId = ?", appId);
    }

    @Override
    public void deleteByAppId(Long appId) {
        super.delete("delete from " + getEntityClassName(AppPVPo.class.getName()) + " where appId = ?", appId);
    }

    @Override
    public List<AppPVPo> getPVsByVolumeId(Long volumeId) {
        return super.find(" from " + getEntityClassName(AppPVPo.class.getName()) + " where volumeId = ?", volumeId);
    }
}
