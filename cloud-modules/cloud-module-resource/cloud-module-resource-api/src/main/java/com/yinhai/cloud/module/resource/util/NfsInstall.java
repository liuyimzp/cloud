package com.yinhai.cloud.module.resource.util;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.ShellCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.core.api.vo.LinuxCommandFactory;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.pv.api.vo.PvCommandFactory;
import com.yinhai.cloud.module.resource.pv.api.vo.StorageVo;

/**
 * Created by pengwei on 2018/6/21.
 * update by yanglq on 2018/2/20
 */
public class NfsInstall {

    /**
     * 判断是否有安装NFS
     * @param storage
     * @return
     * @throws Exception
     */
    public static boolean existNfs(StorageVo storage) throws Exception{
        boolean exits = true;
        ConnVo conn = NodeUtils.createConnFromStotageVo(storage);
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("exportfs");
        try{
            CommonSshExecUtil.exec(conn,cmd);
        }catch (SSHExecuteException e){
            exits = false;
        }
        return exits;
    }

    /**
     * 判断节点nfs是否启动
     * @param storage
     * @return
     * @throws Exception
     */
    public static boolean activeNfs(StorageVo storage) throws Exception{
        boolean exits = false;
        ConnVo conn = NodeUtils.createConnFromStotageVo(storage);
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("service nfs status|grep -E 'dead|inactive'");
        try{
            MsgVO msgVO = CommonSshExecUtil.exec(conn,cmd).get(cmd);
            String[] resourceInfo = msgVO.getSysoutMsg().split("\\n");
        }catch (SSHExecuteException e){
            exits = true;
        }
        return exits;
    }

    /**
     * 安装NFS
     *
     * @param storage
     * @return
     * @throws Exception
     */
    public static MsgVO installNfsServer(StorageVo storage) throws Exception {
        ConnVo conn = NodeUtils.createConnFromStotageVo(storage);
        String shellContent = createInstallShellContent();
        ConsoleCommand mkInstallDir = PvCommandFactory.makeNfsInstallShellDir();
        mkInstallDir.appendCommand("mkdir -p "+storage.getHostRootPath());
        ShellCommand shell = new ShellCommand();
        shell.setShellContent(shellContent);
        shell.setShellName(ServerCmdConstant.INSTALL_NFS_SHELLNAME);
        shell.setShellServerWorkDir(ServerCmdConstant.INSTALL_SHELL_DIR);
        shell.appendShellParam(storage.getHostRootPath());
        shell.setWithSudo(true);
        ConsoleCommand changeOwnerAsCurrentUser = LinuxCommandFactory.changeOwnerAsCurrentUser(ServerCmdConstant.INSTALL_SHELL_DIR, storage.getHostUserName(), storage.getHostUserName());
        ConsoleCommand changeRootPathOwnerAsCurrentUser = LinuxCommandFactory.changeOwnerAsCurrentUser(storage.getHostRootPath(), storage.getHostUserName(), storage.getHostUserName());
        return CommonSshExecUtil.exec(conn, mkInstallDir, changeOwnerAsCurrentUser, shell, changeRootPathOwnerAsCurrentUser).get(shell);
    }

    /**
     * 添加挂载目录
     *
     * @param storage
     * @return
     * @throws Exception
     */
    public static MsgVO addMountPath(StorageVo storage) throws Exception {
        ConnVo conn = NodeUtils.createConnFromStotageVo(storage);
        ConsoleCommand cmd = PvCommandFactory.addMountPath(storage.getHostRootPath());
        return CommonSshExecUtil.exec(conn, cmd).get(cmd);
    }

    /**
     * 生成安装nfs脚本
     *
     * @return
     * @throws Exception
     */
    private static String createInstallShellContent() throws Exception {


        StringBuffer installNfsStr = new StringBuffer();
        installNfsStr.append("#!/bin/sh\n\n")
                .append("user=`whoami`\n")
                .append("if [ \"$user\" = \"root\" ]; then\n")
                .append("  echo y|yum erase nfs-utils\n")
                .append("  yum install -y nfs-utils\n")
                .append("  if [ $? -eq 0 ] ;then\n")
                .append("    echo \"$1 *(rw,no_root_squash,no_all_squash,sync)\" > /etc/exports\n")
                .append("    systemctl enable rpcbind && systemctl enable nfs-server\n")
                .append("    systemctl start rpcbind && systemctl start nfs-server\n")
                .append("    exportfs -arv\n")
                .append("    echo \"安装NFS SERVER成功\"\n")
                .append("    exit 0;\n")
                .append("  else\n")
                .append("    echo \"安装NFS服务出现异常\"\n")
                .append("    exit 1;\n")
                .append("  fi\n")
                .append("else\n")
                .append("  echo \"请使用ROOT用户进行安装\"\n")
                .append("  exit 2;\n")
                .append("fi\n");


        return installNfsStr.toString();
    }

    /**
     * 删除挂载目录
     * @param storage
     * @return
     * @throws Exception
     */
    public static MsgVO removeMountPath(StorageVo storage) throws SSHExecuteException, SSHConnectionException {
        ConnVo conn = NodeUtils.createConnFromStotageVo(storage);
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("sed -i '/^"+storage.getHostRootPath().replaceAll("/","\\\\/")+"/d' /etc/exports","exportfs -r");
        return CommonSshExecUtil.exec(conn, cmd).get(cmd);
    }

    /**
     * 启动nfs服务
     * @param storage
     * @return
     * @throws Exception
     */
    public static void restartNfs(StorageVo storage) throws Exception {
        ConnVo conn = NodeUtils.createConnFromStotageVo(storage);
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("systemctl restart nfs.service");
        CommonSshExecUtil.exec(conn, cmd);
    }

    /**
     * 停止nfs服务
     * @param storage
     * @return
     * @throws Exception
     */
    public static void stopNfs(StorageVo storage) throws Exception {
        ConnVo conn = NodeUtils.createConnFromStotageVo(storage);
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("systemctl stop nfs.service");
        CommonSshExecUtil.exec(conn, cmd);
    }

}
