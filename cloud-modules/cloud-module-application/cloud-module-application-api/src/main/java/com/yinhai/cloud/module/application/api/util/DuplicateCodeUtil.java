package com.yinhai.cloud.module.application.api.util;

import com.yinhai.cloud.module.application.api.kubernetes.base.BaseStatefulSet;
import com.yinhai.cloud.module.application.api.kubernetes.configmap.ConfigResourceObject;
import com.yinhai.cloud.module.application.api.vo.AppPVVo;
import com.yinhai.cloud.module.application.api.vo.AppServiceVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.core.common.api.util.ValidateUtil;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyi02 on 2019/03/07.
 */

public class DuplicateCodeUtil extends BaseStatefulSet {

    public DuplicateCodeUtil(Long applicationId) {
        super(applicationId);
    }

    public static V1ConfigMap createConfigMap(String configMapName,String namespaceName,String conf,String bind) {
        V1ConfigMap cm = new V1ConfigMap();
        cm.apiVersion("v1");
        cm.kind("ConfigMap");
        V1ObjectMeta cmMeta = new V1ObjectMeta();
        cmMeta.name(configMapName);
        cmMeta.namespace(namespaceName);
        cm.metadata(cmMeta);
        Map<String, String> configData = new HashMap<>();
        configData.put(conf, bind);
        cm.data(configData);
        return cm;

    }

    public static V1beta1StatefulSet createStatefulSet(ConfigResourceObject crb, List<AppPVVo> appPvList, Map<String,String> podLabels) {
        V1beta1StatefulSet ss = getV1beta1StatefulSet(crb.getApplicationName(),crb.getNamespaceName());
        // 设置spec

        V1beta1StatefulSetSpec ssSpec = setV1beta1StatefulSetSpec(crb.getGovernServiceName(),crb.getInstanceNum());
        V1PodTemplateSpec podTemplate = setV1PodTemplateSpec(podLabels);


        V1PodSpec podTemplateSpec = new V1PodSpec();
        List<V1Container> containers = new ArrayList<>();
        V1Container container = setV1Container(crb.getImagePath(),"IfNotPresent",crb.getApplicationName() + "-container");
        AppPVVo appPv = appPvList.get(0);
        String mValue = "";//定义的传入setV1PersistentVolumeClaimSpec中的mValue参数mysql与redis不同
        switch (crb.getType()){
            case "REDIS":
                // 启动参数
                List<String> args = new ArrayList<>();
                args.add("/redis-conf/redis.conf");
                container.args(args);
                mValue = appPv.getSpace()+"Gi";
                break;
            case "MYSQL":
                //mysql
                container.env(CollectionSql(crb.getConfName(),crb.getConfPassword()));
                mValue = (appPv.getSpace()==null? "5":appPv.getSpace())+"Gi";
                break;
        }
        podTemplateSpec.terminationGracePeriodSeconds(crb.getPeriodSeconds());
        // 资源配置
        V1ResourceRequirements redisResourceReq = setV1ResourceRequirements(crb.getDEFAULT_MEMORY_REQUEST(),crb.getDEFAULT_CPU_REQUEST(),crb.getDEFAULT_MEMORY_LIMIT(),crb.getDEFAULT_CPU_LIMIT());

        container.resources(redisResourceReq);
        // 开放端口
        List<V1ContainerPort> containerPorts = setViContainerPortList("server-port",crb.getDefultInnerPort());

        container.ports(containerPorts);
        // 目录挂载

        List<V1VolumeMount> volumeMounts = setV1VolumeMountList(crb.getDataVolumeName(),crb.getMountPath());
        // 数据目录
        // 配置目录
        V1VolumeMount confMount = setV1VolumeMount(crb.getConfigVolumeName(),crb.getSetmountPath());
        volumeMounts.add(confMount);

        container.volumeMounts(volumeMounts);

        podTemplateSpec.restartPolicy("Always");
        // 配置文件生成
        List<V1Volume> ptVols = setV1VolumeList(crb.getConfigVolumeName(),crb.getConfigMapName(),crb.getConf(),crb.getConf());
        podTemplateSpec.volumes(ptVols);


        containers.add(container);
        podTemplateSpec.containers(containers);
        podTemplate.spec(podTemplateSpec);
        ssSpec.template(podTemplate);
        // pvc template
        List<V1PersistentVolumeClaim> vctList = setV1PersistentVolumeClaimList(crb.getDataVolumeName(),appPv.getVolumeUuid(),"storage",mValue,"ReadWriteOnce");
        ssSpec.volumeClaimTemplates(vctList);

        ss.spec(ssSpec);

        return ss;
    }

    public static List<V1EnvVar> CollectionSql(String name,String password){
        List<V1EnvVar> envList = new ArrayList<>();
        V1EnvVar envRootPassword = new V1EnvVar();
        envRootPassword.name(name);
        //liuyi02 2018/3/1 修改硬编码问题
        envRootPassword.value(password);
        envList.add(envRootPassword);
        return envList;
    }

    /**
     * 生成每个V1beta1StatefulSet公共部分
     * @param applicationName
     * @param namespaceName
     * @return V1beta1StatefulSet
     */
    public static V1beta1StatefulSet getV1beta1StatefulSet(String applicationName,String namespaceName){
        V1beta1StatefulSet ss = new V1beta1StatefulSet();
        ss.apiVersion("apps/v1beta1");
        ss.kind("StatefulSet");
        // 设置metadata
        ss.metadata(setV10ObjectMeta(applicationName,namespaceName));
        return ss;
    }

    /**
     * 配置每个V1beta1StatefulSet的V1ObjectMeta
     * @param applicationName
     * @param namespaceName
     * @return V1ObjectMeta
     */
    public static V1ObjectMeta setV10ObjectMeta(String applicationName,String namespaceName){
        V1ObjectMeta ssMetadata = new V1ObjectMeta();
        ssMetadata.name(applicationName);
        ssMetadata.namespace(namespaceName);
        return ssMetadata;
    }

    /**
     * 配置V1beta1StatefulSetSpec
     * @param governServiceName
     * @param instanceNum
     * @return V1beta1StatefulSetSpec
     */
    public static V1beta1StatefulSetSpec setV1beta1StatefulSetSpec(String governServiceName,Integer instanceNum){
        V1beta1StatefulSetSpec ssSpec = new V1beta1StatefulSetSpec();
        ssSpec.serviceName(governServiceName);
        ssSpec.replicas(instanceNum);
        return ssSpec;
    }

    /**
     * 设置初始化V1PodTemplateSpec
     * @param podLabels
     * @return V1PodTemplateSpec
     */
    public static V1PodTemplateSpec setV1PodTemplateSpec(Map<String,String> podLabels){
        V1PodTemplateSpec podTemplate = new V1PodTemplateSpec();
        V1ObjectMeta podMetadata = new V1ObjectMeta();

        podMetadata.labels(podLabels);
        podTemplate.metadata(podMetadata);
        return podTemplate;
    }

    /**
     * 初始化container
     * @param imagePath
     * @param imagePullPolicy
     * @param name
     * @return V1Container
     */
    public static V1Container setV1Container(String imagePath,String imagePullPolicy,String name){
        V1Container container = new V1Container();
        container.image(imagePath);
        container.imagePullPolicy(imagePullPolicy);
        container.name(name);
        return container;
    }

    /**
     * 初始化V1PersistentVolumeClaim列表
     * @param dataVolumeName
     * @param volumeUuid
     * @param mKey
     * @param mValue
     * @param pvcAccessModes
     * @return List<V1PersistentVolumeClaim>
     */
    public static List<V1PersistentVolumeClaim> setV1PersistentVolumeClaimList(String dataVolumeName,String volumeUuid, String mKey, String mValue,String pvcAccessModes){
        List<V1PersistentVolumeClaim> vctList = new ArrayList<>();
        V1PersistentVolumeClaim dataPvc = setV1PersistentVolumeClaim(dataVolumeName,volumeUuid,mKey,mValue,pvcAccessModes);
        vctList.add(dataPvc);
        return vctList;
    }

    /**
     * 初始化单个V1PersistentVolumeClaim
     * @param dataVolumeName
     * @param volumeUuid
     * @param mKey
     * @param mValue
     * @param pvcAccessModes
     * @return V1PersistentVolumeClaim
     */
    public static V1PersistentVolumeClaim setV1PersistentVolumeClaim(String dataVolumeName,String volumeUuid, String mKey, String mValue,String pvcAccessModes){
        V1PersistentVolumeClaim dataPvc = new V1PersistentVolumeClaim();
        V1ObjectMeta dataPvcMetadata = new V1ObjectMeta();
        dataPvcMetadata.name(dataVolumeName);

        dataPvc.metadata(dataPvcMetadata);
        V1PersistentVolumeClaimSpec dataPvcSpec = setV1PersistentVolumeClaimSpec(volumeUuid,mKey,mValue,pvcAccessModes);
        dataPvc.spec(dataPvcSpec);
        return dataPvc;
    }

    /**
     * 配置初始化V1PersistentVolumeClaimSpec
     * @param volumeUuid
     * @param mKey
     * @param mValue
     * @param pvcAccessModes
     * @return V1PersistentVolumeClaimSpec
     */
    public static V1PersistentVolumeClaimSpec setV1PersistentVolumeClaimSpec(String volumeUuid, String mKey, String mValue,String pvcAccessModes){
        V1PersistentVolumeClaimSpec dataPvcSpec = new V1PersistentVolumeClaimSpec();
        dataPvcSpec.storageClassName(volumeUuid);
        V1ResourceRequirements dataPvcResource = new V1ResourceRequirements();
        Map<String, Quantity> pvcRequestQuantity = new HashMap<>();
        pvcRequestQuantity.put(mKey, new Quantity(mValue));
        dataPvcResource.requests(pvcRequestQuantity);
        dataPvcSpec.resources(dataPvcResource);
        List<String> dataPvcAccessModes = new ArrayList<>();
        dataPvcAccessModes.add(pvcAccessModes);
        dataPvcSpec.accessModes(dataPvcAccessModes);
        return dataPvcSpec;
    }

    /**
     * 配置初始化V1ResourceRequirements
     * @param DEFAULT_MEMORY_REQUEST
     * @param DEFAULT_CPU_REQUEST
     * @param DEFAULT_MEMORY_LIMIT
     * @param DEFAULT_CPU_LIMIT
     * @return V1ResourceRequirements
     */
    public static V1ResourceRequirements setV1ResourceRequirements(String DEFAULT_MEMORY_REQUEST,String DEFAULT_CPU_REQUEST,String DEFAULT_MEMORY_LIMIT,String DEFAULT_CPU_LIMIT){
        V1ResourceRequirements resourceReq = new V1ResourceRequirements();
        Map<String, Quantity> requestQuantity = setQuantity(DEFAULT_MEMORY_REQUEST,DEFAULT_CPU_REQUEST);

        Map<String, Quantity> limitQuantity = setQuantity(DEFAULT_MEMORY_LIMIT,DEFAULT_CPU_LIMIT);

        resourceReq.requests(requestQuantity);
        resourceReq.limits(limitQuantity);
        return resourceReq;
    }

    /**
     * 配置初始化memory与cpu所占比例
     * @param DEFAULT_MEMORY
     * @param DEFAULT_CPU
     * @return Map<String, Quantity>
     */
    public static Map<String, Quantity> setQuantity(String DEFAULT_MEMORY,String DEFAULT_CPU){
        Map<String, Quantity> requestQuantity = new HashMap<>();
        requestQuantity.put("memory", new Quantity(DEFAULT_MEMORY));
        requestQuantity.put("cpu", new Quantity(DEFAULT_CPU));
        return requestQuantity;
    }

    /**
     * 配置容器端口列表
     * @param name
     * @param port
     * @return List<V1ContainerPort>
     */
    public static List<V1ContainerPort> setViContainerPortList(String name,Integer port){
        List<V1ContainerPort> containerPorts = new ArrayList<>();
        V1ContainerPort cPort = setV1ContainerPort(name,port);
        containerPorts.add(cPort);
        return containerPorts;
    }

    /**
     * 配置单独容器端口
     * @param name
     * @param port
     * @return V1ContainerPort
     */
    public static V1ContainerPort setV1ContainerPort(String name,Integer port){
        V1ContainerPort cPort = new V1ContainerPort();
        cPort.name(name);
        cPort.containerPort(port);
        return cPort;
    }

    /**
     * 初始化V1VolumeMount列表
     * @param dataVolumeName
     * @param mountPath
     * @return List<V1VolumeMount>
     */
    public static List<V1VolumeMount> setV1VolumeMountList(String dataVolumeName,String mountPath){
        List<V1VolumeMount> volumeMounts = new ArrayList<>();
        // 数据目录
        V1VolumeMount dataMount = setV1VolumeMount(dataVolumeName,mountPath);
        volumeMounts.add(dataMount);
        return volumeMounts;
    }

    /**
     * 初始化单个V1VolumeMount
     * @param dataVolumeName
     * @param mountPath
     * @return V1VolumeMount
     */
    public static V1VolumeMount setV1VolumeMount(String dataVolumeName,String mountPath){
        V1VolumeMount dataMount = new V1VolumeMount();
        dataMount.name(dataVolumeName);//volumn 的名称
        dataMount.mountPath(mountPath);
        return dataMount;
    }

    /**
     * 初始化V1Volume列表
     * @param dataVolumeName
     * @param configMapName
     * @param confKey
     * @param confPath
     * @return List<V1Volume>
     */
    public static List<V1Volume> setV1VolumeList(String dataVolumeName,String configMapName,String confKey,String confPath){
        List<V1Volume> ptVols = new ArrayList<>();
        V1Volume confVol = new V1Volume();
        confVol.name(dataVolumeName);
        V1ConfigMapVolumeSource configMapSource = new V1ConfigMapVolumeSource();
        configMapSource.name(configMapName);
        List<V1KeyToPath> confItemList = new ArrayList<>();
        V1KeyToPath confItem = new V1KeyToPath();
        confItem.key(confKey);
        confItem.path(confPath);
        confItemList.add(confItem);
        configMapSource.items(confItemList);
        confVol.configMap(configMapSource);
        ptVols.add(confVol);
        return ptVols;
    }

    /**
     * 初始化KafaStateSet和ZookerperStatefulSet的V1PodSpec
     * @param applicationName
     * @return V1PodSpec
     */
    public static V1PodSpec setV1PodSpec(String applicationName){
        V1PodSpec podTemplateSpec = new V1PodSpec();
        // Affinity
        V1Affinity aff = new V1Affinity();
        V1PodAntiAffinity podAff = new V1PodAntiAffinity();
        List<V1PodAffinityTerm> reqList = new ArrayList<>();
        V1PodAffinityTerm r = new V1PodAffinityTerm();
        V1LabelSelector rLSel = new V1LabelSelector();
        List<V1LabelSelectorRequirement> expList = new ArrayList<>();
        V1LabelSelectorRequirement exp = new V1LabelSelectorRequirement();
        exp.key("app");
        exp.operator("In");
        List<String> vals = new ArrayList<>();
        vals.add(applicationName);
        exp.values(vals);


        expList.add(exp);

        rLSel.matchExpressions(expList);

        r.labelSelector(rLSel);
        r.topologyKey("kubernetes.io/hostname");
        reqList.add(r);

        podAff.requiredDuringSchedulingIgnoredDuringExecution(reqList);

        aff.podAntiAffinity(podAff);

        podTemplateSpec.affinity(aff);
        return podTemplateSpec;
    }

    /**
     * 初始化KafaStateSet和ZookerperStatefulSet的V1Probe
     * @param cmdListValue
     * @return
     */
    public static V1Probe setV1Probe(String cmdListValue){
        V1Probe rp = new V1Probe();
        V1ExecAction rpExec = new V1ExecAction();
        List<String> rpCmdList = new ArrayList<>();
        rpCmdList.add("sh");
        rpCmdList.add("-c");
        rpCmdList.add(cmdListValue);
        rpExec.command(rpCmdList);

        rp.exec(rpExec);
        return rp;
    }

    /**
     * 创建V1beta1PodDisruptionBudget
     * @param applicationName
     * @param namespaceName
     * @param podLabels
     * @param minAvailable
     * @return V1beta1PodDisruptionBudget
     */
    public static V1beta1PodDisruptionBudget setV1beta1PodDisruptionBudget(String applicationName,String namespaceName,Map<String, String>  podLabels,IntOrString minAvailable){
        V1beta1PodDisruptionBudget pdb = new V1beta1PodDisruptionBudget();
        pdb.apiVersion("policy/v1beta1");
        pdb.setKind("PodDisruptionBudget");
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.name(applicationName + "-pdb");
        meta.namespace(namespaceName);

        pdb.metadata(meta);

        V1beta1PodDisruptionBudgetSpec spec = new V1beta1PodDisruptionBudgetSpec();
        V1LabelSelector sel = new V1LabelSelector();
        sel.matchLabels(podLabels);
        spec.selector(sel);
        spec.minAvailable(minAvailable);

        pdb.spec(spec);

        return pdb;
    }

    public static V1Service setV1Service(String apiVersion,String serKind,String governServiceName,String namespaceName,String key,String value){
        V1Service service = new V1Service();
        service.apiVersion(apiVersion);
        service.kind(serKind);
        V1ObjectMeta sMeta = new V1ObjectMeta();
        sMeta.name(governServiceName);
        sMeta.namespace(namespaceName);
        Map<String, String> svcLabels = new HashMap<>();
        svcLabels.put(key, value);
        sMeta.labels(svcLabels);
        service.metadata(sMeta);
        return service;
    }

    public static AppsV1beta1Deployment setAppsV1beta1Deployment(String apiVersion,String serKind,String applicationName,String namespaceName){
        AppsV1beta1Deployment dep = new AppsV1beta1Deployment();
        dep.apiVersion(apiVersion);
        dep.kind(serKind);
        // 设置metadata
        V1ObjectMeta ssMetadata = new V1ObjectMeta();
        ssMetadata.name(applicationName);

        ssMetadata.namespace(namespaceName);
        dep.metadata(ssMetadata);
        return dep;
    }

    public static List<V1ContainerPort> setV1ContainerPortList(List<AppServiceVo> exposeServiceVoList,Integer defaultInnerPort){
        List<V1ContainerPort> cPortList = new ArrayList<>();
        for (AppServiceVo s : exposeServiceVoList) {
            V1ContainerPort cPort = new V1ContainerPort();
            if (s.getInnerPort() != null) {
                cPort.containerPort(s.getInnerPort());
            } else {
                cPort.containerPort(defaultInnerPort);
            }
            cPortList.add(cPort);
        }
        return cPortList;
    }

    public static List<V1ServicePort> setSpList(String applicationName,Integer defultInnerPort){
        List<V1ServicePort> spList = new ArrayList<>();
        V1ServicePort sp = new V1ServicePort();

        sp.port(defultInnerPort);
        sp.name(applicationName + "port");
        sp.targetPort(new IntOrString(defultInnerPort));
        spList.add(sp);
        return spList;
    }

    public static V1ServiceSpec setV1ServiceSpec(List<V1ServicePort> spList,Map<String, String> podLabels){
        V1ServiceSpec spec = new V1ServiceSpec();
        spec.selector(podLabels);
        spec.ports(spList);
        spec.clusterIP("None");
        return spec;
    }

    @Override
    protected Integer getDefaultInnerPort() {
        return null;
    }


    public static List<Map> parseSysoutMsg(String msg) {
        String[] rows = msg.split("\n");
        List<Map> list = new ArrayList<Map>();
        for (String row : rows) {
            String[] lines = row.split("   ");
            Map<Integer, String> map = new HashMap<>();
            int i = 1;
            for (String line : lines) {
                if (ValidateUtil.isNotEmpty(line)) {
                    map.put(i++, line.trim());
                }
            }
            list.add(map);
        }
        return list;
    }

}
