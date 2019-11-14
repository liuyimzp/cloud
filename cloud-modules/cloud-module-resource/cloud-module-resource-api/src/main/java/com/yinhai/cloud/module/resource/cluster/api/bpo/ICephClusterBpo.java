package com.yinhai.cloud.module.resource.cluster.api.bpo;

import com.yinhai.cloud.module.resource.cluster.api.vo.CephClusterVo;

import java.util.List;

/**
 * 集群管理接口
 *
 * @author liuyi02
 */
public interface ICephClusterBpo {


    /**
     * 查询指定ID的集群信息
     *
     * @param id 集群ID
     * @return ClusterVo
     */
    CephClusterVo getClusterById(Long id);

    /**
     * 查询用户所有节点信息
     * @param userid
     * @return List<CephClusterVo>
     */
    List<CephClusterVo> queryAllClusterBasicInfo();

    /**
     * 添加ceph集群
     * @param vo
     * @return
     */
    CephClusterVo saveCeph(CephClusterVo vo);

    /**
     * 判断英文标识是否存在存在返回true不存在返回false
     * @param identify
     * @return boolean
     */
    boolean checkCluster(String identify);

    /**
     * 修改集群信息
     * @param vo
     */
    void updateCluster(CephClusterVo vo);

    /**
     * 删除集群
     * @param id
     */
    void deleteCephCluster(Long id);

    /**
     * 设置集群状态deleting
     * @param clusterId
     * @param i
     */
    void setDeleting(Long clusterId, int i);

    List<CephClusterVo> queryAllClusterByClusterId(Long clusterId);
}
