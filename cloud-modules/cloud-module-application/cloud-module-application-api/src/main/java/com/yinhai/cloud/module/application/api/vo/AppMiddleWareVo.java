package com.yinhai.cloud.module.application.api.vo;

/**
 * Created by tianhy on 2018/7/10.
 */
public class AppMiddleWareVo extends AppVo {

    // 映射端口
    private Integer mappingPort;
    // 存储ID
    private Long volumeId;
    // 存储请求空间
    private Integer space;

    public Integer getSpace() {
        return space;
    }

    public void setSpace(Integer space) {
        this.space = space;
    }

    public Integer getMappingPort() {
        return mappingPort;
    }

    public void setMappingPort(Integer mappingPort) {
        this.mappingPort = mappingPort;
    }

    public Long getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(Long volumeId) {
        this.volumeId = volumeId;
    }
}
