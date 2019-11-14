package com.yinhai.cloud.module.resource.overview.impl.dao;

import com.yinhai.cloud.module.resource.overview.impl.po.SpaceInfoPo;

import java.util.List;

/**
 * Created by pengwei on 2018/9/29.
 */
public interface IOverviewDao {

    Integer countClusters(Long userId);

    Integer countRunningClusters(Long userId);

    Integer countNodes(Long userId);

    Integer countRunningNodes(Long userId);

    Integer countApps(Long userId);

    Integer countStopApps(Long userId);

    Integer countRunningApps(Long userId);

    Integer countNotRunningApps(Long userId);

    Integer countNoReleases(Long userId);

    Integer countDockerImages(Long userId);

    Integer countDockerImagesByMiddleware(Long userId);

    List<SpaceInfoPo> countStorage(Long userId);

}
