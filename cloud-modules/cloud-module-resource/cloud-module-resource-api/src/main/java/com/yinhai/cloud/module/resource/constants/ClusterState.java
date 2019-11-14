package com.yinhai.cloud.module.resource.constants;

/**
 * @author: zhaokai
 * @create: 2018-09-30 10:49:12
 */
public interface ClusterState {
    // 未运行
    Integer CLUSTER_NOT_RUNNING = 1;
    // 运行正常
    Integer CLUSTER_RUNNING_SUCCESSFULLY = 2;
    // 运行异常
    Integer CLUSTER_RUNNING_FAILED = 3;


}
