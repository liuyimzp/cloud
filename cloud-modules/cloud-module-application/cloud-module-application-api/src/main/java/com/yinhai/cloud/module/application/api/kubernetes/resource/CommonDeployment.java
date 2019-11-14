package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.kubernetes.base.BaseDeployment;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppVersionVo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.core.common.api.util.ValidateUtil;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.models.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yanglq on 2018/11/28 0028.
 */
public class CommonDeployment extends BaseDeployment{

    @Resource
    private IAppImageBpo appImageBpo;

    private AppVersionVo appVersionVo;

    public CommonDeployment(Long applicationId) {
        super(applicationId);
        create();
    }

    private void create() {
        yamlObjectList.addAll(createExposeService());
        yamlObjectList.add(createDeployment());
    }

    private V1HorizontalPodAutoscaler createHorizontalPodAutoscaler(){
        AppConfigVo appConfig = appConfigBpo.getAppConfig(application.getId());
        V1HorizontalPodAutoscaler hpa = new V1HorizontalPodAutoscaler();
        hpa.apiVersion("autoscaling/v2beta1");
        hpa.kind("HorizontalPodAutoscaler");
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.name("hpa-" + applicationName + "v" + appVersionVo.getVersionName());
        meta.namespace(namespaceName);

        V1HorizontalPodAutoscalerSpec spec = new V1HorizontalPodAutoscalerSpec();
        V1CrossVersionObjectReference specstr = new V1CrossVersionObjectReference();
        specstr.apiVersion("apps/v1beta2");
        specstr.kind("Deployment");
        specstr.name(applicationName + "v" + appVersionVo.getVersionName());
        spec.scaleTargetRef(specstr);
        spec.setMinReplicas(1);
        spec.setMaxReplicas(10);
        Double avgCpu = (appConfig.getMaxCPU() * 10);
        spec.setTargetCPUUtilizationPercentage(avgCpu.intValue());
        hpa.spec(spec);
        return hpa;
    }

    private AppsV1beta1Deployment createDeployment(){
        AppsV1beta1Deployment de = new AppsV1beta1Deployment();
        // 设置spec
        AppsV1beta1DeploymentSpec deSpec = new AppsV1beta1DeploymentSpec();
        de.apiVersion("apps/v1beta2");
        de.kind("Deployment");
        AppConfigVo appConfig = appConfigBpo.getAppConfig(application.getId());
        List<AppVersionVo> versionVos = appVersionBpo.getUseVersion(application.getId());
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

        // 设置metadata
        V1ObjectMeta deMetadata = new V1ObjectMeta();
        deMetadata.name(applicationName + "v" + appVersionVo.getVersionName());

        deMetadata.namespace(namespaceName);
        de.metadata(deMetadata);
        String imagePath = appVersionVo.getImageName();
        String version = appVersionVo.getVersionNameStr();
        Map<String, String> podLabelsClone = new HashMap<>();
        podLabelsClone.put("app", application.getAppIdentify());
        podLabelsClone.put("version",version);

        deSpec.replicas(application.getInstanceNum());
        V1LabelSelector labelSel = new V1LabelSelector();
        labelSel.matchLabels(podLabelsClone);
        deSpec.selector(labelSel);
        V1PodTemplateSpec podTemplate = new V1PodTemplateSpec();
        V1ObjectMeta podMetadata = new V1ObjectMeta();
        podMetadata.labels(podLabelsClone);
        podTemplate.metadata(podMetadata);

        V1PodSpec podTemplateSpec = new V1PodSpec();
        podTemplateSpec.terminationGracePeriodSeconds(10L);
        List<V1Container> containers = new ArrayList<>();

        V1Container container = new V1Container();
        container.image(imagePath);
        container.imagePullPolicy("IfNotPresent");
        container.name(applicationName + "-container");
 /*       // 环境变量  废弃 由 configMap 替代
        List<V1EnvVar> envList = appEnvBpo.getAppEnvs(application.getId()).stream().map(n -> {
            V1EnvVar v = new V1EnvVar();
            v.setName(n.getEnvKey());
            v.setValue(n.getEnvValue());
            return v;
        }).collect(Collectors.toList());
        container.env(envList);*/

        // 资源设置
        V1ResourceRequirements redisResourceReq = new V1ResourceRequirements();
        Map<String, Quantity> requestQuantity = new HashMap<>();

        requestQuantity.put("memory", new Quantity(appConfig.getMinMemory() + "Gi"));
        requestQuantity.put("cpu", new Quantity(appConfig.getMinCPU().toString()));
        Map<String, Quantity> limitQuantity = new HashMap<>();
        limitQuantity.put("memory", new Quantity(appConfig.getMaxMemory() + "Gi"));
        limitQuantity.put("cpu", new Quantity(appConfig.getMaxCPU().toString()));
        redisResourceReq.requests(requestQuantity);
        redisResourceReq.limits(limitQuantity);
        container.resources(redisResourceReq);
        // 开放端口
        List<V1ContainerPort> containerPorts = appServiceBpo.getAppServicesByAppId(application.getId()).stream().map(n -> {
            V1ContainerPort cPort = new V1ContainerPort();
            cPort.name("p-" + n.getInnerPort());
            cPort.containerPort(n.getInnerPort());
            return cPort;
        }).collect(Collectors.toList());
        container.ports(containerPorts);
        //configMap
        createConfigMap(container,podTemplateSpec);

        containers.add(container);
        podTemplateSpec.containers(containers);
        podTemplate.spec(podTemplateSpec);
        deSpec.template(podTemplate);
        de.spec(deSpec);
        return de;
    }

    @Override
    protected Integer getDefaultInnerPort() {
        return 8080;
    }
}
