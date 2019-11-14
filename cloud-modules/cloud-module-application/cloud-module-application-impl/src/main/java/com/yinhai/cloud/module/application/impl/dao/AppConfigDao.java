package com.yinhai.cloud.module.application.impl.dao;

import com.yinhai.cloud.module.application.impl.po.AppConfigPo;

import java.util.List;

/**
 * Created by tianhy on 2018/6/13.
 */
public interface AppConfigDao {
    String SERVICEKEY = "appConfigDao";

    AppConfigPo select(Long id);

    AppConfigPo insert(AppConfigPo po);

    void update(AppConfigPo po);

    void delete(AppConfigPo po);

    List<AppConfigPo> getAppConfigsByImageId(Long appImageId);

    void deleteByAppId(Long id);
}
