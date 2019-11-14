package com.yinhai.cloud.module.repertory.impl.dao;

import com.yinhai.cloud.module.repertory.impl.po.AppGroupPo;

import java.util.List;

/**
 * Created by tianhy on 2018/7/30.
 */
public interface AppGroupDao {

    String SERVICEKEY = "appGroupDao";

    AppGroupPo select(Long id);

    AppGroupPo insert(AppGroupPo po);

    void update(AppGroupPo po);

    void delete(AppGroupPo po);

    List<AppGroupPo> getAppGroupsByIdentify(String groupIdentify);

    List<AppGroupPo> getAppGroupsByBusinessArea(String businessArea);

    List<AppGroupPo> getAllAppGroups();
}
