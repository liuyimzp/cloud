package com.yinhai.cloud.module.application.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * Created by tianhy on 2018/7/9.
 */
public class AppPVVo extends BaseVo {

    private static final long serialVersionUID = -2841177999346676411L;
    // 序列号
    private Long id;
    // 应用ID
    private Long appId;
    // 容器内路径
    private String path;
    // 空间
    private Integer space;
    // 存储(PV)ID
    private Long volumeId;
    // 存储(PV)名称
    private String volumeDisplayName;
    // 存储(PV)类型
    private String volumeType;
    // 存储(PV)类型描述
    private String volumeTypeDesc;
    // 存储英文标识
    private String volumeUuid;
    // 存储空间
    private Integer volumeMaxSpace;
    // 剩余可用空间
    private Integer volumeAvailableSpace;

    public void setSpace(Integer space) {
        this.space = space;
    }

    public void setVolumeMaxSpace(Integer volumeMaxSpace) {
        this.volumeMaxSpace = volumeMaxSpace;
    }

    public void setVolumeAvailableSpace(Integer volumeAvailableSpace) {
        this.volumeAvailableSpace = volumeAvailableSpace;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSpace() {
        return space;
    }

    public Long getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(Long volumeId) {
        this.volumeId = volumeId;
    }

    public String getVolumeDisplayName() {
        return volumeDisplayName;
    }

    public void setVolumeDisplayName(String volumeDisplayName) {
        this.volumeDisplayName = volumeDisplayName;
    }

    public String getVolumeType() {
        return volumeType;
    }

    public void setVolumeType(String volumeType) {
        this.volumeType = volumeType;
    }

    public String getVolumeTypeDesc() {
        return volumeTypeDesc;
    }

    public void setVolumeTypeDesc(String volumeTypeDesc) {
        this.volumeTypeDesc = volumeTypeDesc;
    }

    public String getVolumeUuid() {
        return volumeUuid;
    }

    public void setVolumeUuid(String volumeUuid) {
        this.volumeUuid = volumeUuid;
    }

    public Integer getVolumeMaxSpace() {
        return volumeMaxSpace;
    }

    public Integer getVolumeAvailableSpace() {
        return volumeAvailableSpace;
    }
}
