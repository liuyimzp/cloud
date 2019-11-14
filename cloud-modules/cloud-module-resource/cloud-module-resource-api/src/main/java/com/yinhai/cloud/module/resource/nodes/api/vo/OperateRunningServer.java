package com.yinhai.cloud.module.resource.nodes.api.vo;

import com.yinhai.core.common.api.util.ValidateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author: zhaokai
 * @create: 2018-09-27 13:11:27
 */
public class OperateRunningServer {
    private List<String> ipList;
    private Integer serverPort;

    public OperateRunningServer(String runningServerInfo) {
        parse(runningServerInfo);
    }

    public OperateRunningServer(List<String> ipList, Integer serverPort) {
        if (ipList != null){
            this.ipList = new ArrayList<>(ipList);
        }
        this.serverPort = serverPort;
    }

    private void parse(String runningServerInfo) {
        if (runningServerInfo == null) {
            return;
        }
        ipList = Arrays.asList(runningServerInfo.substring(0, runningServerInfo.indexOf(':')).split("\\s*#\\s*"));
        serverPort = Integer.parseInt(runningServerInfo.substring(runningServerInfo.indexOf(':') + 1).trim());
    }

    public List<String> getIpList() {
        if (ipList != null){
            return new ArrayList<>(ipList);
        }
        return null;
    }

    public void setIpList(List<String> ipList) {
        if (ipList != null){
            this.ipList = new ArrayList<>(ipList);
        }
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperateRunningServer that = (OperateRunningServer) o;
        if (ipList == null || ipList.isEmpty() || that.ipList == null || that.ipList.isEmpty()) {
            return false;
        }
        // 满足ip地址中有交集，则表示是在同一个主机
        boolean hasMixElement = false;
        for (String e : ipList) {
            if (that.ipList.contains(e)) {
                hasMixElement = true;
                break;
            }
        }
        return hasMixElement && Objects.equals(serverPort, that.serverPort);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : ipList) {
            sb.append(s).append("#");
        }
        sb.substring(0, sb.length() - 1);
        sb.append(":").append(serverPort);
        return sb.toString();
    }
}
