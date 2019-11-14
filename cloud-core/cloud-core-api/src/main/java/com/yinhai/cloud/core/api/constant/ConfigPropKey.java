package com.yinhai.cloud.core.api.constant;

/**
 * @author: zhaokai
 * @create: 2018-08-21 10:05:05
 */
public interface ConfigPropKey {

    String PRIVATE_YUM_REPO_IP = "yum.private.repo";
    String PRIVATE_DOCKER_REPO_IP = "docker.private.repo.ip";
    String K8S_INSTALL_SERVER_ROOT = "k8s.server.root";
    String DOCKER_IMAGE_BUILD_MAIN_DIR_PATH = "docker.application.build.main.dir.path";
    String IMAGE_UPLOAD_DIR_PATH = "docker.image.upload.dir.path";

    String DIY_IMAGE_UNSUPPORTED_PATH_PREFIX = "image.unsupported.path";

    String CMD_UPLOAD_CONF_PATH = "upload.conf.path";
    String CMD_UPLOAD_SH_PATH = "upload.sh.path";

    String HAPROXY_CONFIG_FILE_PATH = "haproxy.config.path";
    String KEEPALIVED_CONFIG_FILE_PATH = "keepalived.config.path";
    String CHECK_HAPROXY_FILE_PATH = "keepalived.check.haproxy.path";
}
