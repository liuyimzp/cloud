package com.yinhai.cloud.module.application.api.kubernetes.namespace;

import com.yinhai.cloud.module.application.api.kubernetes.base.AppKubernetesYamlObject;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1ResourceQuota;
import io.kubernetes.client.models.V1ResourceQuotaSpec;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tianhy on 2018/11/19.
 */
public class NamespaceResourceQuota extends AppKubernetesYamlObject {

    private String namespaceName;

    private Double maxCpuLimit;

    private Double maxMemoryLimit;

    public NamespaceResourceQuota(String namespaceName, Double maxCpuLimit, Double maxMemoryLimit) {
        this.namespaceName = namespaceName;
        this.maxCpuLimit = maxCpuLimit;
        this.maxMemoryLimit = maxMemoryLimit;
        create();
    }

    private void create() {
        V1ResourceQuota resourceQuota = new V1ResourceQuota();
        resourceQuota.setApiVersion("v1");
        resourceQuota.setKind("ResourceQuota");
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.name(namespaceName + "-resource-quota");
        resourceQuota.setMetadata(meta);
        V1ResourceQuotaSpec spec = new V1ResourceQuotaSpec();
        Map<String, Quantity> hard = new HashMap<>();
        hard.put("limits.cpu", new Quantity(maxCpuLimit.toString()));
        hard.put("limits.memory", new Quantity(maxMemoryLimit.toString() + "Gi"));
        spec.setHard(hard);
        resourceQuota.setSpec(spec);
        yamlObjectList.add(resourceQuota);

    }

    @Override
    protected Integer getDefaultInnerPort() {
        return null;
    }
}
