package com.yinhai.cloud.module.resource.cluster.impl.dao;

import com.yinhai.cloud.module.resource.cluster.impl.po.ClusterPo;

import java.util.List;

/**
 * @author zhaokai
 */
public interface IClusterDao {
    /**
     * 不分页获取所有集群
     *
     * @return List<ClusterPo>
     */
    List<ClusterPo> getClusterList();

    /**
     * 获取指定ID的集群信息
     *
     * @param id 集群ID
     * @return ClusterPo
     */
    ClusterPo getClusterById(Long id);

    /**
     * 获取指定标识的集群信息
     *
     * @param tag 集群标识
     * @return ClusterPo
     */
    ClusterPo getClusterByTag(String tag);

    /**
     * 添加集群信息到数据库
     *
     * @param clusterPo 集群信息
     * @return 新增后的集群Id
     */
    Long addNewCluster(ClusterPo clusterPo);

    /**
     * 根据ID，更新集群信息
     *
     * @param clusterPo 更新的集群信息，必须包含ID
     */
    void updateClusterById(ClusterPo clusterPo);

    /**
     * 节点停用/启用 更新集群的内存和cpu
     * @param id
     * @param mem
     * @param cpu
     */
    void addClusterMemCpu(Long id, Double mem, Double cpu);
    void subtractClusterMemCpu(Long id, Double mem, Double cpu);

    /**
     * 删除指定Id的集群信息
     *
     * @param id 集群ID
     */
    void deleteClusterById(Long id);

    /**
     * 查询指定标识的集群数量，用于检测标识重复
     *
     * @param tag 集群标识
     * @return 指定标识的集群数量
     */
    Long countByTag(String tag);


}
