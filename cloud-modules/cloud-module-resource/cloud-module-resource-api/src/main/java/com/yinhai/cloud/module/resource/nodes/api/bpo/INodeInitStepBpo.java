package com.yinhai.cloud.module.resource.nodes.api.bpo;

import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInitBaseStepVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInitializeStepVo;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-08-28 15:11:28
 */
public interface INodeInitStepBpo {

    /**
     * 获取指定节点的初始化步骤运行状态、运行记录
     *
     * @param nodeId
     * @return
     */
    List<NodeInitializeStepVo> getNodeInitializeStepList(Long nodeId);

    /**
     * 创建初始化步骤运行记录
     *
     * @param nodeId
     */
    List<NodeInitializeStepVo> createNodeInitializeStep(Long nodeId);

    /**
     * 获取基础步骤信息
     *
     * @return
     */
    List<NodeInitBaseStepVo> getAllNodeInitBaseStepList();

    /**
     * 清理当前节点初始化步骤信息
     */
    void cleanStepInfo(long nodeId, Integer startStepOrder);


    /**
     * 完成节点的初始化，修改节点状态，修改操作记录状态
     */
    void finishNodeInit(Long nodeId, Long operateId);

    /**
     * 节点初始化失败，修改节点状态，记录操作日志
     */
    void nodeInitFailed(Long nodeId, Long operateId, String log);

    void updateStepLog(Long nodeId, Integer stepOrder, String replaceLog);

    void appendStepLogAndUpdateFinishPercentage(Long nodeId, Integer stepOrder, String appendLog, Integer finishPer);

    void updateNodeInitStepAsStart(Long nodeId, Integer stepOrder);

    void updateNodeInitStepAsSuccess(Long nodeId, Integer stepOrder);

    void cleanStepLog(Long nodeId, Integer stepOrder);

    void updateNodeInitStepAsFailed(Long nodeId, Integer currentStepOrder, String message);

    NodeInitializeStepVo getNodeInitStep(Long nodeId, int stepOrder);

    /**
     * 查询大于等于指定步骤顺序的初始化步骤
     *
     * @param nodeId
     * @param startStepOrder
     */
    List<NodeInitializeStepVo> getOrderedNodeInitStepListFromStartOrder(Long nodeId, Integer startStepOrder);

    void appendStepLog(Long nodeId, Integer stepOrder, String appendLog);


    void updateNodeStepFinishPercentage(Long currentNodeId, Integer currentStepOrder, int currentFinPer);

    void appendLogToLastStep(Long node, String appendLog);

    /**
     * 获取所有日志
     * @param nodeId
     * @return
     */
    List<NodeInitializeStepVo> getNodeInitializeStep(Long nodeId);

    /**
     * 将日志状态改为失败
     * @param stepVo
     */
    void setFalid(NodeInitializeStepVo stepVo);
}
