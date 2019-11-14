package com.yinhai.cloud.module.resource.task.api;

import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeInitStepBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeOperateRecordBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.common.api.util.NetUtils;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-08-28 14:20:26
 */
public abstract class AbstractOperateTask {
    final protected List<String> localMachineIpList = CommonUtil.getCurrentMachineIp();
    final protected Integer serverPort = NetUtils.getServerPort();
    protected ConnVo currConn;
    protected NodeBasicInfoVo node;
    protected Long nodeId;
    protected Long operateId;
    protected String operateCode;
    protected IClusterBpo clusterBpo = ServiceLocator.getService(IClusterBpo.class);
    protected INodeBpo nodeBpo = ServiceLocator.getService(INodeBpo.class);
    protected INodeInitStepBpo nodeInitStepBpo = ServiceLocator.getService(INodeInitStepBpo.class);
    protected INodeOperateRecordBpo nodeOperateRecordBpo = ServiceLocator.getService(INodeOperateRecordBpo.class);

    public Long getNodeId() {
        return nodeId;
    }

    public final void initCheck() {
        if (nodeId == null || operateCode == null) {
            throw new IllegalArgumentException("nodeId and operateCode can't be empty!");
        }
        this.node = nodeBpo.queryNodeInfoById(nodeId);
        if (this.node == null) {
            throw new IllegalArgumentException("未找到nodeId=" + this.nodeId + "的节点信息");
        }
        this.currConn = NodeUtils.createConnFromNode(node);
    }

    public boolean needCheck() {
        return true;
    }

    public abstract boolean beforeExecute();


    public void executeFailed(Exception e) {
        String msg = CommonUtil.getExceptionMsgContent(e);
        nodeBpo.updateNodeOperateFailed(nodeId, operateId, msg);

    }


    public void successfullyExecuted() {
        nodeOperateRecordBpo.updateNodeOperateAsSuccess(this.operateId);

    }

    public abstract void onExecute() throws Exception;


}
