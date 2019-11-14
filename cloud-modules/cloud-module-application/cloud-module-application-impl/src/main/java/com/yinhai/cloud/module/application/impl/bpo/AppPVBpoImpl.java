package com.yinhai.cloud.module.application.impl.bpo;

import com.yinhai.cloud.module.application.api.bpo.IAppPVBpo;
import com.yinhai.cloud.module.application.api.vo.AppPVVo;
import com.yinhai.cloud.module.application.impl.dao.AppPVDao;
import com.yinhai.cloud.module.application.impl.po.AppPVPo;
import com.yinhai.cloud.module.resource.pv.api.constants.PvConstants;
import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.cloud.module.resource.pv.impl.dao.IPersistentVolumeDao;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tianhy on 2018/7/9.
 */
public class AppPVBpoImpl extends TaBaseBpo implements IAppPVBpo {

    @Resource
    private AppPVDao appPVDao;

    @Resource
    private IPersistentVolumeDao persistentVolumeDao;

    @Override
    public List<AppPVVo> getPVsByAppId(Long appId) {
        List<AppPVPo> list = appPVDao.getPVsByAppId(appId);
        return list.stream().map(po -> {
            AppPVVo vo = po.toVo(new AppPVVo());
            PvVo pvVo = persistentVolumeDao.queryByPoId(Long.valueOf(po.getVolumeId()));
            if (pvVo != null) {
                vo.setVolumeDisplayName(pvVo.getVolumeDisplayName());
                vo.setVolumeType(pvVo.getVolumeType());
                vo.setVolumeTypeDesc(CodeTableUtil.getDesc(PvConstants.PV_TYPE_CODE, vo.getVolumeType()));
                vo.setVolumeUuid(pvVo.getVolumeUuid());
                vo.setVolumeMaxSpace(pvVo.getVolumeMaxSpace());
                vo.setVolumeAvailableSpace(pvVo.getVolumeAvailableSpace());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public AppPVVo savePV(AppPVVo vo) {
        AppPVPo po = appPVDao.insert(vo.toPO(new AppPVPo()));
        vo.setId(po.getId());
        return vo;
    }

    @Override
    public void removePV(Long id) {
        appPVDao.delete(appPVDao.select(id));
    }

    @Override
    public void removePVByAppId(Long appId) {
        appPVDao.deleteByAppId(appId);
    }

    @Override
    public AppPVVo getPV(Long id) {
        return appPVDao.select(id).toVo(new AppPVVo());
    }

    @Override
    public boolean checkPVUsed(Long volumeId) {
        return ValidateUtil.isNotEmpty(appPVDao.getPVsByVolumeId(volumeId));
    }

}
