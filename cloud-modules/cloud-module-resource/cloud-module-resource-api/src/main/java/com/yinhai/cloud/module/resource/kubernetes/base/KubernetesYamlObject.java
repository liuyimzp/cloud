package com.yinhai.cloud.module.resource.kubernetes.base;

import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.module.resource.kubernetes.helper.KubeYaml;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianhy on 2018/11/27.
 */
public abstract class KubernetesYamlObject {

    protected final List<Object> yamlObjectList = new ArrayList<>();

    protected static final String imageBasePath = SystemConfigUtil.getSystemConfigValue("docker.private.repo.ip") + ":" + SystemConfigUtil.getSystemConfigValue("docker.private.repo.port");

    public String buildYaml() {
        String content = KubeYaml.getSnakeYaml().dumpAll(yamlObjectList.iterator());
        return content;
    }
}
