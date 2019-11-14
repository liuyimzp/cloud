package com.yinhai.cloud.module.resource.common.helper;

public interface IK8sInstallHelper {

    String KUBERNETES_INSTALL_TYPE_YUM = "yum";
    String KUBERNETES_INSTALL_TYPE_BINARY = "binary";

    void installSegmentStart(Integer startStepOrder);

    void installFullyStart();
}
