package com.yinhai.cloud.module.application.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * Created by tianhy on 2018/6/13.
 * 应用配置数据库对象
 */
public class AppConfigPo extends BasePo {
    private static final long serialVersionUID = 5680251189734990170L;
    // 应用ID
    private Long appId;
    // 应用镜像ID
    private Long appImageId;
    // 最小CPU
    private Double minCPU;
    // 最大CPU
    private Double maxCPU;
    // 最小内存
    private Double minMemory;
    // 最大内存
    private Double maxMemory;
    // 启动命令
    private String startCommand;
    // 启动后命令
    private String runCommand;
    // 停止前命令
    private String stopCommand;
    // 应用检测时间
    private Integer appCheckTime;
    // 应用检测超时时间
    private Integer appCheckOvertime;
    // 应用检测方式
    private String appCheckType;
    // 应用检测命令
    private String appCheckCommand;
    // 业务检测时间
    private Integer bizCheckTime;
    // 业务检测超时时间
    private Integer bizCheckOvertime;
    // 业务检测方式
    private String bizCheckType;
    // 业务检测命令
    private String bizCheckCommand;

    public AppConfigPo() {
    }

    public AppConfigPo(Long appId, Long appImageId, Double minCPU, Double maxCPU, Double minMemory, Double maxMemory, String startCommand, String runCommand, String stopCommand, Integer appCheckTime, Integer appCheckOvertime, String appCheckType, String appCheckCommand, Integer bizCheckTime, Integer bizCheckOvertime, String bizCheckType, String bizCheckCommand) {
        this.appId = appId;
        this.appImageId = appImageId;
        this.minCPU = minCPU;
        this.maxCPU = maxCPU;
        this.minMemory = minMemory;
        this.maxMemory = maxMemory;
        this.startCommand = startCommand;
        this.runCommand = runCommand;
        this.stopCommand = stopCommand;
        this.appCheckTime = appCheckTime;
        this.appCheckOvertime = appCheckOvertime;
        this.appCheckType = appCheckType;
        this.appCheckCommand = appCheckCommand;
        this.bizCheckTime = bizCheckTime;
        this.bizCheckOvertime = bizCheckOvertime;
        this.bizCheckType = bizCheckType;
        this.bizCheckCommand = bizCheckCommand;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getAppImageId() {
        return appImageId;
    }

    public void setAppImageId(Long appImageId) {
        this.appImageId = appImageId;
    }

    public Double getMinCPU() {
        return minCPU;
    }

    public void setMinCPU(Double minCPU) {
        this.minCPU = minCPU;
    }

    public Double getMaxCPU() {
        return maxCPU;
    }

    public void setMaxCPU(Double maxCPU) {
        this.maxCPU = maxCPU;
    }

    public Double getMinMemory() {
        return minMemory;
    }

    public void setMinMemory(Double minMemory) {
        this.minMemory = minMemory;
    }

    public Double getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(Double maxMemory) {
        this.maxMemory = maxMemory;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public void setStartCommand(String startCommand) {
        this.startCommand = startCommand;
    }

    public String getRunCommand() {
        return runCommand;
    }

    public void setRunCommand(String runCommand) {
        this.runCommand = runCommand;
    }

    public String getStopCommand() {
        return stopCommand;
    }

    public void setStopCommand(String stopCommand) {
        this.stopCommand = stopCommand;
    }

    public Integer getAppCheckTime() {
        return appCheckTime;
    }

    public void setAppCheckTime(Integer appCheckTime) {
        this.appCheckTime = appCheckTime;
    }

    public Integer getAppCheckOvertime() {
        return appCheckOvertime;
    }

    public void setAppCheckOvertime(Integer appCheckOvertime) {
        this.appCheckOvertime = appCheckOvertime;
    }

    public String getAppCheckType() {
        return appCheckType;
    }

    public void setAppCheckType(String appCheckType) {
        this.appCheckType = appCheckType;
    }

    public String getAppCheckCommand() {
        return appCheckCommand;
    }

    public void setAppCheckCommand(String appCheckCommand) {
        this.appCheckCommand = appCheckCommand;
    }

    public Integer getBizCheckTime() {
        return bizCheckTime;
    }

    public void setBizCheckTime(Integer bizCheckTime) {
        this.bizCheckTime = bizCheckTime;
    }

    public Integer getBizCheckOvertime() {
        return bizCheckOvertime;
    }

    public void setBizCheckOvertime(Integer bizCheckOvertime) {
        this.bizCheckOvertime = bizCheckOvertime;
    }

    public String getBizCheckType() {
        return bizCheckType;
    }

    public void setBizCheckType(String bizCheckType) {
        this.bizCheckType = bizCheckType;
    }

    public String getBizCheckCommand() {
        return bizCheckCommand;
    }

    public void setBizCheckCommand(String bizCheckCommand) {
        this.bizCheckCommand = bizCheckCommand;
    }
}
