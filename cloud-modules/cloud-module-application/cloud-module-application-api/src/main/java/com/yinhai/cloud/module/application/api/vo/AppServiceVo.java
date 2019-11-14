package com.yinhai.cloud.module.application.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * Created by tianhy on 2018/6/14.
 */
public class AppServiceVo extends BaseVo {
    private static final long serialVersionUID = 7788667024252640257L;

    // 序列号
    private Long id;
    // 应用ID
    private Long appId;
    // 服务类型
    private String serviceType;
    // 内部端口
    private Integer innerPort;
    // 映射端口
    private Integer mappingPort;
    // 协议类型
    private String protocolType;

    public AppServiceVo() {
    }

    public AppServiceVo(Long id, Long appId, String serviceType, Integer innerPort, Integer mappingPort, String protocolType) {
        this.id = id;
        this.appId = appId;
        this.serviceType = serviceType;
        this.innerPort = innerPort;
        this.mappingPort = mappingPort;
        this.protocolType = protocolType;
    }

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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getInnerPort() {
        return innerPort;
    }

    public void setInnerPort(Integer innerPort) {
        this.innerPort = innerPort;
    }

    public Integer getMappingPort() {
        return mappingPort;
    }

    public void setMappingPort(Integer mappingPort) {
        this.mappingPort = mappingPort;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }
}
