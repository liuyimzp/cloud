package com.yinhai.cloud.module.resource.pv.impl.po;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.po.BasePo;

import java.util.Date;

/**
 * 持久化存储卷 po
 *
 * @author jianglw
 */
@SuppressWarnings("unused")
public class PvPo extends BasePo {
    private static final long serialVersionUID = -7963693775976248559L;
    private Long volumeId;
    private String volumeDisplayName;
    private String volumeUuid;
    private String volumeEnableState;
    private String volumeType;
    private Long volumeCloudStorageId;
    private String volumeStoragePath;
    private Integer volumeMaxSpace;
    private String volumeCreateUser;
    private Date volumeCreateDate;
    private String clusterId;
    private Integer volumeAvailableSpace;
    private Long cephPoolId;

    public PvPo() {
    }

    public PvPo(Long volumeId, String volumeDisplayName, String volumeUuid, String volumeEnableState, String volumeType, Long volumeCloudStorageId, String volumeStoragePath, Integer volumeMaxSpace, String volumeCreateUser, Date volumeCreateDate, String clusterId, Integer volumeAvailableSpace, Long cephPoolId) {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        final PvPo pvPo = (PvPo) o;

        if (!getVolumeId().equals(pvPo.getVolumeId())) {
            return false;
        }
        if (!getVolumeDisplayName().equals(pvPo.getVolumeDisplayName())) {
            return false;
        }
        if (!getVolumeUuid().equals(pvPo.getVolumeUuid())) {
            return false;
        }
        if (!getVolumeEnableState().equals(pvPo.getVolumeEnableState())) {
            return false;
        }
        if (!getVolumeType().equals(pvPo.getVolumeType())) {
            return false;
        }
        if (getVolumeCloudStorageId() != null ? !getVolumeCloudStorageId().equals(pvPo.getVolumeCloudStorageId()) : pvPo.getVolumeCloudStorageId() != null) {
            return false;
        }
        if (!getVolumeStoragePath().equals(pvPo.getVolumeStoragePath())) {
            return false;
        }
        if (!getVolumeMaxSpace().equals(pvPo.getVolumeMaxSpace())) {
            return false;
        }
        if (!getVolumeCreateUser().equals(pvPo.getVolumeCreateUser())) {
            return false;
        }
        if (!getVolumeCreateDate().equals(pvPo.getVolumeCreateDate())) {
            return false;
        }
        return getClusterId().equals(pvPo.getClusterId());
    }

    @Override
    public int hashCode() {
        int result = getVolumeId().hashCode();
        result = 31 * result + getVolumeDisplayName().hashCode();
        result = 31 * result + getVolumeUuid().hashCode();
        result = 31 * result + getVolumeEnableState().hashCode();
        result = 31 * result + getVolumeType().hashCode();
        result = 31 * result + (getVolumeCloudStorageId() != null ? getVolumeCloudStorageId().hashCode() : 0);
        result = 31 * result + getVolumeStoragePath().hashCode();
        result = 31 * result + getVolumeMaxSpace().hashCode();
        result = 31 * result + getVolumeCreateUser().hashCode();
        result = 31 * result + getVolumeCreateDate().hashCode();
        result = 31 * result + getClusterId().hashCode();
        return result;
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
        if (!ValidateUtil.isEmpty(volumeCreateDate)) {
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
