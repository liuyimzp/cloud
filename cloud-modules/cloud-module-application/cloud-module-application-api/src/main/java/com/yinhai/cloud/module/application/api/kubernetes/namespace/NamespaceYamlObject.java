package com.yinhai.cloud.module.application.api.kubernetes.namespace;

import com.yinhai.cloud.module.application.api.kubernetes.base.AppKubernetesYamlObject;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1ObjectMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhaokai
 * @create: 2018-10-09 13:31:49
 */
public class NamespaceYamlObject extends AppKubernetesYamlObject {

    private String namespaceName;

    public NamespaceYamlObject(String namespaceName) {
        this.namespaceName = namespaceName;
        create();
    }

    private void create() {
        V1Namespace namespace = new V1Namespace();
        namespace.setApiVersion("v1");
        namespace.setKind("Namespace");
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.name(namespaceName);
        Map<String, String> labels = new HashMap<>();
        labels.put("name", namespaceName);
        meta.labels(labels);
        namespace.setMetadata(meta);

        yamlObjectList.add(namespace);

    }

    @Override
    protected Integer getDefaultInnerPort() {
        return null;
    }
}
