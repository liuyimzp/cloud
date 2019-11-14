package com.yinhai.cloud.module.resource.cluster.app.controller;

import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.core.api.util.UserUtils;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephPoolBpo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephClusterVo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephPoolVo;
import com.yinhai.cloud.module.resource.constants.ClusterState;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.ICephNodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.CephNodeInfoVo;
import com.yinhai.cloud.module.resource.task.CephClusterDeleteTask;
import com.yinhai.cloud.module.resource.task.TaskExecuteEngine;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuyi02
 */
@Controller
@RequestMapping("/cephCluster")
public class CephClusterController extends BaseController {

    @Resource
    private ICephClusterBpo cephClusterBpo;

    @Resource
    private ICephNodeBpo cephNodeBpo;

    @Resource
    private ICephPoolBpo cephPoolBpo;

    @Resource
    private IClusterBpo clusterBpo;

    /**
     * 获取当前登录用户所有集群
     * @return
     */
    @RequestMapping("get/all")
    public @ResponseBody
    CommonResultVo getAllClusterBasicInfo() {
        CommonResultVo result = new CommonResultVo();
        List<Long> clusterIds = clusterBpo.queryAllClusterBasicInfo(getTaDto().getUser().getUserid()).stream().map(cvo -> {
            return cvo.getId();
        }).collect(Collectors.toList());
        result.setData(cephClusterBpo.queryAllClusterBasicInfo().stream().filter(fvo -> clusterIds.contains(fvo.getClusterId())).collect(Collectors.toList()));
        result.setSuccess(true);
        return result;
    }

    /**
     * 获取当前登录用户所有集群
     * @return
     */
    @RequestMapping("get/allDev")
    public @ResponseBody
    CommonResultVo getAllClusterBasicInfoDev(@RequestBody CephClusterVo vo) {
        CommonResultVo result = new CommonResultVo();
        result.setData(cephClusterBpo.queryAllClusterByClusterId(vo.getClusterId()));
        result.setSuccess(true);
        return result;
    }

    /**
     * 添加一个ceph集群
     * @param vo
     * @param request
     * @return
     */
    @RequestMapping("save.do")
    public @ResponseBody
    CommonResultVo saveCluster(@RequestBody CephClusterVo vo, ServletRequest request) {
        //判断必填选项填写完整否
        CommonResultVo result = cephClusterIsEmpty(vo);
        if (!result.isSuccess()){
            return result;
        }
        if (cephClusterBpo.checkCluster(vo.getIdentify())){
            result.setSuccess(false);
            result.setErrorMsg("英文标识重复！");
            return result;
        }
        //将其余字段赋予初始值
        vo.setMemoryAvaiLabel(0.0);
        vo.setMemoryTotal(0.0);
        vo.setNumOfNodes(0);
        vo.setDeleting(0);
        vo.setCreateDate(CommonUtil.getNow());
        vo.setCreateUserId(UserUtils.getUserId(request));
        vo.setRunningState(ClusterState.CLUSTER_NOT_RUNNING);
        CephClusterVo cvo = cephClusterBpo.saveCeph(vo);
        //添加并返回
        if (cvo == null){
            result.setErrorMsg("添加失败，可尝试重新添加！");
            result.setSuccess(false);
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 修改集群信息
     * @param vo
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public CommonResultVo cephClusterUpdate(@RequestBody CephClusterVo vo){
        CommonResultVo result = cephClusterIsEmpty(vo);
        if (!result.isSuccess()){
            return result;
        }
        CephClusterVo old = cephClusterBpo.getClusterById(vo.getId());
        if (old != null && !ValidateUtil.areEqual(old.getIdentify(),vo.getIdentify()) && cephClusterBpo.checkCluster(vo.getIdentify())){
            result.setSuccess(false);
            result.setErrorMsg("英文标识重复！");
            return result;
        }
        cephClusterBpo.updateCluster(vo);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public CommonResultVo cephClusterDelete(@RequestBody CephClusterVo vo){
        CommonResultVo result = new CommonResultVo();
        //判断id是否被传入
        if (ValidateUtil.isEmpty(vo.getId())){
            result.setSuccess(false);
            result.setErrorMsg("删除集群的id不可为空");
            return result;
        }
        //判断集群是否已经正在被删除
        CephClusterVo cvo = cephClusterBpo.getClusterById(vo.getId());
        if (cvo.getDeleting() == ResourceConstant.CLUSTER_IS_BEEN_DELETING){
            result.setErrorMsg("集群正在被删除");
            result.setSuccess(false);
            return result;
        }
        List<CephNodeInfoVo> nodes = cephNodeBpo.getAllCephNode(vo.getId());
        if (nodes.stream().anyMatch(cpo -> cpo.getCephOperation() != NodeState.OPEATE_NONE)){
            result.setSuccess(false);
            result.setErrorMsg("该集群下有节点正在被操作！");
            return result;
        }
        TaskExecuteEngine.execute(new CephClusterDeleteTask(vo.getId()));
        result.setData(cephClusterBpo.getClusterById(vo.getId()));
        result.setSuccess(true);
        return result;
    }

    /**
     * 检查英文标识是否存在
     * @param vo
     * @return
     */
    @RequestMapping("check.do")
    @ResponseBody
    public CommonResultVo checkCephCluster(@RequestBody CephClusterVo vo){
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getIdentify())){
            result.setSuccess(false);
            result.setErrorMsg("英文标识不能为空！");
            return result;
        }
        if (cephClusterBpo.checkCluster(vo.getIdentify())){
            result.setErrorMsg("英文标识已存在");
            result.setSuccess(false);
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("refreshCephInfo.do")
    @ResponseBody
    public CommonResultVo refreshCephInfo(@RequestBody CephClusterVo vo){
        CommonResultVo resultVo = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())){
            resultVo.setSuccess(false);
            resultVo.setErrorMsg("集群id为空");
        }
        ServerUtils.refreshResources(vo.getId());
        resultVo.setSuccess(true);
        return resultVo;
    }

    @RequestMapping("createFsName.do")
    @ResponseBody
    public CommonResultVo createFsName(@RequestBody CephClusterVo vo){
        CommonResultVo result = new CommonResultVo();
        List<CephNodeInfoVo> nodes = cephNodeBpo.getAllMds(vo.getId());
        if (nodes.isEmpty()){
            result.setErrorMsg("该集群还没有mds服务器");
            result.setSuccess(false);
            return result;
        }
        List<CephPoolVo> pools = cephPoolBpo.getAllNotUsePools(vo.getId());
        if (pools.size() < 2){
            result.setErrorMsg("至少需要两个未使用pool");
            result.setSuccess(false);
            return result;
        }
        try {
            ServerUtils.createCephFs(nodes.get(0),pools,vo.getFsName());
            cephClusterBpo.updateCluster(vo);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setErrorMsg("添加成功！");
        result.setSuccess(true);
        return result;
    }

    private CommonResultVo cephClusterIsEmpty(CephClusterVo vo){
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getName()) || ValidateUtil.isEmpty(vo.getIdentify())){
            result.setSuccess(false);
            result.setErrorMsg("应用标识或者集群名不可为空！");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getPoolSize())){
            result.setSuccess(false);
            result.setErrorMsg("请输入默认副本数！");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getMinPoolSize())){
            result.setSuccess(false);
            result.setErrorMsg("请输入最小副本数！");
            return result;
        }
        result.setSuccess(true);
        return result;
    }
}
