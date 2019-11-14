package com.yinhai.cloud.core.api.vo;

import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;

/**
 * @author: zhaokai
 * @create: 2018-09-06 11:57:22
 */
public class K8sInstallCommandFactory {
    public static ConsoleCommand makeServerDirCmdVo(String rootDir,String user,String group) {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("mkdir -p " + rootDir + "/bin && mkdir -p " + rootDir + "/dependency/sh &&  mkdir -p " + rootDir + "/dependency/conf &&  mkdir -p " + rootDir + "/dependency/rpm/createrepo"
        ).appendCommand("chown -R "+user+":"+group+" "+rootDir );
        cmd.setWithSudo(true);
        return cmd;


    }

    public static ConsoleCommand uncompressBinaryFileCmdVo(String rootDir,String fileName) {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("tar -zvxf " + rootDir + fileName + " -C " + rootDir);
        cmd.setWithSudo(true);
        return cmd;
    }

    public static ConsoleCommand configMasterAsDeployNodeCmdVo(String hostname) {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("source ~/.bash_profile && kubectl taint nodes " + hostname.toLowerCase() + " node-role.kubernetes.io/master-");
        return cmd;
    }
}
