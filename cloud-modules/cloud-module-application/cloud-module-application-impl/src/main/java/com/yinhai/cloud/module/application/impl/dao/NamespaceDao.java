package com.yinhai.cloud.module.application.impl.dao;

import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.cloud.module.application.impl.po.NamespacePo;

import java.util.List;

/**
 * Created by tianhy on 2018/7/6.
 */
public interface NamespaceDao {

    String SERVICEKEY = "namespaceDao";

    NamespacePo select(Long id);

    NamespacePo insert(NamespacePo po);

    void update(NamespacePo po);

    void delete(NamespacePo po);

    List<NamespacePo> getNamespacesByClusterId(Long clusterId);

    void deleteByClusterId(Long clusterId);

    List<NamespacePo> getAllNamespaces();

    List<NamespacePo> getNamespacesByClusterIdAndIdentify(Long clusterId, String namespaceIdentify);
}
