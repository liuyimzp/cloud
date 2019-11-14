package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.kubernetes.base.BaseStatefulSet;
import com.yinhai.cloud.module.application.api.util.DuplicateCodeUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppPVVo;
import io.kubernetes.client.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-09-27 14:29:28
 */
public class ElasticSearchStatefulSet extends BaseStatefulSet {

    public ElasticSearchStatefulSet(Long appId) {
        super(appId);
        create();
    }
    @Override
    protected Integer getDefaultInnerPort() {
        return 9200;
    }

    private void create() {
        yamlObjectList.add(createGovernService());
        yamlObjectList.addAll(createExposeService());
        yamlObjectList.add(createStatefulSet());
    }

    private V1beta1StatefulSet createStatefulSet() {
        V1beta1StatefulSet ss = DuplicateCodeUtil.getV1beta1StatefulSet(applicationName,namespaceName);

        // 设置spec
        V1beta1StatefulSetSpec ssSpec = DuplicateCodeUtil.setV1beta1StatefulSetSpec(governServiceName,application.getInstanceNum());

        V1PodTemplateSpec podTemplate = DuplicateCodeUtil.setV1PodTemplateSpec(podLabels);
        V1PodSpec podTemplateSpec = new V1PodSpec();
        podTemplateSpec.terminationGracePeriodSeconds(10L);
        List<V1Container> containers = new ArrayList<>();
        AppConfigVo appConfigVo = appConfigBpo.getAppConfig(application.getId());//配置详情
        V1Container redisContainer = DuplicateCodeUtil.setV1Container(appConfigVo.getAppImagePath(),"IfNotPresent",applicationName + "-container");

        // 资源配置
        V1ResourceRequirements redisResourceReq = DuplicateCodeUtil.setV1ResourceRequirements(appConfigVo.getMinMemory() + "Gi",appConfigVo.getMinCPU() + "",appConfigVo.getMaxMemory() + "Gi",appConfigVo.getMaxCPU() + "");
        redisContainer.resources(redisResourceReq);
        // 开放端口
        List<V1ContainerPort> containerPorts = DuplicateCodeUtil.setViContainerPortList("server-port",getDefaultInnerPort());

        redisContainer.ports(containerPorts);
        // 目录挂载

        List<V1VolumeMount> volumeMounts = new ArrayList<>();
        // 数据目录
        V1VolumeMount dataMount = new V1VolumeMount();
        AppPVVo appPv = appPvList.get(0);
        dataMount.name(dataVolumeName);//volumn 的名称
        dataMount.mountPath("/opt/elasticsearch/data");
        volumeMounts.add(dataMount);
        redisContainer.volumeMounts(volumeMounts);

        podTemplateSpec.restartPolicy("Always");

        containers.add(redisContainer);
        podTemplateSpec.containers(containers);
        podTemplate.spec(podTemplateSpec);
        ssSpec.template(podTemplate);
        // pvc template
        List<V1PersistentVolumeClaim> vctList = DuplicateCodeUtil.setV1PersistentVolumeClaimList(dataVolumeName,appPv.getVolumeUuid(),"storage",appPv.getSpace() + "Gi","ReadWriteOnce");
        ssSpec.volumeClaimTemplates(vctList);

        ss.spec(ssSpec);

        return ss;
    }


}
