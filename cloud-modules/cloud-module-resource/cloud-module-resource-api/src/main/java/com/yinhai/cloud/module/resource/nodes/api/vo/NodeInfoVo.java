package com.yinhai.cloud.module.resource.nodes.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * @author jianglw
 */
public class NodeInfoVo extends BaseVo {

    private static final long serialVersionUID = -4963160931604091119L;
    private String nodeCpuInfo;
    private String nodeMemoryInfo;
    private String nodeFreeMemory;
    private String nodeDiskInfoTotal;
    private String nodeDiskInfoUsable;
    private String nodeDiskInfoUsage;
    private String nodeVarDisInfoUsable;
    private String nodeVarDisInfoAlwas;
    private String nodeSystem;
    private String errorMsg;
    private int nodeStatus;
    /**
     * 节点可用CPU数量
     */
    private Double nodeAllocatableCpu;

    /**
     * 节点可用内存
     */
    private Double nodeAllocatableMemory;


    public NodeInfoVo() {
    }

    public NodeInfoVo(final int nodeStatus) {
        this.nodeCpuInfo = "0";
        this.nodeMemoryInfo = "0";
        this.nodeFreeMemory = "0";
        this.nodeDiskInfoTotal = "0";
        this.nodeDiskInfoUsable = "0";
        this.nodeDiskInfoUsage = "0";
        this.nodeVarDisInfoUsable = "0";
        this.nodeVarDisInfoAlwas = "0";
        this.nodeSystem = "0";
        this.nodeStatus = nodeStatus;
    }

    public Double getNodeAllocatableMemory() {
        return nodeAllocatableMemory;
    }

    public void setNodeAllocatableMemory(Double nodeAllocatableMemory) {
        this.nodeAllocatableMemory = nodeAllocatableMemory;
    }

    public Double getNodeAllocatableCpu() {
        return nodeAllocatableCpu;
    }

    public void setNodeAllocatableCpu(Double nodeAllocatableCpu) {
        this.nodeAllocatableCpu = nodeAllocatableCpu;
    }

    public String getNodeVarDisInfoUsable() {
        return nodeVarDisInfoUsable;
    }

    public void setNodeVarDisInfoUsable(String nodeVarDisInfoUsable) {
        this.nodeVarDisInfoUsable = nodeVarDisInfoUsable;
    }

    public String getNodeVarDisInfoAlwas() {
        return nodeVarDisInfoAlwas;
    }

    public void setNodeVarDisInfoAlwas(String nodeVarDisInfoAlwas) {
        this.nodeVarDisInfoAlwas = nodeVarDisInfoAlwas;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(final int nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public String getNodeDiskInfoUsable() {
        return nodeDiskInfoUsable;
    }

    public void setNodeDiskInfoUsable(final String nodeDiskInfoUsable) {
        this.nodeDiskInfoUsable = nodeDiskInfoUsable;
    }

    public String getNodeDiskInfoUsage() {
        return nodeDiskInfoUsage;
    }

    public void setNodeDiskInfoUsage(final String nodeDiskInfoUsage) {
        this.nodeDiskInfoUsage = nodeDiskInfoUsage;
    }

    public String getNodeSystem() {
        return nodeSystem;
    }

    public void setNodeSystem(final String nodeSystem) {
        this.nodeSystem = nodeSystem;
    }


    @Override
    public int hashCode() {
        int result = nodeCpuInfo != null ? nodeCpuInfo.hashCode() : 0;
        result = 31 * result + (nodeMemoryInfo != null ? nodeMemoryInfo.hashCode() : 0);
        result = 31 * result + (nodeDiskInfoTotal != null ? nodeDiskInfoTotal.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        final NodeInfoVo that = (NodeInfoVo) o;

        if (nodeCpuInfo != null ? !nodeCpuInfo.equals(that.nodeCpuInfo) : that.nodeCpuInfo != null) {
            return false;
        }
        if (nodeMemoryInfo != null ? !nodeMemoryInfo.equals(that.nodeMemoryInfo) : that.nodeMemoryInfo != null) {
            return false;
        }
        return nodeDiskInfoTotal != null ? nodeDiskInfoTotal.equals(that.nodeDiskInfoTotal) : that.nodeDiskInfoTotal == null;
    }

    public String getNodeCpuInfo() {
        return nodeCpuInfo;
    }

    public void setNodeCpuInfo(final String nodeCpuInfo) {
        this.nodeCpuInfo = nodeCpuInfo;
    }

    public String getNodeMemoryInfo() {
        return nodeMemoryInfo;
    }

    public void setNodeMemoryInfo(final String nodeMemoryInfo) {
        this.nodeMemoryInfo = nodeMemoryInfo;
    }

    public String getNodeDiskInfoTotal() {
        return nodeDiskInfoTotal;
    }

    public void setNodeDiskInfoTotal(final String nodeDiskInfoTotal) {
        this.nodeDiskInfoTotal = nodeDiskInfoTotal;
    }

    public String getNodeFreeMemory() {
        return nodeFreeMemory;
    }

    public void setNodeFreeMemory(String nodeFreeMemory) {
        this.nodeFreeMemory = nodeFreeMemory;
    }

    @Override
    public String toString() {
        return "NodeInfoVo{" +
                "nodeCpuInfo='" + nodeCpuInfo + '\'' +
                ", nodeMemoryInfo='" + nodeMemoryInfo + '\'' +
                ", nodeFreeMemory='" + nodeFreeMemory + '\'' +
                ", nodeDiskInfoTotal='" + nodeDiskInfoTotal + '\'' +
                ", nodeDiskInfoUsable='" + nodeDiskInfoUsable + '\'' +
                ", nodeDiskInfoUsage='" + nodeDiskInfoUsage + '\'' +
                ", nodeVarDisInfoUsable='" + nodeVarDisInfoUsable + '\'' +
                ", nodeVarDisInfoAlwas='" + nodeVarDisInfoAlwas + '\'' +
                ", nodeSystem='" + nodeSystem + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", nodeStatus=" + nodeStatus +
                ", nodeAllocatableCpu=" + nodeAllocatableCpu +
                ", nodeAllocatableMemory=" + nodeAllocatableMemory +
                '}';
    }
}
