package com.yinhai.cloud.module.resource.cluster.impl.bpo;

import com.yinhai.cloud.core.api.constant.CloudEventType;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.INamespaceBpo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.cluster.impl.dao.IClusterDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.ClusterPo;
import com.yinhai.cloud.module.resource.constants.ClusterState;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.event.ClusterDeletedEventResource;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.OperateRunningServer;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeDao;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeInitializeStepDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeBasicInfoPo;
import com.yinhai.cloud.module.resource.pv.impl.dao.IPersistentVolumeDao;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import com.yinhai.cloud.module.user.api.bpo.IUserAuthorityBpo;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.common.api.exception.AppException;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.event.TaEventPublisher;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhaokai
 */
public class IClusterBpoIpml extends TaBaseBpo implements IClusterBpo {
    private final String CODE_KEY_CLUSTER_STATE_DESC = "CLUSTER_STATE_DESC";
    @Autowired
    private INodeInitializeStepDao nodeInitializeStepDao;
    @Autowired
    private IPersistentVolumeDao pvDao;
    @Autowired
    private IAppBpo appBpo;
    @Autowired
    private IClusterDao clusterDao;
    @Autowired
    private INodeDao nodeDao;
    @Autowired
    private IUserAuthorityBpo userAuthorityBpo;
    @Autowired
    private INamespaceBpo namespaceBpo;


    @Resource(name = "taEventPublisher")
    private TaEventPublisher taEventPublisher;

    @Override
    public void updateClusterAsDeleting(Long clusterId, OperateRunningServer operateRunningServer) {
        ClusterPo cluster = clusterDao.getClusterById(clusterId);
        cluster.setDeleting(ResourceConstant.CLUSTER_IS_BEEN_DELETING);
        cluster.setClusterDeletingServer(operateRunningServer.toString());
        clusterDao.updateClusterById(cluster);
        nodeDao.updateClusterNodesAsDeleting(clusterId);
    }

    @Override
    public List<ClusterVo> queryAllClusterBasicInfo(Long userid) {
        List<ClusterPo> clusterPos;
        if(ValidateUtil.areEqual(ServerCmdConstant.ADMIN_ID, userid)){
            clusterPos = clusterDao.getClusterList();
        }
//        else {
//            clusterPos = userAuthorityBpo.getUserClusterAuthoritys(userid).stream().filter(vo -> !ValidateUtil.isEmpty(clusterDao.getClusterById(vo.getResourceId()))).map(userAuthorityVo -> clusterDao.getClusterById(userAuthorityVo.getResourceId())).collect(Collectors.toList());
//        }
        else {
            List<Long> clusterIds = new ArrayList<>();
            clusterPos = userAuthorityBpo.getUserAppAuthoritys(userid).stream().map(vo -> {
                if (IAppConstants.RESOURCE_TYPE_CLUSTER.equals(vo.getResourceType())){
                    if (!clusterIds.contains(vo.getResourceId())){
                        clusterIds.add(vo.getResourceId());
                        return clusterDao.getClusterById(vo.getResourceId());
                    }
                }
                else if (IAppConstants.RESOURCE_TYPE_NAMESPACE.equals(vo.getResourceType())){
                    NamespaceVo namespaceVo = namespaceBpo.getNamespace(vo.getResourceId());
                    if (!clusterIds.contains(namespaceVo.getClusterId())){
                        clusterIds.add(namespaceVo.getClusterId());
                        return clusterDao.getClusterById(namespaceVo.getClusterId());
                    }
                }
                return null;
            }).collect(Collectors.toList());
        }
        if (clusterPos == null){
            clusterPos = new ArrayList<>();
        }
        return clusterPos.stream().filter(cvo -> cvo != null).map(c -> {
            ClusterVo vo = c.toVo(new ClusterVo());
            // 设置集群运行状态描述
            String runningDesc = CodeTableUtil.getDesc(CODE_KEY_CLUSTER_STATE_DESC, String.valueOf(c.getRunningState()));
            vo.setRunningStateDesc(runningDesc);
            vo.setProblemNodesNum(Integer.parseInt(String.valueOf(nodeDao.countClusterNotRunningNode(vo.getId()))));
            // 如果有集群节点正在操作
            vo.setAllowDelete(!(nodeDao.checkClusterHasOperatingNode(vo.getId()) || c.getDeleting().equals(ResourceConstant.CLUSTER_IS_BEEN_DELETING)));
            // 计算集群总资源
            List<NodeBasicInfoPo> nodeList = nodeDao.queryNodesByClusterId(vo.getId());
            vo.setTotalDisk(nodeList.stream().filter(n -> n != null && n.getNodeDiskTotalMb() != null).mapToDouble(NodeBasicInfoPo::getNodeDiskTotalMb).sum());
            vo.setTotalDiskAvailable(nodeList.stream().filter(n -> n != null && n.getNodeDiskUsableMb() != null).mapToDouble(NodeBasicInfoPo::getNodeDiskUsableMb).sum());
            //集群状态判断
            for (NodeBasicInfoPo po : nodeList) {
                if (po.getNodeInitState() == NodeState.INIT_DONE && ResourceConstant.NODE_TYPE_MASTER.equals(po.getNodeType())) {
                    vo.setRunningState(ClusterState.CLUSTER_RUNNING_SUCCESSFULLY);
                    vo.setRunningStateDesc("运行正常");
                }
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public ClusterVo getClusterById(Long id) {
        ClusterPo cluster = clusterDao.getClusterById(id);
        if (cluster == null) {
            return null;
        }
        ClusterVo vo = cluster.toVo(new ClusterVo());
        vo.setAllowDelete(!(nodeDao.checkClusterHasOperatingNode(id) || vo.getDeleting().equals(ResourceConstant.CLUSTER_IS_BEEN_DELETING)));
        return vo;
    }

    @Override
    public void refreshResource(Long clusterId) {
        ClusterVo clusterVo = getClusterById(clusterId);
        //总CPU数
        int totalCpu = 0;
        //节点可用CPU
        Double allocatableCpu = 0d;
        //节点可用内存
        Double allocatableMemory = 0d;
        //总内存
        Double totalMemory = 0d;

        List<NodeBasicInfoPo> nodeList = nodeDao.queryRunningNodesByClusterId(clusterId);

        //从集群节点中获一个主节点
//        NodeBasicInfoPo masterNode = nodeList.stream().filter(nodeBasicInfoPo -> "0".equals(nodeBasicInfoPo.getNodeType())).findAny().orElse(new NodeBasicInfoPo());
        NodeBasicInfoPo masterNode = nodeDao.queryMasterNodesByClusterId(clusterId).stream().findAny().orElse(new NodeBasicInfoPo());
        //使用任意主节点获取各节点可用资源信息

        for (NodeBasicInfoPo n : nodeList) {
            NodeBasicInfoVo vo = n.toVo(new NodeBasicInfoVo());
            NodeInfoVo nodeResourceInfo = ServerUtils.getNodeInfo(vo, true);

            //获取各节点可用资源信息
            NodeInfoVo k8sNodeInfo = ServerUtils.getK8sNodeInfo(masterNode.toVo(new NodeBasicInfoVo()), vo.getNodeName());
            // 未获取到新的资源信息，不进行数据库更新操作
            if (nodeResourceInfo == null || nodeResourceInfo.getNodeStatus() != NodeState.GET_RESOURCE_SUCCESSFULLY) {
                continue;
            }
            NodeBasicInfoVo updateNode = new NodeBasicInfoVo();
            updateNode.setNodeId(vo.getNodeId());
            updateNode.setNodeMemAvailable(Double.parseDouble(nodeResourceInfo.getNodeFreeMemory()));
            updateNode.setNodeMemMb(Double.parseDouble(nodeResourceInfo.getNodeMemoryInfo()));
            updateNode.setNodeDiskTotalMb(Double.parseDouble(nodeResourceInfo.getNodeDiskInfoTotal()));
            updateNode.setNodeDiskUsableMb(Double.parseDouble(nodeResourceInfo.getNodeDiskInfoUsable()));
            updateNode.setNodeDiskUsedMb(Double.parseDouble(nodeResourceInfo.getNodeDiskInfoUsage()));
            updateNode.setNodeCpuAmount(Integer.parseInt(nodeResourceInfo.getNodeCpuInfo()));
            updateNode.setNodeAllocatableCpu(k8sNodeInfo.getNodeAllocatableCpu());
            updateNode.setNodeAllocatableMemory(k8sNodeInfo.getNodeAllocatableMemory());

            nodeDao.updateNode(updateNode);
            //部署节点才计算资源
            if (ResourceConstant.NODE_TYPE_DEPLOY.equals(vo.getNodeType()) || ResourceConstant.MASTER_ALSO_AS_CHILD.equals(vo.getNodeAsChild())) {
                totalCpu += updateNode.getNodeCpuAmount();
                allocatableCpu += updateNode.getNodeAllocatableCpu();
                totalMemory += updateNode.getNodeMemMb();
                allocatableMemory += updateNode.getNodeAllocatableMemory();
            }
        }
        INamespaceBpo namespaceBpo = (INamespaceBpo) ServiceLocator.getService("namespaceBpo");
        List<NamespaceVo> nsList = namespaceBpo.getNamespaces(clusterId);
        clusterVo.setTotalCpu(totalCpu);
        clusterVo.setTotalMemory(totalMemory);
        clusterVo.setTotalAllocatableCpu(allocatableCpu);
        clusterVo.setTotalAllocatableMemory(allocatableMemory);
        clusterVo.setTotalCpuAvailable(allocatableCpu - nsList.stream().mapToDouble(NamespaceVo::getMaxCPULimit).sum());
        clusterVo.setTotalMemAvailable(allocatableMemory - nsList.stream().mapToDouble(NamespaceVo::getMaxMemoryLimit).sum());
        clusterVo.setNumOfNodes(nodeList.size());
        updateClusterById(clusterVo);
    }

    @Override
    public void deleteClusterById(Long id) {
        clusterDao.deleteClusterById(id);
        taEventPublisher.publish(new ClusterDeletedEventResource(CloudEventType.CLUSTER_DELETED, id), CloudEventType.CLUSTER_DELETED);
    }

    @Override
    public Boolean updateClusterById(ClusterVo clusterVo) {
        ClusterPo oldClusterPo = clusterDao.getClusterById(clusterVo.getId());
        if (!clusterVo.getIdentify().equals(oldClusterPo.getIdentify())) {
            Boolean repeat = checkIdentifyRepeat(clusterVo.getIdentify());
            if (repeat) {
                return false;
            }
        }
        clusterDao.updateClusterById(clusterVo.toPO(new ClusterPo()));
        return true;
    }

    @Override
    public void updateClusterMemCpu(Long id, Double mem, Double cpu, Boolean isAdd) {
        if (id == null || mem == null || cpu == null) {
            throw new AppException("更新集群内存cpu，参数错误");
        }
        if (isAdd) {
            clusterDao.addClusterMemCpu(id, mem, cpu);
        } else {
            clusterDao.subtractClusterMemCpu(id, mem, cpu);
        }
    }

    @Override
    public List<ClusterVo> queryAllNotBeenDeletingCluster() {
        return clusterDao.getClusterList().stream().filter(c -> !ResourceConstant.CLUSTER_IS_BEEN_DELETING.equals(c.getDeleting())).map(c -> {
            ClusterVo vo = c.toVo(new ClusterVo());
            vo.setProblemNodesNum(Integer.parseInt(String.valueOf(nodeDao.countClusterNotRunningNode(vo.getId()))));

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public ClusterVo addNewCluster(ClusterVo clusterVo) {
        if (clusterDao.countByTag(clusterVo.getIdentify()) > 0) {
            return null;
        }
        Long id = clusterDao.addNewCluster(clusterVo.toPO(new ClusterPo()));
        ClusterVo addedVo = clusterDao.getClusterById(id).toVo(new ClusterVo());
        return addedVo;
    }

    @Override
    public Boolean checkIdentifyRepeat(String addIdentify) {
        return clusterDao.countByTag(addIdentify) > 0;
    }


    @Override
    public void plusOneClusterNodeAmountById(Long clusterId) {
        ClusterPo clusterPo = clusterDao.getClusterById(clusterId);
        clusterPo.setNumOfNodes((clusterPo.getNumOfNodes() == null ? 0 : clusterPo.getNumOfNodes()) + 1);
        clusterDao.updateClusterById(clusterPo);
    }

    @Override
    public void minusOneClusterNodeAmountById(Long clusterId) {
        ClusterPo clusterPo = clusterDao.getClusterById(clusterId);
        clusterPo.setNumOfNodes((clusterPo.getNumOfNodes() == null || clusterPo.getNumOfNodes() == 0) ? 0 : clusterPo.getNumOfNodes() - 1);
        clusterDao.updateClusterById(clusterPo);
    }


    @Override
    public List<ClusterVo> queryAllClusterWithoutStatistic() {
        return clusterDao.getClusterList().stream().map(c -> c.toVo(new ClusterVo())).collect(Collectors.toList());
    }

    @Override
    public ClusterVo queryClusterStatistics(Long clusterId) {
        return clusterDao.getClusterById(clusterId).toVo(new ClusterVo());
    }

    @Override
    public boolean checkHAIp(String ip) {
        boolean available = false;
        try {
            ConsoleCommand commandVo = new ConsoleCommand();
            commandVo.appendCommand("ping -c1 -w1 " + ip);
            ConnVo connVo = NodeUtils.createDockerRepoServerConn();
            CommonSshExecUtil.exec(connVo, commandVo).get(commandVo);

            /*  Process p = Runtime.getRuntime().exec("ping -w 5 "+ip);
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = p.getInputStream().read(b)) != -1;) {
                out.append(new String(b, 0, n,"GBK"));
            }
            if(out.toString().contains("请求超时")){
                available =true;
            }*/
        } catch (Exception e) {
//            e.printStackTrace();
            available = true;
        }
        return available;
    }
}
