package com.yinhai.cloud.module.resource.nodes.impl.dao.impl;

import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeOperateDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeOperatePo;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;
/**
 * @author jianglw
 */
public class NodeOperateDaoImpl extends HibernateDAO<NodeOperatePo> implements INodeOperateDao {
    @Override
    public IPage<NodeOperatePo> getNodeOperateStackWithPage(Long nodeId, Integer currentPage, Integer pageSize) {
        Integer limit = ValidateUtil.isEmpty(pageSize) ? 10 : pageSize;
        Integer start = ValidateUtil.isEmpty(currentPage) || currentPage <= 0 ? 0 : (currentPage - 1) * limit;

        String hql = "from NodeOperatePo where nodeId = ? order by operateStartTime desc,operateId desc";
        return super.findForPage(hql, start, limit, "", nodeId);
    }

    @Override
    public NodeOperatePo getOperateRecordById(Long operateId) {
        return get(operateId);
    }

    @Override
    public Long saveNewOperate(NodeOperatePo po) {
        save(po);
        return po.getOperateId();
    }

    @Override
    public void updateNodeOperate(NodeOperatePo po) {
        NodeOperatePo persistence = get(po.getOperateId());
        if (po.getOperateLog() != null) {
            persistence.setOperateLog(po.getOperateLog());
        }
        if (po.getOperateState() != null) {
            persistence.setOperateState(po.getOperateState());
        }

        if (po.getOperateStartTime() != null) {
            persistence.setOperateStartTime(po.getOperateStartTime());
        }

        if (po.getOperateFinishTime() != null) {
            persistence.setOperateFinishTime(po.getOperateFinishTime());
        }
        if (po.getOperateFinishedPercentage() != null) {
            persistence.setOperateFinishedPercentage(po.getOperateFinishedPercentage());
        }
        update(persistence);
    }

    @Override
    public List<NodeOperatePo> getRunnigOperate() {
        return find("from NodeOperatePo where operateState = ? ", ResourceConstant.OPERATE_STATE_RUNNING);
    }


    @Override
    public List<NodeOperatePo> getOperateListByNodeId(Long nodeId) {
        return find("from NodeOperatePo where nodeId = ? order by operateStartTime desc", nodeId);
    }

    @Override
    public void deleteByNodeId(Long nodeId) {
        delete("delete from NodeOperatePo where nodeId = ?", nodeId);
    }
}
