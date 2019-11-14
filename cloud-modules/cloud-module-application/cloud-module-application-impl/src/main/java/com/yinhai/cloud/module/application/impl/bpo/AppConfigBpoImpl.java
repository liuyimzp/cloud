package com.yinhai.cloud.module.application.impl.bpo;

import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.IAppConfigBpo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.util.NamespaceUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.application.impl.dao.AppConfigDao;
import com.yinhai.cloud.module.application.impl.po.AppConfigPo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppRepertoryBpo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.repertory.api.vo.AppRepertoryVo;
import com.yinhai.core.common.api.exception.AppException;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;

import javax.annotation.Resource;

/**
 * Created by tianhy on 2018/6/13.
 */
public class AppConfigBpoImpl extends TaBaseBpo implements IAppConfigBpo {

    @Resource
    private IAppBpo appBpo;

    @Resource
    private AppConfigDao appConfigDao;

    @Resource
    private IAppImageBpo appImageBpo;
    @Resource
    private IAppRepertoryBpo appRepertoryBpo;

    @Override
    public AppConfigVo getAppConfig(Long appId) {
        AppConfigPo po = appConfigDao.select(appId);
        if (po == null) {
            return null;
        }
        AppConfigVo vo = po.toVo(new AppConfigVo());
        if (vo.getAppImageId() != 0){
            AppImageVo appImage = appImageBpo.getAppImage(vo.getAppImageId());
            String version = appImage.getVersion();
            Long repertoryId = appImage.getRepertoryId();
            AppRepertoryVo appRepertory = appRepertoryBpo.getAppRepertory(repertoryId);
            vo.setAppImagePath(appRepertory.getImagePath() + ":" + version);
        }
        return vo;
    }

    @Override
    public void saveAppConfig(AppConfigVo vo, AppVo appVo,AppConfigVo oldVo) {
        AppConfigPo oldPo = appConfigDao.select(vo.getAppId());
        if (!ValidateUtil.isEmpty(oldVo)) {
            appConfigDao.delete(oldPo);
        }
        appConfigDao.insert(vo.toPO(new AppConfigPo()));
        if(ValidateUtil.isEmpty(appVo.getAppIdentify())){
            if (vo.getAppImageId() != 0){
                AppImageVo appImage = appImageBpo.getAppImage(vo.getAppImageId());
                AppRepertoryVo repertoryVo = appRepertoryBpo.getAppRepertory(appImage.getRepertoryId());
                AppVo exitAppVo = appBpo.getAppByIdentify(repertoryVo.getIdentify(),appVo.getNamespaceId());
                if(exitAppVo!=null){
                    throw new AppException("该命名空间下存在相同发布镜像，请重新选择镜像");
                }
                appVo.setAppIdentify(repertoryVo.getIdentify());
            }else{
                if (oldVo == null){
                    appVo.setAppStatus("1");
                }
            }
            if (ValidateUtil.isNotEmpty(appVo.getAppRestartPolicy())){
                appVo.setAppStatus(IAppConstants.APP_STATUS_UNSTART);
            }
            appBpo.editApp(appVo);
        }
    }

    @Override
    public void removeAppConfig(Long appId) {
        AppConfigPo po = appConfigDao.select(appId);
        if (!ValidateUtil.isEmpty(po)) {
            appConfigDao.delete(po);
        }
    }

    @Override
    public Boolean checkImageIdUsed(Long appImageId) {
        return ValidateUtil.isNotEmpty(appConfigDao.getAppConfigsByImageId(appImageId));
    }

    @Override
    public void updateAppConfig(AppConfigVo appConfig) {
        AppConfigPo po = appConfigDao.select(appConfig.getAppId());
        if(appConfig.getAppImageId() != null){
            po.setAppImageId(appConfig.getAppImageId());
        }
        if (appConfig.getMaxCPU() != null){
            po.setMaxCPU(appConfig.getMaxCPU());
        }
        if (appConfig.getMaxMemory() != null){
            po.setMaxMemory(appConfig.getMaxMemory());
        }
        if (appConfig.getMinCPU() != null){
            po.setMinCPU(appConfig.getMinCPU());
        }
        if (appConfig.getMinMemory() != null){
            po.setMinMemory(appConfig.getMinMemory());
        }
        appConfigDao.update(po);
    }

}
