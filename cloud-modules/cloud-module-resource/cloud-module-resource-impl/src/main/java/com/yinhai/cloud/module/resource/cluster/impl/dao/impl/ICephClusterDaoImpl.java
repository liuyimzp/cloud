package com.yinhai.cloud.module.resource.cluster.impl.dao.impl;

import com.yinhai.cloud.module.resource.cluster.impl.dao.ICephClusterDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.CephClusterPo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;


public class ICephClusterDaoImpl extends HibernateDAO<CephClusterPo> implements ICephClusterDao {
    @Override
    public List<CephClusterPo> getClusterList() {
        String hql = "from CephClusterPo";
        return find(hql);
    }

    @Override
    public CephClusterPo getClusterById(Long id) {
        return get(id);
    }

    @Override
    public void updateClusterByPo(CephClusterPo po) {
        CephClusterPo old = get(po.getId());
        if (ValidateUtil.isNotEmpty(po.getComment())){
            old.setComment(po.getComment());
        }if (ValidateUtil.isNotEmpty(po.getCreateDate())){
            old.setCreateDate(po.getCreateDate());
        }if (ValidateUtil.isNotEmpty(po.getCreateUserId())){
            old.setCreateUserId(po.getCreateUserId());
        }if (po.getMemoryAvaiLabel() != null){
            old.setMemoryAvaiLabel(po.getMemoryAvaiLabel());
        }if (po.getMemoryTotal() != null){
            old.setMemoryTotal(po.getMemoryTotal());
        }if (po.getDeleting() != null) {
            old.setDeleting(po.getDeleting());
        }if (ValidateUtil.isNotEmpty(po.getIdentify())){
            old.setIdentify(po.getIdentify());
        }if (ValidateUtil.isNotEmpty(po.getName())){
            old.setName(po.getName());
        }if (po.getNumOfNodes() != null){
            old.setNumOfNodes(po.getNumOfNodes());
        }if (po.getRunningState() != null){
            old.setRunningState(po.getRunningState());
        }if (ValidateUtil.isNotEmpty(po.getFsName())){
            old.setFsName(po.getFsName());
        }
        if (po.getClusterId() != null){
            old.setClusterId(po.getClusterId());
        }
        super.update(old);
    }

    @Override
    public List<CephClusterPo> getClusterListByUserId(Long userid) {
        String hql = "from CephClusterPo where createUserId=?";
        return find(hql,userid);
    }

    @Override
    public boolean getCountTag(String identify) {
        String hql = "from CephClusterPo where identify=?";
        return getCount(hql,null,identify) >= 1;
    }

    @Override
    public CephClusterPo saveCeph(CephClusterPo po) {
        save(po);
        return po;
    }

    @Override
    public void deleteCluster(Long id) {
        String hql = "delete from CephClusterPo where id=?";
        delete(hql,id);
    }

    @Override
    public List<CephClusterPo> getClusterByK8sClusterId(Long clusterId) {
        String hql = "from CephClusterPo where clusterId=?";
        return find(hql,clusterId);
    }
}
