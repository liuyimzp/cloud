package com.yinhai.cloud.module.repertory.api.vo;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.vo.BaseVo;

import java.util.Date;
import java.util.Objects;

/**
 * Created by tianhy on 2018/5/17.
 */
public class AppImageVo extends BaseVo {
    private static final long serialVersionUID = 4589245032740364309L;

    // 应用镜像id
    private Long id;
    // 应用仓库id
    private Long repertoryId;
    // 应用分组id
    private Long groupId;
    // 应用组标识
    private String groupIdentify;
    // 应用组名称
    private String groupName;
    // 应用名称
    private String appName;
    // 镜像路径
    private String imagePath;
    // 英文标识
    private String identify;
    // 创建时间
    private Date createTime;
    // 版本号
    private String version;
    // 类型：原始、自定义
    private String type;

    // 操作系统
    private String diyOperateSystem;

    // 镜像中应用类型：war、jar、tar
    private String diyType;

    // JDK版本
    private String diyJdkVersion;

    // Build过程日志
    private String diyBuildLog;

    private Integer downloading;
    private Integer downloadNum;

    private String effective;
    // WEB 容器
    private String diyWebContainer;
    private String uploadTmpFileName;
    // war,ear,jar 等上传到服务器存储的名字
    private String localFileName;
    // 上传到服务器临时文件名
    // Build目录名称
    private String imageBuildDirName;

    public AppImageVo() {
    }

    public AppImageVo(Long id, Long repertoryId, Long groupId, String groupIdentify, String groupName, String appName, String imagePath, String identify, Date createTime, String version, String effective,Integer downloading,Integer downloadNum) {
        this.id = id;
        this.repertoryId = repertoryId;
        this.groupId = groupId;
        this.groupIdentify = groupIdentify;
        this.groupName = groupName;
        this.appName = appName;
        this.imagePath = imagePath;
        this.identify = identify;
        if (!ValidateUtil.isEmpty(createTime)){
            this.createTime = (Date) createTime.clone();
        }
        this.version = version;
        this.effective = effective;
        this.downloading = downloading;
        this.downloadNum = downloadNum;
    }

    public String getDiyBuildLog() {
        return diyBuildLog;
    }

    public void setDiyBuildLog(String diyBuildLog) {
        this.diyBuildLog = diyBuildLog;
    }

    public String getUploadTmpFileName() {
        return uploadTmpFileName;
    }

    public void setUploadTmpFileName(String uploadTmpFileName) {
        this.uploadTmpFileName = uploadTmpFileName;
    }

    public String getImageBuildDirName() {
        return imageBuildDirName;
    }

    public void setImageBuildDirName(String imageBuildDirName) {
        this.imageBuildDirName = imageBuildDirName;
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiyOperateSystem() {
        return diyOperateSystem;
    }

    public void setDiyOperateSystem(String diyOperateSystem) {
        this.diyOperateSystem = diyOperateSystem;
    }

    public String getDiyType() {
        return diyType;
    }

    public void setDiyType(String diyType) {
        this.diyType = diyType;
    }

    public String getDiyJdkVersion() {
        return diyJdkVersion;
    }

    public void setDiyJdkVersion(String diyJdkVersion) {
        this.diyJdkVersion = diyJdkVersion;
    }

    public String getDiyWebContainer() {
        return diyWebContainer;
    }

    public void setDiyWebContainer(String diyWebContainer) {
        this.diyWebContainer = diyWebContainer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRepertoryId() {
        return repertoryId;
    }

    public void setRepertoryId(Long repertoryId) {
        this.repertoryId = repertoryId;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public Date getCreateTime() {
        if (!ValidateUtil.isEmpty(createTime)){
            return (Date) createTime.clone();
        }
        return null;
    }

    public void setCreateTime(Date createTime) {
        if (!ValidateUtil.isEmpty(createTime)){
            this.createTime = (Date) createTime.clone();
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public Integer getDownloading() {
        return downloading;
    }

    public void setDownloading(Integer downloading) {
        this.downloading = downloading;
    }

    public Integer getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(Integer downloadNum) {
        this.downloadNum = downloadNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppImageVo that = (AppImageVo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(repertoryId, that.repertoryId) &&
                Objects.equals(groupId, that.groupId) &&
                Objects.equals(groupIdentify, that.groupIdentify) &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(appName, that.appName) &&
                Objects.equals(imagePath, that.imagePath) &&
                Objects.equals(identify, that.identify) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(version, that.version) &&
                Objects.equals(type, that.type) &&
                Objects.equals(diyOperateSystem, that.diyOperateSystem) &&
                Objects.equals(diyType, that.diyType) &&
                Objects.equals(diyJdkVersion, that.diyJdkVersion) &&
                Objects.equals(diyBuildLog, that.diyBuildLog) &&
                Objects.equals(diyWebContainer, that.diyWebContainer) &&
                Objects.equals(uploadTmpFileName, that.uploadTmpFileName) &&
                Objects.equals(localFileName, that.localFileName) &&
                Objects.equals(imageBuildDirName, that.imageBuildDirName) &&
                Objects.equals(effective, that.effective);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, repertoryId, groupId, groupIdentify, groupName, appName, imagePath, identify, createTime, version, type, diyOperateSystem, diyType, diyJdkVersion, diyBuildLog, diyWebContainer, uploadTmpFileName, localFileName, imageBuildDirName, effective);
    }
}
