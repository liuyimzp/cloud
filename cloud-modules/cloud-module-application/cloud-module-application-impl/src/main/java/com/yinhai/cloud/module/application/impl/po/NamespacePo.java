package com.yinhai.cloud.module.application.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * Created by tianhy on 2018/7/6.
 * 命名空间数据库对象
 */
public class NamespacePo extends BasePo {
    private static final long serialVersionUID = -1001759837004068502L;
    // 序列号
    private Long id;
    // 集群ID
    private Long clusterId;
    // 命名空间标识
    private String namespaceIdentify;
    // 命名空间名称
    private String namespaceName;
    // 可用内存
    private Double availableMemory;
    // 最大内存限制
    private Double maxMemoryLimit;
    // 可用CPU
    private Double availableCPU;
    // 最大CPU限制
    private Double maxCPULimit;

    public NamespacePo() {
    }

    public NamespacePo(Long id, Long clusterId, String namespaceIdentify, String namespaceName, Double availableMemory, Double maxMemoryLimit, Double availableCPU, Double maxCPULimit) {
        this.id = id;
        this.clusterId = clusterId;
        this.namespaceIdentify = namespaceIdentify;
        this.namespaceName = namespaceName;
        this.availableMemory = availableMemory;
        this.maxMemoryLimit = maxMemoryLimit;
        this.availableCPU = availableCPU;
        this.maxCPULimit = maxCPULimit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getNamespaceIdentify() {
        return namespaceIdentify;
    }

    public void setNamespaceIdentify(String namespaceIdentify) {
        this.namespaceIdentify = namespaceIdentify;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public Double getAvailableMemory() {
        return availableMemory;
    }

    public void setAvailableMemory(Double availableMemory) {
        this.availableMemory = availableMemory;
    }

    public Double getMaxMemoryLimit() {
        return maxMemoryLimit;
    }

    public void setMaxMemoryLimit(Double maxMemoryLimit) {
        this.maxMemoryLimit = maxMemoryLimit;
    }

    public Double getAvailableCPU() {
        return availableCPU;
    }

    public void setAvailableCPU(Double availableCPU) {
        this.availableCPU = availableCPU;
    }

    public Double getMaxCPULimit() {
        return maxCPULimit;
    }

    public void setMaxCPULimit(Double maxCPULimit) {
        this.maxCPULimit = maxCPULimit;
    }
}
