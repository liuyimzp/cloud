package com.yinhai.cloud.module.monitor.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.INamespaceBpo;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.cloud.module.monitor.api.bpo.IClusterMonitorBpo;
import com.yinhai.cloud.module.monitor.api.vo.ClusterMonitorVo;
import com.yinhai.cloud.module.monitor.api.vo.ProxyVo;
import com.yinhai.cloud.module.monitor.util.HttpProxyUtil;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.modules.security.api.vo.UserAccountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengwei on 2018/8/29.
 * 集群监控
 */
@Controller
@RequestMapping({"/monitor/cluster"})
public class ClusterMonitorController extends BaseController {

    @Resource
    private IClusterMonitorBpo clusterMonitorBpo;
    @Resource
    private INodeBpo nodeBpo;

    @Resource
    private INamespaceBpo namespaceBpo;

    private static final Logger logger = LoggerFactory.getLogger(ClusterMonitorController.class);

    @RequestMapping("/getAllClusterMonitorInfo.do")
    @ResponseBody
    public CommonResultVo getClusterMonitorInfo(HttpServletRequest request) {
        CommonResultVo vo = new CommonResultVo();
        final UserAccountInfo userAccountInfo = (UserAccountInfo) request.getSession(false).getAttribute(ResourceConstant.USER_INFO_KEY);
        final String userId = userAccountInfo.getUserId();
        //TODO 保留request，为后期权限控制预留接口参数
        try {
            List<ClusterMonitorVo> clusterList = clusterMonitorBpo.getAllClustersMonitorInfo(userId);
            vo.setSuccess(true);
            vo.setData(clusterList);
        } catch (Exception e) {
            logger.error(logger.getName() + "context", e);
            vo.setSuccess(false);
            vo.setErrorMsg("获取集群监控信息出错！");
        }

        return vo;
    }

    /**
     * 根据集群ID获取当前集群的主节点
     *
     * @param cluster
     * @return
     */
    @RequestMapping("/getMaster.do")
    @ResponseBody
    public CommonResultVo getMasterByCluster(@RequestBody ClusterMonitorVo cluster) {

        CommonResultVo vo = new CommonResultVo();
        Map map = new HashMap<String, Object>();
        List<NodeBasicInfoVo> nodeBasicInfoVos = nodeBpo.querInitDoneMasterNodes(cluster.getClusterId());
        if (!nodeBasicInfoVos.isEmpty()) {
            vo.setSuccess(true);
            map.put("masterNode", nodeBasicInfoVos.get(0));
        }
        List<NamespaceVo> namespaces = namespaceBpo.getNamespaces(cluster.getClusterId());
        map.put("namespace", namespaces);
        vo.setData(map);
        return vo;
    }

    /**
     * 代理前端到prometheus的请求
     *
     * @param proxyVo
     * @return
     */
    @RequestMapping("/monitorProxy.do")
    @ResponseBody
    public CommonResultVo monitorProxy(@RequestBody ProxyVo proxyVo) {
        CommonResultVo vo = new CommonResultVo();
        try {

            String result = HttpProxyUtil.HttpProxyGet(proxyVo);

            vo.setSuccess(true);
            vo.setData(result);
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setErrorMsg("获取监控数据失败，请检查监控是否正常");
            logger.error(e.getMessage());
        }
        return vo;
    }


}
