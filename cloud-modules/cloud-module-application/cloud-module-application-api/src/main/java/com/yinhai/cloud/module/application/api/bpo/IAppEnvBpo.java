package com.yinhai.cloud.module.application.api.bpo;

import com.yinhai.cloud.module.application.api.vo.AppEnvVo;

import java.util.List;

/**
 * Created by tianhy on 2018/9/6.
 */
public interface IAppEnvBpo {

    String SERVICEKEY = "appEnvBpo";

    /**
     * 根据应用ID查询应用参数列表
     *
     * @param appId
     * @return
     */
    List<AppEnvVo> getAppEnvs(Long appId);

    /**
     * 报错应用参数配置
     *
     * @param vo
     * @return
     */
    AppEnvVo saveAppEnv(AppEnvVo vo);

    /**
     * 检查该应用是否已存在该参数
     *
     * @param appId
     * @param envKey
     * @return
     */
    boolean checkAppIdAndEnvKeyExist(Long appId, String envKey);

    /**
     * 修改应用参数配置
     *
     * @param vo
     */
    void editAppEnv(AppEnvVo vo);

    /**
     * 删除应用参数配置
     *
     * @param id
     */
    void removeAppEnv(Long id);
}
