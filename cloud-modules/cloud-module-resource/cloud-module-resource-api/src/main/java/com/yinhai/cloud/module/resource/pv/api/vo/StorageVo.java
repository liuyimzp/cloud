package com.yinhai.cloud.module.resource.pv.api.vo;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.vo.BaseVo;

import java.util.Date;

/**
 * 持久化存储卷 vo
 *
 * @author pengwei
 */
public class StorageVo extends BaseVo {

    private static final long serialVersionUID = 7371180257879429695L;
    private Long storageId;
    private String storageName;
    private String hostIp;
    private String hostUserName;
    private String hostPassword;
    private String hostRootPath;
    private String hostNote;
    private String storageCreateUser;
    private Date storageCreateTime;
    private Integer hostPort;

    private String totleSize;
    private String freeSize;
    private boolean isActive;

    public StorageVo(Long storageId, String hostIp, Integer hostPort, String hostUserName, String hostPassword) {
        this.storageId = storageId;
        this.hostIp = hostIp;
        this.hostUserName = hostUserName;
        this.hostPassword = hostPassword;
        this.hostPort = hostPort;
    }

    public StorageVo() {
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getHostUserName() {
        return hostUserName;
    }

    public void setHostUserName(String hostUserName) {
        this.hostUserName = hostUserName;
    }

    public String getHostPassword() {
        return hostPassword;
    }

    public void setHostPassword(String hostPassword) {
        this.hostPassword = hostPassword;
    }

    public String getHostRootPath() {
        return hostRootPath;
    }

    public void setHostRootPath(String hostRootPath) {
        this.hostRootPath = hostRootPath;
    }

    public String getHostNote() {
        return hostNote;
    }

    public void setHostNote(String hostNote) {
        this.hostNote = hostNote;
    }

    public String getStorageCreateUser() {
        return storageCreateUser;
    }

    public void setStorageCreateUser(String storageCreateUser) {
        this.storageCreateUser = storageCreateUser;
    }

    public Date getStorageCreateTime() {
        if (!ValidateUtil.isEmpty(storageCreateTime)){
            return (Date) storageCreateTime.clone();
        }
        return null;
    }

    public void setStorageCreateTime(Date storageCreateTime) {
        if (!ValidateUtil.isEmpty(storageCreateTime)){
            this.storageCreateTime = (Date) storageCreateTime.clone();
        }
    }

    public Integer getHostPort() {
        return hostPort;
    }

    public void setHostPort(Integer hostPort) {
        this.hostPort = hostPort;
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
