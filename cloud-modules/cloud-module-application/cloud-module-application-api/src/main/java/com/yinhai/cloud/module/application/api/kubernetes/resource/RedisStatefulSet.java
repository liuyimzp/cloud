package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.kubernetes.base.BaseStatefulSet;
import com.yinhai.cloud.module.application.api.kubernetes.configmap.ConfigResourceObject;
import com.yinhai.cloud.module.application.api.util.DuplicateCodeUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import io.kubernetes.client.models.*;


/**
 * @author: zhaokai
 * @create: 2018-09-27 14:29:28
 */
public class RedisStatefulSet extends BaseStatefulSet {
    private static final String conf = "redis.conf";

    private static final Integer defultInnerPort = 6379;

    public RedisStatefulSet(Long appId) {
        super(appId);
        create();
    }

    @Override
    protected Integer getDefaultInnerPort() {
        return defultInnerPort;
    }

    private void create() {
        yamlObjectList.add(createConfigMap());
        yamlObjectList.add(createGovernService());
        yamlObjectList.addAll(createExposeService());
        yamlObjectList.add(createStatefulSet());
    }

    private V1ConfigMap createConfigMap() {
        String bind = "bind *\n" +
                "protected-mode yes\n" +
                "port " + defultInnerPort + "\n" +
                "tcp-backlog 511\n" +
                "timeout 0\n" +
                "tcp-keepalive 300\n" +
                "daemonize no\n" +
                "supervised no\n" +
                "pidfile /var/run/redis_" + defultInnerPort + ".pid\n" +
                "loglevel notice\n" +
                "logfile \"\"\n" +
                "databases 16\n" +
//                "save 900 1\n" +
//                "save 300 10\n" +
//                "save 60 10000\n" +
                "stop-writes-on-bgsave-error yes\n" +
                "rdbcompression yes\n" +
                "rdbchecksum yes\n" +
//                "dbfilename dump.rdb\n" +
                "dir /data\n" +
                "slave-serve-stale-data yes\n" +
                "slave-read-only yes\n" +
                "repl-diskless-sync no\n" +
                "repl-diskless-sync-delay 5\n" +
                "repl-disable-tcp-nodelay no\n" +
                "slave-priority 100\n" +
                "appendonly yes\n" +
                "appendfilename \"appendonly.aof\"\n" +
                "appendfsync everysec\n" +
                "no-appendfsync-on-rewrite no\n" +
                "auto-aof-rewrite-percentage 100\n" +
                "auto-aof-rewrite-min-size 64mb\n" +
                "aof-load-truncated yes\n" +
                "lua-time-limit 5000\n" +
                "slowlog-log-slower-than 10000\n" +
                "slowlog-max-len 128\n" +
                "latency-monitor-threshold 0\n" +
                "notify-keyspace-events \"\"\n" +
                "hash-max-ziplist-entries 512\n" +
                "hash-max-ziplist-value 64\n" +
                "list-max-ziplist-size -2\n" +
                "list-compress-depth 0\n" +
                "set-max-intset-entries 512\n" +
                "zset-max-ziplist-entries 128\n" +
                "zset-max-ziplist-value 64\n" +
                "hll-sparse-max-bytes 3000\n" +
                "activerehashing yes\n" +
                "client-output-buffer-limit normal 0 0 0\n" +
                "client-output-buffer-limit slave 256mb 64mb 60\n" +
                "client-output-buffer-limit pubsub 32mb 8mb 60\n" +
                "hz 10\n" +
                "aof-rewrite-incremental-fsync yes";
        return DuplicateCodeUtil.createConfigMap(configMapName,namespaceName,conf,bind);
    }

    private V1beta1StatefulSet createStatefulSet() {
        ConfigResourceObject crb = new ConfigResourceObject();
        AppConfigVo appConfig = appConfigBpo.getAppConfig(application.getId());
        crb.setResideConfig(applicationName,namespaceName,governServiceName,application.getInstanceNum(),"REDIS",appConfig.getAppImagePath(),10L,appConfig.getMinMemory() + "Gi",appConfig.getMinCPU() + "", appConfig.getMaxMemory() + "Gi",appConfig.getMaxCPU() + "", defultInnerPort,dataVolumeName,"/data",configVolumeName,"/redis-conf","redis.conf",configMapName);
        return DuplicateCodeUtil.createStatefulSet(crb,appPvList,podLabels);
    }
}
