package com.yinhai.cloud.module.resource.overview.bpo;

import com.yinhai.cloud.module.resource.overview.vo.OverviewInfoVo;

/**
 * Created by pengwei on 2018/9/28.
 */
public interface IOverviewBpo {

    /**
     * 获取平台概览页统计信息
     *
     * @param userId 用户编号，用于后期权限维护
     * @return
     */
    OverviewInfoVo getPlatformInfo(Long userId) throws Exception;

    /**
     * 获取Pod总数
     * @return
     */
    Integer getPodsSum();
}
