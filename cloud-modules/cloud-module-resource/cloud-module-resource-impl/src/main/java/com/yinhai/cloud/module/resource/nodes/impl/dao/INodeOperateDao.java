package com.yinhai.cloud.module.resource.nodes.impl.dao;

import com.yinhai.cloud.module.resource.nodes.impl.po.NodeOperatePo;
import com.yinhai.core.common.api.base.IPage;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-06-27 15:47:01
 */
public interface INodeOperateDao {
    /**
     * 新增
     *
     * @param po
     * @return
     */
    Long saveNewOperate(NodeOperatePo po);

    /**
     * 更新
     *
     * @param po
     */
    void updateNodeOperate(NodeOperatePo po);

    /**
     * 删除节点的操作记录
     *
     * @param nodeId
     */
    void deleteByNodeId(Long nodeId);


    /**
     * 获取节点的操作记录列表
     *
     * @param nodeId
     * @return
     */
    List<NodeOperatePo> getOperateListByNodeId(Long nodeId);


    NodeOperatePo getOperateRecordById(Long operateId);

    IPage<NodeOperatePo> getNodeOperateStackWithPage(Long nodeId, Integer currentPage, Integer pageSize);

    List<NodeOperatePo> getRunnigOperate();
}
