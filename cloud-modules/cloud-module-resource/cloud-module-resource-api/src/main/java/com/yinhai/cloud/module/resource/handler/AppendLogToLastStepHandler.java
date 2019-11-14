package com.yinhai.cloud.module.resource.handler;

import com.yinhai.cloud.core.api.handler.SSHExecRunningHandler;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeInitStepBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeOperateRecordBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.core.app.api.util.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhaokai
 * @create: 2018-08-21 10:17:02
 */
public class AppendLogToLastStepHandler implements SSHExecRunningHandler {
    private static final String SHELL_RUNNING_LOG_PREFIX = "RunningLog:";
    private final static Logger logger = LoggerFactory.getLogger(AppendLogToLastStepHandler.class);
    private INodeInitStepBpo nodeInitStepBpo = ServiceLocator.getService(INodeInitStepBpo.class);
    private INodeBpo nodeBpo = ServiceLocator.getService(INodeBpo.class);
    private INodeOperateRecordBpo nodeOperateRecordBpo = ServiceLocator.getService(INodeOperateRecordBpo.class);
    private NodeBasicInfoVo currentNode;
    private Long currentNodeId;
    private Integer currentStepOrder;


    public AppendLogToLastStepHandler(NodeBasicInfoVo currentNode) {

        this.currentNode = currentNode;
        this.currentNodeId = this.currentNode.getNodeId();
        this.currentStepOrder = currentStepOrder;

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
            nodeInitStepBpo.appendLogToLastStep(currentNodeId, log);
        } else {
            if (!"".equals(line)) {
                nodeInitStepBpo.appendLogToLastStep(currentNodeId, line);
            }
        }
    }


}
