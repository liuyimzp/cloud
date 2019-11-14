package com.yinhai.cloud.module.monitor.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.monitor.api.vo.QuartzJobVo;
import com.yinhai.cloud.module.monitor.impl.job.MonitorJob;
import com.yinhai.core.app.api.util.JSonFactory;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.exception.AppException;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.modules.schedul.api.bpo.JavaJobDetailsBlo;
import com.yinhai.modules.schedul.api.vo.JavaJobDetailsVo;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengwei on 2018/8/29.
 */
@Controller
@RequestMapping("/monitorConfig")
public class MonitorJobConfigController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MonitorJobConfigController.class);

    @Resource(name = "clusterJobScheduler")
    private Scheduler scheduler;
    @Resource
    private JavaJobDetailsBlo javaJobDetailsBloImpl;

    @RequestMapping("/getAllJob.do")
    @ResponseBody
    public CommonResultVo getAllJob() {
        CommonResultVo resultVo = new CommonResultVo();
        List<JavaJobDetailsVo> jobList = javaJobDetailsBloImpl.getJavaJobList();
        resultVo.setSuccess(true);
        resultVo.setData(jobList);

        return resultVo;
    }

    @RequestMapping("/addMonitorJob.do")
    @ResponseBody
    public CommonResultVo addMonitorJob(final @RequestBody QuartzJobVo vo, ServletRequest request) throws Exception {
        CommonResultVo resultVo = new CommonResultVo();
        if (scheduler.isShutdown()) {
            this.scheduler = ((Scheduler) ServiceLocator.getService("clusterJobScheduler"));
        }

        String jobName = vo.getJobName();
        String jobGroup = vo.getJobGroup();
        String jobDesc = vo.getJobDesc();
        String triggerName = "tri_" + jobName;
        String triggerGroup = vo.getJobGroup();
        Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(vo.getStartTime());

        Date endTime = null;
        if (!ValidateUtil.isEmpty(vo.getEndTime())) {
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(vo.getEndTime());
        }
        Integer repeatCount = vo.getRepeatCount();
        Integer repeatInterval = vo.getRepeatInterval();
        String triggerType = vo.getTriggerType();
        String cronExpression = vo.getCronExpression();
        String jobData = vo.getJobData();
        String isPause = vo.getIsPause();
        try {
            Trigger trigger = null;
            if ("1".equals(triggerType)) {
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerName, triggerGroup)
                        .startAt(startTime)
                        .endAt(endTime)
                        .withSchedule(
                                SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(repeatInterval
                                        .intValue()).withRepeatCount(repeatCount
                                        .intValue()).withMisfireHandlingInstructionNextWithExistingCount()).build();
            } else {
                trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
                        .startAt(startTime)
                        .endAt(endTime)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                                .withMisfireHandlingInstructionDoNothing()).build();
            }

            JobDetail jobDetail = JobBuilder.newJob(MonitorJob.class)
                    .withIdentity(jobName, jobGroup)
                    .withDescription(jobDesc)
                    .build();

            Map map = new HashMap();
            if (!ValidateUtil.isEmpty(jobData)) {
                map = (Map) JSonFactory.json2bean(jobData, HashMap.class);
            }
            map.put("isPause", isPause);
            jobDetail.getJobDataMap().putAll(map);

            this.scheduler.scheduleJob(jobDetail, trigger);
        } catch (ObjectAlreadyExistsException e1) {
            logger.error(logger.getName() + "context",e1);
            throw new AppException("任务：" + jobName + "已经存在！");
        }
        List<JavaJobDetailsVo> jobList = javaJobDetailsBloImpl.getJavaJobList();
        resultVo.setSuccess(true);
        resultVo.setData(jobList);

        return resultVo;
    }

    @RequestMapping("/pauseJob.do")
    @ResponseBody
    public CommonResultVo pauseJob(final @RequestBody JavaJobDetailsVo vo, ServletRequest request) throws Exception {
        CommonResultVo resultVo = new CommonResultVo();
        if (this.scheduler.isShutdown()) {
            this.scheduler = ((Scheduler) ServiceLocator.getService("clusterJobScheduler"));
        }

        this.scheduler.pauseTrigger(new TriggerKey(vo.getTriggerName(), vo.getTriggerGroup()));
        List<JavaJobDetailsVo> jobList = javaJobDetailsBloImpl.getJavaJobList();
        resultVo.setSuccess(true);
        resultVo.setData(jobList);

        return resultVo;
    }

    @RequestMapping("/removeJob.do")
    @ResponseBody
    public CommonResultVo removeJob(final @RequestBody JavaJobDetailsVo vo, ServletRequest request) throws Exception {
        CommonResultVo resultVo = new CommonResultVo();
        if (this.scheduler.isShutdown()) {
            this.scheduler = ((Scheduler) ServiceLocator.getService("clusterJobScheduler"));
        }

        this.scheduler.unscheduleJob(new TriggerKey(vo.getTriggerName(), vo.getTriggerGroup()));
        List<JavaJobDetailsVo> jobList = javaJobDetailsBloImpl.getJavaJobList();
        resultVo.setSuccess(true);
        resultVo.setData(jobList);

        return resultVo;
    }

    @RequestMapping("/resumeJob.do")
    @ResponseBody
    public CommonResultVo resumeJob(final @RequestBody JavaJobDetailsVo vo, ServletRequest request) throws Exception {
        CommonResultVo resultVo = new CommonResultVo();
        if (this.scheduler.isShutdown()) {
            this.scheduler = ((Scheduler) ServiceLocator.getService("clusterJobScheduler"));
        }

        this.scheduler.resumeTrigger(new TriggerKey(vo.getTriggerName(), vo.getTriggerGroup()));
        List<JavaJobDetailsVo> jobList = javaJobDetailsBloImpl.getJavaJobList();
        resultVo.setSuccess(true);
        resultVo.setData(jobList);

        return resultVo;
    }


}
