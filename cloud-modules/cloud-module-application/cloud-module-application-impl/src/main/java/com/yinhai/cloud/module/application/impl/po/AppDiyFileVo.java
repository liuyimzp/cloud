package com.yinhai.cloud.module.application.impl.po;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * Created by liuyi02 on 2019/3/27.
 *  自定义应用文件
 */
public class AppDiyFileVo  extends BaseVo {
    private static final long serialVersionUID = -8096515344720459215L;
    private Long id;

    private String appDiyFile;

    private String appStatus;

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppDiyFile() {
        return appDiyFile;
    }

    public void setAppDiyFile(String appDiyFile) {
        this.appDiyFile = appDiyFile;
    }
}
