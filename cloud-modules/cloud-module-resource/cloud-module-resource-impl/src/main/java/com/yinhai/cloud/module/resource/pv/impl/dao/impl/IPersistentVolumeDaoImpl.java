package com.yinhai.cloud.module.resource.pv.impl.dao.impl;

import com.yinhai.cloud.module.resource.pv.api.constants.PvConstants;
import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.cloud.module.resource.pv.impl.dao.IPersistentVolumeDao;
import com.yinhai.cloud.module.resource.pv.impl.po.PvPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jianglw
 */
public class IPersistentVolumeDaoImpl extends HibernateDAO<PvPo> implements IPersistentVolumeDao {
    @Override
    public List<PvPo> queryAll() {
        final String hql = "from PvPo po";
        return super.find(hql);
    }

    @Override
    public List<PvPo> queryPvByNameAndId(String volumeDisplayName, String clusterId) {
        final String hql = "from " + getEntityClassName(PvPo.class.getName()) + " where volumeDisplayName = ?  and clusterId = ?";
        return find(hql, volumeDisplayName, clusterId);
    }


    @Override
    public PvVo queryByPoId(Long pvId) {
        PvPo po = get(pvId);
        return po == null ? null : po.toVo(new PvVo());
    }

    @Override
    public PvPo queryByStorageId(Long storageId) {
        String hql = "from " + getEntityClassName(PvPo.class.getName()) + " where volumeCloudStorageId=?";
        return get(hql, storageId);
    }

    @Override
    public void savePo(final PvPo po) {
        super.save(po);
    }

    @Override
    public void updatePo(final PvPo po) {
        final PvPo pvPo = queryAll().stream().filter(it -> it.getVolumeId().equals(po.getVolumeId())).collect(Collectors.toList()).get(0);
        if (po.getVolumeDisplayName() != null) {
            pvPo.setVolumeDisplayName(po.getVolumeDisplayName());
        }
        if (po.getVolumeType() != null) {
            pvPo.setVolumeType(po.getVolumeType());
        }
        if (po.getVolumeCloudStorageId() != null) {
            if (po.getVolumeType().equals(PvConstants.PV_TYPE_REMOTE) || po.getVolumeType().equals(PvConstants.PV_TYPE_CEPH)) {
                pvPo.setVolumeCloudStorageId(po.getVolumeCloudStorageId());
            } else {
                pvPo.setVolumeCloudStorageId(0L);
            }
        }
        if (po.getVolumeStoragePath() != null) {
            pvPo.setVolumeStoragePath(po.getVolumeStoragePath());
        }
        if (po.getVolumeMaxSpace() != null) {
            pvPo.setVolumeMaxSpace(po.getVolumeMaxSpace());
        }
        if (po.getVolumeAvailableSpace() != null) {
            pvPo.setVolumeAvailableSpace(po.getVolumeAvailableSpace());
        }
        super.update(pvPo);
    }

    @Override
    public void deletePo(final PvPo po) {
        final PvPo pvPo = queryAll().stream().filter(it -> it.getVolumeId().equals(po.getVolumeId())).limit(1).collect(Collectors.toList()).get(0);
        super.delete(pvPo);
    }

    @Override
    public void updateVolumeAvailable(PvPo po) {
        String hql = "update PvPo set volumeAvailableSpace = ? where volumeId = ?";
        super.update(hql, po.getVolumeAvailableSpace(), po.getVolumeId());
    }


    @Override
    public void deletePvByClusterId(Long clusterId) {
        super.delete("delete from " + getEntityClassName(PvPo.class.getName()) + " where clusterId = ?", String.valueOf(clusterId));
    }

    @Override
    public boolean queryCountByPoolId(Long poolId) {
        String hql = "select count(*) from PvPo where cephPoolId = ? ";// + IAppConstants.APP_STATUS_UNRELEASED;
        return getCount(hql, null, poolId) > 0;
    }
}
