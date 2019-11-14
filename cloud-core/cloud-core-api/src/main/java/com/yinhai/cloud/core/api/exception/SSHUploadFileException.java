package com.yinhai.cloud.core.api.exception;

/**
 * @author zhaokai
 * SSH  上传文件失败异常
 */
public class SSHUploadFileException extends Exception {

    public SSHUploadFileException() {

    }

    public SSHUploadFileException(String msg) {
        super(msg);
    }

    public SSHUploadFileException(Throwable t) {
        super(t);
    }

    public SSHUploadFileException(String msg, Throwable t) {
        super(msg, t);
    }
}
