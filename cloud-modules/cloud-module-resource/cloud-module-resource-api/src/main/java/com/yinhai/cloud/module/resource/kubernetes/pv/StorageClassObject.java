package com.yinhai.cloud.module.resource.kubernetes.pv;

import com.yinhai.cloud.module.resource.kubernetes.base.KubernetesYamlObject;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1StorageClass;

/**
 * Created by tianhy on 2018/11/27.
 */
public class StorageClassObject extends KubernetesYamlObject {

    private String name;

    public StorageClassObject(String name) {
        this.name = name;
        create();
    }

    private void create() {
        V1StorageClass storageClass = new V1StorageClass();
        storageClass.setKind("StorageClass");
        storageClass.setApiVersion("storage.k8s.io/v1beta1");
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.name(name);
        storageClass.setMetadata(meta);
        storageClass.setProvisioner("fuseim.io/ifs");

        yamlObjectList.add(storageClass);
    }

}
