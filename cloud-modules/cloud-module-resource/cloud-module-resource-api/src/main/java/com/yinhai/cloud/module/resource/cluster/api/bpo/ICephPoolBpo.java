package com.yinhai.cloud.module.resource.cluster.api.bpo;

import com.yinhai.cloud.module.resource.cluster.api.vo.CephPoolVo;

import java.util.List;

/**
 * 集群管理接口
 *
 * @author liuyi02
 */
public interface ICephPoolBpo {

    /**
     * 查询所有的pool存储池
     * @param clusterId
     * @return
     */
    List<CephPoolVo> getAllPools(Long clusterId);

    /**
     * 保存pool数据
     * @param vo
     * @return CephPoolVo
     */
    CephPoolVo savePool(CephPoolVo vo);

    /**
     * 检测poolName是否存在
     * @return
     */
    boolean checkPoolName(String poolName);

    /**
     * 根据poolId删除数据
     * @param poolId
     */
    void deleteByPoolId(Long poolId);

    /**
     * 删除某集群下所有pool
     * @param clusterId
     */
    void deleteByClusterId(Long clusterId);

    /**
     * 根据poolId查询pool
     * @param poolId
     * @return
     */
    CephPoolVo getPoolById(Long poolId);

    /**
     * 修改pool数据
     * @param nvo
     */
    void updatePool(CephPoolVo nvo);

    /**
     * 查询集群下所有没有使用的存储池
     * @return
     */
    List<CephPoolVo> getAllNotUsePools(Long clusterId);

    /**
     * 查看集群下可挂载的存储池（未被占用即可）
     * @param clusterId
     * @return
     */
    List<CephPoolVo> getAllUmountPools(Long clusterId);
}
