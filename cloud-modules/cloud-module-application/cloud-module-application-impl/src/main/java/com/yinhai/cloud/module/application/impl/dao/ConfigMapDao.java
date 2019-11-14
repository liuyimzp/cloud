package com.yinhai.cloud.module.application.impl.dao;

import com.yinhai.cloud.module.application.impl.po.ConfigMapPo;

import java.util.List;

/**
 * Created by tianhy on 2019/2/20.
 */
public interface ConfigMapDao {

    String SERVICEKEY = "configMapDao";

    ConfigMapPo select(Long id);

    ConfigMapPo insert(ConfigMapPo po);

    void update(ConfigMapPo po);

    void delete(ConfigMapPo po);

    List<ConfigMapPo> getByAppId(Long appId);

    void deleteByAppId(Long appId);
}
