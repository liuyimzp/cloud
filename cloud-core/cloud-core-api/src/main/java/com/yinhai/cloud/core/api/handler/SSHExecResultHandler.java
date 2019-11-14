package com.yinhai.cloud.core.api.handler;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;

/**
 * @author: zhaokai
 * @create: 2018-08-08 10:33:25
 */
public interface SSHExecResultHandler {
    void handleMessage(AbstractCommand cmd, MsgVO execResult);
}
