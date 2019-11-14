package com.yinhai.cloud.module.application.api.kubernetes.resource;

import com.yinhai.cloud.module.application.api.kubernetes.base.BaseJob;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.core.common.api.util.ValidateUtil;
import io.kubernetes.client.models.*;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyi02 on 2019/4/4
 */
public class CommonJob extends BaseJob {

    public CommonJob(Long applicationId) {
        super(applicationId);
        create();
    }

    private void create() {
        if ("1".equals(application.getAppSchedule())){
            yamlObjectList.add(createCronJob());
        }else{
            yamlObjectList.add(createJob());
        }
    }

    private V1beta1CronJob createCronJob(){

        V1beta1CronJob de = new V1beta1CronJob();
        de.apiVersion("batch/v1beta1");
        de.kind("CronJob");
        // 设置metadata
        V1ObjectMeta deMetadata = new V1ObjectMeta();
        deMetadata.name(applicationName);

        V1beta1CronJobSpec deSpec = new V1beta1CronJobSpec();
        V1beta1JobTemplateSpec jobTemplateSpec = new V1beta1JobTemplateSpec();
        V1JobSpec jobSpec = new V1JobSpec();

        V1PodTemplateSpec podTemplate = new V1PodTemplateSpec();
        V1PodSpec podTemplateSpec = new V1PodSpec();

        List<V1Container> containers = new ArrayList<>();
        V1Container container = new V1Container();
        container.name(applicationName + "-container");
        AppConfigVo appConfig = appConfigBpo.getAppConfig(application.getId());
        String imagePath = appConfig.getAppImagePath();
        container.image(imagePath);
        containers.add(container);
        podTemplateSpec.containers(containers);
        if ("1".equals(application.getAppRestartPolicy())){
            podTemplateSpec.restartPolicy("Never");
        }else if("2".equals(application.getAppRestartPolicy())){
            podTemplateSpec.restartPolicy("OnFailure");
        }
        podTemplate.spec(podTemplateSpec);
//        deMetadata.creationTimestamp(new DateTime(application.getBeginTime()));
//        if (application.getEndTime() != null){
//            deMetadata.deletionTimestamp(new DateTime(application.getEndTime()));
//        }
        jobSpec.template(podTemplate);
        jobTemplateSpec.spec(jobSpec);
        deSpec.schedule(application.getsAppSchedule());
        deSpec.jobTemplate(jobTemplateSpec);
        de.metadata(deMetadata);
        de.spec(deSpec);
        return de;
    }

    private V1Job createJob(){
        V1Job de = new V1Job();
        de.apiVersion("batch/v1");
        de.kind("Job");
        // 设置metadata
        V1ObjectMeta deMetadata = new V1ObjectMeta();
        deMetadata.name(applicationName);

//        deMetadata.namespace(namespaceName);
        de.metadata(deMetadata);

        V1JobSpec deSpec = new V1JobSpec();
        V1PodSpec podTemplateSpec = new V1PodSpec();
        V1PodTemplateSpec podTemplate = new V1PodTemplateSpec();
        V1ObjectMeta podMetadata = new V1ObjectMeta();
        podMetadata.name(applicationName);
        podTemplate.metadata(podMetadata);
        V1Container container = new V1Container();
        container.name(applicationName + "-container");
        AppConfigVo appConfig = appConfigBpo.getAppConfig(application.getId());
        String imagePath = appConfig.getAppImagePath();
        container.image(imagePath);
        List<V1Container> containers = new ArrayList<>();

        //configMap
//        createConfigMap(container,podTemplateSpec);

        containers.add(container);
        podTemplateSpec.containers(containers);
        if ("1".equals(application.getAppRestartPolicy())){
            podTemplateSpec.restartPolicy("Never");
        }else if("2".equals(application.getAppRestartPolicy())){
            podTemplateSpec.restartPolicy("OnFailure");
        }
        podTemplate.spec(podTemplateSpec);
        deSpec.template(podTemplate);
        de.spec(deSpec);
        return de;
    }
    @Override
    protected Integer getDefaultInnerPort() {
        return 8080;
    }
}
