package com.yinhai.cloud.module.application.api.constant;

/**
 * 应用常量类
 * Created by tianhy on 2018/6/14.
 */
public interface IAppConstants {
    // 应用升级策略---覆盖
    public static String APP_STRATEGY_COVER = "1";
    // 应用升级策略---滚动
    public static String APP_STRATEGY_ROLL = "2";
    // 应用状态---未发布
    public static String APP_STATUS_UNRELEASED = "1";
    // 应用状态---未启动
    public static String APP_STATUS_UNSTART = "2";
    // 应用状态---运行中
    public static String APP_STATUS_RUNNING = "3";
    // 应用状态---已停止
    public static String APP_STATUS_STOP = "4";
    // 应用类型---普通应用
    public static String APP_TYPE_COMMON = "1";
    // 应用类型---常用中间件
    public static String APP_TYPE_MIDDLEWARE = "2";
    // 应用类型---自定义应用
    public static String APP_TYPE_CUSTOM = "3";
    // 应用类型---hpa应用
    public static String APP_TYPE_HPA = "4";
    // 默认最小CPU
    public static Double CPU_MIN_DEFAULT = 0.5;
    // 默认最大CPU
    public static Double CPU_MAX_DEFAULT = 2.0;
    // 默认最小内存
    public static Double MEMORY_MIN_DEFAULT = 0.5;
    // 默认最大内存
    public static Double MEMORY_MAX_DEFAULT = 8.0;
    // 探针检测类型——http
    public static String PROBE_CHECKTYPE_HTTP = "httpGet";
    // 探针检测类型——commond
    public static String PROBE_CHECKTYPE_COMMOND = "exec";
    // 探针检测类型——tcp
    public static String PROBE_CHECKTYPE_TCP = "tcpSocket";

    String SERVICE_TYPE_INNER = "0";

    String SERVICE_TYPE_OUTER = "1";

    // 资源类型--集群
    String RESOURCE_TYPE_CLUSTER = "1";
    // 资源类型--命名空间
    String RESOURCE_TYPE_NAMESPACE = "2";
    // 资源类型--业务领域
    String RESOURCE_TYPE_BUSINESSAREA = "3";
    // 资源类型--应用组
    String RESOURCE_TYPE_APPGROUP = "4";

    // zookeeper最小cpu配置
    Double ZOOKEEPER_DEFAULT_CPU_REQUEST = 0.2;
    // zookeeper最小内存配置
    Double ZOOKEEPER_DEFAULT_MEMORY_REQUEST = 0.8;
    // zookeeper最大cpu配置
    Integer ZOOKEEPER_DEFAULT_CPU_LIMIT = 1;
    // zookeeper最大内存配置
    Integer ZOOKEEPER_DEFAULT_MEMORY_LIMIT = 2;
    // redis最小cpu配置
    Double REDIS_DEFAULT_CPU_REQUEST = 0.5;
    // redis最小内存配置
    Integer REDIS_DEFAULT_MEMORY_REQUEST = 1;
    // redis最大cpu配置
    Integer REDIS_DEFAULT_CPU_LIMIT = 2;
    // redis最大内存配置
    Integer REDIS_DEFAULT_MEMORY_LIMIT = 4;
    // mysql最小cpu配置
    Double MYSQL_DEFAULT_CPU_REQUEST = 0.5;
    // mysql最小内存配置
    Integer MYSQL_DEFAULT_MEMORY_REQUEST = 1;
    // mysql最大cpu配置
    Integer MYSQL_DEFAULT_CPU_LIMIT = 2;
    // mysql最大内存配置
    Integer MYSQL_DEFAULT_MEMORY_LIMIT = 4;
    // activemq最大cpu配置
    Integer ACTIVEMQ_DEFAULT_CPU_LIMIT = 1;
    // activemq最大内存配置
    Integer ACTIVEMQ_DEFAULT_MEMORY_LIMIT = 2;
    // oracle最小cpu配置
    Double ORACLE_DEFAULT_CPU_REQUEST = 0.5;
    // oracle最小内存配置
    Integer ORACLE_DEFAULT_MEMORY_REQUEST = 2;
    // oracle最大cpu配置
    Integer ORACLE_DEFAULT_CPU_LIMIT = 4;
    // oracle最大内存配置
    Integer ORACLE_DEFAULT_MEMORY_LIMIT = 10;
    // nginx最大cpu配置
    Integer NGINX_DEFAULT_CPU_LIMIT = 1;
    // nginx最大内存配置
    Integer NGINX_DEFAULT_MEMORY_LIMIT = 2;
    // kafka最小cpu配置
    Double KAFKA_DEFAULT_CPU_REQUEST = 0.5;
    // kafka最小内存配置
    Integer KAFKA_DEFAULT_MEMORY_REQUEST = 1;
    // kafka最大cpu配置
    Integer KAFKA_DEFAULT_CPU_LIMIT = 1;
    // kafka最大内存配置
    Integer KAFKA_DEFAULT_MEMORY_LIMIT = 2;
    // elasticsearch最小cpu配置
    Double ELASTICSEARCH_DEFAULT_CPU_REQUEST = 0.5;
    // elasticsearch最小内存配置
    Integer ELASTICSEARCH_DEFAULT_MEMORY_REQUEST = 1;
    // elasticsearch最大cpu配置
    Integer ELASTICSEARCH_DEFAULT_CPU_LIMIT = 2;
    // elasticsearch最大内存配置
    Integer ELASTICSEARCH_DEFAULT_MEMORY_LIMIT = 4;

    String IMAGE_TYPE_UPLOAD = "1";
    String IMAGE_TYPE_DIY = "2";

    String MIDDLEWARE_ZOOKEEPER = "1";
    String MIDDLEWARE_REDIS = "2";
    String MIDDLEWARE_MYSQL = "3";
    String MIDDLEWARE_ACTIVEMQ = "4";
    String MIDDLEWARE_ORACLE_DATABASE = "5";
    String MIDDLEWARE_NGINX = "6";
    String MIDDLEWARE_KAFKA = "7";
    String MIDDLEWARE_ELASTIC_SEARCH = "8";

    String YAML_DIR_NAME = "yaml";
    String IMAGE_UPLOAD_DIR_NAME = "imageUpload";

    String MIDDLE_WARE_TYPE="MIDDLEWARETYPE";

    /**
     * 日志查询方式-按照时间
     */
    String POD_LOG_SINCE="since";
    /**
     * 日志查询方式-按照行数
     */
    String POD_LOG_TAIL="tail";

    String BACK_UP_CONFIG = "1";//备份存在配置文件
    String BACK_UP_NO_CONFIG = "0";//备份不存在配置文件

    String APPVERSION_IS_USE = "1";//应用版本被使用
    String APPVERSION_NO_USE = "0";//应用版本未被使
    String APPVERSION_ACTION = "1";//应用镜像被启动中
    String APPVERSION_NO_ACTION = "0";//应用镜像未启动
}
