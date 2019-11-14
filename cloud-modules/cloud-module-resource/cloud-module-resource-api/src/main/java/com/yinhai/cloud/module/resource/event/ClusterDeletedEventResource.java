package com.yinhai.cloud.module.resource.event;

import com.yinhai.core.common.api.event.IEventResource;

/**
 * @author: zhaokai
 * @create: 2018-09-26 17:39:02
 */
public class ClusterDeletedEventResource implements IEventResource {
    private String target;
    private Object resource;

    public ClusterDeletedEventResource(String target, Object resource) {
        this.target = target;
        this.resource = resource;
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public Object getResource() {
        return resource;
    }
}
