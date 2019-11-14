package com.yinhai.cloud.core.api.exception;

/**
 * @author zhaokai
 * 节点初始化异常
 */
public class DataDecryptRuntimeException extends RuntimeException {

    public DataDecryptRuntimeException() {

    }

    public DataDecryptRuntimeException(String msg) {
        super(msg);
    }

    public DataDecryptRuntimeException(Throwable t) {
        super(t);
    }

    public DataDecryptRuntimeException(String msg, Throwable t) {
        super(msg, t);
    }
}
