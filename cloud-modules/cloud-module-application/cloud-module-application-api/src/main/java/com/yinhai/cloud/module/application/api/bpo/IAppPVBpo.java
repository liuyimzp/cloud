package com.yinhai.cloud.module.application.api.bpo;

import com.yinhai.cloud.module.application.api.vo.AppPVVo;

import java.util.List;

/**
 * Created by tianhy on 2018/7/9.
 */
public interface IAppPVBpo {

    String SERVICEKEY = "appPVBpo";

    /**
     * 根据应用ID查询存储(PV)配置
     *
     * @param appId
     * @return
     */
    List<AppPVVo> getPVsByAppId(Long appId);

    /**
     * 保存应用存储(PV)配置
     *
     * @param vo
     */
    AppPVVo savePV(AppPVVo vo);

    /**
     * 删除应用存储配置
     *
     * @param id
     */
    void removePV(Long id);
    void removePVByAppId(Long appId);
    /**
     * 根据主键查询配置
     * @param id
     * @return
     */
    AppPVVo getPV(Long id);

    /**
     * 检查pv是否被使用
     * @param volumeId
     * @return
     */
    boolean checkPVUsed(Long volumeId);
}
