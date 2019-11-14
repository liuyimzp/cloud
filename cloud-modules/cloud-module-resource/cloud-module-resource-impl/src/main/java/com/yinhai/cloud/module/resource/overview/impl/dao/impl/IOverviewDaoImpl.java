package com.yinhai.cloud.module.resource.overview.impl.dao.impl;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.repertory.api.constant.RepositoryConstant;
import com.yinhai.cloud.module.resource.constants.ClusterState;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.overview.impl.dao.IOverviewDao;
import com.yinhai.cloud.module.resource.overview.impl.po.SpaceInfoPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by pengwei on 2018/9/29.
 */
public class IOverviewDaoImpl extends HibernateDAO<SpaceInfoPo> implements IOverviewDao {
    @Override
    public Integer countClusters(Long userid) {
        String hql = "select count(*) from ClusterPo as cluster";
        Number count = getCount(hql, null, null);

        return count.intValue();
    }

    @Override
    public Integer countRunningClusters(Long userId) {
        String hql = "select count(*) from ClusterPo as cluster where cluster.clusterRunningStatus = ? ";// + ClusterState.CLUSTER_RUNNING_SUCCESSFULLY;
        Number count = getCount(hql, null, ClusterState.CLUSTER_RUNNING_SUCCESSFULLY);

        return count.intValue();
    }

    @Override
    public Integer countNodes(Long userid) {
        String hql = "select count(*) from NodeBasicInfoPo as node";
        Number count = getCount(hql, null, null);

        return count.intValue();
    }

    @Override
    public Integer countRunningNodes(Long userId) {
        String hql = "select count(*) from NodeBasicInfoPo as node where node.nodeRunningState = ? ";// + NodeState.RUNNING_SUCCESS;
        Number count = getCount(hql, null, NodeState.RUNNING_SUCCESS);

        return count.intValue();
    }

    @Override
    public Integer countApps(Long userid) {
        String hql = "select count(*) from AppPo as app";
        Number count = getCount(hql, null, null);

        return count.intValue();
    }

    @Override
    public Integer countStopApps(Long userId) {
        String hql = "select count(*) from AppPo as app where app.appStatus = ? ";//+ IAppConstants.APP_STATUS_STOP;
        Number count = getCount(hql, null, IAppConstants.APP_STATUS_STOP);

        return count.intValue();
    }

    @Override
    public Integer countRunningApps(Long userId) {
        String hql = "select count(*) from AppPo as app where app.appStatus = ?";// + IAppConstants.APP_STATUS_RUNNING;
        Number count = getCount(hql, null, IAppConstants.APP_STATUS_RUNNING);

        return count.intValue();
    }

    @Override
    public Integer countNotRunningApps(Long userId) {
        String hql = "select count(*) from AppPo as app where app.appStatus = ? ";// + IAppConstants.APP_STATUS_UNSTART;
        Number count = getCount(hql, null, IAppConstants.APP_STATUS_UNSTART);

        return count.intValue();
    }

    @Override
    public Integer countNoReleases(Long userId) {
        String hql = "select count(*) from AppPo as app where app.appStatus = ? ";// + IAppConstants.APP_STATUS_UNRELEASED;
        Number count = getCount(hql, null, IAppConstants.APP_STATUS_UNRELEASED);

        return count.intValue();
    }

    @Override
    public Integer countDockerImages(Long userid) {
        String hql = "select count(1) from AppRepertoryPo as a, AppImagePo b where a.id = b.repertoryId and b.effective = '1' and a.appType = ? ";// + RepositoryConstant.DOCKER_IMAGE_TYPE_ORIGINAL;
        Number count = getCount(hql, null, RepositoryConstant.DOCKER_IMAGE_TYPE_ORIGINAL);

        return count.intValue();
    }

    @Override
    public Integer countDockerImagesByMiddleware(Long userid) {
        String hql = "select count(1) from AppRepertoryPo as a, AppImagePo b where a.id = b.repertoryId and b.effective = '1' and a.appType = ? ";// + RepositoryConstant.DOCKER_IMAGE_TYPE_DIY;
        Number count = getCount(hql, null, RepositoryConstant.DOCKER_IMAGE_TYPE_DIY);

        return count.intValue();
    }

    @Override
    public List<SpaceInfoPo> countStorage(Long userId) {
        String hql = "select new com.yinhai.cloud.module.resource.overview.impl.po.SpaceInfoPo(sum(volumeMaxSpace) as totalSpace, sum(volumeAvailableSpace) as freeSpace) from PvPo as pv";

        return super.find(hql);
    }

}
