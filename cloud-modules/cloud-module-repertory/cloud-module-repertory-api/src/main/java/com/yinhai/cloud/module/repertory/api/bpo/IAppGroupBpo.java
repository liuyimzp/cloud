package com.yinhai.cloud.module.repertory.api.bpo;

import com.yinhai.cloud.module.repertory.api.vo.AppGroupVo;

/**
 * Created by tianhy on 2018/7/30.
 */
public interface IAppGroupBpo {

    String SERVICEKEY = "appGroupBpo";

    /**
     * 检查应用分组标识是否存在
     *
     * @param groupIdentify
     * @return
     */
    Boolean checkIdentifyExist(String groupIdentify);

    /**
     * 保存应用分组信息
     *
     * @param vo
     * @return
     */
    AppGroupVo saveAppGroup(AppGroupVo vo);

    /**
     * 根据主键获取应用分组信息
     *
     * @param groupId
     * @return
     */
    AppGroupVo getAppGroup(Long groupId);

    /**
     * 根据所属业务领域获取分组信息
     *
     * @param vo
     * @return
     */
    Object getAppGroups(AppGroupVo vo);
}
