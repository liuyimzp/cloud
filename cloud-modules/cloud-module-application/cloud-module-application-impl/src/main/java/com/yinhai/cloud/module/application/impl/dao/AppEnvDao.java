package com.yinhai.cloud.module.application.impl.dao;

import com.yinhai.cloud.module.application.impl.po.AppEnvPo;

import java.util.List;

/**
 * Created by tianhy on 2018/9/6.
 */
public interface AppEnvDao {

    String SERVICEKEY = "appEnvDao";

    AppEnvPo select(Long id);

    AppEnvPo insert(AppEnvPo po);

    void update(AppEnvPo po);

    void delete(AppEnvPo po);

    List<AppEnvPo> getAppEnvsByAppId(Long appId);

    List<AppEnvPo> getAppEnvsByAppIdAndEnvKey(Long appId, String envKey);
}
