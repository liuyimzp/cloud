package com.yinhai.cloud.module.application.impl.dao;

import com.yinhai.cloud.module.application.impl.po.AppPVPo;

import java.util.List;

/**
 * Created by tianhy on 2018/7/9.
 */
public interface AppPVDao {
    String SERVICEKEY = "appPVDao";

    AppPVPo select(Long id);

    AppPVPo insert(AppPVPo po);

    void update(AppPVPo po);

    void delete(AppPVPo po);

    List<AppPVPo> getPVsByAppId(Long appId);

    void deleteByAppId(Long appId);

    List<AppPVPo> getPVsByVolumeId(Long volumeId);
}
