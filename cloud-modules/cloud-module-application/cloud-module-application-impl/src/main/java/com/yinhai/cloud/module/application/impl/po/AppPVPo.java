package com.yinhai.cloud.module.application.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * Created by tianhy on 2018/7/9.
 * 应用存储（PV）数据库对象
 */
public class AppPVPo extends BasePo {
    private static final long serialVersionUID = 581653244242936042L;

    private Long id;

    private Long appId;

    private Long volumeId;

    private String path;

    private Integer space;

    public AppPVPo() {
    }

    public AppPVPo(Long id, Long appId, Long volumeId, String path, Integer space) {
        this.id = id;
        this.appId = appId;
        this.volumeId = volumeId;
        this.path = path;
        this.space = space;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(Long volumeId) {
        this.volumeId = volumeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSpace() {
        return space;
    }

    public void setSpace(Integer space) {
        this.space = space;
    }
}
