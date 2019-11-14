package com.yinhai.cloud.module.resource.event.listener;

import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.INamespaceBpo;
import com.yinhai.cloud.module.resource.pv.impl.dao.IPersistentVolumeDao;
import com.yinhai.core.common.api.event.IEvent;
import com.yinhai.core.common.ta3.event.TaEventListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: zhaokai
 * @create: 2018-09-26 17:43:36
 */
public class ClusterDeletedListener extends TaEventListener {
    @Autowired
    private IPersistentVolumeDao pvDao;
    @Autowired
    private IAppBpo appBpo;
    @Autowired
    private INamespaceBpo namespaceBpo;

    @Override
    public void handleEvent(IEvent event) {
        Long clusterId = (Long) event.getResource().getResource();
        pvDao.deletePvByClusterId(clusterId);
        appBpo.deleteAppByClusterId(clusterId);
        namespaceBpo.deleteByClusterId(clusterId);

    }
}
