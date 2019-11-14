package com.yinhai.cloud.module.resource.common.entity;

import java.io.Serializable;

/**
 * @author: zhaokai
 * @create: 2018-08-22 11:50:04
 */
public class ScpInfo implements Serializable {

    private static final long serialVersionUID = -167910740704494039L;
    private String fromServerIp;
    private String fromPath;
    private String toServerIp;
    private String toPath;

    public ScpInfo() {
    }

    public ScpInfo(String fromServerIp, String fromPath, String toServerIp, String toPath) {
        this.fromServerIp = fromServerIp;
        this.fromPath = fromPath;
        this.toServerIp = toServerIp;
        this.toPath = toPath;
    }

    public String getFromServerIp() {
        return fromServerIp;
    }

    public void setFromServerIp(String fromServerIp) {
        this.fromServerIp = fromServerIp;
    }

    public String getFromPath() {
        return fromPath;
    }

    public void setFromPath(String fromPath) {
        this.fromPath = fromPath;
    }

    public String getToServerIp() {
        return toServerIp;
    }

    public void setToServerIp(String toServerIp) {
        this.toServerIp = toServerIp;
    }

    public String getToPath() {
        return toPath;
    }

    public void setToPath(String toPath) {
        this.toPath = toPath;
    }
}
