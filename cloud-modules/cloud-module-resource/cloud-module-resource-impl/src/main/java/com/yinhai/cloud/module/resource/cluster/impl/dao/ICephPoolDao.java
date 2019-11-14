package com.yinhai.cloud.module.resource.cluster.impl.dao;


import com.yinhai.cloud.module.resource.cluster.api.vo.CephPoolVo;
import com.yinhai.cloud.module.resource.cluster.impl.po.CephPoolPo;

import java.util.Arrays;
import java.util.List;

/**
 * @author liuyi02
 */
public interface ICephPoolDao {

    /**
     * 根据集群id查询数据库所有pool
     * @param clusterId
     * @return List<CephPoolPo>
     */
    List<CephPoolPo> queryAllPool(Long clusterId);

    /**
     * 写入一条pool数据
     * @param vo
     * @return CephPoolVo
     */
    CephPoolVo savePool(CephPoolVo vo);

    /**
     * 查询数据库中是否存在poolname的数据存在返回true
     * @param poolName
     * @return
     */
    boolean checkPoolName(String poolName);

    /**
     * 删除数据库一条数据
     * @param poolId
     */
    void deleteByPoolId(Long poolId);

    /**
     * 删除数据库clusterid下的数据
     * @param clusterId
     */
    void deleteByCluster(Long clusterId);

    /**
     * 根据poolId查询数据库
     * @param poolId
     * @return
     */
    CephPoolPo queryPoolById(Long poolId);

    /**
     * 修改数据库pool的数据
     * @param toPO
     */
    void updatePool(CephPoolPo toPO);

    /**
     * 查询数据库某集群下所有未使用的存储池
     * @param clusterId
     * @return
     */
    List<CephPoolPo> getAllNotUsePools(Long clusterId);

    /**
     * 查询数据库中没有被占用的存储池
     * @param clusterId
     * @return
     */
    List<CephPoolPo> getAllUmountPools(Long clusterId);
}
