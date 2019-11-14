package com.yinhai.cloud.module.resource.cluster.app.controller;

import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.core.api.util.UserUtils;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.constants.ClusterState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.task.ClusterDeleteTask;
import com.yinhai.cloud.module.resource.task.TaskExecuteEngine;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.util.List;

;

/**
 * @author zhaokai
 */
@Controller
@RequestMapping("/cluster")
public class ClusterController extends BaseController {

    @Resource
    private IClusterBpo clusterBpo;
    @Resource
    private INodeBpo nodeBpo;


    @RequestMapping("get/all")
    public @ResponseBody
    CommonResultVo getAllClusterBasicInfo() {
        CommonResultVo result = new CommonResultVo();
        result.setSuccess(true);
        result.setData(clusterBpo.queryAllClusterBasicInfo(getTaDto().getUser().getUserid()));
        return result;
    }

    @RequestMapping("get/all/forSelect")
    public @ResponseBody
    CommonResultVo getAllClusterBasicForSelect() {
        CommonResultVo result = new CommonResultVo();
        result.setSuccess(true);
        result.setData(clusterBpo.queryAllNotBeenDeletingCluster());
        return result;
    }


    @RequestMapping("add")
    public @ResponseBody
    CommonResultVo addCluster(@RequestBody ClusterVo clusterAddVo, ServletRequest request) {
        CommonResultVo result = new CommonResultVo();
//                || ValidateUtil.isEmpty(clusterAddVo.getClusterHaVirtualIP())
        if (ValidateUtil.isEmpty(clusterAddVo.getIdentify())
                || ValidateUtil.isEmpty(clusterAddVo.getName())
                ) {
            result.setSuccess(false);
            result.setErrorMsg("集群标识、名称不能为空！");
            return result;
        }
        clusterAddVo.setNumOfNodes(0);
        clusterAddVo.setDeleting(ResourceConstant.CLUSTER_IS_NOT_BEEN_DELETING);
        clusterAddVo.setCreateUserId(UserUtils.getUserId(request));
        clusterAddVo.setCreateDate(CommonUtil.getNow());
        clusterAddVo.setRunningState(ClusterState.CLUSTER_NOT_RUNNING);
        clusterAddVo.setTotalCpu(0);
        clusterAddVo.setTotalCpuAvailable(0.0);
        clusterAddVo.setTotalMemory(0.0);
        clusterAddVo.setTotalMemAvailable(0.0);
        ClusterVo addedVo = clusterBpo.addNewCluster(clusterAddVo);
        if (addedVo == null) {
            result.setSuccess(false);
            result.setErrorMsg("集群标识重复！");
        } else {
            result.setSuccess(true);
            result.setData(addedVo);
        }
        return result;
    }

    @RequestMapping("update")
    public @ResponseBody
    CommonResultVo updateCluster(@RequestBody ClusterVo updateVo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(updateVo.getId())
                || ValidateUtil.isEmpty(updateVo.getName())
                || ValidateUtil.isEmpty(updateVo.getIdentify())
                ) {
            result.setSuccess(false);
            result.setErrorMsg("id,identify,name不能为空！");
            return result;
        }
        ClusterVo oldVo = clusterBpo.getClusterById(updateVo.getId());
        Boolean updateSuccessFlag = clusterBpo.updateClusterById(updateVo);
        result.setSuccess(updateSuccessFlag);
        if (!updateSuccessFlag) {
            result.setErrorMsg("集群标识重复！");
        }
        List<NodeBasicInfoVo> masterList = nodeBpo.queryMasterNodesByClusterId(updateVo.getId());
        if (!masterList.isEmpty() && oldVo.getIstioPort() != updateVo.getIstioPort()){
            try {
                NodeUtils.createIstioIngressGatewayService(masterList.get(0));
            } catch (Exception e) {
                result.setErrorMsg("数据库端口已经修改但是集群端口修改失败" + e.getMessage());
                result.setSuccess(false);
                return result;
            }
        }
        return result;
    }

    @RequestMapping("checkIdentifyRepeat")
    public @ResponseBody
    CommonResultVo checkIdentifyRepeat(@RequestBody ClusterVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getIdentify())
                ) {
            result.setSuccess(false);
            result.setErrorMsg("identify不能为空！");
            return result;
        }

        Boolean repeat = clusterBpo.checkIdentifyRepeat(vo.getIdentify());
        result.setSuccess(true);
        result.setData(repeat);
        return result;
    }

    @RequestMapping("delete")
    public @ResponseBody
    CommonResultVo deleteCluster(@RequestBody ClusterVo clusterVo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(clusterVo.getId())
                ) {

            result.setSuccess(false);
            result.setErrorMsg("id不能为空！");
            return result;
        }
        final ClusterVo queryCluster = clusterBpo.getClusterById(clusterVo.getId());
        if (queryCluster.getDeleting() != null && ResourceConstant.CLUSTER_IS_BEEN_DELETING.equals(queryCluster.getDeleting())) {
            result.setSuccess(false);
            result.setErrorMsg("集群正在被删除！");
            return result;
        }

        List<NodeBasicInfoVo> nodeList = nodeBpo.queryNodesBasicInfoByClusterId(clusterVo.getId());
        boolean hasOperatingNode = nodeList.stream().anyMatch(n -> !NodeUtils.checkNodeIsNotBeenOperating(n).isSuccess());
        if (hasOperatingNode) {
            result.setSuccess(false);
            result.setErrorMsg("集群中有节点正在操作，无法删除！");
            return result;
        }

        TaskExecuteEngine.execute(new ClusterDeleteTask(clusterVo.getId()));
        result.setSuccess(true);
        result.setData(clusterBpo.getClusterById(clusterVo.getId()));
        return result;
    }


    @RequestMapping("refresh/resource")
    public @ResponseBody
    CommonResultVo refreshResource(@RequestBody ClusterVo cluster) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(cluster.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("集群ID不能为空");
            return result;
        }
        clusterBpo.refreshResource(cluster.getId());
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("checkHAIp")
    public @ResponseBody
    CommonResultVo checkHAIp(@RequestBody String json) {
        String ip = com.alibaba.fastjson.JSON.parseObject(json).getString("ip");
        CommonResultVo result = new CommonResultVo();
        result.setSuccess(clusterBpo.checkHAIp(ip));
        return result;
    }
}
