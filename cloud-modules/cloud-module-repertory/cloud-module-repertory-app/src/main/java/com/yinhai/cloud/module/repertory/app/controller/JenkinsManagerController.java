package com.yinhai.cloud.module.repertory.app.controller;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.repertory.api.bpo.IJenkinsBpo;
import com.yinhai.cloud.module.repertory.api.vo.JenkinsVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @Author ：kangdw
 * @Date : 2019/7/25
 */
@Controller
@RequestMapping({"/jenkins"})
public class JenkinsManagerController {

    @Resource
    private IJenkinsBpo jenkinsBpo;

    @RequestMapping("/getJenkinsUrl.do")
    @ResponseBody
    public CommonResultVo getJenkinsUrl() {
        CommonResultVo result = new CommonResultVo();
        result.setData(SystemConfigUtil.getSystemConfigValue("JENKINS.URL"));
        result.setSuccess(true);
        return result;
    }

    /**
     * 获得jenkins所有的流水线
     * @return
     */
    @RequestMapping("/getAllJenkinsJob.do")
    @ResponseBody
    public CommonResultVo getAllJenkinsJob() {
        CommonResultVo result = new CommonResultVo();
        List<JenkinsVo> list;
        try {
            list = jenkinsBpo.getAllJob();
        }catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setData(list);
        result.setSuccess(true);
        return result;
    }
}
