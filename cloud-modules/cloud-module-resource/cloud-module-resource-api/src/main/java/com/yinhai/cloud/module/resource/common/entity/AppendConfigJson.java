package com.yinhai.cloud.module.resource.common.entity;

/**
 * @author: zhaokai
 * @create: 2018-08-23 11:24:53
 */
public class AppendConfigJson {
    private String serverIp;
    private String appendIp;
    private String filePath;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getAppendIp() {
        return appendIp;
    }

    public void setAppendIp(String appendIp) {
        this.appendIp = appendIp;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
