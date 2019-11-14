package com.yinhai.cloud.module.resource.constants;

/**
 * @author zhaokai
 */
public interface ResourceConstant {
    // 服务管理节点
    String NODE_TYPE_MASTER = "0";
    // 部署节点
    String NODE_TYPE_DEPLOY = "1";

    String NTP_NODE_TYPE_MASTER = "master";
    String NTP_NODE_TYPE_SLAVE = "slave";

    String MASTER_ALSO_AS_CHILD = "1";

    Integer NODE_INIT_STEP_FAILED = -1;
    Integer NODE_INIT_STEP_NOT_RUNNING = 0;
    Integer NODE_INIT_STEP_RUNNING = 1;
    Integer NODE_INIT_STEP_SUCCESSFUL = 2;


    Integer NODE_INIT_STATE_FAIL = -3;
    Integer NODE_INIT_STATE_WAIT = -2;
    Integer NODE_INIT_STATE_RUNNING = 0;
    Integer NODE_INIT_STATE_SUCCESS = 10;

    String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String TIME_FORMAT_WITH_NANO = "yyyy-MM-dd HH:mm:ss.SSS";

    Integer NODE_NOT_JOINED = 0;
    Integer NODE_JOINED = 1;


    String DISPLAY_STATE_STYLE_SUCCESS = "success";
    String DISPLAY_STATE_STYLE_WARNING = "warning ";
    String DISPLAY_STATE_STYLE_ERROR = "error";
    String DISPLAY_STATE_STYLE_LOADING = "loading";
    String DISPLAY_STATE_STYLE_NORMAL = "info";
    String DISPLAY_STATE_STYLE_DISABLED = "disabled";

    Integer SHOW_INIT_STEP = 1;
    Integer SHOW_OPERATE_STACK = 2;

    Integer NODE_NOT_IN_NO_PSW = 0;
    Integer NODE_DO_IN_NO_PSW = 1;

    /**
     * session中储存当前登录用户的key
     */
    String USER_INFO_KEY = "ta3.userinfo";
    Integer CLUSTER_IS_BEEN_DELETING = 1;
    Integer CLUSTER_IS_NOT_BEEN_DELETING = 0;

    Integer CLUSTER_ALLOW_DELETE = 1;
    Integer CLUSTER_NOT_ALLOW_DELETE = 0;


    Integer OPERATE_STATE_FAILED = -1;
    Integer OPERATE_STATE_RUNNING = 0;
    Integer OPERATE_STATE_SUCCESSFUL = 1;

    String RUNNING_STEP_HINT = "正在执行中....\n";
}
