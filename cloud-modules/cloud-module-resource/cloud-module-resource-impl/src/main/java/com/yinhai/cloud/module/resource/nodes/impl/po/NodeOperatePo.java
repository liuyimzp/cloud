package com.yinhai.cloud.module.resource.nodes.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * @author jianglw
 */
@SuppressWarnings("unused")
public class NodeOperatePo extends BasePo {
    private static final long serialVersionUID = -4689539293254614899L;
    private Long operateId;
    private Long nodeId;
    private String operateCode;
    private String operateLog;
    private Integer operateState;
    private String operateStartTime;
    private String operateFinishTime;
    private Integer operateFinishedPercentage;
    private String operateUser;
    private String operateRunningServer;

    public String getOperateRunningServer() {
        return operateRunningServer;
    }

    public void setOperateRunningServer(String operateRunningServer) {
        this.operateRunningServer = operateRunningServer;
    }

    public Integer getOperateFinishedPercentage() {
        return operateFinishedPercentage;
    }

    public void setOperateFinishedPercentage(Integer operateFinishedPercentage) {
        this.operateFinishedPercentage = operateFinishedPercentage;
    }

    public Long getOperateId() {
        return operateId;
    }

    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    public String getOperateLog() {
        return operateLog;
    }

    public void setOperateLog(String operateLog) {
        this.operateLog = operateLog;
    }

    public Integer getOperateState() {
        return operateState;
    }

    public void setOperateState(Integer operateState) {
        this.operateState = operateState;
    }

    public String getOperateStartTime() {
        return operateStartTime;
    }

    public void setOperateStartTime(String operateStartTime) {
        this.operateStartTime = operateStartTime;
    }

    public String getOperateFinishTime() {
        return operateFinishTime;
    }

    public void setOperateFinishTime(String operateFinishTime) {
        this.operateFinishTime = operateFinishTime;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }
}
