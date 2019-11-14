package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.kubernetes.base.BaseDeployment;
import com.yinhai.cloud.module.application.api.vo.AppServiceVo;
import com.yinhai.cloud.module.application.api.vo.AppVersionVo;

import java.util.List;

/**
 * Created by liuyi02 on 2019/10/28.
 * 设置应用版本信息对应
 */
public class DestinationRuleAndVirtualService extends BaseDeployment{

    private static final String FILE_SEPARATOR = "/";
    private List<AppVersionVo> versions;
    public DestinationRuleAndVirtualService(Long applicationId) {
        super(applicationId);
        this.versions = appVersionBpo.getAllByAppId(applicationId);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < exposeServiceVoList.size(); i++) {
            AppServiceVo svo = exposeServiceVoList.get(i);
            Integer port;
            if ((svo.getInnerPort() == null)) {
                port = getDefaultInnerPort();
            } else {
                port = svo.getInnerPort();
            }
            sb.append("apiVersion: networking.istio.io/v1alpha3").append("\n")
                    .append("kind: DestinationRule").append("\n")
                    .append("metadata:").append("\n")
                    .append("  name: " + applicationName).append("\n")
                    .append("  namespace: " + namespaceName).append("\n")
                    .append("spec:").append("\n")
                    .append("  host: " + applicationName + "-expose-svc-" + (i + 1)).append("\n")
                    .append("  subsets:").append("\n");
            for (AppVersionVo version : versions){
                sb.append("  - name: " + version.getVersionNameStr()).append("\n")
                        .append("    labels:").append("\n")
                        .append("      version: " + version.getVersionNameStr()).append("\n");
            }
            sb.append("---").append("\n");
            sb.append("apiVersion: networking.istio.io/v1alpha3").append("\n")
                    .append("kind: VirtualService").append("\n")
                    .append("metadata:").append("\n")
                    .append("  name: " + applicationName).append("\n")
                    .append("  namespace: " + namespaceName).append("\n")
                    .append("spec:").append("\n")
                    .append("  hosts:").append("\n")
                    .append("  - \"*\"").append("\n")
                    .append("  gateways:").append("\n")
                    .append("  - app-" + namespaceName).append("\n")
                    .append("  http:").append("\n")
                    .append("  - match:").append("\n")
                    .append("    - uri:").append("\n")
                    .append("        prefix: /" + applicationName + "/").append("\n")
                    .append("    route:").append("\n");
            for (AppVersionVo version : versions){
                sb.append("    - destination:").append("\n")
                        .append("        host: " + applicationName + "-expose-svc-" + (i + 1)).append("\n")
                        .append("        port:").append("\n")
                        .append("          number: " + port).append("\n")
                        .append("        subset: " + version.getVersionNameStr()).append("\n");
                if (IAppConstants.APPVERSION_IS_USE.equals(version.getIsuse())){
                    sb.append("      weight: 100").append("\n");
                }else {
                    sb.append("      weight: 0").append("\n");
                }
            }
            if (i != exposeServiceVoList.size() - 1){
                sb.append("---").append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    protected Integer getDefaultInnerPort() {
        return 8080;
    }
}
