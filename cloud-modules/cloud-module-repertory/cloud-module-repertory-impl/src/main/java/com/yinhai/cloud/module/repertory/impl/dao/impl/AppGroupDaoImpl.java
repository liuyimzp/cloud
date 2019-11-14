package com.yinhai.cloud.module.repertory.impl.dao.impl;

import com.yinhai.cloud.module.repertory.impl.dao.AppGroupDao;
import com.yinhai.cloud.module.repertory.impl.po.AppGroupPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by tianhy on 2018/7/30.
 */
public class AppGroupDaoImpl extends HibernateDAO<AppGroupPo> implements AppGroupDao {
    @Override
    public AppGroupPo select(Long id) {
        return super.get(id);
    }

    @Override
    public AppGroupPo insert(AppGroupPo po) {
        super.save(po);
        return po;
    }

    @Override
    public void update(AppGroupPo po) {
        super.update(po);
    }

    @Override
    public void delete(AppGroupPo po) {
        super.delete(po);
    }

    @Override
    public List<AppGroupPo> getAppGroupsByIdentify(String groupIdentify) {
        return super.find(" from " + super.getEntityClassName(AppGroupPo.class.getName()) + " where groupIdentify = ?", groupIdentify);
    }

    @Override
    public List<AppGroupPo> getAppGroupsByBusinessArea(String businessArea) {
        return super.find(" from " + super.getEntityClassName(AppGroupPo.class.getName()) + " where businessArea = ?", businessArea);
    }

    @Override
    public List<AppGroupPo> getAllAppGroups() {
        return super.find(" from " + super.getEntityClassName(AppGroupPo.class.getName()));
    }
}
