package com.yinhai.cloud.module.resource.task;

import com.yinhai.cloud.core.api.exec.ShellUid;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.KubeletCommandFactory;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.handler.AppendLogToOperateRecordHandler;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.OperateRunningServer;
import com.yinhai.cloud.module.resource.task.api.AbstractOperateTask;
import com.yinhai.cloud.module.resource.task.api.OperateCode;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.cloud.module.shell.util.ShellExecUtil;
import com.yinhai.cloud.module.shell.vo.ExecShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * @author: zhaokai
 * @create: 2018-08-28 15:54:20
 */
public class NodeDeleteTask extends AbstractOperateTask {
    private final static Logger logger = LoggerFactory.getLogger(NodeDeleteTask.class);
    private AppendLogToOperateRecordHandler operateLogHandler;

    public NodeDeleteTask(Long nodeId) {
        this.nodeId = nodeId;
        this.operateCode = OperateCode.NODE_OPERATE_CODE_DELETE;

    }

    @Override
    public boolean beforeExecute() {

        NodeBasicInfoVo updateNode = new NodeBasicInfoVo();
        updateNode.setNodeId(nodeId);
        updateNode.setNodeOperateState(NodeState.OPERATE_DELETING);
        nodeBpo.updateNodeExternalInfo(updateNode);

        Long operateId = nodeBpo.updateNodeExternalInfoAndCreateOperate(updateNode, operateCode, new OperateRunningServer(localMachineIpList, serverPort));
        this.operateId = operateId;
        operateLogHandler = new AppendLogToOperateRecordHandler(operateId);
        if (!Objects.equals(node.getNodeInitState(), NodeState.INIT_DONE)) {
            nodeBpo.deleteNode(node);
            return false;
        }
        return true;
    }

    /**
     * 删除节点时，先删除集群资源总量 edit by tianhy 2018.11.19
     */
    @Override
    public void successfullyExecuted() {
        super.successfullyExecuted();
        ClusterVo clusterVo = clusterBpo.getClusterById(node.getClusterId());
        clusterVo.setTotalCpu(clusterVo.getTotalCpu() - node.getNodeCpuAmount());

        //总资源-预留资源 add by dwkang
        clusterVo.setTotalAllocatableCpu(clusterVo.getTotalAllocatableCpu() - node.getNodeAllocatableCpu());

        clusterVo.setTotalCpuAvailable(clusterVo.getTotalCpuAvailable() - node.getNodeAllocatableCpu());
        clusterVo.setTotalMemory(clusterVo.getTotalMemory() - node.getNodeMemMb());

        //总资源-预留资源 add by dwkang
        clusterVo.setTotalAllocatableMemory(clusterVo.getTotalAllocatableMemory() - node.getNodeAllocatableMemory());

        clusterVo.setTotalMemAvailable(clusterVo.getTotalMemAvailable() - node.getNodeAllocatableMemory());
        clusterBpo.updateClusterById(clusterVo);
        nodeBpo.deleteNode(node);

    }

    @Override
    public void onExecute() throws Exception {

        if (Objects.equals(node.getNodeType(), ResourceConstant.NODE_TYPE_MASTER)) {
            nodeOperateRecordBpo.updateNodeOperatePercentage(operateId, 10);
            // 其他已经初始化完成的Master节点
            List<NodeBasicInfoVo> otherInitDoneMasterNodes = nodeBpo.queryOtherApiServerInitDoneMasterNodes(node.getClusterId(), node.getNodeId());
            nodeOperateRecordBpo.appendOperateLog(operateId, "删除ETCD服务，成员");
            if (otherInitDoneMasterNodes.isEmpty()) {
                // 直接删除当前节点ETCD即可，不用执行etcd命令，删除member
                ShellExecUtil.exec(operateLogHandler, null, currConn, new ExecShell(ShellUid.DELETE_ETCD_NODE, true).appendParam(node.getNodeIP()));
            } else {

                NodeBasicInfoVo anotherMaster = otherInitDoneMasterNodes.get(0);
                ShellExecUtil.exec(operateLogHandler, null, currConn, new ExecShell(ShellUid.DELETE_ETCD_NODE, true)
                        .appendParam(node.getNodeIP())
                        .appendParam(anotherMaster.getNodeIP()));
            }
            // 移除Haproxy 和Keepalived
            nodeOperateRecordBpo.appendOperateLog(operateId, "移除Haproxy 和Keepalived");
            ConsoleCommand removeHaproxyAndKeepalived = new ConsoleCommand();
            removeHaproxyAndKeepalived.appendCommand("yum remove -y haproxy keepalived");
            CommonSshExecUtil.exec(operateLogHandler, null, NodeUtils.createConnFromNode(node), removeHaproxyAndKeepalived);

        }

        nodeOperateRecordBpo.updateNodeOperatePercentage(operateId, 50);
        NodeBasicInfoVo availableAnotherMasterNode = nodeBpo.queryMasterNodesByClusterId(node.getClusterId())
                .stream().filter(n -> Objects.equals(n.getNodeInitState(), NodeState.INIT_DONE)
                        && !Objects.equals(n.getNodeId(), node.getNodeId())
                        && Objects.equals(n.getNodeRunningState(), NodeState.RUNNING_SUCCESS))
                .findAny().orElse(null);

        if (availableAnotherMasterNode == null) {
            // 只有一个Master节点，且Master节点为当前节点
            nodeOperateRecordBpo.appendOperateLog(operateId, "只有一个Master节点，且Master节点为当前节点,强制移除节点");
            CommonSshExecUtil.exec(operateLogHandler, null, NodeUtils.createConnFromNode(node),
                    KubeletCommandFactory.forceRemoveEtcdData(),
                    KubeletCommandFactory.forceRemoveDockerAndK8s());
            nodeOperateRecordBpo.updateNodeOperatePercentage(operateId, 70);
            //删除集群所有的ceph存储
        } else {
            nodeOperateRecordBpo.updateNodeOperatePercentage(operateId, 70);
            ConsoleCommand kubeletDeleteNode = KubeletCommandFactory.deleteKubeletNode(node.getNodeName());
            ConsoleCommand kubeadmResetNode = KubeletCommandFactory.resetNode();
            nodeOperateRecordBpo.appendOperateLog(operateId, "执行节点重置");
            CommonSshExecUtil.exec(operateLogHandler, null, NodeUtils.createConnFromNode(availableAnotherMasterNode), kubeletDeleteNode);
            CommonSshExecUtil.exec(operateLogHandler, null, currConn, kubeadmResetNode);
        }

    }


}
