package com.yinhai.cloud.core.api.exception;

/**
 * @author zhaokai
 * 节点初始化异常
 */
public class DataEncryptRuntimeException extends RuntimeException {

    public DataEncryptRuntimeException() {

    }

    public DataEncryptRuntimeException(String msg) {
        super(msg);
    }

    public DataEncryptRuntimeException(Throwable t) {
        super(t);
    }

    public DataEncryptRuntimeException(String msg, Throwable t) {
        super(msg, t);
    }
}
