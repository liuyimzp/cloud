package com.yinhai.cloud.module.resource.nodes.impl.po;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.po.BasePo;

import java.util.Date;
import java.util.Objects;

/**
 * @author jianglw
 */
@SuppressWarnings("unused")
public class NodeBasicInfoPo extends BasePo {
    private static final long serialVersionUID = 6298514894347458881L;
    private Long nodeId;
    private Long clusterId;
    private String nodeName;
    private String nodeType;
    private String nodeIP;
    private String nodeUser;
    private String nodePassword;
    private Integer nodeSSHPort;
    private Date nodeCreateDate;
    private String nodeCreateUser;
    private String nodeAsChild;
    private Integer nodeJoined;

    // 运行状态
    private Integer nodeRunningState;
    // 操作状态 ：删除中、启动中、停止中
    private Integer nodeOperateState;
    // 初始化状态 ： 初始化中、初始化完成
    private Integer nodeInitState;

    private Integer nodeCpuAmount;
    /**
     * 节点可用CPU数量
     */
    private Double nodeAllocatableCpu;
    private Double nodeSystemCpu;
    /**
     * 节点可用内存
     */
    private Double nodeAllocatableMemory;
    private Double nodeMemMb;
    private Double nodeSysMemMb;//系统预留资源
    private Double nodeMemAvailable;
    private Double nodeDiskTotalMb;
    private Double nodeDiskUsedMb;
    private Double nodeDiskUsableMb;

    private Boolean keepalivedMaster;

    public Double getNodeAllocatableCpu() {
        return nodeAllocatableCpu;
    }

    public Double getNodeAllocatableMemory() {
        return nodeAllocatableMemory;
    }

    public void setNodeAllocatableMemory(Double nodeAllocatableMemory) {
        this.nodeAllocatableMemory = nodeAllocatableMemory;
    }

    public void setNodeAllocatableCpu(Double nodeAllocatableCpu) {
        this.nodeAllocatableCpu = nodeAllocatableCpu;
    }

    public Boolean getKeepalivedMaster() {
        return keepalivedMaster;
    }

    public void setKeepalivedMaster(Boolean keepalivedMaster) {
        this.keepalivedMaster = keepalivedMaster;
    }


    public Integer getNodeSSHPort() {
        return nodeSSHPort;
    }

    public void setNodeSSHPort(Integer nodeSSHPort) {
        this.nodeSSHPort = nodeSSHPort;
    }

    public Double getNodeMemAvailable() {
        return nodeMemAvailable;
    }

    public void setNodeMemAvailable(final Double nodeMemAvailable) {
        this.nodeMemAvailable = nodeMemAvailable;
    }


    public Double getNodeDiskTotalMb() {
        return nodeDiskTotalMb;
    }

    public void setNodeDiskTotalMb(Double nodeDiskTotalMb) {
        this.nodeDiskTotalMb = nodeDiskTotalMb;
    }

    public Double getNodeDiskUsedMb() {
        return nodeDiskUsedMb;
    }

    public void setNodeDiskUsedMb(Double nodeDiskUsedMb) {
        this.nodeDiskUsedMb = nodeDiskUsedMb;
    }

    public Double getNodeDiskUsableMb() {
        return nodeDiskUsableMb;
    }

    public void setNodeDiskUsableMb(Double nodeDiskUsableMb) {
        this.nodeDiskUsableMb = nodeDiskUsableMb;
    }

    public Integer getNodeCpuAmount() {
        return nodeCpuAmount;
    }

    public void setNodeCpuAmount(Integer nodeCpuAmount) {
        this.nodeCpuAmount = nodeCpuAmount;
    }

    public Double getNodeMemMb() {
        return nodeMemMb;
    }

    public Double getNodeSysMemMb() {
        return nodeSysMemMb;
    }

    public void setNodeSysMemMb(Double nodeSysMemMb) {
        this.nodeSysMemMb = nodeSysMemMb;
    }

    public void setNodeMemMb(Double nodeMemMb) {
        this.nodeMemMb = nodeMemMb;
    }


    public Integer getNodeJoined() {
        return nodeJoined;
    }

    public void setNodeJoined(Integer nodeJoined) {
        this.nodeJoined = nodeJoined;
    }

    public Integer getNodeRunningState() {
        return nodeRunningState;
    }

    public void setNodeRunningState(Integer nodeRunningState) {
        this.nodeRunningState = nodeRunningState;
    }

    public Integer getNodeOperateState() {
        return nodeOperateState;
    }

    public void setNodeOperateState(Integer nodeOperateState) {
        this.nodeOperateState = nodeOperateState;
    }


    public Integer getNodeInitState() {
        return nodeInitState;
    }

    public void setNodeInitState(Integer nodeInitState) {
        this.nodeInitState = nodeInitState;
    }

    public String getNodeAsChild() {
        return nodeAsChild;
    }

    public void setNodeAsChild(final String nodeAsChild) {
        this.nodeAsChild = nodeAsChild;
    }

    public Date getNodeCreateDate() {
        if (!ValidateUtil.isEmpty(nodeCreateDate)){
            return (Date) nodeCreateDate.clone();
        }
        return null;
    }

    public void setNodeCreateDate(final Date nodeCreateDate) {
        if (!ValidateUtil.isEmpty(nodeCreateDate)){
            this.nodeCreateDate = (Date) nodeCreateDate.clone();
        }
    }

    public String getNodeCreateUser() {
        return nodeCreateUser;
    }

    public void setNodeCreateUser(final String nodeCreateUser) {
        this.nodeCreateUser = nodeCreateUser;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(final Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(final Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(final String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(final String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeIP() {
        return nodeIP;
    }

    public void setNodeIP(final String nodeIP) {
        this.nodeIP = nodeIP;
    }

    public String getNodeUser() {
        return nodeUser;
    }

    public void setNodeUser(final String nodeUser) {
        this.nodeUser = nodeUser;
    }

    public String getNodePassword() {
        return nodePassword;
    }

    public Double getNodeSystemCpu() {
        return nodeSystemCpu;
    }

    public void setNodeSystemCpu(Double nodeSystemCpu) {
        this.nodeSystemCpu = nodeSystemCpu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeBasicInfoPo that = (NodeBasicInfoPo) o;
        return Objects.equals(nodeId, that.nodeId) &&
                Objects.equals(clusterId, that.clusterId) &&
                Objects.equals(nodeName, that.nodeName) &&
                Objects.equals(nodeType, that.nodeType) &&
                Objects.equals(nodeIP, that.nodeIP) &&
                Objects.equals(nodeUser, that.nodeUser) &&
                Objects.equals(nodePassword, that.nodePassword) &&
                Objects.equals(nodeSSHPort, that.nodeSSHPort) &&
                Objects.equals(nodeCreateDate, that.nodeCreateDate) &&
                Objects.equals(nodeCreateUser, that.nodeCreateUser) &&
                Objects.equals(nodeAsChild, that.nodeAsChild) &&
                Objects.equals(nodeJoined, that.nodeJoined) &&
                Objects.equals(nodeRunningState, that.nodeRunningState) &&
                Objects.equals(nodeOperateState, that.nodeOperateState) &&
                Objects.equals(nodeInitState, that.nodeInitState) &&
                Objects.equals(nodeCpuAmount, that.nodeCpuAmount) &&
                Objects.equals(nodeMemMb, that.nodeMemMb) &&
                Objects.equals(nodeMemAvailable, that.nodeMemAvailable) &&
                Objects.equals(nodeDiskTotalMb, that.nodeDiskTotalMb) &&
                Objects.equals(nodeDiskUsedMb, that.nodeDiskUsedMb) &&
                Objects.equals(nodeDiskUsableMb, that.nodeDiskUsableMb) &&
                Objects.equals(keepalivedMaster, that.keepalivedMaster);

    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, clusterId, nodeName, nodeType, nodeIP, nodeUser, nodePassword, nodeSSHPort, nodeCreateDate, nodeCreateUser, nodeAsChild, nodeJoined, nodeRunningState, nodeOperateState, nodeInitState, nodeCpuAmount, nodeMemMb, nodeMemAvailable, nodeDiskTotalMb, nodeDiskUsedMb, nodeDiskUsableMb, keepalivedMaster);
    }

    public void setNodePassword(final String nodePassword) {
        this.nodePassword = nodePassword;
    }

}
