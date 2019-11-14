package com.yinhai.cloud.module.resource.task;

import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.KubeletCommandFactory;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.OperateRunningServer;
import com.yinhai.cloud.module.resource.task.api.AbstractOperateTask;
import com.yinhai.cloud.module.resource.task.api.OperateCode;

/**
 * @author: zhaokai
 * @create: 2018-08-28 15:54:20
 */
public class NodeStopTask extends AbstractOperateTask {
    public NodeStopTask(Long nodeId) {
        this.nodeId = nodeId;
        this.operateCode = OperateCode.NODE_OPERATE_CODE_STOP;

    }

    @Override
    public boolean beforeExecute() {
        NodeBasicInfoVo updateNode = new NodeBasicInfoVo();
        updateNode.setNodeId(nodeId);
        updateNode.setNodeOperateState(NodeState.OPERATE_STOPPING);
        this.operateId = nodeBpo.updateNodeExternalInfoAndCreateOperate(updateNode, operateCode, new OperateRunningServer(localMachineIpList, serverPort));
        return true;
    }


    @Override
    public void onExecute() throws Exception {
        ConsoleCommand cmd = KubeletCommandFactory.stopKubeletCommand();
        nodeOperateRecordBpo.appendOperateLog(operateId, "停止kubelet服务中");
        CommonSshExecUtil.exec(currConn, cmd);
        nodeOperateRecordBpo.appendOperateLog(operateId, "已成功停止kubelet服务");
    }

    @Override
    public void successfullyExecuted() {
        super.successfullyExecuted();
        NodeBasicInfoVo updateNode = new NodeBasicInfoVo();
        updateNode.setNodeId(nodeId);
        updateNode.setNodeRunningState(NodeState.RUNNING_NOT_NOW);
        updateNode.setNodeOperateState(NodeState.OPERATE_STOPPED);

        nodeBpo.updateNodeExternalInfo(updateNode);
        clusterBpo.updateClusterMemCpu(node.getClusterId(), node.getNodeAllocatableMemory(), node.getNodeAllocatableCpu(), false);
    }
}
