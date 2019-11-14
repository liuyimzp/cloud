package com.yinhai.cloud.module.repertory.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * Created by tianhy on 2018/9/4.
 */
public class AppRepertoryQueryVo extends BaseVo {

    private static final long serialVersionUID = 2113951771952050339L;
    // 所属业务
    private String businessArea;

    // 应用名称
    private String appName;

    // 英文标识
    private String identifyt;

    // 应用分组序列号
    private Long groupId;
    // 当前页
    private Integer currentPage;
    // 页容量
    private Integer pageSize;

    public String getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIdentifyt() {
        return identifyt;
    }

    public void setIdentifyt(String identifyt) {
        this.identifyt = identifyt;
    }
}
