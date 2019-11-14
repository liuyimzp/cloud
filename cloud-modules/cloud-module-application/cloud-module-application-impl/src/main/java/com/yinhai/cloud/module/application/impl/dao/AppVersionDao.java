package com.yinhai.cloud.module.application.impl.dao;


import com.yinhai.cloud.module.application.api.vo.AppVersionVo;
import com.yinhai.cloud.module.application.impl.po.AppVersionPo;
import com.yinhai.core.domain.api.domain.po.IPo;

import java.util.List;

/**
 * Created by liuyi02 on 2019/10/28.
 */
public interface AppVersionDao {


    /**
     * 查询数据库中关于应用版本的信息
     * @param id
     * @return
     */
    List<AppVersionPo> getAllByAppId(Long id);

    /**
     * 获得某一个版本信息
     * @param id
     * @return
     */
    AppVersionPo getVersion(Long id);

    /**
     * 修改版本信息
     * @param po
     */
    void updateVersion(AppVersionPo po);

    /**
     * 查询数据库通过应用ID和镜像id
     * @param id
     * @param appImageId
     * @return
     */
    List<AppVersionPo> getVersionByAppIdAndImage(Long id, Long appImageId);

    /**
     * 往数据库中写一条版本信息的数据
     * @param appVersionPo
     * @return
     */
    AppVersionPo saveVersion(AppVersionPo appVersionPo);

    /**
     * 查询数据库中该应用当前使用版本
     * @param appId
     * @return
     */
    List<AppVersionPo> queryUseVersion(Long appId);

    /**
     * 删除数据库中一条版本信息
     * @param id
     */
    void deleteById(Long id);
}
