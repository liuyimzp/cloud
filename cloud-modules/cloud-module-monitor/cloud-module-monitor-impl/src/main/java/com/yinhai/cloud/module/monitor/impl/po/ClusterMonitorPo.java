package com.yinhai.cloud.module.monitor.impl.po;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.po.BasePo;

import java.util.Date;

/**
 * Created by pengwei on 2018/8/29.
 */
public class ClusterMonitorPo extends BasePo {

    private static final long serialVersionUID = -8317373937145537896L;
    private Long clusterId;
    private String clusterName;
    private Integer clusterStatus;
    private Double cpuAvailability;
    private Double memoryAvailability;
    private Date biztime;

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Integer getClusterStatus() {
        return clusterStatus;
    }

    public void setClusterStatus(Integer clusterStatus) {
        this.clusterStatus = clusterStatus;
    }

    public Double getCpuAvailability() {
        return cpuAvailability;
    }

    public void setCpuAvailability(Double cpuAvailability) {
        this.cpuAvailability = cpuAvailability;
    }

    public Double getMemoryAvailability() {
        return memoryAvailability;
    }

    public void setMemoryAvailability(Double memoryAvailability) {
        this.memoryAvailability = memoryAvailability;
    }

    public Date getBiztime() {
        if (!ValidateUtil.isEmpty(biztime)){
            return (Date) biztime.clone();
        }
        return null;
    }

    public void setBiztime(Date biztime) {
        if (!ValidateUtil.isEmpty(biztime)){
            this.biztime = (Date) biztime.clone();
        }
    }
}
