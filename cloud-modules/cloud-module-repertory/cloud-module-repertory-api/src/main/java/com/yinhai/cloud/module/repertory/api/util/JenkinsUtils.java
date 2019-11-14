package com.yinhai.cloud.module.repertory.api.util;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.DownloadFileCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.constant.JenkinsConstant;
import com.yinhai.cloud.module.repertory.api.vo.JenkinsVo;
import com.yinhai.cloud.module.resource.constants.DownloadState;
import com.yinhai.cloud.module.resource.task.api.AbstractOperateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: liuyi02
 */
public class JenkinsUtils {
    private final static Logger logger = LoggerFactory.getLogger(JenkinsUtils.class);
    private static JenkinsServer jenkinsServer;
    static {
        try {
            jenkinsServer = new JenkinsServer(new URI("http://192.168.247.129:8080/"), "admin", "332d9f61d4d44182a8d13dbd9e2502fb");
        } catch (URISyntaxException e) {
            logger.error("获取jenkins连接失败，请检查链接，用户名，密码是否正确");
        }
    }

    public static JenkinsServer getJenkinsServer(){
        return jenkinsServer;
    }

    //
    public static List<JenkinsVo> getJobs() throws Exception{
        List<JenkinsVo> list = new ArrayList<>();
        JenkinsServer jenkins = JenkinsUtils.getJenkinsServer();
        String aaa = jenkins.getJobXml("asdasd");
        String aaaa = jenkins.getJobXml("gitTest");
        Map<String, Job> jobs = jenkins.getJobs();
        for (Map.Entry<String, Job> entry : jobs.entrySet()) {
            try {
                JobWithDetails job = entry.getValue().details();
                JenkinsVo jenkinsVo = parsJobToMap(job);
                list.add(jenkinsVo);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
        return list;
    }

    /**
     * 解析单个job为map
     * @param job
     * @return
     */
    public static JenkinsVo parsJobToMap(JobWithDetails job) throws Exception{
        JenkinsVo vo = new JenkinsVo();
        vo.setName(job.getName());
        String isSussce = "1";
        Long now = (new Date()).getTime();
        try {
            BuildWithDetails buildWithDetails = job.getLastBuild().details();
//            job.getLastBuild().details().getResult();
            if ("FAILURE".equals(buildWithDetails.getResult().toString())){
                vo.setStat(JenkinsConstant.JOB_FAILED);
                vo.setStatStr("构建失败");
                Time last = new Time(now - buildWithDetails.getTimestamp());
                vo.setLastFailed(last.toString());
                vo.setFailedLog(job.getLastFailedBuild().details().getConsoleOutputHtml());
                vo.setLastFailedId(job.getLastFailedBuild().details().getDisplayName());
                if (job.getLastSuccessfulBuild().getNumber() != -1){
                    vo.setLastSucces(new Time(now - job.getLastSuccessfulBuild().details().getTimestamp()).toString());
                    vo.setSussceLog(job.getLastSuccessfulBuild().details().getConsoleOutputHtml());
                    vo.setLastSuccesId(job.getLastSuccessfulBuild().details().getDisplayName());
                }else {
                    vo.setLastSucces("从未成功");
                    vo.setLastSuccesId("");
                    isSussce = "0";
                }
            }else if ("SUCCESS".equals(buildWithDetails.getResult().toString())){
                vo.setStat(JenkinsConstant.JOB_SUSSCE);
                vo.setStatStr("构建成功");
                Time last = new Time(now - buildWithDetails.getTimestamp());
                vo.setLastSucces(last.toString());
                vo.setSussceLog(job.getLastSuccessfulBuild().details().getConsoleOutputHtml());
                vo.setLastSuccesId(job.getLastSuccessfulBuild().details().getDisplayName());
                if (job.getLastFailedBuild().getNumber() != -1){
                    vo.setLastFailed(new Time(now - job.getLastFailedBuild().details().getTimestamp()).toString());
                    vo.setFailedLog(job.getLastFailedBuild().details().getConsoleOutputHtml());
                    vo.setLastFailedId(job.getLastFailedBuild().details().getDisplayName());
                }else{
                    vo.setLastFailed("从未失败");
                    vo.setLastFailedId("");
                }
            }
            DecimalFormat df = new DecimalFormat("#.0");
            vo.setDescribe(job.getDescription());
            vo.setLastTime(df.format(buildWithDetails.getDuration()/1000) + "秒");
            vo.setIsSussce(isSussce);
        } catch (Exception e) {
            throw new Exception("解析流水线详情失败");
        }
        return vo;
    }
}
