package com.yinhai.cloud.module.repertory.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * Created by tianhy on 2018/5/24.
 */
public class AppImageQueryVo extends BaseVo {

    private static final long serialVersionUID = 1918340206789252552L;

    private Long repertoryId;

    private Long appId;

    private String identify;

    //是否赛选无效镜像
    private String isFine;
    // 所属业务领域
    private String businessArea;
    // 应用分组ID
    private Long groupId;

    private Integer currentPage;

    private Integer pageSize;

    private String effective;

    public Long getRepertoryId() {
        return repertoryId;
    }

    public void setRepertoryId(Long repertoryId) {
        this.repertoryId = repertoryId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

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

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public String getIsFine() {
        return isFine;
    }

    public void setIsFine(String isFine) {
        this.isFine = isFine;
    }
}
