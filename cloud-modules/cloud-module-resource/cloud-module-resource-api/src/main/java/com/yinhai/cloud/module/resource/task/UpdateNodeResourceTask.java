package com.yinhai.cloud.module.resource.task;

import com.yinhai.cloud.module.resource.task.api.AbstractOperateTask;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhaokai
 * @create: 2018-08-28 17:32:10
 */
public class UpdateNodeResourceTask extends AbstractOperateTask {
    private final static Logger logger = LoggerFactory.getLogger(UpdateNodeResourceTask.class);

    public UpdateNodeResourceTask(Long nodeId) {
        this.nodeId = nodeId;
    }


    @Override
    public boolean needCheck() {
        return false;
    }

    @Override
    public boolean beforeExecute() {
        if (nodeId == null) {
            throw new IllegalArgumentException("nodeId  can't be empty!");
        }
        this.node = nodeBpo.queryNodeInfoById(nodeId);
        if (this.node == null) {
            throw new IllegalArgumentException("未找到nodeId=" + this.nodeId + "的节点信息");
        }
        this.currConn = NodeUtils.createConnFromNode(node);
        return true;
    }

    @Override
    public void executeFailed(Exception e) {
        return;
    }

    @Override
    public void successfullyExecuted() {
        logger.info("更新节点 " + node.getNodeIP() + " 资源信息完成！");
    }

    @Override
    public void onExecute() throws Exception {
        ServerUtils.updateNodeResource(node);
    }
}
