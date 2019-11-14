package com.yinhai.cloud.module.resource.pv.impl.dao;

import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.cloud.module.resource.pv.impl.po.PvPo;

import java.util.List;

/**
 * PV Dao
 *
 * @author jianglw
 */
public interface IPersistentVolumeDao {
    List<PvPo> queryAll();

    List<PvPo> queryPvByNameAndId(String volumeDisplayName,String clusterId);

    PvVo queryByPoId(Long pvId);
    PvPo queryByStorageId(Long storageId);

    void savePo(PvPo po);

    void updatePo(PvPo po);

    void deletePo(PvPo po);

    /**
     * 更新存储池可用空间
     *
     * @param po
     */
    public void updateVolumeAvailable(PvPo po);

    /**
     * 删除集群下的所有pv
     * @param clusterId
     */
    void deletePvByClusterId(Long clusterId);

    /**
     * 查询是否有挂载pool的pv
     * @param poolId
     * @return
     */
    boolean queryCountByPoolId(Long poolId);
}
