package com.yinhai.cloud.core.api.handler;

import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;

/**
 * @author: zhaokai
 * @create: 2018-08-14 11:15:30
 */
public interface SSHExecRunningHandler {

    void handle(AbstractCommand cmd, String outOneLineLog);
}
