package com.yinhai.cloud.module.resource.pv.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * 持久化存储卷 vo
 *
 * @author jianglw
 */
public class DiskVo extends BaseVo {

    private static final long serialVersionUID = -358370799683591967L;
    private String mountPath;
    private String totleSize;
    private String freeSize;

    public String getMountPath() {
        return mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }

    public String getTotleSize() {
        return totleSize;
    }

    public void setTotleSize(String totleSize) {
        this.totleSize = totleSize;
    }

    public String getFreeSize() {
        return freeSize;
    }

    public void setFreeSize(String freeSize) {
        this.freeSize = freeSize;
    }
}
