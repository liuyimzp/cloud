package com.yinhai.cloud.module.application.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * Created by liuyi02 on 2019/09/18
 * 中间件配置模板对象
 */
public class ConfigTemplatePo extends BasePo {

    private static final long serialVersionUID = -4128591277097276899L;
    private Long configTemplateId;

    private String middlewareType;//中间件对应类型1.Zookeeper 2.Redis 3.MySQL 4.ActiveMQ 5.Oracle 6.Nginx 7.Kafka 8.Elasticsearch

    private String configTemplateContent;//中间件配置文件模板内容

    public Long getConfigTemplateId() {
        return configTemplateId;
    }

    public void setConfigTemplateId(Long configTemplateId) {
        this.configTemplateId = configTemplateId;
    }

    public String getMiddlewareType() {
        return middlewareType;
    }

    public void setMiddlewareType(String middlewareType) {
        this.middlewareType = middlewareType;
    }

    public String getConfigTemplateContent() {
        return configTemplateContent;
    }

    public void setConfigTemplateContent(String configTemplateContent) {
        this.configTemplateContent = configTemplateContent;
    }
}