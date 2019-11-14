package com.yinhai.cloud.module.application.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * Created by tianhy on 2018/9/26.
 */
public class AppPodVo extends BaseVo {
    private static final long serialVersionUID = -5057862019326739709L;

    // 序列号（主键）
    private Long appId;

    private String podName;

    private String podServerName;

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
}
