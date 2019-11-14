package com.yinhai.cloud.module.monitor.api.bpo;

import com.yinhai.cloud.module.monitor.api.vo.NodeMonitorVo;

import java.util.Date;
import java.util.List;

/**
 * Created by pengwei on 2018/9/21.
 */
public interface INodeMonitorBpo {

    List<NodeMonitorVo> getNodeMonitorInfoByClusterId(Long clusterId);

    void insertNodeMonitorData(NodeMonitorVo nodeMonitorVo);

    /**
     * 删除节点时删除相关的监控数据
     *
     * @param nodeId
     */
    void deleteNodeMonitorData(Long nodeId);

    /*void deleteExpireMonitorData(Date biztime);*/
}
