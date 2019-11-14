package com.yinhai.cloud.core.api.util;

/**
 * Created by pengwei on 2018/8/6.
 */
public class RestConstants {

    //集群状态判断
    public static final String CLUSTER_STATUS = "/healthz";

    //节点状态判断
    public static final String NODES_STATUS = "/api/v1/nodes";

    //应用状态判断
    public static final String APP_STATUS = "/apis/apps/v1/namespaces/%s/deployments/%s";
}
