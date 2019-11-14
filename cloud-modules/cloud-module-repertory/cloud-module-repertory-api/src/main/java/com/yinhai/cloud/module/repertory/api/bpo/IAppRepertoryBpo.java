package com.yinhai.cloud.module.repertory.api.bpo;

import com.yinhai.cloud.module.repertory.api.vo.AppRepertoryQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppRepertoryVo;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.modules.codetable.api.domain.vo.AppCodeVo;

import java.util.List;

/**
 * Created by tianhy on 2018/5/17.
 */
public interface IAppRepertoryBpo {
    String SERVICEKEY = "appRepertoryBpo";

    /**
     * 保存应用仓库信息
     *
     * @param vo
     */
    void saveAppRepertory(AppRepertoryVo vo);

    /**
     * 检查英文标识是否已存在
     *
     *
     * @param groupId
     * @param identify
     * @return
     */
    boolean checkIdentifyExist(Long groupId, String identify);

    /**
     * 检查英文标识是否可修改
     *
     * @param id
     * @param groupId
     *@param identify  @return
     */
    boolean checkIdentifyChange(Long id, Long groupId, String identify);

    /**
     * 修改应用仓库信息
     *
     * @param vo
     */
    void editAppRepertory(AppRepertoryVo vo);

    /**
     * 删除应用仓库信息
     *
     * @param id
     */
    void removeAppRepertory(Long id);

    /**
     * 获取应用仓库信息
     *
     * @param id
     * @return
     */
    AppRepertoryVo getAppRepertory(Long id);

    /**
     * 根据条件查询应用仓库列表
     *
     * @param vo
     * @return
     */
    IPage<AppRepertoryVo> getQueryAppRepertories(AppRepertoryQueryVo vo,Long userId);

    /**
     * 查询所有可用镜像仓库
     * @param userid
     * @return
     */
    List<AppRepertoryVo> getAllAppRepertoryVo(Long userid);

    /**
     * 查询用户被分配有权限的业务领域
     * @param vo
     * @param userId
     * @return
     */
    List<AppCodeVo> getUserBusinessArea(AppCodeVo vo, Long userId);
}
