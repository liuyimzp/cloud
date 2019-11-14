package com.yinhai.cloud.module.resource.task;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephClusterVo;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.ICephNodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.CephNodeInfoVo;
import com.yinhai.cloud.module.resource.task.api.AbstractOperateTask;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: liuyi02
 */
public class CephAddTask extends AbstractOperateTask {
    private final static Logger logger = LoggerFactory.getLogger(CephAddTask.class);
    private ICephNodeBpo cephNodeBpo;//进一步业务操作
    private CephNodeInfoVo vo;//节点信息
    private CephClusterVo clusterVo;//集群信息
    private List<CephNodeInfoVo> monNodes,nodes;//集群列表信息

    public CephAddTask(CephNodeInfoVo vo,ICephNodeBpo cephNodeBpo,CephClusterVo clusterVo) {
        this.vo = vo;
        this.cephNodeBpo = cephNodeBpo;
        this.clusterVo = clusterVo;
    }

    @Override
    public boolean needCheck(){
        return false;
    }

    @Override
    public boolean beforeExecute() {
        nodes = cephNodeBpo.getAllCephNode(vo.getClusterId());
        monNodes = cephNodeBpo.getMonNodeInfo(vo.getClusterId()).stream().filter(fvo -> !fvo.getNodeIP().equals(vo.getNodeIP())).collect(Collectors.toList());
        cephNodeBpo.setNodeOperatingState(vo.getNodeId(),1);//设置操作状态为被操作
        return true;
    }

    @Override
    public void executeFailed(Exception e) {
        vo.setNodeRunningState(NodeState.RUNNING_ERROR);
        cephNodeBpo.update(vo);
    }

    /**
     * 删除节点时，先删除集群资源总量 edit by tianhy 2018.11.19
     */
    @Override
    public void successfullyExecuted() {
        vo.setNodeRunningState(NodeState.RUNNING_SUCCESS);
        if (ServerCmdConstant.CEPH_OSD_NOD_NUM.equals(vo.getNodeType())){
            String cName = ServerUtils.getOsdName(vo);
            if (cName != null){
                vo.setNodeCName(cName);
            }
        }else {
            vo.setNodeCName(vo.getNodeName());//如果是mon节点则节点名为最终生成的集群中的名字
        }
        cephNodeBpo.update(vo);
        cephNodeBpo.setNodeOperatingState(vo.getNodeId(),NodeState.OPEATE_NONE);//设置操作状态为未操作
        ServerUtils.refreshResources(vo.getClusterId());
    }

    @Override
    public void onExecute() throws Exception {
        //设置hosts节点名
        ServerUtils.setHosts(vo,nodes,false);
        CommonResultVo result = ServerUtils.noPwdLogin(vo,nodes);
        if (!result.isSuccess()){
            throw new Exception(this.getClass().getName() + "实现节点间无密连接出错");
        }
        //安装ceph
        result = ServerUtils.installCeph(vo);
        if (!result.isSuccess()){
            throw new Exception(this.getClass().getName() + "安装ceph出错");
        }
        //安装对应节点mon或osd
        ServerUtils.deployCluster(vo,monNodes,nodes,clusterVo);
    }
}
