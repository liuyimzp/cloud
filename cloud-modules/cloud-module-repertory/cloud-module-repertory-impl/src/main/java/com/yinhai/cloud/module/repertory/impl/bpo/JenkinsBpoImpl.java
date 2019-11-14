package com.yinhai.cloud.module.repertory.impl.bpo;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.yinhai.cloud.module.repertory.api.bpo.IAppGroupBpo;
import com.yinhai.cloud.module.repertory.api.bpo.IJenkinsBpo;
import com.yinhai.cloud.module.repertory.api.util.JenkinsUtils;
import com.yinhai.cloud.module.repertory.api.vo.AppGroupVo;
import com.yinhai.cloud.module.repertory.api.vo.JenkinsVo;
import com.yinhai.cloud.module.repertory.impl.dao.AppGroupDao;
import com.yinhai.cloud.module.repertory.impl.po.AppGroupPo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liuyi on 2018/11/13.
 */
public class JenkinsBpoImpl extends TaBaseBpo implements IJenkinsBpo {

    @Override
    public List<JenkinsVo> getAllJob() throws Exception{
        List<JenkinsVo> list;
        try {
            list = JenkinsUtils.getJobs();
        }catch (IOException e) {
            throw new Exception("获取job失败");
        }
        return list == null?new ArrayList<>():list;
    }
}
