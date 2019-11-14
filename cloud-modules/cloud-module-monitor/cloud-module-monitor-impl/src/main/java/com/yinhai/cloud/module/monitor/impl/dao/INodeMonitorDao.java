package com.yinhai.cloud.module.monitor.impl.dao;

import com.yinhai.cloud.module.monitor.impl.po.NodeMonitorPo;

import java.util.List;

/**
 * Created by pengwei on 2018/9/21.
 */
public interface INodeMonitorDao {
    List<NodeMonitorPo> quaryNodeMonitorDataByNodeId(Long nodeId);

    void insertNodeMonitorData(NodeMonitorPo nodeMonitorPo);

    /**
     * 删除节点时删除相关的监控数据
     *
     * @param nodeId
     */
    void deleteNodeMonitorData(Long nodeId);
}
