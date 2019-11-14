package com.yinhai.cloud.module.application.impl.bpo;

import com.yinhai.cloud.module.application.api.bpo.INamespaceBpo;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.cloud.module.application.impl.dao.NamespaceDao;
import com.yinhai.cloud.module.application.impl.po.NamespacePo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tianhy on 2018/7/6.
 */
public class NamespaceBpoImpl extends TaBaseBpo implements INamespaceBpo {

    @Resource
    private NamespaceDao namespaceDao;

    @Override
    public NamespaceVo getNamespace(Long id) {
        NamespacePo po = namespaceDao.select(id);
        return ValidateUtil.isEmpty(po) ? null : po.toVo(new NamespaceVo());
    }

    @Override
    public List<NamespaceVo> getNamespaces(Long clusterId) {
        List<NamespacePo> list = ValidateUtil.isEmpty(clusterId) ? namespaceDao.getAllNamespaces() : namespaceDao.getNamespacesByClusterId(clusterId);
        return list.stream().map(po -> po.toVo(new NamespaceVo())).collect(Collectors.toList());
    }

    @Override
    public NamespaceVo saveNamespace(NamespaceVo vo) {
        NamespacePo po = vo.toPO(new NamespacePo());
        namespaceDao.insert(po);
        return po.toVo(new NamespaceVo());
    }

    @Override
    public void deleteByClusterId(Long clusterId) {
        namespaceDao.deleteByClusterId(clusterId);
    }

    @Override
    public void updateNamespace(NamespaceVo vo) {
        NamespacePo po = namespaceDao.select(vo.getId());
        po.setNamespaceName(vo.getNamespaceName());
        po.setMaxCPULimit(vo.getMaxCPULimit());
        po.setMaxMemoryLimit(vo.getMaxMemoryLimit());
        po.setAvailableCPU(vo.getAvailableCPU());
        po.setAvailableMemory(vo.getAvailableMemory());
        namespaceDao.update(po);
    }

    @Override
    public void deleteNamespace(Long id) {
        NamespacePo po = namespaceDao.select(id);
        namespaceDao.delete(po);
    }

    @Override
    public boolean checkIdentifyExist(Long clusterId, String namespaceIdentify) {
        return ValidateUtil.isNotEmpty(namespaceDao.getNamespacesByClusterIdAndIdentify(clusterId, namespaceIdentify));
    }

}
