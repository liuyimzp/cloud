package com.yinhai.cloud.module.resource.pv.api.vo;

import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;

import java.io.File;

/**
 * @author: zhaokai
 * @create: 2018-09-05 13:59:19
 */
public class PvCommandFactory {


    public static ConsoleCommand addMountPath(String rootPath) {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "mkdir -p " + rootPath,
                "bash -c 'echo \"" + rootPath + " *(rw,no_root_squash,no_all_squash,sync)\" >> /etc/exports'",
                "source ~/.bash_profile && exportfs -r");

        return cmd;


    }

    public static ConsoleCommand makeNfsInstallShellDir() {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "mkdir -p " + ServerCmdConstant.INSTALL_SHELL_DIR

        );
        cmd.setWithSudo(true);
        return cmd;

    }


    public static ConsoleCommand runNfsInstallShell(String hostRootPath) {

        String serverPath = ServerCmdConstant.INSTALL_SHELL_DIR + ServerCmdConstant.FILE_SEPARATOR + ServerCmdConstant.INSTALL_NFS_SHELLNAME;
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "sh " + serverPath + " " + hostRootPath, "rm -rf " + serverPath
        );
        return cmd;

    }


    public static ConsoleCommand makePvServerPath(String volumeUuid) {
        String serverPath = ServerCmdConstant.NFS_CLIENT_PROV_YAML_DIR + volumeUuid;

        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "mkdir -p " + serverPath
        );
        cmd.setWithSudo(true);
        return cmd;

    }


    public static ConsoleCommand backupPvStoragePool(String volumeStoragePath) {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "mv " + volumeStoragePath + " " + volumeStoragePath + ServerCmdConstant.BACKUP_DIRECTORY_SUFFIX
        );
        cmd.setWithSudo(true);
        return cmd;

    }

    public static ConsoleCommand createNfsClientProvisioner(String volumeStoragePath) {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("mkdir -p " + volumeStoragePath);
        cmd.setWithSudo(true);
        return cmd;

    }

    public static ConsoleCommand deleteNfsClientProvisioner(String volumeUuid) {
        String serverPath = ServerCmdConstant.NFS_CLIENT_PROV_YAML_DIR + volumeUuid;
        StringBuilder deleteCmd = new StringBuilder("kubectl delete -f ");
        deleteCmd.append(ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_STORAGECLASS).append(",")
                .append(ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_PROVISIONER).append(",")
                .append(ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_CLUSTERROLEBIND).append(",")
                .append(ServerCmdConstant.NFS_YAML_PREFIX + ServerCmdConstant.NFS_TEMPLATE_SA);

        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "source ~/.bash_profile", "cd " + serverPath, deleteCmd.toString(), "rm -rf " + serverPath
        );
        return cmd;

    }

    public static ConsoleCommand deleteNfsClientProvisioner(String serverPath, String saYamlName, String clusterRoleBindYamlName, String provisionerYamlName, String storageClassYamlName) {
        StringBuilder deleteCmd = new StringBuilder("kubectl delete -f ");
        deleteCmd.append(storageClassYamlName).append(",")
                .append(provisionerYamlName).append(",")
                .append(clusterRoleBindYamlName).append(",")
                .append(saYamlName);

        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                " source ~/.bash_profile", "cd " + serverPath, deleteCmd.toString()

        );
        return cmd;


    }


    public static ConsoleCommand createNfsClientProvisioner(String serverPath, String saYamlName, String clusterRoleBindYamlName, String provisionerYamlName, String storageClassYamlName) {


        StringBuilder createCmd = new StringBuilder("kubectl create -f ");
        createCmd.append(saYamlName).append(",")
                .append(clusterRoleBindYamlName).append(",")
                .append(provisionerYamlName).append(",")
                .append(storageClassYamlName);

        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                " source ~/.bash_profile", "cd " + serverPath, createCmd.toString()

        );
        return cmd;
    }


}
