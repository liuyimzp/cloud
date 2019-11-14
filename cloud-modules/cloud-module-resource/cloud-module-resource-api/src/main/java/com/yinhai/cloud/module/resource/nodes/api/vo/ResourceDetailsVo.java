package com.yinhai.cloud.module.resource.nodes.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

import java.io.Serializable;

public class ResourceDetailsVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -3860163398975542039L;
    private Double systemMem;//系统预留内存
    private Double systemCpu;//系统预留cpu
    private Double kubeResMem;//k8s预留内存用于k8s自己运行使用
    private Double kubeResCpu;//k8s预留cpu
    private Double kubeUseCpu;//k8s可用cpu
    private Double kubeUseMem;//k8s可用内存
    private Double evictionHard;//预留内存当节点可用内存小于该值时k8s会驱逐pod
    private Double nodeMem;//节点总内存
    private Double nodeCpu;//节点总cpu

    public Double getSystemMem() {
        return systemMem;
    }

    public void setSystemMem(Double systemMem) {
        this.systemMem = systemMem;
    }

    public Double getSystemCpu() {
        return systemCpu;
    }

    public void setSystemCpu(Double systemCpu) {
        this.systemCpu = systemCpu;
    }

    public Double getKubeResMem() {
        return kubeResMem;
    }

    public void setKubeResMem(Double kubeResMem) {
        this.kubeResMem = kubeResMem;
    }

    public Double getKubeResCpu() {
        return kubeResCpu;
    }

    public void setKubeResCpu(Double kubeResCpu) {
        this.kubeResCpu = kubeResCpu;
    }

    public Double getKubeUseCpu() {
        return kubeUseCpu;
    }

    public void setKubeUseCpu(Double kubeUseCpu) {
        this.kubeUseCpu = kubeUseCpu;
    }

    public Double getKubeUseMem() {
        return kubeUseMem;
    }

    public void setKubeUseMem(Double kubeUseMem) {
        this.kubeUseMem = kubeUseMem;
    }

    public Double getEvictionHard() {
        return evictionHard;
    }

    public void setEvictionHard(Double evictionHard) {
        this.evictionHard = evictionHard;
    }

    public Double getNodeMem() {
        return nodeMem;
    }

    public void setNodeMem(Double nodeMem) {
        this.nodeMem = nodeMem;
    }

    public Double getNodeCpu() {
        return nodeCpu;
    }

    public void setNodeCpu(Double nodeCpu) {
        this.nodeCpu = nodeCpu;
    }
}
