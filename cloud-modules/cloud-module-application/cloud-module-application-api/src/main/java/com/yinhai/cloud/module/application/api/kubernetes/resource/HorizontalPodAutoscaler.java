package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.kubernetes.base.BaseDeployment;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppVersionVo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.models.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liuyi02 on 2019/11/7 0028.
 */
public class HorizontalPodAutoscaler extends BaseDeployment{


    private AppVersionVo appVersionVo = new AppVersionVo();

    public HorizontalPodAutoscaler(Long applicationId) {
        super(applicationId);
        List<AppVersionVo> versionVos = appVersionBpo.getUseVersion(application.getId());
        if (!versionVos.isEmpty()){
            appVersionVo = versionVos.get(0);
        }
    }

    public String createHorizontalPodAutoscaler(String kind){
        AppConfigVo appConfig = appConfigBpo.getAppConfig(application.getId());
        StringBuilder sb = new StringBuilder();
        Double avgCpu = appConfig.getMaxCPU() * 15;
        sb.append("apiVersion: autoscaling/v2beta1").append("\n")
                .append("kind: HorizontalPodAutoscaler").append("\n")
                .append("metadata:").append("\n");
        if (appVersionVo.getVersionName() != null){
            sb.append("  name: hpa-" + applicationName + "v" + appVersionVo.getVersionName()).append("\n");
        }else {
            sb.append("  name: hpa-" + applicationName).append("\n");
        }
        sb.append("  namespace: " + namespaceName).append("\n")
                .append("spec:").append("\n")
                .append("  scaleTargetRef:").append("\n")
                .append("    apiVersion: apps/v1beta2").append("\n")
                .append("    kind: " + kind).append("\n");
        if (appVersionVo.getVersionName() != null){
            sb.append("    name: " + applicationName + "v" + appVersionVo.getVersionName()).append("\n");
        }else {
            sb.append("    name: " + applicationName).append("\n");
        }
        sb.append("  minReplicas: 1").append("\n")
                .append("  maxReplicas: 10").append("\n")
                .append("  metrics:").append("\n")
                .append("  - type: Resource").append("\n")
                .append("    resource:").append("\n")
                .append("      name: cpu").append("\n")
                .append("      targetAverageUtilization: " + avgCpu.intValue()).append("\n");
        return sb.toString();
    }

    @Override
    protected Integer getDefaultInnerPort() {
        return null;
    }
}
