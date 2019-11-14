package com.yinhai.cloud.module.application.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

import java.util.Date;

/**
 * Created by liuyi02 on 2019/10/28
 */
public class AppVersionVo extends BaseVo {
    private static final long serialVersionUID = -2539957639948331070L;

    private Long id;//主键

    private Long appId;//绑定的应用id

    private String appName;//对应应用标识

    private Integer versionName;//版本名称别名（1表示v1)

    private String versionNameStr;//版本名称别名（1表示v1)

    private String imageName;//改版本使用的镜像

    private String imageVersion;//该版本镜像版本

    private Date createTime;//版本创建时间

    private String createTimeFom;//创建时间格式化的字符串

    private String isuse;//镜像被使用 1：当前使用，0：当前未使用

    private String isAction;//镜像被使用 1：当前使用，0：当前未使用

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

    public String getCreateTimeFom() {
        return createTimeFom;
    }

    public void setCreateTimeFom(String createTimeFom) {
        this.createTimeFom = createTimeFom;
    }

    public String getVersionNameStr() {
        return versionNameStr;
    }

    public void setVersionNameStr(String versionNameStr) {
        this.versionNameStr = versionNameStr;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIsuse() {
        return isuse;
    }

    public void setIsuse(String isuse) {
        this.isuse = isuse;
    }
}
