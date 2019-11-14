package com.yinhai.cloud.module.application.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

import java.util.Date;

/**
 * Created by liuyi02 on 2019/9/25.
 * 中间件数据备份库对象
 */
public class ConfigBackUpPo extends BasePo {
    private static final long serialVersionUID = 3679245090397648885L;
    private Long configId;

    private Long appId;

    private Date createTime;//创建时间

    private String isUse;//是否使用0否1是

    private String isConfig;//是否有配置文件0否1是

    private String backup_file_path;

    private String backup_ismount;

    private Integer instanceNum;//应用实例数

    public Integer getInstanceNum() {
        return instanceNum;
    }

    public void setInstanceNum(Integer instanceNum) {
        this.instanceNum = instanceNum;
    }

    public String getBackup_ismount() {
        return backup_ismount;
    }

    public void setBackup_ismount(String backup_ismount) {
        this.backup_ismount = backup_ismount;
    }

    public String getBackup_file_path() {
        return backup_file_path;
    }

    public void setBackup_file_path(String backup_file_path) {
        this.backup_file_path = backup_file_path;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getIsConfig() {
        return isConfig;
    }

    public void setIsConfig(String isConfig) {
        this.isConfig = isConfig;
    }
}
