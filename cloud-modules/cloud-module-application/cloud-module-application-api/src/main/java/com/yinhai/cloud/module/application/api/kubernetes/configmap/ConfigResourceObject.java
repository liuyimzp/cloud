package com.yinhai.cloud.module.application.api.kubernetes.configmap;

public class ConfigResourceObject {
    private String applicationName;
    private String namespaceName;
    private String governServiceName;
    private Integer instanceNum;
    private String type;
    private String imagePath;
    private long periodSeconds;
    private String DEFAULT_MEMORY_REQUEST;
    private String DEFAULT_CPU_REQUEST;
    private String DEFAULT_MEMORY_LIMIT;
    private String DEFAULT_CPU_LIMIT;
    private Integer defultInnerPort;
    private String dataVolumeName;
    private String mountPath;
    private String configVolumeName;
    private String setmountPath;
    private String conf;
    private String configMapName;
    private String confName = null;
    private String confPassword = null;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getGovernServiceName() {
        return governServiceName;
    }

    public void setGovernServiceName(String governServiceName) {
        this.governServiceName = governServiceName;
    }

    public Integer getInstanceNum() {
        return instanceNum;
    }

    public void setInstanceNum(Integer instanceNum) {
        this.instanceNum = instanceNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getPeriodSeconds() {
        return periodSeconds;
    }

    public void setPeriodSeconds(long periodSeconds) {
        this.periodSeconds = periodSeconds;
    }

    public String getDEFAULT_MEMORY_REQUEST() {
        return DEFAULT_MEMORY_REQUEST;
    }

    public void setDEFAULT_MEMORY_REQUEST(String DEFAULT_MEMORY_REQUEST) {
        this.DEFAULT_MEMORY_REQUEST = DEFAULT_MEMORY_REQUEST;
    }

    public String getDEFAULT_CPU_REQUEST() {
        return DEFAULT_CPU_REQUEST;
    }

    public void setDEFAULT_CPU_REQUEST(String DEFAULT_CPU_REQUEST) {
        this.DEFAULT_CPU_REQUEST = DEFAULT_CPU_REQUEST;
    }

    public String getDEFAULT_MEMORY_LIMIT() {
        return DEFAULT_MEMORY_LIMIT;
    }

    public void setDEFAULT_MEMORY_LIMIT(String DEFAULT_MEMORY_LIMIT) {
        this.DEFAULT_MEMORY_LIMIT = DEFAULT_MEMORY_LIMIT;
    }

    public String getDEFAULT_CPU_LIMIT() {
        return DEFAULT_CPU_LIMIT;
    }

    public void setDEFAULT_CPU_LIMIT(String DEFAULT_CPU_LIMIT) {
        this.DEFAULT_CPU_LIMIT = DEFAULT_CPU_LIMIT;
    }

    public Integer getDefultInnerPort() {
        return defultInnerPort;
    }

    public void setDefultInnerPort(Integer defultInnerPort) {
        this.defultInnerPort = defultInnerPort;
    }

    public String getDataVolumeName() {
        return dataVolumeName;
    }

    public void setDataVolumeName(String dataVolumeName) {
        this.dataVolumeName = dataVolumeName;
    }

    public String getMountPath() {
        return mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }

    public String getConfigVolumeName() {
        return configVolumeName;
    }

    public void setConfigVolumeName(String configVolumeName) {
        this.configVolumeName = configVolumeName;
    }

    public String getSetmountPath() {
        return setmountPath;
    }

    public void setSetmountPath(String setmountPath) {
        this.setmountPath = setmountPath;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public String getConfigMapName() {
        return configMapName;
    }

    public void setConfigMapName(String configMapName) {
        this.configMapName = configMapName;
    }

    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    //配置redisStatefulSet的文件
    public void setResideConfig(String applicationName, String namespaceName, String governServiceName, Integer instanceNum, String type, String imagePath, long periodSeconds, String DEFAULT_MEMORY_REQUEST, String DEFAULT_CPU_REQUEST, String DEFAULT_MEMORY_LIMIT, String DEFAULT_CPU_LIMIT, Integer defultInnerPort, String dataVolumeName, String mountPath, String configVolumeName, String setmountPath, String conf, String configMapName){
        this.applicationName = applicationName;
        this.namespaceName = namespaceName;
        this.governServiceName = governServiceName;
        this.instanceNum = instanceNum;
        this.type = type;
        this.imagePath = imagePath;
        this.periodSeconds = periodSeconds;
        this.DEFAULT_MEMORY_REQUEST = DEFAULT_MEMORY_REQUEST;
        this.DEFAULT_CPU_REQUEST = DEFAULT_CPU_REQUEST;
        this.DEFAULT_MEMORY_LIMIT = DEFAULT_MEMORY_LIMIT;
        this.DEFAULT_CPU_LIMIT = DEFAULT_CPU_LIMIT;
        this.defultInnerPort = defultInnerPort;
        this.dataVolumeName = dataVolumeName;
        this.mountPath = mountPath;
        this.configVolumeName = configVolumeName;
        this.setmountPath = setmountPath;
        this.conf = conf;
        this.configMapName = configMapName;
    }

    //配置Mysql的文件
    public void setMysqlConfig(String applicationName, String namespaceName, String governServiceName, Integer instanceNum, String type, String imagePath, long periodSeconds, String DEFAULT_MEMORY_REQUEST, String DEFAULT_CPU_REQUEST, String DEFAULT_MEMORY_LIMIT, String DEFAULT_CPU_LIMIT, Integer defultInnerPort, String dataVolumeName, String mountPath, String configVolumeName, String setmountPath, String conf, String configMapName, String confName,String confPassword){
        setResideConfig(applicationName,namespaceName,governServiceName,instanceNum,type,imagePath,periodSeconds,DEFAULT_MEMORY_REQUEST,DEFAULT_CPU_REQUEST,DEFAULT_MEMORY_LIMIT,DEFAULT_CPU_LIMIT,defultInnerPort, dataVolumeName,mountPath,configVolumeName,setmountPath,conf,configMapName);
        this.confName = confName;
        this.confPassword = confPassword;
    }
}
