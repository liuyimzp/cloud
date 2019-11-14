package com.yinhai.cloud.module.resource.kubernetes.pv;

import com.yinhai.cloud.module.resource.kubernetes.base.KubernetesYamlObject;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1ServiceAccount;

/**
 * Created by tianhy on 2018/11/27.
 */
public class ServiceAccountObject extends KubernetesYamlObject {

    private String name;

    private String namespace;

    public ServiceAccountObject(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
        create();
    }

    private void create() {
        V1ServiceAccount serviceAccount = new V1ServiceAccount();
        serviceAccount.setApiVersion("v1");
        serviceAccount.setKind("ServiceAccount");
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.name(name);
        meta.setNamespace(namespace);
        serviceAccount.setMetadata(meta);

        yamlObjectList.add(serviceAccount);
    }

}
