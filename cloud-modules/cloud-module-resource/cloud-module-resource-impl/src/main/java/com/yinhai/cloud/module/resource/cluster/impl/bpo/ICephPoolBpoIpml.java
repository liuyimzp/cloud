package com.yinhai.cloud.module.resource.cluster.impl.bpo;

import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephPoolBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephPoolVo;
import com.yinhai.cloud.module.resource.cluster.impl.dao.ICephPoolDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.CephPoolPo;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import com.yinhai.modules.org.ta3.domain.dao.IUserDao;
import com.yinhai.modules.org.ta3.domain.po.impl.UserPo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuyi02
 */
public class ICephPoolBpoIpml extends TaBaseBpo implements ICephPoolBpo {

    @Resource
    private ICephPoolDao cephPoolDao;

    @Autowired
    private IUserDao userDao;

    @Override
    public List<CephPoolVo> getAllPools(Long clusterId) {
        return cephPoolDao.queryAllPool(clusterId).stream().map(cpo -> {
            CephPoolVo nvo = cpo.toVo(new CephPoolVo());
            final UserPo userPo = userDao.getUserByUserId(cpo.getCreateUser());
            nvo.setCreateUser(userPo.getName());
            return nvo;
        }).collect(Collectors.toList());
    }

    @Override
    public CephPoolVo savePool(CephPoolVo vo) {
        return cephPoolDao.savePool(vo);
    }

    @Override
    public boolean checkPoolName(String poolName) {
        return cephPoolDao.checkPoolName(poolName);
    }

    @Override
    public void deleteByPoolId(Long poolId) {
        cephPoolDao.deleteByPoolId(poolId);
    }

    @Override
    public void deleteByClusterId(Long clusterId) {
        cephPoolDao.deleteByCluster(clusterId);
    }

    @Override
    public CephPoolVo getPoolById(Long poolId) {
        CephPoolPo po = cephPoolDao.queryPoolById(poolId);
        return po.toVo(new CephPoolVo());
    }

    @Override
    public void updatePool(CephPoolVo vo) {
        cephPoolDao.updatePool(vo.toPO(new CephPoolPo()));
    }

    @Override
    public List<CephPoolVo> getAllNotUsePools(Long clusterId) {
        return cephPoolDao.getAllNotUsePools(clusterId).stream().map(nvo -> {
            CephPoolVo vo = nvo.toVo(new CephPoolVo());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CephPoolVo> getAllUmountPools(Long clusterId) {
        return cephPoolDao.getAllUmountPools(clusterId).stream().map(nvo -> {
            CephPoolVo vo = nvo.toVo(new CephPoolVo());
            return vo;
        }).collect(Collectors.toList());
    }
}
