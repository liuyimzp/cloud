package com.yinhai.cloud.module.resource.kubernetes.pv;

import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.module.resource.kubernetes.base.KubernetesYamlObject;
import io.kubernetes.client.models.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tianhy on 2018/11/27.
 */
public class NfsClientProvDeployment extends KubernetesYamlObject {

    private String name;

    private String namespace;

    private String nfs_server_ip;

    private String nfs_server_path;

    public NfsClientProvDeployment(String name, String namespace, String nfs_server_ip, String nfs_server_path) {
        this.name = name;
        this.namespace = namespace;
        this.nfs_server_ip = nfs_server_ip;
        this.nfs_server_path = nfs_server_path;
        create();
    }

    private void create() {
        ExtensionsV1beta1Deployment deployment = new ExtensionsV1beta1Deployment();
        deployment.setApiVersion("extensions/v1beta1");
        deployment.setKind("Deployment");

        V1ObjectMeta ssMeta = new V1ObjectMeta();
        Map<String, String> labels = new HashMap<>();
        labels.put("app", name);
        ssMeta.setLabels(labels);
        ssMeta.setName(name);
        ssMeta.setNamespace(namespace);
        deployment.setMetadata(ssMeta);

        ExtensionsV1beta1DeploymentSpec spec = new ExtensionsV1beta1DeploymentSpec();
        spec.setReplicas(1);
        V1LabelSelector selector = new V1LabelSelector();
        selector.setMatchLabels(labels);
        spec.setSelector(selector);
        ExtensionsV1beta1DeploymentStrategy strategy = new ExtensionsV1beta1DeploymentStrategy();
        strategy.setType("Recreate");
        spec.setStrategy(strategy);
        V1PodTemplateSpec template = new V1PodTemplateSpec();
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setLabels(labels);
        template.setMetadata(meta);
        V1PodSpec templateSpec = new V1PodSpec();
        V1Container container = new V1Container();
        V1EnvVar envVar1 = new V1EnvVar();
        envVar1.setName("PROVISIONER_NAME");
        envVar1.setValue("fuseim.io/ifs");
        container.addEnvItem(envVar1);
        V1EnvVar envVar2 = new V1EnvVar();
        envVar2.setName("NFS_SERVER");
        envVar2.setValue(nfs_server_ip);
        container.addEnvItem(envVar2);
        V1EnvVar envVar3 = new V1EnvVar();
        envVar3.setName("NFS_PATH");
        envVar3.setValue(nfs_server_path);
        container.addEnvItem(envVar3);
        container.setImage(imageBasePath + "/quay.io/external_storage/nfs-client-provisioner:latest");
        container.setName(name);
        V1VolumeMount volumeMount = new V1VolumeMount();
        volumeMount.setMountPath("/persistentvolumes");
        volumeMount.setName("nfs-client-root");
        container.addVolumeMountsItem(volumeMount);
        templateSpec.addContainersItem(container);
        templateSpec.setServiceAccountName(name);
        templateSpec.setServiceAccount(name);
        V1Volume volume = new V1Volume();
        volume.setName("nfs-client-root");
        V1NFSVolumeSource nfsVolumeSource = new V1NFSVolumeSource();
        nfsVolumeSource.setPath(nfs_server_path);
        nfsVolumeSource.setServer(nfs_server_ip);
        volume.setNfs(nfsVolumeSource);
        templateSpec.addVolumesItem(volume);
        template.setSpec(templateSpec);
        spec.setTemplate(template);
        deployment.setSpec(spec);

        yamlObjectList.add(deployment);
    }

}
