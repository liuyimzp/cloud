package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.module.application.api.kubernetes.base.BaseStatefulSet;
import com.yinhai.cloud.module.application.api.kubernetes.helper.PvcName;
import com.yinhai.cloud.module.application.api.util.DuplicateCodeUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppPVVo;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhaokai
 * @create: 2018-09-30 12:57:34
 */
public class KafkaStatefulSet extends BaseStatefulSet {
    private String imagePath;
//    private static final String zkImagePath = imageBasePath + "/k8s.gcr.io/kubernetes-zookeeper:1.0-3.4.10";
    private static final String zkImagePath = "192.168.17.187:5000/general/zookeeper:3.0";
    private static final Integer ZK_CLIENT_PORT = 2181;
    private static final Integer ZK_SERVER_PORT = 2888;
    private static final Integer ZK_ELECTION_PORT = 3888;
    private String zkApplicationName;

    private String zkGovernServiceName;
    private Map<String, String> zkPodLabels;
    private String zkDataVolumeName;
    private Integer ZOOKEEPER_DEFAULT_REPLICAS = 3;
    private Integer ZOOKEEPER_DEFAULT_STORAGE_REQUEST = 2;
    private static final String conf = "server.properties";
    public KafkaStatefulSet(Long applicationId) {
        super(applicationId);

        zkApplicationName = applicationName + "-zookeeper";
        zkGovernServiceName = applicationName + "-zookeeper-govern-svc";
        zkDataVolumeName = zkApplicationName + "-data-volume";
        PvcName zkPvcName = new PvcName(zkDataVolumeName, zkApplicationName, 3);
        zkPvcName.setAllowFlexDelete(false);
        pvcList.add(zkPvcName);
        zkPodLabels = new HashMap<>();
        zkPodLabels.put("app", zkApplicationName);

        yamlObjectList.add(createConfigMap());
        yamlObjectList.add(createGovernService());
        yamlObjectList.add(createZkGovernService());
        yamlObjectList.add(createZkPodDisruptionBudget());
        yamlObjectList.add(createZookeeperStatefulSet());
        yamlObjectList.addAll(createExposeService());
        yamlObjectList.add(createPodDisruptionBudget());
        yamlObjectList.add(createStatefulSet());
    }

    private V1beta1PodDisruptionBudget createPodDisruptionBudget() {
        V1beta1PodDisruptionBudget pdb = DuplicateCodeUtil.setV1beta1PodDisruptionBudget(applicationName,namespaceName,podLabels,new IntOrString(2));
        return pdb;
    }

    private V1ConfigMap createConfigMap() {
        String bind = "listeners=PLAINTEXT://:" + getDefaultInnerPort() + "\n" +
//                "zookeeper.connect=127.0.0.1\n" +
                "zookeeper.connect=" + zkApplicationName + "-0." + zkGovernServiceName + "." + namespaceName + ".svc.cluster.local\n" +
                "log.dir=/tmp/kafka-logs\n" +
                "auto.create.topics.enable=true\n" +
                "auto.leader.rebalance.enable=true\n" +
                "background.threads=10\n" +
                "compression.type=producer\n" +
                "delete.topic.enable=false\n" +
                "leader.imbalance.check.interval.seconds=300\n" +
                "leader.imbalance.per.broker.percentage=10\n" +
                "log.flush.interval.messages=9223372036854775807\n" +
                "log.flush.offset.checkpoint.interval.ms=60000\n" +
                "log.flush.scheduler.interval.ms=9223372036854775807\n" +
                "log.retention.bytes=-1\n" +
                "log.retention.hours=168\n" +
                "log.roll.hours=168\n" +
                "log.roll.jitter.hours=0\n" +
                "log.segment.bytes=1073741824\n" +
                "log.segment.delete.delay.ms=60000\n" +
                "message.max.bytes=1000012\n" +
                "min.insync.replicas=1\n" +
                "num.io.threads=8\n" +
                "num.network.threads=3\n" +
                "num.recovery.threads.per.data.dir=1\n" +
                "num.replica.fetchers=1\n" +
                "offset.metadata.max.bytes=4096\n" +
                "offsets.commit.required.acks=-1\n" +
                "offsets.commit.timeout.ms=5000\n" +
                "offsets.load.buffer.size=5242880\n" +
                "offsets.retention.check.interval.ms=600000\n" +
                "offsets.retention.minutes=1440\n" +
                "offsets.topic.compression.codec=0\n" +
                "offsets.topic.num.partitions=50\n" +
                "offsets.topic.replication.factor=3\n" +
                "offsets.topic.segment.bytes=104857600\n" +
                "queued.max.requests=500\n" +
                "quota.consumer.default=9223372036854775807\n" +
                "quota.producer.default=9223372036854775807\n" +
                "replica.fetch.min.bytes=1\n" +
                "replica.fetch.wait.max.ms=500\n" +
                "replica.high.watermark.checkpoint.interval.ms=5000\n" +
                "replica.lag.time.max.ms=10000\n" +
                "replica.socket.receive.buffer.bytes=65536\n" +
                "replica.socket.timeout.ms=30000\n" +
                "request.timeout.ms=30000\n" +
                "socket.receive.buffer.bytes=102400\n" +
                "socket.request.max.bytes=104857600\n" +
                "socket.send.buffer.bytes=102400\n" +
                "unclean.leader.election.enable=true\n" +
                "zookeeper.session.timeout.ms=6000\n" +
                "zookeeper.set.acl=false\n" +
                "broker.id.generation.enable=true\n" +
                "connections.max.idle.ms=600000\n" +
                "controlled.shutdown.enable=true\n" +
                "controlled.shutdown.max.retries=3\n" +
                "controlled.shutdown.retry.backoff.ms=5000\n" +
                "controller.socket.timeout.ms=30000\n" +
                "default.replication.factor=1\n" +
                "fetch.purgatory.purge.interval.requests=1000\n" +
                "group.max.session.timeout.ms=300000\n" +
                "group.min.session.timeout.ms=6000\n" +
                "log.cleaner.backoff.ms=15000\n" +
                "log.cleaner.dedupe.buffer.size=134217728\n" +
                "log.cleaner.delete.retention.ms=86400000\n" +
                "log.cleaner.enable=true\n" +
                "log.cleaner.io.buffer.load.factor=0.9\n" +
                "log.cleaner.io.buffer.size=524288\n" +
                "log.cleaner.io.max.bytes.per.second=1.7976931348623157E308\n" +
                "log.cleaner.min.cleanable.ratio=0.5\n" +
                "log.cleaner.min.compaction.lag.ms=0\n" +
                "log.cleaner.threads=1\n" +
                "log.cleanup.policy=delete\n" +
                "log.index.interval.bytes=4096\n" +
                "log.index.size.max.bytes=10485760\n" +
                "log.message.timestamp.difference.max.ms=9223372036854775807\n" +
                "log.message.timestamp.type=CreateTime\n" +
                "log.preallocate=false\n" +
                "log.retention.check.interval.ms=300000\n" +
                "max.connections.per.ip=2147483647\n" +
                "num.partitions=1\n" +
                "producer.purgatory.purge.interval.requests=1000\n" +
                "replica.fetch.backoff.ms=1000\n" +
                "replica.fetch.max.bytes=1048576\n" +
                "replica.fetch.response.max.bytes=10485760\n" +
                "reserved.broker.max.id=1000";
        return DuplicateCodeUtil.createConfigMap(configMapName,namespaceName,conf,bind);
    }

    private V1beta1StatefulSet createStatefulSet() {
        V1beta1StatefulSet ss = DuplicateCodeUtil.getV1beta1StatefulSet(applicationName,namespaceName);

        V1beta1StatefulSetSpec ssSpec = DuplicateCodeUtil.setV1beta1StatefulSetSpec(governServiceName,application.getInstanceNum());

        V1PodTemplateSpec podTemplate = DuplicateCodeUtil.setV1PodTemplateSpec(podLabels);
        V1PodSpec podSpec = new V1PodSpec();

        V1Affinity aff = new V1Affinity();
        // podAntiAffinity
        V1PodAntiAffinity podAntiAff = new V1PodAntiAffinity();
        List<V1PodAffinityTerm> reqsid = new ArrayList<>();
        V1PodAffinityTerm r = new V1PodAffinityTerm();
        V1LabelSelector ls = new V1LabelSelector();
        List<V1LabelSelectorRequirement> mExp = new ArrayList<>();
        V1LabelSelectorRequirement m = new V1LabelSelectorRequirement();
        m.key("app");
        m.operator("In");
        List<String> vals = new ArrayList<>();
        vals.add(applicationName);
        r.topologyKey("kubernetes.io/hostname");

        m.values(vals);

        mExp.add(m);

        ls.matchExpressions(mExp);
        r.labelSelector(ls);

        reqsid.add(r);

        podAntiAff.requiredDuringSchedulingIgnoredDuringExecution(reqsid);

        aff.podAntiAffinity(podAntiAff);
        // podAffinity
        V1PodAffinity podAff = new V1PodAffinity();
        List<V1WeightedPodAffinityTerm> pdsid = new ArrayList<>();
        V1WeightedPodAffinityTerm p = new V1WeightedPodAffinityTerm();
        p.weight(1);
        V1PodAffinityTerm pAffTerm = new V1PodAffinityTerm();
        V1LabelSelector pls = new V1LabelSelector();
        List<V1LabelSelectorRequirement> pmExp = new ArrayList<>();
        V1LabelSelectorRequirement pm = new V1LabelSelectorRequirement();
        pm.key("app");
        pm.operator("In");
        List<String> pmValList = new ArrayList<>();
        pAffTerm.topologyKey("kubernetes.io/hostname");
        pmValList.add(zkApplicationName);
        pm.values(pmValList);
        pmExp.add(pm);
        pls.matchExpressions(pmExp);
        pAffTerm.labelSelector(pls);
        p.podAffinityTerm(pAffTerm);
        pdsid.add(p);
        podAff.preferredDuringSchedulingIgnoredDuringExecution(pdsid);
        aff.podAffinity(podAff);
        // affinity
        podSpec.affinity(aff);

        podSpec.terminationGracePeriodSeconds(80L);
        List<V1Container> containerList = new ArrayList<>();
        AppConfigVo appConfigVo = appConfigBpo.getAppConfig(application.getId());//配置详情
        V1Container con = DuplicateCodeUtil.setV1Container(appConfigVo.getAppImagePath(),"Always",applicationName + "-container");
        // 资源信息

        V1ResourceRequirements res = DuplicateCodeUtil.setV1ResourceRequirements(appConfigVo.getMinMemory() + "Gi",appConfigVo.getMinCPU() + "",appConfigVo.getMaxMemory() + "Gi",appConfigVo.getMaxCPU() + "");
        con.resources(res);
        // 开放端口
        List<V1ContainerPort> conPortList = DuplicateCodeUtil.setViContainerPortList("server",getDefaultInnerPort());
        con.ports(conPortList);
        // 命令
        List<String> cmdList = new ArrayList<>();
        cmdList.add("sh");
        cmdList.add("-c");
//        cmdList.add("exec sh /kafkaServer/kafka/run.sh --override broker.id=${HOSTNAME##*-}");
//        cmdList.add("exec /opt/kafka/bin/kafka-server-start.sh /kafka-config/server.properties  --override broker.id=${HOSTNAME##*-}");
        cmdList.add("exec sh /kafkaServer/kafka/bin/kafka-server-start.sh /mnt/server.properties  --override broker.id=${HOSTNAME##*-}");
        con.command(cmdList);
        containerList.add(con);
        // 环境变量

        List<V1EnvVar> envList = new ArrayList<>();

        V1EnvVar heapOpts = new V1EnvVar();
        heapOpts.name("KAFKA_HEAP_OPTS");
        heapOpts.value("-Xmx512M -Xms512M");
        envList.add(heapOpts);

        V1EnvVar kafkaOpts = new V1EnvVar();
        kafkaOpts.name("KAFKA_OPTS");
        kafkaOpts.value("-Dlogging.level=INFO");
        envList.add(kafkaOpts);

        con.env(envList);
        // 目录挂载
        // 数据目录
        List<V1VolumeMount> volumeMountsList = DuplicateCodeUtil.setV1VolumeMountList(dataVolumeName,"/var/lib/kafka");

        // 配置目录
        V1VolumeMount confMount = DuplicateCodeUtil.setV1VolumeMount(configVolumeName,"/mnt/");
//        V1VolumeMount confMount = DuplicateCodeUtil.setV1VolumeMount(configVolumeName,"/kafka-config");
        volumeMountsList.add(confMount);
        con.volumeMounts(volumeMountsList);

        // 配置文件生成
        List<V1Volume> ptVols = DuplicateCodeUtil.setV1VolumeList(configVolumeName,configMapName,"server.properties","server.properties");
        podSpec.volumes(ptVols);

        // 探针
        V1Probe rp = DuplicateCodeUtil.setV1Probe("exec /kafkaServer/kafka/bin/kafka-broker-api-versions.sh --bootstrap-server=localhost:" + getDefaultInnerPort());
//        V1Probe rp = DuplicateCodeUtil.setV1Probe("exec /opt/kafka/bin/kafka-broker-api-versions.sh --bootstrap-server=localhost:" + getDefaultInnerPort());

        con.readinessProbe(rp);

        podSpec.containers(containerList);

        V1PodSecurityContext sc = new V1PodSecurityContext();
        sc.runAsUser(1000L);
        sc.fsGroup(1000L);

        podSpec.securityContext(sc);

        podTemplate.spec(podSpec);

        ssSpec.template(podTemplate);
        // pvc template
        AppPVVo appPv = appPvList.get(0);
        List<V1PersistentVolumeClaim> vctList = DuplicateCodeUtil.setV1PersistentVolumeClaimList(dataVolumeName,appPv.getVolumeUuid(),"storage",appPv.getSpace() + "Gi","ReadWriteOnce");

        ssSpec.volumeClaimTemplates(vctList);


        ss.spec(ssSpec);
        return ss;
    }

    private V1beta1StatefulSet createZookeeperStatefulSet() {

        V1beta1StatefulSet ss = new V1beta1StatefulSet();
        ss.apiVersion("apps/v1beta1");
        ss.kind("StatefulSet");
        // 设置metadata
        V1ObjectMeta ssMetadata = new V1ObjectMeta();
        ssMetadata.name(zkApplicationName);

        ssMetadata.namespace(namespaceName);
        ss.metadata(ssMetadata);

        // 设置spec
        V1beta1StatefulSetSpec ssSpec = new V1beta1StatefulSetSpec();

        ssSpec.serviceName(zkGovernServiceName);
        ssSpec.replicas(ZOOKEEPER_DEFAULT_REPLICAS);

        V1LabelSelector podSel = new V1LabelSelector();

        podSel.matchLabels(zkPodLabels);

        ssSpec.selector(podSel);

        V1beta1StatefulSetUpdateStrategy us = new V1beta1StatefulSetUpdateStrategy();
        us.type("RollingUpdate");
        ssSpec.updateStrategy(us);

        ssSpec.podManagementPolicy("Parallel");

        V1PodTemplateSpec podTemplate = DuplicateCodeUtil.setV1PodTemplateSpec(zkPodLabels);

        V1PodSpec podTemplateSpec = DuplicateCodeUtil.setV1PodSpec(zkApplicationName);
        // Affinity

        List<V1Container> containers = new ArrayList<>();
        V1Container container = DuplicateCodeUtil.setV1Container(zkImagePath,"Always",zkApplicationName + "-container");

        // 资源设置
        V1ResourceRequirements resReq = new V1ResourceRequirements();
        Map<String, Quantity> requestQuantity = DuplicateCodeUtil.setQuantity("0.8Gi","0.2");

        resReq.requests(requestQuantity);

        container.resources(resReq);
        // 开放端口
        List<V1ContainerPort> containerPorts = DuplicateCodeUtil.setViContainerPortList("client",ZK_CLIENT_PORT);

        V1ContainerPort serverPort = DuplicateCodeUtil.setV1ContainerPort("server",ZK_SERVER_PORT);

        V1ContainerPort electionPort = DuplicateCodeUtil.setV1ContainerPort("leader-election",ZK_ELECTION_PORT);

        containerPorts.add(serverPort);
        containerPorts.add(electionPort);


        container.ports(containerPorts);
        // 命令
        List<String> commandList = new ArrayList<>();
        commandList.add("sh");
        commandList.add("-c");
        commandList.add("start-zookeeper --servers=3  " +
                " --data_dir=/var/lib/zookeeper/data   " +
                " --data_log_dir=/var/lib/zookeeper/data/log " +
                " --conf_dir=/opt/zookeeper/conf " +
                " --client_port=" + ZK_CLIENT_PORT + "   " +
                " --election_port=" + ZK_ELECTION_PORT + "   " +
                " --server_port=" + ZK_SERVER_PORT + "   " +
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
        V1Probe rp = new V1Probe();
        V1ExecAction rpExec = new V1ExecAction();
        List<String> rpCmdList = new ArrayList<>();
        rpCmdList.add("sh");
        rpCmdList.add("-c");
        rpCmdList.add("[ \"imok\" = \"$(echo ruok | nc -w 1 -q 1 127.0.0.1 2181)\" ]");
        rpExec.command(rpCmdList);

        rp.exec(rpExec);
        rp.initialDelaySeconds(10);
        rp.timeoutSeconds(5);

        container.readinessProbe(rp);
        //livenessProbe
        V1Probe lp = new V1Probe();
        V1ExecAction lpExec = new V1ExecAction();
        List<String> lpCmdList = new ArrayList<>();
        lpCmdList.add("sh");
        lpCmdList.add("-c");
        lpCmdList.add("zookeeper-ready 2181");
        lpExec.command(lpCmdList);

        lp.exec(lpExec);
        lp.initialDelaySeconds(10);
        lp.timeoutSeconds(5);
        container.livenessProbe(lp);
        // 目录挂载
        AppPVVo appPv = appPvList.get(0);
        List<V1VolumeMount> volumeMounts = DuplicateCodeUtil.setV1VolumeMountList(zkDataVolumeName,"/var/lib/zookeeper");
        // 数据目录
        container.volumeMounts(volumeMounts);
        containers.add(container);

        podTemplateSpec.containers(containers);
        podTemplate.spec(podTemplateSpec);
        ssSpec.template(podTemplate);

        List<V1PersistentVolumeClaim> vctList = DuplicateCodeUtil.setV1PersistentVolumeClaimList(zkDataVolumeName,appPv.getVolumeUuid(),"storage",ZOOKEEPER_DEFAULT_STORAGE_REQUEST + "Gi","ReadWriteOnce");

        ssSpec.volumeClaimTemplates(vctList);

        ss.spec(ssSpec);

        return ss;
    }

    protected V1Service createZkGovernService() {
        V1Service service = new V1Service();
        service.apiVersion("v1");
        service.kind("Service");
        V1ObjectMeta sMeta = new V1ObjectMeta();
        sMeta.name(zkGovernServiceName);
        sMeta.namespace(namespaceName);
        Map<String, String> svcLabels = new HashMap<>();
        svcLabels.put("svc", zkGovernServiceName);
        sMeta.labels(svcLabels);
        service.metadata(sMeta);

        V1ServiceSpec spec = new V1ServiceSpec();

        spec.selector(zkPodLabels);

        List<V1ServicePort> spList = new ArrayList<>();
        V1ServicePort spServer = new V1ServicePort();
        spServer.port(ZK_SERVER_PORT);
        spServer.name("server");
        spList.add(spServer);

        V1ServicePort spElection = new V1ServicePort();
        spElection.port(ZK_ELECTION_PORT);
        spElection.name("leader-election");
        spList.add(spElection);

        spec.ports(spList);
        spec.clusterIP("None");
        service.spec(spec);
        return service;

    }


    private V1beta1PodDisruptionBudget createZkPodDisruptionBudget() {
        V1beta1PodDisruptionBudget pdb = new V1beta1PodDisruptionBudget();
        pdb.apiVersion("policy/v1beta1");
        pdb.setKind("PodDisruptionBudget");
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.name(zkApplicationName + "-pdb");
        meta.namespace(namespaceName);
        pdb.metadata(meta);
        V1beta1PodDisruptionBudgetSpec spec = new V1beta1PodDisruptionBudgetSpec();
        V1LabelSelector sel = new V1LabelSelector();
        sel.matchLabels(zkPodLabels);
        spec.selector(sel);
        spec.minAvailable(new IntOrString(1));
        pdb.spec(spec);
        return pdb;
    }

    @Override
    protected Integer getDefaultInnerPort() {
        return 9092;
    }
}
