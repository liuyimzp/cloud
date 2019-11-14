package com.yinhai.cloud.module.resource.kubernetes.pv;

import com.yinhai.cloud.module.resource.cluster.api.vo.CephClusterVo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephPoolVo;
import com.yinhai.cloud.module.resource.kubernetes.base.KubernetesYamlObject;
import com.yinhai.cloud.module.resource.nodes.api.vo.CephNodeInfoVo;
import com.yinhai.core.common.api.util.ValidateUtil;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1StorageClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyi02 on 2019/7/15.
 */
public class StorageClassCephObject extends KubernetesYamlObject {

    private String name;

    private List<CephNodeInfoVo> monNodes;

    private CephPoolVo pool;

    private CephClusterVo clusterVo;

    public StorageClassCephObject(String name,List<CephNodeInfoVo> monNodes,CephPoolVo pool,CephClusterVo clusterVo) {
        this.name = name;
        if (monNodes != null){
            this.monNodes = new ArrayList<>(monNodes);
        }
        this.pool = pool;
        this.clusterVo = clusterVo;
        create();
    }

    private void create() {
        V1StorageClass storageClass = new V1StorageClass();
        storageClass.setKind("StorageClass");
        storageClass.setApiVersion("storage.k8s.io/v1beta1");
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.name(name);
        storageClass.setMetadata(meta);
        storageClass.setProvisioner("ceph.com/rbd");
        Map<String, String> parameters = new HashMap<>();
        StringBuilder monNodeStr = new StringBuilder();
        for (int i = 0; i < monNodes.size(); i++){
            CephNodeInfoVo monNode = monNodes.get(i);
            monNodeStr.append(monNode.getNodeIP() + ":6789");
            if (i < (monNodes.size() - 1)){
                monNodeStr.append(",");
            }
        }
        parameters.put("monitors",monNodeStr.toString());
        parameters.put("pool",pool.getPoolName());
        parameters.put("adminId","admin");
        parameters.put("adminSecretNamespace","default");
        parameters.put("adminSecretName","ceph-admin-secret-" + clusterVo.getIdentify());
        parameters.put("userId",pool.getPoolName());
        parameters.put("userSecretNamespace","default");
        parameters.put("userSecretName","ceph-secret-" + clusterVo.getIdentify() + "-" + pool.getPoolName());
        parameters.put("imageFormat","2");
        parameters.put("imageFeatures","layering");
        storageClass.setParameters(parameters);
        yamlObjectList.add(storageClass);
    }

}
