package com.yinhai.cloud.module.resource.nodes.impl.dao.impl;

import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeInitBaseStepDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeInitBaseStepPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-06-11 16:11:04
 */
public class NodeInitBaseStepDaoImpl extends HibernateDAO<NodeInitBaseStepPo> implements INodeInitBaseStepDao {
    @Override
    public List<NodeInitBaseStepPo> getAllNodeInitBaseStepList() {
        return find("from NodeInitBaseStepPo t order by t.stepOrder");
    }

    @Override
    public NodeInitBaseStepPo getNodeInitBaseStep(Integer stepOrder) {
        return get(stepOrder);
    }


}
