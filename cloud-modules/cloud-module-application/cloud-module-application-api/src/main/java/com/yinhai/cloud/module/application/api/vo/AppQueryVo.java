package com.yinhai.cloud.module.application.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * Created by tianhy on 2018/9/6.
 */
public class AppQueryVo extends BaseVo {

    private static final long serialVersionUID = 6688570857725104575L;

    private Long clusterId;
    // 名称或者标识
    private String field;
    // 当前页
    private Integer currentPage;
    // 页容量
    private Integer pageSize;

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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
}
