package com.yinhai.cloud.module.repertory.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * Created by tianhy on 2018/5/16.
 * 应用仓库数据库对象
 */
public class AppRepertoryPo extends BasePo {

    private static final long serialVersionUID = 4681022227480132226L;
    // 序列号（主键）
    private Long id;
    // 应用组序列号
    private Long groupId;
    // 应用名称
    private String appName;
    // 英文标识
    private String identify;
    // 应用类型
    private String appType;
    // 镜像路径
    private String imagePath;
    // 应用描述
    private String appDesc;
    // 所属业务
    private String businessArea;

    public AppRepertoryPo() {
    }

    public AppRepertoryPo(Long id, Long groupId, String appName, String identify, String appType, String imagePath, String appDesc, String businessArea) {
        this.id = id;
        this.groupId = groupId;
        this.appName = appName;
        this.identify = identify;
        this.appType = appType;
        this.imagePath = imagePath;
        this.appDesc = appDesc;
        this.businessArea = businessArea;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }
}
