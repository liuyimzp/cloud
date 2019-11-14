package com.yinhai.cloud.module.resource.nodes.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * @author jianglw
 */
@SuppressWarnings("unused")
public class NodeOperateVo extends BaseVo {
    private static final long serialVersionUID = -8453174646755830967L;
    private Long operateId;
    private Long nodeId;
    private String operateCode;
    private String operateDesc;
    private String operateLog;
    private Integer operateState;
    private String operateStateDesc;
    private String operateStartTime;
    private String operateFinishTime;
    private Integer operateFinishedPercentage;
    private String takeTimeSeconds;
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

    public String getTakeTimeSeconds() {
        return takeTimeSeconds;
    }

    public void setTakeTimeSeconds(String takeTimeSeconds) {
        this.takeTimeSeconds = takeTimeSeconds;
    }

    public String getOperateStateDesc() {
        return operateStateDesc;
    }

    public void setOperateStateDesc(String operateStateDesc) {
        this.operateStateDesc = operateStateDesc;
    }

    public String getOperateDesc() {
        return operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc;
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
