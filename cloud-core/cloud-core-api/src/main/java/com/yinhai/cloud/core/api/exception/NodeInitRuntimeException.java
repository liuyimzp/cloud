package com.yinhai.cloud.core.api.exception;

/**
 * @author zhaokai
 * 节点初始化异常
 */
public class NodeInitRuntimeException extends RuntimeException {

    public NodeInitRuntimeException() {

    }

    public NodeInitRuntimeException(String msg) {
        super(msg);
    }

    public NodeInitRuntimeException(Throwable t) {
        super(t);
    }

    public NodeInitRuntimeException(String msg, Throwable t) {
        super(msg, t);
    }
}
