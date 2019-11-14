package com.yinhai.cloud.module.application.impl.dao;


import com.yinhai.cloud.module.application.impl.po.ConfigTemplatePo;

/**
 * Created by liuyi02 on 2019/9/17.
 */
public interface ConfigTemplateDao {
    ConfigTemplatePo queryConfigTemplateByAppType(String templateType);
}
