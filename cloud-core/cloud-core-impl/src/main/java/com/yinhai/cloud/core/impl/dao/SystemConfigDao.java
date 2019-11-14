package com.yinhai.cloud.core.impl.dao;

import com.yinhai.cloud.core.impl.po.SystemConfigPo;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-08-15 09:58:38
 */
public interface SystemConfigDao {

    List<SystemConfigPo> getAllSystemConfig();
    SystemConfigPo getSystemConfig(String propKey);
    void updateSystemConfig(SystemConfigPo systemConfigPo);
    void insertSystemConfig(SystemConfigPo systemConfigPo);
    void deleteSystemConfig(String propKey);
}
