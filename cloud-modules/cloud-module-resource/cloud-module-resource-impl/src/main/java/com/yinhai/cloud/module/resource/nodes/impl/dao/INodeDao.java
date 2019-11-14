package com.yinhai.cloud.module.resource.nodes.impl.dao;

import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeBasicInfoPo;

import java.util.List;

/**
 * @author jianglw
 */
public interface INodeDao {


    /**
     * 查询所有的Nodes
     *
     * @return
     */
    List<NodeBasicInfoPo> queryAllNodes();

    /**
     * 根据集群ID查询集群下的Nodes
     *
     * @param clusterId
     * @return
     */
    List<NodeBasicInfoPo> queryNodesByClusterId(Long clusterId);

    /**
     * 根据集群Id查询集群下运行中的节点
     *
     * @param clusterId
     * @return
     */
    List<NodeBasicInfoPo> queryRunningNodesByClusterId(Long clusterId);

    /**
     * 根据集群ID，查询其master节点
     *
     * @param clusterId
     * @return
     */

    List<NodeBasicInfoPo> queryMasterNodesByClusterId(Long clusterId);

    /**
     * 根据集群ID，查询其负载节点
     *
     * @param clusterId
     * @return
     */

    List<NodeBasicInfoPo> queryDeployNodesByClusterId(Long clusterId);


    /**
     * 新增一个Node
     *
     * @param vo
     * @return
     */
    NodeBasicInfoVo saveNode(NodeBasicInfoVo vo);

    /**
     * 更新一个Node
     *
     * @param vo
     * @return
     */
    boolean updateNode(NodeBasicInfoVo vo);

    /**
     * 删除一个Node
     *
     * @param vo
     * @return
     */
    void deleteNode(NodeBasicInfoVo vo);

    /**
     * 检查节点名称否重复
     *
     * @param nodeName
     * @return
     */
    boolean checkNodeNameRepeat(final String nodeName);

    /**
     * 检查节点IP是否重复
     *
     * @param nodeIP
     * @return
     */
    boolean checkNodeIPRepeat(final String nodeIP);


    NodeBasicInfoPo queryNodeById(Long nodeId);

    /**
     * ADD BY tianhy 2018.6.22
     * 根据集群ID查询当前运行中的master节点
     *
     * @param cluster_id
     * @return
     */
    List<NodeBasicInfoPo> queryRunningMasterNodesByClusterId(Long cluster_id);

    /**
     * 更新集群中所有节点为删除中状态
     *
     * @param clusterId
     */
    void updateClusterNodesAsDeleting(Long clusterId);

    /**
     * 检测集群中是否有节点正在操作
     *
     * @param clusterId
     * @return
     */
    boolean checkClusterHasOperatingNode(Long clusterId);

    /**
     * 计算集群中未运行的节点数量
     *
     * @param clusterId
     * @return
     */
    Long countClusterNotRunningNode(Long clusterId);

    /**
     * 获取与指定管理节点通信的负载节点
     *
     * @param nodeId
     * @return
     */
    List<NodeBasicInfoVo> queryDeployNodesCommunicateWithMaster(Long nodeId);

    NodeBasicInfoPo queryNodeInfoByIP(String ip);
}
