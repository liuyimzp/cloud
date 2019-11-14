package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.kubernetes.base.BaseStatefulSet;
import com.yinhai.cloud.module.application.api.util.DuplicateCodeUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppPVVo;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-09-27 14:29:28
 */
public class ZookeeperStatefulSet extends BaseStatefulSet {
    private static final Integer CLIENT_PORT = 2181;
    private static final Integer SERVER_PORT = 2888;
    private static final Integer LEADER_ELECTION_PORT = 3888;


    public ZookeeperStatefulSet(Long appId) {
        super(appId);
        create();
    }

    private void create() {


        yamlObjectList.add(createGovernService());
        yamlObjectList.addAll(createExposeService());
        yamlObjectList.add(createPodDisruptionBudget());
        yamlObjectList.add(createStatefulSet());
    }

    @Override
    protected V1Service createGovernService() {
        V1Service service = DuplicateCodeUtil.setV1Service("v1","Service",governServiceName,namespaceName,"svc",applicationName + "-govern-svc");
        V1ServiceSpec spec = new V1ServiceSpec();

        spec.selector(podLabels);

        List<V1ServicePort> spList = new ArrayList<>();
        V1ServicePort spServer = new V1ServicePort();
        spServer.port(SERVER_PORT);
        spServer.name("server");
        spList.add(spServer);

        V1ServicePort spElection = new V1ServicePort();
        spElection.port(LEADER_ELECTION_PORT);
        spElection.name("leader-election");
        spList.add(spElection);

        spec.ports(spList);
        spec.clusterIP("None");
        service.spec(spec);
        return service;

    }

    private V1beta1PodDisruptionBudget createPodDisruptionBudget() {
        V1beta1PodDisruptionBudget pdb = DuplicateCodeUtil.setV1beta1PodDisruptionBudget(applicationName,namespaceName,podLabels,new IntOrString(1));
        return pdb;
    }

    private V1beta1StatefulSet createStatefulSet() {
        V1beta1StatefulSet ss = DuplicateCodeUtil.getV1beta1StatefulSet(applicationName,namespaceName);

        // 设置spec
        V1beta1StatefulSetSpec ssSpec = DuplicateCodeUtil.setV1beta1StatefulSetSpec(governServiceName,application.getInstanceNum());

        V1LabelSelector podSel = new V1LabelSelector();

        podSel.matchLabels(podLabels);

        ssSpec.selector(podSel);

        V1beta1StatefulSetUpdateStrategy us = new V1beta1StatefulSetUpdateStrategy();
        us.type("RollingUpdate");
        ssSpec.updateStrategy(us);

        ssSpec.podManagementPolicy("Parallel");

        V1PodTemplateSpec podTemplate = DuplicateCodeUtil.setV1PodTemplateSpec(podLabels);

        V1PodSpec podTemplateSpec = DuplicateCodeUtil.setV1PodSpec(applicationName);

        // 资源设置
        AppConfigVo appConfig = appConfigBpo.getAppConfig(application.getId());
        List<V1Container> containers = new ArrayList<>();
        V1Container container = DuplicateCodeUtil.setV1Container(appConfig.getAppImagePath(),"Always",applicationName + "-container");

        // 资源设置
        V1ResourceRequirements resReq = DuplicateCodeUtil.setV1ResourceRequirements(appConfig.getMinMemory() +"Gi",appConfig.getMinCPU() + "",appConfig.getMaxMemory() + "Gi",appConfig.getMaxCPU() + "");
        container.resources(resReq);
        // 端口开放
        List<V1ContainerPort> containerPorts = DuplicateCodeUtil.setViContainerPortList("client",CLIENT_PORT);

        V1ContainerPort serverPort = DuplicateCodeUtil.setV1ContainerPort("server",SERVER_PORT);

        V1ContainerPort electionPort = DuplicateCodeUtil.setV1ContainerPort("leader-election",LEADER_ELECTION_PORT);

        containerPorts.add(serverPort);
        containerPorts.add(electionPort);


        container.ports(containerPorts);
        // 命令设置
        List<String> commandList = new ArrayList<>();
        commandList.add("sh");
        commandList.add("-c");
        commandList.add("start-zookeeper --servers=3  " +
                " --data_dir=/var/lib/zookeeper/data   " +
                " --data_log_dir=/var/lib/zookeeper/data/log " +
                " --conf_dir=/opt/zookeeper/conf " +
                " --client_port=2181   " +
                " --election_port=3888     " +
                " --server_port=2888   " +
                " --tick_time=2000  " +
                " --init_limit=10    " +
                " --sync_limit=5" +
                " --heap=512M " +
                " --max_client_cnxns=60 " +
                " --snap_retain_count=3 " +
                " --purge_interval=12 " +
                " --max_session_timeout=40000 " +
                " --min_session_timeout=4000" +
                " --log_level=INFO");

        container.command(commandList);
        //readinessProbe
        V1Probe rp = DuplicateCodeUtil.setV1Probe("[ \"imok\" = \"$(echo ruok | nc -w 1 -q 1 127.0.0.1 2181)\" ]");
        rp.initialDelaySeconds(10);
        rp.timeoutSeconds(5);

        container.readinessProbe(rp);
        //livenessProbe
        V1Probe lp = DuplicateCodeUtil.setV1Probe("zookeeper-ready 2181");
        lp.initialDelaySeconds(10);
        lp.timeoutSeconds(5);
        container.livenessProbe(lp);
        // 目录挂载
        List<V1VolumeMount> volumeMounts = DuplicateCodeUtil.setV1VolumeMountList(dataVolumeName,"/var/lib/zookeeper");
        AppPVVo appPv = appPvList.get(0);
        // 数据目录

        container.volumeMounts(volumeMounts);

        containers.add(container);

        podTemplateSpec.containers(containers);
        podTemplate.spec(podTemplateSpec);
        ssSpec.template(podTemplate);

        // pvc template
        List<V1PersistentVolumeClaim> vctList = DuplicateCodeUtil.setV1PersistentVolumeClaimList(dataVolumeName,appPv.getVolumeUuid(),"storage",appPv.getSpace() + "Gi","ReadWriteOnce");
        ssSpec.volumeClaimTemplates(vctList);

        ss.spec(ssSpec);

        return ss;
    }


    @Override
    protected Integer getDefaultInnerPort() {
        return CLIENT_PORT;
    }
}
