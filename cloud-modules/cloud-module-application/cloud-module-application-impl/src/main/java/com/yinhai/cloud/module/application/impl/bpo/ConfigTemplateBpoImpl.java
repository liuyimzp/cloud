package com.yinhai.cloud.module.application.impl.bpo;

import com.yinhai.cloud.module.application.api.bpo.IConfigTemplateBpo;
import com.yinhai.cloud.module.application.api.vo.ConfigTemplateVo;
import com.yinhai.cloud.module.application.impl.dao.ConfigTemplateDao;
import com.yinhai.cloud.module.application.impl.po.ConfigTemplatePo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;

import javax.annotation.Resource;

/**
 * Created by liuyi02 on 2019/9/17.
 */
public class ConfigTemplateBpoImpl extends TaBaseBpo implements IConfigTemplateBpo {

    @Resource
    private ConfigTemplateDao configTemplateDao;

    @Override
    public ConfigTemplateVo queryConfigTemplateByAppType(String templateType) {
        ConfigTemplatePo configTemplatePo = configTemplateDao.queryConfigTemplateByAppType(templateType);
        return ValidateUtil.isEmpty(configTemplatePo)?null:configTemplateDao.queryConfigTemplateByAppType(templateType).toVo(new ConfigTemplateVo());
    }
}
