package com.yinhai.cloud.module.resource.cluster.impl.po;


import com.yinhai.core.service.ta3.domain.po.BasePo;

import java.io.Serializable;

/**
 * @author zhaokai
 */
public class ClusterPo extends BasePo implements Serializable {

    private static final long serialVersionUID = -7631295861264058864L;
    private Long id;
    private String name;
    private String identify;
    private Integer numOfNodes;
    private String comment;
    private Integer deleting;
    private String createUserId;
    private String createDate;
    private String clusterDeletingServer;
    private Integer runningState;
    private Integer totalCpu;
    private Integer istioPort;
    private Double totalCpuAvailable;
    private Double totalMemory;
    private Double totalMemAvailable;
    private String clusterHaVirtualIP;
    /**
     * 集群可用总内存（总内存-系统预留）
     */
    private Double totalAllocatableMemory;
    /**
     * 集群可用总CPU（总cpu-系统预留）
     */
    private Double totalAllocatableCpu;

    public Double getTotalAllocatableMemory() {
        return totalAllocatableMemory;
    }

    public void setTotalAllocatableMemory(Double totalAllocatableMemory) {
        this.totalAllocatableMemory = totalAllocatableMemory;
    }

    public Double getTotalAllocatableCpu() {
        return totalAllocatableCpu;
    }

    public void setTotalAllocatableCpu(Double totalAllocatableCpu) {
        this.totalAllocatableCpu = totalAllocatableCpu;
    }

    public String getClusterHaVirtualIP() {
        return clusterHaVirtualIP;
    }

    public void setClusterHaVirtualIP(String clusterHaVirtualIP) {
        this.clusterHaVirtualIP = clusterHaVirtualIP;
    }

    public Integer getRunningState() {
        return runningState;
    }

    public void setRunningState(Integer runningState) {
        this.runningState = runningState;
    }

    public String getClusterDeletingServer() {
        return clusterDeletingServer;
    }

    public void setClusterDeletingServer(String clusterDeletingServer) {
        this.clusterDeletingServer = clusterDeletingServer;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getDeleting() {
        return deleting;
    }

    public void setDeleting(Integer deleting) {
        this.deleting = deleting;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }


    public Integer getNumOfNodes() {
        return numOfNodes;
    }

    public void setNumOfNodes(Integer numOfNodes) {
        this.numOfNodes = numOfNodes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getTotalCpu() {
        return totalCpu;
    }

    public void setTotalCpu(Integer totalCpu) {
        this.totalCpu = totalCpu;
    }

    public Double getTotalCpuAvailable() {
        return totalCpuAvailable;
    }

    public void setTotalCpuAvailable(Double totalCpuAvailable) {
        this.totalCpuAvailable = totalCpuAvailable;
    }

    public Double getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Double totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Double getTotalMemAvailable() {
        return totalMemAvailable;
    }

    public void setTotalMemAvailable(Double totalMemAvailable) {
        this.totalMemAvailable = totalMemAvailable;
    }

    public Integer getIstioPort() {
        return istioPort;
    }

    public void setIstioPort(Integer istioPort) {
        this.istioPort = istioPort;
    }

    @Override
    public String toString() {
        return "ClusterPo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", identify='" + identify + '\'' +
                ", numOfNodes=" + numOfNodes +
                ", comment='" + comment + '\'' +
                '}';
    }
}
