package com.yinhai.cloud.module.repertory.api.bpo;

import com.yinhai.cloud.module.repertory.api.vo.JenkinsVo;

import java.util.List;
import java.util.Map;

/**
 * Created by liuyi on 2019/11/13.
 */
public interface IJenkinsBpo {


    //获得所有的流水线详细信息
    List<JenkinsVo> getAllJob() throws Exception;
}
