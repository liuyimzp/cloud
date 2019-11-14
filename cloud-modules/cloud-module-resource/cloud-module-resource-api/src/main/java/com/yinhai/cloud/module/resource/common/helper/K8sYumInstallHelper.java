package com.yinhai.cloud.module.resource.common.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.NodeInitRuntimeException;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.exception.SSHFileTransferException;
import com.yinhai.cloud.core.api.exec.ShellUid;
import com.yinhai.cloud.core.api.ssh.SlashPath;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadDirCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.K8sInstallCommandFactory;
import com.yinhai.cloud.core.api.vo.KubeletCommandFactory;
import com.yinhai.cloud.core.api.vo.LinuxCommandFactory;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.common.entity.ScpInfo;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.handler.InstallLogHandler;
import com.yinhai.cloud.module.resource.nodes.api.vo.ConfigEtcdCommandFactory;
import com.yinhai.cloud.module.resource.nodes.api.vo.EtcdCtlHttpsCommandFactory;
import com.yinhai.cloud.module.resource.nodes.api.vo.HAProxyAndKeepAlivedCommandFactory;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.cloud.module.shell.util.ShellExecUtil;
import com.yinhai.cloud.module.shell.vo.ExecShell;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: tinahy
 * @create: 2019-10-10
 */
public class K8sYumInstallHelper extends K8sInstallHelper {
    private final static Logger logger = LoggerFactory.getLogger(K8sYumInstallHelper.class);
    private static final String ETCD_SSL_ROOT_DIR = "/etc/etcd/ssl";
    private static final String ETCD_NAME_PREFIX = "etcd-";
    private static final String ETCD_CLUSTER_STATE_NEW = "new";
    private static final String ETCD_CLUSTER_STATE_EXISTING = "existing";
    private static final int ETCD_CLIENT_PORT = 2379;
    private static final int ETCD_SERVER_PORT = 2380;
    private static final String KUBERNETES_VERSION = "1.10.3";
    /**
     * 唯一构造函数
     *
     * @param nodeId    当前安装节点Id
     * @param operateId 当前操作记录Id
     */
    public K8sYumInstallHelper(Long nodeId, Long operateId) {
        super(nodeId, operateId);
    }

    void k8sInstallStep_1_() {
        createServerShellEnvironment();
    }

    void k8sInstallStep_2_() {
        initNodeSystemEnvironment();
    }

    void k8sInstallStep_3_() {
        createPrivateRepoForNode();
    }

    void k8sInstallStep_4_() {
        installToolDependencyForNode();
    }

    void k8sInstallStep_5_() {
        configNodesNtpSyncForNewNode();
    }

    void k8sInstallStep_6_() {
        installDockerForNode();
    }

    void k8sInstallStep_7_() {
        installKubernetesForNode();
    }

    void k8sInstallStep_8_() {
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "初始化ETCD服务");
        try {
            installEtcdForCurrentNode();
        } catch (SSHExecuteException | SSHConnectionException e) {
            throw new NodeInitRuntimeException(e);
        }
        nodeInitStepBpo.updateNodeStepFinishPercentage(currentNodeId, currentStepOrder, 20);
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "开始初始化ApiServer");
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "生成初始化配置yaml文件");
        initK8sForNode();
    }

    void k8sInstallStep_9_() {
        if (ValidateUtil.isNotEmpty( clusterBpo.getClusterById(currentNode.getClusterId()).getClusterHaVirtualIP())){
            configHAproxyAndKeepalived();
        }
        try {
            run_docker_web_console();
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
    }

    // 创建脚本运行环境
    private void createServerShellEnvironment() {
        final String k8sServerRoot = SystemConfigUtil.getSystemConfigValue("k8s.server.root");
        final String k8sLocalRoot = new File(K8sInstallHelper.class.getResource("/").getFile(), SystemConfigUtil.getSystemConfigValue("k8s.local.root")).getAbsolutePath();

        final ConsoleCommand cmdMkDirVo = K8sInstallCommandFactory.makeServerDirCmdVo(k8sServerRoot, currentNode.getNodeUser(), currentNode.getNodeUser());

        final UploadDirCommand cmdUploadConf = new UploadDirCommand(new File(k8sLocalRoot, SystemConfigUtil.getSystemConfigValue(ConfigPropKey.CMD_UPLOAD_CONF_PATH)).getAbsolutePath(), "" + k8sServerRoot + "/dependency");

        final UploadDirCommand cmdUploadSh = new UploadDirCommand(new File(k8sLocalRoot, SystemConfigUtil.getSystemConfigValue(ConfigPropKey.CMD_UPLOAD_SH_PATH)).getAbsolutePath(), "" + k8sServerRoot + "/dependency");

        // 创建固定文件夹，复制文件
        try {
            CommonSshExecUtil.exec(null, (cmd, result) -> {
                String appendLog = result.getSysoutMsg();
                int currentStepFinishPercentage = 0;
                if (cmdMkDirVo.equals(cmd)) {
                    appendLog = "完成脚本运行文件夹创建";
                    currentStepFinishPercentage = 25;
                } else if (cmdUploadConf.equals(cmd)) {
                    appendLog = "完成依赖配置文件上传";
                    currentStepFinishPercentage = 50;
                } else if (cmdUploadSh.equals(cmd)) {
                    appendLog = "完成依赖shell文件上传";
                    currentStepFinishPercentage = 75;
                }

                nodeInitStepBpo.appendStepLogAndUpdateFinishPercentage(currentNodeId, currentStepOrder, appendLog, currentStepFinishPercentage);
            }, currentConn, cmdMkDirVo, cmdUploadConf, cmdUploadSh);

            String workDir = SystemConfigUtil.getSystemConfigValue("k8s.server.root") + "/dependency/sh";
            ShellExecUtil.exec(null, null, workDir, currentConn, new ExecShell(ShellUid.FUNCTIONS_SH, false));
            nodeInitStepBpo.appendStepLogAndUpdateFinishPercentage(currentNodeId, currentStepOrder, "完成依赖函数上传", 100);
        } catch (SSHExecuteException | SSHConnectionException e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    // 创建私有Yum源
    private void createPrivateRepoForNode() {
        String privateYumIp = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.PRIVATE_YUM_REPO_IP);
        try {
            ShellExecUtil.exec(new InstallLogHandler(currentNode, currentStepOrder, 0, 100), null, currentConn, new ExecShell(ShellUid.CREATE_PRIVATE_YUM_REPO, true)
                    .appendParam(currentNode.getNodeIP())
                    .appendParam("slave")
                    .appendParam(privateYumIp)
            );
        } catch (Exception e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    // 安装工具依赖
    private void installToolDependencyForNode() {
        try {
            ShellExecUtil.exec(new InstallLogHandler(currentNode, currentStepOrder, 0, 100), null, currentConn, new ExecShell(ShellUid.INSTALL_TOOL_DEPENDENCY, true));
        } catch (Exception e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    // 安装Docker
    private void installDockerForNode() {
        try {
            ShellExecUtil.exec(new InstallLogHandler(currentNode, currentStepOrder, 0, 100),
                    null, currentConn, new ExecShell(ShellUid.INSTALL_DOCKER, true)
                            .appendParam(privateDockerRepoIp)
                            .appendParam(privateDockerRepoPort)
                            .appendParam("false"));
        } catch (Exception e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    // 安装K8s
    private void installKubernetesForNode() {
        DecimalFormat df = new DecimalFormat("#");
        String systemCpu = df.format(currentNode.getNodeCpuAmount() - currentNode.getNodeAllocatableCpu());
        String systemMem = df.format(currentNode.getNodeSysMemMb());
        String kubeCpu;
        String kubeMem;
        if (ResourceConstant.NODE_TYPE_DEPLOY.equals(currentNode.getNodeType())){
            kubeCpu = SystemConfigUtil.getSystemConfigValue("kube.deploy.cpu");
            kubeMem = SystemConfigUtil.getSystemConfigValue("kube.deploy.mem");
        }else {
            kubeCpu = SystemConfigUtil.getSystemConfigValue("kube.master.cpu");
            kubeMem = SystemConfigUtil.getSystemConfigValue("kube.master.mem");
        }
        if (ValidateUtil.isEmpty(kubeCpu) || ValidateUtil.isEmpty(kubeMem)){
            throw new NodeInitRuntimeException("k8s预留资源未设置");
        }
        String param = " " + systemCpu + " " + systemMem + " " + kubeCpu + " " + kubeMem;
        try {
            ShellExecUtil.exec(new InstallLogHandler(currentNode, currentStepOrder, 0, 100), null, currentConn, new ExecShell(ShellUid.INSTALL_KUBERNETES, true,param));
        } catch (Exception e) {
            throw new NodeInitRuntimeException(e);
        }
    }


    @Override
    protected String getKubeadmInitConfig() {
        List<String> masterIpList = nodeBpo.queryMasterNodesByClusterId(currentNode.getClusterId()).stream().map(NodeBasicInfoVo::getNodeIP).collect(Collectors.toList());
        List<String> masterHostNameList = nodeBpo.queryMasterNodesByClusterId(currentNode.getClusterId()).stream().map(NodeBasicInfoVo::getNodeName).collect(Collectors.toList());
        // https 认证 ip列表
        List<String> certIpList = new ArrayList<>(masterIpList.stream().filter(n -> !n.equals(currentNode.getNodeIP())).collect(Collectors.toList()));
        String clusterHaVirtualIP = clusterBpo.getClusterById(currentNode.getClusterId()).getClusterHaVirtualIP();
        //liuyi02  修改非ha集群添加节点
        if (ValidateUtil.isNotEmpty(clusterHaVirtualIP)){
            certIpList.add(clusterHaVirtualIP);
        }
        certIpList.addAll(masterHostNameList.stream().filter(n -> !n.equals(currentNode.getNodeName())).collect(Collectors.toList()));
        String nodeIp = currentNode.getNodeIP();
        //如果不是ha集群则判断是否已经有master节点如果有则使用以前节点的ip没有使用自己的
        List<NodeBasicInfoVo> otherInitDoneMasterNodes = nodeBpo.queryOtherApiServerInitDoneMasterNodes(currentNode.getClusterId(), currentNodeId);
        if (ValidateUtil.isEmpty(clusterHaVirtualIP) && ValidateUtil.isNotEmpty(otherInitDoneMasterNodes)){
            nodeIp = otherInitDoneMasterNodes.get(0).getNodeIP();
        }
        KubernetesMasterConfiguration kubernetesMasterConfiguration = new KubernetesMasterConfiguration(nodeIp, masterIpList, certIpList, KUBERNETES_VERSION);
        return kubernetesMasterConfiguration.toString();
    }


    /**
     * 1.etcd etcdctl
     * 2./usr/bin
     * 3.生成密钥，新的：直接创建，增加的节点，从其他节点去拷贝
     * 4.配置service 文件
     *
     * @throws SSHExecuteException
     * @throws SSHConnectionException
     */
    private void installEtcdForCurrentNode() throws SSHExecuteException, SSHConnectionException {
        ConsoleCommand copyCmd = ConfigEtcdCommandFactory.copyEtcdFile();
        ConsoleCommand cleanSSL = ConfigEtcdCommandFactory.cleanSSLRootDir(ETCD_SSL_ROOT_DIR);

        ConsoleCommand copyLocalEtcdCert = ConfigEtcdCommandFactory.copyLocalEtcdCert(ETCD_SSL_ROOT_DIR);
        ConsoleCommand createLocalCert = ConfigEtcdCommandFactory.createLocalEtcdCert(ETCD_SSL_ROOT_DIR);

        ConsoleCommand changeEtcdSslRootCurrentUser = LinuxCommandFactory.changeOwnerAsCurrentUser(ETCD_SSL_ROOT_DIR, currentNode.getNodeUser(), currentNode.getNodeUser());
        String clusterUrl = ETCD_NAME_PREFIX + currentNode.getNodeName() + "=https://" + currentNode.getNodeIP() + ":" + ETCD_SERVER_PORT;
        List<NodeBasicInfoVo> otherInitDoneMasterNodes = nodeBpo.queryOtherApiServerInitDoneMasterNodes(currentNode.getClusterId(), currentNodeId);
        // 拷贝运行文件etcd、etcdctl
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "拷贝运行文件etcd、etcdctl");
        CommonSshExecUtil.exec(currentConn, copyCmd, cleanSSL);
        if (ValidateUtil.isNotEmpty(otherInitDoneMasterNodes)) {
            // 对已有集群进行扩容
            CommonSshExecUtil.exec(currentConn, changeEtcdSslRootCurrentUser);
            nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "如果已经存在的集群存在本节点，则进行移除");
            checkNeedRemoveEtcdNodeFirst(otherInitDoneMasterNodes.get(0));
            nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "从节点" + otherInitDoneMasterNodes.get(0).getNodeIP() + "复制密钥文件到当前节点");
            copyExistedCertFromOtherNode(otherInitDoneMasterNodes.get(0));
        } else {
            // 建一个新的集群
            nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "创建新的etcd ssl密钥文件");
            CommonSshExecUtil.exec(currentConn, copyLocalEtcdCert, changeEtcdSslRootCurrentUser, createLocalCert);
        }


        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "配置config.json模板");
        // 配置config.json模板
        ConsoleCommand cfgCmd = ConfigEtcdCommandFactory.generateConfigJsonTemplate(ETCD_SSL_ROOT_DIR, currentNode.getNodeIP(), currentNode.getNodeName());
        CommonSshExecUtil.exec(currentConn, cfgCmd);
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "增加所有的master到config.json中");
        // 增加所有的master到config.json中
        configEtcdSSLJsonWithMasterList();
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "生成密钥文件");
        // 生成密钥文件
        ConsoleCommand gencertAllSSLFile = ConfigEtcdCommandFactory.gencertAllSSLFile(ETCD_SSL_ROOT_DIR);

        CommonSshExecUtil.exec(currentConn, gencertAllSSLFile, changeEtcdSslRootCurrentUser);

        if (ValidateUtil.isNotEmpty(otherInitDoneMasterNodes)) {
            // 分发所有的ssl文件到其他已经初始化的节点，并重启这些节点的etcd服务
            nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "分发所有的ssl文件到其他已经初始化的节点，并重启这些节点的etcd服务");
            distributeEtcdSSLFileToInitDoneMasterNodes();
            clusterUrl = appendCurrentEtcdMember(otherInitDoneMasterNodes.get(0));
        }
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "配置当前节点ETCD服务，并启动");
        configCurrentEtcdServiceAndStartService(ValidateUtil.isNotEmpty(otherInitDoneMasterNodes), clusterUrl);
    }

    private void checkNeedRemoveEtcdNodeFirst(NodeBasicInfoVo availableEtcdNode) {
        // 检测当前节点是否已经存在于已有的集群中，如果存在则先进行删除
        ConsoleCommand memberList = EtcdCtlHttpsCommandFactory.memberList(availableEtcdNode.getNodeIP(), ETCD_CLIENT_PORT);
        try {
            CommonSshExecUtil.exec(null, (cmd, execResult) -> {
                if (cmd == memberList) {
                    String result = execResult.getSysoutMsg();
                    String[] memberListResult = result.split("\n");

                    boolean needRemoveFirst = false;
                    String removeMemberId = "";
                    for (String mem : memberListResult) {
                        if (mem.contains(currentNode.getNodeIP())) {
                            needRemoveFirst = true;
                            removeMemberId = mem.substring(0, mem.indexOf(':'));
                            break;
                        }
                    }
                    if (needRemoveFirst) {
                        ConsoleCommand removeCmd = EtcdCtlHttpsCommandFactory.removeMember(availableEtcdNode.getNodeIP(), ETCD_CLIENT_PORT, removeMemberId);
                        try {
                            CommonSshExecUtil.exec(NodeUtils.createConnFromNode(availableEtcdNode), removeCmd);
                        } catch (SSHConnectionException | SSHExecuteException e) {
                            throw new NodeInitRuntimeException(e);
                        }
                    }
                }
            }, NodeUtils.createConnFromNode(availableEtcdNode), memberList);
        } catch (SSHConnectionException | SSHExecuteException e) {
            throw new NodeInitRuntimeException(e);
        }

    }

    private void copyExistedCertFromOtherNode(NodeBasicInfoVo existedMasterNode) {
        String[] fileArr = new String[]{"ca.pem", "ca-key.pem", "client.pem", "client-key.pem", "ca-config.json"};
        for (String a : fileArr) {
            ScpInfo scpInfo = new ScpInfo();
            scpInfo.setFromServerIp(existedMasterNode.getNodeIP());
            scpInfo.setFromPath(ETCD_SSL_ROOT_DIR + "/" + a);
            scpInfo.setToServerIp(currentNode.getNodeIP());
            scpInfo.setToPath(ETCD_SSL_ROOT_DIR + "/" + a);
            try {
                new JavaScpHelper().scpFileBetweenServer(scpInfo);
            } catch (SSHConnectionException | SSHFileTransferException e) {
                throw new NodeInitRuntimeException(e);
            }
        }
    }

    private void configEtcdSSLJsonWithMasterList() {
        String configJsonFile = ETCD_SSL_ROOT_DIR + "/" + "config.json";
        List<NodeBasicInfoVo> masterList = nodeBpo.queryMasterNodesByClusterId(currentNode.getClusterId());
        // 构建hosts 数据
        final JSONArray hosts = new JSONArray();
        for (NodeBasicInfoVo m : masterList) {
            hosts.add(m.getNodeIP());
            hosts.add(m.getNodeName());
        }
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("cat " + configJsonFile);

        try {
            CommonSshExecUtil.exec(null, (c, execResult) -> {
                if (c == cmd) {
                    String configJsonContent = execResult.getSysoutMsg();
                    JSONObject jo = JSON.parseObject(configJsonContent);
                    jo.put("hosts", hosts);
                    ConsoleCommand putCmd = new ConsoleCommand();
                    putCmd.appendCommand("echo \"" + jo.toString().replace("\"", "\\\"") + "\" > " + configJsonFile);
                    try {
                        CommonSshExecUtil.exec(NodeUtils.createConnFromNode(currentNode), putCmd);
                    } catch (SSHExecuteException | SSHConnectionException e) {
                        throw new NodeInitRuntimeException(e);
                    }
                }
            }, NodeUtils.createConnFromNode(currentNode), cmd);
        } catch (SSHConnectionException | SSHExecuteException e) {
            throw new NodeInitRuntimeException(e);
        }

    }

    private void distributeEtcdSSLFileToInitDoneMasterNodes() {
        List<NodeBasicInfoVo> initDoneOtherNodes = nodeBpo.queryOtherApiServerInitDoneMasterNodes(currentNode.getClusterId(), currentNodeId);
        for (NodeBasicInfoVo master : initDoneOtherNodes) {
            try {
                ScpInfo scpInfo = new ScpInfo();
                scpInfo.setFromServerIp(currentNode.getNodeIP());
                scpInfo.setFromPath(ETCD_SSL_ROOT_DIR);
                scpInfo.setToServerIp(master.getNodeIP());
                scpInfo.setToPath(new SlashPath(ETCD_SSL_ROOT_DIR).getParentPath());
                new JavaScpHelper().scpDirBetweenServer(scpInfo);
            } catch (SSHConnectionException | SSHFileTransferException e) {
                throw new NodeInitRuntimeException(e);
            }
        }
        for (NodeBasicInfoVo master : initDoneOtherNodes) {
            try {
                ConsoleCommand cmd = new ConsoleCommand();
                cmd.setWithSudo(true);
                cmd.appendCommand("service etcd restart");
                CommonSshExecUtil.exec(NodeUtils.createConnFromNode(master), cmd);
            } catch (SSHConnectionException | SSHExecuteException e) {
                throw new NodeInitRuntimeException(e);
            }
        }
    }

    private String appendCurrentEtcdMember(NodeBasicInfoVo availableEtcdNode) {
        ConsoleCommand addMember = EtcdCtlHttpsCommandFactory.addMember(availableEtcdNode.getNodeIP(), ETCD_CLIENT_PORT, "etcd-" + currentNode.getNodeName(), currentNode.getNodeIP(), ETCD_SERVER_PORT);

        try {
            // 给与etcd 服务一定的启动时间
            try {
                Thread.sleep(5000);
                //liuyi修改2019/3/1 IntertedException -> Exception
            } catch (Exception e) {
                logger.error(logger.getName() + "context", e);
            }
            MsgVO msg = CommonSshExecUtil.exec(NodeUtils.createConnFromNode(availableEtcdNode), addMember).get(addMember);
            String sysoutMsg = msg.getSysoutMsg();
            String clusterUrlPrefix = "ETCD_INITIAL_CLUSTER=";
            String[] lines = sysoutMsg.split("\n");
            String clusterUrl = "";
            for (String line : lines) {
                if (line.startsWith(clusterUrlPrefix)) {
                    clusterUrl = line.substring(clusterUrlPrefix.length()).replace("\"", "");
                }
            }
            return clusterUrl;
        } catch (SSHExecuteException | SSHConnectionException e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    private void configCurrentEtcdServiceAndStartService(boolean isScale, String clusterUrl) {
        ConfigEtcdCommandFactory.EtcdServiceConfig config = new ConfigEtcdCommandFactory.EtcdServiceConfig();
        config.setEtcdName(ETCD_NAME_PREFIX + currentNode.getNodeName());
        config.setClientPort(ETCD_CLIENT_PORT);
        config.setServerPort(ETCD_SERVER_PORT);
        config.setClusterUrl(clusterUrl);
        config.setClusterState(ETCD_CLUSTER_STATE_EXISTING);
        config.setSslRoot(ETCD_SSL_ROOT_DIR);
        config.setCurrentIp(currentNode.getNodeIP());
        if (!isScale) {
            config.setClusterState(ETCD_CLUSTER_STATE_NEW);
        }
        ConsoleCommand configCmd = ConfigEtcdCommandFactory.configEtcdServiceAndStart(config);
        try {
            CommonSshExecUtil.exec(currentConn, configCmd);
        } catch (SSHConnectionException | SSHExecuteException e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    private void configHAproxyAndKeepalived() {
        switch (currentNode.getNodeType()) {
            case ResourceConstant.NODE_TYPE_MASTER:
                // Master 节点初始化为同步初始化，更新状态也为同步
                synchronized (clusterLockPool.get(currentNode.getClusterId())) {
                    try {
                        configMasterHAproxyAndKeepalived();
                    } catch (SSHExecuteException | SSHConnectionException e) {
                        throw new NodeInitRuntimeException(e);
                    }
                }
                break;
            case ResourceConstant.NODE_TYPE_DEPLOY:
                changeDeployHeartBeatAddress();
                break;
            default:
                throw new NodeInitRuntimeException("Unsupported Node Type" + currentNode.getNodeType());
        }
    }

    private void configMasterHAproxyAndKeepalived() throws SSHExecuteException, SSHConnectionException {
        // 安装haproxy keepalived，并修改配置文件权限，以防上传配置文件缺少权限
        ConsoleCommand installHaproxyAndKeepalive = HAProxyAndKeepAlivedCommandFactory.installHaproxyAndKeepalive();
        ConsoleCommand chownHaproxy = LinuxCommandFactory.changeOwnerAsCurrentUser("/etc/haproxy", currentNode.getNodeUser(), currentNode.getNodeUser());
        ConsoleCommand chownKeepalived = LinuxCommandFactory.changeOwnerAsCurrentUser("/etc/keepalived", currentNode.getNodeUser(), currentNode.getNodeUser());

        List<NodeBasicInfoVo> masterList = nodeBpo.queryMasterNodesByClusterId(currentNode.getClusterId());
        List<String> masterIpList = masterList.stream().map(NodeBasicInfoVo::getNodeIP).collect(Collectors.toList());

        // 配置haproxy
        AbstractCommand upoloadHaproxyConfig = HAProxyAndKeepAlivedCommandFactory.uploadHaproxyConfig(masterIpList);

        ClusterVo currentClusterInfo = clusterBpo.getClusterById(currentNode.getClusterId());
        // 配置keepalived
        String virtualIp = currentClusterInfo.getClusterHaVirtualIP();
        if (virtualIp == null || "".equals(virtualIp)) {
            throw new NodeInitRuntimeException("集群虚拟IP不能为空，请编辑集群信息并配置，然后重试！");
        }
        ConsoleCommand ipNetworkInterfaceCmd = LinuxCommandFactory.ipNetworkInterface(currentNode.getNodeIP());
        ConsoleCommand subnetMaskBitWidthCmd = LinuxCommandFactory.subnetMaskBitWidth(currentNode.getNodeIP());
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "获取网卡信息");
        HashMap<AbstractCommand, MsgVO> exec = CommonSshExecUtil.exec(currentConn, ipNetworkInterfaceCmd, subnetMaskBitWidthCmd);
        nodeInitStepBpo.updateNodeStepFinishPercentage(currentNodeId, currentStepOrder, 90);
        Integer subnetMaskBitSize = Integer.parseInt(exec.get(subnetMaskBitWidthCmd).getSysoutMsg().trim());
        // 历史数据中，该字段可能为null
        List<NodeBasicInfoVo> nodeList = nodeBpo.queryNodesByClusterId(currentNode.getClusterId());
        long keepalivedMasterCount = nodeList.stream().filter(n -> n.getKeepalivedMaster() != null && n.getKeepalivedMaster()).count();
        boolean isBackup;
        if (keepalivedMasterCount == 0) {
            // 历史数据中没有记录keepalived Master，将当前节点当做keepalived master
            NodeBasicInfoVo updateNode = new NodeBasicInfoVo();
            updateNode.setNodeId(currentNodeId);
            updateNode.setKeepalivedMaster(true);
            nodeBpo.updateNodeExternalInfo(updateNode);
            isBackup = false;
        } else {
            // 有记录keepalived master ，按照记录数据进行配置
            isBackup = !(currentNode.getKeepalivedMaster() == null ? false : currentNode.getKeepalivedMaster());
        }
        String networkInterfaceName = exec.get(ipNetworkInterfaceCmd).getSysoutMsg();
        AbstractCommand upoloadKeepalivedConfig = HAProxyAndKeepAlivedCommandFactory.uploadKeepaliedConfig(virtualIp, subnetMaskBitSize, isBackup, networkInterfaceName);
        // 检测haproxy 是否存活脚本
        AbstractCommand checkHaproxyShellScript = HAProxyAndKeepAlivedCommandFactory.uploadCheckHaproxyShellScript();
        ConsoleCommand addExecuteMode = HAProxyAndKeepAlivedCommandFactory.addExecuteModeToCheckHaproxyShellScript();
        // 启动
        AbstractCommand startHaproxyAndKeepalived = HAProxyAndKeepAlivedCommandFactory.startHaproxyAndKeepalived();
        CommonSshExecUtil.exec(null, (cmd, execResult) -> nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, execResult.getSysoutMsg()), currentConn,
                installHaproxyAndKeepalive, chownHaproxy, chownKeepalived,
                upoloadHaproxyConfig,
                upoloadKeepalivedConfig,
                checkHaproxyShellScript,
                addExecuteMode,
                startHaproxyAndKeepalived);
        List<NodeBasicInfoVo> otherInitDoneMasterNodes = nodeBpo.queryOtherApiServerInitDoneMasterNodes(currentNode.getClusterId(), currentNodeId);
        for (NodeBasicInfoVo n : otherInitDoneMasterNodes) {
            CommonSshExecUtil.exec(NodeUtils.createConnFromNode(n), upoloadHaproxyConfig, startHaproxyAndKeepalived);
        }
    }

    private void changeDeployHeartBeatAddress() {
        nodeInitStepBpo.appendStepLog(currentNodeId, currentStepOrder, "负载节点重新配置心跳地址为HA地址");
        String virtualIP = clusterBpo.getClusterById(currentNode.getClusterId()).getClusterHaVirtualIP();
        ConsoleCommand changeCmd = KubeletCommandFactory.changeDeployNodeCommunicateMaster(virtualIP, HAProxyAndKeepAlivedCommandFactory.HAPROXY_LISTEN_PORT);
        try {
            CommonSshExecUtil.exec(currentConn, changeCmd);
        } catch (SSHExecuteException | SSHConnectionException e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    private static class KubernetesMasterConfiguration {
        private String advertiseAddress;
        private List<String> etcdEndPointsIPList;
        private List<String> apiServerCertSANs;
        private String version;

        public KubernetesMasterConfiguration(String advertiseAddress, List<String> etcdEndPointsIPList, List<String> apiServerCertSANs, String version) {
            this.advertiseAddress = advertiseAddress;
            if (etcdEndPointsIPList != null) {
                this.etcdEndPointsIPList = new ArrayList<>(etcdEndPointsIPList);
            }
            if (apiServerCertSANs != null){
                this.apiServerCertSANs = new ArrayList<>(apiServerCertSANs);
            }
            this.version = version;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("apiVersion: kubeadm.k8s.io/v1alpha1").append("\n")
                    .append("kind: MasterConfiguration").append("\n")
                    .append("api:").append("\n").append("advertiseAddress: ").append(advertiseAddress).append("\n")
                    .append("etcd:").append("\n")
                    .append("  endpoints:").append("\n");
            for (String e : etcdEndPointsIPList) {
                sb.append("  - https://").append(e).append(":").append(ETCD_CLIENT_PORT).append("\n");

            }
            sb.append("  caFile: ").append(ETCD_SSL_ROOT_DIR).append("/ca.pem").append("\n")
                    .append("  certFile: ").append(ETCD_SSL_ROOT_DIR).append("/client.pem").append("\n")
                    .append("  keyFile: ").append(ETCD_SSL_ROOT_DIR).append("/client-key.pem").append("\n")
                    .append("networking:").append("\n")
                    .append("  podSubnet: 10.244.0.0/16").append("\n")
                    .append("kubernetesVersion: ").append(version).append("\n")
                    .append("apiServerCertSANs:").append("\n");
            for (String e : apiServerCertSANs) {
                sb.append("  - ").append(e).append("\n");
            }
            sb.append("  - 127.0.0.1").append("\n")
                    .append("  - 10.244.0.1").append("\n");
            sb.append("kubeProxy: ").append("\n")
                    .append("  config: ").append("\n")
                    .append("    mode: ipvs").append("\n");
            return sb.toString();
        }
    }
}
