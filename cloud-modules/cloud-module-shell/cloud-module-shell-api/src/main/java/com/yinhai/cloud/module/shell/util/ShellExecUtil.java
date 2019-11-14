package com.yinhai.cloud.module.shell.util;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.exec.ShellUid;
import com.yinhai.cloud.core.api.handler.SSHExecResultHandler;
import com.yinhai.cloud.core.api.handler.SSHExecRunningHandler;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.core.api.ssh.command.ShellCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.shell.api.IShellBpo;
import com.yinhai.cloud.module.shell.vo.ExecShell;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @author jianglw
 */
public class ShellExecUtil {

    private static IShellBpo shellBpo = ServiceLocator.getService(IShellBpo.class);


    /**
     * 不带处理接口，在默认服务器路径执行脚本
     *
     * @param conn
     * @param shell
     * @return
     * @throws SSHExecuteException
     * @throws SSHConnectionException
     */
    public static MsgVO exec(final ConnVo conn, final ExecShell shell) throws SSHExecuteException, SSHConnectionException {
        return exec(null, null, conn, shell);

    }

    /**
     * 在默认服务器路径执行脚本k8s.server.root/bin
     *
     * @param runningHandler
     * @param resultHandler
     * @param conn
     * @param shell
     * @return
     * @throws SSHExecuteException
     * @throws SSHConnectionException
     */
    public static MsgVO exec(final SSHExecRunningHandler runningHandler, final SSHExecResultHandler resultHandler, final ConnVo conn, final ExecShell shell) throws SSHExecuteException, SSHConnectionException {
        final String shellWorkDir = SystemConfigUtil.getSystemConfigValue("k8s.server.root") + "/bin";
        return exec(runningHandler, resultHandler, shellWorkDir, conn, shell);
    }

    /**
     * 在服务器上运行脚本
     *
     * @param runningHandler 运行过程处理接口
     * @param resultHandler  运行结束处理接口
     * @param shellWorkDir   运行脚本所在服务器目录
     * @param conn           SSH连接信息
     * @param shell          Shell信息
     * @return
     * @throws SSHExecuteException
     * @throws SSHConnectionException
     */
    public static MsgVO exec(final SSHExecRunningHandler runningHandler, final SSHExecResultHandler resultHandler, final String shellWorkDir, final ConnVo conn, final ExecShell shell) throws SSHExecuteException, SSHConnectionException {
        final ShellCommand shellCmd = new ShellCommand();
        shellCmd.setShellContent(getShell(shell.getShellUid()));
        final String shellName = CodeTableUtil.getDesc(ShellUid.SHELL_UID, shell.getShellUid());
        shellCmd.setShellName(shellName);
        shellCmd.appendShellParam(shell.getParams());
        shellCmd.setShellServerWorkDir(shellWorkDir);
        shellCmd.setWithSudo(shell.isWithSudo());
        HashMap<AbstractCommand, MsgVO> execResult = CommonSshExecUtil.exec(runningHandler, resultHandler, conn, shellCmd);
        return execResult.get(shellCmd);
    }

    /**
     * 获取指定脚本的内容
     *
     * @param uid
     * @return
     */
    public static String getShell(String uid) {
        return shellBpo.queryAllShell().stream().filter(it -> uid.equals(it.getShellUid())).collect(Collectors.toList()).get(0).getShellContent().replaceAll("\r\n", "\n");
    }

}
