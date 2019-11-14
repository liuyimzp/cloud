package com.yinhai.cloud.module.monitor.impl.dao.impl;

import com.yinhai.cloud.module.monitor.impl.dao.INodeMonitorDao;
import com.yinhai.cloud.module.monitor.impl.po.NodeMonitorPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by pengwei on 2018/9/21.
 */
public class INodeMonitorDaoImpl extends HibernateDAO<NodeMonitorPo> implements INodeMonitorDao {

    @Override
    public List<NodeMonitorPo> quaryNodeMonitorDataByNodeId(Long nodeId) {
        String hql = "from NodeMonitorPo where nodeId = ? order by biztime desc";

        return super.find(hql, nodeId);
    }

    @Override
    public void insertNodeMonitorData(NodeMonitorPo nodeMonitorPo) {
        super.save(nodeMonitorPo);
    }

    @Override
    public void deleteNodeMonitorData(Long nodeId) {

        super.delete("delete from NodeMonitorPo where nodeId = ?", nodeId);
    }
}
