package com.yinhai.cloud.module.resource.kubernetes.pv;

import com.yinhai.cloud.module.resource.kubernetes.base.KubernetesYamlObject;
import io.kubernetes.client.models.V1ClusterRoleBinding;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1RoleRef;
import io.kubernetes.client.models.V1Subject;

/**
 * Created by tianhy on 2018/11/27.
 *
 */
public class ClusterRoleBindObject extends KubernetesYamlObject {

    private String name;

    private String namespace;

    public ClusterRoleBindObject(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
        create();
    }

    private void create() {
        V1ClusterRoleBinding clusterRoleBinding = new V1ClusterRoleBinding();
        clusterRoleBinding.setKind("ClusterRoleBinding");
        clusterRoleBinding.setApiVersion("rbac.authorization.k8s.io/v1");
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.name(name);
        clusterRoleBinding.setMetadata(meta);
        V1Subject subject = new V1Subject();
        subject.setKind("ServiceAccount");
        subject.setName(name);
        subject.setNamespace(namespace);
        clusterRoleBinding.addSubjectsItem(subject);
        V1RoleRef roleRef = new V1RoleRef();
        roleRef.setKind("ClusterRole");
        roleRef.setName("nfs-client-provisioner-runner");
        roleRef.setApiGroup("rbac.authorization.k8s.io");
        clusterRoleBinding.setRoleRef(roleRef);

        yamlObjectList.add(clusterRoleBinding);
    }

}
