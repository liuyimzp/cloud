package com.yinhai.cloud.module.application.impl.dao.impl;

import com.yinhai.cloud.module.application.impl.dao.AppServiceDao;
import com.yinhai.cloud.module.application.impl.po.AppPo;
import com.yinhai.cloud.module.application.impl.po.AppServicePo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by tianhy on 2018/6/14.
 */
public class AppServiceDaoImpl extends HibernateDAO<AppServicePo> implements AppServiceDao {
    @Override
    public AppServicePo select(Long id) {
        return super.get(id);
    }

    @Override
    public AppServicePo insert(AppServicePo po) {
        super.save(po);
        return po;
    }

    @Override
    public void delete(AppServicePo o) {
        super.delete(o);
    }

    @Override
    public void update(AppServicePo o) {
        super.update(o);
    }

    @Override
    public List<AppServicePo> getAppServicesByAppId(Long appId) {
        return super.find("from " + super.getEntityClassName(AppServicePo.class.getName()) + " where appId = ?", appId);
    }

    @Override
    public void deleteByAppId(Long appId) {
        super.delete("delete from " + super.getEntityClassName(AppServicePo.class.getName()) + " where appId = ?", appId);
    }

    @Override
    public List<AppServicePo> getAppServicesByClusterIdAndMappingPort(Long clusterId, Integer mappingPort) {
        return super.selectFromMultiTable("select b from " + getEntityClassName(AppPo.class.getName()) + " a," + getEntityClassName(AppServicePo.class.getName()) + " b where a.id = b.appId and a.clusterId = ? and b.mappingPort = ?", clusterId, mappingPort);
    }

    @Override
    public List<AppServicePo> getAppServicesByAppIdAndInnerPort(Long appId, Integer innerPort) {
        return super.find("from " + getEntityClassName(AppServicePo.class.getName()) + " where appId = ? and innerPort = ?", appId, innerPort);
    }
}
