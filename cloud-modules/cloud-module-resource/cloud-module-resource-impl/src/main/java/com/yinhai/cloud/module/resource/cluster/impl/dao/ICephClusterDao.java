package com.yinhai.cloud.module.resource.cluster.impl.dao;

import com.yinhai.cloud.module.resource.cluster.api.vo.CephClusterVo;
import com.yinhai.cloud.module.resource.cluster.impl.po.CephClusterPo;

import java.util.List;

/**
 * @author liuyi02
 */
public interface ICephClusterDao {
    /**
     * 不分页获取所有集群
     *
     * @return List<CephClusterPo>
     */
    List<CephClusterPo> getClusterList();

    /**
     * 通过权限id查询集群
     * @param resourceId
     * @return
     */
    CephClusterPo getClusterById(Long resourceId);

    /**
     * 修改数据
     * @param po
     */
    void updateClusterByPo(CephClusterPo po);

    /**
     * 查用户自己创建的集群
     * @param userid
     * @return
     */
    List<CephClusterPo> getClusterListByUserId(Long userid);

    /**
     * 查询表示是否存在
     * @param identify
     * @return
     */
    boolean getCountTag(String identify);

    /**
     * 添加ceph集群返回添加后的数据
     * @param toPO
     * @return
     */
    CephClusterPo saveCeph(CephClusterPo toPO);

    /**
     * 删除数据库中对应数据
     * @param id
     */
    void deleteCluster(Long id);

    /**
     * 查询某个集群所有ceph集群
     * @param clusterId
     * @return
     */
    List<CephClusterPo> getClusterByK8sClusterId(Long clusterId);
}
