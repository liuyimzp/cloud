package com.yinhai.cloud.module.application.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * @Author ：kangdw
 * @Date : 2019/4/8
 */
public class PodLogVo extends BaseVo {
    private static final long serialVersionUID = 8129823961245936341L;
    private String inputTimes;
    private Long appId;
    private String podName;
    private String podServerName;
    private Long lineNum;
    private String filterType;

    public String getInputTimes() {
        return inputTimes;
    }

    public void setInputTimes(String inputTimes) {
        this.inputTimes = inputTimes;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getPodServerName() {
        return podServerName;
    }

    public void setPodServerName(String podServerName) {
        this.podServerName = podServerName;
    }

    public Long getLineNum() {
        return lineNum;
    }

    public void setLineNum(Long lineNum) {
        this.lineNum = lineNum;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }
}
