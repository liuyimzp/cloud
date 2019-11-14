package com.yinhai.cloud.module.application.impl.dao.impl;


import com.yinhai.cloud.module.application.impl.dao.AppDiyFileDao;
import com.yinhai.cloud.module.application.impl.po.AppDiyFileVo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

/**
 * Created by liuyi on 2019/3/27
 */
public class AppDiyFileDaoImpl extends HibernateDAO<AppDiyFileVo> implements AppDiyFileDao {

    @Override
    public boolean saveFile(Long id,String appDiyFile,String appStatus) {
        String hql = "update AppDiyFileVo set appDiyFile = ?,appStatus = ? where id = ?";
        super.update(hql,appDiyFile,appStatus,id);
        return true;
    }
}
