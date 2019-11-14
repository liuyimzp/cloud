package com.yinhai.cloud.module.resource.nodes.impl.dao.impl;

import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeInitializeStepDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeInitializeStepPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-06-11 16:45:58
 */
public class NodeInitializeStepDaoImpl extends HibernateDAO<NodeInitializeStepPo> implements INodeInitializeStepDao {
    @Override
    public void saveNewInitializeStepForNode(NodeInitializeStepPo step) {
        save(step);
    }

    @Override
    public List<NodeInitializeStepPo> getNodeInitializeStepList(Long nodeId) {
        return find("from NodeInitializeStepPo n  where n.nodeId = ? order by n.stepOrder", nodeId);
    }

    @Override
    public void deleteStepByNodeId(Long nodeId) {
        delete("delete from NodeInitializeStepPo where nodeId = ? ", nodeId);
    }

    @Override
    public NodeInitializeStepPo getNodeInitializeStep(Long nodeId, Integer stepOrder) {
        return get("from NodeInitializeStepPo where nodeId = ? and stepOrder = ?", nodeId, stepOrder);
    }

    @Override
    public void setFalid(NodeInitializeStepPo stepPo) {
        String hql = "update NodeInitializeStepPo set stepState = ? where nodeId = ? and stepState = ?";
        super.update(hql, ResourceConstant.NODE_INIT_STEP_FAILED,stepPo.getNodeId(),ResourceConstant.NODE_INIT_STEP_RUNNING);
    }

    @Override
    public void updateStep(NodeInitializeStepPo step) {
        NodeInitializeStepPo persistentStep = get("from NodeInitializeStepPo where nodeId = ? and stepOrder = ?", step.getNodeId(), step.getStepOrder());
        if (step.getStepLog() != null) {
            persistentStep.setStepLog(step.getStepLog());
        }
        if (step.getStepState() != null) {
            persistentStep.setStepState(step.getStepState());
        }
        if (step.getStepFinishedPercentage() != null) {
            persistentStep.setStepFinishedPercentage(step.getStepFinishedPercentage());
        }
        persistentStep.setStepRunTime(CommonUtil.getNow());
        update(persistentStep);
    }


}
