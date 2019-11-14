package com.yinhai.cloud.module.resource.util;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadContentCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.core.api.vo.LinuxCommandFactory;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.kubernetes.pv.ClusterRoleBindObject;
import com.yinhai.cloud.module.resource.kubernetes.pv.NfsClientProvDeployment;
import com.yinhai.cloud.module.resource.kubernetes.pv.ServiceAccountObject;
import com.yinhai.cloud.module.resource.kubernetes.pv.StorageClassObject;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.pv.api.vo.PvCommandFactory;
import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.cloud.module.resource.pv.api.vo.StorageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pengwei on 2018/8/20.
 * nfs 动态存储卷控制器
 * 所有控制器都在default命名空间下创建
 */

public class NfsClientProvisionerYaml {

    private final static Logger logger = LoggerFactory.getLogger(NfsClientProvisionerYaml.class);
    /**
     *
     * @param pvVo
     * @param storageVo
     * @return
     * @throws Exception
     */
    public static MsgVO createNfsClientProvisioner(NodeBasicInfoVo masterNode, PvVo pvVo, StorageVo storageVo) throws Exception {
        if (storageVo != null) {
            ConnVo connFromStotageVo = NodeUtils.createConnFromStotageVo(storageVo);
            ConsoleCommand mkdir = PvCommandFactory.createNfsClientProvisioner(pvVo.getVolumeStoragePath());
            ConsoleCommand chown = LinuxCommandFactory.changeOwnerAsCurrentUser(pvVo.getVolumeStoragePath(), connFromStotageVo.getUser(), connFromStotageVo.getUser());
            CommonSshExecUtil.exec(connFromStotageVo, mkdir, chown);
        }

        //创建sa
        ServiceAccountObject serviceAccountYaml = new ServiceAccountObject(pvVo.getVolumeUuid(), ServerCmdConstant.NFS_CLIENT_DEFAULT_NAMESPACES);
        //创建角色绑定
        ClusterRoleBindObject clusterRoleBindYaml = new ClusterRoleBindObject(pvVo.getVolumeUuid(), ServerCmdConstant.NFS_CLIENT_DEFAULT_NAMESPACES);
        //创建控制器
        NfsClientProvDeployment provisionerYaml = null;
        if(storageVo == null || "".equals(storageVo)){
            provisionerYaml = new NfsClientProvDeployment(pvVo.getVolumeUuid(), ServerCmdConstant.NFS_CLIENT_DEFAULT_NAMESPACES, null, pvVo.getVolumeStoragePath());
        }else{
            provisionerYaml = new NfsClientProvDeployment(pvVo.getVolumeUuid(), ServerCmdConstant.NFS_CLIENT_DEFAULT_NAMESPACES, storageVo.getHostIp(), pvVo.getVolumeStoragePath());
        }
        //创建存储类
        StorageClassObject storageClassYaml = new StorageClassObject(pvVo.getVolumeUuid());

        String serverPath = ServerCmdConstant.NFS_CLIENT_PROV_YAML_DIR + pvVo.getVolumeUuid();
        ConnVo masterConn = NodeUtils.createConnFromNode(masterNode);
        ConsoleCommand makePvServerPath = PvCommandFactory.makePvServerPath(pvVo.getVolumeUuid());
        //上传yaml文件
        UploadContentCommand uploadSaYaml = new UploadContentCommand(serverPath + "/" + ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_SA, serviceAccountYaml.buildYaml());

        UploadContentCommand uploadClusterRoleBindYaml = new UploadContentCommand(serverPath + "/" + ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_CLUSTERROLEBIND, clusterRoleBindYaml.buildYaml());

        UploadContentCommand uploadProvisionerYaml = new UploadContentCommand(serverPath + "/" + ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_PROVISIONER, provisionerYaml.buildYaml());

        UploadContentCommand uploadStorageClassYaml = new UploadContentCommand(serverPath + "/" + ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_STORAGECLASS, storageClassYaml.buildYaml());

        ConsoleCommand changeOwnerAsMasterUser = LinuxCommandFactory.changeOwnerAsCurrentUser(ServerCmdConstant.NFS_CLIENT_PROV_YAML_DIR, masterConn.getUser(), masterConn.getUser());

        CommonSshExecUtil.exec(masterConn, makePvServerPath, changeOwnerAsMasterUser, uploadSaYaml, uploadClusterRoleBindYaml, uploadProvisionerYaml, uploadStorageClassYaml);

        //创建nfs-client-provisioner命令, 为防止异常造成数据残留，在创建时需要先删除再创建。删除和创建需要注意组件顺序
        ConsoleCommand deleteNfsCp = PvCommandFactory.deleteNfsClientProvisioner(serverPath,
                ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_SA,
                ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_CLUSTERROLEBIND,
                ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_PROVISIONER,
                ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_STORAGECLASS);

        ConsoleCommand createNfsCp = PvCommandFactory.createNfsClientProvisioner(serverPath,
                ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_SA,
                ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_CLUSTERROLEBIND,
                ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_PROVISIONER,
                ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_STORAGECLASS);

        try {
            try {
                CommonSshExecUtil.exec(masterConn, deleteNfsCp);
            } catch (SSHExecuteException e) {
                logger.error(logger.getName() + "context",e);
            }
            return CommonSshExecUtil.exec(masterConn, createNfsCp).get(createNfsCp);
        } catch (SSHExecuteException | SSHConnectionException e) {
            MsgVO msgVO = new MsgVO();
            msgVO.setSuccess(false);
            msgVO.setSysoutMsg(e.getMessage());
            return msgVO;
        }

    }

    /**
     * 删除nfs-client控制器
     *
     * @param masterNode
     * @param pv
     * @param storage
     * @return
     * @throws Exception
     */
    public static MsgVO deleteNfsClientProvisioner(NodeBasicInfoVo masterNode, PvVo pv, StorageVo storage) throws Exception {
        MsgVO msgVO = new MsgVO();
        msgVO.setSuccess(true);
        ConnVo conn = NodeUtils.createConnFromNode(masterNode);
        ConsoleCommand deleteCmd = PvCommandFactory.deleteNfsClientProvisioner(pv.getVolumeUuid());
        try {
            CommonSshExecUtil.exec(conn, deleteCmd);
        } catch (SSHExecuteException | SSHConnectionException e) {
            msgVO.setSuccess(false);
            msgVO.setSysoutMsg("执行删除存储卷时出错！" + e.getMessage());
            return msgVO;
        }

        //TODO 控制策略：删除时备份已有的存储卷池，设置目录名+时间戳方式，采用延时删除策略（T+7）
   /*     if (storage != null) {
            ConnVo nfsServerConn = NodeUtils.createConnFromStotageVo(storage);
            CommonSshExecUtil.exec(nfsServerConn, PvCommandFactory.backupPvStoragePool(pv.getVolumeStoragePath()));
        }*/

        return msgVO;
    }

}
