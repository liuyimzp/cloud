package com.yinhai.cloud.module.resource.cluster.api.vo;


import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.vo.BaseVo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuyi02
 */
public class CephPoolVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = 4033231049280307856L;
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
