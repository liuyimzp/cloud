package com.yinhai.cloud.module.monitor.api.bpo;

import com.yinhai.cloud.module.monitor.api.vo.ClusterMonitorVo;

import java.util.Date;
import java.util.List;

/**
 * Created by pengwei on 2018/8/29.
 */
public interface IClusterMonitorBpo {

    List<ClusterMonitorVo> getAllClustersMonitorInfo(String userId) throws Exception;

    /**
     * 新增集群监控数据
     *
     * @param clusterMonitorVo
     */
    void insertNewMonitorData(ClusterMonitorVo clusterMonitorVo);

    /**
     * 删除集群时删除监控数据
     *
     * @param clusterId
     */
    void deleteMonitorDataByClusterId(Long clusterId);

    /**
     * 删除过期数据，防止由于监控数据无线增长
     */
    /*void deleteExpireMonitorData(Date date);*/
}
