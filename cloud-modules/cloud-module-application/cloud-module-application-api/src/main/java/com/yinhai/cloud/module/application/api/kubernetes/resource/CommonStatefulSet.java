package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.exception.CreateKubernetesResourceRuntimeException;
import com.yinhai.cloud.module.application.api.kubernetes.base.BaseStatefulSet;
import com.yinhai.cloud.module.application.api.kubernetes.helper.PvcName;
import com.yinhai.cloud.module.application.api.util.DuplicateCodeUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppServiceVo;
import com.yinhai.cloud.module.application.api.vo.AppVersionVo;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: zhaokai
 * @create: 2018-10-08 17:02:05
 */
public class CommonStatefulSet extends BaseStatefulSet {

    private AppVersionVo appVersionVo;
    public CommonStatefulSet(Long applicationId) {
        super(applicationId);
        check();
        create();
    }

    private void create() {
        yamlObjectList.add(createGovernService());
        yamlObjectList.addAll(createExposeService());
        yamlObjectList.add(createStatefulSet());

    }

    private void check() {
        if (exposeServiceVoList.size() == 0) {
            throw new CreateKubernetesResourceRuntimeException(application.getAppName() + " 未配置服务端口");
        }
    }


    private V1beta1StatefulSet createStatefulSet() {
        List<AppVersionVo> versionVos = appVersionBpo.getUseVersion(application.getId());
        AppConfigVo appConfig = appConfigBpo.getAppConfig(application.getId());
        if (versionVos.isEmpty()){
            appVersionVo = new AppVersionVo();
            appVersionVo.setImageId(appConfig.getAppImageId());
            appVersionVo.setAppId(application.getId());
            appVersionVo.setIsAction(IAppConstants.APPVERSION_ACTION);
            appVersionVo.setIsuse(IAppConstants.APPVERSION_IS_USE);
            appVersionVo.setImageName(appConfig.getAppImagePath());
            appVersionBpo.saveVersion(appVersionVo);
        }else {
            appVersionVo =versionVos.get(0);
        }
        //设置当前版本为使用
        appVersionBpo.setVersionIsUse(appVersionVo);
        V1beta1StatefulSet ss = DuplicateCodeUtil.getV1beta1StatefulSet(applicationName + "v" + appVersionVo.getVersionName(),namespaceName);
        // 设置spec
        V1beta1StatefulSetSpec ssSpec = DuplicateCodeUtil.setV1beta1StatefulSetSpec(governServiceName,application.getInstanceNum());

        Map<String, String> podLabelsClone = new HashMap<>();
        String version = appVersionVo.getVersionNameStr();
        podLabelsClone.put("app", application.getAppIdentify());
        podLabelsClone.put("version",version);
        V1PodTemplateSpec podTemplate = DuplicateCodeUtil.setV1PodTemplateSpec(podLabelsClone);

        V1PodSpec podTemplateSpec = new V1PodSpec();
        // add by tianhy 控制同一个应用每个节点最多部署一个pod
        /*V1Affinity affinity = new V1Affinity();
        V1PodAntiAffinity podAntiAffinity = new V1PodAntiAffinity();
        V1PodAffinityTerm podAffinityTerm = new V1PodAffinityTerm();
        V1LabelSelector labelSelector = new V1LabelSelector();
        V1LabelSelectorRequirement labelSelectorRequirement = new V1LabelSelectorRequirement();
        labelSelectorRequirement.setKey("app");
        labelSelectorRequirement.setOperator("In");
        labelSelectorRequirement.addValuesItem(applicationName);
        labelSelector.addMatchExpressionsItem(labelSelectorRequirement);
        podAffinityTerm.setLabelSelector(labelSelector);
        podAffinityTerm.setTopologyKey("kubernetes.io/hostname");
        podAntiAffinity.addRequiredDuringSchedulingIgnoredDuringExecutionItem(podAffinityTerm);
        affinity.setPodAntiAffinity(podAntiAffinity);
        podTemplateSpec.setAffinity(affinity);*/
        // add end

        podTemplateSpec.terminationGracePeriodSeconds(10L);
        List<V1Container> containers = new ArrayList<>();

        String imagePath = appConfig.getAppImagePath();
        V1Container container = DuplicateCodeUtil.setV1Container(imagePath,"IfNotPresent",applicationName + "-container");
        // 环境变量
        List<V1EnvVar> envList = appEnvBpo.getAppEnvs(application.getId()).stream().map(n -> {
            V1EnvVar v = new V1EnvVar();
            v.setName(n.getEnvKey());
            v.setValue(n.getEnvValue());
            return v;
        }).collect(Collectors.toList());
        container.env(envList);

        // 资源设置
        V1ResourceRequirements redisResourceReq = DuplicateCodeUtil.setV1ResourceRequirements(appConfig.getMinMemory() + "Gi",appConfig.getMinCPU().toString(),appConfig.getMaxMemory() + "Gi",appConfig.getMaxCPU().toString());

        container.resources(redisResourceReq);
        // 开放端口
        List<V1ContainerPort> containerPorts = appServiceBpo.getAppServicesByAppId(application.getId()).stream().map(n -> {
            V1ContainerPort cPort = new V1ContainerPort();
            cPort.name("p-" + n.getInnerPort());
            cPort.containerPort(n.getInnerPort());
            return cPort;
        }).collect(Collectors.toList());
        container.ports(containerPorts);

        // 目录挂载
        List<V1VolumeMount> volumeMounts = appPvList.stream().map(n -> {
            V1VolumeMount mount = new V1VolumeMount();
            mount.name("mount" + n.getPath().replace("/", ""));//volumn 的名称
            mount.mountPath(n.getPath());
            return mount;
        }).collect(Collectors.toList());
        container.volumeMounts(volumeMounts);
        containers.add(container);

        //configMap
        createConfigMap(container,podTemplateSpec);

        podTemplateSpec.containers(containers);
        podTemplate.spec(podTemplateSpec);
        ssSpec.template(podTemplate);
        // pvc template
        List<V1PersistentVolumeClaim> vctList = appPvList.stream().map(n -> {
            V1PersistentVolumeClaim pvc = DuplicateCodeUtil.setV1PersistentVolumeClaim("mount" + n.getPath().replace("/", ""),n.getVolumeUuid(),"storage",n.getSpace() + "Gi","ReadWriteOnce");
            pvcList.clear();
            pvcList.add(new PvcName("mount" + n.getPath().replace("/", ""), applicationName, application.getInstanceNum()));

            return pvc;

        }).collect(Collectors.toList());
        ssSpec.volumeClaimTemplates(vctList);

        ss.spec(ssSpec);

        return ss;
    }

    @Override
    protected V1Service createGovernService() {
        V1Service service = DuplicateCodeUtil.setV1Service("v1","Service",governServiceName,namespaceName,"svc",applicationName + "-govern-svc");
        List<V1ServicePort> spList = DuplicateCodeUtil.setSpList(applicationName,getDefaultInnerPort());

        List<AppServiceVo> appServiceList = appServiceBpo.getAppServicesByAppId(application.getId());
        appServiceList.stream().filter(n -> IAppConstants.SERVICE_TYPE_INNER.equals(n.getServiceType())).forEach(n -> {
            if (!n.getInnerPort().equals(getDefaultInnerPort())) {
                V1ServicePort vp = new V1ServicePort();
                vp.port(getDefaultInnerPort());
                vp.name(applicationName + "port" + n.getInnerPort());
                vp.targetPort(new IntOrString(getDefaultInnerPort()));
                spList.add(vp);

            }
        });
        service.spec(DuplicateCodeUtil.setV1ServiceSpec(spList,podLabels));
        return service;
    }

    @Override
    protected Integer getDefaultInnerPort() {
        return 8080;
    }
}
