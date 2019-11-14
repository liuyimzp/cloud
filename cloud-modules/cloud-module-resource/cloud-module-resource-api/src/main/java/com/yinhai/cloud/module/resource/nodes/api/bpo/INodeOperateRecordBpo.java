package com.yinhai.cloud.module.resource.nodes.api.bpo;

import com.yinhai.cloud.module.resource.nodes.api.vo.NodeOperateVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.OperateRunningServer;
import com.yinhai.core.common.api.base.IPage;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-08-28 15:11:56
 */
public interface INodeOperateRecordBpo {

    /**
     * 新增一条操作记录，并更新节点状态
     *
     * @param nodeId
     * @param operateCode
     * @param operateRunningServer
     * @return
     */
    Long addNewNodeOperateRecord(Long nodeId, String operateCode, OperateRunningServer operateRunningServer);


    void updateNodeOperatePercentage(Long operateId, int basePer);

    void appendOperateLog(Long operateId, String log);

    void updateNodeOperateAsFailed(Long operateId, String message);

    void updateNodeOperateAsSuccess(Long operateId);

    /**
     * 查询节点操作记录，带分页
     *
     * @param nodeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    IPage<NodeOperateVo> getNodeOperateStackWithPage(Long nodeId, Integer currentPage, Integer pageSize);


    /**
     * 查询节点所有记录不分页
     * @param nodeId
     * @return
     */
    List<NodeOperateVo> getOperateListByNodeId(Long nodeId);
}
