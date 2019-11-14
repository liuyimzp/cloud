package com.yinhai.cloud.module.application.impl.dao;

import com.yinhai.cloud.module.application.impl.po.AppServicePo;

import java.util.List;

/**
 * Created by tianhy on 2018/6/14.
 */
public interface AppServiceDao {
    String SERVICEKEY = "appServiceDao";

    AppServicePo select(Long id);

    AppServicePo insert(AppServicePo po);

    void update(AppServicePo po);

    void delete(AppServicePo po);

    List<AppServicePo> getAppServicesByAppId(Long appId);

    void deleteByAppId(Long appId);

    List<AppServicePo> getAppServicesByClusterIdAndMappingPort(Long clusterId, Integer mappingPort);

    List<AppServicePo> getAppServicesByAppIdAndInnerPort(Long appId, Integer innerPort);
}
