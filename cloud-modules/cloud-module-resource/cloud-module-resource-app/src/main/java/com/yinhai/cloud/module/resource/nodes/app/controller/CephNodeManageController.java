package com.yinhai.cloud.module.resource.nodes.app.controller;

import com.yinhai.cloud.core.api.util.DataEncrypt;
import com.yinhai.cloud.core.api.util.UserUtils;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephClusterVo;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.ICephNodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.CephNodeInfoVo;
import com.yinhai.cloud.module.resource.pv.api.bpo.IPersistentVolumeBpo;
import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.cloud.module.resource.task.CephAddTask;
import com.yinhai.cloud.module.resource.task.CephDeleteTask;
import com.yinhai.cloud.module.resource.task.TaskExecuteEngine;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 节点管理
 *
 * @author liuyi02
 */
@Controller
@RequestMapping("/ceph")
public class CephNodeManageController extends BaseController {
    @Resource
    private ICephNodeBpo cephNodeBpo;

    @Resource
    private ICephClusterBpo cephClusterBpo;

    @Resource
    private IPersistentVolumeBpo persistentVolumeBpo;

    @RequestMapping(value = "/addCephNode.do",method = RequestMethod.POST)
    @ResponseBody
    public CommonResultVo addCephNode(@RequestBody CephNodeInfoVo vo, ServletRequest request){
        //判断所有必要属性是否为空
        CommonResultVo result = chenkCephNodeInfoVo(vo);
        if (!result.isSuccess()){
            return result;
        }
        final String userId = UserUtils.getUserId(request);
        //判断集群是否存在mon节点
        List<CephNodeInfoVo> monNodes = cephNodeBpo.getMonNodeInfo(vo.getClusterId());
        if (monNodes.isEmpty() && ServerCmdConstant.CEPH_OSD_NOD_NUM.equals(vo.getNodeType())){
            result.setSuccess(false);
            result.setErrorMsg("没有mon节点不能添加osd节点");
            return result;
        }
        if (monNodes.size() == 1 && monNodes.get(0).getNodeRunningState() != 1){
            result.setSuccess(false);
            result.setErrorMsg("集群第一个mon节点还在初始化！");
            return result;
        }
        vo.setNodeCreateUser(userId);
        vo.setNodeRunningState(NodeState.INIT_RUNNING);
        vo.setNodeDiskToTalMbIdealState(vo.getNodeDiskToTalMb());
        vo.setNodePassword(DataEncrypt.encrypt(vo.getNodePassword()));
        vo.setNodeCreateDate(new Date());
        vo.setCephOperation(NodeState.OPEATE_NONE);
        vo.setNodeDiskToTalMbIdealState(vo.getNodeDiskToTalMb());//初始化理想磁盘大小
        vo.setIsMds(0);
        vo = cephNodeBpo.saveCephNodeInfo(vo);
        if (ValidateUtil.isEmpty(vo)){
            result.setSuccess(false);
            result.setErrorMsg("保存数据库失败");
        }
//        List<CephNodeInfoVo> nodes = cephNodeBpo.getAllCephNode(vo.getClusterId());
        CephClusterVo clusterVo = cephClusterBpo.getClusterById(vo.getClusterId());
        TaskExecuteEngine.execute(new CephAddTask(vo,cephNodeBpo,clusterVo));
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/cephNodeTest.do",method = RequestMethod.POST)
    @ResponseBody
    public CommonResultVo cephNodeTest(@RequestBody CephNodeInfoVo vo){
        CommonResultVo result = chenkCephNodeInfoVo(vo);
        if (!result.isSuccess()){
            return result;
        }
        if (vo.getNodeId() == null) {
            vo.setNodePassword(DataEncrypt.encrypt(vo.getNodePassword()));
        } else {
            CephNodeInfoVo oldNode = cephNodeBpo.queryNodeById(vo.getNodeId());
            if (!oldNode.getNodePassword().equals(vo.getNodePassword())) {
                vo.setNodePassword(DataEncrypt.encrypt(vo.getNodePassword()));
            }
        }
        result = ServerUtils.getCephNodeInfo(vo,true,false);
        if (!result.isSuccess()){
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 查询所有ceph节点
     * @return
     */
    @RequestMapping(value = "/getAllCephNode.do",method = RequestMethod.POST)
    @ResponseBody
    public CommonResultVo getAllCephNode(@RequestBody CephNodeInfoVo vo){
        CommonResultVo resultVo = new CommonResultVo();
        List<CephNodeInfoVo> nodes = cephNodeBpo.getAllCephNode(vo.getClusterId());
        resultVo.setData(nodes);
        resultVo.setSuccess(true);
        return resultVo;
    }

    /**
     * 删除ceph节点并卸载对应节点上安装的ceph
     * @param vo
     * @return
     */
    @RequestMapping(value = "deleteCephNode",method = RequestMethod.POST)
    @ResponseBody
    public CommonResultVo deleteCephNode(@RequestBody CephNodeInfoVo vo){
        CommonResultVo result = checkCephNode(vo);
        if (!result.isSuccess()){
            return result;
        }
        CephNodeInfoVo cvo = cephNodeBpo.queryNodeById(vo.getNodeId());
        if (cvo == null){
            result.setSuccess(false);
            result.setErrorMsg("该节点已经被删除");
            return result;
        }
        int monNodeNum = cephNodeBpo.getMonNodeNum(vo.getClusterId());
        int osdNodeNum = cephNodeBpo.getOsdNodeNum(vo.getClusterId());
        CephClusterVo cephClusterVo = cephClusterBpo.getClusterById(vo.getClusterId());
        PvVo pvo = persistentVolumeBpo.queryByStorageId(vo.getClusterId());
        if (monNodeNum == 1 && ServerCmdConstant.CEPH_MON_NOD_NUM.equals(vo.getNodeType()) && osdNodeNum > 0){
            result.setErrorMsg("删除最后一个mon节点时需要先删除所有osd节点");
            result.setSuccess(false);
            return result;
        }
        if (pvo != null && cephClusterVo.getMinPoolSize() >= osdNodeNum){
            result.setSuccess(false);
            result.setErrorMsg("该集群下挂载有存储，osd节点不可小于集群最小副本数(" + cephClusterVo.getMinPoolSize() + ")");
            return result;
        }
        result = ServerUtils.checkCephTatol(vo);
        if (!result.isSuccess()){
            return result;
        }
        TaskExecuteEngine.execute(new CephDeleteTask(vo,cephNodeBpo,cephClusterBpo));
        //将状态改为删除中
        vo.setNodeRunningState(2);
        vo.setCephOperation(1);
        cephNodeBpo.update(vo);
        result.setSuccess(true);
        return result;
    }

    /**
     * 停止osd节点
     * @param vo
     * @return
     */
    @RequestMapping("stopOsdNode")
    @ResponseBody
    public CommonResultVo stopOsdNode(@RequestBody CephNodeInfoVo vo){
        CephNodeInfoVo newVo = cephNodeBpo.queryNodeById(vo.getNodeId());
        CommonResultVo result = checkCephNode(newVo);
        if (!result.isSuccess()){
            return result;
        }
        if (newVo.getNodeRunningState() == NodeState.RUNNING_SUCCESS){
            try {
                ServerUtils.stopService(newVo);
                newVo.setNodeRunningState(3);
                cephNodeBpo.update(newVo);
                result.setErrorMsg("停止节点服务中");
                result.setSuccess(true);
            } catch (Exception e) {
                result.setSuccess(false);
                result.setErrorMsg(e.getMessage());
            }
        }else {
            try {
                ServerUtils.startService(newVo);
                newVo.setNodeRunningState(NodeState.RUNNING_SUCCESS);
                cephNodeBpo.update(newVo);
                result.setErrorMsg("启动节点服务中");
                result.setSuccess(true);
            } catch (Exception e) {
                result.setSuccess(false);
                result.setErrorMsg(e.getMessage());
            }
        }
        //更新节点信息
        ServerUtils.refreshResources(newVo.getClusterId());
        return result;
    }

    @RequestMapping("nodeToMds")
    @ResponseBody
    public CommonResultVo nodeToMds(@RequestBody CephNodeInfoVo vo){
        CommonResultVo result = new CommonResultVo();
        CephNodeInfoVo nvo = cephNodeBpo.queryNodeById(vo.getNodeId());
        try {
            ServerUtils.setMonToMds(nvo);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        nvo.setIsMds(1);
        cephNodeBpo.update(nvo);
        result.setSuccess(true);
        result.setErrorMsg("创建成功");
        return result;
    }

    private CommonResultVo chenkCephNodeInfoVo(CephNodeInfoVo vo){
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getNodeType())) {
            result.setSuccess(false);
            result.setErrorMsg("请选择节点类型");
            result.setData(vo);
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNodeName())) {
            result.setSuccess(false);
            result.setErrorMsg("请输入节点名称");
            result.setData(vo);
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNodeIP())) {
            result.setSuccess(false);
            result.setErrorMsg("请输入节点IP");
            result.setData(vo);
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNodeSSHPort())) {
            result.setSuccess(false);
            result.setErrorMsg("请输入节点SSH端口");
            result.setData(vo);
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNodeUser())) {
            result.setSuccess(false);
            result.setErrorMsg("请输入节点SSH用户");
            result.setData(vo);
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNodePassword())) {
            result.setSuccess(false);
            result.setErrorMsg("请输入节点SSH密码");
            result.setData(vo);
            return result;
        }
        if (cephNodeBpo.checkNodeInfoRepeat(vo)) {
            result.setSuccess(false);
            result.setErrorMsg("节点 名称/IP 已在使用！");
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    private CommonResultVo checkCephNode(CephNodeInfoVo vo){
        CommonResultVo result = new CommonResultVo();
        if (vo.getNodeRunningState() == 0){
            result.setSuccess(false);
            result.setErrorMsg("当前节点正在初始化");
            return result;
        }
        if (vo.getNodeRunningState() == 2){
            result.setSuccess(false);
            result.setErrorMsg("当前节点正在被删除");
        }
        result.setSuccess(true);
        return result;
    }
}
