package com.yinhai.cloud.module.resource.kubernetes.lstio;


import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.kubernetes.base.KubernetesYamlObject;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.core.app.api.util.ServiceLocator;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyi02 on 2019/11/5 0028.
 */
public class IstioIngressGateway extends KubernetesYamlObject {

    private IClusterBpo clusterBpo = ServiceLocator.getService(IClusterBpo.class);

    private ClusterVo clusterVo;

    public IstioIngressGateway(NodeBasicInfoVo vo) {
        clusterVo = clusterBpo.getClusterById(vo.getClusterId());
        create();
    }

    private void create() {
        yamlObjectList.add(createService());
    }

    private V1Service createService(){
        V1Service service = new V1Service();
        service.apiVersion("v1");
        service.kind("Service");
        V1ObjectMeta sMeta = new V1ObjectMeta();
        sMeta.name("istio-ingressgateway");
        sMeta.namespace("istio-system");
        Map<String, String> svcLabels = new HashMap<>();
        svcLabels.put("chart", "gateways-1.0.6");
        svcLabels.put("release", "istio");
        svcLabels.put("heritage", "Tiller");
        svcLabels.put("app", "istio-ingressgateway");
        svcLabels.put("istio", "ingressgateway");
        sMeta.labels(svcLabels);
        service.metadata(sMeta);
        V1ServiceSpec spec = new V1ServiceSpec();
        spec.type("NodePort");
        spec.sessionAffinity("ClientIP");
        Map<String, String> selector = new HashMap<>();
        selector.put("app","istio-ingressgateway");
        selector.put("istio","ingressgateway");
        spec.selector(selector);
        List<V1ServicePort> spList = new ArrayList<>();

        V1ServicePort http2 = new V1ServicePort();
        http2.name("http2");
        http2.nodePort(clusterVo.getIstioPort());
        http2.port(80);
        http2.targetPort(new IntOrString(80));
        spList.add(http2);

        V1ServicePort https = new V1ServicePort();
        https.name("https");
        https.nodePort(31390);
        https.port(443);
        spList.add(https);

        V1ServicePort tcp = new V1ServicePort();
        tcp.name("tcp");
        tcp.nodePort(31400);
        tcp.port(31400);
        spList.add(tcp);

        V1ServicePort tpgt = new V1ServicePort();
        tpgt.name("tcp-pilot-grpc-tls");
        tpgt.targetPort(new IntOrString(15011));
        tpgt.port(15011);
        spList.add(tpgt);

        V1ServicePort tcgt = new V1ServicePort();
        tcgt.name("tcp-citadel-grpc-tls");
        tcgt.targetPort(new IntOrString(8060));
        tcgt.port(8060);
        spList.add(tcgt);

        V1ServicePort tdt = new V1ServicePort();
        tdt.name("tcp-dns-tls");
        tdt.targetPort(new IntOrString(853));
        tdt.port(853);
        spList.add(tdt);
        spec.ports(spList);
        service.spec(spec);
        return service;
    }
}
