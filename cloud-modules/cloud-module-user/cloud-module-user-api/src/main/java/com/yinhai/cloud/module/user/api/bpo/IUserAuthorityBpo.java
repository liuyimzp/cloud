package com.yinhai.cloud.module.user.api.bpo;

import com.yinhai.cloud.module.repertory.api.vo.AppImageQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.user.api.vo.UserAuthorityVo;
import com.yinhai.core.common.api.base.IPage;

import java.util.List;

/**
 * Created by tianhy on 2018/9/11.
 */
public interface IUserAuthorityBpo {

    String SERVICEKEY = "userAuthorityBpo";

    /**
     * 获取全部应用权限节点
     *
     * @return
     */
    List<UserAuthorityVo> getAllAppAuthorityNodes();

    /**
     * 获取全部镜像权限节点
     *
     * @return
     */
    List<UserAuthorityVo> getAllImageAuthorityNodes();

    /**
     * 获取人员应用权限信息
     *
     * @param userid
     * @return
     */
    List<UserAuthorityVo> getUserAppAuthoritys(Long userid);

    /**
     * 获取人员镜像权限信息
     *
     * @param userid
     * @return
     */
    List<UserAuthorityVo> getUserImageAuthoritys(Long userid);

    /**
     * 保存人员权限信息
     *
     * @param userId
     * @param list
     */
    void saveAuthoritys(Long userId, List<UserAuthorityVo> list);

    /**
     * 删除人员权限信息
     *
     * @param userId
     * @param list
     */
    void removeAuthoritys(Long userId, List<UserAuthorityVo> list);

    /**
     * 根据人员id删除人员权限信息
     *
     * @param userid
     */
    void deleteUserAuthpritysByUserid(Long userid);

    /**
     * 查询人员有权限的集群信息
     * @param userid
     * @return
     */
    List<UserAuthorityVo> getUserClusterAuthoritys(Long userid);

    /**
     * 获取用户拥有权限的所有命名空间权限
     * @param userid
     * @return List<Long>
     */
    List<Long> getUserNameSpaceIds(Long userid);

    IPage<AppImageVo> getQueryAppImages(AppImageQueryVo vo, Long userid);
}
