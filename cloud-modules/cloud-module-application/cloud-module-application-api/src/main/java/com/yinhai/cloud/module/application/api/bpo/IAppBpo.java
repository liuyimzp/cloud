package com.yinhai.cloud.module.application.api.bpo;

import com.yinhai.cloud.module.application.api.vo.AppPodVo;
import com.yinhai.cloud.module.application.api.vo.AppQueryVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.core.common.api.base.IPage;

import java.util.List;
import java.util.Map;

/**
 * Created by tianhy on 2018/6/12.
 */
public interface IAppBpo {

    String SERVICEKEY = "appBpo";


    /**
     * 根据应用ID获取应用信息
     *
     * @param appId
     * @return
     */
    AppVo getApp(Long appId);

    /**
     * 根据命名空间和镜像 获取应用信息
     * @param namespaceId
     * @param imageId
     * @return
     */
    AppVo getAppByNI(Long namespaceId,Long imageId);

    /**
     * 根据标识和命名空间查询
     * @param identify
     * @return
     */
    AppVo getAppByIdentify(String identify,long namespaceId);
    /**
     * 查询所有集群信息
     *
     * @return
     */
    List<ClusterVo> getAllClusters();

    /**
     * 检查应用标识是否已存在
     *
     * @param vo
     * @return
     */
    boolean checkAppIdentifyExist(AppVo vo);

    /**
     * 检查应用标识是否已存在
     *
     * @param vo
     * @return
     */
    boolean saveFile(Long id,String appDiyFile,String appStatus);

    /**
     * 保存应用信息
     *
     * @param vo
     */
    AppVo saveApp(AppVo vo);

    /**
     * 检查修改后的应用标识是否已存在
     *
     * @param vo
     * @return
     */
    boolean checkIdentifyChange(AppVo vo);

    /**
     * 修改应用信息
     *
     * @param vo
     */
    void editApp(AppVo vo);

    /**
     * 删除应用信息
     *
     * @param id
     */
    void removeApp(Long id);

    /**
     * 查询集群下，相同中间件的数量
     *
     * @param clusterId
     * @param middleWareType
     * @return
     */
    Long queryMiddleWareCount(Long clusterId, String middleWareType);


    void deleteAppByClusterId(Long clusterId);

    /**
     * 根据条件查询应用列表
     *
     * @param vo
     * @return
     */
    IPage<Map> getQueryApps(AppQueryVo vo);
    /**
     * 根据条件查询应用（带权限）
     * @param vo
     * @param userId
     * @return
     */
    IPage<Map> getQueryApps(AppQueryVo vo,Long userId);

    /**
     * 根据命名空间Id查询应用
     * @param namespaceId
     * @return
     */
    List<AppVo> getAppVoByNamespaceId(Long namespaceId);

}
