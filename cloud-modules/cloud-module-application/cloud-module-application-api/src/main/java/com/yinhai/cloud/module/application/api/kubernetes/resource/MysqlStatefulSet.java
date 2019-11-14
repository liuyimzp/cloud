package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.core.api.util.SystemConfigUtil;
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
public class MysqlStatefulSet extends BaseStatefulSet {
    private static final String MYSQL_ROOT_PASSWORD = SystemConfigUtil.getSystemConfigValue("MYSQL_PASSWORD");
    private static final String MYSQL_ROOT_NAME = "root";

    private static final String conf = "mysql.cnf";

    private static final String bind = "[mysqld]\n" +
            "    port=3306\n" +
            "    datadir=/mysql-data-lastest\n" +
            "    innodb_fast_shutdown";
    public MysqlStatefulSet(Long appId) {
        super(appId);
        create();
    }

    private void create() {
        yamlObjectList.add(createConfigMap());
        yamlObjectList.add(createGovernService());
        yamlObjectList.addAll(createExposeService());
        yamlObjectList.add(createStatefulSet());
    }


    private V1ConfigMap createConfigMap() {
        return DuplicateCodeUtil.createConfigMap(configMapName,namespaceName,conf,bind);
    }

    private V1beta1StatefulSet createStatefulSet() {
        ConfigResourceObject crb = new ConfigResourceObject();
        AppConfigVo appConfig = appConfigBpo.getAppConfig(application.getId());
        crb.setMysqlConfig(applicationName,namespaceName,governServiceName,application.getInstanceNum(),"MYSQL",appConfig.getAppImagePath(),10L,appConfig.getMinMemory() + "Gi", appConfig.getMinCPU() + "",appConfig.getMaxMemory() + "Gi",appConfig.getMaxCPU() + "", 3306,dataVolumeName,"/mysql-data-lastest",configVolumeName,"/etc/mysql/mysql.conf.d","mysql.cnf",configMapName,"MYSQL_ROOT_PASSWORD",MYSQL_ROOT_PASSWORD);
        return DuplicateCodeUtil.createStatefulSet(crb,appPvList,podLabels);
    }


    @Override
    protected Integer getDefaultInnerPort() {
        return 3306;
    }
}
