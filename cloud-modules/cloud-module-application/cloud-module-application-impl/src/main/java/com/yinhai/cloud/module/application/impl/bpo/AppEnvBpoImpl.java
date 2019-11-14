package com.yinhai.cloud.module.application.impl.bpo;

import com.yinhai.cloud.module.application.api.bpo.IAppEnvBpo;
import com.yinhai.cloud.module.application.api.vo.AppEnvVo;
import com.yinhai.cloud.module.application.impl.dao.AppEnvDao;
import com.yinhai.cloud.module.application.impl.po.AppEnvPo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tianhy on 2018/9/6.
 */
public class AppEnvBpoImpl extends TaBaseBpo implements IAppEnvBpo {

    @Resource
    private AppEnvDao appEnvDao;

    @Override
    public List<AppEnvVo> getAppEnvs(Long appId) {
        return appEnvDao.getAppEnvsByAppId(appId).stream().map(po -> po.toVo(new AppEnvVo())).collect(Collectors.toList());
    }

    @Override
    public AppEnvVo saveAppEnv(AppEnvVo vo) {
        return appEnvDao.insert(vo.toPO(new AppEnvPo())).toVo(new AppEnvVo());
    }

    @Override
    public boolean checkAppIdAndEnvKeyExist(Long appId, String envKey) {
        return ValidateUtil.isNotEmpty(appEnvDao.getAppEnvsByAppIdAndEnvKey(appId, envKey));
    }

    @Override
    public void editAppEnv(AppEnvVo vo) {
        AppEnvPo po = appEnvDao.select(vo.getId());
        po.setEnvValue(vo.getEnvValue());
        appEnvDao.update(po);
    }

    @Override
    public void removeAppEnv(Long id) {
        appEnvDao.delete(appEnvDao.select(id));
    }


}
