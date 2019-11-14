package com.yinhai.cloud.core.api.bpo;

import com.yinhai.cloud.core.api.vo.SystemConfigVo;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-08-15 09:44:52
 */
public interface ISystemConfigBpo {
    /**
     * 查询所有
     *
     * @return
     */
    List<SystemConfigVo> getAllSystemConfig();

    /**
     * 获取单个
     *
     * @param propKey
     * @return
     */
    SystemConfigVo getSystemConfig(String propKey);

    /**
     * 更新
     *
     * @param vo
     */
    void updateSystemConfig(SystemConfigVo vo);

    /**
     * 保存新的配置项
     *
     * @param vo
     * @return
     */
    SystemConfigVo insertSystemConfig(SystemConfigVo vo);

    /**
     * 删除配置项
     *
     * @param propKey
     */
    void deleteSystemConfig(String propKey);

    /**
     * 分页查询所有的全局变量
     * @param vo
     * @return
     */
    Object getAllSystemConfigByPage(SystemConfigVo vo);
}
