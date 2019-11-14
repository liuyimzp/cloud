package com.yinhai.cloud.module.application.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * Created by tianhy on 2019/2/20.
 */
public class ConfigMapVo extends BaseVo {

    private static final long serialVersionUID = -3580474262858434939L;

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
