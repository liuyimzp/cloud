package com.yinhai.cloud.module.application.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * Created by tianhy on 2018/9/6.
 */
public class AppEnvPo extends BasePo {

    private static final long serialVersionUID = -5358573386172088375L;
    // 序列号
    private Long id;
    // 应用ID
    private Long appId;
    // 环境参数名称
    private String envKey;
    // 环境参数值
    private String envValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getEnvKey() {
        return envKey;
    }

    public void setEnvKey(String envKey) {
        this.envKey = envKey;
    }

    public String getEnvValue() {
        return envValue;
    }

    public void setEnvValue(String envValue) {
        this.envValue = envValue;
    }
}
