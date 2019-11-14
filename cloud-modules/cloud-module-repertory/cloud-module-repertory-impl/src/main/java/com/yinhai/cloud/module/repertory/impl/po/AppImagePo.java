package com.yinhai.cloud.module.repertory.impl.po;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.po.BasePo;

import java.sql.Timestamp;

/**
 * Created by tianhy on 2018/5/16.
 * 应用镜像数据库对象
 */
public class AppImagePo extends BasePo {

    private static final long serialVersionUID = 1984071821942669365L;
    // 序列号（主键）
    private Long id;
    // 所属仓库ID
    private Long repertoryId;
    // 创建时间
    private Timestamp createTime;
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
    // WEB 容器
    private String diyWebContainer;
    // Build过程日志
    private String diyBuildLog;
    // 有效标识
    private String effective;
    // 下载标识
    private Integer downloading;
    //下载数量
    private Integer downloadNum;


    public AppImagePo() {
    }

    public AppImagePo(Long id, Long repertoryId, Timestamp createTime, String version) {
        this.id = id;
        this.repertoryId = repertoryId;
        if (!ValidateUtil.isEmpty(createTime)) {
            this.createTime = (Timestamp) createTime.clone();
        }
        this.version = version;
    }

    public String getDiyBuildLog() {
        return diyBuildLog;
    }

    public void setDiyBuildLog(String diyBuildLog) {
        this.diyBuildLog = diyBuildLog;
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

    public Timestamp getCreateTime() {
        if (!ValidateUtil.isEmpty(createTime)) {
            return (Timestamp) createTime.clone();
        }
        return null;
    }

    public void setCreateTime(Timestamp createTime) {
        if (!ValidateUtil.isEmpty(createTime)) {
            this.createTime = (Timestamp) createTime.clone();
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

        AppImagePo that = (AppImagePo) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (repertoryId != null ? !repertoryId.equals(that.repertoryId) : that.repertoryId != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        return version != null ? version.equals(that.version) : that.version == null;
    }

    public Integer getDownloading() {
        return downloading;
    }

    public void setDownloading(Integer downloading) {
        this.downloading = downloading;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (repertoryId != null ? repertoryId.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
