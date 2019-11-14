package com.yinhai.cloud.module.application.api.exception;

/**
 * @author zhaokai
 * 节点初始化异常
 */
public class CreateKubernetesResourceRuntimeException extends RuntimeException {

    public CreateKubernetesResourceRuntimeException() {

    }

    public CreateKubernetesResourceRuntimeException(String msg) {
        super(msg);
    }

    public CreateKubernetesResourceRuntimeException(Throwable t) {
        super(t);
    }

    public CreateKubernetesResourceRuntimeException(String msg, Throwable t) {
        super(msg, t);
    }
}
