package com.yinhai.cloud.module.application.api.bpo;

import com.yinhai.cloud.module.application.api.vo.ConfigMapVo;

import java.util.List;

/**
 * Created by tianhy on 2019/2/20.
 */
public interface IConfigMapBpo {

    String SERVICEKEY = "configMapBpo";

    List<ConfigMapVo> getConfigMapByAppId(Long appId);

    ConfigMapVo saveConfigMap(ConfigMapVo vo);

    void editConfigMap(ConfigMapVo vo);

    void removeConfigMap(Long id);

    void removeConfigMapByAppId(Long appId);
}
