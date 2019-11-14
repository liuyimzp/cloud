package com.yinhai.cloud.module.application.impl.bpo;

import com.yinhai.cloud.module.application.api.bpo.IConfigMapBpo;
import com.yinhai.cloud.module.application.api.util.sm4.SM4Utils;
import com.yinhai.cloud.module.application.api.vo.ConfigMapVo;
import com.yinhai.cloud.module.application.impl.dao.ConfigMapDao;
import com.yinhai.cloud.module.application.impl.po.ConfigMapPo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import com.yinhai.sysframework.util.IConstants;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tianhy on 2019/2/20.
 */
public class ConfigMapBpoImpl extends TaBaseBpo implements IConfigMapBpo {

    @Resource
    private ConfigMapDao configMapDao;

    @Override
    public List<ConfigMapVo> getConfigMapByAppId(Long appId) {
        return configMapDao.getByAppId(appId).stream().map(po -> po.toVo(new ConfigMapVo())).collect(Collectors.toList());
    }

    @Override
    public ConfigMapVo saveConfigMap(ConfigMapVo vo) {
        ConfigMapPo po = vo.toPO(new ConfigMapPo());
        po.setIsEnv(ValidateUtil.isEmpty(po.getIsEnv()) ? IConstants.COMMON_NO : po.getIsEnv());
        if(ValidateUtil.areEqual(IConstants.COMMON_YES, vo.getEncrypt())){
            po.setPropertyValue(SM4Utils.encrypt(vo.getPropertyValue()));
        }else {
            po.setEncrypt(IConstants.COMMON_NO);
        }
        return configMapDao.insert(po).toVo(new ConfigMapVo());
    }

    @Override
    public void editConfigMap(ConfigMapVo vo) {
        ConfigMapPo po = configMapDao.select(vo.getId());
        po.setPropertyName(vo.getPropertyName());
        if(ValidateUtil.areEqual(IConstants.COMMON_YES, vo.getEncrypt())){
            po.setPropertyValue(SM4Utils.encrypt(vo.getPropertyValue()));
        }else {
            po.setPropertyValue(vo.getPropertyValue());
        }
        configMapDao.update(po);
    }

    @Override
    public void removeConfigMap(Long id) {
        ConfigMapPo po = configMapDao.select(id);
        if(!ValidateUtil.isEmpty(po)){
            configMapDao.delete(po);
        }
    }

    @Override
    public void removeConfigMapByAppId(Long appId) {
        configMapDao.deleteByAppId(appId);
    }
}
