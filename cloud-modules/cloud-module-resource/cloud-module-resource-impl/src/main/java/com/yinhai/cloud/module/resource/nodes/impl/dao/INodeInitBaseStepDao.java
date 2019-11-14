package com.yinhai.cloud.module.resource.nodes.impl.dao;

import com.yinhai.cloud.module.resource.nodes.impl.po.NodeInitBaseStepPo;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-06-11 16:45:58
 */
public interface INodeInitBaseStepDao {
    /**
     * 获取节点初始化步骤配置
     *
     * @return
     */
    List<NodeInitBaseStepPo> getAllNodeInitBaseStepList();

    /**
     * 获取节点初始化步骤信息
     *
     * @param stepOrder
     * @return
     */
    NodeInitBaseStepPo getNodeInitBaseStep(Integer stepOrder);
}
