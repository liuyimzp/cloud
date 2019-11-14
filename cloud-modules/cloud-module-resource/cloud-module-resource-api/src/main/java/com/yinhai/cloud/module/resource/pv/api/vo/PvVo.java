package com.yinhai.cloud.module.resource.pv.api.vo;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.vo.BaseVo;

import java.util.Date;

/**
 * 持久化存储卷 vo
 *
 * @author jianglw
 */
@SuppressWarnings("unused")
public class PvVo extends BaseVo {
    private static final long serialVersionUID = 1652631998417904708L;
    private Long volumeId;
    private String volumeDisplayName;
    private String volumeUuid;
    private String volumeEnableState;
    private String volumeEnableStateDesc;
    private String volumeType;
    private String volumeTypeDesc;
    private Long volumeCloudStorageId;
    private String volumeStoragePath;
    private Integer volumeMaxSpace;
    private String volumeCreateUser;
    private Date volumeCreateDate;
    private String clusterId;
    private String clusterName;
    private Long cephPoolId;
    //可用空间
    private Integer volumeAvailableSpace;

    public PvVo() {
    }
    public PvVo(Long volumeId, String volumeDisplayName, String volumeUuid, String volumeEnableState, String volumeType, Long volumeCloudStorageId, String volumeStoragePath, Integer volumeMaxSpace, String volumeCreateUser, Date volumeCreateDate, String clusterId, Integer volumeAvailableSpace, Long cephPoolId) {
        this.volumeId = volumeId;
        this.volumeDisplayName = volumeDisplayName;
        this.volumeUuid = volumeUuid;
        this.volumeEnableState = volumeEnableState;
        this.volumeType = volumeType;
        this.volumeCloudStorageId = volumeCloudStorageId;
        this.volumeStoragePath = volumeStoragePath;
        this.volumeMaxSpace = volumeMaxSpace;
        this.volumeCreateUser = volumeCreateUser;
        this.volumeCreateDate = volumeCreateDate;
        this.clusterId = clusterId;
        this.volumeAvailableSpace = volumeAvailableSpace;
        this.cephPoolId = cephPoolId;
    }

    public String getVolumeEnableStateDesc() {
        return volumeEnableStateDesc;
    }

    public void setVolumeEnableStateDesc(final String volumeEnableStateDesc) {
        this.volumeEnableStateDesc = volumeEnableStateDesc;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getVolumeTypeDesc() {
        return volumeTypeDesc;
    }

    public void setVolumeTypeDesc(final String volumeTypeDesc) {
        this.volumeTypeDesc = volumeTypeDesc;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(final String clusterId) {
        this.clusterId = clusterId;
    }

    public Long getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(final Long volumeId) {
        this.volumeId = volumeId;
    }

    public String getVolumeDisplayName() {
        return volumeDisplayName;
    }

    public void setVolumeDisplayName(final String volumeDisplayName) {
        this.volumeDisplayName = volumeDisplayName;
    }

    public String getVolumeUuid() {
        return volumeUuid;
    }

    public void setVolumeUuid(final String volumeUuid) {
        this.volumeUuid = volumeUuid;
    }

    public String getVolumeEnableState() {
        return volumeEnableState;
    }

    public void setVolumeEnableState(final String volumeEnableState) {
        this.volumeEnableState = volumeEnableState;
    }

    public String getVolumeType() {
        return volumeType;
    }

    public void setVolumeType(final String volumeType) {
        this.volumeType = volumeType;
    }

    public Long getVolumeCloudStorageId() {
        return volumeCloudStorageId;
    }

    public void setVolumeCloudStorageId(final Long volumeCloudStorageId) {
        this.volumeCloudStorageId = volumeCloudStorageId;
    }

    public String getVolumeStoragePath() {
        return volumeStoragePath;
    }

    public void setVolumeStoragePath(final String volumeStoragePath) {
        this.volumeStoragePath = volumeStoragePath;
    }

    public Integer getVolumeMaxSpace() {
        return volumeMaxSpace;
    }

    public void setVolumeMaxSpace(Integer volumeMaxSpace) {
        this.volumeMaxSpace = volumeMaxSpace;
    }

    public String getVolumeCreateUser() {
        return volumeCreateUser;
    }

    public void setVolumeCreateUser(final String volumeCreateUser) {
        this.volumeCreateUser = volumeCreateUser;
    }

    public Date getVolumeCreateDate() {
        if (!ValidateUtil.isEmpty(volumeCreateDate)){
            return (Date) volumeCreateDate.clone();
        }
        return null;
    }

    public void setVolumeCreateDate(final Date volumeCreateDate) {
        if (!ValidateUtil.isEmpty(volumeCreateDate)){
            this.volumeCreateDate = (Date) volumeCreateDate.clone();
        }
    }

    public Integer getVolumeAvailableSpace() {
        return volumeAvailableSpace;
    }

    public void setVolumeAvailableSpace(Integer volumeAvailableSpace) {
        this.volumeAvailableSpace = volumeAvailableSpace;
    }

    public Long getCephPoolId() {
        return cephPoolId;
    }

    public void setCephPoolId(Long cephPoolId) {
        this.cephPoolId = cephPoolId;
    }
}
