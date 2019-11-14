package com.yinhai.cloud.module.resource.task;

import com.yinhai.cloud.core.api.exception.NodeInitRuntimeException;
import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.common.helper.IK8sInstallHelper;
import com.yinhai.cloud.module.resource.common.helper.K8sBinaryInstallHelper;
import com.yinhai.cloud.module.resource.common.helper.K8sYumInstallHelper;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInitializeStepVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.OperateRunningServer;
import com.yinhai.cloud.module.resource.task.api.AbstractOperateTask;
import com.yinhai.cloud.module.resource.task.api.OperateCode;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * @author: zhaokai
 * @create: 2018-08-28 15:54:20
 */
public class NodeInitTask extends AbstractOperateTask {
    private final static Logger logger = LoggerFactory.getLogger(NodeInitTask.class);
    private boolean retry;
    private Integer startStepOrder = 0;

    public NodeInitTask(Long nodeId, boolean retry) {
        this.nodeId = nodeId;
        this.retry = retry;
        this.operateCode = OperateCode.NODE_OPERATE_CODE_INIT;

    }

    @Override
    public boolean beforeExecute() {

        long failedStepCount = nodeInitStepBpo.getNodeInitializeStepList(node.getNodeId()).stream().filter(s -> Objects.equals(s.getStepState(), ResourceConstant.NODE_INIT_STEP_FAILED)).count();
        if (failedStepCount == 0 && retry) {
            // 没有失败的步骤，但是点击了重试，不继续执行初始化
            return false;
        }
        if (retry && failedStepCount != 0) {
            // 计算初始化起始步骤
            startStepOrder = nodeInitStepBpo.getNodeInitializeStepList(node.getNodeId()).stream().filter(s -> Objects.equals(s.getStepState(), ResourceConstant.NODE_INIT_STEP_FAILED)).mapToInt(NodeInitializeStepVo::getStepOrder).min().getAsInt();
        }
        NodeBasicInfoVo updateNode = new NodeBasicInfoVo();
        // 设置节点正在执行初始化
        updateNode.setNodeId(nodeId);
        updateNode.setNodeOperateState(NodeState.OPERATE_RUNNING_INIT);
        updateNode.setNodeInitState(NodeState.INIT_RUNNING);
        updateNode.setNodeRunningState(NodeState.RUNNING_NOT_NOW);
        this.operateId = nodeBpo.updateNodeExternalInfoAndCreateOperate(updateNode, operateCode, new OperateRunningServer(localMachineIpList, serverPort));
        nodeInitStepBpo.cleanStepInfo(nodeId, startStepOrder);

        return true;
    }

    @Override
    public void onExecute() throws Exception {
        String k8sInstallType = SystemConfigUtil.getSystemConfigValue("k8s.install.type");
        IK8sInstallHelper helper;
        if(ValidateUtil.areEqual(IK8sInstallHelper.KUBERNETES_INSTALL_TYPE_YUM, k8sInstallType)){
            helper = new K8sYumInstallHelper(nodeId, operateId);
        }else if(ValidateUtil.areEqual(IK8sInstallHelper.KUBERNETES_INSTALL_TYPE_BINARY, k8sInstallType)) {
            helper = new K8sBinaryInstallHelper(nodeId, operateId);
        }else {
            throw new NodeInitRuntimeException("不支持的k8s安装方式!");
        }
        if (retry) {
            helper.installSegmentStart(startStepOrder);
        } else {
            helper.installFullyStart();
        }

    }

    /**
     * 节点新增成功后添加集群资源总量 add by tianhy 2018.11.19
     */
    @Override
    public void successfullyExecuted() {
        super.successfullyExecuted();
        if (ValidateUtil.areEqual(node.getNodeType(), ResourceConstant.NODE_TYPE_DEPLOY) || ValidateUtil.areEqual(node.getNodeAsChild(), ResourceConstant.MASTER_ALSO_AS_CHILD)) {
            //管理节点初始化并且启动后才可以获取K8s节点预留资源
            //从管理节点获取负载节点资源信息后保存
            NodeBasicInfoVo masterNodeInfoVo = nodeBpo.queryMasterNodesByClusterId(node.getClusterId()).stream().findAny().orElse(new NodeBasicInfoVo());
            NodeInfoVo k8sNodeInfo = ServerUtils.getK8sNodeInfo(masterNodeInfoVo, node.getNodeName());
            NodeBasicInfoVo updateNode = new NodeBasicInfoVo();
            updateNode.setNodeId(node.getNodeId());
            DecimalFormat df = new DecimalFormat("#.00");
            updateNode.setNodeAllocatableMemory(Double.valueOf(df.format(k8sNodeInfo.getNodeAllocatableMemory())));
            updateNode.setNodeAllocatableCpu(k8sNodeInfo.getNodeAllocatableCpu());
            nodeBpo.updateNodeExternalInfo(updateNode);

            ClusterVo clusterVo = clusterBpo.getClusterById(node.getClusterId());
            Integer totalCpu = ValidateUtil.isEmpty(clusterVo.getTotalCpu()) ? 0 : clusterVo.getTotalCpu();
            clusterVo.setTotalCpu(totalCpu + node.getNodeCpuAmount());
            Double totalAllocatableCpu = ValidateUtil.isEmpty(clusterVo.getTotalAllocatableCpu()) ? 0 : clusterVo.getTotalAllocatableCpu();
            clusterVo.setTotalAllocatableCpu(totalAllocatableCpu + updateNode.getNodeAllocatableCpu());
            //总资源-预留资源 add by dwkang
            Double totalCpuAvailable = ValidateUtil.isEmpty(clusterVo.getTotalCpuAvailable()) ? 0 : clusterVo.getTotalCpuAvailable();
            clusterVo.setTotalCpuAvailable(totalCpuAvailable + updateNode.getNodeAllocatableCpu());

            Double totalMemory = ValidateUtil.isEmpty(clusterVo.getTotalMemory()) ? 0 : clusterVo.getTotalMemory();
            clusterVo.setTotalMemory(totalMemory + node.getNodeMemMb());

            //总资源-预留资源 add by dwkang
            Double totalAllocatableMemory = ValidateUtil.isEmpty(clusterVo.getTotalAllocatableMemory()) ? 0 : clusterVo.getTotalAllocatableMemory();
            clusterVo.setTotalAllocatableMemory(totalAllocatableMemory + updateNode.getNodeAllocatableMemory());

            Double totalMemoryAvailable = ValidateUtil.isEmpty(clusterVo.getTotalMemAvailable()) ? 0 : clusterVo.getTotalMemAvailable();
            clusterVo.setTotalMemAvailable(totalMemoryAvailable + updateNode.getNodeAllocatableMemory());

            clusterBpo.updateClusterById(clusterVo);

        }
    }

    @Override
    public void executeFailed(Exception e) {
        super.executeFailed(e);
        String msg = CommonUtil.getExceptionMsgContent(e);
        nodeInitStepBpo.nodeInitFailed(nodeId, operateId, msg);
        logger.error("K8s 安装出错 Exception:" + msg);
    }
}
