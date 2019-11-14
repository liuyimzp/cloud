package com.yinhai.cloud.module.resource.nodes.impl.bpo;

import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.core.api.util.DataEncrypt;
import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.INamespaceBpo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.cluster.impl.dao.IClusterDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.ClusterPo;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeInitStepBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.OperateRunningServer;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeDao;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeInitializeStepDao;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeOperateDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeBasicInfoPo;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeOperatePo;
import com.yinhai.cloud.module.resource.pv.impl.dao.IPersistentVolumeDao;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import com.yinhai.core.common.api.util.NetUtils;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import com.yinhai.modules.org.ta3.domain.dao.IUserDao;
import com.yinhai.modules.org.ta3.domain.po.impl.UserPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jianglw
 */
public class NodeBpoImpl extends TaBaseBpo implements INodeBpo {
    private static final String CODE_KEY_NODE_OPERATE_STATE_DESC = "NODE_OP_STATE_DESC";
    private static final String CODE_KEY_NODE_INIT_STATE_DESC = "NODE_INIT_STATE_DESC";
    private static final String CODE_KEY_NODE_RUNNING_STATE_DESC = "NODE_RUN_STATE_DESC";
    private static final HashSet<Integer> runningOperateStateList = new HashSet<>();
    private final static Logger logger = LoggerFactory.getLogger(NodeBpoImpl.class);

    static {
        runningOperateStateList.add(NodeState.OPERATE_DELETING);
        runningOperateStateList.add(NodeState.OPERATE_STARTING);
        runningOperateStateList.add(NodeState.OPERATE_STOPPING);
        runningOperateStateList.add(NodeState.OPERATE_RUNNING_INIT);
    }

    @Autowired
    private IClusterDao clusterDao;
    @Autowired
    private IClusterBpo clusterBpo;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private INodeDao nodeDao;
    @Autowired
    private INodeInitializeStepDao nodeInitializeStepDao;
    @Autowired
    private INodeOperateDao nodeOperateDao;
    @Autowired
    private INodeInitStepBpo nodeInitStepBpo;
    @Autowired
    private IPersistentVolumeDao pvDao;
    @Autowired
    private IAppBpo appBpo;
    @Autowired
    private INamespaceBpo namespaceBpo;

    @Override
    public List<NodeBasicInfoVo> querInitDoneMasterNodes(Long clusterId) {
        return nodeDao.queryMasterNodesByClusterId(clusterId).stream().map(node -> node.toVo(new NodeBasicInfoVo())).filter(node ->
                NodeState.INIT_DONE.equals(node.getNodeInitState())
                        && NodeUtils.checkNodeIsNotBeenOperating(node).isSuccess()
        ).collect(Collectors.toList());

    }

    @Override
    public Long updateNodeExternalInfoAndCreateOperate(NodeBasicInfoVo updateNode, String operateCode, OperateRunningServer operateRunningServer) {
        updateNodeExternalInfo(updateNode);
        NodeOperatePo po = new NodeOperatePo();
        po.setNodeId(updateNode.getNodeId());
        po.setOperateFinishedPercentage(0);
        po.setOperateCode(operateCode);
        po.setOperateState(ResourceConstant.OPERATE_STATE_RUNNING);
        po.setOperateStartTime(CommonUtil.getTime(ResourceConstant.TIME_FORMAT_WITH_NANO));
        po.setOperateLog("");
        po.setOperateRunningServer(operateRunningServer.toString());
        return nodeOperateDao.saveNewOperate(po);
    }

    @Override
    public void updateNodeOperateFailed(Long nodeId, Long operateId, String appendMsg) {
        NodeOperatePo updateOperate = new NodeOperatePo();
        updateOperate.setOperateId(operateId);
        updateOperate.setOperateState(ResourceConstant.OPERATE_STATE_FAILED);
        updateOperate.setOperateFinishTime(CommonUtil.getTime(ResourceConstant.TIME_FORMAT_WITH_NANO));

        if (!ValidateUtil.isEmpty(appendMsg)) {
            NodeOperatePo oldOperate = nodeOperateDao.getOperateRecordById(operateId);

            updateOperate.setOperateLog(((oldOperate.getOperateLog() == null ? "" : oldOperate.getOperateLog()) + "\n" + appendMsg).replaceAll("\n+", "\n"));
        }

        NodeBasicInfoVo updateNode = new NodeBasicInfoVo();
        updateNode.setNodeId(nodeId);
        updateNode.setNodeOperateState(NodeState.OPERATE_FAILED);

        updateNodeExternalInfo(updateNode);
        nodeOperateDao.updateNodeOperate(updateOperate);
    }

    @Override
    public List<NodeBasicInfoVo> queryDeployNodesByClusterId(Long clusterId) {
        return nodeDao.queryDeployNodesByClusterId(clusterId).stream().map(it ->
                it.toVo(new NodeBasicInfoVo())
        ).collect(Collectors.toList());
    }

    @Override
    public void updateNodeResource(NodeBasicInfoVo node, NodeInfoVo nodeResourceInfo) {

        NodeBasicInfoVo updateNode = queryNodeInfoById(node.getNodeId());
        updateNode.setNodeMemAvailable(Double.parseDouble(nodeResourceInfo.getNodeFreeMemory()));
        updateNode.setNodeMemMb(Double.parseDouble(nodeResourceInfo.getNodeMemoryInfo()));
        updateNode.setNodeDiskTotalMb(Double.parseDouble(nodeResourceInfo.getNodeDiskInfoTotal()));
        updateNode.setNodeDiskUsableMb(Double.parseDouble(nodeResourceInfo.getNodeDiskInfoUsable()));
        updateNode.setNodeDiskUsedMb(Double.parseDouble(nodeResourceInfo.getNodeDiskInfoUsage()));
        updateNode.setNodeCpuAmount(Integer.parseInt(nodeResourceInfo.getNodeCpuInfo()));
        updateNode.setNodeAllocatableCpu(nodeResourceInfo.getNodeAllocatableCpu());
        updateNode.setNodeAllocatableMemory(nodeResourceInfo.getNodeAllocatableMemory());
        updateNodeExternalInfo(updateNode);
    }


    @Override
    public boolean updateNodeBasicInfo(NodeBasicInfoVo node) {
        NodeBasicInfoVo updateVo = new NodeBasicInfoVo();
        updateVo.setNodeId(node.getNodeId());

        updateVo.setNodePassword(node.getNodePassword());
        updateVo.setNodeUser(node.getNodeUser());
        updateVo.setNodeSSHPort(node.getNodeSSHPort());

        NodeBasicInfoPo oldNode = nodeDao.queryNodeById(node.getNodeId());
        if (node.getNodePassword().equals(oldNode.getNodePassword())) {
            // 未更新密码，不用进行加密
            updateVo.setNodePassword(null);
        } else {
            updateVo.setNodePassword(DataEncrypt.encrypt(node.getNodePassword()));
        }

        nodeDao.updateNode(updateVo);
        return true;
    }

    @Override
    public void updateNodeExternalInfo(NodeBasicInfoVo node) {
        NodeBasicInfoVo updateVo = node.toPO(new NodeBasicInfoPo()).toVo(new NodeBasicInfoVo());
        updateVo.setNodePassword(null);
        updateVo.setNodeUser(null);
        updateVo.setNodeSSHPort(null);
        updateVo.setNodeName(null);
        nodeDao.updateNode(updateVo);
    }

    @Override
    public List<NodeBasicInfoVo> queryDeployNodesCommunicateWithMaster(Long nodeId) {
        return nodeDao.queryDeployNodesCommunicateWithMaster(nodeId);
    }

    @Override
    public List<NodeBasicInfoVo> queryNodesByClusterId(final Long clusterId) {
        final List<NodeBasicInfoPo> pos = nodeDao.queryNodesByClusterId(clusterId);
        final String clusterName = getClusterName(clusterId);
        return pos.stream().map(it -> {
            final NodeBasicInfoVo vo = it.toVo(new NodeBasicInfoVo());
            vo.setClusterName(clusterName);
            vo.setNodeCreateUser(getUserName(it.getNodeCreateUser()));
            setNodeStateInfo(vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<NodeBasicInfoVo> queryNodesBasicInfoByClusterId(Long clusterId) {
        return nodeDao.queryNodesByClusterId(clusterId).stream().map(it -> it.toVo(new NodeBasicInfoVo())).collect(Collectors.toList());
    }

    @Override
    public NodeBasicInfoVo saveNode(final NodeBasicInfoVo vo) {
        if (DEPLOY_NODE.equals(vo.getNodeType())) {
            vo.setNodeAsChild("0");
        }
        if (checkNodeInfoRepeat(vo)) {
            return null;
        }
        Long clusterId = vo.getClusterId();
        boolean hasNoKeepalivedMaster = nodeDao.queryMasterNodesByClusterId(clusterId).isEmpty() || nodeDao.queryMasterNodesByClusterId(clusterId).stream().noneMatch(NodeBasicInfoPo::getKeepalivedMaster);

        // 如果是不存在Keepalived Master节点，将该节点设置为keepalived 的master节点
        boolean isKeepAlivedMaster = hasNoKeepalivedMaster && vo.getNodeType().equals(ResourceConstant.NODE_TYPE_MASTER);

        vo.setKeepalivedMaster(isKeepAlivedMaster);

        final NodeBasicInfoVo resultVo = nodeDao.saveNode(vo);
        clusterBpo.plusOneClusterNodeAmountById(clusterId);
        // 生成该节点的初始化步骤记录
        nodeInitStepBpo.createNodeInitializeStep(resultVo.getNodeId());
        return resultVo;
    }

    @Override
    public void deleteNode(final NodeBasicInfoVo vo) {
        Long clusterId = vo.getClusterId();
        clusterBpo.minusOneClusterNodeAmountById(clusterId);
        nodeInitializeStepDao.deleteStepByNodeId(vo.getNodeId());
        nodeOperateDao.deleteByNodeId(vo.getNodeId());
        long nodesCount = nodeDao.queryNodesByClusterId(clusterId).stream().count();
        if (nodesCount == 1) {
            // 最后一个节点，需要删除集群的附带信息PV 应用等
            ServerUtils.deletePvByClusterId(clusterId);
            pvDao.deletePvByClusterId(clusterId);
            appBpo.deleteAppByClusterId(clusterId);
            namespaceBpo.deleteByClusterId(clusterId);
        }
        nodeDao.deleteNode(vo);
    }


    @Override
    public boolean checkNodeInfoRepeat(final NodeBasicInfoVo vo) {
        if (vo.getNodeId() != null) {
            // 更新信息
            NodeBasicInfoVo oldNode = queryNodeInfoById(vo.getNodeId());
            boolean nameSameAsOld = oldNode.getNodeName().equals(vo.getNodeName());
            boolean ipSameAsOld = oldNode.getNodeIP().equals(vo.getNodeIP());
            return (!nameSameAsOld || !ipSameAsOld) && (!nameSameAsOld && nodeDao.checkNodeNameRepeat(oldNode.getNodeName()) || !ipSameAsOld && nodeDao.checkNodeIPRepeat(oldNode.getNodeIP()));
        } else {
            // 新增
            return nodeDao.checkNodeNameRepeat(vo.getNodeName()) || nodeDao.checkNodeIPRepeat(vo.getNodeIP());
        }
    }

    /**
     * 获取集群名称
     *
     * @param clusterId 集群ID
     * @return 集群名称
     */
    private String getClusterName(Long clusterId) {
        final ClusterVo cluster = clusterBpo.getClusterById(clusterId);

        return cluster == null ? null : cluster.getName();
    }

    /**
     * 获取用户名称
     *
     * @param userId 用户ID
     * @return 用户名称
     */
    private String getUserName(String userId) {
        final UserPo userPo = userDao.getUserByUserId(userId);
        return userPo.getName();
    }

    @Override
    public List<NodeBasicInfoVo> queryMasterNodesByClusterId(Long clusterId) {
        return nodeDao.queryMasterNodesByClusterId(clusterId).stream().map(
                p -> p.toVo(new NodeBasicInfoVo())).collect(Collectors.toList());
    }

    @Override
    public NodeBasicInfoVo queryNodeInfoById(Long nodeId) {
        NodeBasicInfoPo po = nodeDao.queryNodeById(nodeId);
        return po != null ? setNodeStateInfo(po.toVo(new NodeBasicInfoVo())) : null;
    }

    @Override
    public List<NodeBasicInfoVo> queryRunningMasterNodesByClusterId(Long clusterId) {
        return nodeDao.queryRunningMasterNodesByClusterId(clusterId).stream().map(it -> it.toVo(new NodeBasicInfoVo())).collect(Collectors.toList());
    }

    @Override
    public List<NodeBasicInfoVo> queryAllNodes() {
        return nodeDao.queryAllNodes().stream().map(it -> {
            final NodeBasicInfoVo vo = it.toVo(new NodeBasicInfoVo());
            setNodeStateInfo(vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void setNodeAsChild(NodeBasicInfoVo vo) throws Exception{
        if (ResourceConstant.MASTER_ALSO_AS_CHILD.equals(vo.getNodeAsChild())){
            vo.setNodeAsChild("0");
        }else {
            vo.setNodeAsChild(ResourceConstant.MASTER_ALSO_AS_CHILD);
        }
        try {
            //执行命令
            //将数据存入数据库
            nodeDao.updateNode(vo);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private NodeBasicInfoVo setNodeStateInfo(NodeBasicInfoVo n) {

        String operateDesc = CodeTableUtil.getDesc(CODE_KEY_NODE_OPERATE_STATE_DESC, String.valueOf(n.getNodeOperateState()));
        String initDesc = CodeTableUtil.getDesc(CODE_KEY_NODE_INIT_STATE_DESC, String.valueOf(n.getNodeInitState()));
        String runningDesc = CodeTableUtil.getDesc(CODE_KEY_NODE_RUNNING_STATE_DESC, String.valueOf(n.getNodeRunningState()));

        n.setNodeOperateStateDesc(operateDesc);
        n.setNodeRunningStateDesc(runningDesc);
        n.setNodeInitStateDesc(initDesc);

        String nodeDisplayStateStyle = ResourceConstant.DISPLAY_STATE_STYLE_NORMAL;
        Boolean allowOperate = true;
        if (!NodeState.INIT_DONE.equals(n.getNodeInitState())) {
            // 未初始化成功，包括初始化失败，和正在初始化
            n.setNodeDisplayStateText(n.getNodeInitStateDesc());
            if (NodeState.INIT_FAILED.equals(n.getNodeInitState())) {
                nodeDisplayStateStyle = ResourceConstant.DISPLAY_STATE_STYLE_ERROR;
            } else if (NodeState.INIT_RUNNING.equals(n.getNodeInitState())) {
                nodeDisplayStateStyle = ResourceConstant.DISPLAY_STATE_STYLE_LOADING;
                allowOperate = false;
            }
        } else {
            // 初始化成功
            if (runningOperateStateList.contains(n.getNodeOperateState())) {
                // 正在操作
                n.setNodeDisplayStateText(n.getNodeOperateStateDesc());
                nodeDisplayStateStyle = ResourceConstant.DISPLAY_STATE_STYLE_LOADING;
                allowOperate = false;
            } else if (!NodeState.OPERATE_FAILED.equals(n.getNodeOperateState())) {
                // 操作完成，未失败，各种成功（如停止节点，启动节点成功）
                n.setNodeDisplayStateText(n.getNodeRunningStateDesc());
                if (NodeState.RUNNING_SUCCESS.equals(n.getNodeRunningState())) {
                    nodeDisplayStateStyle = ResourceConstant.DISPLAY_STATE_STYLE_SUCCESS;
                } else if (NodeState.RUNNING_NOT_NOW.equals(n.getNodeRunningState())) {
                    nodeDisplayStateStyle = ResourceConstant.DISPLAY_STATE_STYLE_WARNING;
                } else {
                    nodeDisplayStateStyle = ResourceConstant.DISPLAY_STATE_STYLE_NORMAL;
                }
            } else {
                // 操作失败
                if (n.getNodeRunningState().equals(NodeState.RUNNING_SUCCESS)) {
                    n.setNodeDisplayStateText(n.getNodeRunningStateDesc());
                    nodeDisplayStateStyle = ResourceConstant.DISPLAY_STATE_STYLE_SUCCESS;
                } else {
                    n.setNodeDisplayStateText(n.getNodeOperateStateDesc());
                    nodeDisplayStateStyle = ResourceConstant.DISPLAY_STATE_STYLE_ERROR;
                }
            }

        }
        n.setNodeAllowOperate(allowOperate);
        n.setNodeDisplayStateStyle(nodeDisplayStateStyle);
        n.setNodeDisplayStateClickType(ResourceConstant.SHOW_INIT_STEP);
        return n;
    }


    @Override
    public void cleanNodeLoadingState() {

        OperateRunningServer currentRunningServer = new OperateRunningServer(CommonUtil.getCurrentMachineIp(), NetUtils.getServerPort());
        logger.info("清理" + currentRunningServer + "正在运行任务状态!");
        // 清理正在删除的集群
        List<ClusterVo> clusterList = clusterBpo.queryAllClusterBasicInfo(1L);
        for (ClusterVo c : clusterList) {
            // 删除该集群操作是否是在当前主机执行的
            OperateRunningServer oldRunningServer = new OperateRunningServer(c.getClusterDeletingServer());


            boolean currentHostIsDeletingCluster = ResourceConstant.CLUSTER_IS_BEEN_DELETING.equals(c.getDeleting()) && oldRunningServer.equals(currentRunningServer);
            if (currentHostIsDeletingCluster) {
                ClusterPo clu = c.toPO(new ClusterPo());
                clu.setId(c.getId());
                clu.setDeleting(ResourceConstant.CLUSTER_IS_NOT_BEEN_DELETING);
                clusterDao.updateClusterById(clu);

                // 如果集群处于正在删除状态，则对应的节点一定也是正在删除状态
                List<NodeBasicInfoPo> nodes = nodeDao.queryNodesByClusterId(c.getId());
                for (NodeBasicInfoPo node : nodes) {
                    if (Objects.equals(node.getNodeOperateState(), NodeState.OPERATE_DELETING)) {
                        node.setNodeOperateState(NodeState.OPERATE_FAILED);
                        nodeDao.updateNode(node.toVo(new NodeBasicInfoVo()));
                    }
                }


            }
        }

        // 清理操作状态，及其对应的节点状态
        List<NodeOperatePo> runningOperateList = nodeOperateDao.getRunnigOperate();
        for (NodeOperatePo operate : runningOperateList) {
            OperateRunningServer oldRunningServer = new OperateRunningServer(operate.getOperateRunningServer());
            if (oldRunningServer.equals(currentRunningServer)) {
                // 该操作为当前主机上执行的
                operate.setOperateRunningServer("");
                operate.setOperateState(ResourceConstant.OPERATE_STATE_FAILED);
                operate.setOperateLog("服务器异常停止");
                operate.setOperateFinishTime(CommonUtil.getTime(ResourceConstant.TIME_FORMAT_WITH_NANO));
                nodeOperateDao.updateNodeOperate(operate);
                // 查询其所在节点状态，并重置
                NodeBasicInfoPo operateNode = nodeDao.queryNodeById(operate.getNodeId());
                // 清理初始化状态
                if (!ValidateUtil.isEmpty(operateNode)) {
                    if (Objects.equals(operateNode.getNodeInitState(), NodeState.INIT_RUNNING)) {
                        // 节点在初始化，需要修改其初始化状态nodeInitState和节点操作状态nodeOperateState 为失败
                        operateNode.setNodeInitState(NodeState.INIT_FAILED);
                        operateNode.setNodeOperateState(NodeState.OPERATE_FAILED);
                        nodeDao.updateNode(operateNode.toVo(new NodeBasicInfoVo()));

                        // 清理初始化步骤状态
                        nodeInitializeStepDao.getNodeInitializeStepList(operateNode.getNodeId()).stream().filter(s -> Objects.equals(s.getStepState(), ResourceConstant.NODE_INIT_STEP_RUNNING)).forEach(s -> {
                            s.setStepState(ResourceConstant.NODE_INIT_STEP_FAILED);
                            s.setStepLog("服务器异常停止");
                            nodeInitializeStepDao.updateStep(s);
                        });


                    } else if (Objects.equals(operateNode.getNodeOperateState(), NodeState.OPERATE_DELETING)
                            || Objects.equals(operateNode.getNodeOperateState(), NodeState.OPERATE_STOPPING)
                            || Objects.equals(operateNode.getNodeOperateState(), NodeState.OPERATE_STARTING)) {
                        // 节点未处于初始化，只需要修改节点的操作状态nodeOperateState 为失败
                        operateNode.setNodeOperateState(NodeState.OPERATE_FAILED);
                        nodeDao.updateNode(operateNode.toVo(new NodeBasicInfoVo()));
                    }
                }

            }
        }

    }


    @Override
    public NodeBasicInfoVo queryNodeInfoByIP(String ip) {
        NodeBasicInfoPo po = nodeDao.queryNodeInfoByIP(ip);
        return po != null ? setNodeStateInfo(po.toVo(new NodeBasicInfoVo())) : null;
    }

    private static final Integer INIT_APISERVER_STEP_ORDER = 8;

    @Override
    public List<NodeBasicInfoVo> queryOtherApiServerInitDoneMasterNodes(Long clusterId, Long nodeId) {

        return nodeDao.queryMasterNodesByClusterId(clusterId).stream().map(node -> node.toVo(new NodeBasicInfoVo())).filter(node -> {
            boolean isNotCurrentNode = !node.getNodeId().equals(nodeId);
            boolean apiServerInitDone = ResourceConstant.NODE_INIT_STEP_SUCCESSFUL.equals(nodeInitializeStepDao.getNodeInitializeStep(node.getNodeId(), INIT_APISERVER_STEP_ORDER).getStepState());
            return isNotCurrentNode && apiServerInitDone;
        }).collect(Collectors.toList());
    }

    @Override
    public List<NodeBasicInfoVo> queryInitDoneButNotJoinedDeployNodes(Long clusterId) {
        return queryDeployNodesByClusterId(clusterId).stream().filter(n ->
                Objects.equals(n.getNodeJoined(), ResourceConstant.NODE_NOT_JOINED)
                        && Objects.equals(n.getNodeInitState(), NodeState.INIT_DONE)
                        && NodeUtils.checkNodeIsNotBeenOperating(n).isSuccess()).collect(Collectors.toList());
    }


}
