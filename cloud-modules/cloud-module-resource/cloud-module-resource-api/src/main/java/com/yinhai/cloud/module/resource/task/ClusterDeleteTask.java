package com.yinhai.cloud.module.resource.task;

import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.core.api.vo.KubeletCommandFactory;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.OperateRunningServer;
import com.yinhai.cloud.module.resource.task.api.AbstractOperateTask;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-08-28 17:23:41
 */
public class ClusterDeleteTask extends AbstractOperateTask {
    private final static Logger logger = LoggerFactory.getLogger(ClusterDeleteTask.class);
    private Long clusterId;

    public ClusterDeleteTask(Long clusterId) {
        this.clusterId = clusterId;

    }

    @Override
    public boolean beforeExecute() {
        OperateRunningServer operateRunningServer = new OperateRunningServer(localMachineIpList, serverPort);
        clusterBpo.updateClusterAsDeleting(clusterId, operateRunningServer);
        return true;
    }

    @Override
    public boolean needCheck() {
        return false;
    }

    @Override
    public void executeFailed(Exception e) {
        return;
    }

    @Override
    public void onExecute() throws Exception {
        List<NodeBasicInfoVo> nodeList = nodeBpo.queryNodesBasicInfoByClusterId(clusterId);
        for (NodeBasicInfoVo n : nodeList) {
            forceDeleteNode(n);
            nodeBpo.deleteNode(n);
        }
    }

    @Override
    public void successfullyExecuted() {
        clusterBpo.deleteClusterById(clusterId);
    }

    /**
     * 强制删除节点，yum remove
     */
    public void forceDeleteNode(NodeBasicInfoVo node) {
        ConsoleCommand removeEtcdCmd = KubeletCommandFactory.forceRemoveEtcdData();
        ConsoleCommand removeDockerAndK8sCmd = KubeletCommandFactory.forceRemoveDockerAndK8s();
        ConsoleCommand stopHA = KubeletCommandFactory.forceStopHA();
        ConsoleCommand stopKeeplived = KubeletCommandFactory.forceStopKeepalived();
        ConnVo conn = NodeUtils.createConnFromNode(node);
        try {
            if (ResourceConstant.NODE_TYPE_MASTER.equals(node.getNodeType())) {
                CommonSshExecUtil.exec(conn, removeEtcdCmd, removeDockerAndK8sCmd, stopHA, stopKeeplived);
            } else {
                CommonSshExecUtil.exec(conn, removeDockerAndK8sCmd);
            }
        } catch (SSHExecuteException | SSHConnectionException e) {

            logger.warn("删除节点" + node.getNodeName() + "执行命令失败(" + e.getMessage() + ")，需要手动清理");
        }

    }


}
