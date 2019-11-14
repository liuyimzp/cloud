package com.yinhai.cloud.module.resource.cluster.impl.po;

import com.yinhai.core.common.api.util.ValidateUtil;

import java.util.Date;

public class AppArrVo {
    private Long id;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        if (!ValidateUtil.isEmpty(createTime)){
            return (Date) createTime.clone();
        }
        return null;
    }

    public void setCreateTime(Date createTime) {
        if (!ValidateUtil.isEmpty(createTime)){
            this.createTime = (Date) createTime.clone();
        }
    }
}
