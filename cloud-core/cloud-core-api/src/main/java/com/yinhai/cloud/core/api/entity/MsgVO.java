package com.yinhai.cloud.core.api.entity;

import java.io.Serializable;

/**
 * Linux返回信息实体类
 * Created by pengwei on 2018/5/11.
 */
public class MsgVO implements Serializable {

    private static final long serialVersionUID = -4572578201140788028L;
    //命令执行成功标识
    private boolean success;

    //执行命令时控制台输出的信息
    private String sysoutMsg;


    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSysoutMsg() {
        return sysoutMsg;
    }

    public void setSysoutMsg(String sysoutMsg) {
        this.sysoutMsg = sysoutMsg;
    }
}
