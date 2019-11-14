package com.yinhai.cloud.module.application.api.kubernetes.base;

import com.yinhai.cloud.module.application.api.bpo.*;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppServiceVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.resource.kubernetes.base.KubernetesYamlObject;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.pv.api.bpo.IPersistentVolumeBpo;
import com.yinhai.core.app.api.util.ServiceLocator;
import io.kubernetes.client.models.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author: zhaokai
 * @create: 2018-09-27 14:31:08
 */
public abstract class AppKubernetesYamlObject extends KubernetesYamlObject {

    protected final INamespaceBpo namespaceBpo = ServiceLocator.getService(INamespaceBpo.class);
    protected final IAppServiceBpo appServiceBpo = ServiceLocator.getService(IAppServiceBpo.class);
    protected final IAppPVBpo appPVBpo = ServiceLocator.getService(IAppPVBpo.class);
    protected final IPersistentVolumeBpo persistentVolumeBpo = ServiceLocator.getService(IPersistentVolumeBpo.class);
    protected final IAppBpo appBpo = ServiceLocator.getService(IAppBpo.class);
    protected final IAppVersionBpo appVersionBpo = ServiceLocator.getService(IAppVersionBpo.class);
    protected final IAppConfigBpo appConfigBpo = ServiceLocator.getService(IAppConfigBpo.class);
    protected final IAppEnvBpo appEnvBpo = ServiceLocator.getService(IAppEnvBpo.class);
    protected final IConfigMapBpo configMapBpo = ServiceLocator.getService(IConfigMapBpo.class);
    protected final INodeBpo nodeBpo = ServiceLocator.getService(INodeBpo.class);


    protected abstract Integer getDefaultInnerPort();

    /**
     * 共有属性
     */
    protected AppVo application;
    protected String applicationName;
    protected String namespaceName;
    protected String configVolumeName;
    protected String dataVolumeName;
    protected List<AppServiceVo> exposeServiceVoList;
    protected Map<String, String> podLabels;
    protected String configMapName;

    /**
     * 通用配置 configMap
     */
    protected void createConfigMap(V1Container container, V1PodSpec podTemplateSpec) {
        /*config env 配置*/
        List<V1EnvVar> envList = configMapBpo.getConfigMapByAppId(application.getId()).stream().filter(n -> "1".equals(n.getIsEnv())).map(n -> {
            V1EnvVar v = new V1EnvVar();
            v.setName(n.getPropertyName());
            V1EnvVarSource envVarSource = new V1EnvVarSource();
            V1ConfigMapKeySelector keySelector = new V1ConfigMapKeySelector();
            keySelector.setName(configMapName);
            keySelector.setKey(n.getPropertyName());
            envVarSource.setConfigMapKeyRef(keySelector);
            v.setValueFrom(envVarSource);
            return v;
        }).collect(Collectors.toList());
        AppConfigVo configVo = appConfigBpo.getAppConfig(application.getId());
        V1EnvVar jvmXmx = new V1EnvVar();
        jvmXmx.name("JAVA_XMX");
        jvmXmx.value("-Xmx" + Double.valueOf(configVo.getMaxMemory() * 1024).intValue() + "m");
        envList.add(jvmXmx);
        container.env(envList);
        /*config volum 挂载*/
        //configMap路径配置
        V1VolumeMount volumeMount = new V1VolumeMount();
        volumeMount.name(configVolumeName).mountPath("/config/" + applicationName + ".properties").subPath(applicationName + ".properties");
        V1VolumeMount volumeMount1 = new V1VolumeMount();
        volumeMount1.name("host-time").mountPath("/etc/localtime");
        container.addVolumeMountsItem(volumeMount);
        container.addVolumeMountsItem(volumeMount1);
        //将宿主机时区挂载到容器下
        //configMap参数配置
        V1Volume volume = new V1Volume();
        volume.name(configVolumeName);
        V1ConfigMapVolumeSource configMapVolumeSource = new V1ConfigMapVolumeSource();
        configMapVolumeSource.name(configMapName);
        V1KeyToPath itemsItem = new V1KeyToPath();
        itemsItem.setKey(applicationName + ".properties");
        itemsItem.setPath(applicationName + ".properties");
        configMapVolumeSource.addItemsItem(itemsItem);
        volume.configMap(configMapVolumeSource);
        podTemplateSpec.addVolumesItem(volume);
        // 将宿主机时区挂载到容器下
        V1Volume volume1 = new V1Volume();
        V1HostPathVolumeSource hostTimePathVolumeSource = new V1HostPathVolumeSource();
        hostTimePathVolumeSource.setPath("/etc/localtime");
        volume1.hostPath(hostTimePathVolumeSource);
        volume1.name("host-time").hostPath(hostTimePathVolumeSource);
        podTemplateSpec.addVolumesItem(volume1);

    }
}
