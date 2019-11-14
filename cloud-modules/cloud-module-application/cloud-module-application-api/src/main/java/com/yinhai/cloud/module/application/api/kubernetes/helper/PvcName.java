package com.yinhai.cloud.module.application.api.kubernetes.helper;

/**
 * @author: zhaokai
 * @create: 2018-10-08 15:42:09
 */
public class PvcName {

    private String pvcTemplateName;
    private String podName;
    private Integer replicas;
    private boolean allowFlexDelete = true;

    public PvcName(String pvcTemplateName, String podName, Integer replicas) {
        this.pvcTemplateName = pvcTemplateName;
        this.podName = podName;
        this.replicas = replicas;
    }

    public PvcName() {
    }

    public boolean isAllowFlexDelete() {
        return allowFlexDelete;
    }

    public void setAllowFlexDelete(boolean allowFlexDelete) {
        this.allowFlexDelete = allowFlexDelete;
    }

    public String getPvcTemplateName() {
        return pvcTemplateName;
    }

    public void setPvcTemplateName(String pvcTemplateName) {
        this.pvcTemplateName = pvcTemplateName;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

}
