package com.yinhai.cloud.module.resource.handler;

import com.yinhai.cloud.core.api.handler.SSHExecRunningHandler;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeInitStepBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeOperateRecordBpo;
import com.yinhai.core.app.api.util.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhaokai
 * @create: 2018-08-21 10:17:02
 */
public class AppendLogToOperateRecordHandler implements SSHExecRunningHandler {
    private static final String SHELL_RUNNING_LOG_PREFIX = "RunningLog:";
    private final static Logger logger = LoggerFactory.getLogger(AppendLogToOperateRecordHandler.class);
    private INodeInitStepBpo nodeInitStepBpo = ServiceLocator.getService(INodeInitStepBpo.class);
    private INodeBpo nodeBpo = ServiceLocator.getService(INodeBpo.class);
    private INodeOperateRecordBpo nodeOperateRecordBpo = ServiceLocator.getService(INodeOperateRecordBpo.class);

    private Long operateId;


    public AppendLogToOperateRecordHandler(Long operateId) {

        this.operateId = operateId;

    }

    @Override
    public void handle(AbstractCommand cmd, String line) {
        String[] lines = line.split("\n+");
        for (String l : lines) {
            parseLine(cmd, l);
        }
    }

    private void parseLine(AbstractCommand cmd, String line) {
        if (line == null) {
            return;
        } else if (line.startsWith(SHELL_RUNNING_LOG_PREFIX)) {
            // 记录日志
            String log = line.substring(SHELL_RUNNING_LOG_PREFIX.length());
            nodeOperateRecordBpo.appendOperateLog(operateId, log);
        } else {
            if (!"".equals(line)) {
                nodeOperateRecordBpo.appendOperateLog(operateId, line);
            }
        }


    }


}
