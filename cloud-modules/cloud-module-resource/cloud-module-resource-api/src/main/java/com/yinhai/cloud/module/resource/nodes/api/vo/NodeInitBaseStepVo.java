package com.yinhai.cloud.module.resource.nodes.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

import java.io.Serializable;

/**
 * @author: zhaokai
 * @create: 2018-06-11 16:45:58
 */
public class NodeInitBaseStepVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -5311246740679644673L;
    private Integer stepOrder;
    private String stepName;
    private String stepStartStateDesc;
    private String stepFinishStateDesc;

    public Integer getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepStartStateDesc() {
        return stepStartStateDesc;
    }

    public void setStepStartStateDesc(String stepStartStateDesc) {
        this.stepStartStateDesc = stepStartStateDesc;
    }

    public String getStepFinishStateDesc() {
        return stepFinishStateDesc;
    }

    public void setStepFinishStateDesc(String stepFinishStateDesc) {
        this.stepFinishStateDesc = stepFinishStateDesc;
    }

    @Override
    public String toString() {
        return "NodeInitBaseStepVo{" +
                "stepOrder=" + stepOrder +
                ", stepName='" + stepName + '\'' +
                ", stepStartStateDesc='" + stepStartStateDesc + '\'' +
                ", stepFinishStateDesc='" + stepFinishStateDesc + '\'' +
                '}';
    }
}
