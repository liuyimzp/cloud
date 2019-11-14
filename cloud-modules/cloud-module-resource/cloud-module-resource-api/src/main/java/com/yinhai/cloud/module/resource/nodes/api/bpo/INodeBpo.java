package com.yinhai.cloud.module.resource.nodes.api.bpo;

import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.OperateRunningServer;

import java.util.List;

/**
 * @author jianglw
 */
public interface INodeBpo {

    String SERVER_MANAGE_NODE = "0";
    String DEPLOY_NODE = "1";


    /**
     * 根据集群ID查询集群下的Nodes
     *
     * @param clusterId
     * @return
     */
    List<NodeBasicInfoVo> queryNodesByClusterId(Long clusterId);


    /**
     * 新增一个Node
     *
     * @param vo
     * @return
     */
    NodeBasicInfoVo saveNode(NodeBasicInfoVo vo);


    /**
     * 只更新节点的基本信息（如IP，名称，密码，用户等），不更新状态信息（如初始化状态、操作状态等），会判断IP，名称是否重复
     *
     * @param node
     * @return true 更新成功，IP 名称合法不重复，false 更新失败，IP 或者名称重复
     */
    boolean updateNodeBasicInfo(NodeBasicInfoVo node);

    /**
     * 只更新状态信息（如初始化状态、操作状态等），不更新基本信息（如IP，名称，密码，用户等）
     *
     * @param node
     * @return
     */
    void updateNodeExternalInfo(NodeBasicInfoVo node);


    /**
     * 删除一个Node
     *
     * @param vo
     * @return
     */
    void deleteNode(NodeBasicInfoVo vo);

    /**
     * 检查节点名称、IP地址是否重复
     *
     * @param vo
     * @return
     */
    boolean checkNodeInfoRepeat(NodeBasicInfoVo vo);

    /**
     * 查询节点信息
     *
     * @param nodeId 节点 ID
     * @return NodeBasicInfoVo对象
     */
    NodeBasicInfoVo queryNodeInfoById(Long nodeId);


    /**
     * 查询获取指定集群下面的所有master节点
     *
     * @param clusterId
     * @return
     */
    List<NodeBasicInfoVo> queryMasterNodesByClusterId(Long clusterId);

    /**
     * 只获取节点的基础信息
     *
     * @param clusterId 集群ID
     * @return
     */
    List<NodeBasicInfoVo> queryNodesBasicInfoByClusterId(Long clusterId);

    /**
     * 获取部署节点列表，只获取基础信息
     *
     * @param clusterId
     * @return
     */
    List<NodeBasicInfoVo> queryDeployNodesByClusterId(Long clusterId);


    /**
     * 清理节点状态
     * 产生原因:在节点正在操作的时候服务器异常停止
     */
    void cleanNodeLoadingState();


    /**
     * 更新节点的资源信息
     *
     * @param vo
     * @param nodeResourceInfo
     */
    void updateNodeResource(NodeBasicInfoVo vo, NodeInfoVo nodeResourceInfo);


    /**
     * 获取与指定管理节点通信的负载节点
     *
     * @param nodeId
     * @return
     */
    List<NodeBasicInfoVo> queryDeployNodesCommunicateWithMaster(Long nodeId);


    NodeBasicInfoVo queryNodeInfoByIP(String ip);

    /**
     * // 查询初始化步骤中，第八步初始化apiserver 为成功的Master节点
     */
    List<NodeBasicInfoVo> queryOtherApiServerInitDoneMasterNodes(Long clusterId, Long nodeId);

    /**
     * 获取集群中，状态已经为初始化完成，但是没有Join的负载节点
     *
     * @param clusterId
     * @return
     */
    List<NodeBasicInfoVo> queryInitDoneButNotJoinedDeployNodes(Long clusterId);

    void updateNodeOperateFailed(Long nodeId, Long operateId, String appendMsg);

    Long updateNodeExternalInfoAndCreateOperate(NodeBasicInfoVo updateNode, String operateCode, OperateRunningServer operateRunningServer);

    /**
     * 获取集群中已经初始化成功的Master节点
     *
     * @param clusterId
     * @return
     */
    List<NodeBasicInfoVo> querInitDoneMasterNodes(Long clusterId);

    List<NodeBasicInfoVo> queryRunningMasterNodesByClusterId(Long cluster_id);

    /**
     * 获取所有k8s集群节点
     * @return
     */
    List<NodeBasicInfoVo> queryAllNodes();

    /**
     * @param vo
     * 设置管理节点是否可以部署应用（是否同时为子节点）
     */
    void setNodeAsChild(NodeBasicInfoVo vo) throws Exception;
}
