package com.yinhai.cloud.module.resource.cluster.impl.dao.impl;

import com.yinhai.cloud.module.resource.cluster.api.vo.CephPoolVo;
import com.yinhai.cloud.module.resource.cluster.impl.dao.ICephPoolDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.CephPoolPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;


public class ICephPoolDaoImpl extends HibernateDAO<CephPoolPo> implements ICephPoolDao {

    @Override
    public List<CephPoolPo> queryAllPool(Long clusterId) {
        String hql = "from CephPoolPo where clusterId=? order by poolIsUse desc";
        return super.find(hql,clusterId);
    }

    @Override
    public CephPoolVo savePool(CephPoolVo vo) {
        CephPoolPo po = new CephPoolPo();
        super.save(vo.toPO(po));
        return po.toVo(new CephPoolVo());
    }

    @Override
    public boolean checkPoolName(String poolName) {
        return getCount("from CephPoolPo where poolName = ?", null, poolName) >= 1;
    }

    @Override
    public void deleteByPoolId(Long poolId) {
        String hql = "delete from CephPoolPo where poolId=?";
        delete(hql,poolId);
    }

    @Override
    public void deleteByCluster(Long clusterId) {
        String hql = "delete from CephPoolPo where clusterId=?";
        delete(hql,clusterId);
    }

    @Override
    public CephPoolPo queryPoolById(Long poolId) {
        return get(poolId);
    }

    @Override
    public void updatePool(CephPoolPo po) {
        CephPoolPo oldPo = queryPoolById(po.getPoolId());
        if (po.getPgNum() != null){
            oldPo.setPgNum(po.getPgNum());
        }
        if (po.getPoolName() != null){
            oldPo.setPoolName(po.getPoolName());
        }
        if (po.getPoolIsUse() != null){
            oldPo.setPoolIsUse(po.getPoolIsUse());
        }
        super.update(oldPo);
    }

    @Override
    public List<CephPoolPo> getAllNotUsePools(Long clusterId) {
        String hql = "from CephPoolPo where clusterId=? and poolIsUse=? order by poolIsUse desc";
        return super.find(hql,clusterId,0);
    }

    @Override
    public List<CephPoolPo> getAllUmountPools(Long clusterId) {
        String hql = "from CephPoolPo where clusterId=? and poolIsUse!=? order by poolIsUse desc";
        return super.find(hql,clusterId,1);
    }
}
