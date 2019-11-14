package com.yinhai.cloud.module.monitor.api.vo;

import javax.management.monitor.Monitor;

/**
 * @Author ：kangdw
 * @Date : 2019/8/8
 */
public class ProxyVo {
    /**
     * 代理的目标URL
     */
    private String monitorIP;

    private MonitorParamsVo monitorParamsVo;

    public String getMonitorIP() {
        return monitorIP;
    }

    public void setMonitorIP(String monitorIP) {
        this.monitorIP = monitorIP;
    }

    public MonitorParamsVo getMonitorParamsVo() {
        return monitorParamsVo;
    }

    public void setMonitorParamsVo(MonitorParamsVo monitorParamsVo) {
        this.monitorParamsVo = monitorParamsVo;
    }
}
