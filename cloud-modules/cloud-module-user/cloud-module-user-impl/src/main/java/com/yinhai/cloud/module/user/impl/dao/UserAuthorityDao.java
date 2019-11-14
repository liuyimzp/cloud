package com.yinhai.cloud.module.user.impl.dao;

import com.yinhai.cloud.module.repertory.api.vo.AppImageQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.user.impl.po.UserAuthorityId;
import com.yinhai.cloud.module.user.impl.po.UserAuthorityPo;
import com.yinhai.core.common.api.base.IPage;

import java.util.List;

/**
 * Created by tianhy on 2018/9/11.
 */
public interface UserAuthorityDao {

    String SERVICEKEY = "userAuthorityDao";

    UserAuthorityPo select(UserAuthorityId id);

    UserAuthorityPo insert(UserAuthorityPo po);

    void update(UserAuthorityPo po);

    void delete(UserAuthorityPo po);

    List<UserAuthorityPo> getUserAppAuthoritys(Long userid);

    List<UserAuthorityPo> getUserImageAuthoritys(Long userid);

    void deleteByUserid(Long userid);

    List<UserAuthorityPo> getUserClusterAuthoritys(Long userid);

    /**
     * 获取用户命名空间权限
     * @param userid
     * @return List<UserAuthorityPo>
     */
    List<UserAuthorityPo> getUserNameSpaceAuthoritys(Long userid);

    IPage<AppImageVo> getUserAuthorityQueryAppImages(AppImageQueryVo vo, Long userid);
}
