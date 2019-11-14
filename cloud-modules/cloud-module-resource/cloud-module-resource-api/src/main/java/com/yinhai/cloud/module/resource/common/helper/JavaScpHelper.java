package com.yinhai.cloud.module.resource.common.helper;

import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHFileTransferException;
import com.yinhai.cloud.core.api.ssh.SSHConnection;
import com.yinhai.cloud.core.api.ssh.SlashPath;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.module.resource.common.entity.ScpInfo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.core.app.api.util.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author: zhaokai
 * @create: 2018-08-21 17:07:16
 */
public class JavaScpHelper {
    private static final String RELATIVE_TMP_DIR_NAME = "tmp";
    private final static Logger logger = LoggerFactory.getLogger(JavaScpHelper.class);
    private INodeBpo nodeBpo = ServiceLocator.getService(INodeBpo.class);

    public void scpFileBetweenServer(ScpInfo scpInfo) throws SSHConnectionException, SSHFileTransferException {
        scpFileBetweenServer(scpInfo.getFromServerIp(),
                scpInfo.getFromPath(),
                scpInfo.getToServerIp(),
                scpInfo.getToPath()
        );
    }

    public void scpDirBetweenServer(ScpInfo scpInfo) throws SSHConnectionException, SSHFileTransferException {
        scpDirBetweenServer(scpInfo.getFromServerIp(),
                scpInfo.getFromPath(),
                scpInfo.getToServerIp(),
                scpInfo.getToPath()
        );
    }

    public void scpFileFromDockerRepo(ScpInfo scpInfo) throws SSHConnectionException, SSHFileTransferException {
        scpFileFromDockerRepo(scpInfo.getFromPath(),
                scpInfo.getToServerIp(),
                scpInfo.getToPath()
        );
    }

    private void scpFileBetweenServer(String fromServerIp, String fromFilePath, String toServerIp, String toFilePath) throws SSHConnectionException, SSHFileTransferException {
        NodeBasicInfoVo fromServerInfo = nodeBpo.queryNodeInfoByIP(fromServerIp);
        NodeBasicInfoVo toServerInfo = nodeBpo.queryNodeInfoByIP(toServerIp);

        SSHConnection fromServer = new SSHConnection(NodeUtils.createConnFromNode(fromServerInfo));
        SSHConnection toServer = new SSHConnection(NodeUtils.createConnFromNode(toServerInfo));


        File fromFile = new File(fromFilePath);
        File tmpFile = new File(RELATIVE_TMP_DIR_NAME, fromFile.getName());
        SlashPath toPath = new SlashPath(toFilePath);
        try {
            fromServer.openConnection();
            fromServer.downloadFileFromServer(fromFilePath, tmpFile.getAbsolutePath());
            toServer.openConnection();
            toServer.uploadFileToServer(new SlashPath(tmpFile).getFullPath(), toPath.getFullPath());
            logger.info("Successfully scp file From " + fromServerIp + ":" + fromFilePath + "to " + toServerIp + ":" + toFilePath + " !");
        } finally {
            fromServer.closeConnection();
            toServer.closeConnection();
        }

    }


    private void scpDirBetweenServer(String fromServerIp, String fromDirPath, String toServerIp, String toParentDir) throws SSHConnectionException, SSHFileTransferException {
        NodeBasicInfoVo fromServerInfo = nodeBpo.queryNodeInfoByIP(fromServerIp);
        NodeBasicInfoVo toServerInfo = nodeBpo.queryNodeInfoByIP(toServerIp);
        SSHConnection fromServer = new SSHConnection(NodeUtils.createConnFromNode(fromServerInfo));
        SSHConnection toServer = new SSHConnection(NodeUtils.createConnFromNode(toServerInfo));

        File fromDir = new File(fromDirPath);
        File localDirTmp = new File(RELATIVE_TMP_DIR_NAME, fromDir.getName());
        try {
            fromServer.openConnection();
            fromServer.downloadDir(fromDirPath, localDirTmp.getAbsolutePath());
            toServer.openConnection();
            toServer.uploadLocalDirToServer(localDirTmp.getAbsolutePath(), toParentDir);

            logger.info("Successfully scp dir From " + fromServerIp + ":" + fromDirPath + "to " + toServerIp + ":" + toParentDir + " !");
        } finally {
            fromServer.closeConnection();
            toServer.closeConnection();
        }

    }

    private void scpFileFromDockerRepo(String fromFilePath, String toServerIp, String toFilePath) throws SSHConnectionException, SSHFileTransferException {
        String fromServerIp = SystemConfigUtil.getSystemConfigValue("docker.private.repo.ip");
        Integer port = SystemConfigUtil.getSystemConfigValueToInteger("docker.private.repo.server.ssh.port", 22);
        String username = SystemConfigUtil.getSystemConfigValue("docker.private.repo.server.ssh.username");
        String password = SystemConfigUtil.getSystemConfigValue("docker.private.repo.server.ssh.password");
        NodeBasicInfoVo fromServerInfo = new NodeBasicInfoVo();
        fromServerInfo.setNodeIP(fromServerIp);
        fromServerInfo.setNodeSSHPort(port);
        fromServerInfo.setNodeUser(username);
        fromServerInfo.setNodePassword(password);
        NodeBasicInfoVo toServerInfo = nodeBpo.queryNodeInfoByIP(toServerIp);
        SSHConnection fromServer = new SSHConnection(NodeUtils.createConnFromNode(fromServerInfo));
        SSHConnection toServer = new SSHConnection(NodeUtils.createConnFromNode(toServerInfo));


        File fromFile = new File(fromFilePath);
        File tmpFile = new File(RELATIVE_TMP_DIR_NAME, fromFile.getName());
        SlashPath toPath = new SlashPath(toFilePath);
        try {
            fromServer.openConnection();
            fromServer.downloadFileFromServer(fromFilePath, tmpFile.getAbsolutePath());
            toServer.openConnection();
            toServer.uploadFileToServer(new SlashPath(tmpFile).getFullPath(), toPath.getFullPath());
            logger.info("Successfully scp file From " + fromServerIp + ":" + fromFilePath + "to " + toServerIp + ":" + toFilePath + " !");
        } finally {
            fromServer.closeConnection();
            toServer.closeConnection();
        }
    }

}
