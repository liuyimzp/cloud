package com.yinhai.cloud.module.application.impl.bpo;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.IAppServiceBpo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.vo.AppServiceVo;
import com.yinhai.cloud.module.application.impl.dao.AppDao;
import com.yinhai.cloud.module.application.impl.dao.AppServiceDao;
import com.yinhai.cloud.module.application.impl.po.AppPo;
import com.yinhai.cloud.module.application.impl.po.AppServicePo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianhy on 2018/6/14.
 */
public class AppServiceBpoImpl extends TaBaseBpo implements IAppServiceBpo {

    @Resource
    private AppServiceDao appServiceDao;

    @Resource
    private AppDao appDao;

    @Override
    public List<AppServiceVo> getAppServicesByAppId(Long appId) {
        List<AppServicePo> list = appServiceDao.getAppServicesByAppId(appId);
        List<AppServiceVo> result = new ArrayList<>();
        for (AppServicePo po : list) {
            result.add(po.toVo(new AppServiceVo()));
        }
        return result;
    }

    @Override
    public void saveAppService(AppServiceVo vo) {
        appServiceDao.insert(vo.toPO(new AppServicePo()));
    }

    @Override
    public void editAppService(AppServiceVo vo) {
        AppServicePo po = appServiceDao.select(vo.getId());
        po.setInnerPort(vo.getInnerPort());
        po.setMappingPort(vo.getMappingPort());
        po.setProtocolType(vo.getProtocolType());
        po.setServiceType(vo.getServiceType());
        appServiceDao.update(po);
    }

    @Override
    public Boolean checkMappingPortExist(Long clusterId, Integer mappingPort) {
        return ValidateUtil.isNotEmpty(appServiceDao.getAppServicesByClusterIdAndMappingPort(clusterId, mappingPort));
    }

    @Override
    public void removeAppServiceByAppId(Long appId) {
        appServiceDao.deleteByAppId(appId);
    }

    @Override
    public void removeAppService(Long id) {
        appServiceDao.delete(appServiceDao.select(id));
    }

    @Override
    public Boolean checkInnerPortExist(Long appId, Integer innerPort) {
        return ValidateUtil.isNotEmpty(appServiceDao.getAppServicesByAppIdAndInnerPort(appId, innerPort));
    }

    @Override
    public CommonResultVo checkEditPort(AppServiceVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (IAppConstants.SERVICE_TYPE_OUTER.equals(vo.getServiceType())) {
            AppPo appPo = appDao.select(vo.getAppId());
            List<AppServicePo> appServiceList = appServiceDao.getAppServicesByClusterIdAndMappingPort(appPo.getClusterId(), vo.getMappingPort());
            if (ValidateUtil.isNotEmpty(appServiceList)) {
                for (AppServicePo appServicePo : appServiceList) {
                    if (!ValidateUtil.areEqual(appServicePo.getId(), vo.getId())) {
                        result.setSuccess(false);
                        result.setErrorMsg("该映射端口已被占用!");
                        return result;
                    }
                }
            }
        }
        List<AppServicePo> appServiceList2 = appServiceDao.getAppServicesByAppIdAndInnerPort(vo.getAppId(), vo.getInnerPort());
        if (ValidateUtil.isNotEmpty(appServiceList2)) {
            for (AppServicePo appServicePo : appServiceList2) {
                if (!ValidateUtil.areEqual(appServicePo.getId(), vo.getId())) {
                    result.setSuccess(false);
                    result.setErrorMsg("该内部端口已被占用!");
                    return result;
                }
            }
        }
        result.setSuccess(true);
        return result;
    }
}
