package com.yinhai.cloud.module.application.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * @author: tianhy
 * @create: 2019-02-20
 */
public class ConfigMapPo extends BasePo {

    private static final long serialVersionUID = -904846640254637054L;
    private Long id;
    private Long appId;
    private String propertyName;
    private String propertyValue;
    private String isEnv;
    private String encrypt;

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

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getIsEnv() {
        return isEnv;
    }

    public void setIsEnv(String isEnv) {
        this.isEnv = isEnv;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }
}
