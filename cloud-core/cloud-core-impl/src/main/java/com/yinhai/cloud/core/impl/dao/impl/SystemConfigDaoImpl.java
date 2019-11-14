package com.yinhai.cloud.core.impl.dao.impl;

import com.yinhai.cloud.core.impl.dao.SystemConfigDao;
import com.yinhai.cloud.core.impl.po.SystemConfigPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-08-15 09:59:26
 */
public class SystemConfigDaoImpl extends HibernateDAO<SystemConfigPo> implements SystemConfigDao {


    @Override
    public List<SystemConfigPo> getAllSystemConfig() {
        return find("from "+getEntityClassName(SystemConfigPo.class.getName()));
    }

    @Override
    public SystemConfigPo getSystemConfig(String propKey) {
        List<SystemConfigPo> list = find("from " + getEntityClassName(SystemConfigPo.class.getName()) + " where propKey = ?", propKey);
        if(list ==null || list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public void updateSystemConfig(SystemConfigPo systemConfigPo) {
        update(systemConfigPo);
    }

    @Override
    public void deleteSystemConfig(String propKey) {
        delete("delete from "+getEntityClassName(SystemConfigPo.class.getName()) +" where propKey = ?",propKey);
    }

    @Override
    public void insertSystemConfig(SystemConfigPo systemConfigPo) {
        save(systemConfigPo);
    }
}
