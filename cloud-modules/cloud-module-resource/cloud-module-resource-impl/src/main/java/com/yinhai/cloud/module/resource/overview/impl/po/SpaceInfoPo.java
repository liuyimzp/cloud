package com.yinhai.cloud.module.resource.overview.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * Created by pengwei on 2018/9/29.
 */
public class SpaceInfoPo extends BasePo {
    private static final long serialVersionUID = 3756375108629042310L;
    private Long totalSpace;
    private Long freeSpace;

    public SpaceInfoPo() {

    }

    public SpaceInfoPo(Long totalSpace, Long freeSpace) {
        this.totalSpace = totalSpace;
        this.freeSpace = freeSpace;
    }

    public Long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(Long totalSpace) {
        this.totalSpace = totalSpace;
    }

    public Long getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(Long freeSpace) {
        this.freeSpace = freeSpace;
    }
}
