package com.yinhai.cloud.module.resource.cluster.impl.dao.impl;

import com.yinhai.cloud.module.resource.cluster.impl.dao.AppArrDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.AppArrVo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

public class AppArrDaoImpl extends HibernateDAO<AppArrVo> implements AppArrDao {
    @Override
    public List<AppArrVo> getAllApp() {
        return super.find("from " + super.getEntityClassName(AppArrVo.class.getName()) + " ORDER BY createtime DESC");
    }
}
