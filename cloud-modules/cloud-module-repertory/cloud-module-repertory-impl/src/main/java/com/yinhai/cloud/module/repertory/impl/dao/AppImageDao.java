package com.yinhai.cloud.module.repertory.impl.dao;

import com.yinhai.cloud.module.repertory.api.vo.AppImageQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.repertory.impl.po.AppImagePo;
import com.yinhai.core.common.api.base.IPage;

import java.util.List;

/**
 * Created by tianhy on 2018/5/17.
 */
public interface AppImageDao {
    String SERVICEKEY = "appImageDao";

    AppImagePo select(Long id);

    AppImagePo insert(AppImagePo po);

    void update(AppImagePo po);

    void delete(AppImagePo po);

    void deleteByInvalid(AppImagePo po);

    void deleteByRepertoryId(Long id);

    IPage<AppImageVo> getQueryAppImages(AppImageQueryVo vo);

    List<AppImagePo> getAppImagesByRepertoryIdAndVersion(Long repertoryId, String version);

    List<AppImagePo> getAppImagesByRepertoryId(Long repertoryId);
}
