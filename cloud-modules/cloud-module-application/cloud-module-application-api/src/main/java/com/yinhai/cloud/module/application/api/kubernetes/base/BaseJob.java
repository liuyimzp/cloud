package com.yinhai.cloud.module.application.api.kubernetes.base;

import com.yinhai.cloud.module.application.api.exception.CreateKubernetesResourceRuntimeException;
import com.yinhai.cloud.module.application.api.vo.AppServiceVo;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServicePort;
import io.kubernetes.client.models.V1ServiceSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一次性应用
 * @author: liuyi02
 * @create: 2019/4/4
 */
public abstract class BaseJob extends AppKubernetesYamlObject {

    public BaseJob(Long applicationId) {
        this.application = appBpo.getApp(applicationId);
        createBasicInfo();
    }

    private void createBasicInfo() {
        applicationName = application.getAppIdentify();
        NamespaceVo namespace = namespaceBpo.getNamespace(application.getNamespaceId());
        if (namespace == null) {
            throw new CreateKubernetesResourceRuntimeException(application.getAppName() + " 所属命名空间丢失");
        }
        configVolumeName = applicationName + "-volume";
        configMapName = applicationName + "-config-map";
        podLabels = new HashMap<>();
        podLabels.put("app", application.getAppIdentify());
    }

}
