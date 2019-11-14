package com.yinhai.cloud.module.monitor.api.vo;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.vo.BaseVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by pengwei on 2018/8/29.
 */
public class NodeMonitorVo extends BaseVo {

    private static final long serialVersionUID = -6361941861228297985L;
    private Long nodeId;
    private Long clusterId;
    private String nodeIp;
    private String nodeHostName;
    private String nodeOs;
    private Integer nodeMaxPod;
    private Integer nodeUsedPod;
    private Integer nodeStatus;
    private List<Map> cpuAvails;
    private List<Map> memAvails;

    private Double currCpuUsedAvail;
    private Double currMemUsedAvail;
    private Date biztime;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public String getNodeHostName() {
        return nodeHostName;
    }

    public void setNodeHostName(String nodeHostName) {
        this.nodeHostName = nodeHostName;
    }

    public String getNodeOs() {
        return nodeOs;
    }

    public void setNodeOs(String nodeOs) {
        this.nodeOs = nodeOs;
    }

    public Integer getNodeMaxPod() {
        return nodeMaxPod;
    }

    public void setNodeMaxPod(Integer nodeMaxPod) {
        this.nodeMaxPod = nodeMaxPod;
    }

    public Integer getNodeUsedPod() {
        return nodeUsedPod;
    }

    public void setNodeUsedPod(Integer nodeUsedPod) {
        this.nodeUsedPod = nodeUsedPod;
    }

    public Integer getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(Integer nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public List<Map> getCpuAvails() {
        if (cpuAvails != null){
            return new ArrayList<>(cpuAvails);
        }
        return null;
    }

    public void setCpuAvails(List<Map> cpuAvails) {
        if (cpuAvails != null){
            this.cpuAvails = new ArrayList<>(cpuAvails);
        }
    }

    public List<Map> getMemAvails() {
        if (memAvails != null){
            return new ArrayList<>(memAvails);
        }
        return null;
    }

    public void setMemAvails(List<Map> memAvails) {
        if (memAvails != null){
            this.memAvails = new ArrayList<>(memAvails);
        }
    }

    public Double getCurrCpuUsedAvail() {
        return currCpuUsedAvail;
    }

    public void setCurrCpuUsedAvail(Double currCpuUsedAvail) {
        this.currCpuUsedAvail = currCpuUsedAvail;
    }

    public Double getCurrMemUsedAvail() {
        return currMemUsedAvail;
    }

    public void setCurrMemUsedAvail(Double currMemUsedAvail) {
        this.currMemUsedAvail = currMemUsedAvail;
    }

    public Date getBiztime() {
        if (!ValidateUtil.isEmpty(biztime)){
            return (Date) biztime.clone();
        }
        return null;
    }

    public void setBiztime(Date biztime) {
        if (!ValidateUtil.isEmpty(biztime)){
            this.biztime = (Date) biztime.clone();
        }
    }
}
