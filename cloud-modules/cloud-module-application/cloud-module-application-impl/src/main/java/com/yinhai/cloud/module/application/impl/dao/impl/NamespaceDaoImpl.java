package com.yinhai.cloud.module.application.impl.dao.impl;

import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.cloud.module.application.impl.dao.NamespaceDao;
import com.yinhai.cloud.module.application.impl.po.NamespacePo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by tianhy on 2018/7/6.
 */
public class NamespaceDaoImpl extends HibernateDAO<NamespacePo> implements NamespaceDao {
    @Override
    public NamespacePo select(Long id) {
        return super.get(id);
    }

    @Override
    public NamespacePo insert(NamespacePo po) {
        super.save(po);
        return po;
    }

    @Override
    public void update(NamespacePo po) {
        super.update(po);
    }

    @Override
    public void delete(NamespacePo po) {
        super.delete(po);
    }

    @Override
    public List<NamespacePo> getNamespacesByClusterId(Long clusterId) {
        return super.find("from " + getEntityClassName(NamespacePo.class.getName()) + " where clusterId = ?", clusterId);
    }

    @Override
    public List<NamespacePo> getNamespacesByClusterIdAndIdentify(Long clusterId, String namespaceIdentify) {
        return super.find("from " + getEntityClassName(NamespacePo.class.getName()) + " where clusterId = ? and namespaceIdentify = ?", clusterId, namespaceIdentify);
    }

    @Override
    public void deleteByClusterId(Long clusterId) {
        super.delete("delete from " + getEntityClassName(NamespacePo.class.getName()) + " where clusterId = ?", clusterId);
    }

    @Override
    public List<NamespacePo> getAllNamespaces() {
        return super.find("from " + getEntityClassName(NamespacePo.class.getName()));
    }

}
