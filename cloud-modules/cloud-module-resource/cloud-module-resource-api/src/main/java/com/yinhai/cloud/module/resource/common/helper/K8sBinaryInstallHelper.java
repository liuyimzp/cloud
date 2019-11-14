package com.yinhai.cloud.module.resource.common.helper;

import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.exception.NodeInitRuntimeException;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.exception.SSHFileTransferException;
import com.yinhai.cloud.core.api.exec.ShellUid;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadDirCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.K8sInstallCommandFactory;
import com.yinhai.cloud.module.resource.common.entity.ScpInfo;
import com.yinhai.cloud.module.resource.handler.InstallLogHandler;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.shell.util.ShellExecUtil;
import com.yinhai.cloud.module.shell.vo.ExecShell;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * @author: tinahy
 * @create: 2019-10-10
 */
public class K8sBinaryInstallHelper extends K8sInstallHelper {
    private final static Logger logger = LoggerFactory.getLogger(K8sBinaryInstallHelper.class);
    private static final String KUBERNETES_VERSION = "v1.15.3";

    /**
     * 唯一构造函数
     *
     * @param nodeId    当前安装节点Id
     * @param operateId 当前操作记录Id
     */
    public K8sBinaryInstallHelper(Long nodeId, Long operateId) {
        super(nodeId, operateId);
    }

    void k8sInstallStep_1_() {
        copyExistedFileFromDockerRepo();
    }

    void k8sInstallStep_2_() {
        initNodeSystemEnvironment();
    }

    void k8sInstallStep_3_() {
        nodeInitStepBpo.appendStepLogAndUpdateFinishPercentage(currentNodeId, currentStepOrder, "二进制安装跳过私有yum源配置", 1000);
    }

    void k8sInstallStep_4_() {
        nodeInitStepBpo.appendStepLogAndUpdateFinishPercentage(currentNodeId, currentStepOrder, "二进制安装跳过依赖工具配置", 100);
    }

    void k8sInstallStep_5_() {
        configNodesNtpSyncForNewNode();
    }

    void k8sInstallStep_6_() {
        installDockerByBinary();
    }

    void k8sInstallStep_7_() {
        installKubernetesByBinary();
    }

    void k8sInstallStep_8_() {
        initK8sForNode();
    }

    void k8sInstallStep_9_() {
        if (ValidateUtil.isNotEmpty( clusterBpo.getClusterById(currentNode.getClusterId()).getClusterHaVirtualIP())){
            //configHAproxyAndKeepalived();
        }
        try {
            run_docker_web_console();
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
    }

    private void copyExistedFileFromDockerRepo() {
        try {
            final String k8sServerRoot = SystemConfigUtil.getSystemConfigValue("k8s.server.root");
            final ConsoleCommand cmdMkDirVo = K8sInstallCommandFactory.makeServerDirCmdVo(k8sServerRoot, currentNode.getNodeUser(), currentNode.getNodeUser());
            CommonSshExecUtil.exec(currentConn, cmdMkDirVo);
            nodeInitStepBpo.appendStepLogAndUpdateFinishPercentage(currentNodeId, currentStepOrder, "完成文件夹创建", 10);
            String workDir = k8sServerRoot + "/dependency/sh";
            final String k8sLocalRoot = new File(K8sInstallHelper.class.getResource("/").getFile(), SystemConfigUtil.getSystemConfigValue("k8s.local.root")).getAbsolutePath();
            final UploadDirCommand cmdUploadConf = new UploadDirCommand(new File(k8sLocalRoot, SystemConfigUtil.getSystemConfigValue(ConfigPropKey.CMD_UPLOAD_CONF_PATH)).getAbsolutePath(), "" + k8sServerRoot + "/dependency");
            CommonSshExecUtil.exec(currentConn, cmdUploadConf);
            ShellExecUtil.exec(null, null, workDir, currentConn, new ExecShell(ShellUid.FUNCTIONS_SH, false));
            nodeInitStepBpo.appendStepLogAndUpdateFinishPercentage(currentNodeId, currentStepOrder, "完成配置文件和依赖函数上传", 30);
            String[] fileArr = new String[]{"k8sBinaryInstall.tgz"};
            for (String a : fileArr) {
                ScpInfo scpInfo = new ScpInfo();
                scpInfo.setFromPath(k8sServerRoot + a);
                scpInfo.setToServerIp(currentNode.getNodeIP());
                scpInfo.setToPath(k8sServerRoot + a);
                try {
                    new JavaScpHelper().scpFileFromDockerRepo(scpInfo);
                } catch (SSHConnectionException | SSHFileTransferException e) {
                    throw new NodeInitRuntimeException(e);
                }
            }
            nodeInitStepBpo.appendStepLogAndUpdateFinishPercentage(currentNodeId, currentStepOrder, "完成二进制安装文件拷贝", 80);
            final ConsoleCommand cmdUncompressVo = K8sInstallCommandFactory.uncompressBinaryFileCmdVo(k8sServerRoot, "k8sBinaryInstall.tgz");
            nodeInitStepBpo.appendStepLogAndUpdateFinishPercentage(currentNodeId, currentStepOrder, "完成二进制安装文件解压", 100);
            CommonSshExecUtil.exec(currentConn, cmdUncompressVo);
        } catch (SSHConnectionException | SSHExecuteException e) {
            throw new NodeInitRuntimeException(e);
        }

    }

    private void installDockerByBinary() {
        try {
            ShellExecUtil.exec(new InstallLogHandler(currentNode, currentStepOrder, 0, 100), null, currentConn, new ExecShell(ShellUid.INSTALL_DOCKER_BINARY, true)
                .appendParam(privateDockerRepoIp + ":" + privateDockerRepoPort));
        } catch (Exception e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    private void installKubernetesByBinary() {
        try {
            ShellExecUtil.exec(new InstallLogHandler(currentNode, currentStepOrder, 0, 100), null, currentConn, new ExecShell(ShellUid.INSTALL_KUBERNETES_BINARY, true));
        } catch (Exception e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    @Override
    protected String getKubeadmInitConfig(){
        String clusterHaVirtualIP = clusterBpo.getClusterById(currentNode.getClusterId()).getClusterHaVirtualIP();
        String nodeIp = currentNode.getNodeIP();
        List<NodeBasicInfoVo> otherInitDoneMasterNodes = nodeBpo.queryOtherApiServerInitDoneMasterNodes(currentNode.getClusterId(), currentNodeId);
        if (ValidateUtil.isEmpty(clusterHaVirtualIP) && ValidateUtil.isNotEmpty(otherInitDoneMasterNodes)){
            nodeIp = otherInitDoneMasterNodes.get(0).getNodeIP();
        }
        KubernetesMasterConfiguration kubernetesMasterConfiguration = new KubernetesMasterConfiguration(nodeIp, KUBERNETES_VERSION);
        return kubernetesMasterConfiguration.toString();
    }

    private static class KubernetesMasterConfiguration {
        private String advertiseAddress;
        private String version;

        public KubernetesMasterConfiguration(String advertiseAddress, String version) {
            this.advertiseAddress = advertiseAddress;
            this.version = version;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("apiVersion: kubeadm.k8s.io/v1beta2").append("\n")
                    .append("kind: ClusterConfiguration").append("\n")
                    .append("controlPlaneEndpoint: ").append(advertiseAddress).append("\n")
                    .append("kubernetesVersion: ").append(version).append("\n")
                    .append("networking:").append("\n")
                    .append("  serviceSubnet: 10.96.0.0/16").append("\n")
                    .append("  podSubnet: 10.244.0.0/16").append("\n")
                    .append("  dnsDomain: cluster.local").append("\n")
                    .append("apiServerCertSANs: ").append("\n")
                    .append("  - ").append(advertiseAddress).append("\n")
                    .append("  - 127.0.0.1").append("\n")
                    .append("  - 10.244.0.1").append("\n");
            sb.append("kubeProxy: ").append("\n")
                    .append("  config: ").append("\n")
                    .append("    mode: ipvs").append("\n");
            return sb.toString();
        }
    }
}
