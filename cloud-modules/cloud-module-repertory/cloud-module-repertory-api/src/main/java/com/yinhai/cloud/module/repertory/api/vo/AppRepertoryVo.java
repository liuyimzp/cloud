package com.yinhai.cloud.module.repertory.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * Created by tianhy on 2018/5/17.
 */
public class AppRepertoryVo extends BaseVo {
    private static final long serialVersionUID = 484663238586154644L;

    // 序列号（主键）
    private Long id;
    // 应用分组序列号
    private Long groupId;
    // 应用分组标识
    private String groupIdentify;
    // 应用分组名称
    private String groupName;
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
    // 版本数
    private Long verCnt;

    private String businessAreaName;

    public AppRepertoryVo() {
    }

    public AppRepertoryVo(Long id, Long groupId, String groupIdentify, String groupName, String appName, String identify, String appType, String imagePath, String appDesc, String businessArea, Long verCnt) {
        this.id = id;
        this.groupId = groupId;
        this.groupIdentify = groupIdentify;
        this.groupName = groupName;
        this.appName = appName;
        this.identify = identify;
        this.appType = appType;
        this.imagePath = imagePath;
        this.appDesc = appDesc;
        this.businessArea = businessArea;
        this.verCnt = verCnt;
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

    public String getGroupIdentify() {
        return groupIdentify;
    }

    public void setGroupIdentify(String groupIdentify) {
        this.groupIdentify = groupIdentify;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public Long getVerCnt() {
        return verCnt;
    }

    public void setVerCnt(Long verCnt) {
        this.verCnt = verCnt;
    }

    public String getBusinessAreaName() {
        return businessAreaName;
    }

    public void setBusinessAreaName(String businessAreaName) {
        this.businessAreaName = businessAreaName;
    }
}
