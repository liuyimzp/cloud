package com.yinhai.cloud.module.monitor.constants;

/**
 * Created by pengwei on 2018/8/28.
 */
public class MonitorConstants {
    //集群状态
    public static final Integer CLUSTER_STATUS_NOCREATE = 1; //未创建
    public static final Integer CLUSTER_STATUS_NORMAL = 2; //正常
    public static final Integer CLUSTER_STATUS_ERROR = 3; //异常

    //节点状态
    public static final String NODE_STATUS_READY = "Ready";
    public static final String NODE_STATUS_UNREADY = "NotReady";
    //应用状态

    //获取cpu使用率（用户+系统）,取连续3秒的平均值
    public static final String CPU_AVAILABILITY = "top -n 3 -d 1 | grep '%Cpu'| awk '{sum += ($2 + $4) };END {print sum/3}'";

    //空闲内存：单位G
    public static final String SERVER_MEMORY_USED = "free -m | grep 'Mem:' | awk 'NR==1{print $2/1024}'";

    //获取集群状态
    public static final String GET_CLUSTER_STATUS = "kubectl get cs|awk '{if(NR>1) print $2}'|grep 'Unhealthy'|awk 'END{if(NR==0){ print \"0\"} else {print \"1\"}}'";
    //获取集群节点名称和状态
    public static final String NODENAME_AND_STATUS = "kubectl get nodes|awk '{if(NR>1) print $1\":\"$2}'";

    //获取集群kube信息
    public static final String NODE_KUBE_INFO = "kubectl describe node %s|grep 'OS Image'|awk -F ':' '{print $2}' && kubectl describe node %s|grep 'pods:'|awk -F ':' '{if(NR>1) print $2}' && kubectl describe node %s|grep 'Pods'|awk -F ':' '{print $2}'|tr -cd '0-9'";

    public static final String MONITOR_CLASS_NAME = "com.yinhai.cloud.module.monitor.impl.job.MonitorJob";
}
