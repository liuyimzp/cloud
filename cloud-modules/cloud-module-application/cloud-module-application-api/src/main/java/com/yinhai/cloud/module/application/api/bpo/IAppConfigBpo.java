package com.yinhai.cloud.module.application.api.bpo;

import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;

/**
 * Created by tianhy on 2018/6/13.
 */
public interface IAppConfigBpo {
    String SERVICEKEY = "appConfigBpo";

    /**
     * 根据应用ID获取应用配置信息
     *
     * @param appId
     * @return
     */
    AppConfigVo getAppConfig(Long appId);

    /**
     * 保存应用配置信息
     *
     * @param vo
     */
    void saveAppConfig(AppConfigVo vo, AppVo appVo,AppConfigVo oldVo);

    /**
     * 删除应用配置信息
     *
     * @param appId
     */
    void removeAppConfig(Long appId);

    /**
     * 检查镜像ID是否被使用
     *
     * @param appImageId
     * @return
     */
    Boolean checkImageIdUsed(Long appImageId);

    /**
     * 修改配置
     * @param appConfig
     */
    void updateAppConfig(AppConfigVo appConfig);
}
