package com.yinhai.cloud.module.application.impl.dao.impl;

import com.yinhai.cloud.module.application.api.vo.AppQueryVo;
import com.yinhai.cloud.module.application.impl.dao.AppDao;
import com.yinhai.cloud.module.application.impl.po.AppPo;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tianhy on 2018/6/12.
 */
public class AppDaoImpl extends HibernateDAO<AppPo> implements AppDao {
    @Override
    public AppPo select(Long id) {
        return super.get(id);
    }

    @Override
    public AppPo findAppByNI(Long namespaceId, Long imageId) {
        return super.get("from " + super.getEntityClassName(AppPo.class.getName()) + " where namespaceId = ? and imageId = ?",namespaceId,imageId);
    }

    @Override
    public AppPo findAppByIdenify(String identify,long namespaceId) {
        return super.get("from " + super.getEntityClassName(AppPo.class.getName()) + " where appIdentify = ? and namespaceId= ?",identify,namespaceId);
    }

    @Override
    public AppPo insert(AppPo po) {
        super.save(po);
        return po;
    }

    @Override
    public void delete(AppPo o) {
        super.delete(o);
    }

    @Override
    public void update(AppPo o) {
        AppPo po = select(o.getId());
        super.update(po);
    }

    @Override
    public List<AppPo> getAllApps() {
        return super.find("from " + super.getEntityClassName(AppPo.class.getName()) + " where 1 = 1");
    }

    @Override
    public List<AppPo> selectByAppIdentify(Long clusterId, Long namespaceId, String appIdentify) {
        return super.find(" from " + super.getEntityClassName(AppPo.class.getName()) + " where clusterId = ? and namespaceId = ? and appIdentify = ?", clusterId, namespaceId, appIdentify);
    }

    @Override
    public List<AppPo> queryMiddleWare(Long clusterId, String middleWareType) {
        return super.find(" from " + super.getEntityClassName(AppPo.class.getName()) + " where clusterId = ? and middleWareType = ? order by id desc", clusterId, middleWareType);
    }

    @Override
    public List<AppPo> getAppByClusterId(Long clusterId) {
        return super.find(" from " + super.getEntityClassName(AppPo.class.getName()) + " where clusterId = ?", clusterId);
    }

    @Override
    public IPage<AppPo> getQueryApps(AppQueryVo vo) {
        Integer limit = ValidateUtil.isEmpty(vo.getPageSize()) ? 10 : vo.getPageSize();
        Integer start = ValidateUtil.isEmpty(vo.getCurrentPage()) || vo.getCurrentPage() <= 0 ? 0 : (vo.getCurrentPage() - 1) * limit;
        StringBuffer sql = new StringBuffer("");
        sql.append(" from " + super.getEntityClassName(AppPo.class.getName()) + " where 1 = 1");
        if (!ValidateUtil.isEmpty(vo.getClusterId())) {
            sql.append(" and clusterId = " + vo.getClusterId());
        }
        if (ValidateUtil.isNotEmpty(vo.getField())) {
            sql.append(" and (appName like '" + vo.getField() + "%' or appIdentify like '" + vo.getField() + "%')");
        }
        sql.append(" order by clusterId,namespaceId,id");
        return findForPage(sql.toString(), start, limit, "");
    }

    @Override
    public List<AppPo> getAppVoByNamespaceId(Long namespaceId) {
        return super.find(" from " + super.getEntityClassName(AppPo.class.getName()) + " where namespaceId = ?", namespaceId);
    }

    @Override
    public IPage<AppPo> getQueryApps(AppQueryVo vo, List<Long> authorityList) {
        Map<String,Object> map = new HashMap<>();
        map.put("namaspaceIds",authorityList);
        Integer limit = ValidateUtil.isEmpty(vo.getPageSize()) ? 10 : vo.getPageSize();
        Integer start = ValidateUtil.isEmpty(vo.getCurrentPage()) || vo.getCurrentPage() <= 0 ? 0 : (vo.getCurrentPage() - 1) * limit;
        StringBuffer sql = new StringBuffer("");
        sql.append(" from " + super.getEntityClassName(AppPo.class.getName()) + " where 1 = 1");
        if (!ValidateUtil.isEmpty(vo.getClusterId())) {
            sql.append(" and clusterId = " + vo.getClusterId());
        }
        if (ValidateUtil.isNotEmpty(vo.getField())) {
            sql.append(" and (appName like '" + vo.getField() + "%' or appIdentify like '" + vo.getField() + "%')");
        }
        sql.append(" and namespaceId in(:namaspaceIds) order by clusterId,namespaceId,id");
        return findForPage(sql.toString(),map, start, limit, "");
    }
}
