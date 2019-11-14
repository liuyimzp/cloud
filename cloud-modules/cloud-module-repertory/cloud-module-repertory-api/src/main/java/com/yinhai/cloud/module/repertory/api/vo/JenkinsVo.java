package com.yinhai.cloud.module.repertory.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * Created by liuyi on 2019/11/13.
 */
public class JenkinsVo extends BaseVo {

    private static final long serialVersionUID = -3363154103583324554L;

    private String stat;//执行状态

    private String statStr;//执行状态描述

    private String describe;//描述

    private String name;//名称

    private String isSussce;//是否成功过

    private String lastSucces;//上次是否成功

    private String lastSuccesId;//上次成功编号

    private String lastFailedId;//上次失败编号

    private String lastFailed;//上次失败时间

    private String lastTime;//上次持续时间

    private String sussceLog;//上次成功的日志输出

    private String failedLog;//上次失败的日志输出

    public String getLastSuccesId() {
        return lastSuccesId;
    }

    public void setLastSuccesId(String lastSuccesId) {
        this.lastSuccesId = lastSuccesId;
    }

    public String getLastFailedId() {
        return lastFailedId;
    }

    public void setLastFailedId(String lastFailedId) {
        this.lastFailedId = lastFailedId;
    }

    public String getSussceLog() {
        return sussceLog;
    }

    public void setSussceLog(String sussceLog) {
        this.sussceLog = sussceLog;
    }

    public String getFailedLog() {
        return failedLog;
    }

    public void setFailedLog(String failedLog) {
        this.failedLog = failedLog;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getIsSussce() {
        return isSussce;
    }

    public void setIsSussce(String isSussce) {
        this.isSussce = isSussce;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastSucces() {
        return lastSucces;
    }

    public void setLastSucces(String lastSucces) {
        this.lastSucces = lastSucces;
    }

    public String getLastFailed() {
        return lastFailed;
    }

    public void setLastFailed(String lastFailed) {
        this.lastFailed = lastFailed;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getStatStr() {
        return statStr;
    }

    public void setStatStr(String statStr) {
        this.statStr = statStr;
    }
}
