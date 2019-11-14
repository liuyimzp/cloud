package com.yinhai.cloud.module.resource.overview.vo;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.vo.BaseVo;
import io.kubernetes.client.models.V1Affinity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengwei on 2018/9/28.
 */
public class OverviewInfoVo extends BaseVo {
    private static final long serialVersionUID = -1158705075147886614L;
    private Integer totalClusters;
    private Integer runningClusters;
    private Integer abnormalClusters;

    private Integer totalNodes;
    private Integer runningNodes;
    private Integer abnormalNodes;

    private Integer totalStorage;
    private Integer freeStorage;

    //保留属性：命名空间
    private Integer totalNamespaces;

    private Integer totalApps;//应用总数
    private Integer runningApps;//运行中应用数
    private Integer stopApps;//停止应用数
    private Integer noDeployApps;//运行异常应用数
    private Integer noReleases;//未发布应用数
    private Integer notRunnings;//未运行应用数

    private Integer totalPods;

    //仓库状态：1 正常 2 异常
    private Integer registryStatus;
    private Integer middlewareImages;
    private Integer userImages;

    //新增镜像
    private List<String> imageNum;
    private List<String> imageTime;
    //新增应用
    private List<String> appNum;
    private List<String> appTime;

    public Integer getNoReleases() {
        return noReleases;
    }

    public void setNoReleases(Integer noReleases) {
        this.noReleases = noReleases;
    }

    public Integer getNotRunnings() {
        return notRunnings;
    }

    public void setNotRunnings(Integer notRunnings) {
        this.notRunnings = notRunnings;
    }

    public List<String> getAppNum() {
        if (appNum != null){
            return new ArrayList<>(appNum);
        }
        return null;
    }

    public void setAppNum(List<String> appNum) {
        if (appNum != null){
            this.appNum = new ArrayList<>(appNum);
        }
    }

    public List<String> getAppTime() {
        if (appTime != null){
            return new ArrayList<>(appTime);
        }
        return null;
    }

    public void setAppTime(List<String> appTime) {
        if (appTime != null){
            this.appTime = new ArrayList<>(appTime);
        }
    }

    public List<String> getImageNum() {
        if (imageNum != null){
            return new ArrayList<>(imageNum);
        }
        return null;
    }

    public void setImageNum(List<String> imageNum) {
        if (imageNum != null){
            this.imageNum = new ArrayList<>(imageNum);
        }
    }

    public List<String> getImageTime() {
        if (imageTime != null){
            return new ArrayList<>(imageTime);
        }
        return null;
    }

    public void setImageTime(List<String> imageTime) {
        if (imageTime != null){
            this.imageTime = new ArrayList<>(imageTime);
        }
    }

    public Integer getTotalClusters() {
        return totalClusters;
    }

    public void setTotalClusters(Integer totalClusters) {
        this.totalClusters = totalClusters;
    }

    public Integer getRunningClusters() {
        return runningClusters;
    }

    public void setRunningClusters(Integer runningClusters) {
        this.runningClusters = runningClusters;
    }

    public Integer getAbnormalClusters() {
        return abnormalClusters;
    }

    public void setAbnormalClusters(Integer abnormalClusters) {
        this.abnormalClusters = abnormalClusters;
    }

    public Integer getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(Integer totalNodes) {
        this.totalNodes = totalNodes;
    }

    public Integer getRunningNodes() {
        return runningNodes;
    }

    public void setRunningNodes(Integer runningNodes) {
        this.runningNodes = runningNodes;
    }

    public Integer getAbnormalNodes() {
        return abnormalNodes;
    }

    public void setAbnormalNodes(Integer abnormalNodes) {
        this.abnormalNodes = abnormalNodes;
    }

    public Integer getTotalStorage() {
        return totalStorage;
    }

    public void setTotalStorage(Integer totalStorage) {
        this.totalStorage = totalStorage;
    }

    public Integer getFreeStorage() {
        return freeStorage;
    }

    public void setFreeStorage(Integer freeStorage) {
        this.freeStorage = freeStorage;
    }

    public Integer getTotalNamespaces() {
        return totalNamespaces;
    }

    public void setTotalNamespaces(Integer totalNamespaces) {
        this.totalNamespaces = totalNamespaces;
    }

    public Integer getTotalApps() {
        return totalApps;
    }

    public void setTotalApps(Integer totalApps) {
        this.totalApps = totalApps;
    }

    public Integer getRunningApps() {
        return runningApps;
    }

    public void setRunningApps(Integer runningApps) {
        this.runningApps = runningApps;
    }

    public Integer getStopApps() {
        return stopApps;
    }

    public void setStopApps(Integer stopApps) {
        this.stopApps = stopApps;
    }

    public Integer getNoDeployApps() {
        return noDeployApps;
    }

    public void setNoDeployApps(Integer noDeployApps) {
        this.noDeployApps = noDeployApps;
    }

    public Integer getTotalPods() {
        return totalPods;
    }

    public void setTotalPods(Integer totalPods) {
        this.totalPods = totalPods;
    }

    public Integer getRegistryStatus() {
        return registryStatus;
    }

    public void setRegistryStatus(Integer registryStatus) {
        this.registryStatus = registryStatus;
    }

    public Integer getMiddlewareImages() {
        return middlewareImages;
    }

    public void setMiddlewareImages(Integer middlewareImages) {
        this.middlewareImages = middlewareImages;
    }

    public Integer getUserImages() {
        return userImages;
    }

    public void setUserImages(Integer userImages) {
        this.userImages = userImages;
    }
}
