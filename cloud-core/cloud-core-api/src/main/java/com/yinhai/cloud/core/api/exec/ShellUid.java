package com.yinhai.cloud.core.api.exec;

/**
 * @author jianglw
 */
public interface ShellUid {
    /**
     * 脚本UID
     */
    String SHELL_UID = "SHELLUID";
    /**
     * 脚本类型
     */
    String SHELL_CODE_TYPE = "SHELLTYPE";

    String ADD_BASIC_KUBENETES_COMPONENTS = "1";

    String CONFIG_NTP = "2";

    String CREATE_PRIVATE_YUM_REPO = "3";

    String INIT_HIGH_AVAILABLE_API_SERVER = "4";

    String INIT_API_SERVER = "5";

    /**
     * 初始化脚本
     */
    String SHELL_INIT_NODE = "6";

    String INSTALL_DOCKER = "7";

    String INSTALL_EXTERNAL_ETCD_CLUSTER = "8";

    String INSTALL_KUBERNETES = "9";

    String INSTALL_TOOL_DEPENDENCY = "10";

    /**
     * 重置节点 脚本
     */
    String SHELL_K8S_RESET = "11";
    String JOIN_NODE_TO_MASTER = "14";


    String CONFIG_NODES_SSH_NO_PSW = "13";
    String DELETE_CURRENT_NODE = "15";

    String DELETE_ETCD_NODE = "16";

    String BUILD_DIY_JAVA_DOCKER_IMAGE = "17";

    String FUNCTIONS_SH = "18";
    String APP_IMAGE_UPLOAD = "12";
    String INSTALL_DOCKER_BINARY = "19";
    String INSTALL_KUBERNETES_BINARY = "20";
}
