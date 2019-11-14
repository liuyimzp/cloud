package com.yinhai.cloud.module.application.api.kubernetes.gateway;

import com.yinhai.cloud.module.application.api.kubernetes.base.AppKubernetesYamlObject;

/**
 * @author: liuyi02
 * @create: 2019-11-01
 */
public class GatewayYamlObject extends AppKubernetesYamlObject {

    private String namespaceName;

    private String yamlBuildStr;

    public GatewayYamlObject(String namespaceName) {
        this.namespaceName = namespaceName;
        create();
    }

    private void create() {
        StringBuilder sb = new StringBuilder();
        sb.append("apiVersion: networking.istio.io/v1alpha3").append("\n")
                .append("kind: Gateway").append("\n")
                .append("metadata:").append("\n")
                .append("  name: app-" + namespaceName).append("\n")
                .append("  namespace: " + namespaceName).append("\n")
                .append("spec:").append("\n")
                .append("  selector:").append("\n")
                .append("    istio: ingressgateway").append("\n")
                .append("  servers:").append("\n")
                .append("  - port:").append("\n")
                .append("      number: 80").append("\n")
                .append("      name: http").append("\n")
                .append("      protocol: HTTP").append("\n")
                .append("    hosts:").append("\n")
                .append("    - \"*\"").append("\n");
        this.yamlBuildStr = sb.toString();
    }

    public String getYaml(){
        return this.yamlBuildStr;
    }
    @Override
    protected Integer getDefaultInnerPort() {
        return null;
    }
}
