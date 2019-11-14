package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.module.application.api.kubernetes.base.BaseDeployment;
import com.yinhai.cloud.module.application.api.util.DuplicateCodeUtil;
import com.yinhai.cloud.module.application.api.util.MiddlewareConfigUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.core.common.api.exception.AppException;
import com.yinhai.core.common.api.util.ValidateUtil;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zhaokai
 * @create: 2018-09-27 14:29:28
 */
public class NginxDeployment extends BaseDeployment {
    private static final String conf = "nginx.conf";

    private static final String bind = "server {\n" +
            "      listen       80;\n" +
            "      server_name  localhost;\n" +
            "      location / {\n" +
            "          root   html;\n" +
            "          index  index.html index.htm;\n" +
            "        }\n" +
            "       }";
    public NginxDeployment(Long appId) {
        super(appId);
        create();
    }

    private void create() {
        yamlObjectList.add(createConfigMap());
        yamlObjectList.addAll(createExposeService());
        yamlObjectList.add(createDeployment());

    }

    private V1ConfigMap createConfigMap() {
        return DuplicateCodeUtil.createConfigMap(configMapName,namespaceName,conf,bind);
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

        V1PodSpec podSpec = new V1PodSpec();
        List<V1Container> containerList = new ArrayList<>();
        AppConfigVo appConfigVo = appConfigBpo.getAppConfig(application.getId());
        V1Container container = DuplicateCodeUtil.setV1Container(appConfigVo.getAppImagePath(),"IfNotPresent",applicationName + "-container");

        // 开放端口
        List<V1ContainerPort> cPortList = DuplicateCodeUtil.setV1ContainerPortList(exposeServiceVoList,getDefaultInnerPort());
        container.ports(cPortList);
        //uodate by liuyi02 : 中间件配置文件动态挂载
        String containerConfigFilePath = SystemConfigUtil.getSystemConfigValue("config.container.nginx");//容器中挂载配置文件的位置
        List<V1VolumeMount> volMountList = DuplicateCodeUtil.setV1VolumeMountList(configVolumeName,containerConfigFilePath);
//        List<V1VolumeMount> volMountList = DuplicateCodeUtil.setV1VolumeMountList(configVolumeName,"/etc/nginx/conf.d");
        container.volumeMounts(volMountList);

        AppConfigVo appConfig = appConfigBpo.getAppConfig(application.getId());
        V1ResourceRequirements resReq = new V1ResourceRequirements();
        Map<String, Quantity> limitQuantity = DuplicateCodeUtil.setQuantity(appConfig.getMaxMemory() + "Gi",appConfig.getMaxCPU() + "");
        resReq.setLimits(limitQuantity);
        container.resources(resReq);

        containerList.add(container);

        podSpec.containers(containerList);
        List<V1Volume> volList = new ArrayList<>();
//        List<V1Volume> volList = DuplicateCodeUtil.setV1VolumeList(configVolumeName,configMapName,"nginx.conf","server.conf");
        V1Volume volume = new V1Volume();
        volume.name(configVolumeName);
        //将数据库中的配置信息写入对应文件
        List<NodeBasicInfoVo> masterNodes = nodeBpo.queryRunningMasterNodesByClusterId(application.getClusterId());
        if(ValidateUtil.isEmpty(masterNodes)){
            throw new AppException("该集群下无正在运行中的master节点!");
        }
        NodeBasicInfoVo masterNode = masterNodes.get(0);
        String configStr = SystemConfigUtil.getSystemConfigValue("config.nginx");
        try {
            if (ValidateUtil.isNotEmpty(application.getMiddleware_configfile())){
                MiddlewareConfigUtil.setConfigMiddleware(application,masterNode,configStr);
//                Map<String,String> nodeSelecter = new HashMap<>();
//                nodeSelecter.put(masterNode.getNodeName(),masterNode.getNodeName());
                podSpec.setNodeName(masterNode.getNodeName());
                application.setNodeId(masterNode.getNodeId());
                appBpo.editApp(application);
            }
        } catch (Exception e) {
            throw new AppException("创建配置文件失败");
        }
        V1HostPathVolumeSource hostPath = new V1HostPathVolumeSource();
        hostPath.setPath(configStr + "/" + application.getAppIdentify() + ".conf");
        volume.setHostPath(hostPath);
        volList.add(volume);
        podSpec.volumes(volList);

        podTemplate.spec(podSpec);

        spec.template(podTemplate);


        dep.spec(spec);
        return dep;
    }


    @Override
    protected Integer getDefaultInnerPort() {
        return 80;
    }
}
