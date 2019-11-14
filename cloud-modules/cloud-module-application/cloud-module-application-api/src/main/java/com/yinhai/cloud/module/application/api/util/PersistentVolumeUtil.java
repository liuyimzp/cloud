package com.yinhai.cloud.module.application.api.util;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.IAppPVBpo;
import com.yinhai.cloud.module.application.api.vo.AppPVVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.resource.pv.api.bpo.IPersistentVolumeBpo;
import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.common.api.util.ValidateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tianhy on 2018/12/17.
 */
public class PersistentVolumeUtil {

    public static CommonResultVo checkPVSpaceEnough(Long volumeId, Integer space, int instanceNum){
        CommonResultVo result = new CommonResultVo();
        IPersistentVolumeBpo pvBpo = (IPersistentVolumeBpo) ServiceLocator.getService(IPersistentVolumeBpo.SERVICEKEY);
        PvVo pvVo = pvBpo.queryByPvId(volumeId);
        if(space * instanceNum > pvVo.getVolumeAvailableSpace()){
            result.setSuccess(false);
            result.setErrorMsg("存储超过存储池可用空间!");
        }else {
            result.setSuccess(true);
        }
        return result;
    }

    public static CommonResultVo checkPVSpaceEnoughWithInstanceIncrease(AppVo appVo, int increaseNum) {
        CommonResultVo result = new CommonResultVo();
        IAppPVBpo appPVBpo = (IAppPVBpo) ServiceLocator.getService(IAppPVBpo.SERVICEKEY);
        List<AppPVVo> appPVList = appPVBpo.getPVsByAppId(appVo.getId());
        if(ValidateUtil.isNotEmpty(appPVList)){
            Map<Long, Integer> spaceMap = new HashMap<>();
            for (AppPVVo appPVVo : appPVList) {
                Integer space = ValidateUtil.isEmpty(spaceMap.get(appPVVo.getVolumeId())) ? 0 : spaceMap.get(appPVVo.getVolumeId());
                space += appPVVo.getSpace();
                spaceMap.put(appPVVo.getVolumeId(), space);
            }
            for (Long volumeId : spaceMap.keySet()) {
                result = checkPVSpaceEnough(volumeId, spaceMap.get(volumeId), increaseNum);
                if(!result.isSuccess()){
                    return result;
                }
            }
        }
        result.setSuccess(true);
        return result;
    }

    public static void changePVSpace(Long volumeId, Integer space, int instanceNum) {
        IPersistentVolumeBpo pvBpo = (IPersistentVolumeBpo) ServiceLocator.getService(IPersistentVolumeBpo.SERVICEKEY);
        PvVo pvVo = pvBpo.queryByPvId(volumeId);
        pvVo.setVolumeAvailableSpace(pvVo.getVolumeAvailableSpace() - space * instanceNum);
        try {
            pvBpo.updatePv(pvVo);
        } catch (Exception e) {
        }
    }

    public static void changePVSpaceWithInstanceChange(AppVo appVo, int increaseNum) {
        IAppPVBpo appPVBpo = (IAppPVBpo) ServiceLocator.getService(IAppPVBpo.SERVICEKEY);
        List<AppPVVo> appPVList = appPVBpo.getPVsByAppId(appVo.getId());
        if(ValidateUtil.isNotEmpty(appPVList)){
            Map<Long, Integer> spaceMap = new HashMap<>();
            for (AppPVVo appPVVo : appPVList) {
                Integer space = ValidateUtil.isEmpty(spaceMap.get(appPVVo.getVolumeId())) ? 0 : spaceMap.get(appPVVo.getVolumeId());
                space += appPVVo.getSpace();
                spaceMap.put(appPVVo.getVolumeId(), space);
            }
            for (Long volumeId : spaceMap.keySet()) {
                changePVSpace(volumeId, spaceMap.get(volumeId), increaseNum);
            }
        }
    }

    public static void changePVSpaceWithAppDelete(AppVo appVo) {
        changePVSpaceWithInstanceChange(appVo, 0 - appVo.getInstanceNum());
    }
}
