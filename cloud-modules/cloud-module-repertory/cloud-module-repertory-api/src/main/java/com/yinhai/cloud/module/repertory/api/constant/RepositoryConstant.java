package com.yinhai.cloud.module.repertory.api.constant;

/**
 * @author: zhaokai
 * @create: 2018-07-31 17:45:50
 */
public interface RepositoryConstant {

    String WAR_FILE_SUFFIX = "war";
    String EAR_FILE_SUFFIX = "ear";
    String JAR_FILE_SUFFIX = "jar";


    String UPLOAD_FILE_NAME_TIME_FORMAT = "yyyyMMddHHmmssSSS";

    String DOCKER_IMAGE_TYPE_ORIGINAL = "1";
    String DOCKER_IMAGE_TYPE_DIY = "2";

    String IMAGE_BUILD_DIR_PREFIX = "build-image";

    // 固定自定义镜像应用上传的文件夹
    String DOCKER_APPLICATION_UPLOAD_SERVER_DIR_NAME = "uploadApplication";
}
