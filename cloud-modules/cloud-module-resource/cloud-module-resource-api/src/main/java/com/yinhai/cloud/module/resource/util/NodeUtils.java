package com.yinhai.cloud.module.resource.util;

import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadContentCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.kubernetes.lstio.IstioIngressGateway;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeInitStepBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.CephNodeInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.pv.api.vo.StorageVo;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.common.api.util.ValidateUtil;

/**
 * @author jianglw
 */
public class NodeUtils {
    private static INodeInitStepBpo nodeInitStepBpo = ServiceLocator.getService(INodeInitStepBpo.class);
    private static IClusterBpo clusterBpo = ServiceLocator.getService(IClusterBpo.class);

    public static ConnVo createConnFromNode(NodeBasicInfoVo node) {
        return new ConnVo(node.getNodeIP(), node.getNodeSSHPort(), node.getNodeUser(), node.getNodePassword());
    }
    public static ConnVo createConnFromNode(CephNodeInfoVo node) {
        return new ConnVo(node.getNodeIP(), node.getNodeSSHPort(), node.getNodeUser(), node.getNodePassword());
    }

    public static ConnVo createDockerRepoServerConn() {
        NodeBasicInfoVo repoNode = new NodeBasicInfoVo();
        repoNode.setNodeSSHPort(Integer.parseInt(SystemConfigUtil.getSystemConfigValue("docker.private.repo.server.ssh.port")));
        repoNode.setNodeIP(SystemConfigUtil.getSystemConfigValue("docker.private.repo.ip"));
        repoNode.setNodeUser(SystemConfigUtil.getSystemConfigValue("docker.private.repo.server.ssh.username"));
        repoNode.setNodePassword(SystemConfigUtil.getSystemConfigValue("docker.private.repo.server.ssh.password"));
        return createConnFromNode(repoNode);

    }


    public static ConnVo createUploadImageServerConn() {
        NodeBasicInfoVo repoNode = new NodeBasicInfoVo();
        repoNode.setNodeSSHPort(Integer.parseInt(SystemConfigUtil.getSystemConfigValue("upload.port")));
        repoNode.setNodeIP(SystemConfigUtil.getSystemConfigValue("upload.ip"));
        repoNode.setNodeUser(SystemConfigUtil.getSystemConfigValue("upload.username"));
        repoNode.setNodePassword(SystemConfigUtil.getSystemConfigValue("upload.password"));
        return createConnFromNode(repoNode);

    }


    /**
     * 检查节点是否正在被操作
     *
     * @param vo 节点信息
     * @return CommonResultVo->success 如果为true 表示没有正在被操作 false表示正在被操作
     */
    public static CommonResultVo checkNodeIsNotBeenOperating(NodeBasicInfoVo vo) {
        final CommonResultVo result = new CommonResultVo();
        if (NodeState.INIT_RUNNING.equals(vo.getNodeInitState())) {
            result.setSuccess(false);
            result.setErrorMsg("节点正在初始化...");
            return result;
        }

        CommonResultVo checkStepResult = checkNodeDoNotHasRunningInitStep(vo.getNodeId());
        if (!checkStepResult.isSuccess()) {
            return checkStepResult;
        }

        if (NodeState.OPERATE_RUNNING_INIT.equals(vo.getNodeOperateState())) {
            result.setSuccess(false);
            result.setErrorMsg("节点正在初始化...");
            return result;
        }
        if (NodeState.OPERATE_DELETING.equals(vo.getNodeOperateState())) {
            result.setSuccess(false);
            result.setErrorMsg("节点正在删除...");
            return result;
        }
        if (NodeState.OPERATE_STARTING.equals(vo.getNodeOperateState())) {
            result.setSuccess(false);
            result.setErrorMsg("节点正在启动...");
            return result;
        }
        if (NodeState.OPERATE_STOPPING.equals(vo.getNodeOperateState())) {
            result.setSuccess(false);
            result.setErrorMsg("节点正在停止...");
            return result;
        }


        result.setSuccess(true);
        return result;
    }

    public static CommonResultVo checkNodeDoNotHasRunningInitStep(Long nodeId) {
        final CommonResultVo result = new CommonResultVo();
        long runningStepCount = nodeInitStepBpo.getNodeInitializeStepList(nodeId).stream().filter(step -> ResourceConstant.NODE_INIT_STEP_RUNNING.equals(step.getStepState())).count();
        if (runningStepCount > 0) {
            result.setSuccess(false);
            result.setErrorMsg("节点正在初始化...");
            return result;
        }
        result.setSuccess(true);
        return result;

    }

    public static ConnVo createConnFromStotageVo(StorageVo storage) {
        return new ConnVo(storage.getHostIp(), storage.getHostPort(), storage.getHostUserName(), storage.getHostPassword());
    }


    public static CommonResultVo checkClusterResourcesEnough(NodeBasicInfoVo nodeVo) {
        CommonResultVo result = new CommonResultVo();
        ClusterVo clusterVo = clusterBpo.getClusterById(nodeVo.getClusterId());
        //运行中的部署节点 才判定
        if (ValidateUtil.areEqual(nodeVo.getNodeInitState(), ResourceConstant.NODE_INIT_STATE_SUCCESS)) {
            if (ResourceConstant.NODE_TYPE_DEPLOY.equals(nodeVo.getNodeType()) || ResourceConstant.MASTER_ALSO_AS_CHILD.equals(nodeVo.getNodeAsChild())) {
                if (clusterVo.getTotalCpuAvailable() < nodeVo.getNodeAllocatableCpu()) {
                    result.setSuccess(false);
                    result.setErrorMsg("集群可用CPU小于节点CPU数量，不能删除该节点!");
                    return result;
                }
                if (clusterVo.getTotalMemAvailable() < nodeVo.getNodeAllocatableMemory()) {
                    result.setSuccess(false);
                    result.setErrorMsg("集群可用内存小于节点内存大小，不能删除该节点!");
                    return result;
                }
            }
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 给集群安装istio-ingressgateway的service
     * @param vo
     * @throws Exception
     */
    public static void createIstioIngressGatewayService(NodeBasicInfoVo vo) throws Exception{
        ConnVo connVo = NodeUtils.createConnFromNode(vo);
        String servicePath = SystemConfigUtil.getSystemConfigValue("istio.service.path");
        IstioIngressGateway istioIngressGateway = new IstioIngressGateway(vo);
        AbstractCommand uploadYaml = new UploadContentCommand(servicePath, istioIngressGateway.buildYaml());
        ConsoleCommand startCommand = new ConsoleCommand();
        startCommand.appendCommand("source ~/.bash_profile && kubectl apply -f " + servicePath);
        CommonSshExecUtil.exec(connVo, uploadYaml, startCommand);
    }
}
