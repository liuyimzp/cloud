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
public class ClusterMonitorVo extends BaseVo {

    private static final long serialVersionUID = -36364605592216948L;
    private Long clusterId;
    private String clusterName;
    private Integer clusterStatus;
    private Integer totalNodes;
    private Integer totalApps;
    private Integer totalCpu;
    private Double totalMemory;
    private Double totalMemAvailable;
    private List<Map> cpuAvails;
    private List<Map> memAvails;
    //最新cpu利用率和内存使用率
    private Double currCpuUsedAvail;
    private Double currMemUsedAvail;
    private Date biztime;

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Integer getClusterStatus() {
        return clusterStatus;
    }

    public void setClusterStatus(Integer clusterStatus) {
        this.clusterStatus = clusterStatus;
    }

    public Integer getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(Integer totalNodes) {
        this.totalNodes = totalNodes;
    }

    public Integer getTotalApps() {
        return totalApps;
    }

    public void setTotalApps(Integer totalApps) {
        this.totalApps = totalApps;
    }

    public Integer getTotalCpu() {
        return totalCpu;
    }

    public void setTotalCpu(Integer totalCpu) {
        this.totalCpu = totalCpu;
    }

    public Double getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Double totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Double getTotalMemAvailable() {
        return totalMemAvailable;
    }

    public void setTotalMemAvailable(Double totalMemAvailable) {
        this.totalMemAvailable = totalMemAvailable;
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
