package com.yinhai.cloud.module.application.api.kubernetes.helper;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.kubernetes.base.AppKubernetesYamlObject;
import com.yinhai.cloud.module.application.api.kubernetes.resource.*;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.core.common.api.util.ValidateUtil;

/**
 * @author: zhaokai
 * @create: 2018-09-29 15:38:28
 */
public class AppYamlObjectFactory {

    public static AppKubernetesYamlObject createByApp(AppVo app) throws Exception {
        if (IAppConstants.APP_TYPE_COMMON.equals(app.getAppType()) || IAppConstants.APP_TYPE_HPA.equals(app.getAppType())) {
            //如果是一次性应用
            if (ValidateUtil.isNotEmpty(app.getAppRestartPolicy())){
                return new CommonJob(app.getId());
            }
            if(app.getRecordLog()=='0'){
                return new CommonDeployment(app.getId());
            }
            return new CommonStatefulSet(app.getId());
        }
        AppKubernetesYamlObject yamlObject;
        switch (app.getMiddleWareType()) {
            case IAppConstants.MIDDLEWARE_REDIS:
                yamlObject = new RedisStatefulSet(app.getId());
                break;
            case IAppConstants.MIDDLEWARE_MYSQL:
                yamlObject = new MysqlStatefulSet(app.getId());
                break;

            case IAppConstants.MIDDLEWARE_ACTIVEMQ:
                yamlObject = new ActiveMQDeployment(app.getId());
                break;

            case IAppConstants.MIDDLEWARE_ORACLE_DATABASE:
                yamlObject = new OracleDatabaseStatefulSet(app.getId());
                break;

            case IAppConstants.MIDDLEWARE_NGINX:
                yamlObject = new NginxDeployment(app.getId());
                break;
            case IAppConstants.MIDDLEWARE_ZOOKEEPER:
                yamlObject = new ZookeeperStatefulSet(app.getId());
                break;
            case IAppConstants.MIDDLEWARE_KAFKA:
                yamlObject = new KafkaStatefulSet(app.getId());
                break;
            case IAppConstants.MIDDLEWARE_ELASTIC_SEARCH:
                yamlObject = new ElasticSearchStatefulSet(app.getId());
                break;
            default:
                throw new Exception("尚不支持的中间件类型！");

        }
        return yamlObject;
    }

    public static String createHpa(AppVo app){
        String hpaStr = "";
        HorizontalPodAutoscaler hpa = new HorizontalPodAutoscaler(app.getId());
        if (IAppConstants.APP_TYPE_HPA.equals(app.getAppType())) {
            if(app.getRecordLog()=='0'){
                return "---\n" + hpa.createHorizontalPodAutoscaler("Deployment");
            }
            return "---\n" + hpa.createHorizontalPodAutoscaler("StatefulSet");
        }
        return "";
    }
}
