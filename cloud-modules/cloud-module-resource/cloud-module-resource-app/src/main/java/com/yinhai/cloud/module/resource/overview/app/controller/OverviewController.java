package com.yinhai.cloud.module.resource.overview.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.resource.overview.bpo.IOverviewBpo;
import com.yinhai.cloud.module.resource.overview.vo.OverviewInfoVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

/**
 * Created by pengwei on 2018/9/28.
 */
@Controller
@RequestMapping("/overview")
public class OverviewController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(OverviewController.class);
    @Resource
    private IOverviewBpo overviewBpo;

    @RequestMapping("/showPlatformInfo.do")
    @ResponseBody
    public CommonResultVo showPlatformInfo(ServletRequest request) {
        CommonResultVo resultVo = new CommonResultVo();
        try {
            OverviewInfoVo overviewInfoVo = overviewBpo.getPlatformInfo(1L);
            resultVo.setSuccess(true);
            resultVo.setData(overviewInfoVo);
        } catch (Exception e) {
            logger.error(logger.getName() + "context", e);
        }

        return resultVo;
    }

    @RequestMapping("/getPodsSum.do")
    @ResponseBody
    public CommonResultVo getPodsSum() {
        CommonResultVo resultVo = new CommonResultVo();
        resultVo.setSuccess(true);
        resultVo.setData(overviewBpo.getPodsSum());
        return resultVo;
    }
}
