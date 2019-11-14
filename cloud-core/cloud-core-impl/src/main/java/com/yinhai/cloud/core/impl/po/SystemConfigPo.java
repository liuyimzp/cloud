package com.yinhai.cloud.core.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * @author: zhaokai
 * @create: 2018-08-15 09:48:27
 */
public class SystemConfigPo extends BasePo {
    private static final long serialVersionUID = 667180963702019019L;
    private String propKey;
    private String propValue;
    private String propComment;
    private String propUpdateTime;
    private String propCreateUser;

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
