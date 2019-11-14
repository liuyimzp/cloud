package com.yinhai.cloud.module.monitor.impl.dao.impl;

import com.yinhai.cloud.module.monitor.impl.dao.IClusterMonitorDao;
import com.yinhai.cloud.module.monitor.impl.po.ClusterMonitorPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by pengwei on 2018/8/29.
 */
public class IClusterMonitorDaoImpl extends HibernateDAO<ClusterMonitorPo> implements IClusterMonitorDao {

    @Override
    public List<ClusterMonitorPo> queryClusterMonitorInfoByClusterId(Long clusterId) {
        String hql = "from ClusterMonitorPo a where a.clusterId=? order by a.biztime desc";

        return super.find(hql, clusterId);
    }

    @Override
    public void insertNewMonitorData(ClusterMonitorPo clusterMonitorPo) {
        super.save(clusterMonitorPo);
    }

    @Override
    public void deleteMonitorDataByClusterId(Long clusterId) {
        String hql = "delete from ClusterMonitorPo where clusterId = ?";
        super.delete(hql, clusterId);
    }


}
