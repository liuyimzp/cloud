package com.yinhai.cloud.module.application.api.bpo;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.vo.AppServiceVo;

import java.util.List;

/**
 * Created by tianhy on 2018/6/14.
 */
public interface IAppServiceBpo {
    String SERVICEKEY = "appServiceBpo";

    /**
     * 根据应用ID查询应用服务信息
     *
     * @param appId
     * @return
     */
    List<AppServiceVo> getAppServicesByAppId(Long appId);

    /**
     * 保存应用服务信息
     *
     * @param vo
     */
    void saveAppService(AppServiceVo vo);

    /**
     * 修改应用服务信息
     *
     * @param vo
     */
    void editAppService(AppServiceVo vo);

    /**
     * 查询集群下映射端口是否被占用
     *
     * @param clusterId
     * @param mappingPort
     * @return
     */
    Boolean checkMappingPortExist(Long clusterId, Integer mappingPort);

    /**
     * 删除应用的所有服务配置信息
     *
     * @param appId
     */
    void removeAppServiceByAppId(Long appId);

    /**
     * 删除应用服务配置信息
     *
     * @param id
     */
    void removeAppService(Long id);

    /**
     * 检查该应用的内部端口是否已被占用
     *
     * @param appId
     * @param innerPort
     * @return
     */
    Boolean checkInnerPortExist(Long appId, Integer innerPort);

    /**
     * 检查修改的端口是否被占用
     *
     * @param vo
     * @return
     */
    CommonResultVo checkEditPort(AppServiceVo vo);
}
