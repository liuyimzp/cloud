package com.yinhai.cloud.module.resource.pv.api.constants;

/**
 * @author jianglw
 */
@SuppressWarnings("unused")
public interface PvConstants {

    String PV_TYPE_CODE = "PV_TYPE";
    /**
     * 存储类型：本地
     */
    String PV_TYPE_LOCAL = "0";
    /**
     * 存储类型：远程
     */
    String PV_TYPE_REMOTE = "1";
    /**
     * 存储类型：ceph
     */
    String PV_TYPE_CEPH = "2";

    /**
     * PV启用状态 码表
     */
    String PV_STATE_CODE = "PV_STATE";
    /**
     * PV_STATE:启用
     */
    String PV_STATE_ENABLE = "1";
    /**
     * PV_STATE:停用
     */
    String PV_STATE_DISABLE = "0";

    //pv yaml文件存放路径
    /*String PV_YAML_PATH = "~/PVYaml/";*/
}
