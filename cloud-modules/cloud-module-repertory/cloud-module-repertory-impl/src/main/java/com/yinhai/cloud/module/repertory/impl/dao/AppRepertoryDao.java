package com.yinhai.cloud.module.repertory.impl.dao;

import com.yinhai.cloud.module.repertory.impl.po.AppRepertoryPo;

import java.util.List;

/**
 * Created by tianhy on 2018/5/17.
 */
public interface AppRepertoryDao {

    String SERVICEKEY = "appRepertoryDao";

    AppRepertoryPo select(Long id);

    AppRepertoryPo insert(AppRepertoryPo po);

    void update(AppRepertoryPo po);

    void delete(AppRepertoryPo po);

    List<AppRepertoryPo> selectByIdentify(Long groupId, String identify);

    List<AppRepertoryPo> getRepertorysByIdentify(String identify, String businessArea, Long groupId);
}
