package com.yinhai.cloud.module.resource.nodes.impl.bpo;

import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeInitStepBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInitBaseStepVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInitializeStepVo;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeDao;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeInitBaseStepDao;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeInitializeStepDao;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeOperateDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeInitBaseStepPo;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeInitializeStepPo;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeOperatePo;
import com.yinhai.core.service.ta3.domain.service.TaBaseService;
import com.yinhai.modules.org.ta3.domain.dao.IUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhaokai
 * @create: 2018-08-28 15:12:33
 */
public class NodeInitStepBpoImpl extends TaBaseService implements INodeInitStepBpo {
    private final static Logger logger = LoggerFactory.getLogger(NodeBpoImpl.class);
    @Autowired
    private IClusterBpo clusterBpo;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private INodeDao nodeDao;
    @Autowired
    private INodeInitBaseStepDao nodeInitBaseStepDao;
    @Autowired
    private INodeInitializeStepDao nodeInitializeStepDao;
    @Autowired
    private INodeOperateDao nodeOperateDao;

    @Override
    public void cleanStepLog(Long nodeId, Integer stepOrder) {
        NodeInitializeStepPo step = new NodeInitializeStepPo();
        step.setNodeId(nodeId);
        step.setStepOrder(stepOrder);
        step.setStepLog("");
        nodeInitializeStepDao.updateStep(step);
    }

    @Override
    public void updateNodeInitStepAsFailed(Long nodeId, Integer stepOrder, String message) {
        NodeInitializeStepPo step = new NodeInitializeStepPo();
        step.setNodeId(nodeId);
        step.setStepOrder(stepOrder);
        step.setStepState(ResourceConstant.NODE_INIT_STEP_FAILED);

        NodeInitializeStepPo old = nodeInitializeStepDao.getNodeInitializeStep(nodeId, stepOrder);
        //liuyi02 2019/3/1 修改.old为空报错，不为空三目运算则没必要执行
        //String oldLog = old.getStepLog();
        step.setStepLog((old == null ? "" : old.getStepLog()) + "\n初始化出错：\n" + message);
        nodeInitializeStepDao.updateStep(step);
    }

    @Override
    public void updateNodeInitStepAsStart(Long nodeId, Integer stepOrder) {
        NodeInitializeStepPo startStep = new NodeInitializeStepPo();
        startStep.setNodeId(nodeId);
        startStep.setStepOrder(stepOrder);
        startStep.setStepState(ResourceConstant.NODE_INIT_STEP_RUNNING);
        startStep.setStepLog(ResourceConstant.RUNNING_STEP_HINT);
        startStep.setStepFinishedPercentage(0);
        nodeInitializeStepDao.updateStep(startStep);
    }

    @Override
    public List<NodeInitializeStepVo> getOrderedNodeInitStepListFromStartOrder(final Long nodeId, final Integer startStepOrder) {
        return getNodeInitializeStepList(nodeId)
                .stream().filter(n -> n.getStepOrder() >= startStepOrder)
                .sorted(Comparator.comparing(NodeInitializeStepVo::getStepOrder))
                .collect(Collectors.toList());
    }

    @Override
    public void updateNodeInitStepAsSuccess(Long nodeId, Integer stepOrder) {
        NodeInitializeStepPo step = new NodeInitializeStepPo();
        step.setNodeId(nodeId);
        step.setStepOrder(stepOrder);
        step.setStepState(ResourceConstant.NODE_INIT_STEP_SUCCESSFUL);
        step.setStepFinishedPercentage(100);
        String stepLog = nodeInitializeStepDao.getNodeInitializeStep(nodeId, stepOrder).getStepLog().replace(ResourceConstant.RUNNING_STEP_HINT, "");
        step.setStepLog(stepLog);
        nodeInitializeStepDao.updateStep(step);
    }

    @Override
    public List<NodeInitializeStepVo> getNodeInitializeStepList(Long nodeId) {
        List<NodeInitializeStepVo> stepVoList = createNodeInitializeStep(nodeId).stream().map(vo -> {
            NodeInitBaseStepPo baseStep = nodeInitBaseStepDao.getNodeInitBaseStep(vo.getStepOrder());
            NodeInitBaseStepVo baseStepVo = null;
            if (baseStep != null) {
                baseStepVo = baseStep.toVo(new NodeInitBaseStepVo());

            }
            vo.setBaseStep(baseStepVo);
            String oldLog = vo.getStepLog();
            if (oldLog != null) {
                vo.setStepLog(oldLog.replaceAll("\\n+", "\n"));
            }
            return vo;
        }).collect(Collectors.toList());

        return stepVoList;
    }

    @Override
    public List<NodeInitializeStepVo> createNodeInitializeStep(Long nodeId) {
        List<NodeInitializeStepVo> stepVoList = nodeInitializeStepDao.getNodeInitializeStepList(nodeId).stream().map(n->n.toVo(new NodeInitializeStepVo())).collect(Collectors.toList());
        if (stepVoList.isEmpty()) {
            // 兼容之前版本未添加步骤运行记录数据
            nodeInitBaseStepDao.getAllNodeInitBaseStepList().stream().map(baseStep -> {
                NodeInitializeStepPo step = new NodeInitializeStepPo();
                step.setNodeId(nodeId);
                step.setStepState(ResourceConstant.NODE_INIT_STEP_NOT_RUNNING);
                step.setStepOrder(baseStep.getStepOrder());
                return step;
            }).forEach(n->{
                stepVoList.add(n.toVo(new NodeInitializeStepVo()));
                nodeInitializeStepDao.saveNewInitializeStepForNode(n);
            });
        }else{
            List<NodeInitBaseStepPo> baseStepList = nodeInitBaseStepDao.getAllNodeInitBaseStepList();
            if(stepVoList.size() < baseStepList.size()){
                // 如果有新配置的基础步骤，历史数据中缺少该节点的步骤状态记录，则新增一个未执行的步骤
                for(NodeInitBaseStepPo baseStepPo:baseStepList){
                    boolean hasStepStatus = stepVoList.stream().anyMatch(n -> n.getStepOrder().equals(baseStepPo.getStepOrder()));
                    if(!hasStepStatus){
                        NodeInitializeStepPo step = new NodeInitializeStepPo();
                        step.setNodeId(nodeId);
                        step.setStepState(ResourceConstant.NODE_INIT_STEP_NOT_RUNNING);
                        step.setStepOrder(baseStepPo.getStepOrder());
                        stepVoList.add(step.toVo(new NodeInitializeStepVo()));
                        nodeInitializeStepDao.saveNewInitializeStepForNode(step);
                    }

                }
            }
        }

        return stepVoList;
    }


    @Override
    public List<NodeInitBaseStepVo> getAllNodeInitBaseStepList() {
        return nodeInitBaseStepDao.getAllNodeInitBaseStepList().stream().map(n -> n.toVo(new NodeInitBaseStepVo())).collect(Collectors.toList());
    }

    @Override
    public void cleanStepInfo(long nodeId, Integer startStepOrder) {
        List<NodeInitializeStepPo> stepList = nodeInitializeStepDao.getNodeInitializeStepList(nodeId);
        for (NodeInitializeStepPo step : stepList) {
            if (step.getStepOrder() < startStepOrder) {
                continue;
            }
            step.setStepFinishedPercentage(0);
            step.setStepState(ResourceConstant.NODE_INIT_STEP_NOT_RUNNING);
            step.setStepLog("");
            nodeInitializeStepDao.updateStep(step);
        }
    }

    @Override
    public void appendLogToLastStep(Long nodeId, String appendLog) {
        int maxStep = nodeInitBaseStepDao.getAllNodeInitBaseStepList().stream().mapToInt(NodeInitBaseStepPo::getStepOrder).max().orElse(-1);
        if (maxStep == -1) {
            throw new IllegalArgumentException("缺少BaseStep配置！");
        }

        appendStepLog(nodeId, maxStep, appendLog);
    }

    @Override
    public List<NodeInitializeStepVo> getNodeInitializeStep(Long nodeId) {
        return nodeInitializeStepDao.getNodeInitializeStepList(nodeId).stream().map(po -> {
            NodeInitializeStepVo vo = po.toVo(new NodeInitializeStepVo());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void setFalid(NodeInitializeStepVo stepVo) {
        nodeInitializeStepDao.setFalid(stepVo.toPO(new NodeInitializeStepPo()));
    }

    @Override
    public void nodeInitFailed(Long nodeId, Long operateId, String log) {
        NodeBasicInfoVo updateNode = new NodeBasicInfoVo();
        updateNode.setNodeId(nodeId);
        updateNode.setNodeInitState(NodeState.INIT_FAILED);
        updateNode.setNodeRunningState(NodeState.RUNNING_NOT_NOW);
        updateNode.setNodeOperateState(NodeState.OPERATE_FAILED);

        NodeOperatePo po = new NodeOperatePo();
        po.setOperateId(operateId);
        po.setOperateLog(log);
        po.setOperateState(ResourceConstant.OPERATE_STATE_FAILED);
        po.setOperateFinishTime(CommonUtil.getTime(ResourceConstant.TIME_FORMAT_WITH_NANO));

        nodeDao.updateNode(updateNode);
        nodeOperateDao.updateNodeOperate(po);
    }

    @Override
    public void finishNodeInit(Long nodeId, Long operateId) {
        NodeBasicInfoVo updateNode = new NodeBasicInfoVo();
        updateNode.setNodeId(nodeId);
        updateNode.setNodeInitState(NodeState.INIT_DONE);
        updateNode.setNodeOperateState(NodeState.OPERATE_INIT_DONE);
        updateNode.setNodeRunningState(NodeState.RUNNING_SUCCESS);

        NodeOperatePo po = new NodeOperatePo();
        po.setOperateId(operateId);
        po.setOperateState(ResourceConstant.OPERATE_STATE_SUCCESSFUL);
        po.setOperateFinishTime(LocalDateTime.now().format((DateTimeFormatter.ofPattern(ResourceConstant.TIME_FORMAT_WITH_NANO))));
        po.setOperateFinishedPercentage(100);

        nodeDao.updateNode(updateNode);
        nodeOperateDao.updateNodeOperate(po);
        logger.info("finishNodeInit:" + nodeId);
    }

    @Override
    public void appendStepLog(Long nodeId, Integer stepOrder, String appendLog) {
        NodeInitializeStepPo po = new NodeInitializeStepPo();
        po.setNodeId(nodeId);
        po.setStepOrder(stepOrder);
        String oldLog = nodeInitializeStepDao.getNodeInitializeStep(nodeId, stepOrder).getStepLog();
        if (oldLog == null) {
            oldLog = "";
        }
        po.setStepLog(oldLog + appendLog + "\n");
        nodeInitializeStepDao.updateStep(po);
    }

    @Override
    public void updateNodeStepFinishPercentage(Long nodeId, Integer stepOrder, int currentFinPer) {
        NodeInitializeStepPo po = new NodeInitializeStepPo();
        po.setNodeId(nodeId);
        po.setStepOrder(stepOrder);
        po.setStepFinishedPercentage(currentFinPer);
        nodeInitializeStepDao.updateStep(po);
    }

    @Override
    public void appendStepLogAndUpdateFinishPercentage(Long nodeId, Integer stepOrder, String appendLog, Integer finishPer) {
        NodeInitializeStepPo po = new NodeInitializeStepPo();
        po.setNodeId(nodeId);
        po.setStepOrder(stepOrder);
        po.setStepFinishedPercentage(finishPer);
        String oldLog = nodeInitializeStepDao.getNodeInitializeStep(nodeId, stepOrder).getStepLog();
        if (oldLog == null) {
            oldLog = "";
        }
        po.setStepLog(oldLog + appendLog + "\n");
        nodeInitializeStepDao.updateStep(po);
    }

    @Override
    public NodeInitializeStepVo getNodeInitStep(Long nodeId, int stepOrder) {
        NodeInitializeStepPo po = nodeInitializeStepDao.getNodeInitializeStep(nodeId, stepOrder);
        if (po == null) {
            return null;
        }
        return po.toVo(new NodeInitializeStepVo());
    }

    @Override
    public void updateStepLog(Long nodeId, Integer stepOrder, String replaceLog) {
        NodeInitializeStepPo po = new NodeInitializeStepPo();
        po.setNodeId(nodeId);
        po.setStepOrder(stepOrder);
        po.setStepLog(replaceLog + "\n");
        nodeInitializeStepDao.updateStep(po);
    }
}
