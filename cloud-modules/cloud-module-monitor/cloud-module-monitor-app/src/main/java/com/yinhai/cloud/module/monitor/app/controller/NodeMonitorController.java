package com.yinhai.cloud.module.monitor.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.monitor.api.bpo.INodeMonitorBpo;
import com.yinhai.cloud.module.monitor.api.vo.NodeMonitorVo;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.modules.security.api.vo.UserAccountInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by pengwei on 2018/9/21.
 */
@Controller
@RequestMapping({"/monitor/node"})
public class NodeMonitorController extends BaseController {

    @Resource
    private INodeMonitorBpo nodeMonitorBpo;

    @RequestMapping("/getNodeMonitor.do")
    @ResponseBody
    public CommonResultVo getNodeMonitorInfo(@RequestBody NodeMonitorVo vo, HttpServletRequest request) {
        CommonResultVo resultVo = new CommonResultVo();
        /*final UserAccountInfo userAccountInfo = (UserAccountInfo) request.getSession(false).getAttribute(ResourceConstant.USER_INFO_KEY);
        final String userId = userAccountInfo.getUserId();
        //TODO 保留request，为后期权限控制预留接口参数*/
        try {
            List<NodeMonitorVo> clusterList = nodeMonitorBpo.getNodeMonitorInfoByClusterId(vo.getClusterId());
            resultVo.setSuccess(true);
            resultVo.setData(clusterList);
        } catch (Exception e) {
            resultVo.setSuccess(false);
            resultVo.setErrorMsg("获取集群监控信息出错！");
        }

        return resultVo;
    }

}
