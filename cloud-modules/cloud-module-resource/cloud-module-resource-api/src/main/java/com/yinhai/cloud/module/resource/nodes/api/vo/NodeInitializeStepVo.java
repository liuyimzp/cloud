package com.yinhai.cloud.module.resource.nodes.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

import java.io.Serializable;

/**
 * @author: zhaokai
 * @create: 2018-06-11 16:45:58
 */
public class NodeInitializeStepVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 3758908915201504026L;
    private Long nodeId;
    private Integer stepOrder;
    private Integer stepState;
    private String stepLog;
    private String stepRunTime;
    private String stepStartTime;
    private String stepEndTime;
    private Integer stepFinishedPercentage;
    private NodeInitBaseStepVo baseStep;
    private NodeBasicInfoVo nodeBasicInfo;

    public String getStepStartTime() {
        return stepStartTime;
    }

    public void setStepStartTime(String stepStartTime) {
        this.stepStartTime = stepStartTime;
    }

    public String getStepEndTime() {
        return stepEndTime;
    }

    public void setStepEndTime(String stepEndTime) {
        this.stepEndTime = stepEndTime;
    }

    public Integer getStepFinishedPercentage() {
        return stepFinishedPercentage;
    }

    public void setStepFinishedPercentage(Integer stepFinishedPercentage) {
        this.stepFinishedPercentage = stepFinishedPercentage;
    }

    public NodeBasicInfoVo getNodeBasicInfo() {
        return nodeBasicInfo;
    }

    public void setNodeBasicInfo(NodeBasicInfoVo nodeBasicInfo) {
        this.nodeBasicInfo = nodeBasicInfo;
    }

    public NodeInitBaseStepVo getBaseStep() {
        return baseStep;
    }

    public void setBaseStep(NodeInitBaseStepVo baseStep) {
        this.baseStep = baseStep;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }

    public Integer getStepState() {
        return stepState;
    }

    public void setStepState(Integer stepState) {
        this.stepState = stepState;
    }

    public String getStepLog() {
        return stepLog;
    }

    public void setStepLog(String stepLog) {
        this.stepLog = stepLog;
    }

    public String getStepRunTime() {
        return stepRunTime;
    }

    public void setStepRunTime(String stepRunTime) {
        this.stepRunTime = stepRunTime;
    }
}
