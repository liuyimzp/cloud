package com.yinhai.cloud.module.repertory.impl.dao.impl;

import com.yinhai.cloud.module.repertory.impl.dao.AppRepertoryDao;
import com.yinhai.cloud.module.repertory.impl.po.AppRepertoryPo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by tianhy on 2018/5/17.
 */
public class AppRepertoryDaoImpl extends HibernateDAO<AppRepertoryPo> implements AppRepertoryDao {
    @Override
    public AppRepertoryPo select(Long id) {
        return super.get(id);
    }

    @Override
    public AppRepertoryPo insert(AppRepertoryPo po) {
        super.save(po);
        return po;
    }

    @Override
    public void delete(AppRepertoryPo po) {
        super.delete(po);
    }

    @Override
    public void update(AppRepertoryPo po) {
        super.update(po);
    }

    @Override
    public List<AppRepertoryPo> selectByIdentify(Long groupId, String identify) {
        return super.find(" from " + super.getEntityClassName(AppRepertoryPo.class.getName()) + " where groupId = ? and identify = ?", groupId, identify);
    }

    @Override
    public List<AppRepertoryPo> getRepertorysByIdentify(String identify, String businessArea, Long groupId) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" from ").append(super.getEntityClassName(AppRepertoryPo.class.getName())).append(" where identify = ?");
        if(ValidateUtil.isNotEmpty(businessArea)){
            sql.append(" and businessArea = '" + businessArea + "'");
        }
        if(!ValidateUtil.isEmpty(groupId)){
            sql.append(" and groupId = " + groupId);
        }
        return super.find(sql.toString(), identify);
    }

}
