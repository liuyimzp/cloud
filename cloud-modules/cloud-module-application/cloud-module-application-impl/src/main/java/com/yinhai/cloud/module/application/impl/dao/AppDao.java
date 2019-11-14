package com.yinhai.cloud.module.application.impl.dao;

import com.yinhai.cloud.module.application.api.vo.AppQueryVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.application.impl.po.AppPo;
import com.yinhai.core.common.api.base.IPage;

import java.util.List;

/**
 * Created by tianhy on 2018/6/12.
 */
public interface AppDao {
    String SERVICEKEY = "appDao";

    AppPo select(Long id);

    AppPo findAppByNI(Long namespaceId, Long imageId);

    AppPo findAppByIdenify(String identify,long namespaceId);

    AppPo insert(AppPo po);

    void update(AppPo po);

    void delete(AppPo po);

    List<AppPo> getAllApps();

    List<AppPo> selectByAppIdentify(Long clusterId, Long namespaceId, String appIdentify);

    List<AppPo> queryMiddleWare(Long clusterId, String middleWareType);

    List<AppPo> getAppByClusterId(Long clusterId);

    IPage<AppPo> getQueryApps(AppQueryVo vo);

    List<AppPo> getAppVoByNamespaceId(Long namespaceId);

    //带权限数据查询
    IPage<AppPo> getQueryApps(AppQueryVo vo, List<Long> authorityList);
}
