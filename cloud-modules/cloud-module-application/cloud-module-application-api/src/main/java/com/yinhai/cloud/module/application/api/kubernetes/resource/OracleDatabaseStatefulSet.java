package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.core.api.util.SystemConfigUtil;
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
public class OracleDatabaseStatefulSet extends BaseStatefulSet {
    private static final String ORACLE_ROOT_PASSWORD = SystemConfigUtil.getSystemConfigValue("ORACLE_PASSWORD");
    private static final String LINSTER_NAME = "XE";
    private static final String ORACLE_ROOT_USERNAME = "system";
    public OracleDatabaseStatefulSet(Long appId) {
        super(appId);
        create();
    }

    @Override
    protected Integer getDefaultInnerPort() {
        return 1521;
    }

    private void create() {
        yamlObjectList.add(createGovernService());
        yamlObjectList.addAll(createExposeService());
        yamlObjectList.add(createStatefulSet());
    }

    private V1beta1StatefulSet createStatefulSet() {
        V1beta1StatefulSet ss = DuplicateCodeUtil.getV1beta1StatefulSet(applicationName,namespaceName);

        // 设置spec
        V1Volume shmsizeVol = new V1Volume();
        shmsizeVol.name("shmsize");
        V1EmptyDirVolumeSource emptyDir = new V1EmptyDirVolumeSource();
        emptyDir.medium("Memory");
        emptyDir.sizeLimit("1024Mi");
        shmsizeVol.emptyDir(emptyDir);


        V1beta1StatefulSetSpec ssSpec = DuplicateCodeUtil.setV1beta1StatefulSetSpec(governServiceName,application.getInstanceNum());

        V1PodTemplateSpec podTemplate = DuplicateCodeUtil.setV1PodTemplateSpec(podLabels);


        V1PodSpec podTemplateSpec = new V1PodSpec();
        podTemplateSpec.terminationGracePeriodSeconds(10L);

        List<V1Container> containers = new ArrayList<>();
        AppConfigVo appConfig = appConfigBpo.getAppConfig(application.getId());
        V1Container oracleContainer = DuplicateCodeUtil.setV1Container(appConfig.getAppImagePath(),"IfNotPresent",applicationName + "-container");
        List<V1EnvVar> envList = new ArrayList<>();
        V1EnvVar password = new V1EnvVar();
        password.name("ORACLE_PWD");
        //liuyi02 2018/3/1 修改硬编码问题
        password.value(ORACLE_ROOT_PASSWORD);
        envList.add(password);
        oracleContainer.env(envList);
        //资源配置
        V1ResourceRequirements redisResourceReq = DuplicateCodeUtil.setV1ResourceRequirements(appConfig.getMinMemory() + "Gi",appConfig.getMinCPU() + "",appConfig.getMaxMemory() + "Gi",appConfig.getMaxCPU() + "");
        oracleContainer.resources(redisResourceReq)
        ;
        List<V1ContainerPort> containerPorts = DuplicateCodeUtil.setViContainerPortList("client-port",getDefaultInnerPort());

        oracleContainer.ports(containerPorts);
        AppPVVo appPv = appPvList.get(0);
        List<V1VolumeMount> volumeMounts = DuplicateCodeUtil.setV1VolumeMountList(dataVolumeName,"/u01/app/oracle/oradata");

        V1VolumeMount shmsizeMount = DuplicateCodeUtil.setV1VolumeMount(shmsizeVol.getName(),"/dev/shm");

        volumeMounts.add(shmsizeMount);

        oracleContainer.volumeMounts(volumeMounts);


        // 设置检测指针
        V1Probe checkPort = new V1Probe();
        checkPort.initialDelaySeconds(30);
        V1TCPSocketAction socket = new V1TCPSocketAction();
        socket.port(new IntOrString(getDefaultInnerPort()));
        checkPort.tcpSocket(socket);
        oracleContainer.livenessProbe(checkPort);

        podTemplateSpec.restartPolicy("Always");
        // 设置volumes
        List<V1Volume> ptVols = new ArrayList<>();


        ptVols.add(shmsizeVol);

        // 设置volumes结束
        podTemplateSpec.volumes(ptVols);


        containers.add(oracleContainer);

        podTemplateSpec.containers(containers);
        podTemplate.spec(podTemplateSpec);
        ssSpec.template(podTemplate);

        List<V1PersistentVolumeClaim> vctList = DuplicateCodeUtil.setV1PersistentVolumeClaimList(dataVolumeName,appPv.getVolumeUuid(),"storage",appPv.getSpace() + "Gi","ReadWriteOnce");

        ssSpec.volumeClaimTemplates(vctList);

        ss.spec(ssSpec);

        return ss;
    }
}
