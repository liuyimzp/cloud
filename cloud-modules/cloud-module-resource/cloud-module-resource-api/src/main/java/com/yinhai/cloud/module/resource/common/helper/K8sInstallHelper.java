package com.yinhai.cloud.module.resource.common.helper;

import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.NodeInitRuntimeException;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.exception.SSHFileTransferException;
import com.yinhai.cloud.core.api.exec.ShellUid;
import com.yinhai.cloud.core.api.handler.SSHExecRunningHandler;
import com.yinhai.cloud.core.api.ssh.SlashPath;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadContentCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.core.api.vo.K8sInstallCommandFactory;
import com.yinhai.cloud.core.api.vo.KubeletCommandFactory;
import com.yinhai.cloud.core.api.vo.LinuxCommandFactory;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.common.entity.ScpInfo;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.handler.AppendLogToLastStepHandler;
import com.yinhai.cloud.module.resource.handler.AppendStepLogHandler;
import com.yinhai.cloud.module.resource.handler.InstallLogHandler;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeInitStepBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeOperateRecordBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.HAProxyAndKeepAlivedCommandFactory;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInitBaseStepVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInitializeStepVo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.cloud.module.shell.util.ShellExecUtil;
import com.yinhai.cloud.module.shell.vo.ExecShell;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.common.api.util.ValidateUtil;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhaokai
 * @create: 2018-08-08 09:55:31
 */
public abstract class K8sInstallHelper implements IK8sInstallHelper{
    protected String installServerHome = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT);
    protected String privateDockerRepoIp = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.PRIVATE_DOCKER_REPO_IP);
    protected String privateDockerRepoPort = SystemConfigUtil.getSystemConfigValue("docker.private.repo.port");
    protected final static Map<Long, ClusterVo> clusterLockPool = new Hashtable<>();
    protected INodeInitStepBpo nodeInitStepBpo = ServiceLocator.getService(INodeInitStepBpo.class);
    protected INodeBpo nodeBpo = ServiceLocator.getService(INodeBpo.class);
    protected INodeOperateRecordBpo nodeOperateRecordBpo = ServiceLocator.getService(INodeOperateRecordBpo.class);
    protected IClusterBpo clusterBpo = ServiceLocator.getService(IClusterBpo.class);
    protected NodeBasicInfoVo currentNode;
    protected Long currentNodeId;
    protected Integer currentStepOrder;
    protected Long operateId;
    protected ConnVo currentConn;


    /**
     * 唯一构造函数
     *
     * @param nodeId    当前安装节点Id
     * @param operateId 当前操作记录Id
     */
    public K8sInstallHelper(Long nodeId, Long operateId) {
        this.currentNode = nodeBpo.queryNodeInfoById(nodeId);
        this.currentNodeId = nodeId;
        this.operateId = operateId;
        currentConn = NodeUtils.createConnFromNode(currentNode);
        // 同步集群列表与锁列表
        syncLockPool();
    }

    protected void syncLockPool() {

        List<ClusterVo> clusterList = clusterBpo.queryAllClusterWithoutStatistic();
        // 增加锁
        for (ClusterVo c : clusterList) {
            Long id = c.getId();
            if (clusterLockPool.get(id) == null) {
                ClusterVo cluster = new ClusterVo();
                cluster.setId(id);
                clusterLockPool.put(id, cluster);
            }
        }
        // 删除锁
        Set<Map.Entry<Long, ClusterVo>> lockEntries = clusterLockPool.entrySet();
        List<Long> removeLockList = new ArrayList<>();
        for (Map.Entry<Long, ClusterVo> e : lockEntries) {
            Long id = e.getKey();
            if (clusterList.stream().noneMatch(n -> id.equals(n.getId()))) {
                // 当前遍历到的锁id不在集群的列表中，则删除该元素
                 removeLockList.add(id);
            }
        }
        removeLockList.stream().forEach(clusterLockPool::remove);
    }

    /**
     * 并发初始化入口方法，如果是管理节点，则同步初始化，如果是deploy节点则异步初始化
     * 执行的初始化流程是完整的初始化流程
     */
    public void installFullyStart() {

        // 设置节点正在执行初始化
        initNodeEnvironment(0);
    }

    /**
     * 并发从指定步骤开始初始化入口方法
     *
     * @param startStepOrder 步骤顺序
     */
    public void installSegmentStart(final Integer startStepOrder) {
        boolean notContains = nodeInitStepBpo.getAllNodeInitBaseStepList().stream().mapToInt(NodeInitBaseStepVo::getStepOrder).noneMatch(n -> n == startStepOrder);
        if (notContains) {
            return;
        }
        initNodeEnvironment(startStepOrder);
    }

    /**
     * 初始化节点完整流程控制
     * 从指定步骤开始执行初始化
     *
     * @param startStepOrder 起始步骤
     */
    protected void initNodeEnvironment(final Integer startStepOrder) {
        List<NodeInitializeStepVo> stepList = nodeInitStepBpo.getOrderedNodeInitStepListFromStartOrder(currentNode.getNodeId(), startStepOrder);
        int operateAddPer = 100;
        int basePer = 0;
        if (!stepList.isEmpty()) {
            operateAddPer = 100 / stepList.size();
        }
        // 步骤执行，循环开始
        for (int i = 0; i < stepList.size(); i++) {
            basePer += operateAddPer;
            NodeInitializeStepVo currentRunningStep = stepList.get(i);
            currentStepOrder = currentRunningStep.getStepOrder();
            // 上一个步骤没有执行成功，则不能继续执行下一个步骤

            if (i != 0) {
                NodeInitializeStepVo lastStep = nodeInitStepBpo.getNodeInitStep(currentNode.getNodeId(), currentStepOrder - 1);
                if (!ResourceConstant.NODE_INIT_STEP_SUCCESSFUL.equals(lastStep.getStepState())) {

                    throw new NodeInitRuntimeException("在节点：" + currentNode.getNodeName()
                            + "执行初始化，步骤（"
                            + stepList.get(i - 1).getBaseStep().getStepName()
                            + "）未执行成功，退出"
                            + currentRunningStep.getBaseStep().getStepName());

                }
            }
            NodeInitBaseStepVo baseStep = currentRunningStep.getBaseStep();
            String log;
            if (baseStep != null) {
                log = baseStep.getStepStartStateDesc();
            } else {
                log = "执行节点初始化步骤：" + i;
            }
            nodeOperateRecordBpo.appendOperateLog(operateId, log);

            executeSingleStep();

            if (baseStep != null) {
                log = baseStep.getStepFinishStateDesc();
            } else {
                log = "完成节点初始化步骤：" + i;
            }
            nodeOperateRecordBpo.appendOperateLog(operateId, log);
            nodeOperateRecordBpo.updateNodeOperatePercentage(operateId, basePer);
        }
        nodeInitStepBpo.finishNodeInit(currentNodeId, operateId);// 100 per 放这里更新

        // 步骤执行，循环结束同步进行未Join节点的Join操作
        synchronized (clusterLockPool.get(currentNode.getClusterId())) {
            joinClusterNotJoinedNodes();
        }
    }

    protected void executeSingleStep() {
        String methodName = "k8sInstallStep_" + currentStepOrder + "_";
        // 设置该步骤正在执行
        nodeInitStepBpo.updateNodeInitStepAsStart(currentNode.getNodeId(), currentStepOrder);
        try {
            Method handleMethod = this.getClass().getDeclaredMethod(methodName);
            handleMethod.invoke(this);
            // 设置该步骤执行成功
            nodeInitStepBpo.updateNodeInitStepAsSuccess(currentNode.getNodeId(), currentStepOrder);

        } catch (Exception e) {
            // 设置该步骤执行失败
            String msg = CommonUtil.getExceptionMsgContent(e);
            nodeInitStepBpo.updateNodeInitStepAsFailed(currentNode.getNodeId(), currentStepOrder, msg);
            // 有一步出错则终止安装步骤执行
            throw new NodeInitRuntimeException(e);
        }
    }

    // join 当前节点所在集群未join的所有节点
    protected void joinClusterNotJoinedNodes() {

        List<NodeBasicInfoVo> initDoneMasters = nodeBpo.querInitDoneMasterNodes(currentNode.getClusterId());
        if (initDoneMasters.isEmpty()) {
            nodeInitStepBpo.appendLogToLastStep(currentNodeId, "current cluster does not have any available master node for join !");
            return;
        }
        List<NodeBasicInfoVo> notJoinedNodes = nodeBpo.queryInitDoneButNotJoinedDeployNodes(currentNode.getClusterId());
        if (!notJoinedNodes.isEmpty()) {
            nodeInitStepBpo.appendLogToLastStep(currentNodeId, ("需要Join的负载节点：" + notJoinedNodes.stream().map(NodeBasicInfoVo::getNodeIP).collect(Collectors.toList())));
        }
        for (NodeBasicInfoVo n : notJoinedNodes) {
            nodeInitStepBpo.appendLogToLastStep(currentNodeId, "有未Join的负载节点：" + n.getNodeName() + ",即将Join该节点\n");
            NodeBasicInfoVo joinMaster = initDoneMasters.get(0);
            try {
                executeNodeJoin(new AppendLogToLastStepHandler(n), n, joinMaster);
                nodeInitStepBpo.appendLogToLastStep(currentNodeId, "Join节点" + n.getNodeIP() + "到Master" + joinMaster.getNodeIP() + " 成功！");
            } catch (Exception e) {
                nodeInitStepBpo.appendLogToLastStep(currentNodeId, "Join 节点 " + n.getNodeIP() + "到Master" + joinMaster.getNodeIP() + " 失败");
                nodeInitStepBpo.appendLogToLastStep(currentNodeId, "-->原因:\n" + CommonUtil.getExceptionMsgContent(e));
                NodeBasicInfoVo updateVo = new NodeBasicInfoVo();
                updateVo.setNodeId(n.getNodeId());
                updateVo.setNodeJoined(ResourceConstant.NODE_NOT_JOINED);
                nodeBpo.updateNodeExternalInfo(updateVo);
            }
        }

    }

    // 执行节点Join的命令，并更新节点Join状态
    protected void executeNodeJoin(SSHExecRunningHandler runningHandler, NodeBasicInfoVo joinNode, NodeBasicInfoVo joinMaster) throws SSHExecuteException, SSHConnectionException {
        ConsoleCommand tokenAndHashCmd = KubeletCommandFactory.getNodeJoinTokenAndCaHash();
        ConsoleCommand resetNode = KubeletCommandFactory.resetNode();
        MsgVO msgVO = CommonSshExecUtil.exec(NodeUtils.createConnFromNode(joinMaster), tokenAndHashCmd).get(tokenAndHashCmd);
        String sysoutMsg = msgVO.getSysoutMsg();
        String[] arr = sysoutMsg.split("\n");
        String token = "";
        String caHash = "";
        String tokenPrefix = "token:";
        String caHashPrefix = "caHash:";
        for (String a : arr) {
            if (a.startsWith(tokenPrefix)) {
                token = a.substring(tokenPrefix.length());
            }
            if (a.startsWith(caHashPrefix)) {
                caHash = a.substring(caHashPrefix.length());
            }
        }

        if ("".equals(token)) {
            throw new SSHExecuteException("join token empty!");
        }
        if ("".equals(caHash)) {
            throw new SSHExecuteException("join caHash empty!");
        }
        //ha使用虚拟ip非ha使用管理节点ip
        String virtualHAIP = clusterBpo.getClusterById(currentNode.getClusterId()).getClusterHaVirtualIP();
        Integer port = HAProxyAndKeepAlivedCommandFactory.HAPROXY_LISTEN_PORT;
        if (ValidateUtil.isEmpty(virtualHAIP)){
            virtualHAIP = joinMaster.getNodeIP();
            port = HAProxyAndKeepAlivedCommandFactory.NOHACLUSTER_ETCD_PORT;
        }
        ConsoleCommand joinCmd = KubeletCommandFactory.joinNode(token, caHash, virtualHAIP, port);
        CommonSshExecUtil.exec(runningHandler, null, NodeUtils.createConnFromNode(joinNode), resetNode, joinCmd).get(joinCmd);
        nodeInitStepBpo.appendLogToLastStep(joinNode.getNodeId(), "Join 节点" + joinNode.getNodeIP() + "到Master" + joinMaster.getNodeIP());
        NodeBasicInfoVo updateVo = new NodeBasicInfoVo();
        updateVo.setNodeId(joinNode.getNodeId());
        updateVo.setNodeJoined(ResourceConstant.NODE_JOINED);
        nodeBpo.updateNodeExternalInfo(updateVo);

    }

    // 初始化基础环境， 配置Hostname，关闭防火墙等
    protected void initNodeSystemEnvironment() {
        try {
            ShellExecUtil.exec(new InstallLogHandler(currentNode, currentStepOrder, 0, 50), null, currentConn, new ExecShell(ShellUid.SHELL_INIT_NODE, true)
                    .appendParam(currentNode.getNodeIP())
                    .appendParam(currentNode.getNodeName()));
            int currPer = 50;
            // 1.在集群的其他节点增加当前节点的hosts映射
            nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "在集群其他节点修改hosts映射");
            List<NodeBasicInfoVo> otherNodes = nodeBpo.queryNodesByClusterId(currentNode.getClusterId()).stream().filter(n -> !currentNode.getNodeId().equals(n.getNodeId())).collect(Collectors.toList());
            int addPer = 25;
            if (!otherNodes.isEmpty()) {
                addPer = 25 / otherNodes.size();
            }

            for (int i = 0; i < otherNodes.size(); i++) {
                currPer += addPer;
                NodeBasicInfoVo n = otherNodes.get(i);
                ConnVo modifyHostsConn = NodeUtils.createConnFromNode(n);
                String logPrefix = "在节点：" + n.getNodeIP() + "追加Hosts映射 " + currentNode.getNodeIP() + " " + currentNode.getNodeName();
                try {
                    ConsoleCommand modifyHostsCmd = LinuxCommandFactory.modifyHostsCmd(currentNode.getNodeIP(), currentNode.getNodeName());
                    CommonSshExecUtil.exec(modifyHostsConn, modifyHostsCmd);
                    nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, logPrefix + " 成功！");
                } catch (SSHExecuteException | SSHConnectionException e) {
                    String reason = "连接失败，请检查主机SSH连接！";
                    if (e instanceof SSHExecuteException) {
                        reason = "执行命令失败，请检查命令的正确性！" + CommonUtil.getExceptionMsgContent(e);
                    }
                    nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, logPrefix + " 失败！");
                    nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, " --->原因：" + reason);
                } finally {
                    nodeInitStepBpo.updateNodeStepFinishPercentage(currentNodeId, currentStepOrder, currPer);
                }
            }
            nodeInitStepBpo.updateNodeStepFinishPercentage(currentNodeId, currentStepOrder, 75);

            // 2.获取其他节点的hosts映射，写入到当前节点的hosts文件中
            nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "获取其他节点的hosts映射，写入到当前节点的hosts文件中");
            List<ConsoleCommand> list = otherNodes.stream().map(n -> LinuxCommandFactory.modifyHostsCmd(n.getNodeIP(), n.getNodeName())).collect(Collectors.toList());
            CommonSshExecUtil.exec(currentConn, list.toArray(new ConsoleCommand[list.size()]));
            nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "完成本节点的hosts同步");
        } catch (Exception e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    // 配置节点间Ntp服务，非同步，保证最终一致性即可
    protected void configNodesNtpSyncForNewNode() {
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "与Docker仓库节点进行时间同步");
        // 与Docker仓库节点进行时间同步
        String ntpMasterHost = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.PRIVATE_DOCKER_REPO_IP);
        ExecShell configNtp = new ExecShell(ShellUid.CONFIG_NTP, true)
                .appendParam(ResourceConstant.NTP_NODE_TYPE_SLAVE)
                .appendParam(ntpMasterHost);
        try {
            ShellExecUtil.exec(new InstallLogHandler(currentNode, currentStepOrder, 20, 100),
                    null,
                    currentConn, configNtp);
        } catch (Exception e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    // 初始化K8s
    protected void initK8sForNode() {
        // initK8sForNode
        switch (currentNode.getNodeType()) {
            case ResourceConstant.NODE_TYPE_MASTER:
                // Master 节点初始化为同步初始化，更新状态也为同步
                synchronized (clusterLockPool.get(currentNode.getClusterId())) {
                    initK8sMasterNode();
                }
                break;
            case ResourceConstant.NODE_TYPE_DEPLOY:
                joinK8sDeployNode();
                break;
            default:
                throw new NodeInitRuntimeException("Unsupported Node Type" + currentNode.getNodeType());
        }
    }

    protected  void initK8sMasterNode(){
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "开始初始化ApiServer");
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "生成初始化配置yaml文件");
        List<NodeBasicInfoVo> otherInitDoneMasterNodes = nodeBpo.queryOtherApiServerInitDoneMasterNodes(currentNode.getClusterId(), currentNodeId);

        String confFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue("k8s.server.root") + "/dependency/conf/k8s/init.yml").toString();
        UploadContentCommand uploadInitConfig = new UploadContentCommand(confFilePath, getKubeadmInitConfig());
        try {
            CommonSshExecUtil.exec(currentConn, uploadInitConfig);
            nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "已生成初始化配置yaml文件");
        } catch (SSHExecuteException | SSHConnectionException e) {
            throw new NodeInitRuntimeException(e);
        }

        boolean isFirstMaster = otherInitDoneMasterNodes.isEmpty();
        if (!isFirstMaster) {
            // 先拷贝pki 目录到本地缓存
            nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "拷贝pki 目录到本节点缓存");
            NodeBasicInfoVo fromServer = otherInitDoneMasterNodes.get(0);
            try {
                ScpInfo scpInfo = new ScpInfo();
                scpInfo.setFromServerIp(fromServer.getNodeIP());
                scpInfo.setFromPath("/etc/kubernetes/pki");
                scpInfo.setToServerIp(currentNode.getNodeIP());
                scpInfo.setToPath(new SlashPath(installServerHome, "/dependency/k8s/pki").getParentPath());
                new JavaScpHelper().scpDirBetweenServer(scpInfo);
                nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "完成拷贝pki 目录到本节点缓存");
            } catch (SSHConnectionException | SSHFileTransferException e) {
                throw new NodeInitRuntimeException(e);
            }
            nodeInitStepBpo.updateNodeStepFinishPercentage(currentNodeId, currentStepOrder, 30);
        }

        try {
            ShellExecUtil.exec(new InstallLogHandler(currentNode, currentStepOrder, 31, 70), null, currentConn,
                    new ExecShell(ShellUid.INIT_HIGH_AVAILABLE_API_SERVER, true)
                            .appendParam(String.valueOf(isFirstMaster)));

            ShellExecUtil.exec(new InstallLogHandler(currentNode, currentStepOrder, 71, 80), null, currentConn, new ExecShell(ShellUid.ADD_BASIC_KUBENETES_COMPONENTS, false).appendParam(otherInitDoneMasterNodes.isEmpty() ? "true" : "false"));

            if (ResourceConstant.MASTER_ALSO_AS_CHILD.equals(currentNode.getNodeAsChild())) {

                ConsoleCommand configMasterAsDeployNodeCmdVo = K8sInstallCommandFactory.configMasterAsDeployNodeCmdVo(currentNode.getNodeName());
                nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "将管理节点作为负载节点");
                CommonSshExecUtil.exec(new InstallLogHandler(currentNode, currentStepOrder, 80, 90), null, currentConn, configMasterAsDeployNodeCmdVo);

                nodeInitStepBpo.updateNodeStepFinishPercentage(currentNodeId, currentStepOrder, 90);
            }

            currentNode.setNodeJoined(ResourceConstant.NODE_JOINED);
            nodeBpo.updateNodeExternalInfo(currentNode);
            //创建prometheus监控
            String prometheusFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue("k8s.server.root") + ServerCmdConstant.PROMETHEUS_RELA_PATH).toString();
            String istioDemoPath = SystemConfigUtil.getSystemConfigValue("istio.demo.path");
            String hpaPath = SystemConfigUtil.getSystemConfigValue("hpa.install.path");
            ConsoleCommand createPrometheusCmd = new ConsoleCommand();
            createPrometheusCmd.appendCommand("source ~/.bash_profile && kubectl apply -f " + prometheusFilePath);
            if (isFirstMaster){
                nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "创建istio必要pod");
                createPrometheusCmd.appendCommand("source ~/.bash_profile && kubectl apply -f " + istioDemoPath);
                nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "创建metrics-server必要pod");
                createPrometheusCmd.appendCommand("source ~/.bash_profile && kubectl apply -f " + hpaPath);
            }
            CommonSshExecUtil.exec(currentConn, createPrometheusCmd);
            if (isFirstMaster){
                nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "创建istio-ingressgateway-service");
                NodeUtils.createIstioIngressGatewayService(currentNode);
            }
            nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "完成ApiServer初始化");
        } catch (Exception e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    protected abstract String getKubeadmInitConfig();

    // Join Node
    protected void joinK8sDeployNode() {
        // 满足初始化完成，不为当前节点，节点不处于正在操作中
        List<NodeBasicInfoVo> otherInitDoneMasterNodes = nodeBpo.queryOtherApiServerInitDoneMasterNodes(currentNode.getClusterId(), currentNodeId);

        nodeInitStepBpo.appendStepLogAndUpdateFinishPercentage(currentNodeId, currentStepOrder, "检测是否有可用的管理节点", 10);

        if (otherInitDoneMasterNodes.isEmpty()) {
            currentNode.setNodeJoined(ResourceConstant.NODE_NOT_JOINED);
            nodeBpo.updateNodeExternalInfo(currentNode);
            nodeInitStepBpo.appendStepLogAndUpdateFinishPercentage(currentNodeId, currentStepOrder, "无可用的管理节点，请等待管理节点初始化完成，或者主动添加新的管理节点", 100);
            return;
        }
        NodeBasicInfoVo joinMaster = otherInitDoneMasterNodes.get(0);
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "有可用的Master节点：" + joinMaster.getNodeName());
        try {
            executeNodeJoin(new AppendStepLogHandler(currentNode, currentStepOrder), currentNode, joinMaster);
        } catch (Exception e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    protected void run_docker_web_console() throws NodeInitRuntimeException{
        if (ResourceConstant.NODE_TYPE_DEPLOY.equals(currentNode.getNodeType()) || ResourceConstant.MASTER_ALSO_AS_CHILD.equals(currentNode.getNodeAsChild())) {
            ConsoleCommand cmd = new ConsoleCommand();
            cmd.appendCommand(" docker rm $(docker ps -a -f name=docker-container-web)");
            String address = SystemConfigUtil.getSystemConfigValue("DOCKER_REPERTORY_URL");
            String port = SystemConfigUtil.getSystemConfigValue("CONTAINER_PORT");
            cmd.appendCommand(" docker run -d  --name docker-container-web -p " + port + ":" + port + " -v /var/run/docker.sock:/var/run/docker.sock " + address + "/docker-web-console:v1.0");
            try {
                CommonSshExecUtil.exec(currentConn, cmd);
            } catch (Exception e) {
                throw new NodeInitRuntimeException(currentNode.getNodeIP() + "---节点启动 docker-web-console 失败，需要请手动启动");
            }
        }
    }
}


