package com.yinhai.cloud.core.api.util;

import com.yinhai.cloud.core.api.bpo.ISystemConfigBpo;
import com.yinhai.cloud.core.api.vo.SystemConfigVo;
import com.yinhai.core.app.api.util.ServiceLocator;

/**
 * @author: zhaokai
 * @create: 2018-08-15 14:45:18
 */
public class SystemConfigUtil {
    private static ISystemConfigBpo systemConfigBpo = ServiceLocator.getService(ISystemConfigBpo.class);

    public static SystemConfigVo getSystemConfig(String propKey) {
        SystemConfigVo vo = systemConfigBpo.getSystemConfig(propKey);
        if (vo == null) {
            throw new IllegalArgumentException("PropKey:" + propKey + "doesn't exist!");
        }
        return vo;
    }


    public static String getSystemConfigValue(String propKey) {
        return getSystemConfig(propKey).getPropValue();
    }

    public static Integer getSystemConfigValueToInteger(String propKey, int defaultValue) {
        SystemConfigVo vo = systemConfigBpo.getSystemConfig(propKey);
        if (vo == null) {
            return defaultValue;
        }
        return Integer.parseInt(vo.getPropValue());

    }
}
