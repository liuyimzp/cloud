package com.yinhai.cloud.module.application.api.kubernetes.configmap;


import com.yinhai.cloud.module.application.api.util.sm4.SM4Utils;
import com.yinhai.cloud.module.application.api.vo.ConfigMapVo;
import com.yinhai.cloud.module.resource.kubernetes.base.KubernetesYamlObject;
import com.yinhai.core.common.api.util.ValidateUtil;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.models.V1ObjectMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yanglg on 2019/3/4.
 */
public class ConfigMapYamlObject extends KubernetesYamlObject {

    private String namespaceName;
    private String appName;
    private Long imageId;
    private List<ConfigMapVo> configMapList;

    public ConfigMapYamlObject(String namespaceName,String appName,List<ConfigMapVo> configMapList) {
        this.namespaceName = namespaceName;
        this.appName = appName;
        if (configMapList != null){
            this.configMapList = new ArrayList<>(configMapList);
        }
        create();
    }

    private void create() {
        V1ConfigMap configMap = new V1ConfigMap();
        configMap.setApiVersion("v1");
        configMap.setKind("ConfigMap");
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.name(appName+"-config-map");
        meta.namespace(namespaceName);
//        Map<String, String> labels = new HashMap<>();
//        labels.put("name", namespaceName);
//        meta.labels(labels);
        configMap.setMetadata(meta);
//        configMap.setData(configMapList.stream().collect(Collectors.toMap(ConfigMapVo::getPropertyName, ConfigMapVo::getPropertyValue)));
//        configMap.putDataItem(appName+"-properties-file-name",appName+".properties");
        if(!configMapList.isEmpty()){
            StringBuilder values = new StringBuilder();
            configMapList.stream().forEach(vo->{
                if("1".equals(vo.getIsEnv())){
                    configMap.putDataItem(vo.getPropertyName(),"0".equals(vo.getEncrypt())?vo.getPropertyValue(): SM4Utils.decrypt(vo.getPropertyValue()));
                }else{
                    values.append(vo.getPropertyName()).append("=").append("0".equals(vo.getEncrypt())?vo.getPropertyValue(): SM4Utils.decrypt(vo.getPropertyValue())).append("\n");
                }
            });
            configMap.putDataItem(appName+".properties",values.toString());
        }else{
            configMap.putDataItem(appName+".properties","");
        }
        yamlObjectList.add(configMap);
    }

}
