package com.yinhai.cloud.module.monitor.impl.dao;

import com.yinhai.cloud.module.monitor.impl.po.ClusterMonitorPo;

import java.util.List;

/**
 * Created by pengwei on 2018/8/28.
 */
public interface IClusterMonitorDao {

    List<ClusterMonitorPo> queryClusterMonitorInfoByClusterId(Long clusterId);

    void insertNewMonitorData(ClusterMonitorPo clusterMonitorPo);

    /**
     * 删除集群时需要删除该集群相关的监控数据
     *
     * @param clusterId 集群编号
     */
    void deleteMonitorDataByClusterId(Long clusterId);
}
