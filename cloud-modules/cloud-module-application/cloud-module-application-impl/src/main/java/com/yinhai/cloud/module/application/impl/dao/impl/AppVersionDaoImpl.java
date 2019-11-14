package com.yinhai.cloud.module.application.impl.dao.impl;


import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.vo.AppVersionVo;
import com.yinhai.cloud.module.application.impl.dao.AppVersionDao;
import com.yinhai.cloud.module.application.impl.po.AppVersionPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by liuyi02 on 2019/10/28.
 */
public class AppVersionDaoImpl extends HibernateDAO<AppVersionPo> implements AppVersionDao {

    @Override
    public List<AppVersionPo> getAllByAppId(Long id) {
        return super.find("from AppVersionPo where appId = ?",id);
    }

    @Override
    public AppVersionPo getVersion(Long id) {
        return get(id);
    }

    @Override
    public void updateVersion(AppVersionPo po) {
        super.update(po);
    }

    @Override
    public List<AppVersionPo> getVersionByAppIdAndImage(Long id, Long appImageId) {
        return super.find("from AppVersionPo where appId = ? and imageId = ?",id,appImageId);
    }

    @Override
    public AppVersionPo saveVersion(AppVersionPo appVersionPo) {
        super.save(appVersionPo);
        return appVersionPo;
    }

    @Override
    public List<AppVersionPo> queryUseVersion(Long appId) {
        return super.find("from AppVersionPo where appId = ? and isuse = ?",appId, IAppConstants.APPVERSION_IS_USE);
    }

    @Override
    public void deleteById(Long id) {
        super.delete("delete from AppVersionPo where id = ?",id);
    }
}
