package com.yinhai.cloud.module.monitor.impl.po;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.po.BasePo;

import java.util.Date;

/**
 * Created by pengwei on 2018/9/20.
 */
public class NodeMonitorPo extends BasePo {

    private static final long serialVersionUID = -8212544594228481956L;
    private Long nodeId;
    private Long clusterId;
    private String nodeOs;
    private Integer nodeMaxPod;
    private Integer nodeUsedPod;
    private Integer nodeStatus;
    private Double nodeCPUAvailability;
    private Double nodeMemoryAvailability;
    private Date biztime;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getNodeOs() {
        return nodeOs;
    }

    public void setNodeOs(String nodeOs) {
        this.nodeOs = nodeOs;
    }

    public Integer getNodeMaxPod() {
        return nodeMaxPod;
    }

    public void setNodeMaxPod(Integer nodeMaxPod) {
        this.nodeMaxPod = nodeMaxPod;
    }

    public Integer getNodeUsedPod() {
        return nodeUsedPod;
    }

    public void setNodeUsedPod(Integer nodeUsedPod) {
        this.nodeUsedPod = nodeUsedPod;
    }

    public Integer getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(Integer nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public Double getNodeCPUAvailability() {
        return nodeCPUAvailability;
    }

    public void setNodeCPUAvailability(Double nodeCPUAvailability) {
        this.nodeCPUAvailability = nodeCPUAvailability;
    }

    public Double getNodeMemoryAvailability() {
        return nodeMemoryAvailability;
    }

    public void setNodeMemoryAvailability(Double nodeMemoryAvailability) {
        this.nodeMemoryAvailability = nodeMemoryAvailability;
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
