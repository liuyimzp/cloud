package com.yinhai.cloud.module.resource.cluster.impl.po;


import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.po.BasePo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuyi02
 */
public class CephPoolPo extends BasePo implements Serializable {
    private static final long serialVersionUID = -6613735893268576870L;
    private Long poolId;

    private String poolName;

    private Integer poolIsUse;

    private Long clusterId;

    private Date createDate;

    private String createUser;

    private Integer pgNum;

    public Long getPoolId() {
        return poolId;
    }

    public void setPoolId(Long poolId) {
        this.poolId = poolId;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public Integer getPoolIsUse() {
        return poolIsUse;
    }

    public void setPoolIsUse(Integer poolIsUse) {
        this.poolIsUse = poolIsUse;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public Date getCreateDate() {
        if (!ValidateUtil.isEmpty(createDate)){
            return (Date) createDate.clone();
        }
        return null;
    }

    public void setCreateDate(Date createDate) {
        if (!ValidateUtil.isEmpty(createDate)){
            this.createDate = (Date) createDate.clone();
        }
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getPgNum() {
        return pgNum;
    }

    public void setPgNum(Integer pgNum) {
        this.pgNum = pgNum;
    }
}
