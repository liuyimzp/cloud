package com.yinhai.cloud.module.application.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

import java.util.Date;

/**
 * Created by liuyi02 on 2019/10/28.
 * 应用数据库对象
 */
public class AppVersionPo extends BasePo {
    private static final long serialVersionUID = 201370418166467969L;
    private Long id;//主键

    private Long appId;//绑定的应用id

    private Integer versionName;//版本名称别名（1表示v1)

    private String imageName;//改版本使用的镜像

    private String imageVersion;//该版本镜像版本

    private String isuse;//镜像被使用 1：当前使用，0：当前未使用

    private String isAction;//镜像是否被启动 1：启动中，0：未启动

    private Date createTime;//版本创建时间

    private Long imageId;//绑定镜像的id

    public String getIsAction() {
        return isAction;
    }

    public void setIsAction(String isAction) {
        this.isAction = isAction;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
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

    public Integer getVersionName() {
        return versionName;
    }

    public void setVersionName(Integer versionName) {
        this.versionName = versionName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageVersion() {
        return imageVersion;
    }

    public void setImageVersion(String imageVersion) {
        this.imageVersion = imageVersion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIsuse() {
        return isuse;
    }

    public void setIsuse(String isuse) {
        this.isuse = isuse;
    }
}
