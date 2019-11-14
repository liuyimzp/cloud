package com.yinhai.cloud.core.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * @author: zhaokai
 * @create: 2018-08-15 09:48:27
 */
public class SystemConfigVo extends BaseVo {
    private static final long serialVersionUID = -1838831563796289052L;
    private String propKey;
    private String propValue;
    private String propComment;
    private String propUpdateTime;
    private String propCreateUser;
    // 当前页
    private Integer currentPage;
    // 页容量
    private Integer pageSize;


    public SystemConfigVo() {
    }

    public SystemConfigVo(String propKey, String propValue) {
        this.propKey = propKey;
        this.propValue = propValue;
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

    public String getPropComment() {
        return propComment;
    }

    public void setPropComment(String propComment) {
        this.propComment = propComment;
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getPropUpdateTime() {
        return propUpdateTime;
    }

    public void setPropUpdateTime(String propUpdateTime) {
        this.propUpdateTime = propUpdateTime;
    }

    public String getPropCreateUser() {
        return propCreateUser;
    }

    public void setPropCreateUser(String propCreateUser) {
        this.propCreateUser = propCreateUser;
    }
}
