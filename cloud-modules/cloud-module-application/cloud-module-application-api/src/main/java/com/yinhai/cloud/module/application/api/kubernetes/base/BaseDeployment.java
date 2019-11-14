package com.yinhai.cloud.module.application.api.kubernetes.base;

import com.yinhai.cloud.module.application.api.exception.CreateKubernetesResourceRuntimeException;
import com.yinhai.cloud.module.application.api.vo.AppServiceVo;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.core.common.api.util.ValidateUtil;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhaokai
 * @create: 2018-09-29 10:28:34
 */
public abstract class BaseDeployment extends AppKubernetesYamlObject {

    public BaseDeployment(Long applicationId) {
        this.application = appBpo.getApp(applicationId);
        createBasicInfo();
    }

    private void createBasicInfo() {
        applicationName = application.getAppIdentify();
        NamespaceVo namespace = namespaceBpo.getNamespace(application.getNamespaceId());
        if (namespace == null) {
            throw new CreateKubernetesResourceRuntimeException(application.getAppName() + " 所属命名空间丢失");
        }
//        namespaceName = namespace.getNamespaceName();
        namespaceName = namespace.getNamespaceIdentify();
        exposeServiceVoList = appServiceBpo.getAppServicesByAppId(application.getId());
        if (exposeServiceVoList.size() == 0) {
            throw new CreateKubernetesResourceRuntimeException(application.getAppName() + " 未配置外部服务");
        }
        configVolumeName = applicationName + "-volume";
        configMapName = applicationName + "-config-map";
        podLabels = new HashMap<>();
        podLabels.put("app", application.getAppIdentify());
    }

    protected List<V1Service> createExposeService() {

        List<V1Service> list = new ArrayList<>();
        for (int i = 0; i < exposeServiceVoList.size(); i++) {
            AppServiceVo svo = exposeServiceVoList.get(i);
            V1Service service = new V1Service();
            service.apiVersion("v1");
            service.kind("Service");
            V1ObjectMeta sMeta = new V1ObjectMeta();
            sMeta.name(applicationName + "-expose-svc-" + (i + 1));
            sMeta.namespace(namespaceName);
            Map<String, String> svcLabels = new HashMap<>();
            svcLabels.put("svc", applicationName + "-expose-svc");
            sMeta.labels(svcLabels);
            service.metadata(sMeta);
            V1ServiceSpec spec = new V1ServiceSpec();
            spec.sessionAffinity("ClientIP");
            V1SessionAffinityConfig sessionAffinityConfig = new V1SessionAffinityConfig();
            V1ClientIPConfig clientIPConfig = new V1ClientIPConfig();
            clientIPConfig.setTimeoutSeconds(7200);
            sessionAffinityConfig.setClientIP(clientIPConfig);
            spec.setSessionAffinityConfig(sessionAffinityConfig);
            spec.type("NodePort");
            spec.selector(podLabels);
            List<V1ServicePort> spList = new ArrayList<>();
            V1ServicePort sp = new V1ServicePort();
            sp.protocol("TCP");
            sp.name("client-" + i);
            if ((svo.getInnerPort() == null)) {
                sp.targetPort(new IntOrString(getDefaultInnerPort()));
                sp.port(getDefaultInnerPort());
            } else {
                sp.targetPort(new IntOrString(svo.getInnerPort()));
                sp.port(svo.getInnerPort());
            }
            sp.nodePort(svo.getMappingPort());
            spList.add(sp);
            spec.ports(spList);
            service.spec(spec);
            list.add(service);
        }

        return list;


    }

}
