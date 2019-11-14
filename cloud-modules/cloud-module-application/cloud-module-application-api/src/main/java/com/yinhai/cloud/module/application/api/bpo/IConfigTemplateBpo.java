package com.yinhai.cloud.module.application.api.bpo;

import com.yinhai.cloud.module.application.api.vo.ConfigTemplateVo;

/**
 * Created by liuyi02 on 2019/9/17.
 */
public interface IConfigTemplateBpo {

    /**
     * 更据中间件类型查询对应模板
     * @param templateType
     * @return ConfigTemplateVo
     */
    ConfigTemplateVo queryConfigTemplateByAppType(String templateType);
}
