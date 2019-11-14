package com.yinhai.cloud.module.resource.nodes.app.controller;

import com.yinhai.cloud.core.api.util.DataEncrypt;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.util.UserUtils;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.INamespaceBpo;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeInitStepBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeOperateRecordBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.*;
import com.yinhai.cloud.module.resource.task.*;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * 节点管理
 *
 * @author jianglw
 */
@Controller
@RequestMapping("/node")
public class NodeManageController extends BaseController {
    @Resource
    private INodeBpo nodeBpo;

    @Resource
    private IClusterBpo clusterBpo;

    @Resource
    private INodeInitStepBpo nodeInitStepBpo;
    @Resource
    private INodeOperateRecordBpo nodeOperateRecordBpo;

    @Resource
    private INamespaceBpo namespaceBpo;

    /**
     * 保存主机基本信息
     * 1.保存主机基本信息
     * 2.执行节点初始化脚本等操作：调用
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/save.do", method = RequestMethod.POST)
    @ResponseBody
    public CommonResultVo saveNode(final @RequestBody NodeBasicInfoVo vo, ServletRequest request) {
        final CommonResultVo result = new CommonResultVo();
        // 从缓存中获取节点的资源信息
        NodeInfoVo nodeResourceInfo = ServerUtils.getNodeInfo(vo, false);
        if(Integer.parseInt(nodeResourceInfo.getNodeCpuInfo()) < vo.getNodeAllocatableCpu()){
            result.setErrorMsg("输入CPU大于节点最大CPU");
            result.setSuccess(false);
            return result;
        }
        if(Double.valueOf(nodeResourceInfo.getNodeMemoryInfo()) < vo.getNodeAllocatableMemory()){
            result.setErrorMsg("输入内存大于节点最大内存");
            result.setSuccess(false);
            return result;
        }
        String clusterHaVirtualIP = clusterBpo.getClusterById(vo.getClusterId()).getClusterHaVirtualIP();
        List<NodeBasicInfoVo> otherInitDoneMasterNodes = nodeBpo.queryOtherApiServerInitDoneMasterNodes(vo.getClusterId(), vo.getNodeId());
        if (ValidateUtil.isEmpty(clusterHaVirtualIP) && ValidateUtil.isNotEmpty(otherInitDoneMasterNodes) && ResourceConstant.NODE_TYPE_MASTER.equals(vo.getNodeType())){
            result.setErrorMsg("非HA集群只可存在一个master节点");
            result.setSuccess(false);
            return result;
        }
        String masterMem = SystemConfigUtil.getSystemConfigValue("kube.master.mem");
        String deployMem = SystemConfigUtil.getSystemConfigValue("kube.deploy.mem");
        String masterCpu = SystemConfigUtil.getSystemConfigValue("kube.master.cpu");
        String deployCpu = SystemConfigUtil.getSystemConfigValue("kube.deploy.cpu");
        boolean masterMemBlean = vo.getNodeAllocatableMemory() < Double.valueOf(masterMem) && ResourceConstant.NODE_TYPE_MASTER.equals(vo.getNodeType());
        boolean deployMemBlean = vo.getNodeAllocatableMemory() < Double.valueOf(deployMem) && ResourceConstant.NODE_TYPE_DEPLOY.equals(vo.getNodeType());
        boolean masterCpuBlean = vo.getNodeAllocatableCpu() < Double.valueOf(masterCpu) && ResourceConstant.NODE_TYPE_MASTER.equals(vo.getNodeType());
        boolean deployCpuBlean = vo.getNodeAllocatableCpu() < Double.valueOf(deployCpu) && ResourceConstant.NODE_TYPE_DEPLOY.equals(vo.getNodeType());
        if (masterMemBlean || deployMemBlean){
            result.setErrorMsg("输入内存要大于对k8s预留内存以保证k8s集群正常运行,管理节点需要：" + masterMem + "Gi，负载节点需要：" + deployMem + "Gi");
            result.setSuccess(false);
            return result;
        }
        if (masterCpuBlean || deployCpuBlean){
            result.setErrorMsg("输入CPU要大于对k8s预留CPU以保证k8s集群正常运行,管理节点需要：" + masterCpu + "，负载节点需要：" + deployCpu);
            result.setSuccess(false);
            return result;
        }
        ClusterVo cluster = clusterBpo.getClusterById(vo.getClusterId());
        if (cluster == null) {
            result.setSuccess(false);
            result.setErrorMsg("集群已经被删除，无法添加");
            result.setData(vo);
            return result;
        }
        if (ResourceConstant.CLUSTER_IS_BEEN_DELETING.equals(cluster.getDeleting())) {
            result.setSuccess(false);
            result.setErrorMsg("集群正在被删除，无法添加");
            result.setData(vo);
            return result;
        }
        CommonResultVo checkResult = checkNodeBasicInfo(vo);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        final String userId = UserUtils.getUserId(request);
        vo.setNodeCreateUser(userId);
        vo.setNodeCreateDate(new Date());
        // 节点状态初始设置
        vo.setNodeOperateState(NodeState.OPERATE_RUNNING_INIT);
        vo.setNodeInitState(NodeState.INIT_RUNNING);
        vo.setNodeRunningState(NodeState.RUNNING_NOT_NOW);
        vo.setNodeJoined(ResourceConstant.NODE_NOT_JOINED);
        DecimalFormat df = new DecimalFormat("#");
        if (nodeResourceInfo != null && nodeResourceInfo.getNodeStatus() == NodeState.GET_RESOURCE_SUCCESSFULLY) {
            vo.setNodeCpuAmount(Integer.parseInt(nodeResourceInfo.getNodeCpuInfo()));
            vo.setNodeSystemCpu(Integer.parseInt(nodeResourceInfo.getNodeCpuInfo()) - vo.getNodeAllocatableCpu());
            vo.setNodeMemAvailable(Double.parseDouble(nodeResourceInfo.getNodeFreeMemory()));
            vo.setNodeMemMb(Double.parseDouble(nodeResourceInfo.getNodeMemoryInfo()));
            vo.setNodeSysMemMb(Double.valueOf(df.format(Double.parseDouble(nodeResourceInfo.getNodeMemoryInfo()) - vo.getNodeAllocatableMemory())));
            vo.setNodeDiskTotalMb(Double.parseDouble(nodeResourceInfo.getNodeDiskInfoTotal()));
            vo.setNodeDiskUsableMb(Double.parseDouble(nodeResourceInfo.getNodeDiskInfoUsable()));
            vo.setNodeDiskUsedMb(Double.parseDouble(nodeResourceInfo.getNodeDiskInfoUsage()));
//            vo.setNodeAllocatableMemory(Double.parseDouble(nodeResourceInfo.getNodeMemoryInfo()));
//            vo.setNodeAllocatableCpu(Double.parseDouble(nodeResourceInfo.getNodeCpuInfo()));
        }
        vo.setNodePassword(DataEncrypt.encrypt(vo.getNodePassword()));

        final NodeBasicInfoVo savedNode = nodeBpo.saveNode(vo);

        if (savedNode == null) {
            result.setSuccess(false);
            result.setErrorMsg("在集群中已经存在同名的节点");
            result.setData(vo);
            return result;
        }
        TaskExecuteEngine.execute(new NodeInitTask(savedNode.getNodeId(), false));
        // 执行系统初始化
        result.setSuccess(true);
        return result;
    }


    @RequestMapping(value = "/setNodeAsChild.do")
    @ResponseBody
    public CommonResultVo setNodeAsChild(final @RequestBody NodeBasicInfoVo vo){
        CommonResultVo result = new CommonResultVo();
        if (ResourceConstant.NODE_TYPE_DEPLOY.equals(vo.getNodeType())){
            result.setSuccess(false);
            result.setErrorMsg("节点不是管理节点");
            return result;
        }
        result = NodeUtils.checkClusterResourcesEnough(vo);
        if (!result.isSuccess() && ResourceConstant.MASTER_ALSO_AS_CHILD.equals(vo.getNodeAsChild())) {
            return result;
        }
        try {
            ServerUtils.setNodeAsChild(vo);
            nodeBpo.setNodeAsChild(vo);
            //刷新集群资源
            clusterBpo.refreshResource(vo.getClusterId());
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }


    @RequestMapping(value = "/operateStack.do")
    @ResponseBody
    public CommonResultVo nodeOperateStack(final @RequestBody() PageQueryVo query) {
        final CommonResultVo result = new CommonResultVo();
        Long nodeId = query.getNodeId();
        if (ValidateUtil.isEmpty(nodeId)) {
            result.setSuccess(false);
            result.setErrorMsg("节点ID不能为空！");
            return result;
        }
        IPage<NodeOperateVo> list = nodeOperateRecordBpo.getNodeOperateStackWithPage(nodeId, query.getStart(), query.getLimit());
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/findResourceDetails.do")
    @ResponseBody
    public CommonResultVo findResourceDetails(final @RequestBody NodeBasicInfoVo vo) {
        final CommonResultVo result = new CommonResultVo();
        if ("0".equals(vo.getNodeAsChild()) && ResourceConstant.NODE_TYPE_MASTER.equals(vo.getNodeType())){
            result.setSuccess(false);
            result.setErrorMsg("该节点资源不计入集群资源");
            return result;
        }
        if (!NodeState.INIT_DONE.equals(vo.getNodeInitState())){
            result.setSuccess(false);
            result.setErrorMsg("该节点不是成功状态不能查看");
            return result;
        }
        NodeInfoVo nodeResourceInfo = ServerUtils.getNodeInfo(vo, false);
        ResourceDetailsVo rvo = new ResourceDetailsVo();
        Double sysCpu = vo.getNodeSystemCpu();
        Double nodeCpu = Double.valueOf(nodeResourceInfo.getNodeCpuInfo());
        Double kubeUseCpu = vo.getNodeAllocatableCpu();
        Double nodeMem = Double.valueOf(nodeResourceInfo.getNodeMemoryInfo());
        Double sysMem = vo.getNodeSysMemMb();
        Double kubeUseMem = vo.getNodeAllocatableMemory();
        DecimalFormat df = new DecimalFormat("#.00");
        Double kubeResMem = Double.valueOf(df.format(nodeMem - sysMem - kubeUseMem - 0.5));
        rvo.setSystemCpu(sysCpu);
        rvo.setSystemMem(sysMem);
        rvo.setNodeCpu(nodeCpu);
        rvo.setNodeMem(nodeMem);
        rvo.setKubeUseCpu(kubeUseCpu);
        rvo.setKubeResCpu(nodeCpu - sysCpu - kubeUseCpu);
        rvo.setEvictionHard(0.5);
        rvo.setKubeUseMem(kubeUseMem);
        rvo.setKubeResMem(kubeResMem);
        result.setData(rvo);
        result.setSuccess(true);
        return result;
    }


    @RequestMapping(value = "/reInitNode/fromFailed")
    @ResponseBody
    public CommonResultVo reInitNodeFromFailed(final @RequestBody NodeBasicInfoVo vo) {
        final CommonResultVo result = new CommonResultVo();
        Long nodeId = vo.getNodeId();
        if (ValidateUtil.isEmpty(nodeId)) {
            result.setSuccess(false);
            result.setErrorMsg("从失败步骤开始重新初始化的节点，ID不能为空！");
            result.setData(vo);
            return result;
        }
        NodeBasicInfoVo queryNodeInfo = nodeBpo.queryNodeInfoById(vo.getNodeId());
        if (queryNodeInfo == null) {
            result.setSuccess(false);
            result.setErrorMsg("已删除节点");
            return result;
        }
        CommonResultVo checkNodeResult = NodeUtils.checkNodeIsNotBeenOperating(queryNodeInfo);
        if (!checkNodeResult.isSuccess()) {
            return checkNodeResult;
        }
        TaskExecuteEngine.execute(new NodeInitTask(nodeId, true));
        TaskExecuteEngine.execute(new UpdateNodeResourceTask(nodeId));

        result.setSuccess(true);
        result.setData(nodeBpo.queryNodeInfoById(vo.getNodeId()));
        return result;
    }


    @RequestMapping(value = "/reInitNode/fully")
    @ResponseBody
    public CommonResultVo reInitNodeFully(final @RequestBody NodeBasicInfoVo vo) {

        final CommonResultVo result = new CommonResultVo();

        if (ValidateUtil.isEmpty(vo.getNodeId())) {
            result.setSuccess(false);
            result.setErrorMsg("需要重新初始化的节点，ID不能为空！");
            result.setData(vo);
            return result;
        }
        NodeBasicInfoVo queryNodeInfo = nodeBpo.queryNodeInfoById(vo.getNodeId());
        if (queryNodeInfo == null) {
            result.setSuccess(false);
            result.setErrorMsg("已删除节点");
            return result;
        }

        //最后一个管理节点初始化时判断命名空间，负载节点是否已删除
        //获取管理节点数量
        long masterNodesCount = (long) nodeBpo.queryMasterNodesByClusterId(queryNodeInfo.getClusterId()).size();
        if (masterNodesCount == 1 && queryNodeInfo.getNodeType().equals(ResourceConstant.NODE_TYPE_MASTER)) {
            //先判断负载节点是否已删除
            List<NodeBasicInfoVo> nodeBasicInfoVos = nodeBpo.queryDeployNodesByClusterId(queryNodeInfo.getClusterId());
            if (!ValidateUtil.isEmpty(nodeBasicInfoVos)) {
                result.setErrorMsg("请先删除负载节点");
                result.setSuccess(false);
                return result;
            }
            //再判断命名空间是否已删除
            List<NamespaceVo> namespaces = namespaceBpo.getNamespaces(queryNodeInfo.getClusterId());
            if (!ValidateUtil.isEmpty(namespaces)) {
                result.setErrorMsg("请先删除命名空间");
                result.setSuccess(false);
                return result;
            }
        }

        CommonResultVo checkNodeResult = NodeUtils.checkNodeIsNotBeenOperating(queryNodeInfo);
        if (!checkNodeResult.isSuccess()) {
            return checkNodeResult;
        }
        //强制初始化前 节点是运行状态，更新集群资源
        if (queryNodeInfo.getNodeRunningState() == NodeState.RUNNING_SUCCESS) {
            clusterBpo.updateClusterMemCpu(queryNodeInfo.getClusterId(), queryNodeInfo.getNodeAllocatableMemory(), queryNodeInfo.getNodeAllocatableCpu(), false);
        }
        // 异步更新节点节点资源信息
        TaskExecuteEngine.execute(new UpdateNodeResourceTask(vo.getNodeId()));
        // 异步初始化系统环境
        TaskExecuteEngine.execute(new NodeInitTask(vo.getNodeId(), false));

        // 执行系统初始化
        result.setSuccess(true);
        result.setData(nodeBpo.queryNodeInfoById(vo.getNodeId()));
        return result;
    }

    /**
     * 预先检查节点状态，获取节点信息
     *
     * @param vo
     * @return
     */
    @RequestMapping("/preCheck.do")
    @ResponseBody
    public CommonResultVo preCheck(@RequestBody NodeBasicInfoVo vo) {
        CommonResultVo result = new CommonResultVo();
        CommonResultVo checkResult = checkNodeBasicInfo(vo);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        if (nodeBpo.checkNodeInfoRepeat(vo) && vo.getNodeId() == null) {
            result.setSuccess(false);
            result.setErrorMsg("节点 名称/IP 已在使用！");
            return result;
        }
        if (vo.getNodeId() == null) {
            vo.setNodePassword(DataEncrypt.encrypt(vo.getNodePassword()));
        } else {
            NodeBasicInfoVo oldNode = nodeBpo.queryNodeInfoById(vo.getNodeId());
            if (!oldNode.getNodePassword().equals(vo.getNodePassword())) {
                // 加密
                vo.setNodePassword(DataEncrypt.encrypt(vo.getNodePassword()));
            }
        }

        // 获取主机信息
        final NodeInfoVo nodeInfo = ServerUtils.getNodeInfo(vo, true);
        if (nodeInfo == null) {
            result.setSuccess(false);
            result.setErrorMsg("获取主机源信息解析失败！资源信息为空！");
            return result;
        }
        if (nodeInfo.getNodeStatus() != NodeState.GET_RESOURCE_SUCCESSFULLY) {
            result.setSuccess(false);
            result.setData(nodeInfo);
            result.setErrorMsg(nodeInfo.getErrorMsg());
            return result;
        }
        String osVersion = nodeInfo.getNodeSystem();
        boolean red_hat_7 = osVersion.contains("Red Hat") && osVersion.contains("7");
        boolean cent_os_7 = osVersion.contains("CentOS") && osVersion.contains("7");

        if (osVersion != null && !red_hat_7 && !cent_os_7) {
            result.setSuccess(false);
            result.setErrorMsg("操作系统" + nodeInfo.getNodeSystem() + "不支持，仅支持CentOS 7/Red Hat 7 版本以上");
            return result;
        }

        result.setSuccess(true);
        result.setErrorMsg(null);
        result.setData(nodeInfo);
        return result;


    }

    /**
     * 刷新缓存信息
     *
     * @param vo
     * @return
     */
    @RequestMapping("/refreshInfo.do")
    @ResponseBody
    public CommonResultVo refreshCache(@RequestBody final NodeBasicInfoVo vo) {
        CommonResultVo result = new CommonResultVo();
        // 获取主机信息
        final NodeInfoVo nodeInfo = ServerUtils.getNodeInfo(vo, true);

        // 异步更新节点节点资源信息
        TaskExecuteEngine.execute(new UpdateNodeResourceTask(vo.getNodeId()));
        result.setSuccess(nodeInfo != null);
        result.setErrorMsg("主机信息");
        result.setData(nodeInfo);
        result.setSuccess(true);
        return result;
    }


    /**
     * 查询集群下的所有节点信息（包括主机信息）
     *
     * @param vo
     * @return
     */
    @RequestMapping("/findClusterNodes.do")
    @ResponseBody
    public CommonResultVo queryClusterNodesInfo(@RequestBody NodeBasicInfoVo vo) {
        final List<NodeBasicInfoVo> vos = nodeBpo.queryNodesByClusterId(vo.getClusterId());
        final CommonResultVo resultVo = new CommonResultVo();
        resultVo.setData(vos);
        resultVo.setSuccess(true);
        return resultVo;
    }

    /**
     * 更新节点信息
     *
     * @param vo
     * @return
     */
    @RequestMapping("/update.do")
    @ResponseBody
    public CommonResultVo updateNode(@RequestBody NodeBasicInfoVo vo) {
        final boolean success = nodeBpo.updateNodeBasicInfo(vo);
        final CommonResultVo result = new CommonResultVo();

        if (!success) {
            result.setSuccess(false);
            result.setData(vo);
            result.setErrorMsg("在集群中已经存在同名的节点");
            return result;
        }
        NodeBasicInfoVo queryNodeInfo = nodeBpo.queryNodeInfoById(vo.getNodeId());
        TaskExecuteEngine.execute(new UpdateNodeResourceTask(vo.getNodeId()));
        result.setData(queryNodeInfo);
        result.setSuccess(true);
        return result;
    }

    /**
     * 删除一个节点
     *
     * @param vo
     * @return
     */
    @RequestMapping("/delete.do")
    @ResponseBody
    public CommonResultVo deleteNode(@RequestBody NodeBasicInfoVo vo) {
        // 删除节点
        final CommonResultVo result = new CommonResultVo();
        NodeBasicInfoVo queryVo = nodeBpo.queryNodeInfoById(vo.getNodeId());
        if (queryVo == null) {
            result.setSuccess(true);
            result.setData("已删除节点");
            return result;
        }
        long masterNodesCount = (long) nodeBpo.queryMasterNodesByClusterId(queryVo.getClusterId()).size();
        long deployNodesCount = (long) nodeBpo.queryDeployNodesByClusterId(queryVo.getClusterId()).size();

        if (masterNodesCount == 1 && Objects.equals(queryVo.getNodeType(), ResourceConstant.NODE_TYPE_MASTER) && deployNodesCount != 0) {
            result.setSuccess(false);
            result.setErrorMsg("删除最后一个服务管理节点之前，需要删除所有的负载节点！");
            return result;
        }
        CommonResultVo checkResult = NodeUtils.checkNodeIsNotBeenOperating(queryVo);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        checkResult = NodeUtils.checkClusterResourcesEnough(queryVo);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        //删除前节点如果是停用状态，先启用
        if (NodeState.RUNNING_NOT_NOW == queryVo.getNodeRunningState()) {
            TaskExecuteEngine.execute(new NodeStartTask(queryVo.getNodeId()));
        }
        TaskExecuteEngine.execute(new NodeDeleteTask(queryVo.getNodeId()));
        result.setSuccess(true);
        result.setData(nodeBpo.queryNodeInfoById(vo.getNodeId()));
        return result;
    }

    /**
     * 设置节点的启用/禁用状态
     *
     * @param vo
     * @return
     */
    @RequestMapping("/toggleNode.do")
    @ResponseBody
    public CommonResultVo toggleNode(@RequestBody NodeBasicInfoVo vo) {
        // 设置启用/禁用节点
        final CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getNodeId())) {
            result.setSuccess(false);
            result.setErrorMsg("节点ID不能为空！");
            return result;
        }
        NodeBasicInfoVo queryNodeInfo = nodeBpo.queryNodeInfoById(vo.getNodeId());
        if (queryNodeInfo == null) {
            result.setSuccess(false);
            result.setErrorMsg("已删除节点");
            return result;
        }
        CommonResultVo checkResult = NodeUtils.checkNodeIsNotBeenOperating(queryNodeInfo);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        Integer oldState = queryNodeInfo.getNodeRunningState();

        if (NodeState.RUNNING_SUCCESS.equals(oldState)) {
            checkResult = NodeUtils.checkClusterResourcesEnough(queryNodeInfo);
            if (!checkResult.isSuccess()) {
                checkResult.setSuccess(false);
                checkResult.setErrorMsg("节点资源正在使用，无法停止");
                return checkResult;
            }
            TaskExecuteEngine.execute(new NodeStopTask(vo.getNodeId()));
            result.setErrorMsg("停止节点中！");
        } else if (NodeState.RUNNING_NOT_NOW.equals(oldState)) {
            TaskExecuteEngine.execute(new NodeStartTask(vo.getNodeId()));
            result.setErrorMsg("启动节点中！");
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/initStep.do")
    @ResponseBody
    public CommonResultVo getNodeInitStep(@RequestBody NodeBasicInfoVo vo) {
        final CommonResultVo resultVo = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getNodeId())) {
            resultVo.setSuccess(false);
            resultVo.setErrorMsg("必须指定节点ID");
            return resultVo;
        }
        List<NodeInitializeStepVo> stepList = nodeInitStepBpo.getNodeInitializeStepList(vo.getNodeId());
        List<NodeInitBaseStepVo> baseStepList = nodeInitStepBpo.getAllNodeInitBaseStepList();
        resultVo.setSuccess(true);
        HashMap<String, Object> data = new HashMap<>();
        data.put("stepList", stepList);
        data.put("baseStepList", baseStepList);
        resultVo.setData(data);
        return resultVo;
    }

    @RequestMapping(value = "/{nodeId}/test.do")
    @ResponseBody
    public CommonResultVo test(@PathVariable Long nodeId) {
        final CommonResultVo resultVo = new CommonResultVo();
        List<NodeInitializeStepVo> stepList = nodeInitStepBpo.getNodeInitializeStepList(nodeId);
        resultVo.setSuccess(true);
        HashMap<String, Object> data = new HashMap<>();
        data.put("stepList", stepList);
        resultVo.setData(data);
        return resultVo;
    }

    private CommonResultVo checkNodeBasicInfo(NodeBasicInfoVo vo) {
        final CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getNodeIP())) {
            result.setSuccess(false);
            result.setErrorMsg("节点IP不能为空");
            result.setData(vo);
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNodeName())) {
            result.setSuccess(false);
            result.setErrorMsg("节点名称不能为空");
            result.setData(vo);
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNodeType())) {
            result.setSuccess(false);
            result.setErrorMsg("节点类型");
            result.setData(vo);
            return result;
        }

        if (ValidateUtil.isEmpty(vo.getNodeSSHPort())) {
            result.setSuccess(false);
            result.setErrorMsg("节点SSH端口不能为空");
            result.setData(vo);
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNodeUser())) {
            result.setSuccess(false);
            result.setErrorMsg("节点SSH用户不能为空");
            result.setData(vo);
            return result;
        }

        if (ValidateUtil.isEmpty(vo.getNodePassword())) {
            result.setSuccess(false);
            result.setErrorMsg("节点SSH密码不能为空");
            result.setData(vo);
            return result;
        }
        String virtualIP = clusterBpo.getClusterById(vo.getClusterId()).getClusterHaVirtualIP();
        if (ValidateUtil.isNotEmpty(virtualIP) && vo.getNodeIP().trim().equals(virtualIP.trim())) {
            result.setSuccess(false);
            result.setErrorMsg("节点IP地址 " + vo.getNodeIP() + " 与集群HA虚拟IP重复");
            result.setData(vo);
            return result;
        }

        result.setSuccess(true);
        return result;
    }

}
