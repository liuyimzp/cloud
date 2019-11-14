package com.yinhai.cloud.module.resource.cluster.impl.dao.impl;

import com.yinhai.cloud.module.resource.cluster.impl.dao.IClusterDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.ClusterPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

public class IClusterDaoImpl extends HibernateDAO<ClusterPo> implements IClusterDao {
    @Override
    public List<ClusterPo> getClusterList() {
        String hql = "from ClusterPo";
        return find(hql);
    }

    @Override
    public ClusterPo getClusterById(Long id) {
        return get(id);
    }

    @Override
    public ClusterPo getClusterByTag(String tag) {
        StringBuffer hql = new StringBuffer();
        hql.append("from ClusterPo");
        hql.append(" c where c.identify = ? ");
        return get(hql.toString(), tag);
    }

    @Override
    public Long addNewCluster(ClusterPo clusterPo) {
        save(clusterPo);
        return clusterPo.getId();
    }

    @Override
    public void updateClusterById(ClusterPo clusterPo) {
        ClusterPo persistence = get(clusterPo.getId());
        if (clusterPo.getDeleting() != null) {
            persistence.setDeleting(clusterPo.getDeleting());
        }
        if (clusterPo.getNumOfNodes() != null) {
            persistence.setNumOfNodes(clusterPo.getNumOfNodes());
        }
        if (clusterPo.getName() != null) {
            persistence.setName(clusterPo.getName());
        }
        if (clusterPo.getIdentify() != null) {
            persistence.setIdentify(clusterPo.getIdentify());
        }
        if (clusterPo.getRunningState() != null) {
            persistence.setRunningState(clusterPo.getRunningState());
        }
        if (clusterPo.getClusterHaVirtualIP() != null) {
            persistence.setClusterHaVirtualIP(clusterPo.getClusterHaVirtualIP());
        }
        if(clusterPo.getTotalCpu() != null){
            persistence.setTotalCpu(clusterPo.getTotalCpu());
        }
        if(clusterPo.getTotalMemory() != null){
            persistence.setTotalMemory(clusterPo.getTotalMemory());
        }
        if(clusterPo.getTotalCpuAvailable() != null){
            persistence.setTotalCpuAvailable(clusterPo.getTotalCpuAvailable());
        }
        if(clusterPo.getTotalMemAvailable() != null){
            persistence.setTotalMemAvailable(clusterPo.getTotalMemAvailable());
        }
        if(clusterPo.getIstioPort() != null){
            persistence.setIstioPort(clusterPo.getIstioPort());
        }
        persistence.setClusterDeletingServer(clusterPo.getClusterDeletingServer());
        persistence.setComment(clusterPo.getComment());
        persistence.setTotalAllocatableCpu(clusterPo.getTotalAllocatableCpu());
        persistence.setTotalAllocatableMemory(clusterPo.getTotalAllocatableMemory());
        super.update(persistence);
    }

    @Override
    public void addClusterMemCpu(Long id, Double mem, Double cpu) {
        String hql ="update ClusterPo set totalAllocatableMemory=totalAllocatableMemory + ?, totalMemAvailable=totalMemAvailable + ?, " +
                " totalAllocatableCpu=totalAllocatableCpu + ?, totalCpuAvailable=totalCpuAvailable + ? where id=? ";
        super.update(hql,mem,mem,cpu,cpu,id);
    }

    @Override
    public void subtractClusterMemCpu(Long id, Double mem, Double cpu) {
        String hql ="update ClusterPo set totalAllocatableMemory=totalAllocatableMemory  - ?, totalMemAvailable=totalMemAvailable - ?, " +
                " totalAllocatableCpu=totalAllocatableCpu - ?, totalCpuAvailable=totalCpuAvailable - ? where id=? ";
        super.update(hql,mem,mem,cpu,cpu,id);
    }

    @Override
    public void deleteClusterById(Long id) {
        delete("delete from ClusterPo where id = ?", id);
    }

    @Override
    public Long countByTag(String tag) {
        return getCount("from ClusterPo where cluster_tag = ?", null, tag);
    }


}
