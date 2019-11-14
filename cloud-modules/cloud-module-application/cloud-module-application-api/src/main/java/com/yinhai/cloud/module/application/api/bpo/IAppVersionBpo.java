package com.yinhai.cloud.module.application.api.bpo;

import com.yinhai.cloud.module.application.api.vo.AppQueryVo;
import com.yinhai.cloud.module.application.api.vo.AppVersionVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.core.common.api.base.IPage;

import java.util.List;
import java.util.Map;

/**
 * Created by liuyi02 on 2019/10/28.
 */
public interface IAppVersionBpo {

    /**
     * 查询一个应用已经发布的所有的版本
     * @param id
     * @return
     */
    List<AppVersionVo> getAllByAppId(Long id);

    /**
     * 修改版本信息
     * @param version
     */
    void updateVersion(AppVersionVo version);

    /**
     * 查询应用的某个镜像版本是否存在
     * @param id
     * @param appImageId
     * @return
     */
    List<AppVersionVo> getVersionByAppIdAndImage(Long id, Long appImageId);

    /**
     * 得到应用当前最高版本
     * @param id
     * @return
     */
    int getMaxVersion(Long id);

    /**
     * 处理存入数据以便存入
     * @param appVersionVo
     */
    AppVersionVo saveVersion(AppVersionVo appVersionVo);

    /**
     * 将某一版本设为使用其余版本设为未使用
     * @param vo
     */
    void setVersionIsUse(AppVersionVo vo);

    /**
     * 查询当前正在使用的版本
     * @param appId
     * @return
     */
    List<AppVersionVo> getUseVersion(Long appId);

    /**
     * 获得指定id的版本信息
     * @param id
     * @return
     */
    AppVersionVo getVersion(Long id);

    /**
     * 删除一个版本信息
     * @param vo
     */
    void deleteVersion(AppVersionVo vo) throws Exception;
}
