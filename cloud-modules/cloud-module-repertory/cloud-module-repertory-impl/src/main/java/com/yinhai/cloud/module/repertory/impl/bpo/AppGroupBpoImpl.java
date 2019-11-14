package com.yinhai.cloud.module.repertory.impl.bpo;

import com.yinhai.cloud.module.repertory.api.bpo.IAppGroupBpo;
import com.yinhai.cloud.module.repertory.api.vo.AppGroupVo;
import com.yinhai.cloud.module.repertory.impl.dao.AppGroupDao;
import com.yinhai.cloud.module.repertory.impl.po.AppGroupPo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * Created by tianhy on 2018/7/30.
 */
public class AppGroupBpoImpl extends TaBaseBpo implements IAppGroupBpo {

    @Resource
    private AppGroupDao appGroupDao;

    @Override
    public Object getAppGroups(AppGroupVo vo) {
        return appGroupDao.getAppGroupsByBusinessArea(vo.getBusinessArea()).stream().map(po -> po.toVo(new AppGroupVo())).collect(Collectors.toList());
    }

    @Override
    public Boolean checkIdentifyExist(String groupIdentify) {
        return ValidateUtil.isNotEmpty(appGroupDao.getAppGroupsByIdentify(groupIdentify));
    }

    @Override
    public AppGroupVo saveAppGroup(AppGroupVo vo) {
        return appGroupDao.insert(vo.toPO(new AppGroupPo())).toVo(new AppGroupVo());
    }

    @Override
    public AppGroupVo getAppGroup(Long groupId) {
        AppGroupPo po = appGroupDao.select(groupId);
        return ValidateUtil.isEmpty(po) ? null : po.toVo(new AppGroupVo());
    }
}
