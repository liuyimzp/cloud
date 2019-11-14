package com.yinhai.cloud.module.resource.nodes.impl.dao;

import com.yinhai.cloud.module.resource.nodes.impl.po.NodeInitializeStepPo;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-06-11 16:45:58
 */
public interface INodeInitializeStepDao {

    void saveNewInitializeStepForNode(NodeInitializeStepPo step);

    List<NodeInitializeStepPo> getNodeInitializeStepList(Long nodeId);

    void deleteStepByNodeId(Long nodeId);

    void updateStep(NodeInitializeStepPo step);

    NodeInitializeStepPo getNodeInitializeStep(Long nodeId, Integer stepOrder);

    /**
     * 将日志状态修改为失败
     * @param stepPo
     */
    void setFalid(NodeInitializeStepPo stepPo);
}
