package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.module.application.api.kubernetes.base.BaseDeployment;
import com.yinhai.cloud.module.application.api.util.DuplicateCodeUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zhaokai
 * @create: 2018-09-27 14:29:28
 */
public class ActiveMQDeployment extends BaseDeployment {

    public ActiveMQDeployment(Long appId) {
        super(appId);
        create();
    }

    private void create() {
        yamlObjectList.addAll(createExposeService());
        yamlObjectList.add(createDeployment());

    }

    private AppsV1beta1Deployment createDeployment() {
        AppsV1beta1Deployment dep = DuplicateCodeUtil.setAppsV1beta1Deployment("apps/v1beta1","Deployment",applicationName,namespaceName);

        // 设置spec
        AppsV1beta1DeploymentSpec spec = new AppsV1beta1DeploymentSpec();
        spec.replicas(application.getInstanceNum());
        V1PodTemplateSpec podTemplate = new V1PodTemplateSpec();
        V1ObjectMeta podMeta = new V1ObjectMeta();
        podMeta.setLabels(podLabels);
        podTemplate.metadata(podMeta);
        V1LabelSelector labelSel = new V1LabelSelector();
        labelSel.matchLabels(podLabels);
        spec.selector(labelSel);
        V1PodSpec podSpec = new V1PodSpec();
        List<V1Container> containerList = new ArrayList<>();
        AppConfigVo appConfigVo = appConfigBpo.getAppConfig(application.getId());//配置详情
        V1Container container = DuplicateCodeUtil.setV1Container(appConfigVo.getAppImagePath(),"IfNotPresent",applicationName + "-container");
        // 开放端口
        List<V1ContainerPort> cPortList = DuplicateCodeUtil.setV1ContainerPortList(exposeServiceVoList,getDefaultInnerPort());
        container.ports(cPortList);

        V1ResourceRequirements resReq = new V1ResourceRequirements();
        Map<String, Quantity> limitQuantity = DuplicateCodeUtil.setQuantity(appConfigVo.getMaxMemory() + "Gi",appConfigVo.getMaxCPU() + "");
        resReq.setLimits(limitQuantity);
        container.resources(resReq);

        containerList.add(container);

        podSpec.containers(containerList);

        podTemplate.spec(podSpec);

        spec.template(podTemplate);


        dep.spec(spec);
        return dep;
    }


    @Override
    protected Integer getDefaultInnerPort() {
        return 61616;
    }
}
