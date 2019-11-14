package com.yinhai.cloud.module.application.api.bpo;

import com.yinhai.cloud.module.application.api.vo.NamespaceVo;

import java.util.List;

/**
 * Created by tianhy on 2018/7/6.
 */
public interface INamespaceBpo {

    String SERVICEKEY = "namespaceBpo";

    /**
     * 根据主键查询命名空间
     *
     * @param id
     * @return
     */
    NamespaceVo getNamespace(Long id);

    /**
     * 查询命名空间信息
     *
     * @param clusterId
     * @return
     */
    List<NamespaceVo> getNamespaces(Long clusterId);

    /**
     * 保存命名空间
     *
     * @param vo
     */
    NamespaceVo saveNamespace(NamespaceVo vo);

    /**
     * 删除集群下的所有命名空间
     * @param clusterId
     */
    void deleteByClusterId(Long clusterId);

    /**
     * 修改命名空间信息
     * @param vo
     */
    void updateNamespace(NamespaceVo vo);

    /**
     * 删除命名空间
     * @param id
     */
    void deleteNamespace(Long id);

    /**
     * 查询该集群下是否已存在该命名空间标识
     * @param clusterId
     * @param namespaceIdentify
     * @return
     */
    boolean checkIdentifyExist(Long clusterId, String namespaceIdentify);
}
