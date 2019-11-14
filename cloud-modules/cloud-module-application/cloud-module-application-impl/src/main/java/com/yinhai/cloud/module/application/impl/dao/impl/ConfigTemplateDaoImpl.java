package com.yinhai.cloud.module.application.impl.dao.impl;

import com.yinhai.cloud.module.application.impl.dao.ConfigTemplateDao;
import com.yinhai.cloud.module.application.impl.po.ConfigTemplatePo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

/**
 * Created by liuyi02 on 2019/9/17.
 */
public class ConfigTemplateDaoImpl extends HibernateDAO<ConfigTemplatePo> implements ConfigTemplateDao {

    @Override
    public ConfigTemplatePo queryConfigTemplateByAppType(String templateType) {
        String hql = "from ConfigTemplatePo where middlewareType = ?";
        return get(hql,templateType);
    }
}

