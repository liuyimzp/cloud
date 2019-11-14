package com.yinhai.cloud.module.application.api.kubernetes.base;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.exception.CreateKubernetesResourceRuntimeException;
import com.yinhai.cloud.module.application.api.kubernetes.helper.PvcName;
import com.yinhai.cloud.module.application.api.util.DuplicateCodeUtil;
import com.yinhai.cloud.module.application.api.vo.AppPVVo;
import com.yinhai.cloud.module.application.api.vo.AppServiceVo;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhaokai
 * @create: 2018-09-29 10:28:10
 */
public abstract class BaseStatefulSet extends AppKubernetesYamlObject {

    protected String governServiceName;
    protected List<AppPVVo> appPvList;
    protected List<PvcName> pvcList = new ArrayList<>();

    public BaseStatefulSet(Long applicationId) {
        this.application = appBpo.getApp(applicationId);
        createBasicInfo();
    }

    public List<PvcName> getPvcNameList() {
        return pvcList;
    }

    public Map<Long,Integer> getVolumeUsedSpace(){
        final Map<Long,Integer> usedSpace = new HashMap<>();
        appPvList.stream().forEach(n->{
            long vid = n.getVolumeId();
            if(usedSpace.get(vid)==null){
                usedSpace.put(vid,0);
            }
            usedSpace.put(vid,usedSpace.get(vid)+(n.getSpace()==null?0:n.getSpace()));
        });
        Set<Map.Entry<Long, Integer>> entries = usedSpace.entrySet();
        for(Map.Entry<Long, Integer> e:entries){
            usedSpace.put(e.getKey(),application.getInstanceNum()*e.getValue());
        }
        return usedSpace;
    }

    private void createBasicInfo() {

        applicationName = application.getAppIdentify();
        NamespaceVo namespace = namespaceBpo.getNamespace(application.getNamespaceId());
        if (namespace == null) {
            throw new CreateKubernetesResourceRuntimeException(application.getAppName() + " 所属命名空间丢失");
        }
        namespaceName = namespace.getNamespaceIdentify();

        exposeServiceVoList = appServiceBpo.getAppServicesByAppId(application.getId()).stream().filter(n -> IAppConstants.SERVICE_TYPE_OUTER.equals(n.getServiceType())).collect(Collectors.toList());

        appPvList = appPVBpo.getPVsByAppId(application.getId());
        if (appPvList.isEmpty()) {
            throw new CreateKubernetesResourceRuntimeException(applicationName + "未配置存储");
        }
        configVolumeName = applicationName + "-config-volume";
        dataVolumeName = applicationName + "-data-volume";
        // 删除pvc的前缀记录
        PvcName AppPvcName = new PvcName(dataVolumeName, applicationName, application.getInstanceNum());
        pvcList.add(AppPvcName);
        governServiceName = applicationName + "-govern-svc";
        configMapName = applicationName + "-config-map";
        podLabels = new HashMap<>();
        podLabels.put("app", application.getAppIdentify());
    }

    protected V1Service createGovernService() {
        V1Service service = DuplicateCodeUtil.setV1Service("v1","Service",governServiceName,namespaceName,"svc",applicationName + "-govern-svc");
        List<V1ServicePort> spList = DuplicateCodeUtil.setSpList(applicationName,getDefaultInnerPort());
        service.spec(DuplicateCodeUtil.setV1ServiceSpec(spList,podLabels));
        return service;

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
            sp.port(svo.getInnerPort());
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
