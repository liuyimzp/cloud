package com.yinhai.cloud.module.resource.constants;

import com.yinhai.cloud.core.api.util.SystemConfigUtil;

/**
 * Created by pengwei on 2018/5/28.
 */
public class ServerCmdConstant {

    //安装NFS脚本名称
    public static final String INSTALL_NFS_SHELLNAME = "installNfs.sh";
    //NFS安装脚本存放目录
    public static final String INSTALL_SHELL_DIR = "/nfsInstall";
    //nfsclient provisioner默认安装命名空间
    public static final String NFS_CLIENT_DEFAULT_NAMESPACES = "default";
    //nfsclient provisioner yaml服务器临时存放目录
    public static final String NFS_CLIENT_PROV_YAML_DIR = "/nfsProvisioner/";
    //nfsclient provisioner yaml meta_name前缀
    public static final String NFS_YAML_PREFIX = "nfsprov-";
    //provisioner本地模板文件目录
    public static final String NFS_YAML_TEMPLATE_DIR = "yaml";
    /**
     * prometheusyaml在管理节点存放相对目录
     */
    public static final String PROMETHEUS_RELA_PATH = "dependency/conf/k8s/prometheus";

    //nfs yaml模板带替换的配置参数
    public static final String NFS_PARAM_METANAME = "\\$\\{meta_name\\}";
    public static final String NFS_PARAM_NAMESPACE = "\\$\\{namespace\\}";
    public static final String NFS_PARAM_NFS_IP = "\\$\\{nfs_server_ip\\}";
    public static final String NFS_PARAM_NFS_PATH = "\\$\\{nfs_server_path\\}";
    public static final String NFS_PARAM_REPERTORY_URL = "\\$\\{docker_repertory_url\\}";

    //nfs yaml模板文件名
    public static final String NFS_TEMPLATE_SA = "serviceaccount.yaml";
    public static final String NFS_TEMPLATE_STORAGECLASS = "storageclass.yaml";
    public static final String NFS_TEMPLATE_CLUSTERROLEBIND = "clusterrolebind.yaml";
    public static final String NFS_TEMPLATE_PROVISIONER = "nfsclientprov.yaml";

    public static final String BACKUP_DIRECTORY_SUFFIX = "_\"`date '+%Y%m%d_%H%M%S'`\"";

    //ceph节点类型
    public static final String CEPH_MON_NOD_NUM = "0";
    public static final String CEPH_OSD_NOD_NUM = "1";

    //超级管理员id
    public static final Long ADMIN_ID = 1L;

    public static final String PV_TYPE_NFS= "1";
    public static final String PV_TYPE_CEPH= "2";

    public static final String CEPH_TEMPLATE_STORAGECLASS = "storageclass.yaml";

    //文件分隔符
    public static final String FILE_SEPARATOR = "/";
}
