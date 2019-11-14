package com.yinhai.cloud.module.resource.cluster.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

import java.io.Serializable;

/**
 * @author zhaokai
 */
public class ClusterVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 4636505776053146249L;
    private Long id;
    private String name;
    private String identify;
    private Double totalMemory;
    /**
     * 集群可用总内存（总内存-系统预留）
     */
    private Double totalAllocatableMemory;
    private Double totalMemAvailable;
    private Double totalDisk;
    private Double totalDiskAvailable;
    private Integer totalCpu;
    /**
     * 集群可用总CPU（总cpu-系统预留）
     */
    private Double totalAllocatableCpu;
    private Double totalCpuAvailable;
    private Integer numOfNodes;
    private Integer istioPort;
    private Integer problemNodesNum;
    private Integer deleting;
    private Boolean allowDelete;
    private String createUserId;
    private String createDate;
    private String comment;
    private Integer runningState;
    private String runningStateDesc;
    private String clusterDeletingServer;
    private String clusterHaVirtualIP;

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

    public String getRunningStateDesc() {
        return runningStateDesc;
    }

    public void setRunningStateDesc(String runningStateDesc) {
        this.runningStateDesc = runningStateDesc;
    }

    public String getClusterDeletingServer() {
        return clusterDeletingServer;
    }

    public void setClusterDeletingServer(String clusterDeletingServer) {
        this.clusterDeletingServer = clusterDeletingServer;
    }

    public Double getTotalDiskAvailable() {

        return totalDiskAvailable;
    }

    public void setTotalDiskAvailable(Double totalDiskAvailable) {
        this.totalDiskAvailable = totalDiskAvailable;
    }

    public Double getTotalMemAvailable() {
        return totalMemAvailable;
    }

    public void setTotalMemAvailable(Double totalMemAvailable) {
        this.totalMemAvailable = totalMemAvailable;
    }

    public Integer getRunningState() {
        return runningState;
    }

    public void setRunningState(Integer runningState) {
        this.runningState = runningState;
    }

    public Boolean getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(Boolean allowDelete) {
        this.allowDelete = allowDelete;
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

    public Integer getProblemNodesNum() {
        return problemNodesNum;
    }

    public void setProblemNodesNum(Integer problemNodesNum) {
        this.problemNodesNum = problemNodesNum;
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

    public Double getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Double totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Double getTotalDisk() {
        return totalDisk;
    }

    public void setTotalDisk(Double totalDisk) {
        this.totalDisk = totalDisk;
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

    public Integer getIstioPort() {
        return istioPort;
    }

    public void setIstioPort(Integer istioPort) {
        this.istioPort = istioPort;
    }

    @Override
    public String toString() {
        return "ClusterVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", identify='" + identify + '\'' +
                ", totalMemory=" + totalMemory +
                ", totalDisk=" + totalDisk +
                ", totalCpu=" + totalCpu +
                ", numOfNodes=" + numOfNodes +
                ", comment='" + comment + '\'' +
                ", totalAllocatableCpu ='" + totalAllocatableCpu + '\'' +
                ", totalAllocatableMemory ='" + totalAllocatableMemory + '\'' +
                '}';
    }
}
