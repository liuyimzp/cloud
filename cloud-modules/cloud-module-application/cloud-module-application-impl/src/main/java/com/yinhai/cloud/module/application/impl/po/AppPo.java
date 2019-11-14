package com.yinhai.cloud.module.application.impl.po;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.po.BasePo;

import java.util.Date;

/**
 * Created by tianhy on 2018/6/12.
 * 应用数据库对象
 */
public class AppPo extends BasePo {
    private static final long serialVersionUID = -7567128364910615065L;

    // 序列号（主键）
    private Long id;
    // 集群ID
    private Long clusterId;
    // 命名空间ID
    private Long namespaceId;
    // 应用名称
    private String appName;
    // 中间件类型
    private String middleWareType;
    // 应用状态(未发布，未启动，运行中)
    private String appStatus;
    // 应用标识
    private String appIdentify;
    // 实例数
    private Integer instanceNum;
    // 应用升级策略
    private String appStrategy;
    // 应用描述
    private String appDesc;
    // 应用类型
    private String appType;
    // 创建人
    private Long createUser;
    // 创建时间
    private Date createTime;
    //记录日志标志
    private char recordLog;
    //自定义服务文件
    private String appDiyFile;
    //服务重启条件
    private String appRestartPolicy;

    //是否设置周期（一次性应用）
    private String appSchedule;

    //运行周期
    private String sAppSchedule;

    //开始时间
    private Date beginTime;

    private String middleware_configfile;

    private Long nodeId;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getMiddleware_configfile() {
        return middleware_configfile;
    }

    public void setMiddleware_configfile(String middleware_configfile) {
        this.middleware_configfile = middleware_configfile;
    }

    public Date getBeginTime() {
        if (!ValidateUtil.isEmpty(beginTime)){
            return (Date) beginTime.clone();
        }
        return null;
    }

    public void setBeginTime(Date beginTime) {
        if (!ValidateUtil.isEmpty(beginTime)){
            this.beginTime = (Date) beginTime.clone();
        }
    }

    public String getAppSchedule() {
        return appSchedule;
    }

    public void setAppSchedule(String appSchedule) {
        this.appSchedule = appSchedule;
    }

    public String getsAppSchedule() {
        return sAppSchedule;
    }

    public void setsAppSchedule(String sAppSchedule) {
        this.sAppSchedule = sAppSchedule;
    }

    public String getAppDiyFile() {
        return appDiyFile;
    }

    public void setAppDiyFile(String appDiyFile) {
        this.appDiyFile = appDiyFile;
    }

    public String getAppRestartPolicy() {
        return appRestartPolicy;
    }

    public void setAppRestartPolicy(String appRestartPolicy) {
        this.appRestartPolicy = appRestartPolicy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public Long getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Long namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMiddleWareType() {
        return middleWareType;
    }

    public void setMiddleWareType(String middleWareType) {
        this.middleWareType = middleWareType;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getAppIdentify() {
        return appIdentify;
    }

    public void setAppIdentify(String appIdentify) {
        this.appIdentify = appIdentify;
    }

    public Integer getInstanceNum() {
        return instanceNum;
    }

    public void setInstanceNum(Integer instanceNum) {
        this.instanceNum = instanceNum;
    }

    public String getAppStrategy() {
        return appStrategy;
    }

    public void setAppStrategy(String appStrategy) {
        this.appStrategy = appStrategy;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        if (!ValidateUtil.isEmpty(createTime)){
            return (Date) createTime.clone();
        }
        return null;
    }

    public void setCreateTime(Date createTime) {
        if (!ValidateUtil.isEmpty(createTime)){
            this.createTime = (Date) createTime.clone();
        }
    }

    public char getRecordLog() {
        return recordLog;
    }

    public void setRecordLog(char recordLog) {
        this.recordLog = recordLog;
    }

}
