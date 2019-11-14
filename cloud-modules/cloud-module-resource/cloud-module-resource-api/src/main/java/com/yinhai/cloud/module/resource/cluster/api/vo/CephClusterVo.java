package com.yinhai.cloud.module.resource.cluster.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

import java.io.Serializable;

/**
 * @author liuyi02
 */
public class CephClusterVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = 152398711095950281L;
    private Long id;
    private String name;
    private String identify;
    private Integer numOfNodes;
    private String comment;
    private Integer deleting;
    private String createUserId;
    private String createDate;
    private Double memoryTotal;
    private Double memoryAvaiLabel;
    private Integer runningState;
    private String runningStateDesc;
    private Integer poolSize;
    private Integer minPoolSize;
    private String fsName;
    private Long clusterId;

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
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

    public Integer getDeleting() {
        return deleting;
    }

    public void setDeleting(Integer deleting) {
        this.deleting = deleting;
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

    public Double getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(Double memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public Double getMemoryAvaiLabel() {
        return memoryAvaiLabel;
    }

    public void setMemoryAvaiLabel(Double memoryAvaiLabel) {
        this.memoryAvaiLabel = memoryAvaiLabel;
    }

    public Integer getRunningState() {
        return runningState;
    }

    public void setRunningState(Integer runningState) {
        this.runningState = runningState;
    }

    public String getRunningStateDesc() {
        return runningStateDesc;
    }

    public void setRunningStateDesc(String runningStateDesc) {
        this.runningStateDesc = runningStateDesc;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public Integer getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(Integer minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public String getFsName() {
        return fsName;
    }

    public void setFsName(String fsName) {
        this.fsName = fsName;
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
