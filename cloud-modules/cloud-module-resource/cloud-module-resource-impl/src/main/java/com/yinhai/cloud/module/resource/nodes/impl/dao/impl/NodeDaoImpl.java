package com.yinhai.cloud.module.resource.nodes.impl.dao.impl;

import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeBasicInfoPo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jianglw
 */
public class NodeDaoImpl extends HibernateDAO<NodeBasicInfoPo> implements INodeDao {

    @Override
    public List<NodeBasicInfoPo> queryAllNodes() {
        final String hql = "from NodeBasicInfoPo";
        return super.find(hql);
    }

    @Override
    public List<NodeBasicInfoPo> queryNodesByClusterId(final Long clusterId) {
        final String hql = "from NodeBasicInfoPo  where clusterId=? order by nodeType, nodeIP";
        return super.find(hql, clusterId);
    }

    @Override
    public List<NodeBasicInfoPo> queryRunningNodesByClusterId(Long clusterId) {
        final String hql = "from NodeBasicInfoPo nodes where  nodes.clusterId=? and nodes.nodeInitState=? and nodes.nodeRunningState=?";
        return super.find(hql,clusterId, NodeState.INIT_DONE, NodeState.RUNNING_SUCCESS);
    }

    @Override
    public List<NodeBasicInfoPo> queryMasterNodesByClusterId(Long clusterId) {
        final String hql = "from NodeBasicInfoPo nodes where nodes.nodeType= ? and nodes.clusterId=? order by nodeCreateDate";
        return super.find(hql, ResourceConstant.NODE_TYPE_MASTER, clusterId);
    }

    @Override
    public NodeBasicInfoPo queryNodeById(Long nodeId) {
        NodeBasicInfoPo po = get(nodeId);
        return po;
    }

    @Override
    public List<NodeBasicInfoPo> queryDeployNodesByClusterId(Long clusterId) {
        final String hql = "from NodeBasicInfoPo nodes where nodes.nodeType= ? and  nodes.clusterId=?";
        return super.find(hql, ResourceConstant.NODE_TYPE_DEPLOY, clusterId);
    }

    @Override
    public NodeBasicInfoPo queryNodeInfoByIP(String ip) {
        final String hql = "from NodeBasicInfoPo nodes where nodes.nodeIP= ?";
        List<NodeBasicInfoPo> list = super.find(hql, ip);
        if (list == null || list.isEmpty()) {
            return null;
        }
        NodeBasicInfoPo po = list.get(0);
        return po;
    }

    @Override
    public NodeBasicInfoVo saveNode(final NodeBasicInfoVo vo) {
        final NodeBasicInfoPo po = new NodeBasicInfoPo();
        super.save(vo.toPO(po));
        NodeBasicInfoVo saveVo = po.toVo(new NodeBasicInfoVo());
        return saveVo;
    }

    @Override
    public void updateClusterNodesAsDeleting(Long clusterId) {
        update("update NodeBasicInfoPo set nodeOperateState= ? where clusterId = ?", NodeState.OPERATE_DELETING, clusterId);
    }

    @Override
    public boolean checkClusterHasOperatingNode(Long clusterId) {

        return getCount("from NodeBasicInfoPo " +
                        "where ( nodeOperateState = ? " +
                        " or nodeOperateState = ?  " +
                        "or nodeOperateState = ? " +
                        " or nodeOperateState = ? ) and clusterId = ?", null,
                NodeState.OPERATE_RUNNING_INIT,
                NodeState.OPERATE_DELETING,
                NodeState.OPERATE_STARTING,
                NodeState.OPERATE_STOPPING,
                clusterId) > 0;
    }

    @Override
    public Long countClusterNotRunningNode(Long clusterId) {
        return getCount("from NodeBasicInfoPo where nodeRunningState != ? and clusterId = ?", null, NodeState.RUNNING_SUCCESS, clusterId);
    }

    @Override
    public List<NodeBasicInfoVo> queryDeployNodesCommunicateWithMaster(Long nodeId) {
        return find("from NodeBasicInfoPo where nodeJoinMaster = ?", String.valueOf(nodeId)).stream().map(n -> {
            NodeBasicInfoVo v = n.toVo(new NodeBasicInfoVo());
            return v;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean updateNode(final NodeBasicInfoVo vo) {
        final NodeBasicInfoPo persistentNode = get(vo.getNodeId());
        if (vo.getNodeName() != null) {
            persistentNode.setNodeName(vo.getNodeName());
        }

        if (vo.getNodeAsChild() != null) {
            persistentNode.setNodeAsChild(vo.getNodeAsChild());
        }

        if (vo.getNodeUser() != null) {
            persistentNode.setNodeUser(vo.getNodeUser());
        }

        if (vo.getNodePassword() != null) {
            persistentNode.setNodePassword(vo.getNodePassword());
        }

        if (vo.getNodeRunningState() != null) {
            persistentNode.setNodeRunningState(vo.getNodeRunningState());
        }
        if (vo.getNodeOperateState() != null) {
            persistentNode.setNodeOperateState(vo.getNodeOperateState());
        }

        if (vo.getNodeInitState() != null) {
            persistentNode.setNodeInitState(vo.getNodeInitState());
        }

        if (vo.getNodeJoined() != null) {
            persistentNode.setNodeJoined(vo.getNodeJoined());
        }

        if (vo.getNodeCpuAmount() != null) {
            persistentNode.setNodeCpuAmount(vo.getNodeCpuAmount());
        }
        if (vo.getNodeMemMb() != null) {
            persistentNode.setNodeMemMb(vo.getNodeMemMb());
        }
        if (vo.getNodeDiskTotalMb() != null) {
            persistentNode.setNodeDiskTotalMb(vo.getNodeDiskTotalMb());
        }
        if (vo.getNodeDiskUsedMb() != null) {
            persistentNode.setNodeDiskUsedMb(vo.getNodeDiskUsedMb());
        }
        if (vo.getNodeDiskUsableMb() != null) {
            persistentNode.setNodeDiskUsableMb(vo.getNodeDiskUsableMb());
        }

        if (vo.getNodeSSHPort() != null) {
            persistentNode.setNodeSSHPort(vo.getNodeSSHPort());
        }
        if (vo.getNodeMemAvailable() != null) {
            persistentNode.setNodeMemAvailable(vo.getNodeMemAvailable());
        }
        if (vo.getNodeAllocatableCpu() != null) {
            persistentNode.setNodeAllocatableCpu(vo.getNodeAllocatableCpu());
        }
        if (vo.getNodeAllocatableMemory() != null) {
//            persistentNode.setNodeAllocatableMemory(vo.getNodeAllocatableMemory());
            DecimalFormat df = new DecimalFormat("#.00");
            persistentNode.setNodeAllocatableMemory(Double.valueOf(df.format(vo.getNodeAllocatableMemory())));
        }
        update(persistentNode);
        return true;
    }

    @Override
    public void deleteNode(final NodeBasicInfoVo vo) {
        super.delete("delete from NodeBasicInfoPo where nodeId = ?", vo.getNodeId());
    }

    @Override
    public boolean checkNodeNameRepeat(final String nodeName) {
        return getCount("from NodeBasicInfoPo where nodeName = ?", null, nodeName) >= 1;
    }

    @Override
    public boolean checkNodeIPRepeat(final String nodeIP) {
        return getCount("from NodeBasicInfoPo where nodeIP = ?", null, nodeIP) >= 1;
    }

    @Override
    public List<NodeBasicInfoPo> queryRunningMasterNodesByClusterId(Long cluster_id) {
        final String hql = "from NodeBasicInfoPo nodes where nodes.nodeType= ? and nodes.clusterId=? and nodes.nodeInitState=? and nodes.nodeRunningState=?";
        return super.find(hql, ResourceConstant.NODE_TYPE_MASTER, cluster_id, NodeState.INIT_DONE, NodeState.RUNNING_SUCCESS);
    }
}
