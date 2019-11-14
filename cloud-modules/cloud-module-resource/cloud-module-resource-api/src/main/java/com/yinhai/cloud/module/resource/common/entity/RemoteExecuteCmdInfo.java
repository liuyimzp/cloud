package com.yinhai.cloud.module.resource.common.entity;

import java.io.Serializable;

/**
 * @author: zhaokai
 * @create: 2018-08-22 13:56:00
 */
public class RemoteExecuteCmdInfo implements Serializable {

    private static final long serialVersionUID = 1956289330734756197L;
    private String serverIp;
    private String cmd;

    public RemoteExecuteCmdInfo() {
    }

    public RemoteExecuteCmdInfo(String serverIp, String cmd) {
        this.serverIp = serverIp;
        this.cmd = cmd;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
